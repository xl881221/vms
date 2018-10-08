<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@include file="../../common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv='Expires' content='0'>
	<meta http-equiv='Pragma'  content='no-cache'>
	<meta http-equiv='Cache-Control' content='no-cache'>
	<%@include file="../../common/include.jsp"%>
	<link href="<c:out value="${sysTheme}"/>/css/subWindow.css"	type="text/css" rel="stylesheet">
	<script language="javascript" type="text/javascript">	
		/*
		* 查询提交
		*/
		function findOutSubmit() {
			document.formNotice.action="listLog.action";
			document.formNotice.method="post";
			document.formNotice.submit();
		}
	</script>
</head>
<body>
	<form name="formNotice" action="<c:out value='${webapp}'/>/listLog.action" method="post">
		<s:hidden name="log.noticeId"></s:hidden>
		<table id="tbl_main" cellpadding="0" cellspacing="0">
			<tr>
				<td align="left">
					<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
						<tr>
							<th colspan="4">通告日志查看</th>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td align="left">
					<table id="tbl_query" cellpadding="1" cellspacing="1">
						<tr>
							<td align="right">用户&nbsp;</td>
							<td ><input name="log.userCName"  value='<s:property value="log.userCName"/>' type="text"/></td>
			      			<td align="right">
							<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="findOutSubmit()" name="BtnView" value="查询" id="BtnView"></div>							
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	 	<div style="overflow: auto; width: 100%;" id="lessGridListTemp">
	       <table class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse;">
	           <tr style="width: 100%;" class="lessGridTemp headTemp">
					<th style="text-align:center;width:30%;" nowrap>用户</th>
					<th style="text-align:center;width:40%;">查看时间</th>
					<th style="text-align:center;width:30%;"nowrap>用户IP</th>
	           </tr>
	           <s:iterator value="paginationList.recordList" id="n" status="stuts">
	           		<tr align="center" class="<s:if test="#stuts.odd==true">lessGridTemp rowA</s:if><s:else>lessGridTemp rowB</s:else>">
						<td><s:property value="#n.userCName"/> / <s:property value="#n.userEName"/></td>
						<td><s:property value="#n.viewTime2"/></td>
						<td><s:property value="#n.ip"/></td>
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
<script type="text/javascript">
	document.getElementById("lessGridListTemp").style.height = screen.availHeight - 340;
</script>
</html>
