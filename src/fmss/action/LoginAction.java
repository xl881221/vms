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
 * 版权所有:(C)2003-2010 
 * </p>
 * 
 * @作者: xindengquan
 * @日期: 2009-6-27 上午10:10:31
 * @描述: [LoginAction]用户登录
 */
public class LoginAction extends BaseAction {

	private static final String SUCC = "1";
	private static final String LOGIN_MSG = "登录";
	private static final String LOGIN_USER = Constants.LOGIN_USER;
	private static final String FAIL = "0";
	private static final long serialVersionUID = 1L;
	/** 登录用户信息 */
	private LoginDO user;
	/** 登录service */
	private LoginService loginService;
	private OnlineService onlineService;
	/** 日志service */
	private UBaseSysLogService sysLogService;

	private LogManagerService logManagerService;
	/** 消息 */
	private String mess;
	/** 缓存 */
	private CacheManager cacheManager;
	/** 系统配置 */
	private ParamConfigService paramConfigService;
	
	private LdapAccessConnection ldapConnectionApi;
	/**
	 * <p>
	 * 方法名称: isLDAPLogin|描述: 判断是否需要LDAP验证
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
	 * 方法名称: isLDAPLoginMethod|描述: 判断是否需要LDAP验证方式 false-只验证用户名;true-验证用户名和密码
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
	 * 方法名称: getLdapUtilEntity|描述: 获取LDAP信息
	 * </p>
	 * 
	 * @param null
	 * @return LdapUtil;
	 */
	private LdapUtil getLdapUtilEntity() {
		// modify by wangxin 20100329 修改从缓存获取
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
			user.setLanguage(SecurityPassword.filterStr(user.getLanguage()));//过滤敏感字符，防止注入
 			
 	 		//超级用户是否可以url登陆
			boolean adminLogin=(StringUtils.isNotEmpty(cacheManager.getParemerCacheMapValue(Constants.PARAM_ADMIN_LOGIN))&&user!=null&&user.isSuperUser(user.getUserId())&&cacheManager.getParemerCacheMapValue(Constants.PARAM_ADMIN_LOGIN).equals(request.getParameter("key")));
			
			//判断用户是否是从登陆页登陆的
			String passInfo=request.getParameter("passInfo");
			if(StringUtils.isNotEmpty(passInfo)&&!adminLogin){
				try{
				    passInfo=new String(common.crms.util.encode.Base64.decode(passInfo));//系统安全性漏洞修改
	 			}catch(Exception e){
	 				this.setMess("e".equals(user.getLanguage())?"Prohibit access to unauthorized users":("请输入用户名和密码"));
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
						this.setMess("e".equals(user.getLanguage())?"Prohibit access to unauthorized users":("请输入用户名和密码"));
						return INPUT;
					}
				}else{
					this.setMess("e".equals(user.getLanguage())?"Prohibit access to unauthorized users":("请输入用户名和密码"));
					return INPUT;
				}
				user.setUserId(passId);
				user.setPassword(passWord);
			}else if(!adminLogin){
				this.setMess((user!=null&&"e".equals(user.getLanguage()))?"Prohibit access to unauthorized users":("请输入用户名和密码"));
				return INPUT;
			}
			
			// 取参数信息
			

