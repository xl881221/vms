package fmss.services;

import java.util.Collection;
import java.util.List;

import fmss.common.util.PageBox;
import fmss.common.util.PaginationList;

import org.hibernate.criterion.DetachedCriteria;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-29 下午04:52:46
 * @描述: [BaseService]数据库操作基本接口
 */
public interface BaseService{

	/**
	 * <p>方法名称: save|描述:保存对象到数据库 </p>
	 * @param object
	 */
	public void save(final Object object);

	/**
	 * <p>方法名称: update|描述:更新一个对象到数据库 </p>
	 * @param object
	 */
	public void update(final Object object);

	/**
	 * <p>方法名称: delete|描述:从数据库中删除一个指定的对象 </p>
	 * @param object
	 */
	public void delete(Object object);

	/**
	 * <p>方法名称: deleteAll|描述:从数据库中删除集合中指定的所有对象 </p>
	 * @param entities
	 */
	public void deleteAll(Collection entities);

	/**
	 * <p>方法名称: getAllByDetachedCriteria|描述:从数据库中查询数据 </p>
	 * @param detachedCriteria
	 * @return
	 */
	public abstract List getAllByDetachedCriteria(
			DetachedCriteria detachedCriteria);

	/**
	 * <p>方法名称: getCountByCriteria|描述:返回所查询的数据个数 </p>
	 * @param detachedCriteria
	 * @return
	 */
	public abstract int getCountByCriteria(
			final DetachedCriteria detachedCriteria);

	/**
	 * <p>方法名称: getByFormWithPaging|描述:分页查询数据 </p>
	 * @param detachedCriteria
	 * @param pageSize
	 * @param pageNum
	 * @return
	 */
	public abstract PageBox getByFormWithPaging(
			final DetachedCriteria detachedCriteria, final int pageSize,
			final int pageNum);

	/**
	 * <p>方法名称: find|描述:根据指定的HQL字符串查询数据 </p>
	 * @param queryString
	 * @return
	 */
	public abstract List find(String queryString);

	/**
	 * <p>方法名称: find|描述:根据指定的HQL字符串及单个参数查询数据 </p>
	 * @param query
	 * @param parameter
	 * @return
	 */
	public abstract List find(final String query, final Object parameter);

	/**
	 * <p>方法名称: find|描述:根据指定的HQL字符串及多个参数查询数据 </p>
	 * @param query
	 * @param parameter
	 * @return
	 */
	public abstract List find(final String query, final Object[] parameter);

	/**
	 * <p>方法名称: find|描述:根据指定的HQL字符串及参数列表查询数据 </p>
	 * @param query
	 * @param parameters
	 * @return
	 */
	public abstract List find(final String query, final List parameters);

	/**
	 * <p>方法名称: find|描述:根据指定的HQL字符串、参数列表及分页信息查询数据 </p>
	 * @param hql
	 * @param parameters
	 * @param pageInfo
	 * @return
	 */
	public abstract List find(final String hql, final List parameters,
			final PaginationList pageInfo);
}