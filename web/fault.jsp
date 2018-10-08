<%@ page language="java" contentType="text/html; charset=UTF-8" import="com.jes.core.api.servlet.ServletHelper" pageEncoding="UTF-8"%>
<% Object language=request.getParameter("user.language");
   if(language==null)
   	  language=ServletHelper.getCookieValue(request,"USYS_USER_LANGUAGE");

%>
<script>
alert("<%if("e".equals(language))out.print("Prohibit access to unauthorized users");else out.print("非法用户禁止访问");%>");
if(window.top!=undefined)
   window.top.location.href="/fmss/"
else{
   window.location.href="/fmss/"
}

</script>