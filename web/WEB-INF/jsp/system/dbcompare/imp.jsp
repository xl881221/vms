<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>����XML</title>
    <%@include file="../../common/include.jsp"%>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
	<script type="text/javascript">
		function sub(resulttype){
			var file=mytrim(document.getElementById("file").value);
			if(file==""){
				alert("��ѡ���ļ�");
				return false;
			}
			var tmp = file.substring(file.length-3,file.length);
			if(tmp!="xml"){
				alert("���ϴ�xml�ļ�");
		   		return false
			}
			try {
				var fso = new ActiveXObject("Scripting.FileSystemObject");
				var f = fso.GetFile(file.value);
				if (f.size > 10240000) {
					alert("��ѡ����ļ���С����10M��������ѡ��");
					return;
				}
			} catch (e) {}
			var filename=file.replace(/\\/g, "/");
			document.getElementById("filename").value=filename;
			
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
			
			document.getElementById("resulttype").value=resulttype;
			window.form1.submit();
		}
		function mytrim(str){
			return str.replace(/(^\s*)|(\s*$)/g, "");
		}
		
	</script>
  </head>
  
  <body>
  	<form name="form1" action="compareAction.action" method="post" enctype="multipart/form-data">
	    <table>
			<tr>
				<td>
					<img src="<c:out value="${sysTheme}"/>/img/jes/icon/home.png" />
					<span class="current_status_menu">��ǰλ�ã�</span>
					<span class="current_status_submenu">���ݿ�ȶ�<span
						class="actionIcon">-&gt;</span>����xml�ȶԵ�ǰ��</span>
				</td>
			</tr>
		</table>
	    <div id="tab_content_UPRR" class="tab_body" style=' '>
			<div class="body">
				<div align="right">
				    <table cellpadding="0" cellspacing="5" width="100%" border="0">
				    <tr>
				    	<td colspan="2" align="center"></td>
				    </tr>
				    <tr>
				    	<td align="right" style="width:40%">ѡ��xml�ļ�&nbsp;&nbsp;</td>
				    	<td style="width:60%"><input type="file" id="file" name="file" style="width:60%"></td>
				    </tr>
				    <tr>
				    	<td align="right">���ݿ�URL&nbsp;&nbsp;</td>
				    	<td align="left"><input type="text" id="url" name="url" value="" style="width:60%"></td>
				    </tr>
				    <tr>
				    	<td align="right">���ݿ��û���&nbsp;&nbsp;</td>
				    	<td align="left"><input type="text" id="username" name="username" value="" style="width:60%"></td>
				    </tr>
				    <tr>
				    	<td align="right">���ݿ�����&nbsp;&nbsp;</td>
				    	<td align="left"><input type="password" id="password" name="password" value="" style="width:60%"></td>
				    </tr>
				    
				    <tr>
				    	
				    	<td  align="right"><input type="button" value="�õ�����xml" onclick ="sub('0')"></td>
				    	<td align="left">
				    		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" value="�õ�����sql" onclick ="sub('1')">
				    		<input type="hidden" id="resultType" name="resultType" value="">
				    		<input type="hidden" id="filename" name="filename" value="">
				    	</td>
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
