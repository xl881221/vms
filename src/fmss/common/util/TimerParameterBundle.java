package fmss.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TimerParameterBundle {

	private static final Log log = LogFactory
			.getLog(TimerParameterBundle.class);

	static String bundleName = "/fmss/config/roundexcute.properties";

	static Properties jdbc = new Properties();
	static {
		initProperties();
	}

	private static void initProperties() {
		InputStream is = null;
		try {
			is = TimerParameterBundle.class.getClassLoader().getResource(
					bundleName).openConnection().getInputStream();
			if (is == null) {
				throw new RuntimeException("Properties not found:" + bundleName);
			}
			jdbc.load(is);
		} catch (Exception e) {
			log.error("load properites error", e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				log.warn("Could not close InputStream", e);
			}
		}
	}

	public static String getProperty(String name) {
		String property = null;
		try {
			property = jdbc.getProperty(name);
		} catch (Exception e) {
			log.error("could not found properties:" + name, e);
		}
		if (property != null) {
			return property;
		} else {
			return "";
		}
	}

	public static String getProperty(String name, String def) {
		String property = null;
		try {
			// jdbc = new Properties();
			// initProperties();
			property = jdbc.getProperty(name, def);
		} catch (Exception e) {
			log.error("could not found properties:" + name, e);
		}
		if (property != null) {
			return property;
		} else {
			return def;
		}
	}

	public static long parsePeriodExpression(String name, String def) {
		String value = getProperty(name, def);
		if (value != null) {
			value = value.trim();
		}
		return parsePeriod(value);
	}

	public static long parsePeriodExpression(String name) {
		String value = getProperty(name);
		if (value != null) {
			value = value.trim();
		}
		return parsePeriod(value);
	}

	private static long parsePeriod(String value) {
		String[] values = value.split("\\*");
		long time = 1l;
		for (int i = 0; i < values.length; i++) {
			String s = values[i];
			try {
				time *= Long.parseLong(s.trim());
			} catch (NumberFormatException e) {
				log.error("parse long error:" + s.trim(), e);
			}
		}
		return time;
	}

	public static Date parseTimeExpression(String name, String def) {
		String value = getProperty(name, def);
		if (value != null) {
			value = value.trim();
		}
		return parseTime(value);
	}

	public static Date parseTimeExpression(String name) {
		String value = getProperty(name);
		if (value != null) {
			value = value.trim();
		}
		return parseTime(value);
	}

	private static Date parseTime(String value) {
		Date date = null;
		if (StringUtils.isEmpty(value)) {
			return date;
		}
		if (value.indexOf("-") > -1 && value.indexOf(":") > -1) {
			try {
				date = DateUtils.parseDate(value, new String[] { LoginUtil.FULL_TIME_FORMAT });//
			} catch (ParseException e) {
				log.error("parse date error:" + value, e);
				throw new IllegalArgumentException("无效的日期参数:" + value);
			}
		} else if (value.indexOf("-") > -1 && value.indexOf(":") == -1) {
			try {
				date = DateUtils.parseDate(value, new String[] { LoginUtil.SHORT_FORMAT });//
			} catch (ParseException e) {
				log.error("parse date error:" + value, e);
				throw new IllegalArgumentException("无效的日期参数:" + value);
			}
		} else if (value.indexOf("-") == -1 && value.indexOf(":") > -1) {
			try {
				date = DateUtils.parseDate(value, new String[] { LoginUtil.SHORT_TIME_FORMAT });//
				log.info("parse date is:" + DateFormatUtils.format(date, LoginUtil.FULL_TIME_FORMAT));
				date = LoginUtil.setYears(date, Calendar.getInstance().get(Calendar.YEAR));
				date = LoginUtil.setMonths(date, Calendar.getInstance().get(Calendar.MONTH));
				date = LoginUtil.setDays(date, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
				log.info("parse date is:" + DateFormatUtils.format(date, LoginUtil.FULL_TIME_FORMAT));
			} catch (ParseException e) {
				log.error("parse date error:" + value, e);
				throw new IllegalArgumentException("无效的日期参数:" + value);
			}
		} else {
			throw new IllegalArgumentException("无效的日期参数:" + value);
		}
		return date;
	}

	public static void main(String[] a) {
		initProperties();
		jdbc.get("ss");
	}
}