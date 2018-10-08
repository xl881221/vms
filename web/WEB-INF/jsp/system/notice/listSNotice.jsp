<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv='Expires' content='0'>
	<meta http-equiv='Pragma'  content='no-cache'>
	<meta http-equiv='Cache-Control' content='no-cache'>
	<%@include file="../../common/include.jsp"%>
	<script language="javascript" type="text/javascript">
		function findOutSubmit() {
			document.formNotice.submit();
		}
		function init() {
			var rs = document.getElementById("lessGridTable").rows;
			if (rs.length > 1) {
				var L = rs[0].cells[1].offsetWidth;
				for (var i=1; i<rs.length; i++) {
					var a = rs[i].cells[1].children(0);
					a.style.display = "";
					if (a.offsetWidth > L) {
						a.style.overflowX = "hidden";
						a.style.textOverflow = "ellipsis";
						a.style.width = L - 10;
						a.title = a.innerText;
					}
				}
			}
		}
	</script>
</head>
<body onload="init()">
	<form name="formNotice" action="<c:out value='${webapp}'/>/listSNotice.action" method="post">
		<table id="tbl_main" cellpadding="0" cellspacing="0">
			<tr>
				<td align="left">
					<table id="tbl_current_status">
						<tr>
							<td>
								<img src="<c:out value="${sysTheme}"/>/img/jes/icon/home.png" /> 
								<span class="current_status_menu">当前位置：</span> 
								<span class="current_status_submenu">通告查询</span>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td align="left">
					<table id="tbl_query" cellpadding="1" cellspacing="1">
						<tr>
							<td>通告标题：</td>
							<td><input name="notice.title" id="notice.title" value="<c:out value="${notice.title}"/>" type="text" style="width: 100;" /></td>
							<td>通告类型：</td>
							<td>
								<s:select list="types" name="notice.type" listKey="key" listValue="value"></s:select>
							</td>			      			
							<td align="right" width="60px"> <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"  onclick="findOutSubmit()" name="BtnView" value="查询" id="BtnView"/></td>
						
						</tr>
					</table>
				</td>
			</tr>
		</table>
	 	<div style="overflow: auto; width: 100%;" id="lessGridList">
	       <table style="width:100%;" id="lessGridTable" class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" display="none" style="border-collapse: collapse;">
	           <tr class="lessGrid head">
					<th style="text-align:center;width:5%;" nowrap><input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'nIds')" /></th>
					<th style="text-align:center;width:50%;" nowrap>通告标题</th>
					<th style="text-align:center;width:20%;" nowrap>通告类型</th>
					<th style="text-align:center;width:25%;" nowrap>发布时间</th>
	           </tr>
	           <s:iterator value="paginationList.recordList" id="n" status="stuts">
	           		<tr align="center" class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
						<td><input type="checkbox" style="width:13px;height:13px;" name="nIds" value='<s:property value="#n.id"/>'/></td>
						<td align="left" nowrap>
							<a style="display: none;" href="#" onClick="OpenModalWindow('<c:out value="${webapp}"/>/viewNotice.action?id=<s:property value="#n.id"/>',700,500)"><s:property value="#n.title"/></a>
						</td>
						<td><s:property value="#n.type"/></td>
						<td nowrap><s:property value="#n.createTime2"/></td>
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
	document.getElementById("lessGridList").style.height = screen.availHeight - 370;
</script>
</html>
