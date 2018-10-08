<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*,fmss.dao.entity.UAuthResMapDO"%>
<%
Map resMapKey = (Map)request.getAttribute("resMapKey");
Map resMapCtn = (Map)request.getAttribute("resMapCtn");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<%@page import="fmss.services.DictionaryService"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%@include file="../../common/include.jsp"%>
	<script language="javascript" type="text/javascript">
		var resMapArray = '<s:property value="%{resMapArray}"/>';
		var autoCheck = function(){
			var resArr = resMapArray.split(',');
			var checks = document.getElementsByName("resMap");
			for (i = 0; i < checks.length; i++) {
					if(resMapArray.indexOf(checks[i].value)>-1){
						checks[i].checked=true;
					}
			}
		}
		var query = function() {
			document.forms[0].action='queryConfigRes.action';
			document.forms[0].submit();
		}
		var openNew = function(url) {
			window.open(url,'','top=150,left=260,width=600,height=500,menubar=no,status=yes');
		}
		
		var saveCfg = function(){
			if(check('resMapList')){
				document.forms[0].action="saveResByRole.action";
				document.forms[0].submit();
				alert('保存成功');
				window.opener.location.reload();
			}
		}
	</script>
</head>
<!-- <body onmousemove="MM(event)" onmouseout="MO(event)"> -->
<body>
<form name="frm" action="saveResByRole.action" method="post">
    <s:if test="role.roleId != null && role.roleId != '' ">
    	<input type="hidden" name="role.roleId" value="<s:property value="role.roleId"/>"/>
    </s:if>
	<table id="tbl_main" cellpadding="0" cellspacing="0">
		<tr>
			<td align="center">
				<table id="tbl_tools" cellpadding="1" cellspacing="0">
					<tr>
						<td align="left">
						<input type="button" name="BtnSave" value="保&nbsp;存" id="BtnSave" onclick="saveCfg()"/>
		            </td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
   	<div style="overflow: auto; width: 100%; height: 390px;" id="lessGridList">
        <table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" display="none" style="border-collapse: collapse;">
            <tr class="lessGrid head">
                <th width="3%">
                	<input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="cbxselectall(this,'resMapList')"/>
                </th>
                <th>资源类型</th>
				<th>资源编号</th>
				<th>资源名称</th>
            </tr>
 			<%
 			int i=1;
 			if(resMapKey!=null&&resMapKey.size()>0){
 				Set keySet = resMapKey.keySet();
 				for(Iterator iterator = keySet.iterator(); iterator.hasNext();){
 					String key = (String) iterator.next();
 					UAuthResMapDO resDO = (UAuthResMapDO)resMapKey.get(key);
 					String srcKeyField = resDO.getSrcKeyField();
 					String srcIdField = resDO.getSrcIdField();
 					String srcNameField = resDO.getSrcNameField();
 					List valueList = (List)resMapCtn.get(key);
 					for(Iterator _iterator = valueList.iterator(); _iterator.hasNext();){
 						Map row = (Map)_iterator.next();
 						
 			%>
 			
				<tr align="center" class="<%if(i%2==0){out.print("lessGrid rowA");}else{out.print("lessGrid rowB");} %>">
				<td align=center>
					<input type="checkbox" style="width:13px;height:13px;" name="resMapList" value="<%out.print(resDO.getResId()); %>;<%out.print(row.get(srcIdField)); %>;<%out.print(row.get(srcNameField)); %>"/>
				</td>
				<td><%out.print(key); %></td>
				<td><%out.print(row.get(srcIdField)); %></td>
				<td><%out.print(row.get(srcNameField)); %></td>
				</tr>
			<%
						i++;
 					}
 				}
			}else{ %>
				<tr class="grd_row_1">
					<td colspan="3" align="center">暂时没有需要配置的资源</td>
				</tr>
			<%} %>
        </table>
    </div>
    <div id="anpBoud" align="Right" style="width:100%;">
        <table width="100%" cellspacing="0" border="0">
            <tr>
                <td align="left"></td>
                <td align="right"><ww:component template="pagediv"/></td>
            </tr>
        </table>
    </div>
</form>
</body>
</html>
