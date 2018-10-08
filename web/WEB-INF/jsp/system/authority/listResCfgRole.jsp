<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!-- <%=request.getRequestURI() %> -->
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>

<head>
<% if("true".equals(request.getParameter("readOnly"))){%>
	<base target="_self">
<%}%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="../../common/include.jsp"%>
<%@include file="../../common/commen-ui.jsp"%>
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
		document.forms[0].action='queryResByRole.action';
		document.forms[0].submit();
	}
	function add(){
		var rr = document.getElementById("authResMap.resId");

		/*
		if(rr.selectedIndex == 0){
			alert("请选择资源名");
			return;
		}
		*/
		var date = new Date();
		var url = 'showResTreeByRole.action?role.roleId=<s:property value="role.roleId"/>&authResMap.resId='+rr[rr.selectedIndex].value+'&uBaseConfig.systemId=<s:property value="uBaseConfig.systemId"/>&'+date.getTime();
		OpenModalWindowSubmit(url,450,530,true);
	}
	function OpenModalWindowSubmit(newURL,width,height,needReload) {
		try {
			var retData = false;
	
			if(typeof(width) == 'undefined'){
				width 	= screen.width * 0.9;
			}
			if(typeof(height) == 'undefined'){
				height 	= screen.height * 0.9;
			}
			if(typeof(needReload) == 'undefined'){
				needReload 	= false;
			}
			retData = showModalDialog(newURL, 
					  window, 
					  "dialogWidth:" + width
					+ "px;dialogHeight:" + height
					+ "px;center=1;scroll=1;help=0;status=0;");
	
			if(needReload && retData){
				query();
			}
		} catch (err) {
		}
	}
</script>
</head>
<body>
    <form name="frm" action="viewResByRole.action" method="post">
    <s:if test="role.roleId != null && role.roleId != '' ">
    	<input type="hidden" name="role.roleId" value="<s:property value="role.roleId"/>"/>
    	<input type="hidden" name="role.systemId" value="<s:property value="role.systemId"/>"/>
    </s:if>
    <input type="hidden" name="uBaseConfig.systemId" value="<s:property value="uBaseConfig.systemId"/>"/>
    <input type="hidden" name="roleId" value="<s:property value="role.roleId"/>"/>
    <input type="hidden" name="readOnly" value="<s:property value="readOnly"/>"/>
    <input type="hidden" name="systemId" value="<s:property value="uBaseConfig.systemId"/>"/>
   <div class="centercondition1">
	<div class="widthauto">
		<table id="tbl_main" cellpadding="0" cellspacing="0">
			<tr>
				<td align="center">
					<div id="tbl_query">
						<table>
							<tr>
								<td align="left">资源名：
								<span><s:select list="resNames" headerKey="" headerValue="全部" id="authResMap.resId" name="authResMap.resId" listKey="resId" listValue="resName" value="authResMap.resId"  /></span>
								<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="query()" name="BtnView" value="查询" id="BtnView" />
								<% if(!"true".equals(request.getParameter("readOnly"))){%>
								<input type="button" class="tbl_query_button mleft5" onMouseMove="this.className='tbl_query_button_on mleft5'" onMouseOut="this.className='tbl_query_button mleft5'" onclick="add()" name="BtnSave" value="添加" id="BtnSave" />
								<input type="button" class="tbl_query_button mleft5" onMouseMove="this.className='tbl_query_button_on mleft5'" onMouseOut="this.className='tbl_query_button mleft5'" onclick="beforeDelete('delAllResByRole.action','resMapList')" name="BtnSave" value="删除" id="BtnSave" />
								<%}%>
								</td>
							</tr>
						</table>
					</div>	
				</td>
			</tr>
		</table>
	</div>
   <div id="lessGridList1">
        <table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" display="none" style="border-collapse: collapse; width:100%;">
            <tr class="lessGrid head">
                <% if(!"true".equals(request.getParameter("readOnly"))){%>
						<th width="5%" style="text-align:center">
		                	<input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="cbxselectall(this,'resMapList')"/>
		                </th>
				<%}%>
                <th width="20%" style="text-align:center">资源名</th>
				<th width="40%" style="text-align:center">资源编号</th>
				<th width="40%" style="text-align:center">资源名称</th>
            </tr>
 			<s:iterator value="resMapList" status="stuts" id="row">
				<tr align="center" class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
					<% if(!"true".equals(request.getParameter("readOnly"))){%>
						<td>
							<input type="checkbox" style="width:13px;height:13px;" name="resMapList" value="<s:property value="resId"/>;<s:property value="resDetailValue" />;<s:property value="systemId" />;<s:property value="resDetailName" />"/>
						</td>
					<%}%>
					
	                <td><s:property value="resName" /></td>
	                <td><s:property value="resDetailValue" /></td>
	                <td align="center">
	                <!-- 注意：此处的判断是写死的 -->
	                <s:if test="'00003'==role.systemId && resId=='34'">
                	<s:property value="systemCname" />-
                	</s:if><s:property value="resDetailName" />
                	</td>
				</tr>
			</s:iterator>
			<s:if test="resMapList==null||resMapList.size==0">
				<tr class="grd_row_1">
					<td colspan="8" align="center">
						暂时没有记录
					</td>
				</tr>
			</s:if>
        </table>
        </div>
    <div id="anpBoud" align="Right" style="width:100%; margin-left:5px;">
        <table width="100%" cellspacing="0" border="0">
            <tr>
                <td align="left"></td>
                <td align="right"><s:component template="pagediv"/></td>
            </tr>
        </table>
    </div>
    </div>
    </form>
</body>
</html>
