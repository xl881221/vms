package fmss.action.base;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;

import fmss.action.entity.SubSystemAdminChangeDO;

public class SubSystemAdminAuditBase extends AuditBase {
	
	public static final String SUB_BAK_TABLE = "u_auth_system_admin_change";

	public static final String SUB_MAIN_TABLE = "u_auth_system_admin";

	public static String[] subSysFullAttributeFields = new String[] { "userId",
			"systemId", "changeUser", "changeTime", "auditUser", "auditTime",
			"auditStatus", "id", "changeStatus" ,"systemCname"};

	public static String[] subSysFullColumnFields = new String[] { "PARAM_ID",
			"SYSTEM_ID", "CHANGE_USER", "CHANGE_TIME", "AUDIT_USER",
			"AUDIT_TIME", "AUDIT_STATUS", "ID", "CHANGE_STATUS" ,"SYSTEM_CNAME"};
		
	public void auditThis(Object o) {
		SubSystemAdminChangeDO info = (SubSystemAdminChangeDO) o;
		dao.update("update " + SUB_BAK_TABLE
				+ " set audit_user=?,audit_time=?,audit_status=? where user_id=? and system_id=?",
				new Object[] { info.getAuditUser(), info.getAuditTime(),
						info.getAuditStatus(), info.getUserId(),info.getSystemId() });
	}

	public void flush2Prime(Object o) {
		SubSystemAdminChangeDO info = (SubSystemAdminChangeDO) o;
		String sql = "select count(*) from " + SUB_BAK_TABLE
				+ " where audit_status=" + AUDIT_STATUS_NOADUITED + " and user_id=? ";
		// 存在未审核对象,更新
		int count = dao.findForInt(sql, new Object[] { info.getUserId()});
		if (count > 0) {
			DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(
					dao.getDataSource());
			TransactionDefinition definition = new DefaultTransactionDefinition();
			TransactionStatus status = transactionManager
					.getTransaction(definition);
			try {
			sql ="select * from "+SUB_BAK_TABLE+" where system_id=? and user_id=?";
			List list = null;String nuserId="";String nsystemId="";
			list =dao.find(sql, new Object[] {info.getSystemId(),info.getUserId() });	
			if (CollectionUtils.isNotEmpty(list)) {
				Map map = (Map) list.get(0);
				nuserId =(String)map.get("user_id");
				nsystemId =(String)map.get("system_id");
				sql ="insert into "+SUB_MAIN_TABLE+"( user_id ,system_id) values (? ,?)";
	            dao.update(sql, new Object[] {nuserId,nsystemId});	
			}
			} catch (RuntimeException e) {
				transactionManager.rollback(status);
				throw new AuditException("flush2Prime error", e);
			}
		}
	}

	public void onSave(Object o) {
		Assert.isTrue(getColumnFields().length == getValueFields().length);
		String sql = "select count(*) from " + SUB_BAK_TABLE
				+ " where audit_status=" + AUDIT_STATUS_NOADUITED
				+ " and param_id=?";
		SubSystemAdminChangeDO user = (SubSystemAdminChangeDO) o;
		int count = dao.findForInt(sql, new Object[] { user.getUserId() });
		if (count > 0)
			throw new AuditException("存在未审核的系统参数信息:" + user.getUserId());
		dao.save(SUB_BAK_TABLE, o, getValueFields(), getColumnFields());
	}

}
