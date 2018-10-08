/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 版权所有:(C)2003-2010
 * </p>
 * 
 * @作者: zhangshoufeng
 * @日期: 2009-6-23 下午04:03:04
 * @描述: [UBaseInstDO]机构表
 */
public class UBaseInstDO extends BaseDO implements Serializable {

	private final String TRUE = "TRUE";
	private final String CH_YES = "是";
	private final String CH_NO = "否";
	private String instId;// 机构编号 INST_ID varchar2(20)
	private String instName;// 机构名称 INST_NAME varchar2(30)
	private String instSmpName;// 机构简称 INST_SMP_NAME varchar2(20)
	private String parentInstId;// 上级机构 PARENT_INST_ID varchar2(20)
	private Integer instLayer;// 机构级别 INST_LAYER INT
	private String address;// 机构地址 ADDRESS varchar2(100)
	private String zip;// 机构邮编 ZIP varchar2(6)
	private String tel;// 机构电话 TEL varchar2(27)
	private String fax;// 机构传真 FAX varchar2(20)
	private String isBussiness = "true";// 是否业务机构 IS_BUSSINESS VARCHAR2(5)
	private Integer orderNum;// 排序值 ORDER_NUM INT
	private String description;// 描述 DESCRIPTION VARCHAR2(100)
	private Date startDate = new Date();// 启用日期 START_DATE DATE
	private Date endDate;// 终止日期 END_DATE DATE
	private Date createTime;// 创建时间 CREATE_TIME DATE
	private String enabled = "true";// 启用标识 ENABLED VARCHAR2(5)
	private String startDateStr;// 开始日期字符串
	private String endDateStr;// 结束日期字符串
	private String createTimeStr;// 创建时间字符串
	private String email;// 机构邮箱
	private String instPath;//
	private Integer instLevel;// 机构级别
	private String parentInstName;
	private String isHead;// 是否是总行
	private String account;// 纳税人账号
	private String taxpernumber;// 税务登记号
	private String taxpername;// 纳税人名称
	private String taxaddress;// 纳税人地址
	private String taxtel;// 纳税人电话
	private String taxbank;// 纳税人开户行
	private String taxPayerType; //纳税人类别

	public String getTaxaddress() {
		return taxaddress;
	}

	public void setTaxaddress(String taxaddress) {
		this.taxaddress = taxaddress;
	}

	public String getTaxtel() {
		return taxtel;
	}

	public void setTaxtel(String taxtel) {
		this.taxtel = taxtel;
	}

	public String getTaxbank() {
		return taxbank;
	}

	public void setTaxbank(String taxbank) {
		this.taxbank = taxbank;
	}

	public String getParentInstName() {
		return parentInstName;
	}

	public void setParentInstName(String parentInstName) {
		this.parentInstName = parentInstName;
	}

	/**
	 * <p>
	 * 方法名称: getEmail|描述:机构邮箱
	 * </p>
	 * 
	 * @return email：String
	 */
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private String instRegion;// 创建时间字符串

	public String getInstRegion() {
		return instRegion;
	}

	public void setInstRegion(String instRegion) {
		this.instRegion = instRegion;
	}

	/**
	 * <p>
	 * 方法名称: getEnabledCH|描述:取得启用标识中文描述
	 * </p>
	 * 
	 * @return
	 */
	public String getEnabledCH() {

		if (enabled.toUpperCase().equals(TRUE))
			return CH_YES;
		else
			return CH_NO;
	}

	/**
	 * <p>
	 * 方法名称: getIsBussinessCH|描述:取得是否业务机构 中文描述
	 * </p>
	 * 
	 * @return
	 */
	public String getIsBussinessCH() {
		if (isBussiness.toUpperCase().equals(TRUE))
			return CH_YES;
		else
			return CH_NO;
	}

	/**
	 * <p>
	 * 方法名称: getIsBussinessCH|描述:取得是否是总行 中文描述
	 * </p>
	 * 
	 * @return
	 */
	public String getIsHeadCH() {
		if (isHead.toUpperCase().equals(TRUE))
			return CH_YES;
		else
			return CH_NO;
	}

	/**
	 * <p>
	 * 构造函数名称: |描述:无参构造函数
	 * </p>
	 */
	public UBaseInstDO() {
	};

