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
		<title><%if("e".equals(language))out.print("VMS");else out.print("VMS | Chinajes");%></title>
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
		<!-- End of Libraries  		-->	
		
		<%@include file="common/commen-ui.jsp"%>
		
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
	<body class="withvernav">

		<div class="header">
			<div class="logo"><img src="<c:out value="${webapp}"/>/themes/images/logo.png"/></div>
			<div class="sysmeta">
				<p>欢迎: <%=login.getUserEname()%> !<img src="<c:out value="${webapp}"/>/themes/images/icons/icon10.png" onclick="logout()" /></p>
				<ul>
					<a href="#"><img src="<c:out value="${webapp}"/>/themes/images/icons/icon11.png"/>系统管理</a>
					<a id="pswl_v" href="#" onclick="udPwd()"><img src="<c:out value="${webapp}"/>/themes/images/icons/icon12.png"/>修改密码</a>
				</ul>	
			</div>
			<div class="menu_dropdown">
				<img src="<c:out value="${webapp}"/>/themes/images/button01.png"/>
			</div>
			<div class="headerbar" id="barwidth">
				<ul class="headermenu">
		        	<li><a href="#"><span class="icon icon8"></span>自由查询</a></li>
		        	<li><a href="#"><span class="icon icon7"></span>人行分析表</a></li>
		        	<li><a href="#"><span class="icon icon6"></span>合规预警</a></li>
		        	<li><a href="#"><span class="icon icon5"></span>数据自动化</a></li>
		        	<li><a href="#"><span class="icon icon4"></span>补录系统</a></li>
		        	<li><a href="#"><span class="icon icon3"></span>支付信息系统</a></li>
		        	<li><a href="#"><span class="icon icon2"></span>客户风险评级</a></li>
		        	<li class="current"><a href="#"><span class="icon icon1"></span>首页</a></li>
		        </ul>
			</div>
			<div id="navbar"></div>			
		</div>
		<div id="container">
			<iframe name="mainFramePage" id="cbody"  src="" class="framebody" allowtransparency="true" frameborder="0" scrolling="no"></iframe>
		</div>
		<div id="footer">
			Copyright © 上海华颉信息技术有限公司 版权所有
		</div>
 


	</body>
<script type="text/javascript">
    document.getElementById("cbody").style.height = screen.availHeight - 230;
    document.getElementById("barwidth").style.width = screen.availWidth - 630;
</script>
</html>
				
