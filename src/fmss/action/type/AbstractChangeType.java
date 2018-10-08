/**
 * 
 */
package fmss.action.type;

import java.util.Date;

import fmss.action.base.JdbcDaoAccessor;
import fmss.action.base.UserChangingService;
import fmss.dao.entity.BaseDO;
import fmss.dao.entity.LoginDO;
import fmss.common.util.SpringContextUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public abstract class AbstractChangeType {

	protected transient Log logger = LogFactory.getLog(this.getClass());

	protected UserChangingService userInfoChangingService = (UserChangingService) SpringContextUtils
			.getBean("userChangingService");

	//PROPAGATION_REQUIRED,readOnly
	protected JdbcDaoAccessor dao = (JdbcDaoAccessor) SpringContextUtils
			.getBean("jdbcDaoAccessor");
	
	protected static final String SHORT_DATE_FORMAT = "yyyy-MM-dd";
	
	protected static final String LONG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	// attributes ����
	protected String typeDesc;

	protected String contentDesc;

	protected String submitDate;

	protected Long changeType;
	
	protected String cancelURL;
	
	protected Boolean canAudit = null;
	// attributes ����
	
	protected LoginDO login;
	protected BaseDO entity;
	protected Date changeTime;
	
	public void setLogin(LoginDO login) {
		this.login = login;
	}

	public void setChangeType(Long changeType) {
		this.changeType = changeType;
	}

	public void setEntity(BaseDO entity) {
		this.entity = entity;
	}

	/**
	 * ��������
	 * 
	 * @return
	 */
	public abstract String getTypeDesc();

	/**
	 * �����������û���|�û���,��ɫ��*|��ɫ��,��Դ��*�� ->���û���|��ɫ����
	 * 
	 * @return
	 */
	public abstract String getContentDesc();

	/**
	 * ��ϸ����
	 * 
	 * @return
	 */
	public abstract String getDetailURL();

	/**
	 * ����(�û�)
	 */
	public abstract void transform();

	/**
	 * latest submit date���ύ����
	 * 
	 * @return
	 */
	public abstract String getSubmitDate();
	
	/**
	 * ͨ��
	 * @return
	 */
	public abstract String getApproveURL();

	/**
	 * ����
	 * @return
	 */
	public abstract String getRejectURL();
	
	/**
	 * ��˽��
	 * @return
	 */
	public abstract String getAuditResult();
	
	/**
	 * ����
	 * @return
	 */
	public abstract String getCancelURL();
	
	/**
	 * �Լ����ύ
	 * @return
	 */
	public abstract String getOwnDetailURL();
	
	/**
	 * ����
	 * @return
	 */
	public abstract Boolean getCanAudit();

	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}
}
