<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires",0);
%>
<HTML>
<HEAD>
<meta content="http://schemas.microsoft.com/intellisense/ie5; text/html; charset=UTF-8" name="vs_targetSchema">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<%@include file="common/include.jsp"%>
<title></title>

<%@include file="common/commen-ui.jsp"%>

<LINK href="<c:out value="${sysTheme}"/>/css/welcome.css" type="text/css" rel="stylesheet">
<script language="javascript" type="text/javascript" src="jquery-1.4.4.min.js"></script>
<!-- 首页TAB JS -->
<script language="javascript" src="<c:out value="${webapp}"/>/js/welcome.js" charset="gb2312"></script>
		
<!-- ajax 封装JS 开始 -->
<script language="javascript" src="<c:out value="${webapp}"/>/js/ScrollTab/js/ajax.js"></script>
<!-- ajax 封装JS 结束 -->

<!-- tab控件JS及CSS 开始 -->
<script language="javascript" src="<c:out value="${webapp}"/>/js/ScrollTab/js/ScrollTab.js" charset="gb2312"></script>
<link	rel="stylesheet" href="<c:out value="${webapp}"/>/themes/ScrollTab/css/ScrollTab_default.css" type="text/css" />
<!-- tab控件JS及CSS 结束 -->

<script type="text/javascript"	src="<c:out value="${webapp}"/>/dwr/engine.js"></script>
<script type="text/javascript"	src="<c:out value="${webapp}"/>/dwr/util.js"></script>
<script type="text/javascript"	src="<c:out value="${webapp}"/>/dwr/interface/dwrAsynService.js"></script>
<!-- Libraries -->
<link type="text/css" href="<c:out value="${webapp}"/>/wide/css/layout.css" rel="stylesheet" />	
<script type="text/javascript" src="<c:out value="${webapp}"/>/wide/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="<c:out value="${webapp}"/>/wide/js/easyTooltip.js"></script>
<script type="text/javascript" src="<c:out value="${webapp}"/>/wide/js/jquery-ui-1.7.2.custom.min.js"></script>
<script type="text/javascript" src="<c:out value="${webapp}"/>/wide/js/jquery.wysiwyg.js"></script>
<script type="text/javascript" src="<c:out value="${webapp}"/>/wide/js/hoverIntent.js"></script>
<script type="text/javascript" src="<c:out value="${webapp}"/>/wide/js/superfish.js"></script>
<!-- End of Libraries -->
</HEAD>
<body class="transparent" >
<form>
	<div id="container_index">
		<div id="bgwrapleft">
		    <div class="boxtitle">待处理问题</div>
		 	<!-- 需要处理问题 BLOCK 开始 -->
          	<div class="tabbox">
			  <div id="mainTab">loading...</div>
			  <div id="mainTabTarget"></div>
			</div>
			<!-- 需要处理问题 BLOCK 结束 -->
		</div>
		<div id="bgwrapright">
		    <div class="boxtitle">
		    	系统通告
		    	<span class="managebut"><a id="operNotice" onclick="goto();" href="javascript:void(0);">通告管理</a></span>
		    </div>
			 <!-- 通告信息 BLOCK 开始 -->
            <div class="tabbox">
			  <div id="infoBlock"></div>
			  <div id="infoBlockContent" ></div>
			</div> 
			<!-- 通告信息 BLOCK 结束 -->
		</div>
			<!-- End of bgwrap -->
	</div>
</form>
</body>


<script type="text/javascript">
   	var hightValue = screen.availHeight - 303;
   	var hightValueStr = "height:"+ hightValue + "px";
   	document.getElementById("mainTabTarget").setAttribute("style", hightValueStr);
   	document.getElementById("infoBlockContent").setAttribute("style", hightValueStr);
</script>

<script type="text/javascript">
 	
	dwrAsynService.isSysAdmin(function (f) {if (f) {
		document.getElementById("operNotice").style.display = "";
		//document.getElementById("operNotice").href = "<%=webapp%>/listNotice.action";
		//document.getElementById("operNotice").onclick = goto;
	}});
	function goto(){
		dwrAsynService.isSysAdmin(function (f) {if (f) {
		document.forms[0].action = "<%=webapp%>/listNotice.action";
		document.forms[0].submit();
	}});
	}
</script>
</HTML>
