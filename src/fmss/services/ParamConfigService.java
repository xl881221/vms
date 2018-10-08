package fmss.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import fmss.action.base.JdbcDaoAccessor;
import fmss.action.base.SubSystemChangeAuditBase;
import fmss.action.base.SysParamAuditBase;
import fmss.action.entity.UBaseSysParamChangeDO;
import fmss.common.util.BeanUtil;
import fmss.dao.entity.UBaseConfigDO;
import fmss.dao.entity.UBaseSysParamDO;

/**
 * <p>版权所有:(C)2003-2010 </p>
 * @作者: sunzhan
 * @日期: 2009-6-24 上午10:03:08
 * @描述: [ThemeService]主题风格功能服务类
 */
public class ParamConfigService extends CommonService{

	/** 参数可见 */
	public static final String PARAM_VISABLE = "true";

	/** 参数不可见 */
	public static final String PARAM_NOT_VISABLE = "false";

	/** 参数可改 */
	public static final String PARAM_MODIFY = "true";

	/** 参数不可改 */
	public static final String PARAM_NOT_MODIFY = "false";
	
	/** 是否启用LDAP登陆验证 */
	public static final String PARAM_SYS_ISNEEDLDAP="PARAM_SYS_ISNEEDLDAP";
	
	/** LDAP登陆验证方式 */
	public static final String PARAM_SYS_METHOD="PARAM_SYS_METHOD";
	
	/** LDAP登陆设置 */
	public static final String PARAM_SYS_LDAP="PARAM_SYS_LDAP";
	
	private JdbcDaoAccessor jdbcDaoAccessor;

	public void setJdbcDaoAccessor(JdbcDaoAccessor jdbcDaoAccessor) {
		this.jdbcDaoAccessor = jdbcDaoAccessor;
	}

	public JdbcDaoAccessor getJdbcDaoAccessor() {
		return jdbcDaoAccessor;
	}


	/**
	 * <p>方法名称: findUsed|描述:根据子系统和类别查找对应的子参数 </p>
	 * @param topParam 顶层参数
	 * @return 子参数列表
	 */
	public List findByTop(UBaseSysParamDO topParam) throws Exception{
		List parameters = new ArrayList();
		parameters.add(topParam.getSystemId());
		parameters.add(topParam.getType());
		String query = "select usp from UBaseSysParamDO usp where usp.systemId=? and usp.type=? order by usp.orderNum";
		return this.find(query, parameters);
	}

	/**
	 * <p>方法名称: findByType|描述:根据参数项名查找 </p>
	 * @param parameter 参数项名
	 * @return 参数列表
	 */
	public List findByItem(Object parameter){
		String query = " select usp from UBaseSysParamDO usp where usp.itemEname=? ";
		return this.find(query, parameter);
	}
	
	public UBaseSysParamDO findById(String id){
		String query = " select usp from UBaseSysParamDO usp where usp.paramId=? ";
		List l = this.find(query, new Integer(id));
		return CollectionUtils.isNotEmpty(l) ? (UBaseSysParamDO) l.get(0) : null;
	}

	/**
	 * <p>方法名称: findAll|描述:查找所有 </p>
	 * @return 所有参数信息
	 */
	public List findAll(){
		String query = " select usp from UBaseSysParamDO usp ";
		return this.find(query);
	}

	/**
	 * <p>方法名称: findWithMap|描述:返回参数列表的map形式 </p>
	 * @return 参数列表的map
	 */
	public Map findWithMap(){
		Map map = new HashMap();
		List list = this.findAll();
		Iterator ite = list.iterator();
		while(ite.hasNext()){
			UBaseSysParamDO usd = (UBaseSysParamDO) ite.next();
			map.put(usd.getItemEname().trim(), usd);
		}
		return map;
	}

	/**
	 * <p>方法名称: findSystemById|描述:根据系统ID,返回子系统 </p>
	 * @param systemId 子系统ID
	 * @return 子系统对象
	 */
	public UBaseConfigDO findSystemById(String systemId){
		return (UBaseConfigDO) this.get(UBaseConfigDO.class, systemId);
	}
	
