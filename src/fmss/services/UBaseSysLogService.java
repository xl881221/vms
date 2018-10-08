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
 * ��Ȩ����:(C)2003-2010 
 * </p>
 * 
 * @����: xindengquan
 * @����: 2009-6-27 ����01:42:26
 * @����: [UBaseSysLogService]��־�ļ�¼�Ͳ�ѯ
 */
public class UBaseSysLogService extends CommonService {

	/** id������ */
	private static IdGenerator idGenerator; // id������

	/**
	 * <p>
	 * ��������: saveUBaseSysLog|����: ������־
	 * </p>
	 * 
	 * @param sysLog
	 *            UBaseSysLog����
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
	 * ��������: saveUBaseSysLog|����: ������־���Ƽ�ʹ�ô˺�����
	 * </p>
	 * 
	 * @param loginUser
	 *            ��ǰ��¼�û�
	 * @param logType
	 *            ��־����
	 * @param menuId
	 *            �˵�id
	 * @param menuName
	 *            �˵�������
	 * @param description
	 *            ��������
	 * @param status
	 *            ����״̬
	 */
	public synchronized void saveUBaseSysLog(LoginDO loginUser, String logType,
			String menuId, String menuName, String description, String status,
			String systemId) {

		try {
			/*
			 * ������־����
			 */
			UBaseSysLogDO sysLog = setUBaseSysLog(loginUser, logType, status,
					systemId);
			sysLog.setMenuId(menuId);
			sysLog.setMenuName(menuName);
			sysLog.setDescription(description);
			sysLog.setStatus(status);
			// ������־��Ϣ
			this.save(sysLog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * ��������: queryUBaseSysLog|����: ��ѯ��־
	 * </p>
	 * 
	 * @param sysLog
	 *            UBaseSysLog����
	 * @return List(��־�����б�)
	 */
	public List queryUBaseSysLog(UBaseSysLogDO sysLog) {
		String sql = "from UBaseSysLogDO ubsl";
		String condition = "";

		if (StringUtils.isNotEmpty(sysLog.getUserId())) {

			// ���ò�ѯ����
			condition = "ubsl.userId='" + sysLog.getUserId() + "'";
		}

		if (StringUtils.isNotEmpty(sysLog.getUserCname())) {
			if (StringUtils.isNotEmpty(condition)) {

				// ���ò�ѯ����
				condition = condition + " and ubsl.userCname='"
						+ sysLog.getUserCname() + "'";
			} else {
				// ���ò�ѯ����
				condition = condition + " ubsl.userCname='"
						+ sysLog.getUserCname() + "'";
			}
		}

		if (StringUtils.isNotEmpty(condition)) {

			// �������յĲ�ѯ���
			sql = " where " + condition;
		}

		return this.find(sql);
	}

	/**
	 * <p>
	 * ��������: setUBaseSysLog|����: ������־������Ϣ
	 * </p>
	 * 
	 * @param loginUser
	 *            ��¼�û�
	 * @param logType
	 *            ��־����
	 * @return UBaseSysLog����
	 */
	public synchronized UBaseSysLogDO setUBaseSysLog(LoginDO loginUser,
			String logType, String status, String systemId) {
		UBaseSysLogDO sysLog = new UBaseSysLogDO();

		/*
		 * ������־������Ϣ
		 */
		idGenerator = IdGenerator.getInstance("LOG");
		// sysLog.setLogId(idGenerator.getNextKey()); // �����־���
		sysLog.setLogId(createLogId()); // �����־���
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
