<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="../../common/modalPage.jsp"%>
<%@include file="../../common/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link href="<c:out value="${sysTheme}"/>/css/subWindow.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="<c:out value="${webapp}"/>/js/zTree/js/jquery-1.4.4.min.js"></script>
<!-- 	<script type="text/javascript" src="<c:out value="${webapp}"/>/js/SimpleTree/js/ajax.js"></script> -->
</head>
<body topmargin="0" leftmargin="0" rightmargin="0" scroll="no" style="overflow:hidden;">
	<form name='frm' action="" method="post">
	<input type="hidden" name="uBaseConfig.systemId" value="<s:property value="uBaseConfig.systemId"/>"/>
	<input type="hidden" name="roleId" value="<s:property value="role.roleId"/>"/>
		<table id="contenttable" class="lessGrid" cellspacing="0" width="90%" align="center" cellpadding="0">
			<tr>
				<th colspan="4">资源配置</th>
			</tr>
			<tr>
				<td align="left">
					<table id="tbl_query" cellpadding="1" cellspacing="1">
						<tr>
							<td id="tdUserControl" align="left">
								<input type="hidden" id="role.roleId" value="<s:property value="role.roleId"/>" name="role.roleId"/>
								<input type="hidden" value="" name="value"  id="value"/>
								资源名:&nbsp;&nbsp;&nbsp;&nbsp;
								<s:select list="resNames" name="authResMap.resId" id="resId" listKey="resId" listValue="resName" value="authResMap.resId" onchange="changeCtn(this)" />
								&nbsp;查询:	
								<input type="text" id="txtQuery" style="width:150" onkeyup="if(event.keyCode==13){event.keyCode=9;}else{query();}" value=""/>
								<input type="text" id="hiddenBTN" style="display:none" value=""/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<div id="queryResult"></div>
		<div id="treeboxbox_tree" style="background:#e9f4f8;overflow: auto; width: 100%; height: 380px;">
			<div id="simpleTree">loading...</div>
		</div>
		<div id="ctrlbutton" class="ctrlbutton">
		<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="saveCfg()" name="BtnSave" value="保存" id="BtnSave" class="input_button"/>
		<input type="button" class="tbl_query_button" onMouseMove="this.className='tbl_query_button_on'" onMouseOut="this.className='tbl_query_button'" onclick="window.close()" name="BtnReturn" value="关闭" id="BtnReturn" class="input_button"/>
		</div>
	</form>
