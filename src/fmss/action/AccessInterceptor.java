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
 * ��Ȩ����:(C)2003-2010 
 * </p>
 * 
 * @����: xindengquan
 * @����: 2009-6-27 ����10:39:38
 * @����: [AccessInterceptor]�û����ʵ�������
 */
public class AccessInterceptor implements Interceptor {
	/** ��¼��־��Ϣ */
	private static final Logger log = Logger.getLogger(AccessInterceptor.class);

	private static final long serialVersionUID = 1L;
	/* �����ڴ��е��û�key */
	private static final String LOGIN_USER = "LOGIN_USER";
	/* ��¼service */
	private LoginService loginService;
	private OnlineService onlineService;
	/* ��־service */
	private UBaseSysLogService sysLogService;

	private LogManagerService logManagerService;

	private FunctionService functionService;
	
	private CacheManager cacheManager;
	/* userA */
	private LoginDO userA;
	/* userB */
	private LoginDO userB;

	public String intercept(ActionInvocation invocation) throws Exception {
		Map session = invocation.getInvocationContext().getSession(); // ���session����
		HttpServletRequest request = ServletActionContext.getRequest(); // ���requeset����
		String uri = request.getRequestURI();
		Object obj = invocation.getAction();
		String flag = ""; // ��־
		
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
		if (obj instanceof LoginAction) { // �û���¼
			LoginAction loginAction = (LoginAction) obj;
			userA = loginAction.getUser();
			LoginDO temp = (LoginDO) session.get(LOGIN_USER);
			/* ����ڴ�ļ�¼ */
			if (temp != null && temp.getLoginId() != null
					&& temp.getLoginId().length() > 0) {
				session.remove(LOGIN_USER);
			}
			flag = "login";
		} else if (!".ajax".equals(uri.substring(uri.indexOf(".")))
				&& !(obj instanceof LogoutAction)) { // ����.ajaxΪ��β�Ҳ���ע��
			// ���ڴ��л���û���Ϣ
			userB = (LoginDO) session.get(LOGIN_USER);
			flag = "common";
		}
		if (".ajax".equals(uri.substring(uri.indexOf(".")))) { // ��.ajaxΪ��β����������
			return invocation.invoke();
		} else { // ����
			LoginDO user = null;
			if ("login".equals(flag)) {
				user = userA;
			} else if ("common".equals(flag)) {
				user = userB;
			}
			if ("common".equals(flag) && (user != null)) {
				// ��¼�������������

				// ϸ���ȷ��ʿ���
				ActionProxy action = invocation.getProxy();
				if (functionService.checkPrivilegeWith00003MenuFunc(user
						.getUserId(), action.getActionName() + ".action")) {
					ServletActionContext.getResponse().sendRedirect("messageDisplay.ajax?mes=1005");
					return null;
				}
			} else if ("common".equals(flag) && (user == null)) {
				// sessionʵЧ�����������ӣ���ʾ������Ϣ�������ص�¼ҳ��
				ServletActionContext.getResponse().sendRedirect(
						"messageDisplay.ajax?mes=1004");
			} else {
				// �û���������Ϊ��ʱֱ�ӷ��ص�¼����
				ServletActionContext.getResponse().sendRedirect(
						"goLoginJsp.ajax");
			}
			return invocation.invoke();
		}
	}

	/*
	 * ���� Javadoc�� <p>��д����: destroy|����: </p>
	 * 
	 * @see com.opensymphony.xwork2.interceptor.Interceptor#destroy()
	 */
	public void destroy() {
	}

	/*
	 * ���� Javadoc�� <p>��д����: init|����: </p>
	 * 
	 * @see com.opensymphony.xwork2.interceptor.Interceptor#init()
	 */
	public void init() {
	}

	/**
	 * <p>
	 * ��������: getLOGIN_USER|����: ����ڴ��û�key
	 * </p>
	 * 
	 * @return
	 */
	public static String getLOGIN_USER() {
		return LOGIN_USER;
	}

	/**
	 * <p>
	 * ��������: setLoginService|����: ����LoginService
	 * </p>
	 * 
	 * @param loginService
	 */
	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
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
