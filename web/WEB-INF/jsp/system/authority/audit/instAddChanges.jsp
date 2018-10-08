<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@include file="/WEB-INF/jsp/common/include.jsp"%>
	<link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
	<link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
	<%@include file="/WEB-INF/jsp/common/publicComplete.jsp"%>
	<script language="javascript" type="text/javascript">
		<s:if test="inst.instId != null && inst.instId != ''">
		var adds = '0';
		</s:if>
		<s:else>
		var adds = '1';
		</s:else>
		window.onload = function(){
			var region = '<s:property value="inst.instRegion"/>';
			c = new Region(new Location("province","city","area", region));
			c.init();
			$G('province').disabled = true;
			$G('city').disabled = true;
			$G('area').disabled = true;
		}
	</script>
</head>
<body>
<div class="showBoxDiv">
<form name="viewUser" method="post">
	<div id="editpanel">
	<div id="editsubpanel" class="editsubpanel">
	<table id="contenttable" class="lessGrid" cellspacing="0"
		width="100%" align="center" cellpadding="0">
		<tr>
			<th colspan="4">机构查看</th>
		</tr>
		<tr>
		<td width="15%" align="right">机构编号:</td>
		<td width="35%">
			<s:property value="inst.instId"/>
		</td>
		<td width="15%" align="right">机构名称:</td>
		<td>
			<s:property value="inst.instName" />
		</td>
	</tr>
	<tr>
		<td align="right">机构简称:</td>
		<td>
			<s:property value="inst.instSmpName" />
        </td>
		<td align="right">上级机构:</td>
		<td>
			<s:if test="inst.parentInstId!=null"><s:property value="inst.parentInstName" />[<s:property value="inst.parentInstId" />]</s:if>
		</td>
	</tr>
	<tr>
		<td align="right">机构级别:</td>
		<td >
			<s:property value="inst.instLayer" />
		</td>
		<td align="right">启用标识:</td>
		<td>
			<s:property value="inst.enabledCH" />
		</td>
	</tr>
	<tr>
		<td align="right">机构地址:</td>
		<td><s:property value="inst.address"/></td>
		<td align="right">机构邮编:</td>
		<td><s:property value="inst.zip"/></td>
	</tr>
	<tr>
		<td align="right">机构电话:</td>
		<td><s:property value="inst.tel"/></td>
		<td align="right">机构传真:</td>
		<td><s:property value="inst.fax"/></td>
	</tr>
	<tr>
		<td align="right">启用日期:</td>
		<td>
			<s:if test="inst.startDate != null ">
				<s:date name="inst.startDate" format="yyyy-MM-dd"/>
			</s:if>
		</td>
		<td align="right">是否总行:</td>
		<td>
			<s:property value="inst.isHeadCH"/>
		</td>
	</tr>
	<tr>
		<td align="right">是否业务机构:</td>
		<td><s:property value="inst.isBussinessCH"/></td>
		<td align="right">排序值:</td>
		<td><s:property value="inst.orderNum"/></td>
	</tr>
	<tr>
		<td align="right">区域:</td>
		<td colspan="3">
		<input id="region" type="text" value="<s:property value="inst.instRegion"/>" name="region" style="display: none;"/>
			  <select name="province" id="province">
			  </select>
			  <select name="city" id="city">
			  </select>
			<select name="area" id="area">
			  </select>
			  
		  <input id="inst.instRegion" type="hidden" value="" name="inst.instRegion"/>
		</td>
		</tr>
	<!--  
	<tr>
		<td align="right">启用日期:</td>
		<td>
			<s:if test="inst.startDate != null ">
				<s:text name="format.date"><s:param value="inst.startDate"/></s:text>
			</s:if>
		</td>
		<td align="right">终止日期:</td>
		<td>
			<s:if test="inst.endDate != null ">
				<s:text name="format.date"><s:param value="inst.endDate"/></s:text>
			</s:if>
		</td>
	</tr>-->
	<tr>
		<td align="right">描述:</td>
		<td colspan=3><s:property value="inst.description"/></td>
	</tr>
	</table>
	</div>
	</div>
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
		<div  type="btn" img="<c:out value="${webapp}"/>/image/button/cancel.gif" onClick="window.close()" name="btnReturn" value="关闭" id="btnReturn"></div>
	  </div>
</form>
</div>
</body>
</html>
