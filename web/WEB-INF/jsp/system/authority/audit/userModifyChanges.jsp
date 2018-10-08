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
					<span class="current_status_submenu">用户修改</span>
				</td>
			</tr>
		</table>
	<div id="editpanel">
	<div class="editsubpanel" style="overflow: auto; width: 100%;" >     
	<div id="editsubpanel" >
	<table id="contenttable" class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" display="none" style="border-collapse: collapse;">
		<tr>
			<th width="15%" align="right">&nbsp;</th>
			<th width="15%" align="right">变更前</th>
			<th width="15%" align="right">变更后</th>
		</tr>
		<tr>
			<th width="15%" align="right">用户登录名:</td>
			<td width="35%"><s:property value="originalUser.userId" /></td>
			<td width="35%"><s:property value="user.userId" /></td>
		</tr>
		<tr>
			<th width="15%" align="right">用户中文名:</td>
			<td width="35%"><s:property value="originalUser.userCname" /></td>
			<td width="35%"><s:property value="user.userCname" /></td>
		</tr>
		<tr>
			<th align="right">用户机构:</th>
			<td ><s:property value="originalUser.instName" /></td>
			<td ><s:property value="user.instName" /></td>
		</tr>
		<tr>
			<th align="right">座机电话:</th>
			<td><s:property value="originalUser.tel" /></td>
			<td><s:property value="user.tel" /></td>
		</tr>
		<tr>
			<th align="right">手机号:</th>
			<td><s:property value="originalUser.mobile" /></td>
			<td><s:property value="user.mobile" /></td>
		</tr>
		<tr>
			<th align="right">地址:</th>
			<td><s:property value="originalUser.address" /></td>
			<td><s:property value="user.address" /></td>
		</tr>
		<tr>
			<th align="right">邮箱:</th>
			<td><s:property value="originalUser.email" /></td>
			<td><s:property value="user.email" /></td>
		</tr>
		
		<tr>
			<th align="right">启用日期:</th>
			<td>
				<s:if test="user.startDate != null ">
					<s:text name="format.date"><s:param value="originalUser.startDate"/></s:text>
				</s:if>	
			</td>
			<td>
				<s:if test="user.startDate != null ">
					<s:text name="format.date"><s:param value="user.startDate"/></s:text>
				</s:if>	
			</td>
		</tr>
		<tr>
			<th align="right">终止日期:</th>
			<td>
				<s:if test="user.endDate != null ">
					<s:text name="format.date"><s:param value="originalUser.endDate"/></s:text>
				</s:if>	
			</td>
			<td>
				<s:if test="user.endDate != null ">
					<s:text name="format.date"><s:param value="user.endDate"/></s:text>
				</s:if>	
			</td>
		</tr>
		
		<tr>
			<th align="right">描述:</th>
			<td ><s:property value="originalUser.description" /></td>
			<td ><s:property value="user.description" /></td>
		</tr>
			<tr>
                		<th align="right">修改备注:</td>
                		<td><s:property value="user.changeRemark" /></td>

                	</tr>
		<s:if test="showAuditColumn">
			<tr>
                		<th align="right">审核人:</td>
                		<td><s:property value="user.auditUser" /></td>
                		</tr>
                		<tr>
                		<th align="right">审核时间:</td>
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
