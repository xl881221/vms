<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="common/include.jsp"%>
<%@include file="common/commen-ui.jsp"%>
<script type="text/javascript" src="js/My97DatePicker/WdatePicker.js"></script>
<meta http-equiv="Pragma" content="no-cache" />
<title>操作日志查询</title>

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
	filter: alpha(opacity =     90);
	-moz-opacity: 0.9;
	display: none;
}
</style>
<script type="text/javascript">
	function submitForm(actionUrl){
		var form = document.getElementById("main");
		form.action=actionUrl;
		form.submit();
		form.action = "listSysLog.action";
	}
	function selectAll(obj,itemName){
	      var inputs=document.getElementsByName(itemName);
	      for(var i=0;i<inputs.length;i++){
	      	if(inputs[i].disabled == false){
	     	    inputs[i].checked=obj.checked; 
	      	}else{
	      		inputs[i].checked=false; 
	      	}
	     }
	  };
	  
	 function deleteClicked(actionUrl){
	    var inputs=document.getElementsByTagName("input");
 	    var flag = "N"; //默认没选择
 	 	for(var i=0;i<inputs.length;i++){
     	    if(inputs[i].checked == true && inputs[i].id!='checkAll'){//选择了
     	    	flag = "Y";
     	    	break;}     	    
     	}
     	if(flag == "N"){
    	     alert("请首先选择要删除的数据!");
    	     return false;
  		};
  			 
       if(confirm("是否确认删除已点选的数据！")){
    	 	var form = document.getElementById("main");
			form.action=actionUrl;
			form.submit();
			form.action = "listSysLog.action";
	   }else{
	        return false;
	   }
  }
  function hideDetailInfoDIV(){
		document.getElementById("detailInfoDIV").style.display='none';
	}

	function showDetailInfoDIV(logID){
	 	var currRow =  window.event.srcElement.parentElement.parentElement;// 获取当前行

	 	document.getElementById("_td2").innerHTML=logID;

	 	for(var i=3;i<16;i++){
	 		document.getElementById("_td"+i).innerHTML=currRow.cells[i-1].title;
	 	}
	 	
	 	document.getElementById("detailInfoDIV").style.display='block';
	}
	
	function output() {
		//拷贝 
		var elTable = document.getElementById("lessGridList"); //这里的page1 是包含表格的Div层的ID
		var oRangeRef = document.body.createTextRange();
		oRangeRef.moveToElementText( elTable );
		oRangeRef.execCommand("Copy");
		
		
		//粘贴 
		try{
			var appExcel = new ActiveXObject( "Excel.Application" ); 
			appExcel.Visible = true; 
			appExcel.Workbooks.Add().Worksheets.Item(1).Paste(); 
		//appExcel = null; 
		}catch(e){
			alert("使用此功能必须在浏览器中设置:Internet选项->安全->将本站加入“受信任的站点”。"); 
		}
		
		
		var elTable1 = document.getElementById("lessGridList"); 
		var oRangeRef1 = document.body.createTextRange(); 
		oRangeRef1.moveToElementText( elTable1 ); 
		oRangeRef1.execCommand( "Copy" );
		
		
		//粘贴 
		try{ 
		appExcel.Visible = true; 
		appExcel.Worksheets.Item(2).Paste(); 
		appExcel1 = null; 
		}catch(e){ 
		alert("使用此功能必须在浏览器中设置:Internet选项->安全->将本站加入“受信任的站点”。"); 
		} 
	}
	function toExcel(){
		var form = document.getElementById("main");
		form.action="logToExcel.action";
		form.submit();
		form.action = "listSysLog.action";
	}

