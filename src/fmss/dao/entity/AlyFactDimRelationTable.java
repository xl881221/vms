package fmss.dao.entity;

/**
 * AlyFactDimRelationTable entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AlyFactDimRelationTable implements java.io.Serializable {

	// Fields

	private Long id;
	private AlyDimTableMeta alyDimTableMeta;
	private String relationCode;
	private String relationName;
	private Long factTableId;
	private String factTableClmName;
	private String dimTableClmName;

	// Constructors

	/** default constructor */
	public AlyFactDimRelationTable() {
	}

	/** full constructor */
	public AlyFactDimRelationTable(AlyDimTableMeta alyDimTableMeta,
			String relationCode, String relationName, Long factTableId,
			String factTableClmName, String dimTableClmName) {
		this.alyDimTableMeta = alyDimTableMeta;
		this.relationCode = relationCode;
		this.relationName = relationName;
		this.factTableId = factTableId;
		this.factTableClmName = factTableClmName;
		this.dimTableClmName = dimTableClmName;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AlyDimTableMeta getAlyDimTableMeta() {
		return this.alyDimTableMeta;
	}

	public void setAlyDimTableMeta(AlyDimTableMeta alyDimTableMeta) {
		this.alyDimTableMeta = alyDimTableMeta;
	}

	public String getRelationCode() {
		return this.relationCode;
	}

	public void setRelationCode(String relationCode) {
		this.relationCode = relationCode;
	}

	public String getRelationName() {
		return this.relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	public Long getFactTableId() {
		return this.factTableId;
	}

	public void setFactTableId(Long factTableId) {
		this.factTableId = factTableId;
	}

	public String getFactTableClmName() {
		return this.factTableClmName;
	}

	public void setFactTableClmName(String factTableClmName) {
		this.factTableClmName = factTableClmName;
	}

	public String getDimTableClmName() {
		return this.dimTableClmName;
	}

	public void setDimTableClmName(String dimTableClmName) {
		this.dimTableClmName = dimTableClmName;
	}

}