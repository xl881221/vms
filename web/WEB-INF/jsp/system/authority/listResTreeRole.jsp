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
		
				
		PAGE_CACHE = {};

		function createAuthResMap(){
			var date = new Date();
			var url = '<c:out value="${webapp}"/>/createAuth.action?'+date.getTime();
			OpenModalWindow(url,500,350,true);	
		}
	
		function edit(){
			/**if (window.ActiveXObject) {
				var treeNode = PAGE_CACHE.tree.GetSelectedTreeNodes();
				if(null == treeNode) return;
				var id = treeNode.getAttribute("id");
				var level = treeNode.getAttribute("levelType");
	
				if(level==1){
				}else{
					var url = "<c:out value='${webapp}'/>/editAuth.action?roleId="+id;
					OpenModalWindow(url,500,350,true);
				}
			}else{*/
				var zTree = $.fn.zTree.getZTreeObj("simpleTree");//获取ztree对象 
		        var node=zTree.getSelectedNodes();
		        if(node[0]==null || node[0].isParent){
		        	alert("请选择角色信息。");
		        	return;
		        } else {
			        var id = node[0].pIds;
			        var url = "<c:out value='${webapp}'/>/editAuth.action?roleId="+id;
					OpenModalWindow(url,500,350,true);
				}
			/**}*/
		}
		
		function deleteRole(){
			/**if (window.ActiveXObject) {
				var treeNode = PAGE_CACHE.tree.GetSelectedTreeNodes();
				if(null == treeNode) return;
				var id = treeNode.getAttribute("id");
				var level = treeNode.getAttribute("levelType");
				
				if(level==1){
				}else{
					if (confirm('确定要删除当前选中角色吗？')) {
					
						document.forms[0].action="<c:out value='${webapp}'/>/deleteAuth.action?returnView=1&roleId="+id;
						document.forms[0].roleId.value=id;
						document.forms[0].roleId.returnView="1";
						document.forms[0].submit();
					}
				}
			} else {*/
				var zTree = $.fn.zTree.getZTreeObj("simpleTree");//获取ztree对象 
		        var node=zTree.getSelectedNodes();
		       	if(node[0]==null || node[0].isParent){
		        	alert("请选择角色信息。");
		        	return;
		        } else {
			        var id = node[0].pIds;
			       if (confirm('确定要删除当前选中角色吗？')) {
						document.forms[0].action="<c:out value='${webapp}'/>/deleteAuth.action?returnView=1&roleId="+id;
						document.forms[0].roleId.value=id;
						document.forms[0].roleId.returnView="1";
						document.forms[0].submit();
					}
				};
			/**}*/
		}
		
		//默认选择第一项
		function setSelectFirst(){
			var treeNodes = PAGE_CACHE.tree.data.selectNodes(".//TreeNode");
			if(treeNodes == null) return ;
			for(var i=0;i<treeNodes.length;i++){
		    	var id = treeNodes[i].getAttribute("id");
		    	var level = treeNodes[i].getAttribute("levelType");		    	
		    	if(level =='2'){
		    		PAGE_CACHE.tree.clickNode(id,false);
		    		return;
				}
		    } 
		}
		window.onload = init;
		function init(){
			/**if (window.ActiveXObject) {
				var p = new ajax();
				p.url = "getResTreeRole.action?X=" + (new Date()).getTime();
				p.onresult = function(){
					if(null == this.data) return;
					var t = new SimpleTree();
					t.container = t.$("simpleTree");
					t.iconPath = t.$("simpleTree").getAttribute("iconPath");
					t.dataStr = this.text;
					t.radioBox = true;
					t.updateBox = true;
					t.init();
					t.OnClick = function(){
			            var treeNode = this.selectedNode;
			            var id = treeNode.getAttribute("id");
			            var level = treeNode.getAttribute("levelType");
						if(level==1){
						}else{
							var pNode = treeNode.parentNode;
							var systemId = pNode.getAttribute("id");
							parent.frames["listResMain"].location.href="<c:out value='${webapp}'/>/initResByRole.action?role.roleId="+id+"&uBaseConfig.systemId="+systemId;
						}
					}
					PAGE_CACHE.tree = t;
					setSelectFirst();
				}
				p.send();
			} else { */
				var setting = {
						view: {
							showIcon: showIconForTree
						},
						data: {
							simpleData: {
								enable: true
							}
						},
						callback:{
							onClick:function zTreeOnClick(event, treeId, treeNode){
								if(treeNode && !treeNode.isParent){
									parent.frames["listResMain"].location.href="<c:out value='${webapp}'/>/initResByRole.action?role.roleId="+treeNode.pIds+"&uBaseConfig.systemId="+treeNode.getParentNode().pIds;
								}
							}
						}
					};
				$.ajax({
		            type: "Post",
		            url: "getResZTreeRole.action?X= + (new Date()).getTime()",
		            dataType: "json", //可以是text，如果用text，返回的结果为字符串；如果需要json格式的，可是设置为json
		            success: function (data) {
		                $.fn.zTree.init($("#simpleTree"), setting, data).expandAll(true);
		                var zTree = $.fn.zTree.getZTreeObj("simpleTree");//获取ztree对象 
		                //var node = zTree.getNodeByParam("id", 1);
		                var nodes = zTree.getNodes()[0].children;
						zTree.selectNode(nodes[0]);//选择点  
				        zTree.setting.callback.onClick(null, zTree.setting.treeId, nodes[0]);
		            },
		            error: function (msg) {
		                alert(" 数据加载失败！" + msg);
		            }
		        });
		    /**}*/
	}
	function getZtreeFristSubNodes(treeNode){
	
	}
	function showIconForTree(treeId, treeNode) {
		return !treeNode.isParent;
	};

	</script>
</head>
<body topmargin="0" leftmargin="0" rightmargin="0" class="pl12">
	<div class="p-station">
	<table id="tbl_tools" class="tree_top_bar"  cellpadding="1" cellspacing="1">
			<tr>
				<td align="left">角色编辑</td>
				<td align="right">
	            </td>
			</tr>
	</table>
	<form name='frm' action="" method="get">
		<input type="hidden" value="" name="roleId"/>
		<input type="hidden" value="1" name="returnView"/>
	</form>
	<div id="treeboxbox_tree" class="treeContainerBox"  style="overflow:auto">
		<div class="treetopbut">
			<span class="topbut middlebtn"><a class="width_but2" href="javascript:createAuthResMap()" id="img1">新增</a></span>
			<span class="topbut middlebtn"><a class="width_but2" href="javascript:edit()">修改</a></span>
			<span class="topbut middlebtn1"><a class="width_but2" href="javascript:deleteRole()">删除</a></span>
		</div>
		<div class="clearall"></div>
		<div id="simpleTree" iconPath="<c:out value="${webapp}"/>/js/SimpleTree/images/">loading...</div>
	</div>
	</div>
</body>
<script type="text/javascript">
	document.getElementById("treeboxbox_tree").style.height = screen.availHeight - 251;
</script>
</html>
