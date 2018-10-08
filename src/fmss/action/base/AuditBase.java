package fmss.action.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseConfigDO;
import fmss.dao.entity.UBaseUserDO;
import fmss.common.cache.CacheManager;
import fmss.services.AuthRoleService;
import fmss.common.util.SpringContextUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;


public abstract class AuditBase {

	protected transient Log logger = LogFactory.getLog(getClass());

	protected static JdbcDaoAccessor dao = (JdbcDaoAccessor) SpringContextUtils.getBean("jdbcDaoAccessor");

	protected static CacheManager cacheManager = (CacheManager) SpringContextUtils.getBean("cacheManager");

	public static final int AUDIT_STATUS_NOADUITED = 1;// δ��
	public static final int AUDIT_STATUS_APPROVED = 2;// ͨ��
	public static final int AUDIT_STATUS_REJECTED = 3;// ��ͨ��
	public static final int AUDIT_STATUS_CANCEL = 4;// ����
	public static final int AUDIT_STATUS_HASAUDIT = 5;// ����

	public static final int ROLE_RES_CHANGE_STATUS_DELETE = 3;// ɾ��
	public static final int ROLE_RES_CHANGE_STATUS_ADD = 2;// ����
	public static final int ROLE_RES_CHANGE_STATUS_NORMAL = 1;// ����
	public static final int ROLE_RES_CHANGE_STATUS_ABANDON = 4;// ����

	public static final int USER_ROLE_CHANGE_STATUS_DELETE = 3;// ɾ��
	public static final int USER_ROLE_CHANGE_STATUS_ADD = 2;// ����
	public static final int USER_ROLE_CHANGE_STATUS_NORMAL = 1;// ����
	public static final int USER_ROLE_CHANGE_STATUS_ABANDON = 4;// ����

	public static final Long CHANGE_TYPE_USER_ADD = new Long(1);// �û�����
	public static final Long CHANGE_TYPE_USER_MODIFY = new Long(2);// �û��޸�
	public static final Long CHANGE_TYPE_USER2ROLE = new Long(3);// �û����ɫ
	public static final Long CHANGE_TYPE_ROLE2USER = new Long(4);// ��ɫ���û�
	public static final Long CHANGE_TYPE_ROLE2RESOURCE = new Long(5);// ��ɫ����Դ
	public static final Long CHANGE_TYPE_USER_DELETE = new Long(6);// �û�ɾ��
	public static final Long CHANGE_TYPE_INST_ADD = new Long(7);//    ��������
	public static final Long CHANGE_TYPE_INST_MODIFY = new Long(8);// �����޸�
	public static final Long CHANGE_TYPE_INST_DELETE = new Long(9);// ����ɾ��
	public static final Long CHANGE_TYPE_SYS_PARAM_MODIFY = new Long(10);// ϵͳ�����޸�
	public static final Long CHANGE_TYPE_SUB_SYSTEM_ADMIN_MODIFY = new Long(11);// ��ϵͳ���ò����޸����
	public static final Long CHANGE_TYPE_SUB_SYSTEM_ADMIN_DELETE = new Long(12);// ��ϵͳ���ù���Աɾ��
	public static final Long CHANGE_TYPE_SUB_SYSTEM_MODIFY = new Long(13);// ��ϵͳ�޸�
	public static final Long CHANGE_TYPE_HOLIDAY_TYPE_ADD = new Long(14);// �ڼ�����������
	public static final Long CHANGE_TYPE_HOLIDAY_ADD = new Long(16);// �ڼ�������
	public static final Long CHANGE_TYPE_HOLIDAY_TYPE_DELETE = new Long(15);// �ڼ�������ɾ��
	public static final Long CHANGE_TYPE_HOLIDAY_TYPE_MODIFY = new Long(18);// �ڼ��������޸�
	public static final Long CHANGE_TYPE_HOLIDAY_MODIFY = new Long(19);// �ڼ����޸�
	public static final Long CHANGE_TYPE_HOLIDAY_DELETE = new Long(17);// �ڼ���ɾ��

