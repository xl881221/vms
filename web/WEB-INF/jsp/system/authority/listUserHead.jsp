<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%@include file="../../common/include.jsp"%>
    <%@include file="../../common/commen-ui.jsp"%>
	<script language="javascript">		
		function displayTreeInst(obj){
			if(obj==true){
				parent.document.getElementById("frame").cols="300,*";
				}
			else
				parent.document.getElementById("frame").cols="0,*";
				
		}
		
		/*
		* 查询提交定位
		*/
		function findOutSubmit()
		{
			displayTreeInst(true);
			document.frm.action="<c:out value='${webapp}'/>/listUserMain.action";
			document.frm.method="post";
			document.frm.submit();
			
		}
	</script>
</head>
<body onload="findOutSubmit()">
    <form name="frm" id="frm" action="<c:out value='${webapp}'/>/listUserMain.action" method="post" target="userSearchResultFrame">
		<table id="tbl_main" cellpadding="0" cellspacing="0">
			<tr>
				<td align="left">
					<div id="tbl_current_status">
						<span><img src="<c:out value="${webapp}"/>/themes/images/icons/icon13.png" /></span>
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">系统管理</span>
						<span class="current_status_submenu">基础信息管理</span>
						<span class="current_status_submenu">用户管理</span>
					</div>
				</td>
			</tr>
	    	<tr>
				<td align="center" class="plr12">
					<div id="tbl_query">
						<table>
							<tr>
								<td align="left" >
								用户登录名
								 </td>
								 <td align="left" >
								 <span><input class="tbl_query_text"  name="user.userEname" id="user.userEname" type="text" /></span>
								 </td>
								 <td align="left" >
								 用户中文名
								 </td>
								 <td align="left" >
								 <span><input class="tbl_query_text"  name="user.userCname" id="user.userCname" type="text"  /></span>
								 </td>
								 <td align="left" >
								 机构编号
								 </td>
								 <td align="left" >
								 <span><input class="tbl_query_text"  name="user.instId" type="text" id="user.instId" /></span>
								 </td>
								 <td align="left" >
								 机构简称
								 </td>
								 <td align="left" >
								 <span><input class="tbl_query_text"  name="inst.instSmpName" id="inst.instSmpName" type="text" /></span>
								 </td>
				      			<td align="left" ><input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="findOutSubmit()" name="BtnView" value="查询" id="BtnView"/></td>
							</tr>
						</table>
					</div>	
				</td>
			</tr>
		</table>
    </form>
</body>
</html>


