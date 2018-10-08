<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@include file="../../common/include.jsp"%>
	<%@include file="../../common/commen-ui.jsp"%>
	<script type="text/javascript" src="<c:out value="${webapp}"/>/js/SimpleTree/js/ajax.js"></script>
	<script type="text/javascript" src="<c:out value="${webapp}"/>/js/SimpleTree/js/simpleTree.js"></script>
	<link href="<c:out value="${webapp}"/>/js/SimpleTree/css/simpleTree.css" rel="stylesheet" type="text/css">
	
	<script language="javascript">
		function displayTreeInst(){
			var t = document.getElementById("treeboxbox_tree");
			var iframe = parent.document.getElementById("frame");
			if(iframe.cols=="300,*")
	        {
				iframe.cols="0,*";
	        }
			else
			{
				iframe.cols="300,*";
				var t = document.getElementById("treeboxbox_tree");
			}
		}
		window.onload = init;
		var folderTree = null;
		function init(){
			WaitingLayer.show();
			var p = new ajax();
			p.url = "<%=webapp%>/loadFolderXml.action?X=" + (new Date()).getTime();
			folderTree = new SimpleTree();
			p.onresult = function(){
				//WaitingLayer.hide();
				if(null == this.data) return;
				folderTree.container = folderTree.$("simpleTree");
				folderTree.iconPath = folderTree.$("simpleTree").getAttribute("iconPath");
				folderTree.dataStr = this.text;
				folderTree.radioBox = true;
				folderTree.updateBox = true;
				folderTree.init();
				folderTree.OnClick = function(){
				    var treeNode = this.selectedNode;
			            var id = treeNode.getAttribute("id");
			            var level = treeNode.getAttribute("levelType");
						parent.frames["fileSearchResultFrame"].location.href="<c:out value='${webapp}'/>/system/authority/listFileMain.action?baseFolder.folderId="+id+"&fixQuery=true";;
				}
		        folderTree.GetAnalisyNodes = function(id){
		        	WaitingLayer.show();
		            var p = new ajax();
		            p.url = "<%=webapp%>/loadFolderXml.action?" + (new Date()).getTime()+ "&id="+id;
		            p.onresult = function(){
		                var data = this.text;
		                folderTree.BuildAnalisyNodes(id,data);
		                WaitingLayer.hide();
		            }
		            p.send();
		        }
			}
			p.send();
		}
	</script>
</head>
<body topmargin="0" leftmargin="0" rightmargin="0" class="pl12">
	<div class="p-station">
	<table id="treetitlebar" class="tree_top_bar" width="100%"  cellpadding="1" cellspacing="1">
		<tr>
			<td align="left">文件存储路径列表</td>
			<td align="right" valign="top">
            </td>
		</tr>
	</table>
	<div id="treeboxbox_tree" class="treeContainerBox" style="overflow:auto">
		<div class="treetopbut">
			<span class="topbut"><a class="width_but1" onclick="init();" href="#">刷新</a></span>
		</div>
		<div id="simpleTree" iconPath="<c:out value="${webapp}"/>/js/SimpleTree/images/">loading...</div>
	</div>
	</div>
</body>
<script type="text/javascript">
	document.getElementById("treeboxbox_tree").style.height = screen.availHeight - 316;
</script>
</html>
