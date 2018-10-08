<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@include file="../../common/include.jsp"%>
	<script type="text/javascript" src="<c:out value="${webapp}"/>/js/SimpleTree/js/ajax.js"></script>
	<script type="text/javascript" src="<c:out value="${webapp}"/>/js/SimpleTree/js/simpleTree.js"></script>
	<link href="<c:out value="${webapp}"/>/js/SimpleTree/css/simpleTree.css" rel="stylesheet" type="text/css">
	<link href="<c:out value="${sysTheme}"/>/css/main.css" rel="stylesheet" type="text/css" />
	<link type="text/css" href="<c:out value="${webapp}"/>/themes/css/default.css" rel="stylesheet" />
	<link type="text/css" href="<c:out value="${webapp}"/>/themes/css/menu.css" rel="stylesheet" />
	<script language="javascript">
		PAGE_CACHE = {};
		function create(){
			var date = new Date();
			var url = "addHolidayType.action?"+date.getTime();
			OpenModalWindow(url,500,350,true);  
		}
		function edit(){
			var treeNode = PAGE_CACHE.tree.GetSelectedTreeNodes();
			if(null == treeNode) return;
			var id = treeNode.getAttribute("id");
			var level = treeNode.getAttribute("levelType");
			if(level==1){
			}else{
				var url = "editHolidayType.action?holidayType="+id;
				OpenModalWindow(url,500,350,true);
			}
		}
		function deleteI(){
			var treeNode = PAGE_CACHE.tree.GetSelectedTreeNodes();
			if(null == treeNode) return;
			var id = treeNode.getAttribute("id");
			var level = treeNode.getAttribute("levelType");
			if(level==1){
			}else{
				if (confirm('确定要删除选中节假日类别吗？对应类别的节假日也会一并删除')) {
					document.forms[0].action="deleteHolidayType.action?holidayType="+id;
					document.forms[0].holidayType.value=id;
					document.forms[0].submit();
				}
			}
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
			var p = new ajax();
			p.url = "getHolidayTypeXML.action?roleId=593&X=" + (new Date()).getTime();
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
						parent.document.getElementById("mainFrame").src="listHolidayByType.action?holidayType="+id+"&readOnly="+<%=request.getParameter("readOnly")%>;
					}
				}
				PAGE_CACHE.tree = t;
				setSelectFirst();
			}
			p.send();
		}
	</script>
</head>
<body topmargin="0" leftmargin="0" rightmargin="0">
	<table id="tbl_tools" cellpadding="1" cellspacing="1">
	<tr>
		<td align="left">节假日类别管理</td>
		<td align="right">
	<%
		if((request.getParameter("readOnly")!=null)&&request.getParameter("readOnly").equals("false")){
	%>
		<a href="#" onClick="javascript:create();">
			<img src="<c:out value="${webapp}"/>/themes/images/icons/icon15.png"/>新增
		</a>
		<a href="#" onClick="javascript:edit();">
			<img src="<c:out value="${webapp}"/>/themes/images/icons/icon17.png"/>修改
		</a>
		<a href="#" onClick="javascript:deleteI();">
			<img src="<c:out value="${webapp}"/>/themes/images/icons/icon18.png"/>删除
		</a>
	<%
		}
	%>
		</td>
	</tr>
	</table>
	<form name='frm' action="" method="get" >
		<input type="hidden" value="" name="holidayType"/>
	</form>
	<div id="treeboxbox_tree" class="treeContainerBox" style="width:100%;height:90%;overflow:auto">
		<div id="simpleTree" iconPath="<c:out value="${webapp}"/>/js/SimpleTree/images/">loading...</div>
	</div>
</body>
</html>