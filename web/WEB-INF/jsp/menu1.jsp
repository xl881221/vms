<%@ page language="java" import="com.jes.core.api.servlet.ServletHelper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
String menuHTML = (String)request.getAttribute("menuHTML");
%>
<%@include file="common/include-null.jsp"%>
<%
String language=ServletHelper.getCookieValue(request,"USYS_USER_LANGUAGE");
%> 
<s:if test="menuList==null">

</s:if>
<s:if test="menuList!=null">
<%=menuHTML.replaceAll("null", "") %>
 
</s:if>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%@include file="common/commen-ui.jsp"%>
</head>
<body class="gray">
     
</body>
</html>