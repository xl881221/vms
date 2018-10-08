package fmss.dao.config;

public interface Dialect {

	public void registerUserDefinedTypes(String typeName, Class dataType);

	/**
	 * 
	 * @param sql
	 * @param offset
	 *            no offset = 0, skip one = 1, ...
	 * @param limit
	 */
	public String getLimitedString(String sql, int offset, int limit);

	public String getForUpdateString(String sql);

	public String getForUpdateNoWaitString(String sql);

	public String getRowCountString(String sql);
}
