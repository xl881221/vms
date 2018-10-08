/*
 * 
 */
package fmss.services;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import fmss.common.util.PageBox;
import fmss.common.util.PaginationList;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;

import fmss.common.db.BaseEntityManager;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-23 ����10:31:18
 * @����:[CommonService]�����࣬�ṩ����������ݿ��������
 */
public class CommonService implements BaseService{

	private BaseEntityManager bem; // ʵ��������

	/**
	 * <p>��������: setBem|����:ע��ʵ������� </p>
	 * @param bem
	 */
	public void setBem(BaseEntityManager bem){
		this.bem = bem;
	}
	

	/**
	 * <p>��������: save|����:����������ݿ� </p>
	 * @param object
	 */
	public void save(final Object object){
		bem.save(object);
	}

	/**
	 * <p>��������: update|����:����һ���������ݿ� </p>
	 * @param object
	 */
	public void update(final Object object){
		bem.update(object);
	}

	/**
	 * <p>��������: delete|����:�����ݿ���ɾ��һ��ָ���Ķ��� </p>
	 * @param object
	 */
	public void delete(Object object){
		bem.delete(object);
	}

	/**
	 * <p>��������: deleteAll|����:����ָ����ID�����ݿ���ɾ������ </p>
	 * @param id
	 */
	public void deleteById(Class cl, Serializable id){
		bem.deleteById(cl, id);
	}

	/**
	 * <p>��������: get|����:����ָ����ID��ѯ���� </p>
	 * @param cl
	 * @param id
	 * @return
	 */
	public Object get(Class cl, Serializable id){
		return bem.get(cl, id);
	}

	/**
	 * <p>��������: load|����:����ָ����ID��ѯ���� </p>
	 * @param cl
	 * @param id
	 * @return
	 */
	public Object load(Class cl, Serializable id){
		return bem.load(cl, id);
	}

	/**
	 * <p>��������: deleteAll|����:�����ݿ���ɾ��������ָ�������ж��� </p>
	 * @param entities
	 */
	public void deleteAll(Collection entities){
		bem.deleteAll(entities);
	}

	/**
	 * <p>��������: find|����:����ָ����HQL�ַ�����ѯ���� </p>
	 * @param queryString
	 * @return
	 */
	public List find(String queryString){
		return bem.find(queryString);
	}

	/**
	 * <p>��������: find|����:����ָ����HQL�ַ���������������ѯ���� </p>
	 * @param query
	 * @param parameter
	 * @return
	 */
	public List find(String query, Object parameter){
		return bem.find(query, parameter);
	}

	/**
	 * <p>��������: find|����:����ָ����HQL�ַ��������������ѯ���� </p>
	 * @param query
	 * @param parameter
	 * @return
	 */
	public List find(String query, Object[] parameter){
		return bem.find(query, parameter);
	}

	/**
	 * <p>��������: getAllByDetachedCriteria|����:�����ݿ��в�ѯ���� </p>
	 * @param detachedCriteria
	 * @return
	 */
	public List getAllByDetachedCriteria(DetachedCriteria detachedCriteria){
		return bem.getAllByDetachedCriteria(detachedCriteria);
	};

	/**
	 * <p>��������: getCountByCriteria|����:��������ѯ�����ݸ��� </p>
	 * @param detachedCriteria
	 * @return
	 */
	public int getCountByCriteria(final DetachedCriteria detachedCriteria){
		return bem.getCountByCriteria(detachedCriteria);
	};

	/**
	 * <p>��������: getByFormWithPaging|����:��ҳ��ѯ���� </p>
	 * @param detachedCriteria
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	public PageBox getByFormWithPaging(final DetachedCriteria detachedCriteria,
			final int pageSize, final int pageNum){
		return bem.getByFormWithPaging(detachedCriteria, pageSize, pageNum);
	};

	/**
	 * <p>��������: useCreateQuery|����:����HQL�ַ�������һ��Query���� </p>
	 * @param sql
	 * @return
	 */
	public Query useCreateQuery(String sql){
		return bem.useCreateQuery(sql);
	}

	/**
	 * <p>��������: executeUpdate|����:����HQL�ַ����������� </p>
	 * @param hsql
	 * @return
	 */
	public int executeUpdate(String hsql){
		return bem.executeUpdate(hsql);
	}
	/**
	 * <p>��������: executeSQLUpdate|����:����SQL�ַ����������� </p>
	 * @param hsql
	 * @return
	 */
	public int executeSQLUpdate(String sql){
		return bem.executeSQLUpdate(sql);
	}

	/**
	 * <p>��������: find|����:����ָ����HQL�ַ����������б��ѯ���� </p>
	 * @param query
	 * @param parameters
	 * @return
	 */
	public List find(final String query, final List parameters){
		return bem.find(query, parameters);
	}

	/**
	 * <p>��������: find|����:����ָ����HQL�ַ����������б���ҳ��Ϣ��ѯ���� </p>
	 * @param hql
	 * @param parameters
	 * @param pageInfo
	 * @return
	 */
	public List find(final String hql, final List parameters,
			final PaginationList pageInfo){
		return bem.find(hql, parameters, pageInfo);
	}


	public BaseEntityManager getBem() {
		return bem;
	}
}
