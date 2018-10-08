package fmss.common.util;

/**
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: sunzhan
 * @����: 2009-6-24 ����10:13:00
 * @����: [Constants]�����ӿ�
 */
public interface Constants{

	/** LOGIN_USER ��½�û�key */
	public static final String LOGIN_USER = "LOGIN_USER";
	/** BASE_SYS_LOG_LOGIN �û���¼��־ */
	public static final String BASE_SYS_LOG_LOGIN = "00001";
	/** BASE_SYS_LOG_BASEINFO ������Ϣ������־ */
	public static final String BASE_SYS_LOG_BASEINFO = "00002";
	/** BASE_SYS_LOG_AUTHORITY Ȩ�޲�����־ */
	public static final String BASE_SYS_LOG_AUTHORITY = "00003";
	/** SYSTEM_COMMON_ID ϵͳ����ID ��ȡϵͳ������Դ��,д�� */
	public static final String SYSTEM_COMMON_ID = "00003";
	/** ϵͳ�˵����� */
	public static final String SYSTEM_MENU_TABLE = "u_base_menu";
	/** ϵͳ�������� */
	public static final String SYSTEM_INST_TABLE = "u_base_inst";
	/** ��Դ����-���� */
	public static final String PUB_RESOURCE_DIC_TYPE = "PUB";
	/** ��Դ����-˽�� */
	public static final String PRI_RESOURCE_DIC_TYPE = "PRI";
	/** ���������� */
	public static final String PAPAMETER_CACHE_MAP = "PAPAMETER_CACHE_MAP";
	/** ע����Ϣ��ʾ */
	public static final String PARAM_SYS_LOGOUT_INFO = "PARAM_SYS_LOGOUT_INFO";
	/** �������·���� */
	public static final String PARAM_THEME_PATH_CONFIG = "PARAM_SYS_THEME_PATH";
	/** �����ļ�·�� */
	public static final String PARAM_SYS_HELPFILE_PATH = "PARAM_SYS_HELPFILE_PATH";
	/** ��¼�Ƿ���֤IP */
	public static final String PARAM_SYS_LOGIN_IP_VALIDATE = "PARAM_SYS_LOGIN_IP_VALIDATE";
	/** �û��޸��Ƿ���� */
	public static final String PARAM_SYS_USER_CHANGE_AUDIT = "PARAM_SYS_USER_CHANGE_AUDIT";
	/** �û��Ƿ��߼�ɾ�� */
	public static final String PARAM_SYS_USER_DELETE_LOGIC = "PARAM_SYS_USER_DELETE_LOGIC";
	/** ����������� */
	public static final String PARAM_SYS_ISUSE = "PARAM_SYS_ISUSE";
	/** ��½ҳ�ļ���ʾ���� */
	public static final String PARAM_SYS_LOGIN_PAGE_TIPS = "PARAM_SYS_LOGIN_PAGE_TIPS";
	/** ��½ҳ��logo */
	public static final String PARAM_SYS_BIG_LOGO_PIC_NAME = "PARAM_SYS_BIG_LOGO_PIC_NAME";
	/** ��ҳСlogo */
	public static final String PARAM_SYS_SMALL_LOGO_PIC_NAME = "PARAM_SYS_SMALL_LOGO_PIC_NAME";
	/**��¼ҳ��ʾ����*/
	public static final String PARAM_SYS_TEXT_VALUE = "PARAM_SYS_TEXT_VALUE";
	/** ÿ������������ */
	public static final String PARAM_SYS_ERROR_NUM = "PARAM_SYS_ERROR_NUM";
	/** �û�ʧЧ���� */
	public static final String PARAM_SYS_USER_OVERDUE_DAYS = "PARAM_SYS_USER_OVERDUE_DAYS";
	/** �Ƿ�ʱ��ʧЧ�û��������� */
	public static final String PARAM_SYS_TIMING_USER_UPDATE = "PARAM_SYS_TIMING_USER_UPDATE";
	/** �Ƿ����û�������� */
	public static final String PARAM_SYS_USER_ADD_AUDIT = "PARAM_SYS_USER_ADD_AUDIT";
	/** �Ƿ���������� */
	public static final String PARAM_SYS_INST_AUDIT = "PARAM_SYS_INST_AUDIT";
	/** �Ƿ�������������� */
	public static final String PARAM_SYS_PARAM_CONFIG_AUDIT = "PARAM_SYS_PARAM_CONFIG_AUDIT";
	/** �Ƿ�����ϵͳ��Ϣ������� */
	public static final String PARAM_SYS_SUBSYSTEM_INFO_CONFIG_AUDIT = "PARAM_SYS_SUBSYSTEM_INFO_CONFIG_AUDIT";
	
