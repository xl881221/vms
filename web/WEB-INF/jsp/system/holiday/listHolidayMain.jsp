<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%@include file="../../common/include.jsp"%>
</head>
<body>
    <form name="frm" action="" method="post" onSubmit="return Validator.Validate(this,3)">
	 	<div style="overflow: auto; width: 100%; height: 390px;" id="list1" class="list">

	    </div>
	    <div id="anpBoud" align="Right" style="width:100%;">
	        <table width="100%" cellspacing="0" border="0">
	            <tr>
	                <td align="left"></td>
	                <td align="right"><ww:component template="pagediv"/></td>
	            </tr>
	        </table>
	    </div>
    </form>
</body>

</html>
