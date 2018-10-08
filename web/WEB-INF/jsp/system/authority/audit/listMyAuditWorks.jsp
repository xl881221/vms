<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <title> </title>
    <meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@include file="../../../common/include.jsp"%>
<meta http-equiv="expires" content="0">
    
<style type="text/css">
.detailInfoDIV {
	border: 1px solid green;
	background-color: khaki;
	top: 110px;
	left: 150px;
	width: 450px;
	height: 300px;
	position: absolute;
	z-index: 2;
	filter: alpha(opacity =     90);
	-moz-opacity: 0.9;
	display: none;
}
</style>

	<script type="text/javascript">
	function approve(id){
		document.frm1.action="approveAudit.action?id="+id;
		document.frm1.method="post";
		document.frm1.submit();
	}
	function reject(id){
		document.frm1.action="rejectAudit.action?id="+id;
		document.frm1.method="post";
		document.frm1.submit();
	}
	function doSubmit(action) {
		if(action != ''){
			document.frm1.action=action;
			document.frm1.method="post";
			document.frm1.submit();
		}
	}
	function query() {
		document.frm1.method="post";
		document.frm1.action="listMyAuditWorks.action";
		document.frm1.submit();
	}
	function showDetailInfoDIV(){
	 	var currRow =  window.event.srcElement.parentElement.parentElement;// 获取当前行
	 	document.getElementById("detailInfo").innerHTML=currRow.cells[currRow.cells.length-1].innerHTML;
	 	document.getElementById("detailInfoDIV").style.display='block';
	}
	function hideDetailInfoDIV(){
		document.getElementById("detailInfoDIV").style.display='none';
	}
	function showDetail(action,a,b){
	 	OpenModalWindow(action,a,b);
	}
	window.onload = function(){
		var message  = '<s:property value="operationMessage" />';
		if(message!=''){
			alert(message);
		}
	}
	</script>
</head>
<body>
    <form name="frm1" method="post" action="" id="frm1">
    <input type="hidden" name="curPage" value="<s:property value="paginationList.currentPage" />">
<table id="tbl_main" cellpadding="0" cellspacing="0">
	<tr>
		<td align="left">
		<table id="tbl_current_status">
			<tr>
				<td>
					<img src="<c:out value="${sysTheme}"/>/img/jes/icon/home.png" />
					<span class="current_status_menu">当前位置：</span>
					<span class="current_status_submenu">基础信息管理<span class="actionIcon">-&gt;</span>权限变更查看</span>
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
				 变更主题：
				 <s:select list="SELECT_TYPE_MAP" listKey="key" listValue="value" name="changeType" onchange="query()"></s:select>
				</td>
	      		<td align="right">
	                <div type="btn" img="<c:out value="${webapp}"/>/image/button/search.gif" onclick="query()" name="BtnView" value="查询" id="BtnView" style="display: none"></div>
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
                <th style="text-align:center">变更主题</th>
                <th style="text-align:center">修改描述</th>
                <th style="text-align:center">提交日期</th>
                <th style="text-align:center">详细信息</th>
				<th style="text-align:center">状态</th>
				<th style="text-align:center">撤销</th>
            </tr>
				
				<s:iterator value="paginationList.recordList" status="stuts" var="iList">
					<tr align="center" class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
					<td style="display:none;"><input type="checkbox" style="width:13px;height:13px;" name="resIDs" value="<s:property value='resId' />" /></td>
	                <td><s:property value="typeDesc" /></td>
	                <td><s:property value="contentDesc" /></td>
	                <td><s:property value="submitDate" /></td>
					<td align="center">
					<s:if test="ownDetailURL!=null&&ownDetailURL!=''">
					<a href="javascript:void(0)" onclick="<s:property value="ownDetailURL" />">查看</a>
					</s:if></td>
					<td><s:property value="auditResult" /></td>
					<td style="">
					<s:if test="cancelURL!=null&&cancelURL!=''">
						<a href="#" onclick="doSubmit('<s:property value="cancelURL" />')"> <img src="<c:out value="${webapp}"/>/image/button/delete_message.gif" alt="撤销" style="border-width: 0px;"/></a>
					</s:if>
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