	/**
	 * <p>
	 * 构造函数名称: |描述:有参构造函数，设置机构ID
	 * </p>
	 * 
	 * @param instId
	 */
	public UBaseInstDO(String instId) {
		this.instId = instId;
	}

	/**
	 * <p>
	 * 方法名称: getStartDateStr|描述:取得开始日期字符串
	 * </p>
	 * 
	 * @return
	 */
	public String getStartDateStr() {
		return formatDate(getStartDate());
	}

	/**
	 * <p>
	 * 方法名称: setStartDateStr|描述:设置开始日期字符串
	 * </p>
	 * 
	 * @param startDateStr
	 */
	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}

	/**
	 * <p>
	 * 方法名称: getEndDateStr|描述:取得结束日期字符串
	 * </p>
	 * 
	 * @return
	 */
	public String getEndDateStr() {
		return formatDate(getEndDate());
	}

	/**
	 * <p>
	 * 方法名称: setEndDateStr|描述:设置结束日期字符串
	 * </p>
	 * 
	 * @param endDateStr
	 */
	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	/**
	 * <p>
	 * 方法名称: getCreateTimeStr|描述: 取得创建时间字符串
	 * </p>
	 * 
	 * @return
	 */
	public String getCreateTimeStr() {
		return formatDate(getCreateTime());
	}

	/**
	 * <p>
	 * 方法名称: setCreateTimeStr|描述:设置创建时间字符串
	 * </p>
	 * 
	 * @param createTimeStr
	 */
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	/**
	 * <p>
	 * 方法名称: formatDate|描述:格式化日期为yyyy-MM-dd样式
	 * </p>
	 * 
	 * @param date
	 * @return
	 */
	private String formatDate(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		return date == null ? "" : df.format(date);
	}

	/**
	 * <p>
	 * 方法名称: getInstId|描述:取得机构编号
	 * </p>
	 * 
	 * @return
	 */
	public String getInstId() {
		return instId;
	}

	/**
	 * <p>
	 * 方法名称: setInstId|描述:设置机构编号
	 * </p>
	 * 
	 * @param instId
	 */
	public void setInstId(String instId) {
		this.instId = instId;
	}

	/**
	 * <p>
	 * 方法名称: getInstName|描述:取得机构名称
	 * </p>
	 * 
	 * @return
	 */
	public String getInstName() {
		return instName;
	}

	/**
	 * <p>
	 * 方法名称: setInstName|描述: 设置机构名称
	 * </p>
	 * 
	 * @param instName
	 */
	public void setInstName(String instName) {
		this.instName = instName;
	}

	/**
	 * <p>
	 * 方法名称: getInstSmpName|描述: 取得机构简称
	 * </p>
	 * 
	 * @return
	 */
	public String getInstSmpName() {
		return instSmpName;
	}

	/**
	 * <p>
	 * 方法名称: setInstSmpName|描述:设置机构简称
	 * </p>
	 * 
	 * @param instSmpName
	 */
	public void setInstSmpName(String instSmpName) {
		this.instSmpName = instSmpName;
	}

	/**
	 * <p>
	 * 方法名称: getParentInstId|描述: 取得上级机构编号
	 * </p>
	 * 
	 * @return
	 */
	public String getParentInstId() {
		return parentInstId;
	}

	/**
	 * <p>
	 * 方法名称: setParentInstId|描述:设置上级机构编号
	 * </p>
	 * 
	 * @param parentInstId
	 */
	public void setParentInstId(String parentInstId) {
		this.parentInstId = parentInstId;
	}

	/**
	 * <p>
	 * 方法名称: getInstLayer|描述:取得机构级别
	 * </p>
	 * 
	 * @return
	 */
	public Integer getInstLayer() {
		return instLayer;
	}

	/**
	 * <p>
	 * 方法名称: setInstLayer|描述:设置机构级别
	 * </p>
	 * 
	 * @param instLayer
	 */
	public void setInstLayer(Integer instLayer) {
		this.instLayer = instLayer;
	}

	/**
	 * <p>
	 * 方法名称: getAddress|描述:取得机构地址
	 * </p>
	 * 
	 * @return
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * <p>
	 * 方法名称: setAddress|描述: 设置机构地址
	 * </p>
	 * 
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * <p>
	 * 方法名称: getZip|描述:取得机构邮编
	 * </p>
	 * 
	 * @return
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * <p>
	 * 方法名称: setZip|描述:设置机构邮编
	 * </p>
	 * 
	 * @param zip
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}

	/**
	 * <p>
	 * 方法名称: getTel|描述:取得机构电话号码
	 * </p>
	 * 
	 * @return
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * <p>
	 * 方法名称: setTel|描述:设置机构电话号码
	 * </p>
	 * 
	 * @param tel
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * <p>
	 * 方法名称: getFax|描述:取得机构传真号码
	 * </p>
	 * 
	 * @return
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * <p>
	 * 方法名称: setFax|描述:设置机构传真号码
	 * </p>
	 * 
	 * @param fax
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * <p>
	 * 方法名称: getIsBussiness|描述:取得是否是业务机构
	 * </p>
	 * 
	 * @return
	 */
	public String getIsBussiness() {
		return isBussiness;
	}

	/**
	 * <p>
	 * 方法名称: setIsBussiness|描述:设置为业务机构
	 * </p>
	 * 
	 * @param isBussiness
	 */
	public void setIsBussiness(String isBussiness) {
		this.isBussiness = isBussiness;
	}

	/**
	 * <p>
	 * 方法名称: getOrderNum|描述:取得排序号
	 * </p>
	 * 
	 * @return
	 */
	public Integer getOrderNum() {
		return orderNum;
	}

	/**
	 * <p>
	 * 方法名称: setOrderNum|描述: 设置排序号
	 * </p>
	 * 
	 * @param orderNum
	 */
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	/**
	 * <p>
	 * 方法名称: getDescription|描述:取得描述信息
	 * </p>
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <p>
	 * 方法名称: setDescription|描述:设置描述信息
	 * </p>
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * <p>
	 * 方法名称: getStartDate|描述:取得开始日期
	 * </p>
	 * 
	 * @return
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * <p>
	 * 方法名称: setStartDate|描述: 设置开始日期
	 * </p>
	 * 
	 * @param startDate
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * <p>
	 * 方法名称: getEndDate|描述:取得结束日期
	 * </p>
	 * 
	 * @return
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * <p>
	 * 方法名称: setEndDate|描述:设置结束日期
	 * </p>
	 * 
	 * @param endDate
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * <p>
	 * 方法名称: getCreateTime|描述: 取得创建时间
	 * </p>
	 * 
	 * @return
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * <p>
	 * 方法名称: setCreateTime|描述: 设置创建时间
	 * </p>
	 * 
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * <p>
	 * 方法名称: getEnabled|描述:判断是否可用
	 * </p>
	 * 
	 * @return
	 */
	public String getEnabled() {
		return enabled;
	}

	/**
	 * <p>
	 * 方法名称: setEnabled|描述:设置可用标识
	 * </p>
	 * 
	 * @param enabled
	 */
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public UBaseInstDO(String instId, String instName, String parentInstId) {
		super();
		this.instId = instId;
		this.instName = instName;
		this.parentInstId = parentInstId;
	}

	public UBaseInstDO(String instId, String instName) {
		super();
		this.instId = instId;
		this.instName = instName;
	}

	public Integer getInstLevel() {
		return instLevel;
	}

	public void setInstLevel(Integer instLevel) {
		this.instLevel = instLevel;
	}

	public String getInstPath() {
		return instPath;
	}

	public void setInstPath(String instPath) {
		this.instPath = instPath;
	}

	public String getIsHead() {
		return isHead;
	}

	public void setIsHead(String isHead) {
		this.isHead = isHead;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getTaxpernumber() {
		return taxpernumber;
	}

	public void setTaxpernumber(String taxpernumber) {
		this.taxpernumber = taxpernumber;
	}

	public String getTaxpername() {
		return taxpername;
	}

	public void setTaxpername(String taxpername) {
		this.taxpername = taxpername;
	}

	public String getTaxPayerType() {
		return taxPayerType;
	}

	public void setTaxPayerType(String taxPayerType) {
		this.taxPayerType = taxPayerType;
	}
}
