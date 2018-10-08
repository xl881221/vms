package fmss.dao.entity;

/**
 * ExtentBase entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class ExtentBase implements java.io.Serializable {

	// Fields

	private ExtentBaseId id;

	// Constructors

	/** default constructor */
	public ExtentBase() {
	}

	/** full constructor */
	public ExtentBase(ExtentBaseId id) {
		this.id = id;
	}

	// Property accessors

	public ExtentBaseId getId() {
		return this.id;
	}

	public void setId(ExtentBaseId id) {
		this.id = id;
	}

}