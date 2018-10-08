package fmss.dao.entity;

public class UBaseFolderDO {
	
	private Long folderId;
	private Long parentFolderId;
	private UBaseConfigDO baseConfig;
	private String folderCode;
	private String folderName;
	private String folderPath;
	private Long folderLevel;
	private String display;
	

	public String getFolderPath() {
		return folderPath;
	}
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}
	
	public Long getFolderId() {
		return folderId;
	}
	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}
	public String getFolderCode() {
		return folderCode;
	}
	public void setFolderCode(String folderCode) {
		this.folderCode = folderCode;
	}
	public String getFolderName() {
		return folderName;
	}
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	public UBaseConfigDO getBaseConfig() {
		return baseConfig;
	}
	public void setBaseConfig(UBaseConfigDO baseConfig) {
		this.baseConfig = baseConfig;
	}
	public Long getParentFolderId() {
		return parentFolderId;
	}
	public void setParentFolderId(Long parentFolderId) {
		this.parentFolderId = parentFolderId;
	}
	public Long getFolderLevel() {
		return folderLevel;
	}
	public void setFolderLevel(Long folderLevel) {
		this.folderLevel = folderLevel;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}

}
