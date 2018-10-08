<%@ page language="java" import="fmss.dao.entity.LoginDO"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="fmss.common.util.*,java.util.*"%>

<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@include file="common/include.jsp"%>
<meta http-equiv="expires" content="0">
<link href="<c:out value="${webapp}"/>/theme/db/css/top.css" type="text/css"
	rel="stylesheet">
<link href="<c:out value="${sysTheme}"/>/css/help_pop.css"
	type="text/css" rel="stylesheet">
<script defer type="text/javascript"
	src="<c:out value="${webapp}"/>/js/png.js"></script>
<script src="<c:out value="${webapp}"/>/js/top.js"
	type="text/javascript" language="javascript"></script>
<script type="text/javascript"
	src="<c:out value="${webapp}"/>/dwr/engine.js"></script>
<script type="text/javascript"
	src="<c:out value="${webapp}"/>/dwr/util.js"></script>
<script type="text/javascript"
	src="<c:out value="${webapp}"/>/dwr/interface/dwrAsynService.js"></script>
<title></title>
<script type="text/javascript">
	// 2009-07-16 14:09 ShiCH JS方法完善
	$ = function(id){
		return document.getElementById(id);
	}
	var ifrm = window.parent.document.getElementById("frame");
	var testLoginTimes = 0;
	function gotoSite(url1,url2){
		window.parent.frames["leftFramePage"].location=url2;
	}
	
	function bar_onclick(e){	
		var topFrm = window.top.document.getElementById("indexFrame");
		var evt = e ? e : window.event;
		var elm = evt.srcElement || evt.target;
		var rootPath = "<c:out value='${webapp}'/>/theme/db";
		var tDiv = $("topDiv"),img = $("ctrlimg");
		if(elm.getAttribute("_closed") != "true"){
			tDiv.style.display = "none";
			img.title = "向下还原";
			img.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+rootPath+"/img/down.png', sizingMethod='scale')";
			topFrm.rows="29,0,*";
			elm.setAttribute("_closed","true");
		}else{
			tDiv.style.display = "";
			img.title = "最大化";
			img.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+rootPath+"/img/up.png', sizingMethod='scale')";
			topFrm.rows="81,0,*";
			elm.setAttribute("_closed","false");
		}
	}
	function udPwd() {
		dwrAsynService.isEditPwd(function (f) {
			if (!f) {
				alert("今天您已修改过密码，每天只能修改一次！");
				return;
			}
			showModalDialog("editPswd.action", function () {
				document.getElementById("pswl_v").style.display = "none";
			}, "dialogWidth:360px;dialogHeight:240px;scroll=0;help=0;status=0;");
		});
	}
	function forceUdPwd() {
		dwrAsynService.isEditPwd(function (f) {
			if (!f) {
				alert("今天您已修改过密码，每天只能修改一次！");
				return;
			}
			results = window.showModalDialog("editPswd.action", function () {
				document.getElementById("pswl_v").style.display = "none";
			}, "dialogWidth:360px;dialogHeight:240px;scroll=0;help=0;status=0;");
			if(results != true){
				top.location.href = 'logout.ajax';
			} else {
				dwrAsynService.updatePasswordCallback(function (f) {});
			}
		});
	}
	var ajaxUrls = [];
	function logout() {
		//取得注销提示信息并显示
		dwrAsynService.getLogoutAlertInfo(function (f) {
			if (f!=null && f!="") {
				alert(f);
			}
		});
		
		for (var i=0; i<ajaxUrls.length; i++) {
			var s = getAjax();
			if (!s) continue;
				s.open("GET", ajaxUrls[i] + "logout.ajax", true);
				s.send(null);
		}
	}
	function getAjax() {
		var s = ["MSXML2.XMLHTTP", "Microsoft.XMLHTTP", "MSXML.XMLHTTP", "MSXML3.XMLHTTP"];
		if (window.ActiveXObject) {
			for(var i=0; i<s.length; i++) {
				try{
					return new ActiveXObject(s[i]);
				}catch(e){ }
			}
		} else if (window.XMLHttpRequest) {
			return new XMLHttpRequest();
		} else return null;
	}

	function nextPage(){
		var vfp = document.getElementById("firstPage"); 
		var vsp = document.getElementById("secondPage");
		var btm = document.getElementById("nextPageImg");
		
		if(vfp.style.display == "block"){
			vfp.style.display = "none";
			vsp.style.display = "block";
			btm.title = "上一页菜单";
			btm.src = "<c:out value='${sysTheme}'/>/img/page-prev.gif";
		}else{
			vfp.style.display = "block";
			vsp.style.display = "none";
			btm.title = "下一页菜单";
			btm.src = "<c:out value='${sysTheme}'/>/img/page-next.gif";
		}
	}

