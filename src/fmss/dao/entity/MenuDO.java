package fmss.dao.entity;

import java.util.List;
import fmss.dao.entity.BaseDO;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-27 下午12:04:08
 * @描述: [MenuDO]菜单DO
 */
public class MenuDO extends BaseDO implements java.io.Serializable{

	private static final long serialVersionUID = 4308547504611152386L;// 序列号
	private java.lang.String itemcode;// 栏目编号
	private java.lang.String itemname;// 栏目名称
	private java.lang.String target;// 目标位置
	private java.lang.String url;// 连接地址
	private java.lang.String innerUrl;// 连接地址
	private java.lang.String imgsrc;// 图片位置
	private Integer ordernum;// 排序顺序
	private java.lang.String display;// 显示信息
	private java.lang.String enabled;// 是否可用
	private List subMenuList = null;// 子菜单信息
	private String systemId;// 子系统编号
	private String menu_ename;//栏目英文名称
	
	/**
	 * @return systemId
	 */
	public String getSystemId(){
		return systemId;
	}

	
	/**
	 * @param systemId 要设置的 systemId
	 */
	public void setSystemId(String systemId){
		this.systemId = systemId;
	}

	/**
	 * <p>方法名称: setItemcode|描述:设置菜单栏目代码 </p>
	 * @param itemcode
	 */
	public void setItemcode(java.lang.String itemcode){
		this.itemcode = itemcode;
	}

	/**
	 * <p>方法名称: getItemcode|描述: 取得菜单栏目代码</p>
	 * @return
	 */
	public java.lang.String getItemcode(){
		return itemcode;
	}

	/**
	 * <p>方法名称: setItemname|描述:设置菜单栏目名称 </p>
	 * @param itemname
	 */
	public void setItemname(java.lang.String itemname){
		this.itemname = itemname;
	}

	/**
	 * <p>方法名称: getItemname|描述:取得菜单栏目名称 </p>
	 * @return
	 */
	public java.lang.String getItemname(){
		return itemname;
	}

	/**
	 * <p>方法名称: setTarget|描述:取得菜单弹出位置 </p>
	 * @param target
	 */
	public void setTarget(java.lang.String target){
		this.target = target;
	}

	/**
	 * <p>方法名称: getTarget|描述: 取得菜单弹出位置</p>
	 * @return
	 */
	public java.lang.String getTarget(){
		return target;
	}

	/**
	 * <p>方法名称: setUrl|描述:设置菜单连接地址 </p>
	 * @param url
	 */
	public void setUrl(java.lang.String url){
		this.url = url;
	}

	/**
	 * <p>方法名称: getUrl|描述: 取得菜单连接地址</p>
	 * @return
	 */
	public java.lang.String getUrl(){
		return url;
	}

	/**
	 * <p>方法名称: setImgsrc|描述:设置图片地址 </p>
	 * @param imgsrc
	 */
	public void setImgsrc(java.lang.String imgsrc){
		this.imgsrc = imgsrc;
	}

	/**
	 * <p>方法名称: getImgsrc|描述: 取得图片地址</p>
	 * @return
	 */
	public java.lang.String getImgsrc(){
		return imgsrc;
	}

	/**
	 * <p>方法名称: setDisplay|描述:设置菜单显示信息 </p>
	 * @param display
	 */
	public void setDisplay(java.lang.String display){
		this.display = display;
	}

	/**
	 * <p>方法名称: getDisplay|描述: 取得菜单显示信息</p>
	 * @return
	 */
	public java.lang.String getDisplay(){
		return display;
	}

	/**
	 * <p>方法名称: setEnabled|描述: 设置菜单是否启用</p>
	 * @param enabled
	 */
	public void setEnabled(java.lang.String enabled){
		this.enabled = enabled;
	}

	/**
	 * <p>方法名称: getEnabled|描述: 判断菜单是否启用</p>
	 * @return
	 */
	public java.lang.String getEnabled(){
		return enabled;
	}

	/**
	 * <p>方法名称: getOrdernum|描述: 取得序号</p>
	 * @return
	 */
	public Integer getOrdernum(){
		return ordernum;
	}

	/**
	 * <p>方法名称: setOrdernum|描述:设置菜单显示序号 </p>
	 * @param ordernum
	 */
	public void setOrdernum(Integer ordernum){
		this.ordernum = ordernum;
	}

	/**
	 * <p>方法名称: getSubMenuList|描述:取得菜单列表 </p>
	 * @return
	 */
	public List getSubMenuList(){
		return subMenuList;
	}

	/**
	 * <p>方法名称: setSubMenuList|描述:设置菜单列表 </p>
	 * @param subMenuList
	 */
	public void setSubMenuList(List subMenuList){
		this.subMenuList = subMenuList;
	}


	public String getMenu_ename() {
		return menu_ename;
	}


	public void setMenu_ename(String menu_ename) {
		this.menu_ename = menu_ename;
	}


	public java.lang.String getInnerUrl() {
		return innerUrl;
	}


	public void setInnerUrl(java.lang.String innerUrl) {
		this.innerUrl = innerUrl;
	}
	
}