	/**
	 * <p>方法名称: findSystemById|描述:根据系统ID,返回子系统 </p>
	 * @param systemId 子系统ID
	 * @return 子系统对象
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public UBaseSysParamChangeDO findSystemChangeById(String systemId) throws InstantiationException, IllegalAccessException{
		String sql = "select * from " + SysParamAuditBase.SPBAK_TABLE
		+ " where system_Id=? ";
		List list = null;
		list = jdbcDaoAccessor.find(sql, new Object[] { systemId });
		if (CollectionUtils.isNotEmpty(list)) {
			Map map = (Map) list.get(0);
			UBaseSysParamChangeDO o = (UBaseSysParamChangeDO) BeanUtil
					.reflectToFillValue(UBaseSysParamChangeDO.class, map, SysParamAuditBase.sysFullAttributeFields, 
							SysParamAuditBase.sysFullColumnFields);
			return o;
			
		}
		return null ;
	}

	/**
	 * <p>方法名称: findSystemId|描述:返回参数中的所有子系统id </p>
	 * @return 子系统id列表
	 */
	public List findSystemId(){
		String query = " select usp.systemId from UBaseSysParamDO usp,UBaseConfigDO ubc "
				+ " where usp.systemId=ubc.systemId"
				+ " order by ubc.menuOrderNum";
		List tempList = this.find(query);
		List list = new LinkedList();
		for(Iterator iterator = tempList.iterator(); iterator.hasNext();){
			String systemId = (String) iterator.next();
			if(!list.contains(systemId)){
				list.add(systemId);
			}
		}
		return list;
	}

	/**
	 * <p>方法名称: findParamBySystem|描述:根据子系统取系统下参数类别 </p>
	 * @param systemId 系统id
	 * @return 参数类别
	 */
	public List findParamBySystem(String systemId){
		String query = " select distinct new Map(usp.type as type,usp.typeDesc as typeDesc)"
				+ " from UBaseSysParamDO usp where usp.systemId=? ";
		return this.find(query, systemId);
	}
	
	/**
	 * <p>方法名称: findParamOneBySystem|描述:根据子系统取系统下参数类别 </p>
	 * @param systemId 系统id
	 * @return 参数类别
	 */
	public List findParamOneBySystem(String systemId){
		String query = " select distinct usp.TYPE as type,usp.TYPE_DESC as typeDesc from "+SysParamAuditBase.SPBAK_TABLE 
				+ " usp where usp.SYSTEM_ID=? ";
		return jdbcDaoAccessor.find(query, new Object[]{systemId});
	}
	/**
	 * <p>方法名称: findParamBySystem|描述:根据子系统取系统下参数类别 </p>
	 * @param systemId 系统id
	 * @return 参数类别
	 */
	public boolean isChanged(Integer paramId, String newSv) {
		String query = " select * from "
				+ SysParamAuditBase.SPMAIN_TABLE + " where PARAM_ID=?";
		Map map = jdbcDaoAccessor.findForMap(query, new Object[] { paramId });
		String sv =(String)map.get("SELECTED_VALUE");
	    if(sv==null &&newSv.equals("")){
	    	return false;}
		if (sv != null && !" ".equals(sv)) {
			if (sv.equals(newSv) || sv == newSv)
				return false;
		}
		String sqlString =" select selected_value from "
			+ SysParamAuditBase.SPBAK_TABLE + " where PARAM_ID=?";
		Map map2 = jdbcDaoAccessor.findForMap(sqlString, new Object[] { paramId });
		if (map2 == null) {
			sqlString = "insert into "
					+ SysParamAuditBase.SPBAK_TABLE
					+ "( PARAM_ID,SYSTEM_ID,TYPE,TYPE_DESC, ITEM_ENAME,ITEM_CNAME,SELECTED_VALUE," +
							" VALUE_LIST, IS_MODIFY, IS_VISIBLE,ORDER_NUM) values(?,?,?,?,?,?,?,?,?,?,?)";
			jdbcDaoAccessor.update(sqlString, new Object[] {
					map.get("PARAM_ID").toString(), map.get("SYSTEM_ID"), map.get("TYPE"),
					map.get("TYPE_DESC"), map.get("ITEM_ENAME"),
					map.get("ITEM_CNAME"), map.get("SELECTED_VALUE"),
					map.get("VALUE_LIST"), map.get("IS_MODIFY"),
					map.get("IS_VISIBLE"), map.get("ORDER_NUM") });
			return true  ;
		}
		return true;
	}
	
