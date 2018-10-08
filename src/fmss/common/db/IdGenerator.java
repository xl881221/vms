package fmss.common.db;

import java.util.HashMap;

import javax.sql.DataSource;

/**
 * <p>
 * ��Ȩ����:(C)2003-2010 
 * </p>
 * 
 * @����: zhangshoufeng
 * @����: 2009-6-23 ����10:36:56
 * @����:[IdGenerator]ID������
 */
public class IdGenerator {

	private static HashMap kengens = new HashMap(10);
	private static final int POOL_SIZE = 20;
	private IdGeneratorKey keyinfo;
	private static DataSource dataSource;

	/**
	 * ˽�й��캯������֤����޷�ֱ��ʵ����
	 */
	private IdGenerator() {
	}

	/**
	 * ˽�й��캯������֤����޷�ֱ��ʵ����
	 */
	private IdGenerator(String keyName, DataSource dataSource) {
		keyinfo = new IdGeneratorKey(POOL_SIZE, keyName, dataSource);
	}

	/**
	 * ��̬�����������ṩ�Լ���ʵ��
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
	 * ȡֵ�������ṩ��һ�����ʵļ�ֵ
	 */
	public synchronized long getNextKey() {
		return keyinfo.getNextKey();
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
