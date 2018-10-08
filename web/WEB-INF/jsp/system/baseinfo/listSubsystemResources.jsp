<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%@include file="../../common/include.jsp"%>
    <%@include file="../../common/commen-ui.jsp"%>
	<script type="text/javascript">
		function createAuthResMap(){
			var date = new Date();
			var url = "createResource.action?"+date.getTime();
			OpenModalWindow(url,600,400,true);
		}
		function deleteAuthResMap(){
			 var inputs=document.getElementsByName("resIDs");
		      var flag = true;	      
		      for(var i=0;i<inputs.length;i++){
		    	  if(true == inputs[i].checked){
		     	    	flag = false;
		    	  } 
		     	}
		      if(true == flag){
			      alert("请先选择要删除的记录!");
			      return;
		      }
		      else if(confirm("您确定要删除选择的记录信息吗?")){
			      document.forms[0].action = "<c:out value='${webapp}'/>/deleteResource.action";
	      		  document.forms[0].submit();
		      }
		}
		function edit(id){
			var date = new Date();
			var url = "editResource.action?"+date.getTime()+"&uAuthResMap.resId="+id;
			OpenModalWindow(url,600,392,true);
		}
		function view(id,systemId){
			var date = new Date();
			var url = "viewResourceContent.action?uAuthResMap.resId="+id+"&uBaseConfig.systemId="+systemId+"&"+date.getTime();
			OpenModalWindow(url,600,510,true);
		}
		/*
		* 查询提交
		*/
		function findOutSubmit() {
			document.frm1.action="listResource.action";
			document.frm1.method="post";
			document.frm1.submit();
		}
	</script>
</head>
<body>
    <form name="frm1" method="post" action="listResource.action" id="frm1">
<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
	<tr>
		<td class="centercondition">
			<div id="tbl_current_status">
				<span><img src="<c:out value="${webapp}"/>/themes/images/icons/icon13.png" /></span>
				<span class="current_status_menu">当前位置：</span>
				<span class="current_status_submenu1">系统管理</span>
				<span class="current_status_submenu">子系统信息管理</span>
				<span class="current_status_submenu">子系统资源配置</span>
			</div>
			<div class="widthauto">
				<div id="tbl_query">
					<table>
						<tr>
							 <td align="left" >
							 系统选择
							 </td>
							 <td align="left" >
							 <span><s:select list="subSystemList" name="uAuthResMap.systemId" listKey="systemId" listValue="systemCname" theme="simple"/></span>
							 </td>
							 <td align="left" >
							 资源类型
							 </td>
							 <td align="left" >
							 <span><s:select list="resTypeDic" name="uAuthResMap.resType" listKey="dicValue" listValue="dicName" value="uAuthResMap.resType" theme="simple"/></span>
							 </td>
							 <td align="left" >
							 资源名称
							 </td>
							 <td align="left" >
							 <span><input class="tbl_query_text" name="uAuthResMap.resName" type="text" value="<s:property value="uAuthResMap.resName"/>"/></span>
							 </td>
			      			<td align="left" ><input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="findOutSubmit()" name="BtnView" value="查询" id="BtnView" /></td>
						</tr>
					</table>
				</div>
			</div>
			<table style="display:none;">
				<tr>
					<td align="center">
					<table id="tbl_tools" cellpadding="1" cellspacing="1">
						<tr>
							<td align="left">&nbsp;
				            	<div type="btn" img="<c:out value="${webapp}"/>/image/button/add3.gif" onclick="createAuthResMap()" name="BtnReturn" value="新增" id="BtnReturn"></div>
				            	<div style="margin-left:5px" type="btn" img="<c:out value="${webapp}"/>/image/button/delete.gif" onclick="deleteAuthResMap('<s:property value="resId" />')" name="BtnReturn" value="删除" id="BtnReturn"></div>
			            	</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			 <div id="lessGridList">
		        <table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
		            <tr class="lessGrid head" >
		<!--            <th width="3%" style="display:none;"><input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="cbxselectall(this,'resIDs')" /></th>-->
		                <th width="3%" style="text-align: center">序号</th>
		                <th style="text-align:center">系统名称</th>
		                <th style="text-align:center">资源类型</th>
		                <th style="text-align:center">资源名称</th>
		                <th style="text-align:center">资源来源表</th>
						<th style="text-align:center">资源ID来源字段</th>
						<th style="text-align:center">资源名称来源字段</th>
						<th style="text-align:center">资源内容查看</th>
						<th style="display:none;">修改</th>
		            </tr>
					<s:iterator value="authResMapList" status="stuts">
						<tr align="center" class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
		<!--					<td style="display:none;"><input type="checkbox" style="width:13px;height:13px;" name="resIDs" value="<s:property value='resId' />" /></td>-->
			                <td align="center"><s:property value='#stuts.count' /></td>
			                <td><s:property value="systemCname" /></td>
			                <td><s:property value="resType" /></td>
			                <td><s:property value="resName" /></td>
							<td align="center"><s:property value="srcTable" /></td>
			                <td><s:property value="srcIdField" /></td>
							<td><s:property value="srcNameField" /></td>
							<td><a href="#" onclick="view('<s:property value="resId" />','<s:property value="systemId" />')"><img src="<c:out value="${webapp}"/>/themes/images/icons/view.png" alt="查看" style="border-width: 0px;"/></a></td>
							<td style="display:none;"><a href="#" onclick="edit('<s:property value="resId" />')"><img src="<c:out value="${webapp}"/>/themes/images/icons/edit.png" alt="修改" style="border-width: 0px;"/></a></td>
						</tr>
		            </s:iterator>                  		          
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
        
        
		</td>
	</tr>
</table>
  </form>
</body>
<script type="text/javascript">
   	document.getElementById("lessGridList").style.height = screen.availHeight - 280;
</script>
</html>
