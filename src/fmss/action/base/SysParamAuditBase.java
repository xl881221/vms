package fmss.action.base;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;

import fmss.action.entity.UBaseInstChangeDO;
import fmss.action.entity.UBaseSysParamChangeDO;
import fmss.common.util.ArrayUtil;
import fmss.common.util.BeanUtil;

public class SysParamAuditBase extends AuditBase {

	
	public static final String SPBAK_TABLE = "u_base_sys_param_change";

    public static final String SPMAIN_TABLE = "u_base_sys_param";
    
    public static String[] sysFullAttributeFields = new String[] { "paramId", "selectedValue","itemCname","changeUser", "changeTime",
		"auditUser", "auditTime", "auditStatus", "id", "changeStatus"};
	
	public static String[] sysFullColumnFields = new String[] { "PARAM_ID", "SELECTED_VALUE", "ITEM_CNAME","CHANGE_USER", "CHANGE_TIME",
		"AUDIT_USER", "AUDIT_TIME", "AUDIT_STATUS", "ID", "CHANGE_STATUS"};
	
	public void auditThis(Object o) {
		UBaseSysParamChangeDO info = (UBaseSysParamChangeDO) o;
		dao.update("update " + SysParamAuditBase.SPBAK_TABLE
				+ " set audit_user=?,audit_time=?,audit_status=? where param_id=?",
				new Object[] { info.getAuditUser(), info.getAuditTime(),
						info.getAuditStatus(), info.getParamId() });
	}

	public void flush2Prime(Object o) {
		UBaseSysParamChangeDO info = (UBaseSysParamChangeDO) o;
		String sql = "select count(*) from " + SPBAK_TABLE
				+ " where audit_status=" + AUDIT_STATUS_NOADUITED + " and param_id=?";
		// 存在未审核对象,更新
		int count = dao.findForInt(sql, new Object[] { info.getParamId() });
		if (count > 0) {
			DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(
					dao.getDataSource());
			TransactionDefinition definition = new DefaultTransactionDefinition();
			TransactionStatus status = transactionManager
					.getTransaction(definition);
			try {
		    //更新临时表
			sql ="update "+SPBAK_TABLE+" set SELECTED_VALUE=? where  PARAM_ID=? ";
			dao.update(sql, new Object[] {info.getSelectedValue(),info.getParamId() });	
			//更新主表
			sql ="update "+SPMAIN_TABLE+" set SELECTED_VALUE=? where  PARAM_ID=? ";
			dao.update(sql, new Object[] {info.getSelectedValue(),info.getParamId() });	
			} catch (RuntimeException e) {
				transactionManager.rollback(status);
				throw new AuditException("flush2Prime error", e);
			}
		}
	}

	public void onSave(Object o) {
		Assert.isTrue(getColumnFields().length == getValueFields().length);
		String sql = "select count(*) from " + SPBAK_TABLE
				+ " where audit_status=" + AUDIT_STATUS_NOADUITED
				+ " and param_id=?";
		UBaseSysParamChangeDO user = (UBaseSysParamChangeDO) o;
		int count = dao.findForInt(sql, new Object[] { user.getParamId() });
		if (count > 0)
			throw new AuditException("存在未审核的系统参数信息:" + user.getParamId());
		dao.save(SPBAK_TABLE, o, getValueFields(), getColumnFields());
	}
}
