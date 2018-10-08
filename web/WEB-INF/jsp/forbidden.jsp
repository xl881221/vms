<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>MESSAGE</title>
	</head>
	<body>
		未执行的脚本如下 ：
		<table border="0" width="100%">
			<tr height="200">
				<td>
				<s:iterator value="#list">
					<font color="red"><s:property/></font></br>
				</s:iterator>
					
				</td>
			</tr>
		</table>
	</body>
</html>
<script type="text/javascript">

</script>