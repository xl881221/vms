package fmss.services;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import common.logger.LogDO;
import common.logger.LogManagerDAO;
import common.logger.PageBox;

import fmss.dao.entity.LoginDO;
import fmss.common.util.PaginationList;


/**
 * <p>
 * 版权所有:(C)2003-2010 
 * </p>
 * 
 * @作者: zhangshoufeng
 * @日期: 2009-11-3 下午01:47:36
 * @描述: [LogManagerService]请在此简要描述类的功能
 */
public class LogManagerService {
	public LogManagerDAO logManagerDAO;
	public void setLogManagerDAO(LogManagerDAO logManagerDAO) {
		this.logManagerDAO = logManagerDAO;
	}

	private static final String DAY = "yyyy-MM-dd";
	public static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat(DAY,java.util.Locale.ENGLISH);
	
	/**
	 * <p>
	 * 方法名称: writeLog|描述:
	 * </p>
	 * 
	 * @param logDO
	 *            日志对象
	 */
	public void writeLog(LogDO logDO) {
		logManagerDAO.insert(logDO);
	}

	/**
	 * <p>
	 * 方法名称: writeLog|描述:
	 * </p>
	 * @param loginUser
	 * 			用户登录信息
	 * @param menuId
	 *            栏目编号
	 * @param menuName
	 *            栏目名称
	 * @param logType
	 *            日志类型
	 * @param description
	 *            描述
	 */
	public void writeLog(LoginDO loginUser,String logType,String status) {
		LogDO logDO = new LogDO();
		logDO.setUserId(loginUser.getUserId());
		logDO.setUserEname(loginUser.getUserEname());
		logDO.setUserCname(loginUser.getUserCname());
		logDO.setInstId(loginUser.getInstId());
		logDO.setInstCname(loginUser.getInstCname());
		logDO.setMenuId(loginUser.getMenuId());
		logDO.setMenuName(loginUser.getMenuName());
		logDO.setDescription(loginUser.getDescription());
		logDO.setLogType(logType);
		logDO.setStatus(status);
		logDO.setExecTime(new Date());
		logDO.setIp(loginUser.getIp());
		logDO.setBrowse(loginUser.getBrowser());
		//logDO.setSystemId(systemId);
		logManagerDAO.insert(logDO);
	}

	/**
	 * <p>
	 * 方法名称: writeLog|描述:
	 * </p>
	 * 
	 * @param userId
	 *            用户编号
	 * @param userEname
	 *            用户登录名
	 * @param userCname
	 *            用户中文
	 * @param instId
	 *            机构编号
	 * @param instCname
	 *            机构名称
	 * @param menuId
	 *            栏目编号
	 * @param menuName
	 *            栏目名称
	 * @param ip
	 *            用户IP
	 * @param browse
	 *            用户浏览器
	 * @param logType
	 *            日志类型
	 * @param execTime
	 *            执行时间
	 * @param description
	 *            描述
	 * @param status
	 *            执行状态
	 */
	public void writeLog(String userId, String userEname, String userCname,
			String instId, String instCname, String menuId, String menuName,
			String ip, String browse, String logType, Date execTime,
			String description, String status) {
		LogDO logDO = new LogDO();
		logDO.setUserId(userId);
		logDO.setUserEname(userEname);
		logDO.setUserCname(userCname);
		logDO.setInstId(instId);
		logDO.setInstCname(instCname);
		logDO.setMenuId(menuId);
		logDO.setMenuName(menuName);
		logDO.setIp(ip);
		logDO.setBrowse(browse);
		logDO.setLogType(logType);
		logDO.setExecTime(execTime);
		logDO.setDescription(description);
		logDO.setStatus(status);
		logManagerDAO.insert(logDO);
	}
	
	public PageBox selectByFormWithPaging(LogDO log, int pageSize,
			int pageNum){
		PageBox pb=logManagerDAO.selectByFormWithPaging(log, pageSize, pageNum);
		return pb;
	} 
	
	public List selectByFormWithPaging(Map parms, PaginationList paginationList){
		LogDO logDO = new LogDO();
		logDO.setUserId((String)parms.get("userId"));
		logDO.setUserEname((String)parms.get("userEname"));
		logDO.setUserCname((String)parms.get("userCname"));
		logDO.setInstId((String)parms.get("instId"));
		logDO.setInstCname((String)parms.get("instCname"));
		logDO.setMenuId((String)parms.get("menuId"));
		logDO.setMenuName((String)parms.get("menuName"));
		logDO.setIp((String)parms.get("ip"));
		logDO.setBrowse((String)parms.get("browse"));
		logDO.setLogType((String)parms.get("logType"));
		logDO.setExecTime((Date)parms.get("execTime"));
		logDO.setDescription((String)parms.get("description"));
		
		logDO.setStatus((String)parms.get("status"));
		try {
			if (parms.get("startTime") != null && !"".equals(parms.get("startTime"))) {
				logDO.setBeginExecTime(DAY_FORMAT.parse(parms.get("startTime").toString()));
			}
			if (parms.get("endTime") != null && !"".equals(parms.get("endTime"))) {
				Date endDate = DAY_FORMAT.parse(parms.get("endTime").toString());
				Calendar cal = Calendar.getInstance();
				cal.setTime(endDate);
				cal.add(Calendar.DATE, 1);
				logDO.setEndExecTime(cal.getTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		PageBox pb=logManagerDAO.selectByFormWithPaging(logDO,  paginationList.getPageSize(), paginationList.getCurrentPage());
		paginationList.setCurrentPage(pb.getPageObject().getPageIndex());
		paginationList.setPageSize(pb.getPageObject().getPageSize());
		paginationList.setRecordCount(pb.getPageObject().getItemAmount());
		paginationList.setRecordList(pb.getPageList());
		return pb.getPageList();
	}
	
	public java.util.Map selectAllByParams(Map parms){
		LogDO logDO = new LogDO();
		logDO.setUserId((String)parms.get("userId"));
		logDO.setUserEname((String)parms.get("userEname"));
		logDO.setUserCname((String)parms.get("userCname"));
		logDO.setInstId((String)parms.get("instId"));
		logDO.setInstCname((String)parms.get("instCname"));
		logDO.setMenuId((String)parms.get("menuId"));
		logDO.setMenuName((String)parms.get("menuName"));
		logDO.setIp((String)parms.get("ip"));
		logDO.setBrowse((String)parms.get("browse"));
		logDO.setLogType((String)parms.get("logType"));
		logDO.setExecTime((Date)parms.get("execTime"));
		logDO.setDescription((String)parms.get("description"));
		logDO.setStatus((String)parms.get("status"));
		try {
			if (parms.get("startTime") != null && !"".equals(parms.get("startTime"))) {
				logDO.setBeginExecTime(DAY_FORMAT.parse(parms.get("startTime").toString()));
			}
			if (parms.get("endTime") != null && !"".equals(parms.get("endTime"))) {
				Date endDate = DAY_FORMAT.parse(parms.get("endTime").toString());
				Calendar cal = Calendar.getInstance();
				cal.setTime(endDate);
				cal.add(Calendar.DATE, 1);
				logDO.setEndExecTime(cal.getTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logManagerDAO.selectAllByParams(logDO);
	}
	
	public int deleteByPrimarys(String []ids){
		return logManagerDAO.deleteByPrimarys(ids);
	}
}
