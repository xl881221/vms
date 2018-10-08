package fmss.services;

import java.util.Collection;
import java.util.List;

import fmss.common.util.PageBox;
import fmss.common.util.PaginationList;

import org.hibernate.criterion.DetachedCriteria;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-29 ����04:52:46
 * @����: [BaseService]���ݿ���������ӿ�
 */
public interface BaseService{

	/**
	 * <p>��������: save|����:����������ݿ� </p>
	 * @param object
	 */
	public void save(final Object object);

	/**
	 * <p>��������: update|����:����һ���������ݿ� </p>
	 * @param object
	 */
	public void update(final Object object);

	/**
	 * <p>��������: delete|����:�����ݿ���ɾ��һ��ָ���Ķ��� </p>
	 * @param object
	 */
	public void delete(Object object);

	/**
	 * <p>��������: deleteAll|����:�����ݿ���ɾ��������ָ�������ж��� </p>
	 * @param entities
	 */
	public void deleteAll(Collection entities);

	/**
	 * <p>��������: getAllByDetachedCriteria|����:�����ݿ��в�ѯ���� </p>
	 * @param detachedCriteria
	 * @return
	 */
	public abstract List getAllByDetachedCriteria(
			DetachedCriteria detachedCriteria);

	/**
	 * <p>��������: getCountByCriteria|����:��������ѯ�����ݸ��� </p>
	 * @param detachedCriteria
	 * @return
	 */
	public abstract int getCountByCriteria(
			final DetachedCriteria detachedCriteria);

	/**
	 * <p>��������: getByFormWithPaging|����:��ҳ��ѯ���� </p>
	 * @param detachedCriteria
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	public abstract PageBox getByFormWithPaging(
			final DetachedCriteria detachedCriteria, final int pageSize,
			final int pageNum);

	/**
	 * <p>��������: find|����:����ָ����HQL�ַ�����ѯ���� </p>
	 * @param queryString
	 * @return
	 */
	public abstract List find(String queryString);

	/**
	 * <p>��������: find|����:����ָ����HQL�ַ���������������ѯ���� </p>
	 * @param query
	 * @param parameter
	 * @return
	 */
	public abstract List find(final String query, final Object parameter);

	/**
	 * <p>��������: find|����:����ָ����HQL�ַ��������������ѯ���� </p>
	 * @param query
	 * @param parameter
	 * @return
	 */
	public abstract List find(final String query, final Object[] parameter);

	/**
	 * <p>��������: find|����:����ָ����HQL�ַ����������б��ѯ���� </p>
	 * @param query
	 * @param parameters
	 * @return
	 */
	public abstract List find(final String query, final List parameters);

	/**
	 * <p>��������: find|����:����ָ����HQL�ַ����������б���ҳ��Ϣ��ѯ���� </p>
	 * @param hql
	 * @param parameters
	 * @param pageInfo
	 * @return
	 */
	public abstract List find(final String hql, final List parameters,
			final PaginationList pageInfo);
}