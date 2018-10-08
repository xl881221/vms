package fmss.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fmss.action.base.AuditBase;
import fmss.action.base.HolidayTypeBaseAuditBase;
import fmss.action.base.JdbcDaoAccessor;
import fmss.action.base.UserRoleAuditBase;
import fmss.common.util.ArrayUtil;
import fmss.dao.entity.UBaseHolidayDO;
import fmss.dao.entity.UBaseHolidayTypeDO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.jes.core.api.holiday.HolidayManager;

public class HolidayService extends CommonService {

	private static final Log log = LogFactory.getLog(HolidayService.class);
	
	private JdbcDaoAccessor jdbcDaoAccessor;

	public List getHolidayTypes(String enabled) {
		List list = null;
		String hql = "from UBaseHolidayTypeDO u ";
		if (StringUtils.isNotEmpty(enabled)) {
			hql += " where u.enable=? order by u.holidayName";
			list = find(hql, new Object[] { enabled });
		} else {
			list = find(hql + " order by u.holidayName");
		}
		return list;
	}
	
	public List getAllHolidayTypes() {
		//select t.holiday_name name,t.holiday_type from u_base_holiday_type t 
		//union all select t1.holiday_name name,t1.holiday_type from u_base_holiday_type_change t1 where t1.audit_status=1 and t1.change_status=14 order by name

		List list = jdbcDaoAccessor.find("select t.holiday_name name,t.holiday_type from " + HolidayTypeBaseAuditBase.HT_TABLE
				+ " t where t.holiday_type not in(select holiday_type from " + HolidayTypeBaseAuditBase.HTTBAK_TABLE+" where audit_status=1  ) " +
						"union all select t1.holiday_name name,t1.holiday_type from " + HolidayTypeBaseAuditBase.HTTBAK_TABLE+
				" t1 where t1.audit_status=1  order by name ");
	//" t1 where t1.audit_status=1 and t1.change_status=14 order by name ");
		return list;
	}

	public int isHolidayInstance(String date, String holidayType) {
		UBaseHolidayTypeDO holiday = (UBaseHolidayTypeDO) this.get(UBaseHolidayTypeDO.class, holidayType);
		/*		List list0 = find(
						"from UBaseHolidayTypeDO u where u.holidayType=? and u.enable=?",
						new Object[] { holidayType, HolidayManager.TYPE_ENABLED });
				if (CollectionUtils.isEmpty(list0))
					return HolidayManager.CHECK_CODE_TYPE_DISABLED;*/
		if(null == holiday){
			return HolidayManager.CHECK_CODE_TYPE_LOOSE;
		}else if(HolidayManager.TYPE_DISABLED.equalsIgnoreCase(holiday.getEnable())){
			return HolidayManager.CHECK_CODE_TYPE_DISABLED;
		}
		List list = find(
				"from UBaseHolidayDO u where u.holidayType=? and u.holidayValue=?",
				new Object[] { holidayType, date });
		return CollectionUtils.isNotEmpty(list) ? HolidayManager.CHECK_CODE_IS_HOLIDAY
				: HolidayManager.CHECK_CODE_NOT_HOLIDAY;
	}

	public UBaseHolidayTypeDO getHolidayType(String holidayType) {
		List list = find("from UBaseHolidayTypeDO u where u.holidayType=?",
				new Object[] { holidayType });
		return CollectionUtils.isNotEmpty(list) ? (UBaseHolidayTypeDO) list
				.get(0) : null;
	}

	public UBaseHolidayDO getHoliday(String holidayType, String holidayValue) {
		List list = find(
				"from UBaseHolidayDO u where u.holidayType=? and u.holidayValue=?",
				new Object[] { holidayType, holidayValue });
		return CollectionUtils.isNotEmpty(list) ? (UBaseHolidayDO) list.get(0)
				: null;
	}

	public List getHolidays(String holidayType) {
		List list = find("from UBaseHolidayDO u where u.holidayType=? ",
				new Object[] { holidayType });
		return list;
	}

	public void deteteHolidayType(String holidayType) {
		executeUpdate("delete from UBaseHolidayDO u where u.holidayType='"
				+ holidayType + "'");
		deleteById(UBaseHolidayTypeDO.class, holidayType);
	}

	public int saveBatchHolidays(String[] holidays, String holidayType) {
		int counts = 0;
		int checkCode = HolidayManager.CHECK_CODE_ADD_HOLIDAY_SUCC;
		if (getHolidayType(holidayType) == null) {
			UBaseHolidayTypeDO o = new UBaseHolidayTypeDO();
			o.setHolidayType(holidayType);
			o.setHolidayName(holidayType);
			o.setEnable(HolidayManager.TYPE_ENABLED);
			o.setRemark("udl import");
			save(o);
			log.info("新增节假日类别成功,holidayType:" + holidayType);
		} else {
			int i = executeUpdate("delete from UBaseHolidayDO u where u.holidayType='"
					+ holidayType + "'");
			log.info("删除类别:" + holidayType + "下节假日:" + i);
		}
		for (int i = 0; i < holidays.length; i++) {
			String date = holidays[i];
			try {
				new SimpleDateFormat(
						HolidayManager.SHORT_PATTERN).parse(date);
			} catch (ParseException e) {
				log.error("日期参数date格式必须为" + HolidayManager.SHORT_PATTERN
						+ ",或者日期不存在:" + date + ",跳过", e);
				checkCode = HolidayManager.CHECK_CODE_ADD_HOLIDAY_CONTAINS_INVALID;
				continue;
			}
			UBaseHolidayDO o = new UBaseHolidayDO();
			o.setHolidayType(holidayType);
			o.setHolidayValue(date);
			save(o);
			counts++;
		}
		log.info("批量节假日添加成功:" + counts);
		return checkCode;
	}
	
	public String getMostEarlyHoliday(String holidayType, String currentMonth, String currentYear){
		int month = Integer.parseInt(currentMonth);
		int year = Integer.parseInt(currentYear);
		List list = find("select min(u.holidayValue) as holidayValue from UBaseHolidayDO u where u.holidayType=?",
				new Object[] { holidayType });
		if (list != null) {
			String s = (String) list.get(0);
			if (StringUtils.isNotEmpty(s)) {
				try {
					Date d = new SimpleDateFormat(
							HolidayManager.SHORT_PATTERN).parse(s);
					Calendar c = Calendar.getInstance();
					c.setTime(d);
					month = c.get(Calendar.MONTH);
					year = c.get(Calendar.YEAR);
				} catch (ParseException e) {
					log.error("parse error: " + s, e);
				}
			}
		}
		return year + "-" + month;
	}

	public JdbcDaoAccessor getJdbcDaoAccessor() {
		return jdbcDaoAccessor;
	}

	public void setJdbcDaoAccessor(JdbcDaoAccessor jdbcDaoAccessor) {
		this.jdbcDaoAccessor = jdbcDaoAccessor;
	}
	
}
