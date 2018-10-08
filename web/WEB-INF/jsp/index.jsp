<%@ page language="java" import="com.jes.core.api.servlet.ServletHelper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires",0);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="common/include.jsp"%>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">

<!-- Page title -->
<%String language=ServletHelper.getCookieValue(request,"USYS_USER_LANGUAGE");%>
<title><%if("e".equals(language))out.print("VMS");else out.print("VMS | Chinajes");%></title>
<!-- End of Page title -->
<meta name="GENERATOR" content="Microsoft Visual Studio .NET 7.1">
<meta content="http://schemas.microsoft.com/intellisense/ie5" name="vs_targetSchema">
<script type="text/javascript">
var timer, curr = new Date().getTime();
function keyListener() {
	window.frames["topFramePage"].window.SSO_LOGIN_TIME=new Date();
}
function frameCheck() {
    window.frames["topFramePage"].window.SSO_LOGIN_TIME=new Date();
	var e = document.getElementById("mainFramePage");
	if (e.readyState == "complete") {
	    window.setTimeout(function (){
		    var win = document.frames["mainFramePage"].window;
			var doc = win.document;
			if (window.attachEvent) {
				doc.attachEvent("onkeypress", keyListener);
				doc.attachEvent("onmousedown", keyListener);
			} else if (window.addEventListener) {
				doc.addEventListener("onkeypress", keyListener, false);
				doc.addEventListener("onmousedown", keyListener, false);
			}
	    },3000)
	} else 
		window.setTimeout(frameCheck, 5000);
}

function refresher(){
	// 增加定时刷新
	//document.frames("tempFramePage").window.location.reload();
	//window.setTimeout("refresher()", 60000);
}

</script>
</head>
<frameset  id="indexFrame" name="indexFrame" rows="88,0,*" cols="*" framespacing="0" frameborder="0" border="0">
	<frame src="top.action" name="topFramePage" id="topFramePage" scrolling="no" noresize frameborder="0" marginwidth="0">
	<frame src="refresher.action" name="tempFramePage" id="tempFramePage" scrolling="no" noresize frameborder="0" marginwidth="0">
	<frameset rows="*" cols="0,0,*" framespacing="0" frameborder="0" id="frame" name="frame">
		<frame name="leftFramePage" id="leftFramePage" src="" frameborder="0" scrolling="auto" noresize>
		<frame name="midFramePage"  id="midFramePage"  src="midlle.action" frameborder="0" scrolling="no" noresize>
		<frame name="mainFramePage" id="mainFramePage" src="welcome.action"  frameborder="0" scrolling="auto" 
			id="mainFramePage" marginheight="0" onload="frameCheck();refresher()">
	</frameset>
</frameset>
</html>