package fmss.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fmss.dao.entity.UBaseDictionaryDO;
import fmss.common.util.PageBox;
import fmss.common.util.PaginationList;

import org.hibernate.criterion.DetachedCriteria;

import fmss.common.db.BaseEntityManager;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: sunzhan
 * @����: 2009-6-24 ����09:28:27
 * @����: [DictionaryService]�ֵ������
 */
public class DictionaryService extends CommonService{

	/** bem ʵ�������� */
	private BaseEntityManager bem;
	/** RESOURCE_DIC_TYPE ��Դ */
	public static final String RESOURCE_DIC_TYPE = "RT";
	/** RESOURCE_DIC_TYPE ��Դ-�˵� */
	public static final String MEN_RESOURCE_DIC_TYPE = "MEN";
	/** RESOURCE_DIC_TYPE ��Դ-���� */
	public static final String ORG_RESOURCE_DIC_TYPE = "ORG";
	/** RESOURCE_DIC_TYPE ��Դ-���� */
	public static final String RPT_RESOURCE_DIC_TYPE = "RPT";
	/** ��Դ����-���� */
	public static final String PUB_RESOURCE_DIC_TYPE = "PUB";
	/** ��Դ����-˽��*/
	public static final String PRI_RESOURCE_DIC_TYPE = "PRI";
	/** DB_DIC_TYPE ���ݿ� */
	public static final String DB_DIC_TYPE = "DBT";
	/** AOT_DIC_TYPE Ȩ��������� */
	public static final String AOT_DIC_TYPE = "AOT";
	/** ROLE_AOT_DIC_TYPE Ȩ���������-��ɫ */
	public static final String ROLE_AOT_DIC_TYPE = "ROLE";
	/** USER_AOT_DIC_TYPE Ȩ���������-�û� */
	public static final String USER_AOT_DIC_TYPE = "USER";
	/** USER_AOT_DIC_TYPE Ȩ������ */
	public static final String AUTH_AOT_DIC_TYPE = "AUTH";
	/** RES_AOT_DIC_TYPE ��Դ */
	public static final String RES_AOT_DIC_TYPE = "RES";
	/** NOTICE_TYPE ͨ�� */
	public static final String NOTICE_TYPE = "NT";
	/** NOTICE_TYPE ͨ������ */
	public static final String NOTICE_MAIN_TYPE = "NTM";
	/** NOTICE_TYPE ͨ����־ */
	public static final String NOTICE_LOG_TYPE = "NTL";
	/** NOTICE_TYPE ͨ�渽�� */
	public static final String NOTICE_AFFIX_TYPE = "NTA";
	/** NOTICE_TYPE ͨ�淴�� */
	public static final String NOTICE_FEEDBACK_TYPE = "NTFB";
	/** QUERY_STRING ��ѯhql */
	public static final String QUERY_STRING = "select udb from UBaseDictionaryDO udb where udb.dicType=? order by udb.orderNum";

	/* ���� Javadoc��
	* <p>��д����: delete|����:ɾ������ </p>
	* @param object ����
	* @see fmss.services.CommonService#delete(java.lang.Object)
	*/
	public void delete(Object object){
		// TODO Auto-generated method stub
	}

	/* ���� Javadoc��
	* <p>��д����: deleteAll|����:ɾ������ </p>
	* @param entities ���϶���
	* @see fmss.services.CommonService#deleteAll(java.util.Collection)
	*/
	public void deleteAll(Collection entities){
		// TODO Auto-generated method stub
	}

	/* ���� Javadoc��
	* <p>��д����: find|����:����hql��ѯ���� </p>
	* @param queryString ��ѯhql
	* @return ��ѯ�������
	* @see fmss.services.CommonService#find(java.lang.String)
	*/
	public List find(String queryString){
		// TODO Auto-generated method stub
		return null;
	}

	/* ���� Javadoc��
	* <p>��д����: find|����:��ѯ�ֵ���� </p>
	* @param query ��ѯhql
	* @param parameter ��ѯ����
	* @return ��ѯ�������
	* @see fmss.services.CommonService#find(java.lang.String, java.lang.Object)
	*/
	public List find(String query, Object parameter){
		return this.bem.find(query, parameter);
	}

	/* ���� Javadoc��
	* <p>��д����: find|����:����hql��ѯ���� </p>
	* @param query ��ѯhql
	* @param parameter ��ѯ��������
	* @return ��ѯ�������
	* @see fmss.services.CommonService#find(java.lang.String, java.lang.Object[])
	*/
	public List find(String query, Object[] parameter){
		// TODO Auto-generated method stub
		return null;
	}

