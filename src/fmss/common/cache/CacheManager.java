package fmss.common.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fmss.dao.entity.UBaseSysParamDO;
import fmss.common.util.Constants;


/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-29 下午05:52:54
 * @描述: [CacheManager]系统缓存管理类
 */
public class CacheManager{

	public static final String U_BASE_CONFIG_LIST = "baseConfigList";
	public static final String SUB_SYS_MENU_MAP = "subSysMenuMap";
	private static final Map cacheDescMap = new HashMap();
	static{
		cacheDescMap.put(U_BASE_CONFIG_LIST, "子系统信息");
		cacheDescMap.put(SUB_SYS_MENU_MAP, "子系统菜单信息");
	}

	public static Map getCacheDescMap(){
		return cacheDescMap;
	}
	
	private static Map cache = new HashMap();

	/**
	 * <p>方法名称: clean|描述:根据键，清除指定的缓存对象 </p>
	 * @param key
	 */
	public void clean(String key){
		Object object = cache.get(key);
		if(object != null){
			((Cacheable) object).clear();
		}
	}

	/**
	 * <p>方法名称: cleanAll|描述: 清空所有缓存</p>
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
	 * <p>方法名称: register|描述: 是否是增量式的缓存</p>
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
	 * <p>方法名称: getRegisterCount|描述:取得缓存大小 </p>
	 * @return
	 */
	public int getRegisterCount(){
		return cache.size();
	}

	public Iterator keyIterator(){
		return cache.keySet().iterator();
	}

	/**
	 * <p>方法名称: getCacheObject|描述:根据键取得缓存对象 </p>
	 * @param key
	 * @return
	 */
	public static Cacheable getCacheObject(String key){
		return (Cacheable) cache.get(key);
	}
	
	/**
	 * <p>方法名称: getParemerCacheMapValue|描述:根据键值获取缓存PAPAMETER_CACHE_MAP中对应的值 </p>
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