	public static final String PARAM_SYS_INVAL_DAYS = "PARAM_SYS_INVAL_DAYS";
	/**����ʧЧ����ʱ��Ƶ��*/
	public static final String PARAM_SYS_PWD_REMIND_TIME ="PARAM_SYS_PWD_REMIND_TIME";
	
	public static final String PARAM_SYS_INITPWD = "PARAM_SYS_INITPWD";
	
	public static final String PARAM_SYS_ALLOW_SAMEUSER_LOGIN = "PARAM_SYS_ALLOW_SAMEUSER_LOGIN";
	/**��Ȩ�ޱ���Ƿ�ȫ���ɼ���-����־����ı�̬Ҫ�󣬸�˭�����ˡ�Ȩ�ޱ���鿴�����ܣ�˭�Ϳ��Կ���ȫ���������Ϣ*/
	public static final String PARAM_SYS_USER_CHANGE_SHOW_ALL = "PARAM_SYS_USER_CHANGE_SHOW_ALL";
	/**0034901: 20120210-��������-��ҳ���û���Ϣ��Ϊͨ��ϵͳ���������Ƿ���ʾ����������Ϊ��ʾʱ����ʾ�û���Ϣ��������ʾ����*/
	public static final String PARAM_SYS_USER_INFO_SHOW = "PARAM_SYS_USER_INFO_SHOW";
	
	/** �˵���Դid��д�� */
	public static final int MENU_RES_ID = 34;
	/** ������Դid��д�� */
	public static final int INST_RES_ID = 35;
	/** ϵͳ��ǰ���������session�е�key */
	public static final String SESSION_THEME_KEY = "sysTheme";
	/** ƥ���¼����������ʽ�ַ�����ʽ */
	public static final String PATTERN_NAME = "^\\w*$";
	/** ƥ���¼����������ʽ�ַ�����ʽ */
	public static final String PATTERN_PWD = "^[\\w!@#$]*$";
	
	/** �ַ� NO */
	public static final String STR_NO = "NO";	
	/** �ַ� YES */
	public static final String STR_YES = "YES";  
	public static final String SESSION_MENU_LANGUAGE = "language";
	
	/** ������ϵͳҪ��Ӧ�����󷵻����¸�ʽ��jdbc������Ϣ
	 *  {"dbDriver":"com.microsoft.sqlserver.jdbc.SQLServerDriver","dbPassword":"sa","dbUrl":"jdbc:sqlserver://bj-sa-guojz:1443;database=METABASE;","dbUserName":"sa","hibernateDialect":""}*/
	public static final String DATASOURCE_CONFIG_URL = "dataSourceConfig.ajax";
	/** �����ɹ� */
	public static final String OPERATION_SUCC = "1";
	/** ����ʧ�� */
	public static final String OPERATION_FAIL = "0";

	/**Ĭ��URL IP��ַռλ��*/
	public static final String DEFAULT_URL_IP = "$address$";
	public static final String DEFAULT_URL_IP_UNICODE = "\\u0024address\\u0024";

	/**Ĭ��URL �˿�ռλ��*/
	public static final String DEFAULT_URL_PORT = "$port$";
	public static final String DEFAULT_URL_PORT_UNICODE = "\\u0024port\\u0024";

	public static final String URL_PORT = "port";
	public static final String URL_IP = "ipAddress";
	
	public static final int SESSION_NORMAL = 0;
	public static final int SESSION_TIME_OUT = -2;
	public static final int SESSION_MUTIL_LOGIN = -1;
	
	public static final String PARAM_SYS_LANGUAGE_CHANGE = "PARAM_SYS_LANGUAGE_CHANGE";//�Ƿ������Թ��ʻ�
	public static final String PARAM_ADMIN_LOGIN = "PARAM_ADMIN_LOGIN";//�Ƿ�admin����ֱ֤�ӵ�½
	
	
	
	
	public static final String PARAM_WELCOME_STYLE = "PARAM_WELCOME_STYLE";//��ӭͼƬ�ϵ�������ʽ
	public static final String PARAM_TIME_OUT = "PARAM_TIME_OUT";//��½��ʱʱ��(��λ��)
	public static final String PARAM_REFRESH_CACHE = "PARAM_REFRESH_CACHE";//ˢ��ϵͳ����ʱ����(��Ⱥ/��λ��)
	public static final String PARAM_COLONY= "PARAM_COLONY";//�Ƿ�ʹ�ü�Ⱥ
	public static final String PARAM_CLEAR_USER= "PARAM_CLEAR_USER";//��������û�������ִ��ʱ��(hh:mm)
	public static final String PARAM_KICKOUT= "PARAM_KICKOUT";//�Ƿ���Ա���ע��
	public static final String PARAM_KICKOUT_TIME = "PARAM_KICKOUT_TIME";//����ע��ʱ��(��λ��)
	
	public static final String PARAM_USYS_INNER_URL = "PARAM_USYS_INNER_URL";//USYS������ַ
	
	
}
