<script language="javascript" type="text/javascript" src="<c:out value='${webapp}'/>/js/main.js" charset="GBK"></script>
<script language="javascript" type="text/javascript" src="<c:out value='${webapp}'/>/js/window.js" charset="UTF-8"></script>
<script language="javascript" type="text/javascript" src="<c:out value='${webapp}'/>/js/validator.js" charset="UTF-8"></script>
<script src="<c:out value='${webapp}'/>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<script language="javascript" type="text/javascript" charset="UTF-8">
	<%
	if(request.getParameter("RESULT_MESSAGE") !=null && !"".equals(request.getParameter("RESULT_MESSAGE"))){
	%>
		alert("<%=java.net.URLDecoder.decode(request.getParameter("RESULT_MESSAGE"),"utf-8")%>");
	<%
	}else if(request.getAttribute("RESULT_MESSAGE") !=null && !"".equals(request.getAttribute("RESULT_MESSAGE"))){	
	%>
	    alert("<%=java.net.URLDecoder.decode(request.getAttribute("RESULT_MESSAGE").toString(),"utf-8")%>");
	<%
	}else if(request.getParameter("resultMessages") !=null && !"".equals(request.getParameter("resultMessages"))){	
	%>
		alert("<%=request.getParameter("resultMessages")%>");
	<%
	}else if(request.getAttribute("resultMessages") !=null && !"".equals(request.getAttribute("resultMessages"))){	
	%>
	    alert("<%=request.getAttribute("resultMessages")%>");
	<%
	}	
	%>
</script>


