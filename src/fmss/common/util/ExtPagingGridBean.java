/**
 * 
 */
package fmss.common.util;

import java.util.List;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-27 ����02:31:21
 * @����: [ExtPagingGridBean]���ݷ�װ����Bean
 */
public class ExtPagingGridBean{

	private List dataList; // ���ݼ�¼
	private int totalCount; // �ܼ�¼��

	/**
	 * <p>��������: getDataList|����:ȡ�������б� </p>
	 * @return
	 */
	public List getDataList(){
		return dataList;
	}

	/**
	 * <p>��������: setDataList|����:���������б� </p>
	 * @param dataList
	 */
	public void setDataList(List dataList){
		this.dataList = dataList;
	}

	/**
	 * <p>��������: getTotalCount|����:ȡ���ܼ�¼�� </p>
	 * @return
	 */
	public int getTotalCount(){
		return totalCount;
	}

	/**
	 * <p>��������: setTotalCount|����:�����ܼ�¼�� </p>
	 * @param totalCount
	 */
	public void setTotalCount(int totalCount){
		this.totalCount = totalCount;
	}
}
