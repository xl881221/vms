package fmss.common.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-29 下午05:21:44
 * @描述: [CacheabledMap]请在此简要描述类的功能
 */
public class CacheabledMap /* extends HashMap */implements Cacheable{

	private Map map = null;// 存放缓存数据的集合对象

	/**
	 * <p>构造函数名称: |描述: 初始化一个缓存对象</p>
	 */
	protected CacheabledMap(){
		map = new ConcurrentHashMap();
	}

	/**
	 * <p>方法名称: values|描述:返回集合中所有值 </p>
	 * @return
	 */
	public Collection values(){
		return map.values();
	}

	/**
	 * <p>构造函数名称: |描述:定义级缓存 *@param isIncreasing 是否由不同线程不断增量式改变put() VS
	 * 一次性初始化好.以后只作get()访问 </p>
	 * @param isIncreasing
	 */
	public CacheabledMap(boolean isIncreasing){
		if(isIncreasing){
			map = new ConcurrentHashMap();
		}else{
			map = new HashMap();
		}
	}

	/**
	 * <p>方法名称: isEmpty|描述: 判断集合是否为空 </p>
	 * @return
	 */
	public boolean isEmpty(){
		return map.isEmpty();
	}

	/**
	 * <p>构造函数名称: |描述:控制数量的缓存(数据缓存).使用lruMap </p>
	 * @param size
	 */
	public CacheabledMap(int size){
		map = Collections
				.synchronizedMap(new org.apache.commons.collections.map.LRUMap(
						size));
	}

	/**
	 * <p>构造函数名称: |描述: 控制数量的缓存(数据缓存).使用lruMap (建立一个可以支持文件缓存)</p>
	 * @param size
	 * @param localFileStorage
	 */
	public CacheabledMap(int size, int localFileStorage){
		map = Collections
				.synchronizedMap(new org.apache.commons.collections.map.LRUMap(
						size));
	}

	/* （非 Javadoc）
	* <p>重写方法: getCacheObject|描述:取得一个缓存对象 </p>
	* @return
	* @see fmss.common.cache.Cacheable#getCacheObject()
	*/
	public Object getCacheObject(){
		return this;
	}

	/**
	 * <p>方法名称: get|描述:根据键，从集合中取得一个对象 </p>
	 * @param key
	 * @return
	 */
	public Object get(Object key){
		return map.get(key);
	}

	/**
	 * <p>方法名称: put|描述: 保存一个对象到集合</p>
	 * @param key
	 * @param value
	 * @return
	 */
	public Object put(Object key, Object value){
		return map.put(key, value);
	}

	/* （非 Javadoc）
	* <p>重写方法: init|描述: 初始化</p>
	* @see fmss.common.cache.Cacheable#init()
	*/
	public void init(){
		// FixedSizeMapPool only permit the 10 cache.
	}

	/* （非 Javadoc）
	* <p>重写方法: clear|描述: 清除集合中的所有数据</p>
	* @see fmss.common.cache.Cacheable#clear()
	*/
	public void clear(){
		map.clear();
	}

	/**
	 * <p>方法名称: containsValue|描述:根据键判断对象是否存在 </p>
	 * @param value
	 * @return
	 */
	public boolean containsKey(Object key){
		return map.containsKey(key);
	}

	/**
	 * <p>方法名称: remove|描述: 根据键从集合中删除一个对象</p>
	 * @param key
	 * @return
	 */
	public Object remove(Object key){
		return map.remove(key);
	}

	/**
	 * <p>方法名称: size|描述:返回集合中对象个数 </p>
	 * @return
	 */
	public int size(){
		return map.size();
	}

	/**
	 * <p>方法名称: containsValue|描述:根据值判断对象是否存在 </p>
	 * @param value
	 * @return
	 */
	public boolean containsValue(Object value){
		return map.containsValue(value);
	}

	/**
	 * <p>方法名称: keySet|描述:返回map的所有键 </p>
	 * @return
	 */
	public Set keySet(){
		return map.keySet();
	}
}
