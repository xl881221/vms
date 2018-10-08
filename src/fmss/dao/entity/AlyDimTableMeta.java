package fmss.dao.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * AlyDimTableMeta entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AlyDimTableMeta implements java.io.Serializable {

	// Fields

	private Long id;
	private String dimTableName;
	private String dimTableDes;
	private String dimTableType;
	private String dimTableCreator;
	private String dimTableDate;
	private String isAddData;
	private String isGatherFinish;
	private Set alyFactDimRelationTables = new HashSet(0);

	// Constructors

	/** default constructor */
	public AlyDimTableMeta() {
	}

	/** minimal constructor */
	public AlyDimTableMeta(String dimTableName, String dimTableDes,
			String dimTableType) {
		this.dimTableName = dimTableName;
		this.dimTableDes = dimTableDes;
		this.dimTableType = dimTableType;
	}

	/** full constructor */
	public AlyDimTableMeta(String dimTableName, String dimTableDes,
			String dimTableType, String dimTableCreator, String dimTableDate,
			String isAddData, String isGatherFinish,
			Set alyFactDimRelationTables) {
		this.dimTableName = dimTableName;
		this.dimTableDes = dimTableDes;
		this.dimTableType = dimTableType;
		this.dimTableCreator = dimTableCreator;
		this.dimTableDate = dimTableDate;
		this.isAddData = isAddData;
		this.isGatherFinish = isGatherFinish;
		this.alyFactDimRelationTables = alyFactDimRelationTables;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDimTableName() {
		return this.dimTableName;
	}

	public void setDimTableName(String dimTableName) {
		this.dimTableName = dimTableName;
	}

	public String getDimTableDes() {
		return this.dimTableDes;
	}

	public void setDimTableDes(String dimTableDes) {
		this.dimTableDes = dimTableDes;
	}

	public String getDimTableType() {
		return this.dimTableType;
	}

	public void setDimTableType(String dimTableType) {
		this.dimTableType = dimTableType;
	}

	public String getDimTableCreator() {
		return this.dimTableCreator;
	}

	public void setDimTableCreator(String dimTableCreator) {
		this.dimTableCreator = dimTableCreator;
	}

	public String getDimTableDate() {
		return this.dimTableDate;
	}

	public void setDimTableDate(String dimTableDate) {
		this.dimTableDate = dimTableDate;
	}

	public String getIsAddData() {
		return this.isAddData;
	}

	public void setIsAddData(String isAddData) {
		this.isAddData = isAddData;
	}

	public String getIsGatherFinish() {
		return this.isGatherFinish;
	}

	public void setIsGatherFinish(String isGatherFinish) {
		this.isGatherFinish = isGatherFinish;
	}

	public Set getAlyFactDimRelationTables() {
		return this.alyFactDimRelationTables;
	}

	public void setAlyFactDimRelationTables(Set alyFactDimRelationTables) {
		this.alyFactDimRelationTables = alyFactDimRelationTables;
	}

}