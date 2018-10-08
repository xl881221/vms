<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>

<%@page import="java.util.*"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="../../common/include.jsp"%>
		<%@include file="../../common/commen-ui.jsp"%>
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
			
			window.onload = function(){
				var msg = "<s:property value="innerHtml"/>";
				if(msg!=''&&msg=='success'){
					alert('保存成功');
				}
				if(msg!=''&&msg=='noChanges'){
					alert('没有变更');
				}
				if(msg!=''&&msg=='saveSuccess'){
					alert('保存成功，待审核后生效');
				}
				if(msg!=''&&msg=='saveFaliures'){
					alert('保存失败，在待审核信息中已存在该项信息');
				}
				if(msg!=''&&msg=='fail'){
					alert('保存失败');
				}if(msg!=''&&msg=="cannot"){
					alert('输入项中的值在数据库中保存的长度不能超过200');
				}
			}
			
			function saveConfig(){
				
				if(document.forms[0].selectTab.value==''){
					document.forms[0].selectTab.value=document.getElementById('firstTab').innerText.replace(/^(\s)*|(\s)*$/g,"");
				}
				document.forms[0].action="<c:out value='${webapp}'/>/saveParamConfig.action";
				
			}	
				
			/*
			* 查询提交
			*/
			function findOutSubmit() {
			
			    var min=parseInt(document.getElementsByName("param_25")[0].value);
			    var max=parseInt(document.getElementsByName("param_26")[0].value);
			    var psw=document.getElementsByName("param_24")[0].value;
			    if (isNaN(min)){
			        min=0;
			    }
			    if (isNaN(max)){
			        max=0;
			    }
			   //document.getElementsByName("param_25")[0].value=min;
			   //document.getElementsByName("param_26")[0].value=max;
			   if(max<min){
			       alert("密码最大长度必须大于等于最小长度！");
			       return;
			   }
			   if(psw.length<min||psw.length>max){
			   		alert("初始化密码必须在密码最小长度和最大长度之间！") ;
			   		return ;
			   }
				document.frm.action="saveParamConfig.action";
				document.frm.method="post";
				document.frm.submit();
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
			<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
				<tr>
					<td class="centercondition">
						<div id="tbl_current_status">
							<span><img src="<c:out value="${webapp}"/>/themes/images/icons/icon13.png" /></span>
							<span class="current_status_menu">当前位置：</span>
							<span class="current_status_submenu1">系统管理</span>
							<span class="current_status_submenu">系统参数管理</span>
							<span class="current_status_submenu">参数配置</span>
						</div>
						<div id="textheight">
						<s:set name="Tab" value="Tab"></s:set>
						<s:iterator value="paramMaps" status="va">
						    <s:set name="key" value="key"></s:set>
						    <s:set id="value" name="value" value="value"></s:set>
		                    <%String key1=(String)pageContext.getAttribute("key");
						      String systemId1=key1.substring(0,key1.indexOf("#"));
						      String systemName1=key1.substring(key1.indexOf("#")+1);
						      pageContext.setAttribute("systemId",systemId1);
						      pageContext.setAttribute("systemName",systemName1);
						    %>							
							<div id="tab_content_<c:out value="${systemId}"/>" class="tab_body" style='<s:if test="${selectTab!=systemId}">display : none; </s:if>;'>
								<div class="body">
									<div align="right">
										<s:property value="value" escape="false" />
									</div>
								</div>
							</div>							
						</s:iterator>
						</div>
						<div class="configsave">
						<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="findOutSubmit()" name="BtnSave" value="保存" id="BtnSave"/>
						<!-- <a href="#" onclick="findOutSubmit()">保存</a> -->
						</div>
					</td>
				</tr>
			</table>
		</form>
	</body>
<script type="text/javascript">
   	document.getElementById("textheight").style.height = screen.availHeight - 260;
</script>
</html>
