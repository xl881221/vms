<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<%@include file="common/include.jsp"%>
<link type="text/css"
	href="<c:out value="${webapp}"/>/themes/css/bar.css" rel="stylesheet" />
<title>伸缩栏</title>
<link href="<c:out value="${sysTheme}"/>/css/bar.css" type="text/css" rel="stylesheet">

<script type="text/javascript" >
	function bar_onclick(flag) {
            var mfp = window.parent.frames["mainFramePage"];
            var ifrm = parent.document.getElementById("frame");
            if (flag == true) {
                mfp.missWidth();
            } else {
                // 修改兼容性问题 2009-08-03 13:13 ShiCH
                if (ifrm.cols == "230,10,*") {
                    ifrm.cols = "0,10,*";
                    document.getElementById("ImgArrow").src = "<c:out value="${webapp}"/>/themes/images/icons/right.png";
                    if (mfp.mess && typeof (mfp.mess) === "function") {
                        mfp.mess(1);
                    }
                } else {
                    ifrm.cols = "230,10,*"
                    document.getElementById("ImgArrow").src = "<c:out value="${webapp}"/>/themes/images/icons/left.png";
                    if (mfp.mess && typeof (mfp.mess) === "function") {
                        mfp.mess(2);
                    }
                }
            }
        }
</script>
</head>
<body>
	<TABLE id="Table1" height="100%" width="20" cellpadding=0 cellspacing=0>
		<TR>
			<TD valign="top" height="100%" width="100%"><IMG id="ImgArrow"
				onclick="bar_onclick(false)"
				src="<c:out value="${webapp}"/>/themes/images/icons/left.png"
				name="ImgArrow" height="40"></TD>
		</TR>
	</TABLE>
</body>
</html>
