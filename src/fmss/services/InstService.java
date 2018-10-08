package fmss.services;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fmss.action.base.AuditBase;
import fmss.action.base.InstBaseAuditBase;
import fmss.action.base.JdbcDaoAccessor;
import fmss.action.base.SysParamAuditBase;
import fmss.action.entity.UBaseInstChangeDO;
import fmss.action.entity.UBaseSysParamChangeDO;
import fmss.common.util.BeanUtil;
import fmss.dao.entity.BaseUserEmailDO;
import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseInstDO;
import fmss.dao.entity.UBaseInstRelaDO;
import fmss.dao.entity.UBaseUserDO;
import fmss.dao.entity.VcrmsSystemRelaNewDO;
import fmss.common.ui.InstGridTreeNode;
import fmss.common.db.IdGenerator;
import fmss.common.util.PageBox;
import fmss.common.util.PaginationList;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;

import net.sf.json.JSONObject;

/**
 * <p> 版权所有:(C)2003-2010  </p>
 * @作者: zhangshoufeng
 * @日期: 2009-6-23 上午10:34:18
 * @描述:[InstService]机构操作service类
 */
public class InstService extends CommonService{

	private static final Log log = LogFactory.getLog(InstService.class);
	private JdbcDaoAccessor jdbcDaoAccessor;

	public void setJdbcDaoAccessor(JdbcDaoAccessor jdbcDaoAccessor){
		this.jdbcDaoAccessor = jdbcDaoAccessor;
	}

	/*
	 * 查看当前机构是否存在用户
	 */
	public String isHavingUserInInst(String instId){
		String hql = "select ubu from UBaseUserDO ubu where ubu.instId='"
				+ instId + "'";
		List l = this.find(hql);
		if(l != null && l.size() > 0)
			return "1";
		return "0";
	}
	
	/*
	 * 查询未设置邮箱的用户信息【翻页】
	 */
	public List userInInst(String userEname, String bankId, String systemId,
			PaginationList paginationList){
		List paras = new ArrayList();
		if(StringUtils.isBlank(bankId)){
			paras.add("");
		}else{
			paras.add(bankId);
		}
		if(StringUtils.isBlank(systemId)){
			paras.add("");
		}else{
			paras.add(systemId);
		}
		String hql = "";
		if(StringUtils.isBlank(userEname)){
			hql = "select ubu from UBaseUserDO ubu where NOT EXISTS (select 1 from BaseUserEmailDO b where b.userId=ubu.userId and b.bankId=? and b.systemId=? )";
		}else{
			hql = "select ubu from UBaseUserDO ubu where NOT EXISTS (select 1 from BaseUserEmailDO b where b.userId=ubu.userId and b.bankId=? and b.systemId=? ) and ubu.userEname like ?";
			paras.add("%" + userEname + "%");
		}
		return this.find(hql, paras, paginationList);
	}

	/*
	 * 查询未设置邮箱的用户信息
	 */
	public List userInInstAll(String userEname, String bankId, String systemId){
		List paras = new ArrayList();
		if(StringUtils.isBlank(bankId)){
			paras.add("");
		}else{
			paras.add(bankId);
		}
		if(StringUtils.isBlank(systemId)){
			paras.add("");
		}else{
			paras.add(systemId);
		}
		String hql = "";
		if(StringUtils.isBlank(userEname)){
			hql = "select ubu from UBaseUserDO ubu where NOT EXISTS (select 1 from BaseUserEmailDO b where b.userId=ubu.userId and b.bankId=? and b.systemId=? )";
		}else{
			hql = "select ubu from UBaseUserDO ubu where NOT EXISTS (select 1 from BaseUserEmailDO b where b.userId=ubu.userId and b.bankId=? and b.systemId=? ) and ubu.userEname like '%"
					+ userEname + "%'";
		}
		return this.find(hql, paras);
	}

	/**
	 * <p> 方法名称: getAllInsts|描述: 返回所有机构数据 </p>
	 * @return 获取所有机构列表
	 */
	public List getAllInsts(){
		StringBuffer sb = new StringBuffer();
		// sb.append("SELECT bInst FROM UBaseInstDO bInst order by
		// bInst.orderNum,bInst.instId asc");
		sb
				.append("SELECT bInst FROM UBaseInstDO bInst order by bInst.instLayer,bInst.parentInstId desc,bInst.instId");
		List insts = find(sb.toString());
		return insts;
	}

	/**
	 * <p> 方法名称: getRootInsts|描述: 返回所有根机构数据 </p>
	 * @param user 当前用户,控制分级授权
	 * @return 获取所有根机构列表
	 */
	public List getRootInsts(LoginDO user){
		StringBuffer sb = new StringBuffer();
		List insts = null;
		sb.append(" SELECT bInst FROM UBaseInstDO bInst where 1=1 ");
		// 控制分级授权
		sb
				.append(" and exists (select 1 from UBaseInstDO a where a.instId=? and (a.instId=bInst.instId or (a.isHead='true' and bInst.parentInstId is null )))");
		sb
				.append(" order by bInst.instLayer,bInst.orderNum,bInst.parentInstId desc,bInst.instId");
		if(user != null){
			insts = find(sb.toString(), user.getInstId());
		}else{
			insts = find(sb.toString());
		}
		return insts;
	}

	public List getRootInsts(String instId){
		StringBuffer sb = new StringBuffer();
		List insts = null;
		sb
				.append(" SELECT bInst FROM UBaseInstDO bInst where bInst.instId like ? ");
		sb
				.append(" order by bInst.instLayer,bInst.orderNum,bInst.parentInstId desc,bInst.instId");
		insts = find(sb.toString(), "%" + instId + "%");
		return insts;
	}

	public void deleteUserInInst(List l){
		String userId = "";
		// 拼接选中的用户编号
		for(Iterator i = l.iterator(); i.hasNext();){
			UBaseUserDO ubu = (UBaseUserDO) i.next();
			userId += "'" + ubu.getUserId() + "',";
		}
		// 删除用户, 先删除与用户相关的数据(用户历史密码表、系统管理员管理员表、角色用户表、系统日志表、用户表、权限主体表)
		if(!StringUtils.isEmpty(userId)){
			userId = userId.substring(0, userId.length() - 1);
			// 清除用户历史密码表
			String sqlDelete = "delete from UBaseHisUserPwdDO hup where hup.userId in ("
					+ userId + ")";
			this.executeUpdate(sqlDelete);
			// 清除系统管理员管理员表
			sqlDelete = "delete from UAuthSystemAdminDO sad where sad.userId in ("
					+ userId + ")";
			this.executeUpdate(sqlDelete);
			// 角色用户表
			sqlDelete = "delete from UAuthRoleUserDO rud where rud.userId in ("
					+ userId + ")";
			this.executeUpdate(sqlDelete);
			// 系统日志
			sqlDelete = "delete from UBaseSysLogDO sld where sld.userId in ("
					+ userId + ")";
			this.executeUpdate(sqlDelete);
			// 用户表
			sqlDelete = "delete from UBaseUserDO ud where ud.userId in ("
					+ userId + ")";
			this.executeUpdate(sqlDelete);
			// 权限主体表
			sqlDelete = "delete from UAuthObjectDO od where od.objectId in ("
					+ userId + ")";
			this.executeUpdate(sqlDelete);
		}
	}

