package fmss.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import fmss.action.base.AuditBase;
import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UAuthResMapDO;
import fmss.dao.entity.UAuthRoleDO;
import fmss.dao.entity.UAuthRoleResourceDO;
import fmss.dao.entity.UAuthRoleUserDO;
import fmss.dao.entity.UBaseConfigDO;
import fmss.dao.entity.UBaseInstDO;
import fmss.dao.entity.UBaseUserDO;
import fmss.dao.ResourceConfigDao;
import fmss.common.util.Constants;
import fmss.common.util.PaginationList;

/**
 * <p>
 * 版权所有:(C)2003-2010 
 * </p>
 * 
 * @作者: sunzhan
 * @日期: 2009-6-27 下午02:53:27
 * @描述: [ResourceConfigService]子系统资源配置定义服务类
 */
public class ResourceConfigService extends CommonService {

	/** authRoleService 角色服务 */
	private AuthRoleService authRoleService;
	/** resourceConfigDao 读取子系统资源dao */
	private ResourceConfigDao resourceConfigDao;
	/** dicService 字典服务 */
	private DictionaryService dicService;

	/**
	 * @return 字典服务对象
	 */
	public DictionaryService getDicService() {
		return this.dicService;
	}

	/**
	 * @param dicService
	 *            要设置的 字典服务对象
	 */
	public void setDicService(DictionaryService dicService) {
		this.dicService = dicService;
	}

	/**
	 * @return 角色服务
	 */
	public AuthRoleService getAuthRoleService() {
		return this.authRoleService;
	}

	/**
	 * @param authRoleService
	 *            要设置的 角色服务
	 */
	public void setAuthRoleService(AuthRoleService authRoleService) {
		this.authRoleService = authRoleService;
	}

	/**
	 * @return 子系统资源dao
	 */
	public ResourceConfigDao getResourceConfigDao() {
		return this.resourceConfigDao;
	}

	/**
	 * @param resourceConfigDao
	 *            要设置的 子系统资源dao
	 */
	public void setResourceConfigDao(ResourceConfigDao resourceConfigDao) {
		this.resourceConfigDao = resourceConfigDao;
	}