</script>
<%
	response.setHeader("Pragma", "No-cache");
	response.setHeader("Cache-Control", "no-cache");
	
	String[] week = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
	LoginDO login = (LoginDO) request.getSession().getAttribute(
			"LOGIN_USER");
	String userCname = login.getUserCname();
	DateManager dm = new DateManager();
	java.sql.Date currentDay = dm.getDay(new Date());
	java.util.Calendar calendar = java.util.Calendar.getInstance();
	String weekDay = week[calendar.get(java.util.Calendar.DAY_OF_WEEK) - 1];;
%>


</head>
<body leftmargin="0" topmargin="0" scroll="no" marginheight="0"
	marginwidth="0"  onLoad="init()"
	onselectstart="return false">

<div class="head" id="topDiv" style="cursor: default">
<div class="left"><img
	src="<c:out value="${webapp}"/>/theme/db/img/logo.png" height="50"></div>
<div class="right01"><a href="javascript:;" onclick="udPwd()">修改密码</a>
| <a href="logout.ajax" onclick="logout()" target="_top">注销</a> | <a
	href="#" id="help" onClick="showHelp()">帮助</a></div>
<div class="right02">平台版本:4.0</div>
<div class="nav"><%=userCname%>[<%=login.getUserEname()%>] 欢迎您！ <%=currentDay%>&nbsp;<%=weekDay%></div>
</div>
<table class="rav" cellspacing="0" cellpadding="0" width="100%"
	border="0" height="30">
	<tr height="30">
		<td class="mainNav"><!-- 
			<li id="firstId" >
				<a href="welcome.action" onclick="tabChang(this)" onFocus="this.blur()" name="mainpage" target="mainFramePage">
					<img src="<c:out value="${webapp}"/>/image/system/000.gif" border="0" align="absmiddle">&nbsp;首&nbsp;页
				</a>
			</li>
		 -->
		<div id="firstPage" style="display: block;">
		<ul style="display: inline; float: left; text-align: left;">
			<s:iterator value="top10MenuList" status="rowstatus" id="row">
				<s:if test="menuRes == 1 || systemId == '00010'">
					<li id="firstId"><a href="<s:property value='unitLoginUrl'/>"
						onclick="tabChang(this)" onFocus="this.blur()" name="mainpage"
						target="mainFramePage"> <img
						src="<c:out value="${webapp}"/>/image/system/<s:property value="menuImgSrc" />"
						border="0" align="absmiddle"> <s:property
						value="systemCname" />&nbsp; </a></li>
				</s:if>
				<s:else>
					<script type="text/javascript">ajaxUrls[ajaxUrls.length] = '<s:property value="linkSiteUrl" />';</script>
					<li><a
						onclick="gotoSite('<s:property value="unitLoginUrl" />','menu.action?subSysId=<s:property value="systemId" />&subSysName=<s:property value="systemEname" />');tabChang(this);"
						href="#" onFocus="this.blur()"> <img
						src="<c:out value="${webapp}"/>/image/system/<s:property value="menuImgSrc" />"
						border="0" align="absmiddle"> <s:property
						value="systemCname" />&nbsp; </a></li>
				</s:else>

			</s:iterator>
		</ul>
		</div>
		<s:if test="otherTopMenuList.size()>0">
			<div id="secondPage" style="display: none;">
			<ul style="display: inline; float: left; text-align: left;">
				<s:iterator value="otherTopMenuList" status="rowOtherstatus"
					id="rowOther">
					<s:if test="menuRes == 1 || systemId == '00010'">
						<li id="firstId"><a href="<s:property value='unitLoginUrl'/>"
							onclick="tabChang(this)" onFocus="this.blur()" name="mainpage"
							target="mainFramePage"> <img
							src="<c:out value="${webapp}"/>/image/system/<s:property value="menuImgSrc" />"
							border="0" align="absmiddle"> <s:property
							value="systemCname" />&nbsp; </a></li>
					</s:if>
					<s:else>
						<script type="text/javascript">ajaxUrls[ajaxUrls.length] = '<s:property value="linkSiteUrl" />';</script>
						<li><a
							onclick="gotoSite('<s:property value="unitLoginUrl" />','menu.action?subSysId=<s:property value="systemId" />&subSysName=<s:property value="systemEname" />');tabChang(this);"
							href="#" onFocus="this.blur()"> <img
							src="<c:out value="${webapp}"/>/image/system/<s:property value="menuImgSrc" />"
							border="0" align="absmiddle"> <s:property
							value="systemCname" />&nbsp; </a></li>
					</s:else>
				</s:iterator>
			</ul>
			</div>
		</s:if></td>
		<s:if test="otherTopMenuList.size()>0">
			<td onclick="nextPage();" valign="middle" class="mainNav"
				style="cursor: hand; width: 20px;" id="btNextPage" title="下页菜单">
			<img id="nextPageImg"
				src="<c:out value="${sysTheme}"/>/img/page-next.gif" alt=""
				border="0" style="cursor: hand;"></td>
		</s:if>
		<td valign="middle" class="mainNav" style="width: 20px;"><img
			id="pswl_v" src="<c:out value="${sysTheme}"/>/img/ptt_chgpwd.gif"
			alt="" border="0" style="cursor: hand; display: none;"></td>
		<td valign="middle" class="mainNav" onclick="bar_onclick(event)"
			style="width: 20px;"><img
			src="<c:out value="${sysTheme}"/>/img/up.png" id="ctrlimg" alt="最大化"
			border="0" style="cursor: hand;"></td>
	</tr>
