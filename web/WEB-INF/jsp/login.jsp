<%@ page language="java" import="fmss.common.cache.CacheManager,com.jes.core.api.servlet.ServletHelper" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="fmss.common.config.UIConfig"%>
<%@page import="java.util.Date"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<%@include file="common/include.jsp"%> 
<% Object language=request.getParameter("user.language");
   if(language==null)
   	  language=ServletHelper.getCookieValue(request,"USYS_USER_LANGUAGE");
   String passKey=new Long(new Date().getTime()).toString();
   ServletHelper.setCookie(response,"USYS_PASSKEY",passKey);
%>
<title>增值税管理平台</title>

<!-- Libraries -->
		<link type="text/css" href="<c:out value="${webapp}"/>/themes/css/login.css" rel="stylesheet" />	
		<link type="text/css" href="<c:out value="${webapp}"/>/wide/css/smoothness/jquery-ui-1.7.2.custom.html" rel="stylesheet" />	
		
		<script type="text/javascript" src="<c:out value="${webapp}"/>/wide/js/jquery-1.3.2.min.js"></script>
		<script type="text/javascript" src="<c:out value="${webapp}"/>/wide/js/easyTooltip.js"></script>
		<script type="text/javascript" src="<c:out value="${webapp}"/>/wide/js/jquery-ui-1.7.2.custom.min.js"></script>

		<script src="<c:out value="${webapp}"/>/js/login.js" type="text/javascript" charset="gb2312"></script>
		<script type="text/javascript" src="<c:out value="${webapp}"/>/dwr/engine.js"></script>
		<script type="text/javascript" src="<c:out value="${webapp}"/>/dwr/util.js"></script>
		<script type="text/javascript" src="<c:out value="${webapp}"/>/dwr/interface/dwrAsynService.js"></script>
		
		<%@include file="common/commen-ui.jsp"%>
<!-- End of Libraries -->	

<script language="javascript">	
var PASSKEY="<%=passKey%>";
 

</script>

</head>

<body class="white" scrolling="no" >
	 <% String isHengSheng="no"; 
		if(pageContext.getAttribute("sysTheme").toString().indexOf("green")>-1){
		isHengSheng = "yes";
		} 
	%>

	<div class="logo_login">
		<div class="logo_box"><img src="<c:out value="${webapp}"/>/themes/images/temp02.png"/></div>
	</div>
	<div class="login_banner">
		<div class="login_inner">
			<div id="box" style="float:right;">
				<div class="loginbox">
					<b><img src="<c:out value="${webapp}"/>/themes/images/login.png"/></b>
					<s:form method="post" action="login.action">
					<div style="display:none">
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
			   			<select name="user.language" onchange="change_lan(this.value);" id="user.language" class="select">
			   				<option value="c">中文</option>
			   				<option value="e" <%if("e".equals(language))out.print("selected");%>>English</option>
			   			</select>
					</div>
					<div class="spaceMsg" id="msg">
					 		 
						 
					         	<s:if test="mess != null">
						         	<s:property value="mess"/>
						        </s:if>
		
						 
					</div>
					
						
					<div class="main">
						<div class="username">
		                	<div class="usernameinner">
		                    	<input name="userId" id="userId"  /> 
		                    </div>
		                </div>
		                <div class="password">
		                	<div class="passwordinner">
		                    	<input type="password" name="pwd" id="pwd" >	
		                    </div>
		                </div>
					</div>
		
					<p class="space">
						<input type="hidden" id="passInfo" name="passInfo" />
						<button onclick="login()">登录</button>
					</p>
					<p class="space">
					 		
					         <c:if test="${showTips != null && showTips == '1'}">
		
					               <span size="2" id="jinggao">
					                      <%if("e".equals(language)){%>
					                     	Authorized access only. Please do not try to access this application if you are not an authorized user.
					                      <%}else{%>
					                            禁止非授权用户访问,请不要尝试攻击本平台
					                      <%}%>
					               </span>
		
					       		</c:if> 		  
					</p>
					 </s:form>
				 </div>
			</div>
			<div><img src="<c:out value="${webapp}"/>/themes/images/loginbg.png"/></div>
		</div>
	<div id="container">
<!--		<div class="logo">-->
<!--			<ul class="iconflag">-->
<!--				<li><img src="<c:out value="${webapp}"/>/wide/assets/1859.png"/></li>-->
<!--				<li><img src="<c:out value="${webapp}"/>/wide/assets/1863.png"/></li>-->
<!--				<li><img src="<c:out value="${webapp}"/>/wide/assets/1811.png"/></li>-->
<!--				<li><img src="<c:out value="${webapp}"/>/wide/assets/1868.png"/></li>-->
<!--			</ul>-->
<!--			<a href="#"><img src="<c:out value="${webapp}"/>/wide/assets/logo.png" alt="" /></a>-->
<!--		</div>-->
		
	</div>
	</div>
	<div id="footer">
		Copyright © 中科软科技股份有限公司 版权所有
	</div>
	
	
   
</body>

</html>