<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="fmss.dao.entity.LoginDO,fmss.common.util.Constants"%>

<%@include file="../../common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<%@include file="../../common/include.jsp"%>
	<title><s:if test="inst.instId != null && inst.instId != '' ">修改机构</s:if><s:else>新增机构</s:else></title>
	<link type="text/css" href="<c:out value="${sysTheme}"/>/css/subWindow.css" rel="stylesheet">
	<link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
	<link rel="stylesheet" type="text/css" href="css/easyui.css">
	<link rel="stylesheet" type="text/css" href="css/icon.css">
	<script type="text/javascript" src="js/jquery-1.4.4.min.js"></script>
	<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<c:out value='${webapp}'/>/js/jquery/region.js" charset="GBK"></script>
	<script language="javascript" type="text/javascript">
		//标识页面是否已提交
		var subed = false;
		function check(){
			//验证是否重复提交
			if (subed == true){
				alert("信息正在发送给服务器，请不要重复提交信息！");
				return false;
			}
			//机构编码不能为空
			if(fucCheckNull(document.getElementById("inst.instId"),"机构编码不能为空")==false){
				//var m = new MessageBox(document.getElementById("inst.instId"));
			    //m.Show("机构编码不能为空");
				return false;
			}
			
			var objs=document.getElementsByName("inst.isHead");
			if(objs[0]&&objs[0].checked){
			       if(document.getElementById("parentInstId").value!=""){
			          alert("只有根机构才能设置为总行");
			          return;
			       }
			 }
			if(/\W/.test(document.getElementById("inst.instId").value)){
				var m = new MessageBox(document.getElementById("inst.instId"));
				m.Show("机构编码不能含数字字母之外的字符");
				return false;
			}
			
			//机构编码必须大于四位,防止资源编号和功能的资源编号重复
			/*
			if(1){
			    
			     var p = document.getElementById("inst.instId").value;
			     if(p.length<1){
			     var m = new MessageBox(document.getElementById("inst.instId"));
			     m.Show("机构编码不能为空");
			     return false;
			     }
			    
			}
			*/
			
			//机构编码超出长度, 机构编码超出指定长度,最大20字节
			if(fucCheckLength(document.getElementById("inst.instId"),20,"机构编码超出指定长度,最大20字节")==false){
				return false;
			}
			
			//机构名称不能为空
			if(fucCheckNull(document.getElementById("inst.instName"),"机构名称不能为空")==false){
				return false;
			}
			
			//机构名称超出长度, 机构名称超出指定长度,最大30字节
			if(fucCheckLength(document.getElementById("inst.instName"),60,"机构名称超出指定长度,最大60字节")==false){
				return false;
			}
			
			//机构简称不能为空
			if(fucCheckNull(document.getElementById("inst.instSmpName"),"机构简称不能为空")==false){
				return false;
			}
			
			//机构简称超出指定长度,最大20字节
			if(fucCheckLength(document.getElementById("inst.instSmpName"),60,"机构简称超出指定长度,最大60字节")==false){
				return false;
			}
			
			//机构地址超出指定长度,最大100字节
			if(fucCheckLength(document.getElementById("inst.address"),100,"机构地址超出指定长度,最大100字节")==false){
				return false;
			}
			
			//机构邮编超出指定长度,最大6字节
			if(fucCheckLength(document.getElementById("inst.zip"),6,"机构邮编超出指定长度,最大6字节")==false){
				return false;
			}
			
			//机构电话超出指定长度,最大27字节
			if(fucCheckPhone(document.getElementById("inst.tel"),"机构电话格式不对")==false){
				return false;
			}
			
			//机构传真超出指定长度,最大20字节
			if(fucCheckLength(document.getElementById("inst.fax"),20,"机构传真超出指定长度,最大20字节")==false){
				return false;
			}
			
			//排序值超出指定长度,最大60字节
			if(fucCheckLength(document.getElementById("inst.orderNum"),60,"排序值超出指定长度,最大60字节")==false){
				return false;
			}
			

			//判断时间是否合法
			//if(fucCheckDateFormat(document.getElementById("inst.startDate"),"启用日期格式不合法")==false){
				//return false;
			//}
			
			//判断时间是否合法
			//if(fucCheckDateFormat(document.getElementById("inst.endDate"),"终止日期格式不合法")==false){
				//return false;
			//}
			//创建日期不能大于终止日期!
			//if(fucCheckDateOrder(document.getElementById("inst.createTime"),document.getElementById("inst.endDate"),"启用日期不能大于终止日期")==false){
			//	return false;
			//}
			
			//机构描述超出指定长度,最大300字节			
			if(fucCheckLength(document.getElementById("inst.description"),300,"机构描述超出指定长度,最大300字节")==false){
				return false;
			}

			//排序值必须非负整数
			if(fucIsNoUnsignedInteger(document.getElementById("inst.orderNum"),"排序值必须非负整数 ")==false){
				return false;
			}	
			//获取机构类别id
			var taxPayerType=document.getElementById("inst.taxPayerType").value;
			//当机构类别id位1时，字符验证
			if(taxPayerType=="1"){
				
				if(fucCheckNull(document.getElementById("inst.taxpernumber"),"纳税人识别号不能为空")==false){
					return false;
				}
				//纳税人账号不能为空
				if(fucCheckNull(document.getElementById("inst.account"),"纳税人账号不能为空")==false){
					return false;
				}
				//纳税人名称不能为空
				if(fucCheckNull(document.getElementById("inst.taxpername"),"纳税人名称不能为空")==false){
					return false;
				}
				//纳税人地址不能为空
				if(fucCheckNull(document.getElementById("inst.taxaddress"),"纳税人地址不能为空")==false){
					return false;
				}
				//纳税人电话不能为空
				if(fucCheckNull(document.getElementById("inst.taxtel"),"纳税人电话不能为空")==false){
					return false;
				}
				//纳税人开户行
				if(fucCheckNull(document.getElementById("inst.taxbank"),"纳税人开户行不能为空")==false){
					return false;
				}
			}
			//当机构类别id位0时，字符验证
			if(taxPayerType=="0"){
				//纳税人识别号不能为空
				if(fucCheckNull(document.getElementById("inst.taxpernumber"),"纳税人识别号不能为空")==false){
					return false;
				}
				//纳税人账号不能为空
				if(fucCheckNull(document.getElementById("inst.account"),"纳税人账号不能为空")==false){
					return false;
				}
				//纳税人名称不能为空
				if(fucCheckNull(document.getElementById("inst.taxpername"),"纳税人名称不能为空")==false){
					return false;
				}
				//纳税人地址不能为空
				if(fucCheckNull(document.getElementById("inst.taxaddress"),"纳税人地址不能为空")==false){
					return false;
				}
				//纳税人电话不能为空
				if(fucCheckNull(document.getElementById("inst.taxtel"),"纳税人电话不能为空")==false){
					return false;
				}
				//纳税人开户行
				if(fucCheckNull(document.getElementById("inst.taxbank"),"纳税人开户行不能为空")==false){
					return false;
				}
				else{
				//纳税人识别号必须是16~20位的纯数字，电话必须为纯数字
					/*if(fucIsUnsigned(document.getElementById("inst.taxpernumber"),"纳税人识别号必须为15~20位的纯数字")==false){
						return false;
					}
					if(fucCheckPhone(document.getElementById("inst.taxtel"),"纳税人电话格式不对")==false){
						return false;
					}else{
						if(document.getElementById("inst.taxpernumber").value.length<15||document.getElementById("inst.taxpernumber").value.length>20){
							var m = new MessageBox(document.getElementById("inst.taxpernumber"));
		  						 m.Show("纳税人识别号必须为15~20位的纯数字");
		  						 return false;
						}
					}*/
				}
			}
			subed=true;
			return subed;
		}
		
		function findOutSubmit() {		
			if(check()) {	
			   // document.getElementById('combobox_test').value=eval($('#combobox_test').combobox('getValues'));	
				document.formInst.action="saveInst.action";
				document.formInst.method="post";
				document.formInst.submit();
				document.getElementById('BtnSave').disabled=true;
			}
		}
		<s:if test="inst.instId != null && inst.instId != ''">
		var adds = '0';
		</s:if>
		<s:else>
		var adds = '1';
		</s:else>
		window.onload = function(){
			var region = '<s:property value="inst.instRegion"/>';
			c = new Region(new Location("province","city","area", region));
			c.init();
		}
		var emailJsonp;
		//初始化多选框
		$(document).ready(function(){

			if('<s:property value="editType"/>'=='add'){
				 var checkedTreeNodeId=window.dialogArguments.parent.frames["leftFrame"].document.getElementById('checkedTreeNodeId').value;
				 var checkedTreeNodeName=window.dialogArguments.parent.frames["leftFrame"].document.getElementById('checkedTreeNodeName').value;
				if(checkedTreeNodeId !='' && checkedTreeNodeName != '' &&'<s:property value="isRoot"/>'!='true'){
			     $('#parentInstId').val(checkedTreeNodeId);
			     $('#parentInstName').val(checkedTreeNodeName);
			    }
			}
		});
		
		function onSelectChanged(){
			if(document.getElementById("inst.taxPayerType").value=="1"){
				$.ajax({
						type : "Post",
						url : "onSelectChang.action?inst.instId=" +document.getElementById("inst.instIdb").value,
						dataType : "json", //可以是text，如果用text，返回的结果为字符串；如果需要json格式的，可是设置为json
						success : function(data) {
							if(data.length==1){
								//设置只读属性
								document.getElementById("inst.taxpernumber").readOnly=true;
								document.getElementById("inst.account").readOnly=true;
								document.getElementById("inst.taxpername").readOnly=true;
								document.getElementById("inst.taxaddress").readOnly=true;
								document.getElementById("inst.taxbank").readOnly=true;
								document.getElementById("inst.taxtel").readOnly=true;
								//设置class
								document.getElementById("inst.taxpernumber").setAttribute("class","readonly");
								document.getElementById("inst.account").setAttribute("class","readonly");
								document.getElementById("inst.taxpername").setAttribute("class","readonly");
								document.getElementById("inst.taxaddress").setAttribute("class","readonly");
								document.getElementById("inst.taxbank").setAttribute("class","readonly");
								document.getElementById("inst.taxtel").setAttribute("class","readonly");
								return false;
							}
							var optList=data[1];
							var opt=document.getElementById("inst.instIdSelect");
							for(var i=0;i<data[1].length;i++){
								var newoption = document.createElement("OPTION");
								opt.options.add(newoption);
								newoption.innerText=optList[i].instName;
								newoption.value=optList[i].instId;
							}
							opt.removeAttribute("disabled");
							opt.removeAttribute("class");
							//填充数据
							document.getElementById("inst.taxpernumber").value=data[0].taxpernumber;
							document.getElementById("inst.account").value=data[0].account;
							document.getElementById("inst.taxpername").value=data[0].taxpername;
							document.getElementById("inst.taxaddress").value=data[0].taxaddress;
							document.getElementById("inst.taxtel").value=data[0].taxtel;
							document.getElementById("inst.taxbank").value=data[0].taxbank;
							//设置只读属性
							document.getElementById("inst.taxpernumber").readOnly=true;
							document.getElementById("inst.account").readOnly=true;
							document.getElementById("inst.taxpername").readOnly=true;
							document.getElementById("inst.taxaddress").readOnly=true;
							document.getElementById("inst.taxbank").readOnly=true;
							document.getElementById("inst.taxtel").readOnly=true;
							//设置class
							document.getElementById("inst.taxpernumber").setAttribute("class","readonly");
							document.getElementById("inst.account").setAttribute("class","readonly");
							document.getElementById("inst.taxpername").setAttribute("class","readonly");
							document.getElementById("inst.taxaddress").setAttribute("class","readonly");
							document.getElementById("inst.taxbank").setAttribute("class","readonly");
							document.getElementById("inst.taxtel").setAttribute("class","readonly");
						},
						error : function(msg) {
							alert(" 数据加载失败！" + msg);
						}
					});
				
			}else{
				clearText();
			}
		}
		function clearText(){
			if(document.getElementById("editType").value=="add"){
			editTextChanged()
			//清空文本框的值
			document.getElementById("inst.instId").value="";
			document.getElementById("inst.instName").value="";
			document.getElementById("inst.instSmpName").value="";
			document.getElementById("inst.fax").value="";
			document.getElementById("inst.address").value="";
			document.getElementById("inst.zip").value="";
			document.getElementById("inst.tel").value="";
			document.getElementById("inst.orderNum").value="";
			document.getElementById("startDate").value="";
			document.getElementById("inst.description").value="";
			//设置class
			document.getElementById("inst.instId").removeAttribute("class");
			document.getElementById("inst.instName").removeAttribute("class");
			document.getElementById("inst.instSmpName").removeAttribute("class");
			document.getElementById("inst.fax").removeAttribute("class");
			document.getElementById("inst.address").removeAttribute("class");
			document.getElementById("inst.zip").removeAttribute("class");
			document.getElementById("inst.tel").removeAttribute("class");
			document.getElementById("inst.orderNum").removeAttribute("class");
			document.getElementById("startDate").removeAttribute("class");
			document.getElementById("inst.description").removeAttribute("class");
			
			}else{
				editTextChanged()
			}
		}
			
		//当修改时，文本框的变化
		function editTextChanged(){
			var opt=document.getElementById("inst.instIdSelect");
				opt.setAttribute("disabled","disabled");
				opt.setAttribute("class","readonly");
				opt.options.length=0;
				//清除文本框
				document.getElementById("inst.taxpernumber").value="";
				document.getElementById("inst.account").value="";
				document.getElementById("inst.taxpername").value="";
				document.getElementById("inst.taxaddress").value="";
				document.getElementById("inst.taxtel").value="";
				document.getElementById("inst.taxbank").value="";
				//移除文本框clss
				document.getElementById("inst.taxpernumber").removeAttribute("class");
				document.getElementById("inst.account").removeAttribute("class");
				document.getElementById("inst.taxpername").removeAttribute("class");
				document.getElementById("inst.taxaddress").removeAttribute("class");
				document.getElementById("inst.taxtel").removeAttribute("class");
				document.getElementById("inst.taxbank").removeAttribute("class");
				//更改文本框只读属性
				document.getElementById("inst.instId").readOnly=false;
				document.getElementById("inst.taxpernumber").readOnly=false;
				document.getElementById("inst.account").readOnly=false;
				document.getElementById("inst.taxpername").readOnly=false;
				document.getElementById("inst.taxaddress").readOnly=false;
				document.getElementById("inst.taxbank").readOnly=false;
				document.getElementById("inst.taxtel").readOnly=false;
		}
	function getTaxInfo(){
		var param = "instCode=" + document.getElementById("inst.instIdSelect").value;
		$.ajax({
			type: "Post",
		    url: "loadSelfInfo.action?"+param,
		    dataType: "json", //可以是text，如果用text，返回的结果为字符串；如果需要json格式的，可是设置为json
		    success: function (data) {
				document.getElementById("inst.taxpernumber").value=data.taxpernumber;
				document.getElementById("inst.account").value=data.account;
				document.getElementById("inst.taxpername").value=data.taxpername;
				document.getElementById("inst.taxaddress").value=data.taxaddress;
				document.getElementById("inst.taxtel").value=data.taxtel;
				document.getElementById("inst.taxbank").value=data.taxbank;
			},
		    error: function (msg) {
		    	alert(" 数据加载失败！" + msg);
		    }
		 });
	}
	</script>
