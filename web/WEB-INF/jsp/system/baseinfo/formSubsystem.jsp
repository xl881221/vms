<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@include file="../../common/include.jsp"%>
	<title>系统处理</title>
	<link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
	<script language="javascript" type="text/javascript">
		<!--
		//标识页面是否已提交
		var subed = false;
		function checkForm(){
			//验证是否重复提交
			if (subed == true){
				alert("信息正在发送给服务器，请不要重复提交信息！");
				return false;
			}
			//验证系统中文名是否为空
			if(fucCheckNull(document.getElementById("sysCname"),"请输入系统中文名")==false){
				return false;
			}
			//验证系统中文简称是否为空
			if(fucCheckNull(document.getElementById("sysNickCname"),"请输入系统中文简称")==false){
				return false;
			}
			//验证系统英文名是否为空
			if(fucCheckNull(document.getElementById("sysEname"),"请输入系统英文名")==false){
				return false;
			}
			//验证系统英文简称是否为空
			if(fucCheckNull(document.getElementById("sysNickEname"),"请输入系统英文简称")==false){
				return false;
			}
			//验证菜单序号
			if(fucIsInteger(document.getElementById("sysMenuOrderNum"),"菜单序号应该为整数")==false){
				return false;
			}
			//验证菜单序号
			if(!new RegExp("^[http|https]+://[^/]+(:[^/]+)?/[^/]+").test(document.getElementById("subSystem.linkSiteUrl").value)){
				alert("系统外网地址格式不正确");
				return false;
			}
			if(!new RegExp("^[http|https]+://[^/]+(:[^/]+)?/[^/]+").test(document.getElementById("subSystem.linkSiteInnerUrl").value)){
				alert("系统内网地址格式不正确");
				return false;
			}
			subed=true;
			return true;
		}
		function submitForm(){
			if(true == checkForm()){
				document.forms[0].action = '<c:out value='${webapp}'/>/saveSystem.action';
				document.forms[0].submit();
			}
		}
		function updateSystemIcon(imgName){
			var img = document.getElementById("systemIcon");
			if(null != img){
				img.style.backgroundImage ="url('<c:out value="${webapp}"/>/image/system/" + imgName + "')";
			}
		}
		-->
	</script>
	<!--<script language="javascript">-->
	<!--window.onload = function(){-->
	<!--window.dialogHeight = "280px";-->
	<!--}-->
	<!--</script>-->
