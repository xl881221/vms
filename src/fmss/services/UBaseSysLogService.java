package fmss.services;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseSysLogDO;
import fmss.common.db.IdGenerator;

/**
 * <p>
 * 版权所有:(C)2003-2010 
 * </p>
 * 
 * @作者: xindengquan
 * @日期: 2009-6-27 下午01:42:26
 * @描述: [UBaseSysLogService]日志的记录和查询
 */
public class UBaseSysLogService extends CommonService {

	/** id生成器 */
	private static IdGenerator idGenerator; // id生成器

	/**
	 * <p>
	 * 方法名称: saveUBaseSysLog|描述: 保存日志
	 * </p>
	 * 
	 * @param sysLog
	 *            UBaseSysLog对象
	 */
	public synchronized void saveUBaseSysLog(UBaseSysLogDO sysLog) {
		try {
			this.save(sysLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * 方法名称: saveUBaseSysLog|描述: 保存日志（推荐使用此函数）
	 * </p>
	 * 
	 * @param loginUser
	 *            当前登录用户
	 * @param logType
	 *            日志类型
	 * @param menuId
	 *            菜单id
	 * @param menuName
	 *            菜单的名字
	 * @param description
	 *            操作描述
	 * @param status
	 *            操作状态
	 */
	public synchronized void saveUBaseSysLog(LoginDO loginUser, String logType,
			String menuId, String menuName, String description, String status,
			String systemId) {

		try {
			/*
			 * 设置日志对象
			 */
			UBaseSysLogDO sysLog = setUBaseSysLog(loginUser, logType, status,
					systemId);
			sysLog.setMenuId(menuId);
			sysLog.setMenuName(menuName);
			sysLog.setDescription(description);
			sysLog.setStatus(status);
			// 保存日志信息
			this.save(sysLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * 方法名称: queryUBaseSysLog|描述: 查询日志
	 * </p>
	 * 
	 * @param sysLog
	 *            UBaseSysLog对象
	 * @return List(日志对象列表)
	 */
	public List queryUBaseSysLog(UBaseSysLogDO sysLog) {
		String sql = "from UBaseSysLogDO ubsl";
		String condition = "";

		if (StringUtils.isNotEmpty(sysLog.getUserId())) {

			// 设置查询条件
			condition = "ubsl.userId='" + sysLog.getUserId() + "'";
		}

		if (StringUtils.isNotEmpty(sysLog.getUserCname())) {
			if (StringUtils.isNotEmpty(condition)) {

				// 设置查询条件
				condition = condition + " and ubsl.userCname='"
						+ sysLog.getUserCname() + "'";
			} else {
				// 设置查询条件
				condition = condition + " ubsl.userCname='"
						+ sysLog.getUserCname() + "'";
			}
		}

		if (StringUtils.isNotEmpty(condition)) {

			// 设置最终的查询语句
			sql = " where " + condition;
		}

		return this.find(sql);
	}

	/**
	 * <p>
	 * 方法名称: setUBaseSysLog|描述: 设置日志对象信息
	 * </p>
	 * 
	 * @param loginUser
	 *            登录用户
	 * @param logType
	 *            日志类型
	 * @return UBaseSysLog对象
	 */
	public synchronized UBaseSysLogDO setUBaseSysLog(LoginDO loginUser,
			String logType, String status, String systemId) {
		UBaseSysLogDO sysLog = new UBaseSysLogDO();

		/*
		 * 设置日志对象信息
		 */
		idGenerator = IdGenerator.getInstance("LOG");
		// sysLog.setLogId(idGenerator.getNextKey()); // 获得日志编号
		sysLog.setLogId(createLogId()); // 获得日志编号
		sysLog.setUserId(loginUser.getUserId());
		sysLog.setUserEname(loginUser.getUserEname());
		sysLog.setUserCname(loginUser.getUserCname());
		sysLog.setInstId(loginUser.getInstId() != null ? loginUser.getInstId()
				: " ");
		sysLog.setInstCname(loginUser.getInstCname());
		sysLog.setIp(loginUser.getIp());
		if (loginUser.getBrowser().length() > 100) {
			sysLog.setBrowse(loginUser.getBrowser().substring(0, 99));
		} else {
			sysLog.setBrowse(loginUser.getBrowser());
		}
		sysLog.setDescription(loginUser.getDescription());
		sysLog.setExecTime(new java.sql.Timestamp(new java.util.Date()
				.getTime()));
		sysLog.setLogType(logType);
		sysLog.setMenuId(loginUser.getMenuId());
		sysLog.setMenuName(loginUser.getMenuName());
		sysLog.setStatus(status);
		sysLog.setSystemId(systemId);
		return sysLog;
	}

	public void setIdGenerator(IdGenerator idGenerator) {
		UBaseSysLogService.idGenerator = idGenerator;
	}

	private long createLogId() {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");

		String lLogId = df.format(new Date());

		int iran = (int) (Math.random() * 99);

		if (iran >= 10) {
			lLogId += String.valueOf(iran);
		} else {
			lLogId += "0" + String.valueOf(iran);
		}

		return Long.parseLong(lLogId);
	}
}
