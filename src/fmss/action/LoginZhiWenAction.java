package fmss.action;

import org.apache.commons.lang.StringUtils;

import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseOnlineDO;
import fmss.dao.entity.UBaseSysLogDO;
import fmss.common.cache.CacheManager;
import fmss.services.LoginService;
import fmss.services.OnlineService;
import fmss.services.ParamConfigService;
import fmss.services.UBaseSysLogService;
import fmss.common.util.Constants;

import com.jes.core.api.servlet.ServletHelper;
 
public class LoginZhiWenAction extends BaseAction {

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

 	/** ��Ϣ */
	private String mess;
	/** ���� */
	private CacheManager cacheManager;
	/** ϵͳ���� */ 

	public String execute() throws Exception {
		UBaseSysLogDO sysLog;
  		log.info("ָ�Ƶ�½���...");
		String userId = request.getParameter("userId");
 		log.info("ָ����֤�û�="+userId);
		//���¶���ҵ���߼�
		if(StringUtils.isNotEmpty(userId)){ 
			// ȡ������Ϣ 
			String value = cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_LOGIN_PAGE_TIPS);
			if (StringUtils.isNotEmpty(value)) {
				request.setAttribute("showTips",value);
			}
	
			// ����ڴ�ļ�¼
			LoginDO tempUser = (LoginDO) request.getSession().getAttribute(LOGIN_USER);
			if (tempUser != null && tempUser.getLoginId() != null&& tempUser.getLoginId().length() > 0) {
				onlineService.deleteLogin(tempUser.getLoginId());
				request.getSession().removeAttribute(LOGIN_USER);
			}
			
			// �����û��Ƿ���� 
			LoginDO usdo=loginService.checkUserById(userId);
			if(usdo==null){ 
				this.setMess("�û�:��"+userId+"��������!");
			    return INPUT;
			}else{
				usdo.setBrowser(request.getHeader("User-Agent"));
				usdo.setIp(ServletHelper.getRemoteAddr(request));
					if ("0".equals(usdo.getEnabled())) {
							this.setMess("�û��Ѿ���ͣ�ã�����ϵ����Ա����");
							usdo.setDescription("��¼FMSSϵͳʧ��:" + "�û��Ѿ���ͣ�ã�����ϵ����Ա����");
							sysLog = sysLogService.setUBaseSysLog(usdo, LOGIN_MSG,
									FAIL, Constants.BASE_SYS_LOG_AUTHORITY);
							sysLogService.saveUBaseSysLog(sysLog);
							return INPUT;
				    }
	
		        boolean isNotAllowSameUserLogin = "1".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_ALLOW_SAMEUSER_LOGIN));
				if (isNotAllowSameUserLogin) {
					if (onlineService.hasLoginByUserIdAndAddr(userId,ServletHelper.getRemoteAddr(request))) {
						log.warn("�Ѵ�����ͬ���û���¼" + userId);
						sysLog = sysLogService.setUBaseSysLog(usdo, LOGIN_MSG,
								FAIL, Constants.BASE_SYS_LOG_AUTHORITY);
						sysLog.setDescription("��¼ʧ�ܣ��Ѵ�����ͬ���û���¼");
						sysLogService.saveUBaseSysLog(sysLog);
						this.setMess("���û��ѵ�¼�������ظ���¼");
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
							this.setMess("�Ѿ��ﵽ���ͬʱ�������������Ժ��½");
							return INPUT;
						}
					}
				}
				
				// ��ȡ�û���Ϣ
				usdo.setLanguage("c");
				usdo.setBrowser(request.getHeader("User-Agent"));
				usdo.setDescription("��¼FMSSϵͳ");
				usdo.setIp(ServletHelper.getRemoteAddr(request));
				String newLoginId = loginService.getNewLoginId(usdo.getUserId(),usdo.getIp());
				synchronized(this){
					if ("0" .equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_LOGIN_IP_VALIDATE))) {
						// ��ϵͳ������֤ip
						newLoginId = String.valueOf(usdo.getUserId() + "-"+ System.currentTimeMillis());
					}
					
					String s=onlineService.checkKickout(userId, usdo.getIp());
					if(StringUtils.isNotEmpty(s)){
						if(s.indexOf("ERROR_2")==0)
							this.setMess(("�Բ���,���Ѿ����߳�ϵͳ.���´ε�½ʱ�仹ʣ###��").replaceAll("###", s.substring("ERROR_2".length()+1)));
						else if(s.indexOf("ERROR_3")==0){
							this.setMess("�Բ���,���Ѿ����߳�ϵͳ.");
						}
						return INPUT;
					}
					
					newLoginId=onlineService.registerUser(new UBaseOnlineDO(newLoginId,usdo.getUserId(),usdo.getIp()));
		
					UsysSSOFilter.setUserId(response, usdo.getUserId());
					UsysSSOFilter.setParam(response,request,UsysSSOFilter.SSO_SYSTEM_ID+"_"+request.getRequestURI().substring(1).substring(0,request.getRequestURI().substring(1).indexOf("/")), "00003");
					UsysSSOFilter.setParam(response,request, "USYS_LOGIN_ID", newLoginId);
					UsysSSOFilter.setParam(response,request, "USYS_USER_LANGUAGE", "c");
					UsysSSOFilter.setParam(response,request, "USYS_ISLDAPTYPE", String.valueOf(usdo.getLdapLoginType()));
					request.getSession().setAttribute(UsysSSOFilter.SSO_SESSION_USER_ID, usdo.getUserId());
					
					usdo.setLoginId(newLoginId);
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
	 				log.error("login write system log failed ", e);
				} 
	 			value = cacheManager.getParemerCacheMapValue(Constants.PARAM_THEME_PATH_CONFIG);
				if (StringUtils.isNotEmpty(value)) {
					request.getSession().setAttribute(Constants.SESSION_THEME_KEY,value);
				}
	 			setUseSessionUrlInfo(request.getRequestURL().toString());
	  			request.getSession().getServletContext().setAttribute( Constants.SESSION_MENU_LANGUAGE, "c");
	  			log.info("�û���"+userId+"����¼�ɹ�");
	  			return SUCCESS;
			   }
		  }else{ 
				log.info("ָ����֤����ȡ�û�IDʧ��!");
				this.setMess("ָ����֤����ȡ�û�IDʧ��");
				return INPUT;
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
}
