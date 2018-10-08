package fmss.action.base;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;

import fmss.action.entity.RoleResourceChangeDO;

public class RoleResourceAuditBase extends AuditBase {

	public static final String BAK_TABLE = "u_auth_role_resource_change";

	public static final String MAIN_TABLE = "u_auth_role_resource";

	public void auditThis(Object o) {
		RoleResourceChangeDO obj = (RoleResourceChangeDO) o;
		Assert.notNull(login, "登录参数不能为空");
		String sql = "update "
				+ BAK_TABLE
				+ " set audit_user=?,audit_time=?,audit_status=? where object_id=? and audit_status=? ";
		//审核不审自己的
		if (AuditBase.AUDIT_STATUS_CANCEL != obj.getAuditStatus().intValue())
			sql += " and change_user!='" + login.getUserId() + "'";
		//撤回只撤自己的
		else
			sql += " and change_user='" + login.getUserId() + "'";
		dao.update(sql, new Object[] { obj.getAuditUser(), obj.getAuditTime(),
				obj.getAuditStatus(), obj.getObjectId(),
				new Long(AuditBase.AUDIT_STATUS_NOADUITED) });
	}

	public void flush2Prime(Object o) {
		Assert.isTrue(getColumnFields().length == getValueFields().length);
		Assert.notNull(login, "登录参数不能为空");
		RoleResourceChangeDO obj = (RoleResourceChangeDO) o;
		String sql = "select count(*) from " + BAK_TABLE
				+ " where audit_status=" + AUDIT_STATUS_NOADUITED
				+ " and object_id=? and change_user!=?";
		// 存在未审核对象,更新
		int count = dao.findForInt(sql, new Object[] { obj.getObjectId(),
				login.getUserId() });
		if (count > 0) {
			DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(
					dao.getDataSource());
			TransactionDefinition definition = new DefaultTransactionDefinition();
			TransactionStatus status = transactionManager
					.getTransaction(definition);
			try {
				// 清除再增加
				sql = "delete from "
						+ MAIN_TABLE
						+ " where exists (select 1 from "
						+ BAK_TABLE
						+ " b where "+MAIN_TABLE+".object_id=b.object_id and "+MAIN_TABLE+".res_id=b.res_id and "+MAIN_TABLE+".res_detail_value=b.res_detail_value and "+MAIN_TABLE+".system_id=b.system_id" 
						+ " and b.object_id=? and b.change_status=? and b.change_user!=? and b.audit_status=?)";
				// transaction here.
				dao.update(sql, new Object[] { obj.getObjectId(),
						new Long(ROLE_RES_CHANGE_STATUS_DELETE),
						login.getUserId(), new Long(AUDIT_STATUS_NOADUITED) });
				sql = "insert into "
						+ MAIN_TABLE
						+ "(object_id,res_id,res_detail_value,res_detail_name,system_id) " 
						+"select distinct object_id,res_id,res_detail_value,res_detail_name,system_id from "
						+ BAK_TABLE
						+ " a where a.object_id=? and a.change_status=? and a.change_user!=? and a.audit_status=? " 
						+ " and  "
						+ " not exists(select 1 from "
						+ MAIN_TABLE 
						+ " b where object_id=? and a.object_id=b.object_id and a.res_id=b.res_id and a.system_id=b.system_id and a.res_detail_value=b.res_detail_value)";
				dao.update(sql, new Object[] { obj.getObjectId(),
						new Long(ROLE_RES_CHANGE_STATUS_ADD),
						login.getUserId(), new Long(AUDIT_STATUS_NOADUITED),
						obj.getObjectId() });
				transactionManager.commit(status);
			} catch (Exception e) {
				transactionManager.rollback(status);
				throw new AuditException("flush2Prime error", e);
			}
		}
	}

	public void onSave(Object o) {
		Assert.isTrue(getColumnFields().length == getValueFields().length);
		dao.save(BAK_TABLE, o, getValueFields(), getColumnFields());
	}

}
