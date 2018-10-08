/*
 * 
 */
package fmss.dao.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * ��Ȩ����:(C)2003-2010
 * </p>
 * 
 * @����: zhangshoufeng
 * @����: 2009-6-23 ����04:03:04
 * @����: [UBaseInstDO]������
 */
public class UBaseInstDO extends BaseDO implements Serializable {

	private final String TRUE = "TRUE";
	private final String CH_YES = "��";
	private final String CH_NO = "��";
	private String instId;// ������� INST_ID varchar2(20)
	private String instName;// �������� INST_NAME varchar2(30)
	private String instSmpName;// ������� INST_SMP_NAME varchar2(20)
	private String parentInstId;// �ϼ����� PARENT_INST_ID varchar2(20)
	private Integer instLayer;// �������� INST_LAYER INT
	private String address;// ������ַ ADDRESS varchar2(100)
	private String zip;// �����ʱ� ZIP varchar2(6)
	private String tel;// �����绰 TEL varchar2(27)
	private String fax;// �������� FAX varchar2(20)
	private String isBussiness = "true";// �Ƿ�ҵ����� IS_BUSSINESS VARCHAR2(5)
	private Integer orderNum;// ����ֵ ORDER_NUM INT
	private String description;// ���� DESCRIPTION VARCHAR2(100)
	private Date startDate = new Date();// �������� START_DATE DATE
	private Date endDate;// ��ֹ���� END_DATE DATE
	private Date createTime;// ����ʱ�� CREATE_TIME DATE
	private String enabled = "true";// ���ñ�ʶ ENABLED VARCHAR2(5)
	private String startDateStr;// ��ʼ�����ַ���
	private String endDateStr;// ���������ַ���
	private String createTimeStr;// ����ʱ���ַ���
	private String email;// ��������
	private String instPath;//
	private Integer instLevel;// ��������
	private String parentInstName;
	private String isHead;// �Ƿ�������
	private String account;// ��˰���˺�
	private String taxpernumber;// ˰��ǼǺ�
	private String taxpername;// ��˰������
	private String taxaddress;// ��˰�˵�ַ
	private String taxtel;// ��˰�˵绰
	private String taxbank;// ��˰�˿�����
	private String taxPayerType; //��˰�����

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
	 * ��������: getEmail|����:��������
	 * </p>
	 * 
	 * @return email��String
	 */
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private String instRegion;// ����ʱ���ַ���

	public String getInstRegion() {
		return instRegion;
	}

	public void setInstRegion(String instRegion) {
		this.instRegion = instRegion;
	}

	/**
	 * <p>
	 * ��������: getEnabledCH|����:ȡ�����ñ�ʶ��������
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
	 * ��������: getIsBussinessCH|����:ȡ���Ƿ�ҵ����� ��������
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
	 * ��������: getIsBussinessCH|����:ȡ���Ƿ������� ��������
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
	 * ���캯������: |����:�޲ι��캯��
	 * </p>
	 */
	public UBaseInstDO() {
	};

	/**
	 * <p>
	 * ���캯������: |����:�вι��캯�������û���ID
	 * </p>
	 * 
	 * @param instId
	 */
	public UBaseInstDO(String instId) {
		this.instId = instId;
	}

	/**
	 * <p>
	 * ��������: getStartDateStr|����:ȡ�ÿ�ʼ�����ַ���
	 * </p>
	 * 
	 * @return
	 */
	public String getStartDateStr() {
		return formatDate(getStartDate());
	}

