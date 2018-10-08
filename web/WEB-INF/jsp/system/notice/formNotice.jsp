<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv='Expires' content='0'>
	<meta http-equiv='Pragma'  content='no-cache'>
	<meta http-equiv='Cache-Control' content='no-cache'>
	<%@include file="../../common/include.jsp"%>
	<base target="_self">
	<meta http-equiv='pragma' content='no-cache'>
	<meta http-equiv='cache-control' content='no-cache'>  
	<META    HTTP-EQUIV="Expires"    CONTENT="0">
		
	<script type="text/javascript" src="<c:out value="${webapp}"/>/js/types.js"></script>
	<script type="text/javascript" src="<%=webapp%>/js/fck/fckeditor.js"></script>
	<link type="text/css" href="<c:out value="${sysTheme}"/>/css/subWindow.css" rel="stylesheet">
	<style>
		body { overflow: auto; }
	</style>
	<title><s:if test="notice.id!=null&&notice.id!=''">修改通告</s:if><s:else>新增通告</s:else></title>
	<script language="javascript" type="text/javascript">
	<!--
		//标识页面是否已提交
		String.prototype.trim = function () {
			return this.replace(/(^\s*)|(\s*$)/g, "");
		};
		String.prototype.len = function() {
		    var realLength = 0, len = this.length, charCode = -1;
		    for (var i = 0; i < len; i++) {
		        charCode = this.charCodeAt(i);
		        if (charCode >= 0 && charCode <= 128) realLength += 1;
		        else realLength += 2;
		    }
		    return realLength;
		};
		function lengthCheck(text, size) {
			var len = 0;
			for(var i=0; i < text.length; i++) {
				var ech = escape(text.charAt(i));
				if (ech.length > 4)
					len++;
				len++;
			}
			if (len > size)
				return true;
			return false;
		}
		function checkForm() {
			if(fucCheckNull(document.formNotice("notice.title"), "通告标题不能为空") == false){
			   return false;
			}
			document.formNotice("notice.title").value=CovertFromXmlTag(document.formNotice("notice.title").value);
			if (fucCheckLength(document.formNotice("notice.title"), 200, "通告标题不能多于200个字符，100个汉字") == false) {
				return false;
			}
			var html = document.frames(0).window.document.frames(0).window.document.body.innerHTML;
			var reg = /^\<p\>(\&nbsp\;)+\<\/p\>$/i;
			if (html == "" || reg.test(html)) {
				alert("通告内容不能为空！");
				return false;
			} else if (lengthCheck(html, 4000)) {
				alert("通告内容过长！"); return false;
			}
			return true;
		}
		
		var _n = 0;
		function addFile() {
			var vs = event.srcElement;
			var nvs = vs.cloneNode();
			document.getElementById("fileCont").appendChild(nvs);
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
			tr.insertCell().innerHTML = "<img src='<%=sysTheme%>/img/jes/icon/import.png' align=absmiddle>&nbsp;" + fn;
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
		function init() {
			var s = document.getElementById("affixList");
			if (!s) return;
			var rs = s.rows;
			var t, n, m;
			for (var i=1; i<rs.length; i++) {
				t = _TYPES["def"];
				n = rs[i].cells[1].innerText.replace(/^\s+|\s+$/g, "");
				m = n.lastIndexOf(".");
				if (m != -1 && _TYPES[n.substring(m+1).toLowerCase()])
					t = _TYPES[n.substring(m+1).toLowerCase()];
				rs[i].cells[0].innerHTML += "<img src='<%=webapp%>" + t + "' align=absmiddle>";
			}
		}		
		var isSubmit = false;
		function findOutSubmit() {
			if (isSubmit) {
				alert("信息正在发送给服务器，请不要重复提交信息！");
				return;
			}
			if (!checkForm()) return;
			isSubmit = true;
			document.formNotice.submit();
			addWaiting();
		}
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
		function CovertFromXmlTag(value) {
		    value = value.split("&").join("");
		    value = value.split("<").join("");
		    value = value.split(">").join("");
		    value = value.split('"').join("");
		    value = value.split(" ").join("");
		    value = value.split("\r").join("");
		    value = value.split("\n").join("");
		    return value;
		}
	-->

	</script>
</head>
<body onload="init()" scroll="yes">
	<div class="showBoxDiv">
	<form name="formNotice" action="saveNotice.action" target="formFrame" method="post" enctype="multipart/form-data">
		<s:hidden name="notice.id"></s:hidden>
		<div id="editpanel">
			<div id="editsubpanel" class="editsubpanel">
				<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
					<tr class="header">
						<th colspan="4"><s:if test="notice.id!=null&&notice.id!=''">修改通告</s:if>
						<s:else>新增通告</s:else></th>
					</tr>
					<tr align="left">
						<td>通告标题</td>
						<td><s:textfield name="notice.title" size="30"></s:textfield>&nbsp;
						<span class="spanstar">*</span></td>
						<td>通告类型</td>
						<td><s:select list="types" name="notice.type" listKey="key"
							listValue="value"></s:select>&nbsp; <span class="spanstar">*</span></td>
					</tr>
					<tr class="header">
						<th colspan="4" align="left">通告内容(不能超过4000字符)&nbsp; <span class="spanstar">*</span>
						</th>
					</tr>
					<tr>
						<td colspan="4">
							<s:hidden id="notice.content" name="notice.content"></s:hidden>
							<script type="text/javascript">
							<!--
								var oFCKeditor = new FCKeditor('notice.content') ;
								oFCKeditor.BasePath	= "<%=webapp%>/js/fck/" ;
								oFCKeditor.Height = 220 ;
								oFCKeditor.Value = document.getElementById("notice.content").value;
								oFCKeditor.Create() ;
							//-->
							</script>
						</td>
					</tr>
					<s:if test="notice.id!=null&&notice.id!=''">
						<tr style="display:none">
							<td colspan="4">已上传附件列表</td>
						</tr>
						<tr style="display:none">
							<td colspan=4  valign="top" align="center">
								<table class="lessGridTemp" cellspacing="0" rules="all"
									border="0" cellpadding="0" 
									style="border-collapse: collapse;">
									<tr class="lessGridTemp headTemp">
										<th colspan="2" style="text-align:center;width:30%;">名称</th>
										<th style="text-align:center;width:20%;">大小</th>
										<th style="text-align:center;width:30%;">上传时间</th>
										<th style="text-align:center;width:20%;">上传人</th>
										<th style="text-align:center;width:10%;">操作</th>
									</tr>
								</table>
								<div style="height:70px;width:690px;overflow:auto">
									<table class="lessGridTemp" id="affixList" cellspacing="0" rules="all"
										border="0" cellpadding="0" display="none"
										style="border-collapse: collapse;">
										<s:iterator value="notice.affixs" id="a" status="stuts">
												<tr
													class="<s:if test="#stuts.odd==true">lessGridTemp rowA</s:if><s:else>lessGridTemp rowB</s:else>">
													<td align="right" width="20"><input type="hidden" name="nIds"
														disabled value="<s:property value="#a.fileId"/>">
												    </td>
													<td><s:property value="#a.fileName" />&nbsp;</td>
													<td width="110"><s:property value="#a.fileSize" />&nbsp;&nbsp;KB&nbsp;</td>
													<td width="100"><s:date name="#a.uploadTime" format="yyyy-MM-dd"/>&nbsp;</td>
													<td width="100"> <s:property value="#a.createUserName" />&nbsp;</td>
													<td width="40"><a href="javascript:;" onclick="delRow(this)">删除</a></td>
												</tr>
										</s:iterator>
									</table>
								</div>
							</td>
						</tr>
					</s:if>
					<tr style="display:none">
						<td colspan=4 id="fileCont">上传附件(附件大小最好不要超过10M)
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a
							href="javascript:;" style="color: red;" hidefocus>选择附件...</a> <input
							name=file onchange="addFile()" type=file
							style='position: relative; filter: alpha(opacity = 0); left: -64; width: 0px; height: 15; cursor: hand;-moz-opacity:0;'
							id=upfile hidefocus></td>
					</tr>
					<tr>
						<td colspan=4  valign="top" align="left">
							<table style="width:100%;" class="lessGridTemp" border="0">
								<tr class="lessGridTemp headTemp">
									<th style="text-align:center;width:80%;">附件名称</th>
									<th style="text-align:center;width:20%;">删除附件</th>
								</tr>
							</table>
							<div style="height:70px;width:690px;overflow:auto">
								<table class="lessGrid" border="0" id="fileList">
								</table>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
		<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
			<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="findOutSubmit()" value="保存"/>
			<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="window.close()" value="关闭"/>
		</div>
	</form>
	</div>
	<iframe id="formFrame" name="formFrame" style="display: none;"></iframe>
</body>
</html>
