/**
 * 
 */
package fmss.services;

import fmss.common.db.BaseEntityManager;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: sunzhan
 * @����: 2009-6-24 ����10:03:08
 * @����: [AuthObjectService]Ȩ�����������
 */
public class AuthObjectService extends CommonService{

	/** bem ʵ������� */
	private BaseEntityManager bem;

	/**
	 * @return ʵ�������
	 */
	public BaseEntityManager getBem(){
		return bem;
	}

	/**
	 * @param bem Ҫ���õ� ʵ�������
	 */
	public void setBem(BaseEntityManager bem){
		this.bem = bem;
	}

	/* ���� Javadoc��
	* <p>��д����: save|����:����Ȩ��������� </p>
	* @param object
	* @see fmss.services.CommonService#save(java.lang.Object)
	*/
	public void save(Object object){
		this.bem.save(object);
	}

}