</script>
</head>
<body>
<form id="main" action="listSysLog.action" method="post">
<table id="tbl_main" cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td class="centercondition">
			<div id="tbl_current_status">
				<span><img src="<c:out value="${webapp}"/>/themes/images/icons/icon13.png" /></span>
				<span class="current_status_menu">当前位置：</span>
				<span class="current_status_submenu1">系统管理</span>
				<span class="current_status_submenu">基础信息管理</span>
				<span class="current_status_submenu">操作日志查询</span>
			</div>
			<div class="widthauto">
			<table id="tbl_query" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td align="left" >
					用户名
					</td>
					<td align="left" >
					  <span><input id="userCname" class="tbl_query_text" name="userCname" type="text" value="<s:property value='userCname' />" /></span>
					</td>
					<td align="left" >
					菜单名
					</td>
					<td align="left" >
					  <span><input id="menuName" class="tbl_query_text" name="menuName" type="text" value="<s:property value='menuName' />" /></span>
					</td>
					<td align="left" colspan="2" >
					状态<span class="mleft15"><s:select list="STATUS_MAP" listKey="key" listValue="value" name="status"></s:select></span>
					</td>
				</tr>
				<tr>
					<td width="80" align="left" >
					开始时间
					</td>
					<td align="left" >
					<span><input id="startTime" name="startTime" type="text"  value="<s:property value="startTime" />" class="tbl_query_time" onFocus="WdatePicker()" /></span>
					</td>
					<td width="80" align="left" >
					结束时间
					</td>
					<td align="left" >
					<span><input id="endTime" name="endTime" type="text"	value="<s:property value='endTime' />" class="tbl_query_time" onFocus="WdatePicker()" /></span>
					</td>
					<td width="90" style="text-align:left">
					<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="submitForm('listSysLog.action');" name="cmdFilter" value="查询" id="cmdFilter" />				
					</td>
					<td width="90" style="text-align:left">
					<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="toExcel();" name="delBtn" value="导出" id="export" />				
					</td>
				</tr>
			</table>
			</div>
			<div id="lessGridList6" style="overflow: auto; width: 100%;">
			<table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" style="border-collapse: collapse; width: 100%;">
				<tr class="lessGrid head">
					<th style="text-align: center">序号</th>
					<th style="text-align: center; display: none">日志编号</th>
					<th style="text-align: center; display: none">用户编号</th>
					<th style="text-align: center; display: none">用户登录名</th>
					<th style="text-align: center">用户中文名</th>
					<th style="text-align: center; display: none">机构编号</th>
					<th style="text-align: center">机构名称</th>
					<th style="text-align: center; display: none">菜单编号</th>
					<th style="text-align: center">菜单名</th>
					<th style="text-align: center; display: none">用户IP</th>
					<th style="text-align: center; display: none">用户浏览器</th>
					<th style="text-align: center; display: none">日志类型</th>
					<th style="text-align: center">操作时间</th>
					<th style="text-align: center">说明</th>
					<th style="text-align: center">操作状态</th>
				</tr>
				<s:iterator value="dataList" id="dList" status="stuts">
					<tr
						class="<s:if test='#stuts.index%2 == 0'>lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>"
						>
						<td align="center"><s:property value='#stuts.count' /></td>
						<td title="<s:property value='logId'/>" style="display: none">
						<s:property value='logId' /></td>
						<td
							title="<s:property value='userId'/>" style="display: none">
						<s:property value='userId' /></td>
						<td align="center" title="<s:property value='userEname' />" style="display: none">
							<s:property value='userEname'/></td>
						<td align="center" title="<s:property value='userCname' />"><a href="#"
							onclick="showDetailInfoDIV('<s:property value='logId'/>');"><s:property value='userCname' /></a></td>
						<td align="center" title="<s:property value='instId' />" style="display: none">
						<s:property value='instId' /></td>
						<td align="center" title="<s:property value='instCname' />">
						<s:property value='instCname' /></td>
						<td align="center" title="<s:property value='menuId' />" style="display: none">
						<s:property value='menuId'/></td>
						<td align="center" title="<s:property value='menuName'/>"><s:property value='menuName'/></td>
						<td align="center" title="<s:property value='ip'/>" style="display: none"><s:property value='ip' /></td>
						<td align="center" title="<s:property value='browse'/>" style="display: none"><s:property value='browse' /></td>
						<td align="center" title="<s:property value='logType' />" style="display: none"><s:property value='logType'/></td>
						<td align="center" title="<s:date name="execTime" format="yyyy-MM-dd HH:mm:ss" />"><s:date name="execTime" format="yyyy-MM-dd HH:mm:ss" /></td>
						<td align="center" title="<s:property value='description4Html'/>">
						    <s:if test="!descriptionShort.trim().equals('')">
						        <s:property value='descriptionShort'></s:property>
						    </s:if>
						    <s:else>
						        <s:property value='description'></s:property>
						    </s:else>
							
						</td>
						<td align="center"	title="<s:if test='status=="1"'>成功</s:if><s:else>失败</s:else>">
							<s:if test='status=="1"'>成功</s:if><s:else>失败</s:else>
						</td>
					</tr>
					</s:iterator>
			</table>
			</div>
			<div id="anpBoud" align="Right" style="width:100%;">
		        <table width="100%" cellspacing="0" border="0">
		            <tr>           
		                <td align="right"><s:component template="pagediv"/></td>
		            </tr>
		        </table>
		    </div>
		</td>
	</tr>
