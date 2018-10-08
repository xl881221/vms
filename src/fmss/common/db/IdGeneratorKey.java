package fmss.common.db;

import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class IdGeneratorKey extends JdbcDaoSupport {
	private long keyMax;
	private long keyMin;
	private long nextKey;
	private int poolSize;
	private String keyName;

	/**
	 * ���캯��
	 */
	public IdGeneratorKey() {
	};

	/**
	 * ���캯��
	 */
	public IdGeneratorKey(int poolSize, String keyName, DataSource dataSource) {
		this.poolSize = poolSize;
		this.keyName = keyName;
		this.setDataSource(dataSource);
		retrieveFromDB();
	}

	/**
	 * ȡֵ�������ṩ�������ֵ
	 */
	public long getKeyMax() {
		return keyMax;
	}

	/**
	 * ȡֵ�������ṩ������Сֵ
	 */
	public long getKeyMin() {
		return keyMin;
	}

	/**
	 * ȡֵ�������ṩ���ĵ�ǰֵ
	 */
	public long getNextKey() {
		if (nextKey > keyMax) {
			retrieveFromDB();
		}
		return nextKey++;
	}

	/**
	 * �ڲ������������ݿ���ȡ���ĵ�ǰֵ
	 */
	private void retrieveFromDB() {

		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(
				this.getDataSource());
		TransactionDefinition definition = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager
				.getTransaction(definition);
		Object[] params = new Object[] { keyName };
		try {
			String update = "UPDATE U_BASE_FORM_NO SET CURRENT_ID=CURRENT_ID + "
					+ poolSize + " WHERE FORM_TYPE=?";
			this.getJdbcTemplate().update(update, params);
			String select = "SELECT CURRENT_ID  FROM U_BASE_FORM_NO WHERE FORM_TYPE=?";
			long keyFromDB = this.getJdbcTemplate()
					.queryForLong(select, params);
			transactionManager.commit(status);
			keyMax = keyFromDB;
			keyMin = keyFromDB - poolSize + 1;
			nextKey = keyMin;
		} catch (Exception e) {
			transactionManager.rollback(status);
		}
		;
	}

	/**
	 * <p>
	 * ��������: resetId|����: ���ݵ�ǰ������ID���ֵ���������ݿ��и����͵�IDֵ
	 * </p>
	 * 
	 * @param currentId
	 */
	public void resetId(long currentId) {
		Object[] params = new Object[] { new Long(currentId), keyName };
		String updateSql = "UPDATE U_BASE_FORM_NO SET CURRENT_ID=? WHERE FORM_TYPE=?";
		this.getJdbcTemplate().update(updateSql, params);
	};

	/**
	 * <p>
	 * ��������: resetAllId|����: �������ݿ�����������IDֵΪ��
	 * </p>
	 */
	public void resetAllId() {
		this.getJdbcTemplate().execute(
				"UPDATE  U_BASE_FORM_NO SET CURRENT_ID=0");
	};

	/**
	 * <p>
	 * ��������: rebuildGenerator|����: �����ֵ��FNT���ͣ��ع���ű�
	 * </p>
	 */
	public void rebuildGenerator() {
		this.getJdbcTemplate().execute("DELETE FROM U_BASE_FORM_NO");
		List dictFnt = getJdbcTemplate().queryForList(
				"SELECT DIC_VALUE FROM U_BASE_DICTIONARY WHERE DIC_TYPE='FNT'");
		for (int i = 0; i < dictFnt.size(); i++) {
			this.getJdbcTemplate().execute(
					"INSERT INTO U_BASE_FORM_NO VALUES('"
							+ ((ListOrderedMap) dictFnt.get(i))
									.get("DIC_VALUE") + "',0)");
		}
	};
}
