<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@include file="../../common/include.jsp"%>
	<link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
</head>
<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">
<form name="viewUser" method="post">
	<div id="editpanel">
	<div class="editsubpanel" style="overflow: auto; width: 100%;" >     
	<div id="editsubpanel" >
	<table id="contenttable" class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" display="none" style="border-collapse: collapse;">
		<tr>
			<th colspan="4">用户查看</th>
		</tr>
		<tr>
			<td width="15%" align="right" class="listbar">用户登录名:</td>
			<td width="35%"><s:property value="user.userEname" /></td>
			<td width="15%" align="right" class="listbar">用户中文名:</td>
			<td><s:property value="user.userCname" /></td>
		</tr>
		<tr>
			<td align="right" class="listbar">用户机构:</td>
			<td colspan=3>
		      
			    <s:property value="inst.instName" />[<s:property value="inst.instId" />]
			</td>
		</tr>
		<tr>
			<td align="right" class="listbar">座机电话:</td>
			<td><s:property value="user.tel" /></td>
			<td align="right" class="listbar">手机号:</td>
			<td><s:property value="user.mobile" /></td>
		</tr>
		<tr>
			<td align="right" class="listbar">地址:</td>
			<td><s:property value="user.address" /></td>
			<td align="right" class="listbar">邮箱:</td>
			<td><s:property value="user.email" /></td>
		</tr>
		<!--  
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
		-->
		<tr>
			<td align="right" class="listbar">描述:</td>
			<td colspan=3><s:property value="user.description" /></td>
		</tr>
	</table>	
	</div>
	</div>
	</div>
	<div  id="ctrlbutton" class="ctrlbutton"  style="border:0px">		
		<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="window.close()" name="BtnReturn" value="关闭" id="BtnReturn"/>
	</div>
</form>
</div>
</body>
</html>
