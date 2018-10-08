<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@include file="../../common/modalPage.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="../../common/include.jsp"%>
		<title>
			<s:if test="(isSuccess=='update'||isSuccess=='updateNo'||isSuccess=='updateYes')">
				修改角色
			</s:if>
			<s:else>新增角色</s:else>
		</title>
		<link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css" rel="stylesheet"> 
		<script type="text/javascript" src="<c:out value="${webapp}"/>/js/SimpleTree/js/ajax.js"></script>
		<script type="text/javascript" src="<c:out value="${webapp}"/>/js/zTree/js/jquery-1.4.4.min.js"></script>
		<script type="text/javascript" src="<c:out value="${webapp}"/>/js/zTree/js/jquery.ztree.core-3.5.js"></script>
		
        <script language="javascript" type="text/javascript">
		//标识页面是否已提交
		var subed = false;
		
		function check(){
			//验证是否重复提交
			if (subed == true){
				alert("信息正在发送给服务器，请不要重复提交信息！");
				return false;
			}
			//角色名不能为空	
			if(fucCheckNull(document.getElementById("role.roleName"),"请输入角色名")==false){
			   return false;
			}
			//角色名超出指定长度,最大15字节
			if(fucCheckLength(document.getElementById("role.roleName"),30,"角色名超出指定长度,最大30字节")==false){
				return false;
			}
			//角色描述超出指定长度,最大300字节
			if(fucCheckLength(document.getElementById("role.description"),300,"角色描述超出指定长度,最大300字节")==false){
				return false;
			}

			//判断时间是否合法
			//if(fucCheckDateFormat(document.getElementById("role.startDate"),"启用日期格式不合法")==false){
				//return false;
			//}
			
			//判断时间是否合法
			//if(fucCheckDateFormat(document.getElementById("role.endDate"),"终止日期格式不合法")==false){
				//return false;
			//}
			
			
			return true;
		}
		

		
		function findOutSubmit() {
		    if(check()){
		    	if (window.ActiveXObject) {
			       var p = new ajax();
		           p.url = "<%=webapp%>/checkAuth.action?role.roleName="+encodeURI(encodeURI(document.getElementById("role.roleName").value));
		           var select=document.getElementById("role.systemId").value;
		           p.url =p.url+"&role.systemId="+select.value+'&role.roleId=<s:property value="role.roleId"/>';
		           p.onresult = function(){	        	   
		                if(this.text=="true"){
		                   document.frm.submit();	
		                }else if(this.text=="false"){
		                   alert(select.options[select.selectedIndex].text+"子系统里已经存在相同角色");
		                }
		                subed=false;
		           }
		           subed=true;
		           p.send();
		    	} else {
		    		var select = document.getElementById("role.systemId").value；
		    		$.ajax({
		                cache: true,
		                type: "POST",
		                url: "<%=webapp%>/checkAuth.action?role.roleName="+encodeURI(encodeURI(document.getElementById("role.roleName").value))+"&role.systemId="+select+'&role.roleId=<s:property value="role.roleId"/>',
		                data:$('#frm').serialize(),// 你的formid
		                async: false,
		                error: function(request) {
		                    alert("操作失败，请联系系统管理员。");
		                },
		                success: function(data) {
		                    if(data == "true"){
								document.frm.submit();
							}else{
								alert(select.options[select.selectedIndex].text+"子系统里已经存在相同角色");
							}
		                }
		            });
		   	 	}	 
		   } 					
		}	
		window.onload = function(){
			var message  = '<s:property value="resultMessages"/>';
			if(message != '')
				alert(message);
		}
	</script>
	</head>
	<body scroll="no">
