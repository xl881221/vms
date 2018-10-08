package fmss.common.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fmss.dao.entity.UBaseSysParamDO;
import fmss.common.util.Constants;


/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-29 ����05:52:54
 * @����: [CacheManager]ϵͳ���������
 */
public class CacheManager{

	public static final String U_BASE_CONFIG_LIST = "baseConfigList";
	public static final String SUB_SYS_MENU_MAP = "subSysMenuMap";
	private static final Map cacheDescMap = new HashMap();
	static{
		cacheDescMap.put(U_BASE_CONFIG_LIST, "��ϵͳ��Ϣ");
		cacheDescMap.put(SUB_SYS_MENU_MAP, "��ϵͳ�˵���Ϣ");
	}

	public static Map getCacheDescMap(){
		return cacheDescMap;
	}
	
	private static Map cache = new HashMap();

	/**
	 * <p>��������: clean|����:���ݼ������ָ���Ļ������ </p>
	 * @param key
	 */
	public void clean(String key){
		Object object = cache.get(key);
		if(object != null){
			((Cacheable) object).clear();
		}
	}

	/**
	 * <p>��������: cleanAll|����: ������л���</p>
	 */
	public void cleanAll(){
		for(Iterator iter = cache.keySet().iterator(); iter.hasNext();){
			Object o = iter.next();
			iter.remove();
			cache.remove(o);
		}
	}

	public boolean isIncreaseCache(String cacheKey){
		return !needReLoadL.contains(cacheKey);
	}

	private List needReLoadL = new ArrayList();

	/**
	 * <p>��������: register|����: �Ƿ�������ʽ�Ļ���</p>
	 * @param key
	 * @param value
	 * @param isIncreasing
	 */
	public void register(String key, Cacheable value, boolean isIncreasing){
		// logger.debug("Register: [" + key + "]");
		cache.put(key, value);
		if(!isIncreasing){
			needReLoadL.add(key);
		}
	}

	/**
	 * <p>��������: getRegisterCount|����:ȡ�û����С </p>
	 * @return
	 */
	public int getRegisterCount(){
		return cache.size();
	}

	public Iterator keyIterator(){
		return cache.keySet().iterator();
	}

	/**
	 * <p>��������: getCacheObject|����:���ݼ�ȡ�û������ </p>
	 * @param key
	 * @return
	 */
	public static Cacheable getCacheObject(String key){
		return (Cacheable) cache.get(key);
	}
	
	/**
	 * <p>��������: getParemerCacheMapValue|����:���ݼ�ֵ��ȡ����PAPAMETER_CACHE_MAP�ж�Ӧ��ֵ </p>
	 * @param paramkey
	 * @return value
	 */
	public static String getParemerCacheMapValue(String paramkey){
		CacheabledMap cache = (CacheabledMap) getCacheObject(Constants.PAPAMETER_CACHE_MAP);
		Map params = null;
		if(cache != null){
			params = (Map) cache.get(Constants.PAPAMETER_CACHE_MAP);
			if(null != params){
				UBaseSysParamDO param = (UBaseSysParamDO) params.get(paramkey);
				if(param !=null)
					return param.getSelectedValue()==null?"":param.getSelectedValue();
			}
		}
		return "";
	}

}
