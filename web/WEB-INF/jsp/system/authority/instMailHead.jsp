<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>   
    <title>配置部门邮箱信息 </title>
    <meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@include file="../../common/include.jsp"%>
	<script type="text/javascript" src="scripts/select.js"></script>
	<script language="javascript" type="text/javascript">
		function ajaxReadData(){
		    var selectObj=document.getElementById('systemId');
		    var systemId=selectObj.value;
	        var systemName=selectObj.options[selectObj.selectedIndex].text;
		    var bankId= parent.frames["InstTreeFrame"].document.getElementById('instId').value;
		    parent.frames["InstMailUserResultFrame"].location.href="<c:out value='${webapp}'/>/system/authority/instMailUser.action?bankId="+bankId + "&systemId="+systemId;
		    parent.frames["InstMailResultFrame"].location.href="<c:out value='${webapp}'/>/system/authority/instMailMain.action?bankId="+bankId + "&systemId="+systemId+"&systemName="+systemName;
	    }
	</script>
</head>
<body>
	
	<table id="tbl_main" cellpadding="0" cellspacing="0">
		<tr>
			<td align="left">
			<table id="tbl_current_status">
				<tr>
					<td>
						<img src="<c:out value="${sysTheme}"/>/img/jes/icon/home.png" />
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu">机构邮箱配置管理</span>
					</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
	<div class="showBoxDiv">
		<form name="formInst" id="formInst" action="" method="post">
		    <table>
				<td>模块名称:</td>
				<td>
					<select id="systemId" name="systemId" onchange="ajaxReadData(this)" style="width: 133;">				
						<s:iterator value="instSystemReal" id="s">
							<option value="<s:property value="#s.systemId"/>" <s:if test="vSystemId==#s.systemId">selected</s:if>>
								<s:property value="#s.systemCname"/>
							</option>
						</s:iterator>
					</select>
				</td>
			</table>
		</form>
	</div>
</body>
</html>
