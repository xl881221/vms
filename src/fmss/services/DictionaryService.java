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
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: sunzhan
 * @日期: 2009-6-24 上午09:28:27
 * @描述: [DictionaryService]字典服务类
 */
public class DictionaryService extends CommonService{

	/** bem 实体管理对象 */
	private BaseEntityManager bem;
	/** RESOURCE_DIC_TYPE 资源 */
	public static final String RESOURCE_DIC_TYPE = "RT";
	/** RESOURCE_DIC_TYPE 资源-菜单 */
	public static final String MEN_RESOURCE_DIC_TYPE = "MEN";
	/** RESOURCE_DIC_TYPE 资源-机构 */
	public static final String ORG_RESOURCE_DIC_TYPE = "ORG";
	/** RESOURCE_DIC_TYPE 资源-报表 */
	public static final String RPT_RESOURCE_DIC_TYPE = "RPT";
	/** 资源类型-公有 */
	public static final String PUB_RESOURCE_DIC_TYPE = "PUB";
	/** 资源类型-私有*/
	public static final String PRI_RESOURCE_DIC_TYPE = "PRI";
	/** DB_DIC_TYPE 数据库 */
	public static final String DB_DIC_TYPE = "DBT";
	/** AOT_DIC_TYPE 权限主体类别 */
	public static final String AOT_DIC_TYPE = "AOT";
	/** ROLE_AOT_DIC_TYPE 权限主体类别-角色 */
	public static final String ROLE_AOT_DIC_TYPE = "ROLE";
	/** USER_AOT_DIC_TYPE 权限主体类别-用户 */
	public static final String USER_AOT_DIC_TYPE = "USER";
	/** USER_AOT_DIC_TYPE 权限主体 */
	public static final String AUTH_AOT_DIC_TYPE = "AUTH";
	/** RES_AOT_DIC_TYPE 资源 */
	public static final String RES_AOT_DIC_TYPE = "RES";
	/** NOTICE_TYPE 通告 */
	public static final String NOTICE_TYPE = "NT";
	/** NOTICE_TYPE 通告主表 */
	public static final String NOTICE_MAIN_TYPE = "NTM";
	/** NOTICE_TYPE 通告日志 */
	public static final String NOTICE_LOG_TYPE = "NTL";
	/** NOTICE_TYPE 通告附件 */
	public static final String NOTICE_AFFIX_TYPE = "NTA";
	/** NOTICE_TYPE 通告反馈 */
	public static final String NOTICE_FEEDBACK_TYPE = "NTFB";
	/** QUERY_STRING 查询hql */
	public static final String QUERY_STRING = "select udb from UBaseDictionaryDO udb where udb.dicType=? order by udb.orderNum";

	/* （非 Javadoc）
	* <p>重写方法: delete|描述:删除对象 </p>
	* @param object 对象
	* @see fmss.services.CommonService#delete(java.lang.Object)
	*/
	public void delete(Object object){
		// TODO Auto-generated method stub
	}

	/* （非 Javadoc）
	* <p>重写方法: deleteAll|描述:删除集合 </p>
	* @param entities 集合对象
	* @see fmss.services.CommonService#deleteAll(java.util.Collection)
	*/
	public void deleteAll(Collection entities){
		// TODO Auto-generated method stub
	}

	/* （非 Javadoc）
	* <p>重写方法: find|描述:根据hql查询对象 </p>
	* @param queryString 查询hql
	* @return 查询结果集合
	* @see fmss.services.CommonService#find(java.lang.String)
	*/
	public List find(String queryString){
		// TODO Auto-generated method stub
		return null;
	}

	/* （非 Javadoc）
	* <p>重写方法: find|描述:查询字典对象 </p>
	* @param query 查询hql
	* @param parameter 查询参数
	* @return 查询结果集合
	* @see fmss.services.CommonService#find(java.lang.String, java.lang.Object)
	*/
	public List find(String query, Object parameter){
		return this.bem.find(query, parameter);
	}

