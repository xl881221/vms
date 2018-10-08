package fmss.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

import fmss.action.base.AuditBase;
import fmss.action.base.AuditException;
import fmss.action.base.UserChangingService;
import fmss.action.entity.UBaseConfigChangeDO;
import fmss.action.entity.UBaseInstChangeDO;
import fmss.action.entity.UserChangeDO;
import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UAuthRoleDO;
import fmss.dao.entity.UBaseDictionaryDO;
import fmss.dao.entity.UBaseHolidayDO;
import fmss.dao.entity.UBaseHolidayTypeDO;
import fmss.dao.entity.UBaseInstDO;
import fmss.dao.entity.UBaseSysLogDO;
import fmss.dao.entity.UBaseUserDO;
import fmss.common.cache.CacheManager;
import fmss.services.AuthorityNameFetcher;
import fmss.services.DictionaryService;
import fmss.services.HolidayService;
import fmss.services.InstService;
import fmss.services.ParamConfigService;
import fmss.services.UBaseSysLogService;
import fmss.common.util.Constants;
import fmss.common.util.PaginationList;

public class UserChangeAuditAction extends BaseAction {
	public static final int NOT_AUDIT_RECORD = -1;
	public static final Map OPERATION_DESC_MAP = new LinkedHashMap();

	static {
		OPERATION_DESC_MAP.put(new Long(AuditBase.AUDIT_STATUS_APPROVED), "审核成功");
		OPERATION_DESC_MAP.put(new Long(AuditBase.AUDIT_STATUS_CANCEL), "撤回成功");
		OPERATION_DESC_MAP.put(new Long(AuditBase.AUDIT_STATUS_REJECTED), "驳回成功");
		OPERATION_DESC_MAP.put(new Long(NOT_AUDIT_RECORD), "当前没有您可以审核的记录");
	}

