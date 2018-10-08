package fmss.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fmss.common.util.ArrayUtil;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.enums.ValuedEnum;


public abstract class LoggingDifference {
	protected String desc;
	protected String[] before;
	protected String[] after;

	public LoggingDifference(String desc, String[] before, String[] after) {
		this.desc = desc;
		this.before = before;
		this.after = after;
	}

	public LoggingDifference(String desc, List before, List after) {
		this.desc = desc;
		this.before = (String[]) before.toArray(new String[] {});
		this.after = (String[]) after.toArray(new String[] {});
	}

	static class Difference extends ValuedEnum {
		public static final int INT_NORMAL = 0;
		public static final int INT_ADD = 1;
		public static final int INT_DEL = -1;
		public static final int INT_ADD_DEL = 100;

		public static final ValuedEnum NORMAL = new Difference("未做改变", INT_NORMAL);
		public static final ValuedEnum ADD = new Difference("添加", INT_ADD);
		public static final ValuedEnum DEL = new Difference("删除", INT_DEL);
		public static final ValuedEnum ADD_DEL = new Difference("添加删除", INT_ADD_DEL);

		protected Difference(String name, int value) {
			super(name, value);
		}

		public static ValuedEnum getDifference(String[] adds, String[] dels) {
			if (adds.length == 0 && dels.length == 0)
				return NORMAL;
			if (adds.length > 0 && dels.length == 0)
				return ADD;
			if (dels.length > 0 && adds.length == 0)
				return DEL;
			return ADD_DEL;
		}

		public static Map getDifferenceMap(String[] before, String[] after) {
			Map map = new HashMap();
			if (before == null)
				before = new String[] {};
			if (after == null)
				after = new String[] {};
			Arrays.sort(before);
			Arrays.sort(after);
			if (ArrayUtils.isEquals(before, after)) {
				map.put(NORMAL, new String[] {});
				return map;
			}
			String[] adds = ArrayUtil.minus(after, before);
			String[] dels = ArrayUtil.minus(before, after);
			ValuedEnum e = getDifference(adds, dels);
			switch (e.getValue()) {
			case INT_ADD_DEL:
				map.put(ADD, adds);
				map.put(DEL, dels);
				break;
			case INT_ADD:
				map.put(e, adds);
				break;
			case INT_DEL:
				map.put(e, dels);
				break;
			case INT_NORMAL:
				map.put(e, new String[] {});
				break;
			default:
				break;
			}
			return map;
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		Map m = Difference.getDifferenceMap(before, after);
		for (Iterator iterator = m.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry e = (Map.Entry) iterator.next();
			ValuedEnum v = (ValuedEnum) e.getKey();
			String[] array = (String[]) e.getValue();
			sb.append(v.getName()).append(v.getValue() == Difference.INT_NORMAL ? "" : desc);
			if (!ArrayUtils.isEmpty(array)) {
				sb.append(StringUtils.join(fetchAuthName(array), ",")).append(" ");
			}
		}
		return sb.toString();
	}

	public abstract String[] fetchAuthName(String[] array);
}


