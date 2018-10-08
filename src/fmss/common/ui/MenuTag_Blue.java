package fmss.common.ui;

import java.util.List;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import fmss.dao.entity.MenuDO;

import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

public class MenuTag_Blue extends TagSupport{

	private static final long serialVersionUID = 5314979962850592049L;
	private String layerWidth;// 菜单宽度
	private String language;// 语言
	private String titleHeight;// 标题高度
	private String contentHeight;// 卫菜单容器高度
	private String menuCss;// 菜单样式路径
	private List menuList;// 菜单信息存储表
	private Object list;
	private String titleUnClickCssClass;
	private String titleOnClickCssClass;
	private String contentCssClass;
	private String themeImagesPath;

	public void setThemeImagesPath(String themeImagesPath){
		this.themeImagesPath = themeImagesPath;
	}

	public void setTitleUnClickCssClass(String titleUnClickCssClass){
		this.titleUnClickCssClass = titleUnClickCssClass;
	}

	public void setTitleOnClickCssClass(String titleOnClickCssClass){
		this.titleOnClickCssClass = titleOnClickCssClass;
	}

	public void setContentCssClass(String contentCssClass){
		this.contentCssClass = contentCssClass;
	}

	public List getMenuList(){
		return menuList;
	}

	public void setMenuList(List menuList){
		this.menuList = menuList;
	}

	public String getLayerWidth(){
		return layerWidth;
	}

	public void setLayerWidth(String layerWidth){
		this.layerWidth = layerWidth;
	}

	public String getTitleHeight(){
		return titleHeight;
	}

	public void setTitleHeight(String titleHeight){
		this.titleHeight = titleHeight;
	}

	public String getContentHeight(){
		return contentHeight;
	}

	public void setContentHeight(String contentHeight){
		this.contentHeight = contentHeight;
	}

	public String getMenuCss(){
		return menuCss;
	}

	public void setMenuCss(String menuCss){
		this.menuCss = menuCss;
	}

