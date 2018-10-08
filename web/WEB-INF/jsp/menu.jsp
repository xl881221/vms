<%@ page language="java" import="com.jes.core.api.servlet.ServletHelper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/tld/menu.tld" prefix="menuOut"%>

<% 
java.util.List menuList = (java.util.List)request.getAttribute("menuList");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@include file="common/include.jsp"%>
<title>LeftMenu</title>
<%@include file="common/commen-ui.jsp"%>
<script language="javascript">
var SUBSYSIMG='<c:out value="${webapp}"/>/sysImg.action?subSysId=<%out.print(request.getParameter("subSysId"));%>';
</script>
<script language="javascript" src="<c:out value="${webapp}"/>/js/leftMenu.js"></script>
<script type="text/javascript" src="js/jquery-1.4.4.min.js"></script>
</head>
<body class="gray">
<%
String language=ServletHelper.getCookieValue(request,"USYS_USER_LANGUAGE");
%>
<div id="menuContainer">

<s:if test="menuList==null">
	<div style="margin-top: 100px; margin-left:50px;">=loading...</div>
</s:if>
<s:if test="menuList!=null">
	<menuOut:out menuList="<%=menuList %>" titleUnClickCssClass="titleStyle_1"
		titleOnClickCssClass="titleStyle_2" contentCssClass="contentStyle"
		menuCss="${sysTheme}/css/menu.css" contentHeight="250" layerWidth="230"
		themeImagesPath="${sysTheme}/img/menu" titleHeight="39" language="<%=language%>">
	</menuOut:out>
</s:if>
</div>

</body>
</html>
