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
				var selectObj=parent.frames["headFrame"].document.getElementById('systemId');
	            var systemName=selectObj.options[selectObj.selectedIndex].text;
				parent.frames["InstMailResultFrame"].location.href="<c:out value='${webapp}'/>/system/authority/instMailMain.action?bankId="+bankId+"&systemId="+systemId+"&systemName="+systemName+"&time="+new Date().getTime();	
	        }
	    }
	    
	    function down(){
	        if(document.getElementById('CheckAll').checked){
	           if(window.confirm("是否将所选人员置为该机构邮件模块的接收邮箱！")){
		            document.frm.action="<c:out value='${webapp}'/>/system/authority/instMailUser.action?down=true&checkAll=true";
					document.frm.method="post";
					document.frm.submit();
	           }
	        }else{
		        document.frm.action="<c:out value='${webapp}'/>/system/authority/instMailUser.action?down=true";
				document.frm.method="post";
				document.frm.submit();
	   		}
	    }
	    
		function findOutSubmit(){
			document.frm.action="<c:out value='${webapp}'/>/system/authority/instMailUser.action";
			document.frm.method="post";
			document.frm.submit();
		}
	</script>
</head>
<body>
<span width="100px">人员列表:</span>
<br>

<div>
    <form id='frm' name="frm" action="<c:out value='${webapp}'/>/system/authority/instMailUser.action" method="post">
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
				<td><input id="systemName" name ="systemName" type ="hidden" value="<s:property value='systemName'/>" /></td>	
			</tr>
        </table> 
	 	<div style="overflow: auto; width: 100%; height: 200px;" id="lessGridList">
	       <table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" display="none" style="border-collapse: collapse;">
	            <tr class="lessGrid head">
	                <th width="3%"><input id="CheckAll" style="width:13px;height:13px;" type="checkbox" onClick="checkAll(this,'ids')" />全选</th>
					<th width="20%"  style="text-align:center" nowrap>用户登录名</th>
					<th  style="text-align:center">用户中文名</th>
					<th  style="text-align:center">所属机构</th>
					<th  style="text-align:center">邮箱地址</th>
	           </tr>
	           <s:iterator value="userList" id="s" status="stuts">
	           		<tr class="lessGrid" align="center"  >
	           		    <td>
	           		    	<input type="checkbox" style="width:13px;height:13px;" name="ids" value="<s:property value="#s.userId"/>" />
	           		    </td>
	           		    <td><s:property value="#s.userId"/></td>
						<td><s:property value="#s.userCname"/></td>
						<td><s:property value="#s.instName"/></td>
						<td><s:property value="#s.email"/></td>
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
</div>
</body>
</html>