	public void deleteUserInInst(String instId) throws InstantiationException,
			IllegalAccessException{
		String userId = "";
		// 拼接选中的用户编号
		String sql = "select * from U_BASE_USER where inst_id = ?";
		List list = null;
		UBaseUserDO ubu = null;
		list = jdbcDaoAccessor.find(sql, new Object[] {instId});
		if(CollectionUtils.isNotEmpty(list)){
			Map map = (Map) list.get(0);
			ubu = (UBaseUserDO) BeanUtil.reflectToFillValue(UBaseUserDO.class,
					map, InstBaseAuditBase.baseAddAttributeFields,
					InstBaseAuditBase.baseAddColumnFields);
			// o.setInstName(map.get("inst_name") != null ?
			// map.get("inst_name").toString() : "");
			userId += "'" + ubu.getUserId() + "',";
		}
		//			
		// 删除用户, 先删除与用户相关的数据(用户历史密码表、系统管理员管理员表、角色用户表、系统日志表、用户表、权限主体表)
		if(!StringUtils.isEmpty(userId)){
			userId = userId.substring(0, userId.length() - 1);
			// 删除角色所包含的此机构
			String rolehql = "delete from UAuthRoleResourceDO res where res.resDetailValue='"
					+ userId.toString() + "' and res.resId = '35'";
			this.executeUpdate(rolehql);
			// 清除用户历史密码表
			String sqlDelete = "delete from UBaseHisUserPwdDO hup where hup.userId in ("
					+ userId + ")";
			this.executeUpdate(sqlDelete);
			// 清除系统管理员管理员表
			sqlDelete = "delete from UAuthSystemAdminDO sad where sad.userId in ("
					+ userId + ")";
			this.executeUpdate(sqlDelete);
			// 角色用户表
			sqlDelete = "delete from UAuthRoleUserDO rud where rud.userId in ("
					+ userId + ")";
			this.executeUpdate(sqlDelete);
			// 系统日志
			sqlDelete = "delete from UBaseSysLogDO sld where sld.userId in ("
					+ userId + ")";
			this.executeUpdate(sqlDelete);
			// 用户表
			sqlDelete = "delete from UBaseUserDO ud where ud.userId in ("
					+ userId + ")";
			this.executeUpdate(sqlDelete);
			// 权限主体表
			sqlDelete = "delete from UAuthObjectDO od where od.objectId in ("
					+ userId + ")";
			this.executeUpdate(sqlDelete);
		}
	}

	/**
	 * <p> 方法名称: getInstsByCondition|描述:通过查询条件取得机构列表 </p>
	 * @param user 用户对象
	 * @param inst 机构对象
	 * @return 形成的查询语句
	 */
	public String getInstsByCondition(UBaseInstDO inst, boolean isFixQuery){
		// 获取的字段
		StringBuffer sb = new StringBuffer();
		sb.append("select bInst from UBaseInstDO bInst ");
		sb.append("where 1 = 1 ");
		// 用户机构号条件判断
		if(StringUtils.isNotEmpty(inst.getInstId())){
			if(isFixQuery)
				sb.append(" and bInst.instId = '" + inst.getInstId() + "' ");
			else
				sb.append(" and bInst.instId like '%" + inst.getInstId()
						+ "%' ");
		}
		// 用户机构名称条件判断
		if(StringUtils.isNotEmpty(inst.getInstSmpName())){
			sb.append(" and bInst.instSmpName like '%" + inst.getInstSmpName()
					+ "%' ");
		}
		sb.append(" order by bInst.orderNum,bInst.instId");
		return sb.toString();
	}

	/**
	 * <p> 方法名称: getAllInstByParentInst|描述:返回此机构的所属下级机构 </p>
	 * @param parentId 上级机构编号
	 * @return 机构列表
	 */
	public List getAllInstByParentInst(String parentId){
		List list = find(
				"SELECT bInst FROM UBaseInstDO bInst "
						+ "WHERE bInst.parentInstId =? order by bInst.instLayer,bInst.orderNum,bInst.instId asc",
				parentId);
		return CollectionUtils.isNotEmpty(list) ? list : Collections.EMPTY_LIST;
	}

	/**
	 * <p> 方法名称: getInstByInstId|描述:根据机构ID，返回当前机构信息 </p>
	 * @param instId 机构编号
	 * @return 机构信息
	 */
	public Object getInstByInstId(String instId){
		if(instId == null){
			return null;
		}
		List insts = find("SELECT bInst FROM UBaseInstDO bInst "
				+ "WHERE bInst.instId =?", instId);
		if(insts != null && insts.size() != 0){
			return insts.get(0);
		}
		return null;
	}

	public Object getInstcByInstId(String instId)
			throws InstantiationException, IllegalAccessException{
		if(instId == null){
			return null;
		}
		String sql = "select * from " + InstBaseAuditBase.IBAK_TABLE
				+ " where INST_Id=? ";
		List list = null;
		list = jdbcDaoAccessor.find(sql, new Object[] {instId});
		if(CollectionUtils.isNotEmpty(list)){
			Map map = (Map) list.get(0);
			UBaseInstChangeDO o = (UBaseInstChangeDO) BeanUtil
					.reflectToFillValue(UBaseInstChangeDO.class, map,
							InstBaseAuditBase.fullBaseAddAttributeFields,
							InstBaseAuditBase.fullBaseAddColumnFields);
			o.setInstName(map.get("inst_name") != null ? map.get("inst_name")
					.toString() : "");
			o.setInstLayer(map.get("INST_LAYER") != null ? Integer.valueOf(map
					.get("INST_LAYER").toString()) : new Integer(0));
			o.setInstLevel(map.get("INST_LEVEL") != null ? Integer.valueOf(map
					.get("INST_LEVEL").toString()) : new Integer(0));
			o.setOrderNum(map.get("ORDER_NUM") != null ? Integer.valueOf(map
					.get("ORDER_NUM").toString()) : new Integer(0));
			return o;
		}
		return null;
	}

	public Object getSysParmaByParamId(String paramId)
			throws InstantiationException, IllegalAccessException{
		if(paramId == null){
			return null;
		}
		String sql = "select * from " + SysParamAuditBase.SPBAK_TABLE
				+ " where PARAM_Id=? ";
		List list = null;
		list = jdbcDaoAccessor.find(sql, new Object[] {paramId});
		if(CollectionUtils.isNotEmpty(list)){
			Map map = (Map) list.get(0);
			UBaseSysParamChangeDO o = (UBaseSysParamChangeDO) BeanUtil
					.reflectToFillValue(UBaseSysParamChangeDO.class, map,
							SysParamAuditBase.sysFullAttributeFields,
							SysParamAuditBase.sysFullColumnFields);
			o.setParamId(Integer.valueOf(map.get("PARAM_ID") != null ? map.get(
					"PARAM_ID").toString() : ""));
			return o;
		}
		return null;
	}

	public String loadInstAndUsrXmlEx(String instId, LoginDO user, String reInit){
		return loadInstAndUsrXmlEx(instId, 2, user, reInit);
	}

	/**
	 * <p> 方法名称: loadInstAndUsrXmlEx|描述:异步获取部门列表 </p>
	 * @param instId 机构编号
	 * @param next 显示几级
	 * @return 机构列表生成的XML文件
	 */
	public String loadInstAndUsrXmlEx(String instId, int level, LoginDO user,
			String reInit){
		if("Y".equals(reInit)){// 当有参数reInit,根据页面输入框[机构ID],重新加载机构树
			return this.initTree(instId, level, user, reInit); // 第一次加载时，获取上级机构的清单-显示两层
		}else{
			if(StringUtils.isNotBlank(instId)){
				// 异步加载该机构下包含的子机构
				return LoadSubInstTree(instId, true);
			}else{
				return this.initTree(instId, level, user, reInit); // 第一次加载时，获取上级机构的清单-显示两层
			}
		}
	}