	/**
	 * <p>
	 * 方法名称: queryAuthResMap|描述:
	 * </p>
	 * 
	 * @param uAuthResMap
	 *            资源对象查询条件
	 * @return 资源对象列表
	 * @throws Exception
	 */
	public List queryAuthResMap(UAuthResMapDO uAuthResMap) throws Exception {
		// 查询hql
		StringBuffer sql = new StringBuffer(
				"select new Map(c.systemCname as systemCname,m.systemId as systemId,");
		sql
				.append(
						"m.resId as resId,m.resName as resName,m.srcTable as srcTable,m.srcKeyField as srcKeyField,")
				.append(
						"m.srcIdField as srcIdField,m.srcNameField as srcNameField,ubd.dicName as resType) ")
				.append("from UAuthResMapDO m left join m.ubaseConfig c ")
				.append(
						",UBaseDictionaryDO ubd  where m.resType=ubd.dicValue and ubd.dicType='RT' ");
		Map map = new HashMap();
		// 组合条件查询，如果带有查询参数则将查询条件和对应的值放入map中
		if (StringUtils.isNotEmpty(uAuthResMap.getSystemId())) {
			String condition = " and m.systemId=?";
			map.put(condition, uAuthResMap.getSystemId());
		}
		if (StringUtils.isNotEmpty(uAuthResMap.getResName())) {
			String condition = " and m.resName like ?";
			map.put(condition, "%" + uAuthResMap.getResName() + "%");
		}
		if (StringUtils.isNotEmpty(uAuthResMap.getResType())) {
			String condition = " and m.resType = ?";
			map.put(condition, uAuthResMap.getResType());
		}
		List valueList = new ArrayList();
		Set keySet = map.keySet();
		// 迭代map，将查询条件连接在原有hql之后,每个条件对应的值放入list
		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			sql.append(key);
			valueList.add(map.get(key));
		}
		sql.append(" order by m.systemId,m.resType");
		// 根据查询条件和对应的条件数据查询结果
		return this.find(sql.toString(), valueList.toArray(new Object[] {}));
	}

	/**
	 * <p>
	 * 方法名称: saveResMap|描述:保存资源对象
	 * </p>
	 * 
	 * @param uarm
	 *            资源信息对象
	 */
	public void saveResMap(UAuthResMapDO uarm) {
		this.save(uarm);
	}

	/**
	 * <p>
	 * 方法名称: deletes|描述:删除资源对象
	 * </p>
	 * 
	 * @param resIDs
	 *            资源id数组
	 */
	public void deletes(String[] resIDs) {
		// 删除资源对照表的关系
		if (null != resIDs && resIDs.length > 0) {
			for (int index = 0; index < resIDs.length; index++) {
				String hsql = "delete from UAuthRoleResourceDO urr where urr.resId="
						+ resIDs[index];
				this.executeUpdate(hsql);
			}
		}
		// 删除资源
		if (null != resIDs && resIDs.length > 0) {
			List delList = new ArrayList();
			for (int index = 0; index < resIDs.length; index++) {
				UAuthResMapDO arm = new UAuthResMapDO();
				arm.setResId(resIDs[index]);
				delList.add(arm);
			}
			this.deleteAll(delList);
		}
	}

	/**
	 * <p>
	 * 方法名称: getsubSystemList|描述:获得子系统列表
	 * </p>
	 * 
	 * @return 子系统列表
	 */
	public List getSubSystemList(boolean isAll) {
		return this.authRoleService.getSystemList(isAll);
	}

	/**
	 * <p>
	 * 方法名称: get|描述:根据resId获取resMap对象
	 * </p>
	 * 
	 * @param resMap
	 *            资源对象
	 * @return 资源对象详细信息
	 */
	public UAuthResMapDO get(UAuthResMapDO resMap) {
		return this.authRoleService.getAuthResMap(resMap.getResId());
	}

	/**
	 * <p>
	 * 方法名称: updateResMap|描述:更新resMap
	 * </p>
	 * 
	 * @param object
	 *            资源对象
	 */
	public void updateResMap(UAuthResMapDO object) {
		this.update(object);
	}

	/**
	 * <p>
	 * 方法名称: getResContent|描述:获取子系统的私有资源内容
	 * </p>
	 * 
	 * @param authResMap
	 *            资源对象
	 * @param cfg
	 *            子系统配置
	 * @return 子系统的资源内容
	 * @throws Exception
	 */
	public List getResContent(UAuthResMapDO authResMap, UBaseConfigDO cfg)
			throws Exception {
		return this.resourceConfigDao.getResource(authResMap, cfg.getDbUrl());
	}

	/**
	 * <p>
	 * 方法名称: getHavResByRole|描述: 根据角色获取该角色已有的资源，为不带条件的查询
	 * </p>
	 * 
	 * @param role
	 *            角色对象
	 * @param baseConfig
	 *            子系统信息对象
	 * @param page
	 *            分页对象
	 * @return 角色已有的资源列表
	 */
	public List getHavResByRole(UAuthRoleDO role, UBaseConfigDO baseConfig,
			PaginationList page) {
		return this.getHavResByObjectId(role.getRoleId(), baseConfig
				.getSystemId(), page);
	}

	/**
	 * <p>
	 * 方法名称: getHavResByRole|描述:根据角色和资源条件获取该角色已有的资源,为带条件的查询
	 * </p>
	 * 
	 * @param role
	 *            角色对象
	 * @param authResMap
	 *            资源查询条件
	 * @param baseConfig
	 *            子系统对象
	 * @param page
	 *            分页对象
	 * @return 角色已有的资源
	 */
	public List getHavResByRole(UAuthRoleDO role, UAuthResMapDO authResMap,
			UBaseConfigDO baseConfig, PaginationList paginationList) {
		return this.getHavResByObjectId(role.getRoleId(), authResMap,
				baseConfig.getSystemId(), paginationList);
	}

	/**
	 * <p>
	 * 方法名称: getLevResByRole|描述:根据角色获取该角色没有的私有资源
	 * </p>
	 * 
	 * @param role
	 *            角色对象
	 * @param baseConfig
	 *            子系统对象
	 * @return 角色没有的资源
	 * @throws Exception
	 */
	public Map getLevResByRole(UAuthRoleDO role, UBaseConfigDO baseConfig)
			throws Exception {
		return this.getLevResByObjectId(role.getRoleId(), baseConfig
				.getSystemId());
	}

	/**
	 * <p>
	 * 方法名称: getHavResByObjectId|描述:根据根据权限主体ID获取该根据权限主体已有的资源
	 * </p>
	 * 
	 * @param objectId
	 *            权限主体id
	 * @return 权限主体已有的资源
	 */
	public List getHavResByObjectId(String objectId) {
		String query = " select new Map(uarr.resId as resId,uarm.resType as resType,"
				+ " ubd.dicName as resTypeName,ubc.systemCname as systemCname,"
				+ " uarr.resDetailValue as resDetailValue,"
				+ " uarr.resDetailName as resDetailName)"
				+ " from UAuthResMapDO uarm left join uarm.ubaseConfig ubc ,"
				+ " UBaseDictionaryDO ubd , UAuthRoleResourceDO uarr "
				+ " where uarm.resId=uarr.resId and uarm.resType=ubd.dicValue and ubd.dicType='RT' "
				+ " and uarr.objectId=?";
		return this.find(query, objectId);
	}

	/**
	 * <p>
	 * 方法名称: getHavResByObjectId|描述:根据根据权限主体ID获取该根据权限主体已有的资源，加入了系统管理公共角色的判断
	 * </p>
	 * 
	 * @param objectId
	 *            权限主体id
	 * @param objectId
	 *            systemId
	 * @param page
	 *            分页对象
	 * @return 权限主体已有的资源
	 */
	public List getHavResByObjectId(String objectId, String systemId,
			PaginationList page) {
		// 查询sql
		String query = " select new Map("
				+ " uarr.resDetailValue as resDetailValue,"
				+ " uarr.resDetailName as resDetailName,uarm.resName as resName,"
				+ " uarr.systemId as systemId,uarr.resId as resId,ubc.systemCname as systemCname)"
				+ " from UAuthResMapDO uarm, UAuthRoleResourceDO uarr left join uarr.ubaseConfig ubc"
				+ " where uarm.resId=uarr.resId ";
		List param = new ArrayList();
		param.add(objectId);
		query += " and uarr.objectId=? ";
		query += " order by uarr.resDetailValue";
		return this.find(query, param, page);
	}

	/**
	 * <p>
	 * 方法名称: getLevResByObjectId|描述: 根据根据权限主体ID和系统id获取该根据权限主体没有的资源
	 * </p>
	 * 
	 * @param objectId
	 *            主体id
	 * @param systemId
	 *            子系统id
	 * @return 主体没有的资源
	 * @throws Exception
	 */
	public Map getLevResByObjectId(String objectId, String systemId)
			throws Exception {
		Map resCtnMap = new HashMap();
		try {
			// 取现有已配置的子系统资源信息
			String query = "select uarm from UAuthResMapDO uarm where uarm.systemId=?";
			List authResMapList = this.find(query, systemId);
			// 获取子系统信息
			UBaseConfigDO uBaseConfig = (UBaseConfigDO) this.get(
					UBaseConfigDO.class, systemId);
			// 获取已有的资源
			query = " select new Map(uarr.resId as resId,uarm.resType as resType,"
					+ " ubd.dicName as resTypeName,"
					+ " uarr.resDetailValue as resDetailValue,"
					+ " uarr.resDetailName as resDetailName)"
					+ " from UAuthResMapDO uarm ,UBaseDictionaryDO ubd , UAuthRoleResourceDO uarr "
					+ " where uarm.resId=uarr.resId and uarm.resType=ubd.dicValue and ubd.dicType='RT' "
					+ " and uarm.systemId=? and uarr.objectId=?";
			List hasList = this
					.find(query, new Object[] { systemId, objectId });
			// 获取资源配置中没有配置的资源
			for (Iterator iterator = authResMapList.iterator(); iterator
					.hasNext();) {
				UAuthResMapDO authResMap = (UAuthResMapDO) iterator.next();
				// 注意：这里的key是资源类型,因为能确定子系统类型
				resCtnMap.put(authResMap.getResType(), this.resourceConfigDao
						.getLevResCtn(hasList, authResMap, uBaseConfig
								.getDbUrl(), uBaseConfig.getSystemId()));
			}
		} catch (Exception e) {
			throw e;
		}
		return resCtnMap;
	}

	/**
	 * <p>
	 * 方法名称: getHavResByObjectId|描述:根据权限主体ID和资源条件获取该权限主体已有的资源
	 * </p>
	 * 
	 * @param objectId
	 *            主体id
	 * @param authResMap
	 *            资源条件
	 * @param systemId
	 *            子系统id
	 * @param page
	 *            分页对象
	 * @return 主体已有的资源
	 */
	public List getHavResByObjectId(String objectId, UAuthResMapDO authResMap,

	String systemId, PaginationList paginationList) {

		// 查询hql modify by wangxin 20091116 解决查询后删除报错问题，原因为系统ID和资源ID未获取

		String query = " select new Map("

				+ " uarr.resDetailValue as resDetailValue,"

				+ " uarr.resDetailName as resDetailName,uarm.resName as resName,"

				+ " uarr.systemId as systemId,uarr.resId as resId,ubc.systemCname as systemCname)"

				+ " from UAuthResMapDO uarm, UAuthRoleResourceDO uarr left join uarr.ubaseConfig ubc"

				+ " where uarm.resId=uarr.resId ";

		// String query = " select new Map(uarm.resName as resName,"

		// + " uarr.resDetailValue as resDetailValue,"

		// + " uarr.resDetailName as resDetailName)"

		// + " from UAuthResMapDO uarm, UAuthRoleResourceDO uarr "

		// + " where uarm.resId=uarr.resId ";

		Map map = new HashMap();

		// 组合条件查询，如果带有查询参数则将查询条件和对应的值放入map中

		if (StringUtils.isNotEmpty(authResMap.getResName())) {

			String condition = " and uarr.resDetailName like ?";

			map.put(condition, "%" + authResMap.getResName() + "%");

		}

		if (StringUtils.isNotEmpty(authResMap.getResId())) {

			String condition = " and uarm.resId=?";

			map.put(condition, authResMap.getResId());

		}

		List valueList = new ArrayList();

		valueList.add(objectId);

		query += " and uarr.objectId=? ";

		Set keySet = map.keySet();

		// 迭代map，将查询条件连接在原有hql之后,每个条件对应的值放入list

		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {

			String key = (String) iterator.next();

			query += key;

			valueList.add(map.get(key));

		}

		query += " order by uarr.resDetailValue";

		// 根据查询条件和对应的条件数据查询结果

		return this.find(query, valueList, paginationList);

	}

	/**
	 * <p>
	 * 方法名称: getLevResKey|描述:获取没有配置的资源字段名
	 * </p>
	 * 
	 * @param baseConfig
	 *            子系统信息
	 * @return 没有配置的资源字段名
	 */
	public Map getLevResKey(UBaseConfigDO baseConfig) {
		// 取现有已配置的子系统资源信息
		Map resKey = new HashMap();
		String query = "select uarm from UAuthResMapDO uarm where uarm.systemId=?";
		List authResMapList = this.find(query, baseConfig.getSystemId());
		for (Iterator iterator = authResMapList.iterator(); iterator.hasNext();) {
			UAuthResMapDO resMap = (UAuthResMapDO) iterator.next();
			// 注意：这里的key是资源类型,因为能确定子系统类型
			resKey.put(resMap.getResType(), resMap);
		}
		return resKey;
	}

	/**
	 * <p>
	 * 方法名称: getLevResKey|描述:获取没有配置的资源字段名
	 * </p>
	 * 
	 * @return 没有配置的资源字段名
	 */
	public Map getLevResKey() {
		// 取现有已配置的子系统资源信息
		Map resKey = new HashMap();
		String query = "select uarm from UAuthResMapDO uarm";
		List authResMapList = this.find(query);
		for (Iterator iterator = authResMapList.iterator(); iterator.hasNext();) {
			// 注意：key是资源类型加子系统类型
			UAuthResMapDO resMap = (UAuthResMapDO) iterator.next();
			resKey.put(resMap.getResType() + SPERATOR + resMap.getSystemId(),
					resMap);
		}
		return resKey;
	}

	/**
	 * <p>
	 * 方法名称: saveConfigRes|描述:根据主体对象保存配置的资源
	 * </p>
	 * 
	 * @param string
	 *            资源字符串,值以;分割
	 * @param objectId
	 *            主体对象id
	 * @param authResMapDO
	 */
	public void saveConfigRes(LoginDO login, String string, String objectId) {
		if (StringUtils.isNotEmpty(string)) {
			String[] strArray = string.split(";");
			/*
			 * 分隔每行的值和值对应的名字,依次为 resID,resValue,resName,以&,分割
			 * 形成角色-资源对应关系，然后保存角色-资源关系表
			 */
			login.addDescription(",资源[");
			for (int i = 0; i < strArray.length; i++) {
				String value = strArray[i];
				String[] valueArray = value.split("\\" + SPERATOR);
				if (valueArray != null && valueArray.length >= 3) {
					String resId = valueArray[0];
					String resValue = valueArray[1];
					String resName = valueArray[2];
					String systemId = valueArray[3];
					UAuthRoleResourceDO urr = new UAuthRoleResourceDO();
					urr.setResDetailValue(resValue);
					urr.setResDetailName(resName);
					urr.setResId(resId);
					urr.setObjectId(objectId);
					urr.setSystemId(systemId);
					this.save(urr);
					login.addDescription(resValue).addDescription("-").addDescription(resName).addDescription(" ");
				}
			}
			login.addDescription("]");
		}
	}

	/**
	 * <p>
	 * 方法名称: deleteAllRes|描述:删除主体对应的多个资源
	 * </p>
	 * 
	 * @param resMapList
	 *            资源列表,列表以;隔开，依次为resId,resValue
	 * @param objectId
	 *            权限主体id
	 */
	public void deleteAllRes(LoginDO login, List resMapList, String objectId) {
		List list = new ArrayList();
		/*
		 * 形成角色-资源对应关系，然后删除角色-资源关系表
		 */
		login.addDescription(",资源[");
		for (Iterator iterator = resMapList.iterator(); iterator.hasNext();) {
			String value = iterator.next().toString();
			String[] valueArray = value.split(";");
			if (valueArray != null && valueArray.length >= 2) {
				String resId = valueArray[0];
				String resValue = valueArray[1];
				String systemId = valueArray[2];
				UAuthRoleResourceDO urr = new UAuthRoleResourceDO();
				urr.setObjectId(objectId);
				urr.setResId(resId);
				urr.setResDetailValue(resValue);
				urr.setSystemId(systemId);
				login.addDescription(resValue).addDescription("-").addDescription(valueArray[3]).addDescription(" ");
				list.add(urr);
			}
		}
		login.addDescription("]");
		this.deleteAll(list);
	}

	/**
	 * <p>
	 * 方法名称: getResTreeXml|描述:获取没有配置的资源列表树，简化取值逻辑, 只用从resmap取单一值即可
	 * </p>
	 * 
	 * @param role
	 *            角色对象
	 * @param baseConfig
	 *            子系统对象
	 * @param resMap
	 *            资源对象
	 * @return 资源列表树字符串
	 * @throws Exception
	 */
	/*
	 * public String getResTreeXml(UAuthRoleDO role, UBaseConfigDO baseConfig,
	 * UAuthResMapDO resMap) throws Exception{ StringBuffer sb = new
	 * StringBuffer(); try{ Map ctnMap = new HashMap(); resMap =
	 * this.get(resMap); // 获取资源配置中没有配置的资源,公有资源的特殊处理,菜单 if(Constants.MENU_RES_ID
	 * == Integer.parseInt(resMap.getResId())){ // 菜单的特殊展现 ctnMap =
	 * this.getLevPubMenuCtn(role.getRoleId(), baseConfig .getSystemId(),
	 * resMap); }else{ ctnMap = this.getLevRes(role.getRoleId(), baseConfig
	 * .getSystemId(), resMap); }
	 * sb.append("<?xml version='1.0' encoding='UTF-8'?>");
	 * sb.append("<tree id='0'>"); Set keySet = ctnMap.keySet(); for(Iterator
	 * iterator = keySet.iterator(); iterator.hasNext();){ String key = (String)
	 * iterator.next(); sb.append("<item text='"); sb.append(key);
	 * sb.append("' id='"); sb.append(key); sb.append("' child='1");
	 * sb.append("'>"); sb.append("<userdata name='levelType'>1</userdata>"); //
	 * 取各类资源下资源内容 List resList = (List) ctnMap.get(key); for(int j = 0; resList
	 * != null && j < resList.size(); j++){ Map row = (Map) resList.get(j);
	 * sb.append("<item text='"); sb.append(row.get(resMap.getSrcIdField()) +
	 * "-" + row.get(resMap.getSrcNameField()));
	 * 
	 * sb.append("' id='"); // 注意：此id加上子系统id限定,作为id的唯一约束
	 * sb.append(row.get("systemId") + ".");
	 * sb.append(row.get(resMap.getSrcKeyField())); sb.append("'>");
	 * sb.append("<userdata name='levelType'>2</userdata>");
	 * sb.append("<userdata name='value'>"); // 加入systemid
	 * sb.append(resMap.getResId() + SPERATOR + row.get(resMap.getSrcIdField())
	 * + SPERATOR + row.get(resMap.getSrcNameField()) + SPERATOR +
	 * row.get("systemId")); sb.append("</userdata>"); sb.append("</item>"); }
	 * sb.append("</item>"); } sb.append("</tree>"); }catch (Exception e){ throw
	 * e; } return sb.toString(); }
	 */
	/**
	 * <p>
	 * 方法名称: getResTreeXmlEx|描述:获取没有配置的资源列表树，简化取值逻辑, 只用从resmap取单一值即可
	 * </p>
	 * 
	 * @param role
	 *            角色对象
	 * @param baseConfig
	 *            子系统对象
	 * @param resMap
	 *            资源对象
	 * @return 资源列表树字符串
	 * @param defaultUrlInfo
	 *            默认IP地址信息
	 * @throws Exception
	 */
	public String getResTreeXmlEx(UAuthRoleDO role, UBaseConfigDO baseConfig,
			UAuthResMapDO resMap, Map defaultUrlInfo) throws Exception {
		StringBuffer sb = new StringBuffer();
		// 重新赋值
		UBaseConfigDO config = new UBaseConfigDO();
		BeanUtils.copyProperties(baseConfig, config);
		try {
			Map ctnMap = new HashMap();
			resMap = this.get(resMap);
			// 获取资源配置中没有配置的资源,公有资源的特殊处理,菜单
			if (Constants.MENU_RES_ID == Integer.parseInt(resMap.getResId())) {
				// 菜单的特殊展现
				ctnMap = this.getLevPubMenuCtn(role.getRoleId(), role
						.getSystemId(), resMap);
			} else {
				if (Constants.INST_RES_ID == Integer
						.parseInt(resMap.getResId())) {
					config.setSystemId(Constants.SYSTEM_COMMON_ID);
				}
				/*
				 * //add by fwy ？ if(44 == Integer.parseInt(resMap.getResId())){
				 * baseConfig.setSystemId("00006"); }
				 */

				if (49 == Integer.parseInt(resMap.getResId())) {
					config.setSystemId("00003");
				}
				ctnMap = this.getLevRes(role.getRoleId(), config.getSystemId(),
						resMap, defaultUrlInfo);
			}
			sb.append("<?xml version='1.0' encoding='UTF-8'?>");
			sb.append("<Response><Data><Tree>");
			Set keySet = ctnMap.keySet();
			for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				sb.append("<TreeNode name=\"");
				sb.append(key);
				sb.append("\" id=\"");
				sb.append(key.replaceAll("\'", ""));
				sb.append("\" levelType='1' ");
				//				
				// //sb.append("<userdata name='levelType'>1</userdata>");
				// // 取各类资源下资源内容
				List resList = (List) ctnMap.get(key);
				if (resList != null && resList.size() > 0) {
					sb.append(" _hasChild='1' ");
				}
				sb.append(" _canSelect='1' ");
				sb.append(">");
				for (int j = 0; resList != null && j < resList.size(); j++) {
					Map row = (Map) resList.get(j);
					sb.append("<TreeNode name='");
					sb.append(row.get(resMap.getSrcIdField()) + "-"
							+ row.get(resMap.getSrcNameField()));
					sb.append("' id='");
					// 注意：此id加上子系统id限定,作为id的唯一约束
					sb.append(row.get("systemId") + ".");
					sb.append(row.get(resMap.getSrcKeyField()));
					sb.append("' levelType='2'");
					sb.append(" _hasChild='0' ");
					sb.append(" _canSelect='1' ");
					sb.append(" value='");
					// 加入systemid
					sb.append(resMap.getResId() + SPERATOR
							+ row.get(resMap.getSrcIdField()) + SPERATOR
							+ row.get(resMap.getSrcNameField()) + SPERATOR
							+ role.getSystemId());
					sb.append("'>");
					sb.append("</TreeNode>");
				}
				sb.append("</TreeNode>");
			}
			sb.append("</Tree></Data></Response>");
		} catch (Exception e) {
			throw e;
		}
		return sb.toString();
	}

	/**
	 * <p>
	 * 方法名称: getResTreeXmlEx|描述:获取没有配置的资源列表树，简化取值逻辑, 只用从resmap取单一值即可
	 * </p>
	 * 
	 * @param role
	 *            角色对象
	 * @param baseConfig
	 *            子系统对象
	 * @param resMap
	 *            资源对象
	 * @return 资源列表树字符串
	 * @param defaultUrlInfo
	 *            默认IP地址信息
	 * @throws Exception
	 */
	public String getResTreeListEx(UAuthRoleDO role, UBaseConfigDO baseConfig, UAuthResMapDO resMap, Map defaultUrlInfo) throws Exception {
		StringBuffer sb = new StringBuffer();
		// 重新赋值
		UBaseConfigDO config = new UBaseConfigDO();
		BeanUtils.copyProperties(baseConfig, config);
		try {
			Map ctnMap = new HashMap();
			resMap = this.get(resMap);
			// 获取资源配置中没有配置的资源,公有资源的特殊处理,菜单
			if (Constants.MENU_RES_ID == Integer.parseInt(resMap.getResId())) {
				// 菜单的特殊展现
				ctnMap = this.getLevPubMenuCtn(role.getRoleId(), role.getSystemId(), resMap);
			} else {
				if (Constants.INST_RES_ID == Integer.parseInt(resMap.getResId())) {
					config.setSystemId(Constants.SYSTEM_COMMON_ID);
				}
				if (49 == Integer.parseInt(resMap.getResId())) {
					config.setSystemId("00003");
				}
				ctnMap = this.getLevRes(role.getRoleId(), config.getSystemId(),	resMap, defaultUrlInfo);
			}
			Set keySet = ctnMap.keySet();
			for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
				String key = (String) iterator.next();
				List resList = (List) ctnMap.get(key);
				for (int j = 0; resList != null && j < resList.size(); j++) {
					Map row = (Map) resList.get(j);
					//String id = row.get("systemId") + "." + row.get(resMap.getSrcKeyField());
					String levelType = "2";
					String _hasChild = "0";
					String _canSelect= "1";
					String id = row.get(resMap.getSrcIdField()).toString();
						   id = id.replaceAll("\\.", "");
					String value = 
						resMap.getResId() + SPERATOR + 
						row.get(resMap.getSrcIdField()) + SPERATOR + 
						row.get(resMap.getSrcNameField()) + SPERATOR + 
						role.getSystemId();
					String optionText = row.get(resMap.getSrcIdField()) + "-" + row.get(resMap.getSrcNameField());
					sb.append(id + "#" + value + "#" + optionText + ";");
					//System.out.println(id + "#" + value + "#" + option + ";");
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return sb.toString();
	}

	/**
	 * <p>
	 * 方法名称: getPubRes|描述:获取所有公有资源map
	 * </p>
	 * 
	 * @return
	 */
	public List getPubRes() {
		// 菜单，机构，固定的公有资源
		String query = " select new Map(md.itemcode as itemcode,md.itemname as itemname,"
				+ " md.systemId as systemId) from MenuDO md order by md.systemId,md.itemcode";
		return this.find(query);
	}

	/**
	 * <p>
	 * 方法名称: getResMap|描述: 获取所有资源map
	 * </p>
	 * 
	 * @param baseConfigDO
	 * @return
	 */
	public List getResMap(UBaseConfigDO baseConfigDO) {
		// 系统公共资源条件
		if (Constants.SYSTEM_COMMON_ID.equals(baseConfigDO.getSystemId())) {
			return this.find("from UAuthResMapDO");
		} else {
			String query = "from UAuthResMapDO where ((resType='PRI' and systemId=?) or resType='PUB')";
			return this.find(query, baseConfigDO.getSystemId());
		}
	}

	/**
	 * <p>
	 * 方法名称: getPubMenuCtn|描述:获取公共资源-菜单,以多选框样式显示
	 * </p>
	 * 
	 * @return 公共资源-菜单
	 */
	public List getPubMenuCtn() {
		List list = new ArrayList();
		Map menuMap = new HashMap();
		String query = "select distinct md.systemId from MenuDO md";
		List systemIds = this.find(query);
		for (Iterator iterator = systemIds.iterator(); iterator.hasNext();) {
			String systemId = (String) iterator.next();
			UBaseConfigDO baseConfig = (UBaseConfigDO) this.get(
					UBaseConfigDO.class, systemId);
			query = " select new Map(md.itemcode as itemcode,md.itemname as itemname,"
					+ " md.systemId as systemId) from MenuDO md "
					+ "where md.systemId=? order by md.systemId,md.itemcode";
			List menus = this.find(query, systemId);
			for (int i = 0; i < menus.size(); i++) {
				Map menu = (Map) menus.get(i);
				// 增加空白
				String itemCode = menu.get("itemcode").toString();
				String[] itemCodeArray = itemCode.split("\\.");
				String space = "";
				for (int j = 0; itemCodeArray != null
						&& j < itemCodeArray.length; j++) {
					space += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
				}
				menu.put("itemname", space + menu.get("itemname"));
			}
			// 加入key
			menuMap.put(baseConfig.getSystemEname(), menus);
		}
		list.add(menuMap);
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getPubInstCtn|描述:获取公共资源-机构,以多选框样式显示
	 * </p>
	 * 
	 * @return 公共资源-机构
	 */
	public List getPubInstCtn() {
		List list = new ArrayList();
		String query = "from UBaseInstDO order by instId,parentInstId,instLayer";
		List insts = this.find(query);
		for (Iterator iterator = insts.iterator(); iterator.hasNext();) {
			UBaseInstDO inst = (UBaseInstDO) iterator.next();
			Map instMap = new HashMap();
			instMap.put("instId", inst.getInstId());
			String space = "";
			// 添加空白
			for (int j = 0; inst.getInstLayer() != null
					&& j < inst.getInstLayer().intValue(); j++) {
				space += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
			}
			instMap.put("instName", space + inst.getInstId() + "-"
					+ inst.getInstName());
			list.add(instMap);
		}
		return list;
	}

	/**
	 * <p>
	 * 方法名称: getLevPubResCtn|描述:获取没有配置的公共资源
	 * </p>
	 * 
	 * @param resCtnMap
	 * @param objectId
	 * @param authResMap
	 * @return
	 * @throws Exception
	 */
	public Map getLevPubMenuCtn(String objectId, String systemId,
			UAuthResMapDO authResMap) throws Exception {
		Map resCtnMap = new HashMap();
		// 获取已有的资源
		// 获取子系统信息
		UBaseConfigDO uBaseConfig = (UBaseConfigDO) this.get(
				UBaseConfigDO.class, systemId);
		List hasList = null;
		String query = "";
		UAuthRoleDO role = this.authRoleService.getRoleByRoleId(objectId);
		if (!Constants.SYSTEM_COMMON_ID.equals(role.getSystemId())) {
			// 非系统管理角色,只能查看自己子系统的菜单
			query = " select new Map(uarr.resId as resId,"
					+ " uarr.resDetailValue as resDetailValue,"
					+ " uarr.resDetailName as resDetailName,md.systemId as systemId)"
					+ " from  UAuthRoleResourceDO uarr, MenuDO md"
					+ " where md.itemcode=uarr.resDetailValue"
					+ " and md.systemId=? and uarr.objectId=?";
			query = "select new Map(urdo.itemcode as menu_id,urdo.itemcode as menu_id,"
					+ " urdo.itemname as menu_name,"
					+ " urdo.systemId as systemId) from MenuDO urdo where "
					+ " urdo.itemcode not in ( select "
					+ " uarr.resDetailValue as resDetailValue"
					+ " from UAuthRoleResourceDO uarr, MenuDO md"
					+ " where md.itemcode=uarr.resDetailValue"
					+ " and uarr.objectId=? and md.systemId=uarr.systemId "
					+ " and md.systemId=? and uarr.resId="
					+ "'"+Constants.MENU_RES_ID+"'"
					+ " ) and urdo.systemId=?  and urdo.enabled = 'YES' "
					+ "order by urdo.systemId, urdo.itemcode";
			hasList = this.find(query, new Object[] { objectId,
					uBaseConfig.getSystemId(), uBaseConfig.getSystemId() });
			resCtnMap.put(authResMap.getResName() + "-"
					+ uBaseConfig.getSystemEname(), hasList);
		} else {
			// 系统管理角色，能查看所有子系统的菜单
			List sysList = this.getSubSystemList(true);
			for (int i = 0; i < sysList.size(); i++) {
				UBaseConfigDO ubc = (UBaseConfigDO) sysList.get(i);
				query = "select new Map(urdo.itemcode as menu_id,urdo.itemcode as menu_id,"
						+ " urdo.itemname as menu_name,"
						+ " urdo.systemId as systemId) from MenuDO urdo where "
						+ " urdo.itemcode not in ( select "
						+ " uarr.resDetailValue as resDetailValue"
						+ " from UAuthRoleResourceDO uarr, MenuDO md"
						+ " where md.itemcode=uarr.resDetailValue"
						+ " and uarr.objectId=? and md.systemId=uarr.systemId "
						+ " and md.systemId=? and uarr.resId="
						+ "'"+Constants.MENU_RES_ID+"'"
						+ " ) and urdo.systemId=? "
						+ "order by urdo.systemId, urdo.itemcode";
				hasList = this.find(query, new Object[] { objectId,
						ubc.getSystemId(), ubc.getSystemId() });
				resCtnMap.put(authResMap.getResName() + "-"
						+ ubc.getSystemEname(), hasList);
			}
		}
		return resCtnMap;
	}

	/**
	 * <p>
	 * 方法名称: getLevRes|描述:(简化后)取单一资源
	 * </p>
	 * 
	 * @param objectId
	 * @param systemId
	 * @param authResMap
	 * @return
	 * @throws Exception
	 */
	private Map getLevRes(String objectId, String systemId,
			UAuthResMapDO authResMap,Map defaultUrlInfo) throws Exception {
		Map resCtnMap = new HashMap();
		try {
			// 获取子系统信息
			UBaseConfigDO uBaseConfig = authResMap.getUbaseConfig();
			// 注意：这里的key是系统名+资源名
			// 获取已有的资源
			String query = " select new Map(uarr.resId as resId,uarm.resType as resType,"
					+ " ubd.dicName as resTypeName,"
					+ " uarr.resDetailValue as resDetailValue,"
					+ " uarr.resDetailName as resDetailName)"
					+ " from UAuthResMapDO uarm ,UBaseDictionaryDO ubd , UAuthRoleResourceDO uarr "
					+ " where uarm.resId=uarr.resId and uarm.resType=ubd.dicValue and ubd.dicType='RT' "
					+ " and uarr.resId =  '" + authResMap.getResId()+"'"// 加上限定资源标识符
					+ " and uarm.systemId=? and uarr.objectId=?";
			List hasList = this
					.find(query, new Object[] { systemId, objectId });
			
			String url = uBaseConfig.getDbUrl();

			if (url != null && url.indexOf(Constants.DEFAULT_URL_IP) != -1) {
				url = url.replaceFirst(Constants.DEFAULT_URL_IP_UNICODE,
						defaultUrlInfo.get(Constants.URL_IP).toString());
			}

			if (url != null && url.indexOf(Constants.DEFAULT_URL_PORT) != -1) {
				url = url.replaceFirst(Constants.DEFAULT_URL_PORT_UNICODE,
						defaultUrlInfo.get(Constants.URL_PORT).toString());
			}

			resCtnMap.put(uBaseConfig.getSystemEname() + "-"
					+ authResMap.getResName(), this.resourceConfigDao
					.getLevResCtn(hasList, authResMap, url, uBaseConfig
							.getSystemId()));
		} catch (Exception e) {
			throw e;
		}
		return resCtnMap;
	}

	/**
	 * <p>
	 * 方法名称: getAllRoles|描述:
	 * </p>
	 * 
	 * @param user
	 * @param 当前用户Id
	 */
	public List getAllRoles(UBaseUserDO user) {
		List allRoles = new ArrayList();
		// 根据登录用户获取子系统列表
		List ubcs = authRoleService.getBaseConfig(user);// this.getSubSystemList(false);
		for (Iterator iterator = ubcs.iterator(); iterator.hasNext();) {
			UBaseConfigDO ubc = (UBaseConfigDO) iterator.next();
			if (ubc.getSystemId().equals("00000"))
				continue;
			List ubcRoles = this.authRoleService.getRoleBySysId(ubc
					.getSystemId(),user.getUserId());
			Map ubcMap = new HashMap();
			ubcMap.put(ubc.getSystemNickCname(), ubcRoles); // modify by wangxin
			// 20091110
			// 修改为取系统中文简称
			allRoles.add(ubcMap);
		}
		return allRoles;
	}

	/**
	 * <p>
	 * 方法名称: saveConfigRole|描述:
	 * </p>
	 * 
	 * @param user
	 * @param ids
	 * @param login 
	 */
	public void saveConfigRole(UBaseUserDO user, String[] ids, LoginDO login) {
		String hsql = "delete from UAuthRoleUserDO where userId='"
				+ user.getUserId() + "'" + AuditBase.appendHaveSystemAuthsRangeHQL(login);
		this.executeUpdate(hsql);
		if (ids != null && ids.length > 0) {
			// 形成角色-用户对应关系，然后保存角色-用户关系表
			for (int i = 0; i < ids.length; i++) {
				String roleId = ids[i];
				UAuthRoleUserDO roleUser = new UAuthRoleUserDO();
				roleUser.setRoleId(roleId);
				roleUser.setUserId(user.getUserId());
				if (this.get(UAuthRoleUserDO.class, roleUser) == null) {
					this.save(roleUser);
				}
			}
		}
	}

	/** SPERATOR 分隔符 */
	public static final String SPERATOR = "$,";
}
