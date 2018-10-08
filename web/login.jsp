<%@ page language="java" import="fmss.common.cache.CacheManager,com.jes.core.api.servlet.ServletHelper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="fmss.common.config.UIConfig"%>
<%@page import="java.util.Date"%>
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
	System.out.println("--------------------"+sysTheme);
	pageContext.setAttribute("webapp", webapp);
	pageContext.setAttribute("sysTheme", sysTheme);
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
 <% Object language=request.getParameter("user.language");
   if(language==null)
   	  language=ServletHelper.getCookieValue(request,"USYS_USER_LANGUAGE");
   String passKey=new Long(new Date().getTime()).toString();
   ServletHelper.setCookie(response,"USYS_PASSKEY",passKey);
%>
<title>1<%if("e".equals(language))out.print("VMS");else out.print("增值税管理平台");%></title>

<script language="javascript" type="text/javascript" src="<%=webapp %>/js/main.js" charset="GBK"></script>
<link href="<c:out value="${sysTheme}"/>/css/main.css" rel="stylesheet" type="text/css" />
<script language="javascript" type="text/javascript" src="<%=webapp %>/js/window.js" charset="UTF-8"></script>
<script language="javascript" type="text/javascript" src="<%=webapp %>/js/validator.js" charset="UTF-8"></script>

<!-- MessageBox -->
<script src="<c:out value="${webapp}"/>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<c:out value="${webapp}"/>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
    
<script language="javascript" type="text/javascript" charset="UTF-8">
	<%
	if(request.getParameter("RESULT_MESSAGE") !=null && !"".equals(request.getParameter("RESULT_MESSAGE"))){
	%>
		alert("<%=java.net.URLDecoder.decode(request.getParameter("RESULT_MESSAGE"),"utf-8")%>");
	<%
	}else if(request.getAttribute("RESULT_MESSAGE") !=null && !"".equals(request.getAttribute("RESULT_MESSAGE"))){	
	%>
	    alert("<%=java.net.URLDecoder.decode(request.getAttribute("RESULT_MESSAGE").toString(),"utf-8")%>");
	<%
	}else if(request.getParameter("resultMessages") !=null && !"".equals(request.getParameter("resultMessages"))){	
	%>
		alert("<%=request.getParameter("resultMessages")%>");
	<%
	}else if(request.getAttribute("resultMessages") !=null && !"".equals(request.getAttribute("resultMessages"))){	
	%>
	    alert("<%=request.getAttribute("resultMessages")%>");
	<%
	}	
	%>
</script> 

<link href="<c:out value="${sysTheme}"/>/css/login.css" rel="stylesheet" type="text/css" />

