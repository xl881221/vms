<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="../../common/include.jsp"%>
		<%@include file="../../common/commen-ui.jsp"%>
		<script type="text/javascript"
			src="<c:out value="${webapp}"/>/dwr/engine.js"></script>
		<script type="text/javascript"
			src="<c:out value="${webapp}"/>/dwr/util.js"></script>
		<script type="text/javascript"
			src="<c:out value="${webapp}"/>/dwr/interface/dwrAsynService.js"></script>
		<script language="javascript">	
		function beforeMoveUser(o){
	        var inputs = document.getElementsByName('userIdList');
	        if(o.a!=1){
		        var t = "";
				for ( var i = 0; i < inputs.length; i++) {
					if (inputs[i].checked == true) {
						t += inputs[i].value + ",";
					}
				}
				if (t.length == 0) {
					alert("请先选择要移动的用户！");
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
				parent.document.getElementById("frame").cols="300,*";
				}
			else
				parent.document.getElementById("frame").cols="0,*";
		}
		function resetPwd(e, id) {
			dwrAsynService.resetPWD(id, function (f) {
				alert(f);
			});
		}
		function removeLock(e, id) {
			if(dwrAsynService.removeUserLock){
				dwrAsynService.removeUserLock(id, function (f) {
					/*if (f == true) {
						//e.style.display = "none";
						alert("锁定已经解除！");
					} else {alert("解锁失败！");}*/
					alert(f);
					submitForm();
				});
			}
		}
		function lockThisUser(e, id) {
			dwrAsynService.lockThisUser(id, function (f) {
				if (f==0) {alert("锁定成功！");} 
				else if(f==-1) { alert("锁定失败！");} 
				else if(f==-2) { alert("无法锁定，超级管理员才能执行此操作！");}
				submitForm();
			});
		}
		function submitForm(){
			parent.frames['topFrame'].document.forms[0].action = 'listUserMain.action';
			var a = parent.frames['topFrame'].document.forms[0].submit();
			
		}
		function toExcel(){
			parent.frames['topFrame'].document.forms[0].action = 'userToExcel.action?fixQuery='+document.forms[0]["fixQuery"].value;
			if(document.forms[0]["user.instId"]!=undefined){
			   parent.frames['topFrame'].document.forms[0].action+="&instTreeId="+document.forms[0]["user.instId"].value;
			}
			parent.frames['topFrame'].document.forms[0].submit();
			parent.frames['topFrame'].document.forms[0].action = '';
		}
		function kickout(id){
			dwrAsynService.kickoutUser(id, function (f) {
				alert(f);
				submitForm();
			});
		}
		function enabled(able,id){
			dwrAsynService.enabled(able,id,function(f){
				alert(f);
			    submitForm();
			});
		}
		function updateList(able,id){
			dwrAsynService.updateList(able,id,function(f){
				alert(f);
				submitForm();			
			});
		}
	</script>
	</head>
	<body>

		<form name="frm" action="<c:out value='${webapp}'/>/system/authority/listUserMain.action" method="post">
		<s:if test="user.instId != null && user.instId != '' ">
	      <input type="hidden" name="user.instId" id="user.instId" value="<s:property value="user.instId"/>" />
        </s:if>
            <input type="hidden" name="fixQuery"  id="fixQuery" value="<s:property value="fixQuery"/>"/>
        	<input name="user.userEname" id="user.userEname" type="hidden"  value="<s:property value="user.userEname"/>" />
        	<input name="user.userCname" id="user.userCname" type="hidden"  value="<s:property value="user.userCname"/>" />
        	<input name="inst.instSmpName" id="inst.instSmpName" type="hidden"  value="<s:property value="inst.instSmpName"/>" />
			<div class="centercondition1">
			<table id="tbl_button" cellpadding="0" cellspacing="0">
				<tr>
					<td align="left">
						<a href="#" onClick="OpenModalWindow('<c:out value="${webapp}"/>/createUser.action',600,400,true,'add')"><img src="<c:out value="${webapp}"/>/themes/images/icons/icon16.png"/>增加</a>
						<a href="#" onClick="beforeDelete('<c:out value="${webapp}"/>/deleteUser.action', 'userIdList')" name="BtnReturn" id="BtnReturn"><img src="<c:out value="${webapp}"/>/themes/images/icons/icon18.png"/>删除</a>
						<%-- <div style="margin-left: 5px" type="btn"
										img="<c:out value="${webapp}"/>/image/button/view.gif"
										value="按机构查看" onClick="displayTreeInst(true);"
										id="btnViewByInst"></div>--%>
						<a href="#" onClick="beforeMoveUser(this);" name="BtnMove" id="BtnMove"><img src="<c:out value="${webapp}"/>/themes/images/icons/icon19.png"/>移动</a>
						<a href="#" onclick="toExcel();" name="delBtn" id="export" ><img src="<c:out value="${webapp}"/>/themes/images/icons/icon20.png"/>导出</a>
					</td>
				</tr>
			</table>
			<div id="lessGridList7">
				<table class="lessGrid" cellspacing="0" rules="all" border="0"
					cellpadding="0" display="none" style="border-collapse: collapse; width: 100%;">
					<tr class="lessGrid head">
						<th width="4%" style="text-align:center">
							<input id="CheckAll" style="width: 13px; height: 13px;"
								type="checkbox" onClick="checkAll(this,'userIdList')" />
						</th>
						<th width="15%"  style="text-align:center" nowrap>
							用户登录名
						</th>
						<th width="15%"  style="text-align:center" nowrap>
							用户中文名
						</th>
						<th width="15%" style="text-align:center" nowrap>
							机构编号
						</th>
						<th width="15%" style="text-align:center" nowrap>
							机构简称
						</th>
						<th style="text-align:center" nowrap>
							重置密码
						</th>
						<th style="text-align:center" nowrap>
							启用标识
						</th>
						<s:if test="param.selectedValue==1">
							<th style="text-align:center" nowrap>
								锁定
							</th>
							<th style="text-align:center" nowrap>
								解锁
							</th>
							<th style="text-align:center" nowrap>
								用户状态
							</th>
						</s:if>
						<th  style="text-align:center" nowrap>
							修改
						</th>
						<th  style="text-align:center" nowrap>
							配置角色
						</th>
					</tr>
					<s:iterator value="paginationList.recordList" id="iList"
						status="stuts">
						<%
						//	UBaseUserDO user = (UBaseUserDO) iList;
						 %>
						<tr class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
							<td align="center" >
							<input type="checkbox"
									<s:if test="userId==loginUserId">disabled</s:if>
									<s:if test="canAccess==false">disabled</s:if>
									style="width: 13px; height: 13px;" name="userIdList"
									value="<s:property value="userId"/>" />
							</td>
							<td align="center">
								<s:property value="userEname" />
							</td>
							<td align="center">
								<s:property value="userCname" />
							</td>
							<td align="center">
								<s:property value="instId" />
							</td>
							<td align="center">
								<s:property value="instSmpName" />
							</td>
							<td align="center">
								<s:if test="canAccess==true">
								<a href="#"
									onClick="resetPwd(this,'<s:property value="#iList.userId"/>')">
									<img src="<c:out value="${webapp}"/>/themes/images/icons/reset.png"
										alt="重置密码" style="border-width: 0px;" />
								</a>
								</s:if>
							</td>
							<td align="center">
							    <s:if test="canAccess==true">
							    	<s:if test="enabled==0">
								    	<a href="#" onClick="enabled('1','<s:property value="#iList.userId"/>')" >
									    	<img src="<c:out value="${webapp}"/>/themes/images/icons/stop.png"
									    	alt="单击启用" style="border-width: 0px;" />
								    	</a>
							    	</s:if>
							    	<s:else>
								    	<a href="#" onClick="enabled('0','<s:property value="#iList.userId"/>')" >
									    	<img src="<c:out value="${webapp}"/>/themes/images/icons/confirm.png"
									    	alt="单击停用" style="border-width: 0px;" />
								    	</a>
							    	</s:else>
							    </s:if>
							</td>
							<s:if test="param.selectedValue==1">
								<td align="center">
									<s:if test="canAccess==true">
									<a href="#" style="display:<s:if test="isUserLocked==1">none</s:if>"
										onClick="lockThisUser(this,'<s:property value="#iList.userId"/>')">
										<img src="<c:out value="${webapp}"/>/themes/images/icons/unlock.png"
											alt="锁定用户" style="border-width: 0px;" />
									</a>
									</s:if>
								</td>
								
								<td align="center">
									<s:if test="canAccess==true">
									<a href="#" style="display:<s:if test="isUserLocked==null||isUserLocked==0">none</s:if>"
										onClick="removeLock(this,'<s:property value="#iList.userId"/>')">
										<img src="<c:out value="${webapp}"/>/themes/images/icons/unlock.png"
											alt="解除锁定" style="border-width: 0px;" />
									</a>
									</s:if>
								</td>
								<td align="center">
									<s:property value="userStatus" />
								</td>
							</s:if>
							<td align="center">
								<s:if test="canAccess==true">
								<a href="#"
									onClick="OpenModalWindow('<c:out value="${webapp}"/>/editUser.action?user.userId=<s:property value="#iList.userId"/>',600,370,true) ">
									<img src="<c:out value="${webapp}"/>/themes/images/icons/edit.png" alt="修改"
										style="border-width: 0px;" />
								</a>
								</s:if>
							</td>
							<td align="center">
								<s:if test="canAccess==true">
								<a href="#"
									onClick="OpenModalWindow('viewRoleByUser.action?user.userId=<s:property value="#iList.userId"/>',706,550,true)">
									<img src="<c:out value="${webapp}"/>/themes/images/icons/admin.png" alt="配置"
										style="border-width: 0px;" />
								</a>
								</s:if>
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
	</body>

</html>
<script language="javascript">
function OpenModalWindowSubmit(newURL,width,height,needReload,s) {

	    //var node=window.parent.frames["leftFrame"].window.instTree.selectedNode;
	     var checkedTreeNodeId=window.parent.frames["leftFrame"].document.getElementById('checkedTreeNodeId').value;
	    var checkedTreeNodeName=window.parent.frames["leftFrame"].document.getElementById('checkedTreeNodeName').value;
	    //alert(checkedTreeNodeId +"-----" +checkedTreeNodeName);
	    if((checkedTreeNodeId=='' || checkedTreeNodeName=='') &&s=='add'){
	   // if(node==null&&s=='add'){
	        alert("请先选择新增用户所在的机构");
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
		retData = showModalDialog(newURL, 
				  window, 
				  "dialogWidth:" + width
				+ "px;dialogHeight:" + height
				+ "px;center=1;scroll=1;help=0;status=0;");
        
		if(needReload && retData){
			//var a = parent.frames['topFrame'].document.forms[0].submit();
			window.document.forms[0].submit();
		}

}

</script>