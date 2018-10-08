package fmss.dao.config;

public class MSSQLDialect implements Dialect {

	static int getAfterSelectInsertPoint(String sql) {
		int selectIndex = sql.toLowerCase().indexOf("select");
		final int selectDistinctIndex = sql.toLowerCase().indexOf(
				"select distinct");
		return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
	}

	public String getLimitedString(String querySelect, int offset, int limit) {
		if (offset > 0) {
			throw new UnsupportedOperationException(
					"query result offset is not supported");
		}
		querySelect = "select * from (" + querySelect + ") querys";
		return new StringBuffer(querySelect.length() + 8)
				.append(querySelect)
				.insert(getAfterSelectInsertPoint(querySelect), " top " + limit)
				.toString();
	}

	public String getForUpdateNoWaitString(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getForUpdateString(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	public void registerUserDefinedTypes(String typeName, Class dataType) {
		// TODO Auto-generated method stub

	}

	public String getRowCountString(String sql) {
		StringBuffer sb = new StringBuffer("select count(*) from (");
		sb.append(sql);
		sb.append(") counts_i38783232");
		return sb.toString();
	}

}
