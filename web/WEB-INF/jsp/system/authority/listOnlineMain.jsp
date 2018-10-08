<%@ page language="java" contentType="text/html; charset=UTF-8" 
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


function submitForm(){
	document.forms[0].submit();
}

function kickout(){
    var ids="";
    var array=document.getElementsByName("userIdList");
    for(var i=0;i<array.length;i++){
        if(array[i].checked){
           ids+=","+array[i].value;
        }
    }
    if(ids==""){
       alert("请选择要注销的用户");
       return;
    }
	dwrAsynService.kickoutUser(ids, function (f) {
		alert(f);
		submitForm();
	});
}
function deleteLogin(){
    var ids="";
    var array=document.getElementsByName("userIdList");
    for(var i=0;i<array.length;i++){
        if(array[i].checked){
           ids+=","+array[i].value;
        }
    }
    if(ids==""){
       alert("请选择要删除的用户");
       return;
    }
	dwrAsynService.deleteLogin(ids, function (f) {
		alert(f);
		submitForm();
	});
}
</script>
</head>
<body>
<form name="frm" action="<c:out value='${webapp}'/>/system/authority/listOnlineMain.action" method="post">
	<s:if test="user.instId != null && user.instId != '' ">
	    <input type="hidden" name="user.instId" id="user.instId" value="<s:property value="user.instId"/>" />
	</s:if>
    <input type="hidden" name="fixQuery" value="<s:property value="fixQuery"/>"/>
    <input name="user.userEname" id="user.userEname" type="hidden"  value="<s:property value="user.userEname"/>" />
    <input name="user.userCname" id="user.userCname" type="hidden"  value="<s:property value="user.userCname"/>" />
	<div class="centercondition1">
	<table id="tbl_button" cellpadding="0" cellspacing="0">
		<tr align="left">
			<td align="left">
			 	<%

				String PARAM_KICKOUT=(String)request.getAttribute("PARAM_KICKOUT");
				if(PARAM_KICKOUT.equals("1")){
				%>
				<div type="btn"
					img="<c:out value="${webapp}"/>/image/button/all_not_pass.gif"
					value="注销"
					onClick="kickout()"></div>
					
				<div style="margin-left: 5px" type="btn"
					img="<c:out value="${webapp}"/>/image/button/delete.gif"
					value="删除"
					onClick="deleteLogin()"></div>
				<%
				}
				%>
				<a href="#" onclick="submitForm()" name="BtnReturn" id="BtnReturn"><img src="<c:out value="${webapp}"/>/themes/images/icons/icon21.png"/>刷新</a>	 
           </tr>
	</table>
	<div id="lessGridList">
		<table class="lessGrid" width="100%" cellspacing="0" rules="all" border="0"
			cellpadding="0" display="none" style="border-collapse: collapse;">
			<tr class="lessGrid head" style="text-align:center">
				<th width="4%" style="text-align:center">
					<input id="CheckAll" style="width: 13px; height: 13px;"
						type="checkbox" onClick="checkAll(this,'userIdList')" />
				</th>
				<th style="text-align:center">
					登录标识
				</th>
				<th style="text-align:center">
					用户登陆名
				</th>
				<th style="text-align:center">
					用户名
				</th>
				<th style="text-align:center">
					机构
				</th>
				<th style="text-align:center">
					登陆时间
				</th>
				<th style="text-align:center">
					状态
				</th>
				<th style="text-align:center">
					注销时间
				</th>
				<th style="text-align:center">
					白名单标识
				</th>
			</tr>
			<s:iterator value="paginationList.recordList" id="iList" status="stuts">
				<tr align="center" class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>" >
					<td>
						<input type="checkbox"
							style="width: 13px; height: 13px;" name="userIdList"
							value="<s:property value="#iList[0].loginId" />" />
					</td>
					<td>
						<s:property value="#iList[0].loginId" />
					</td>
					<td>
						<s:property value="#iList[1].userId" />
					</td>
					<td>
						<s:property value="#iList[1].userCname" />
					</td>
					<td>
						<s:property value="#iList[1].ubaseInst.instName" />
					</td>
					<td>
						<s:date name="#iList[0].loginTime" format="yyyy-MM-dd HH:mm:ss" />
					</td>
					<td>
					    <s:if test="#iList[0].status==1">
						    	在线
					    </s:if>
					    <s:else>
						    	已注销
					    </s:else>
					</td>
					<td>
					    <s:date name="#iList[0].kickoutTime" format="yyyy-MM-dd HH:mm" />
					</td>
					<td>
						<s:if test="#iList[1].isList==1">
						    	是
					    	</s:if>
					    	<s:else>
						    	否
					    </s:else>
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
<script type="text/javascript">
	document.getElementById("lessGridList").style.height = screen.availHeight - 335;
</script>
</html>
<script language="javascript">
function OpenModalWindowSubmit(newURL,width,height,needReload,s) {
		retData = showModalDialog(newURL, 
				  window, 
				  "dialogWidth:" + width
				+ "px;dialogHeight:" + height
				+ "px;center=1;scroll=1;help=0;status=0;");
        
		if(needReload && retData){
			//var a = parent.frames['topFrame'].document.forms[0].submit();
			window.location.reload();
		}
}
</script>