</head>
<body scroll="no" style="overflow:hidden;">
<div class="showBoxDiv">
<form name="formInst" id="formInst" action="saveInst.action" method="post">
<input type="hidden" name="editType" id="editType" value="<s:property value="editType"/>" />
<input type="hidden" name="inst.instPath" value="<s:property value="inst.instPath" />" /> 
<input type="hidden" name="inst.instLevel" value="<s:property value="inst.instLevel" />" /> 
<div id="editpanel">
<div id="editsubpanel" class="editsubpanel">
<table id="contenttable" class="lessGrid" cellspacing="0" width="100%" align="center" cellpadding="0" >
	<tr class="header">
		<th colspan="4" >
			<s:if test="editType != 'add' ">修改机构</s:if> <s:else>新增机构</s:else>
		</th>
	</tr>
	<tr>
		<td width="15%" align="right" class="listbar">机构编号:</td>
		<td width="35%">
			<input id="inst.instIdb" name="inst.instIdb" type="hidden" value="<s:property value="inst.instId"/>"/>
			<s:if test="editType != 'add' ">
				<input id="inst.instId" name="inst.instId" type="text" value="<s:property value="inst.instId"/>" readonly class="readonly"/>
            </s:if> 
            <s:else>
				<input id="inst.instId" name="inst.instId" type="text" value=""/>
			</s:else> 
			&nbsp;<span class="spanstar">*</span>
		</td>
		<td width="15%" align="right" class="listbar">机构名称:</td>
		<td>
			<input id="inst.instName" name="inst.instName" type="text" value="<s:property value="inst.instName" />" /> 
			&nbsp;<span class="spanstar">*</span>
		</td>
	</tr>
	<tr>
		<td align="right" class="listbar">机构简称:</td>
		<td>
			<s:if test="editType != 'add' ">
				<input id="inst.instSmpName" name="inst.instSmpName" type="text" value="<s:property value="inst.instSmpName" />" />
			</s:if> 
            <s:else>
            	<input id="inst.instSmpName" name="inst.instSmpName" type="text" />
            </s:else> 
            &nbsp;<span style="color: #FF0000">*</span>
        </td>
		<td align="right" class="listbar">上级机构:</td>
		<td>
		    <input id="parentInstId" name="inst.parentInstId" type="hidden" value="<s:property value="inst.parentInstId" />"/>
		    <input id="parentInstName" readonly name="inst.parentInstName" type="text" value='<s:if test="inst.parentInstId!=null"><s:property value="inst.parentInstName" />[<s:property value="inst.parentInstId" />]</s:if>' />
			<%--<s:select id="inst.parentInstId" name="inst.parentInstId" value="inst.parentInstId" list="instList" listKey="instId" listValue="instSmpName" theme="simple" />--%>
        </td>
	</tr>
	<tr>
		<td align="right" class="listbar">机构级别:</td>
		<td>
			<s:select id="inst.instLayer" name="inst.instLayer" value="inst.instLayer" list="instLevList" listKey="dicValue" listValue="dicName" theme="simple" />
		</td>
		<td align="right" class="listbar">机构传真:</td>
		<td><input id="inst.fax" type="text" value="<s:property value="inst.fax"/>" name="inst.fax"/></td>
	
	</tr>
	<tr>
		<td align="right" class="listbar">机构地址:</td>
		<td><input id="inst.address" type="text" value="<s:property value="inst.address"/>" name="inst.address"/></td>
		<td align="right" class="listbar">机构邮编:</td>
		<td><input id="inst.zip" type="text" value="<s:property value="inst.zip"/>" name="inst.zip"/></td>
	</tr>
	<tr>
		<td align="right" class="listbar">机构电话:</td>
		<td><input id="inst.tel" type="text" value="<s:property value="inst.tel"/>" name="inst.tel"/></td>
		<td align="right" class="listbar">排序值:</td>
		<td><input id="inst.orderNum" type="text" value="<s:property value="inst.orderNum"/>" name="inst.orderNum"/></td>
	</tr>
