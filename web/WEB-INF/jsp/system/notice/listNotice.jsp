<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv='Expires' content='0'>
	<meta http-equiv='Pragma'  content='no-cache'>
	<meta http-equiv='Cache-Control' content='no-cache'>
	<%@include file="../../common/include.jsp"%>
	<%@include file="../../common/commen-ui.jsp"%>
	<script language="javascript" type="text/javascript">
		function findOutSubmit() {
			document.formNotice.action="<c:out value='${webapp}'/>/listNotice.action";
			document.formNotice.method="post";
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
	<form name="formNotice" action="<c:out value='${webapp}'/>/listNotice.action" method="post">
		<table id="tbl_main" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<span><img src="<c:out value="${webapp}"/>/themes/images/icons/icon13.png" /></span>
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">通告管理</span>
					</div>	
					<div class="widthauto">				
					<div id="tbl_query">
						<table>
							<tr>
								<td align="left" >
								通告标题<span><input class="tbl_query_text"  name="notice.title"  value="<c:out value="${notice.title}"/>" id="notice.title" type="text" /></span>
								通告类型<span><s:select list="types" name="notice.type" listKey="key" listValue="value"></s:select></span>
								 </td>
				      			<td align="left" >
									<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="findOutSubmit()" name="BtnView" value="查询" id="BtnView" />
									<input type="button" class="tbl_query_button mleft5" onMouseMove="this.className='tbl_query_button_on mleft5'" onMouseOut="this.className='tbl_query_button mleft5'" onClick="OpenModalWindow('<c:out value="${webapp}"/>/editNotice.action', 740, 540, true)" name="BtnReturn" value="新增" id="BtnReturn" />
									<input type="button" class="tbl_query_button mleft5" onMouseMove="this.className='tbl_query_button_on mleft5'" onMouseOut="this.className='tbl_query_button mleft5'" onClick="beforeDelete('<c:out value="${webapp}"/>/deleteNotice.action', 'nIds')" name="BtnReturn" value="删除" id="BtnReturn" />
								</td>
							</tr>
						</table>
					</div>
					</div>
					<div id="lessGridList">
				       <table id="lessGridTable" class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse;" style="width:100%">
				           <tr class="lessGrid head">
								<th style="text-align:center" width="5%" nowrap><input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'nIds')" /></th>
								<th style="text-align:center" width="25%" nowrap>通告标题</th>
								<th style="text-align:center" width="25%" nowrap>通告类型</th>
								<th style="text-align:center" width="15%" nowrap>发布时间</th>
								<th style="text-align:center" width="10%" nowrap>反馈</th>
								<th style="text-align:center" width="10%" nowrap>日志</th>
								<th style="text-align:center" width="10%" nowrap>修改</th>
				           </tr>
				           <s:iterator value="paginationList.recordList" id="n" status="stuts">
				           		<tr align="center" class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
									<td><input type="checkbox" style="width:13px;height:13px;" name="nIds" value='<s:property value="#n.id"/>'/></td>
									<td align="left" nowrap>
										<a style="display: none;" href="javascript:void(0);" onClick="OpenWin('<c:out value="${webapp}"/>/viewNotice.action?id=<s:property value="#n.id"/>',null,null,700,500)"><s:property value="#n.title"/></a>
									</td>
									<td>
										<s:property value="#n.type"/>
									</td>
									<td nowrap><s:property value="#n.createTime2"/></td>
									<td align="center">
										<a href="javascript:;" onClick="OpenModalWindow('<c:out value="${webapp}"/>/listFeedBack.action?feedBack.noticeId=<s:property value="#n.id"/>',700,500)">
										<img src="<c:out value="${webapp}"/>/themes/images/icons/anser.png" alt="反馈" style="border-width: 0px;"/></a>
									</td>
									<td align="center">
										<a href="javascript:;" onClick="OpenModalWindow('<c:out value='${webapp}'/>/listLog.action?log.noticeId=<s:property value="#n.id"/>',700,500)">
										<img src="<c:out value="${webapp}"/>/themes/images/icons/doc.png" alt="日志" style="border-width: 0px;"/></a>
									</td>
									<td align="center">
										<a href="javascript:void(0);" onClick="OpenModalWindowReload('<c:out value='${webapp}'/>/editNotice.action?id=<s:property value="#n.id"/>', 740,670, true)">
										<img src="<c:out value="${webapp}"/>/themes/images/icons/edit.png" alt="修改" style="border-width: 0px;"/></a>
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
					
					
				</td>
			</tr>
		</table>
    </form>
</body>
<script type="text/javascript">
	document.getElementById("lessGridList").style.height = screen.availHeight - 305;
</script>
</html>
