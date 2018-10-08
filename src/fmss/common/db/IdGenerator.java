package fmss.common.db;

import java.util.HashMap;

import javax.sql.DataSource;

/**
 * <p>
 * 版权所有:(C)2003-2010 
 * </p>
 * 
 * @作者: zhangshoufeng
 * @日期: 2009-6-23 上午10:36:56
 * @描述:[IdGenerator]ID生成器
 */
public class IdGenerator {

	private static HashMap kengens = new HashMap(10);
	private static final int POOL_SIZE = 20;
	private IdGeneratorKey keyinfo;
	private static DataSource dataSource;

	/**
	 * 私有构造函数，保证外界无法直接实例化
	 */
	private IdGenerator() {
	}

	/**
	 * 私有构造函数，保证外界无法直接实例化
	 */
	private IdGenerator(String keyName, DataSource dataSource) {
		keyinfo = new IdGeneratorKey(POOL_SIZE, keyName, dataSource);
	}

	/**
	 * 静态工厂方法，提供自己的实例
	 */
	public static synchronized IdGenerator getInstance(String keyName) {
		IdGenerator keygen;
		if (kengens.containsKey(keyName)) {
			keygen = (IdGenerator) kengens.get(keyName);
		} else {
			keygen = new IdGenerator(keyName, dataSource);
			kengens.put(keyName, keygen);
		}
		return keygen;
	}

	/**
	 * 取值方法，提供下一个合适的键值
	 */
	public synchronized long getNextKey() {
		return keyinfo.getNextKey();
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
