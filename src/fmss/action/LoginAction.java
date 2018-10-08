package fmss.action;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseOnlineDO;
import fmss.dao.entity.UBaseSysLogDO;
import fmss.common.cache.CacheManager;
import fmss.services.LogManagerService;
import fmss.services.LoginService;
import fmss.services.OnlineService;
import fmss.services.ParamConfigService;
import fmss.services.UBaseSysLogService;
import fmss.services.ldap.LdapAccessByCertificate;
import fmss.services.ldap.LdapAccessByCommon;
import fmss.services.ldap.LdapAccessByWSConnection;
import fmss.services.ldap.LdapAccessConnection;
import fmss.common.util.Constants;
import fmss.common.util.LdapUtil;
import fmss.common.util.LoginUtil;
import fmss.common.util.SecurityPassword;

import com.jes.core.api.servlet.ServletHelper;

import common.crms.util.StringUtil;
/**
 * <p>
 * ��Ȩ����:(C)2003-2010 
 * </p>
 * 
 * @����: xindengquan
 * @����: 2009-6-27 ����10:10:31
 * @����: [LoginAction]�û���¼
 */
public class LoginAction extends BaseAction {

	private static final String SUCC = "1";
	private static final String LOGIN_MSG = "��¼";
	private static final String LOGIN_USER = Constants.LOGIN_USER;
	private static final String FAIL = "0";
	private static final long serialVersionUID = 1L;
	/** ��¼�û���Ϣ */
	private LoginDO user;
	/** ��¼service */
	private LoginService loginService;
	private OnlineService onlineService;
	/** ��־service */
	private UBaseSysLogService sysLogService;

	private LogManagerService logManagerService;
	/** ��Ϣ */
	private String mess;
	/** ���� */
	private CacheManager cacheManager;
	/** ϵͳ���� */
	private ParamConfigService paramConfigService;
	
	private LdapAccessConnection ldapConnectionApi;
	/**
	 * <p>
	 * ��������: isLDAPLogin|����: �ж��Ƿ���ҪLDAP��֤
	 * </p>
	 * 
	 * @param null
	 * @return boolean;
	 */
	private boolean isLdapLoginType() {
		String isLdapType = cacheManager
				.getParemerCacheMapValue(ParamConfigService.PARAM_SYS_ISNEEDLDAP);
		// String isLdapType ="1";
		log.info("current login type is ldap: "+ isLdapType);
		if(user!=null)
			user.setLdapLoginType("1".equals(isLdapType)) ;
		return "1".equals(isLdapType);
	}

	/**
	 * <p>
	 * ��������: isLDAPLoginMethod|����: �ж��Ƿ���ҪLDAP��֤��ʽ false-ֻ��֤�û���;true-��֤�û���������
	 * </p>
	 * 
	 * @param null
	 * @return boolean;
	 */
	// private boolean isLDAPLoginMethod(){
	// return
	// "1".equals(cacheManager.getParemerCacheMapValue(ParamConfigService.PARAM_SYS_METHOD));
	// }
	/**
	 * <p>
	 * ��������: getLdapUtilEntity|����: ��ȡLDAP��Ϣ
	 * </p>
	 * 
	 * @param null
	 * @return LdapUtil;
	 */
	private LdapUtil getLdapUtilEntity() {
		// modify by wangxin 20100329 �޸Ĵӻ����ȡ
		LdapUtil lu = new LdapUtil();
		lu.setBASEDN(cacheManager.getParemerCacheMapValue("PARAM_SYS_ADPATH"));
		lu.setURL(cacheManager.getParemerCacheMapValue("PARAM_SYS_SERVER"));
		lu.setUID(cacheManager.getParemerCacheMapValue("PARAM_SYS_UID"));
		lu.setPWD(cacheManager.getParemerCacheMapValue("PARAM_SYS_PWD"));

		lu.setFilter(cacheManager.getParemerCacheMapValue("PARAM_SYS_FILTER"));
		lu.setDomainName(cacheManager
				.getParemerCacheMapValue("PARAM_SYS_DOMAIN_NAME"));
		return lu;
	}

