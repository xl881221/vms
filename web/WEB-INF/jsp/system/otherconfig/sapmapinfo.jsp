<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="../../common/include.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD html 4.0 Transitional//EN" >
<html>
	<head>
		<title>总账配置维护</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<script type="text/javascript">
			function findOutSubmit(){
				sapMapInfo.submit();
			}
			function submitDelete(){
				//删除方法
				beforeDelete('<c:out value="${webapp}"/>/deleteSapMapInfo.action', 'sapMapInfoIds')
			}
		</script>
	</head>

	<body>
		<form id="sapMapInfo" method="post"
			action="<c:out value='${webapp}'/>/listSapMapInfo.action" id="sapMapInfo">
			<table id="tbl_main" cellpadding="0" cellspacing="0">
				<tr>
					<td align="left">
						<table id="tbl_current_status">
							<tr>
								<td>
									<img src="<c:out value="${sysTheme}"/>/img/jes/icon/home.png" />
									<span class="current_status_menu">当前位置：</span>
									<span class="current_status_submenu">指标配置<span
										class="actionIcon">-&gt;</span>总账配置</span>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td align="center">
						<table id="tbl_query" cellpadding="1" cellspacing="0">
							<tr>
								<td align="left">
									指标号：
									<input name="sapMapInfoQuery.dpvId" type="text"
										value="<s:property value="sapMapInfoQuery.dpvId" />"
										id="sapMapInfoQuery.dpvId" style="width: 100;" />
									&nbsp;&nbsp;指标名称：
									<input name="sapMapInfoQuery.dpvName" type="text"
										value="<s:property value="sapMapInfoQuery.dpvName" />"
										id="sapMapInfoQuery.dpvName" style="width: 100;" />
									&nbsp;&nbsp; 科目号：
									<input name="sapMapInfoQuery.acciNo" type="text"
										value="<s:property value="sapMapInfoQuery.acciNo" />"
										id="sapMapInfoQuery.acciNo" style="width: 100;" />
									&nbsp;&nbsp; 产品号：
									<input name="sapMapInfoQuery.prodId" type="text"
										value="<s:property value="sapMapInfoQuery.prodId" />"
										id="sapMapInfoQuery.prodId" style="width: 100;" />
								</td>
								<td align="right">
									<div type="btn"
										img="<c:out value="${webapp}"/>/image/button/search.gif"
										onclick="findOutSubmit()" name="BtnView" value="查询"
										id="BtnView"></div>
								</td>
								
							</tr>
							
			</table>
			</td>
		</tr>
		<tr>
								<td align="center">
								<table id="tbl_tools" cellpadding="1" cellspacing="1">
									<tr>
									<td align="left">&nbsp;			
						            	<div type="btn" img="<c:out value="${webapp}"/>/image/button/add3.gif"  onclick="OpenSubSystemWindow('loadSapMapInfo.action',true)" name="BtnReturn" value="新增" id="BtnReturn"></div>
	 									<div style="margin-left:5px" type="btn" img="<c:out value="${webapp}"/>/image/button/delete.gif" onclick="submitDelete()" name="BtnReturn" value="删除" id="BtnReturn"></div>          
						            </td>
									</tr>
								</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
			<div style="overflow: auto; width: 100%; height: 390px;" id="lessGridList">
	       <table  class="lessGrid" cellspacing="0" rules="all" border="0"  cellpadding="0" display="none" style="border-collapse: collapse;">
	           <tr class="lessGrid head">
	           		<th width="4%">
							<input id="CheckAll" style="width: 13px; height: 13px;"
								type="checkbox" onClick="checkAll(this,'sapMapInfoIds')" />
					</th>
					<th style="text-align:center">指标号</th>
					<th style="text-align:center">指标名称</th>
					<th style="text-align:center">是否取减法</th>
					<th style="text-align:center">科目号</th>
					<th style="text-align:center">产品号</th>
					<th style="text-align:center">钆差组别</th>
					<th style="text-align:center">数据类别</th>
					<th style="text-align:center">大于或小于零</th>
					<th width="5%" style="text-align:center">修改</th>
	           </tr>
	           <s:iterator value="paginationList.recordList" id="iList" status="stuts">
	           		<tr align="center" class="<s:if test="#stuts.odd==true">lessGrid rowA</s:if><s:else>lessGrid rowB</s:else>">
	           			<td>
							<input type="checkbox"
									style="width: 13px; height: 13px;" name="sapMapInfoIds"
									value="<s:property value="#iList.id"/>" />
						</td>
						<td><s:property value="#iList.dpvId"/></td>
						<td><s:property value="#iList.dpvName"/></td>
						<td><s:if test='#iList.isDel=="Y".toString()'>是</s:if><s:else>否</s:else></td>
						<td><s:property value="#iList.acciNo"/></td>
						<td><s:property value="#iList.prodId"/></td>
						<td><s:property value="#iList.drFlag"/></td>
						<td><s:if test="#iList.dataType=='BAL'">余额</s:if>
							<s:elseif test="#iList.dataType=='DBAL'">借方余额</s:elseif>
							<s:elseif test="#iList.dataType=='CBAL'">贷方余额</s:elseif>
							<s:elseif test="#iList.dataType=='DAML'">借方月累计发生额</s:elseif>
							<s:elseif test="#iList.dataType=='CAML'">贷方月累计发生额</s:elseif>
							<s:elseif test="#iList.dataType=='YDAML'">借方年累计发生额</s:elseif>
							<s:elseif test="#iList.dataType=='YCAML'">贷方年累计发生额</s:elseif>
						</td>
						<td><s:if test="#iList.posFlag=='Y'.toString()">结果要求大于零</s:if><s:elseif test="#iList.posFlag=='N'.toString()">结果要求小于零</s:elseif><s:elseif test="#iList.posFlag=='-'.toString()">对结果无要求</s:elseif></td>
						<td align="center"><a href="#" onclick="OpenSubSystemWindow('loadSapMapInfo.action?&sapMapInfo.id=<s:property value="#iList.id" />',true)"><img src="<c:out value="${sysTheme}"/>/img/jes/icon/edit.png" alt="修改" style="border-width: 0px;"/></a></td>
					</tr>
	           </s:iterator>
	       </table>
	    </div>
	    <div id="anpBoud" align="Right" style="width:100%;vlign=top;">
	        <table width="100%" cellspacing="0" border="0">
	            <tr>
	                <td align="left"></td>
	                <td align="right"><s:component template="pagediv"/></td>
	            </tr>
	        </table>
	    </div>
		</form>
	</body>
</html>