	//判断是否修改过
	public boolean modifiedSysSystem(String paramId)
	{
		String sql ="select count(*) from "+SysParamAuditBase.SPBAK_TABLE+ " where param_id=? and audit_status=1";
		int count=jdbcDaoAccessor.findForInt(sql, new Object[] { paramId});
		if(count>0) return true ;
		return false ;
	}
	/**
	 * <p>方法名称: findParamByType|描述:根据子系统取系统下参数类别 </p>
	 * @param type 参数类型
	 * @return 参数内容
	 */
	public List findParamByType(String type){
		String query = " select distinct new Map(usp.itemEname as itemEname,usp.itemCname as itemCname,usp.selectedValue as selectedValue)"
				+ " from UBaseSysParamDO usp where usp.type=? ";
		return this.find(query, type);
	}


	/**
	 * <p>方法名称: saveParamConfig|描述: 保存参数设置</p>
	 * @param paramList
	 * @throws Exception
	 */
	public void saveParamConfig(List paramList) {
		for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
			UBaseSysParamDO param = (UBaseSysParamDO) iterator.next();
			this.update(param);
		}
	}
	
	/**
	 * <p>方法名称: saveParamChangeConfig|描述: 保存参数设置</p>
	 * @param paramList
	 * @throws Exception
	 */
	public void saveParamChangeConfig(List paramList) {
		for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
			UBaseSysParamChangeDO param = (UBaseSysParamChangeDO) iterator.next();
//			String paramId=String.valueOf(param.getParamId());
//			String sql ="delete from "+SysParamAuditBase.SPBAK_TABLE+" where param_id=?";
//			jdbcDaoAccessor.update(sql, new Object[]{paramId});
			String sql ="update "+SysParamAuditBase.SPBAK_TABLE+" set SELECTED_VALUE=? ,CHANGE_USER=?,CHANGE_TIME=?," +
					"ID=?,CHANGE_STATUS=?,AUDIT_STATUS=? where param_id=?";
			jdbcDaoAccessor.update(sql, new Object[]{param.getSelectedValue(),param.getChangeUser(),param.getChangeTime(),
					param.getId(),param.getChangeStatus(),param.getAuditStatus(),param.getParamId()});
			
			
		}
	}

	/**
	 * <p>方法名称: getParamsMap|描述:返回参数列表Map </p>
	 * @param configs 子系统列表
	 * @return 列表Map
	 * @throws Exception
	 */
	public Map getParamsMap(List configs) throws Exception{
		Map map = new LinkedHashMap();
		try{
			for(int i = 0; i < configs.size(); i++){
				StringBuffer sb = new StringBuffer();
				UBaseConfigDO ubc = (UBaseConfigDO) configs.get(i);
				sb.append("<table class='editblock' width=100% border=0 id='"
						+ ubc.getSystemId() + "'>");
				List paramTypes = this.findParamBySystem(ubc.getSystemId());
				for(int j = 0; j < paramTypes.size(); j++){
					Map paramType = (Map) paramTypes.get(j);
					UBaseSysParamDO topParam = new UBaseSysParamDO();
					topParam.setSystemId(ubc.getSystemId());
					topParam.setType(paramType.get("type").toString());
					topParam.setTypeDesc(paramType.get("typeDesc").toString());
					List childParams = this.findByTop(topParam);
					sb.append("<tr>");
					sb.append("<td style='cursor:hand' onclick=\"javascript:Expan('");
					sb.append(topParam.getType() + "');\">");
					sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img align=absmiddle style='width:20px;height:20px' src='themes/images/icons/folder-closed.png' /> ");
					sb.append(topParam.getTypeDesc() + "</td></tr><tr><td>");
					sb.append("<table width=100% border=0 id='"	+ topParam.getType() + "'>");
					Iterator ite = childParams.iterator();
					while(ite.hasNext()){
						UBaseSysParamDO childParam = (UBaseSysParamDO) ite.next();
						String value = childParam.getSelectedValue();
						String readOnly = childParam.getIsMofify();
						String visible = childParam.getIsVisible();
						if(PARAM_NOT_MODIFY.equalsIgnoreCase(visible)){
							continue;
						}
						String valueList = childParam.getValueList();
						String paramId = String
								.valueOf(childParam.getParamId());
						String itemCname = childParam.getItemCname();
						sb
								.append("<tr><td nowrap>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
						sb
								.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
						sb.append(itemCname);
						sb.append("</td><td width=100%>");
						sb.append(this.getInputHtml(value, valueList, paramId,
								readOnly));
						sb.append("</tr>");
					}
					sb.append("</table></td></tr>");
				}
				sb.append("</table>");
				map.put(ubc.getSystemId()+"#"+ubc.getSystemCname(), sb.toString());//modify by wangxin 20091110 修改为取系统中文简称
			}
		}catch (Exception e){
			// TODO Auto-generated catch block
			throw e;
		}
		return map;
	}

	
	/**
	 * <p>方法名称: getParamsMap|描述:返回参数列表Map </p>
	 * @param configs 子系统列表
	 * @return 列表Map
	 * @throws Exception
	 */
	public Map getParamsOneMap(String systemId) throws Exception{
		Map map = new LinkedHashMap();
		try {
			StringBuffer sb = new StringBuffer();
			sb
					.append("<table class='editblock' width=100% border=0 id='"+"00003#系统管理"+"'>");
			List paramTypes = this.findParamOneBySystem(systemId);
			for (int j = 0; j < paramTypes.size(); j++) {
				Map paramType = (Map) paramTypes.get(j);
				UBaseSysParamDO topParam = new UBaseSysParamDO();
				topParam.setSystemId(systemId);
				topParam.setType(paramType.get("type").toString());
				topParam.setTypeDesc(paramType.get("typeDesc").toString());
				List childParams = this.findByTop(topParam);
				sb.append("<tr>");
				sb
						.append("<td style='cursor:hand' onclick=\"javascript:Expan('");
				sb.append(topParam.getType() + "');\">");
				sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img align=absmiddle style='width:20px;height:20px display:none' src='themes/images/icons/folder-open.png' /> ");
				sb.append(topParam.getTypeDesc() + "</td></tr><tr><td>");
				sb.append("<table width=100% border=0 id='"
						+ topParam.getType() + "'>");
				Iterator ite = childParams.iterator();
				while (ite.hasNext()) {
					UBaseSysParamDO childParam = (UBaseSysParamDO) ite.next();
					String value = childParam.getSelectedValue();
					String readOnly = childParam.getIsMofify();
					String visible = childParam.getIsVisible();
					if (PARAM_NOT_MODIFY.equalsIgnoreCase(visible)) {
						continue;
					}
					String valueList = childParam.getValueList();
					String paramId = String.valueOf(childParam.getParamId());
					String itemCname = childParam.getItemCname();
					sb
							.append("<tr><td nowrap>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					sb
							.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
					sb.append(itemCname);
					sb.append("</td><td width=100% disabled='disabled'>");
					sb.append(this.getInputHtml(value, valueList, paramId,
							readOnly));
					sb.append("</tr>");
				}
				sb.append("</table></td></tr>");
			}
			sb.append("</table>");
			map.put("00003#系统管理", sb.toString());// modify by wangxin 20091110
											// 修改为取系统中文简称
		}catch (Exception e){
			// TODO Auto-generated catch block
			throw e;
		}
		return map;
	}

	/**
	 * <p>方法名称: getInputHtml|描述:构建html输入控件字符串 </p>
	 * @param value 参数值
	 * @param valueList 参数项值列表
	 * @param paramId 参数id
	 * @param readOnly 是否可修改
	 * @return
	 */
	private String getInputHtml(String value, String valueList, String paramId,
			String readOnly){
		if(value == null){
			value = " ";
		}
		String strInput = "";
		if(valueList != null && !"".equals(valueList)){
			String[] valueArray = valueList.split(";");
			strInput += " <select name='param_" + paramId + "'> ";
			for(int i = 0; i < valueArray.length; i++){
				String[] valueSubArray = valueArray[i].split("-");
				if(value.toUpperCase().equals(valueSubArray[0].toUpperCase())){
					if(PARAM_NOT_MODIFY.equalsIgnoreCase(readOnly)){
						return valueArray[i];
					}
					strInput += " 	<option value='" + valueSubArray[0]
							+ "' selected =true>" + valueArray[i]
							+ "</option> ";
				}else{
					strInput += " 	<option value='" + valueSubArray[0] + "'>"
							+ valueArray[i] + "</option> ";
				}
			}
			strInput += " </select> ";
		}else{
			if(PARAM_NOT_MODIFY.equalsIgnoreCase(readOnly)){
				return value;
			}
			int len = value.length() * 10 < 100 ? 100 : value.length() * 10;
			len = len > 400 ? 400 : len;
			strInput += " <input name='param_" + paramId + "' value='" + (value != null ? value.trim() : value)
					+ "' style='width:" + len + "'> ";
		}
		return strInput;
	}
}