	public Map getSELECT_TYPE_MAP() {
		Map map = new LinkedHashMap();
		for (Iterator iterator = AuditBase.TYPE_DESC_MAP.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry entry = (Map.Entry) iterator.next();
			map.put(entry.getKey(), entry.getValue());
		}
		if ("0".equals(cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_ADD_AUDIT))) {
			// 用户新增审核关闭
			map.remove(AuditBase.CHANGE_TYPE_USER_ADD);
		}
		return map;
	}

	private static final String MENU_CODE = "0002.0006";

	private static final String MENU_NAME = "基础信息管理.权限变更审核";
	
	final String DIC_TYPE_INSTLAYER = "LEVEL";		//机构级别字典类型

	/**
	 * 
	 */
	private static final long serialVersionUID = 4184454248353918664L;

	private UserChangingService userChangingService;

	private UBaseSysLogService sysLogService;
	
	private HolidayService holidayService;

	private CacheManager cacheManager; // 缓存

	/* parameters */
	private Long id;
	
	private String holidayType;

	private Long changeType;

	private String userId;

	private String roleId;
	
	private String systemId;
	
	private String isShowAllUser;
	
	private List instList; 							// 机构列表
	
	private List instLevList; 						// 机构级别列表
	
	/** 字典信息服务类*/
	private DictionaryService dicService;

	private UserChangeDO user = new UserChangeDO();
	
	private UBaseInstChangeDO inst = new UBaseInstChangeDO(); 	// 创建机构change对象实例
	
	private UBaseInstChangeDO newInst =new UBaseInstChangeDO();
	
	private UBaseInstDO  oldInst = new  UBaseInstChangeDO();
	
	/** 机构信息服务类*/
	private InstService instService;
	
	private ParamConfigService paramConfigService;

	private UBaseUserDO originalUser = new UBaseUserDO();

	private List roleChanges = new ArrayList();

	private List resourceInstChanges = new ArrayList();

	private List resourceMenuChanges = new ArrayList();

	private List resourceReportChanges = new ArrayList();

	private LoginDO currentUser = null;

	private List roleBelongToUsers = new ArrayList();

	private Boolean showAuditColumn = new Boolean(false);

	private String operationMessage = null;
	
	private String oldFRegion = null;
	private String oldMRegion = null;
	private String oldLRegion = null;
	private String newFRegion = null;
	private String newMRegion = null;
	private String newLRegion = null;

	private int operationType = 0;

	private PaginationList paginationList;
	
	/** 参数map,key为子系统名*/
	private Map paramMaps;
	
	/** 选中的tab页*/
	private String selectTab;
	
	UBaseConfigChangeDO subSystem =null;

	/* parameters */

	public UBaseConfigChangeDO getSubSystem() {
		return subSystem;
	}

	public void setSubSystem(UBaseConfigChangeDO subSystem) {
		this.subSystem = subSystem;
	}

	public String getOperationMessage() {
		return operationMessage;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getSelectTab() {
		return selectTab;
	}

	public void setSelectTab(String selectTab) {
		this.selectTab = selectTab;
	}

	public Map getParamMaps() {
		return paramMaps;
	}

	public void setParamMaps(Map paramMaps) {
		this.paramMaps = paramMaps;
	}

	public ParamConfigService getParamConfigService() {
		return paramConfigService;
	}

	public void setParamConfigService(ParamConfigService paramConfigService) {
		this.paramConfigService = paramConfigService;
	}

	public void setOperationMessage(String operationMessage) {
		this.operationMessage = operationMessage;
	}

	public int getOperationType() {
		return operationType;
	}

	public void setOperationType(int operationType) {
		this.operationType = operationType;
	}

	public List getRoleBelongToUsers() {
		return roleBelongToUsers;
	}

	public List getRoleChanges() {
		return roleChanges;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UBaseInstChangeDO getNewInst() {
		return newInst;
	}

	public void setNewInst(UBaseInstChangeDO newInst) {
		this.newInst = newInst;
	}

	public UBaseInstDO getOldInst() {
		return oldInst;
	}

	public void setOldInst(UBaseInstDO oldInst) {
		this.oldInst = oldInst;
	}

	public void setUserChangingService(UserChangingService userChangingService) {
		this.userChangingService = userChangingService;
	}

	public String getOldFRegion() {
		return oldFRegion;
	}

	public void setOldFRegion(String oldFRegion) {
		this.oldFRegion = oldFRegion;
	}

	public String getOldMRegion() {
		return oldMRegion;
	}

	public void setOldMRegion(String oldMRegion) {
		this.oldMRegion = oldMRegion;
	}

	public String getOldLRegion() {
		return oldLRegion;
	}

	public void setOldLRegion(String oldLRegion) {
		this.oldLRegion = oldLRegion;
	}

	public String getNewFRegion() {
		return newFRegion;
	}

	public void setNewFRegion(String newFRegion) {
		this.newFRegion = newFRegion;
	}

	public String getNewMRegion() {
		return newMRegion;
	}

	public void setNewMRegion(String newMRegion) {
		this.newMRegion = newMRegion;
	}

	public String getNewLRegion() {
		return newLRegion;
	}

	public void setNewLRegion(String newLRegion) {
		this.newLRegion = newLRegion;
	}

	public String listAllAuditWorks() throws Exception {
		boolean isNotShowOwnChangedRecord = true;// TODO mark
		try {
			if (paginationList == null)
				paginationList = new PaginationList();
			// paginationList.setCurrentPage(this.curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			// 1021修改为过滤所有自己提交的修改。
			paginationList = userChangingService.getFullAuditInfos(login, new Long(AuditBase.AUDIT_STATUS_NOADUITED),
					null, null, changeType, paginationList, isNotShowOwnChangedRecord ? login.getUserId() : null, true,null);
			this.operationMessage = (String) OPERATION_DESC_MAP.get(new Long(operationType));
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String approveUserAdd() throws Exception {
		try {
			if (id == null) {
				return INPUT;
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userChangingService.updateUserAudit(login, AuditBase.CHANGE_TYPE_USER_ADD, id,
					AuditBase.AUDIT_STATUS_APPROVED);
			this.operationType = AuditBase.AUDIT_STATUS_APPROVED;

			UserChangeDO u = this.userChangingService.getUserChanges(id);
			login.setDescription("新增用户[" + AuthorityNameFetcher.fetchUserName(u.getUserId()) + "]审核通过");
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId(MENU_CODE);
			sysLog.setMenuName(MENU_NAME);
			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String rejectUserAdd() throws Exception {
		try {
			if (id == null) {
				return INPUT;
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userChangingService.updateUserAudit(login, AuditBase.CHANGE_TYPE_USER_ADD, id,
					AuditBase.AUDIT_STATUS_REJECTED);
			this.operationType = AuditBase.AUDIT_STATUS_REJECTED;

			UserChangeDO u = this.userChangingService.getUserChanges(id);
			login.setDescription("新增用户[" + AuthorityNameFetcher.fetchUserName(u.getUserId()) + "]审核不通过");
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId(MENU_CODE);
			sysLog.setMenuName(MENU_NAME);
			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String approveUserModify() throws Exception {
		try {
			if (id == null) {
				return INPUT;
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userChangingService.updateUserAudit(login, AuditBase.CHANGE_TYPE_USER_MODIFY, id,
					AuditBase.AUDIT_STATUS_APPROVED);
			this.operationType = AuditBase.AUDIT_STATUS_APPROVED;

			UserChangeDO u = this.userChangingService.getUserChanges(id);
			login.setDescription("修改用户[" + AuthorityNameFetcher.fetchUserName(u.getUserId()) + "]" + (u.getChangeRemark() != null ? u.getChangeRemark() : "") + "审核通过");
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId(MENU_CODE);
			sysLog.setMenuName(MENU_NAME);
			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String rejectUserModify() throws Exception {
		try {
			if (id == null) {
				return INPUT;
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userChangingService.updateUserAudit(login, AuditBase.CHANGE_TYPE_USER_MODIFY, id,
					AuditBase.AUDIT_STATUS_REJECTED);
			this.operationType = AuditBase.AUDIT_STATUS_REJECTED;

			UserChangeDO u = this.userChangingService.getUserChanges(id);
			login.setDescription("修改用户[" + AuthorityNameFetcher.fetchUserName(u.getUserId()) + "]" + (u.getChangeRemark() != null ? u.getChangeRemark() : "") + "审核不通过");
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId(MENU_CODE);
			sysLog.setMenuName(MENU_NAME);
			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String approveUserDelete() throws Exception {
		try {
			if (id == null) {
				return INPUT;
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userChangingService.updateUserAudit(login, AuditBase.CHANGE_TYPE_USER_DELETE, id,
					AuditBase.AUDIT_STATUS_APPROVED);
			this.operationType = AuditBase.AUDIT_STATUS_APPROVED;

			UserChangeDO u = this.userChangingService.getUserChanges(id);
			login.setDescription("删除用户[" + AuthorityNameFetcher.fetchUserName(u.getUserId()) + "]审核通过");
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId(MENU_CODE);
			sysLog.setMenuName(MENU_NAME);
			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String rejectUserDelete() throws Exception {
		try {
			if (id == null) {
				return INPUT;
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userChangingService.updateUserAudit(login, AuditBase.CHANGE_TYPE_USER_DELETE, id,
					AuditBase.AUDIT_STATUS_REJECTED);
			this.operationType = AuditBase.AUDIT_STATUS_REJECTED;

			UserChangeDO u = this.userChangingService.getUserChanges(id);
			login.setDescription("删除用户[" + AuthorityNameFetcher.fetchUserName(u.getUserId()) + "]审核不通过");
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId(MENU_CODE);
			sysLog.setMenuName(MENU_NAME);
			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String approveRoleChanges() throws Exception {
		try {
			if (StringUtils.isEmpty(userId)) {
				return INPUT;
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			if (userChangingService.roleAuditCount(userId, login.getUserId(),
					new Long(AuditBase.AUDIT_STATUS_NOADUITED)) <= 0) {
				this.operationType = NOT_AUDIT_RECORD;
				return SUCCESS;
			}
			this.userChangingService.updateRoleAudit(login, AuditBase.CHANGE_TYPE_USER2ROLE, userId,
					AuditBase.AUDIT_STATUS_APPROVED);
			this.operationType = AuditBase.AUDIT_STATUS_APPROVED;

			login.setDescription("用户[" + AuthorityNameFetcher.fetchUserName(userId) + "]角色修改审核通过");
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId(MENU_CODE);
			sysLog.setMenuName(MENU_NAME);
			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String rejectRoleChanges() throws Exception {
		try {
			if (StringUtils.isEmpty(userId)) {
				return INPUT;
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			if (userChangingService.roleAuditCount(userId, login.getUserId(),
					new Long(AuditBase.AUDIT_STATUS_NOADUITED)) <= 0) {
				this.operationType = NOT_AUDIT_RECORD;
				return SUCCESS;
			}
			this.userChangingService.updateRoleAudit(login, AuditBase.CHANGE_TYPE_USER2ROLE, userId,
					AuditBase.AUDIT_STATUS_REJECTED);
			this.operationType = AuditBase.AUDIT_STATUS_REJECTED;

			login.setDescription("用户[" + AuthorityNameFetcher.fetchUserName(userId) + "]角色修改审核不通过");
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId(MENU_CODE);
			sysLog.setMenuName(MENU_NAME);
			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String approveResourceChanges() throws Exception {
		try {
			if (StringUtils.isEmpty(roleId)) {
				return INPUT;
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			if (userChangingService.resourceAuditCount(roleId, login.getUserId(), new Long(
					AuditBase.AUDIT_STATUS_NOADUITED)) <= 0) {
				this.operationType = NOT_AUDIT_RECORD;
				return SUCCESS;
			}
			this.userChangingService.updateResourceAudit(login, AuditBase.CHANGE_TYPE_ROLE2RESOURCE, roleId,
					AuditBase.AUDIT_STATUS_APPROVED);
			this.operationType = AuditBase.AUDIT_STATUS_APPROVED;

			UAuthRoleDO r = userChangingService.getRole(roleId);
			login.setDescription("角色[" + (r != null ? r.getRoleName() : "NULL") + "]资源修改审核通过");
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId(MENU_CODE);
			sysLog.setMenuName(MENU_NAME);
			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}
	
	//审核通过节假日
	public String approveHolidayChanges() throws Exception {
		try {
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			List listHolidayChange = userChangingService.getHolidayChanges(holidayType);
			List listHolidayAddChange = userChangingService.getAddHolidayChanges(holidayType);
			List listHolidayRemoveChange = userChangingService.getRemoveHolidayChanges(holidayType);
			if (id!=null) {
				List list = userChangingService.getHolidayTypeChangesById(id);
				if(list!=null&&list.size()>0){
					Map map = (Map)list.get(0);
					//审核删除时
					if(map.get("CHANGE_STATUS").toString().equals(AuditBase.CHANGE_TYPE_HOLIDAY_TYPE_DELETE+"")){
						this.userChangingService.updateHolidayAudit(login, AuditBase.CHANGE_TYPE_HOLIDAY_TYPE_ADD, id, AuditBase.AUDIT_STATUS_APPROVED,holidayType);
						holidayService.deteteHolidayType(holidayType);
						return SUCCESS;
					}
					UBaseHolidayTypeDO holidayTypeDO = new UBaseHolidayTypeDO();
					holidayTypeDO.setEnable(map.get("ENABLE").toString());
					holidayTypeDO.setHolidayName(map.get("HOLIDAY_NAME").toString());
					holidayTypeDO.setHolidayType(map.get("HOLIDAY_TYPE").toString());
					if(map.get("REMARK")!=null){
						holidayTypeDO.setRemark(map.get("REMARK").toString());
					}
					UBaseHolidayTypeDO uht = holidayService.getHolidayType(holidayType);
					if(uht!=null){
						holidayService.update(holidayTypeDO);
					}else{
						holidayService.save(holidayTypeDO);
					}
				}
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			this.userChangingService.updateHolidayAudit(login, AuditBase.CHANGE_TYPE_HOLIDAY_TYPE_ADD, id, AuditBase.AUDIT_STATUS_APPROVED,holidayType);
			this.operationType = AuditBase.AUDIT_STATUS_APPROVED;
			if(listHolidayChange!=null&&listHolidayChange.size()>0){
				List listHoliday = holidayService.getHolidays(holidayType);
				List listHolidayAfterAudit = new ArrayList();
				if(listHoliday!=null&&listHoliday.size()>0){
					for(int j=0;j<listHoliday.size();j++){
						UBaseHolidayDO ubhdo = (UBaseHolidayDO)listHoliday.get(j);
						listHolidayAfterAudit.add(ubhdo.getHolidayValue());
					}
				}
				if(listHolidayAddChange!=null&&listHolidayAddChange.size()>0){
					for(int j=0;j<listHolidayAddChange.size();j++){
						Map map = (Map)listHolidayAddChange.get(j);
						if(map.get("HOLIDAY_VALUE")!=null){
							listHolidayAfterAudit.add(map.get("HOLIDAY_VALUE").toString());
						}
					}
				}
				if(listHolidayRemoveChange!=null&&listHolidayRemoveChange.size()>0){
					for(int j=0;j<listHolidayRemoveChange.size();j++){
						Map map = (Map)listHolidayRemoveChange.get(j);
						if(map.get("HOLIDAY_VALUE")!=null){
							listHolidayAfterAudit.remove(map.get("HOLIDAY_VALUE").toString());
						}
					}
				}
				String holidays[]=new String[listHolidayAfterAudit.size()];
				for(int i=0;i<listHolidayAfterAudit.size();i++){
					if(listHolidayAfterAudit.get(i)!=null){
						holidays[i]=(String)listHolidayAfterAudit.get(i);
					}
				}
				//此处判断CHANGE表中的日期，哪些是添加的节假日，哪些是取消的节假日，分别处理。
				holidayService.saveBatchHolidays(holidays,holidayType);
			}
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}
	
	//审核不通过节假日
	public String rejectHolidayChanges() throws Exception {
		try {
			/*if (id==null) {
				return INPUT;
			}*/
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userChangingService.updateHolidayAudit(login, AuditBase.CHANGE_TYPE_HOLIDAY_TYPE_ADD, id, AuditBase.AUDIT_STATUS_REJECTED,holidayType);
			this.operationType = AuditBase.AUDIT_STATUS_REJECTED;
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String approveInstAddChanges() throws Exception {
		try {
			if (StringUtils.isEmpty(roleId)) {
				return INPUT;
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			if (userChangingService.instAuditCount(roleId, login.getUserId(), new Long(
					AuditBase.AUDIT_STATUS_NOADUITED)) <= 0) {
				this.operationType = NOT_AUDIT_RECORD;
				return SUCCESS;
			}
			this.userChangingService.updateUBaseInstChangeAudit(login, AuditBase.CHANGE_TYPE_INST_ADD, roleId,
					AuditBase.AUDIT_STATUS_APPROVED);
			this.operationType = AuditBase.AUDIT_STATUS_APPROVED;

//			UAuthRoleDO r = userChangingService.getRole(roleId);
//			login.setDescription("角色[" + (r != null ? r.getRoleName() : "NULL") + "]资源修改审核通过");
//			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
//			sysLog.setMenuId(MENU_CODE);
//			sysLog.setMenuName(MENU_NAME);
//			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}
	
	public String approveInstChanges() throws Exception {
		try {
			if (StringUtils.isEmpty(roleId)) {
				return INPUT;
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			if (userChangingService.instAuditCount(roleId, login.getUserId(), new Long(
					AuditBase.AUDIT_STATUS_NOADUITED)) <= 0) {
				this.operationType = NOT_AUDIT_RECORD;
				return SUCCESS;
			}
			this.userChangingService.updateUBaseInstChangeAudit(login, AuditBase.CHANGE_TYPE_INST_ADD, roleId,
					AuditBase.AUDIT_STATUS_APPROVED);
			this.operationType = AuditBase.AUDIT_STATUS_APPROVED;

//			UAuthRoleDO r = userChangingService.getRole(roleId);
//			login.setDescription("角色[" + (r != null ? r.getRoleName() : "NULL") + "]资源修改审核通过");
//			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
//			sysLog.setMenuId(MENU_CODE);
//			sysLog.setMenuName(MENU_NAME);
//			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}
	//审核通过系统参数审核
	public String approveSysParamChanges() throws Exception {
		try {
			if (StringUtils.isEmpty(roleId)) {
				return INPUT;
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			if (userChangingService.sysParamAuditCount(roleId, login.getUserId(), new Long(
					AuditBase.AUDIT_STATUS_NOADUITED)) <= 0) {
				this.operationType = NOT_AUDIT_RECORD;
				return SUCCESS;
			}
			this.userChangingService.updateSysParamChangeAudit(login, AuditBase.CHANGE_TYPE_SYS_PARAM_MODIFY, roleId,
					AuditBase.AUDIT_STATUS_APPROVED);
			this.operationType = AuditBase.AUDIT_STATUS_APPROVED;

//			UAuthRoleDO r = userChangingService.getRole(roleId);
//			login.setDescription("角色[" + (r != null ? r.getRoleName() : "NULL") + "]资源修改审核通过");
//			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
//			sysLog.setMenuId(MENU_CODE);
//			sysLog.setMenuName(MENU_NAME);
//			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}
	
	//审核通过增加管理员审核
	public String approveSubSystemAdminChanges() throws Exception {
		try {
			roleId =roleId +systemId ;
			if (StringUtils.isEmpty(roleId)) {
				return INPUT;
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userChangingService.updateSubSystemAdminChangeAudit(login, AuditBase.CHANGE_TYPE_SUB_SYSTEM_ADMIN_MODIFY, roleId,
					AuditBase.AUDIT_STATUS_APPROVED,id);
			this.operationType = AuditBase.AUDIT_STATUS_APPROVED;

//			UAuthRoleDO r = userChangingService.getRole(roleId);
//			login.setDescription("角色[" + (r != null ? r.getRoleName() : "NULL") + "]资源修改审核通过");
//			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
//			sysLog.setMenuId(MENU_CODE);
//			sysLog.setMenuName(MENU_NAME);
//			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}
	
	//审核通过删除管理员审核
	public String approveSubSystemAdminDelChanges() throws Exception {
		try {
			roleId =roleId +systemId ;
			if (StringUtils.isEmpty(roleId)) {
				return INPUT;
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userChangingService.updateSubSystemAdminChangeAudit(login, AuditBase.CHANGE_TYPE_SUB_SYSTEM_ADMIN_DELETE, roleId,
					AuditBase.AUDIT_STATUS_APPROVED,id);
			this.operationType = AuditBase.AUDIT_STATUS_APPROVED;

//			UAuthRoleDO r = userChangingService.getRole(roleId);
//			login.setDescription("角色[" + (r != null ? r.getRoleName() : "NULL") + "]资源修改审核通过");
//			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
//			sysLog.setMenuId(MENU_CODE);
//			sysLog.setMenuName(MENU_NAME);
//			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}
	//审核通过子系统信息修改
	public String approveUbaseCfgChanges() throws Exception {
		try {
			if (StringUtils.isEmpty(roleId)) {
				return INPUT;
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userChangingService.updateUBaseCfgChangeAudit(login, AuditBase.CHANGE_TYPE_SUB_SYSTEM_MODIFY, roleId,
					AuditBase.AUDIT_STATUS_APPROVED);
			this.operationType = AuditBase.AUDIT_STATUS_APPROVED;

		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}
	
	public String approveInstDeleteChanges() throws Exception {
		try {
			if (StringUtils.isEmpty(roleId)) {
				return INPUT;
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			if (userChangingService.instAuditCount(roleId, login.getUserId(), new Long(
					AuditBase.AUDIT_STATUS_NOADUITED)) <= 0) {
				this.operationType = NOT_AUDIT_RECORD;
				return SUCCESS;
			}
			this.userChangingService.updateUBaseInstChangeAudit(login, AuditBase.CHANGE_TYPE_INST_DELETE, roleId,
					AuditBase.AUDIT_STATUS_APPROVED);
			this.operationType = AuditBase.AUDIT_STATUS_APPROVED;

//			UAuthRoleDO r = userChangingService.getRole(roleId);
//			login.setDescription("角色[" + (r != null ? r.getRoleName() : "NULL") + "]资源修改审核通过");
//			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
//			sysLog.setMenuId(MENU_CODE);
//			sysLog.setMenuName(MENU_NAME);
//			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String rejectResourceChanges() throws Exception {
		try {
			if (StringUtils.isEmpty(roleId)) {
				return INPUT;
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			if (userChangingService.resourceAuditCount(roleId, login.getUserId(), new Long(
					AuditBase.AUDIT_STATUS_NOADUITED)) <= 0) {
				this.operationType = NOT_AUDIT_RECORD;
				return SUCCESS;
			}
			this.userChangingService.updateResourceAudit(login, AuditBase.CHANGE_TYPE_ROLE2RESOURCE, roleId,
					AuditBase.AUDIT_STATUS_REJECTED);
			this.operationType = AuditBase.AUDIT_STATUS_REJECTED;

			UAuthRoleDO r = userChangingService.getRole(roleId);
			login.setDescription("角色[" + (r != null ? r.getRoleName() : "NULL") + "]资源修改审核不通过");
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId(MENU_CODE);
			sysLog.setMenuName(MENU_NAME);
			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String rejectInstBaseChanges() throws Exception {
		try {
			if (StringUtils.isEmpty(roleId)) {
				return INPUT;
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			if (userChangingService.instAuditCount(roleId, login.getUserId(), new Long(
					AuditBase.AUDIT_STATUS_NOADUITED)) <= 0) {
				this.operationType = NOT_AUDIT_RECORD;
				return SUCCESS;
			}
			this.userChangingService.updateUBaseInstChangeAudit(login, AuditBase.CHANGE_TYPE_INST_MODIFY, roleId,
					AuditBase.AUDIT_STATUS_REJECTED);
			this.operationType = AuditBase.AUDIT_STATUS_REJECTED;

//			UAuthRoleDO r = userChangingService.getRole(roleId);
//			login.setDescription("角色[" + (r != null ? r.getRoleName() : "NULL") + "]资源修改审核不通过");
//			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
//			sysLog.setMenuId(MENU_CODE);
//			sysLog.setMenuName(MENU_NAME);
//			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}
	//撤销系统参数配置审核
	public String rejectSysParamChanges() throws Exception {
		try {
			if (StringUtils.isEmpty(roleId)) {
				return INPUT;
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			if (userChangingService.sysParamAuditCount(roleId, login.getUserId(), new Long(
					AuditBase.AUDIT_STATUS_NOADUITED)) <= 0) {
				this.operationType = NOT_AUDIT_RECORD;
				return SUCCESS;
			}
			this.userChangingService.updateSysParamChangeAudit(login, AuditBase.CHANGE_TYPE_SYS_PARAM_MODIFY, roleId,
					AuditBase.AUDIT_STATUS_REJECTED);
			this.operationType = AuditBase.AUDIT_STATUS_REJECTED;

//			UAuthRoleDO r = userChangingService.getRole(roleId);
//			login.setDescription("角色[" + (r != null ? r.getRoleName() : "NULL") + "]资源修改审核不通过");
//			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
//			sysLog.setMenuId(MENU_CODE);
//			sysLog.setMenuName(MENU_NAME);
//			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}
	
	//撤销子系配置增加管理员审核
	public String rejectSubSystemAdminChanges() throws Exception {
		try {
		
			if (StringUtils.isEmpty(roleId)) {
				return INPUT;
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			roleId =roleId +systemId ;
			this.userChangingService.updateSubSystemAdminChangeAudit(login, AuditBase.CHANGE_TYPE_SUB_SYSTEM_ADMIN_MODIFY, roleId,
					AuditBase.AUDIT_STATUS_REJECTED,id);
			this.operationType = AuditBase.AUDIT_STATUS_REJECTED;

//			UAuthRoleDO r = userChangingService.getRole(roleId);
//			login.setDescription("角色[" + (r != null ? r.getRoleName() : "NULL") + "]资源修改审核不通过");
//			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
//			sysLog.setMenuId(MENU_CODE);
//			sysLog.setMenuName(MENU_NAME);
//			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}
	
	//撤销子系配置删除管理员审核
	public String rejectSubSystemAdminDelChanges() throws Exception {
		try {
			if (StringUtils.isEmpty(roleId)) {
				return INPUT;
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			roleId =roleId +systemId ;
			this.userChangingService.updateSubSystemAdminChangeAudit(login, AuditBase.CHANGE_TYPE_SUB_SYSTEM_ADMIN_DELETE, roleId,
					AuditBase.AUDIT_STATUS_REJECTED,id);
			this.operationType = AuditBase.AUDIT_STATUS_REJECTED;

//			UAuthRoleDO r = userChangingService.getRole(roleId);
//			login.setDescription("角色[" + (r != null ? r.getRoleName() : "NULL") + "]资源修改审核不通过");
//			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "保存", "1", Constants.BASE_SYS_LOG_AUTHORITY);
//			sysLog.setMenuId(MENU_CODE);
//			sysLog.setMenuName(MENU_NAME);
//			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}
	
	//撤销子系配置删除管理员审核
	public String rejectUBaseCfgChanges() throws Exception {
		try {
			if (StringUtils.isEmpty(roleId)) {
				return INPUT;
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userChangingService.updateUBaseCfgChangeAudit(login, AuditBase.CHANGE_TYPE_SUB_SYSTEM_MODIFY, roleId,
					AuditBase.AUDIT_STATUS_REJECTED);
			this.operationType = AuditBase.AUDIT_STATUS_REJECTED;
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}
	
	public String showRoleChanges() throws Exception {
		try {
			if (userId == null) {
				return INPUT;
			}
			this.currentUser = (LoginDO) super.get(Constants.LOGIN_USER);
			this.roleChanges = this.userChangingService.getRoleChanges(new Long(AuditBase.AUDIT_STATUS_NOADUITED),
					userId, null);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String showUserChanges() throws Exception {
		try {
			if (id == null) {
				return INPUT;
			}
			this.currentUser = (LoginDO) super.get(Constants.LOGIN_USER);
			user = this.userChangingService.getUserChanges(id);
			if (user.getChangeStatus().equals(AuditBase.CHANGE_TYPE_USER_ADD)) {
				return "add";
			}
			if (user.getChangeStatus().equals(AuditBase.CHANGE_TYPE_USER_DELETE)) {
				UBaseUserDO o = this.userChangingService.getUser(user.getUserId());
				BeanUtils.copyProperties(o, user);
				return "add";
			}
			if (user.getChangeStatus().equals(AuditBase.CHANGE_TYPE_USER_MODIFY)) {
				this.originalUser = this.userChangingService.getUser(user.getUserId());
				return "modify";
			}
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String showResourceChanges() throws Exception {
		try {
			if (roleId == null) {
				return INPUT;
			}
			this.currentUser = (LoginDO) super.get(Constants.LOGIN_USER);
			this.resourceInstChanges = this.userChangingService.getResourceChanges(new Long(
					AuditBase.AUDIT_STATUS_NOADUITED), roleId, new Long(Constants.INST_RES_ID), null);
			this.resourceMenuChanges = this.userChangingService.getResourceChanges(new Long(
					AuditBase.AUDIT_STATUS_NOADUITED), roleId, new Long(Constants.MENU_RES_ID), null);
			this.resourceReportChanges = this.userChangingService.getResourceChangesWithoutPub(new Long(
					AuditBase.AUDIT_STATUS_NOADUITED), roleId, null);
			this.roleBelongToUsers = this.userChangingService.getUsers(roleId);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String showOwnRoleChanges() throws Exception {
		try {
			if (userId == null) {
				return INPUT;
			}
			this.currentUser = (LoginDO) super.get(Constants.LOGIN_USER);
			this.roleChanges = this.userChangingService.getRoleChanges(null, userId, null);
			this.showAuditColumn = new Boolean(true);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String showOwnUserChanges() throws Exception {
		try {
			if (id == null) {
				return INPUT;
			}
			this.showAuditColumn = new Boolean(true);
			this.currentUser = (LoginDO) super.get(Constants.LOGIN_USER);
			user = this.userChangingService.getUserChanges(id);
			if (user.getChangeStatus().equals(AuditBase.CHANGE_TYPE_USER_ADD)) {
				return "add";
			}
			if (user.getChangeStatus().equals(AuditBase.CHANGE_TYPE_USER_DELETE)) {
				return "add";
			}
			if (user.getChangeStatus().equals(AuditBase.CHANGE_TYPE_USER_MODIFY)) {
				this.originalUser = this.userChangingService.getUser(user.getUserId());
				return "modify";
			}
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String showOwnResourceChanges() throws Exception {
		try {
			if (roleId == null) {
				return INPUT;
			}
			this.currentUser = (LoginDO) super.get(Constants.LOGIN_USER);
			this.resourceInstChanges = this.userChangingService.getResourceChanges(null, roleId, new Long(
					Constants.INST_RES_ID), null);
			this.resourceMenuChanges = this.userChangingService.getResourceChanges(null, roleId, new Long(
					Constants.MENU_RES_ID), null);
			this.resourceReportChanges = this.userChangingService.getResourceChangesWithoutPub(null, roleId, null);
			this.roleBelongToUsers = this.userChangingService.getUsers(roleId);
			this.showAuditColumn = new Boolean(true);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String showOwnUBaseInstChanges() throws Exception {
		try{
			inst = (UBaseInstChangeDO) instService.getInstcByInstId(roleId);
			InitPage();
			return "inst";
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}
	
	public String showModifyUBaseInstChanges() throws Exception {
		try{
			newInst = (UBaseInstChangeDO) instService.getInstcByInstId(roleId);
			oldInst = (UBaseInstDO) instService.getInstByInstId(roleId);
			String lastNResion = newInst.getInstRegion() ;
			String lastOResion = oldInst.getInstRegion() ;
			String mnResion = newInst.getInstRegion().substring(0,4)+"00" ;
			String fnResion = newInst.getInstRegion().substring(0,2)+"0000" ;
			String moResion = oldInst.getInstRegion().substring(0,4)+"00" ;
			String foResion = oldInst.getInstRegion().substring(0,2)+"0000" ;
			List listLast = dicService.find("select udb from UBaseDictionaryDO udb where udb.dicValue=?",lastNResion) ;
			List listF = dicService.find("select udb from UBaseDictionaryDO udb where  udb.dicValue=?",fnResion) ;
			List listM = dicService.find("select udb from UBaseDictionaryDO udb where  udb.dicValue=?",mnResion) ;
			List listoLast = dicService.find("select udb from UBaseDictionaryDO udb where udb.dicValue=?",lastOResion) ;
			List listoF = dicService.find("select udb from UBaseDictionaryDO udb where  udb.dicValue=?",foResion) ;
			List listoM = dicService.find("select udb from UBaseDictionaryDO udb where  udb.dicValue=?",moResion) ;
			UBaseDictionaryDO uBaseDictionaryDO = (UBaseDictionaryDO)listLast.get(0) ;
			newLRegion = uBaseDictionaryDO.getDicName() ;
			uBaseDictionaryDO = (UBaseDictionaryDO)listM.get(0) ;
			newMRegion = uBaseDictionaryDO.getDicName() ;
			uBaseDictionaryDO = (UBaseDictionaryDO)listF.get(0) ;
			newFRegion = uBaseDictionaryDO.getDicName() ;
			uBaseDictionaryDO = (UBaseDictionaryDO)listoF.get(0) ;
			oldFRegion = uBaseDictionaryDO.getDicName() ;
			uBaseDictionaryDO = (UBaseDictionaryDO)listoM.get(0) ;
			oldMRegion = uBaseDictionaryDO.getDicName() ;
			uBaseDictionaryDO = (UBaseDictionaryDO)listoLast.get(0) ;
			oldLRegion = uBaseDictionaryDO.getDicName() ;
			InitPage();
			return "modify";
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}
	public String showOwnUBaseCfgChanges() throws Exception {
		try{
			if(roleId!=null&&!"".equals(roleId)){
			subSystem=userChangingService.getBaseCfgChangeById(roleId);
			return SUCCESS;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}
	
	public String showOwnSysParamChanges() throws Exception {
		try{
			this.paramMaps = this.paramConfigService.getParamsOneMap("00003");
			// 取默认选中项
			if(StringUtils.isEmpty(this.selectTab)){
				this.selectTab="00003";
			}
			return SUCCESS;
		}catch (Exception e){
			e.printStackTrace();
		}
		return ERROR;
	}
	
	/**
	* <p>方法名称: InitPage|描述: 初始化页面信息</p>
	* @return instList 上级机构列表
	* @return instLevList 机构级别列表
	*/
	private void InitPage(){
		InitInstList();
		instLevList = dicService.getDictoryByDicType(DIC_TYPE_INSTLAYER);
//		instSystemReal= instService.getSystemRela();
//		String bankId= inst.getInstId();
//		VcrmsSystemRelaDO vcDo=(VcrmsSystemRelaDO) instSystemReal.get(0);
//		String systemId= vcDo.getSystemId();
//		vSystemId=systemId;
//		instEmailAddrs= instService.getEmailAddrs(systemId);
//		if(StringUtils.isNotBlank(bankId)){
//			selectedEmailAddrs= instService.getSelectedEmailAddrs(systemId,bankId);
//		}
//		createJsonp(instEmailAddrs,selectedEmailAddrs);
	}
	/**
	* <p>方法名称: InitInstList|描述: 初始化上级机构列表，并增加空行</p> 
	* @return instList 用于选择上级机构的机构列表
	*/
	private void InitInstList(){
		instList = instService.getAllInsts();
		UBaseInstDO ui = new UBaseInstDO();
		ui.setInstId("");
		ui.setInstName("");
		instList.add(0, ui);
	}
	/** ************************workbench*********************************** */

	/**
	 * 获取审核工作
	 * 
	 * @return
	 * @throws Exception
	 */
	public String listMyAuditWorks() throws Exception {
		try {
			if (paginationList == null)
				paginationList = new PaginationList();
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
 			
			this.isShowAllUser = request.getParameter("PARAM_SYS_SAFETY"); //判断是否显示全部的用户
			paginationList = userChangingService.getFullAuditInfos(login, null, login.getUserId(), login.getUserId(),changeType, paginationList, null, false,isShowAllUser);
 			this.operationMessage = (String) OPERATION_DESC_MAP.get(new Long(operationType));
 		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String cancelRoleChanges() throws Exception {
		try {
			if (userId == null) {
				throw new AuditException("用户id为空");
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userChangingService.updateRoleAudit(login, AuditBase.CHANGE_TYPE_USER2ROLE, userId,
					AuditBase.AUDIT_STATUS_CANCEL);
			this.operationType = AuditBase.AUDIT_STATUS_CANCEL;

			// TODO
			login.setDescription("撤销用户[" + AuthorityNameFetcher.fetchUserName(userId) + "]角色审核");
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "撤销", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId(MENU_CODE);
			sysLog.setMenuName(MENU_NAME);
			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String cancelUserAdd() throws Exception {
		try {
			if (id == null) {
				throw new AuditException("id为空");
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userChangingService.updateUserAudit(login, AuditBase.CHANGE_TYPE_USER_ADD, id,
					AuditBase.AUDIT_STATUS_CANCEL);
			this.operationType = AuditBase.AUDIT_STATUS_CANCEL;
			// TODO
			UserChangeDO u = this.userChangingService.getUserChanges(id);
			login.setDescription("撤销新增用户[" + AuthorityNameFetcher.fetchUserName(u.getUserId()) + "]审核");
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "撤销", "1", Constants.BASE_SYS_LOG_BASEINFO);
			sysLog.setMenuId(MENU_CODE);
			sysLog.setMenuName(MENU_NAME);
			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String cancelUserModify() throws Exception {
		try {
			if (id == null) {
				throw new AuditException("id为空");
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userChangingService.updateUserAudit(login, AuditBase.CHANGE_TYPE_USER_MODIFY, id,
					AuditBase.AUDIT_STATUS_CANCEL);
			this.operationType = AuditBase.AUDIT_STATUS_CANCEL;
			// TODO
			UserChangeDO u = this.userChangingService.getUserChanges(id);
			login.setDescription("撤销修改用户[" + AuthorityNameFetcher.fetchUserName(u.getUserId()) + "]" + (u.getChangeRemark() != null ? u.getChangeRemark() : "") + "审核");
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "撤销", "1", Constants.BASE_SYS_LOG_BASEINFO);
			sysLog.setMenuId(MENU_CODE);
			sysLog.setMenuName(MENU_NAME);
			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String cancelUserDelete() throws Exception {
		try {
			if (id == null) {
				throw new AuditException("id为空");
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userChangingService.updateUserAudit(login, AuditBase.CHANGE_TYPE_USER_DELETE, id,
					AuditBase.AUDIT_STATUS_CANCEL);
			this.operationType = AuditBase.AUDIT_STATUS_CANCEL;

			// TODO
			UserChangeDO u = this.userChangingService.getUserChanges(id);
			login.setDescription("撤销删除用户[" + AuthorityNameFetcher.fetchUserName(u.getUserId()) + "]审核");
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "撤销", "1", Constants.BASE_SYS_LOG_BASEINFO);
			sysLog.setMenuId(MENU_CODE);
			sysLog.setMenuName(MENU_NAME);
			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}

	public String cancelResourceChanges() throws Exception {
		try {
			if (roleId == null) {
				throw new AuditException("角色id为空");
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userChangingService.updateResourceAudit(login, AuditBase.CHANGE_TYPE_ROLE2RESOURCE, roleId,
					AuditBase.AUDIT_STATUS_CANCEL);
			this.operationType = AuditBase.AUDIT_STATUS_CANCEL;

			// TODO
			UAuthRoleDO r = userChangingService.getRole(roleId);
			login.setDescription("撤销角色[" + (r != null ? r.getRoleName() : "NULL") + "]资源修改审核");
			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "撤销", "1", Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId(MENU_CODE);
			sysLog.setMenuName(MENU_NAME);
			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}
  
	
	//撤销机构
	public String cancelUBaseInstChanges() throws Exception {
		try {
			if (roleId == null) {
				throw new AuditException("机构id为空");
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userChangingService.updateUBaseInstChangeAudit(login, AuditBase.CHANGE_TYPE_INST_MODIFY, roleId,
					AuditBase.AUDIT_STATUS_CANCEL);
			this.operationType = AuditBase.AUDIT_STATUS_CANCEL;

			// TODO
//			UBaseInstChangeDO r = (UBaseInstChangeDO) InstService.getInstcByInstId(instId);
//			login.setDescription("撤销角色[" + (r != null ? r.getRoleName() : "NULL") + "]资源修改审核");
//			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "撤销", "1", Constants.BASE_SYS_LOG_AUTHORITY);
//			sysLog.setMenuId(MENU_CODE);
//			sysLog.setMenuName(MENU_NAME);
//			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}
	
	//撤销系统参数
	public String cancelUBaseSysParamChanges() throws Exception {
		try {
			if (roleId == null) {
				throw new AuditException("系统参数id为空");
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userChangingService.updateSysParamChangeAudit(login, AuditBase.CHANGE_TYPE_SYS_PARAM_MODIFY, roleId,
					AuditBase.AUDIT_STATUS_CANCEL);
			this.operationType = AuditBase.AUDIT_STATUS_CANCEL;

			// TODO
//			UBaseInstChangeDO r = (UBaseInstChangeDO) InstService.getInstcByInstId(instId);
//			login.setDescription("撤销角色[" + (r != null ? r.getRoleName() : "NULL") + "]资源修改审核");
//			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "撤销", "1", Constants.BASE_SYS_LOG_AUTHORITY);
//			sysLog.setMenuId(MENU_CODE);
//			sysLog.setMenuName(MENU_NAME);
//			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}
	//撤销子配置管理员增加
	public String cancelSubSystemAdminChanges() throws Exception {
		try {
			roleId =roleId +systemId ;
			if (roleId == null) {
				throw new AuditException("子系统userid为空");
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userChangingService.updateSubSystemAdminChangeAudit(login, AuditBase.CHANGE_TYPE_SUB_SYSTEM_ADMIN_MODIFY, roleId,
					AuditBase.AUDIT_STATUS_CANCEL,id);
			this.operationType = AuditBase.AUDIT_STATUS_CANCEL;

			// TODO
//			UBaseInstChangeDO r = (UBaseInstChangeDO) InstService.getInstcByInstId(instId);
//			login.setDescription("撤销角色[" + (r != null ? r.getRoleName() : "NULL") + "]资源修改审核");
//			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "撤销", "1", Constants.BASE_SYS_LOG_AUTHORITY);
//			sysLog.setMenuId(MENU_CODE);
//			sysLog.setMenuName(MENU_NAME);
//			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}
	//撤销子配置管理员增加cancelSubSystemAdminDelChanges
	public String cancelSubSystemAdminDelChanges() throws Exception {
		try {
			roleId =roleId +systemId ;
			if (roleId == null) {
				throw new AuditException("子系统userid为空");
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userChangingService.updateSubSystemAdminChangeAudit(login, AuditBase.CHANGE_TYPE_SUB_SYSTEM_ADMIN_DELETE, roleId,
					AuditBase.AUDIT_STATUS_CANCEL,id);
			this.operationType = AuditBase.AUDIT_STATUS_CANCEL;

			// TODO
//			UBaseInstChangeDO r = (UBaseInstChangeDO) InstService.getInstcByInstId(instId);
//			login.setDescription("撤销角色[" + (r != null ? r.getRoleName() : "NULL") + "]资源修改审核");
//			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "撤销", "1", Constants.BASE_SYS_LOG_AUTHORITY);
//			sysLog.setMenuId(MENU_CODE);
//			sysLog.setMenuName(MENU_NAME);
//			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}
	//撤销子系统修改
	public String cancelUbaseCfgChanges() throws Exception {
		try {
			if (roleId == null) {
				throw new AuditException("子系统systemId为空");
			}
			if (curPage > 1)
				this.paginationList.setCurrentPage(curPage);
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			this.userChangingService.updateUBaseCfgChangeAudit(login, AuditBase.CHANGE_TYPE_SUB_SYSTEM_MODIFY, roleId,
					AuditBase.AUDIT_STATUS_CANCEL);
			this.operationType = AuditBase.AUDIT_STATUS_CANCEL;

			// TODO
//			UBaseInstChangeDO r = (UBaseInstChangeDO) InstService.getInstcByInstId(instId);
//			login.setDescription("撤销角色[" + (r != null ? r.getRoleName() : "NULL") + "]资源修改审核");
//			UBaseSysLogDO sysLog = sysLogService.setUBaseSysLog(login, "撤销", "1", Constants.BASE_SYS_LOG_AUTHORITY);
//			sysLog.setMenuId(MENU_CODE);
//			sysLog.setMenuName(MENU_NAME);
//			sysLogService.saveUBaseSysLog(sysLog);
		} catch (Exception e) {
			log.error("", e);
			throw e;
		}
		return SUCCESS;
	}
	/** ************************workbench*********************************** */

	public Long getChangeType() {
		return changeType;
	}

	public void setChangeType(Long changeType) {
		this.changeType = changeType;
	}

	public UserChangeDO getUser() {
		return user;
	}

	public void setUser(UserChangeDO user) {
		this.user = user;
	}

	public UBaseUserDO getOriginalUser() {
		return originalUser;
	}

	public void setOriginalUser(UBaseUserDO originalUser) {
		this.originalUser = originalUser;
	}

	public LoginDO getCurrentUser() {
		return currentUser;
	}

	public List getResourceInstChanges() {
		return resourceInstChanges;
	}

	public List getResourceMenuChanges() {
		return resourceMenuChanges;
	}

	public List getResourceReportChanges() {
		return resourceReportChanges;
	}

	public Boolean getShowAuditColumn() {
		return showAuditColumn;
	}

	public PaginationList getPaginationList() {
		return paginationList;
	}

	public void setPaginationList(PaginationList paginationList) {
		this.paginationList = paginationList;
	}

	public void setSysLogService(UBaseSysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}


	public InstService getInstService() {
		return instService;
	}

	public void setInstService(InstService instService) {
		this.instService = instService;
	}

	public DictionaryService getDicService() {
		return dicService;
	}

	public void setDicService(DictionaryService dicService) {
		this.dicService = dicService;
	}

	public List getInstList() {
		return instList;
	}

	public void setInstList(List instList) {
		this.instList = instList;
	}

	public List getInstLevList() {
		return instLevList;
	}

	public void setInstLevList(List instLevList) {
		this.instLevList = instLevList;
	}

	public UBaseInstChangeDO getInst() {
		return inst;
	}

	public void setInst(UBaseInstChangeDO inst) {
		this.inst = inst;
	}

	public String getHolidayType() {
		return holidayType;
	}

	public void setHolidayType(String holidayType) {
		this.holidayType = holidayType;
	}

	public void setHolidayService(HolidayService holidayService) {
		this.holidayService = holidayService;
	}

	public String getIsShowAllUser() {
		return isShowAllUser;
	}

	public void setIsShowAllUser(String isShowAllUser) {
		this.isShowAllUser = isShowAllUser;
	}
	
   
}
