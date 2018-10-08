<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="fmss.dao.entity.UBaseNoticeInfoDO"%>

<%
	UBaseNoticeInfoDO n = (UBaseNoticeInfoDO) request
			.getAttribute("notice");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >

<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv='Expires' content='0'>
<meta http-equiv='Pragma'  content='no-cache'>
<meta http-equiv='Cache-Control' content='no-cache'>
<%@include file="../../common/include.jsp"%>
<script type="text/javascript"
	src="<c:out value="${webapp}"/>/js/types.js"></script>
<script type="text/javascript"
	src="<c:out value="${webapp}"/>/dwr/engine.js"></script>
<script type="text/javascript"
	src="<c:out value="${webapp}"/>/dwr/util.js"></script>
<script type="text/javascript"
	src="<c:out value="${webapp}"/>/dwr/interface/dwrAsynService.js"></script>
<link href="<c:out value="${sysTheme}"/>/css/subWindow.css"
	type="text/css" rel="stylesheet">
<style>body{overflow: auto;}</style>
<title>通告查看</title>
<script type="text/javascript">
	<!--
		function init() {
			writeContent();
			var s = document.getElementById("affixList");
			var rs = s.rows;
			var t, n, m;
			for (var i=1; i<rs.length; i++) {
				t = _TYPES["def"];
				n = rs[i].cells[1].innerText.replace(/^\s+|\s+$/g, "");
				m = n.lastIndexOf(".");
				if (m != -1 && _TYPES[n.substring(m+1).toLowerCase()])
					t = _TYPES[n.substring(m+1).toLowerCase()];
				rs[i].cells[0].innerHTML += "<img onclick='viewFile(this)' src='<%=webapp%>" 
							+ t + "' align=absmiddle style='cursor:hand;'>";
			}
		}
		
		function writeContent() {
			var html = '<html><head><LINK href="<%=webapp%>/js/fck/css/fck_editorarea.css" type=text/css rel=stylesheet>' + 
					'<LINK href="<%=webapp%>/js/fck/css/fck_internal.css" type=text/css rel=stylesheet></head><body>' +
					document.getElementById("nContent").value + '</body></html>';
			var doc = document.frames["contFrame"].window.document;
			doc.write(html);
		}
		
		function viewFile(e) {
			var vs = e.parentElement.parentElement;
			vs.getElementsByTagName("a")[0].click();
		}
		
		function saveFeedBack() {
			if(fucCheckNull(document.getElementById("content"),"请填写反馈内容") == false){
			   return false;
			}
			if(fucCheckLength(document.getElementById("content"),"反馈内容不能多于2000个字符，1000个汉字") == false){
			   return false;
			}

			var o = {noticeId:document.formNotice("notice.id").value, content: document.getElementById("content").value};
			dwrAsynService.saveFeedBack(o, function (f) {
				if (f) {
					document.getElementById("content").value = "";
					alert("保存成功！");
				} else 
					alert("保存失败，请重新执行操作！");
			});
		}
	-->
	</script>
</head>
<body onload="init()" scroll=auto>
<form name="formNotice"><s:hidden id="nContent"
	name="notice.content"></s:hidden> <s:hidden name="notice.id"></s:hidden>
<div id="editsubpanel" class="editsubpanel">
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
	<tr class="header">
		<th colspan="4">通告信息</th>
	</tr>
	<tr>
		<td width="12%" align="left">通告标题：</td>
		<td width="55%" style="word-break:break-all;"><s:property value="notice.title"/></td>
		<td width="15%" align="left">通告类型：</td>
		<td><s:property value="notice.type"/></td>
	</tr>
	<tr>
		<td colspan="4" align="center"><iframe
			style="border: 1px solid #bcbcbc;" name="contFrame" id="contFrame"
			frameBorder=0 width="100%" height="150"></iframe></td>
	</tr>
	<tr>
		<td colspan="4">附件列表：</td>
	</tr>
	<tr>
		<td colspan=4 height="100px" valign="top" align="left">
		<table style="width:100%" class="lessGridTemp" id="affixList" cellspacing="0" rules="all"	border="0" cellpadding="0" 	style="border-collapse: collapse;">
			<tr class="lessGridTemp headTemp" style="width:100%">
				<th style="width:40%" colspan="2">名称</th>
				<th style="width:15%">大小&nbsp;</th>
				<th style="width:15%">上传时间</th>
				<th style="width:20%">上传人</th>
			</tr>
			<s:iterator value="notice.affixs" id="a" status="stuts">
				<tr class="<s:if test="#stuts.odd==true">lessGridTemp rowA</s:if><s:else>lessGridTemp rowB</s:else>">
					<td width="20" align="right"></td>
					<td><a href="downloadAttach.action?id=<s:property value="#a.fileId"/>"><s:property
						value="#a.fileName" /></a>&nbsp;</td>
					<td align="right"><s:property value="#a.fileSizeKB" />&nbsp;&nbsp;KB&nbsp;
					</td>
					<td><s:property value="#a.uploadTime2" />&nbsp;</td>
					<td><s:property value="#a.uploadUser" />&nbsp;</td>
				</tr>
			</s:iterator>
		</table>
		</td>
	</tr>
	<tr>
		<td colspan="4">
			<table class="lessGrid" cellspacing="0" rules="all" border="0" cellpadding="0" style="border-collapse: collapse;">
				<tr class="lessGrid head">
					<th colspan="2" align="left">添加反馈：</th>
				</tr>
				<tr>
					<td>
						<textarea name="content" id="content" rows="4" style="width: 100%;"></textarea>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
<div id="ctrlbutton" align="right" style="padding: 5px;">
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"  onClick="saveFeedBack()" value="提交"/>
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"  onClick="window.close()" value="关闭"/>
</div>
</div>
</form>
</body>
</html>
