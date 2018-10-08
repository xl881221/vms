/**
 * 
 */
package fmss.common.util;

import java.util.List;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-27 下午02:31:21
 * @描述: [ExtPagingGridBean]数据封装公共Bean
 */
public class ExtPagingGridBean{

	private List dataList; // 数据记录
	private int totalCount; // 总记录数

	/**
	 * <p>方法名称: getDataList|描述:取得数据列表 </p>
	 * @return
	 */
	public List getDataList(){
		return dataList;
	}

	/**
	 * <p>方法名称: setDataList|描述:设置数据列表 </p>
	 * @param dataList
	 */
	public void setDataList(List dataList){
		this.dataList = dataList;
	}

	/**
	 * <p>方法名称: getTotalCount|描述:取得总记录数 </p>
	 * @return
	 */
	public int getTotalCount(){
		return totalCount;
	}

	/**
	 * <p>方法名称: setTotalCount|描述:设置总记录数 </p>
	 * @param totalCount
	 */
	public void setTotalCount(int totalCount){
		this.totalCount = totalCount;
	}
}
