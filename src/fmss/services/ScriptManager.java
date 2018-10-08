package fmss.services;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

public class ScriptManager extends JdbcDaoSupport {

	private String separator;

	private String sql;

	public Set getNames(Set names) {
		final Set scripts = new HashSet();
		getJdbcTemplate().query(sql, names.toArray(), new RowCallbackHandler() {
			public void processRow(ResultSet rs) throws SQLException {
				String s = rs.getString(1);
				if (!scripts.contains(s) && StringUtils.isNotEmpty(s))
					scripts.add(s.trim());
			}
		});
		Collection c = CollectionUtils.intersection(scripts, names);
		Set results = new HashSet();
		if (!c.isEmpty()) {
			for (Iterator iterator = c.iterator(); iterator.hasNext();) {
				String s = (String) iterator.next();
				if (!scripts.contains(s) && StringUtils.isNotEmpty(s))
					results.add(s);
			}
		}
		return results;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

}
