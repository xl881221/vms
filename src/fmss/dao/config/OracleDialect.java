package fmss.dao.config;

public class OracleDialect implements Dialect {

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
		boolean isForUpdateNoWait = false;

		if (sql2.endsWith(" for update")) {
			sql = sql.substring(0, sql.length() - 11);
			isForUpdate = true;
		} else if (sql2.endsWith(" for update nowait")) {
			sql = sql.substring(0, sql.length() - 18);
			isForUpdateNoWait = true;
		}

		StringBuffer sb = new StringBuffer(sql.length() + 128);

		if (offset > 0) {
			sb.append("select * from ( select row_.*, rownum rownum_ from ( ");
		} else {
			sb.append("select * from ( ");
		}

		sb.append(sql);

		if (offset > 0) {
			sb.append(" ) row_ where rownum <= ").append(limit + offset)
					.append(") where rownum_ > ").append(offset);
		} else {
			sb.append(" ) where rownum <= ").append(limit);
		}

		if (isForUpdate) {
			sb.append(" for update");
		} else if (isForUpdateNoWait) {
			sb.append(" for update nowait");
		}

		return sb.toString();
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
