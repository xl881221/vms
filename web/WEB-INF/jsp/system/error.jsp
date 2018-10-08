<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@include file="../common/include.jsp" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<link href="<%=sysTheme%>/css/common.css" type="text/css"
			rel="stylesheet">
		<script type="text/javascript">
				function show(id){
					o = document.getElementById(id);
					o.style.display == 'none' ? o.style.display='' : o.style.display = 'none';
				}
			</script>
	</head>

	<body>
		<table class="location">
			<tr>
				<td>
					<table id="tbl_current_status">
						<tr>
							<td>
								<img src="<c:out value="${sysTheme}"/>/img/jes/icon/home.png" />
								<span class="current_status_submenu">系统错误<span
									class="actionIcon">-&gt;</span>错误信息</span>
							</td>
						</tr>
					</table>
				</td>
			</tr>

			<tr>
				<td>
					<s:actionerror />
					<s:actionmessage />
				</td>
			</tr>
			<tr>
				<td>
					<s:property value="exception.message" />
				</td>
			</tr>
			<tr>
				<td>
					<input type="button" value="详细信息" onclick="show('message')" />
				</td>
			</tr>
			<tr>
				<td>

				</td>
			</tr>
		</table>
		<div style="display: none;" id="message" align="left"
			class="width:80%">
			<s:property value="exceptionStack" />
		</div>
	</body>
</html>