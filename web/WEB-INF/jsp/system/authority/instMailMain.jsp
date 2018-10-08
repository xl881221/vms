<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <%@include file="../../common/include.jsp"%>
	<link href="<c:out value="${sysTheme}"/>/css/main.css" rel="stylesheet" type="text/css" />
	<script language="javascript" type="text/javascript">
	    window.onload=reshPag;
	    function reshPag(){
	        var reshFlag='<%=request.getAttribute("reshFlag")%>'; 
	        if(reshFlag=='1'){
		    	var bankId=document.getElementById('bankId').value;
				var systemId=document.getElementById('systemId').value;
			    parent.frames["InstMailUserResultFrame"].location.href="<c:out value='${webapp}'/>/system/authority/instMailUser.action?bankId="+bankId + "&systemId="+systemId+"&time="+new Date().getTime();
	    	}
	    }
	    function up(){
	        var bankValue=parent.frames["InstTreeFrame"].document.getElementById('instId').value;
	        if(bankValue==null||bankValue==""){
			   alert("请先选择设置邮箱的机构！");
			   return;
	        }
		     if(document.getElementById('CheckAll').checked){
		            if(window.confirm("是否删除所选已有收件人信息！")){
			            document.form.action="<c:out value='${webapp}'/>/system/authority/instMailMain.action?up=true&checkAll=true";
						document.form.method="post";
						document.form.submit();
		            }
		     }else{
		        var ids="";
	            var array=document.getElementsByName("ids");
			    for(var i=0;i<array.length;i++){
			        if(array[i].checked){
			           ids+=","+array[i].value;
			        }
			    }
			    if(ids==""){
			       alert("请选择要删除的用户");
			       return;
			    }
		     	document.form.action="<c:out value='${webapp}'/>/system/authority/instMailMain.action?up=true";
				document.form.method="post";
				document.form.submit();
		     }  
	    }
	    function down(){
	        var bankValue=parent.frames["InstTreeFrame"].document.getElementById('instId').value;
	        if(bankValue==null||bankValue==""){
			   alert("请先选择设置邮箱的机构！");
			   return;
	        }
	        var ids="";
            var array=parent.frames["InstMailUserResultFrame"].document.getElementsByName("ids");
		    for(var i=0;i<array.length;i++){
		        if(array[i].checked){
		           ids+=","+array[i].value;
		        }
		    }
		    if(ids==""){
		       alert("请选择要添加的用户");
		       return;
		    }
	    	parent.frames["InstMailUserResultFrame"].down();
	    	document.getElementById('add').disabled=true;
	    }
	    
	    function findOutSubmit(){
			document.form.action="<c:out value='${webapp}'/>/system/authority/instMailMain.action";
			document.form.method="post";
			document.form.submit();
		}
		
	</script>
</head>
<body>
<table>
	<tr>
	    <td width="100px" align="left">
			    已有收件人信息:
	    </td>
		<td align="right" width="60px">
			<div   id='add' type="btn" img="<c:out value="${webapp}"/>/image/button/down.gif" onclick="down()" value="添加" title="设置机构模块邮箱,点击数据下移"></div> 
		</td>	
		<td align="right" width="60px">
			<div   id='delete' type="btn" img="<c:out value="${webapp}"/>/image/button/upper.gif" onclick="up()" value="删除" title="取消设置机构模块邮箱,点击数据上移"></div> 
		</td>
	</tr>
</table> 
<form name="form" action="<c:out value='${webapp}'/>/system/authority/instMailMain.action" method="post">
      <table id="tbl_query" cellpadding="1" cellspacing="0">
	<tr>
		<td width="100px" align="left">用户登录名:</td>
		<td><input name="user.userEname" id="user.userEname" type="text"  value ="<s:property value="user.userEname"/>" style="width: 90;" /></td>
		<td align="right" width="60px">
			<div style="width:60px" type="btn" img="<c:out value="${webapp}"/>/image/button/search.gif" onclick="findOutSubmit()" name="BtnView" value="查询" id="BtnView">
			</div>
		</td>
		<td><input type="hidden" name="bankId" id="bankId" value="<s:property value="bankId"/>" /></td>
		<td><input id="systemId" name ="systemId" type ="hidden" value="<s:property value='systemId'/>" /></td>		
	</tr>
    </table> 
	<div style="overflow: auto; width: 100%; height: 170px;" id="lessGridList">
      <table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" display="none" style="border-collapse: collapse;">
           <tr class="lessGrid head">
               <th width="3%"><input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'ids')" />全选</th>
			<th  style="text-align:center">用户登录名</th>
			<th  style="text-align:center">用户中文名</th>
			<th  style="text-align:center">邮箱配置机构名称</th>
			<th  style="text-align:center">模块名称</th>
          </tr>
          <s:iterator value="selectedEmailAddrs" id="s" status="stuts">
          		<tr class="lessGrid" align="center"  >
          		    <td>
          		    	<input type="checkbox" style="width:13px;height:13px;" name="ids" value="<s:property value="#s.userId"/>" />
          		    </td>
				<td><s:property value="#s.userId"/></td>
				<td><s:property value="#s.userCname"/></td>
				<td><s:property value="#s.bankName"/></td>
				<td><s:property value="#s.systemName"/></td>
			</tr>
          </s:iterator>
      </table>
      <div id="anpBoud" align="Right" style="width:100%;vlign=top;">
       <table width="100%" cellspacing="0" border="0">
            <tr>
                <td align="left"></td>
                <td align="right">
                	<s:component template="pagediv"/>
                </td>
            </tr>
       </table>
      </div>
    </div>
</form>

   
</body>
</html>