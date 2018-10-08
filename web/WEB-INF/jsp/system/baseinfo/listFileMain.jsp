<%@ page language="java" contentType="text/html; charset=UTF-8" 
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../../common/include.jsp"%>
<%@include file="../../common/commen-ui.jsp"%>
<script type="text/javascript" src="<c:out value="${webapp}"/>/dwr/engine.js"></script>
<script type="text/javascript" src="<c:out value="${webapp}"/>/dwr/util.js"></script>
<script type="text/javascript" src="<c:out value="${webapp}"/>/dwr/interface/dwrAsynService.js"></script>
<script language="javascript">	


function submitForm(){
	document.forms[0].submit();
}
</script>
</head>
<body>
<form name="frm" action="<c:out value='${webapp}'/>/system/authority/listFileMain.action" method="post">
    <input type="hidden" name="fixQuery" value="<s:property value="fixQuery"/>"/>
    <input name="baseFolder.folderId" id="baseFolder.folderId" type="hidden"  value="<s:property value="baseFolder.folderId"/>"/>
	<div class="centercondition1">
	<table id="tbl_button" cellpadding="0" cellspacing="0">
		<tr align="left">
			<td align="left">
			 <a href="#" onClick="upload()"><img src="<c:out value="${webapp}"/>/themes/images/icons/icon23.png"/>上传文件</a>
			 <a href="#" onClick="uploadZIP()"><img src="<c:out value="${webapp}"/>/themes/images/icons/icon24.png"/>上传ZIP包</a>
			 <a href="#" onClick="downZIP()"><img src="<c:out value="${webapp}"/>/themes/images/icons/icon25.png"/>ZIP包下载</a>
			 <a href="#" onClick="beforeDeleteFile()" name="BtnReturn" id="BtnReturn"><img src="<c:out value="${webapp}"/>/themes/images/icons/icon18.png"/>文件删除</a>
			 <a href="#" onClick="changeStatus('1')" name="BtnReturn" id="BtnReturn"><img src="<c:out value="${webapp}"/>/themes/images/icons/icon26.png"/>文件启用</a>
			 <a href="#" onClick="changeStatus('0')" name="BtnReturn" id="BtnReturn"><img src="<c:out value="${webapp}"/>/themes/images/icons/icon27.png"/>文件停用</a>
			 <a href="#" onClick="synchronization()" name="BtnReturn" id="BtnReturn"><img src="<c:out value="${webapp}"/>/themes/images/icons/icon28.png"/>文件同步</a>
			</td>
		</tr>
	</table>
	<div id="lessGridList7">
		<table class="lessGrid" cellspacing="0" rules="all" border="0"
			cellpadding="0" display="none" style="border-collapse: collapse;width:100%">
			<tr class="lessGrid head" style="text-align:center;">
				<th width="4%"  style="text-align:center">
					<input id="CheckAll" style="width: 13px; height: 13px;"
						type="checkbox" onClick="checkAll(this,'fileIdList')" />
				</th>
				<th style="text-align:center">
					文件名
				</th>
				<th style="text-align:center">
					相关ID
				</th>
				<th style="text-align:center">
					子系统
				</th>
				<th style="text-align:center">
					类别
				</th>
				<th style="text-align:center">
					上传时间
				</th>
				<th style="text-align:center">
					上传人
				</th>
				<th style="text-align:center">
					大小
				</th>
				<th style="text-align:center;">
					状态
				</th>
				<th style="text-align:center;">
					下载
				</th>
			</tr>
			<s:iterator value="paginationList.recordList" id="iList"
				status="stuts">
				<tr align="center" class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>" >
					<td>
						<input type="checkbox"
							style="width: 13px; height: 13px;" name="fileIdList"
							value="<s:property value="#iList[0].fileId" />" />
					</td>
					<td>
						<s:property value="#iList[0].fileName" />
					</td>
					<td>
						<s:property value="#iList[0].refId" />
					</td>
					<td>
						<s:property value="#iList[1].baseConfig.systemCname" />
					</td>
					<td>
						<s:property value="#iList[1].folderName" />[<s:property value="#iList[1].folderCode" />]
					</td>
					<td>
						<s:date name="#iList[0].uploadTime" format="yyyy-MM-dd" />
					</td>
					<td>
					    <s:if test="#iList[0].createUserId!=null">
					        <s:property value="#iList[0].createUserId" />
					    </s:if>
					    <s:else>
					        <s:property value="#iList[0].createUserName" />
					    </s:else>
						
					</td>
					<td>
						<s:property value="#iList[0].fileSizeKB" />K
					</td>
					<td><s:if test="#iList[0].status eq 1">
					          启用
					    </s:if>
					    <s:else>
					          停用
					    </s:else>
					</td>
					<td>
						<a href="downLoadFile.action?fileId=<s:property value="#iList[0].fileId" />">
						   <img src="<c:out value="${webapp}"/>/themes/images/icons/down.png" style="border:0;" />
						</a>
					</td>
				</tr>
			</s:iterator>

		</table>
	</div>
	<div id="anpBoud" align="Right">
       <table width="100%" cellspacing="0" border="0">
           <tr>
               <td align="left"></td>
               <td align="right"><s:component template="pagediv"/></td>
           </tr>
       </table>
   </div>
   </div>
