<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../../common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%@include file="../../../common/include.jsp"%>	
	<link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
	<link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
		<script language="javascript" type="text/javascript">
		<s:if test="inst.instId != null && inst.instId != ''">
		var adds = '0';
		</s:if>
		<s:else>
		var adds = '1';
		</s:else>
		window.onload = function(){
		}
	</script>
</head>
<body>
<div class="showBoxDiv">
<form name="viewUser" method="post">
<table id="tbl_current_status">
			<tr>
				<td>
					<img src="<c:out value="${sysTheme}"/>/img/jes/icon/home.png" />
					<span class="current_status_submenu">机构修改</span>
				</td>
			</tr>
		</table>
	<div id="editpanel">
	<div class="editsubpanel" style="overflow: auto; width: 100%;" >     
	<div id="editsubpanel" >
	<table id="contenttable" class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" display="none" style="border-collapse: collapse;">
		<tr>
			<th width="15%" align="right">&nbsp;</th>
			<th width="15%" align="right">变更前</th>
			<th width="15%" align="right">变更后</th>
		</tr>
		<tr>
			<th width="15%" align="right">机构编号:</td>
			<td width="35%"><s:property value="oldInst.instId" /></td>
			<td width="35%"><s:property value="newInst.instId" /></td>
		</tr>
		<tr>
			<th width="15%" align="right">机构名称:</td>
			<td width="35%"><s:property value="oldInst.instName" /></td>
			<td width="35%"><s:property value="newInst.instName" /></td>
		</tr>
		<tr>
			<th align="right">机构简称:</th>
			<td ><s:property value="oldInst.instSmpName" /></td>
			<td ><s:property value="newInst.instSmpName" /></td>
		</tr>
		<tr>
			<th align="right">机构地址:</th>
			<td><s:property value="oldInst.address" /></td>
			<td><s:property value="newInst.address" /></td>
		</tr>
		<tr>
			<th align="right">机构邮箱:</th>
			<td><s:property value="oldInst.email" /></td>
			<td><s:property value="newInst.email" /></td>
		</tr>
		<tr>
			<th align="right">机构邮编:</th>
			<td><s:property value="oldInst.zip" /></td>
			<td><s:property value="newInst.zip" /></td>
		</tr>
		<tr>
			<th align="right">机构电话:</th>
			<td><s:property value="oldInst.tel" /></td>
			<td><s:property value="newInst.tel" /></td>
		</tr>
		<tr>
			<th align="right">机构传真:</th>
			<td><s:property value="oldInst.fax" /></td>
			<td><s:property value="newInst.fax" /></td>
		</tr>
		<tr>
			<th align="right">排序值:</th>
			<td><s:property value="oldInst.orderNum" /></td>
			<td><s:property value="newInst.orderNum" /></td>
		</tr>
		<tr>
			<th align="right">机构级别:</th>
			<td><s:property value="oldInst.instLevel" /></td>
			<td><s:property value="newInst.instLevel" /></td>
		</tr>
		<tr>
			<th align="right">上级机构:</th>
			<td><s:property value="oldInst.parentInstId" /></td>
			<td><s:property value="newInst.parentInstId" /></td>
		</tr>
		
		<tr>
			<th align="right">启用日期:</th>
			<td>
				<s:if test="newInst.startDate != null ">
					<s:text name="format.date"><s:param value="oldInst.startDate"/></s:text>
				</s:if>	
			</td>
			<td>
				<s:if test="newInst.startDate != null ">
					<s:text name="format.date"><s:param value="newInst.startDate"/></s:text>
				</s:if>	
			</td>
		</tr>
		<tr>
			<th align="right">是否业务机构:</th>
			<td><s:if test="oldInst.isBussiness =='true'">是</s:if><s:else>否</s:else></td>
			<td><s:if test="newInst.isBussiness =='true'">是</s:if><s:else>否</s:else></td>
		</tr>
		<tr>
			<th align="right">启用标识:</th>
			<td><s:if test="oldInst.enabled=='true'">是</s:if><s:else>否</s:else></td>
			<td><s:if test="newInst.enabled=='true'">是</s:if><s:else>否</s:else></td>
		</tr>
		<tr>
			<th align="right">是否总行:</th>
		<td><s:if test="oldInst.isHead=='true'">是</s:if><s:else>否</s:else></td>
		<td><s:if test="newInst.isHead=='true'">是</s:if><s:else>否</s:else></td>
		</tr>
		<tr>
		<th width="15%" align="right">区域:</td>
		<td> <div>
		<table><tr>
		<td colspan="3">
		<input id="oldregion" type="text" value="<s:property value="oldInst.instRegion"/>" name="oldregion" style="display: none;"/>
			  <select name="province" id="province" disabled="true">
			  <option><s:property value="oldFRegion" /></option>
			  </select>
			  <select name="city" id="city" disabled="true">
			  <option><s:property value="oldMRegion" /></option>
			  </select>
			<select name="area" id="area" disabled="true">
			  <option><s:property value="oldLRegion" /></option>
			  </select>
		  <input id="oldInst.instRegion" type="hidden" value="" name="oldInst.instRegion"/><span class="spanstar">*</span>
		</td>
		</tr></table>
		</div></td>
		
		<td><div>
		<table><tr>
		<td colspan="3">
		<input id="newregion" type="text" value="<s:property value="newInst.instRegion"/>" name="newregion" style="display: none;"/>
			  <select name="nprovince" id="nprovince" disabled="true">
			    <option><s:property value="newFRegion" /></option>
			  </select>
			  <select name="ncity" id="ncity" disabled="true">
			    <option><s:property value="newMRegion" /></option>
			  </select>
			<select name="narea" id="narea" disabled="true">
			    <option><s:property value="newLRegion" /></option>
			  </select>
		  <input id="newInst.instRegion" type="hidden" value="" name="newInst.instRegion"/><span class="spanstar">*</span>
		</td>
		</tr></table>
		</div></td>
		</tr>
		
		
		<tr>
			<th align="right">描述:</th>
			<td ><s:property value="oldInst.description" /></td>
			<td ><s:property value="newInst.description" /></td>
		</tr>
			
		<s:if test="showAuditColumn">
			<tr>
                		<th align="right">审核人:</td>
                		<td><s:property value="newInst.auditUser" /></td>
                		</tr>
                		<tr>
                		<th align="right">审核时间:</td>
                		<td>11<s:date name="newInst.auditTime" format="yyyy-MM-dd"/> </td>
                		</tr>
                </s:if>
	</table>	
	</div>
	</div>
	
	<div  id="ctrlbutton" class="ctrlbutton"  style="border:0px">		
		<div style="float:right;margin-right: 10px;height:25px;margin-top:5px" type="btn" img="<c:out value="${webapp}"/>/image/button/cancel.gif" onClick="window.close()" name="btnReturn" value="关闭" id="btnReturn"></div>	  	
	</div>
</form>
</div>
</body>
</html>
