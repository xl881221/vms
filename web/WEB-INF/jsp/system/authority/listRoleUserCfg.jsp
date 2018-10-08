<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="fmss.common.util.MessageFormat"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../../common/include.jsp"%>
<%@include file="../../common/commen-ui.jsp"%>
</head>
<!-- 2009-07-17 14:00 ShiCH 更改事件加载机制 -->
<!-- <body onmousemove="MM(event)" onmouseout="MO(event)"> -->
<body>
<form name="frm" action="<c:out value="${webapp}"/>/viewUserByRole.action" method="post">
    <input type="hidden" name="fixQuery" id="fixQuery" value="<s:property value="fixQuery"/>"/>
    <s:if test="role.roleId != null && role.roleId != '' ">
    	<input type="hidden" name="role.roleId" value="<s:property value="role.roleId"/>"/>
    </s:if>
	<div class="centercondition1">
	<div class="widthauto">
		<table id="tbl_main" cellpadding="0" cellspacing="0">
			<tr>
				<td align="center">
					<div id="tbl_query">
						<table>
							<tr>
								<td align="left" >
								用户登录名或中文名
								 </td>
								 <td align="left" >
								 	<span><input class="tbl_query_text"  name="user.userEname" type="text" id="user.userEname" /></span>
								 </td>
								 <td align="left" >
								 	所属机构
								 </td>
								 <td align="left" >
								 	<span><input class="tbl_query_text"  name="user.instName" type="text" id="user.instName"  /></span>
								 </td>
				      			 <td align="left" >
									<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="document.forms[0].fixQuery.value='true';query();" name="BtnView" value="查询" id="BtnView" />
									<input type="button" class="tbl_query_button mleft5" onMouseMove="this.className='tbl_query_button_on mleft5'" onMouseOut="this.className='tbl_query_button mleft5'" onclick="OpenModalWindowSubmit('configUserByRole.action?role.roleId=<s:property value="role.roleId"/>',540,560,true)" name="BtnSave" value="添加" id="BtnSave" />
									<input type="button" class="tbl_query_button mleft5" onMouseMove="this.className='tbl_query_button_on mleft5'" onMouseOut="this.className='tbl_query_button mleft5'" onclick="beforeDelete('deleteUserByRole.action','userList')" name="BtnSave" value="删除" id="BtnSave" />
								 </td>
							</tr>
						</table>
					</div>	
				</td>
			</tr>
		</table>
	</div>
   <div id="lessGridList1">
        <table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" display="none" style="border-collapse: collapse; width:100%;">
            <tr  class="lessGrid head" >
                <th width="5%" style="text-align:center">
                	<input id="CheckAll" type="checkbox" style="width:13px;height:13px;" onClick="cbxselectall(this,'userList')" />
                </th>
                <th  style="text-align:center">角色名称</th>
                <th  style="text-align:center">用户登录名</th>
                <th  style="text-align:center">用户中文名</th>
                <th  style="text-align:center">所属机构</th>
                <th  style="text-align:center">所有角色</th>  
                <th  style="text-align:center">查看</th>
            </tr>

 			<s:iterator value="userList" status="stuts" id="row" var="row">
				<tr align="center" class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
				<td>
						<input type="checkbox" style="width:13px;height:13px;" name="userList" value="<s:property value="#row[0].userId"/>"/>
					
				</td>
				<td><s:property value="#row[1]"/></td>
				<td><s:property value="#row[0].userEname"/></td>
                <td><s:property value="#row[0].userCname"/></td>
                <td><s:property value="#row[0].ubaseInst.instName"/></td>
                <!--  <td><s:iterator value="uauthRole"><s:property value="authrole.roleName"/>&nbsp;&nbsp;&nbsp;</s:iterator></td>-->
                <td align="center"><a href="#" onclick="OpenModalWindow('checkRoleByUser.action?user.userId=<s:property value="#row[0].userId"/>',706,490,true)"><img src="<c:out value="${webapp}"/>/themes/images/icons/admin.png" alt="查看"
										style="border-width: 0px;" /></a></td>
                <td align="center"><a href="#" onclick="OpenModalWindow('<c:out value="${webapp}"/>/viewUser.action?user.userId=<s:property value="#row[0].userId"/>',600,310)"><img src="<c:out value="${webapp}"/>/themes/images/icons/view.png" alt="查看" style="border-width: 0px;"/></a></td>
				</tr>
			</s:iterator>
        </table>
   </div>
   <div id="anpBoud" align="Right" style="width:100%;vlign=top;">
        <table width="100%" cellspacing="0" border="0">
            <tr>
                <td align="left"></td>
                <td align="right"><s:component template="pagediv"/></td>
            </tr>
        </table>
   </div>
   </div>
</form>
</body>
<script language="javascript" type="text/javascript">
	String.prototype.trim = function() { 
		return this.replace(/(^\s*)|(\s*$)/g, ""); 
	}
	var userArray = '<s:property value="%{userArray}"/>';
	var str;
	var autoCheck = function(){
		var userArr = userArray.split(',');
		var checks = document.getElementsByName("userList");
			for (var i = 0; i < checks.length; i++) {
				if(userArray.indexOf(checks[i].value)>-1){
					checks[i].checked=true;
				}
			}
	}
	var query = function() {
		for (i = 0; i < document.forms[0].elements.length; i++) {
			var field = document.forms[0].elements[i];
			if (field == undefined) {
				continue;
			}
			if (field.type == "text" || field.type == "textarea" ) {
				field.value = field.value.trim();
			}
		}
		document.forms[0].action='viewUserByRole.action';
		document.forms[0].submit();
	}
	var openNew = function(url) {
		window.open(url,'','top=150,left=260,width=600,height=500,menubar=no,status=yes');
	}
	
	function OpenModalWindowSubmit(newURL,width,height,needReload) {
		try {
			var retData = false;
			if(typeof(width) == 'undefined'){
				width 	= screen.width * 0.9;
			}
			if(typeof(height) == 'undefined'){
				height 	= screen.height * 0.9;
			}
			if(typeof(needReload) == 'undefined'){
				needReload 	= false;
			}
			retData = showModalDialog(newURL, 
					  window, 
					  "dialogWidth:" + width
					+ "px;dialogHeight:" + height
					+ "px;center=1;scroll=1;help=0;status=0;");
			if(needReload && retData){
				DOM.$('user.userEname').value = '';
				DOM.$('user.instName').value = '';
				query();
			}
		} catch (err) {
		}
	}
	
	var displayMessage = '<%=MessageFormat.decode((String)request.getAttribute("displayMessage"))%>';
	if(displayMessage != '') alert(displayMessage);
</script>
</html>
