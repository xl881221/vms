<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <title>配置机构邮箱信息 </title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
</head>
	<frameset rows="56,*" cols="*" framespacing="0" frameborder="0" border="0">
		<frame src="instMailHead.action" name="headFrame" scrolling="no" noresize frameborder="0" marginwidth="0">
		<frameset rows="*" cols="200,*" framespacing="0" frameborder="0" id="frame">
			<frame src="instMailTree.action" name="InstTreeFrame" id="leftFrame" frameborder="0" scrolling="no" noresize>
			<frameset rows="240,*" cols="*" framespacing="0" frameborder="0" border="0">
				<frame src="instMailUser.action" name="InstMailUserResultFrame" id="topFrame" frameborder="0" scrolling="no" noresize>
				<frameset  framespacing="0" frameborder="0" border="0">
					<frame src="instMailMain.action" name="InstMailResultFrame" id="footFrame" frameborder="0" scrolling="no" noresize>
				</frameset> 	
			</frameset>    	
		</frameset>
	</frameset>
</html>