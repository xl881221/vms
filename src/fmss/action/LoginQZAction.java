package fmss.action;

import org.apache.commons.lang.StringUtils;

import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseOnlineDO;
import fmss.dao.entity.UBaseSysLogDO;
import fmss.dao.entity.UBaseSysParamDO;
import fmss.dao.entity.UBaseUserDO;
import fmss.common.cache.CacheManager;
import fmss.services.LogManagerService;
import fmss.services.LoginService;
import fmss.services.OnlineService;
import fmss.services.ParamConfigService;
import fmss.services.UBaseSysLogService;
import fmss.services.ldap.LdapAccessByApiTWCredit;
import fmss.services.ldap.LdapAccessByCertificate;
import fmss.services.ldap.LdapAccessByCommon;
import fmss.services.ldap.LdapAccessByWSConnection;
import fmss.services.ldap.LdapAccessConnection;
import fmss.common.util.Constants;
import fmss.common.util.LdapUtil;
import fmss.common.util.LoginUtil;

import com.easycon.busi.element.UniKeyValueObject;
import com.easycon.busi.util.PackUtil;
import com.easycon.portal.client.PortalClient;
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
public class LoginQZAction extends BaseAction {

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
	

	/** 2011-07-15 李志刚添加 用户ID，用于接收统一门户的用户信息 **/
	private String userCode;
	
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
		String appLoginKey=request.getParameter("AppLoginKey");
		 
