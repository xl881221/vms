/**
 * 
 */
package fmss.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class LoginUtil {

	public static final String LOCK_NORMAL = "0";
	public static final String LOCK_LOCKED = "1";
	public static final String LOCK_PWD_OVERDUE = "2";
	public static final String LOCK_ACCT_DISABLE = "3";

	public static final Map LOCK_USER_STATUS = new HashMap();
	static {
		LOCK_USER_STATUS.put(LOCK_NORMAL, "正常");
		LOCK_USER_STATUS.put(LOCK_LOCKED, "锁定");
		LOCK_USER_STATUS.put(LOCK_LOCKED, "密码过期");
		LOCK_USER_STATUS.put(LOCK_LOCKED, "账户失效");
	}

	public static final String LOCK_REASON_NORMAL = "00";
	public static final String LOCK_REASON_PWD_INVALID_COUNTS = "01";
	public static final String LOCK_REASON_BY_USER = "02";
	public static final String LOCK_REASON_USER_OVERDUE = "03";
	public static final String LOCK_REASON_PWD_OVERDUE = "04";

	public static final Map LOCK_REASON_DESC = new HashMap();
	static {
		LOCK_REASON_DESC.put(LOCK_REASON_NORMAL, "正常");
		LOCK_REASON_DESC.put(LOCK_REASON_PWD_INVALID_COUNTS, "密码输错$var$次");
		LOCK_REASON_DESC.put(LOCK_REASON_BY_USER, "被管理员$var$锁定");
		LOCK_REASON_DESC.put(LOCK_REASON_USER_OVERDUE, "用户$var$天未登录");
		LOCK_REASON_DESC.put(LOCK_REASON_PWD_OVERDUE, "密码过期");
	}

	public static String getDetailLockReason(String lockFlag, String var) {
		String desc = (String) LOCK_REASON_DESC.get(lockFlag);
		if (StringUtils.isNotEmpty(desc)) {
			desc = desc.replaceAll("\\$var\\$", var != null ? var : "");
		}
		return desc != null ? desc : "";
	}

	public static final String SHORT_FORMAT = "yyyy-MM-dd";

	public static final String LONG_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String FULL_TIME_FORMAT = "yyyy-MM-dd HH:mm";

	public static final String SHORT_TIME_FORMAT = "HH:mm";


	public static boolean compareDate(Date date1, Date date2, String format) {
		if (date1 == null || date2 == null)
			return false;
		DateFormat df = new SimpleDateFormat(format);
		if (df.format(date1).equals(df.format(date2)))
			return true;
		return false;
	}

	/**
	 * Adds a number of days to a date returning a new object. The original date
	 * object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 */
	public static Date addDays(Date date, int amount) {
		return add(date, Calendar.DAY_OF_MONTH, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Adds to a date returning a new object. The original date object is
	 * unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param calendarField
	 *            the calendar field to add to
	 * @param amount
	 *            the amount to add, may be negative
	 * @return the new date object with the amount added
	 * @throws IllegalArgumentException
	 *             if the date is null
	 * @deprecated Will become privately scoped in 3.0
	 */
	public static Date add(Date date, int calendarField, int amount) {
		if (date == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(calendarField, amount);
		return c.getTime();
	}

	// -----------------------------------------------------------------------
	/**
	 * Sets the years field to a date returning a new object. The original date
	 * object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to set
	 * @return a new Date object set with the specified value
	 * @throws IllegalArgumentException
	 *             if the date is null
	 * @since 2.4
	 */
	public static Date setYears(Date date, int amount) {
		return set(date, Calendar.YEAR, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Sets the months field to a date returning a new object. The original date
	 * object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to set
	 * @return a new Date object set with the specified value
	 * @throws IllegalArgumentException
	 *             if the date is null
	 * @since 2.4
	 */
	public static Date setMonths(Date date, int amount) {
		return set(date, Calendar.MONTH, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Sets the day of month field to a date returning a new object. The
	 * original date object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to set
	 * @return a new Date object set with the specified value
	 * @throws IllegalArgumentException
	 *             if the date is null
	 * @since 2.4
	 */
	public static Date setDays(Date date, int amount) {
		return set(date, Calendar.DAY_OF_MONTH, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Sets the hours field to a date returning a new object. Hours range from
	 * 0-23. The original date object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to set
	 * @return a new Date object set with the specified value
	 * @throws IllegalArgumentException
	 *             if the date is null
	 * @since 2.4
	 */
	public static Date setHours(Date date, int amount) {
		return set(date, Calendar.HOUR_OF_DAY, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Sets the minute field to a date returning a new object. The original date
	 * object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to set
	 * @return a new Date object set with the specified value
	 * @throws IllegalArgumentException
	 *             if the date is null
	 * @since 2.4
	 */
	public static Date setMinutes(Date date, int amount) {
		return set(date, Calendar.MINUTE, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Sets the seconds field to a date returning a new object. The original
	 * date object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to set
	 * @return a new Date object set with the specified value
	 * @throws IllegalArgumentException
	 *             if the date is null
	 * @since 2.4
	 */
	public static Date setSeconds(Date date, int amount) {
		return set(date, Calendar.SECOND, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Sets the miliseconds field to a date returning a new object. The original
	 * date object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param amount
	 *            the amount to set
	 * @return a new Date object set with the specified value
	 * @throws IllegalArgumentException
	 *             if the date is null
	 * @since 2.4
	 */
	public static Date setMilliseconds(Date date, int amount) {
		return set(date, Calendar.MILLISECOND, amount);
	}

	// -----------------------------------------------------------------------
	/**
	 * Sets the specified field to a date returning a new object. This does not
	 * use a lenient calendar. The original date object is unchanged.
	 * 
	 * @param date
	 *            the date, not null
	 * @param calendarField
	 *            the calendar field to set the amount to
	 * @param amount
	 *            the amount to set
	 * @return a new Date object set with the specified value
	 * @throws IllegalArgumentException
	 *             if the date is null
	 * @since 2.4
	 */
	private static Date set(Date date, int calendarField, int amount) {
		if (date == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		// getInstance() returns a new object, so this method is thread safe.
		Calendar c = Calendar.getInstance();
		c.setLenient(false);
		c.setTime(date);
		c.set(calendarField, amount);
		return c.getTime();
	}

}
