package fmss.common.cache;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-29 ����05:21:44
 * @����: [CacheabledMap]���ڴ˼�Ҫ������Ĺ���
 */
public class CacheabledMap /* extends HashMap */implements Cacheable{

	private Map map = null;// ��Ż������ݵļ��϶���

	/**
	 * <p>���캯������: |����: ��ʼ��һ���������</p>
	 */
	protected CacheabledMap(){
		map = new ConcurrentHashMap();
	}

	/**
	 * <p>��������: values|����:���ؼ���������ֵ </p>
	 * @return
	 */
	public Collection values(){
		return map.values();
	}

	/**
	 * <p>���캯������: |����:���弶���� *@param isIncreasing �Ƿ��ɲ�ͬ�̲߳�������ʽ�ı�put() VS
	 * һ���Գ�ʼ����.�Ժ�ֻ��get()���� </p>
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
	 * <p>��������: isEmpty|����: �жϼ����Ƿ�Ϊ�� </p>
	 * @return
	 */
	public boolean isEmpty(){
		return map.isEmpty();
	}

	/**
	 * <p>���캯������: |����:���������Ļ���(���ݻ���).ʹ��lruMap </p>
	 * @param size
	 */
	public CacheabledMap(int size){
		map = Collections
				.synchronizedMap(new org.apache.commons.collections.map.LRUMap(
						size));
	}

	/**
	 * <p>���캯������: |����: ���������Ļ���(���ݻ���).ʹ��lruMap (����һ������֧���ļ�����)</p>
	 * @param size
	 * @param localFileStorage
	 */
	public CacheabledMap(int size, int localFileStorage){
		map = Collections
				.synchronizedMap(new org.apache.commons.collections.map.LRUMap(
						size));
	}

	/* ���� Javadoc��
	* <p>��д����: getCacheObject|����:ȡ��һ��������� </p>
	* @return
	* @see fmss.common.cache.Cacheable#getCacheObject()
	*/
	public Object getCacheObject(){
		return this;
	}

	/**
	 * <p>��������: get|����:���ݼ����Ӽ�����ȡ��һ������ </p>
	 * @param key
	 * @return
	 */
	public Object get(Object key){
		return map.get(key);
	}

	/**
	 * <p>��������: put|����: ����һ�����󵽼���</p>
	 * @param key
	 * @param value
	 * @return
	 */
	public Object put(Object key, Object value){
		return map.put(key, value);
	}

	/* ���� Javadoc��
	* <p>��д����: init|����: ��ʼ��</p>
	* @see fmss.common.cache.Cacheable#init()
	*/
	public void init(){
		// FixedSizeMapPool only permit the 10 cache.
	}

	/* ���� Javadoc��
	* <p>��д����: clear|����: ��������е���������</p>
	* @see fmss.common.cache.Cacheable#clear()
	*/
	public void clear(){
		map.clear();
	}

	/**
	 * <p>��������: containsValue|����:���ݼ��ж϶����Ƿ���� </p>
	 * @param value
	 * @return
	 */
	public boolean containsKey(Object key){
		return map.containsKey(key);
	}

	/**
	 * <p>��������: remove|����: ���ݼ��Ӽ�����ɾ��һ������</p>
	 * @param key
	 * @return
	 */
	public Object remove(Object key){
		return map.remove(key);
	}

	/**
	 * <p>��������: size|����:���ؼ����ж������ </p>
	 * @return
	 */
	public int size(){
		return map.size();
	}

	/**
	 * <p>��������: containsValue|����:����ֵ�ж϶����Ƿ���� </p>
	 * @param value
	 * @return
	 */
	public boolean containsValue(Object value){
		return map.containsValue(value);
	}

	/**
	 * <p>��������: keySet|����:����map�����м� </p>
	 * @return
	 */
	public Set keySet(){
		return map.keySet();
	}
}
