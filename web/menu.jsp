<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="java.util.*"%>
<%@page import="org.springframework.jdbc.core.JdbcTemplate"%>

<% 
WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletConfig().getServletContext());
DataSource ds=(DataSource)wac.getBean("dataSource");
JdbcTemplate jt=new JdbcTemplate(ds);
List menus=jt.queryForList("SELECT *  FROM (SELECT         U.SYSTEM_ID,U.SYSTEM_CNAME,M.MENU_ID,M.MENU_NAME,         CASE WHEN LENGTH(M.MENU_ID)=4 THEN   'S'||U.SYSTEM_ID||'_'|| 'S'||U.SYSTEM_ID||'M'||M.MENU_ID ELSE 'S'||U.SYSTEM_ID||'M'||REPLACE(MENU_ID,'.', '_S'||U.SYSTEM_ID||'M2$') END               AS MENUSID ,                                trim(M.MENU_NAME) AS MN       ,                 M.ORDER_NUM    AS ORDER_NUM             FROM U_BASE_CONFIG U               JOIN U_BASE_MENU M ON U.SYSTEM_ID=M.SYSTEM_ID                                                               UNION ALL                       SELECT U2.SYSTEM_ID,U2.SYSTEM_CNAME,'' AS MENU_ID ,'' AS MENU_NAME ,'P000000_S'||U2.SYSTEM_ID AS MENUSID,U2.SYSTEM_CNAME AS MN,0     AS ORDER_NUM              FROM U_BASE_CONFIG U2               ) AAA      ORDER BY AAA.SYSTEM_ID , substr( AAA.MENU_ID,1,4),length(AAA.MENU_ID) asc,AAA.ORDER_NUM,AAA.MENU_ID ");


 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
	<title>系统菜单生成器</title>
   
	<link rel="stylesheet" type="text/css" href="js/jquery/!style.css" />
	<script type="text/javascript" src="js/jquery/scripts/shCore.js"></script>
	<script type="text/javascript" src="js/jquery/scripts/shBrushSql.js"></script>
	<link type="text/css" rel="stylesheet" href="js/jquery/styles/shCore.css"/>
	<link type="text/css" rel="stylesheet" href="js/jquery/styles/shThemeDefault.css"/>
	<script type="text/javascript">
		SyntaxHighlighter.config.clipboardSwf = 'js/jquery/scripts/clipboard.swf';
		SyntaxHighlighter.all();
	</script>
	
	
	<script type="text/javascript" src="js/MzTreeView12.js"></script>
	<script type="text/javascript"  src="js/jquery/jquery_1.42.js"></script>



<style>
body {font:normal 12px 宋体}
a.MzTreeview /* TreeView 链接的基本样式 */ { cursor: hand; color: #000080; margin-top: 5px; padding: 2 1 0 2; text-decoration: none; }
.MzTreeview a.select /* TreeView 链接被选中时的样式 */ { color: highlighttext; background-color: highlight; }
#kkk input {
vertical-align:middle;
}
.MzTreeViewRow {border:none;width:500px;padding:0px;margin:0px;border-collapse:collapse}
.MzTreeViewCell0 {border-bottom:1px solid #CCCCCC;padding:0px;margin:0px;}
.MzTreeViewCell1 {border-bottom:1px solid #CCCCCC;border-left:1px solid #CCCCCC;width:200px;padding:0px;margin:0px;}
</style>
</head>
<body  class="nosidebar">
<div id="container">
		<div id="content" class="documentation">
		<h1>系统平台菜单脚本生成器</h1>
		<h2 id="demos">说明</h2>
<div class="panel">

<h3>菜单脚本，选择：<input type="radio" name="full"  checked />增量生成<input type="radio" name="full" />全量生成,然后从下表中选择菜单，完成后点击<input type="button" name="gen" value="下载" onclick="downSql();"/>&nbsp;<input type="button" name="view" value="预览" onclick="showView();"/></h3>
<div id="kkk"  class="demo" >


</div>
<div id="kksk" class="code" >

<pre class="brush: sql;" >

</pre>
</div>
</div>
</div>



	<script language="javascript" type="text/javascript">

	window.tree = new MzTreeView("tree");

	tree.setIconPath("image/tree/"); //可用相对路径
tree.N["0_P000000"] = "ctrl:sel;mid:;checked:0;T;"
		<% 
		Iterator it=menus.iterator();
		while(it.hasNext()){
				Map m=(Map)it.next();
				String id="";
				if(m.get("MENU_ID")!=null){
					id=m.get("SYSTEM_ID").toString()+"_"+m.get("MENU_ID").toString();
				}
				out.println("tree.N[\""+m.get("MENUSID").toString()+"\"] = \"mid:"+id+";ctrl:sel;checked:0;T:"+m.get("MN").toString().replaceAll("\n", "").replaceAll("\r", "")+";url:'javascrpit:return false;'\"");
			}
		%>	
	tree.setURL("#");
	tree.wordLine = false;
	tree.setTarget("main");
	document.getElementById("kkk").innerHTML=tree.toString();
//	tree.expandAll();

	function showView()
	{
		var ms="";
		ms=getSelectNode();
		getSql(ms,document.getElementsByName( "full")[0].checked?"0":"1");
	
	}
	
	function downSql(){
		
		var ms="";
		ms=getSelectNode();
		
		var t="";
		t=document.getElementsByName( "full")[0].checked?"0":"1";
		
		getSql(ms,t);
		
		if(document.all && window.external){
			
			window.location="menugen.jsp?sms="+ms+"&gentype="+t+"&download=1";
		}else{
			window.open("menugen.jsp?sms="+ms+"&gentype="+t+"&download=1");
		}
		
		
	
	}
	function getSelectNode(){
		var menus="";
		var es=document.getElementsByName("sel");
		var out="";
		var id;
		
		for(var i=0;i<es.length;i++)
		{
			if (es[i].checked) 
			{
					id=es[i].id.substr(13)*1;
					var node;
					node=tree.node[id];
					
					if(node.mid&&node.mid!=""){		
						
						menus+=menus==""?node.mid:","+node.mid;	
					}
			}
		}
		return menus;
	}
	
	function getSql(ids,gentype){
			$.ajax({url: 'menugen.jsp',
			type: 'POST',
			data:{sms:ids,gentype:gentype},
			dataType: 'html',
			timeout: 1000,
			error: function(){alert('Error loading menu');},
			success: function(result){
				var sqlpanel="<pre class=\"brush: sql;\" >";
				sqlpanel+=result;
				sqlpanel+="</pre>";
				document.getElementById("kksk").innerHTML=sqlpanel;
				SyntaxHighlighter.highlight();
			} 
			}); 
	
	}
	//-->
	</script>



	</body>
</html>

