<%@page import="fmss.common.cache.CacheManager,org.apache.commons.lang.StringUtils"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();

	// webapp
	String webapp = request.getScheme() + "://" + request.getServerName() + ":"+ request.getServerPort() + path;
	
	// sysTheme
	String sysTheme = webapp + "/theme/default";
	if(StringUtils.isNotEmpty(CacheManager.getParemerCacheMapValue(fmss.common.util.Constants.PARAM_THEME_PATH_CONFIG))){
		sysTheme = webapp + CacheManager.getParemerCacheMapValue(fmss.common.util.Constants.PARAM_THEME_PATH_CONFIG);
	}
	pageContext.setAttribute("webapp", webapp);
	pageContext.setAttribute("sysTheme", sysTheme);
%>
 


