<%@ page language="java"  import="fmss.dao.entity.LoginDO,com.jes.core.api.servlet.ServletHelper"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="fmss.common.util.*,java.util.*"%>
<%@page import="fmss.common.config.UIConfig"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires",0);
%>

<!DOCTYPE html>
<html>
	<head>
		<!-- Meta -->
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<!-- End of Meta -->
		
<%@include file="common/include-null.jsp"%>
		<!-- Page title -->
<%String language=ServletHelper.getCookieValue(request,"USYS_USER_LANGUAGE");%>
<title><%if("e".equals(language))out.print("FMSS");else out.print("FMSS | Chinajes");%></title>
		<!-- End of Page title -->
		
		<!-- Libraries -->
		<link type="text/css" href="<c:out value="${webapp}"/>/wide/css/layout.css" rel="stylesheet" />	
		
		<script type="text/javascript" src="<c:out value="${webapp}"/>/wide/js/jquery-1.3.2.min.js"></script>
		<script type="text/javascript" src="<c:out value="${webapp}"/>/wide/js/easyTooltip.js"></script>
		<script type="text/javascript" src="<c:out value="${webapp}"/>/wide/js/jquery-ui-1.7.2.custom.min.js"></script>
		<script type="text/javascript" src="<c:out value="${webapp}"/>/wide/js/jquery.wysiwyg.js"></script>
		<script type="text/javascript" src="<c:out value="${webapp}"/>/wide/js/hoverIntent.js"></script>
		<script type="text/javascript" src="<c:out value="${webapp}"/>/wide/js/superfish.js"></script>
		<script type="text/javascript" src="<c:out value="${webapp}"/>/wide/js/jquery.bgiframe.min.js"></script>
		
		<script type="text/javascript" src="<c:out value="${webapp}"/>/wide/js/custom.js"></script>
		<!-- End of Libraries -->	
<%		
	LoginDO login = (LoginDO) request.getSession().getAttribute("LOGIN_USER");
	
	%>
		<script language="javascript">
		 
		function udPwd() {
				showModalDialog("editPswd.action", function () {
					document.getElementById("pswl_v").style.display = "none";
				}, "dialogWidth:360px;dialogHeight:240px;scroll=0;help=0;status=0;");
		}
		
var ajaxUrls = [];
function logout() {

	//取得注销提示信息并显示
	try{
		dwrAsynService.getLogoutAlertInfo(function(f){
			if (f!=null && f!="") {
				alert(f);
			}
		});
		var o={}
		for (var i=0; i<ajaxUrls.length; i++) {
		    var s = getAjax();
			try{
				if(ajaxUrls[i].indexOf("/fmss")==-1&&ajaxUrls[i]!='/'){
				    s.open("GET", ajaxUrls[i] + "logout.ajax", true);
					s.send(null);
				}
				window.onunload=function(){}
				
			}catch(e){}
		}
	}catch(e){}
	window.top.location.href="logout.ajax"
}
</script>	
	</head>
	<body id="body">
		<!-- Container -->
		<div id="container">
			<!-- Header -->
			<div id="header">
				<!-- Top -->
				<div id="top">
					<!-- Logo -->
					<div class="logo"> 
						<img src="<c:out value="${webapp}"/>/wide/assets/logo.png"/>
					</div>
					<!-- End of Logo -->
					<!-- Meta information -->
					<div class="meta">
						<p>Welcome: <%=login.getUserEname()%> !</p>
						<ul>
							<li><a onclick="logout()" href="#" class="tooltip"><span class="ui-icon ui-icon-power"></span>退出登录</a></li>
							<li ><a id="pswl_v" href="#" class="tooltip" onclick="udPwd()"><span class="ui-icon ui-icon-person"></span>修改密码</a></li>
						</ul>	
					</div>
					<!-- End of Meta information -->
				</div>
				<!-- End of Top-->
				<!-- The navigation bar -->
				<div id="navbar">
				</div>
				<!-- end navigation bar -->			
			</div>
			<!-- End of Header -->			
			<!-- Background wrapper -->
				<iframe name="mainFramePage" id="cbody"  src="" class="framebody" frameborder="0" scrolling="no" style="background:#CCC;"></iframe>				
			<!-- End of bgwrap -->
		</div>
<!--		 End of Container -->
		
		<!-- Footer -->
		<div id="footer" style="position: absolute;">
			<p class="mid" style="margin-bottom:0!important; line-height:15px!important;">
				Copyright © 上海华颉信息技术有限公司 版权所有
<!--				&copy; ChinaJesIT.com 2012. All rights reserved.-->
			</p>
		</div>
		<!-- End of Footer -->
 
 
	</body>
<script type="text/javascript">
    document.getElementById("cbody").style.height = screen.availHeight - 272;
</script>
</html>
				