</table>
<div id="detailInfoDIV" class="detailInfoDIV">
<center>
<table width="440px" border="0">
	<tr>
		<td colspan="2" align="center"><span
			style='color: #A24313; font: normal 12px Arial'>操作日志详细信息</span></td>
	</tr>
	<tr>
		<td width="20%"><span
			style='color: #A24313; font: normal 12px Arial'>日志编号：</span></td>
		<td width="85%" id="_td2" name="_td2"></td>
	</tr>
	<tr>
		<td width="20%"><span
			style='color: #A24313; font: normal 12px Arial'>用户登录名：</span></td>
		<td width="85%" id="_td3" name="_td3"></td>
	</tr>
	<tr style="display:none">
		<td width="20%"><span
			style='color: #A24313; font: normal 12px Arial'>用户英文名：</span></td>
		<td width="85%" id="_td4" name="_td4"></td>
	</tr>
	<tr>
		<td width="20%"><span
			style='color: #A24313; font: normal 12px Arial'>用户中文名：</span></td>
		<td width="85%" id="_td5" name="_td5"></td>
	</tr>
	<tr>
		<td width="20%"><span
			style='color: #A24313; font: normal 12px Arial'>机构编号：</span></td>
		<td width="85%" id="_td6" name="_td6"></td>
	</tr>
	<tr>
		<td width="20%"><span
			style='color: #A24313; font: normal 12px Arial'>机构名称：</span></td>
		<td width="85%" id="_td7" name="_td7"></td>
	</tr>
	<tr>
		<td width="20%"><span
			style='color: #A24313; font: normal 12px Arial'>菜单编号：</span></td>
		<td width="85%" id="_td8" name="_td8"></td>
	</tr>
	<tr>
		<td width="20%"><span
			style='color: #A24313; font: normal 12px Arial'>菜单名称：</span></td>
		<td width="85%" id="_td9" name="_td9"></td>
	</tr>
	<tr>
		<td width="20%"><span
			style='color: #A24313; font: normal 12px Arial'>用户IP：</span></td>
		<td width="85%" id="_td10" name="_td10"></td>
	</tr>
	<tr  style='display: none'>
		<td width="20%"><span
			style='color: #A24313; font: normal 12px Arial'>用户浏览器：</span></td>
		<td width="85%" id="_td11" name="_td11"></td>
	</tr>
	<tr>
		<td width="20%"><span
			style='color: #A24313; font: normal 12px Arial'>日志类型：</span></td>
		<td width="85%" id="_td12" name="_td12"></td>
	</tr>
	<tr>
		<td width="20%"><span
			style='color: #A24313; font: normal 12px Arial'>执行时间：</span></td>
		<td width="85%" id="_td13" name="_td13"></td>
	</tr>
	<tr>
		<td width="20%"><span
			style='color: #A24313; font: normal 12px Arial'>描述：</span></td>
		<td width="85%" id="_td14" name="_td14"></td>
	</tr>
	<tr>
		<td width="20%"><span
			style='color: #A24313; font: normal 12px Arial'>状态：</span></td>
		<td width="85%" id="_td15" name="_td15"></td>
	</tr>
	<tr>
		<td nowrap="nowrap" colspan="2" align="center">
		<div style="float: left; margin-left: 180px" type="btn"
			onclick="hideDetailInfoDIV();" id="_returnBtn"
			img="<c:out value="${webapp}"/>/image/button/back.gif"
			name="_returnBtn" value="返回" />
		</td>
	</tr>
</table>
</center>
</div>
</form>
</body>
</html>