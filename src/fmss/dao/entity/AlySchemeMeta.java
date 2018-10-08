package fmss.dao.entity;

/**
 * AlySchemeMeta entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class AlySchemeMeta implements java.io.Serializable {

	// Fields

	private Long id;
	private AlyPurviewMeta alyPurviewMeta;
	private Long subjectId;
	private String schemeCode;
	private String schemeName;
	private String schemeDes;
	private String schemeCreator;
	private String schemeDate;
	private String schemeContext;
	private String schemeIconClass;
	private String partUserPurview;
	private String schemeType;
	private String isfirstpage;
	private String defalutDatadate;

	// Constructors

	/** default constructor */
	public AlySchemeMeta() {
	}

	/** minimal constructor */
	public AlySchemeMeta(AlyPurviewMeta alyPurviewMeta, Long subjectId,
			String schemeCode, String schemeName) {
		this.alyPurviewMeta = alyPurviewMeta;
		this.subjectId = subjectId;
		this.schemeCode = schemeCode;
		this.schemeName = schemeName;
	}

	/** full constructor */
	public AlySchemeMeta(AlyPurviewMeta alyPurviewMeta, Long subjectId,
			String schemeCode, String schemeName, String schemeDes,
			String schemeCreator, String schemeDate, String schemeContext,
			String schemeIconClass, String partUserPurview, String schemeType,
			String isfirstpage, String defalutDatadate) {
		this.alyPurviewMeta = alyPurviewMeta;
		this.subjectId = subjectId;
		this.schemeCode = schemeCode;
		this.schemeName = schemeName;
		this.schemeDes = schemeDes;
		this.schemeCreator = schemeCreator;
		this.schemeDate = schemeDate;
		this.schemeContext = schemeContext;
		this.schemeIconClass = schemeIconClass;
		this.partUserPurview = partUserPurview;
		this.schemeType = schemeType;
		this.isfirstpage = isfirstpage;
		this.defalutDatadate = defalutDatadate;
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

	public Long getSubjectId() {
		return this.subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public String getSchemeCode() {
		return this.schemeCode;
	}

	public void setSchemeCode(String schemeCode) {
		this.schemeCode = schemeCode;
	}

	public String getSchemeName() {
		return this.schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public String getSchemeDes() {
		return this.schemeDes;
	}

	public void setSchemeDes(String schemeDes) {
		this.schemeDes = schemeDes;
	}

	public String getSchemeCreator() {
		return this.schemeCreator;
	}

	public void setSchemeCreator(String schemeCreator) {
		this.schemeCreator = schemeCreator;
	}

	public String getSchemeDate() {
		return this.schemeDate;
	}

	public void setSchemeDate(String schemeDate) {
		this.schemeDate = schemeDate;
	}

	public String getSchemeContext() {
		return this.schemeContext;
	}

	public void setSchemeContext(String schemeContext) {
		this.schemeContext = schemeContext;
	}

	public String getSchemeIconClass() {
		return this.schemeIconClass;
	}

	public void setSchemeIconClass(String schemeIconClass) {
		this.schemeIconClass = schemeIconClass;
	}

	public String getPartUserPurview() {
		return this.partUserPurview;
	}

	public void setPartUserPurview(String partUserPurview) {
		this.partUserPurview = partUserPurview;
	}

	public String getSchemeType() {
		return this.schemeType;
	}

	public void setSchemeType(String schemeType) {
		this.schemeType = schemeType;
	}

	public String getIsfirstpage() {
		return this.isfirstpage;
	}

	public void setIsfirstpage(String isfirstpage) {
		this.isfirstpage = isfirstpage;
	}

	public String getDefalutDatadate() {
		return this.defalutDatadate;
	}

	public void setDefalutDatadate(String defalutDatadate) {
		this.defalutDatadate = defalutDatadate;
	}

}