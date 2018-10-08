<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="fmss.dao.entity.LoginDO,fmss.common.util.Constants"%>

<%@include file="../../common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../../common/include.jsp"%>
<title>上传文件</title>
<link type="text/css" href="<c:out value="${sysTheme}"/>/css/subWindow.css" rel="stylesheet">
<link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
<script type="text/javascript" src="<c:out value="${webapp}"/>/js/types.js"></script>
<link rel="stylesheet" type="text/css" href="css/easyui.css">
<script language="javascript" type="text/javascript">
function addWaiting() {
	var div = document.body.appendChild(document.createElement("div"));
	var temp = div.appendChild(document.createElement("iframe"));
	temp.frameborder = 0;
	temp.style.filter = "alpha(opacity=30)";
	temp.style.backgroundColor = "#aaaaaa";
	temp.style["-moz-opacity"] = 30;
	temp.style.position = "absolute";
	temp.style.left = 0;
	temp.style.top = 0;
	temp.style.width = document.body.clientWidth;
	temp.style.height = document.body.clientHeight;
	var w = div.appendChild(document.createElement("div"));
	w.style.position = "absolute";
	w.style.left = 0;
	w.style.top = 180;
	w.style.width = document.body.clientWidth;
	w.align = "center";
	w.innerHTML = "<img src='<%=sysTheme%>/img/loading_16x16.gif' align=absmiddle>&nbsp;正在保存...";
	document.body.style.overflow = "hidden";
	window._WAIT = div;
}
function clearWaiting() {
	if (window._WAIT) window._WAIT.removeNode(true);
	document.body.style.overflow = "auto";
}
function findOutSubmit() {		
	document.formFile.submit();
	addWaiting();
}
var _n = 0;
function addFile() {
	var vs = event.srcElement;
	var nvs = vs.cloneNode();
	document.getElementById("filesCont").appendChild(nvs);
	vs.id = "doc_file_" + (++_n);
	vs.style.display = "none"; 
	var fn = vs.value.replace(/\\/g, "/");
	var t = _TYPES["def"];
	var m = fn.lastIndexOf(".");
	if (m != -1 && _TYPES[fn.substring(m+1).toLowerCase()])
		t = _TYPES[fn.substring(m+1).toLowerCase()];
	var tab = document.getElementById("fileList");
	var tr = tab.insertRow(); 
	tr.id = "doc_view_" + _n;
	tr.insertCell().innerHTML = "<img src='<%=webapp%>" + t + "' align=absmiddle>&nbsp;" + fn;
		tr.insertCell().innerHTML = "&nbsp;&nbsp;<span style='color:red;cursor:hand;font-weight:bold;' title='删除' onclick='delFile(" 
									+ _n + ")'>×</span>";
									
}
function delFile(v) {
	document.getElementById("doc_file_" + v).removeNode(true);
	document.getElementById("doc_view_" + v).removeNode(true);
	if (document.getElementById("fileList").rows.length == 0)
		_n = 0;
}
function delRow(e) {
	var vs = e.parentElement.parentElement;
	vs.getElementsByTagName("input")[0].disabled = "";
	vs.style.display = "none";
}
</script>
</head>
<body>
<div class="showBoxDiv">
<form name="formFile" id="formFile" action="<s:property value="action"/>.action" method="post" enctype="multipart/form-data">
 <input name="baseFolder.folderId" id="baseFolder.folderId" type="hidden"  value="<s:property value="baseFolder.folderId"/>"/>
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr class="header">
		<th colspan="4" id="filesCont">
			上传文件(文件大小最好不要超过10M)
			<a
			href="javascript:;" style="color: red;" hidefocus>选择文件...</a> <input
			name=files onchange="addFile()" type=file
			style='position: relative; filter: alpha(opacity = 0); left: -64; width: 0px; height: 15; cursor: hand;-moz-opacity:0;'
			id=upfile hidefocus>
		</th>
	</tr>
	<tr>
		<td colspan=4  valign="top" align="center">
			<div style="height:170px;width:453;overflow:auto">
				<table class="lessGrid" border="0" id="fileList"></table>
			</div>
		</td>
	</tr>
</table>

<div id="ctrlbutton" class="ctrlbutton" style="border:0px">		
<div type="btn" onclick="findOutSubmit()" name="BtnSave" value="保存"  img="<c:out value="${webapp}"/>/image/button/save.gif" id="BtnSave"></div>		
<div style="margin-left:5px"type="btn" onClick="window.close()" name="BtnReturn" value="关闭" img="<c:out value="${webapp}"/>/image/button/cancel.gif" id="BtnReturn"></div>
</div>
</form>
</div>
</body>
</html>
