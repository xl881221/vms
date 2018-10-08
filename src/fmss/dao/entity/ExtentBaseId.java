package fmss.dao.entity;

/**
 * ExtentBaseId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ExtentBaseId implements java.io.Serializable {

	// Fields

	private String instName;
	private String ddate;
	private String systemId;
	private String itemId;
	private String itemName;
	private String rcurrCd;
	private String itemValue;
	private String reportId;
	private String srcsysCd;

	// Constructors

	/** default constructor */
	public ExtentBaseId() {
	}

	/** minimal constructor */
	public ExtentBaseId(String instName) {
		this.instName = instName;
	}

	/** full constructor */
	public ExtentBaseId(String instName, String ddate, String systemId,
			String itemId, String itemName, String rcurrCd, String itemValue,
			String reportId, String srcsysCd) {
		this.instName = instName;
		this.ddate = ddate;
		this.systemId = systemId;
		this.itemId = itemId;
		this.itemName = itemName;
		this.rcurrCd = rcurrCd;
		this.itemValue = itemValue;
		this.reportId = reportId;
		this.srcsysCd = srcsysCd;
	}

	// Property accessors

	public String getInstName() {
		return this.instName;
	}

	public void setInstName(String instName) {
		this.instName = instName;
	}

	public String getDdate() {
		return this.ddate;
	}

	public void setDdate(String ddate) {
		this.ddate = ddate;
	}

	public String getSystemId() {
		return this.systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return this.itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getRcurrCd() {
		return this.rcurrCd;
	}

	public void setRcurrCd(String rcurrCd) {
		this.rcurrCd = rcurrCd;
	}

	public String getItemValue() {
		return this.itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public String getReportId() {
		return this.reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getSrcsysCd() {
		return this.srcsysCd;
	}

	public void setSrcsysCd(String srcsysCd) {
		this.srcsysCd = srcsysCd;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ExtentBaseId))
			return false;
		ExtentBaseId castOther = (ExtentBaseId) other;

		return ((this.getInstName().equals(castOther.getInstName())) || (this
				.getInstName() != null
				&& castOther.getInstName() != null && this.getInstName()
				.equals(castOther.getInstName())))
				&& ((this.getDdate().equals(castOther.getDdate())) || (this
						.getDdate() != null
						&& castOther.getDdate() != null && this.getDdate()
						.equals(castOther.getDdate())))
				&& ((this.getSystemId().equals(castOther.getSystemId())) || (this
						.getSystemId() != null
						&& castOther.getSystemId() != null && this
						.getSystemId().equals(castOther.getSystemId())))
				&& ((this.getItemId().equals(castOther.getItemId())) || (this
						.getItemId() != null
						&& castOther.getItemId() != null && this.getItemId()
						.equals(castOther.getItemId())))
				&& ((this.getItemName().equals(castOther.getItemName())) || (this
						.getItemName() != null
						&& castOther.getItemName() != null && this
						.getItemName().equals(castOther.getItemName())))
				&& ((this.getRcurrCd().equals(castOther.getRcurrCd())) || (this
						.getRcurrCd() != null
						&& castOther.getRcurrCd() != null && this.getRcurrCd()
						.equals(castOther.getRcurrCd())))
				&& ((this.getItemValue().equals(castOther.getItemValue())) || (this
						.getItemValue() != null
						&& castOther.getItemValue() != null && this
						.getItemValue().equals(castOther.getItemValue())))
				&& ((this.getReportId().equals(castOther.getReportId())) || (this
						.getReportId() != null
						&& castOther.getReportId() != null && this
						.getReportId().equals(castOther.getReportId())))
				&& ((this.getSrcsysCd().equals(castOther.getSrcsysCd())) || (this
						.getSrcsysCd() != null
						&& castOther.getSrcsysCd() != null && this
						.getSrcsysCd().equals(castOther.getSrcsysCd())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getInstName() == null ? 0 : this.getInstName().hashCode());
		result = 37 * result
				+ (getDdate() == null ? 0 : this.getDdate().hashCode());
		result = 37 * result
				+ (getSystemId() == null ? 0 : this.getSystemId().hashCode());
		result = 37 * result
				+ (getItemId() == null ? 0 : this.getItemId().hashCode());
		result = 37 * result
				+ (getItemName() == null ? 0 : this.getItemName().hashCode());
		result = 37 * result
				+ (getRcurrCd() == null ? 0 : this.getRcurrCd().hashCode());
		result = 37 * result
				+ (getItemValue() == null ? 0 : this.getItemValue().hashCode());
		result = 37 * result
				+ (getReportId() == null ? 0 : this.getReportId().hashCode());
		result = 37 * result
				+ (getSrcsysCd() == null ? 0 : this.getSrcsysCd().hashCode());
		return result;
	}

}