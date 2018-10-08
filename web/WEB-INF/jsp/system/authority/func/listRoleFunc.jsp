<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title></title>
		<%@include file="../../../common/include.jsp"%>
		<link href="<c:out value="${sysTheme}"/>/css/subWindow.css"
			type="text/css" rel="stylesheet">
</head>
<body>
    <form name="frm1" method="post" action="" id="frm1">
<table id="tbl_main" cellpadding="0" cellspacing="0">
	<tr>
		<td align="left">
		<table id="tbl_current_status">
			<tr>
				<td>
					<img src="<c:out value="${sysTheme}"/>/img/jes/icon/home.png" />
					<span class="current_status_menu">当前位置：</span>
					<span class="current_status_submenu">基础信息管理<span class="actionIcon">-&gt;</span>功能配置</span>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td align="center">
		<table id="tbl_query" cellpadding="1" cellspacing="0">
			<tr>
				<td align="left">
				 子系统：<s:select list="baseConfigs" listKey="key" listValue="value" name="systemId" onchange="query()"></s:select>
				</td>
	      		<td align="right">
	                <div type="btn" img="<c:out value="${webapp}"/>/image/button/search.gif" onclick="query()" name="BtnView" value="查询" id="BtnView" style="display: none"></div>
	           </td>
			</tr>
		</table>
		</td>
	</tr>
	<tr style="display:none;">
		<td align="center">
		<table id="tbl_tools" cellpadding="1" cellspacing="1">
			<tr>
				<td align="left">&nbsp;
	            	<div type="btn" img="<c:out value="${webapp}"/>/image/button/add3.gif" onclick="createAuthResMap()" name="BtnReturn" value="新增" id="BtnReturn"></div>
	            	<div style="margin-left:5px" type="btn" img="<c:out value="${webapp}"/>/image/button/delete.gif" onclick="deleteAuthResMap('<s:property value="resId" />')" name="BtnReturn" value="删除" id="BtnReturn"></div>
            	</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
 <div style="overflow: auto; width: 100%; height: 100%;" id="lessGridList">
        <table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" display="none" style="border-collapse: collapse;">
            <tr class="lessGrid head" >
                <th width="3%" style="display:none;"><input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="cbxselectall(this,'resIDs')" /></th>
                <th style="text-align:center">系统名</th>
                <th style="text-align:center">角色名</th>
                <th style="text-align:center">菜单资源查看</th>
                <th style="text-align:center">具体功能配置</th>
            </tr>
            <s:iterator value="paginationList.recordList" status="stuts" var="iList">
				<tr align="center" class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
					<td style="display:none;"><input type="checkbox" style="width:13px;height:13px;" name="resIDs" value="<s:property value='resId' />" /></td>
	                <td><s:property value="uBaseConfig.systemCname" /></td>
	                <td><s:property value="roleName" /></td>
	                <td align="center">
	                	<a href="#" onclick="OpenModalWindow('viewMenuRes.action?roleId=<s:property value="roleId" />',400,500);">
						<img src="<c:out value="${sysTheme}"/>/img/jes/icon/view.png" alt="" style="border-width: 0px;"/></a>
					</td>
					 <td align="center">
						<img src="<c:out value="${webapp}"/>/image/button/check.gif" alt="审核通过" style="border-width: 0px;"/>
					</td>
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
    


  </form>
</body>
</html>
<script language="javascript">
	function query(){
		document.forms[0].action = "listRoleFunc.action";
		document.forms[0].submit();
	}
</script>