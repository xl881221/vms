package fmss.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;

public class MessageFormat {
	public static final String UTF8_ENCODING = "utf-8";

	public static String decode(String message, String encoding) {
		if (StringUtils.isNotEmpty(message))
			try {
				return URLDecoder.decode(message, encoding);
			} catch (UnsupportedEncodingException e) {
				LogFactory.getLog(MessageFormat.class).warn(e);
			}
		return StringUtils.EMPTY;
	}

	public static String decode(String message) {
		return decode(message, UTF8_ENCODING);
	}

	public static String encode(String message, String encoding) {
		if (StringUtils.isNotEmpty(message))
			try {
				return URLEncoder.encode(message, encoding);
			} catch (UnsupportedEncodingException e) {
				LogFactory.getLog(MessageFormat.class).warn(e);
			}
		return StringUtils.EMPTY;
	}

	public static String encode(String message) {
		return encode(message, UTF8_ENCODING);
	}
}
