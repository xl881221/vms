<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@include file="/WEB-INF/jsp/common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/WEB-INF/jsp/common/include.jsp"%>
<link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css"
	rel="stylesheet">

</head>
<body>
<div class="showBoxDiv">


<form name="frm" action="<c:out value='${webapp}'/>/system/baseinfo/setAdmin.action"
	method="post">

<div id="editpanel">
<div id="editsubpanel" class="editsubpanel">
<div style="overflow: auto; width: 100%;" >  
<s:hidden name="subSystem.systemId"></s:hidden>
<table  id="contenttable" class="lessGrid" cellspacing="0"
	width="100%" align="center" cellpadding="0">
	<tr>
		<th colspan="4">配置管理员
		</th>
	</tr>
	<tr>
	<td><div align="center" id="optiontransferselectbtn">
		<style type="text/css">
		/* add list button style.  ShiCH 2009-08-13*/
		input {
			font:normal 12px SimSun !important;
		}
		</style>
		
			<!--<s:optiontransferselect 
				leftTitle="所有用户"
				name="allUsers"
				list="allUsers"
				multiple="true"
				listKey="userId"
				listValue="userCname" 
				cssStyle="height:300;width:260"
				rightTitle="管理员"
				doubleName="newAdmins"
				doubleList="systemAdmins"
				doubleListKey="userId"
				doubleListValue="userCname" 
				doubleMultiple="true"
				doubleCssStyle="height:300;width:260"
				addToRightLabel="=>"
				addToLeftLabel="<="
				allowAddAllToLeft="false"
				allowAddAllToRight="false"
				allowSelectAll="false"
				allowUpDownOnLeft="false"
				allowUpDownOnRight="false"/>-->
				
			<script type="text/javascript" src="<c:out value='${webapp}'/>/js/struts2/optiontransferselect.js"></script>
			<table border="0">
			<tr>
			<td>
				<label for="leftTitle">所有用户</label><br />
			<select name="allUsers" size="15" id="allUsers" style="height:300;width:260" multiple="multiple">
				<s:iterator value="allUsers" status="st">
				    <option value="<s:property value="userId" />"><s:property value="userCname" /></option>
				</s:iterator>
			
			</select>
			</td>
			<td>
				<label for="rightTitle">管理员</label><br />
					<select 
						name="newAdmins"
						size="15"		
						multiple="multiple"
						id="newAdmins"
						style="height:300;width:260">
						<s:iterator value="systemAdmins" status="st">
						    <option value="<s:property value="userId" />"><s:property value="userCname" /></option>
						</s:iterator>
					</select>
			</td>
			</tr>
			</table>
				</div></td>
	</tr>
</table>
</div>
</div>
<div id="ctrlbutton" class="ctrlbutton" style="border:0px;">
<div  style="margin-left:5px" type="btn" img="<c:out value="${webapp}"/>/image/button/cancel.gif" onclick="CloseWindow()" name="BtnReturn" value="关闭" id="BtnReturn"></div>
</div>
</form>
</div>
</body>
</html>
