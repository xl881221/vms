<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@include file="../../../common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
	<head>
		<title></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <%@include file="../../../common/include.jsp"%>
		<style type="text/css">
.detailInfoDIV {
	border: 1px solid green;
	background-color: khaki;
	top: 110px;
	left: 150px;
	width: 450px;
	height: 300px;
	position: absolute;
	z-index: 2;
	filter: alpha(opacity =                                     90);
	-moz-opacity: 0.9;
	display: none;
}
</style>

		<script type="text/javascript">
			function tabChange(e) {
				if (e.className == "selct") return;
				if (e.className == "on") e.className = "normal";
				else e.className = "on";
			} 
			
			function clickTab(e,divId) {
				if (!window._CURR_C) 
					window._CURR_C = document.getElementById("firstTab");
				window._CURR_C.className = "normal";
				window._CURR_C = e;
				window._CURR_C.className = "selct";
				//切换tab页
				document.getElementById(divId).style.display="";
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
		if(/msie/gi.test(navigator.userAgent))
			try{document.execCommand("BackgroundImageCache", false, true);}catch(e){};
			
			window.onload = function () {
				autosize();
			}
			
			function autosize(){
				height = maxHeight($('tab_content_inst'),$('tab_content_menu'),$('tab_content_report'),$('tab_content_user'))+100;
			    width = maxWidth($('div_content_inst'),$('div_content_menu'),$('div_content_report'),$('div_content_user'))+30;
			    
			    $('tab_content_menu').style.display="none";
			    $('tab_content_report').style.display="none";
			    $('tab_content_user').style.display="none";
			    if(height > 400)
			    	height = 400;
				window.dialogHeight = height + "px"
			    window.dialogWidth  = width + "px"
			    window.dialogLeft = ((window.screen.availWidth - document.body.clientWidth) / 2).toString() + "px"     
			    window.dialogTop = ((window.screen.availHeight - document.body.clientHeight) / 2).toString() + "px"
			    
			}
			
			function maxHeight(a,b,c,d){
				return max(max(a.scrollHeight,b.scrollHeight),max(c.scrollHeight,d.scrollHeight));
			}
			function maxWidth(a,b,c,d){
				return max(max(a.scrollWidth,b.scrollWidth),max(c.scrollWidth,d.scrollWidth));
			}
			function max(a,b){
				return a > b ? a : b;
			}
			$ = function(id){
				return document.getElementById(id);
			}
	</script>
	</head>
	<body>
		<form name="frm1" method="post" action="" id="frm1">
		<table id="tbl_current_status">
			<tr>
				<td>
					<img src="<c:out value="${sysTheme}"/>/img/jes/icon/home.png" />
					<span class="current_status_submenu">角色资源修改</span>
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
									onclick="clickTab(this,'tab_content_inst')">
									机构资源
								</div>
							</td>
							<td>
								<div class="normal" onmouseover="tabChange(this)"
									onmouseout="tabChange(this)"
									onclick="clickTab(this,'tab_content_menu')">
									菜单资源
								</div>
							</td>
							<td>
								<div class="normal" onmouseover="tabChange(this)"
									onmouseout="tabChange(this)"
									onclick="clickTab(this,'tab_content_report')">
									其他资源
								</div>
							</td>
							<td>
								<div class="normal" onmouseover="tabChange(this)"
									onmouseout="tabChange(this)"
									onclick="clickTab(this,'tab_content_user')">
									所属用户
								</div>
							</td>
						</tr>
					</table>
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
										资源名
									</th>
									<th style="text-align: center">
										资源值
									</th>
									<th style="text-align: center">
										提交状态
									</th>
									<th style="text-align: center">
										提交人
									</th>
									<th style="text-align: center">
										提交时间
									</th>
									<s:if test="showAuditColumn">
									<th style="text-align:center">审核状态</th>
                					<th style="text-align:center">审核人</th>
               				 		<th style="text-align:center">审核时间</th></s:if>
								</tr>
								<s:iterator value="resourceInstChanges" status="stuts">
									<tr align="center"
										class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>"
										<s:if test="currentUser.userId == changeUser">style="font:bold"</s:if>>
										<td>
											<s:property value="resDetailName" />
										</td>
										<td>
											<s:property value="resDetailValue" />
										</td>
										<td>
											<s:property value="ownShowStatus" />
										</td>
										<td>
											<s:property value="changeUser" />
										</td>
										<td>
											<s:date name="changeTime" format="yyyy-MM-dd" />
										</td>
						<s:if test="showAuditColumn">
						<td><s:property value="auditInfo" /></td>
                		<td><s:property value="auditUser" /></td>
                		<td><s:date name="auditTime" format="yyyy-MM-dd"/> </td></s:if>
									</tr>
								</s:iterator>
							</table>
						</div>
					</div>
				</div>
				<div id="tab_content_menu" class="tab_body" style='display: '>
					<div class="body">
						<div style="overflow: auto; width: 100%; height: 100%;"
							id="div_content_menu">
							<table class="lessGrid" cellspacing="0" rules="all" border="0"
								cellpadding="0" display=""
								style="border-collapse: collapse;">
								<tr class="lessGrid head">
									<th style="text-align: center">
										资源名
									</th>
									<th style="text-align: center">
										资源值
									</th>
									<th style="text-align: center">
										提交状态
									</th>
									<th style="text-align: center">
										提交人
									</th>
									<th style="text-align: center">
										提交时间
									</th>
									<s:if test="showAuditColumn">
									<th style="text-align:center">审核状态</th>
                					<th style="text-align:center">审核人</th>
               				 		<th style="text-align:center">审核时间</th></s:if>
								</tr>
								<s:iterator value="resourceMenuChanges" status="stuts"
									var="info">
									<tr align="center"
										class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>" <s:if test="currentUser.userId == changeUser">style="font: bold"</s:if>>
										<td>
											<s:property value="resDetailName" />
										</td>
										<td >
											<s:property value="resDetailValue" />
										</td>
										<td>
											<s:property value="ownShowStatus" />
										</td>
										<td>
											<s:property value="changeUser" />
										</td>
										<td>
											<s:date name="changeTime" format="yyyy-MM-dd" />
										</td>
										<s:if test="showAuditColumn">
										<td><s:property value="auditInfo" /></td>
                		<td><s:property value="auditUser" /></td>
                		<td><s:date name="auditTime" format="yyyy-MM-dd"/> </td></s:if>
									</tr>
								</s:iterator>
							</table>
						</div>
					</div>
				</div>
				<div id="tab_content_report" class="tab_body" style='display: '>
					<div class="body">
						<div style="overflow: auto; width: 100%; height: 100%;"
							id="div_content_report">
							<table class="lessGrid" cellspacing="0" rules="all" border="0"
								cellpadding="0" display=""
								style="border-collapse: collapse;">
								<tr class="lessGrid head">
									<th style="text-align: center">
										资源名
									</th>
									<th style="text-align: center">
										资源值
									</th>
									<th style="text-align: center">
										提交状态
									</th>
									<th style="text-align: center">
										提交人
									</th>
									<th style="text-align: center">
										提交时间
									</th>
									<s:if test="showAuditColumn">
									<th style="text-align:center">审核状态</th>
                					<th style="text-align:center">审核人</th>
               				 		<th style="text-align:center">审核时间</th></s:if>
								</tr>
								<s:iterator value="resourceReportChanges" status="stuts"
									var="info">
									<tr align="center"
										class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>" <s:if test="currentUser.userId == changeUser">style="font: bold"</s:if>>
										<td>
											<s:property value="resDetailName" />
										</td>
										<td>
											<s:property value="resDetailValue" />
										</td>
										<td>
											<s:property value="ownShowStatus" />
										</td>
										<td>
											<s:property value="changeUser" />
										</td>
										<td>
											<s:date name="changeTime" format="yyyy-MM-dd" />
										</td>
										<s:if test="showAuditColumn">
										<td><s:property value="auditInfo" /></td>
                		<td><s:property value="auditUser" /></td>
                		<td><s:date name="auditTime" format="yyyy-MM-dd"/> </td></s:if>
									</tr>
								</s:iterator>
							</table>
						</div>
					</div>
				</div>
				<div id="tab_content_user" class="tab_body" style='display: '>
					<div class="body">
						<div style="overflow: auto; width: 100%; height: 100%;"
							id="div_content_user">
							<table class="lessGrid" cellspacing="0" rules="all" border="0"
								cellpadding="0" display=""
								style="border-collapse: collapse;">
								<tr class="lessGrid head">
									<th style="text-align: center">
										用户id
									</th>
									<th style="text-align: center">
										用户名
									</th>
									<th style="text-align: center">
										机构号
									</th>
									<th style="text-align: center">
										机构名
									</th>
								</tr>
								<s:iterator value="roleBelongToUsers" status="stuts" var="info">
									<tr align="center"
										class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
										<td>
											<s:property value="userId" />
										</td>
										<td>
											<s:property value="userCname" />
										</td>
										<td>
											<s:property value="instId" />
										</td>
										<td>
											<s:property value="ubaseInst.instName" />
										</td>
									</tr>
								</s:iterator>
							</table>
						</div>
					</div>
				</div>
			</div>
<div id="ctrlbutton" class="ctrlbutton" style="border: 0px">
				<div
					style="float: right; margin-right: 10px; height: 25px; margin-top: 5px"
					type="btn" img="<c:out value="${webapp}"/>/image/button/cancel.gif"
					onClick="window.close()" name="btnReturn" value="关闭" id="btnReturn"></div>
			</div>

		</form>
	</body>
</html>
