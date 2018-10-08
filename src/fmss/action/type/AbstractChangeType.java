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
	
	// attributes 缓冲
	protected String typeDesc;

	protected String contentDesc;

	protected String submitDate;

	protected Long changeType;
	
	protected String cancelURL;
	
	protected Boolean canAudit = null;
	// attributes 缓冲
	
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
	 * 类型描述
	 * 
	 * @return
	 */
	public abstract String getTypeDesc();

	/**
	 * 内容描述【用户名|用户名,角色名*|角色名,资源名*】 ->【用户名|角色名】
	 * 
	 * @return
	 */
	public abstract String getContentDesc();

	/**
	 * 详细链接
	 * 
	 * @return
	 */
	public abstract String getDetailURL();

	/**
	 * 归类(用户)
	 */
	public abstract void transform();

	/**
	 * latest submit date，提交日期
	 * 
	 * @return
	 */
	public abstract String getSubmitDate();
	
	/**
	 * 通过
	 * @return
	 */
	public abstract String getApproveURL();

	/**
	 * 驳回
	 * @return
	 */
	public abstract String getRejectURL();
	
	/**
	 * 审核结果
	 * @return
	 */
	public abstract String getAuditResult();
	
	/**
	 * 撤销
	 * @return
	 */
	public abstract String getCancelURL();
	
	/**
	 * 自己的提交
	 * @return
	 */
	public abstract String getOwnDetailURL();
	
	/**
	 * 可审
	 * @return
	 */
	public abstract Boolean getCanAudit();

	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}
}