<!--	     <ww:action name="saveAuth" id="saveAuth"/>  -->
		<div class="showBoxDiv">
		<form name="frm" id="frm" action="
			<s:if test="(isSuccess=='update'||isSuccess=='updateNo'||isSuccess=='updateYes')">
				updateAuth.action
			</s:if>
			<s:else>saveAuth.action</s:else>" method="post">
			<s:if test="role.roleId != null && role.roleId != '' ">
    			<input type="hidden" name="role.roleId" id="role.roleId" value="<s:property value="role.roleId"/>"/>
   			</s:if>
   			
   			<div id="editpanel" >
			<div id="editsubpanel" class="editsubpanel">
				<table id="contenttable" class="lessGrid" cellspacing="0"
					width="100%" align="center" cellpadding="0">
					<tr>
						<th colspan="4">
							<s:if test="(isSuccess=='update'||isSuccess=='updateNo'||isSuccess=='updateYes')">
								修改角色
							</s:if>
							<s:else>新增角色</s:else>
						</th>
					</tr>
					<tr>
						<td align="right" class="listbar">
							所属子系统:
						</td>
						<td>
							
							<s:if test="(isSuccess=='update'||isSuccess=='updateNo'||isSuccess=='updateYes')">
								<!-- disabled 的控件不能获取数据，所以需要用hidden -->
								<select name="role.systemId"  id="role.systemId" readOnly style="width:70%">
									<s:iterator value="sysList">
									    <s:if test="systemId==role.systemId">
											<option value="<s:property value="systemId"/>" >
												<s:property value="systemCname"/>
											</option>
										</s:if>
									</s:iterator>
								</select>
							</s:if>
							<s:else>
								<select id="role.systemId" name="role.systemId" style="width:70%">
									<s:iterator value="sysList">
										<option value="<s:property value="systemId"/>" <s:if test="systemId==role.systemId">selected</s:if>>
											<s:property value="systemCname"/>
										</option>
									</s:iterator>
								</select>
							</s:else>
							
							
							<span style="color: #FF0000">*</span>							
						</td>
					</tr>					

					<tr>
						<td align="right" class="listbar">
							角色名:
						</td>
						<td>
							<input name="role.roleName" type="text" value="<s:property value="role.roleName"/>" id="role.roleName"
							       dataType="LimitB" min="1" max="50" msg="角色名不能为空且小于50字符"  />
							<span style="color: #FF0000">*</span>
						</td>
					</tr>
					<tr>
						<td align="right" class="listbar">
							是否仅总行可用:
						</td>
						<td>
							<input name="role.isHead" type="radio" value="true"   <s:if test="role.isHead eq 'true'">checked</s:if> />是&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							<input name="role.isHead" type="radio" value="false" <s:if test="role.isHead eq 'false'||role.isHead eq null">checked</s:if> />否	
						</td>
					</tr>
					<tr>
						<td align="right" class="listbar">
							角色启用:
						</td>
						<td>
							<s:if test="role.enabled==1||role.enabled==null">
								<input name="role.enabled" type="radio" value="1"  checked />启用&nbsp;&nbsp;
								<input name="role.enabled" type="radio" value="0"  />不启用	
							</s:if>
							<s:else>
								<input name="role.enabled" type="radio" value="1"  />启用&nbsp;&nbsp;
								<input name="role.enabled" type="radio" value="0"  checked/>不启用	
							</s:else>									
						</td>
					</tr>					
<!--  
					<tr>
						<td align="right">
							启用日期:
						</td>
						<td>
							<s:if test="role.roleId != null && role.roleId != '' && role.startDate != null ">
								<input name="role.startDate" type="text" value="<s:text name="format.date"><s:param value="role.startDate"/></s:text>" class="Wdate"
								   id="role.startDate" dataType="LimitB"  onFocus="WdatePicker()"/>
							</s:if>
							<s:else>
								<input name="role.startDate" type="text"  class="Wdate" id="role.startDate" dataType="LimitB"  onFocus="WdatePicker()"/>
							</s:else>
						</td>
					</tr>
					<tr>
						<td align="right">
							终止日期:
						</td>
						<td>
							<s:if test="role.roleId != null && role.roleId != '' && role.endDate != null ">
								<input name="role.endDate" type="text" value="<s:text name="format.date"><s:param value="role.endDate"/></s:text>" class="Wdate"
								   id="role.endDate" dataType="LimitB" onFocus="WdatePicker()" />
							</s:if>
							<s:else>
								<input name="role.endDate" type="text"  class="Wdate" id="role.endDate" dataType="LimitB"  onFocus="WdatePicker()"/>
							</s:else>
						</td>
					</tr>
					-->
					<tr>
						<td align="right" class="listbar">
							角色描述:
						</td>
						<td>
							<textarea name="role.description" id="role.description"
							 		  dataType="LimitB" style="width:320px;height:60px;"><s:property value="role.description"/></textarea>										
						</td>
					</tr>	
				</table>
			</div>
			</div>
   			
   			<div id="ctrlbutton" class="ctrlbutton" style="border:0px">
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="findOutSubmit()" name="BtnSave" value="保存" id="BtnSave"/>
				<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="window.close()" name="BtnReturn" value="关闭" id="BtnReturn"/>
			</div>
			
		</form>
	</body>
</html>
