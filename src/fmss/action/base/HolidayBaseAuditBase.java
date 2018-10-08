package fmss.action.base;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;

import fmss.action.entity.HolidayChangeDO;
import fmss.action.entity.UBaseInstChangeDO;
import fmss.common.util.ArrayUtil;
import fmss.common.util.BeanUtil;


public class HolidayBaseAuditBase extends AuditBase {

	public static final String HTTBAK_TABLE = "u_base_holiday_type_change";

	public static final String HT_TABLE = "u_base_holiday_type";
	
	public static final String HTBAK_TABLE = "u_base_holiday_change";
	
	public static final String H_TABLE = "u_base_holiday";
	
	private static String[] baseAddHTAttributeFields = new String[] { "holidayType", "holidayName", "enable", "remark" };
	
	private static String[] baseAddHTColumnFields = new String[] { "HOLIDAY_TYPE", "HOLIDAY_NAME", "ENABLE", "REMARK" };

	private static String[] appendAttributeFields = new String[] { "changeUser", "changeTime", "auditUser",
		"auditTime", "auditStatus", "id", "changeStatus" };
	
	private static String[] appendColumnFields = new String[] { "CHANGE_USER", "CHANGE_TIME", "AUDIT_USER",
		"AUDIT_TIME", "AUDIT_STATUS", "ID", "CHANGE_STATUS" };
	
	public static String[] fullBaseAddAttributeFields = ArrayUtil
	.concat(baseAddHTAttributeFields, appendAttributeFields);
	
	
	public static String[] fullBaseAddColumnFields = ArrayUtil.concat(baseAddHTColumnFields, appendColumnFields);


	public void deleteThis(Object o) {
		HolidayChangeDO info = (HolidayChangeDO) o;
		dao.update("delete " + HTBAK_TABLE
				+ " where holiday_type=? and holiday_value=?",
				new Object[] {info.getHolidayType(),info.getHolidayValue()});
	}
	
	public void auditThis(Object o) {
		HolidayChangeDO info = (HolidayChangeDO) o;
		dao.update("update " + HTBAK_TABLE
				+ " set audit_user=?,audit_time=?,audit_status=? where holiday_type=?",
				new Object[] { info.getAuditUser(), info.getAuditTime(),
				info.getAuditStatus(), info.getHolidayType()});
	}

	public void flush2Prime(Object o) {
		Assert.isTrue(getColumnFields().length == getValueFields().length);
		Assert.notNull(login, "登录参数不能为空");
		UBaseInstChangeDO obj = (UBaseInstChangeDO) o;
		String sql = "select count(*) from " + HTBAK_TABLE
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
				sql="select count(*) from " + H_TABLE
				+ " where  inst_id=? " ;
				int count2 = dao.findForInt(sql, new Object[] { obj.getInstId()});
				if(count2>0){
					//
					String sql2 = "select * from " + HTBAK_TABLE
					+ " where INST_Id=? ";
					List list = null;
					list = dao.find(sql2, new Object[] { obj.getInstId() });
					if (CollectionUtils.isNotEmpty(list)) {
						Map map = (Map) list.get(0);
						obj = (UBaseInstChangeDO) BeanUtil
								.reflectToFillValue(UBaseInstChangeDO.class, map, ArrayUtil.concat(baseAddHTColumnFields, appendAttributeFields), 
										ArrayUtil.concat(baseAddHTColumnFields, appendColumnFields));
						obj.setInstName(map.get("inst_name") != null ? map.get("inst_name").toString() : "");
						obj.setInstLayer(map.get("INST_LAYER") != null ? Integer.valueOf(map.get("INST_LAYER").toString()) : new Integer(0));
						obj.setInstLevel(map.get("INST_LEVEL") != null ? Integer.valueOf(map.get("INST_LEVEL").toString()) : new Integer(0));
						obj.setOrderNum(map.get("ORDER_NUM") != null ? Integer.valueOf(map.get("ORDER_NUM").toString()) : new Integer(0));
						
					}
					sql ="update "+H_TABLE+" set PARENT_INST_ID=?,INST_NAME=?,INST_SMP_NAME=?,INST_LAYER=?,ADDRESS=?,ZIP=?," +
							"TEL=?,FAX=?,IS_BUSSINESS=?,ORDER_NUM=?,DESCRIPTION=?,START_DATE=?,END_DATE=?,CREATE_TIME=?,ENABLED=?" +
							",EMAIL=?,INST_PATH=?,INST_LEVEL=?,IS_HEAD=?,INST_REGION=?" +
							" where  INST_ID=? ";
				dao.update(sql, new Object[] {obj.getParentInstId(),obj.getInstName(),obj.getInstSmpName(),obj.getInstLayer(),obj.getAddress(),
						obj.getZip(),obj.getTel(),obj.getFax(),obj.getIsBussiness(),obj.getOrderNum(),obj.getDescription(),obj.getStartDate(),
						obj.getEndDate(),obj.getCreateTime(),obj.getEnabled(),obj.getEmail(),obj.getInstPath(),obj.getInstLevel(),obj.getIsHead(),
						obj.getInstRegion(), obj.getInstId() });	
				}else{
				sql = "insert into "
						+ H_TABLE
						+ "(INST_ID,INST_NAME,INST_SMP_NAME,PARENT_INST_ID,INST_LAYER,ADDRESS,ZIP,TEL," +
								"FAX,IS_BUSSINESS,ORDER_NUM,DESCRIPTION,START_DATE,END_DATE,CREATE_TIME,ENABLED,EMAIL,INST_PATH,INST_LEVEL,IS_HEAD,INST_REGION)" 
						+" select distinct INST_ID,INST_NAME,INST_SMP_NAME,PARENT_INST_ID,INST_LAYER,ADDRESS,ZIP,TEL," +
								"FAX,IS_BUSSINESS,ORDER_NUM,DESCRIPTION,START_DATE,END_DATE,CREATE_TIME,ENABLED,EMAIL,INST_PATH,INST_LEVEL,IS_HEAD,INST_REGION from "
						+ HTBAK_TABLE
						+ " where inst_id=? and change_status=? and change_user!=? and audit_status=? and (inst_id) "
						+ " not in(select inst_id from "
						+ H_TABLE + " where inst_id=?)";
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
		HolidayChangeDO holiday = (HolidayChangeDO) o;
		/*String sql2 = "select count(*) from " + HTBAK_TABLE
		+" where holiday_type=?";
		int count2 = dao.findForInt(sql2, new Object[] {holiday.getHolidayType()});
		if(count2>0) {
			String sql3 ="delete from "+ HTBAK_TABLE+" where holiday_type=?";
			dao.update(sql3,  new Object[] {holiday.getHolidayType()});
		}*/
		dao.save(HTBAK_TABLE, o, getValueFields(), getColumnFields());
	}
}