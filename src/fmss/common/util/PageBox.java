package fmss.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-27 下午02:36:13
 * @描述: [PageBox]分页对象
 */
public class PageBox{

	private List pageList;// 数据列表
	private PageObject pageObject;// 数据对象

	/**
	 * <p>方法名称: getPageList|描述:取得数据列表 </p>
	 * @return
	 */
	public List getPageList(){
		return pageList == null ? new ArrayList() : pageList;
	}

	/**
	 * <p>方法名称: setPageList|描述:设置数据列表 </p>
	 * @param pageList
	 */
	public void setPageList(List pageList){
		this.pageList = pageList;
	}

	/**
	 * <p>方法名称: getPageObject|描述: 取得数据对象</p>
	 * @return
	 */
	public PageObject getPageObject(){
		return pageObject;
	}

	/**
	 * <p>方法名称: setPageObject|描述:设置数据对象 </p>
	 * @param pageObject
	 */
	public void setPageObject(PageObject pageObject){
		this.pageObject = pageObject;
	}
}