	private String initTree(String instId, int level, LoginDO user,
			String reInit){
		StringBuffer sb = new StringBuffer();
		UBaseInstDO inst = new UBaseInstDO();
		// 第一次加载时，获取上级机构的清单-显示两层
		sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<Response><Data><Tree>");
		List listInst = null;
		if("Y".equals(reInit) && StringUtils.isNotEmpty(instId)){
			listInst = this.getRootInsts(instId);
		}else{
			listInst = this.getRootInsts(user);
		}
		// 新增一个list，存放子机构的机构id(唯一)
		List instInstTemp1 = new ArrayList();
		for(int i = 0; i < listInst.size(); i++){
			inst = (UBaseInstDO) listInst.get(i);
			// 避免在创建树的同时二次将子机构放进去
			if(instId == null || instId.equals("")){
				if(instInstTemp1.contains(inst.getInstId())){
					continue;
				}
			}
			sb.append("<TreeNode name='");
			sb.append(inst.getInstSmpName()).append("[").append(
					inst.getInstId()).append("]");
			sb.append("' id='");
			sb.append(inst.getInstId());
			sb.append("' levelType='1' ");
			// FIXME: 此地方判断是否有子结点，需要优化
			List listInstTmp = this.getAllInstByParentInst(inst.getInstId());
			if(listInstTmp != null && listInstTmp.size() > 0){
				sb.append(" _hasChild='1' ");
				sb.append(" _opened='true' ");
				for(int j = 0; j < listInstTmp.size(); j++){
					UBaseInstDO uinst = (UBaseInstDO) listInstTmp.get(j);
					// 存放子机构的机构id(唯一)
					instInstTemp1.add(uinst.getInstId());
				}
			}
			sb.append(" _canSelect='1' ");
			sb.append("> ");
			if(listInstTmp != null && listInstTmp.size() > 0){
				if(level == 2){
					if(instId == null || instId.equals("")){
						sb.append(LoadSubInstTree(inst.getInstId(), false));
					}
				}
			}
			sb.append("</TreeNode>");
		}
		sb.append("</Tree></Data></Response>");
		return sb.toString();
	}

	public String loadInstAndUsrXmlEx2(String instId, LoginDO user){
		return loadInstAndUsrXmlEx2(instId, 2, user);
	}

	public String loadInstAndUsrXmlEx2(String instId, int level, LoginDO user){
		StringBuffer sb = new StringBuffer();
		UBaseInstDO inst = new UBaseInstDO();
		if(StringUtils.isNotBlank(instId)){
			// 异步加载该机构下包含的子机构
			return LoadSubInstTree(instId, true);
		}else{
			// 第一次加载时，获取上级机构的清单-显示两层
			sb.append("<?xml version='1.0' encoding='UTF-8'?>");
			sb.append("<Response><Data><Tree>");
			inst = (UBaseInstDO) this.getInstByInstId(instId);
			if(null != inst){
				sb.append("<TreeNode name='");
				sb.append(inst.getInstSmpName()).append("[").append(
						inst.getInstId()).append("]");
				sb.append("' id='");
				sb.append(inst.getInstId());
				sb.append("' levelType='1' ");
				// FIXME: 此地方判断是否有子结点，需要优化
				List listInstTmp = this
						.getAllInstByParentInst(inst.getInstId());
				if(listInstTmp != null && listInstTmp.size() > 0){
					sb.append(" _hasChild='1' ");
					sb.append(" _opened='true' ");
				}
				sb.append(" _canSelect='1' ");
				sb.append("> ");
				if(listInstTmp != null && listInstTmp.size() > 0){
					if(level == 2){
						sb.append(LoadSubInstTree(inst.getInstId(), false));
					}
				}
				sb.append("</TreeNode>");
			}
			sb.append("<TreeNode name='");
			sb.append(inst.getInstSmpName()).append("[").append(
					inst.getInstId()).append("]");
			sb.append("' id='");
			sb.append(inst.getInstId());
			sb.append("' levelType='1' ");
			List listInstTmp = this.getAllInstByParentInst(inst.getInstId());
			if(listInstTmp != null && listInstTmp.size() > 0){
				sb.append(" _hasChild='1' ");
				sb.append(" _opened='true' ");
			}
			sb.append(" _canSelect='1' ");
			sb.append("> ");
			if(listInstTmp != null && listInstTmp.size() > 0){
				if(level == 2)
					sb.append(LoadSubInstTree(inst.getInstId(), false));
			}
			sb.append("</TreeNode>");
			sb.append("</Tree></Data></Response>");
		}
		return sb.toString();
	}

	/**
	 * <p> 方法名称: LoadSubInstTree|描述:异步获取子机构列表 </p>
	 * @param instId 机构编号
	 * @param isHaveRoot 是否包含根节点
	 * @return 机构列表生成的XML文件
	 */
	private String LoadSubInstTree(String instId, boolean isHaveRoot){
		StringBuffer sb = new StringBuffer();
		UBaseInstDO inst = new UBaseInstDO();
		if(StringUtils.isNotBlank(instId)){
			// 异步加载该机构下包含的子机构
			List listInst = this.getAllInstByParentInst(instId);
			if(listInst != null){
				// 如果是异步获取需要增加<data>节点，否则无法加载
				if(isHaveRoot)
					sb.append("<data>");
				// 新增一个list，存放子机构的机构id(唯一)
				List instInstTemp1 = new ArrayList();
				for(int i = 0; i < listInst.size(); i++){
					inst = (UBaseInstDO) listInst.get(i);
					// 避免在创建树的同时二次将子机构放进去
					if(instInstTemp1.contains(inst.getInstId())){
						continue;
					}
					sb.append("<TreeNode name='");
					sb.append(inst.getInstSmpName()).append("[").append(
							inst.getInstId()).append("]");
					sb.append("' id='");
					sb.append(inst.getInstId());
					sb.append("' levelType='2' ");
					// FIXME: 此地方判断是否有子结点，需要优化
					List listInstTmp = this.getAllInstByParentInst(inst
							.getInstId());
					if(listInstTmp != null && listInstTmp.size() > 0){
						sb.append(" _hasChild='1' ");
						sb.append(" _opened='false' ");
						for(int j = 0; j < listInstTmp.size(); j++){
							UBaseInstDO uinst = (UBaseInstDO) listInstTmp
									.get(j);
							// 存放子机构的机构id(唯一)
							instInstTemp1.add(uinst.getInstId());
						}
					}
					sb.append(" _canSelect='1' ");
					sb.append("> ");
					sb.append("</TreeNode>");
				}
				if(isHaveRoot)
					sb.append("</data>");
			}
		}
		return sb.toString();
	}

	/**
	 * <p> 方法名称: getInstsByFilterFormWithPaging|描述:分页返回机构列表 </p>
	 * @param instId 机构编码
	 * @param instName 机构名称
	 * @param pageSize 页面大小
	 * @param pageNum 页码
	 * @return 返回机构列表（带分页信息）
	 */
	public PageBox getInstsByFilterFormWithPaging(String instId,
			String instName, final int pageSize, final int pageNum){
		DetachedCriteria _instDc = DetachedCriteria.forClass(UBaseInstDO.class);
		if(instId != null && instId.trim().length() > 0){
			_instDc.add(Property.forName("instId").like("%" + instId + "%"));
		}
		;
		if(instName != null && instName.trim().length() > 0){
			_instDc
					.add(Property.forName("instName")
							.like("%" + instName + "%"));
		}
		;
		_instDc.addOrder(Order.asc("instId"));
		return getByFormWithPaging(_instDc, pageSize, pageNum);
	}

	/**
	 * <p> 方法名称: getInstsCountByFilterCriteria|描述:统计满足条件的机构个数 </p>
	 * @param instId 机构编码
	 * @param instName 机构名称
	 * @return 机构个数
	 */
	public int getInstsCountByFilterCriteria(String instId, String instName){
		DetachedCriteria _instDc = DetachedCriteria.forClass(UBaseInstDO.class);
		if(instId != null && instId.trim().length() > 0){
			_instDc.add(Property.forName("instId").like("%" + instId + "%"));
		}
		;
		if(instName != null && instName.trim().length() > 0){
			_instDc
					.add(Property.forName("instName")
							.like("%" + instName + "%"));
		}
		;
		_instDc.addOrder(Order.asc("instId"));
		return getCountByCriteria(_instDc);
	};

