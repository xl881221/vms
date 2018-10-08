package fmss.common.cache;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-29 下午05:15:50
 * @描述: [Cacheable]声明可以被缓存的对象
 */
public interface Cacheable{

	/**
	 * <p>方法名称: init|描述:初始化 </p>
	 */
	public void init();

	/**
	 * <p>方法名称: clear|描述:清除数据 </p>
	 */
	public void clear();

	/**
	 * <p>方法名称: getCacheObject|描述:取得缓存对象 </p>
	 * @return
	 */
	public Object getCacheObject();
}
