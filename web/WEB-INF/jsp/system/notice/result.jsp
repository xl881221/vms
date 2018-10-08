<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<html>
<head>
	<meta http-equiv='Expires' content='0'>
	<meta http-equiv='Pragma'  content='no-cache'>
	<meta http-equiv='Cache-Control' content='no-cache'>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%@include file="../../common/include.jsp"%>

	<script type="text/javascript">
		var msg = "<s:property value='resultMessages'/>";
		msg = msg.replace(/&lt;/g, "<");
		msg = msg.replace(/&gt;/g, ">");
		msg = msg.replace(/&quot;/g, "\"");
		document.write(msg);
		
	function CloseWindow(needReload){ 
		if(typeof(needReload) == 'undefined'){
			needReload 	= false;
		}		
		window.returnValue = needReload;
		
	     var ua = navigator.userAgent; 
	     var ie = navigator.appName=="Microsoft Internet Explorer" ? true : false; 
	     if(ie){
			 var IEversion = parseFloat(ua.substring(ua.indexOf("MSIE ")+5, ua.indexOf(";",ua.indexOf("MSIE ")))); 
			 if( IEversion< 5.5){
					 var str = '';
					 document.body.insertAdjacentHTML("beforeEnd", str);
					 document.all.noTipClose.Click(); 
			    } else {
			     window.opener =null; window.close();
			    }
		   }else{ 
		   		window.close() 
		   }
	}
		
	</script>
</head>
<body />
</html>