	/**
	 * <p> 方法名称: buildData|描述: 构造树模型 </p>
	 * @param instId 机构编码
	 * @param instName 机构名称
	 * @param pageSize 页面大小
	 * @param pageNum 页码
	 * @return 返回构造完成的机构树模型及数据
	 */
	public List buildData(String instId, String instName, int pageSize,
			int pageNum){
		List instTree = new LinkedList();
		PageBox pb = getInstsByFilterFormWithPaging(instId, instName, pageSize,
				pageNum);
		List insts = pb.getPageList();
		for(int i = 0; i < insts.size(); i++){
			UBaseInstDO instModel = (UBaseInstDO) insts.get(i);
			InstGridTreeNode instNode = new InstGridTreeNode();
			instNode.set_parent(instModel.getParentInstId());
			instNode.set_id(instModel.getInstId());
			if(isLeaf(instModel.getInstId(), insts)){
				instNode.set_is_leaf(true);
			}else{
				instNode.set_is_leaf(false);
			}
			;
			instNode.setInstId(instModel.getInstId());
			instNode.setInstName(instModel.getInstName());
			instNode.setParentInstId(instModel.getParentInstId());
			instNode.setInstLayer(instModel.getInstLayer());
			instNode.setIsBussiness(instModel.getIsBussiness());
			instNode.setStartDate(formatDate(instModel.getStartDate()));
			instNode.setEndDate(formatDate(instModel.getEndDate()));
			instNode.setEnabled(instModel.getEnabled());
			instTree.add(instNode);
		}
		return instTree;
	}

	/**
	 * <p> 方法名称: getParentsByInstId|描述:根据机构ID，返回其所有上级机构 </p>
	 * @param instId 机构编码
	 * @return 返回所有上级机构数据
	 */
	public List getParentsByInstId(String instId){
		List insts;
		if(instId == null){
			insts = find("SELECT new Map(ubi.instId as instId,ubi.instName as instName,ubi.instSmpName as instSmpName)"
					+ " FROM UBaseInstDO ubi");
		}else{
			insts = find(
					"SELECT new Map(ubi.instId as instId,ubi.instName as instName,ubi.instSmpName as instSmpName)"
							+ " FROM UBaseInstDO ubi Where ubi.instId=?",
					new Object[] {instId});
		}
		return insts;
	}

	/**
	 * <p> 方法名称: isLeaf|描述: 判断是否叶子结点 </p>
	 * @param instId 机构编码
	 * @param insts 机构列表
	 * @return 是否叶子结点
	 */
	private boolean isLeaf(String instId, List insts){
		for(int i = 0; i < insts.size(); i++){
			UBaseInstDO instModel = (UBaseInstDO) insts.get(i);
			if(instId.equals(instModel.getParentInstId())
					&& instModel.getParentInstId() != null){
				return false;
			}
		}
		return true;
	}

	/**
	 * <p> 方法名称: toUbaseInst|描述:对象封装，将字符串转换成对象 </p>
	 * @param instInfo 机构的JSON数据
	 * @return 返回机构对象UBaseInstDO
	 */
	public UBaseInstDO toUbaseInst(String instInfo){
		UBaseInstDO instDo = new UBaseInstDO();
		// json串到json对象
		JSONObject jsonObject = JSONObject.fromObject(instInfo);
		instDo.setInstId(jsonObject.getString("instIds"));
		instDo.setInstName(jsonObject.getString("instName"));
		instDo.setInstSmpName(jsonObject.getString("instSmpName"));
		instDo.setParentInstId(jsonObject.getString("parentInstId"));
		instDo.setInstLayer(toInt(jsonObject.getString("instLayer")));
		instDo.setAddress(jsonObject.getString("address"));
		instDo.setZip(jsonObject.getString("zip"));
		instDo.setTel(jsonObject.getString("tel"));
		instDo.setFax(jsonObject.getString("fax"));
		instDo.setIsBussiness(jsonObject.getString("isBussiness"));
		instDo.setOrderNum(toInt(jsonObject.getString("orderNum")));
		instDo.setDescription(jsonObject.getString("description"));
		instDo.setStartDate(toDate(jsonObject.getString("startDateStr")));
		instDo.setEndDate(toDate(jsonObject.getString("endDateStr")));
		instDo.setCreateTime(toDate(jsonObject.getString("createTimeStr")));
		instDo.setEnabled(jsonObject.getString("enabled"));
		return instDo;
	}

	/**
	 * <p> 方法名称: toDate|描述:字符转换成日期类型数据 </p>
	 * @param 日期字符串
	 * @return 日期型数据
	 */
	private Date toDate(String str){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = null;
		try{
			date = df.parse(str);
		}catch (ParseException e){
			return null;
		}
		return new java.sql.Date(date.getTime());
	}

	/**
	 * <p> 方法名称: toInt|描述:字符转换成整型类型 </p>
	 * @param 整型字符串
	 * @return 返回一个整型数据
	 */
	private Integer toInt(String str){
		if(str.length() == 0)
			return null;
		return Integer.valueOf(str);
	}

	/**
	 * <p> 方法名称: formatDate|描述:格式化日期类型数据 </p>
	 * @param date 日期类型数据
	 * @return 返回yyyy-MM-dd格式的日期数据
	 */
	private String formatDate(Date date){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		return date == null ? "" : df.format(date);
	}

