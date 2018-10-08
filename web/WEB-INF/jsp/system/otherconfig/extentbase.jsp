<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../../common/include.jsp"%>
<%@include file="../../common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
	<head>
		<title>指标核对</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<script type="text/javascript">
			function findOutSubmit(){
				// extentBase.action = '<c:out value='${webapp}'/>/listExtentBase.action';
				extentBase.submit();
			}
			
			function batchExportList() {
				document.getElementById("batchExport_id_instName").value = document.getElementById("extentBase_id_instName").value;
				document.getElementById("batchExport_id_ddate").value = document.getElementById("extentBase_id_ddate").value;
				document.getElementById("batchExport_id_systemId").value = document.getElementById("extentBase_id_systemId").value;
				document.getElementById("batchExport_id_itemId").value = document.getElementById("extentBase_id_itemId").value;
				document.getElementById("batchExport_id_srcsysCd").value = document.getElementById("extentBase_id_srcsysCd").value;
				batchExport.submit();
			}
		</script>
	</head>

	<body>
		<form id="extentBase" method="post"
			action="<c:out value='${webapp}'/>/listExtentBase.action">
			<table id="tbl_main" cellpadding="0" cellspacing="0">
				<tr>
					<td align="left">
						<table id="tbl_current_status">
							<tr>
								<td>
									<img src="<c:out value="${sysTheme}"/>/img/jes/icon/home.png" />
									<span class="current_status_menu">当前位置：</span>
									<span class="current_status_submenu">指标配置<span
										class="actionIcon">-&gt;</span>指标核对</span>
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
									机构编号：
									<input name="extentBase.id.instName" type="text"
										value="<s:property value="extentBase.id.instName" />"
										id="extentBase_id_instName" style="width: 100;" />
									&nbsp;&nbsp;数据日期：
									<input name="extentBase.id.ddate" type="text"
										value="<s:property value="extentBase.id.ddate" />"
										id="extentBase_id_ddate" style="width: 100;" 
										class="Wdate" onFocus="WdatePicker()"/>
								
									&nbsp;&nbsp; 所属模块：
									<input name="extentBase.id.systemId" type="text"
										value="<s:property value="extentBase.id.systemId" />"
										id="extentBase_id_systemId" style="width: 100;" />
								</td>
							</tr>
								<td align="left">
									指标编码：
									<input name="extentBase.id.itemId" type="text"
										value="<s:property value="extentBase.id.itemId" />"
										id="extentBase_id_itemId" style="width: 100;" />
									&nbsp;&nbsp; 来源系统：
									<input name="extentBase.id.srcsysCd" type="text"
										value="<s:property value="extentBase.id.srcsysCd" />"
										id="extentBase_id_srcsysCd" style="width: 100;" />
								</td>
								<td align="right">
									<div type="btn"
										img="<c:out value="${webapp}"/>/image/button/search.gif"
										onclick="findOutSubmit()" name="BtnView" value="查询"
										id="BtnView"></div>&nbsp;<div type="btn"
										img="<c:out value="${webapp}"/>/image/button/output_execel.gif"
										onclick="batchExportList();" name="BtnView" value="导出"
										id="BtnView"></div>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<div style="overflow: auto; width: 100%; height: 390px;" id="lessGridList">
	       <table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" display="none" style="border-collapse: collapse;">
	           <tr class="lessGrid head">
					<th style="text-align:center">机构编号</th>
					<th style="text-align:center">数据日期</th>
					<th style="text-align:center">所属模块</th>
					<th style="text-align:center">指标编码</th>
					<th style="text-align:center">指标名称</th>
					<th style="text-align:center">币种</th>
					<th style="text-align:center">指标值</th>
					<th style="text-align:center">报表编号</th>
					<th style="text-align:center">来源系统</th>
	           </tr>
	           <s:iterator value="paginationList.recordList" id="iList" status="stuts">
	           		<tr align="center" class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
						<td><s:property value="#iList.id.instName"/></td>
						<td><s:property value="#iList.id.ddate"/></td>
						<td><s:property value="#iList.id.systemId"/></td>
						<td><s:property value="#iList.id.itemId"/></td>
						<td><s:property value="#iList.id.itemName"/></td>
						<td><s:property value="#iList.id.rcurrCd"/></td>
						<td><s:property value="#iList.id.itemValue"/></td>
						<td><s:property value="#iList.id.reportId"/></td>
						<td><s:property value="#iList.id.srcsysCd"/></td>
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
		
		<form id="batchExport" method="post" action="<c:out value='${webapp}'/>/batchExportList.action">
			<input id="batchExport_id_instName" name="extentBase.id.instName" type="hidden" value="<s:property value="extentBase.id.instName" />" />
			<input id="batchExport_id_ddate" name="extentBase.id.ddate" type="hidden" value="<s:property value="extentBase.id.ddate" />" />
			<input id="batchExport_id_systemId" name="extentBase.id.systemId" type="hidden" value="<s:property value="extentBase.id.systemId" />" />
			<input id="batchExport_id_itemId" name="extentBase.id.itemId" type="hidden" value="<s:property value="extentBase.id.itemId" />" />
			<input id="batchExport_id_srcsysCd" name="extentBase.id.srcsysCd" type="hidden" value="<s:property value="extentBase.id.srcsysCd" />" />
		</form>
	</body>
</html>