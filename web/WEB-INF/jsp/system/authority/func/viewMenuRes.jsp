<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@include file="../../../common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="../../../common/include.jsp"%>
		<link href="<c:out value="${sysTheme}"/>/css/subWindow.css"
			type="text/css" rel="stylesheet">

		<script type="text/javascript"
			src="<c:out value="${webapp}"/>/js/SimpleTree/js/ajax.js"></script>
		<script type="text/javascript"
			src="<c:out value="${webapp}"/>/js/SimpleTree/js/simpleTree.js"></script>
		<link
			href="<c:out value="${webapp}"/>/js/SimpleTree/css/simpleTree.css"
			rel="stylesheet" type="text/css">



	</head>
	<body topmargin="0" leftmargin="0" rightmargin="0">
		<form name='frm' action="saveResByRole.action" method="post">
			<input type="hidden" name="roleId"
				value="<s:property value="roleId"/>" />
			<input type="hidden" name="userId"
				value="<s:property value="userId"/>" />
			<input type="hidden" name="values" value="" />
			<div id="queryResult"></div>
			<div id="treeboxbox_tree"
				style="background: #e9f4f8; overflow: auto; width: 100%; height: 380px;">
				<div id="simpleTree"
					iconPath="<c:out value="${webapp}"/>/js/SimpleTree/images/">
					loading...
				</div>
			</div>
			<div id="ctrlbutton" class="ctrlbutton"
				style="border: 0px solid #F2A7BA;">
				<s:if test="config">
				<div style="margin-left: 5px; margin-right: 5px" type="btn"
					img="<c:out value="${webapp}"/>/image/button/save.gif"
					onclick="save()" name="BtnSave" value="保存" id="BtnSave"
					class="input_button"></div></s:if>
				<div style="margin-left: 5px; margin-right: 5px" type="btn"
					img="<c:out value="${webapp}"/>/image/button/cancel.gif"
					onclick="window.close()" name="BtnReturn" value="关闭" id="BtnReturn"
					class="input_button"></div>
		</form>
	</body>

	<script language="javascript"><!--
		PAGE_CACHE = {};		
		 
		var save = function(){
			
			var treeNodes = PAGE_CACHE.tree.GetSelectedTreeNodes();
			if(null == treeNodes) {
	    		alert('请您先选择要操作的记录');
				return;
		    }
	    	
	    	var value = '';
	    	for(var i=0;i<treeNodes.length;i++){
		    	var v = treeNodes[i].getAttribute("id");
		    	var level = treeNodes[i].getAttribute("levelType");
		    	if(level=="3"){
		    		value+=v+",";
			    }
		    } 
		    //alert(value);
	    	document.getElementById('BtnSave').disabled=true;
			document.forms[0].values.value = value;
			document.forms[0].action="saveFuncWithUser.action";
			document.forms[0].submit();
		}
		
		var query = function(){
			var str = document.getElementById('txtQuery').value;
			var treeNodes = PAGE_CACHE.tree.data.selectNodes(".//TreeNode");
			var varr= [];
			for(var i=0;i<treeNodes.length;i++){
		    	var n = treeNodes[i].getAttribute("name");
		    	var id = treeNodes[i].getAttribute("id");
		    	var level = treeNodes[i].getAttribute("levelType");		    	
		    	if(level =='2' && str !='' && n.indexOf(str)>-1){
		    		varr.push(id);
				}
		    } 
			PAGE_CACHE.tree.SelectNodesByID(varr);
		}
		
		
		window.onload = function (){
			var r1 = document.getElementById("roleId");
			var r2 = document.getElementById("userId");
			var date = new Date();
			var p = new ajax();
			p.url = "viewMenuResWithXML.action?"+date.getTime()+"&roleId="+r1.value + "&userId="+r2.value;
			p.onresult = function(){
				if(null == this.data) return;
				var t = PAGE_CACHE.tree;
				var t = new SimpleTree();
				t.container = t.$("simpleTree");
				t.iconPath = t.$("simpleTree").getAttribute("iconPath");
				t.dataStr = this.text;
				<s:if test="config">t.checkBox = true;</s:if>
				<s:else>t.radioBox = true;</s:else>
				t.updateBox = true;
				t.GetAnalisyNodes = function(id){
		        	WaitingLayer.show();
		            var p = new ajax();
		            p.url = "viewFuncResWithXML.action?X=" + (new Date()).getTime()+ "&menuId="+id+"&userId="+r2.value;
		            p.onresult = function(){
		                var data = this.text;
		                t.BuildAnalisyNodes(id,data);
		                WaitingLayer.hide();
		            }
		            p.send();
		        }
				t.init();	
				PAGE_CACHE.tree = t;	
			}
			p.send();
		
		}
	--></script>
</html>
