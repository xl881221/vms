<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="fmss.dao.entity.LoginDO,fmss.common.util.Constants"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@page import="fmss.dao.entity.LoginDO,fmss.common.cache.CacheManager,org.apache.commons.lang.StringUtils"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<%
	String path = request.getContextPath();
	String webapp = request.getScheme() + "://" + request.getServerName() + ":"+ request.getServerPort() + path;
	String sysTheme = webapp + "/theme/default";
	if(StringUtils.isNotEmpty(CacheManager.getParemerCacheMapValue(fmss.common.util.Constants.PARAM_THEME_PATH_CONFIG))){
		sysTheme = webapp + CacheManager.getParemerCacheMapValue(fmss.common.util.Constants.PARAM_THEME_PATH_CONFIG);
	}
	pageContext.setAttribute("webapp", webapp);
	pageContext.setAttribute("sysTheme", sysTheme);
	response.setHeader("Pragma","No-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setDateHeader("Expires",0);
%>
<script language="javascript" type="text/javascript" src="<c:out value='${webapp}'/>/js/main.js" charset="GBK"></script>
<link href="<c:out value="${sysTheme}"/>/css/main.css" rel="stylesheet" type="text/css" />
<%@include file="../../common/commen-ui.jsp"%>
<script type="text/javascript" src="<c:out value="${webapp}"/>/js/SimpleTree/js/ajax.js"></script>
<script language="javascript" type="text/javascript" src="<c:out value='${webapp}'/>/js/window.js" charset="UTF-8"></script>
<script language="javascript" type="text/javascript" src="<c:out value='${webapp}'/>/js/validator.js" charset="GBK"></script>
<script language="javascript" type="text/javascript" charset="UTF-8">
	<%
	if(request.getParameter("RESULT_MESSAGE") !=null && !"".equals(request.getParameter("RESULT_MESSAGE"))){
	%>
		alert("<%=java.net.URLDecoder.decode(request.getParameter("RESULT_MESSAGE"),"utf-8")%>");
	<%
	}else if(request.getAttribute("RESULT_MESSAGE") !=null && !"".equals(request.getAttribute("RESULT_MESSAGE"))){	
	%>
	    alert("<%=java.net.URLDecoder.decode(request.getAttribute("RESULT_MESSAGE").toString(),"utf-8")%>");
	<% 
	}
	%>
</script>
	<script language="javascript" type="text/javascript">
	    
	    function beforeMoveInst(o){
	        var inputs = document.getElementsByName('ids');
	        if(o.a!=1){
		        var t = "";
				for ( var i = 0; i < inputs.length; i++) {
					if (inputs[i].checked == true) {
						t += inputs[i].value + ",";
					}
				}
				if (t.length == 0) {
					alert("请先选择要移动的机构！");
					return;
				}
				for ( var i = 0; i < inputs.length; i++) {
					inputs[i].disabled="disabled";
				}
				document.getElementById('CheckAll').disabled="disabled";
				o.innerHTML=o.innerHTML.replace("移动","取消")
				o.a=1;
				document.getElementById("moveMsg").innerHTML="请选择目的机构，点击'取消'可以取消移动"
	        }else{
	            for ( var i = 0; i < inputs.length; i++) {
					inputs[i].disabled="";
				}
				document.getElementById('CheckAll').disabled="";
				o.innerHTML=o.innerHTML.replace("取消","移动")
				o.a=0;
				document.getElementById("moveMsg").innerHTML=""
	        }
	    }
	    
	   	function displayTreeInst(obj){
			if(obj==true){
				parent.frame.cols="300,*";
				}
			else
				parent.frame.cols="0,*";
		}
	   /*
		* 查询提交定位
		*/
		function findOutSubmit()
		{
			displayTreeInst(true);
			document.frm.action="<c:out value='${webapp}'/>/listInstMain.action";
			document.frm.method="post";
			document.frm.submit();
		}
	</script>
</head>
<body>
    <form name="frm" id="frm" action="<c:out value='${webapp}'/>/system/authority/listInstMain.action" method="post">

        <input type="hidden" name="fixQuery" value="<s:property value="fixQuery"/>"/>
        <s:if test="inst.instId != null && inst.instId != '' ">
	      <input type="hidden" name="inst.instId" id="inst.instId" value="<s:property value="inst.instId"/>" />
        </s:if>
	 	<input type="hidden" name="inst.instName" value="<s:property value="inst.instName"/>"/>
	 	<%
	 		LoginDO login = (LoginDO)request.getSession().getAttribute(Constants.LOGIN_USER);
			String instIsHead = login.getInstIsHead();
			String userId = login.getUserId();
		%>
		<div class="centercondition1">
		<table id="tbl_button" cellpadding="0" cellspacing="0">
			<tr align="left">
				<td align="left">
				 <%if(instIsHead.equals("true")||userId.equals("admin")) { %>				
				 <a href="#" onClick="OpenModalWindowSubmit('<c:out value="${webapp}"/>/createInst.action?editType=add',600,500,true,'root')"><img src="<c:out value="${webapp}"/>/themes/images/icons/icon15.png"/>新增根机构</a>
				 <% 
				 } 
				 %>
				 <a href="#" onClick="OpenModalWindowSubmit('<c:out value="${webapp}"/>/createInst.action?editType=add',600,500,true,'add')"><img src="<c:out value="${webapp}"/>/themes/images/icons/icon16.png"/>新增</a>
				 <a href="#" onClick="document.forms[0].action = 'initInst.action';document.forms[0].submit();" name="BtnReturn" id="BtnReturn"><img src="<c:out value="${webapp}"/>/themes/images/icons/icon17.png"/>初始化</a>
				 <a href="#" onClick="beforeDeleteInst('<c:out value="${webapp}"/>/deleteInst.action');" name="BtnReturn" id="BtnReturn"><img src="<c:out value="${webapp}"/>/themes/images/icons/icon18.png"/>删除</a>
				 <a href="#" onClick="beforeMoveInst(this);" name="BtnMove" id="BtnMove"><img src="<c:out value="${webapp}"/>/themes/images/icons/icon18.png"/>移动</a>
<!--	             <input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" value="移动" onClick="" name="BtnMove" id="BtnMove"/> -->
	             </td>
        			<%-- <div style="margin-left:5px" type="btn" img="<c:out value="${webapp}"/>/image/button/view.gif" value="机构层级查看" onClick="displayTreeInst(true);" id="btnViewByInst"></div>	--%>		 
            </tr>
		</table>
	 	<div id="lessGridList">
	       <table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" display="none" style="border-collapse: collapse;width: 100%;">
	           <tr class="lessGrid head">
					<th width="3%" style="text-align:center"><input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'ids')" /></th>
					<th width="20%"  style="text-align:center" nowrap>机构编号</th>
					<th width="20%"  style="text-align:center" nowrap>机构名称</th>
					<th  style="text-align:center">是否业务机构</th>
					<!--  <th  style="text-align:center">启用日期</th>
					<th  style="text-align:center">终止日期</th>-->
					<th  style="text-align:center">启用标识</th>
					<th width="10%"  style="text-align:center" nowrap>查看</th>
					<th width="10%"  style="text-align:center" nowrap>修改</th>
	           </tr>
	           <s:iterator value="paginationList.recordList" id="iList" status="stuts">
	           		<tr align="center" class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>" >
						<td><input type="checkbox" style="width:13px;height:13px;" name="ids" value="<s:property value="#iList.instId"/>" /></td>
						<td><s:property value="#iList.instId"/></td>
						<td><s:property value="#iList.instName"/></td>
						<td><s:property value="#iList.isBussinessCH"/></td>
						<!--<td><s:property value="#iList.startDateStr"/></td>
						<td><s:property value="#iList.endDateStr"/></td>-->
						<td><s:property value="#iList.enabledCH"/></td>
						<td align="center">
							<a href="javascript:void(0)" onClick="OpenModalWindow('<c:out value="${webapp}"/>/viewInst.action?inst.instId=<s:property value="#iList.instId"/>',
							600,460,true) ">
							<img src="<c:out value="${webapp}"/>/themes/images/icons/view.png" alt="查看" style="border-width: 0px;"/></a>
						</td>
						<td align="center">
							<a href="javascript:void(0)" onClick="OpenModalWindowSubmit('<c:out value="${webapp}"/>/editInst.action?inst.instId=<s:property value="#iList.instId"/>',
							600,500,true) ">
							<img src="<c:out value="${webapp}"/>/themes/images/icons/edit.png" alt="修改" style="border-width: 0px;"/></a>
						</td>
					</tr>
	           </s:iterator>
	       </table>
	    </div>
	    <div id="anpBoud" align="Right" style="width:100%;vlign=top;">
	        <table width="100%" cellspacing="0" border="0">
	            <tr>
	                <td align="left"></td>
	                <td align="right"><s:component template="pagediv"/></td>
	            </tr>
	        </table>
	        </div>
	    </div>
    </form>
    <form id="reflsher" action="listInst.action" target="mainFramePage" method="post"></form>
