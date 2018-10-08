<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@include file="../../common/include.jsp"%>
	<script language="javascript">
		var titleStr = "子系统表单" + new Array(1000).join("&nbsp;");
		document.write("<title>"+titleStr+"</title>");
	</script>
	<link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
</head>
<body scroll="no" style="overflow:hidden;">
	<div class="showBoxDiv">
	<form name="frm" action="<c:out value='${webapp}'/>/createSystem.action" method="post" enctype="multipart/form-data" >
	<div id="editpanel">
		<div id="editsubpanel" class="editsubpanel">
		<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
		<tr>
			<th colspan="4">子系统详细信息</th>
		</tr>
		<tr>
			<td class="contnettable-subtitle" colspan="10">系统基本信息</td>
		</tr>
		<tr>
			<td width="15%" style="text-align:right" class="listbar">系统中文名:</td>
			<td width="35%"><s:property value="subSystem.systemCname" /></td>
			<td width="15%" style="text-align:right" class="listbar">系统中文简称:</td>
			<td><s:property value="subSystem.systemNickCname" /></td>
		</tr>
		<tr>
			<td width="15%" style="text-align:right" class="listbar">系统英文名:</td>
			<td width="35%" ><s:property value="subSystem.systemEname" /></td>
			<td width="15%" style="text-align:right" class="listbar">系统英文简称:</td>
			<td><s:property value="subSystem.systemNickEname" /></td>
		</tr>
		<tr>
			<td style="text-align:right" class="listbar">显示方式:</td>
			<td>
				<s:if test="subSystem.display=='true'">全显示</s:if>
				<s:elseif test="operType=='menu'">仅菜单显示</s:elseif>
				<s:elseif test="operType=='left'">待处理问题显示</s:elseif>
				<s:else>不显示</s:else>
			</td>
			<td style="text-align:right" class="listbar">是否启用:</td>
			<td>
				<s:if test="subSystem.enabled=='true'">是</s:if><s:else>否</s:else>
			</td>
		</tr>
		<!--
		<tr>
			<td class="contnettable-subtitle" colspan="10">数据库配置</td>
		</tr>
		<tr>
			<td style="text-align:right">数据库驱动:</td>
			<td colspan="9"><input type="text" style="width:200px;border:0px; border-style:none" value="<s:property value='subSystem.dbDriverClass' />" /></td>
		</tr>
		<tr>
			<td style="text-align:right">数据库链接:</td>
			<td colspan="9"><input type="text" style="width:200px;border:0px;border-style:none" value="<s:property value='subSystem.dbUrl' />"/></td>
		</tr>
		<tr>
			<td style="text-align:right">数据库用户名:</td>
			<td><s:property value="subSystem.dbUserId" /></td>
			<td style="text-align:right">数据库密码:</td>
			<td><input type="password" style="border:none;background:inherit" disabled="disabled" value="<s:property value="subSystem.dbPassword" />" /></td>
		</tr>
		-->
		<tr>
			<td class="contnettable-subtitle" colspan="10">系统菜单配置</td>
		</tr>
		<tr>
			<td width="100" style="text-align:right" class="listbar">菜单序号:</td>
			<td><s:property value="subSystem.menuOrderNum" /></td>
			<td style="text-align:right" class="listbar">菜单表名:</td>
			<td><s:property value="subSystem.menuTable" /></td>
		</tr>
		<tr>
			<td style="text-align:right" class="listbar">系统外网地址:</td>
			<td colspan="3"><s:property value="subSystem.linkSiteUrl" /></td>
		</tr>
		<tr>
			<td style="text-align:right" class="listbar">系统内网地址:</td>
			<td colspan="3"><s:property value="subSystem.linkSiteInnerUrl" /></td>
		</tr>
		</table>
		</div>
		<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
			<input type="button" class="tbl_query_button"
				onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
				onclick="CloseWindow()" name="BtnReturn" value="关闭" id="BtnReturn"/>
		</div>
	</div>
	</form>
	</div>
</body>
</html>