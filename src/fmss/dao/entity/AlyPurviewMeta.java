package fmss.dao.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * AlyPurviewMeta entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AlyPurviewMeta implements java.io.Serializable {

	// Fields

	private Long id;
	private String purviewCode;
	private String purviewName;
	private String isDefault;
	private Set alySubjectMetas = new HashSet(0);
	private Set alySchemeMetas = new HashSet(0);

	// Constructors

	/** default constructor */
	public AlyPurviewMeta() {
	}

	/** minimal constructor */
	public AlyPurviewMeta(String purviewCode, String purviewName) {
		this.purviewCode = purviewCode;
		this.purviewName = purviewName;
	}

	/** full constructor */
	public AlyPurviewMeta(String purviewCode, String purviewName,
			String isDefault, Set alySubjectMetas, Set alySchemeMetas) {
		this.purviewCode = purviewCode;
		this.purviewName = purviewName;
		this.isDefault = isDefault;
		this.alySubjectMetas = alySubjectMetas;
		this.alySchemeMetas = alySchemeMetas;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPurviewCode() {
		return this.purviewCode;
	}

	public void setPurviewCode(String purviewCode) {
		this.purviewCode = purviewCode;
	}

	public String getPurviewName() {
		return this.purviewName;
	}

	public void setPurviewName(String purviewName) {
		this.purviewName = purviewName;
	}

	public String getIsDefault() {
		return this.isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}

	public Set getAlySubjectMetas() {
		return this.alySubjectMetas;
	}

	public void setAlySubjectMetas(Set alySubjectMetas) {
		this.alySubjectMetas = alySubjectMetas;
	}

	public Set getAlySchemeMetas() {
		return this.alySchemeMetas;
	}

	public void setAlySchemeMetas(Set alySchemeMetas) {
		this.alySchemeMetas = alySchemeMetas;
	}

}