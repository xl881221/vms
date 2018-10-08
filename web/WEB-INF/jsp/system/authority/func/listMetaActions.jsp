<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<%@include file="../../../common/include.jsp"%>
		<%@include file="../../../common/publicjqueryUI-merge.jsp"%>
		<script type="text/javascript" src="<c:out value="${webapp}"/>/js/SimpleTree/js/ajax.js"></script>
		<script type="text/javascript" src="<c:out value="${webapp}"/>/js/SimpleTree/js/simpleTree.js"></script>
		<link href="<c:out value="${webapp}"/>/js/SimpleTree/css/simpleTree.css" rel="stylesheet" type="text/css">
	<script language="javascript" type="text/javascript"> 
	$(function() {
		$("[title]").tooltip();
	});
	</script>
		<style>
		label {
			display: inline-block;
			width: 5em;
		}
		
		.ui-icon {
			display: inline-block;
		}
		
		fieldset div {
			margin-bottom: 2em;
		}
		
		.ui-tooltip {
			width: 210px;
		}
		
		.readonly {
			background-color: #FFFF99;
			color: #B6B6B6;
		}
		</style>

	<script language="javascript" type="text/javascript">
		function topMenu(obj){
	    	if(obj.checked){
	    		document.forms[0].enableTopMenu.value = 'true';
	    	}else{
	    		document.forms[0].enableTopMenu.value = 'false';
	    	}
	    	document.forms[0].submit();
		}
    
	$(function() {
		$("#dialog").dialog("destroy");
		// a workaround for a flaw in the demo system (http://dev.jqueryui.com/ticket/4375), ignore!
		$("#dialog-form").dialog({
			autoOpen: false,
			height: 300,
			width: 350,
			modal: true,
			buttons: {
				'保存': function() {
					var treeNodes = PAGE_CACHE.tree.GetSelectedTreeNodes();
					if(null == treeNodes) {
			    		alert('请您先选择要操作的记录');
						return;
				    }
			    	var values = '';
			    	for(var i = 0; i < treeNodes.length; i++){
				    	var v = treeNodes[i].getAttribute("id");
				    	var level = treeNodes[i].getAttribute("levelType");
				    	if(level == "3"){
				    		values += v + ",";
					    }
				    }
					$.post("saveMenuActionRelations.action?itemcode="+$('#menuId').val(), {actions : values}, function (data) {
						if(data != null && data != ''){
							$('#detailMessage').html(data);
							$('#dialog-message').dialog({
								buttons:{
									'刷新本页': function(){
										document.forms[0].submit();
									}
								}
							})
							$('#dialog-message').dialog('open');
						}
					});
					$(this).dialog('close');
				},
				'关闭': function() {
					$("#menuId").val("");
					$(this).dialog('close');
				}
			},
			close: function() {
				//..
			}
		});
		
		$("#dialog-message").dialog({
			modal: true,
			autoOpen: false,
			buttons: {
				Ok: function() {
					$(this).dialog('close');
				}
			}
		});
		
		$("#dialog-metas").dialog({
			autoOpen: false,
			height: 750,
			width: 620,
			modal: true,
			buttons: {
				Ok: function() {
					$(this).dialog('close');
				}
			}
		});
		
		$("#dialog-tips").dialog({
			autoOpen: false,
			resizable: false,
			width:420,
			height:160,
			modal: true,
			buttons: {
				'立即初始化': function() {
					$.post("immediateInitMetaActions.action", {}, function (data) {
						if(data != null && data != ''){
							$('#detailMessage').html(data);
							$('#dialog-message').dialog('open');
						}
					});
					$(this).dialog('close');
				},
				'取消，将无法保存没有配置的Action关联': function() {
					$(this).dialog('close');
				}
			}
		});
		
		<c:if test="${tips != null}">$('#dialog-tips').dialog('open');</c:if>
		
	});
	
	function buildXMLTree(obj,itemcode) {
		$('#dialog-form').dialog('open');
		$('#menuId').val(itemcode);
		$("#simpleTree").html("加载中...");
		PAGE_CACHE = {};
		var p = new ajax();
		p.url = "viewMetaActionConfigs.action?"+new Date().getTime()+"&itemcode="+itemcode;
		p.onresult = function() {
			if(null == this.data) return;
			var t = PAGE_CACHE.tree;
			var t = new SimpleTree();
			t.container = t.$("simpleTree");
			t.iconPath = t.$("simpleTree").getAttribute("iconPath");
			t.dataStr = this.text;
			t.checkBox = true;
			t.updateBox = true;
			t.GetAnalisyNodes = function(id){
		        WaitingLayer.show();
		        var p = new ajax();
		        p.url = "viewMetaActionConfigs.action?X=" + (new Date()).getTime()+ "&itemcode="+id;
		        p.onresult = function(){
		                var data = this.text;
		                t.BuildAnalisyNodes(id,data);
		                WaitingLayer.hide();
		        }
		        p.send();
		    }
			t.init();	
			PAGE_CACHE.tree = t;	
		}
		p.send();
	}
	
	function showMetaActions(){
		$('#dialog-metas').dialog('open');
	}
	</script>
	</head>
	<body>
		<form name="frm" action="" method="post">
			<table id="tbl_tools" cellpadding="1" cellspacing="0">
				<tr>
					<td align="left" style="padding-top: 2px;padding-left: 5px;">
						&nbsp;&nbsp;&nbsp;
						<div style="margin-top:2px margin-left:5px;" type="btn" img="<c:out value="${webapp}"/>/image/button/view.gif"
						 value="基础数据维护" onClick="showMetaActions()" align="right"></div>
					</td>
					<td align="right" style="padding-left: 5px;">
						<input type="hidden" name="enableTopMenu"
							value="<s:property value="enableTopMenu"/>" />
						<input type="checkbox" onclick="topMenu(this)"
							<s:if test="enableTopMenu"> checked </s:if> />
						显示顶级菜单
					</td>
				</tr>
			</table>
			<div style="overflow: auto; width: 100%; height: 550px;"
				id="lessGridList">
				<table class="lessGrid" cellspacing="0" rules="all" border="0"
					cellpadding="0" display="none" style="border-collapse: collapse;">
					<tr class="lessGrid head">
						<th width="" style="text-align: center">
							菜单代码
						</th>
						<th width="" style="text-align: center">
							菜单名
						</th>
						<th style="text-align: center">
							对应功能
						</th>
						<th style="text-align: center">
							配置
						</th>
					</tr>
					<s:iterator value="menus" status="stuts">
						<tr align="center"
							class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
							<td>
								<s:property value="itemcode" />
							</td>
							<td <s:if test="url==null">style="font: bold;"</s:if>>
								<s:property value="itemname" />
							</td>
							<td align="left" title="<s:property value="configs"/>">
								<s:if test="configs.length()>40">
									<s:property value="configs.substring(0,40)" escape="false" />......
							</s:if>
								<s:else>
									<s:property value="configs" escape="false" />
								</s:else>
							</td>
							<td>
								<s:if test="url!=null">
									<a href="javascript:void(0);" id="buildRelation"
										onclick="buildXMLTree(this,'<s:property value="itemcode"/>')" />配置</a>
								</s:if>
							</td>
						</tr>
					</s:iterator>
				</table>
			</div>

			<div id="dialog-form" title="配置菜单与功能的关联">
				<div id="treeboxbox_tree"
					style="background: #e9f4f8; overflow: auto; width: 100%; height: 380px;">
					<div id="simpleTree"
						iconPath="<c:out value="${webapp}"/>/js/SimpleTree/images/">
						加载中...
						</div>
					</div>
			</div>
			
			<input type="hidden" value="" name="actions" id="actions">
			<input type="hidden" value="" name="menuId" id="menuId">
			
