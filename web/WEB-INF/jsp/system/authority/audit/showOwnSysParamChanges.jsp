<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<%@page import="java.util.*"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	    <%@include file="/WEB-INF/jsp/common/include.jsp"%>
		<script language="javascript" type="text/javascript">
		var page = {};
		page.submitForm = function(obj){
			if(Validator.Validate(obj,3)){
				return page.checkRepassword();
			}
			return false;
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
						obj_table[i].style.disabled="true"; //判断是否隐藏
				}
				
				obj_table=document.getElementsByTagName ("TD");						
				for( var i = 0 ; i<obj_table.length; i++){	
						obj_table[i].style.disabled="true"; //判断是否隐藏
				}
				
			}
			
			function RetractAll(){
				obj_table=document.getElementsByTagName ("TABLE");
				for( var i = 0 ; i<obj_table.length; i++){
					if(obj_table[i] != undefined && obj_table[i] != null && obj_table[i].id != null && obj_table[i].id != ""){
						if(obj_table[i].id!='tbl_current_status')
							obj_table[i].style.disabled="true"; //判断是否隐藏
					}
				}	
				
				obj_table=document.getElementsByTagName ("TD");						
				for( var i = 0 ; i<obj_table.length; i++){	
						obj_table[i].style.disabled="true"; //判断是否隐藏
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
							document.getElementById(divs[i].id).style.disabled="true";
					}
				}
				//置选中标志
				document.forms[0].selectTab.value=divId.replace('tab_content_','');
			}
			
			
</script>

		<script language="javascript">
	// 2009-07-17 14:08 ShiCH 为IE添加背景缓存功能,可解决TAB鼠标事件时重新加载图片闪烁问题
	if(/msie/gi.test(navigator.userAgent))
		try{document.execCommand("BackgroundImageCache", false, true);}catch(e){};
	</script>
	</head>
	<body>
		<form name="frm" action="saveParamConfig.action" method="post">
			<input type="hidden" name="selectTab" value="<s:property value="selectTab"/>" />
			<table>
				<tr>
					<td>
						<img src="<c:out value="${sysTheme}"/>/img/jes/icon/home.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu">系统参数管理<span
							class="actionIcon">-&gt;</span>参数配置审核</span>
					</td>
				</tr>
			</table>
			<div class="tab_page">
				<div class="tabs" onselectstart="return false">
					<s:iterator value="paramMaps" status="status">
					    <s:set name="key" value="key"></s:set>
					    <%String key=(String)pageContext.getAttribute("key");
					      String systemId=key.substring(0,key.indexOf("#"));
					      String systemName=key.substring(key.indexOf("#")+1);
					      pageContext.setAttribute("systemId",systemId);
					      pageContext.setAttribute("systemName",systemName);
					    %>
						<div id="<c:if test="${selectTab==systemId}">firstTab</c:if>"
								class="<c:if test="${selectTab==systemId}">selct</c:if><c:if test="${selectTab!=systemId}">normal</c:if>"
								onmouseover="tabChange(this)" onmouseout="tabChange(this)"
								onclick="clickTab(this,'tab_content_<c:out value="${systemId}"/>')" style="float:left;margin-top:0px;">
								<c:out value="${systemName}" />
						</div>
					</s:iterator>
				</div>
				<s:set name="selectTab" value="selectTab"></s:set>
				<s:iterator value="paramMaps" status="va">
				    <s:set name="key" value="key"></s:set>
				    <s:set name="value" value="value" id="value"></s:set>
                    <%String key1=(String)pageContext.getAttribute("key");
				      String systemId1=key1.substring(0,key1.indexOf("#"));
				      String systemName1=key1.substring(key1.indexOf("#")+1);
				      pageContext.setAttribute("systemId",systemId1);
				      pageContext.setAttribute("systemName",systemName1);
				    %>
					<div id="tab_content_<c:out value="${systemId}"/>" class="tab_body"
						style='<c:if test="${selectTab!=systemId}">display : none; </c:if> '>
						<div class="body">
							<div align="right">
								<s:property value="value" escape="false" />
								<div type="btn"
									img="<c:out value="${webapp}"/>/image/button/save.gif"
									onclick="window.close()" value="关闭"></div>
							</div>
						</div>
					</div>
				</s:iterator>
			</div>
		</form>
	</body>
</html>
