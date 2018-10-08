<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%@include file="../../common/include.jsp"%>
</head>
<body onmousemove="MM(event)" onmouseout="MO(event)">
    <form name="frm" action="<c:out value='${webapp}'/>/saveConfigRes.action" method="post">
    <s:if test="role.roleId != null && role.roleId != '' ">
    	<input type="hidden" name="role.roleId" value="<s:property value="role.roleId"/>"/>
    </s:if>
    
    
<table id="tbl_main" cellpadding="0" cellspacing="0">
	<!-- <tr>
		<td align="left">
		<table id="tbl_current_status">
			<tr>
				<td>
					 
					<span ><a href="editConfigUser.action?roleId=<s:property value="role.roleId"/>">用户配置</a></span>
					<span
					class="current_status_submenu">资源配置</span>
				</td>
			</tr>
		</table>
		</td>
	</tr> -->
	<tr>
		<td align="center">
			<table id="tbl_tools" cellpadding="1" cellspacing="1">
				<tr>
					<td align="left">
					<span ><a href="editConfigUser.action?roleId=<s:property value="role.roleId"/>">用户配置</a></span>
						<span
						class="current_status_submenu">资源配置</span>
	                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" name="BtnSave" value="保&nbsp;存" id="BtnSave" onclick=""
	                    />
	            </td>
				</tr>
			</table>
		</td>
	</tr>
	<tr>
		<td>
			<table id="tbl_query" cellpadding="1" cellspacing="1">
						<tr>
							<td width="50px" align="right" nowrap>资源类型</td>
							<td width="100px">
							<s:select list="resTypeDic" name="authResMap.resType" listKey="dicValue" listValue="dicName" value="authResMap.resType"  theme="simple"/>
							<td width="50px" align="right">资源ID</td>
							<td width="100px"><input name="authResMap.resId" type="text" id="authResMap.resId"  style="width: 100;" value="<s:property value="authResMap.resId"/>"/></td>
							<td width="40px" align="right">资源名</td>
							<td width="100px"><input name="authResMap.resName" type="text" id="authResMap.resName"  style="width: 100;" value="<s:property value="authResMap.resName"/>"/></td>
			      			<td align="right"><input type="button" onclick="query()" name="BtnView" value="查&nbsp;&nbsp;询" id="BtnView" class="input_button" /></td>
						</tr>
			</table>
		</td>
	</tr>
</table>
    
  
   
   <div style="overflow: auto; width: 100%; height: 390px;" id="lessGridList1">
        <table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" display="none" style="border-collapse: collapse;">
            <tr class="lessGrid head">
                <th width="3%">
                	<input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="cbxselectall(this,'resMap')" />
                </th>
                <th>资源ID</th>
                <th>资源名称</th>
                <th>资源描述</th>
                <th>查看</th>
                <th>修改</th>
            </tr>

 			<s:iterator value="allResMap" status="stuts" id="row">
				<tr align="center" class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
				<td align=center>
						<input type="checkbox" style="width:13px;height:13px;" name="resMap" value="<s:property value="resId"/>"/>
					
				</td>
				<td><s:property value="resId"/></td>
                <td><s:property value="resName"/></td>
                <td><s:property value="description"/></td>
				<td align="center"><a href="#" onclick="openNew('<c:out value="${webapp}" />/viewSimpleResCfg.action?authResMap.resId=<s:property value="resId"/>&role.roleId=${role.roleId}')"><img src="<c:out value='${sysTheme}'/>/images/edit.gif" alt="查看" style="border-width: 0px;"/></a></td>
                <td align="center"><a href="#" onClick="openNew('<c:out value="${webapp}" />/editSimpleResCfg.action?authResMap.resId=<s:property value="resId"/>&role.roleId=${role.roleId}')"><img src="<c:out value='${sysTheme}'/>/images/edit.gif" alt="修改" style="border-width: 0px;"/></a></td>
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
 
    
    </form>
</body>
	<script language="javascript" type="text/javascript">
		var resMapArray = '<s:property value="%{resMapArray}"/>';
		var autoCheck = function(){
			var resArr = resMapArray.split(',');
			var checks = document.getElementsByName("resMap");
			for (i = 0; i < checks.length; i++) {
				//for(j = 0; j <  resArr.length; j++){
					if(resMapArray.indexOf(checks[i].value)>-1){
						checks[i].checked=true;
					}
				//}
			}
		}
		var query = function() {
			document.forms[0].action='queryConfigRes.action';
			document.forms[0].submit();
		}
		var openNew = function(url) {
			window.open(url,'','top=150,left=260,width=600,height=500,menubar=no,status=yes');
		}
		autoCheck();
	</script>
</html>