<div id="dialog-tips" title="是否初始化Action基础数据?"> 
	<p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>存在未初始化的action数据:
	<textarea name="tips" style="width:320px;height:80px;" ><c:out value="${tips}" escapeXml="false"/></textarea></p> 
</div> 

<div id="dialog-metas" title="基础数据维护(由于Action可能被引用存在外键，不提供删除功能)"> 
		<span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
		<div style="background: #e9f4f8; overflow: auto; width: 95%; height: 680px;">
		<table >
			<tr align="left">
				<th>URL</th>
				<th>描述</th>
				<th>类型</th>
				<th>修改</th>
			</tr>
			<s:iterator value="functions" status="stuts">
			<form id="form<s:property value="funcId"/>">
				<tr align="left" id="record<s:property value="funcId"/>">
					<input type="hidden" value="<s:property value="funcId"/>" name="funcId"/>
					<td class="funcURL" nowrap="nowrap"><s:property value="funcURL" /></td>
					<td class="funcDesc" nowrap="nowrap"><s:property value="funcDesc" /></td>
					<td class="funcType" nowrap="nowrap"><s:property value="funcType" /></td>
					<td class="funcAction" nowrap="nowrap">
					<a href="javascript:void(0);" onclick="new Record('record<s:property value="funcId"/>').prepare()" >修改</a>&nbsp;
					</td>
				</tr>
			</form>
			</s:iterator>
		</table>
		</div>
</div> 
<div id="dialog-message" title="提示信息"> 
	<p> 
		<span class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span> 
		<span id="detailMessage"></span>
	</p> 
