package fmss.common.ui.controller;

import java.util.Collection;
import java.util.HashMap;

/**
 * ��˵��:  ����ģ�͵�������<br>
 * ����ʱ��: 2009-2-6 ����01:17:18<br>
 * 
 * @author �����<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class AnalyseContext {
	/**
	 * ��ǰ����ģ��
	 */
	public static final String CURR_QUERY_DATA = "queryData";
	/**
	 * ��ǰ������ ������col,row,cell��
	 */
	public static final String CURR_EFFECTIVE_DATA_TYPE = "effectiveRangeType";
	/**
	 * ��ǰ����
	 */
	public static final String CURR_EFFECTIVE_DATA = "effectiveRange";
	
	public static final String REALTIME_CALC_RESULT = "resultCalcResult";

	private HashMap contextValues = new HashMap();

	public void setBooleanContext(String key, boolean value) {
		contextValues.put(key, new Boolean(value));
	}

	public Boolean getBooleanContext(String key) {
		return ((Boolean) contextValues.get(key));
	}

	public void setContext(String key, Object value) {
		contextValues.put(key, value);
	}

	public Object getContext(String key) {
		return contextValues.get(key);
	}

	public boolean containsKey(Object key) {
		return contextValues.containsKey(key);
	}

	public void clear() {
		contextValues.clear();
	}

	public boolean containsValue(Object value) {
		return contextValues.containsValue(value);
	}

	public Object remove(Object key) {
		return contextValues.remove(key);
	}

	public int size() {
		return contextValues.size();
	}

	public String toString() {
		return contextValues.toString();
	}

	public Collection values() {
		return contextValues.values();
	}

	public void setContext(Object key, Object value) {
		this.contextValues.put(key, value);

	}

	public Object getContext(Object key) {
		return this.contextValues.get(key);
	}
}
