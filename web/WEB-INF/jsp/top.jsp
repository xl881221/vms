<%@ page language="java"  import="fmss.dao.entity.LoginDO,com.jes.core.api.servlet.ServletHelper"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="fmss.common.util.*,java.util.*"%>
<%@page import="fmss.common.config.UIConfig"%>
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
<link href="<c:out value="${sysTheme}"/>/css/top.css" type="text/css"rel="stylesheet">
<link href="<c:out value="${sysTheme}"/>/css/help_pop.css" type="text/css" rel="stylesheet">
<script src="<c:out value="${webapp}"/>/js/top.js"  type="text/javascript" language="javascript"></script>
<script type="text/javascript" src="<c:out value="${webapp}"/>/dwr/engine.js"></script>
<script type="text/javascript" src="<c:out value="${webapp}"/>/dwr/util.js"></script>
<script type="text/javascript" src="<c:out value="${webapp}"/>/dwr/interface/dwrAsynService.js"></script>
<script type="text/javascript" src="<c:out value="${webapp}"/>/js/SimpleTree/js/ajax.js"></script>
<script type="text/javascript" src="<c:out value="${webapp}"/>/js/jquery-1.4.4.min.js"></script>
<%
String language=ServletHelper.getCookieValue(request,"USYS_USER_LANGUAGE");
String paramSysUserInfoShow=(String)request.getAttribute("PARAM_SYS_USER_INFO_SHOW");
%>
<title>金融监管统计平台</title>
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
	var rootPath = "<c:out value='${sysTheme}'/>";
	var tDiv = $("topDiv"),img = $("ctrlimg");
	if(elm.getAttribute("_closed") != "true"){
		tDiv.style.display = "none";
		img.title = "<%if("e".equals(language))out.print("Reset");else out.print("向下还原");%>";
		img.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+rootPath+"/img/down.png', sizingMethod='scale')";
		topFrm.rows="29,0,*";
		elm.setAttribute("_closed","true");
	}else{
		tDiv.style.display = "";
		img.title = "<%if("e".equals(language))out.print("Maximum");else out.print("最大化");%>";
		img.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='"+rootPath+"/img/up.png', sizingMethod='scale')";
		topFrm.rows="81,0,*";
		elm.setAttribute("_closed","false");
	}
}
function udPwd() {
	dwrAsynService.isEditPwd(function (f) {
		if (!f) {
			alert('<%if("e".equals(language))out.print("Today you have modified the password,don’t modify again!");else out.print("今天您已修改过密码，每天只能修改一次！");%>');
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
			alert('<%if("e".equals(language))out.print("Today you have modified the password,don’t modify again!");else out.print("今天您已修改过密码，每天只能修改一次！");%>');
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
				if(ajaxUrls[i].indexOf("/vms")==-1&&ajaxUrls[i]!='/'){
				    s.open("GET", ajaxUrls[i] + "logout.ajax", true);
					s.send(null);
				}
				window.onunload=function(){}
				
			}catch(e){}
		}
	}catch(e){}
	window.top.location.href="logout.ajax"
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
/**function cleanWhitespace(oEelement){
	for(var i=0;i<oEelement.childNodes.length;i++){
	  	var node=oEelement.childNodes[i];
	  	if(node.nodeType==3 && !/\S/.test(node.nodeValue)){
	  		node.parentNode.removeChild(node)
	  	}
	}	
}
var index_menuBar=0;
function nextPage(){
	var page_size=6;
	var btm = document.getElementById("nextPageImg");
	var menuBar=document.getElementById("menuBar");
	cleanWhitespace(menuBar);
	var pages=menuBar.childNodes.length%page_size==0?menuBar.childNodes.length/page_size:menuBar.childNodes.length/page_size+1;
	pages=parseInt(pages);
	if(pages==1){
	    btm.style.display="none";
	}
	index_menuBar++;
	if(index_menuBar>pages){
	   index_menuBar=1;
	}
	for(var i=0;i<menuBar.childNodes.length;i++){
	    if(i>=(index_menuBar-1)*page_size&&i<index_menuBar*page_size){
	      	if(window.ActiveXObject){
	   	   		menuBar.childNodes[i].style.display="block";
	   	   	} else {
	       		menuBar.childNodes[i].display="block";
	       	}
	    }else{
	    	if(window.ActiveXObject){
	       		menuBar.childNodes[i].style.display="none";
	       		} else {
	       		menuBar.childNodes[i].display="none";
	       }
	    }
	}

	if(menuBar.childNodes.length<=page_size)
	   document.getElementById("nextPageImg").style.display="none";
}*/
 
</script>
	<%
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");

		String[] week = null;
		if("e".equals(language)){
		  week=new String[]{ "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
		}else{
		  week=new String[]{ "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		}
		
		
		LoginDO login = (LoginDO) request.getSession().getAttribute(
				"LOGIN_USER");
		String userCname = login.getUserCname();
		DateManager dm = new DateManager();
		java.sql.Date currentDay = dm.getDay(new Date());
		java.util.Calendar calendar = java.util.Calendar.getInstance();
		String weekDay = week[calendar.get(java.util.Calendar.DAY_OF_WEEK) - 1];
	%>


</head>
<body scrool="no"  leftmargin="0" topmargin="0" marginheight="0" marginwidth="0" onload="init();" onselectstart="return false" class="bluetop">
    
	<div class="header" id="topDiv" style="">
		<div class="logo">
<!--			<%=UIConfig.getInstance().getTopLogoPic()%>-->
<!--			<img src="listLogo.ajax?pramName=LOGO3&defaultPath=<c:out value="${sysTheme}"/>/img/logo.png" height="50">-->
			<img src="<c:out value="${webapp}"/>/themes/images/logo.png"/>
		</div>
		<div class="sysmeta">
		    <p>
				<%if("0".equals(paramSysUserInfoShow)){ }else{ %>
		     		 <%if("e".equals(language))out.print("You are welcome：");else out.print("欢迎：");%><%if("e".equals(language))out.print(login.getUserEname());else out.print(userCname);%>[<%=login.getUserEname()%>]！
		    	<%}%>
			</p>
			<ul>
				<a href="javascript:void(0);" onclick="udPwd()"><img src="<c:out value="${webapp}"/>/themes/images/icons/icon12.png"/><span>修改密码</span></a>
				<a href="javascript:void(0)" id="logout" onclick="logout()" ><img src="<c:out value="${webapp}"/>/themes/images/icons/icon10.png" /><span>退出登录</span></a>
			</ul>
		</div>
		<ul class="headermenu1">
	     	<li>
			<s:iterator value="top10MenuList" status="rowstatus" id="row">
				<s:if test="systemId == '00003' ">
						<script type="text/javascript">ajaxUrls[ajaxUrls.length] = '<s:property value="linkSiteUrl" />';</script>
						<a onclick="gotoSite('<s:property value="unitLoginUrl" />','menu.action?subSysId=<s:property value="systemId" />&subSysName=<s:property value="systemEname" />');tabChang(this);" href="javascript:void(0);" onFocus="this.blur()">
							<span class="icon"><img src="<c:out value="${webapp}"/>/themes/images/icons/<s:property value="menuImgSrc" />"></span>
							<s:if test='menu_language=="e"'><s:property value="systemNickEname" /></s:if><s:else><s:property value="systemCname" /></s:else>
						</a>
				</s:if>
			</s:iterator>
			</li>
		</ul>
<!--		<div class="menu_dropdown" onclick="nextPage();" id="btNextPage" title="<%if("e".equals(language))out.print("Next");else out.print("下页菜单");%>">-->
<!--			<img id="nextPageImg" src="<c:out value="${webapp}"/>/themes/images/button01.png" title="<%if("e".equals(language))out.print("Next");else out.print("下一页菜单");%>"/>-->
<!--		</div>-->
		<div class="menu_dropdown"  id="btNextPage">
			<a href="#" id="aNextPage" ><img id="nextPageImg" src="<c:out value="${webapp}"/>/themes/images/button01.png" title="<%if("e".equals(language))out.print("Next");else out.print("下一页菜单");%>"/></a>
		</div>
		<div class="headerbar">
<!--			<div class="headerbarmenu">-->
				<ul class="headermenu">
	        	    <s:iterator value="top10MenuList" status="rowstatus" id="row">
						<s:if test="menuRes == 1 || systemId == '00000' ">
							<li id="firstId" class="on">
							<a href="<s:property value='unitLoginUrl'/>" onclick="tabChang(this)" onFocus="this.blur()" name="mainpage" target="mainFramePage">
								<span class="icon"><img src="<c:out value="${webapp}"/>/themes/images/icons/<s:property value="menuImgSrc" />"></span>									
								<s:if test='menu_language=="e"'><s:property value="systemNickEname" /></s:if><s:else><s:property value="systemCname" /></s:else>
							</a>
							</li>
						</s:if>
					</s:iterator>
					<s:iterator value="top10MenuList" status="rowstatus" id="row">
						<s:if test="menuRes == 1 || systemId == '00010' || systemId == '00000' || systemId == '00003' "></s:if>
						<s:else>
							<li>
								<a onclick="gotoSite('<s:property value="unitLoginUrl" />','menu.action?subSysId=<s:property value="systemId" />&subSysName=<s:property value="systemEname" />');tabChang(this);" href="javascript:void(0);" onFocus="this.blur()">
									<span class="icon"><img src="<c:out value="${webapp}"/>/themes/images/icons/<s:property value="menuImgSrc" />"></span>
									<s:if test='menu_language=="e"'><s:property value="systemNickEname" /></s:if><s:else><s:property value="systemCname" /></s:else>
								</a>
							</li>
						</s:else>
					</s:iterator>
				</ul>
<!--			</div>-->
		</div>
	</div>
</body>
<script type="text/javascript">
	jQuery(function(){		
		jQuery("#aNextPage").click(function(){
			var liteam = 6;
			var lilength =  jQuery(".headermenu li").length;
			var menuposition = jQuery(".headermenu").position().top;
			var pagesum = lilength%liteam > 0 ? parseInt(lilength/liteam) + 1 : parseInt(lilength/liteam);
			if(menuposition == (-pagesum*100)+100){
				jQuery(".headermenu").animate({"top": 0},100)
			}
			else{
				
				jQuery(".headermenu").animate({"top": jQuery(".headermenu").position().top-100},100);
				//alert((jQuery(".headermenu").position().top) + "px");
			}
			return;
			
			
			
			var tabsum = 8;
			var curindex = jQuery(".headermenu li.on").index();
			var tabwidth = 100;
			
			jQuery(".headermenu li").eq(curindex).removeClass("on");
			if(curindex >=5)
			{
				if(curindex == tabsum-1){
					jQuery(".headermenu").animate({"left": 0},100);
				}
				else{
					jQuer(".headermenu").animate({"left": -((curindex-5)*tabwidth+tabwidth)},100);
				}
			}
			if(curindex == tabsum-1){
				jQuery(".headermenu li").eq(0).addClass("on");
			}
			else{
				jQuery(".headermenu li").eq(curindex+1).addClass("on");
			}
		});
	});	
   	//document.getElementById("barwidth").style.width = screen.availWidth - 760;
   	//document.getElementById("menuBar").style.width = screen.availWidth - 866;
   	
   	
   	
   	//window.onload = function(){
	//	var hightValue = screen.availHeight - 856;
	//	var hightValueStr = "height:"+ hightValue + "px";
		
	//	if (typeof(eval("document.all.menuBar"))!= "undefined"){
	//		document.getElementById("menuBar").setAttribute("style", hightValueStr);
	//	}
	//}
</script>
<script type="text/javascript">
function nextpage(){
	alert($("#aNextPage").html());
}

function viewPSWL() {
	dwrAsynService.getPswUDTime(function (l) {
		if (l =="F") {
			//alert(<%=login.isLdapLoginType()%>);
			//alert(<c:out value="${login.ldapLoginType}" />);
			//alert(<c:if test="${login.ldapLoginType=='false'}" />);
			if ((<%=login.isLdapLoginType()%>==false) ){
				if(confirm("<%=login.getUserCname()%>：\n    <%if("e".equals(language))out.print("Welcome to use the system.This is the first time that you log on to the system, whether to modify the login password?\\n【Tip: if no changes will be forced to exit the system】");else out.print("您好！欢迎使用本系统。\\n这是您首次登录系统，是否修改登录密码？\\n【提示：如果不修改将会强制退出系统】");%>"))
			  		forceUdPwd();
			  	else
			  		top.location.href='logout.ajax';
			}
		}  
		else if (l =="T"){
			if ((<%=login.isLdapLoginType()%>==false) ){
				if(confirm("<%=login.getUserCname()%>：\n    <%if("e".equals(language))out.print("Welcome to use the system. \\nYour password has expired, Please modify the login password! \\n【Tip: if no changes will be forced to exit the system】");else out.print("您好！欢迎使用本系统。\\n密码已经过期，请修改登录密码！\\n【提示：如果不修改将会强制退出系统】");%>"))
			  		forceUdPwd();
			  	else
			  		top.location.href='logout.ajax';
			}
		}  
		else if(l !=""){
				var e = document.getElementById("pswl_v");
				e.style.display = "";
				e.onclick = function () { 
					alert(l); 
				}
				if(<%=login.isLdapLoginType()%>==false)
					e.onclick();
			else
				return ;
		}
		else{
			return ;
		}
	});
}
viewPSWL();
window.onunload=function(){
   document.getElementById("logout").click();
}
var SSO_LOGIN_TIME=new Date();
<%
String PARAM_TIME_OUT=(String)request.getAttribute("PARAM_TIME_OUT");
String PARAM_KICKOUT=(String)request.getAttribute("PARAM_KICKOUT");
if(PARAM_KICKOUT!=null&&!PARAM_KICKOUT.equals("")){
%>
setInterval(function(){
                if(new Date().getTime()-SSO_LOGIN_TIME.getTime()>1000*<%=PARAM_TIME_OUT%>){
                   document.getElementById("logout").click();
                }
                //document.getElementById("TIME_TEST").innerHTML=(new Date().getTime()-SSO_LOGIN_TIME.getTime()-1000*<%=PARAM_TIME_OUT%>)/1000
            },2000)
<%
}
%>
<%
if(PARAM_KICKOUT.equals("1")){
%>
function checkLogin(){
    try{
	    dwrAsynService.checkLogin(function (s) {
			if(s!=null){
			   if(s.indexOf("ERROR_")==0){
			      s=s.substr(s.indexOf("ERROR_")+"ERROR_".length);
			      if(s=='1'){
			         alert("<%if("e".equals(language))out.print("Sorry, landing has timed out");else out.print("对不起,登陆已超时");%>");
			      }else if(s.indexOf("2_")==0){
			         alert("<%if("e".equals(language))out.print("Sorry, you have been kicked out of the system. from the next landing with ### seconds");else out.print("对不起,您已经被踢出系统.距下次登陆时间还剩###秒");%>".replace("###",s.substr(2))); 
			      }else if(s=="3"){
			         alert("<%if("e".equals(language))out.print("Sorry, you have been kicked out of the system.");else out.print("对不起,您已经被踢出系统.");%>"); 
			      }
			   }else{
			      alert("<%if("e".equals(language))out.print("Sorry, landing has timed out");else out.print("对不起,登陆已超时");%>");
			   }
			   document.getElementById("logout").click();
			}
			window.setTimeout(checkLogin,1000*60);
		});
    }catch(e){
        document.getElementById("logout").click();
    }
};
window.setTimeout(checkLogin,1000*60);
<%
}
%>
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
	<div class="close">
		<a href="#" onClick="closeHelp()"> <img
				src="<c:out value="${uprrTheme}"/>/img/close.gif" width="18"
				height="18" style="border: none" />
		</a>
	</div>
	<div>
		<ul>
			<s:iterator value="topMenuList">
				<s:if test="systemId!='00000'&&systemId!='00003'">
					<li>
						<a href="#"
							onclick="IsExistFile('<s:property value="systemId"/>')"><s:property
								value="systemNickCname" />
						</a>
					</li>
				</s:if>
			</s:iterator>
		</ul>
	</div>
</div>
</html>