		appLoginKey.toString();
		UniKeyValueObject uko = PortalClient.doRequestLoginInfo(appLoginKey);//获取门户登陆用户的相关信息
		log.info("appLoginKey1111111-----"+appLoginKey);
		log.info("应答信息是："+uko.getValue("YDXX"));
		log.info("userCode="+ uko.getValue("userId")+",userName="+ new String(uko.getValue("userName").getBytes(),"UTF-8"));
		log.info("deptid ="+ uko.getValue("deptid"));
		//如果应答码为00，则可以获取登录用户的相关信息
		if ("00".equals(uko.getValue("YDM"))) {
			this.userCode = uko.getValue("userId");
			//this.orgCode = uko.getValue("deptid");
			//this.userName = new String(uko.getValue("userName")
			user=new LoginDO();
			user.setUserId(userCode);
			user.setLanguage("c");
			UniKeyValueObject uko1 =  PortalClient.doRequestPortalIndexPage();
			//获取统一门户退出的url
			UsysSSOFilter.setParam(response, request, "USYS_PORTALURL", uko1.getValue("PORTALURL"));
		} else {
			PackUtil.backErrorPack("" + uko.getIntValue("JYM"), "登陆失败");
		}
			

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
		if (user != null && StringUtils.isNotEmpty(user.getUserId())) {
			usdo=loginService.checkUserById(user.getUserId());
			if (usdo != null) {
				usdo.setBrowser(request.getHeader("User-Agent"));
				usdo.setIp(ServletHelper.getRemoteAddr(request));

				if (loginService.checkPswIsUsing()) {
					// 启用密码策略
					if (!loginService.checkUserDate(user.getUserId())) {
						
						this.setMess(("e".equals(user.getLanguage())?"The account is not enabled for the period.":("账户在该时间段内未被启用。")));
						usdo.setDescription("登录系统失败:" + "账户在该时间段内未被启用。");
						PackUtil.backErrorPack("" + uko.getIntValue("JYM"),("e".equals(user.getLanguage())?"The account is not enabled for the period.":("账户在该时间段内未被启用。")));
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
							if (LoginUtil.LOCK_REASON_BY_USER
									.equals(usdo.getUserLockedReson())) {
								this.setMess(("e".equals(user.getLanguage())?"Account locked down by the administrator, please contact the administrator unlocking":("帐户已被管理员锁定，请联系管理员解锁")));
								usdo.setDescription("登录系统失败:" + "帐户已被管理员锁定，请联系管理员解锁");
								PackUtil.backErrorPack("" + uko.getIntValue("JYM"),("e".equals(user.getLanguage())?"Account locked down by the administrator, please contact the administrator unlocking":("帐户已被管理员锁定，请联系管理员解锁")));
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
								PackUtil.backErrorPack("" + uko.getIntValue("JYM"),("e".equals(user.getLanguage())?"User not log on  more than ":("帐户超过"))
										+ loginService.getUserOverdueDays()
										+ ("e".equals(user.getLanguage())?" days,has expired, please contact the administrator":("天未登录，已失效，请联系管理员")));
								sysLog = sysLogService.setUBaseSysLog(usdo,
										LOGIN_MSG, FAIL,
										Constants.BASE_SYS_LOG_AUTHORITY);
								sysLogService.saveUBaseSysLog(sysLog);
								return INPUT;
							}
						}
					}
					loginService.comeback(usdo.getUserId());
				}
				if ("0".equals(usdo.getEnabled())) {
					this.setMess(("e".equals(user.getLanguage())?"The user has been deactivated, please contact the administrator to enable":("用户已经被停用，请联系管理员启用")));
					PackUtil.backErrorPack("" + uko.getIntValue("JYM"),("e".equals(user.getLanguage())?"The user has been deactivated, please contact the administrator to enable":("用户已经被停用，请联系管理员启用")));
					usdo.setDescription("登录系统失败:" + "用户已经被停用，请联系管理员启用");
					sysLog = sysLogService.setUBaseSysLog(usdo, LOGIN_MSG,
							FAIL, Constants.BASE_SYS_LOG_AUTHORITY);
					sysLogService.saveUBaseSysLog(sysLog);
					return INPUT;
				}

			}
		} else {
			this.setMess(("e".equals(user.getLanguage())?"Login failed, illegal parameter":("登录失败，非法参数")));
			PackUtil.backErrorPack("" + uko.getIntValue("JYM"),("e".equals(user.getLanguage())?"Login failed, illegal parameter":("登录失败，非法参数")));
			return INPUT;
		}
		if (usdo == null) {
			usdo=loginService.checkUserById(user.getUserId());
 			if(usdo==null){
				usdo = new LoginDO();
				usdo.setUserId(user.getUserId());
				usdo.setUserCname(user.getUserId());
				usdo.setUserEname(user.getUserId());
 			}
 			
		
			usdo.setBrowser(request.getHeader("User-Agent"));
			usdo.setIp(ServletHelper.getRemoteAddr(request));
			usdo.setDescription("统一门户登陆错误");
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
					try {
						sysLog = sysLogService.setUBaseSysLog(usdo, LOGIN_MSG,
								FAIL, Constants.BASE_SYS_LOG_AUTHORITY);
						sysLog.setDescription("登录失败，已存在相同的用户登录");
						
						sysLogService.saveUBaseSysLog(sysLog);
					} catch (Exception e) {
						log.error(e);
					}
					PackUtil.backErrorPack("" + uko.getIntValue("JYM"),("e".equals(user.getLanguage())?"The user has logged in, please don't login again":("该用户已登录，请勿重复登录")));
					return INPUT;
				}
			}
			
			UBaseSysParamDO u=paramConfigService.findById("60");
			int maxValue=0;
			if(u!=null){
				if(u.getSelectedValue()!=null){
					maxValue=Integer.parseInt(u.getSelectedValue());
				}
			}
