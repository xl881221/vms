<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <%@include file="../../common/include.jsp"%>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script type="text/javascript" src="<c:out value="${webapp}"/>/js/SimpleTree/js/ajax.js"></script>
	<script type="text/javascript" src="<c:out value="${webapp}"/>/js/SimpleTree/js/simpleTree.js"></script>
	<link href="<c:out value="${webapp}"/>/js/SimpleTree/css/simpleTree.css" rel="stylesheet" type="text/css">
	
	<script language="javascript">
		window.onload = init;
		var instTree = null;
		function init(path){
			WaitingLayer.show();
			var p = new ajax();
			p.url = "<%=webapp%>/loadInstAndUsrXml.action?X=" + (new Date()).getTime();
			instTree=new SimpleTree();
			p.onresult = function(){
				WaitingLayer.hide();
				if(null == this.data) return;
				instTree.container = instTree.$("simpleTree");
				instTree.iconPath = instTree.$("simpleTree").getAttribute("iconPath");
				instTree.dataStr = this.text;
				instTree.radioBox = true;
				instTree.updateBox = true;
				instTree.OnClick = function(){
			        var treeNode = this.selectedNode;
	                var id = treeNode.getAttribute("id");
	                var level = treeNode.getAttribute("levelType");
	                document.getElementById('instId').value=id;
	                var selectObj=parent.frames["headFrame"].document.getElementById('systemId');
	                var systemId=selectObj.value;
	                var systemName=selectObj.options[selectObj.selectedIndex].text;
	                parent.frames["InstMailUserResultFrame"].location.href="<c:out value='${webapp}'/>/system/authority/instMailUser.action?bankId="+id+"&systemId="+systemId+"&systemName="+systemName;
				    parent.frames["InstMailResultFrame"].location.href="<c:out value='${webapp}'/>/system/authority/instMailMain.action?bankId="+id+"&systemId="+systemId+"&systemName="+systemName;
				}
				instTree.GetAnalisyNodes = function(id){
		        	WaitingLayer.show();
		            var p = new ajax();
		            p.url = "<%=webapp%>/loadInstAndUsrXml.action?" + (new Date()).getTime()+ "&id="+id;
		            p.onresult = function(){
		                var data = this.text
		                instTree.BuildAnalisyNodes(id,data);				    
		                WaitingLayer.hide();
		            }
		            p.send();
		        }   
		        instTree.init();
			}
			p.send();
		}
		function refreshTreeInst(){
			init();
			document.getElementById('instId').value="";
		}
	</script>
</head>
<body topmargin="0" leftmargin="0" rightmargin="0">
	<div>机构树：</div>
	<div id="treetitlebar" class="tree_top_bar" >
	    <span class="controlbtn_Ref" onclick="refreshTreeInst();" title="刷新"></span>
	</div>
	<div id="treeboxbox_tree" class="treeContainerBox">
		<div id="simpleTree" iconPath="<c:out value="${webapp}"/>/js/SimpleTree/images/">loading...</div>
	</div>
	<div>
		<from>
			<intput id='instId' name ='instId'  type='hidden' value='' />
		</from>
	</div>
</body>
</html>
