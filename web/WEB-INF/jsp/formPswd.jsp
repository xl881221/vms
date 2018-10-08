<%@ page language="java" import="com.jes.core.api.servlet.ServletHelper" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="fmss.common.util.SecurityPassword"%>

<%@include file="common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<% Object language=ServletHelper.getCookieValue(request,"USYS_USER_LANGUAGE");%>	
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="common/include.jsp"%>
<title><%if("e".equals(language))out.print("Modify Password");else out.print("修改密码");%></title>
<link type="text/css" href="<c:out value="${sysTheme}"/>/css/subWindow.css" rel="stylesheet">
<script type="text/javascript" src="<c:out value="${webapp}"/>/dwr/engine.js"></script>
<script type="text/javascript" src="<c:out value="${webapp}"/>/dwr/util.js"></script>
<script type="text/javascript" src="<c:out value="${webapp}"/>/dwr/interface/dwrAsynService.js"></script>
<script language="javascript" type="text/javascript">
<!--
	function lengthCheck(text, size) {
		var len = 0;
		for(var i=0; i < text.length; i++) {
			var ech = escape(text.charAt(i));
			if (ech.length > 4)
				len++;
			len++;
		}
		if (len > size)
			return true;
		return false;
	}
	
	var isSubmit = false;
	function findOutSubmit() {
		if (isSubmit) return;
		var obj = document.getElementById("newPwd");
		if (obj.value != document.getElementById("newPwd2").value) {var m = new MessageBox(obj);m.Show("<%if("e".equals(language))out.print("Two input password is not the same");else out.print("两次输入的密码不一样");%>");return false;}
		isSubmit = true;
		dwrAsynService.updatePWD(document.getElementById("oldPwd").value, document.getElementById("newPwd").value, 
			function (map) {
			//alert(map.rel);
				switch (map.rel) {
					case '0': alert("<%if("e".equals(language))out.print("fail");else out.print("保存失败");%>！"); isSubmit = false; break;
					case '1': alert("<%if("e".equals(language))out.print("success");else out.print("保存成功");%>！"); isSubmit = false;  var call = window.dialogArguments; call();window.returnValue = true;window.close(); break;
					case '2': var m = new MessageBox(obj); m.Show("<%if("e".equals(language))out.print("Password can not be empty");else out.print("密码不能为空");%>");isSubmit = false; break;
					case '3': var m = new MessageBox(obj); m.Show("<%if("e".equals(language))out.print("The password can only contain letters, numbers and“ _! @ # $” and other characters");else out.print("密码中只能包含大小写字母、数字和“_!@#$”等字符");%>"); isSubmit = false;break;
					case '7': var m = new MessageBox(obj); m.Show("<%if("e".equals(language))out.print("The password does not meet specification");else out.print("密码不符合规范");%> : <br>" + map.message, 4000); isSubmit = false;break;
					case '8': var m = new MessageBox(obj); m.Show("<%if("e".equals(language))out.print("Password cannot contain user information");else out.print("密码不能包含用户名信息");%>"); isSubmit = false;break;
					case '4': var m = new MessageBox(obj); m.Show("<%if("e".equals(language))out.print("Password length shall be");else out.print("密码长度应在");%>"+map.min+"<%if("e".equals(language))out.print(" to ");else out.print("到");%>"+map.max+"<%if("e".equals(language))out.print("");else out.print("位之间");%>"); isSubmit = false;break;
					case '5': alert("<%if("e".equals(language))out.print("Today you have modified the code");else out.print("您今天已经修改过密码了");%>！"); isSubmit = false;break;
					case '6': 
						new MessageBox(document.getElementById("oldPwd")).Show("<%if("e".equals(language))out.print("The old password is incorrect");else out.print("旧密码不正确");%>");
						document.getElementById("oldPwd").focus();
						isSubmit = false; break;
					default: 
						if (map.rel < 0) {
							var m = new MessageBox(obj); 
							m.Show("<%if("e".equals(language))out.print("Password in ");else out.print("密码曾在最近");%>" + (-map.rel) + "<%if("e".equals(language))out.print(" recent used");else out.print("次使用过");%>");
						}
						isSubmit = false;
						break;
				}
			}
		);
	}
	
	document.onkeyup = function () {
		if (event.keyCode == 13) findOutSubmit();
	}
-->
</script>
</head>
<body>
	<div class="showBoxDiv">
		<div id="editpanel">
			<div id="editsubpanel" class="editsubpanel">
				<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
					<tr class="header">
						<th colspan="2"><%if("e".equals(language))out.print("Modify Password");else out.print("密码修改");%></th>
					</tr>
					<tr>
						<td width="30%" align="right" class="listbar"><%if("e".equals(language))out.print("Old Password");else out.print("旧密码");%>：</td>
						<td><input type="password" name="oldPwd" id="oldPwd" size="30">&nbsp;<span class="spanstar">*</span>
					</tr>
					<tr>
						<td align="right" class="listbar"><%if("e".equals(language))out.print("New Password");else out.print("新密码");%>：</td>
						<td><input type="password" name="newPwd" id="newPwd" size="30">&nbsp;<span class="spanstar">*</span>
					</tr>
					<tr>
						<td align="right" class="listbar"><%if("e".equals(language))out.print("New Password");else out.print("新密码");%>：</td>
						<td><input type="password" name="newPwd2" id="newPwd2" size="30">&nbsp;<span class="spanstar">*</span>
					</tr>
				</table>
			</div>
		</div>
		<div id="ctrlbutton" class="ctrlbutton">
			<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="findOutSubmit()" value="<%if("e".equals(language))out.print("save");else out.print("保存");%>"/>
			<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="window.close()" value="<%if("e".equals(language))out.print("close");else out.print("关闭");%>"/>
		</div>
		<span id="ruleTIP"><%if("e".equals(language))out.print("rule");else out.print("密码规则");%>：
			<%if(!"e".equals(language))out.print(SecurityPassword.securityMessage(","));else out.print("must contain lowercase letters,must contain capitals,must contain numbers,must contain @#$%");%>
		</span>
	</div>
</body>
</html>
<script language="javascript" type="text/javascript">
	dwrAsynService.checkPswIsUsing(function(a){if(a==false)DOM.$("ruleTIP").style.display="none";});
</script>