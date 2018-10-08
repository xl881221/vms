<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@include file="../../../common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%@include file="../../../common/include.jsp"%>	
	<link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
	<link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
</head>
<body>
<div class="showBoxDiv">
<form name="viewUser" method="post">
<table id="tbl_current_status">
			<tr>
				<td>
					<img src="<c:out value="${sysTheme}"/>/img/jes/icon/home.png" />
					<span class="current_status_submenu">
					<s:if test="user.changeStatus == 1 ">用户新增</s:if>
					<s:if test="user.changeStatus == 6 ">用户删除</s:if>
					</span>
				</td>
			</tr>
		</table>
	<div id="editpanel">
	<div class="editsubpanel" style="overflow: auto; width: 100%;" >     
	<div id="editsubpanel" >
	<table id="contenttable" class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" display="none" style="border-collapse: collapse;">
		<tr>
			<th colspan="4">用户查看</th>
		</tr>
		<tr>
			<td width="15%" align="right">用户登录名:</td>
			<td width="35%"><s:property value="user.userEname" /></td>
			<td width="15%" align="right">用户中文名:</td>
			<td><s:property value="user.userCname" /></td>
		</tr>
		<tr>
			<td align="right">用户机构:</td>
			<td colspan=3><s:property value="user.instName" /></td>
		</tr>
		<tr>
			<td align="right">座机电话:</td>
			<td><s:property value="user.tel" /></td>
			<td align="right">手机号:</td>
			<td><s:property value="user.mobile" /></td>
		</tr>
		<tr>
			<td align="right">地址:</td>
			<td><s:property value="user.address" /></td>
			<td align="right">邮箱:</td>
			<td><s:property value="user.email" /></td>
		</tr>
		<tr>
			<td align="right">启用日期:</td>
			<td>
				<s:if test="user.startDate != null ">
					<s:text name="format.date"><s:param value="user.startDate"/></s:text>
				</s:if>	
			</td>
			<td align="right">终止日期:</td>
			<td>
				<s:if test="user.endDate != null ">
					<s:text name="format.date"><s:param value="user.endDate"/></s:text>
				</s:if>	
			</td>
		</tr>
		<tr>
			<td align="right">描述:</td>
			<td colspan=3><s:property value="user.description" /></td>
		</tr>
			<s:if test="showAuditColumn">
			<tr>
                		<td align="right">审核人:</td>
                		<td><s:property value="user.auditUser" /></td>
                		<td align="right">审核时间:</td>
                		<td><s:date name="user.auditTime" format="yyyy-MM-dd"/> </td>
                		</tr>
                	</s:if>
	</table>	
	</div>
	</div>
	
	<div  id="ctrlbutton" class="ctrlbutton"  style="border:0px">		
		<div style="float:right;margin-right: 10px;height:25px;margin-top:5px" type="btn" img="<c:out value="${webapp}"/>/image/button/cancel.gif" onClick="window.close()" name="btnReturn" value="关闭" id="btnReturn"></div>	  	
	</div>
</form>
</div>
</body>
</html>
