<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <%@include file="../../common/include.jsp"%>
</head>
<body onmousemove="MM(event)" onmouseout="MO(event)">
<form name="frm" action="<c:out value='${webapp}'/>/createUserRights.action" method="get">
	<table id="tbl_main" cellpadding="0" cellspacing="0">
		<tr>
			<td align="left">
			<table id="tbl_current_status">
				<tr>
					<td>
						<img src="<c:out value='${sysTheme}'/>/img/jes/icon/home.png" /> <span
						class="current_status_menu">当前位置：</span> <span
						class="current_status_submenu">基础信息管理=>查看角色权限</span>
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td align="center">
			<table id="tbl_tools" cellpadding="1" cellspacing="1">
				<tr>
					<td align="left">
	                <input type="button" name="BtnSave" value="关&nbsp;闭" id="BtnSave" onclick="window.close()"
	                    />
	            </td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
    <div class="editblock">
        <table cellspacing="0" width="95%" align="center" >
        	 <tr>
                <th colspan="4" style="background:#eff7fb;;text-align:center;border-bottom:1px solid #94c6e3;">功能权限设置</th>
             </tr>
            <tr>
                <th width="15%" ></th>
              <td width="85%" ><s:property value="%{role.roleName}"/> </td>
          </tr>       	
          <tr>
           	<th>权限资源</th>
           	<td>       
				<s:iterator value="authRoleResList">
					<input type="checkbox" name="resId" value="<s:property value="resId"/>" id="user.ids-1" checked="checked" disabled="disabled"/>
					<label for="user.ids-1" class="checkboxLabel"><s:property value="resObject"/> </label>
				</s:iterator>
           	</td> 	
          </tr>
        </table>
    </div>
</form>
</body>
</html>