</body>
<script language="javascript">
<!--
		var desc_id=[];//二维数组,存放(资源名列表数)个数组对象
		
		//向二维数组中的各个元素数组重新赋值
		var setValue = function(){
			var resId = document.getElementById("resId");
			var index = resId.selectedIndex;//下拉框索引
        	var array = desc_id[index];
        		array.splice(0,array.length);//清空数组
			var obj = document.getElementById("selectList");
			for(var i=0;i<obj.options.length;i++){
			    if(obj.options[i].selected){
			        array.push(obj.options[i].value);// 重新收集选中项
			    }
			}
		}
		
		var saveCfg = function(){
			var str = "";
			for(var i=0;i<desc_id.length;i++){
				var array = desc_id[i];
				for(var j=0;j<array.length;j++){
					str=str+array[j]+";";// 从二维数组中的数组元素取值,循环赋给str
				}
			}
		    if(str == '') {
	    		alert('请您先选择要操作的记录');
				return;
		    }
	    	document.getElementById('BtnSave').disabled=true;
			document.forms[0].action="saveResByRole.action";
			document.getElementById('value').value = str;//将字符串str的值赋给value隐藏对象
			document.forms[0].submit();
		}
		
		var query = function(){
			var obj = document.getElementById("selectList");
			var query = document.getElementById("txtQuery");
	        for(i=0;i<obj.options.length;i++){
		        if(query.value != "" && obj[i].text.indexOf(query.value) > -1){
	        		obj[i].selected = true;
		        }else{
		        	obj[i].selected = false;
		        }
	        }
	        setValue();//将查询结果赋值给二维数组中的数组元素
		}
		
		var changeCtn = function(obj){
			var r1 = document.getElementById("role.roleId");
			var r2 = document.getElementById("resId");
			var date = new Date();
			var systemId = '<s:property value="uBaseConfig.systemId"/>';
			/*var p = new ajax();
			p.url = "getResTreeXMLRole.action?"+date.getTime()+"&role.roleId="+r1.value+"&authResMap.resId="+r2[r2.selectedIndex].value+"&uBaseConfig.systemId="+systemId;
			p.send();
			p.onresult = function(){
				var dataStr = this.text;
				//alert(dataStr);
				var arrmp = new Array();
				arrmp = dataStr.split(";");
				var data = "<select id=\"selectList\" multiple=\"multiple\" style=\"width:100%;height:380px\" onChange=\"setValue()\">\n";
				for(i=0;i<arrmp.length-1;i++){
					var str = new Array();
					str = arrmp[i].split("#");
					var option = "<option id=\"" + str[0] + "\" value=\""+ str[1] + "\">" + str[2] + "</option>\n";
					data = data + option;
				}
				data = data + "</select>";
				//alert(data);
				//var temp = document.getElementById("simpleTree").innerHTML;
				document.getElementById("simpleTree").innerHTML = data;//赋值完毕
				var index = r2.selectedIndex;//下拉框索引
	        	var array = desc_id[index];
				for(var i=0;i<array.length;i++){
					if(array[i].length>0){
						var id = array[i].split("$,")[1];
							id = id.replace(/\./g,'');//去除元素中的"."
						var str = "document.getElementById('"+id+"').selected=true";//取二维数组中的数组元素值,重新选中下拉框中的option
						eval(str);
					}
				}
			}*/
			$.ajax({
	            type: "Get",
	            url: "getResTreeXMLRole.action?"+date.getTime()+"&role.roleId="+r1.value+"&authResMap.resId="+r2[r2.selectedIndex].value+"&uBaseConfig.systemId="+systemId,	            
	            dataType: "text", //可以是text，如果用text，返回的结果为字符串；如果需要json格式的，可是设置为json
	            success: function (data) {
	            	var dataStr = data;
	            	var arrmp = new Array();
					var dataSelect = "<select id=\"selectList\" multiple=\"multiple\" style=\"width:100%;height:380px\" onChange=\"setValue()\">\n";
					arrmp = dataStr.split(";");
					for(i=0;i<arrmp.length-1;i++){
						var str = new Array();
						str = arrmp[i].split("#");
						var option = "<option id=\"" + str[0] + "\" value=\""+ str[1] + "\">" + str[2] + "</option>\n";
						dataSelect = dataSelect + option;
					}
					dataSelect = dataSelect + "</select>";
					//alert(dataSelect);
					document.getElementById("simpleTree").innerHTML = dataSelect;//赋值完毕
					var index = r2.selectedIndex;//下拉框索引
		        	var array = desc_id[index];
					for(var i=0;i<array.length;i++){
						if(array[i].length>0){
							var id = array[i].split("$,")[1];
								id = id.replace(/\./g,'');//去除元素中的"."
							var str = "document.getElementById('"+id+"').selected=true";//取二维数组中的数组元素值,重新选中下拉框中的option
							eval(str);
						}
					}
	            },
	            error: function (msg) {
	                alert(" 数据加载失败！" + msg);
	            }
	        });
		}
		
		//页面初始化方法
		function init(){
		    var resId = document.getElementById("resId");
			for (i=0;i<resId.length;i++) {
				if(null==resId.options[i].text || resId.options[i].text.replace(/' '/g,'').length>0){
					var value = resId.options[i].value.replace(/-/g,'');
					var str01 = "var resId_"+value+"=[];";
					var str02 = "desc_id.push(resId_"+value+");";
					eval(str01);//创建数组对象
					eval(str02);//将创建的对象放到数组desc_id中,desc_id是一个二维数组
				}
		    }
			changeCtn();
		}
		window.onload = init;//加载页面初始化方法
-->
</script>
</html>