	public String execute() throws Exception {
		
		try{
			user.setLanguage(SecurityPassword.filterStr(user.getLanguage()));//���������ַ�����ֹע��
 			
 	 		//�����û��Ƿ����url��½
			boolean adminLogin=(StringUtils.isNotEmpty(cacheManager.getParemerCacheMapValue(Constants.PARAM_ADMIN_LOGIN))&&user!=null&&user.isSuperUser(user.getUserId())&&cacheManager.getParemerCacheMapValue(Constants.PARAM_ADMIN_LOGIN).equals(request.getParameter("key")));
			
			//�ж��û��Ƿ��Ǵӵ�½ҳ��½��
			String passInfo=request.getParameter("passInfo");
			if(StringUtils.isNotEmpty(passInfo)&&!adminLogin){
				try{
				    passInfo=new String(common.crms.util.encode.Base64.decode(passInfo));//ϵͳ��ȫ��©���޸�
	 			}catch(Exception e){
	 				this.setMess("e".equals(user.getLanguage())?"Prohibit access to unauthorized users":("�������û���������"));
					return INPUT;
				}
	  			String passWord=null;
				String passId=null;
				String passKey=null;
	 			if(passInfo.split("#@#@#").length>=3){
					    String mid=passInfo.substring(passInfo.indexOf("#@#@#")+5);
						passKey=mid.substring(0,mid.indexOf("#@#@#"));
					//if(passKey.equals(ServletHelper.getCookieValue(request, "USYS_PASSKEY"))){
					if(1==1){
						passId=passInfo.substring(0,passInfo.indexOf("#@#@#"));
					    mid=mid.substring(mid.indexOf("#@#@#")+5);
						passWord=mid;
					}else{
						this.setMess("e".equals(user.getLanguage())?"Prohibit access to unauthorized users":("�������û���������"));
						return INPUT;
					}
				}else{
					this.setMess("e".equals(user.getLanguage())?"Prohibit access to unauthorized users":("�������û���������"));
					return INPUT;
				}
				user.setUserId(passId);
				user.setPassword(passWord);
			}else if(!adminLogin){
				this.setMess((user!=null&&"e".equals(user.getLanguage()))?"Prohibit access to unauthorized users":("�������û���������"));
				return INPUT;
			}
			
			// ȡ������Ϣ
			

			String value = cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_LOGIN_PAGE_TIPS);
			if (StringUtils.isNotEmpty(value)) {
				request.setAttribute("showTips",value);
			}

			LoginDO tempUser;
			UBaseSysLogDO sysLog;
			// ����ڴ�ļ�¼
			tempUser = (LoginDO) request.getSession().getAttribute(LOGIN_USER);
			if (tempUser != null && tempUser.getLoginId() != null
					&& tempUser.getLoginId().length() > 0) {
				onlineService.deleteLogin(tempUser.getLoginId());
				request.getSession().removeAttribute(LOGIN_USER);
			}
			
			
			// �����û��Ƿ����
			LoginDO usdo = null;
			if (user != null && StringUtils.isNotEmpty(user.getUserId())
					&& (StringUtils.isNotEmpty(user.getPassword())||adminLogin)) {

				// ϵͳĬ�ϵĳ����û�admin��������ldap
				if (isLdapLoginType() && !user.isSuperUser(user.getUserId())) {
					boolean pass = false;
					try {
						pass = checkUserByLDAP();
						// user.setUserId(user.getUserId().toUpperCase());// MS LADP
						// �����ִ�Сд ����
						log.info("�û�" + user.getUserId() + " ldap��֤�ɹ�");
						usdo = loginService.checkUserById(user.getUserId());
						if (usdo == null) {
							String msg = "e".equals(user.getLanguage())?"The user is not registered in the system ":("�û�[" + user.getUserId() + "]��ϵͳ��δע��");
							this.setMess(msg);
							log.error(msg);
							pass = false;
						}
					} catch (Exception e) {
						pass = false;
						String msg = "e".equals(user.getLanguage())?"User authentication failed:":("�û�" + user.getUserId() + " ldap��֤ʧ��:")
								+ e.getMessage();
						this.setMess(msg);
						log.error(msg, e);
					}
					if (!pass) {
						return INPUT;
					}
					usdo.setBrowser(request.getHeader("User-Agent"));
					usdo.setIp(ServletHelper.getRemoteAddr(request));
					// ��ldap��¼��ʽ�£������û��޷���¼
					if (StringUtils.isNotEmpty(usdo.getIsUserLocked())) {
						if (usdo.getIsUserLocked().equals(LoginUtil.LOCK_LOCKED)) {
							if (LoginUtil.LOCK_REASON_BY_USER.equals(usdo
									.getUserLockedReson())) {
								this.setMess("e".equals(user.getLanguage())?"Account locked down by the administrator, please contact the administrator unlocking":("�ʻ��ѱ�����Ա����������ϵ����Ա����"));
								usdo.setDescription("��¼ϵͳʧ��:" + "�ʻ��ѱ�����Ա����������ϵ����Ա����");
								sysLog = sysLogService.setUBaseSysLog(usdo,
										LOGIN_MSG, FAIL,
										Constants.BASE_SYS_LOG_AUTHORITY);
								sysLogService.saveUBaseSysLog(sysLog);
								return INPUT;

							} else if (LoginUtil.LOCK_REASON_USER_OVERDUE
									.equals(usdo.getUserLockedReson())) {
								this.setMess(("e".equals(user.getLanguage())?"User not log on  more than ":("�ʻ�����"))
										+ loginService.getUserOverdueDays()
										+ ("e".equals(user.getLanguage())?" days,has expired, please contact the administrator":("��δ��¼����ʧЧ������ϵ����Ա")));
								usdo.setDescription("��¼ϵͳʧ��:" + "�ʻ�����"+ loginService.getUserOverdueDays()+"��δ��¼����ʧЧ������ϵ����Ա");
								sysLog = sysLogService.setUBaseSysLog(usdo,
										LOGIN_MSG, FAIL,
										Constants.BASE_SYS_LOG_AUTHORITY);
								sysLogService.saveUBaseSysLog(sysLog);
								return INPUT;
							}
						}
					}
					
					if ("0".equals(usdo.getEnabled())) {
						this.setMess(("e".equals(user.getLanguage())?"The user has been deactivated, please contact the administrator to enable":("�û��Ѿ���ͣ�ã�����ϵ����Ա����")));
						usdo.setDescription("��¼ϵͳʧ��:" + "�û��Ѿ���ͣ�ã�����ϵ����Ա����");
						sysLog = sysLogService.setUBaseSysLog(usdo, LOGIN_MSG,
								FAIL, Constants.BASE_SYS_LOG_AUTHORITY);
						sysLogService.saveUBaseSysLog(sysLog);
						return INPUT;
					}

				} else {
					if(adminLogin){
						usdo=loginService.checkUserById(user.getUserId());
					}else{
						usdo=loginService.checkUser(user.getUserId(), user
								.getPassword());
					}

					if (usdo != null) {
						usdo.setBrowser(request.getHeader("User-Agent"));
						usdo.setIp(ServletHelper.getRemoteAddr(request));

						if (cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_ISUSE).equals("1")) {
							// �����������
							if (!loginService.checkUserDate(user.getUserId())) {
								
								this.setMess(("e".equals(user.getLanguage())?"The account is not enabled for the period.":("�˻��ڸ�ʱ�����δ�����á�")));
								usdo.setDescription("��¼ϵͳʧ��:" + "�˻��ڸ�ʱ�����δ�����á�");
								sysLog = sysLogService.setUBaseSysLog(usdo,
										LOGIN_MSG, FAIL,
										Constants.BASE_SYS_LOG_AUTHORITY);
								sysLogService.saveUBaseSysLog(sysLog);
								return INPUT;
							}
							// isUserLocked: 1:���������������;2:�������;3:�������룬ʧЧ;
							if (StringUtils.isNotEmpty(usdo.getIsUserLocked())) {

								// �����������
								if (usdo.getIsUserLocked().equals(
										LoginUtil.LOCK_LOCKED)) {
									if (LoginUtil.LOCK_REASON_PWD_INVALID_COUNTS
											.equals(usdo.getUserLockedReson())) {
										// ���������������
										if (usdo.getWrongPwdDate() != null
												&& LoginUtil.compareDate(
														new Date(), usdo
																.getWrongPwdDate(),
														LoginUtil.SHORT_FORMAT)) {
											
											this.setMess(("e".equals(user.getLanguage())?"More than ":("������������������"))
													+ loginService
															.getPWDInvalidCounts()
													+ ("e".equals(user.getLanguage())?" input the wrong password, account is locked, can not access the system today":("�Σ��ʻ��ѱ����������ղ����ٷ���ϵͳ")));
											usdo.setDescription("��¼ϵͳʧ��:" +"������������������"+loginService.getPWDInvalidCounts()+"�Σ��ʻ��ѱ����������ղ����ٷ���ϵͳ");
											sysLog = sysLogService
													.setUBaseSysLog(
															usdo,
															LOGIN_MSG,
															FAIL,
															Constants.BASE_SYS_LOG_AUTHORITY);
											sysLogService.saveUBaseSysLog(sysLog);
											return INPUT;
										}

									} else if (LoginUtil.LOCK_REASON_BY_USER
											.equals(usdo.getUserLockedReson())) {
										this.setMess(("e".equals(user.getLanguage())?"Account locked down by the administrator, please contact the administrator unlocking":("�ʻ��ѱ�����Ա����������ϵ����Ա����")));
										usdo.setDescription("��¼ϵͳʧ��:" + "�ʻ��ѱ�����Ա����������ϵ����Ա����");
										sysLog = sysLogService.setUBaseSysLog(usdo,
												LOGIN_MSG, FAIL,
												Constants.BASE_SYS_LOG_AUTHORITY);
										sysLogService.saveUBaseSysLog(sysLog);
										return INPUT;

									} else if (LoginUtil.LOCK_REASON_USER_OVERDUE
											.equals(usdo.getUserLockedReson())) {// ʧЧ
										this.setMess(("e".equals(user.getLanguage())?"User not log on  more than ":("�ʻ�����"))
												+ loginService.getUserOverdueDays()
												+ ("e".equals(user.getLanguage())?" days,has expired, please contact the administrator":("��δ��¼����ʧЧ������ϵ����Ա")));
										usdo.setDescription("��¼ϵͳʧ��:" + "�ʻ�����"+loginService.getUserOverdueDays()+"��δ��¼����ʧЧ������ϵ����Ա");
										sysLog = sysLogService.setUBaseSysLog(usdo,
												LOGIN_MSG, FAIL,
												Constants.BASE_SYS_LOG_AUTHORITY);
										sysLogService.saveUBaseSysLog(sysLog);
										return INPUT;
									}
								}
							}
							//������ڣ�ǿ���û��޸����빦������ͨ�á�
							/*boolean b = loginService.isOverdue(usdo); // �Ƿ����
							if (b) {
								this.setMess("�����ѹ��ڣ�����ϵ����Ա��������");
								usdo.setDescription("��¼ϵͳʧ��:" + this.getMess());
								sysLog = sysLogService.setUBaseSysLog(usdo,
										LOGIN_MSG, FAIL,
										Constants.BASE_SYS_LOG_AUTHORITY);
								sysLogService.saveUBaseSysLog(sysLog);
								return INPUT;
							}*/

							// �û��Ƿ�����
							// this.setMess("�ʻ��ѱ�����������ϵ����Ա������");
							loginService.comeback(usdo.getUserId());
						}
						if ("0".equals(usdo.getEnabled())) {
							this.setMess(("e".equals(user.getLanguage())?"The user has been deactivated, please contact the administrator to enable":("�û��Ѿ���ͣ�ã�����ϵ����Ա����")));
							usdo.setDescription("��¼ϵͳʧ��:" + "�û��Ѿ���ͣ�ã�����ϵ����Ա����");
							sysLog = sysLogService.setUBaseSysLog(usdo, LOGIN_MSG,
									FAIL, Constants.BASE_SYS_LOG_AUTHORITY);
							sysLogService.saveUBaseSysLog(sysLog);
							return INPUT;
						}
					}
				}
			} else {
				this.setMess(("e".equals(user.getLanguage())?"Login failed, illegal parameter":("��¼ʧ�ܣ��Ƿ�����")));
				return INPUT;
			}
			if (usdo == null) {
				usdo=loginService.checkUserById(user.getUserId());
	 			if(usdo==null){
					usdo = new LoginDO();
					usdo.setUserId(user.getUserId());
					usdo.setUserCname(user.getUserId());
					usdo.setUserEname(user.getUserId());
					usdo.setInstCname(user.getInstCname());
	 			}
				 
				usdo.setBrowser(request.getHeader("User-Agent"));
				usdo.setIp(ServletHelper.getRemoteAddr(request));
				
				
				
				usdo.setDescription("��¼ϵͳʧ��:�û������������");
				
				// ���ݿ��в����ڵ�¼�û����Ͷ�Ӧ������
				if (cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_ISUSE).equals("1")) {
					try {
						int m = loginService.checkUser(user.getUserId()); // �������������
						if (m < 0)
							this.setMess(("e".equals(user.getLanguage())?"Error ":("���������������")) + (-m) + ("e".equals(user.getLanguage())?"consecutive input password, account is locked":("�Σ��ʻ��ѱ�����")));
						else {
							this.setMess(("e".equals(user.getLanguage())?"The user name or password is incorrect, please enter again.":("�û����������������������")));
						}
					} catch (Exception e) {
						log.error("login write system log failed ", e);
					}
				} else {
					this.setMess(("e".equals(user.getLanguage())?"The user name or password is incorrect, please enter again.":("�û����������������������")));
				}