	/**
	 * <p>
	 * ��������: setStartDateStr|����:���ÿ�ʼ�����ַ���
	 * </p>
	 * 
	 * @param startDateStr
	 */
	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}

	/**
	 * <p>
	 * ��������: getEndDateStr|����:ȡ�ý��������ַ���
	 * </p>
	 * 
	 * @return
	 */
	public String getEndDateStr() {
		return formatDate(getEndDate());
	}

	/**
	 * <p>
	 * ��������: setEndDateStr|����:���ý��������ַ���
	 * </p>
	 * 
	 * @param endDateStr
	 */
	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	/**
	 * <p>
	 * ��������: getCreateTimeStr|����: ȡ�ô���ʱ���ַ���
	 * </p>
	 * 
	 * @return
	 */
	public String getCreateTimeStr() {
		return formatDate(getCreateTime());
	}

	/**
	 * <p>
	 * ��������: setCreateTimeStr|����:���ô���ʱ���ַ���
	 * </p>
	 * 
	 * @param createTimeStr
	 */
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	/**
	 * <p>
	 * ��������: formatDate|����:��ʽ������Ϊyyyy-MM-dd��ʽ
	 * </p>
	 * 
	 * @param date
	 * @return
	 */
	private String formatDate(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// �������ڸ�ʽ
		return date == null ? "" : df.format(date);
	}

	/**
	 * <p>
	 * ��������: getInstId|����:ȡ�û������
	 * </p>
	 * 
	 * @return
	 */
	public String getInstId() {
		return instId;
	}

	/**
	 * <p>
	 * ��������: setInstId|����:���û������
	 * </p>
	 * 
	 * @param instId
	 */
	public void setInstId(String instId) {
		this.instId = instId;
	}

	/**
	 * <p>
	 * ��������: getInstName|����:ȡ�û�������
	 * </p>
	 * 
	 * @return
	 */
	public String getInstName() {
		return instName;
	}

	/**
	 * <p>
	 * ��������: setInstName|����: ���û�������
	 * </p>
	 * 
	 * @param instName
	 */
	public void setInstName(String instName) {
		this.instName = instName;
	}

	/**
	 * <p>
	 * ��������: getInstSmpName|����: ȡ�û������
	 * </p>
	 * 
	 * @return
	 */
	public String getInstSmpName() {
		return instSmpName;
	}

	/**
	 * <p>
	 * ��������: setInstSmpName|����:���û������
	 * </p>
	 * 
	 * @param instSmpName
	 */
	public void setInstSmpName(String instSmpName) {
		this.instSmpName = instSmpName;
	}

	/**
	 * <p>
	 * ��������: getParentInstId|����: ȡ���ϼ��������
	 * </p>
	 * 
	 * @return
	 */
	public String getParentInstId() {
		return parentInstId;
	}

	/**
	 * <p>
	 * ��������: setParentInstId|����:�����ϼ��������
	 * </p>
	 * 
	 * @param parentInstId
	 */
	public void setParentInstId(String parentInstId) {
		this.parentInstId = parentInstId;
	}

	/**
	 * <p>
	 * ��������: getInstLayer|����:ȡ�û�������
	 * </p>
	 * 
	 * @return
	 */
	public Integer getInstLayer() {
		return instLayer;
	}

	/**
	 * <p>
	 * ��������: setInstLayer|����:���û�������
	 * </p>
	 * 
	 * @param instLayer
	 */
	public void setInstLayer(Integer instLayer) {
		this.instLayer = instLayer;
	}

	/**
	 * <p>
	 * ��������: getAddress|����:ȡ�û�����ַ
	 * </p>
	 * 
	 * @return
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * <p>
	 * ��������: setAddress|����: ���û�����ַ
	 * </p>
	 * 
	 * @param address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * <p>
	 * ��������: getZip|����:ȡ�û����ʱ�
	 * </p>
	 * 
	 * @return
	 */
	public String getZip() {
		return zip;
	}

	/**
	 * <p>
	 * ��������: setZip|����:���û����ʱ�
	 * </p>
	 * 
	 * @param zip
	 */
	public void setZip(String zip) {
		this.zip = zip;
	}

	/**
	 * <p>
	 * ��������: getTel|����:ȡ�û����绰����
	 * </p>
	 * 
	 * @return
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * <p>
	 * ��������: setTel|����:���û����绰����
	 * </p>
	 * 
	 * @param tel
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * <p>
	 * ��������: getFax|����:ȡ�û����������
	 * </p>
	 * 
	 * @return
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * <p>
	 * ��������: setFax|����:���û����������
	 * </p>
	 * 
	 * @param fax
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * <p>
	 * ��������: getIsBussiness|����:ȡ���Ƿ���ҵ�����
	 * </p>
	 * 
	 * @return
	 */
	public String getIsBussiness() {
		return isBussiness;
	}

	/**
	 * <p>
	 * ��������: setIsBussiness|����:����Ϊҵ�����
	 * </p>
	 * 
	 * @param isBussiness
	 */
	public void setIsBussiness(String isBussiness) {
		this.isBussiness = isBussiness;
	}

	/**
	 * <p>
	 * ��������: getOrderNum|����:ȡ�������
	 * </p>
	 * 
	 * @return
	 */
	public Integer getOrderNum() {
		return orderNum;
	}

	/**
	 * <p>
	 * ��������: setOrderNum|����: ���������
	 * </p>
	 * 
	 * @param orderNum
	 */
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	/**
	 * <p>
	 * ��������: getDescription|����:ȡ��������Ϣ
	 * </p>
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <p>
	 * ��������: setDescription|����:����������Ϣ
	 * </p>
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * <p>
	 * ��������: getStartDate|����:ȡ�ÿ�ʼ����
	 * </p>
	 * 
	 * @return
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * <p>
	 * ��������: setStartDate|����: ���ÿ�ʼ����
	 * </p>
	 * 
	 * @param startDate
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * <p>
	 * ��������: getEndDate|����:ȡ�ý�������
	 * </p>
	 * 
	 * @return
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * <p>
	 * ��������: setEndDate|����:���ý�������
	 * </p>
	 * 
	 * @param endDate
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * <p>
	 * ��������: getCreateTime|����: ȡ�ô���ʱ��
	 * </p>
	 * 
	 * @return
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * <p>
	 * ��������: setCreateTime|����: ���ô���ʱ��
	 * </p>
	 * 
	 * @param createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * <p>
	 * ��������: getEnabled|����:�ж��Ƿ����
	 * </p>
	 * 
	 * @return
	 */
	public String getEnabled() {
		return enabled;
	}

	/**
	 * <p>
	 * ��������: setEnabled|����:���ÿ��ñ�ʶ
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