</table>
</body>
<script type="text/javascript">
	function viewPSWL() {
		dwrAsynService.getPswUDTime(function (l) {
			if (l =="F") {
				//alert(<%=login.isLdapLoginType()%>);
				//alert(<c:out value="${login.ldapLoginType}" />);
				//alert(<c:if test="${login.ldapLoginType=='false'}" />);
				if ((<%=login.isLdapLoginType()%>==false) ){
					if(confirm("<%=login.getUserCname()%>：\n    您好！欢迎使用本系统。\n这是您首次登录系统，是否修改登录密码？\n【提示：如果不修改将会强制退出系统】"))
				  		forceUdPwd();
				  	else
				  		top.location.href='logout.ajax';
				}
			}  
			else if(l !=""){
				var e = document.getElementById("pswl_v");
				e.style.display = "";
				e.onclick = function () { alert(l); }
				if(<%=login.isLdapLoginType()%>==false)
					e.onclick();
			}
			else{
				return ;
			}
		});
	}
	viewPSWL();
	
	function isUserValid() {
		dwrAsynService.isUserValid(function (s) {
			if (s<0) {
				if(s==-2)
					alert("用户失效，请重新登陆");
				if(s==-1)
					alert("该用户已在其他地方登陆，请重新登陆");
				window.top.document.getElementById("indexFrame").onunload = null;
				window.top.location.href="index.html";
			}
		});
		window.setTimeout("isUserValid()", 5000);
	}
	dwrAsynService.isAllowSameUserLogin(function (s) {
		if(s==true){
			//isUserValid();
		}
	});
</script>
<!-- popup -->
<script src="<c:out value="${webapp}"/>/js/help.js"
	type="text/javascript" language="javascript"></script>
<script language="javascript">
		function IsExistFile(systemId){
			_POSTURL = "isExistFile.action?systemId="+systemId;
			//window.location = "down.action?systemId="+systemId;
			/*$.post(_POSTURL, {}, function (data) {
				a = eval('(' + data + ')');
				if(a){
					if(a.code == 5){
						window.location = "down.action?systemId="+systemId;
					}else{
						alert(a.message);
					}
				}
			});*/
			var loader = new ajax.ContentLoader("get",_POSTURL,"",function() {
			  var req = this.req;
			  if(req.readyState == ajax.READY_STATE_COMPLATE) {
			   if(req.status == ajax.HTTP_SUCCESS_CODE) {
			   	data = req.responseText;
			    a = eval('(' + data + ')');
				if(a){
					if(a.code == 5){
						window.location = "down.action?systemId="+systemId;
					}else{
						alert(a.message);
					}
				}
			   }
			  }
			});
		}
		
		function showHelp(){
			document.getElementById("helps").style.left = document.body.offsetWidth-510;
			document.getElementById("helps").style.top = "0px";
			document.getElementById("helps").style.display = "";
		}
		function closeHelp(){
			document.getElementById("helps").style.display = "none";
		}
	</script>

<div class="bg" id="helps" style="display: none; position: absolute;">
<div class="close"><a href="#" onClick="closeHelp()"> <img
	src="<c:out value="${sysTheme}"/>/img/close.gif" width="18"
	height="18" style="border: none" /></a></div>
<div>
<ul>
	<s:iterator value="topMenuList">
		<s:if test="systemId!='00000'&&systemId!='00003'">
			<li><a href="#"
				onclick="IsExistFile('<s:property value="systemId"/>')"><s:property
				value="systemNickCname" /></a></li>
		</s:if>
	</s:iterator>
</ul>
</div>
</div>
</html>
