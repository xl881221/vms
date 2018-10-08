<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma","No-cache");
response.setHeader("Cache-Control","no-cache");
response.setDateHeader("Expires",0);
%>
<%@include file="../../common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<%@include file="../../common/include.jsp"%>
	<script type="text/javascript" src="js/jquery-1.4.4.min.js"></script>
	<title><s:if test="user.userId != null && user.userId != '' ">修改用户</s:if><s:else>新增用户</s:else></title>
	<link type="text/css" href="<c:out value="${sysTheme}"/>/css/subWindow.css" rel="stylesheet">
	<script language="javascript" type="text/javascript">
		//标识页面是否已提交
		var subed = false;
		function check(){
			//验证是否重复提交
			if (subed == true){
				alert("信息正在发送给服务器，请不要重复提交信息！");
				return false;
			}
				
			//验证用户名是否合法			
			if(nameCheck(document.getElementById("user.userEname"))==false){
			   return false;
			}
					
			//验证用中文名是否为空
			if(fucCheckNull(document.getElementById("user.userCname"),"请输入用户中文名")==false){
			   return false;
			}
			//描述超出指定长度,最大100字节
			if(fucCheckLength(document.getElementById("user.userCname"),20,"用户中文名超出指定长度,最大20字符")==false){
				return false;
			}

			//描述超出指定长度,最大100字节
			if(fucCheckLength(document.getElementById("user.address"),60,"地址超出指定长度,最大60字符")==false){
				return false;
			}
			
		    //验证EMail地址是否为空
			/*if(fucCheckNull(document.getElementById("user.email"),"请输入Email地址")==false){
			   alert("Email地址为必填项 !");
			   return false;
			}*/	
			
			//验证是否是有效的EMail地址
			if(fucCheckMail(document.getElementById("user.email"),"无效的Email地址")==false){
			   return false;
			}	
			
			//描述超出指定长度,最大100字节
			if(fucCheckLength(document.getElementById("user.email"),30,"Email超出指定长度,最大30字符")==false){
				return false;
			}
			
			//描述超出指定长度,最大300字节
			if(fucCheckLength(document.getElementById("user.description"),300,"描述超出指定长度,最大300字节")==false){
				return false;
			}
			
		
			subed=true;
			return subed;
		}
		function findOutSubmit() {		
			if(check()) {			
				document.formUser.action="saveUser.action";
				document.formUser.method="post";
				document.formUser.submit();
				document.getElementById('BtnSave').disabled=true;
			}
		}

   function load(){
          <s:if test="user.userId == null || user.userId == '' ">
         		var checkedTreeNodeId=window.dialogArguments.parent.frames["leftFrame"].document.getElementById('checkedTreeNodeId').value;
			 	var checkedTreeNodeName=window.dialogArguments.parent.frames["leftFrame"].document.getElementById('checkedTreeNodeName').value;
			  	$('#instId').val(checkedTreeNodeId);
			    $('#instName').val(checkedTreeNodeName);
			 
			   /** var node=window.dialogArguments.parent.frames["userInstTreeFrame"].window.instTree.GetSelectedTreeNodes() 
			    document.getElementById("instName").value=node.getAttribute("name");
			    document.getElementById("instId").value=node.getAttribute("id");*/
		  </s:if>
    }
    
  
	</script>
</head>
<body onload="load()" scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">
<form name="formUser" id="formUser" action="saveUser.action" method="post">
<s:if test="user.userId != null && user.userId != '' ">
	<input type="hidden" name="user.userId" id="user.userId" value="<s:property value="user.userId"/>" />
</s:if>
<input type="hidden" id="user.password" type="text" value="<s:property value="user.password"/>" name="user.password"/>
<div id="editpanel">
<div id="editsubpanel" class="editsubpanel">
 
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr class="header">
		<th colspan="4" >
			<s:if test="user.userId != null && user.userId != '' ">修改用户</s:if> <s:else>新增用户</s:else>
		</th>
	</tr>
	<tr>
		<td width="15%" align="right" class="listbar">用户登录名:</td>
		<td width="35%">
			<s:if test="user.userId != null && user.userId != ''">
				<input id="user.userEname" name="user.userEname" type="text" value="<s:property value="user.userEname"/>" readonly class="readonly"/>&nbsp;
            </s:if> 
            <s:else>
				<input id="user.userEname" name="user.userEname" value="<s:property value="user.userEname"/>"  type="text" />&nbsp;
			</s:else> 
			<span class="spanstar">*</span>
		</td>
		<td width="15%" align="right" class="listbar">用户中文名:</td>
		<td>
			<input id="user.userCname" name="user.userCname" type="text" value="<s:property value="user.userCname" />"  maxlength="20" /> 
			<span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td align="right" class="listbar">用户机构:</td>
		<td colspan=3>
		    <input id="instId" name="user.instId" type="hidden" value="<s:property value="user.instId" />"/>
		    <input id="instName" readonly name="user.instName" type="text" value='<s:if test="user.instId!=null"><s:property value="user.baseInst.instName" />[<s:property value="user.instId" />]</s:if>' />
		   <%--<s:select id="user.instId" name="user.instId" list="instList" listKey="instId" listValue="instSmpName" theme="simple" />--%>
		</td>
	</tr>
	<tr>
		<td align="right" class="listbar">座机电话:</td>
		<td><input id="user.tel" type="text" value="<s:property value="user.tel"/>" name="user.tel" maxlength="20"/></td>
		<td align="right" class="listbar">手机号:</td>
		<td><input id="user.mobile" type="text" value="<s:property value="user.mobile"/>" name="user.mobile"  maxlength="20"/></td>
	</tr>
	<tr>
		<td align="right" class="listbar">地址:</td>
		<td><input id="user.address" type="text" value="<s:property value="user.address"/>" name="user.address" maxlength="80"/></td>
		<td align="right" class="listbar">邮箱:</td>
		<td><input id="user.email" type="text" value="<s:property value="user.email"/>" name="user.email" maxlength="40"/><span class="spanstar"></span></td>
	</tr>
	<!-- 
	<tr>
		<td align="right">启用日期:</td>
		<td>
			<s:if test="user.userId != null && user.userId != '' && user.startDate != null ">
				<input id="user.startDate" type="text"  name="user.startDate" class="Wdate"  onFocus="WdatePicker()"  value="<s:text name="format.date"><s:param value="user.startDate"/></s:text>"/>
			</s:if>
			<s:else>
				<input id="user.startDate" type="text" class="Wdate" name="user.startDate" onFocus="WdatePicker()" />
			</s:else> 
		</td>
		<td align="right">终止日期:</td>
		<td>
			<s:if test="user.userId != null && user.userId != '' && user.endDate != null">
				<input id="user.endDate" name="user.endDate" type="text" class="Wdate" onFocus="WdatePicker()"  value="<s:text name="format.date"><s:param value="user.endDate"/></s:text>" />
			</s:if>
			<s:else>
				<input id="user.endDate" name="user.endDate" type="text" class="Wdate" onFocus="WdatePicker()" />
			</s:else> 
		</td>
	</tr> -->
	<tr>
		<td align="right" class="listbar">描述:</td>
		<td colspan=3><textarea id="user.description" name="user.description" style="width:420px;height:80px;" ><s:property value="user.description"/></textarea></td>
	</tr>
</table>
</div>
</div>
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px">		
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="findOutSubmit()" name="BtnSave" value="保存" id="BtnSave"/>
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="window.close()" name="BtnReturn" value="关闭" id="BtnReturn"/>	
	</div>
</form>
</div>
</body>
</html>