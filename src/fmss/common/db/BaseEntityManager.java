/*
 * 
 */
package fmss.common.db;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import fmss.common.util.PageBox;
import fmss.common.util.PaginationList;

import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-23 ����04:08:05
 * @����: [BaseEntityManager]hibernateʵ�����������ӿ�
 */
public interface BaseEntityManager{

	/**
	 * <p>��������: save|����:����һ��ʵ����� </p>
	 * @param entity
	 */
	public abstract void save(final Object entity);

	/**
	 * <p>��������: update|����: ����һ��ʵ�����</p>
	 * @param entity
	 */
	public abstract void update(final Object entity);

	/**
	 * <p>��������: delete|����: ɾ��һ��ʵ�����</p>
	 * @param entity
	 */
	public abstract void delete(final Object entity);

	/**
	 * <p>��������: deleteAll|����:ɾ�������е�����ʵ����� </p>
	 * @param entities
	 */
	public abstract void deleteAll(Collection entities);

	/**
	 * <p>��������: deleteById|����:����ʵ��ID��ɾ������ </p>
	 * @param cl
	 * @param id
	 */
	public abstract void deleteById(Class cl, Serializable id);

	/**
	 * <p>��������: load|����:����ʵ��ID����ѯ���� </p>
	 * @param entity
	 * @param id
	 * @return
	 */
	public abstract Object load(final Class entity, final Serializable id);

	/**
	 * <p>��������: get|����:����ʵ��ID����ѯ���� </p>
	 * @param entity
	 * @param id
	 * @return
	 */
	public abstract Object get(final Class entity, final Serializable id);

	/**
	 * <p>��������: getAllObject|����: �������иö��������</p>
	 * @param cl
	 * @return
	 */
	public abstract List getAllObject(Class cl);

	/**
	 * <p>��������: find|����: ���ظ�����HQL��ѯ����������</p>
	 * @param queryString
	 * @return
	 */
	public abstract List find(String queryString);

	/**
	 * <p>��������: find|����:���ݵ������������ظ�����HQL��ѯ���������� </p>
	 * @param query
	 * @param parameter
	 * @return
	 */
	public abstract List find(final String query, final Object parameter);

	/**
	 * <p>��������: find|����:���ݶ�����������ظ�����HQL��ѯ���������� </p>
	 * @param query
	 * @param parameter
	 * @return
	 */
	public abstract List find(final String query, final Object[] parameter);

	/**
	 * <p>��������: find|����:���ݶ�����������ظ�����HQL��ѯ���������� </p>
	 * @param query
	 * @param parameter
	 * @return
	 */
	public abstract List find(final String query, final List parameters);

	/**
	 * <p>��������: find|����: ���ظ�������ʵ�������</p>
	 * @param queryString
	 * @return
	 */
	public abstract List findAll(final Class entity);

	public abstract List findByNamedQuery(final String namedQuery);

	public abstract List findByNamedQuery(final String query,
			final Object parameter);

	public abstract List findByNamedQuery(final String query,
			final Object[] parameters);

	public abstract List getAllByDetachedCriteria(
			DetachedCriteria detachedCriteria);

	public abstract int getCountByCriteria(
			final DetachedCriteria detachedCriteria);

	public abstract PageBox getByFormWithPaging(
			final DetachedCriteria detachedCriteria, final int pageSize,
			final int pageNum);

	public abstract Query useCreateQuery(String sql);

	/**
	 * ִ��hsql��������
	 * @param sql
	 * @return
	 */
	public abstract int executeUpdate(String sql);
	
	public abstract int executeSQLUpdate(String sql);

	public abstract List find(final String hql, final List parameters,
			final PaginationList pageInfo);
}