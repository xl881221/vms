<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@include file="/WEB-INF/jsp/common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@include file="/WEB-INF/jsp/common/include.jsp"%>
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
<div style="overflow: auto; width: 100%;" >    
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
	<tr>
		<th colspan="4">子系统详细信息</th>
	</tr>
	<tr>
		<td class="contnettable-subtitle" colspan="10">系统基本信息</td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right">系统中文名:</td>
		<td width="35%"><s:property value="subSystem.systemCname" /></td>
		<td width="15%" style="text-align: right">系统中文简称:</td>
		<td><s:property value="subSystem.systemNickCname" /></td>
	</tr>
	<tr>
		<td width="15%" style="text-align: right">系统英文名:</td>
		<td width="35%" ><s:property value="subSystem.systemEname" /></td>
		<td width="15%" style="text-align: right">系统英文简称:</td>
		<td><s:property value="subSystem.systemNickEname" /></td>
	</tr>
	<tr>
		<td style="text-align: right">是否显示:</td>
		<td><s:if test="subSystem.display=='true'">是</s:if><s:else>否</s:else>
		</td>
		<td style="text-align: right">是否启用:</td>
		<td><s:if test="subSystem.enabled=='true'">是</s:if><s:else>否</s:else></td>
	</tr>
	<!-- 
	<tr>
		<td class="contnettable-subtitle" colspan="10">数据库配置</td>
	</tr>
	<tr>
		<td style="text-align: right">数据库驱动:</td>
		<td  colspan="9"><input type="text" style="width:200px;border:0px; border-style:none" value="<s:property value='subSystem.dbDriverClass' />" /></td>
	</tr>
	<tr>
		<td style="text-align: right">数据库链接:</td>
		<td colspan="9"><input type="text" style="width:200px;border:0px;border-style:none" value="<s:property value='subSystem.dbUrl' />"/></td>
	</tr>
	<tr>
		<td style="text-align: right">数据库用户名:</td>
		<td><s:property value="subSystem.dbUserId" /></td>

		<td style="text-align: right">数据库密码:</td>
		<td><input type="password" style="border: none;background: inherit" disabled="disabled" value="<s:property value="subSystem.dbPassword" />" /></td>
	</tr>
	 -->
	<tr>
		<td class="contnettable-subtitle" colspan="10">系统菜单配置</td>
	</tr>
	<tr>
		<td width="100" style="text-align: right">菜单序号:</td>
		<td><s:property value="subSystem.menuOrderNum" /></td>
		<td style="text-align: right">菜单表名:</td>
		<td><s:property value="subSystem.menuTable" /></td>
	</tr>
	<tr>
		<td style="text-align: right">系统外网地址:</td>
		<td><s:property value='subSystem.linkSiteUrl' /></td>
		<td style="text-align: right">菜单图标:</td>
		<td>
			<span style="width:16px;height:16px;background-image:url('<c:out value="${webapp}"/>/image/system/<s:property value="subSystem.menuImgSrc" />')"></span>
		</td>
	</tr>
	<tr>
		<td style="text-align: right">系统内网地址:</td>
		<td><s:property value='subSystem.linkSiteInnerUrl' /></td>
		<td style="text-align: right"></td>
		<td>
		</td>
	</tr>
	
</table>
</div>
</div>
<div id="ctrlbutton" class="ctrlbutton" style="border:0px">		
	<div tyle="float:right;margin-right: 10px;height:25px;margin-top:5px" type="btn" img="<c:out value="${webapp}"/>/image/button/cancel.gif" onclick="CloseWindow()" name="BtnReturn" value="关闭" id="BtnReturn"></div>
</div>
</div>
</form>
</div>
</body>
</html>