	public int doStartTag(){
		JspWriter out = pageContext.getOut();
		try{
			// 该类修改为webloigc8下不支持jsp2.0时，jstl不能直接解析el表达式，
			// 此时需要辅助工具类改写，不能直接使用TagSupport的out方法
			Object menuCss = ExpressionEvaluatorManager.evaluate("menuCss",
					this.menuCss, Object.class, this, pageContext);
			Object themeImagesPath = ExpressionEvaluatorManager.evaluate(
					"themeImagesPath", this.themeImagesPath, Object.class,
					this, pageContext);
			if(menuCss != null){
				this.menuCss = (String) menuCss;
			}
			if(themeImagesPath != null){
				this.themeImagesPath = (String) themeImagesPath;
				this.themeImagesPath = this.themeImagesPath.replaceAll("/img", "img") ;
			}
			// 只这三项需要重新接收
			out.print(getStartScript() + getMenuString() + getEndScript());
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return SKIP_BODY;
	}

	private String getStartScript(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("<base target=\"mainFrame\">\n");
		buffer.append("<link href=" + menuCss
				+ " rel='stylesheet' type=text/css>\n");
		// 取出栏目条数
		int itemCount = menuList.size();
		buffer.append("<SCRIPT language=JavaScript>\n");
		buffer.append("\tvar layerTop=0; //菜单顶边距 \n");
		buffer.append("\tvar layerLeft=0; //菜单左边距 \n");
		buffer.append("\tvar layerWidth=" + layerWidth + "; //菜单总宽 \n");
		buffer.append("\tvar titleHeight=" + titleHeight + "; //标题栏高度 \n");
		buffer.append("\tvar contentHeight=" + contentHeight + "; //内容区高度 \n");
		buffer.append("\tvar itemCount=" + itemCount + "; //栏目条数 \n");
		buffer.append("\tvar stepNo=10;\n");
		buffer.append("\tvar itemNo=0;runtimes=0; \n");
		buffer.append("\tcontentHeight = (itemCount > 1 ? contentHeight : "
				+ "(parseInt(document.body.offsetHeight) - titleHeight));\n");
		buffer.append("\tdocument.write('<span id=itemsLayer style=\""
				+ "position:absolute;overflow:hidden;border:0px "
				+ "solid #F1F1F1;left:'+layerLeft+';top:'+layerTop+'px;"
				+ "width:'+layerWidth+';\">');\n");
		buffer.append("\tfunction addItem(itemTitle,itemContent){\n");
//		buffer.append("\t\titemHTML='<div class=\"aaa\" id=item'+itemNo+' itemIndex='"
//				+ "+itemNo+' style=\"position:relative;left:0;top:'"
//				+ "+(-contentHeight*itemNo)+'px;width:'+layerWidth+'px;\">"
		buffer.append("\t\titemHTML='<div class=\"aaa\" id=item'+itemNo+' itemIndex='"
				+ "+itemNo+' style=\"width:'+layerWidth+'px;\">"
				+ "<table width=100% cellspacing=0 cellpadding=0 ><tr>"
				+ "<td id=title'+itemNo+' height='+titleHeight+'");
		//buffer.append("\t\tonclick=changeItem('+itemNo+') class="
		buffer.append("\t\t class="
				+ this.titleUnClickCssClass
				+ " >'+itemTitle+'</td></tr>'+ '<tr>"
				+ "<td class=" + this.contentCssClass//height='+contentHeight+' 
				+ " >'+itemContent+'</td></tr></table></div>';\n");
		buffer.append("\t\tdocument.write(itemHTML);\n");
		buffer.append("\t\titemNo++;\n");
		buffer.append("\t}\n");
		buffer.append("</SCRIPT>\n");
		return buffer.toString();
	}

	private String getMenuString(){
		StringBuffer strReturn = new StringBuffer(
				"<SCRIPT language=JavaScript>\n");
		for(int i = 0; i < menuList.size(); i++){
			MenuDO menu = (MenuDO) menuList.get(i);
			if(menu.getSubMenuList().size() > 0){// 如果无子项 则不显示
				StringBuffer strText = new StringBuffer("\taddItem('");
				if(menu.getImgsrc() != null && !"".equals(menu.getImgsrc())){
					strText.append("");
				}
				String menu_text=language.equals("e")?(menu.getMenu_ename()!=null?menu.getMenu_ename().trim():""):menu.getItemname().trim();
				strText.append("&nbsp;<span style=\"display:inline;white-space:nowrap;\" title=\""+menu_text+"\" >" +menu_text+"</span>'");
				List subMenuList = menu.getSubMenuList();
				strText.append(",'<div class=\"dropmenu\" style=\"display:none;width:'+layerWidth+'px;overflow:auto;height:'+contentHeight+'px;\">");//
//				strText.append(",'<div class=\"dropmenu\" style=\"overflow:auto; height:'300px';\">");
				int k = 0;
				int m = 0;
				for(int j = 0; j < subMenuList.size(); j++){
					MenuDO submenu = (MenuDO) subMenuList.get(j);
					m++;
					if(k++ != 0)
					if(submenu.getImgsrc() != null
							&& !"".equals(submenu.getImgsrc())){
						strText.append("");
					}else{
						strText.append("");
					}
					strText.append("<a subMenuId="+submenu.getItemcode()+" href=" + submenu.getUrl());
					if(submenu.getTarget() != null
							&& !"".equals(submenu.getTarget())){
						strText.append(" target =\"" + submenu.getTarget()
								+ "\"");
					}
					String submenu_text=language.equals("e")?(submenu.getMenu_ename()!=null?submenu.getMenu_ename().trim():""):submenu.getItemname().trim();
					strText.append(">" +submenu_text+ "</a>");
				}
				strText.append("</div>');\n");
				if(m > 0) // 如果无子项 则不显示 
					strReturn.append(strText);
			}
		}
		strReturn.append("\n</SCRIPT>");
		return strReturn.toString();
	}

	private String getEndScript(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("<SCRIPT language=JavaScript>\n");
		buffer.append("\tdocument.write('</span>');\n");
		
		//末尾的6是为了在ie中能够完整显示界面内容加上的
//		buffer.append("\tdocument.all.itemsLayer.style.height="
//				+ "eval(itemNo*titleHeight+contentHeight);\n");
/**		buffer.append("\tvar hightValueStr='height:'+eval(itemNo*titleHeight+contentHeight)+'px';\n");
		buffer.append("\tvar itemsLayerStyle=document.getElementById('itemsLayer').getAttribute('style')+';'+hightValueStr;\n");
		buffer.append("\tdocument.getElementById('itemsLayer').setAttribute('style', itemsLayerStyle);\n");*/
		//测试用alert
//		buffer.append("\talert(document.getElementById('itemsLayer').);\n");
		
		
		buffer.append("	$(function(){");
		buffer.append("	$('.dropmenu:eq(0)').show();$('.aaa').find('.titleStyle_1').eq(0).removeClass('.titleStyle_1');$('.aaa').find('.titleStyle_1').eq(0).addClass('titleStyle_2');");
		buffer.append("	$('td[id^=title]').click(function(){");
		buffer.append(" $('td[id^=title]').removeClass('titleStyle_2');$(this).addClass('titleStyle_2');");
//		buffer.append("	var index = $(this).index();");
		buffer.append(" $('.dropmenu').hide(); $(this).parents('.aaa').find('.dropmenu').show();");
		buffer.append("});");
		buffer.append("$('.dropmenu a').click(function(){$('.aaa a').css('color','#999');$(this).css('color','#02A2AA')});");
		buffer.append("});");
		
		
		
		
		
		
		
		//$('title'+(index-1)).attr('style',titleStyle_2);
/**		buffer.append("\ttoItemIndex=itemNo-1;onItemIndex=itemNo-1;\n");
		buffer.append("\tfunction changeItem(clickItemIndex){\n");
		buffer.append("\t\ttoItemIndex=clickItemIndex;\n");
		//buffer.append("\t\talert('toItemIndex:'+toItemIndex+'&clickItemIndex:'+clickItemIndex);\n");
		buffer.append("\t\tif(toItemIndex==onItemIndex) return;\n");
		buffer.append("\t\tdocument.getElementById('title'+toItemIndex)"
				+ ".className='" + this.titleOnClickCssClass + "';\n");
		buffer.append("\t\tdocument.getElementById('title'+onItemIndex)"
				+ ".className='" + this.titleUnClickCssClass + "';\n");
		buffer.append("\t\tif(toItemIndex-onItemIndex>0) moveUp(); "
				+ "\t\telse moveDown();\n");
		buffer.append("\t\truntimes++;\n");
		buffer.append("\t\tif(runtimes>=stepNo){\n");
		buffer.append("\t\t\tonItemIndex=toItemIndex;\n");
		buffer.append("\t\t\truntimes=0;\n\t\t} else\n");
		buffer.append("\t\t\tsetTimeout('changeItem(toItemIndex)',10);\n\t}\n");
		/**buffer.append("\tfunction moveUp(){\n");
		buffer.append("\t\tfor(i=onItemIndex+1;i<=toItemIndex;i++){\n");
		buffer.append("\t\t\ttry{\n");
//		buffer.append("\t\t\t\talert(document.getElementById('item'+i).style.top);\n");
//		buffer.append("\t\t\t\tvar itemTop = document.getElementById('item'+i).style.top;\n");
		buffer.append("\t\t\t\teval('document.getElementById(\"item\"+i).style.top"
				+ "=parseInt(document.getElementById(\"item\"+i).style.top)"
				+ "-contentHeight/stepNo');\n");
//		buffer.append("\t\t\t\teval('itemTop"
//				+ "=parseInt(itemTop)"
//				+ "-contentHeight/stepNo');\n");
//		buffer.append("\t\t\tvar itemStr='item'+i;\n");
		//buffer.append("\t\t\talert(itemStr);\n");
		//buffer.append("\t\t\talert(document.getElementById(itemStr).getAttribute('offsetTop'));\n");
		//buffer.append("\t\t\tdocument.getElementById(itemStr).setAttribute('offsetTop',eval(document.getElementById(itemStr).getAttribute('offsetTop')-contentHeight/stepNo));\n");
//		buffer.append("\t\t\t\tdocument.getElementById(itemStr).style.top = document.getElementById(itemStr).style.top-contentHeight/stepNo;\n");
		
		buffer.append("\t\t\t}catch(e){};\n\t\t}\n\t}\n");
		buffer.append("\tfunction moveDown(){\n");
//		buffer.append("\t\t\talert('onItemIndex: '+onItemIndex+'$$toItemIndex:'+toItemIndex);\n");
		buffer.append("\t\tfor(i=onItemIndex;i>toItemIndex;i--){\n");
		buffer.append("\t\t\ttry{\n");
		buffer.append("\t\t\t\talert(document.getElementById('item'+i).style.top);\n");
		buffer.append("\t\t\t\talert(parseInt(document.getElementById(\"item\"+i).style.top));\n");
//		buffer.append("\t\t\t\tvar itemTop = document.getElementById('item'+i).style.top;\n");
		buffer.append("\t\t\t\teval('document.getElementById(\"item\"+i).style.top"
				+ "=parseInt(document.getElementById(\"item\"+i).style.top)"
				+ "+contentHeight/stepNo');\n");
//		buffer.append("\t\t\t\teval('itemTop"
//				+ "=parseInt(itemTop)"
//				+ "+contentHeight/stepNo');\n");
//		buffer.append("\t\t\t\tvar itemStrD='item'+i;\n");
		//buffer.append("\t\t\t\talert('contentHeight:'+contentHeight+'##stepNo:'+stepNo);\n");
		//buffer.append("\t\t\t\talert(document.getElementById(itemStrD).getAttribute('offsetTop'));\n");
		//buffer.append("\t\t\t\tdocument.getElementById(itemStrD).setAttribute('offsetTop',eval(document.getElementById(itemStrD).getAttribute('offsetTop')+contentHeight/stepNo));\n");
//		buffer.append("\t\t\t\tdocument.getElementById(itemStrD).style.top = document.getElementById(itemStrD).style.top+contentHeight/stepNo;\n");
		
		buffer.append("\t\t\t}catch(e){};\n\t\t}\n\t}\n");
		buffer.append("\tchangeItem(0);\n");*/
		buffer.append("</SCRIPT>\n");
		return buffer.toString();
	}

	public Object getList(){
		return list;
	}

	public void setList(Object list){
		this.list = list;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}
