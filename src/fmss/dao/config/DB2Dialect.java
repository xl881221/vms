package fmss.dao.config;

public class DB2Dialect implements Dialect {
	
	public String getForUpdateNoWaitString(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getForUpdateString(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	//special for audit
	public String getLimitedString(String sql, int offset, int limit) {
		int startOfSelect = sql.toLowerCase().indexOf("select");
		StringBuffer pagingSelect = new StringBuffer( sql.length()+100 )
				.append( sql.substring(0, startOfSelect) )	// add the comment
				.append("select * from ( select ") 			// nest the main query in an outer select
				.append( getRowNumber(sql) ); 				// add the rownnumber bit into the outer query select list
		if ( hasDistinct(sql) ) {
			//TODO ...
			pagingSelect.append(sql).append(") temp_inner"); // add the main query
		}
		else {
			pagingSelect.append(sql).append(") temp_inner"); // add the main query
		}
		pagingSelect.append(" ) as temp_outer where rownumber_ ");
		//add the restriction to the outer select
		if (offset > 0) {
			pagingSelect.append("between ").append(offset + 1 ).append(" and ").append(offset + limit );
		}
		else {
			pagingSelect.append("<= ").append(limit);
		}

		return pagingSelect.toString();
	}

	public void registerUserDefinedTypes(String typeName, Class dataType) {
		// TODO Auto-generated method stub

	}

	private String getRowNumber(String sql) {
		StringBuffer rownumber = new StringBuffer(50).append("rownumber() over(");
		int orderByIndex = sql.toLowerCase().indexOf("order by");
		if (orderByIndex > 0 ) {
			rownumber.append(sql.substring(orderByIndex));
		}
		rownumber.append(") as rownumber_, temp_inner.* from (");
		return rownumber.toString();
	}
	
	public String getRowCountString(String sql) {
		StringBuffer sb = new StringBuffer("select count(*) from (");
		sb.append(sql);
		sb.append(") counts_i38783232");
		return sb.toString();
	}
	
	private static boolean hasDistinct(String sql) {
		return sql.toLowerCase().indexOf("select distinct")>=0;
	}

}