			String value = cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_LOGIN_PAGE_TIPS);
			if (StringUtils.isNotEmpty(value)) {
				request.setAttribute("showTips",value);
			}

			LoginDO tempUser;
			UBaseSysLogDO sysLog;
			// 清空内存的记录
			tempUser = (LoginDO) request.getSession().getAttribute(LOGIN_USER);
			if (tempUser != null && tempUser.getLoginId() != null
					&& tempUser.getLoginId().length() > 0) {
				onlineService.deleteLogin(tempUser.getLoginId());
				request.getSession().removeAttribute(LOGIN_USER);
			}
			
			
			// 检验用户是否存在
			LoginDO usdo = null;
			if (user != null && StringUtils.isNotEmpty(user.getUserId())
					&& (StringUtils.isNotEmpty(user.getPassword())||adminLogin)) {

				// 系统默认的超级用户admin将不会走ldap
				if (isLdapLoginType() && !user.isSuperUser(user.getUserId())) {
					boolean pass = false;
					try {
						pass = checkUserByLDAP();
						// user.setUserId(user.getUserId().toUpperCase());// MS LADP
						// 不区分大小写 密码
						log.info("用户" + user.getUserId() + " ldap认证成功");
						usdo = loginService.checkUserById(user.getUserId());
						if (usdo == null) {
							String msg = "e".equals(user.getLanguage())?"The user is not registered in the system ":("用户[" + user.getUserId() + "]在系统中未注册");
							this.setMess(msg);
							log.error(msg);
							pass = false;
						}
					} catch (Exception e) {
						pass = false;
						String msg = "e".equals(user.getLanguage())?"User authentication failed:":("用户" + user.getUserId() + " ldap验证失败:")
								+ e.getMessage();
						this.setMess(msg);
						log.error(msg, e);
					}
					if (!pass) {
						return INPUT;
					}
					usdo.setBrowser(request.getHeader("User-Agent"));
					usdo.setIp(ServletHelper.getRemoteAddr(request));
					// 在ldap登录方式下，锁定用户无法登录
					if (StringUtils.isNotEmpty(usdo.getIsUserLocked())) {
						if (usdo.getIsUserLocked().equals(LoginUtil.LOCK_LOCKED)) {
							if (LoginUtil.LOCK_REASON_BY_USER.equals(usdo
									.getUserLockedReson())) {
								this.setMess("e".equals(user.getLanguage())?"Account locked down by the administrator, please contact the administrator unlocking":("帐户已被管理员锁定，请联系管理员解锁"));
								usdo.setDescription("登录系统失败:" + "帐户已被管理员锁定，请联系管理员解锁");
								sysLog = sysLogService.setUBaseSysLog(usdo,
										LOGIN_MSG, FAIL,
										Constants.BASE_SYS_LOG_AUTHORITY);
								sysLogService.saveUBaseSysLog(sysLog);
								return INPUT;

							} else if (LoginUtil.LOCK_REASON_USER_OVERDUE
									.equals(usdo.getUserLockedReson())) {
								this.setMess(("e".equals(user.getLanguage())?"User not log on  more than ":("帐户超过"))
										+ loginService.getUserOverdueDays()
										+ ("e".equals(user.getLanguage())?" days,has expired, please contact the administrator":("天未登录，已失效，请联系管理员")));
								usdo.setDescription("登录系统失败:" + "帐户超过"+ loginService.getUserOverdueDays()+"天未登录，已失效，请联系管理员");
								sysLog = sysLogService.setUBaseSysLog(usdo,
										LOGIN_MSG, FAIL,
										Constants.BASE_SYS_LOG_AUTHORITY);
								sysLogService.saveUBaseSysLog(sysLog);
								return INPUT;
							}
						}
					}
					
					if ("0".equals(usdo.getEnabled())) {
						this.setMess(("e".equals(user.getLanguage())?"The user has been deactivated, please contact the administrator to enable":("用户已经被停用，请联系管理员启用")));
						usdo.setDescription("登录系统失败:" + "用户已经被停用，请联系管理员启用");
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
							// 启用密码策略
							if (!loginService.checkUserDate(user.getUserId())) {
								
								this.setMess(("e".equals(user.getLanguage())?"The account is not enabled for the period.":("账户在该时间段内未被启用。")));
								usdo.setDescription("登录系统失败:" + "账户在该时间段内未被启用。");
								sysLog = sysLogService.setUBaseSysLog(usdo,
										LOGIN_MSG, FAIL,
										Constants.BASE_SYS_LOG_AUTHORITY);
								sysLogService.saveUBaseSysLog(sysLog);
								return INPUT;
							}
							// isUserLocked: 1:连续输入错误锁定;2:密码过期;3:忘记密码，失效;
							if (StringUtils.isNotEmpty(usdo.getIsUserLocked())) {

								// 被锁定情况下
								if (usdo.getIsUserLocked().equals(
										LoginUtil.LOCK_LOCKED)) {
									if (LoginUtil.LOCK_REASON_PWD_INVALID_COUNTS
											.equals(usdo.getUserLockedReson())) {
										// 连续输入错误锁定
										if (usdo.getWrongPwdDate() != null
												&& LoginUtil.compareDate(
														new Date(), usdo
																.getWrongPwdDate(),
														LoginUtil.SHORT_FORMAT)) {
											
											this.setMess(("e".equals(user.getLanguage())?"More than ":("输入错误密码次数超过"))
													+ loginService
															.getPWDInvalidCounts()
													+ ("e".equals(user.getLanguage())?" input the wrong password, account is locked, can not access the system today":("次，帐户已被锁定，今日不能再访问系统")));
											usdo.setDescription("登录系统失败:" +"输入错误密码次数超过"+loginService.getPWDInvalidCounts()+"次，帐户已被锁定，今日不能再访问系统");
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
										this.setMess(("e".equals(user.getLanguage())?"Account locked down by the administrator, please contact the administrator unlocking":("帐户已被管理员锁定，请联系管理员解锁")));
										usdo.setDescription("登录系统失败:" + "帐户已被管理员锁定，请联系管理员解锁");
										sysLog = sysLogService.setUBaseSysLog(usdo,
												LOGIN_MSG, FAIL,
												Constants.BASE_SYS_LOG_AUTHORITY);
										sysLogService.saveUBaseSysLog(sysLog);
										return INPUT;

									} else if (LoginUtil.LOCK_REASON_USER_OVERDUE
											.equals(usdo.getUserLockedReson())) {// 失效
										this.setMess(("e".equals(user.getLanguage())?"User not log on  more than ":("帐户超过"))
												+ loginService.getUserOverdueDays()
												+ ("e".equals(user.getLanguage())?" days,has expired, please contact the administrator":("天未登录，已失效，请联系管理员")));
										usdo.setDescription("登录系统失败:" + "帐户超过"+loginService.getUserOverdueDays()+"天未登录，已失效，请联系管理员");
										sysLog = sysLogService.setUBaseSysLog(usdo,
												LOGIN_MSG, FAIL,
												Constants.BASE_SYS_LOG_AUTHORITY);
										sysLogService.saveUBaseSysLog(sysLog);
										return INPUT;
									}
								}
							}
							//密码过期，强制用户修改密码功能做成通用。
							/*boolean b = loginService.isOverdue(usdo); // 是否过期
							if (b) {
								this.setMess("密码已过期，请联系管理员重置密码");
								usdo.setDescription("登录系统失败:" + this.getMess());
								sysLog = sysLogService.setUBaseSysLog(usdo,
										LOGIN_MSG, FAIL,
										Constants.BASE_SYS_LOG_AUTHORITY);
								sysLogService.saveUBaseSysLog(sysLog);
								return INPUT;
							}*/

							// 用户是否被锁定
							// this.setMess("帐户已被锁定，请联系管理员解锁！");
							loginService.comeback(usdo.getUserId());
						}
						if ("0".equals(usdo.getEnabled())) {
							this.setMess(("e".equals(user.getLanguage())?"The user has been deactivated, please contact the administrator to enable":("用户已经被停用，请联系管理员启用")));
							usdo.setDescription("登录系统失败:" + "用户已经被停用，请联系管理员启用");
							sysLog = sysLogService.setUBaseSysLog(usdo, LOGIN_MSG,
									FAIL, Constants.BASE_SYS_LOG_AUTHORITY);
							sysLogService.saveUBaseSysLog(sysLog);
							return INPUT;
						}
					}
				}
			} else {
				this.setMess(("e".equals(user.getLanguage())?"Login failed, illegal parameter":("登录失败，非法参数")));
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
				
				
				
				usdo.setDescription("登录系统失败:用户名或密码错误");
				
				// 数据库中不存在登录用户名和对应的密码
				if (cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_ISUSE).equals("1")) {
					try {
						int m = loginService.checkUser(user.getUserId()); // 检查输错密码次数
						if (m < 0)
							this.setMess(("e".equals(user.getLanguage())?"Error ":("密码连续输入错误")) + (-m) + ("e".equals(user.getLanguage())?"consecutive input password, account is locked":("次，帐户已被锁定")));
						else {
							this.setMess(("e".equals(user.getLanguage())?"The user name or password is incorrect, please enter again.":("用户名或密码错误，请重新输入")));
						}
					} catch (Exception e) {
						log.error("login write system log failed ", e);
					}
				} else {
					this.setMess(("e".equals(user.getLanguage())?"The user name or password is incorrect, please enter again.":("用户名或密码错误，请重新输入")));
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
						log.warn("已存在相同的用户登录" + user.getUserId());
						sysLog = sysLogService.setUBaseSysLog(usdo, LOGIN_MSG,
								FAIL, Constants.BASE_SYS_LOG_AUTHORITY);
						sysLog.setDescription("登录失败，已存在相同的用户登录");
						sysLogService.saveUBaseSysLog(sysLog);
						this.setMess(("e".equals(user.getLanguage())?"The user has logged in, please don't login again":("该用户已登录，请勿重复登录")));
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
							this.setMess(("e".equals(user.getLanguage())?"Has reached the maximum number online, please later landing":("已经达到最大同时在线人数，请稍后登陆")));
							return INPUT;
						}
					}
				}
				
				// 获取用户信息
				usdo.setLanguage(user.getLanguage());
				usdo.setBrowser(request.getHeader("User-Agent"));
				usdo.setDescription("登录FMSS系统");
				usdo.setIp(ServletHelper.getRemoteAddr(request));
				String newLoginId = loginService.getNewLoginId(usdo.getUserId(),usdo.getIp());
	 			synchronized(this){
					if ("0".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_LOGIN_IP_VALIDATE))) {
						// 子系统无需验证ip
		 					newLoginId = String.valueOf(usdo.getUserId() + "-" + System.currentTimeMillis());
		 			}
					String s=onlineService.checkKickout(user.getUserId(), usdo.getIp());
					if(StringUtils.isNotEmpty(s)){
						if(s.indexOf("ERROR_2")==0)
							this.setMess(("e".equals(user.getLanguage())?"Sorry, you have been kicked out of the system. from the next landing with ### seconds":("对不起,您已经被踢出系统.距下次登陆时间还剩###秒")).replaceAll("###", s.substring("ERROR_2".length()+1)));
						else if(s.indexOf("ERROR_3")==0){
							this.setMess(("e".equals(user.getLanguage())?"Sorry, you have been kicked out of the system.":("对不起,您已经被踢出系统.")));
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
				// 注册用户信息

				request.getSession().setAttribute(LOGIN_USER, usdo);
				
				
				
				// 记录日至
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
			
		}catch(Exception e){//安全漏洞中，不希望抛出异常到页面信息上。
			this.setMess("请输入用户名和密码");
			log.error("登录异常：", e);
			return INPUT;
		}
 	}

	// 是否启用逻辑删除用户
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

	// LDAP验证
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
		//使用注入方式，导入api类，该类需要实现LdapAccessConnection类
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
				msg = "该用户[" + user.getUserId() + "]在系统中未注册";
				log.warn(msg + ":" + user.getUserId());
				throw new Exception(msg);
			}
		} else {
			throw new Exception(msg);
		}

	}

	/**
	 * <p>
	 * 方法名称: getUser|描述: 获得用户
	 * </p>
	 * 
	 * @return 返回用户对象
	 */
	public LoginDO getUser() {
		return user;
	}

	/**
	 * <p>
	 * 方法名称: setUser|描述: 设置用户
	 * </p>
	 * 
	 * @param user
	 */
	public void setUser(LoginDO user) {
		this.user = user;
	}

	/**
	 * <p>
	 * 方法名称: getLoginService|描述: 获得LoginService对象
	 * </p>
	 * 
	 * @return 返回LoginService对象
	 */
	public LoginService getLoginService() {
		return loginService;
	}

	/**
	 * <p>
	 * 方法名称: setLoginService|描述: 设置LoginService对象
	 * </p>
	 * 
	 * @param loginService
	 */
	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	/**
	 * <p>
	 * 方法名称: getMess|描述: 获得消息
	 * </p>
	 * 
	 * @return
	 */
	public String getMess() {
		return mess;
	}

	/**
	 * <p>
	 * 方法名称: setMess|描述: 设置消息
	 * </p>
	 * 
	 * @param mess
	 */
	public void setMess(String mess) {
		this.mess = mess;
	}

	/**
	 * <p>
	 * 方法名称: getSysLogService|描述: 获得UBaseSysLogService
	 * </p>
	 * 
	 * @return
	 */
	public UBaseSysLogService getSysLogService() {
		return sysLogService;
	}

	/**
	 * <p>
	 * 方法名称: setSysLogService|描述: 设置UBaseSysLogService
	 * </p>
	 * 
	 * @param sysLogService
	 */
	public void setSysLogService(UBaseSysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}

	/**
	 * @param cacheManager
	 *            要设置的 cacheManager
	 */
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	/**
	 * @param paramConfigService
	 *            要设置的 paramConfigService
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
			log.error("setUseSessionUrlInfo:"+sUrl+" 发生错误：", e);
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
