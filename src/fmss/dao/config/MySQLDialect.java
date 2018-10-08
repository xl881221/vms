package fmss.dao.config;

public class MySQLDialect implements Dialect {

	public String getForUpdateNoWaitString(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getForUpdateString(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getLimitedString(String sql, int offset, int limit) {
		sql = sql.trim();
		String sql2 = sql.toLowerCase();

		boolean isForUpdate = false;

		if (sql2.endsWith(" for update")) {
			sql = sql.substring(0, sql.length() - 11);
			isForUpdate = true;
		}

		StringBuffer sb = new StringBuffer(sql.length() + 16);
		sb.append(sql);

		if (offset <= 0) {
			if (limit >= Integer.MAX_VALUE) { // 读取所有，不需要Limit.

			} else {
				sb.append(" limit ").append(limit);
			}
		} else {
			sb.append(" limit ").append((offset)).append(", ").append(limit);
		}

		if (isForUpdate) {
			sb.append(" for update");
		}

		return sb.toString();
	}

	public void registerUserDefinedTypes(String typeName, Class dataType) {
		// TODO Auto-generated method stub

	}

	public String getRowCountString(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

}