	/* ���� Javadoc��
	* <p>��д����: find|����:����hql��ѯ���� </p>
	* @param query ��ѯhql
	* @param parameters ��ѯ�����б�
	* @return ��ѯ�������
	* @see fmss.services.CommonService#find(java.lang.String, java.util.List)
	*/
	public List find(String query, List parameters){
		// TODO Auto-generated method stub
		return null;
	}

	/* ���� Javadoc��
	* <p>��д����: find|����:����hql��ѯ���󣬴���ҳ���� </p>
	* @param hql ��ѯhql
	* @param parameters ��ѯ�����б�
	* @param pageInfo ��ҳ����
	* @return ��ѯ�������
	*/
	public List find(String hql, List parameters, PaginationList pageInfo){
		// TODO Auto-generated method stub
		return null;
	}

	/* ���� Javadoc��
	* <p>��д����: getAllByDetachedCriteria|����:����������ѯ </p>
	* @param detachedCriteria ��ѯ����
	* @return ��ѯ�������
	* @see fmss.services.CommonService#getAllByDetachedCriteria(org.hibernate.criterion.DetachedCriteria)
	*/
	public List getAllByDetachedCriteria(DetachedCriteria detachedCriteria){
		// TODO Auto-generated method stub
		return null;
	}

	/* ���� Javadoc��
	* <p>��д����: getByFormWithPaging|����:���ݷ�ҳ����������ѯ </p>
	* @param detachedCriteria ��ѯ����
	* @param pageSize ҳ��size
	* @param pageNum ҳ��
	* @return ��ҳ����
	* @see fmss.services.CommonService#getByFormWithPaging(org.hibernate.criterion.DetachedCriteria, int, int)
	*/
	public PageBox getByFormWithPaging(DetachedCriteria detachedCriteria,
			int pageSize, int pageNum){
		// TODO Auto-generated method stub
		return null;
	}

	/* ���� Javadoc��
	* <p>��д����: getCountByCriteria|����:����������ѯ��¼�� </p>
	* @param detachedCriteria ��ѯ����
	* @return ��¼��
	* @see fmss.services.CommonService#getCountByCriteria(org.hibernate.criterion.DetachedCriteria)
	*/
	public int getCountByCriteria(DetachedCriteria detachedCriteria){
		// TODO Auto-generated method stub
		return 0;
	}

	/* ���� Javadoc��
	* <p>��д����: save|����:������� </p>
	* @param object ����
	* @see fmss.services.CommonService#save(java.lang.Object)
	*/
	public void save(Object object){
		// TODO Auto-generated method stub
	}

	/* ���� Javadoc��
	* <p>��д����: update|����:���¶��� </p>
	* @param object ����
	* @see fmss.services.CommonService#update(java.lang.Object)
	*/
	public void update(Object object){
		// TODO Auto-generated method stub
	}

	/**
	 * <p>��������: addDefault|����:���Ĭ���ֵ���� </p>
	 * @param list �ֵ��б�
	 * @param dic Ĭ���ֵ����
	 * @return �ֵ��б�
	 */
	public List addDefault(List list, UBaseDictionaryDO dic){
		list = new ArrayList();
		list.add(dic);
		return list;
	}

	/**
	 * <p>��������: getDictoryBy|����: �����ֵ����ͣ���ȡ�ֵ���Ϣ</p>
	 * @param dicType �ֵ�����
	 * @return ���ش������µ������ֵ�����
	 */
	public List getDictoryByDicType(String dicType){
		return find(
				"SELECT new Map(ubd.dicType as dicType,ubd.dicValue as dicValue,ubd.dicName as dicName)"
						+ "FROM UBaseDictionaryDO ubd WHERE ubd.dicType=? order by ubd.orderNum",
				dicType);
	}
	
	public List getDictionaryByDicType(String dicType){
		return find("FROM UBaseDictionaryDO ubd WHERE ubd.dicType=? order by ubd.orderNum",
				dicType);
	}
	
	public List getTopRegions(){
		return find("FROM UBaseDictionaryDO ubd WHERE ubd.dicType='region' and substring(udb.dicValue,2,4)='0000' order by ubd.orderNum");
	}
	
	public static final String INST_REGIONS = "INST_REGIONS";

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
	
	public List getDictoryByCodeType(String codeType){
		return find(
				"SELECT new Map(tcd.codeName as codeName,tcd.codeValueStandardNum as codeValueStandardNum)"
						+ "FROM TCodeDictionaryDO tcd WHERE tcd.codeType=?",
						codeType);
	}
	
}
