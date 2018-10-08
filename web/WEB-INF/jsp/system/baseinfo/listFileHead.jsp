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
			document.frm.action="<c:out value='${webapp}'/>/listFileMain.action";
			document.frm.method="post";
			document.frm.submit();
			
		}
	</script>
</head>
<body onload="displayTreeInst(true)">
    <form name="frm" id="frm" action="<c:out value='${webapp}'/>/listFileMain.action" method="post" target="fileSearchResultFrame">
		<table id="tbl_main" cellpadding="0" cellspacing="0">
			<tr>
				<td align="left">
					<div id="tbl_current_status">
						<span><img src="<c:out value="${webapp}"/>/themes/images/icons/icon13.png" /></span>
						<span class="current_status_menu">当前位置：</span>
						<span class="current_status_submenu1">系统管理</span>
						<span class="current_status_submenu">系统参数管理</span>
						<span class="current_status_submenu">文件存储</span>
					</div>
				</td>
			</tr>
	    	<tr>
				<td align="center" class="plr12">
					<div id="tbl_query">
						<table>
							<tr>
								<td align="left" >
								文件名
								 </td>
								 <td align="left" >
								 <span><input class="tbl_query_text"  name="baseFile.fileName" id="baseFile.fileName" type="text" /></span>
								 </td>
								 <td align="left" >
								 类别编号
								 </td>
								 <td align="left" >
								 <span><input class="tbl_query_text"  name="baseFolder.folderCode" id="baseFolder.folderCode" type="text"  /></span>
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


