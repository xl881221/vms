package fmss.dao.entity;

/**
 * AlySubjectMeta entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AlySubjectMeta implements java.io.Serializable {

	// Fields

	private Long id;
	private AlyPurviewMeta alyPurviewMeta;
	private String subjectCode;
	private String subjectName;
	private String subjectDes;
	private String subjectIconClass;
	private String partUserPurview;

	// Constructors

	/** default constructor */
	public AlySubjectMeta() {
	}

	/** minimal constructor */
	public AlySubjectMeta(AlyPurviewMeta alyPurviewMeta, String subjectCode,
			String subjectName) {
		this.alyPurviewMeta = alyPurviewMeta;
		this.subjectCode = subjectCode;
		this.subjectName = subjectName;
	}

	/** full constructor */
	public AlySubjectMeta(AlyPurviewMeta alyPurviewMeta, String subjectCode,
			String subjectName, String subjectDes, String subjectIconClass,
			String partUserPurview) {
		this.alyPurviewMeta = alyPurviewMeta;
		this.subjectCode = subjectCode;
		this.subjectName = subjectName;
		this.subjectDes = subjectDes;
		this.subjectIconClass = subjectIconClass;
		this.partUserPurview = partUserPurview;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AlyPurviewMeta getAlyPurviewMeta() {
		return this.alyPurviewMeta;
	}

	public void setAlyPurviewMeta(AlyPurviewMeta alyPurviewMeta) {
		this.alyPurviewMeta = alyPurviewMeta;
	}

	public String getSubjectCode() {
		return this.subjectCode;
	}

	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}

	public String getSubjectName() {
		return this.subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getSubjectDes() {
		return this.subjectDes;
	}

	public void setSubjectDes(String subjectDes) {
		this.subjectDes = subjectDes;
	}

	public String getSubjectIconClass() {
		return this.subjectIconClass;
	}

	public void setSubjectIconClass(String subjectIconClass) {
		this.subjectIconClass = subjectIconClass;
	}

	public String getPartUserPurview() {
		return this.partUserPurview;
	}

	public void setPartUserPurview(String partUserPurview) {
		this.partUserPurview = partUserPurview;
	}

}