package fmss.action.base;

import java.util.Date;
import java.util.List;
import java.util.Map;

import fmss.action.entity.UBaseInstChangeDO;
import fmss.common.util.ArrayUtil;
import fmss.common.util.BeanUtil;
import fmss.common.util.Constants;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;


public class InstBaseAuditBase extends AuditBase {

	public static final String IBAK_TABLE = "u_base_inst_change";

	public static final String IMAIN_TABLE = "u_base_inst";
	
	public static String[] baseAddAttributeFields = new String[] { "instId", "instName", "instSmpName", "parentInstId",
		"instLayer", "address", "zip", "tel", "fax", "isBussiness", "orderNum", "description", "startDate","endDate", "createTime", 
		"enabled","email","instPath","instLevel" ,"isHead","instRegion"};
	
	private static String[] appendAttributeFields = new String[] { "changeUser", "changeTime", "auditUser",
		"auditTime", "auditStatus", "id", "changeStatus" };
	
	public static String[] fullBaseAddAttributeFields = ArrayUtil
	.concat(baseAddAttributeFields, appendAttributeFields);
	
	
	public static String[] baseAddColumnFields = new String[] {"INST_ID", "INST_NAME", "INST_SMP_NAME", "PARENT_INST_ID",
		"INST_LAYER", "ADDRESS", "ZIP", "TEL", "FAX", "IS_BUSSINESS", "ORDER_NUM", "DESCRIPTION", "START_DATE","END_DATE", "CREATE_TIME", 
		"ENABLED", "EMAIL" ,"INST_PATH","INST_LEVEL" ,"IS_HEAD","INST_REGION"};
	
	private static String[] appendColumnFields = new String[] { "CHANGE_USER", "CHANGE_TIME", "AUDIT_USER",
		"AUDIT_TIME", "AUDIT_STATUS", "ID", "CHANGE_STATUS" };
	
	public static String[] fullBaseAddColumnFields = ArrayUtil.concat(baseAddColumnFields, appendColumnFields);


	public void auditThis(Object o) {
		UBaseInstChangeDO info = (UBaseInstChangeDO) o;
		dao.update("update " + IBAK_TABLE
				+ " set audit_user=?,audit_time=?,audit_status=? where inst_id=?",
				new Object[] { info.getAuditUser(), info.getAuditTime(),
						info.getAuditStatus(), info.getInstId()});
	}

