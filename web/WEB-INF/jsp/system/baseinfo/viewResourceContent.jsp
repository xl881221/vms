<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@page
	import="java.util.*,fmss.common.util.*,fmss.dao.entity.UAuthResMapDO"%>
<%
			UAuthResMapDO uAuthResMap = (UAuthResMapDO) request
			.getAttribute("uAuthResMap");
	String srcId = uAuthResMap.getSrcIdField();
	String srcField = uAuthResMap.getSrcNameField();
	List list = (List) request.getAttribute("resContent");
	String msg = (String) request.getAttribute("type");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="../../common/include.jsp"%>
		<link href="<c:out value="${sysTheme}"/>/css/subWindow.css"
			type="text/css" rel="stylesheet">
		<script language="javascript" type="text/javascript">
		
		var page = {};
		
		page.submitForm = function(obj){
			if(Validator.Validate(obj,3)){
				return page.checkRepassword();
			}
			return false;
		}
		function query(){
			var left = document.getElementById("resCtn");
			var query = document.getElementById("txtQuery");

	        for(i=0;left&&i<left.options.length;i++){
		        if(query.value != "" && left[i].text.indexOf(query.value) > -1){
	        		left[i].selected = true;
		        }else{
		        	left[i].selected = false;
		        }
	        }
		}
	</script>
	</head>
	<body scroll="no" style="overflow:hidden;">
			<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0">
				<tr>
					<th colspan="4">资源内容查看</th>
				</tr>
				<tr align="left">
					<td id="tdUserControl" align="left">
						<s:if
							test="uAuthResMap.resId != null && uAuthResMap.resId != '' ">
							<input type="hidden" name="uAuthResMap.resId"
								value="<s:property value="uAuthResMap.resId"/>" />
						</s:if>
						<td align="left"> 查询： <input type="text" id="txtQuery" class="tbl_query_text" onkeyup="query()" style="width: 200px;"></td>
					</td>
				</tr>
			</table>

			<div style="  width: 100%; height: 360px" id="list1">
				<%
						if(list != null && list.size() > 0){
						out
						.println("<select name='resCtn' size='18' id='resCtn' style='width:100%;height: 355px'");
						out.println("multiple='multiple' onchange=''>");
						if(Constants.SYSTEM_MENU_TABLE.equalsIgnoreCase(msg)){
							Map menuMap = (Map) list.get(0);
							Set keys = menuMap.keySet();
							for(Iterator ite = keys.iterator(); ite.hasNext();){
								String key = (String) ite.next();
								List menus = (List) menuMap.get(key);
								out.print("<option value=\"" + key + "\">");
								out.println(key + "</option>");
								for(int i = 0; menus != null && i < menus.size(); i++){
									Map row = (Map) menus.get(i);
									out.print("<option value='" + row.get("itemcode")
									+ "'>");
									out.println(row.get("itemname") + "</option>");
								}
							}
						}else if(Constants.SYSTEM_INST_TABLE.equalsIgnoreCase(msg)){
							for(int j = 0; j < list.size(); j++){
								Map row = (Map) list.get(j);
								out.print("<option value='" + row.get("instId") + "'>");
								out.println(row.get("instName") + "</option>");
							}
						}else{
							for(int j = 0; j < list.size(); j++){
								Map row = (Map) list.get(j);
								out.print("<option value='" + row.get(srcId) + "'>");
								out.println(row.get(srcField) + "</option>");
							}
						}
						out.println("</select>");
					}
				%>
			</div>
			<div id="ctrlbutton" class="ctrlbutton1" style="border: 0px; text-align:right; padding-top: 15px;">
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="window.close()" name="BtnReturn" value="关闭" id="BtnReturn"/>
			</div>

	</body>

</html>
