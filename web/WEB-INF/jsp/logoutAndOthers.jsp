<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>MESSAGE</title>
		<script type="text/javascript">
		
			var ajaxUrls = [];
		</script>
		<s:iterator value="baseConfigs">
		<script type="text/javascript">ajaxUrls[ajaxUrls.length] = '<s:property value="linkSiteUrl" />';</script>
		</s:iterator>
		<script type="text/javascript">	
	function logout() {
		for (var i=0; i<ajaxUrls.length; i++) {
			var s = getAjax();
			if (!s) continue;
			if(ajaxUrls[i] != ''){
				s.open("GET", ajaxUrls[i] + "logout.ajax", true);
				s.send(null);
			}
		}
	}
	function getAjax() {
		var s = ["MSXML2.XMLHTTP", "Microsoft.XMLHTTP", "MSXML.XMLHTTP", "MSXML3.XMLHTTP"];
		if (window.ActiveXObject) {
			for(var i=0; i<s.length; i++) {
				try{
					return new ActiveXObject(s[i]);
				}catch(e){ }
			}
		} else if (window.XMLHttpRequest) {
			return new XMLHttpRequest();
		} else return null;
	}
		</script>
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
<script type="text/javascript">

</script>