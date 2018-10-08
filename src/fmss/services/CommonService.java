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
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-23 上午10:31:18
 * @描述:[CommonService]公共类，提供最基本的数据库操作代码
 */
public class CommonService implements BaseService{

	private BaseEntityManager bem; // 实体管理对象

	/**
	 * <p>方法名称: setBem|描述:注入实体类管理 </p>
	 * @param bem
	 */
	public void setBem(BaseEntityManager bem){
		this.bem = bem;
	}
	

	/**
	 * <p>方法名称: save|描述:保存对象到数据库 </p>
	 * @param object
	 */
	public void save(final Object object){
		bem.save(object);
	}

	/**
	 * <p>方法名称: update|描述:更新一个对象到数据库 </p>
	 * @param object
	 */
	public void update(final Object object){
		bem.update(object);
	}

	/**
	 * <p>方法名称: delete|描述:从数据库中删除一个指定的对象 </p>
	 * @param object
	 */
	public void delete(Object object){
		bem.delete(object);
	}

	/**
	 * <p>方法名称: deleteAll|描述:根据指定的ID从数据库中删除数据 </p>
	 * @param id
	 */
	public void deleteById(Class cl, Serializable id){
		bem.deleteById(cl, id);
	}

	/**
	 * <p>方法名称: get|描述:根据指定的ID查询数据 </p>
	 * @param cl
	 * @param id
	 * @return
	 */
	public Object get(Class cl, Serializable id){
		return bem.get(cl, id);
	}

	/**
	 * <p>方法名称: load|描述:根据指定的ID查询数据 </p>
	 * @param cl
	 * @param id
	 * @return
	 */
	public Object load(Class cl, Serializable id){
		return bem.load(cl, id);
	}

	/**
	 * <p>方法名称: deleteAll|描述:从数据库中删除集合中指定的所有对象 </p>
	 * @param entities
	 */
	public void deleteAll(Collection entities){
		bem.deleteAll(entities);
	}

	/**
	 * <p>方法名称: find|描述:根据指定的HQL字符串查询数据 </p>
	 * @param queryString
	 * @return
	 */
	public List find(String queryString){
		return bem.find(queryString);
	}

	/**
	 * <p>方法名称: find|描述:根据指定的HQL字符串及单个参数查询数据 </p>
	 * @param query
	 * @param parameter
	 * @return
	 */
	public List find(String query, Object parameter){
		return bem.find(query, parameter);
	}

	/**
	 * <p>方法名称: find|描述:根据指定的HQL字符串及多个参数查询数据 </p>
	 * @param query
	 * @param parameter
	 * @return
	 */
	public List find(String query, Object[] parameter){
		return bem.find(query, parameter);
	}

	/**
	 * <p>方法名称: getAllByDetachedCriteria|描述:从数据库中查询数据 </p>
	 * @param detachedCriteria
	 * @return
	 */
	public List getAllByDetachedCriteria(DetachedCriteria detachedCriteria){
		return bem.getAllByDetachedCriteria(detachedCriteria);
	};

	/**
	 * <p>方法名称: getCountByCriteria|描述:返回所查询的数据个数 </p>
	 * @param detachedCriteria
	 * @return
	 */
	public int getCountByCriteria(final DetachedCriteria detachedCriteria){
		return bem.getCountByCriteria(detachedCriteria);
	};

	/**
	 * <p>方法名称: getByFormWithPaging|描述:分页查询数据 </p>
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
	 * <p>方法名称: useCreateQuery|描述:根据HQL字符串返回一个Query对象 </p>
	 * @param sql
	 * @return
	 */
	public Query useCreateQuery(String sql){
		return bem.useCreateQuery(sql);
	}

	/**
	 * <p>方法名称: executeUpdate|描述:根据HQL字符串更新数据 </p>
	 * @param hsql
	 * @return
	 */
	public int executeUpdate(String hsql){
		return bem.executeUpdate(hsql);
	}
	/**
	 * <p>方法名称: executeSQLUpdate|描述:根据SQL字符串更新数据 </p>
	 * @param hsql
	 * @return
	 */
	public int executeSQLUpdate(String sql){
		return bem.executeSQLUpdate(sql);
	}

	/**
	 * <p>方法名称: find|描述:根据指定的HQL字符串及参数列表查询数据 </p>
	 * @param query
	 * @param parameters
	 * @return
	 */
	public List find(final String query, final List parameters){
		return bem.find(query, parameters);
	}

	/**
	 * <p>方法名称: find|描述:根据指定的HQL字符串、参数列表及分页信息查询数据 </p>
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
