package fmss.action;

import java.io.PrintWriter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.jes.core.api.servlet.ServletHelper;
import com.opensymphony.xwork2.ActionSupport;
import common.crms.util.StringUtil;

import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseSysLogDO;
import fmss.common.cache.CacheManager;
import fmss.services.LoginService;
import fmss.services.OnlineService;
import fmss.services.UBaseSysLogService;
import fmss.common.util.Constants;

/**
 * <p>
 * ��Ȩ����:(C)2003-2010 
 * </p>
 * 
 * @����: xindengquan
 * @����: 2009-6-27 ����11:43:46
 * @����: [LogoutAction]�˳�ϵͳ��ע��session
 */
public class LogoutAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	/** ��¼service */
	private LoginService loginService;
	private CacheManager cacheManager;
	private OnlineService onlineService;
	private UBaseSysLogService sysLogService;
	private Log log2=LogFactory.getLog(LogoutAction.class);

	public void setSysLogService(UBaseSysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}

	public String execute() throws Exception {
		// ���request����
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpServletResponse response = ServletActionContext.getResponse();
 		try {
//			request.getSession().removeAttribute("_const_cas_assertion_");//cas�����¼ע��session
			LoginDO temp =  (LoginDO) request.getSession(true).getAttribute("LOGIN_USER");
			if(temp!=null){
				temp.setDescription("�˳�FMSSϵͳ");
				onlineService.deleteLogin(temp.getLoginId());
				/* ��¼���� */
				UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(temp,
						"�˳�", "1", Constants.BASE_SYS_LOG_AUTHORITY);
				sysLogService.saveUBaseSysLog(sysLog);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log2.error(e);
		}
		
		try{
			System.out.println(request.getSession().getId()+ ":logout................");
			Cookie[] cookies = request.getCookies();
			for(int i = 0; i < cookies.length; i++){
				cookies[i].setMaxAge(0);
				ServletHelper.setCookie(response, cookies[i]);
			}
		}catch (Exception e){
			log2.error(e);
			e.printStackTrace();
		}
		request.getSession().invalidate();
		String url=UsysSSOFilter.getParamValue(request, "USYS_PORTALURL");
		if(StringUtil.isEmpty(url))
			return SUCCESS;
		else{
			
			response.setContentType("text/html;charset=GBK");
			response.setCharacterEncoding("GBK");
			response.setHeader("Cache-Control",
					"no-store, max-age=0, no-cache, must-revalidate");
			// Set IE extended HTTP/1.1 no-cache headers.
			response.addHeader("Cache-Control", "post-check=0, pre-check=0");
			response.setHeader("Pragma", "no-cache");
			PrintWriter out = response.getWriter();
			out.print("<html><head></head><body onload=\"window.top.location.href='"+url+"'\"></body></html>");
			return null;
		}
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	/**
	 * <p>
	 * ��������: getLoginService|����: ���LoginService����
	 * </p>
	 * 
	 * @return
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

	public void setOnlineService(OnlineService onlineService) {
		this.onlineService = onlineService;
	}
}
