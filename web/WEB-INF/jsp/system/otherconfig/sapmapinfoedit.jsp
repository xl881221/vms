<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../../common/include.jsp"%>
<%@include file="../../common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
	<head>
		<title>子系统表单</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

		<link href="<c:out value="${sysTheme}"/>/css/subWindow.css"
			type="text/css" rel="stylesheet">
		<link href="<c:out value="${sysTheme}"/>/css/subWindow.css"
			type="text/css" rel="stylesheet">
		<script language="javascript" type="text/javascript">
		//提交表单验证
		//标识页面是否已提交
		var subed = false;
		function checkForm(){
			//验证是否重复提交
		    if (subed == true){
		       alert("信息正在发送给服务器，请不要重复提交信息！");
		       return false;
		    }
		    if(fucCheckNull(document.getElementById("sapMapInfo.dpvId"),"请输入指标号")==false){
		       return false;
		    }
		    if(fucCheckNull(document.getElementById("sapMapInfo.dpvName"),"请输入指标名称")==false){
		       return false;
		    }
		    if(fucCheckNull(document.getElementById("sapMapInfo.acciNo"),"请输入科目号")==false){
		       return false;
		    }
   			return true;
		}
			function submitForm(){
				//保存方法
				if(true == checkForm()){
					document.forms[0].action = '<c:out value='${webapp}'/>/saveSapMapInfo.action';
					document.forms[0].submit();
					subed=true;
					//查询页面刷新
					//document.getElementById("sapMapInfo").submit();
					
				}
				
			}
		</script>
	</head>
	<body>
		<div class="showBoxDiv">
			<form name="frm"
				action="<c:out value='${webapp}'/>/saveSapMapInfo.action"
				method="post" enctype="multipart/form-data">
				<div id="editpanel">
					<div id="editsubpanel" class="editsubpanel">
						<div style="overflow: auto; width: 100%;">
							<input id="sapMapInfo.id" name="sapMapInfo.id"
											type="hidden" value="<s:property value="sapMapInfo.id" />" />
							<table id="contenttable" class="lessGrid" cellspacing="0"
								width="100%" align="center" cellpadding="0">
								<tr>
									<th colspan="4">
										<s:if test="sapMapInfo.id==null ">新增总账配置</s:if>
										<s:else>修改总账配置</s:else>
									</th>
								</tr>

								<tr>
									<td width="15%" style="text-align: right">
										指标号:
									</td>
									<td width="35%">
										<input id="sapMapInfo.dpvId" name="sapMapInfo.dpvId"
											type="text" value="<s:property value="sapMapInfo.dpvId" />" />
										<span class="spanstar">*</span>
									</td>
									<td width="15%" style="text-align: right">
										指标名称:
									</td>
									<td width="35%">
										<input id="sapMapInfo.dpvName" name="sapMapInfo.dpvName"
											type="text" value="<s:property value="sapMapInfo.dpvName" />" />
										<span class="spanstar">*</span>
									</td>
								</tr>
								<tr>
									<td width="15%" style="text-align: right">
										是否取减法:
									</td>
									<td width="35%">
										<s:select theme="simple" list="#{'Y':'是','N':'否'}"
											name="sapMapInfo.isDel"
											headerValue="sapMapInfo.isDel" cssStyle="width:132;"></s:select>
										<span class="spanstar">*</span>
									</td>
									<td width="15%" style="text-align: right">
										科目号:
									</td>
									<td width="35%">
										<input id="sapMapInfo.acciNo" name="sapMapInfo.acciNo"
											type="text" value="<s:property value="sapMapInfo.acciNo" />" />
										<span class="spanstar">*</span>
									</td>
								</tr>
								<tr>
									<td width="15%" style="text-align: right">
										产品号:
									</td>
									<td width="35%">
										<input id="sapMapInfo.prodId" name="sapMapInfo.prodId"
											type="text" value="<s:property value="sapMapInfo.prodId" />" />
									</td>
									<td width="15%" style="text-align: right">
										钆差组别:
									</td>
									<td width="35%">
										<input id="sapMapInfo.drFlag" name="sapMapInfo.drFlag"
											type="text" value="<s:property value="sapMapInfo.drFlag" />" />
									</td>
								</tr>
								<tr>
									<td width="15%" style="text-align: right">
										数据类别:
									</td>
									<td width="35%">
										<s:select theme="simple" list="#{'BAL':'余额','DBAL':'借方余额','CBAL':'贷方余额','DAML':'借方月累计发生额','CAML':'贷方月累计发生额','YDAML':'借方年累计发生额','YCAML':'贷方年累计发生额'}"
											emptyOption="true" name="sapMapInfo.dataType"
											headerValue="sapMapInfo.dataType" cssStyle="width:132;"></s:select>
									</td>
									<td width="15%" style="text-align: right">
										大于或小于零:
									</td>
									<td width="35%">
										<s:select theme="simple" list="#{'Y':'结果要求大于零','N':'结果要求小于零','-':'对结果无要求'}"
											emptyOption="true" name="sapMapInfo.posFlag"
											headerValue="sapMapInfo.posFlag" cssStyle="width:132;"></s:select>
									</td>
								</tr>
							</table>
						</div>
					</div>
					<div id="ctrlbutton" class="ctrlbutton" style="border: 0px">
						<div type="btn"
							img="<c:out value="${webapp}"/>/image/button/save.gif"
							onclick="submitForm()" name="BtnSave" value="保存" id="BtnSave"></div>
						<div style="margin-left: 5px" type="btn"
							img="<c:out value="${webapp}"/>/image/button/cancel.gif"
							onclick="CloseWindow()" name="BtnReturn" value="关闭"
							id="BtnReturn"></div>
					</div>
			</form>
		</div>
	</body>
</html>