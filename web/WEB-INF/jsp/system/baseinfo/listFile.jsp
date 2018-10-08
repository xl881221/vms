<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
    <title>用户列表 </title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
	<frameset rows="110,*" cols="*" framespacing="0" frameborder="0" border="0">
		<frame src="listFileHead.action" name="topFrame" scrolling="no" noresize frameborder="0" marginwidth="0">
		<frameset rows="*" cols="0,*" framespacing="0" frameborder="0" id="frame">
			<frame src="listFolderTree.action" name="folderTreeFrame" id="leftFrame" frameborder="0" scrolling="no" noresize>
			<frame src="listFileMain.action?fixQuery=false" name="fileSearchResultFrame" id="mainFrame" frameborder="0" scrolling="no" marginheight="0" marginwidth="0">
		</frameset>
	</frameset>
</html>