<!--
	<tr>
		<td align="right">模块名称:</td>
		<td>
			<select id="systemId" name="systemId" onchange="ajaxReadData(this)" style="width: 133;">				
				<s:iterator value="instSystemReal" id="s">
					<option value="<s:property value="#s.systemId"/>" <s:if test="vSystemId==#s.systemId">selected</s:if>>
						<s:property value="#s.systemCname"/>
					</option>
				</s:iterator>
			</select>
		</td>
		<td align="right">邮箱用户:</td>
		<td>
	        <input class="easyui-combobox"  id="combobox_test" name="combobox_test" style="width: 133;" />
		</td>
	</tr>
-->	
	<tr>
		<td align="right" class="listbar">启用日期:</td>
		<td><input id="startDate" type="text" value="<s:date name="inst.startDate" format="yyyy-MM-dd"/>" name="startDate" class="Wdate" onFocus="WdatePicker()"/></td>
		<%
	 		LoginDO login = (LoginDO) session.getAttribute(Constants.LOGIN_USER);
			String instIsHead = login.getInstIsHead();
			String userId = login.getUserId();
		%>
		<td align="right" class="listbar"><input id="inst.email" type="hidden" value="<s:property value="inst.email"/>" name="inst.email" maxlength="40"/>
			是否总行:
		</td>
		
		<td>
		   <%if(userId.equals("admin")) {
		   %> <s:checkbox theme="simple"   name="inst.isHead" value="inst.isHead"></s:checkbox><%
             }else{
           %><s:property value="inst.isHeadCH"/>
              <input type="hidden" name="inst.isHead" value="<s:property value="inst.isHead" />" /> 
           <%
             } 
           %>
		  
		</td>
	</tr>
	
	<tr>
		<td align="right" class="listbar">是否业务机构:</td>
		<td>
			<s:checkbox theme="simple" name="inst.isBussiness" value="inst.isBussiness"></s:checkbox>
		</td>
		<td align="right" class="listbar">启用标识:</td>
		<td>
			<s:checkbox theme="simple" name="inst.enabled" value="inst.enabled"></s:checkbox>
		</td>
		</tr>
		<s:if test='inst.taxPayerType=="1"'>
		<tr>
		<td align="right" class="listbar">纳税人识别号:</td>
		<td>
			<input type="text" readonly class="readonly" id="inst.taxpernumber" name="inst.taxpernumber" value="<s:property value="inst.taxpernumber"/>">
		</td>
		<td align="right"  class="listbar">纳税人账号:</td>
		<td>
			<input type="text" readonly class="readonly" id="inst.account" name="inst.account" value="<s:property value="inst.account"/>">
		</td>
		
		</tr>
	    <tr>
		<td align="right" class="listbar">纳税人名称:</td>
		<td>
			<input type="text" readonly class="readonly" id="inst.taxpername" name="inst.taxpername" value="<s:property value="inst.taxpername"/>">
		</td>
		<td align="right" class="listbar">纳税人地址:</td>
		<td>
			<input type="text" readonly class="readonly" id="inst.taxaddress" name="inst.taxaddress" value="<s:property value="inst.taxaddress"/>">
		</td>
		</tr>
	<tr>
		
		<td align="right" class="listbar">纳税人电话:</td>
		<td>
			<input type="text" readonly class="readonly" id="inst.taxtel" name="inst.taxtel" value="<s:property value="inst.taxtel"/>">
		</td>
		<td align="right"  class="listbar">纳税人开户行:</td>
		<td>
			<input type="text" readonly class="readonly" id="inst.taxbank" name="inst.taxbank" value="<s:property value="inst.taxbank"/>">
		</td>
		</tr>
		</s:if>
		<s:else>
			<tr>
		<td align="right" class="listbar">纳税人识别号:</td>
		<td>
			<input type="text" id="inst.taxpernumber" name="inst.taxpernumber" value="<s:property value="inst.taxpernumber"/>">
		</td>
		<td align="right"  class="listbar">纳税人账号:</td>
		<td>
			<input type="text" id="inst.account" name="inst.account" value="<s:property value="inst.account"/>">
		</td>
		
		</tr>
	    <tr>
		<td align="right" class="listbar">纳税人名称:</td>
		<td>
			<input type="text" id="inst.taxpername" name="inst.taxpername" value="<s:property value="inst.taxpername"/>">
		</td>
		<td align="right" class="listbar">纳税人地址:</td>
		<td>
			<input type="text" id="inst.taxaddress" name="inst.taxaddress" value="<s:property value="inst.taxaddress"/>">
		</td>
		</tr>
	<tr>
		
		<td align="right" class="listbar">纳税人电话:</td>
		<td>
			<input type="text" id="inst.taxtel" name="inst.taxtel" value="<s:property value="inst.taxtel"/>">
		</td>
		<td align="right"  class="listbar">纳税人开户行:</td>
		<td>
			<input type="text" id="inst.taxbank" name="inst.taxbank" value="<s:property value="inst.taxbank"/>">
		</td>
		</tr>
		</s:else>
		<tr>
		<td align="right" class="listbar"> 机构类别:</td>
		<td>
			<s:select id="inst.taxPayerType" name="inst.taxPayerType" value="inst.taxPayerType" list="instPayerList" listKey="codeValueStandardNum" listValue="codeName" theme="simple" onchange="onSelectChanged()"/>
		</td>
		<td align="right" class="listbar">纳税主体机构:</td>
			<td>
				<s:if test='inst.taxPayerType=="1"'>
					<s:select id="inst.instIdSelect" name="inst.instNameSelect" value="inst.instName" list="instPathList" listKey="instId" listValue="instName" theme="simple" onchange="getTaxInfo()"/>
				</s:if>
				<s:else>
					<select id="inst.instIdSelect" disabled="disabled" class="readOnly" onchange="getTaxInfo()"></select>
				</s:else>
			</td>
		</tr>
		<tr>
			<td align="right" class="listbar">区域:</td>
		<td colspan="3">
		<input id="region" type="text" value="<s:property value="inst.instRegion"/>" name="region" style="display: none;"/>
			  <select name="province" id="province">
			  </select>
			  <select name="city" id="city">
			  </select>
			<select name="area" id="area">
			  </select>
		  <input id="inst.instRegion" type="hidden" value="" name="inst.instRegion"/><span class="spanstar">*</span>
		</td>
		</tr>

	<tr>
		<td align="right" class="listbar">描述:</td>
		<td colspan=3><textarea id="inst.description" name="inst.description" style="width: 86%"><s:property value="inst.description"/></textarea></td>
	</tr>
</table>
</div>
</div>
	<div id="ctrlbutton" class="ctrlbutton" style="border:0px">		
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="findOutSubmit()" name="BtnSave" value="保存" id="BtnSave"/>
	<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="window.close()" name="BtnReturn" value="关闭" id="BtnReturn"/>		
	</div>
</form>
</div>
</body>
</html>
