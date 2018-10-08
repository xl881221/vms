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
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-23 下午04:08:05
 * @描述: [BaseEntityManager]hibernate实体操作基础类接口
 */
public interface BaseEntityManager{

	/**
	 * <p>方法名称: save|描述:保存一个实体对象 </p>
	 * @param entity
	 */
	public abstract void save(final Object entity);

	/**
	 * <p>方法名称: update|描述: 更新一个实体对象</p>
	 * @param entity
	 */
	public abstract void update(final Object entity);

	/**
	 * <p>方法名称: delete|描述: 删除一个实体对象</p>
	 * @param entity
	 */
	public abstract void delete(final Object entity);

	/**
	 * <p>方法名称: deleteAll|描述:删除集合中的所有实体对象 </p>
	 * @param entities
	 */
	public abstract void deleteAll(Collection entities);

	/**
	 * <p>方法名称: deleteById|描述:根据实体ID，删除数据 </p>
	 * @param cl
	 * @param id
	 */
	public abstract void deleteById(Class cl, Serializable id);

	/**
	 * <p>方法名称: load|描述:根据实体ID，查询数据 </p>
	 * @param entity
	 * @param id
	 * @return
	 */
	public abstract Object load(final Class entity, final Serializable id);

	/**
	 * <p>方法名称: get|描述:根据实体ID，查询数据 </p>
	 * @param entity
	 * @param id
	 * @return
	 */
	public abstract Object get(final Class entity, final Serializable id);

	/**
	 * <p>方法名称: getAllObject|描述: 返回所有该对象的数据</p>
	 * @param cl
	 * @return
	 */
	public abstract List getAllObject(Class cl);

	/**
	 * <p>方法名称: find|描述: 返回给定的HQL查询出来的数据</p>
	 * @param queryString
	 * @return
	 */
	public abstract List find(String queryString);

	/**
	 * <p>方法名称: find|描述:根据单个参数，返回给定的HQL查询出来的数据 </p>
	 * @param query
	 * @param parameter
	 * @return
	 */
	public abstract List find(final String query, final Object parameter);

	/**
	 * <p>方法名称: find|描述:根据多个参数，返回给定的HQL查询出来的数据 </p>
	 * @param query
	 * @param parameter
	 * @return
	 */
	public abstract List find(final String query, final Object[] parameter);

	/**
	 * <p>方法名称: find|描述:根据多个参数，返回给定的HQL查询出来的数据 </p>
	 * @param query
	 * @param parameter
	 * @return
	 */
	public abstract List find(final String query, final List parameters);

	/**
	 * <p>方法名称: find|描述: 返回给定的类实体的数据</p>
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
	 * 执行hsql批量更新
	 * @param sql
	 * @return
	 */
	public abstract int executeUpdate(String sql);
	
	public abstract int executeSQLUpdate(String sql);

	public abstract List find(final String hql, final List parameters,
			final PaginationList pageInfo);
}