</form>

</html>
<script language="javascript">
function beforeDeleteFile() {
    var t = "";
	var inputs = document.getElementsByName('fileIdList');
	for ( var i = 0; i < inputs.length; i++) {
		if (inputs[i].checked == true) {
			t += inputs[i].value + ",";
		}
	}
	if (t.length == 0) {
		alert("请先选择要删除的文件！");
		return;
	}
	document.forms[0].action = "deleteFile.action";
	document.forms[0].submit();
}
function changeStatus(s){
    var t = "";
	var inputs = document.getElementsByName('fileIdList');
	for ( var i = 0; i < inputs.length; i++) {
		if (inputs[i].checked == true) {
			t += inputs[i].value + ",";
		}
	}
	if (t.length == 0) {
		alert("请先选择要修改状态的文件！");
		return;
	}
	document.forms[0].action = "changeStatus.action?status="+s;
	document.forms[0].submit();
}
function synchronization(){
    document.forms[0].action = "synchronization.action";
	document.forms[0].submit();
}
function upload(){
	    var node=window.parent.frames["leftFrame"].window.folderTree.selectedNode;
	    if(node==null){
	        alert("请先选择上传文件的类别");
	        return;
	    }
		retData = showModalDialog("createFile.action?action=upLoadFile&baseFolder.folderId="+node.getAttribute("id"), 
				  window, 
				  "dialogWidth:" + 500
				+ "px;dialogHeight:" + 350
				+ "px;center=1;scroll=1;help=0;status=0;");
	    if(retData){
			window.document.forms["frm"].submit();
		}
		
}
function uploadZIP(){
	    var node=window.parent.frames["leftFrame"].window.folderTree.selectedNode;
	    if(node==null){
	        alert("请先选择上传文件的类别");
	        return;
	    }
		retData = showModalDialog("createFile.action?action=upLoadZipFile&baseFolder.folderId="+node.getAttribute("id"), 
				  window, 
				  "dialogWidth:" + 500
				+ "px;dialogHeight:" + 350
				+ "px;center=1;scroll=1;help=0;status=0;");
	    if(retData){
			window.document.forms["frm"].submit();
		}
}
function downZIP(){
	    var node=window.parent.frames["leftFrame"].window.folderTree.selectedNode;
	    if(node==null){
	        alert("请先选择要下载的目录");
	        return;
	    }
	    window.location.href="downLoadZipFile.action?baseFolder.folderId="+node.getAttribute("id");

}


</script>