				try {
					sysLog = sysLogService.setUBaseSysLog(usdo, LOGIN_MSG, FAIL,
							Constants.BASE_SYS_LOG_AUTHORITY);
					sysLogService.saveUBaseSysLog(sysLog);
				} catch (Exception e) {
					// log insert fail ,can't effect login. by xieli
					log.error("login write system log failed ", e);
				}

				return INPUT;
			} else {

				boolean isNotAllowSameUserLogin = "1"
						.equals(cacheManager
								.getParemerCacheMapValue(Constants.PARAM_SYS_ALLOW_SAMEUSER_LOGIN));
				if (isNotAllowSameUserLogin) {
					if (onlineService.hasLoginByUserIdAndAddr(user.getUserId(),ServletHelper.getRemoteAddr(request))) {
						log.warn("�Ѵ�����ͬ���û���¼" + user.getUserId());
						sysLog = sysLogService.setUBaseSysLog(usdo, LOGIN_MSG,
								FAIL, Constants.BASE_SYS_LOG_AUTHORITY);
						sysLog.setDescription("��¼ʧ�ܣ��Ѵ�����ͬ���û���¼");
						sysLogService.saveUBaseSysLog(sysLog);
						this.setMess(("e".equals(user.getLanguage())?"The user has logged in, please don't login again":("���û��ѵ�¼�������ظ���¼")));
						return INPUT;
					}
				}
				String param = cacheManager.getParemerCacheMapValue("PARAM_SYS_MAX_PERSON");
				int maxValue=0;
				if(StringUtils.isNotEmpty(param)){
					maxValue=Integer.parseInt(param);
				}
				if(maxValue>0){
					if(!"1".equals(usdo.getIsList())){
						if(onlineService.countUserNotList()>=maxValue){
							this.setMess(("e".equals(user.getLanguage())?"Has reached the maximum number online, please later landing":("�Ѿ��ﵽ���ͬʱ�������������Ժ��½")));
							return INPUT;
						}
					}
				}
				
				// ��ȡ�û���Ϣ
				usdo.setLanguage(user.getLanguage());
				usdo.setBrowser(request.getHeader("User-Agent"));
				usdo.setDescription("��¼FMSSϵͳ");
				usdo.setIp(ServletHelper.getRemoteAddr(request));
				String newLoginId = loginService.getNewLoginId(usdo.getUserId(),usdo.getIp());
	 			synchronized(this){
					if ("0".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_LOGIN_IP_VALIDATE))) {
						// ��ϵͳ������֤ip
		 					newLoginId = String.valueOf(usdo.getUserId() + "-" + System.currentTimeMillis());
		 			}
					String s=onlineService.checkKickout(user.getUserId(), usdo.getIp());
					if(StringUtils.isNotEmpty(s)){
						if(s.indexOf("ERROR_2")==0)
							this.setMess(("e".equals(user.getLanguage())?"Sorry, you have been kicked out of the system. from the next landing with ### seconds":("�Բ���,���Ѿ����߳�ϵͳ.���´ε�½ʱ�仹ʣ###��")).replaceAll("###", s.substring("ERROR_2".length()+1)));
						else if(s.indexOf("ERROR_3")==0){
							this.setMess(("e".equals(user.getLanguage())?"Sorry, you have been kicked out of the system.":("�Բ���,���Ѿ����߳�ϵͳ.")));
						}
						return INPUT;
					}
					newLoginId=onlineService.registerUser(new UBaseOnlineDO(newLoginId,usdo.getUserId(),usdo.getIp()));
					
	 				UsysSSOFilter.setUserId(response, usdo.getUserId());
					UsysSSOFilter.setParam(response,request,UsysSSOFilter.SSO_SYSTEM_ID+"_"+request.getRequestURI().substring(1).substring(0,request.getRequestURI().substring(1).indexOf("/")), "00003");
					UsysSSOFilter.setParam(response,request, "USYS_LOGIN_ID", newLoginId);
					UsysSSOFilter.setParam(response,request, "USYS_USER_LANGUAGE", user.getLanguage());
					UsysSSOFilter.setParam(response,request, "USYS_ISLDAPTYPE", String.valueOf(usdo.getLdapLoginType()));
					request.getSession().setAttribute(UsysSSOFilter.SSO_SESSION_USER_ID, usdo.getUserId());
					
					usdo.setLoginId(newLoginId);
		
					usdo.setLdapLoginType(user.getLdapLoginType()) ;
	 			}
				loginService.updateLastLoginDate(usdo.getUserId());
				// ע���û���Ϣ

				request.getSession().setAttribute(LOGIN_USER, usdo);
				
				
				
				// ��¼����
				try {
					sysLog = sysLogService.setUBaseSysLog(usdo, LOGIN_MSG, SUCC,
							Constants.BASE_SYS_LOG_AUTHORITY);
					sysLogService.saveUBaseSysLog(sysLog);
				} catch (Exception e) {
					// log insert fail ,can't effect login. by xieli
					log.error("login write system log failed ", e);
				}
				/* logManagerService.writeLog(usdo, "00001", "1"); */
				

				value = cacheManager.getParemerCacheMapValue(Constants.PARAM_THEME_PATH_CONFIG);
				if (StringUtils.isNotEmpty(value)) {
					request.getSession().setAttribute(Constants.SESSION_THEME_KEY,value);
				}
				
				
				
				setUseSessionUrlInfo(request.getRequestURL().toString());

				
				request.getSession().getServletContext().setAttribute(
						Constants.SESSION_MENU_LANGUAGE,
						user.getLanguage());
				return SUCCESS;
			}
			
		}catch(Exception e){//��ȫ©���У���ϣ���׳��쳣��ҳ����Ϣ�ϡ�
			this.setMess("�������û���������");
			log.error("��¼�쳣��", e);
			return INPUT;
		}
 	}

	// �Ƿ������߼�ɾ���û�
	private boolean isUserDeleteLogic() {
		String isDeleteLogic = cacheManager
				.getParemerCacheMapValue(Constants.PARAM_SYS_USER_DELETE_LOGIC);
		return StringUtil.isNotEmpty(isDeleteLogic)
				&& "1".equals(isDeleteLogic) ? true : false;
	}

	private boolean isLdapLoginTypeByCommon() {
		return "3".equals(cacheManager
				.getParemerCacheMapValue(ParamConfigService.PARAM_SYS_METHOD));
	}

	// LDAP��֤
	private boolean checkUserByLDAP() throws Exception {
		LdapAccessConnection conn = null;
		if (isLdapLoginTypeByCommon()) {
			System.out.println("checkUser by MS AD ");
			conn = LdapAccessByCommon.getConnection();
		}
		if (isLdapLoginTypeByWebservice()) {
			conn = LdapAccessByWSConnection.getConnection();
		}
		if (isLdapLoginTypeByCertificate()) {
			conn = LdapAccessByCertificate.getConnection();
		}
		//ʹ��ע�뷽ʽ������api�࣬������Ҫʵ��LdapAccessConnection��
		if (isLdapLoginTypeByApi()) {
			conn = this.ldapConnectionApi;
		}
		if (conn == null) {
			throw new RuntimeException(
					"unsupported authorize type, authorized  fail");
		}
		String msg = conn.auth(user.getUserId(), user.getPassword());
		if (LdapAccessConnection.isAuthorizeSuccess(msg)) {
			if (loginService.checkUserExit(user.getUserId()) != null)
				return true;
			else {
				msg = "���û�[" + user.getUserId() + "]��ϵͳ��δע��";
				log.warn(msg + ":" + user.getUserId());
				throw new Exception(msg);
			}
		} else {
			throw new Exception(msg);
		}

	}

	/**
	 * <p>
	 * ��������: getUser|����: ����û�
	 * </p>
	 * 
	 * @return �����û�����
	 */
	public LoginDO getUser() {
		return user;
	}

	/**
	 * <p>
	 * ��������: setUser|����: �����û�
	 * </p>
	 * 
	 * @param user
	 */
	public void setUser(LoginDO user) {
		this.user = user;
	}

	/**
	 * <p>
	 * ��������: getLoginService|����: ���LoginService����
	 * </p>
	 * 
	 * @return ����LoginService����
	 */
	public LoginService getLoginService() {
		return loginService;
	}

	/**
	 * <p>
	 * ��������: setLoginService|����: ����LoginService����
	 * </p>
	 * 
	 * @param loginService
	 */
	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	/**
	 * <p>
	 * ��������: getMess|����: �����Ϣ
	 * </p>
	 * 
	 * @return
	 */
	public String getMess() {
		return mess;
	}

	/**
	 * <p>
	 * ��������: setMess|����: ������Ϣ
	 * </p>
	 * 
	 * @param mess
	 */
	public void setMess(String mess) {
		this.mess = mess;
	}

	/**
	 * <p>
	 * ��������: getSysLogService|����: ���UBaseSysLogService
	 * </p>
	 * 
	 * @return
	 */
	public UBaseSysLogService getSysLogService() {
		return sysLogService;
	}

	/**
	 * <p>
	 * ��������: setSysLogService|����: ����UBaseSysLogService
	 * </p>
	 * 
	 * @param sysLogService
	 */
	public void setSysLogService(UBaseSysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}

	/**
	 * @param cacheManager
	 *            Ҫ���õ� cacheManager
	 */
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	/**
	 * @param paramConfigService
	 *            Ҫ���õ� paramConfigService
	 */
	public void setParamConfigService(ParamConfigService paramConfigService) {
		this.paramConfigService = paramConfigService;
	}

	public void setLogManagerService(LogManagerService logManagerService) {
		this.logManagerService = logManagerService;
	}

	private boolean isLdapLoginTypeByWebservice() {
		return "0".equals(cacheManager
				.getParemerCacheMapValue(ParamConfigService.PARAM_SYS_METHOD));
	}

	private boolean isLdapLoginTypeByApi() {
		return "1".equals(cacheManager
				.getParemerCacheMapValue(ParamConfigService.PARAM_SYS_METHOD));
	}

	private boolean isLdapLoginTypeByCertificate() {
		return "2".equals(cacheManager
				.getParemerCacheMapValue(ParamConfigService.PARAM_SYS_METHOD));
	}

	public void setUseSessionUrlInfo(String sUrl) {
		try {
			String[] splitList = sUrl.split(":");
			String defaultIPAddress;
			String defaultPort;

			if (splitList.length > 2) {
				int lastIndex = sUrl.lastIndexOf(":");
				String sDefaultPort = sUrl.substring(lastIndex + 1, sUrl
						.length());
				String sDefaultIPAddress = sUrl.substring(0, lastIndex);

				defaultIPAddress = sDefaultIPAddress
						.substring(sDefaultIPAddress.lastIndexOf("/") + 1);
				defaultPort = sDefaultPort.substring(0, sDefaultPort
						.indexOf("/"));
				log.info("-----------Has port!");
			} else {
				String httpTop = sUrl.substring(0, sUrl.indexOf(":"));
				if ("https".equals(httpTop.toLowerCase())) {
					defaultPort = "443";
				} else {
					defaultPort = "80";
				}

				defaultIPAddress = sUrl.substring(sUrl.indexOf(":") + 3);
				defaultIPAddress = defaultIPAddress.substring(0,
						defaultIPAddress.indexOf("/"));
				log.info("-----------With out port!");
			}
			log.info("-----------loginUrl:" + sUrl);
			log.info("-----------defaultPort:" + defaultPort);
			log.info("-----------defaultIPAddress:" + defaultIPAddress);

			request.getSession().setAttribute("defaultIPAddress",
					defaultIPAddress);
			request.getSession().setAttribute("defaultPort",defaultPort);
			
			String usysInnerUrl=CacheManager.getParemerCacheMapValue(Constants.PARAM_USYS_INNER_URL);
			usysInnerUrl=BaseAction.getURL(usysInnerUrl,defaultIPAddress,defaultPort).replaceAll(" ", "").replaceAll(":80/", "/");
			

			
			UsysSSOFilter.setParam(response,request,UsysSSOFilter.SSO_COOKIE_PARENT_INNER_URL,usysInnerUrl);
			UsysSSOFilter.setParam(response,request,UsysSSOFilter.SSO_COOKIE_PARENT_URL, request.getRequestURL().substring(0, 1 + request.getRequestURL().lastIndexOf("/")));

			if(request.getRequestURL().indexOf(usysInnerUrl)!=-1){
				request.getSession().setAttribute("isInnerNet","true");
			}else{
				request.getSession().setAttribute("isInnerNet","false");
			}
			UsysSSOFilter.setParam(response, request,"isInnerNet","true");
		} catch (Exception e) {
			log.error("setUseSessionUrlInfo:"+sUrl+" ��������", e);
		}
	}

	public void setOnlineService(OnlineService onlineService) {
		this.onlineService = onlineService;
	}

	public LdapAccessConnection getLdapConnectionApi() {
		return ldapConnectionApi;
	}

	public void setLdapConnectionApi(LdapAccessConnection ldapConnectionApi) {
		this.ldapConnectionApi = ldapConnectionApi;
	}
}
