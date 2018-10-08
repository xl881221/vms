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
	<link rel="stylesheet" href="<c:out value="${webapp}"/>/js/zTree/css/demo.css" type="text/css">
	<link rel="stylesheet" href="<c:out value="${webapp}"/>/js/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="<c:out value="${webapp}"/>/js/zTree/js/jquery-1.4.4.min.js"></script>
	<script type="text/javascript" src="<c:out value="${webapp}"/>/js/zTree/js/jquery.ztree.core-3.5.js"></script>
	
	<script language="javascript">

		function treeInit(){
			tree=new dhtmlXTreeObject("treeboxbox_tree","100%","100%",0);
			tree.setImagePath("<c:out value="${sysTheme}"/>/tree/imgs/");
			tree.enableCheckBoxes(0);
			try{
				var date = new Date();
				tree.setXMLAutoLoading("<%=webapp%>/loadInstAndUsrXml.action?"+date.getTime());
				tree.loadXMLExt("<%=webapp%>/loadInstAndUsrXml.action?"+date.getTime());
			}catch(ex){
			}
			deptId = tree.getFirstNodeId();
			//tree.selectItem(deptId,true);
			tree.openItem(deptId);
		}
		function displayTreeInst(){
			var t = document.getElementById("treeboxbox_tree");
			var iframe = parent.document.getElementById("frame");
			if(iframe.cols=="200,*")
	        {
				iframe.cols="0,*";
	        }
			else
			{
				iframe.cols="200,*";
				var t = document.getElementById("treeboxbox_tree");
			}
		}
		
		window.onload = init;
		var instTree = null;
		function init(){
			//if(window.ActiveXObject){	
			//	WaitingLayer.show();
			//	var p = new ajax();
			//	p.url = "<%=webapp%>/loadInstAndUsrXml.action?X=" + (new Date()).getTime();
			//	instTree = new SimpleTree();
			//	p.onresult = function(){
			//		WaitingLayer.hide();
			//		if(null == this.data) return;
			//		instTree.container = instTree.$("simpleTree");
			//		instTree.iconPath = instTree.$("simpleTree").getAttribute("iconPath");
			//		instTree.dataStr = this.text;
			//		instTree.radioBox = true;
			//		instTree.updateBox = true;
			//		instTree.init();
			//		instTree.OnClick = function(){
			//		    var treeNode = this.selectedNode;
			//	            var id = treeNode.getAttribute("id");
			//	            var level = treeNode.getAttribute("levelType");
			//				parent.frames["onlineSearchResultFrame"].location.href="<c:out value='${webapp}'/>/system/authority/listOnlineMain.action?user.instId="+id+"&fixQuery=true";;
			//		}
			//        instTree.GetAnalisyNodes = function(id){
			//        	WaitingLayer.show();
			//            var p = new ajax();
			//            p.url = "<%=webapp%>/loadInstAndUsrXml.action?" + (new Date()).getTime()+ "&id="+id;
			//            p.onresult = function(){
			//                var data = this.text;
			//                instTree.BuildAnalisyNodes(id,data);
			//                WaitingLayer.hide();
			//            }
			//            p.send();
			//        }
			//	}
			//	p.send();
			//} else {
			 var setting = {
				data: {
					simpleData: {
						enable: true
					}
				},
				callback:{
					onClick:function zTreeOnClick(event, treeId, treeNode){
						parent.frames["onlineSearchResultFrame"].location.href="<c:out value='${webapp}'/>/system/authority/listOnlineMain.action?user.instId="+treeNode.id+"&fixQuery=true";;
					}
				}
			};
			$.ajax({
	            type: "Get",
	            url: "loadInstXmlZTree.action?X= + (new Date()).getTime()",
	            dataType: "json", //可以是text，如果用text，返回的结果为字符串；如果需要json格式的，可是设置为json
	            success: function (data) {
	                $.fn.zTree.init($("#simpleTree"), setting, data).expandAll(true);
	            },
	            error: function (msg) {
	                alert(" 数据加载失败！" + msg);
	            }
	        });
		//}
		}
	</script>
</head>
<body topmargin="0" leftmargin="0" rightmargin="0" class="pl12">
	<div class="p-station">
	<table id="treetitlebar" class="tree_top_bar" width="100%"  cellpadding="1" cellspacing="1">
		<tr>
			<td align="left">机构列表</td>
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