</div> 
		</form>
	</body>
</html>
<script language="javascript" type="text/javascript">
	function Record(ID){
		OBJID = ID;
	}
	
	Record.prototype.prepare = function () {
		var values = $("#"+OBJID).find("td.funcURL").html();
		$("#"+OBJID).find("td.funcURL").html("<input type='text' name='funcURL' value='" + values + "' length='" + values.length + "' readonly class='readonly'/>"  );
		values = $("#"+OBJID).find("td.funcDesc").html();
		$("#"+OBJID).find("td.funcDesc").html("<input type='text' name='funcDesc' value='" + values + "' length='" + values.length + "'/>"  );
		values = $("#"+OBJID).find("td.funcType").html();
		if(values == 'UNCHECK'){
			$("#"+OBJID).find("td.funcType").html("<select name='funcType'><option value='CHECK'>CHECK</option><option value='UNCHECK' selected>UNCHECK</option> " + "</select>"  );
		} else {
			$("#"+OBJID).find("td.funcType").html("<select name='funcType'><option value='CHECK'>CHECK</option><option value='UNCHECK'>UNCHECK</option> " + "</select>"  );
		}
		$("#"+OBJID).find("td.funcAction").html("<a href='javascript:void(0);' onclick=\"new Record('" + OBJID + "').save('update');\" >保存</a>&nbsp;<a href='javascript:void(0);' onclick=\"new Record('" + OBJID + "').reset(true);\" >取消</a>");
	}
	
	Record.prototype.save = function (method) {
		var oThis = this;
		var values = $("#"+OBJID + " > tr > td.funcURL > input").html();
		funcId="",funcURL = "",funcDesc="",funcType="";
		$("#"+OBJID).find("tr,input[name='funcId']").each(function(){
			funcId = $(this).attr("value");
		});
		$("#"+OBJID).find("td.funcURL,input[name='funcURL']").each(function(){
			$(this).attr("value", $.trim($(this).attr("value")));
			funcURL = $(this).attr("value");
		});
		$("#"+OBJID).find("td.funcDesc,input[name='funcDesc']").each(function(){
			$(this).attr("value", $.trim($(this).attr("value")));
			funcDesc = $(this).attr("value");
		});
		$("#"+OBJID).find("td.funcType,select[name='funcType']").each(function(){
			funcType = $(this).attr("value");
		});
		$.post("saveMetaActions.action", {method:method,funcId:funcId,funcURL:funcURL,funcDesc:funcDesc,funcType:funcType}, function (data) {
    		$('#detailMessage').html(data);
			$('#dialog-message').dialog({
				buttons:{
				'关闭并更新': function(){
						oThis.reset();
						$('#dialog-message').dialog('close');
					}
				}
			});
			$('#dialog-message').dialog('open');
		});
	}
	
	Record.prototype.reset = function (realtive) {
		funcId="";
		$("#"+OBJID).find("tr,input[name='funcId']").each(function(){
			funcId = $(this).attr("value");
		});
		$("#"+OBJID).find("td.funcURL,input[name='funcURL']").each(function(){
			$("#"+OBJID).find("td.funcURL").html($(this).attr("value"));
		});
		$("#"+OBJID).find("td.funcDesc,input[name='funcDesc']").each(function(){
			$("#"+OBJID).find("td.funcDesc").html($(this).attr("value"));
		});
		$("#"+OBJID).find("td.funcType,select[name='funcType']").each(function(){
			$("#"+OBJID).find("td.funcType").html($(this).attr("value"));
		});
		$("#"+OBJID).find("td.funcAction").html("<a href='javascript:void(0);' onclick=\"new Record('" + OBJID + "').prepare();\" >修改</a>&nbsp;");
		if(realtive){
			$.post("getOneMetaActions.action", {funcId:funcId}, function (data) {
				var obj = eval('(' + data + ')');
				if(obj.status == 1){
					$("#"+OBJID).find("td.funcURL,input[name='funcURL']").each(function(){
						$("#"+OBJID).find("td.funcURL").html(obj.funcURL);
					});
					$("#"+OBJID).find("td.funcDesc,input[name='funcDesc']").each(function(){
						$("#"+OBJID).find("td.funcDesc").html(obj.funcDesc);
					});
					$("#"+OBJID).find("td.funcType,select[name='funcType']").each(function(){
						$("#"+OBJID).find("td.funcType").html(obj.funcType);
					});
				} else {
					$('#detailMessage').html(obj.message);
					$('#dialog-message').dialog({
						buttons:{'关闭': function(){$('#dialog-message').dialog('close');}}
					})
					$('#dialog-message').dialog('open');
				}
			});
		}
	}
	
</script>