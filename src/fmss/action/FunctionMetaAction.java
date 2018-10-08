package fmss.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import fmss.common.util.ArrayUtil;
import fmss.dao.entity.MenuDO;
import fmss.dao.entity.UBaseFuncDO;
import fmss.dao.entity.UBaseFuncMenuDO;
import fmss.common.util.Constants;
import fmss.common.util.SpringContextUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.dispatcher.Dispatcher;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;


import fmss.common.db.BaseEntityManager;

import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.RuntimeConfiguration;
import com.opensymphony.xwork2.config.entities.ActionConfig;

public class FunctionMetaAction extends BaseAction {

	private BaseEntityManager baseEntityManager;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Object lock = new Object();

	public List actions = new ArrayList();

	private List menus = new ArrayList();

	private List functions = new ArrayList();

	private Boolean enableTopMenu = Boolean.TRUE;

	private String itemcode;

	private JdbcTemplate jdbcTemplate;

	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

	public String listMetaActions() {
		Map m = getMetaActionConfigs();
		for (Iterator iterator = m.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry o = (Map.Entry) iterator.next();
			Map m2 = (Map) o.getValue();
			for (Iterator iterator2 = m2.entrySet().iterator(); iterator2.hasNext();) {
				Map.Entry o2 = (Map.Entry) iterator2.next();
				ActionConfig ac = (ActionConfig) o2.getValue();
				System.out.println(ac.getClassName() + "---" + ac.getMethodName());
				actions.add(ac);
			}
		}
		return SUCCESS;
	}

	private Map getMetaActionConfigs() {
		Dispatcher dispatcher = Dispatcher.getInstance();
		ConfigurationManager configurationManager = dispatcher.getConfigurationManager();
		Configuration config = configurationManager.getConfiguration();
		RuntimeConfiguration c = config.getRuntimeConfiguration();
		Map acs = c.getActionConfigs();
		return acs;
	}

	private Map getMetaActions() {
		Map actionsM = new TreeMap();
		Map acs = getMetaActionConfigs();
		for (Iterator iterator = acs.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry o = (Map.Entry) iterator.next();
			Map m = (Map) o.getValue();
			for (Iterator iterator2 = m.entrySet().iterator(); iterator2.hasNext();) {
				Map.Entry e = (Map.Entry) iterator2.next();
				ActionConfig ac = (ActionConfig) e.getValue();
				String clsName = ac.getClassName();
				try {
					Class.forName(clsName).newInstance();
				} catch (InstantiationException e1) {
				} catch (IllegalAccessException e1) {
				} catch (ClassNotFoundException e1) {
					Object cc = SpringContextUtils.getBean(clsName);
					clsName = cc.getClass().getName();
				}
				String method = (StringUtils.isNotEmpty(ac.getName()) ? ac.getName() : "main") + ".action";
				if (!actionsM.containsKey(clsName)) {
					Set s = new HashSet();
					s.add(method);
					actionsM.put(clsName, s);
				} else {
					Set s = (Set) actionsM.get(clsName);
					s.add(method);
				}
			}
		}
		return actionsM;
	}

	// 显示目前 已有的菜单和功能配置
	public String list00003Menus() {
		String sql = "from MenuDO where systemId=? ";
		if (!enableTopMenu.booleanValue())
			sql += " and url is not null";
		sql += " order by systemId,itemcode";
		List _menus = baseEntityManager.find(sql, new Object[] { Constants.SYSTEM_COMMON_ID });
		for (Iterator iterator = _menus.iterator(); iterator.hasNext();) {
			MenuDO o = (MenuDO) iterator.next();
			MetaMenuConfig c = new MetaMenuConfig();
			BeanUtils.copyProperties(o, c);
			List configFunctions = getMenuFunctionRelations(o.getItemcode());
			c.setConfigFunctions(configFunctions);
			menus.add(c);
		}
		String[] minus = getNotInitedMetaActions();
		if (!ArrayUtils.isEmpty(minus)) {
			String tips = StringUtils.join(minus, "&nbsp;");
			request.setAttribute("tips", tips);
		}
		// 基础数据维护
		functions = baseEntityManager.find("from UBaseFuncDO order by funcURL");
		return SUCCESS;
	}

	// 立即初始化基础数据
	public void immediateInitMetaActions() {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
		} catch (IOException e1) {
			log.error(e1);
		}
		final String[] minus = getNotInitedMetaActions();
		if (ArrayUtils.isEmpty(minus)) {
			pw.print("没有需要初始化的基础数据");
			return;
		}
		Assert.notNull(jdbcTemplate);
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(jdbcTemplate.getDataSource());
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			String query = "select max(func_id) from u_base_func";
			String insert = "insert into u_base_func(func_id,func_url,func_desc,func_type) values (?,?,?,?)";
			synchronized (lock) {
				final int max = jdbcTemplate.queryForInt(query);
				BatchPreparedStatementSetter set = new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return minus.length;
					}

