package fmss.action.base;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import fmss.common.util.BeanUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.support.JdbcDaoSupport;


public class JdbcDaoAccessor extends JdbcDaoSupport {

	public void save(String tableName, Object obj, String[] valueFields, String[] columnFields) {
		String sql = "insert into " + tableName + "(";
		String placeHolder = "(";
		Object[] parameters = new Object[valueFields.length];
		for (int i = 0; i < valueFields.length; i++) {
			sql += columnFields[i] + ",";
			placeHolder += "?,";
			parameters[i] = BeanUtil.getProperty(obj, valueFields[i]);
			Class cls = BeanUtils.findPropertyType(valueFields[i], new Class[] { obj.getClass() });
			
			// 在db2下某些字段值为NULL导致JDBC插入时报错
			// error code [-4228]; [jcc][10271][10295][3.59.81] 无法识别 JDBC 类型：0
			if (cls.getName().equals(String.class.getName()) && parameters[i] == null) {
				parameters[i] = StringUtils.EMPTY;
			}
		}
		if (sql.endsWith(",")) {
			sql = sql.substring(0, sql.length() - 1);
		}
		sql += ") values";
		if (placeHolder.endsWith(",")) {
			placeHolder = placeHolder.substring(0, placeHolder.length() - 1);
		}
		placeHolder += ")";
		logger.info(sql + placeHolder);
		if(tableName!=null&&tableName.equals("u_base_user_change")){
			if(parameters[19]==""){ 
				parameters[19]=new Double(0) ;
			}
			else if(parameters[19]!=null){
				parameters[19] = new Double(Double.parseDouble(parameters[19].toString())) ;
			}
		}
		this.getJdbcTemplate().update(sql + placeHolder, parameters);
	}

	public Object find(String tableName, Object id) {
		String sql = "select * from " + tableName + " where id = ?";
		List list = this.getJdbcTemplate().queryForList(sql, new Object[] { id });
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	public Map findForMap(String sql, Object[] parameters) {
		List list = this.getJdbcTemplate().queryForList(sql, parameters);
		if (CollectionUtils.isNotEmpty(list)) {
			return (Map) list.get(0);
		}
		return null;
	}

	public int update(String mainTable, Object obj, String[] valueFields, String[] columnFields, String append) {
		Map info = (Map) obj;
		String sql = "update " + mainTable + " set ";
		Object[] parameters = new Object[valueFields.length];
		for (int i = 0; i < valueFields.length; i++) {
			parameters[i] = info.get(columnFields[i]);
			sql += columnFields[i] + "=?,";
		}
		if (sql.endsWith(",")) {
			sql = sql.substring(0, sql.length() - 1);
		}
		logger.info(sql + append);
		return this.getJdbcTemplate().update(sql + append, parameters);
	}

	public int update(String sql, Object[] parameters) {
		logger.info(sql);
		return this.getJdbcTemplate().update(sql, parameters);
	}

	public void insert(String sql, Object[] parameters) {
		logger.info(sql);
		this.getJdbcTemplate().update(sql, parameters);
	}

	public List find(String sql, Object[] parameters) {
		logger.info(sql);
		List list = this.getJdbcTemplate().queryForList(sql, parameters);
		return list != null ? list : Collections.EMPTY_LIST;
	}

	public List find(String sql) {
		logger.info(sql);
		List list = this.getJdbcTemplate().queryForList(sql);
		return list != null ? list : Collections.EMPTY_LIST;
	}

	public int findForInt(String sql, Object[] parameters) {
		logger.info(sql);
		return this.getJdbcTemplate().queryForInt(sql, parameters);
	}
	
	public int findForHoliday(String sql, Object[] parameters) {
		logger.info(sql);
		return this.getJdbcTemplate().queryForInt(sql, parameters);
	}

	public int findForInt(String sql) {
		logger.info(sql);
		return this.getJdbcTemplate().queryForInt(sql);
	}

	public long getRowCount(String sql, Object[] parameters) {
		StringBuffer countSql = new StringBuffer("select count(*) from (");
		countSql.append(sql);
		countSql.append(") counts");
		return this.getJdbcTemplate().queryForLong(countSql.toString(), parameters);
	}

	public long getRowCount(String sql) {

		StringBuffer countSql = new StringBuffer("select count(*) from (");
		countSql.append(sql);
		countSql.append(") counts");
		return this.getJdbcTemplate().queryForLong(countSql.toString());
	}

	public long findForLong(String sql) {
		logger.info(sql);
		return this.getJdbcTemplate().queryForLong(sql);
	}
}
