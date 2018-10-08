<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>MESSAGE</title>
	</head>
	<body>
		<table border="0" width="100%">
			<tr height="50">
				<td></td>
			</tr>
			<tr height="200">
				<td>
					<font color="red"><%=request.getAttribute("mes")%></font>
				</td>
			</tr>
		</table>
	</body>
</html>