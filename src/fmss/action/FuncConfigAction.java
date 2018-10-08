package fmss.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fmss.dao.entity.LoginDO;
import fmss.dao.entity.MenuDO;
import fmss.dao.entity.UAuthRoleResourceDO;
import fmss.dao.entity.UBaseConfigDO;
import fmss.dao.entity.UBaseFuncAuthDO;
import fmss.dao.entity.UBaseFuncMenuDO;
import fmss.dao.entity.UBaseSysLogDO;
import fmss.services.AuthorityNameFetcher;
import fmss.services.FunctionService;
import fmss.services.LoggingFunctionDifference;
import fmss.services.LoggingUserDifference;
import fmss.services.ResourceConfigService;
import fmss.services.UBaseSysLogService;
import fmss.common.util.Constants;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;


//针对某一角色的具体功能配置
public class FuncConfigAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 294448731879794896L;

	private List roleList;

	private List menus;

	private String roleId;

	private String userId;

	private FunctionService functionService;

	private static Map baseConfigs = null;

	private String systemId = null;

	private String menuId = null;

	private String id = null;

	private boolean config = false;

	private String values = null;
	
	private UBaseSysLogService sysLogService;

	public void setSysLogService(UBaseSysLogService sysLogService) {
		this.sysLogService = sysLogService;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public Map getBaseConfigs() {
		return baseConfigs;
	}

	public String getSystemId() {
		return systemId;
	}

	public String listRoleFunc() {
		roleList = functionService.getRoleFuncList(this.paginationList,
				systemId);
		//TODO
		return SUCCESS;
	}

	public String listUserFunc() {
		functionService.getAdminUserList(this.paginationList);
		return SUCCESS;
	}

	public String viewMenuRes() {

		return SUCCESS;
	}

	public String saveFuncWithUser() {
		String[] value = values.split(",");
		if (!ArrayUtils.isEmpty(value)) {
			String[] ids = new String[value.length];
			if (StringUtils.isEmpty(values)) {
				ids = new String[0];
			} else {
				for (int i = 0; i < value.length; i++) {
					String v = value[i];
					String[] temp = v.split("-");
					ids[i] = temp[1];
				}
			}
			LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
			List l = functionService.getFuncAuthList(userId);
			List olds = new ArrayList();
			for (Iterator iterator = l.iterator(); iterator.hasNext();) {
				UBaseFuncAuthDO o = (UBaseFuncAuthDO) iterator.next();
				if(!olds.contains(String.valueOf(o.getFuncId()))) olds.add(String.valueOf(o.getFuncId()));
			}
			
			user.setDescription("保存[" + AuthorityNameFetcher.fetchUserName(userId) + "]权限配置信息");
			if (ArrayUtils.isEmpty(ids))
				ids = new String[] {};
			user.addDescription(",[" + new LoggingFunctionDifference("功能", (String[]) olds.toArray(new String[] {}), ids) + "]");
			
			functionService.saveFuncAuth(ids, userId, "user");
			
			UBaseSysLogDO sysLog = this.sysLogService.setUBaseSysLog(user,"删除","1",Constants.BASE_SYS_LOG_AUTHORITY);
			sysLog.setMenuId("0002.0008");
			sysLog.setMenuName("基础信息管理.权限功能配置");
			this.sysLogService.saveUBaseSysLog(sysLog);
		}
		setResultMessages("保存成功");
		return SUCCESS;
	}

	public void viewMenuResWithXML() {

		try {
			StringBuffer sb = new StringBuffer();
			if (StringUtils.isNotEmpty(roleId)) {
				menus = functionService.getMenuResList(roleId);
				List hasChilds = functionService.getFuncAuthList(roleId);
				UBaseConfigDO baseConfig = functionService
						.getSubSystemBySystemId(functionService
								.getRoleByRoleId(roleId).getSystemId());
				sb.append("<?xml version='1.0' encoding='UTF-8'?>");
				sb.append("<Response><Data><Tree>");
				sb.append("<TreeNode name=\"");
				sb.append(baseConfig.getSystemCname());
				sb.append("\" id=\"");
				sb.append(baseConfig.getSystemEname().replaceAll("\'", ""));
				sb.append("\" levelType='1' ");
				if (CollectionUtils.isNotEmpty(menus)) {
					sb.append(" _hasChild='1' ");
				}
				sb.append(" _canSelect='1' ");
				sb.append(">");
				for (int j = 0; menus != null && j < menus.size(); j++) {
					UAuthRoleResourceDO o = (UAuthRoleResourceDO) menus.get(j);
					sb.append("<TreeNode name='");
					sb.append(o.getResDetailValue() + "-"
							+ o.getResDetailName());
					sb.append("' id='");
					sb.append(baseConfig.getSystemId() + "-"
							+ o.getResDetailValue());
					sb.append("' levelType='2'");
					sb.append(" _canSelect='1' ");
					List childs = functionService.getFuncList(o
							.getResDetailValue(), baseConfig.getSystemId());
					StringBuffer s = new StringBuffer();
					if (CollectionUtils.isNotEmpty(childs)) {
						sb.append(" _hasChild='1' ");
						sb.append(" _opened='true' ");
						boolean selectedThis = this.viewFuncResWithXML(o
								.getResDetailValue(), baseConfig.getSystemId(),
								userId, hasChilds, s);
						if (selectedThis)
							sb.append(" _selected='1'");
					}
					sb.append(" value='");
					sb.append(Constants.MENU_RES_ID
							+ ResourceConfigService.SPERATOR
							+ o.getResDetailValue()
							+ ResourceConfigService.SPERATOR
							+ o.getResDetailName()
							+ ResourceConfigService.SPERATOR
							+ baseConfig.getSystemId());
					sb.append("'>");
					if (CollectionUtils.isNotEmpty(childs)) {
						sb.append(s);
					}
					sb.append("</TreeNode>");
				}
				sb.append("</TreeNode>");
				sb.append("</Tree></Data></Response>");
			}
			if (StringUtils.isNotEmpty(userId)) {
				List hasChilds = functionService.getFuncAuthList(userId);
				menus = functionService.getSystemMenuResList(userId);
				UBaseConfigDO baseConfig = functionService
						.getSubSystemBySystemId(Constants.SYSTEM_COMMON_ID);
				sb.append("<?xml version='1.0' encoding='UTF-8'?>");
				sb.append("<Response><Data><Tree>");
				sb.append("<TreeNode name=\"");
				sb.append(baseConfig.getSystemCname());
				sb.append("\" id=\"");
				sb.append(baseConfig.getSystemEname().replaceAll("\'", ""));
				sb.append("\" levelType='1' ");
				if (CollectionUtils.isNotEmpty(menus)) {
					sb.append(" _hasChild='1' ");
				}
				sb.append(" _canSelect='1' ");
				sb.append(">");
				for (int j = 0; menus != null && j < menus.size(); j++) {
					MenuDO o = (MenuDO) menus.get(j);
					List childs = functionService.getFuncList(o.getItemcode(),
							baseConfig.getSystemId());
					if (CollectionUtils.isNotEmpty(childs)) {
						sb.append("<TreeNode name='");
						sb.append(o.getItemcode() + "-" + o.getItemname());
						sb.append("' id='");
						sb.append(baseConfig.getSystemId() + "-"
								+ o.getItemcode());
						sb.append("' levelType='2'");
						StringBuffer s = new StringBuffer();

						sb.append(" _hasChild='1' ");
						sb.append(" _opened='true' ");
						boolean selectedThis = this.viewFuncResWithXML(o
								.getItemcode(), baseConfig.getSystemId(),
								userId, hasChilds, s);
						if (selectedThis)
							sb.append(" _selected='1'");

						sb.append(" _canSelect='1' ");
						sb.append(" value='");
						sb.append(Constants.MENU_RES_ID
								+ ResourceConfigService.SPERATOR
								+ o.getItemcode()
								+ ResourceConfigService.SPERATOR
								+ o.getItemname()
								+ ResourceConfigService.SPERATOR
								+ baseConfig.getSystemId());
						sb.append("'>");
						if (CollectionUtils.isNotEmpty(childs)) {
							sb.append(s);
						}
						sb.append("</TreeNode>");
					}
				}
				sb.append("</TreeNode>");
				sb.append("</Tree></Data></Response>");
			}
			log.debug(sb.toString());
			this.response.setContentType("text/html; charset=UTF-8");
			this.response.getWriter().print(sb.toString());
			this.response.getWriter().close();
		} catch (Exception e) {
			log.error(e);
		}
	}

	private boolean isConfigFunc(List hasChilds, UBaseFuncMenuDO func) {
		for (Iterator iterator = hasChilds.iterator(); iterator.hasNext();) {
			UBaseFuncAuthDO o = (UBaseFuncAuthDO) iterator.next();
			if (o.getFuncId() == func.getFuncId())
				return true;
		}
		return false;
	}

	private boolean viewFuncResWithXML(String menuId, String systemId,
			String userId, List hasChilds, StringBuffer sb) {
		boolean selectedParent = false;
		try {
			UBaseConfigDO baseConfig = functionService
					.getSubSystemBySystemId(systemId);
			List childs = functionService.getFuncList(menuId, baseConfig
					.getSystemId());

			if (childs != null) {
				// 如果是异步获取需要增加<data>节点，否则无法加载
				for (int i = 0; i < childs.size(); i++) {
					UBaseFuncMenuDO func = (UBaseFuncMenuDO) childs.get(i);
					sb.append("<TreeNode name='");
					sb.append(func.getFunc().getFuncDesc() + "-"
							+ func.getFunc().getFuncURL());
					sb.append("' id='");
					sb.append(menuId + "-" + func.getFunc().getFuncId());
					sb.append("' levelType='3' ");
					if (isConfigFunc(hasChilds, func)) {
						sb.append(" _selected='1'");
						selectedParent = true;
					}
					sb.append(" _canSelect='1' ");
					sb.append("> ");
					sb.append("</TreeNode>");
				}
			}
			log.debug(sb.toString());
		} catch (Exception e) {
			log.error(e);
		}
		return selectedParent;

	}

	public List getRoleList() {
		return roleList;
	}

	public void setFunctionService(FunctionService functionService) {
		this.functionService = functionService;
	}

	public List getMenus() {
		return menus;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isConfig() {
		return config;
	}

	public void setConfig(boolean config) {
		this.config = config;
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

}