</body>
<script type="text/javascript">
	document.getElementById("lessGridList").style.height = screen.availHeight - 340;
</script>
<script language="javascript" type="text/javascript">
function beforeDeleteInst(actionName) {
	var t = "";
	var inputs = document.getElementsByName('ids');
	for ( var i = 0; i < inputs.length; i++) {
		if (inputs[i].checked == true) {
			t += inputs[i].value + ",";
		}
	}
	if (t.length == 0) {
		alert("请先选择要删除的机构！");
		return;
	}
    document.forms[0].action = actionName;
	document.forms[0].submit();
}

function OpenModalWindowSubmit(newURL,width,height,needReload,s) {
	try {
	    //var node=window.parent.frames["leftFrame"].window.instTree.selectedNode;
	    var checkedTreeNodeId=window.parent.frames["leftFrame"].document.getElementById('checkedTreeNodeId').value;
	    var checkedTreeNodeName=window.parent.frames["leftFrame"].document.getElementById('checkedTreeNodeName').value;
	    //alert(checkedTreeNodeId +"-----" +checkedTreeNodeName);
	    if((checkedTreeNodeId=='' || checkedTreeNodeName=='') &&s=='add'){
	        alert("请先选择新增机构的上级机构");
	        return;
	   }
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
		if(s=='root'){
		   newURL+='&isRoot=true'
		}
		retData = showModalDialog(newURL, 
				  window, 
				  "dialogWidth:" + width
				+ "px;dialogHeight:" + height
				+ "px;center=1;scroll=1;help=0;status=0;");

		if(needReload && retData){
			window.document.forms[0].submit();
		}
	} catch (err) {
	alert(err)
	}
}
 </script>
</html>