//			("e".equals(user.getLanguage())?"Sorry, you have been kicked out of the system. From the next landing with ### seconds":("对不起,您已经被踢出系统.距下次登陆时间还剩###秒"))
			if(maxValue>0){
				UBaseUserDO currentUser=loginService.findByUser(user.getUserId());
				if(!"1".equals(currentUser.getIsList())){
					if(onlineService.countUserNotList()>=maxValue){
						this.setMess(("e".equals(user.getLanguage())?"Has reached the maximum number online, please later landing":("已经达到最大同时在线人数，请稍后登陆")));
						return INPUT;
					}
				}
			}
			
			// 获取用户信息
			usdo = loginService.checkUserExit(user.getUserId());
			usdo.setUserId(user.getUserId());
			usdo.setLanguage(user.getLanguage());
			usdo.setBrowser(request.getHeader("User-Agent"));
			usdo.setDescription("登录FMSS系统");
			usdo.setIp(ServletHelper.getRemoteAddr(request));
			String newLoginId = loginService.getNewLoginId(usdo.getUserId(),usdo.getIp());
			synchronized(this){
				if ("0".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_LOGIN_IP_VALIDATE))) {
					// 子系统无需验证ip
					newLoginId = String.valueOf(usdo.getUserId() + "-"
							+ System.currentTimeMillis());
				}
				String s=onlineService.checkKickout(user.getUserId(), usdo.getIp());
				if(StringUtils.isNotEmpty(s)){
					if(s.indexOf("ERROR_2")==0){
						PackUtil.backErrorPack("" + uko.getIntValue("JYM"),("e".equals(user.getLanguage())?"Sorry, you have been kicked out of the system. from the next landing with ### seconds":("对不起,您已经被踢出系统.距下次登陆时间还剩###秒")).replaceAll("###", s.substring("ERROR_2".length()+1)));
						this.setMess(("e".equals(user.getLanguage())?"Sorry, you have been kicked out of the system. from the next landing with ### seconds":("对不起,您已经被踢出系统.距下次登陆时间还剩###秒")).replaceAll("###", s.substring("ERROR_2".length()+1)));
					}
						
					else if(s.indexOf("ERROR_3")==0){
						PackUtil.backErrorPack("" + uko.getIntValue("JYM"),("e".equals(user.getLanguage())?"Sorry, you have been kicked out of the system.":("对不起,您已经被踢出系统.")));
						this.setMess(("e".equals(user.getLanguage())?"Sorry, you have been kicked out of the system.":("对不起,您已经被踢出系统.")));
					}
					return INPUT;
				}
				newLoginId=onlineService.registerUser(new UBaseOnlineDO(newLoginId,usdo.getUserId(),usdo.getIp()));
	
				UsysSSOFilter.setUserId(response, usdo.getUserId());
				UsysSSOFilter.setParam(response,request, "USYS_LOGIN_ID", newLoginId);
				UsysSSOFilter.setParam(response,request, "USYS_USER_LANGUAGE", user.getLanguage());
				UsysSSOFilter.setParam(response,request, "USYS_ISLDAPTYPE", String.valueOf(usdo.getLdapLoginType()));
				request.getSession().setAttribute(UsysSSOFilter.SSO_SESSION_USER_ID, usdo.getUserId());
				
				usdo.setLoginId(newLoginId);
			}	
			usdo.setLdapLoginType(user.getLdapLoginType()) ;

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
	private boolean checkUserByLDAP(LoginDO usdo) throws Exception {
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
		// 暂只支持信托的API方式 ，更多 API方式，需要有配置处 得到当前银行的API ldap instance.
		if (isLdapLoginTypeByApi()) {
			conn = LdapAccessByApiTWCredit.getConnection();
		}
		if (conn == null) {
			throw new RuntimeException(
					"unsupported authorize type, authorized  fail");
		}
		String msg = conn.auth(usdo.getUserId(), usdo.getPassword());
		if (LdapAccessConnection.isAuthorizeSuccess(msg)) {
			if (loginService.checkUserExit(usdo.getUserId()) != null)
				return true;
			else {
				msg = "该用户[" + usdo.getUserId() + "]在系统中未注册";
				log.warn(msg + ":" + usdo.getUserId());
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
			request.getSession().setAttribute("defaultPort", defaultPort);
			
			String usysInnerUrl=cacheManager.getParemerCacheMapValue(Constants.PARAM_USYS_INNER_URL);
			usysInnerUrl=this.getURL(usysInnerUrl,defaultIPAddress,defaultPort).replaceAll(" ", "").replaceAll(":80/", "/");
			if(request.getRequestURL().indexOf(usysInnerUrl)!=-1){
				request.getSession().setAttribute("isInnerNet","true");
			}
			request.getSession().setAttribute("isInnerNet","true");
			UsysSSOFilter.setParam(response, request,"isInnerNet","true");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}



	public String getUserCode() {
		return userCode;
	}

	public void setOnlineService(OnlineService onlineService) {
		this.onlineService = onlineService;
	}
}
