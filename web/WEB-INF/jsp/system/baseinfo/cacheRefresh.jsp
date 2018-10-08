<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="../../common/include.jsp" %>
		<%@include file="../../common/commen-ui.jsp"%>
		<title>刷新缓存</title>
		<script type="text/javascript"
			src="<c:out value="${webapp}"/>/dwr/engine.js"></script>
		<script type="text/javascript"
			src="<c:out value="${webapp}"/>/dwr/util.js"></script>
		<script type="text/javascript"
			src="<c:out value="${webapp}"/>/dwr/interface/dwrAsynService.js"></script>
		<script type="text/javascript">
	<!--
		function refreshCache() {
			dwrAsynService.refreshCache(function (f) {
				if (f) {
					alert("刷新成功！");
				} else 
					alert("刷新失败，请重新执行操作！");
			});
		}
	-->
	</script>
	</head>
	<body>
		<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
			<tr>
				<td class="centercondition">
					<div id="tbl_current_status">
						<span><img src="<c:out value="${webapp}"/>/themes/images/icons/icon13.png" /></span>
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">系统管理</span>
						<span class="current_status_submenu">系统参数管理</span>
						<span class="current_status_submenu">刷新缓存</span>
					</div>
					<div id="tbl_button1">
						<a href="#" onClick="refreshCache();"><span>刷新缓存</span></a>
					</div>
					<div id="lessGridList">
					<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
						<tr>
			<!--				<td style="width: 30%;  text-align: left;">-->
			<!--					<a href="javascript:void(0)" onClick="refreshCache()"><img alt="刷新缓存"-->
			<!--						src="<c:out value="${sysTheme}"/>/img/jes/refresh_32.png"></a>-->
			<!--				</td>-->
			<!--				<td style="width: 50px;">-->
			<!--				</td>-->
						</tr>
<!--						<tr class="lessGrid head">-->
<!--							<th style="text-align: left; width:100%" nowrap>-->
<!--								刷新内容列表-->
<!--							</th>-->
<!--						</tr>-->
						<tr class="lessGrid">
							<td>刷新主题信息</td>
						</tr>
						<tr class="lessGrid">
							<td>刷新功能菜单信息</td>
						</tr>
					</table>
					</div>
				</td>
			</tr>
		</table>
	</body>
<script type="text/javascript">
   	document.getElementById("lessGridList").style.height = screen.availHeight - 258;
</script>
</html>
