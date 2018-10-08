<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../../common/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<title>资源表单</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<c:out value='${sysTheme}'/>/css/subWindow.css" type="text/css"
	rel="stylesheet">
<script language="javascript" type="text/javascript">
		
		var page = {};
		
		page.submitForm = function(obj){
			return page.checkRoleName();
		}
		
		page.checkRoleName = function(){
			var roleName = document.getElementById("role.roleName");
			if(roleName==null || roleName==''){
				alert("角色名不能为空!");
				return false;
			}
			return true;
		}
		
		function formSubmit(){
			document.forms[0].submit();
			parent.frames["listRoleTreeFrame"].location.reload();
		}
	</script>
<script language="javascript" type="text/javascript">
		var msg = '<s:property value='%{isSuccess}'/>';
		if(msg=='success'){
			alert('保存成功');
			//window.opener.parent.frames["listRoleMainFrame"].location.href="editConfigRes.action?roleId=${role.roleId}";
			window.opener.parent.frames["listRoleMainFrame"].location.reload();
			window.close();
		}else if(msg=='fail'){
			alert('保存失败');
		}
	</script>
</head>
<body>
<form name="frm" action="<c:out value='${webapp}'/>/saveSimpleResCfg.action"
	method="post" onSubmit="return page.submitForm(this)"><s:if
	test="authResMap.resId != ''&& authResMap.resId != null">
	<input type="hidden" name="authResMap.resId"
		value="${authResMap.resId}" />
</s:if>
<div id="editpanel" class="editpanel">
<div id="editsubpanel" class="editsubpanel">
<table id="contenttable" class="contenttable" cellspacing="0"
	width="100%" align="center" cellpadding="0">
	<tr>
		<th colspan="4">查看资源</th>
	</tr>

	<tr>
		<td align="right">子系统<span class="spanstar">*</span></td>
		<td><s:if test="view==true">
			<s:select list="sysList" name="authResMap.systemId"
				listKey="systemId" listValue="systemCname"
				value="authResMap.systemId" theme="simple" disabled="true" />
		</s:if> <s:else>
			<s:select list="sysList" name="authResMap.systemId"
				listKey="systemId" listValue="systemCname"
				value="authResMap.systemId" theme="simple" />
		</s:else></td>
		<td align="right">资源类型<span class="spanstar">*</span></td>
		<td><s:if test="view==true">
			<s:select list="resTypeDic" name="authResMap.resType"
				listKey="dicValue" listValue="dicName" value="authResMap.resType"
				theme="simple" disabled="true" />
		</s:if> <s:else>
			<s:select list="resTypeDic" name="authResMap.resType"
				listKey="dicValue" listValue="dicName" value="authResMap.resType"
				theme="simple" />
		</s:else></td>
	</tr>
	<tr>
		<td align="right">资源ID<span class="spanstar">*</span></td>
		<td><input name="authResMap.resId" type="text"
			value="${authResMap.resId}" id="authResMap.resId" dataType="LimitB"
			min="1" max="50" msg="" disabled="disabled" /></td>

		<td align="right">资源名<span class="spanstar">*</span></td>
		<td><input name="authResMap.resName" type="text"
			value="${authResMap.resName}" id="authResMap.resName"
			dataType="LimitB" min="1" max="50" msg=""
			<s:if test="view==true">disabled="disabled"</s:if> /></td>
	</tr>

	<tr>
		<td align="right">来源表<span class="spanstar">*</span></td>
		<td><input name="authResMap.srcTable" type="text"
			value="${authResMap.srcTable}" id="authResMap.srcTable" dataType="LimitB"
			min="1" max="50" msg="" disabled="disabled" /></td>

		<td align="right">&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td align="right">源ID<span class="spanstar">*</span></td>
		<td><input name="authResMap.srcId" type="text"
			value="${authResMap.srcId}" id="authResMap.srcId" dataType="LimitB"
			min="1" max="50" msg="" disabled="disabled" /></td>

		<td align="right">源名字段<span class="spanstar">*</span></td>
		<td><input name="authResMap.srcField" type="text"
			value="${authResMap.srcField}" id="authResMap.srcField"
			dataType="LimitB" min="1" max="50" msg=""
			<s:if test="view==true">disabled="disabled"</s:if> /></td>
	</tr>
	<tr>
		<td align="right">资源描述</td>
		<td colspan="3"><input style="width: 90%;"
			name="authResMap.description" type="text"
			value="${authResMap.description}" id="authResMap.description"
			dataType="LimitB" min="1" max="50" msg=""
			<s:if test="view==true">disabled="disabled"</s:if> /></td>
	</tr>
</table>
</div>
</div>
<div id="ctrlbutton" class="ctrlbutton"><s:if test="view==false">
	<input type="button" name="BtnSave" value="保存" id="BtnSave"
		onclick="formSubmit()" />
</s:if> <input type="button" onClick="window.close()" name="BtnReturn"
	value="关闭" id="BtnReturn" /></div>
</form>
</body>
</html>