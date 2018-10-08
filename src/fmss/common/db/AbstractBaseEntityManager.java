package fmss.common.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import fmss.common.util.PageBox;
import fmss.common.util.PageObject;
import fmss.common.util.PaginationList;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: zhangshoufeng
 * @����: 2009-6-27 ����02:16:03
 * @����: [AbstractBaseEntityManager]hibernate�ײ�ʵ�������
 */
public class AbstractBaseEntityManager extends HibernateDaoSupport implements
		BaseEntityManager{

	/**
	 * <p>��������: save|����:����һ��ʵ����� </p>
	 * @param entity ����ʵ��
	 */
	public void save(final Object entity){
		getHibernateTemplate().save(entity);
	}

	/**
	 * <p>��������: update|����: ����һ��ʵ�����</p>
	 * @param entity ����ʵ��
	 */
	public void update(final Object entity){
		getHibernateTemplate().saveOrUpdate(entity);
	}

	/**
	 * <p>��������: delete|����: ɾ��һ��ʵ�����</p>
	 * @param entity ����ʵ��
	 */
	public void delete(final Object entity){
		getHibernateTemplate().delete(entity);
	}

	/**
	 * <p>��������: deleteAll|����:ɾ�������е�����ʵ����� </p>
	 * @param entities ���󼯺�
	 */
	public void deleteAll(Collection entities){
		this.getHibernateTemplate().deleteAll(entities);
	}

	/**
	 * <p>��������: deleteById|����:����ʵ��ID��ɾ������ </p>
	 * @param cl ����������
	 * @param id ����ID
	 */
	public void deleteById(Class cl, Serializable id){
		Object entity = this.getHibernateTemplate().get(cl, id);
		this.getHibernateTemplate().delete(entity);
	}

	/**
	 * <p>��������: load|����:����ʵ��ID����ѯ���� </p>
	 * @param entity ����������
	 * @param id ����ID
	 * @return
	 */
	public Object load(final Class entity, final Serializable id){
		return getHibernateTemplate().load(entity, id);
	}

	/**
	 * <p>��������: get|����:����ʵ��ID����ѯ���� </p>
	 * @param entity ����������
	 * @param id ����ID
	 * @return
	 */
	public Object get(final Class entity, final Serializable id){
		return getHibernateTemplate().get(entity, id);
	}

	/**
	 * <p>��������: getAllObject|����: �������иö��������</p>
	 * @param cl ����������
	 * @return
	 */
	public List getAllObject(Class cl){
		return this.getHibernateTemplate().loadAll(cl);
	}

	/**
	 * <p>��������: find|����: ���ظ�����HQL��ѯ����������</p>
	 * @param queryString ��ѯ�ַ���
	 * @return ���ظ����ַ�����ѯ���������б�
	 */
	public List find(String queryString){
		return this.getHibernateTemplate().find(queryString);
	}

	/**
	 * <p>��������: find|����:���ݵ������������ظ�����HQL��ѯ���������� </p>
	 * @param query ��ѯ�ַ���
	 * @param parameter ����
	 * @return ���ظ����ַ�����������ѯ���������б�
	 */
	public List find(final String query, final Object parameter){
		return getHibernateTemplate().find(query, parameter);
	}

	/**
	 * <p>��������: find|����:���ݶ�����������ظ�����HQL��ѯ���������� </p>
	 * @param query ��ѯ�ַ���
	 * @param parameter ����
	 * @return ���ظ����ַ�����������ѯ���������б�
	 */
	public List find(final String query, final Object[] parameter){
		return getHibernateTemplate().find(query, parameter);
	}

	/**
	 * <p>��������: find|����: ���ظ�������ʵ�������</p>
	 * @param queryString ��ѯ�ַ���
	 * @return ���ظ����ַ�����������ѯ���������б�
	 */
	public List findAll(final Class entity){
		return getHibernateTemplate().find("from " + entity.getName());
	}

	/* ���� Javadoc��
	* <p>��д����: findByNamedQuery|����:����Query���Ʒ��ز�ѯ�б� </p>
	* @param namedQuery query����
	* @return  ����ָ������Ĳ�ѯ�б�
	* @see fmss.common.db.BaseEntityManager#findByNamedQuery(java.lang.String)
	*/
	public List findByNamedQuery(final String namedQuery){
		return getHibernateTemplate().findByNamedQuery(namedQuery);
	}

	/* ���� Javadoc��
	* <p>��д����: findByNamedQuery|����:����Query���Ƽ���ѯ�������ز�ѯ�б�  </p>
	* @param query  query����
	* @param parameter ��ѯ����
	* @return  ����ָ������Ĳ�ѯ�б�
	* @see fmss.common.db.BaseEntityManager#findByNamedQuery(java.lang.String, java.lang.Object)
	*/
	public List findByNamedQuery(final String query, final Object parameter){
		return getHibernateTemplate().findByNamedQuery(query, parameter);
	}

	/* ���� Javadoc��
	* <p>��д����: findByNamedQuery|����: ����Query���Ƽ���ѯ�����б��ز�ѯ�б�</p>
	* @param query query����
	* @param parameters ��ѯ�����б�
	* @return ����ָ������Ĳ�ѯ�б�
	* @see fmss.common.db.BaseEntityManager#findByNamedQuery(java.lang.String, java.lang.Object[])
	*/
	public List findByNamedQuery(final String query, final Object[] parameters){
		return getHibernateTemplate().findByNamedQuery(query, parameters);
	}

	/* ���� Javadoc��
	* <p>��д����: getAllByDetachedCriteria|����:����DetachedCriteria��ѯ���󣬽������ݲ�ѯ </p>
	* @param detachedCriteria DetachedCriteria��ѯ����
	* @return  ����ָ������Ĳ�ѯ�б�
	* @see fmss.common.db.BaseEntityManager#getAllByDetachedCriteria(org.hibernate.criterion.DetachedCriteria)
	*/
	public List getAllByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.getHibernateTemplate().findByCriteria(detachedCriteria);
	}

	/* ���� Javadoc��
	* <p>��д����: getCountByCriteria|����: ����DetachedCriteria��ѯ�������</p>
	* @param detachedCriteria DetachedCriteria��ѯ����
	* @return ����ָ���������
	* @see fmss.common.db.BaseEntityManager#getCountByCriteria(org.hibernate.criterion.DetachedCriteria)
	*/
	public int getCountByCriteria(final DetachedCriteria detachedCriteria){
		Integer count = (Integer) this.getHibernateTemplate().execute(
				new HibernateCallback(){

					public Object doInHibernate(Session session)
							throws HibernateException{
						Criteria criteria = detachedCriteria
								.getExecutableCriteria(session);
						return criteria.setProjection(Projections.rowCount())
								.uniqueResult();
					}
				});
		return count.intValue();
	}

	/* ���� Javadoc��
	* <p>��д����: getByFormWithPaging|����: </p>
	* @param detachedCriteria
	* @param pageSize
	* @param pageNum
	* @return
	* @see fmss.common.db.BaseEntityManager#getByFormWithPaging(org.hibernate.criterion.DetachedCriteria, int, int)
	*/
	public PageBox getByFormWithPaging(final DetachedCriteria detachedCriteria,
			final int pageSize, final int pageNum){
		return (PageBox) getHibernateTemplate().execute(
				new HibernateCallback(){

					public Object doInHibernate(Session session)
							throws HibernateException{
						Criteria criteria = detachedCriteria
								.getExecutableCriteria(session);
						int itemAmount = ((Integer) criteria.setProjection(
								Projections.rowCount()).uniqueResult())
								.intValue();
						criteria.setProjection(null);
						PageBox pageBox = new PageBox();
						java.util.List pageList = null;
						PageObject pageObject = new PageObject();
						pageObject.setPageSize(pageSize);
						pageObject.setPageIndex(pageNum);
						pageObject.setItemAmount(itemAmount);
						if(pageObject.getBeginIndex() <= pageObject
								.getItemAmount()){
							pageList = criteria.setFirstResult(
									pageObject.getBeginIndex() - 1)
									.setMaxResults(pageSize).list();
						}
						pageBox.setPageObject(pageObject);
						pageBox.setPageList(pageList);
						return pageBox;
					}
				});
	}

	/* ���� Javadoc��
	* <p>��д����: useCreateQuery|����: ����HQL��������query����</p>
	* @param sql ��ѯ��
	* @return query����
	* @see fmss.common.db.BaseEntityManager#useCreateQuery(java.lang.String)
	*/
	public Query useCreateQuery(String sql){
		return this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createQuery(sql);
	}

	/* ���� Javadoc��
	* <p>��д����: find|����:���ݲ����б�HQL��ѯ�������������б� </p>
	* @param query HQL��ѯ��
	* @param parameters �����б�
	* @return ָ������������б�
	* @see fmss.common.db.BaseEntityManager#find(java.lang.String, java.util.List)
	*/
	public List find(final String query, final List parameters){
		List result = getHibernateTemplate().executeFind(
				new HibernateCallback(){

					public Object doInHibernate(Session session)
							throws HibernateException{
						Query qu = session.createQuery(query);
						setParameters(qu, parameters);
						List l = qu.list();
						return l;
					}
				});
		return result;
	}

	/**
	 * <p>��������: find|����:���ݶ�����������ظ�����HQL��ѯ���������� </p>
	 * @param query ��ѯ��
	 * @param parameter �����б�
	 * @return ���ز�ѯ�������б�
	 */
	public List find(final String hql, final List parameters,
			final PaginationList pageInfo){
		return (List) getHibernateTemplate().execute(new HibernateCallback(){

			public Object doInHibernate(Session session){
				pageInfo.setRecordCount(getRowCount(hql, parameters)
						.longValue());
				Query query = session.createQuery(hql);
				setParameters(query, parameters);
				int startIndex = (pageInfo.getCurrentPage() - 1)
						* pageInfo.getPageSize();
				query.setFirstResult(startIndex);
				query.setMaxResults(pageInfo.getPageSize());
				query.setCacheable(true);
				Object obj = query.list();
				if(obj == null){
					obj = new ArrayList();
				}
				pageInfo.setRecordList((List) obj);
				return (List) obj;
			}
		});
	}

	/**
	 * <p>��������: getRowCount|����:����HQL��ѯ���������б�ͳ����Ӱ������� </p>
	 * @param query HQL��ѯ��
	 * @param parameters �����б�
	 * @return ������Ӱ�������
	 */
	public Long getRowCount(final String query, final List parameters){
		Long i = (Long) getHibernateTemplate().execute(new HibernateCallback(){

			public Object doInHibernate(Session session)
					throws HibernateException{
				StringBuffer coutSql = new StringBuffer("select count(*) ");
				String trimSQL = query;
				if(StringUtils.contains(query.toLowerCase(), "select")){
					trimSQL = query.substring(query.indexOf("from"), query
							.length());
				}
				if(StringUtils.contains(trimSQL.toLowerCase(), "order")){
					trimSQL = trimSQL.substring(0, trimSQL.indexOf("order"));
				}
				coutSql.append(trimSQL);
				Query qu = session.createQuery(coutSql.toString());
				setParameters(qu, parameters);
				Iterator irr = qu.iterate();
				return (Long) irr.next();
			}
		});
		return i;
	}

	/**
	 * <p>��������: setParameters|����:���ò�ѯ������ </p>
	 * @param query query��ѯ����
	 * @param parameters �����б�
	 */
	private void setParameters(Query query, List parameters){
		for(int i = 0; i < parameters.size(); i++){
			if(parameters.get(i).getClass().getName().equals(
					java.sql.Date.class.getName())){
				query.setDate(i, (java.sql.Date) parameters.get(i));
			}else if(parameters.get(i).getClass().getName().equals(
					java.util.Date.class.getName())){
				query.setTimestamp(i, (java.util.Date) parameters.get(i));
			}else{
				query.setString(i, parameters.get(i).toString());
			}
		}
	}

	/* ���� Javadoc��
	* <p>��д����: executeUpdate|����: ����HQL�ַ���,���ж�����²���</p>
	* @param hsql HQL�ַ���
	* @return �����µļ�¼��
	* @see fmss.common.db.BaseEntityManager#executeUpdate(java.lang.String)
	*/
	public int executeUpdate(final String hsql){
		Long i = (Long) getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session)
					throws HibernateException{
				int deletedEntities = 0;
				deletedEntities = session.createQuery(hsql).executeUpdate();
				return new Long(deletedEntities);
			}
		});
		return i.intValue();
	}
	public int executeSQLUpdate(final String sql){
		Long i = (Long) getHibernateTemplate().execute(new HibernateCallback(){
			public Object doInHibernate(Session session)
					throws HibernateException{
				int deletedEntities = 0;
				deletedEntities = session.createSQLQuery(sql).executeUpdate();
				return new Long(deletedEntities);
			}
		});
		return i.intValue();
	}
}
