package fmss.action.base;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;

import fmss.action.entity.UserRoleChangeDO;

public class UserRoleAuditBase extends AuditBase {

	public static final String BAK_TABLE = "u_auth_role_user_change";

	public static final String MAIN_TABLE = "u_auth_role_user";

	public void auditThis(Object o) {
		UserRoleChangeDO obj = (UserRoleChangeDO) o;
		Assert.notNull(login, "��¼��������Ϊ��");
		String sql = "update " + BAK_TABLE
				+ " set audit_user=?,audit_time=?,audit_status=? where user_id=? and audit_status=? ";
		if (AuditBase.AUDIT_STATUS_CANCEL != obj.getAuditStatus().intValue())
			sql += " and change_user!='" + login.getUserId() + "'";
		// ����ֻ���Լ���
		else
			sql += " and change_user='" + login.getUserId() + "'";
		sql += appendHaveSystemAuthsRangeSQL(login);
		dao.update(sql, new Object[] { obj.getAuditUser(), obj.getAuditTime(), obj.getAuditStatus(), obj.getUserId(),
				new Long(AuditBase.AUDIT_STATUS_NOADUITED) });
	}

	public void flush2Prime(Object o) {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dao.getDataSource());
		TransactionDefinition definition = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(definition);
		try {
			Assert.isTrue(getColumnFields().length == getValueFields().length);
			Assert.notNull(login, "��¼��������Ϊ��");
			UserRoleChangeDO obj = (UserRoleChangeDO) o;
			String sql = "select count(*) from " + BAK_TABLE + " where audit_status=" + AUDIT_STATUS_NOADUITED
					+ " and user_id=?" + appendHaveSystemAuthsRangeSQL(login);

			// ����δ��˶���,����
			int count = dao.findForInt(sql, new Object[] { obj.getUserId() });
			if (count > 0) {
				// ��������ӣ�����ɾ��δ��״̬
				sql = "delete from " + MAIN_TABLE + " where exists (select 1 from "
						+ BAK_TABLE + " a where a.role_id=" + MAIN_TABLE + ".role_id and a.user_id=" + MAIN_TABLE + ".user_id and a.user_id=? and a.change_status=? and a.change_user!=? and a.audit_status=?)"
						+ appendHaveSystemAuthsRangeSQL(login);
				// transaction here.
				dao.update(sql, new Object[] { obj.getUserId(), new Long(USER_ROLE_CHANGE_STATUS_DELETE),
						login.getUserId(), new Long(AUDIT_STATUS_NOADUITED) });
				// δ������+���Լ��ύ
				sql = "insert into "
						+ MAIN_TABLE
						+ "(user_id,role_id) select distinct user_id,role_id from "
						+ BAK_TABLE
						+ " a where user_id=? and change_status=? and change_user!=? and audit_status=? and not exists (select 1 from "
						+ MAIN_TABLE + " b where a.user_id=b.user_id and a.role_id=b.role_id) and role_id in(select role_id from u_auth_role)"
						+ appendHaveSystemAuthsRangeSQL(login);
				dao.update(sql, new Object[] { obj.getUserId(), new Long(USER_ROLE_CHANGE_STATUS_ADD),
						login.getUserId(), new Long(AUDIT_STATUS_NOADUITED) });
				transactionManager.commit(status);
			} else {
				logger.info("��������Ҫ�Լ���˵ļ�¼,rangesql:" + appendHaveSystemAuthsRangeSQL(login));
			}
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw new AuditException("flush2Prime error", e);
		}
	}

	public void onSave(Object o) {
		Assert.isTrue(getColumnFields().length == getValueFields().length);
		// TODO
		// �������������,key(role_id,user_id,change_user,change_status,audit_status)
		dao.save(BAK_TABLE, o, getValueFields(), getColumnFields());
	}

}
