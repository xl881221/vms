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
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-27 下午02:16:03
 * @描述: [AbstractBaseEntityManager]hibernate底层实体操作类
 */
public class AbstractBaseEntityManager extends HibernateDaoSupport implements
		BaseEntityManager{

	/**
	 * <p>方法名称: save|描述:保存一个实体对象 </p>
	 * @param entity 对象实体
	 */
	public void save(final Object entity){
		getHibernateTemplate().save(entity);
	}

	/**
	 * <p>方法名称: update|描述: 更新一个实体对象</p>
	 * @param entity 对象实体
	 */
	public void update(final Object entity){
		getHibernateTemplate().saveOrUpdate(entity);
	}

	/**
	 * <p>方法名称: delete|描述: 删除一个实体对象</p>
	 * @param entity 对象实体
	 */
	public void delete(final Object entity){
		getHibernateTemplate().delete(entity);
	}

	/**
	 * <p>方法名称: deleteAll|描述:删除集合中的所有实体对象 </p>
	 * @param entities 对象集合
	 */
	public void deleteAll(Collection entities){
		this.getHibernateTemplate().deleteAll(entities);
	}

	/**
	 * <p>方法名称: deleteById|描述:根据实体ID，删除数据 </p>
	 * @param cl 对象类类型
	 * @param id 对象ID
	 */
	public void deleteById(Class cl, Serializable id){
		Object entity = this.getHibernateTemplate().get(cl, id);
		this.getHibernateTemplate().delete(entity);
	}

	/**
	 * <p>方法名称: load|描述:根据实体ID，查询数据 </p>
	 * @param entity 对象类类型
	 * @param id 对象ID
	 * @return
	 */
	public Object load(final Class entity, final Serializable id){
		return getHibernateTemplate().load(entity, id);
	}

	/**
	 * <p>方法名称: get|描述:根据实体ID，查询数据 </p>
	 * @param entity 对象类类型
	 * @param id 对象ID
	 * @return
	 */
	public Object get(final Class entity, final Serializable id){
		return getHibernateTemplate().get(entity, id);
	}

	/**
	 * <p>方法名称: getAllObject|描述: 返回所有该对象的数据</p>
	 * @param cl 对象类类型
	 * @return
	 */
	public List getAllObject(Class cl){
		return this.getHibernateTemplate().loadAll(cl);
	}

	/**
	 * <p>方法名称: find|描述: 返回给定的HQL查询出来的数据</p>
	 * @param queryString 查询字符串
	 * @return 返回根据字符串查询到的数据列表
	 */
	public List find(String queryString){
		return this.getHibernateTemplate().find(queryString);
	}

	/**
	 * <p>方法名称: find|描述:根据单个参数，返回给定的HQL查询出来的数据 </p>
	 * @param query 查询字符串
	 * @param parameter 参数
	 * @return 返回根据字符串及参数查询到的数据列表
	 */
	public List find(final String query, final Object parameter){
		return getHibernateTemplate().find(query, parameter);
	}

	/**
	 * <p>方法名称: find|描述:根据多个参数，返回给定的HQL查询出来的数据 </p>
	 * @param query 查询字符串
	 * @param parameter 参数
	 * @return 返回根据字符串及参数查询到的数据列表
	 */
	public List find(final String query, final Object[] parameter){
		return getHibernateTemplate().find(query, parameter);
	}

	/**
	 * <p>方法名称: find|描述: 返回给定的类实体的数据</p>
	 * @param queryString 查询字符串
	 * @return 返回根据字符串及参数查询到的数据列表
	 */
	public List findAll(final Class entity){
		return getHibernateTemplate().find("from " + entity.getName());
	}

	/* （非 Javadoc）
	* <p>重写方法: findByNamedQuery|描述:根据Query名称返回查询列表 </p>
	* @param namedQuery query名称
	* @return  返回指定对象的查询列表
	* @see fmss.common.db.BaseEntityManager#findByNamedQuery(java.lang.String)
	*/
	public List findByNamedQuery(final String namedQuery){
		return getHibernateTemplate().findByNamedQuery(namedQuery);
	}

	/* （非 Javadoc）
	* <p>重写方法: findByNamedQuery|描述:根据Query名称及查询参数返回查询列表  </p>
	* @param query  query名称
	* @param parameter 查询参数
	* @return  返回指定对象的查询列表
	* @see fmss.common.db.BaseEntityManager#findByNamedQuery(java.lang.String, java.lang.Object)
	*/
	public List findByNamedQuery(final String query, final Object parameter){
		return getHibernateTemplate().findByNamedQuery(query, parameter);
	}

	/* （非 Javadoc）
	* <p>重写方法: findByNamedQuery|描述: 根据Query名称及查询参数列表返回查询列表</p>
	* @param query query名称
	* @param parameters 查询参数列表
	* @return 返回指定对象的查询列表
	* @see fmss.common.db.BaseEntityManager#findByNamedQuery(java.lang.String, java.lang.Object[])
	*/
	public List findByNamedQuery(final String query, final Object[] parameters){
		return getHibernateTemplate().findByNamedQuery(query, parameters);
	}

	/* （非 Javadoc）
	* <p>重写方法: getAllByDetachedCriteria|描述:根据DetachedCriteria查询对象，进行数据查询 </p>
	* @param detachedCriteria DetachedCriteria查询对象
	* @return  返回指定对象的查询列表
	* @see fmss.common.db.BaseEntityManager#getAllByDetachedCriteria(org.hibernate.criterion.DetachedCriteria)
	*/
	public List getAllByDetachedCriteria(DetachedCriteria detachedCriteria){
		return this.getHibernateTemplate().findByCriteria(detachedCriteria);
	}

	/* （非 Javadoc）
	* <p>重写方法: getCountByCriteria|描述: 根据DetachedCriteria查询对象个数</p>
	* @param detachedCriteria DetachedCriteria查询对象
	* @return 返回指定对象个数
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

	/* （非 Javadoc）
	* <p>重写方法: getByFormWithPaging|描述: </p>
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

	/* （非 Javadoc）
	* <p>重写方法: useCreateQuery|描述: 根据HQL串，返回query对象</p>
	* @param sql 查询串
	* @return query对象
	* @see fmss.common.db.BaseEntityManager#useCreateQuery(java.lang.String)
	*/
	public Query useCreateQuery(String sql){
		return this.getHibernateTemplate().getSessionFactory()
				.getCurrentSession().createQuery(sql);
	}

	/* （非 Javadoc）
	* <p>重写方法: find|描述:根据参数列表及HQL查询串，返回数据列表 </p>
	* @param query HQL查询串
	* @param parameters 参数列表
	* @return 指定对象的数据列表
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
	 * <p>方法名称: find|描述:根据多个参数，返回给定的HQL查询出来的数据 </p>
	 * @param query 查询串
	 * @param parameter 参数列表
	 * @return 返回查询到数据列表
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
	 * <p>方法名称: getRowCount|描述:根据HQL查询串及参数列表，统计受影响的行数 </p>
	 * @param query HQL查询串
	 * @param parameters 参数列表
	 * @return 返回受影响的行数
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
	 * <p>方法名称: setParameters|描述:设置查询的属性 </p>
	 * @param query query查询对象
	 * @param parameters 参数列表
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

	/* （非 Javadoc）
	* <p>重写方法: executeUpdate|描述: 根据HQL字符串,进行对象更新操作</p>
	* @param hsql HQL字符串
	* @return 被更新的记录数
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
