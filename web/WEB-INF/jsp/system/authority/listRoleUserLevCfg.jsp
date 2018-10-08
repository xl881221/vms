<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@include file="../../common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%@include file="../../common/include.jsp"%>
    <link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
</head>
<body scroll="no" style="overflow:hidden;">
    <form name="frm" action="<c:out value='${webapp}'/>/saveUserByRole.action" method="post">
    <table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
    <tr><td class="centercondition">
    <s:if test="role.roleId != null && role.roleId != '' ">
    	<input type="hidden" name="role.roleId" value="<s:property value="role.roleId"/>"/>
    </s:if>
   <table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
   	<tr>
		<th colspan="4">角色人员配置</th>
	</tr>
	<tr align="left">
		<td>用户名：<input name="user.userEname" type="text" id="user.userEname"  style="width: 100;"  value="<s:property value="user.userEname"/>"/>&nbsp;&nbsp;&nbsp;
	  	所属机构：<input name="user.instName" type="text" id="user.instName"  style="width: 100;" value="<s:property value="user.instName"/>"/>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="query()" name="BtnView" value="查询" id="BtnView" class="input_button"/>
		</td>	
	</tr>
	</table>
   
  <div style="overflow: auto; width: 100%; height: 100%;" id="lessGridListTemp">
       <table  class="lessGridTemp" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse;">
            <tr class="lessGridTemp headTemp">
                <th width="3%" style="text-align: center;">
                	<input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="cbxselectall(this,'userList')" />
                </th>
                <th style="text-align: center;">用户登陆名</th>
                <th style="text-align: center;">用户中文名</th>
                <th style="text-align: center;">所属机构</th>
                <th style="text-align: center;">查看</th>
            </tr>

 			<s:iterator value="userList" status="stuts" id="row">
				<tr align="center" class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
				<td align=center>
						<input type="checkbox" style="width:13px;height:13px;" name="userList" value="<s:property value="userId"/>"/>
				</td>
				<td><s:property value="userEname"/></td>
                <td><s:property value="userCname"/></td>
                <td><s:property value="ubaseInst.instName"/></td>
                <td align="center"><a href="#" onclick="OpenModalWindow('<c:out value="${webapp}"/>/viewUser.action?user.userId=<s:property value="userId"/>',600,310,270,600)"><img src="<c:out value="${sysTheme}"/>/img/jes/icon/view.png" alt="查看" style="border-width: 0px;"/></a></td>
				</tr>
			</s:iterator>
        </table>
        </div>
    <div id="anpBoud" align="Right" style="width:100%;">
        <table width="100%" cellspacing="0" border="0">
            <tr>
                <td align="left"></td>
                <td align="right"><ww:component template="pagediv"/></td>
            </tr>
        </table>
    </div>
    <div id="ctrlbutton" class="ctrlbutton" align="right" style="border:0px">
    	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"  onclick="saveCfg()" name="BtnSave" value="保存" id="BtnSave" class="input_button"/>
	    <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"  onclick="window.close()" name="BtnSave" value="关闭" id="BtnSave" class="input_button"/>
	 </div>
	 </td></tr></table>
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
			//for(var j = 0; j <  userArr.length; j++){
				for (var i = 0; i < checks.length; i++) {
					if(userArray.indexOf(checks[i].value)>-1){
						checks[i].checked=true;
					}
				}
			//}
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
			document.forms[0].action='queryConfigUserLev.action';
			document.forms[0].submit();
		}
		var saveCfg = function(){
			if(check('userList')){
				document.forms[0].action="saveUserByRole.action";
				document.forms[0].submit();
					}
		}
	</script>
</html>
