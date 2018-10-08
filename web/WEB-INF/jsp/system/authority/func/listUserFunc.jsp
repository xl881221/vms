<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="fmss.common.config.PrivateConfig"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
   		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title></title>
		<%@include file="../../../common/include.jsp"%>
</head>
<body>
    <form name="frm1" method="post" action="" id="frm1">
<table id="tbl_main" cellpadding="0" cellspacing="0">
	<tr>
		<td align="left">
		<table id="tbl_current_status">
			<tr>
				<td>
					<img src="<c:out value="${sysTheme}"/>/img/jes/icon/home.png" />
					<span class="current_status_menu">当前位置：</span>
					<span class="current_status_submenu">基础信息管理<span class="actionIcon">-&gt;</span>权限功能配置</span>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>


 		<div class="tab_page">
				<div class="tabs" onselectstart="return false">
					<table cellpadding="0" cellspacing="0" style="display: inline">
						<tr>
							<td>
								<div id="firstTab" class="selct" onmouseover="tabChange(this)"
									onmouseout="tabChange(this)"
									onclick="if(!canMetaConfig)showTip();else window.location='list00003Menus.action';">
									用户权限配置
								</div>
							</td>
							<td>
								<div class="normal" onmouseover="tabChange(this)"
									onmouseout="tabChange(this)"
									onclick="clickTab(this,'listRoleFunc.action')" style="display: none">
									角色功能配置
								</div>
							</td>
							<td align="right">
									
							</td>
						</tr>
					</table>
				</div>
        </div>
        <div id="tab_content_inst" class="tab_body" style='display: '>
					<div class="body">
						<div style="overflow: auto; width: 100%; height: 100%;"
							id="div_content_inst">
							<table class="lessGrid" cellspacing="0" rules="all" border="0"
								cellpadding="0" display="none"
								style="border-collapse: collapse;">
								<tr class="lessGrid head">
									<th style="text-align: center">
										用户登录名
									</th>
									<th style="text-align: center">
										用户中文名
									</th>
									<th style="text-align: center">
										机构编号
									</th>
									<th style="text-align: center">
										查看
									</th>
									<th style="text-align: center">
										配置
									</th>
								</tr>
								<s:iterator value="paginationList.recordList" status="stuts">
									<tr align="center"
										class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>"
										<s:if test="currentUser.userId == changeUser">style="color: gray;"</s:if>>
										<td>
											<s:property value="userId" />
										</td>
										<td>
											<s:property value="userCname" />
										</td>
										<td>
											<s:property value="instId" />
										</td>
										<td align="center">
	                		<a href="#" onclick="OpenModalWindow('viewMenuRes.action?userId=<s:property value="userId" />',400,500);">
							<img src="<c:out value="${sysTheme}"/>/img/jes/icon/view.png" alt="" style="border-width: 0px;"/></a>
								</td>
								<td align="center">
	                		<a href="#" onclick="OpenModalWindow('viewMenuRes.action?userId=<s:property value="userId" />&config=true',400,500);">
							<img src="<c:out value="${sysTheme}"/>/img/jes/icon/edit.png" alt="" style="border-width: 0px;"/></a>
								</td>
									</tr>
								</s:iterator>
							</table>
						</div>
					</div>
				</div>
        
	    <div id="anpBoud" align="Right" style="width:100%;vlign=top;">
	        <table width="100%" cellspacing="0" border="0">
	            <tr>
	                <td align="left"></td>
	                <td align="right"><s:component template="pagediv"/></td>
	            </tr>
	        </table>
	    </div>
    


  </form>
</body>
</html>
<script language="javascript">
	function query(){
		document.forms[0].action = "listRoleFunc.action";
		document.forms[0].submit();
	}
		function tabChange(e) {
				if (e.className == "selct") return;
				if (e.className == "on") e.className = "normal";
				else e.className = "on";
			} 
			
			function clickTab(e,url) {
				if (!window._CURR_C) 
					window._CURR_C = document.getElementById("firstTab");
				window._CURR_C.className = "normal";
				window._CURR_C = e;
				window._CURR_C.className = "selct";
				//切换tab页
				window.location = url;
				var divs = document.getElementsByTagName("div");
				for(i=0;i<divs.length;i++){
					if(divs[i].id!=null&&divs[i].id!=''&&divs[i].id.indexOf('tab_content_')>-1){
						if(divId!=divs[i].id){
							document.getElementById(divs[i].id).style.display="none";
						}
					}
				}
				//置选中标志
				//document.forms[0].selectTab.value=divId.replace('tab_content_','');
			}
			
			window.onload = function () {
				//showTip();
			}
			function showTip(){
				var m = new MessageBox(document.getElementById("firstTab"));
			    m.Show("提示:只有子系统管理员才可以配置功能权限");
			}
			
			var canMetaConfig = <%=PrivateConfig.isCanMetaConfig()%>;
</script>