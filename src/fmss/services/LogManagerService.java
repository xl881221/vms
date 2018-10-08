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
 * ��Ȩ����:(C)2003-2010 
 * </p>
 * 
 * @����: zhangshoufeng
 * @����: 2009-11-3 ����01:47:36
 * @����: [LogManagerService]���ڴ˼�Ҫ������Ĺ���
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
	 * ��������: writeLog|����:
	 * </p>
	 * 
	 * @param logDO
	 *            ��־����
	 */
	public void writeLog(LogDO logDO) {
		logManagerDAO.insert(logDO);
	}

	/**
	 * <p>
	 * ��������: writeLog|����:
	 * </p>
	 * @param loginUser
	 * 			�û���¼��Ϣ
	 * @param menuId
	 *            ��Ŀ���
	 * @param menuName
	 *            ��Ŀ����
	 * @param logType
	 *            ��־����
	 * @param description
	 *            ����
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
	 * ��������: writeLog|����:
	 * </p>
	 * 
	 * @param userId
	 *            �û����
	 * @param userEname
	 *            �û���¼��
	 * @param userCname
	 *            �û�����
	 * @param instId
	 *            �������
	 * @param instCname
	 *            ��������
	 * @param menuId
	 *            ��Ŀ���
	 * @param menuName
	 *            ��Ŀ����
	 * @param ip
	 *            �û�IP
	 * @param browse
	 *            �û������
	 * @param logType
	 *            ��־����
	 * @param execTime
	 *            ִ��ʱ��
	 * @param description
	 *            ����
	 * @param status
	 *            ִ��״̬
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
