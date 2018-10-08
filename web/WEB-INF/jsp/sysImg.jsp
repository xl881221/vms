<%@ page language="java"  contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	response.setDateHeader("Expires", 0);
%>
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@include file="common/include.jsp"%>
<%@include file="common/commen-ui.jsp"%>
<meta http-equiv="expires" content="0">
<style type="text/css">
.english_style{
<%String style=CacheManager.getParemerCacheMapValue(fmss.common.util.Constants.PARAM_WELCOME_STYLE);
  out.print(style);
%>

}
</style>
</head>
<body style="background-image:url(<c:out value="${webapp}"/>/themes/images/icons/bg.png); background-repeat: no-repeat;">
<div id="tbl_current_status">
	<span><img src="<c:out value="${webapp}"/>/themes/images/icons/icon13.png" /></span>
	<span class="current_status_menu">当前位置：</span>
	<span class="current_status_submenu1"><c:out value="${title}"/></span>
</div>
<div class="welcomeword">欢迎进入<c:out value="${title}"/></div>
</body>
</html>