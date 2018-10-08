<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@include file="common/include.jsp"%>
<title></title>
    <link href="<c:out value="${webapp}"/>/theme/db/css/login.css" rel="stylesheet" type="text/css" />
	
    <style type="text/css">
    	.errClass {
    		font-size: 12px;
			font-weight: bold;
			font-color: #00ff00;
    	}
    </style>
	<script src="<c:out value="${webapp}"/>/js/png.js"  type="text/javascript"></script>
	<script src="<c:out value="${webapp}"/>/js/login.js" type="text/javascript"></script>
</head>
<body >
 <% String isHengSheng="no"; 
if(pageContext.getAttribute("sysTheme").toString().indexOf("green")>-1){
	isHengSheng = "yes";
} %>
	<s:form method="post" action="login.action">
		<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" class="top">
		  <tr>
		    <td valign="top" ><table width="100%" border="0" cellspacing="10" cellpadding="0" height="480">
      <tr>
        <td height="50">&nbsp;</td>
      </tr>
      <tr>
        <td><table width="100%" border="0" cellspacing="10" cellpadding="0">
          <tr>
            <td width="300"></td>
            <td valign="middle"></td>
          </tr>
          
        </table></td>
      </tr>
      <tr>
        <td>&nbsp;</td>
      </tr>
    </table>
		    
		    <table width="560" border="0" cellpadding="0" cellspacing="10" class="login">
		       <tr style="display:none">
		         <td colspan="5" id="msg">
		         	<s:if test="mess != null">
		         		<s:property value="mess"/>
		         	</s:if>
		         </td>
		       </tr>
		       <tr>
		          <td width="50" nowrap="nowrap">用户名</td>		
		          <td width="140">
		          	<input type="text" name="user.userId" id="userId" class="input01" 
					onKeyDown="if(event.keyCode==13) event.keyCode=9" size="14" 
					dataType="Require" msg="请输入用户名。"  />
		          </td>
		          <td width="40" nowrap="nowrap">密 码</td>
		          <td width="140">
		          	<input type="password" name="user.password" id="pwd" class="input01" 
		          	 onKeyDown="if(event.keyCode==13) {login();}" size="14"
		          	 dataType="Require" msg="请输入密码。"  />
		          </td>		        
		          <td>
		          	<input name="Submit" type="button" class="input02" onclick="login()">
		          </td>
		       </tr>
		       <c:if test="${showTips != null && showTips == '1'}">
		       <tr><TD colspan="5"><FONT size="2">Authorized access only. Please do not try to access this application if you are not an authorized user.</FONT></TD></tr>	</c:if> 		        
		     </table>
		      </td>
		  </tr>
		  <tr>
		  <% if(isHengSheng.equals("no")){ %>
		    <td valign="bottom" align=center><table width="800" border="0" cellspacing="30" cellpadding="0">
		      <tr>
		        <td width="170" align="right"><img src="<c:out value="${sysTheme}"/>/img/login_logo_s.png" width="100" height="40" align="absmiddle"></td>
		        <td align="left">Copyright &copy;2009-2013 ChinaJesIT  Tel:021-63390952 </td>
		      </tr>
		    </table></td>
		    <%}else{ %>
		    <td valign="bottom"><table width="1000" border="0" cellspacing="10" cellpadding="0">
           <tr>
            <td align="right">技术提供&nbsp;</td>
            <td width="100" align="right"><img src="<c:out value="${sysTheme}"/>/img/login_logo_s.png" width="100" height="40" align="absmiddle"></td>
            </tr>
            </table></td>
		    <%} %>
		  </tr>
		</table>
    </s:form>
</body>
</html>