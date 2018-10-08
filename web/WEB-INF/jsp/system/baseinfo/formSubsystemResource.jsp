<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@include file="../../common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="../../common/include.jsp"%>
<link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css"
	rel="stylesheet">
	<script language="javascript" type="text/javascript">
	<!--
		//标识页面是否已提交
		var subed = false;
		function check(){
			//验证是否重复提交
			if (subed == true){
				alert("信息正在发送给服务器，请不要重复提交信息！");
				return false;
			}
			
			if(fucCheckNull(document.getElementById("uAuthResMap.systemId"),"请选择子系统")==false){
			   return false;
			}	
			
			if(fucCheckNull(document.getElementById("uAuthResMap.resName"),"请输入资源名")==false){
			   return false;
			}
						
			if(fucCheckNull(document.getElementById("uAuthResMap.resType"),"请选择资源类型")==false){
			   return false;
			}
			
			if(fucCheckNull(document.getElementById("uAuthResMap.srcTable"),"请输入源表名")==false){
			   return false;
			}
			
			if(fucCheckNull(document.getElementById("uAuthResMap.srcKeyField"),"请输入源表主键")==false){
			   return false;
			}
			
			if(fucCheckNull(document.getElementById("uAuthResMap.srcIdField"),"请输入源表编号")==false){
			   return false;
			}
			
			if(fucCheckNull(document.getElementById("uAuthResMap.srcNameField"),"请输入源名称字段")==false){
			   return false;
			}
			subed=true;
			return subed;
		}

		function enableSubSystemSelect(){
			var rtype = document.getElementById("uAuthResMap.resType");
			var sysid = document.getElementById("uAuthResMap.systemId");
			if(rtype != null && rtype.value=="PUB"){
				sysid.value="00003";
				sysid.disabled=true;
			}else{
				sysid.disabled=false;
				sysid.value="00001";
			}
		}
		
		function findOutSubmit() {
			if(check())
			{			
				document.frm.action="saveResource.action";
				document.frm.method="post";				
				document.frm.submit();		
			}	
		}
	-->
	</script>
	</head>
	<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">
		<form name="frm" id="frm" action="saveResource.action" method="post">
			<s:if test="uAuthResMap.resId != null && uAuthResMap.resId != '' ">
    			<input type="hidden" name="uAuthResMap.resId" id="uAuthResMap.resId" value="<s:property value="uAuthResMap.resId"/>"/>
   			</s:if>
			<div id="editpanel">
			<div id="editsubpanel" class="editsubpanel">
				<table class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
					<tr class="header">
						<th colspan="4" style="text-align:center;">
							<s:if test="isSuccess!='update' || isSuccess!='updateY' || isSuccess!='updateN'">
								新增</s:if><s:else>修改</s:else>子系统资源配置
						</th>
					</tr>
					<tr>
						<td align="right">资源类型:</td>
						<td>
							<s:if test="uAuthResMap.resType=='PUB'">
								<s:select list="resTypeDic" name="uAuthResMap.resType" id="uAuthResMap.resType" 
							  listKey="dicValue" listValue="dicName" cssStyle="width:100" disabled="true"/>
							</s:if>
							<s:else>
								<s:select list="resTypeDic" name="uAuthResMap.resType" id="uAuthResMap.resType" 
							  listKey="dicValue" onclick="enableSubSystemSelect();" listValue="dicName" cssStyle="width:100"/>
							</s:else>
							<span style="color: #FF0000">*</span>
						</td>	
					</tr>
					<tr>
						<td style="text-align: right">子系统选择:</td>
						<td>
							<s:if test="uAuthResMap.resType=='PUB'">
								<s:select list="subSystemList" name="uAuthResMap.systemId" id="uAuthResMap.systemId"
							  listKey="systemId" listValue="systemCname" theme="simple" cssStyle="width:100" disabled="true"/>
							 </s:if>
							 <s:else>
								<s:select list="subSystemList" name="uAuthResMap.systemId" id="uAuthResMap.systemId" 
							  listKey="systemId" listValue="systemCname" theme="simple" cssStyle="width:100"/>
							 </s:else>
							<span style="color: #FF0000">*</span>
						</td>
					</tr>
                  <tr>
						<td width="15%" style="text-align: right">资源名称:</td>
						<td>
							<input type="text" value="<s:property value="uAuthResMap.resName"/>" name="uAuthResMap.resName" id="uAuthResMap.resName" dataType="LimitB" <s:if test="uAuthResMap.resType=='PUB'">disabled</s:if>/>
							<span style="color: #FF0000">*</span>
						</td>	
					</tr>
				    
					<tr>
						<td align="right">源表名:</td>
						<td>
							<input type="text" value="<s:property value="uAuthResMap.srcTable"/>" name="uAuthResMap.srcTable" id="uAuthResMap.srcTable" dataType="LimitB" <s:if test="uAuthResMap.resType=='PUB'">disabled</s:if>/>
							<span style="color: #FF0000">*</span>
						</td>	
					</tr>
					<tr>
						<td align="right">
							源表主键:
						</td>
						<td>
							<input type="text" value="<s:property value="uAuthResMap.srcKeyField"/>" name="uAuthResMap.srcKeyField" id="uAuthResMap.srcKeyField" dataType="LimitB" <s:if test="uAuthResMap.resType=='PUB'">disabled</s:if>/>
							<span style="color: #FF0000">*</span>
						</td>	
					</tr>
					<tr>
						<td align="right">
							源表编号:
						</td>
						<td>
							<input type="text" value="<s:property value="uAuthResMap.srcIdField"/>" name="uAuthResMap.srcIdField" id="uAuthResMap.srcIdField" dataType="LimitB" <s:if test="uAuthResMap.resType=='PUB'">disabled</s:if>/>
							<span style="color: #FF0000">*</span>
						</td>	
					</tr>
					<tr>
						<td align="right">
							源名称字段:
						</td>
						<td>
							<input type="text" value="<s:property value="uAuthResMap.srcNameField"/>" name="uAuthResMap.srcNameField" id="uAuthResMap.srcNameField" dataType="LimitB" <s:if test="uAuthResMap.resType=='PUB'">disabled</s:if>/>
							<span style="color: #FF0000">*</span>
						</td>	
					</tr>
					<tr>
						<td align="right">
							描述:
						</td>
						<td>
							<textarea rows="3" cols="50" name="uAuthResMap.description" id="uAuthResMap.description" <s:if test="uAuthResMap.resType=='PUB'">disabled</s:if> ><s:property value="uAuthResMap.description"/></textarea>
						</td>																											
					</tr>	
				</table>
			</div></div>
						
				<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
					<s:if test="uAuthResMap.resType!='PUB'"><!--
					 2009-07-17 15:50 ShiCH 修改按钮样式-->							 
					<div  type="btn" onclick="findOutSubmit()" img="<c:out value="${webapp}"/>/image/button/save.gif"  name="BtnSave" value="保存" id="BtnSave"></div>
					</s:if>&nbsp;	 			 
			     	<div  type="btn" img="<c:out value="${webapp}"/>/image/button/cancel.gif" onclick="window.close()" name="BtnReturn" value="关闭" id="BtnReturn"></div>
	     		</div>
		</form>
	</div>
</body>

</html>