	public void flush2Prime(Object o) {
		Assert.isTrue(getColumnFields().length == getValueFields().length);
		Assert.notNull(login, "登录参数不能为空");
		UBaseInstChangeDO obj = (UBaseInstChangeDO) o;
		String sql = "select count(*) from " + IBAK_TABLE
				+ " where audit_status=" + AUDIT_STATUS_NOADUITED
				+ " and inst_id=? and change_user!=?";
		// 存在未审核对象,更新
		int count = dao.findForInt(sql, new Object[] { obj.getInstId(),
				login.getUserId() });
		
		if (count > 0) {
			DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(
					dao.getDataSource());
			TransactionDefinition definition = new DefaultTransactionDefinition();
			TransactionStatus status = transactionManager
					.getTransaction(definition);
			try {
				// 若是新增则直接添加，否则就是修改update
				sql="select count(*) from " + IMAIN_TABLE
				+ " where  inst_id=? " ;
				int count2 = dao.findForInt(sql, new Object[] { obj.getInstId()});
				if(count2>0){
					//
					String sql2 = "select * from " + InstBaseAuditBase.IBAK_TABLE
					+ " where INST_Id=? ";
					List list = null;
					list = dao.find(sql2, new Object[] { obj.getInstId() });
					if (CollectionUtils.isNotEmpty(list)) {
						Map map = (Map) list.get(0);
						obj = (UBaseInstChangeDO) BeanUtil
								.reflectToFillValue(UBaseInstChangeDO.class, map, ArrayUtil.concat(baseAddAttributeFields, appendAttributeFields), 
										ArrayUtil.concat(baseAddColumnFields, appendColumnFields));
						obj.setInstName(map.get("inst_name") != null ? map.get("inst_name").toString() : "");
						obj.setInstLayer(map.get("INST_LAYER") != null ? Integer.valueOf(map.get("INST_LAYER").toString()) : new Integer(0));
						obj.setInstLevel(map.get("INST_LEVEL") != null ? Integer.valueOf(map.get("INST_LEVEL").toString()) : new Integer(0));
						obj.setOrderNum(map.get("ORDER_NUM") != null ? Integer.valueOf(map.get("ORDER_NUM").toString()) : new Integer(0));
						
					}
					sql ="update "+IMAIN_TABLE+" set PARENT_INST_ID=?,INST_NAME=?,INST_SMP_NAME=?,INST_LAYER=?,ADDRESS=?,ZIP=?," +
							"TEL=?,FAX=?,IS_BUSSINESS=?,ORDER_NUM=?,DESCRIPTION=?,START_DATE=?,END_DATE=?,CREATE_TIME=?,ENABLED=?" +
							",EMAIL=?,INST_PATH=?,INST_LEVEL=?,IS_HEAD=?,INST_REGION=?" +
							" where  INST_ID=? ";
				dao.update(sql, new Object[] {obj.getParentInstId(),obj.getInstName(),obj.getInstSmpName(),obj.getInstLayer(),obj.getAddress(),
						obj.getZip(),obj.getTel(),obj.getFax(),obj.getIsBussiness(),obj.getOrderNum(),obj.getDescription(),obj.getStartDate(),
						obj.getEndDate(),obj.getCreateTime(),obj.getEnabled(),obj.getEmail(),obj.getInstPath(),obj.getInstLevel(),obj.getIsHead(),
						obj.getInstRegion(), obj.getInstId() });	
				}else{
				sql = "insert into "
						+ IMAIN_TABLE
						+ "(INST_ID,INST_NAME,INST_SMP_NAME,PARENT_INST_ID,INST_LAYER,ADDRESS,ZIP,TEL," +
								"FAX,IS_BUSSINESS,ORDER_NUM,DESCRIPTION,START_DATE,END_DATE,CREATE_TIME,ENABLED,EMAIL,INST_PATH,INST_LEVEL,IS_HEAD,INST_REGION)" 
						+" select distinct INST_ID,INST_NAME,INST_SMP_NAME,PARENT_INST_ID,INST_LAYER,ADDRESS,ZIP,TEL," +
								"FAX,IS_BUSSINESS,ORDER_NUM,DESCRIPTION,START_DATE,END_DATE,CREATE_TIME,ENABLED,EMAIL,INST_PATH,INST_LEVEL,IS_HEAD,INST_REGION from "
						+ IBAK_TABLE
						+ " where inst_id=? and change_status=? and change_user!=? and audit_status=? and (inst_id) "
						+ " not in(select inst_id from "
						+ IMAIN_TABLE + " where inst_id=?)";
				dao.update(sql, new Object[] { obj.getInstId(),
						CHANGE_TYPE_INST_ADD,
						login.getUserId(), new Long(AUDIT_STATUS_NOADUITED),
						obj.getInstId() });
				}
				transactionManager.commit(status);
			} catch (Exception e) {
				transactionManager.rollback(status);
				throw new AuditException("flush2Prime error", e);
			}
		}
	}

	public void onSave(Object o) {
		Assert.isTrue(getColumnFields().length == getValueFields().length);
		UBaseInstChangeDO inst = (UBaseInstChangeDO) o;
		String sql2 = "select count(*) from " + IBAK_TABLE
		+" where inst_id=?";
		int count2 = dao.findForInt(sql2, new Object[] {inst.getInstId()});
		if(count2>0) {
			String sql3 ="delete from "+ IBAK_TABLE+" where inst_id=?";
			dao.update(sql3,  new Object[] {inst.getInstId()});
		}
		dao.save(IBAK_TABLE, o, getValueFields(), getColumnFields());
	}
}