	/**
	 * <p> 方法名称: updateInstCName|描述: 修改用机构名称时，关联更新有机构名称冗余字段的表 </p>
	 * @param user
	 * @return
	 */
	public void updateInstCName(UBaseInstDO inst){
		try{
			// 系统日志
			this.executeUpdate("update UBaseSysLogDO set  instCname =  '"
					+ inst.getInstName() + "' where instId = '"
					+ inst.getInstId() + "'");
			// 权限资源
			this
					.executeUpdate("update UAuthRoleResourceDO set  resDetailName =  '"
							+ inst.getInstName()
							+ "' where resId = '35'  and  resDetailValue = '"
							+ inst.getInstId() + "'");
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public boolean isLeaf(String instId){
		
		List list = find("SELECT new Map(bInst.instId) FROM UBaseInstDO bInst "
				+ "WHERE bInst.parentInstId =? ", instId);
		return CollectionUtils.isEmpty(list) ? true : false;
	}

	/**
	 * 递归取下级机构
	 * @param instId
	 * @param list
	 */
	public void getAllChildInst(String instId, List list){
		if(list == null){
			list = new ArrayList();
		}
		if(StringUtils.isEmpty(instId))
			return;
		if(!isLeaf(instId)){
			List childs = this.getAllInstByParentInst(instId);
			for(Iterator iterator = childs.iterator(); iterator.hasNext();){
				UBaseInstDO child = (UBaseInstDO) iterator.next();
				list.add(child);
				getAllChildInst(child.getInstId(), list);
			}
		}
		return;
	}

	public void deleteAllChildInst(UBaseInstDO inst){
		List alls = new ArrayList();
		getAllChildInst(inst.getInstId(), alls);
		for(Iterator iterator = alls.iterator(); iterator.hasNext();){
			UBaseInstDO o = (UBaseInstDO) iterator.next();
			log.info("删除下级机构[" + o.getInstId() + "][" + o.getInstName() + "]");
			delete(o);
		}
		delete(inst);
		return;
	}

	public boolean hasChildInst(List list, StringBuffer sb){
		boolean isHasChildInst = false;
		for(Iterator iterator = list.iterator(); iterator.hasNext();){
			UBaseInstDO inst = (UBaseInstDO) iterator.next();
			if(!isLeaf(inst.getInstId())){
				sb.append("机构[" + inst.getInstId() + "]存在下级机构,");
				isHasChildInst = true;
			}
		}
		sb.append("不能直接删除");
		return isHasChildInst;
	}

	public void deleteAllInst(List list){
		for(Iterator iterator = list.iterator(); iterator.hasNext();){
			UBaseInstDO inst = (UBaseInstDO) iterator.next();
			// deleteAllChildInst(inst);
			delete(inst);
			// UBaseInstRelaDO rela = new UBaseInstRelaDO();
			// rela.setInstId(inst.getInstId());
			// delete(rela);
		}
		// txInstRela();
	}

	public void deleteInstByInstId(String instId){
		String sql = "select count(*) from  u_base_inst  where inst_id =?";
		int count = jdbcDaoAccessor.findForInt(sql, new Object[] {instId});
		if(count > 0){
			UBaseInstDO inst = (UBaseInstDO) getInstByInstId(instId);
			delete(inst);
		}
	}

	public void deleteInstcByInstId(String instId){
		String sql = "select count(*) from  u_base_inst_change  where inst_id =?";
		int count = jdbcDaoAccessor.findForInt(sql, new Object[] {instId});
		if(count > 0){
			String sql1 = "delete from " + InstBaseAuditBase.IBAK_TABLE
					+ " where inst_id=?";
			jdbcDaoAccessor.update(sql1, new Object[] {instId});
		}
	}

	private final String triggerSQL = "select\n" + "         u1.inst_id,\n"
			+ "         u1.parent_inst_id as inst_id_level_1,\n"
			+ "         u2.parent_inst_id as inst_id_level_2,\n"
			+ "         u3.parent_inst_id as inst_id_level_3,\n"
			+ "         u4.parent_inst_id as inst_id_level_4,\n"
			+ "         u5.parent_inst_id as inst_id_level_5,\n"
			+ "         u6.parent_inst_id as inst_id_level_6\n"
			+ "  from u_base_inst u1\n" + "  left join u_base_inst u2\n"
			+ "  on u1.parent_inst_id=u2.inst_id\n"
			+ "  left join u_base_inst u3\n"
			+ "  on u2.parent_inst_id=u3.inst_id\n"
			+ "  left join u_base_inst u4\n"
			+ "  on u3.parent_inst_id=u4.inst_id\n"
			+ "  left join u_base_inst u5\n"
			+ "  on u4.parent_inst_id=u5.inst_id\n"
			+ "  left join u_base_inst u6\n"
			+ "  on u5.parent_inst_id=u6.inst_id";
	private final String triggerSql1_oracle = "select root as inst_id,"
			+ "min(decode(lev,20,'\\'||inst_id,''))||"
			+ "min(decode(lev,19,'\\'||inst_id,''))||"
			+ "min(decode(lev,18,'\\'||inst_id,''))||"
			+ "min(decode(lev,17,'\\'||inst_id,''))||"
			+ "min(decode(lev,16,'\\'||inst_id,''))||"
			+ "min(decode(lev,15,'\\'||inst_id,''))||"
			+ "min(decode(lev,14,'\\'||inst_id,''))||"
			+ "min(decode(lev,13,'\\'||inst_id,''))||"
			+ "min(decode(lev,12,'\\'||inst_id,''))||"
			+ "min(decode(lev,11,'\\'||inst_id,''))||"
			+ "min(decode(lev,10,'\\'||inst_id,''))||"
			+ "min(decode(lev,9,'\\'||inst_id,'')) ||"
			+ "min(decode(lev,8,'\\'||inst_id,'')) ||"
			+ "min(decode(lev,7,'\\'||inst_id,'')) ||"
			+ "min(decode(lev,6,'\\'||inst_id,'')) ||"
			+ "min(decode(lev,5,'\\'||inst_id,'')) ||"
			+ "min(decode(lev,4,'\\'||inst_id,'')) ||"
			+ "min(decode(lev,3,'\\'||inst_id,'')) ||"
			+ "min(decode(lev,2,'\\'||inst_id,'')) ||"
			+ "min(decode(lev,1,'\\'||inst_id,'')) || '\\' as inst_path,"
			+ "max(lev) as inst_level "
			+ "from ("
			+ "select CONNECT_BY_ROOT(inst_id) as root,level as lev,t.inst_id,t.parent_inst_id,t.enabled "
			+ "from (select inst_id,parent_inst_id,enabled from u_base_inst t ) t "
			+ "connect by NOCYCLE  prior parent_inst_id=inst_id "
			+ ")group by root ";

	// private final String triggerSql1_DB2_1="with temptab
	// (root,inst_id,parent_inst_id,enabled,lev) as ( "+
	// "SELECT root.inst_id,root.inst_id, root.parent_inst_id,enabled,1 "+
	// "FROM u_base_inst root "+
	// "UNION ALL "+
	// "SELECT temptab.root,sub.inst_id,
	// sub.parent_inst_id,sub.enabled,temptab.lev+1 "+
	// "FROM u_base_inst sub, temptab temptab "+
	// "WHERE sub.inst_id = temptab.parent_inst_id "+
	// ") ";
	// private final String triggerSql1_DB2_2="select root as inst_id, "+
	// "max(case lev when 20 then '\\' || inst_id else '' end) || "+
	// "max(case lev when 19 then '\\' || inst_id else '' end) || "+
	// "max(case lev when 18 then '\\' || inst_id else '' end) || "+
	// "max(case lev when 17 then '\\' || inst_id else '' end) || "+
	// "max(case lev when 16 then '\\' || inst_id else '' end) || "+
	// "max(case lev when 15 then '\\' || inst_id else '' end) || "+
	// "max(case lev when 14 then '\\' || inst_id else '' end) || "+
	// "max(case lev when 13 then '\\' || inst_id else '' end) || "+
	// "max(case lev when 12 then '\\' || inst_id else '' end) || "+
	// "max(case lev when 11 then '\\' || inst_id else '' end) || "+
	// "max(case lev when 10 then '\\' || inst_id else '' end) || "+
	// "max(case lev when 9 then '\\' || inst_id else '' end) || "+
	// "max(case lev when 8 then '\\' || inst_id else '' end) || "+
	// "max(case lev when 7 then '\\' || inst_id else '' end) || "+
	// "max(case lev when 6 then '\\' || inst_id else '' end) || "+
	// "max(case lev when 5 then '\\' || inst_id else '' end) || "+
	// "max(case lev when 4 then '\\' || inst_id else '' end) || "+
	// "max(case lev when 3 then '\\' || inst_id else '' end) || "+
	// "max(case lev when 2 then '\\' || inst_id else '' end) || "+
	// "max(case lev when 1 then '\\' || inst_id else '' end) || '\\' as
	// inst_path, "+
	// "max(lev) as inst_level "+
	// "from temptab "+
	// "group by root ";
	private static List reverse(List l){
		Collections.reverse(l);
		return l;
	}

	// 保存机构
	public void saveInst(UBaseInstDO inst){
		if(getInstByInstId(inst.getInstId()) != null){
			log.warn("已经存在该机构号：" + inst.getInstId());
			throw new InstLevelException("已经存在该机构号：" + inst.getInstId());
		}
		if(StringUtils.isEmpty(inst.getParentInstId())){
			inst.setParentInstId(null);
		}else{
			if(getInstByInstId(inst.getParentInstId()) == null){
				log.warn("不存在该机构号：" + inst.getParentInstId());
				throw new InstLevelException("不存在该机构号："
						+ inst.getParentInstId());
			}
		}
		save(inst);
		// UBaseInstRelaDO rela = buildOneInstRela(inst);
		// if (rela != null)
		// save(rela);
		updateInstRela1(inst);
	}

	// 保存机构change
	public void saveInst(UBaseInstChangeDO o){
		if(getInstByInstId(o.getInstId()) != null){
			log.warn("已经存在该机构号：" + o.getInstId());
			throw new InstLevelException("已经存在该机构号：" + o.getInstId());
		}
		IdGenerator idGenerator = IdGenerator.getInstance(AuditBase.AUDIT);
		long id = idGenerator.getNextKey();
		o.setId(new Long(id));
		o.setChangeStatus(AuditBase.CHANGE_TYPE_INST_ADD);
		o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_NOADUITED));
		o.getInstAuditEntity().setValueFields(
				InstBaseAuditBase.fullBaseAddAttributeFields);
		o.getInstAuditEntity().setColumnFields(
				InstBaseAuditBase.fullBaseAddColumnFields);
		o.getInstAuditEntity().save(o);
	}

