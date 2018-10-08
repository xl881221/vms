<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@include file="../../../common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../../../common/include.jsp"%>
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
			window.onload = function () {
				autosize();
			}
			
			function autosize(){
				height = $('lessGridList').scrollHeight+100;
			   	width = $('lessGridList').scrollWidth+30;
			    
			    
			    window.dialogHeight = height + "px"
			    window.dialogWidth = width + "px"
			
			    window.dialogLeft = ((window.screen.availWidth - document.body.clientWidth) / 2).toString() + "px"     
			    window.dialogTop = ((window.screen.availHeight - document.body.clientHeight) / 2).toString() + "px"
			    
			}
			
			function maxHeight(a,b,c,d){
				return max(max(a.scrollHeight,b.scrollHeight,c.scrollHeight),d.scrollHeight);
			}
			function maxWidth(a,b,c,d){
				return max(max(a.scrollWidth,b.scrollWidth,c.scrollWidth),d.scrollWidth);
			}
			function max(a,b,c){
				return max(max(a,b),c);
			}
			function max(a,b){
				return a > b ? a : b;
			}
			$ = function(id){
				return document.getElementById(id);
			}
	</script>
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
					<span class="current_status_submenu">用户角色修改</span>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
 <div style="overflow: auto; width: 100%; height: 100%;" id="lessGridList">
 
        <table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" display="none" style="border-collapse: collapse;">
            <tr class="lessGrid head" >
                <th style="text-align:center">角色名称</th>
                <th style="text-align:center">子系统</th>
                <th style="text-align:center">修改状态</th>
                <th style="text-align:center">提交人</th>
                <th style="text-align:center">提交时间</th>
                <th style="text-align:center">审核状态</th>
                <s:if test="showAuditColumn">
                <th style="text-align:center">审核人</th>
                <th style="text-align:center">审核时间</th></s:if>
            </tr>
				
				<s:iterator value="roleChanges" status="stuts" var="info">
					<tr align="center" class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>" <s:if test="currentUser.userId == changeUser">style="color: gray;"</s:if> >
						<td><s:property value="roleName" /></td>
						<td><s:property value="systemName" /></td>
	                	<td><s:property value="showStatus" /></td>
	               	 	<td><s:property value="changeUser" /></td>
						<td><s:date name="changeTime" format="yyyy-MM-dd"/> </td>
						<td><s:property value="auditInfo" /></td>
						<s:if test="showAuditColumn">
                		<td><s:property value="auditUser" /></td>
                		<td><s:date name="auditTime" format="yyyy-MM-dd"/> </td></s:if>
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
    
 <div id="detailInfoDIV" class="detailInfoDIV">
<center>
<table width="440px" border="0">
	<tr>
		<td width="20%"><span
			style='color: #A24313; font: normal 12px Arial'>操作详细信息：</span></td>
		
	</tr>
	<tr><td width="85%" id="detailInfo" name="_td2"></td></tr>
	<tr>
		<td nowrap="nowrap" colspan="2" align="center">
		<div style="float: left; margin-left: 180px" type="btn"
			onclick="hideDetailInfoDIV();" id="_returnBtn"
			img="<c:out value="${webapp}"/>/image/button/back.gif"
			name="_returnBtn" value="返回" />
		</td>
	</tr>
</table>
</center>
</div>

  </form>
  <div  id="ctrlbutton" class="ctrlbutton"  style="border:0px">		
		<div style="float:right;margin-right: 10px;height:25px;margin-top:5px" type="btn" img="<c:out value="${webapp}"/>/image/button/cancel.gif" onClick="window.close()" name="btnReturn" value="关闭" id="btnReturn"></div>	  	
	</div>
</body>
</html>