					private int id = max + 1;

					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ps.setInt(1, id++);
						ps.setString(2, minus[i]);
						ps.setString(3, minus[i]);
						ps.setString(4, "CHECK");
					}
				};
				jdbcTemplate.batchUpdate(insert, set);
				transactionManager.commit(status);
			}
			pw.print("基础数据初始化完成");
		} catch (Exception e) {
			transactionManager.rollback(status);
			log.error(e);
			pw.print("基础数据初始化出错:<br>" + e.getMessage());
		} finally {
			pw.close();
		}

	}

	private String[] getNotInitedMetaActions() {
		// 是否存在初始化数据
		Set metaActionS = new HashSet();
		Map m = getMetaActionConfigs();
		for (Iterator iterator = m.entrySet().iterator(); iterator.hasNext();) {
			Map.Entry o = (Map.Entry) iterator.next();
			Map m2 = (Map) o.getValue();
			for (Iterator iterator2 = m2.entrySet().iterator(); iterator2.hasNext();) {
				Map.Entry o2 = (Map.Entry) iterator2.next();
				ActionConfig ac = (ActionConfig) o2.getValue();
				metaActionS.add(ac.getName() + ".action");
			}
		}
		String sql = "select funcURL from UBaseFuncDO";
		List l = baseEntityManager.find(sql);
		String[] minus = ArrayUtil.minus((String[]) metaActionS.toArray(new String[] {}), (String[]) l
				.toArray(new String[] {}));
		return minus;
	}

	private List getMenuFunctionRelations(String itemcode) {
		return baseEntityManager.find("from UBaseFuncMenuDO where menuId=? and systemId=?", new Object[] { itemcode,
				Constants.SYSTEM_COMMON_ID });
	}

	private boolean containsAction(String itemcode, String action) {
		List list = getMenuFunctionRelations(itemcode);
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			UBaseFuncMenuDO o = (UBaseFuncMenuDO) iterator.next();
			if (o.getFunc().getFuncURL().equals(action)) {
				return true;
			}
		}
		return false;
	}

	// 保存菜单代码和功能对应的关系
	public void saveMenuActionRelations() {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter pw = null;
		String insert = "insert into u_base_func_menu_rela(func_id,menu_id,system_id) "
				+ " select func_id,?,? from u_base_func where func_url in ";
		String delete = "delete from u_base_func_menu_rela where func_id in "
				+ " (select func_id from u_base_func where menu_id=? and system_id=?)";
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(jdbcTemplate.getDataSource());
		TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
		try {
			pw = response.getWriter();
			String s = request.getParameter("actions");
			String[] action = s.split(",");
			if (StringUtils.isNotEmpty(itemcode) && !ArrayUtils.isEmpty(action)) {
				Assert.notNull(jdbcTemplate);
				synchronized (lock) {
					int count = jdbcTemplate.update(delete, new Object[] { itemcode, Constants.SYSTEM_COMMON_ID });
					log.info("delete u_base_func_menu_rela : " + count);
					s = "('" + StringUtils.join(action, "','") + "')";
					count = jdbcTemplate.update(insert + s, new Object[] { itemcode, Constants.SYSTEM_COMMON_ID });
					log.info("insert to u_base_func_menu_rela : " + count);
					transactionManager.commit(status);
					pw.print("保存菜单和功能关联成功，共保存关系" + count);
				}
				return;
			}
			pw.print("没有选中任何关系进行保存");
		} catch (Exception e) {
			transactionManager.rollback(status);
			log.error(e);
			pw.print("保存菜单和功能关联失败:<br>" + e.getMessage());
		} finally {
			pw.close();
		}
	}

	// 显示功能与action配置树
	public void viewMetaActionConfigs() {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter pw = null;
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='UTF-8'?>").append("<Response><Data><Tree>").append(
				"<TreeNode name='配置");
		sb.append("' id='xsedd").append("' levelType='1' ");
		try {
			pw = response.getWriter();
			Map m = getMetaActions();
			if (CollectionUtils.isNotEmpty(menus)) {
				sb.append(" _hasChild='1' ");
			}
			sb.append(" _canSelect='1' ").append(">");
			for (Iterator iterator = m.entrySet().iterator(); iterator.hasNext();) {
				Map.Entry o = (Map.Entry) iterator.next();
				Set l = (Set) o.getValue();
				StringBuffer _sb = new StringBuffer();
				boolean selectThis = false;
				for (Iterator iterator2 = l.iterator(); iterator2.hasNext();) {
					String ac = (String) iterator2.next();
					_sb.append("<TreeNode name='").append(ac).append("' id='").append(ac).append("' levelType='3' ");
					if (containsAction(itemcode, ac)) {
						selectThis = true;
						_sb.append(" _selected='1'");
					}
					_sb.append(" _canSelect='1' ").append("> ").append("</TreeNode>");
				}
				sb.append("<TreeNode name='").append(o.getKey()).append("' id='");
				sb.append(o.getKey()).append("' levelType='2'").append(" _canSelect='1' ");
				if (CollectionUtils.isNotEmpty(l)) {
					sb.append(" _hasChild='1' ").append(" _opened='true' ");
					if (selectThis)
						sb.append(" _selected='1' ");
				}
				sb.append(" value='").append("sddsdd").append("'>");
				sb.append(_sb).append("</TreeNode>");
			}
			sb.append("</TreeNode>").append("</Tree></Data></Response>");
			log.debug(sb.toString());
			pw.print(sb.toString());
		} catch (Exception e) {
			pw.print("获取机构出错");
			log.error(e);
		} finally {
			pw.close();
		}
	}

	// 保存基础数据
	public void saveMetaActions() {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter pw = null;
		String update = "update u_base_func set func_url=?,func_desc=?,func_type=? where func_id=?";
		String delete = "delete u_base_func where func_id=?";
		try {
			pw = response.getWriter();
			String method = request.getParameter("method");
			String funcId = request.getParameter("funcId");
			Assert.notNull(method, "method不能为空");
			Assert.notNull(funcId, "id不能为空");
			if (method.equals("update")) {
				String funcURL = request.getParameter("funcURL");
				String funcDesc = request.getParameter("funcDesc");
				String funcType = request.getParameter("funcType");
				Assert.notNull(funcURL, "funcURL不能为空");
				Assert.notNull(funcDesc, "funcDesc不能为空");
				Assert.notNull(funcType, "funcType不能为空");
				int count = jdbcTemplate.update(update, new Object[] { funcURL, funcDesc, funcType, funcId });
				log.info("update metas:" + count);
				pw.print("保存成功");
			}
			if (method.equals("delete")) {
				int count = jdbcTemplate.update(delete, new Object[] { funcId });
				log.info("delete metas:" + count);
				pw.print("删除成功");
			}
		} catch (Exception e) {
			log.error(e);
			pw.print("保存基础数据失败:<br>" + e.getMessage());
		} finally {
			pw.close();
		}
	}

	// 获取基础数据
	public void getOneMetaActions() {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter pw = null;
		String sql = "from UBaseFuncDO where funcId=?";
		StringBuffer sb = new StringBuffer();
		try {
			pw = response.getWriter();
			String funcId = request.getParameter("funcId");
			Assert.isTrue(StringUtils.isNotEmpty(funcId), "id不能为空");
			List l = baseEntityManager.find(sql, new Object[] { new Integer(funcId) });
			if (CollectionUtils.isNotEmpty(l)) {
				UBaseFuncDO f = (UBaseFuncDO) l.get(0);
				sb.append("{").append("funcId:").append(String.valueOf(f.getFuncId()));
				sb.append(",").append("funcURL:").append("\"").append(f.getFuncURL()).append("\"");
				sb.append(",").append("funcDesc:").append("\"").append(f.getFuncDesc()).append("\"");
				sb.append(",").append("funcType:").append("\"").append(f.getFuncType()).append("\"");
			}
			sb.append(",status:1}");
			pw.print(sb.toString());
		} catch (Exception e) {
			log.error(e);
			pw.print("{status:0,message:\""+e.getMessage()+"\"}");
		} finally {
			pw.close();
		}
	}

	public List getActions() {
		return actions;
	}

	public void setActions(List actions) {
		this.actions = actions;
	}

	public BaseEntityManager getBaseEntityManager() {
		return baseEntityManager;
	}

	public void setBaseEntityManager(BaseEntityManager baseEntityManager) {
		this.baseEntityManager = baseEntityManager;
	}

	public List getMenus() {
		return menus;
	}

	public void setMenus(List menus) {
		this.menus = menus;
	}

	public Boolean getEnableTopMenu() {
		return enableTopMenu;
	}

	public void setEnableTopMenu(Boolean enableTopMenu) {
		this.enableTopMenu = enableTopMenu;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List getFunctions() {
		return functions;
	}

	public void setFunctions(List functions) {
		this.functions = functions;
	}

}

class MetaMenuConfig extends MenuDO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List configFunctions;
	private String configs;

	public List getConfigFunctions() {
		return configFunctions;
	}

	public void setConfigFunctions(List configFunctions) {
		this.configFunctions = configFunctions;
	}

	public String getConfigs() {
		if (StringUtils.isEmpty(configs) && configFunctions != null) {
			StringBuffer sb = new StringBuffer();
			for (Iterator iterator = configFunctions.iterator(); iterator.hasNext();) {
				UBaseFuncMenuDO o = (UBaseFuncMenuDO) iterator.next();
				sb.append(o.getFunc().getFuncDesc()).append("[").append(o.getFunc().getFuncURL()).append("] &nbsp;");
			}
			configs = sb.toString();
		}
		return configs;
	}

	public void setConfigs(String configs) {
		this.configs = configs;
	}
}

class MetaActionConfig extends ActionConfig {

	protected MetaActionConfig(ActionConfig orig) {
		super(orig);
	}

	private String descName;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getDescName() {
		return descName;
	}

	public void setDescName(String descName) {
		this.descName = descName;
	}
}
