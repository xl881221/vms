<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@page import="fmss.common.cache.CacheManager,org.apache.commons.lang.StringUtils"%>
<%
	String path = request.getContextPath();
	String webapp = request.getScheme() + "://" + request.getServerName() + ":"+ request.getServerPort() + path;
	String sysTheme = webapp + "/theme/default";
	if(StringUtils.isNotEmpty(CacheManager.getParemerCacheMapValue(fmss.common.util.Constants.PARAM_THEME_PATH_CONFIG))){
		sysTheme = webapp + CacheManager.getParemerCacheMapValue(fmss.common.util.Constants.PARAM_THEME_PATH_CONFIG);
	}
	pageContext.setAttribute("webapp", webapp);
	pageContext.setAttribute("sysTheme", sysTheme);
%>
<link href="<c:out value="${sysTheme}"/>/css/main.css" rel="stylesheet" type="text/css" />
<link href="<c:out value="${webapp}"/>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />


