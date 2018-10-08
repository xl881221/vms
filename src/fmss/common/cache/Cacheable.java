package fmss.common.cache;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-29 ����05:15:50
 * @����: [Cacheable]�������Ա�����Ķ���
 */
public interface Cacheable{

	/**
	 * <p>��������: init|����:��ʼ�� </p>
	 */
	public void init();

	/**
	 * <p>��������: clear|����:������� </p>
	 */
	public void clear();

	/**
	 * <p>��������: getCacheObject|����:ȡ�û������ </p>
	 * @return
	 */
	public Object getCacheObject();
}