<style type="text/css">
.errClass {font-size: 12px;font-weight: bold;font-color: #00ff00;}
</style>
<script src="<c:out value="${webapp}"/>/js/png.js"  type="text/javascript" charset="UTF-8"></script>
<script src="<c:out value="${webapp}"/>/js/login.js" type="text/javascript" charset="UTF-8"></script>
<!--<script type="text/javascript" src="<c:out value="${webapp}"/>/dwr/engine.js"></script>-->
<!--<script type="text/javascript" src="<c:out value="${webapp}"/>/dwr/util.js"></script>-->
<!--<script type="text/javascript" src="<c:out value="${webapp}"/>/dwr/interface/dwrAsynService.js"></script>-->
<script language="javascript">	

var PASSKEY="<%=passKey%>";
 
var iRet; 
//--------------------------------------------------------------//
// 获取指纹特征(BASE64格式)
//--------------------------------------------------------------//
function FingerMatch_OnClick()
{
	var strIP, nPort, nMaxTime;
	var strSysCode;
	var strOrgCode;
	var strTlrCode;
	var strResult;
	var dtm ;
 	
	// 参数初始化
	strIP = "127.0.0.1";
	nPort = "8000";
	nMaxTime = "10";
	strTlrCode = document.getElementById('userId').value;
	strSysCode = "1001";
	strOrgCode = "999999";
 	dtm = document.getElementById('dtm'); 
 	
 	var userNameIpt = $G("userId");
	var reg = /(^\s*)|(\s*$)/gim;
	if(userNameIpt.value.replace(reg,"") == ""){
    var m = new MessageBox(userNameIpt);
    m.Show("请输入用户名");
 		userNameIpt.focus();
		return;
	}
 	
	// 调用指纹通信端口
    strResult = dtm.FPIFingerMatch(-1, strIP, nPort, strIP, nPort, nMaxTime, strSysCode, strOrgCode, strTlrCode, 1, 0);
    //strResult="0";
    if("0"==strResult){
     		form.action = "loginZhiWen.action?userId="+userNameIpt.value;
  			form.submit();
    }else{
   	    alert("指纹验证失败:" + strResult);
    }
}
 


function outPrintText(){
	dwrAsynService.outPrintText(function(f){
		var n=document.getElementById('dynamic');
		if(n!=null){
			n.innerHTML=f;
		}
	});
	<%if("e".equals(language))out.print("change_lan('e');");%>
}

function change_text(text1,text2,text3,text4,text5,text6,text7){

	document.getElementById('di1').innerHTML=text1;
	document.getElementById('di2').innerHTML=text2;
	document.getElementById('di3').innerHTML=text3;
	document.getElementsByName("Submit").item(0).className=text4;
	if(document.getElementById('bottom'))
		document.getElementById('bottom').innerHTML=text5;
	if(document.getElementById('url'))
		document.getElementById('url').innerHTML=text6;
    if(document.getElementById('jinggao'))
		document.getElementById('jinggao').innerHTML=text7;
	
}

function change_lan(n){
	if(n==='e'){
		change_text('userid','password','语言选择：','input05','','','Authorized access only. Please do not try to access this application if you are not an authorized user.');
	}else if(n==='c'){
	    change_text('用户名','密 码','LANGUAGE CHOOSE：','input02','','','禁止非授权用户访问,请不要尝试攻击本平台');}
	}


</script>

</head>

<body onload="outPrintText()">
	 <% String isHengSheng="no"; 
		if(pageContext.getAttribute("sysTheme").toString().indexOf("green")>-1){
		isHengSheng = "yes";
		} 
	%>
<form method="post" id="form" autocomplete="off">
		<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" class="top">
		 <tr>
		  <td valign="top" align=right>
		        <%
		          boolean useLanguage=false;
						   
				  if("1".equals(fmss.common.cache.CacheManager.getParemerCacheMapValue(fmss.common.util.Constants.PARAM_SYS_LANGUAGE_CHANGE))){
				     useLanguage=true;
				  }	  
		        %>
		        
	            <table  height="20" border="0" cellpadding="0" cellspacing="0" style="margin-top:10px;<%if(!useLanguage)out.print("display:none;"); %>>">
			    	<tr>
			    		<td valign="bottom"><div id="di3">LANGUAGE CHOOSE：</div></td>
			    		<td>
			    			<select name="user.language" onchange="change_lan(this.value);" id="user.language" class="select">
			    				<option value="c">中文</option>
			    				<option value="e" <%if("e".equals(language))out.print("selected");%>>English</option>
			    			</select>
			    		</td>
			    	</tr>
				</table>		      
		    </td>
		  </tr>
		  <tr>
		    <td valign="top" align=center>
		      <table width="800" border="0" cellspacing="10" cellpadding="0">
			      <tr>
			        <td height="110"><%if(isHengSheng.equals("yes")){ %>
			        <img src="<c:out value="${sysTheme}"/>/img/login_c_logo.png" width="390" height="55">
			        <%}else{out.print(UIConfig.getInstance().getLoginLogoPic());} %>
			        </td>
			      </tr>
			      <tr>
			        <td>
				        <table width="100%" border="0" cellspacing="10" cellpadding="0">
					          <tr>
					            <td width="300" rowspan="2">
			
					           <img src="listLogo.ajax?pramName=LOGO1&defaultPath=<c:out value="${sysTheme}"/>/img/login_logo.png" />
					            </td>
					            <td valign="bottom"><img src="listLogo.ajax?pramName=LOGO2&defaultPath=<c:out value="${sysTheme}"/>/img/login_title.png" width="400" height="110"></td>
					          </tr>
					          <%if(isHengSheng.equals("no")){ %>
					          <tr>
					            <td valign="top" class="intro">
					                <div id='dynamic'></div> 
					            </td>
					          </tr><%} %>
				         </table>
			        </td>
			      </tr>
			      <tr>
			        <td><%if(isHengSheng.equals("no")){ %><span id="url">网址</span>：http://www.chinajesit.com<%}else{ %>&nbsp;<%} %> </td>
			      </tr>
		      </table>
		    
		    
		      <table width="560" border="0" cellpadding="0" cellspacing="10" class="login">
			       <tr>
			         <td colspan="5" id="msg">
			         	<s:if test="mess != null">
				         	<s:property value="mess"/>
				        </s:if>
			         </td>
			       </tr>
			        <tr>
			       	<td>
			         <br>
						<OBJECT ID="dtm" CLASSID="CLSID:3C532100-D810-4666-82F7-55708C52F379" codebase="<c:out value="${webapp}"/>/libPkgComm.cab#version=1,0,0,1"></OBJECT>
					 <br>
			       </td>
			       </tr>
			     
			       <tr>
			          <td width="50" nowrap="nowrap"><div id="di1">用户名</div></td>		
			          <td width="140">
			          	<input type="text" id="userId" class="input01" 
						onKeyDown="if(event.keyCode==13) event.keyCode=9" size="14" 
						dataType="Require" msg="请输入用户名。"   />
			          </td>
			          <td width="40" nowrap="nowrap"><div id="di2">密 码</div></td>
			          <td width="140">
			          	<input type="password"  id="pwd" class="input01" 
			          	 onKeyDown="if(event.keyCode==13) {FingerMatch_OnClick();}" size="14"
			          	 dataType="Require" msg="请输入密码。"  />
			          </td>		        
			          <td>
			            <input type="hidden" id="passInfo" name="passInfo" />
			          	<input name="Submit" type="button" class="input02" onclick="FingerMatch_OnClick();">
			          </td>
			       </tr>
			       <c:if test="${showTips != null && showTips == '1'}">
			       <tr><TD colspan="5">
			               <FONT size="2" id="jinggao">
			                      <%if("e".equals(language)){%>
			                     	Authorized access only. Please do not try to access this application if you are not an authorized user.
			                      <%}else{%>
			                            禁止非授权用户访问,请不要尝试攻击本平台
			                      <%}%>
			               </FONT>
			           </TD>
			       </tr>	
			       </c:if> 		        
		      </table>
		   </td>
		   
		  <tr>
			  <% if(isHengSheng.equals("no")){ %>
			    <td valign="bottom" align=center><table width="800" border="0" cellspacing="0" cellpadding="0">
			      <tr>
			        <td width="170" align="right"><img src="<c:out value="${sysTheme}"/>/img/login_logo_s.png" width="100" height="40" align="absmiddle"></td>
			        <td align="left" id="bottom"><%if("e".equals(language))out.print("Copyright &copy;2009-2013 ChinaJesIT  Tel:021-63390952 ");else out.print("上海华颉信息技术有限公司 2009&copy;版权所有 技术服务电话：021-63390952");%>
			        </td>
			      </tr>
			    </table></td>
			    <%}else{ %>
			    <td valign="bottom"><table width="800" border="0" cellspacing="10" cellpadding="0">
	           <tr>
	            <td align="right">技术提供&nbsp;</td>
	            <td width="100" align="right"><img src="<c:out value="${sysTheme}"/>/img/login_logo_s.png" width="100" height="40" align="absmiddle"></td>
	            </tr>
	            </table></td>
			    <%} %>
		  </tr>
		</table>
</form>
</body>

</html>