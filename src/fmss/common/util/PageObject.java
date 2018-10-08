package fmss.common.util;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-27 下午02:37:53
 * @描述: [PageObject]分页所用的数据对象
 */
public class PageObject{

	private int pageIndex;// 页码
	private int itemAmount;// 总条数
	private int pageSize = 1;// 页面大小
	private int startNum;// 开始记录数
	private int endNum;// 结束记录数

	/**
	 * <p>方法名称: getPageAmount|描述:取得总条数 </p>
	 * @return
	 */
	public int getPageAmount(){
		int remainder = itemAmount % pageSize;
		if(remainder == 0)
			return itemAmount / pageSize;
		else
			return itemAmount / pageSize + 1;
	}

	/**
	 * <p>方法名称: getPageIndex|描述:页面编号，从1开始</p>
	 * @return
	 */
	public int getPageIndex(){
		return pageIndex;
	}

	/**
	 * <p>方法名称: setPageIndex|描述:设置页码索引 </p>
	 * @param pageIndex
	 */
	public void setPageIndex(int pageIndex){
		this.pageIndex = pageIndex;
	}

	/**
	 * <p>方法名称: getItemAmount|描述:取得总记录数 </p>
	 * @return
	 */
	public int getItemAmount(){
		return itemAmount;
	}

	/**
	 * <p>方法名称: setItemAmount|描述: 设置总记录数</p>
	 * @param itemAmount
	 */
	public void setItemAmount(int itemAmount){
		this.itemAmount = itemAmount;
	}

	/**
	 * <p>方法名称: getPageSize|描述: 取得页面大小</p>
	 * @return
	 */
	public int getPageSize(){
		return pageSize;
	}

	/**
	 * <p>方法名称: setPageSize|描述:设置页面大小 </p>
	 * @param pageSize
	 */
	public void setPageSize(int pageSize){
		this.pageSize = pageSize;
	}

	/**
	 * <p>方法名称: getStartNum|描述:取得页开始编号 </p>
	 * @return
	 */
	public int getStartNum(){
		return startNum;
	}

	/**
	 * <p>方法名称: setStartNum|描述:设置页开始编号 </p>
	 * @param startNum
	 */
	public void setStartNum(int startNum){
		this.startNum = startNum;
	}

	/**
	 * <p>方法名称: getEndNum|描述:取得页结束编号 </p>
	 * @return
	 */
	public int getEndNum(){
		return endNum;
	}

	/**
	 * <p>方法名称: isFirstPage|描述:是否第一页 </p>
	 * @return
	 */
	public boolean isFirstPage(){
		return pageIndex == 1;
	}

	/**
	 * <p>方法名称: isLastPage|描述:是否最后一页 </p>
	 * @return
	 */
	public boolean isLastPage(){
		return getPageAmount() > pageIndex;
	}

	/**
	 * <p>方法名称: getBeginIndex|描述:取得开始索引号 </p>
	 * @return
	 */
	public int getBeginIndex(){
		return (pageIndex - 1) * pageSize + 1;
	}
}