	/* （非 Javadoc）
	* <p>重写方法: find|描述:根据hql查询对象 </p>
	* @param query 查询hql
	* @param parameter 查询参数数组
	* @return 查询结果集合
	* @see fmss.services.CommonService#find(java.lang.String, java.lang.Object[])
	*/
	public List find(String query, Object[] parameter){
		// TODO Auto-generated method stub
		return null;
	}

	/* （非 Javadoc）
	* <p>重写方法: find|描述:根据hql查询对象 </p>
	* @param query 查询hql
	* @param parameters 查询参数列表
	* @return 查询结果集合
	* @see fmss.services.CommonService#find(java.lang.String, java.util.List)
	*/
	public List find(String query, List parameters){
		// TODO Auto-generated method stub
		return null;
	}

	/* （非 Javadoc）
	* <p>重写方法: find|描述:根据hql查询对象，带分页功能 </p>
	* @param hql 查询hql
	* @param parameters 查询参数列表
	* @param pageInfo 分页对象
	* @return 查询结果集合
	*/
	public List find(String hql, List parameters, PaginationList pageInfo){
		// TODO Auto-generated method stub
		return null;
	}

	/* （非 Javadoc）
	* <p>重写方法: getAllByDetachedCriteria|描述:根据条件查询 </p>
	* @param detachedCriteria 查询条件
	* @return 查询结果集合
	* @see fmss.services.CommonService#getAllByDetachedCriteria(org.hibernate.criterion.DetachedCriteria)
	*/
	public List getAllByDetachedCriteria(DetachedCriteria detachedCriteria){
		// TODO Auto-generated method stub
		return null;
	}

	/* （非 Javadoc）
	* <p>重写方法: getByFormWithPaging|描述:根据分页参数条件查询 </p>
	* @param detachedCriteria 查询条件
	* @param pageSize 页面size
	* @param pageNum 页数
	* @return 分页对象
	* @see fmss.services.CommonService#getByFormWithPaging(org.hibernate.criterion.DetachedCriteria, int, int)
	*/
	public PageBox getByFormWithPaging(DetachedCriteria detachedCriteria,
			int pageSize, int pageNum){
		// TODO Auto-generated method stub
		return null;
	}

	/* （非 Javadoc）
	* <p>重写方法: getCountByCriteria|描述:根据条件查询记录数 </p>
	* @param detachedCriteria 查询条件
	* @return 记录数
	* @see fmss.services.CommonService#getCountByCriteria(org.hibernate.criterion.DetachedCriteria)
	*/
	public int getCountByCriteria(DetachedCriteria detachedCriteria){
		// TODO Auto-generated method stub
		return 0;
	}

	/* （非 Javadoc）
	* <p>重写方法: save|描述:保存对象 </p>
	* @param object 对象
	* @see fmss.services.CommonService#save(java.lang.Object)
	*/
	public void save(Object object){
		// TODO Auto-generated method stub
	}

	/* （非 Javadoc）
	* <p>重写方法: update|描述:更新对象 </p>
	* @param object 对象
	* @see fmss.services.CommonService#update(java.lang.Object)
	*/
	public void update(Object object){
		// TODO Auto-generated method stub
	}

	/**
	 * <p>方法名称: addDefault|描述:添加默认字典对象 </p>
	 * @param list 字典列表
	 * @param dic 默认字典对象
	 * @return 字典列表
	 */
	public List addDefault(List list, UBaseDictionaryDO dic){
		list = new ArrayList();
		list.add(dic);
		return list;
	}

	/**
	 * <p>方法名称: getDictoryBy|描述: 根据字典类型，获取字典信息</p>
	 * @param dicType 字典类型
	 * @return 返回此类型下的所有字典数据
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
	 * @return 实体管理器
	 */
	public BaseEntityManager getBem(){
		return bem;
	}

	/**
	 * @param bem 要设置的 实体管理器
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
