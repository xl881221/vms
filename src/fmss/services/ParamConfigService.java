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
 * <p>��Ȩ����:(C)2003-2010 </p>
 * @����: sunzhan
 * @����: 2009-6-24 ����10:03:08
 * @����: [ThemeService]�������ܷ�����
 */
public class ParamConfigService extends CommonService{

	/** �����ɼ� */
	public static final String PARAM_VISABLE = "true";

	/** �������ɼ� */
	public static final String PARAM_NOT_VISABLE = "false";

	/** �����ɸ� */
	public static final String PARAM_MODIFY = "true";

	/** �������ɸ� */
	public static final String PARAM_NOT_MODIFY = "false";
	
	/** �Ƿ�����LDAP��½��֤ */
	public static final String PARAM_SYS_ISNEEDLDAP="PARAM_SYS_ISNEEDLDAP";
	
	/** LDAP��½��֤��ʽ */
	public static final String PARAM_SYS_METHOD="PARAM_SYS_METHOD";
	
	/** LDAP��½���� */
	public static final String PARAM_SYS_LDAP="PARAM_SYS_LDAP";
	
	private JdbcDaoAccessor jdbcDaoAccessor;

	public void setJdbcDaoAccessor(JdbcDaoAccessor jdbcDaoAccessor) {
		this.jdbcDaoAccessor = jdbcDaoAccessor;
	}

	public JdbcDaoAccessor getJdbcDaoAccessor() {
		return jdbcDaoAccessor;
	}


	/**
	 * <p>��������: findUsed|����:������ϵͳ�������Ҷ�Ӧ���Ӳ��� </p>
	 * @param topParam �������
	 * @return �Ӳ����б�
	 */
	public List findByTop(UBaseSysParamDO topParam) throws Exception{
		List parameters = new ArrayList();
		parameters.add(topParam.getSystemId());
		parameters.add(topParam.getType());
		String query = "select usp from UBaseSysParamDO usp where usp.systemId=? and usp.type=? order by usp.orderNum";
		return this.find(query, parameters);
	}

	/**
	 * <p>��������: findByType|����:���ݲ����������� </p>
	 * @param parameter ��������
	 * @return �����б�
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
	 * <p>��������: findAll|����:�������� </p>
	 * @return ���в�����Ϣ
	 */
	public List findAll(){
		String query = " select usp from UBaseSysParamDO usp ";
		return this.find(query);
	}

	/**
	 * <p>��������: findWithMap|����:���ز����б��map��ʽ </p>
	 * @return �����б��map
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
	 * <p>��������: findSystemById|����:����ϵͳID,������ϵͳ </p>
	 * @param systemId ��ϵͳID
	 * @return ��ϵͳ����
	 */
	public UBaseConfigDO findSystemById(String systemId){
		return (UBaseConfigDO) this.get(UBaseConfigDO.class, systemId);
	}
	
	/**
	 * <p>��������: findSystemById|����:����ϵͳID,������ϵͳ </p>
	 * @param systemId ��ϵͳID
	 * @return ��ϵͳ����
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
	 * <p>��������: findSystemId|����:���ز����е�������ϵͳid </p>
	 * @return ��ϵͳid�б�
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
	 * <p>��������: findParamBySystem|����:������ϵͳȡϵͳ�²������ </p>
	 * @param systemId ϵͳid
	 * @return �������
	 */
	public List findParamBySystem(String systemId){
		String query = " select distinct new Map(usp.type as type,usp.typeDesc as typeDesc)"
				+ " from UBaseSysParamDO usp where usp.systemId=? ";
		return this.find(query, systemId);
	}
	
	/**
	 * <p>��������: findParamOneBySystem|����:������ϵͳȡϵͳ�²������ </p>
	 * @param systemId ϵͳid
	 * @return �������
	 */
	public List findParamOneBySystem(String systemId){
		String query = " select distinct usp.TYPE as type,usp.TYPE_DESC as typeDesc from "+SysParamAuditBase.SPBAK_TABLE 
				+ " usp where usp.SYSTEM_ID=? ";
		return jdbcDaoAccessor.find(query, new Object[]{systemId});
	}
	/**
	 * <p>��������: findParamBySystem|����:������ϵͳȡϵͳ�²������ </p>
	 * @param systemId ϵͳid
	 * @return �������
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
	
	//�ж��Ƿ��޸Ĺ�
	public boolean modifiedSysSystem(String paramId)
	{
		String sql ="select count(*) from "+SysParamAuditBase.SPBAK_TABLE+ " where param_id=? and audit_status=1";
		int count=jdbcDaoAccessor.findForInt(sql, new Object[] { paramId});
		if(count>0) return true ;
		return false ;
	}
	/**
	 * <p>��������: findParamByType|����:������ϵͳȡϵͳ�²������ </p>
	 * @param type ��������
	 * @return ��������
	 */
	public List findParamByType(String type){
		String query = " select distinct new Map(usp.itemEname as itemEname,usp.itemCname as itemCname,usp.selectedValue as selectedValue)"
				+ " from UBaseSysParamDO usp where usp.type=? ";
		return this.find(query, type);
	}


	/**
	 * <p>��������: saveParamConfig|����: �����������</p>
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
	 * <p>��������: saveParamChangeConfig|����: �����������</p>
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
	 * <p>��������: getParamsMap|����:���ز����б�Map </p>
	 * @param configs ��ϵͳ�б�
	 * @return �б�Map
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
				map.put(ubc.getSystemId()+"#"+ubc.getSystemCname(), sb.toString());//modify by wangxin 20091110 �޸�Ϊȡϵͳ���ļ��
			}
		}catch (Exception e){
			// TODO Auto-generated catch block
			throw e;
		}
		return map;
	}

	
	/**
	 * <p>��������: getParamsMap|����:���ز����б�Map </p>
	 * @param configs ��ϵͳ�б�
	 * @return �б�Map
	 * @throws Exception
	 */
	public Map getParamsOneMap(String systemId) throws Exception{
		Map map = new LinkedHashMap();
		try {
			StringBuffer sb = new StringBuffer();
			sb
					.append("<table class='editblock' width=100% border=0 id='"+"00003#ϵͳ����"+"'>");
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
			map.put("00003#ϵͳ����", sb.toString());// modify by wangxin 20091110
											// �޸�Ϊȡϵͳ���ļ��
		}catch (Exception e){
			// TODO Auto-generated catch block
			throw e;
		}
		return map;
	}

	/**
	 * <p>��������: getInputHtml|����:����html����ؼ��ַ��� </p>
	 * @param value ����ֵ
	 * @param valueList ������ֵ�б�
	 * @param paramId ����id
	 * @param readOnly �Ƿ���޸�
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
