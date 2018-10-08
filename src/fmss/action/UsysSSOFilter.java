package fmss.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseSysParamDO;
import fmss.dao.entity.UBaseUserDO;
import fmss.common.cache.CacheManager;
import fmss.common.cache.CacheabledMap;
import fmss.services.LoginService;
import fmss.services.ParamConfigService;
import fmss.services.UserService;
import fmss.common.util.Constants;


import com.jes.core.api.servlet.ServletHelper;
import com.jes.core.sso.filter.BaseSSOFilter;

import common.crms.util.StringUtil;

public class UsysSSOFilter extends BaseSSOFilter{
	/**
	* <p>方法名称: reLogin|描述: session失效后,重新设置session信息,子类实现</p>
	* @param loginId 
	* @return 登陆ID
	*/
	public int reLogin(HttpServletRequest request,
			HttpServletResponse response,String userId) {
		// TODO Auto-generated method stub
		ApplicationContext c = WebApplicationContextUtils
	    .getRequiredWebApplicationContext(request.getSession().getServletContext());
		LoginService loginService = (LoginService)c.getBean("loginService");
		CacheManager cacheManager = (CacheManager)c.getBean("cacheManager");
		LoginDO usdo = loginService.checkUserExit(userId);
		usdo.setUserId(usdo.getUserId());
		usdo.setLanguage(UsysSSOFilter.getParamValue(request, "USYS_USER_LANGUAGE"));
		usdo.setBrowser(request.getHeader("User-Agent"));
		usdo.setDescription("登录FMSS系统");
		usdo.setIp(ServletHelper.getRemoteAddr(request));
		usdo.setLoginId(UsysSSOFilter.getParamValue(request, "USYS_LOGIN_ID"));
		usdo.setLdapLoginType(Boolean.valueOf(UsysSSOFilter.getParamValue(request, "USYS_ISLDAPTYPE")).booleanValue()) ;

		// 注册用户信息
		request.getSession().setAttribute(Constants.LOGIN_USER, usdo);
		
		
		CacheabledMap cache = (CacheabledMap) cacheManager
		.getCacheObject(Constants.PAPAMETER_CACHE_MAP);
		Map params = null;
		if (cache != null) {
			params=(Map) cache.get(Constants.PAPAMETER_CACHE_MAP);
			if (null != params) {
				UBaseSysParamDO param = (UBaseSysParamDO) params
						.get(Constants.PARAM_THEME_PATH_CONFIG);
				request.getSession().setAttribute(
						Constants.SESSION_THEME_KEY,
						param.getSelectedValue());
			}
		}
		setUseSessionUrlInfo(request.getRequestURL().toString(),request);
		request.getSession().getServletContext().setAttribute(
				Constants.SESSION_MENU_LANGUAGE,
				UsysSSOFilter.getParamValue(request, "USYS_USER_LANGUAGE"));
		//request.getSession().getServletContext().setAttribute(
		//		Constants.SESSION_MENU_LANGUAGE,
		//		this.getParam(request, "language"));
		return 1;
	}
	public void setUseSessionUrlInfo(String sUrl,HttpServletRequest request) {

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
		}
		request.getSession().setAttribute("defaultIPAddress",
				defaultIPAddress);
		request.getSession().setAttribute("defaultPort",defaultPort);
		
		request.getSession().setAttribute("isInnerNet",
				BaseSSOFilter.getParamValue(request, "isInnerNet"));
		

	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain chain) throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest)arg0;
		HttpServletResponse response = (HttpServletResponse)arg1;
		String noFilter=";checkHoliday.action;checkPrivs.ajax;getHolidayTypes.action;homeNoteData.action;loginqz.action;loginzs.action;login.action;logincas.action;loginZhiWen.action;logon.ajax;dwrAsynService.outPrintText.dwr;checkLogin.ajax;login.action;goLoginJsp.ajax;listLogo.ajax;";
		String s=request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/")+1);
		if(noFilter.indexOf(";"+s+";")!=-1){
			chain.doFilter(request, response);
		}
		else{
			super.doFilter(arg0, arg1, chain);
		}	
	}

    
	public boolean isSysEntry(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return false;
	}
}
