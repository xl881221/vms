<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <%@include file="../../common/include.jsp"%>
    <title>����XML</title>
    <meta http-equiv='Expires' content='0'>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
	
	<script type="text/javascript">
		function exp(){
			var url=document.getElementById("url");
			var username=document.getElementById("username");
			var psd=document.getElementById("password");
			if(mytrim(url.value)==""){
				alert("URL����Ϊ��");
				url.focus();
				return false;
			}
			if(mytrim(username.value)==""){
				alert("�û�������Ϊ��");
				username.focus();
				return false;
			}
			window.form1.submit();
		}
		function mytrim(str){
			return str.replace(/(^\s*)|(\s*$)/g, "");
		}
		
	</script>
  </head>
  
  <body>
  	<form name="form1" action="expAction.action">
	    <table>
			<tr>
				<td>
					<img src="<c:out value="${sysTheme}"/>/img/jes/icon/home.png" /> 
					<span class="current_status_menu">��ǰλ�ã�</span>
					<span class="current_status_submenu">���ݿ�ȶ�<span
						class="actionIcon">-&gt;</span>��������Ϣxml</span>
				</td>
			</tr>
		</table>
	    <div id="tab_content_UPRR" class="tab_body" style=' '>
			<div class="body">
				<div align="right">
				    <table cellpadding="0" cellspacing="5" width="100%" border="0" >
				    <tr>
				    	<td colspan="2" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ע:����xml��Ҫ�ṩӵ��dbaȨ�޵��û���,����</td>
				    </tr>
				    <tr>
				    	<td align="right" style="width:40%">���ݿ�URL&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				    	<td align="left" style="width:60%"><input type="text" id="url" name="url" value="" style="width:60%"></td>
				    </tr>
				    <tr>
				    	<td align="right">���ݿ��û���&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				    	<td align="left"><input type="text" id="username" name="username" value="" style="width:60%"></td>
				    </tr>
				    <tr>
				    	<td align="right">���ݿ�����&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
				    	<td align="left"><input type="password" id="password" name="password" value="" style="width:60%"></td>
				    </tr>
				    <tr>
				    	<td align="right"></td>
				    	<td align="left"><input type="button" value="��&nbsp;&nbsp;��" onclick ="exp()"></td>
				    </tr>
				    
				    <tr>
				    	<td colspan="2" align="center">
				    	<%
				    		String err=(String)request.getAttribute("errmsg");
				    		if(err!=null){
				    			out.println(err);
				    		}
				    	 %>
				    	</td>
				    </tr>
				    </table>
				</div>
			</div>
		</div>
    </form>
  </body>
</html>
