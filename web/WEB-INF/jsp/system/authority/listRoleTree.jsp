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
<!--
PAGE_CACHE = {};

function query(){
	var id;
	var systemId;
	if (window.ActiveXObject) {
		var date = new Date();
		var treeNode = PAGE_CACHE.tree.GetSelectedTreeNodes();
		id = treeNode.getAttribute("id");
		systemId = treeNode.parentNode.getAttribute("id");
	}else {
		var zTree = $.fn.zTree.getZTreeObj("simpleTree");//获取ztree对象 
        var node=zTree.getSelectedNodes();
        if(node[0]==null || node[0].isParent){
		   	alert("请选择角色信息。");
		   	return;
		} else {
	        id = node[0].pIds;
	        systemId = node[0].getParentNode().pIds;
	        
        }
	}
	var url ="<c:out value='${webapp}'/>/initResByRole.action?readOnly=true&role.roleId="+id+"&uBaseConfig.systemId="+systemId;
	OpenModalWindow(url,700,470,true);	
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
	/** if (window.ActiveXObject) {
		var p = new ajax();
		p.url = "getRoleTree.action?X=" + (new Date()).getTime();
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
					parent.document.getElementById("mainFrame").src="<c:out value='${webapp}'/>/viewUserByRole.action?fixQuery=false&role.roleId="+id;
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
							parent.document.getElementById("mainFrame").src="<c:out value='${webapp}'/>/viewUserByRole.action?fixQuery=false&role.roleId="+treeNode.pIds;
						}
					}
				}
			};
		$.ajax({
            type: "Post",
            url: "getRoleZTree.action?X= + (new Date()).getTime()",
            dataType: "json", //可以是text，如果用text，返回的结果为字符串；如果需要json格式的，可是设置为json
            success: function (data) {
                $.fn.zTree.init($("#simpleTree"), setting, data).expandAll(true);
                var zTree = $.fn.zTree.getZTreeObj("simpleTree");//获取ztree对象 
				//var node = zTree.getNodeByParam("id", 1);
				//var pNodes = zTree.getNodes();
				//for(int i=0; i<pNodes.length; i++){
					var nodes = zTree.getNodes()[0].children;
					//if(nodes.length>0){
						zTree.selectNode(nodes[0]);//选择点  
				        zTree.setting.callback.onClick(null, zTree.setting.treeId, nodes[0]);
				    //    return;
			        //}
		        //}
            },
            error: function (msg) {
                alert(" 数据加载失败！" + msg);
            }
        });
	/**} */
	
	function showIconForTree(treeId, treeNode) {
		return !treeNode.isParent;
	};
}

-->
</script>
</head>
<body topmargin="0" leftmargin="0" rightmargin="0" class="pl12">
	<div class="p-station">
	<table id="tbl_tools" class="tree_top_bar" width="100%"  cellpadding="1" cellspacing="1">
		<tr>
			<td align="left">角色列表</td>
			<td align="right" valign="top">
			<%-- <span class="controlbtn" onclick="displayTreeInst();" title="机构树">&nbsp;</span>--%>
            </td>
		</tr>
	</table>
	<form name='frm' action="" method="get" >
		<input type="hidden" value="" name="roleId"/>
	</form>	
	<div id="treeboxbox_tree" class="treeContainerBox"  style="overflow:auto">
		<div class="treetopbut">
			<span class="topbut"><a class="width_but1" href="javascript:query()" id="img1">查看</a></span>
		</div>
		<div id="simpleTree" iconPath="<c:out value="${webapp}"/>/js/SimpleTree/images/">loading...</div>
	</div>
	</div>
</body>	
<script type="text/javascript">
	document.getElementById("treeboxbox_tree").style.height = screen.availHeight - 251;
</script>		
</html>
