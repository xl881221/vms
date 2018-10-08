/*
 * 
 */
package fmss.common.ui;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-27 下午02:11:45
 * @描述: [InstGridTreeNode]请在此简要描述类的功能
 */
public class InstGridTreeNode extends GridTreeNode{

	private String instId;// 机构编号 INST_ID varchar2(20)
	private String instName;// 机构名称 INST_NAME varchar2(30)
	private String instSmpName;// 机构简称 INST_SMP_NAME varchar2(20)
	private String parentInstId;// 上级机构 PARENT_INST_ID varchar2(20)
	private Integer instLayer;// 机构级别 INST_LAYER INT
	private String address;// 机构地址 ADDRESS varchar2(100)
	private String zip;// 机构邮编 ZIP varchar2(6)
	private String tel;// 机构电话 TEL varchar2(27)
	private String fax;// 机构传真 FAX varchar2(20)
	private String isBussiness;// 是否业务机构 IS_BUSSINESS VARCHAR2(5)
	private Integer orderNum;// 排序值 ORDER_NUM INT
	private String description;// 描述 DESCRIPTION VARCHAR2(100)
	private String startDate;// 启用日期 START_DATE DATE
	private String endDate;// 终止日期 END_DATE DATE
	private String createTime;// 创建时间 CREATE_TIME DATE
	private String enabled;// 启用标识 ENABLED VARCHAR2(5)

	/**
	 * <p>方法名称: getInstId|描述:取得机构编号 </p>
	 * @return
	 */
	public String getInstId(){
		return instId;
	}

	/**
	 * <p>方法名称: setInstId|描述:设置机构编号 </p>
	 * @param instId
	 */
	public void setInstId(String instId){
		this.instId = instId;
	}

	/**
	 * <p>方法名称: getInstName|描述:取得机构名称 </p>
	 * @return
	 */
	public String getInstName(){
		return instName;
	}

	/**
	 * <p>方法名称: setInstName|描述:设置机构名称 </p>
	 * @param instName
	 */
	public void setInstName(String instName){
		this.instName = instName;
	}

	/**
	 * <p>方法名称: getInstSmpName|描述:取得机构简称 </p>
	 * @return
	 */
	public String getInstSmpName(){
		return instSmpName;
	}

	/**
	 * <p>方法名称: setInstSmpName|描述:设置机构简称 </p>
	 * @param instSmpName
	 */
	public void setInstSmpName(String instSmpName){
		this.instSmpName = instSmpName;
	}

	/**
	 * <p>方法名称: getParentInstId|描述:取得上级机构ID </p>
	 * @return
	 */
	public String getParentInstId(){
		return parentInstId;
	}

	/**
	 * <p>方法名称: setParentInstId|描述:设置上级机构ID </p>
	 * @param parentInstId
	 */
	public void setParentInstId(String parentInstId){
		this.parentInstId = parentInstId;
	}

	/**
	 * <p>方法名称: getInstLayer|描述:取得机构级别 </p>
	 * @return
	 */
	public Integer getInstLayer(){
		return instLayer;
	}

	/**
	 * <p>方法名称: setInstLayer|描述:设置机构级别 </p>
	 * @param instLayer
	 */
	public void setInstLayer(Integer instLayer){
		this.instLayer = instLayer;
	}

	/**
	 * <p>方法名称: getAddress|描述:取得机构所在地址 </p>
	 * @return
	 */
	public String getAddress(){
		return address;
	}

	/**
	 * <p>方法名称: setAddress|描述:设置机构所在地址 </p>
	 * @param address
	 */
	public void setAddress(String address){
		this.address = address;
	}

	/**
	 * <p>方法名称: getZip|描述:取得邮政编码 </p>
	 * @return
	 */
	public String getZip(){
		return zip;
	}

	/**
	 * <p>方法名称: setZip|描述:设置邮政编码 </p>
	 * @param zip
	 */
	public void setZip(String zip){
		this.zip = zip;
	}

	/**
	 * <p>方法名称: getTel|描述:取得电话号码 </p>
	 * @return
	 */
	public String getTel(){
		return tel;
	}

	/**
	 * <p>方法名称: setTel|描述:设置电话号码 </p>
	 * @param tel
	 */
	public void setTel(String tel){
		this.tel = tel;
	}

	/**
	 * <p>方法名称: getFax|描述:取得传真号码 </p>
	 * @return
	 */
	public String getFax(){
		return fax;
	}

	/**
	 * <p>方法名称: setFax|描述:设置传真号码 </p>
	 * @param fax
	 */
	public void setFax(String fax){
		this.fax = fax;
	}

	/**
	 * <p>方法名称: getIsBussiness|描述:判断是否业务机构 </p>
	 * @return
	 */
	public String getIsBussiness(){
		return isBussiness;
	}

	/**
	 * <p>方法名称: setIsBussiness|描述:设置是否为业务机构 </p>
	 * @param isBussiness
	 */
	public void setIsBussiness(String isBussiness){
		this.isBussiness = isBussiness;
	}

	/**
	 * <p>方法名称: getOrderNum|描述:取得排序序号 </p>
	 * @return
	 */
	public Integer getOrderNum(){
		return orderNum;
	}

	/**
	 * <p>方法名称: setOrderNum|描述:设置排序序号 </p>
	 * @param orderNum
	 */
	public void setOrderNum(Integer orderNum){
		this.orderNum = orderNum;
	}

	/**
	 * <p>方法名称: getDescription|描述:取得描述信息 </p>
	 * @return
	 */
	public String getDescription(){
		return description;
	}

	/**
	 * <p>方法名称: setDescription|描述:设置描述信息 </p>
	 * @param description
	 */
	public void setDescription(String description){
		this.description = description;
	}

	/**
	 * <p>方法名称: getStartDate|描述:取得开始日期 </p>
	 * @return
	 */
	public String getStartDate(){
		return startDate;
	}

	/**
	 * <p>方法名称: setStartDate|描述:设置开始日期 </p>
	 * @param startDate
	 */
	public void setStartDate(String startDate){
		this.startDate = startDate;
	}

	/**
	 * <p>方法名称: getEndDate|描述:取得结束日期 </p>
	 * @return
	 */
	public String getEndDate(){
		return endDate;
	}

	/**
	 * <p>方法名称: setEndDate|描述:设置结束日期 </p>
	 * @param endDate
	 */
	public void setEndDate(String endDate){
		this.endDate = endDate;
	}

	/**
	 * <p>方法名称: getCreateTime|描述:取得创建时间 </p>
	 * @return
	 */
	public String getCreateTime(){
		return createTime;
	}

	/**
	 * <p>方法名称: setCreateTime|描述:设置创建时间 </p>
	 * @param createTime
	 */
	public void setCreateTime(String createTime){
		this.createTime = createTime;
	}

	/**
	 * <p>方法名称: getEnabled|描述: 判断是否启用此机构</p>
	 * @return
	 */
	public String getEnabled(){
		return enabled;
	}

	/**
	 * <p>方法名称: setEnabled|描述: 设置机构是否启用</p>
	 * @param enabled
	 */
	public void setEnabled(String enabled){
		this.enabled = enabled;
	}
}