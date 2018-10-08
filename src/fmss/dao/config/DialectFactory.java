package fmss.dao.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import fmss.action.base.AuditException;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;


public class DialectFactory {

	private static String DATABASE_TYPE = "";

	private static final Map types = new CaseInsensitiveMap();
	static {
		types.put("oracle", OracleDialect.class);
		types.put("MS-SQL", MSSQLDialect.class);
		types.put("Microsoft SQL Server", MSSQLDialect.class);
		types.put("DB2", DB2Dialect.class);
		types.put("mysql", MySQLDialect.class);
	}

	public static Dialect getDialect(DataSource ds) {
		Assert.notNull(ds);
		try {
			if (StringUtils.isEmpty(DATABASE_TYPE))
				DATABASE_TYPE = (String) new JdbcTemplate(ds).execute(new ConnectionCallback() {
					public Object doInConnection(Connection arg0) throws SQLException, DataAccessException {
						return arg0.getMetaData().getDatabaseProductName();
					}
				});
			if(DATABASE_TYPE.indexOf("/")!=-1){
                DATABASE_TYPE=DATABASE_TYPE.substring(0,DATABASE_TYPE.indexOf("/"));
            }
			Class cls = (Class) types.get(DATABASE_TYPE);
			return (Dialect) cls.newInstance();
		} catch (Exception e) {
			throw new AuditException("getDialect error", e);
		}
	}
}
