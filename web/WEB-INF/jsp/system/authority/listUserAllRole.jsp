<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@page import="fmss.common.cache.CacheManager,org.apache.commons.lang.StringUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<%@include file="../../common/include.jsp"%>
<html>
<head>
	
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv='pragma' content='no-cache'>
        <link href="<c:out value="${sysTheme}"/>/css/main.css" rel="stylesheet" type="text/css" />

	<link type="text/css" href="<c:out value="${sysTheme}"/>/css/subWindow.css" rel="stylesheet">
	
	<style type="text/css">
	.tab_page .selct {
		line-height:16px!important;
	}
	.tab_page .normal {
		line-height:16px!important;
	}
	</style>
	<script language="javascript" type="text/javascript">
		var page = {};
		
		var resMapArray = '<s:property value="%{resMapArray}"/>';
		var autoCheck = function(){
			var resArr = resMapArray.split(',');
			var checks = document.getElementsByName("resMap");
			for (i = 0; i < checks.length; i++) {
					if(resMapArray.indexOf(checks[i].value)>-1){
						checks[i].checked=true;
					}
			}
		}
		var query = function() {
			document.forms[0].action='queryResByUser.action';
			document.forms[0].submit();
		}
		var openNew = function(url) {
			window.open(url,'','top=150,left=260,width=600,height=500,menubar=no,status=yes');
		}
		
		function findOutSubmit() {
			document.frm.action="saveRoleByUser.action";
			document.frm.method="post";
			document.frm.submit();
		}	
		function Expan(o)
		{
			var obj=document.all(o); //声明一个变量
			obj.style.display=obj.style.display=="none"?"":"none"; //判断是否隐藏
		}
		
		
		var obj_table = null;
		function ExpanAll(){
			obj_table=document.getElementsByTagName ("TABLE");						
			for( var i = 0 ; i<obj_table.length; i++){	
				if(obj_table[i] != undefined && obj_table[i] != null && obj_table[i].id != null && obj_table[i].id != ""){
					obj_table[i].style.display=""; //判断是否隐藏
				}					
			}
			
			obj_table=document.getElementsByTagName ("TD");						
			for( var i = 0 ; i<obj_table.length; i++){	
				if(obj_table[i] != undefined && obj_table[i] != null && obj_table[i].id != null && obj_table[i].id != ""){
					obj_table[i].style.display=""; //判断是否隐藏
				}					
			}
			
		}
		
		function RetractAll(){
			obj_table=document.getElementsByTagName ("TABLE");
			for( var i = 0 ; i<obj_table.length; i++){
				if(obj_table[i] != undefined && obj_table[i] != null && obj_table[i].id != null && obj_table[i].id != ""){
					if(obj_table[i].id!='tbl_current_status')
						obj_table[i].style.display="none"; //判断是否隐藏
				}
			}	
			
			obj_table=document.getElementsByTagName ("TD");						
			for( var i = 0 ; i<obj_table.length; i++){	
				if(obj_table[i] != undefined && obj_table[i] != null && obj_table[i].id != null && obj_table[i].id != ""){
					obj_table[i].style.display=""; //判断是否隐藏
				}					
			}			
		}

		function tabChange(e) {
			if (e.className == "selct") return;
			if (e.className == "on") e.className = "normal";
			else e.className = "on";
		} 
		
		function clickTab(e,divId) {
			if (!window._CURR_C) 
				window._CURR_C = document.getElementById("firstTab");
			window._CURR_C.className = "normal";
			window._CURR_C = e;
			window._CURR_C.className = "selct";
			//切换tab页
			document.getElementById(divId).style.display="";
			var divs = document.getElementsByTagName("div");
			for(i=0;i<divs.length;i++){
				if(divs[i].id!=null&&divs[i].id!=''&&divs[i].id.indexOf('tab_content_')>-1){
					if(divId!=divs[i].id){
						document.getElementById(divs[i].id).style.display="none";
					}
				}
			}
			//置选中标志
			document.forms[0].selectTab.value=divId.replace('tab_content_','');
		}	
	</script>
	<style>
		input {width:50px;}
	</style>
</head>
<!-- 2009-07-17 16:51 ShiCH 修改GRID列表加载形式 -->
<!-- <body onmousemove="MM(event)" onmouseout="MO(event)"> -->

<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">
<form name="frm" action="saveRoleByUser.action" method="post">
    <s:if test="user.userId != null && user.userId != '' ">
    	<input type="hidden" name="user.userId" value="<s:property value="user.userId"/>"/>
    </s:if>
    <input type="hidden" name="selectTab" value="<s:property value="selectTab"/>"/>
<div id="editpanel">
<div id="editsubpanel" class="editsubpanel">
<table id="contenttable" class="lessGrid"  cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr class="header">
		<th colspan="10" >
			角色查看
		</th>
	</tr> 
</table>

<div class="tab_page" style="background:#FFF;">
	
	<div class="tabs" onselectstart="return false">	
		<table cellpadding="0" cellspacing="0" style="display: inline">
			<tr>
			<%int iroles=0 ;  %>
			<s:iterator value="allRoles" id="allRole">	
			<%
			if(iroles>6){
				%></tr><tr><%				
				iroles=0;
			}
			iroles++; 
			%>
			<c:out value="${iroles}"/>
				<td>
				<s:iterator value="#allRole" id="tmp">
					<%int i=0; %>
					<div id="<s:if test="selectTab==#tmp.key ">firstTab</s:if>" class="<s:if test="selectTab==#tmp.key">selct</s:if><s:else>normal</s:else>" onclick="clickTab(this,'tab_content_<s:property value="#tmp.key"/>')"> <s:property value="#tmp.key"/> </div>
				</s:iterator>
				</td>
			</s:iterator>
			</tr>
		</table>
	</div> 
	
	<s:iterator value="allRoles" id="allRole">
			<s:iterator value="#allRole" id="tmp">
			<div id="tab_content_<s:property value="#tmp.key"/>" class="tab_body" style="<s:if test="selectTab!=#tmp.key">display:none;</s:if>;height:260px; ">
				
				<div class="body">
					<div align="left">
						<s:checkboxlist name="userRoles" id="ids" list="#tmp.value" listKey="roleId" listValue="roleName" value="userRoles"  />
					</div>
				</div>
			</div>
			</s:iterator>
	</s:iterator>
</div>
<div id="ctrlbutton" class="ctrlbutton" style="border:0px solid #F2A7BA;">		
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="window.close()" name="BtnReturn" value="关闭" id="BtnReturn"/>
</div>

</div>
</div>
</form>
</div>
</body>
</html>
