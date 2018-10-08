package fmss.action;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseSysLogDO;
import fmss.common.cache.CacheManager;
import fmss.services.FunctionService;
import fmss.services.LogManagerService;
import fmss.services.LoginService;
import fmss.services.OnlineService;
import fmss.services.UBaseSysLogService;
import fmss.common.util.Constants;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;


import com.jes.core.api.servlet.ServletHelper;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * <p>
 * 版权所有:(C)2003-2010 
 * </p>
 * 
 * @作者: xindengquan
 * @日期: 2009-6-27 上午10:39:38
 * @描述: [AccessInterceptor]用户访问的拦截器
 */
public class AccessInterceptor implements Interceptor {
	/** 记录日志信息 */
	private static final Logger log = Logger.getLogger(AccessInterceptor.class);

	private static final long serialVersionUID = 1L;
	/* 放入内存中的用户key */
	private static final String LOGIN_USER = "LOGIN_USER";
	/* 登录service */
	private LoginService loginService;
	private OnlineService onlineService;
	/* 日志service */
	private UBaseSysLogService sysLogService;

	private LogManagerService logManagerService;

	private FunctionService functionService;
	
	private CacheManager cacheManager;
	/* userA */
	private LoginDO userA;
	/* userB */
	private LoginDO userB;

	public String intercept(ActionInvocation invocation) throws Exception {
		Map session = invocation.getInvocationContext().getSession(); // 获得session对象
		HttpServletRequest request = ServletActionContext.getRequest(); // 获得requeset对象
		String uri = request.getRequestURI();
		Object obj = invocation.getAction();
		String flag = ""; // 标志
		
		//  
		if(obj instanceof HolidayAPIAction){
			return invocation.invoke();
		}
		if(obj instanceof FunctionMetaAction){
			return invocation.invoke();
		}
		if(obj instanceof HomeDataXmlAction){
			return invocation.invoke();
		}
		if (obj instanceof LoginAction) { // 用户登录
			LoginAction loginAction = (LoginAction) obj;
			userA = loginAction.getUser();
			LoginDO temp = (LoginDO) session.get(LOGIN_USER);
			/* 清空内存的记录 */
			if (temp != null && temp.getLoginId() != null
					&& temp.getLoginId().length() > 0) {
				session.remove(LOGIN_USER);
			}
			flag = "login";
		} else if (!".ajax".equals(uri.substring(uri.indexOf(".")))
				&& !(obj instanceof LogoutAction)) { // 不以.ajax为结尾且不是注销
			// 从内存中获得用户信息
			userB = (LoginDO) session.get(LOGIN_USER);
			flag = "common";
		}
		if (".ajax".equals(uri.substring(uri.indexOf(".")))) { // 以.ajax为结尾的无需拦截
			return invocation.invoke();
		} else { // 拦截
			LoginDO user = null;
			if ("login".equals(flag)) {
				user = userA;
			} else if ("common".equals(flag)) {
				user = userB;
			}
			if ("common".equals(flag) && (user != null)) {
				// 登录后访问其他链接

				// 细粒度访问控制
				ActionProxy action = invocation.getProxy();
				if (functionService.checkPrivilegeWith00003MenuFunc(user
						.getUserId(), action.getActionName() + ".action")) {
					ServletActionContext.getResponse().sendRedirect("messageDisplay.ajax?mes=1005");
					return null;
				}
			} else if ("common".equals(flag) && (user == null)) {
				// session实效访问其他链接，显示出错信息，并返回登录页面
				ServletActionContext.getResponse().sendRedirect(
						"messageDisplay.ajax?mes=1004");
			} else {
				// 用户名和密码为空时直接返回登录界面
				ServletActionContext.getResponse().sendRedirect(
						"goLoginJsp.ajax");
			}
			return invocation.invoke();
		}
	}

	/*
	 * （非 Javadoc） <p>重写方法: destroy|描述: </p>
	 * 
	 * @see com.opensymphony.xwork2.interceptor.Interceptor#destroy()
	 */
	public void destroy() {
	}

	/*
	 * （非 Javadoc） <p>重写方法: init|描述: </p>
	 * 
	 * @see com.opensymphony.xwork2.interceptor.Interceptor#init()
	 */
	public void init() {
	}

	/**
	 * <p>
	 * 方法名称: getLOGIN_USER|描述: 获得内存用户key
	 * </p>
	 * 
	 * @return
	 */
	public static String getLOGIN_USER() {
		return LOGIN_USER;
	}

	/**
	 * <p>
	 * 方法名称: setLoginService|描述: 设置LoginService
	 * </p>
	 * 
	 * @param loginService
	 */
	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
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

	public void setLogManagerService(LogManagerService logManagerService) {
		this.logManagerService = logManagerService;
	}

	public void setFunctionService(FunctionService functionService) {
		this.functionService = functionService;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public void setOnlineService(OnlineService onlineService) {
		this.onlineService = onlineService;
	}
}
