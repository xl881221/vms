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
 * ��Ȩ����:(C)2003-2010 
 * </p>
 * 
 * @����: sunzhan
 * @����: 2009-6-27 ����02:53:27
 * @����: [ResourceConfigService]��ϵͳ��Դ���ö��������
 */
public class ResourceConfigService extends CommonService {

	/** authRoleService ��ɫ���� */
	private AuthRoleService authRoleService;
	/** resourceConfigDao ��ȡ��ϵͳ��Դdao */
	private ResourceConfigDao resourceConfigDao;
	/** dicService �ֵ���� */
	private DictionaryService dicService;

	/**
	 * @return �ֵ�������
	 */
	public DictionaryService getDicService() {
		return this.dicService;
	}

	/**
	 * @param dicService
	 *            Ҫ���õ� �ֵ�������
	 */
	public void setDicService(DictionaryService dicService) {
		this.dicService = dicService;
	}

	/**
	 * @return ��ɫ����
	 */
	public AuthRoleService getAuthRoleService() {
		return this.authRoleService;
	}

	/**
	 * @param authRoleService
	 *            Ҫ���õ� ��ɫ����
	 */
	public void setAuthRoleService(AuthRoleService authRoleService) {
		this.authRoleService = authRoleService;
	}

	/**
	 * @return ��ϵͳ��Դdao
	 */
	public ResourceConfigDao getResourceConfigDao() {
		return this.resourceConfigDao;
	}

	/**
	 * @param resourceConfigDao
	 *            Ҫ���õ� ��ϵͳ��Դdao
	 */
	public void setResourceConfigDao(ResourceConfigDao resourceConfigDao) {
		this.resourceConfigDao = resourceConfigDao;
	}

