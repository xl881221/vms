package fmss.dao.entity;


public class UAuthRoleVO extends UAuthRoleDO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UBaseConfigDO uBaseConfig;
	public UBaseConfigDO getUBaseConfig() {
		return uBaseConfig;
	}
	public void setUBaseConfig(UBaseConfigDO baseConfig) {
		uBaseConfig = baseConfig;
	}
}
