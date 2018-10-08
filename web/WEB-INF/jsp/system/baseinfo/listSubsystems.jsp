<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<%String s=request.getParameter("isGrant");
  boolean isGrant=true;
  if(s!=null&&s.equals("false")){
      isGrant=false;
  }
%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%@include file="../../common/include.jsp"%>
    <%@include file="../../common/commen-ui.jsp"%>
    
    
	<script type="text/javascript">
		function submitDelete(){
		      var inputs=document.getElementsByName("checkItems");
		      var flag = true;	      
		      for(var i=0;i<inputs.length;i++){
		    	  if(true == inputs[i].checked){
		     	    	flag = false;
		    	  } 
		     	}
		      if(true == flag){
			      alert("请先选择要删除的子系统!");
			      return;
		      }
		      else if(false == confirm("您确定要删除选择的子系统吗?")){
			      return;
		      }
	      document.forms[0].action = "<c:out value='${webapp}'/>/deleteSystem.action";
	      document.forms[0].submit();
		}
		
		/*
		* 查询提交
		*/
		function findOutSubmit() {
			document.frm1.action="<c:out value='${webapp}'/>/listSystem.action?isGrant=<%=isGrant%>";
			document.frm1.method="post";
			document.frm1.submit();
		}
	</script>	
	
</head>

<!-- 2009-07-16 15:38 ShiCH 修改GRID列表加载形式 -->
<body>
    <form name="frm1" method="post" action="<c:out value='${webapp}'/>/listSystem.action" id="frm1">
	<table id="tbl_main" cellpadding="0" cellspacing="0" class="tablewh100">
		<tr>
			<td class="centercondition">
				<div id="tbl_current_status">
					<span><img src="<c:out value="${webapp}"/>/themes/images/icons/icon13.png" /></span>
					<span class="current_status_menu">当前位置：</span>
					<span class="current_status_submenu1">系统管理</span>
					<span class="current_status_submenu">子系统信息管理</span>
					<span class="current_status_submenu"><%if(isGrant){out.print("子系统管理员设置");}else{out.print("子系统信息配置");}%></span>
				</div>
				<div class="widthauto">
					<div id="tbl_query">
						<table>
							<tr>
								 <td align="left" >
								 子系统英文名
								 </td>
								 <td align="left" >
								 <span><input class="tbl_query_text"  name="conditionSystemEname" type="text" value="<s:property value="conditionSystemEname" />" id="conditionSystemEname" /></span>
								 </td>
								 <td align="left" >
								 子系统中文名
								 </td>
								 <td align="left" >
								 <span><input class="tbl_query_text"  name="conditionSystemCname" type="text" value="<s:property value="conditionSystemCname" />" id="conditionSystemCname"  /></span>
								 </td>
				      			<td align="left" ><input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'"  onclick="findOutSubmit()" name="BtnView" value="查询" id="BtnView"/></td>
							</tr>
						</table>
					</div>	
				</div>			
				<table id="tbl_tools" cellpadding="1" cellspacing="1" style="display:none;">
					<tr>
						 <td align="left">&nbsp;			
		            	<div style="display:none" type="btn" img="<c:out value="${webapp}"/>/image/button/add3.gif"  onclick="OpenSubSystemWindow('<c:out value="${webapp}"/>/createSystem.action',true)" name="BtnReturn" value="新增" id="BtnReturn"></div>
		 				<div style="display:none" style="margin-left:5px" type="btn" img="<c:out value="${webapp}"/>/image/button/delete.gif" onclick="submitDelete()" name="BtnReturn" value="删除" id="BtnReturn"></div>          
		            </td>
					</tr>
				</table>
				<div id="lessGridList">
			        <table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
			            <tr class="lessGrid head">
			<!--                <th width="4%" style="display:none"><input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'checkItems')" /></th>-->
			<!--                <th width="5%" style="text-align:center">图标</th>-->
							<th width="3%" style="text-align: center">序号</th>
			                <th width="15%" style="text-align:center">系统英文名</th>
			                <th width="15%" style="text-align:center">系统中文名</th>
			                <%if(!isGrant){
							%>
							<th style="text-align:center">系统外网地址</th>
							<th style="text-align:center">系统内网地址</th>
							<th width="5%" style="text-align:center">启用</th>
							<th width="5%" style="text-align:center">显示</th>
							<th width="5%" style="text-align:center">&nbsp;查看&nbsp;</th>
			                <th width="5%" style="text-align:center">&nbsp;修改&nbsp;</th>
							<%
							}%>
							<th width="10%" style="text-align:center">设置管理员</th>
			            </tr>
			
						<s:iterator value="baseConfigList" status="stuts">
							<tr align="center" class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
			<!--					<td align="center" style="display:none"><input type="checkbox" style="width:13px;height:13px;" name="checkItems" value="<s:property value='systemId' />" /></td>-->
			<!--	                <td align="center"><img src="<c:out value="${webapp}"/>/image/system/<s:property value="menuImgSrc" />" style="border-width: 0px;"/></td>-->
				                <td align="center"><s:property value='#stuts.count' /></td>
				                <td align="left"><s:property value="systemEname" /></td>
				                <td align="left"><s:property value="systemCname" /></td>
				                <%if(!isGrant){
								%>
								<td align="left"><s:property value="linkSiteUrl" /></td>
								<td align="left"><s:property value="linkSiteInnerUrl" /></td>
				                <td align="center"><s:if test='enabled=="true"'>是</s:if><s:else>否</s:else></td>
								<td align="center">
									<s:if test='display=="true"'>全显示</s:if>
									<s:elseif test="display=='menu'">仅菜单显示</s:elseif>
									<s:elseif test="display=='left'">待处理问题显示</s:elseif>
									<s:else>不显示</s:else>
								</td>
								<td align="center"><a href="#" onclick="OpenSubSystemWindow('viewSystem.action?operType=view&subSystem.systemId=<s:property value="systemId" />')"><img src="<c:out value="${webapp}"/>/themes/images/icons/view.png" alt="查看" style="border-width: 0px;"/></a></td>
								<td align="center"><a href="#" onclick="OpenSubSystemWindow('editSystem.action?operType=edit&subSystem.systemId=<s:property value="systemId" />',true)"><img src="<c:out value="${webapp}"/>/themes/images/icons/edit.png" alt="修改" style="border-width: 0px;"/></a></td>
								<%
								}%>
								<td align="center"><s:if test='!systemEname.equals("HOMEPAGE") && !systemId.equals("00003")'><a href="#" onclick="OpenAdministratorWindow('setAdmin.action?subSystem.systemId=<s:property value="systemId" />')" /><img src="<c:out value="${webapp}"/>/themes/images/icons/admin.png" alt="配置管理员" style="border-width: 0px;"/></a></s:if></td>
				            </tr>
			            </s:iterator>  			         
			        </table>
			    </div>
			    <div id="anpBoud" align="Right" style="width:100%;">
			        <table width="100%" cellspacing="0" border="0">
			            <tr>
			                <td align="left"></td>
			                <td align="right"></td>
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