	/**
	 * <p>
	 * ��������: queryAuthResMap|����:
	 * </p>
	 * 
	 * @param uAuthResMap
	 *            ��Դ�����ѯ����
	 * @return ��Դ�����б�
	 * @throws Exception
	 */
	public List queryAuthResMap(UAuthResMapDO uAuthResMap) throws Exception {
		// ��ѯhql
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
		// ���������ѯ��������в�ѯ�����򽫲�ѯ�����Ͷ�Ӧ��ֵ����map��
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
		// ����map������ѯ����������ԭ��hql֮��,ÿ��������Ӧ��ֵ����list
		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			sql.append(key);
			valueList.add(map.get(key));
		}
		sql.append(" order by m.systemId,m.resType");
		// ���ݲ�ѯ�����Ͷ�Ӧ���������ݲ�ѯ���
		return this.find(sql.toString(), valueList.toArray(new Object[] {}));
	}

	/**
	 * <p>
	 * ��������: saveResMap|����:������Դ����
	 * </p>
	 * 
	 * @param uarm
	 *            ��Դ��Ϣ����
	 */
	public void saveResMap(UAuthResMapDO uarm) {
		this.save(uarm);
	}

	/**
	 * <p>
	 * ��������: deletes|����:ɾ����Դ����
	 * </p>
	 * 
	 * @param resIDs
	 *            ��Դid����
	 */
	public void deletes(String[] resIDs) {
		// ɾ����Դ���ձ�Ĺ�ϵ
		if (null != resIDs && resIDs.length > 0) {
			for (int index = 0; index < resIDs.length; index++) {
				String hsql = "delete from UAuthRoleResourceDO urr where urr.resId="
						+ resIDs[index];
				this.executeUpdate(hsql);
			}
		}
		// ɾ����Դ
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
	 * ��������: getsubSystemList|����:�����ϵͳ�б�
	 * </p>
	 * 
	 * @return ��ϵͳ�б�
	 */
	public List getSubSystemList(boolean isAll) {
		return this.authRoleService.getSystemList(isAll);
	}

	/**
	 * <p>
	 * ��������: get|����:����resId��ȡresMap����
	 * </p>
	 * 
	 * @param resMap
	 *            ��Դ����
	 * @return ��Դ������ϸ��Ϣ
	 */
	public UAuthResMapDO get(UAuthResMapDO resMap) {
		return this.authRoleService.getAuthResMap(resMap.getResId());
	}

	/**
	 * <p>
	 * ��������: updateResMap|����:����resMap
	 * </p>
	 * 
	 * @param object
	 *            ��Դ����
	 */
	public void updateResMap(UAuthResMapDO object) {
		this.update(object);
	}

	/**
	 * <p>
	 * ��������: getResContent|����:��ȡ��ϵͳ��˽����Դ����
	 * </p>
	 * 
	 * @param authResMap
	 *            ��Դ����
	 * @param cfg
	 *            ��ϵͳ����
	 * @return ��ϵͳ����Դ����
	 * @throws Exception
	 */
	public List getResContent(UAuthResMapDO authResMap, UBaseConfigDO cfg)
			throws Exception {
		return this.resourceConfigDao.getResource(authResMap, cfg.getDbUrl());
	}

	/**
	 * <p>
	 * ��������: getHavResByRole|����: ���ݽ�ɫ��ȡ�ý�ɫ���е���Դ��Ϊ���������Ĳ�ѯ
	 * </p>
	 * 
	 * @param role
	 *            ��ɫ����
	 * @param baseConfig
	 *            ��ϵͳ��Ϣ����
	 * @param page
	 *            ��ҳ����
	 * @return ��ɫ���е���Դ�б�
	 */
	public List getHavResByRole(UAuthRoleDO role, UBaseConfigDO baseConfig,
			PaginationList page) {
		return this.getHavResByObjectId(role.getRoleId(), baseConfig
				.getSystemId(), page);
	}

	/**
	 * <p>
	 * ��������: getHavResByRole|����:���ݽ�ɫ����Դ������ȡ�ý�ɫ���е���Դ,Ϊ�������Ĳ�ѯ
	 * </p>
	 * 
	 * @param role
	 *            ��ɫ����
	 * @param authResMap
	 *            ��Դ��ѯ����
	 * @param baseConfig
	 *            ��ϵͳ����
	 * @param page
	 *            ��ҳ����
	 * @return ��ɫ���е���Դ
	 */
	public List getHavResByRole(UAuthRoleDO role, UAuthResMapDO authResMap,
			UBaseConfigDO baseConfig, PaginationList paginationList) {
		return this.getHavResByObjectId(role.getRoleId(), authResMap,
				baseConfig.getSystemId(), paginationList);
	}

	/**
	 * <p>
	 * ��������: getLevResByRole|����:���ݽ�ɫ��ȡ�ý�ɫû�е�˽����Դ
	 * </p>
	 * 
	 * @param role
	 *            ��ɫ����
	 * @param baseConfig
	 *            ��ϵͳ����
	 * @return ��ɫû�е���Դ
	 * @throws Exception
	 */
	public Map getLevResByRole(UAuthRoleDO role, UBaseConfigDO baseConfig)
			throws Exception {
		return this.getLevResByObjectId(role.getRoleId(), baseConfig
				.getSystemId());
	}

	/**
	 * <p>
	 * ��������: getHavResByObjectId|����:���ݸ���Ȩ������ID��ȡ�ø���Ȩ���������е���Դ
	 * </p>
	 * 
	 * @param objectId
	 *            Ȩ������id
	 * @return Ȩ���������е���Դ
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
	 * ��������: getHavResByObjectId|����:���ݸ���Ȩ������ID��ȡ�ø���Ȩ���������е���Դ��������ϵͳ��������ɫ���ж�
	 * </p>
	 * 
	 * @param objectId
	 *            Ȩ������id
	 * @param objectId
	 *            systemId
	 * @param page
	 *            ��ҳ����
	 * @return Ȩ���������е���Դ
	 */
	public List getHavResByObjectId(String objectId, String systemId,
			PaginationList page) {
		// ��ѯsql
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
	 * ��������: getLevResByObjectId|����: ���ݸ���Ȩ������ID��ϵͳid��ȡ�ø���Ȩ������û�е���Դ
	 * </p>
	 * 
	 * @param objectId
	 *            ����id
	 * @param systemId
	 *            ��ϵͳid
	 * @return ����û�е���Դ
	 * @throws Exception
	 */
	public Map getLevResByObjectId(String objectId, String systemId)
			throws Exception {
		Map resCtnMap = new HashMap();
		try {
			// ȡ���������õ���ϵͳ��Դ��Ϣ
			String query = "select uarm from UAuthResMapDO uarm where uarm.systemId=?";
			List authResMapList = this.find(query, systemId);
			// ��ȡ��ϵͳ��Ϣ
			UBaseConfigDO uBaseConfig = (UBaseConfigDO) this.get(
					UBaseConfigDO.class, systemId);
			// ��ȡ���е���Դ
			query = " select new Map(uarr.resId as resId,uarm.resType as resType,"
					+ " ubd.dicName as resTypeName,"
					+ " uarr.resDetailValue as resDetailValue,"
					+ " uarr.resDetailName as resDetailName)"
					+ " from UAuthResMapDO uarm ,UBaseDictionaryDO ubd , UAuthRoleResourceDO uarr "
					+ " where uarm.resId=uarr.resId and uarm.resType=ubd.dicValue and ubd.dicType='RT' "
					+ " and uarm.systemId=? and uarr.objectId=?";
			List hasList = this
					.find(query, new Object[] { systemId, objectId });
			// ��ȡ��Դ������û�����õ���Դ
			for (Iterator iterator = authResMapList.iterator(); iterator
					.hasNext();) {
				UAuthResMapDO authResMap = (UAuthResMapDO) iterator.next();
				// ע�⣺�����key����Դ����,��Ϊ��ȷ����ϵͳ����
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
	 * ��������: getHavResByObjectId|����:����Ȩ������ID����Դ������ȡ��Ȩ���������е���Դ
	 * </p>
	 * 
	 * @param objectId
	 *            ����id
	 * @param authResMap
	 *            ��Դ����
	 * @param systemId
	 *            ��ϵͳid
	 * @param page
	 *            ��ҳ����
	 * @return �������е���Դ
	 */
	public List getHavResByObjectId(String objectId, UAuthResMapDO authResMap,

	String systemId, PaginationList paginationList) {

		// ��ѯhql modify by wangxin 20091116 �����ѯ��ɾ���������⣬ԭ��ΪϵͳID����ԴIDδ��ȡ

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

		// ���������ѯ��������в�ѯ�����򽫲�ѯ�����Ͷ�Ӧ��ֵ����map��

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

		// ����map������ѯ����������ԭ��hql֮��,ÿ��������Ӧ��ֵ����list

		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {

			String key = (String) iterator.next();

			query += key;

			valueList.add(map.get(key));

		}

		query += " order by uarr.resDetailValue";

		// ���ݲ�ѯ�����Ͷ�Ӧ���������ݲ�ѯ���

		return this.find(query, valueList, paginationList);

	}

	/**
	 * <p>
	 * ��������: getLevResKey|����:��ȡû�����õ���Դ�ֶ���
	 * </p>
	 * 
	 * @param baseConfig
	 *            ��ϵͳ��Ϣ
	 * @return û�����õ���Դ�ֶ���
	 */
	public Map getLevResKey(UBaseConfigDO baseConfig) {
		// ȡ���������õ���ϵͳ��Դ��Ϣ
		Map resKey = new HashMap();
		String query = "select uarm from UAuthResMapDO uarm where uarm.systemId=?";
		List authResMapList = this.find(query, baseConfig.getSystemId());
		for (Iterator iterator = authResMapList.iterator(); iterator.hasNext();) {
			UAuthResMapDO resMap = (UAuthResMapDO) iterator.next();
			// ע�⣺�����key����Դ����,��Ϊ��ȷ����ϵͳ����
			resKey.put(resMap.getResType(), resMap);
		}
		return resKey;
	}

	/**
	 * <p>
	 * ��������: getLevResKey|����:��ȡû�����õ���Դ�ֶ���
	 * </p>
	 * 
	 * @return û�����õ���Դ�ֶ���
	 */
	public Map getLevResKey() {
		// ȡ���������õ���ϵͳ��Դ��Ϣ
		Map resKey = new HashMap();
		String query = "select uarm from UAuthResMapDO uarm";
		List authResMapList = this.find(query);
		for (Iterator iterator = authResMapList.iterator(); iterator.hasNext();) {
			// ע�⣺key����Դ���ͼ���ϵͳ����
			UAuthResMapDO resMap = (UAuthResMapDO) iterator.next();
			resKey.put(resMap.getResType() + SPERATOR + resMap.getSystemId(),
					resMap);
		}
		return resKey;
	}

	/**
	 * <p>
	 * ��������: saveConfigRes|����:����������󱣴����õ���Դ
	 * </p>
	 * 
	 * @param string
	 *            ��Դ�ַ���,ֵ��;�ָ�
	 * @param objectId
	 *            �������id
	 * @param authResMapDO
	 */
	public void saveConfigRes(LoginDO login, String string, String objectId) {
		if (StringUtils.isNotEmpty(string)) {
			String[] strArray = string.split(";");
			/*
			 * �ָ�ÿ�е�ֵ��ֵ��Ӧ������,����Ϊ resID,resValue,resName,��&,�ָ�
			 * �γɽ�ɫ-��Դ��Ӧ��ϵ��Ȼ�󱣴��ɫ-��Դ��ϵ��
			 */
			login.addDescription(",��Դ[");
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
	 * ��������: deleteAllRes|����:ɾ�������Ӧ�Ķ����Դ
	 * </p>
	 * 
	 * @param resMapList
	 *            ��Դ�б�,�б���;����������ΪresId,resValue
	 * @param objectId
	 *            Ȩ������id
	 */
	public void deleteAllRes(LoginDO login, List resMapList, String objectId) {
		List list = new ArrayList();
		/*
		 * �γɽ�ɫ-��Դ��Ӧ��ϵ��Ȼ��ɾ����ɫ-��Դ��ϵ��
		 */
		login.addDescription(",��Դ[");
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
	 * ��������: getResTreeXml|����:��ȡû�����õ���Դ�б�������ȡֵ�߼�, ֻ�ô�resmapȡ��һֵ����
	 * </p>
	 * 
	 * @param role
	 *            ��ɫ����
	 * @param baseConfig
	 *            ��ϵͳ����
	 * @param resMap
	 *            ��Դ����
	 * @return ��Դ�б����ַ���
	 * @throws Exception
	 */
	/*
	 * public String getResTreeXml(UAuthRoleDO role, UBaseConfigDO baseConfig,
	 * UAuthResMapDO resMap) throws Exception{ StringBuffer sb = new
	 * StringBuffer(); try{ Map ctnMap = new HashMap(); resMap =
	 * this.get(resMap); // ��ȡ��Դ������û�����õ���Դ,������Դ�����⴦��,�˵� if(Constants.MENU_RES_ID
	 * == Integer.parseInt(resMap.getResId())){ // �˵�������չ�� ctnMap =
	 * this.getLevPubMenuCtn(role.getRoleId(), baseConfig .getSystemId(),
	 * resMap); }else{ ctnMap = this.getLevRes(role.getRoleId(), baseConfig
	 * .getSystemId(), resMap); }
	 * sb.append("<?xml version='1.0' encoding='UTF-8'?>");
	 * sb.append("<tree id='0'>"); Set keySet = ctnMap.keySet(); for(Iterator
	 * iterator = keySet.iterator(); iterator.hasNext();){ String key = (String)
	 * iterator.next(); sb.append("<item text='"); sb.append(key);
	 * sb.append("' id='"); sb.append(key); sb.append("' child='1");
	 * sb.append("'>"); sb.append("<userdata name='levelType'>1</userdata>"); //
	 * ȡ������Դ����Դ���� List resList = (List) ctnMap.get(key); for(int j = 0; resList
	 * != null && j < resList.size(); j++){ Map row = (Map) resList.get(j);
	 * sb.append("<item text='"); sb.append(row.get(resMap.getSrcIdField()) +
	 * "-" + row.get(resMap.getSrcNameField()));
	 * 
	 * sb.append("' id='"); // ע�⣺��id������ϵͳid�޶�,��Ϊid��ΨһԼ��
	 * sb.append(row.get("systemId") + ".");
	 * sb.append(row.get(resMap.getSrcKeyField())); sb.append("'>");
	 * sb.append("<userdata name='levelType'>2</userdata>");
	 * sb.append("<userdata name='value'>"); // ����systemid
	 * sb.append(resMap.getResId() + SPERATOR + row.get(resMap.getSrcIdField())
	 * + SPERATOR + row.get(resMap.getSrcNameField()) + SPERATOR +
	 * row.get("systemId")); sb.append("</userdata>"); sb.append("</item>"); }
	 * sb.append("</item>"); } sb.append("</tree>"); }catch (Exception e){ throw
	 * e; } return sb.toString(); }
	 */
	/**
	 * <p>
	 * ��������: getResTreeXmlEx|����:��ȡû�����õ���Դ�б�������ȡֵ�߼�, ֻ�ô�resmapȡ��һֵ����
	 * </p>
	 * 
	 * @param role
	 *            ��ɫ����
	 * @param baseConfig
	 *            ��ϵͳ����
	 * @param resMap
	 *            ��Դ����
	 * @return ��Դ�б����ַ���
	 * @param defaultUrlInfo
	 *            Ĭ��IP��ַ��Ϣ
	 * @throws Exception
	 */
	public String getResTreeXmlEx(UAuthRoleDO role, UBaseConfigDO baseConfig,
			UAuthResMapDO resMap, Map defaultUrlInfo) throws Exception {
		StringBuffer sb = new StringBuffer();
		// ���¸�ֵ
		UBaseConfigDO config = new UBaseConfigDO();
		BeanUtils.copyProperties(baseConfig, config);
		try {
			Map ctnMap = new HashMap();
			resMap = this.get(resMap);
			// ��ȡ��Դ������û�����õ���Դ,������Դ�����⴦��,�˵�
			if (Constants.MENU_RES_ID == Integer.parseInt(resMap.getResId())) {
				// �˵�������չ��
				ctnMap = this.getLevPubMenuCtn(role.getRoleId(), role
						.getSystemId(), resMap);
			} else {
				if (Constants.INST_RES_ID == Integer
						.parseInt(resMap.getResId())) {
					config.setSystemId(Constants.SYSTEM_COMMON_ID);
				}
				/*
				 * //add by fwy �� if(44 == Integer.parseInt(resMap.getResId())){
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
				// // ȡ������Դ����Դ����
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
					// ע�⣺��id������ϵͳid�޶�,��Ϊid��ΨһԼ��
					sb.append(row.get("systemId") + ".");
					sb.append(row.get(resMap.getSrcKeyField()));
					sb.append("' levelType='2'");
					sb.append(" _hasChild='0' ");
					sb.append(" _canSelect='1' ");
					sb.append(" value='");
					// ����systemid
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
	 * ��������: getResTreeXmlEx|����:��ȡû�����õ���Դ�б�������ȡֵ�߼�, ֻ�ô�resmapȡ��һֵ����
	 * </p>
	 * 
	 * @param role
	 *            ��ɫ����
	 * @param baseConfig
	 *            ��ϵͳ����
	 * @param resMap
	 *            ��Դ����
	 * @return ��Դ�б����ַ���
	 * @param defaultUrlInfo
	 *            Ĭ��IP��ַ��Ϣ
	 * @throws Exception
	 */
	public String getResTreeListEx(UAuthRoleDO role, UBaseConfigDO baseConfig, UAuthResMapDO resMap, Map defaultUrlInfo) throws Exception {
		StringBuffer sb = new StringBuffer();
		// ���¸�ֵ
		UBaseConfigDO config = new UBaseConfigDO();
		BeanUtils.copyProperties(baseConfig, config);
		try {
			Map ctnMap = new HashMap();
			resMap = this.get(resMap);
			// ��ȡ��Դ������û�����õ���Դ,������Դ�����⴦��,�˵�
			if (Constants.MENU_RES_ID == Integer.parseInt(resMap.getResId())) {
				// �˵�������չ��
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
	 * ��������: getPubRes|����:��ȡ���й�����Դmap
	 * </p>
	 * 
	 * @return
	 */
	public List getPubRes() {
		// �˵����������̶��Ĺ�����Դ
		String query = " select new Map(md.itemcode as itemcode,md.itemname as itemname,"
				+ " md.systemId as systemId) from MenuDO md order by md.systemId,md.itemcode";
		return this.find(query);
	}

	/**
	 * <p>
	 * ��������: getResMap|����: ��ȡ������Դmap
	 * </p>
	 * 
	 * @param baseConfigDO
	 * @return
	 */
	public List getResMap(UBaseConfigDO baseConfigDO) {
		// ϵͳ������Դ����
		if (Constants.SYSTEM_COMMON_ID.equals(baseConfigDO.getSystemId())) {
			return this.find("from UAuthResMapDO");
		} else {
			String query = "from UAuthResMapDO where ((resType='PRI' and systemId=?) or resType='PUB')";
			return this.find(query, baseConfigDO.getSystemId());
		}
	}

	/**
	 * <p>
	 * ��������: getPubMenuCtn|����:��ȡ������Դ-�˵�,�Զ�ѡ����ʽ��ʾ
	 * </p>
	 * 
	 * @return ������Դ-�˵�
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
				// ���ӿհ�
				String itemCode = menu.get("itemcode").toString();
				String[] itemCodeArray = itemCode.split("\\.");
				String space = "";
				for (int j = 0; itemCodeArray != null
						&& j < itemCodeArray.length; j++) {
					space += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
				}
				menu.put("itemname", space + menu.get("itemname"));
			}
			// ����key
			menuMap.put(baseConfig.getSystemEname(), menus);
		}
		list.add(menuMap);
		return list;
	}

	/**
	 * <p>
	 * ��������: getPubInstCtn|����:��ȡ������Դ-����,�Զ�ѡ����ʽ��ʾ
	 * </p>
	 * 
	 * @return ������Դ-����
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
			// ��ӿհ�
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
	 * ��������: getLevPubResCtn|����:��ȡû�����õĹ�����Դ
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
		// ��ȡ���е���Դ
		// ��ȡ��ϵͳ��Ϣ
		UBaseConfigDO uBaseConfig = (UBaseConfigDO) this.get(
				UBaseConfigDO.class, systemId);
		List hasList = null;
		String query = "";
		UAuthRoleDO role = this.authRoleService.getRoleByRoleId(objectId);
		if (!Constants.SYSTEM_COMMON_ID.equals(role.getSystemId())) {
			// ��ϵͳ�����ɫ,ֻ�ܲ鿴�Լ���ϵͳ�Ĳ˵�
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
			// ϵͳ�����ɫ���ܲ鿴������ϵͳ�Ĳ˵�
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
	 * ��������: getLevRes|����:(�򻯺�)ȡ��һ��Դ
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
			// ��ȡ��ϵͳ��Ϣ
			UBaseConfigDO uBaseConfig = authResMap.getUbaseConfig();
			// ע�⣺�����key��ϵͳ��+��Դ��
			// ��ȡ���е���Դ
			String query = " select new Map(uarr.resId as resId,uarm.resType as resType,"
					+ " ubd.dicName as resTypeName,"
					+ " uarr.resDetailValue as resDetailValue,"
					+ " uarr.resDetailName as resDetailName)"
					+ " from UAuthResMapDO uarm ,UBaseDictionaryDO ubd , UAuthRoleResourceDO uarr "
					+ " where uarm.resId=uarr.resId and uarm.resType=ubd.dicValue and ubd.dicType='RT' "
					+ " and uarr.resId =  '" + authResMap.getResId()+"'"// �����޶���Դ��ʶ��
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
	 * ��������: getAllRoles|����:
	 * </p>
	 * 
	 * @param user
	 * @param ��ǰ�û�Id
	 */
	public List getAllRoles(UBaseUserDO user) {
		List allRoles = new ArrayList();
		// ���ݵ�¼�û���ȡ��ϵͳ�б�
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
			// �޸�Ϊȡϵͳ���ļ��
			allRoles.add(ubcMap);
		}
		return allRoles;
	}

	/**
	 * <p>
	 * ��������: saveConfigRole|����:
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
			// �γɽ�ɫ-�û���Ӧ��ϵ��Ȼ�󱣴��ɫ-�û���ϵ��
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

	/** SPERATOR �ָ��� */
	public static final String SPERATOR = "$,";
}
