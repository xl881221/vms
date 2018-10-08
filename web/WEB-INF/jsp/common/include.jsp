<%@page import="fmss.common.cache.CacheManager,org.apache.commons.lang.StringUtils"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();

	// webapp
	String webapp = request.getScheme() + "://" + request.getServerName() + ":"+ request.getServerPort() + path;
	// sysTheme
	String sysTheme = webapp + "/theme/default";
	if(StringUtils.isNotEmpty(CacheManager.getParemerCacheMapValue(fmss.common.util.Constants.PARAM_THEME_PATH_CONFIG))){
		sysTheme = webapp + CacheManager.getParemerCacheMapValue(fmss.common.util.Constants.PARAM_THEME_PATH_CONFIG);
	}
	pageContext.setAttribute("webapp", webapp);
	pageContext.setAttribute("sysTheme", sysTheme);
%>
<script language="javascript" type="text/javascript" src="<%=webapp %>/js/main.js"></script>
<link href="<c:out value="${sysTheme}"/>/css/main.css" rel="stylesheet" type="text/css" />
<script language="javascript" type="text/javascript" src="<%=webapp %>/js/window.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp %>/js/validator.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp %>/dwr/engine.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp %>/dwr/util.js"></script>
<script language="javascript" type="text/javascript" src="<%=webapp %>/dwr/interface/dwrAsynService.js"></script>
<script type="text/javascript" src="<%=webapp%>/js/jquery/jquery_1.42.js"></script>

<!-- MessageBox -->
<script src="<c:out value="${webapp}"/>/js/MessageBox/js/messageBox.js" type="text/javascript"></script>
<link href="<c:out value="${webapp}"/>/js/MessageBox/css/messageBox.css" rel="stylesheet" type="text/css" />
    
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
<script language="javascript" type="text/javascript" charset="UTF-8">
window.onload = function(){
	var hightValue = screen.availHeight - 280;
	var hightValueStr = "height:"+ hightValue + "px";
	
	var hightValue1 = screen.availHeight - 313;
	var hightValueStr1 = "height:"+ hightValue1 + "px";
	
	var hightValue2 = screen.availHeight - 227;
	var hightValueStr2 = "height:"+ hightValue2 + "px";
	
	var hightValue3 = screen.availHeight - 395;
	var hightValueStr3 = "height:"+ hightValue3 + "px";
	
	var hightValue4 = screen.availHeight - 353;
	var hightValueStr4 = "height:"+ hightValue4 + "px";
	
	var hightValue5 = screen.availHeight - 435;
	var hightValueStr5 = "height:"+ hightValue5 + "px";
	
	var hightValue6 = screen.availHeight - 320;
	var hightValueStr6 = "height:"+ hightValue6 + "px";
	
	var hightValue7 = screen.availHeight - 340;
	var hightValueStr7 = "height:"+ hightValue7 + "px";
	
	var hightValue8 = screen.availHeight - 460;
	var hightValueStr8 = "height:"+ hightValue8 + "px";
	
	
	if (typeof(eval("document.all.lessGridList"))!= "undefined"){
		document.getElementById("lessGridList").setAttribute("style", hightValueStr);
	}
	if (typeof(eval("document.all.lessGridList1"))!= "undefined"){
		document.getElementById("lessGridList1").setAttribute("style", hightValueStr1);
	}
	if (typeof(eval("document.all.lessGridList2"))!= "undefined"){
		document.getElementById("lessGridList2").setAttribute("style", hightValueStr2);
	}
	if (typeof(eval("document.all.lessGridList3"))!= "undefined"){
		document.getElementById("lessGridList3").setAttribute("style", hightValueStr3);
	}
	if (typeof(eval("document.all.lessGridList4"))!= "undefined"){
		document.getElementById("lessGridList4").setAttribute("style", hightValueStr4);
	}
	if (typeof(eval("document.all.lessGridList5"))!= "undefined"){
		document.getElementById("lessGridList5").setAttribute("style", hightValueStr5);
	}
	if (typeof(eval("document.all.lessGridList6"))!= "undefined"){
		document.getElementById("lessGridList6").setAttribute("style", hightValueStr6);
	}
	if (typeof(eval("document.all.lessGridList7"))!= "undefined"){
		document.getElementById("lessGridList7").setAttribute("style", hightValueStr7);
	}
	if (typeof(eval("document.all.blankbox"))!= "undefined"){
		document.getElementById("blankbox").setAttribute("style", hightValueStr);
	}
	if (typeof(eval("document.all.lessGridList8"))!= "undefined"){
		document.getElementById("lessGridList8").setAttribute("style", hightValueStr8);
	}
	if (typeof(eval("document.all.lessGridList30"))!= "undefined"){
		document.getElementById("lessGridList30").setAttribute("style", hightValueStr); 
	}
}
</script>

