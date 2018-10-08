<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@include file="../../common/include.jsp"%>
	<script type="text/javascript" src="<c:out value="${webapp}"/>/js/SimpleTree/js/ajax.js"></script>
	<script type="text/javascript" src="<c:out value="${webapp}"/>/js/SimpleTree/js/simpleTree.js"></script>
	<link href="<c:out value="${webapp}"/>/js/SimpleTree/css/simpleTree.css" rel="stylesheet" type="text/css">
	<link rel="stylesheet" href="<c:out value="${webapp}"/>/js/zTree/css/demo.css" type="text/css">
	<link rel="stylesheet" href="<c:out value="${webapp}"/>/js/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="<c:out value="${webapp}"/>/js/zTree/js/jquery-1.4.4.min.js"></script>
	<script type="text/javascript" src="<c:out value="${webapp}"/>/js/zTree/js/jquery.ztree.core-3.5.js"></script>
	<%@include file="../../common/commen-ui.jsp"%>
	
	<script language="javascript">
		function displayTreeInst(){
			var t = document.getElementById("treeboxbox_tree");
			if(parent.frame.cols=="300,*")
	        {
				parent.frame.cols="0,*";
	        }
			else
			{
				parent.frame.cols="300,*";
			}
		}
		window.onload = init;
		//var instTree = null;
		function init(){
			//if(window.ActiveXObject){	
			//	WaitingLayer.show();
			//	var p = new ajax();
			//	p.url = "<%=webapp%>/loadInstAndUsrXml.action?X=" + (new Date()).getTime();
			//	instTree=new SimpleTree();
			//	p.onresult = function(){
			//		WaitingLayer.hide();
			//		if(null == this.data) return;
			//		instTree.container = instTree.$("simpleTree");
			//		instTree.iconPath = instTree.$("simpleTree").getAttribute("iconPath");
			//		instTree.dataStr = this.text;
			//		instTree.radioBox = true;
			//		instTree.updateBox = true;
					
			//		instTree.OnClick = function(){
			//		    var b=parent.frames["InstSearchResultFrame"].document.getElementById("BtnMove");
			//		    if(b.a!=1||b.a==undefined){
			//		       var treeNode = this.selectedNode;
			//               var id = treeNode.getAttribute("id");
			//               var level = treeNode.getAttribute("levelType");
			//			   parent.frames["InstSearchResultFrame"].location.href="<c:out value='${webapp}'/>/system/authority/listInstMain.action?inst.instId="+id + "&fixQuery=true";
			//		    }else{
			//		       if(window.confirm('你确定要移动到此机构？')){
			//		          var t="";
			//		          var inputs = parent.frames["InstSearchResultFrame"].document.getElementsByName('ids');
			//					for ( var i = 0; i < inputs.length; i++) {
			//						if (inputs[i].checked == true) {
			//							t += inputs[i].value + ",";
			//						}
			//				  }
			//		          var p2 = new ajax();
			//		          p2.url = "<%=webapp%>/moveInst.action?ids="+t+"&dest="+this.selectedNode.getAttribute("id");
			//		          p2.onresult= function(){
			//		              eval("o="+this.text);
			//		              alert(o.msg);
			//		              if(o.success){
			//		                 parent.frames["InstSearchResultFrame"].document.getElementById('frm').submit();
			//		              }else{
			//		                 parent.frames["InstSearchResultFrame"].document.getElementById("BtnMove").disabled='';
			//		                 parent.frames["InstSearchResultFrame"].document.getElementById("BtnMove").click();
			//		              }
			//		          }
			//		          parent.frames["InstSearchResultFrame"].document.getElementById("BtnMove").disabled='disabled';
			//		          p2.send();
			//		       } 
			//		    }
			//		}
			//		instTree.GetAnalisyNodes = function(id){
			///        	WaitingLayer.show();
			//            var p = new ajax();
			//            p.url = "<%=webapp%>/loadInstAndUsrXml.action?" + (new Date()).getTime()+ "&id="+id;
			 //           p.onresult = function(){
			 ///               var data = this.text
			 //               instTree.BuildAnalisyNodes(id,data);				    
			 //               WaitingLayer.hide();
			//            }
			//            p.send();
			//        }
			//        instTree.init();
			//	}
			//	p.send();
			//} else {
				var setting = {
						data: {
							simpleData: {
								enable: true
							}
							,treeId:'instTree'
						},
						callback:{
							onClick:function zTreeOnClick(event, treeId, treeNode){
								instTree = treeNode;
								document.getElementById("checkedTreeNodeId").value = treeNode.id;
								document.getElementById("checkedTreeNodeName").value = treeNode.name;
								parent.frames["InstSearchResultFrame"].location.href="<c:out value='${webapp}'/>/system/authority/listInstMain.action?inst.instId="+treeNode.id + "&fixQuery=true";
							}
						}
				};
				$.ajax({
		            type: "Post",
		            url: "loadInstXmlZTree.action?X= + (new Date()).getTime()",
		            dataType: "json", //可以是text，如果用text，返回的结果为字符串；如果需要json格式的，可是设置为json
		            success: function (data) {
		                $.fn.zTree.init($("#simpleTree"), setting, data).expandAll(false);
		            },
		            error: function (msg) {
		                alert(" 数据加载失败！" + msg);
		            }
		        });
		    //}
		}
		function refreshTreeInst(){
		    $.fn.zTree.destroy('instTree');
			init();
		}
	</script>
</head>
<body topmargin="0" leftmargin="0" rightmargin="0" class="pl12">
	<div class="p-station">
		<table id="treetitlebar" class="tree_top_bar" width="100%" >
			<tr>
				<td align="left">机构列表</td>
				<td align="right" valign="top">
				<input type="hidden" id="checkedTreeNodeId"  name="checkedTreeNodeId" value="">
				<input type="hidden" id="checkedTreeNodeName"  name="checkedTreeNodeName" value="">
	            </td>
			</tr>
		</table>	
		<div id="treeboxbox_tree" class="treeContainerBox"  style="overflow:auto">
			<div class="treetopbut">
				<span class="topbut"><a class="width_but1" onclick="refreshTreeInst();" href="#">刷新</a></span>
			</div>			
			<div id="simpleTree" iconPath="<c:out value="${webapp}"/>/js/SimpleTree/images/">loading...</div>
		</div>
	</div>
	
</body>
<script type="text/javascript">
	document.getElementById("treeboxbox_tree").style.height = screen.availHeight - 314;
</script>
</html>