	public static final Map USER_ROLE_TYPE_DESC_MAP = new LinkedHashMap();
	public static final Map OWN_USER_ROLE_TYPE_DESC_MAP = new LinkedHashMap();
	public static final Map ROLE_RES_TYPE_DESC_MAP = new LinkedHashMap();
	public static final Map OWN_ROLE_RES_TYPE_DESC_MAP = new LinkedHashMap();
	public static final Map AUDIT_STATUS_MAP = new LinkedHashMap();
	public static final Map TYPE_DESC_MAP = new LinkedHashMap();
	static {
		USER_ROLE_TYPE_DESC_MAP.put(new Long(0), "");
		USER_ROLE_TYPE_DESC_MAP.put(new Long(AuditBase.USER_ROLE_CHANGE_STATUS_NORMAL), "δ��");
		USER_ROLE_TYPE_DESC_MAP.put(new Long(AuditBase.USER_ROLE_CHANGE_STATUS_ADD), "����");
		USER_ROLE_TYPE_DESC_MAP.put(new Long(AuditBase.USER_ROLE_CHANGE_STATUS_DELETE), "ɾ��");
		USER_ROLE_TYPE_DESC_MAP.put(new Long(AuditBase.USER_ROLE_CHANGE_STATUS_ABANDON), "����");
		OWN_USER_ROLE_TYPE_DESC_MAP.put(new Long(0), "");
		OWN_USER_ROLE_TYPE_DESC_MAP.put(new Long(AuditBase.USER_ROLE_CHANGE_STATUS_NORMAL), "����");
		OWN_USER_ROLE_TYPE_DESC_MAP.put(new Long(AuditBase.USER_ROLE_CHANGE_STATUS_ADD), "������");
		OWN_USER_ROLE_TYPE_DESC_MAP.put(new Long(AuditBase.USER_ROLE_CHANGE_STATUS_DELETE), "��ɾ��");
		OWN_USER_ROLE_TYPE_DESC_MAP.put(new Long(AuditBase.USER_ROLE_CHANGE_STATUS_ABANDON), "����");
		ROLE_RES_TYPE_DESC_MAP.put(new Long(0), "");
		ROLE_RES_TYPE_DESC_MAP.put(new Long(AuditBase.ROLE_RES_CHANGE_STATUS_NORMAL), "δ��");
		ROLE_RES_TYPE_DESC_MAP.put(new Long(AuditBase.ROLE_RES_CHANGE_STATUS_ADD), "����");
		ROLE_RES_TYPE_DESC_MAP.put(new Long(AuditBase.ROLE_RES_CHANGE_STATUS_DELETE), "ɾ��");
		ROLE_RES_TYPE_DESC_MAP.put(new Long(AuditBase.ROLE_RES_CHANGE_STATUS_ABANDON), "����");
		OWN_ROLE_RES_TYPE_DESC_MAP.put(new Long(0), "");
		OWN_ROLE_RES_TYPE_DESC_MAP.put(new Long(AuditBase.ROLE_RES_CHANGE_STATUS_NORMAL), "����");
		OWN_ROLE_RES_TYPE_DESC_MAP.put(new Long(AuditBase.ROLE_RES_CHANGE_STATUS_ADD), "������");
		OWN_ROLE_RES_TYPE_DESC_MAP.put(new Long(AuditBase.ROLE_RES_CHANGE_STATUS_DELETE), "��ɾ��");
		OWN_ROLE_RES_TYPE_DESC_MAP.put(new Long(AuditBase.ROLE_RES_CHANGE_STATUS_ABANDON), "����");
		AUDIT_STATUS_MAP.put(new Long(AUDIT_STATUS_NOADUITED), "δ��");
		AUDIT_STATUS_MAP.put(new Long(AUDIT_STATUS_APPROVED), "ͨ��");
		AUDIT_STATUS_MAP.put(new Long(AUDIT_STATUS_REJECTED), "��ͨ��");
		AUDIT_STATUS_MAP.put(new Long(AUDIT_STATUS_CANCEL), "����");
		AUDIT_STATUS_MAP.put(new Long(AUDIT_STATUS_HASAUDIT), "����");
		TYPE_DESC_MAP.put(new Long(0), "ȫ��");
		TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_USER_ADD, "�û�����");
		TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_USER_MODIFY, "�û��޸�");
		TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_USER_DELETE, "�û�ɾ��");
		TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_USER2ROLE, "�û���ɫ�޸�");
		// TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_ROLE2USER, "�û���ɫ�޸�");
		TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_ROLE2RESOURCE, "��ɫ��Դ�޸�");
		TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_INST_MODIFY, "�����޸�");
		TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_SYS_PARAM_MODIFY, "ϵͳ�����޸�");
		TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_SUB_SYSTEM_ADMIN_MODIFY, "��ϵͳ����Ա�޸�");
		TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_SUB_SYSTEM_MODIFY, "��ϵͳ�޸�");
		TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_HOLIDAY_TYPE_ADD, "�ڼ��ձ��");
		//TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_HOLIDAY_TYPE_MODIFY, "�ڼ����޸�");
		//TYPE_DESC_MAP.put(AuditBase.CHANGE_TYPE_HOLIDAY_TYPE_DELETE, "�ڼ���ɾ��");
	}

	/**
	 * ���id����������
	 */
	public static final String AUDIT = "AUD";

	protected String[] columnFields;

	protected String[] valueFields;

	protected LoginDO login;

	/**
	 * ���
	 * 
	 * @param id
	 */
	public void audit(Object id) {
		if (id instanceof Integer || id instanceof Long || id instanceof String) {
			flush2Prime(id);
		} else {
			throw new AuditException("invaid id");
		}
	}

	/**
	 * �޸ı���
	 * 
	 * @param obj
	 */
	public void save(Object o) {
		onSave(o);
	}

	public abstract void auditThis(Object o);

	public abstract void flush2Prime(Object o);

	public abstract void onSave(Object o);

	public String[] getColumnFields() {
		return columnFields;
	}

	public void setColumnFields(String[] columnFields) {
		this.columnFields = columnFields;
	}

	public String[] getValueFields() {
		return valueFields;
	}

	public void setValueFields(String[] valueFields) {
		this.valueFields = valueFields;
	}

	public static JdbcDaoAccessor getDao() {
		return dao;
	}

	public void setLogin(LoginDO login) {
		this.login = login;
	}

	/**
	 * �޶���ΧΪ��ǰ��¼�û�ӵ�е���ϵͳ��Ӧ��ȫ����ɫ
	 */
	public static String appendHaveSystemAuthsRangeSQL(LoginDO args0) {
		Assert.notNull(args0, "��¼��������Ϊ��");
		UBaseUserDO u = new UBaseUserDO();
		u.setUserId(args0.getUserId());
		AuthRoleService authRoleService = (AuthRoleService) SpringContextUtils.getBean("authRoleService");
		Assert.notNull(authRoleService, "authRoleService����Ϊ��");
		List sysList = authRoleService.getBaseConfig(u);
		List l = new ArrayList();
		if (CollectionUtils.isNotEmpty(sysList)) {
			for (Iterator iterator = sysList.iterator(); iterator.hasNext();) {
				UBaseConfigDO o = (UBaseConfigDO) iterator.next();
				l.add(o.getSystemId());
			}
		}
		String rangeSQL = " and role_id in(select role_id from u_auth_role where system_id in ('"
				+ StringUtils.join(l.iterator(), "','") + "'))";
		if (LogFactory.getLog(AuditBase.class).isDebugEnabled())
			LogFactory.getLog(AuditBase.class).debug("rangeSQL is :" + rangeSQL);
		return l.size() > 0 ? rangeSQL : "";
	}
	
	/**
	 * �޶���ΧΪ��ǰ��¼�û�ӵ�е���ϵͳ��Ӧ��ȫ����ɫ
	 */
	public static String appendHaveSystemAuthsRangeHQL(LoginDO args0) {
		Assert.notNull(args0, "��¼��������Ϊ��");
		UBaseUserDO u = new UBaseUserDO();
		u.setUserId(args0.getUserId());
		AuthRoleService authRoleService = (AuthRoleService) SpringContextUtils.getBean("authRoleService");
		Assert.notNull(authRoleService, "authRoleService����Ϊ��");
		List sysList = authRoleService.getBaseConfig(u);
		List l = new ArrayList();
		if (CollectionUtils.isNotEmpty(sysList)) {
			for (Iterator iterator = sysList.iterator(); iterator.hasNext();) {
				UBaseConfigDO o = (UBaseConfigDO) iterator.next();
				l.add(o.getSystemId());
			}
		}
		String rangeSQL = " and roleId in(select roleId from UAuthRoleDO where systemId in ('"
				+ StringUtils.join(l.iterator(), "','") + "'))";
		if (LogFactory.getLog(AuditBase.class).isDebugEnabled())
			LogFactory.getLog(AuditBase.class).debug("rangeHQL is :" + rangeSQL);
		return l.size() > 0 ? rangeSQL : "";
	}

}