</head>
<body scroll="no" style="overflow:hidden;">
	<div class="showBoxDiv">
	<form name="frm" action="<c:out value='${webapp}'/>/createSystem.action" method="post" enctype="multipart/form-data">
	<div id="editpanel">
		<div id="editsubpanel" class="editsubpanel">
			<div style=" display:none;">
				<input type="hidden" name="operType" value="<s:property value="operType" />" />
				<input type="hidden" name="subSystem.systemId" value="<s:property value="subSystem.systemId" />" />
				<input type="hidden" name="subSystem.resDbType" value="<s:property value="subSystem.resDbType" />" />
				<input type="hidden" name="subSystem.resDbUserId" value="<s:property value="subSystem.resDbUserId" />" />
				<input type="hidden" name="subSystem.resDbServerPort" value="<s:property value="subSystem.resDbServerPort" />" />
				<input type="hidden" name="subSystem.resDbServerIp" value="<s:property value="subSystem.resDbServerIp" />" />
				<input type="hidden" name="subSystem.resDbSid" value="<s:property value="subSystem.resDbSid" />" />
				<input type="hidden" name="subSystem.resDbPassword" value="<s:property value="subSystem.resDbPassword" />" />
				<input type="hidden" name="subSystem.menuRes" value="<s:property value="subSystem.menuRes" />" />
				<input type="hidden" name="subSystem.unitLoginUrl" value="<s:property value="subSystem.unitLoginUrl" />" />
				<input type="hidden" name="subSystem.dbUrl" value="<s:property value="subSystem.dbUrl" />" />
				<input type="hidden" name="subSystem.menuImgSrc" value="<s:property value="subSystem.menuImgSrc" />" />
			</div>
			<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
			<tr>
				<th colspan="4">
					<s:if test="operType=='add'">新增子系统</s:if>
					<s:elseif test="operType=='edit'">修改子系统</s:elseif>
				</th>
			</tr>
			<tr>
				<td class="contnettable-subtitle" colspan="10">系统基本信息</td>
			</tr>
			<tr>
				<td width="15%" style="text-align:right" class="listbar">系统中文名:</td>
				<td width="35%">
					<input id="sysCname" name="subSystem.systemCname" type="text" value="<s:property value="subSystem.systemCname" />" />
					<span class="spanstar">*</span>
				</td>
				<td width="15%" style="text-align:right" class="listbar">系统中文简称:</td>
				<td>
					<input id="sysNickCname" name="subSystem.systemNickCname" type="text" value="<s:property value="subSystem.systemNickCname" />" />
					<span class="spanstar">*</span>
				</td>
			</tr>
			<tr>
				<td width="15%" style="text-align:right" class="listbar">系统英文名:</td>
				<td width="35%">
					<input id="sysEname" name="subSystem.systemEname" type="text" value="<s:property value="subSystem.systemEname" />" />
					<span class="spanstar">*</span>
				</td>
				<td width="15%" style="text-align:right" class="listbar">系统英文简称:</td>
				<td>
					<input id="sysNickEname" name="subSystem.systemNickEname" type="text" value="<s:property value="subSystem.systemNickEname" />" />
					<span class="spanstar">*</span>
				</td>
			</tr>
			<tr>
				<td style="text-align:right" class="listbar">显示方式</td>
				<td>
					<s:select name="subSystem.display" list="subSysDisplayList" listKey='key' listValue='value'/>
				</td>
				<td style="text-align:right" class="listbar">是否启用</td>
				<td>
					<s:checkbox theme="simple" name="subSystem.enabled" value="subSystem.enabled"></s:checkbox>
				</td>
			</tr>
			<!--
			<tr>
				<td class="contnettable-subtitle" colspan="10">数据库配置</td>
			</tr>
			<tr>
				<td style="text-align:right">数据库驱动:</td>
				<td colspan="9">
					<input style="width:415px" name="subSystem.dbDriverClass" type="text" value="<s:property value="subSystem.dbDriverClass" />" />
				</td>
			</tr>
			<tr>
				<td style="text-align:right">数据库链接:</td>
				<td colspan="9">
					<input style="width:415px" name="subSystem.dbUrl" type="text" value="<s:property value="subSystem.dbUrl" />" />
				</td>
			</tr>
			<tr>
				<td style="text-align: right">数据库用户名:</td>
				<td>
					<input name="subSystem.dbUserId" type="text" value="<s:property value="subSystem.dbUserId" />" />
				</td>
				<td style="text-align: right">数据库密码:</td>
				<td>
					<input name="subSystem.dbPassword" type="password" value="<s:property value="subSystem.dbPassword" />" />
				</td>
			</tr>
			-->
			<tr>
				<td class="contnettable-subtitle" colspan="10">系统菜单配置</td>
			</tr>
			<tr>
				<td width="100" style="text-align:right" class="listbar">菜单序号:</td>
				<td>
					<input id="sysMenuOrderNum" name="subSystem.menuOrderNum" type="text" value="<s:property value="subSystem.menuOrderNum" />" />
				</td>
				<td style="text-align:right" class="listbar">菜单表名:</td>
				<td>
					<input name="subSystem.menuTable" readonly=true type="text" value="<s:property value="subSystem.menuTable" />" />(不可改)
				</td>
			</tr>
			<tr>
				<td style="text-align:right" class="listbar">系统外网地址:</td>
				<td colspan="3">
					<input style="width:459;" name="subSystem.linkSiteUrl" id="subSystem.linkSiteUrl" type="text" value="<s:property value="subSystem.linkSiteUrl" />" />
				</td>
			</tr>
			<tr>
				<td style="text-align:right" class="listbar">系统内网地址:</td>
				<td colspan="3">
					<input style="width:459;" name="subSystem.linkSiteInnerUrl" id="subSystem.linkSiteInnerUrl" type="text" value="<s:property value="subSystem.linkSiteInnerUrl" />" />
				</td>
			</tr>
			</table>
		</div>
	</div>
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
		<input type="button" class="tbl_query_button"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			onclick="submitForm()" name="BtnSave" value="保存" id="BtnSave"/>
		<input type="button" class="tbl_query_button"
			onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
			onclick="CloseWindow()" name="BtnReturn" value="关闭" id="BtnReturn"/>
	</div>
	</form>
	</div>
</body>
</html>