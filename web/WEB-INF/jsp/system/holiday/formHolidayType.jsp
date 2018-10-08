<!--file: <%=request.getRequestURI() %> -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<style>
	.red{
		color:"red";
	}
	</style>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@include file="../../common/include.jsp"%>
	<title>
		<s:if test="update">
		修改节假日类别
		</s:if>
		<s:else>
		新增节假日类别
		</s:else>
	</title>
	<link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
	<script language="javascript" type="text/javascript">
		<s:if test="update">update = true;</s:if>
		<s:else>update = false;</s:else>
		//标识页面是否已提交
		var subed = false;
		function check(){
			//验证是否重复提交
			if (subed == true){
				alert("信息正在发送给服务器，请不要重复提交信息！");
				return false;
			}/*
			if(fucCheckNull(document.getElementById("holidayTypeDO.holidayType"),"请输入节假日类别编号") == false){
				return false;
			}
			var patrn=/^[A-Za-z0-9]+$/;
			if (!patrn.exec(document.getElementById("holidayTypeDO.holidayType").value)){
				var m = new MessageBox(document.getElementById("holidayTypeDO.holidayType"));
				m.Show("节假日类别编号只能数字和字母");
				document.getElementById("holidayTypeDO.holidayType").focus();
				return false;
			}
			if(fucCheckLength(document.getElementById("holidayTypeDO.holidayType"),10,"节假日类别编号超出指定长度,最大10字节")  == false){
				return false;
			}*/
			if(fucCheckNull(document.getElementById("holidayTypeDO.holidayName"),"请输入节假日名") == false){
				return false;
			}
			var patrn= /^[a-zA-Z0-9_\u4e00-\u9fa5]+$/;
			if (!patrn.exec(document.getElementById("holidayTypeDO.holidayName").value)){
				var m = new MessageBox(document.getElementById("holidayTypeDO.holidayName"));
				m.Show("节假日名只能是数字、字母、下划线、汉字");
				document.getElementById("holidayTypeDO.holidayName").focus();
				return false;
			}
			if(fucCheckLength(document.getElementById("holidayTypeDO.holidayName"),20,"节假日名超出指定长度,最大20字节")  == false){
				return false;
			}
			if(fucCheckLength(document.getElementById("holidayTypeDO.remark"),30,"节假日描述超出指定长度,最大30字节")  == false){
				return false;
			}
			subed=true;
			return subed;
		}
		function checkthis(obj){
			var boxArray = document.getElementsByName('role.enabled');
			for(var i=0;i<=boxArray.length-1;i++){
				if(boxArray[i]==obj && obj.checked){
					boxArray[i].checked = true;
				}else{
					boxArray[i].checked = false;
				}
			}
		}
		function findOutSubmit() {
			if(check()){
				if(update){
					document.frm.action = "updateHolidayType.action";
				}else{
					document.frm.action = "saveHolidayType.action";
				}
				document.frm.submit();
			}
		}
		window.onload = function(){
			var message  = '<s:property value="resultMessages"/>';
			if(message != '')
				alert(message);
		}
	</script>
</head>
<body>
	<div class="showBoxDiv">
	<form name="frm" id="frm" action="" method="post" onSubmit="return check()">
		<input type="hidden" name="holidayType" value="<s:property value="holidayTypeDO.holidayType"/>"/>
		<input type="hidden" name="isUpdate" value="<s:property value="update"/>"/>
		<div id="editpanel" >
			<div id="editsubpanel" class="editsubpanel">
			<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
			<tr>
				<th colspan="4">
					<s:if test="update">
					修改节假日类别
					</s:if>
					<s:else>
					新增节假日类别
					</s:else>
				</th>
			</tr>
			<tr style="display: none;">
				<td align="right">
					节假日类别编号:
				</td>
				<td>
					<s:if test="update">
					<input id="holidayTypeDO.holidayType" name="holidayTypeDO.holidayType" type="text" value="<s:property value="holidayTypeDO.holidayType"/>" readonly class="readonly" />
					</s:if>
					<s:else>
					<input name="holidayTypeDO.holidayType" type="text" value="<s:property value="holidayTypeDO.holidayType"/>" id="holidayTypeDO.holidayType"
						dataType="LimitB" min="1" max="50" msg="角色名不能为空且小于50字符"  />
					<span style="color: #FF0000">*</span>
					</s:else>
				</td>
			</tr>
			<tr>
				<td align="right">
					节假日名称:
				</td>
				<td>
					<input name="holidayTypeDO.holidayName" type="text" value="<s:property value="holidayTypeDO.holidayName"/>" id="holidayTypeDO.holidayName"
					dataType="LimitB" min="1" max="50" msg="角色名不能为空且小于50字符"  />
					<span style="color: #FF0000">*</span>
					<input id="holidayNameType" name="holidayNameType" type="hidden" value="<s:property value="holidayTypeDO.holidayName"/>" readonly class="readonly" />
				</td>
			</tr>
			<tr>
				<td align="right">
					节假日启用:
				</td>
				<td>
					<input name="holidayTypeDO.enable" type="radio" value="1" id="holidayTypeDO.enable" <s:if test="holidayTypeDO==null||holidayTypeDO.enable==null||holidayTypeDO.enable==1">checked</s:if> />启用&nbsp;&nbsp;
					<input name="holidayTypeDO.enable" type="radio" value="0" id="holidayTypeDO.enable" <s:if test="holidayTypeDO.enable==0">checked</s:if> />不启用
				</td>
			</tr>
			<tr>
				<td align="right">
					节假日备注:
				</td>
				<td>
					<textarea name="holidayTypeDO.remark" id="holidayTypeDO.remark"
						dataType="LimitB" style="width:260px;height:60px;"><s:property value="holidayTypeDO.remark"/></textarea>
				</td>
			</tr>
			<tr>
				<td class='red' colspan="2">
					<%if("true".equals(request.getAttribute("hasAML")))out.print("当使用反洗钱监测系统,AMLM为反洗钱系统专用标识");%>
				</td>
			</tr>
			</table>
			</div>
		</div>
		<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
			<input type="button" class="tbl_query_button"
				onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
				onclick="findOutSubmit()" name="BtnSave" value="保存" id="BtnSave"/>
			<input type="button" class="tbl_query_button"
				onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"
				onclick="window.close()" name="BtnReturn" value="关闭" id="BtnReturn"/>
		</div>
	</form>
</body>
</html>