	// 保存删除机构change
	public void saveDeleteInst(UBaseInstChangeDO o, List list, String userId)
			throws InstantiationException, IllegalAccessException{
		for(Iterator iterator = list.iterator(); iterator.hasNext();){
			UBaseInstDO inst = (UBaseInstDO) iterator.next();
			String sql = "select * from " + InstBaseAuditBase.IMAIN_TABLE
					+ " where inst_id=? ";
			List list2 = jdbcDaoAccessor.find(sql, new Object[] {inst
					.getInstId()});
			if(CollectionUtils.isNotEmpty(list2)){
				Map map = (Map) list2.get(0);
				UBaseInstDO inst2 = (UBaseInstDO) BeanUtil.reflectToFillValue(
						UBaseInstDO.class, map,
						InstBaseAuditBase.baseAddAttributeFields,
						InstBaseAuditBase.baseAddColumnFields);
				copyInst(o, inst2);
				IdGenerator idGenerator = IdGenerator
						.getInstance(AuditBase.AUDIT);
				long id = idGenerator.getNextKey();
				o.setId(new Long(id));
				o.setChangeUser(userId);
				o.setChangeTime(new java.sql.Timestamp(System
						.currentTimeMillis()));
				o.setChangeStatus(AuditBase.CHANGE_TYPE_INST_DELETE);
				o.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_NOADUITED));
				o.getInstAuditEntity().setValueFields(
						InstBaseAuditBase.fullBaseAddAttributeFields);
				o.getInstAuditEntity().setColumnFields(
						InstBaseAuditBase.fullBaseAddColumnFields);
				o.getInstAuditEntity().save(o);
			}
		}
	}

	// 复制机构change
	public void copyInst(UBaseInstChangeDO instc, UBaseInstDO inst){
		if(inst != null){
			instc.setAddress(inst.getAddress());
			instc.setCreateTime(inst.getCreateTime());
			instc.setCreateTimeStr(inst.getCreateTimeStr());
			instc.setDescription(inst.getDescription());
			instc.setEmail(inst.getEmail());
			instc.setEnabled(inst.getEnabled());
			instc.setEndDate(inst.getEndDate());
			instc.setEndDateStr(inst.getEndDateStr());
			instc.setFax(inst.getFax());
			instc.setInstId(inst.getInstId());
			instc.setInstLayer(inst.getInstLayer());
			instc.setInstName(inst.getInstName());
			instc.setInstRegion(inst.getInstRegion());
			instc.setInstSmpName(inst.getInstSmpName());
			instc.setIsBussiness(inst.getIsBussiness());
			instc.setIsHead(inst.getIsHead());
			instc.setOrderNum(inst.getOrderNum());
			instc.setParentInstId(inst.getParentInstId());
			instc.setInstPath(inst.getInstPath());
			instc.setInstLevel(inst.getInstLevel());
			instc.setStartDate(inst.getStartDate());
			instc.setStartDateStr(inst.getStartDateStr());
			instc.setTel(inst.getTel());
			instc.setZip(inst.getZip());
			instc.setAccount(inst.getAccount());
			instc.setTaxaddress(inst.getTaxaddress());
			instc.setTaxtel(inst.getTaxtel());
			instc.setTaxbank(inst.getTaxbank());
			instc.setTaxpername(inst.getTaxpername());
			instc.setTaxpernumber(inst.getTaxpernumber());
			instc.setTaxPayerType(inst.getTaxPayerType());
		}
	}

	// 判断是否修改过
	public boolean modifiedInst(String instId){
		String sql = "select count(*) from " + InstBaseAuditBase.IBAK_TABLE
				+ " where inst_id=? and audit_status=1";
		int count = jdbcDaoAccessor.findForInt(sql, new Object[] {instId});
		if(count > 0)
			return true;
		return false;
	}

	// 生成单个InstRela对象
	private UBaseInstRelaDO buildOneInstRela(UBaseInstDO inst){
		UBaseInstRelaDO rela = new UBaseInstRelaDO();
		rela.setInstId(inst.getInstId());
		String s = getParentInstPath(inst);
		String[] insts = s.split(",");
		if(insts != null && insts.length > 0 && insts.length <= 6){
			for(int i = 0; i < insts.length; i++){
				String value = insts[i];
				try{
					BeanUtils.setProperty(rela, "instIdLevel" + (i + 1), value);
				}catch (IllegalAccessException e){
					log.error("error occur when set property " + "instIdLevel"
							+ (i + 1) + " to UBaseInstRelaDO");
				}catch (InvocationTargetException e){
					log.error("error occur when set property " + "instIdLevel"
							+ (i + 1) + " to UBaseInstRelaDO");
				}
			}
		}
		if(insts != null && insts.length > 6){
			log.warn("机构层级超过6层，无法添加机构关系");
			return null;
		}
		// 即使没有上级机构也需要添加关系
		return rela;
	}

	// 获取inst机构数路径from bottom to top
	public String getParentInstPath(UBaseInstDO inst){
		List parents = new ArrayList();
		parents.add(inst.getInstId());
		UBaseInstDO parentInst = getParentInst(inst);
		StringBuffer sb = new StringBuffer();
		while(parentInst != null){
			if(parents.contains(parentInst.getInstId())){
				log.warn("机构上下级存在循环引用");
				sb = new StringBuffer();
				break;
			}
			sb.append(parentInst.getInstId() + ",");
			parents.add(parentInst.getInstId());
			parentInst = getParentInst(parentInst);
		}
		return sb.toString();
	}

	// 获取inst机构数路径from bottom to top
	public boolean getParentInstPath(UBaseInstDO inst, List parents){
		boolean loopReference = false;
		parents = parents == null ? parents = new ArrayList() : parents;
		parents.add(inst.getInstId());
		UBaseInstDO parentInst = getParentInst(inst);
		while(parentInst != null){
			if(parents.contains(parentInst.getInstId())){
				loopReference = true;
				parents.add(parentInst.getInstId());// add the last one too
				log.warn("机构上下级存在循环引用: "
						+ StringUtils.join(parents.iterator(), "-"));
				break;
			}
			parents.add(parentInst.getInstId());
			parentInst = getParentInst(parentInst);
		}
		return loopReference;
	}

	public UBaseInstDO getParentInst(UBaseInstDO inst){
		List list = find("from UBaseInstDO where instId=? ", new Object[] {inst
				.getParentInstId()});
		return CollectionUtils.isNotEmpty(list) ? (UBaseInstDO) list.get(0)
				: null;
	}

	// 保存机构
	public void updateInst(UBaseInstDO inst) throws Exception{
		List l = new ArrayList();
		if(StringUtils.isNotEmpty(inst.getParentInstId())
				&& getParentInstPath(inst, l)){
			log.warn("警告:机构层级存在异常："
					+ StringUtils.join(reverse(l).iterator(), "-"));
			throw new InstLevelException("机构上下级设置存在异常："
					+ StringUtils.join(reverse(l).iterator(), "-"));
		}
		UBaseInstDO old = (UBaseInstDO) getInstByInstId(inst.getInstId());
		PropertyUtils.copyProperties(old, inst);
		if(StringUtils.isEmpty(old.getParentInstId())){
			old.setParentInstId(null);
		}
		this.update(old);
		updateInstRela1(old);
		// if (old == null)
		// return;
		// if (old.getParentInstId() != null
		// && !old.getParentInstId().equals(inst.getParentInstId())) {
		// //上级机构被修改
		// UBaseInstRelaDO rela = buildOneInstRela(inst);
		// update(rela);
		// txInstRela();
		// }
		// if(old.getParentInstId()==null&&inst.getParentInstId()!=null){
		// UBaseInstRelaDO rela = buildOneInstRela(inst);
		// txInstRela();
		// }
	}

	// 保存机构
	public void updateInstc(UBaseInstChangeDO instc) throws Exception{
		List l = new ArrayList();
		if(StringUtils.isNotEmpty(instc.getParentInstId())
				&& getParentInstPath(instc, l)){
			log.warn("警告:机构层级存在异常："
					+ StringUtils.join(reverse(l).iterator(), "-"));
			throw new InstLevelException("机构上下级设置存在异常："
					+ StringUtils.join(reverse(l).iterator(), "-"));
		}
		IdGenerator idGenerator = IdGenerator.getInstance(AuditBase.AUDIT);
		long id = idGenerator.getNextKey();
		instc.setId(new Long(id));
		instc.setChangeStatus(AuditBase.CHANGE_TYPE_INST_MODIFY);
		instc.setAuditStatus(new Long(AuditBase.AUDIT_STATUS_NOADUITED));
		instc.getInstAuditEntity().setValueFields(
				InstBaseAuditBase.fullBaseAddAttributeFields);
		instc.getInstAuditEntity().setColumnFields(
				InstBaseAuditBase.fullBaseAddColumnFields);
		instc.getInstAuditEntity().save(instc);
		// if (old == null)
		// return;
		// if (old.getParentInstId() != null
		// && !old.getParentInstId().equals(inst.getParentInstId())) {
		// //上级机构被修改
		// UBaseInstRelaDO rela = buildOneInstRela(inst);
		// update(rela);
		// txInstRela();
		// }
		// if(old.getParentInstId()==null&&inst.getParentInstId()!=null){
		// UBaseInstRelaDO rela = buildOneInstRela(inst);
		// txInstRela();
		// }
	}

	// 重新机构设置路径
	public void updateInstRela1(UBaseInstDO inst){
		if(StringUtils.isNotEmpty(inst.getParentInstId())){
			UBaseInstDO parentInst = (UBaseInstDO) this.get(UBaseInstDO.class,
					inst.getParentInstId());
			if(parentInst != null){
				executeUpdate("update UBaseInstDO t set t.instPath='"
						+ parentInst.getInstPath()
						+ "'||t.instId||'\\',instLevel="
						+ (parentInst.getInstLevel().intValue() + 1)
						+ " where t.instId='" + inst.getInstId() + "'");
			}else{
				executeUpdate("update UBaseInstDO t set instPath='\\'||instId||'\\',instLevel=1 where instId='"
						+ inst.getInstId() + "'");
			}
			executeUpdate("update UBaseInstDO t set t.instPath='"
					+ parentInst.getInstPath() + "'||t.instId||'\\',instLevel="
					+ (parentInst.getInstLevel().intValue() + 1)
					+ " where t.instId='" + inst.getInstId() + "'");
		}else{
			executeUpdate("update UBaseInstDO t set instPath='\\'||instId||'\\',instLevel=1 where instId='"
					+ inst.getInstId() + "'");
		}
		List list = this.find("from UBaseInstDO where parentInstId=?", inst
				.getInstId());
		for(int i = 0, j = list.size(); i < j; i++){
			UBaseInstDO o = (UBaseInstDO) list.get(i);
			updateInstRela1(o);
		}
	}

	public void setInstPath(UBaseInstDO inst){
	}

	// 保存机构
	public void txInstRela(){
		try{
			executeUpdate("delete from UBaseInstRelaDO");
			List list = jdbcDaoAccessor.find(triggerSQL);
			for(Iterator iterator = list.iterator(); iterator.hasNext();){
				Map row = (Map) iterator.next();
				String instId = (String) row.get("inst_id");
				String instIdLevel1 = (String) row.get("inst_id_level_1");
				String instIdLevel2 = (String) row.get("inst_id_level_2");
				String instIdLevel3 = (String) row.get("inst_id_level_3");
				String instIdLevel4 = (String) row.get("inst_id_level_4");
				String instIdLevel5 = (String) row.get("inst_id_level_5");
				String instIdLevel6 = (String) row.get("inst_id_level_6");
				UBaseInstRelaDO o = new UBaseInstRelaDO();
				o.setInstId(instId);
				o.setInstIdLevel1(instIdLevel1);
				o.setInstIdLevel2(instIdLevel2);
				o.setInstIdLevel3(instIdLevel3);
				o.setInstIdLevel4(instIdLevel4);
				o.setInstIdLevel5(instIdLevel5);
				o.setInstIdLevel6(instIdLevel6);
				save(o);
			}
		}catch (Exception e){
			log.error(e);
		}
	}

	/**
	 * <p>方法名称: getSystemRela|描述:模块集合</p>
	 * @return 返回模块集合
	 */
	public List getSystemRela(){
		List insts = find("SELECT distinct vcrms FROM VcrmsSystemRelaDisDO vcrms");
		return insts;
	}

	/**
	 * <p>方法名称: getSystemRelaBySystemId|描述:模块集合</p>
	 * @return 返回模块集合
	 */
	public List getSystemRelaBySystemId(String systemId){
		List insts = find(
				"SELECT vcrms FROM VcrmsSystemRelaDO vcrms where vcrms.systemId=?",
				systemId);
		return insts;
	}

	/**
	 * <p>方法名称: getSystemRelaBySystemId|描述:模块集合</p>
	 * @return 返回模块集合
	 */
	public VcrmsSystemRelaNewDO getDistinctSystemRelaBySystemId(String systemId){
		List vCrms = find(
				"SELECT distinct vcrms FROM VcrmsSystemRelaNewDO vcrms where vcrms.systemId=?",
				systemId);
		VcrmsSystemRelaNewDO vCrmsTe = new VcrmsSystemRelaNewDO();
		if(null != vCrms && vCrms.size() != 0){
			vCrmsTe = (VcrmsSystemRelaNewDO) vCrms.get(0);
		}
		return vCrmsTe;
	}

	/**
	 * <p>方法名称: getSystemRela|描述:模块集合</p>
	 * @return 返回模块集合
	 */
	public List getEmailAddrs(String systemId){
		List instEmailAddList = find(
				"SELECT ua FROM VBaseUserEmailDO ua where ua.systemId=? and ua.emailAddr is not null order by ua.userEname",
				systemId);
		return instEmailAddList;
	}

	public List getSelectedEmailAddrs(String systemId, String bankId){
		List paras = new ArrayList();
		paras.add(bankId);
		paras.add(systemId);
		List instEmailAddList = find(
				"SELECT bu FROM BaseUserEmailDO bu where  bu.bankId=? and bu.systemId=?",
				paras);
		return instEmailAddList;
	}

	public List getSelectedEmailAddrsByBankId(String userId, String bankId,
			String systemId, PaginationList paginationList){
		List instEmailAddList = null;
		List paras = new ArrayList();
		if(StringUtils.isBlank(bankId)){
			paras.add("");
		}else{
			paras.add(bankId);
		}
		if(StringUtils.isBlank(systemId)){
			paras.add("");
		}else{
			paras.add(systemId);
		}
		String hql = "";
		if(StringUtils.isBlank(userId)){
			hql = "SELECT bu from BaseUserEmailDO bu where  bu.bankId=?  and bu.systemId=? ";
		}else{
			paras.add(userId);
			hql = "SELECT bu from BaseUserEmailDO bu where  bu.bankId=?  and bu.systemId=? and bu.userId=? ";
		}
		instEmailAddList = find(hql, paras, paginationList);
		return instEmailAddList;
	}

	public List getSelectedEmailAddrsByBankIdAll(String userId, String bankId,
			String systemId){
		List instEmailAddList = null;
		List paras = new ArrayList();
		if(StringUtils.isBlank(bankId)){
			paras.add("");
		}else{
			paras.add(bankId);
		}
		if(StringUtils.isBlank(systemId)){
			paras.add("");
		}else{
			paras.add(systemId);
		}
		String hql = "";
		if(StringUtils.isBlank(userId)){
			hql = "SELECT bu from BaseUserEmailDO bu where  bu.bankId=?  and bu.systemId=? ";
		}else{
			hql = "SELECT bu from BaseUserEmailDO bu where  bu.bankId=?  and bu.systemId=? and bu.userId=? ";
		}
		instEmailAddList = find(hql, paras);
		return instEmailAddList;
	}

	public List getSelectedEmailAddrsByBankIdAndUserEname(String userEname,
			String bankId, String systemId, PaginationList paginationList){
		List instEmailAddList = null;
		List paras = new ArrayList();
		if(StringUtils.isBlank(bankId)){
			paras.add("");
		}else{
			paras.add(bankId);
		}
		if(StringUtils.isBlank(systemId)){
			paras.add("");
		}else{
			paras.add(systemId);
		}
		String hql = "";
		if(StringUtils.isBlank(userEname)){
			hql = "SELECT bu from BaseUserEmailDO bu where  bu.bankId=?  and bu.systemId=? ";
		}else{
			hql = "SELECT bu from BaseUserEmailDO bu where  bu.bankId=?  and bu.systemId=?  and  EXISTS (select 1 from UBaseUserDO  b where b.userId=bu.userId and b.userEname like ?)";
			paras.add("%" + userEname + "%");
		}
		instEmailAddList = find(hql, paras, paginationList);
		return instEmailAddList;
	}

	public List getSelectedEmailAddrsByBankIdAndUserEnameAll(String userEname,
			String bankId, String systemId){
		List instEmailAddList = null;
		List paras = new ArrayList();
		if(StringUtils.isBlank(bankId)){
			paras.add("");
		}else{
			paras.add(bankId);
		}
		if(StringUtils.isBlank(systemId)){
			paras.add("");
		}else{
			paras.add(systemId);
		}
		String hql = "";
		if(StringUtils.isBlank(userEname)){
			hql = "SELECT bu from BaseUserEmailDO bu where  bu.bankId=?  and bu.systemId=? ";
		}else{
			hql = "SELECT bu from BaseUserEmailDO bu where  bu.bankId=?  and bu.systemId=?  and  EXISTS (select 1 from UBaseUserDO  b where b.userId=bu.userId and b.userEname like ?)";
			paras.add("%" + userEname + "%");
		}
		instEmailAddList = find(hql, paras);
		return instEmailAddList;
	}

	public List getSelectedBaseUserEmail(String userId, String bankId,
			String systemId){
		List paras = new ArrayList();
		paras.add(userId);
		paras.add(bankId);
		paras.add(systemId);
		List instEmailAddList = find(
				"SELECT bu FROM BaseUserEmailDO bu where bu.userId=? and bu.bankId=? and bu.systemId=?",
				paras);
		return instEmailAddList;
	}

	public List getSelectedBaseUserEmailAll(String bankId, String systemId){
		List paras = new ArrayList();
		paras.add(bankId);
		paras.add(systemId);
		List instEmailAddList = find(
				"SELECT bu FROM BaseUserEmailDO bu where  bu.bankId=? and bu.systemId=?",
				paras);
		return instEmailAddList;
	}

	public void saveBaseUserEmail(BaseUserEmailDO baseDO){
		this.save(baseDO);
	}

	public void moveInst(String ids, String dest){
		// TODO Auto-generated method stub+
		this.executeUpdate("update UBaseInstDO set parentInstId='" + dest + "'"
				+ "where instId in ('"
				+ ids.substring(0, ids.length() - 1).replaceAll(",", "\',\'")
				+ "')");
		String args[] = ids.split(",");
		for(int i = 0, j = args.length; i < j; i++){
			if(!args[i].trim().equals("")){
				updateInstRela1((UBaseInstDO) this.get(UBaseInstDO.class,
						args[i]));
			}
		}
	}

	/**
	 * <p>方法名称: checkMoveInst|描述:校验机构是否可以移动</p>
	 * @return
	 * @return 返回不能移动的机构
	 */
	public List checkMoveInst(String ids, String dest){
		// TODO Auto-generated method stub+
		List list = this
				.find("from UBaseInstDO t where instId in ('"
						+ ids.substring(0, ids.length() - 1).replaceAll(",",
								"\',\'")
						+ "') and exists (select 1 from UBaseInstDO t1 where t1.instId = '"
						+ dest
						+ "' and substring(t1.instPath,1,length(t.instPath))=t.instPath)");
		return list;
	}

	/**
	 * <p>方法名称: initInst|描述:初始化机构路径</p>
	 * @return
	 * @return
	 */
	public void initInst(){
		// TODO Auto-generated method stub+
		List list = this
				.find("from UBaseInstDO where instPath is null or instPath=''");
		for(int i = 0; i < list.size(); i++){
			UBaseInstDO u = (UBaseInstDO) list.get(i);
			refreshInst(u);
		}
	}

	public Object[] refreshInst(UBaseInstDO u){
		Object orgs[] = null;
		if(StringUtils.isEmpty(u.getInstPath())){
			if(StringUtils.isEmpty(u.getParentInstId())){
				orgs = new Object[] {"\\", Integer.valueOf("0")};
			}else{
				log.info("refreshInst!!!" + u.getParentInstId());
				UBaseInstDO pu = (UBaseInstDO) this.get(UBaseInstDO.class, u
						.getParentInstId());
				if(pu == null){
					orgs = new Object[] {"\\", Integer.valueOf("0")};
				}else if(StringUtils.isEmpty(pu.getInstPath())){
					orgs = refreshInst(pu);
				}else{
					orgs = new Object[] {pu.getInstPath(), pu.getInstLevel()};
				}
			}
			u.setInstPath(String.valueOf(orgs[0]) + u.getInstId() + "\\");
			u.setInstLevel(new Integer(Integer.valueOf(orgs[1].toString())
					.intValue() + 1));
			this.update(u);
		}
		return new Object[] {u.getInstPath(), u.getInstLevel()};
	}
	/**
	 * 兼容IE11的机构树创建
	 * @param instId
	 * @param user
	 * @param reInit
	 * @return
	 */
	public List loadInstZTreeEx(){
		return this.initZTree();
	}
	/**
	 * 兼容IE 11的机构树控件数据源创建
	 * @param instId
	 * @param level
	 * @param user
	 * @param reInit
	 * @return
	 */
	private List initZTree(){
		List resList = new ArrayList();
		List listInst = this.getAllInsts();
		if(null == listInst){
			return null;
		}
		for (int i=0;i<listInst.size();i++){
			UBaseInstDO inst = (UBaseInstDO) listInst.get(i);
			Map itemMap = new HashMap();
			itemMap.put("id", inst.getInstId());
			itemMap.put("name", inst.getInstName());
			if(null == inst.getParentInstId() || "".equals(inst.getParentInstId() )){
				itemMap.put("pId", "0");
			} else {
				itemMap.put("pId", inst.getParentInstId());
			}
			resList.add(itemMap);
		}
		return resList;
	}
	
	/**
	 * @title 根据纳税人识别码查询UBaseInstDO
	 * @description TODO
	 * @author dev4
	 * @param taxPayerType
	 * @return
	 */
	public Object getUBaseInstDOBytaxPernumberAndTaxPayerType(String taxPernumber,String taxPayerType){
		String hql="select ubd from UBaseInstDO ubd where ubd.taxpernumber="+"'"+taxPernumber+"'"
				+" and taxPayerType="+"'"+taxPayerType+"'";
		List inst=find(hql);
		if(inst.size()==0){
			return null;
		}else{
			return inst.get(0);
		}
		
	}
	
	public String getInstTaxPayerTypeByInstId(String instId){
		if(instId == null){
			return null;
		}
		List insts = find("SELECT bInst.taxPayerType FROM UBaseInstDO bInst "
				+ "WHERE bInst.instId =?", instId);
		if(insts != null && insts.size() != 0){
			return insts.get(0).toString();
		}
		return null;
	}
}
