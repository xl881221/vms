package fmss.dao.entity;

/**
 * SapMapInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SapMapInfo implements java.io.Serializable {

	// Fields

	private String id;
	private String dpvId;
	private String dpvName;
	private String isDel;
	private String acciNo;
	private String prodId;
	private String drFlag;
	private String dataType;
	private String posFlag;
	private String reserved1;
	private String reserved2;
	private String reserved3;
	private String reserved4;

	// Constructors

	/** default constructor */
	public SapMapInfo() {
	}

	/** minimal constructor */
	public SapMapInfo(String id,String dpvName, String isDel, String acciNo) {
		this.id = id;
		this.dpvName = dpvName;
		this.isDel = isDel;
		this.acciNo = acciNo;
	}

	/** full constructor */
	public SapMapInfo(String id,String dpvName, String isDel, String acciNo,
			String prodId, String drFlag, String dataType, String posFlag,
			String reserved1, String reserved2, String reserved3,
			String reserved4) {
		this.id = id;
		this.dpvName = dpvName;
		this.isDel = isDel;
		this.acciNo = acciNo;
		this.prodId = prodId;
		this.drFlag = drFlag;
		this.dataType = dataType;
		this.posFlag = posFlag;
		this.reserved1 = reserved1;
		this.reserved2 = reserved2;
		this.reserved3 = reserved3;
		this.reserved4 = reserved4;
	}

	// Property accessors

	public String getDpvId() {
		return this.dpvId;
	}

	public void setDpvId(String dpvId) {
		this.dpvId = dpvId;
	}

	public String getDpvName() {
		return this.dpvName;
	}

	public void setDpvName(String dpvName) {
		this.dpvName = dpvName;
	}

	public String getIsDel() {
		return this.isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}

	public String getAcciNo() {
		return this.acciNo;
	}

	public void setAcciNo(String acciNo) {
		this.acciNo = acciNo;
	}

	public String getProdId() {
		return this.prodId;
	}

	public void setProdId(String prodId) {
		this.prodId = prodId;
	}

	public String getDrFlag() {
		return this.drFlag;
	}

	public void setDrFlag(String drFlag) {
		this.drFlag = drFlag;
	}

	public String getDataType() {
		return this.dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getPosFlag() {
		return this.posFlag;
	}

	public void setPosFlag(String posFlag) {
		this.posFlag = posFlag;
	}

	public String getReserved1() {
		return this.reserved1;
	}

	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}

	public String getReserved2() {
		return this.reserved2;
	}

	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}

	public String getReserved3() {
		return this.reserved3;
	}

	public void setReserved3(String reserved3) {
		this.reserved3 = reserved3;
	}

	public String getReserved4() {
		return this.reserved4;
	}

	public void setReserved4(String reserved4) {
		this.reserved4 = reserved4;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}