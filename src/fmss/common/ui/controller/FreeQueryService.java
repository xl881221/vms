/**
 * 
 */
package fmss.common.ui.controller;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.LobRetrievalFailureException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.support.lob.LobHandler;

import fmss.dao.entity.AlyFactDimRelationTable;
import fmss.dao.entity.AlyPurviewMeta;
import fmss.dao.entity.AlySubjectMeta;
import fmss.dao.entity.LoginDO;
import fmss.common.db.SQLDAO;
import fmss.services.CommonService;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * 自由查询服务类
 * 
 * @author liuhaibo
 * 
 */
public class FreeQueryService extends SQLDAO {

	class OrderByObject {
		String orderType; // 待校验的上级的ReportKey

		List orderList = new LinkedList();// 待校验的下级ReportKey
	}

	public final static String SELECT = " select ";

	public final static String FORM = " from ";

	public final static String WHERE = " where ";

	public final static String DOT = ".";

	public final static String DOTS = ".*";

	public final static String JOIN = " join ";

	public final static String AS = " as ";

	public final static String PUR = " ROWNUM < 20 ";

	public final static String ON = " on ";

	public final static String EQUALSIGN = " = ";

	public final static String ORDERBY = " order by ";

	public final static String GROUPBY = " group by ";

	public final static String BLANK = " ";

	public final static String COMMA = ",";

	public final static String AND = " and ";

	private String metabaseusername;
	private LoginDO user;

	private LobHandler lobHandler;// 为操作大对象提供统一的访问接口
	private CommonService commonService;
	private QueryDataToHtml queryDataToHtml;

	private ParamsParseFactory paramsParseFactory;

	public QueryDataToHtml getQueryDataToHtml() {
		return queryDataToHtml;
	}

	public void setQueryDataToHtml(QueryDataToHtml queryDataToHtml) {
		this.queryDataToHtml = queryDataToHtml;
	}

	public CommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}

	public List getAlySchemeMetaList() {
		String hql = "from AlySchemeMeta a where a.subjectId='10460'";
		return commonService.find(hql);
	}

	/**
	 * @param schemeCode
	 * @return 获取自由查询对象
	 */
	public FreeQueryForm getFreeQueryForm(final String schemeCode) {

		Object object = new Object();

		XStream xstream = new XStream(new DomDriver());

		JdbcTemplate jdbcTemplate = getJdbcTemplate();
		final StringBuffer result = new StringBuffer();

		String sql = "select SCHEME_CONTEXT from aly_scheme_meta where  scheme_code=?";

		jdbcTemplate.query(sql, new Object[] { schemeCode },
				new AbstractLobStreamingResultSetExtractor() {

					protected void handleNoRowFound()
							throws LobRetrievalFailureException {
						logger.warn("not found scheme!	schemeCode:"
								+ schemeCode);
					}

					protected void streamData(ResultSet rs)
							throws SQLException, IOException,
							DataAccessException {

						InputStream is = lobHandler
								.getBlobAsBinaryStream(rs, 1);

						result.append(StreamUtil.read(is, "GBK"));
					}
				});
		String scheme = result.toString();

		xstream.alias("freeQueryForm", FreeQueryForm.class);

		object = xstream.fromXML(scheme);

		FreeQueryForm freeQueryForm = (FreeQueryForm) object;

		AlySubjectMeta subjectMeta = (AlySubjectMeta) commonService.load(
				AlySubjectMeta.class, Long
						.valueOf(freeQueryForm.getSubjectId()));
		AlyPurviewMeta purviewMeta = (AlyPurviewMeta) commonService.load(
				AlyPurviewMeta.class, Long
						.valueOf(freeQueryForm.getPurviewId()));

		freeQueryForm.setSubjectDes(subjectMeta.getSubjectName());
		freeQueryForm.setPurviewDes(purviewMeta.getPurviewName());

		return freeQueryForm;

	}

	/**
	 * @return 获取domElementsList对象
	 */
	public String getDomElementsList(FreeQueryForm freeQueryForm) {

		List conditonList = freeQueryForm.getConditionList();
		loadConditionListData(conditonList);
		JSONArray jsonArray = JSONArray.fromObject(conditonList);
		return jsonArray.toString();
	}

	private void loadConditionListData(List conditionList) {
		for (Iterator iterator = conditionList.iterator(); iterator.hasNext();) {
			FreeQueryClmForm freeQueryClmForm = (FreeQueryClmForm) iterator
					.next();
			if ("dataTable".equals(freeQueryClmForm.getConditionType())) {
				List conditionData = getCondtionDataByClmCol(freeQueryClmForm);
				freeQueryClmForm.setConditioncustomEnumeratedData(JSONArray
						.fromObject(conditionData).toString());
			}
		}
	}

	private List getCondtionDataByClmCol(FreeQueryClmForm freeQueryClmForm) {
		List result = new ArrayList();
		String sql = freeQueryClmForm.getConditionDataTableSql();
		List data = getJdbcTemplate().queryForList(sql);
		for (int i = 0; i < data.size(); i++) {
			Map rowMap = (Map) data.get(i);
			String key = (String) rowMap.get(freeQueryClmForm
					.getConditionDataTableValueColName());
			String name = (String) rowMap.get(freeQueryClmForm
					.getConditionDataTableDisplayColName());
			if (null == key) {
				continue;
			}
			KeyNameObject keyNameObject = new KeyNameObject();
			keyNameObject.setKey(key);
			if (null != name) {
				keyNameObject.setName(name);
			}
			result.add(keyNameObject);
			// ResultSetMetaData rm = (ResultSetMetaData)iterator.next();
		}
		return result;
	}

	public String getDomElements(FreeQueryForm freeQueryForm) {

		List conditonList = freeQueryForm.getConditionList();
		Iterator iterator1 = conditonList.iterator();
		StringBuffer sbf = new StringBuffer();

		if (!iterator1.hasNext()) {
			sbf.append("该方案没有设置条件！");

		} else {
			sbf
					.append("<table id=\"conditionTable\" border=0  cellpadding=0 cellspacing=0>");
			for (Iterator iterator = iterator1; iterator.hasNext();) {

				FreeQueryClmForm freeQueryClmForm = (FreeQueryClmForm) iterator
						.next();
				sbf.append("<tr>");
				sbf
						.append("<td width=\"90\" style=\"text-align: right;padding-right: 5px;\">");
				sbf.append(freeQueryClmForm.getTableClmDes() + ":");
				sbf.append("</td>");
				sbf.append("<td>");
				sbf.append(HtmlUtil.buildHtmlInput(freeQueryClmForm
						.getTableClmName(), freeQueryClmForm.getTableClmName(),
						"text"));
				sbf.append("</td>");
				sbf.append("</tr>");
			}
			sbf.append("</table>");
		}
		return sbf.toString();
	}

	/**
	 * 生成Html
	 * 
	 * @param queryViewData
	 * @return
	 * @throws DataTypeException
	 * @throws AnalyzingException
	 */
	public String toHtml(QueryData queryViewData, String title,
			String currencyUnit, String decimalDigits) throws DataTypeException {
		return queryDataToHtml.toHtml((FreeQueryData) queryViewData, title,
				currencyUnit, decimalDigits);
	}

	public PageBox getQueryDataForPage(FreeQueryForm form, int pageSize,
			int curpage) throws Exception {
		FreeQueryForm freeQueryForm = (FreeQueryForm) form;
		PageBox pb = null;
		freeQueryForm.setSql(generateSQL(freeQueryForm, false));
		pb = getQueryDataForPage(freeQueryForm.getSql(), freeQueryForm
				.generateAllClmList(), pageSize, curpage, new ArrayList(),
				freeQueryForm.getConditionList());

		doOperation(freeQueryForm, (FreeQueryData) pb.getQueryData());
		return pb;
	}

	/**
	 * 方法说明:组外排序 顺序 左-右<br>
	 * 
	 * @param paramsMap
	 *            操作所需的参数 MAP ,freeQueryData 数据模型 创建时间: 2009-1-9 下午04:38:08<br>
	 */
	private void sortGroupOut(Map paramsMap, FreeQueryData freeQueryData) {
		int metaPostion = Integer.parseInt((String) paramsMap
				.get("selColPostion"));
		String sortMode = (String) paramsMap.get("sortMode");
		ColMeta colMeta = freeQueryData.getMetaByPostion(metaPostion);

		try {
			FreeQueryDataHelper.groupOutSort(freeQueryData, colMeta, sortMode);
		} catch (DataTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 方法说明:分发 分析操作<br>
	 * 创建时间: 2009-1-9 下午04:39:37<br>
	 * 
	 * @throws FormulaDefineException
	 */
	public void doOperation(FreeQueryForm freeQueryForm,
			FreeQueryData freeQueryData) {

		if (freeQueryForm.getOperation() != null) {
			Operation operation = freeQueryForm.getOperation();

			String operationKey = operation.getOperationKey();

			if (AnalyseCst.SORT_GROUP_OUT.equals(operationKey)) {
				// 组外排序
				sortGroupOut(operation.getOperationParamMap(), freeQueryData);
			} else if (AnalyseCst.ADD_BLANK_COL.equals(operationKey)) {
				// 添加空白列
				addBlankCol(freeQueryForm, freeQueryData);
			} else if (AnalyseCst.PARSE_FLM.equals(operationKey)) {
				parseQueryData(freeQueryForm, operation.getOperationParamMap(),
						freeQueryData);
			}
		}
		freeQueryForm.setOperation(null);
	}

	/**
	 * 方法说明:解析当前QUERYDATA<br>
	 * 创建时间: 2009-2-9 下午06:13:07<br>
	 * 
	 * @throws FormulaDefineException
	 */
	private void parseQueryData(FreeQueryForm freeQueryForm,
			Map operationParamMap, FreeQueryData freeQueryData) {
		FreeQueryDataHelper.parseQueryData(freeQueryData);
		freeQueryForm.setAllClmNotEdit();
	}

	/**
	 * 
	 * 方法说明: 添加空白列 <br>
	 * 
	 * @param 操作所需的参数
	 *            MAP ,freeQueryData 数据模型
	 * @author yangxufei 创建时间: 2009-2-2 上午10:16:13<br>
	 */
	private void addBlankCol(FreeQueryForm freeQueryForm, FreeQueryData fq) {
		Operation operation = freeQueryForm.getOperation();
		Map operationParamMap = operation.getOperationParamMap();

		String addPostion = (String) operationParamMap.get("columnPostion");

		ColumnPostion cp = new ColumnPostion(addPostion);
		String customColumnMetaKey = (String) operationParamMap
				.get("columnKey");
		String customColumnMetaName = (String) operationParamMap
				.get("columnName");
		String customColumnMetaType = (String) operationParamMap
				.get("columnType");

		FreeQueryDataHelper.addCustomBlankColumn(fq, cp, customColumnMetaKey,
				customColumnMetaName, customColumnMetaType);
		freeQueryForm
				.setCustomColumn(freeQueryForm.getCurrentClmListIsSelect(), cp,
						customColumnMetaKey, customColumnMetaName,
						customColumnMetaType);

	}

	public String generateSQL(FreeQueryForm form, boolean isPreview)
			throws IllegalAccessException, InvocationTargetException {

		boolean isSelClm = form.getIsSelectColumn();

		String sql;
		if (!isSelClm) {
			// 如果为选择任何查询字段 那么查询所有维表及事实表
			sql = this.buildSQLByTables(form, isPreview);
		} else {
			sql = this.buildSQL(form, isPreview);
		}

		return sql;
	}

	public String buildSQLByTables(FreeQueryForm form, boolean isPreview) {
		FreeQuerySelFRForm freeQuerySelFRForm = (FreeQuerySelFRForm) form
				.getSelectedFactRelation().get(0);
		String factTable = form.getFactTableName();
		List relList = freeQuerySelFRForm.getJsonRelTables();
		Map dialectMap = generateTableDialect(form.getFactTableName(), form
				.getDimsTablesMap());

		StringBuffer sql = new StringBuffer();
		sql.append(SELECT);

		String factTableDialect = (String) dialectMap.get(factTable);

		StringBuffer selCol = new StringBuffer();
		Set allTableSet = dialectMap.keySet();
		for (Iterator iterator = allTableSet.iterator(); iterator.hasNext();) {
			String tableName = (String) iterator.next();
			selCol.append((String) dialectMap.get(tableName)).append(DOTS);
			if (iterator.hasNext()) {
				selCol.append(COMMA);
			}
		}
		sql.append(selCol).append(FORM).append(
				metabaseusername + DOT + factTable).append(BLANK).append(
				(String) dialectMap.get(factTable));

		for (Iterator iterator = relList.iterator(); iterator.hasNext();) {

			String relId = (String) iterator.next();
			AlyFactDimRelationTable alyFactDimRelationTable = (AlyFactDimRelationTable) commonService
					.load(AlyFactDimRelationTable.class, relId);

			String dimTableName = alyFactDimRelationTable.getAlyDimTableMeta()
					.getDimTableName();
			String dimTableNameDialect = (String) dialectMap.get(dimTableName);

			sql.append(JOIN).append(metabaseusername + DOT + dimTableName)
					.append(BLANK).append(dimTableNameDialect).append(BLANK)
					.append(ON);
			sql.append(dimTableNameDialect).append(DOT).append(
					alyFactDimRelationTable.getDimTableClmName()).append(
					EQUALSIGN).append(factTableDialect).append(DOT).append(
					alyFactDimRelationTable.getFactTableClmName());
		}

		if (isPreview)
			sql.append(WHERE).append(BLANK).append(PUR);
		logger.info("生成SQL" + sql.toString());
		return sql.toString();
	}

	public Map generateTableDialect(String factTableName, Map dimTablesMap) {
		Map map = new HashMap();
		try {
			map
					.put(factTableName, AnalyzingUtil.getDialect(factTableName,
							"f"));
			for (Iterator iterator = dimTablesMap.keySet().iterator(); iterator
					.hasNext();) {
				String name = (String) iterator.next();
				map.put(name, AnalyzingUtil.getDialect(name, "d"));
			}
		} catch (AnalyzingException e) {
			logger
					.error(
							"构建数据表别名map 时出错！$generateTableDialect(String factTableName,List dimTableNames)",
							e);
			e.printStackTrace();
		}
		return map;
	}

	public String getMetabaseusername() {
		return metabaseusername;
	}

	public void setMetabaseusername(String metabaseusername) {
		this.metabaseusername = metabaseusername;
	}

	private String getFilterClmName(String factId) {
		String sql = "select t.dim_orgfit_clm as fit from aly_fact_table_meta t where t.id=?";
		List a = getJdbcTemplate().queryForList(sql, new Object[] { factId });
		String clm = "";
		if (a.size() > 0) {
			if (((Map) a.get(0)).get("fit") != null)
				clm = ((Map) a.get(0)).get("fit").toString();
		}

		return clm;
	}

	public String buildSQL(FreeQueryForm form, boolean isPreview)
			throws IllegalAccessException, InvocationTargetException {
		FreeQuerySelFRForm freeQuerySelFRForm = (FreeQuerySelFRForm) form
				.getSelectedFactRelation().get(0);

		String factTable = form.getFactTableName();
		String fitClm = getFilterClmName(form.getFactTableId());
		Map dialectMap = generateTableDialect(form.getFactTableName(), form
				.getDimsTablesMap());
		List relList = freeQuerySelFRForm.getJsonRelTables();
		List selectColumns = form.getCurrentClmListIsSelect();

		boolean isGroupBy = getIsGroupBy(selectColumns);

		String factDName = (String) dialectMap.get(factTable);
		List allFactColumnL = new ArrayList();
		List allDimsColumnL = new ArrayList();
		List isNeedSelDimTable = new ArrayList();

		List colsL = new ArrayList();
		List joins = new ArrayList();
		List filterCondtions = new ArrayList();
		List groupBys = new ArrayList();

		for (Iterator iterator = selectColumns.iterator(); iterator.hasNext();) {
			FreeQueryClmForm freeQueryClmForm = (FreeQueryClmForm) iterator
					.next();
			String currentClmName = freeQueryClmForm.getTableClmName();
			if ("f".equals(freeQueryClmForm.getTableTypeMark())) {
				// 维度表
				String selected;
				if (isGroupBy) {
					if (freeQueryClmForm.getGroupBy().booleanValue()) {
						groupBys.add(factDName + DOT + currentClmName);
					}
					selected = freeQueryClmForm.getFunc() + "(" + factDName
							+ DOT + currentClmName + ")" + AS + currentClmName;
				} else {
					selected = factDName + DOT + currentClmName + AS
							+ currentClmName;
				}
				colsL.add(selected);
				allFactColumnL.add(freeQueryClmForm.getTableClmName());
			} else if ("d".equals(freeQueryClmForm.getTableTypeMark())) {
				allDimsColumnL.add(freeQueryClmForm.getTableClmName());
			} else {
				// throw Exception //未知的表类型
			}
		}

		for (Iterator iterator = relList.iterator(); iterator.hasNext();) {
			Long relId = new Long((String) iterator.next());
			AlyFactDimRelationTable alyFactDimRelationTable = (AlyFactDimRelationTable) commonService
					.load(AlyFactDimRelationTable.class, relId);

			String currentFactClmName = alyFactDimRelationTable
					.getFactTableClmName();
			String currentDimClmName = alyFactDimRelationTable
					.getDimTableClmName();
			String currentDimTableName = alyFactDimRelationTable
					.getAlyDimTableMeta().getDimTableName();
			String currentDimTableNameDialect = (String) dialectMap
					.get(currentDimTableName);

			// if (allFactColumnL.contains(currentFactClmName)
			// && allDimsColumnL.contains(currentDimClmName)) {
			isNeedSelDimTable.add(currentDimTableName);
			joins.add(JOIN + metabaseusername + DOT + currentDimTableName
					+ BLANK + currentDimTableNameDialect + BLANK + ON + BLANK
					+ currentDimTableNameDialect + DOT + currentDimClmName
					+ EQUALSIGN + factDName + DOT + currentFactClmName + BLANK);
			// }
		}

		for (Iterator iterator = selectColumns.iterator(); iterator.hasNext();) {
			FreeQueryClmForm freeQueryClmForm = (FreeQueryClmForm) iterator
					.next();

			for (Iterator iterator2 = isNeedSelDimTable.iterator(); iterator2
					.hasNext();) {
				String name = (String) iterator2.next();
				if (name.equals(freeQueryClmForm.getTableName())
						&& "d".equals(freeQueryClmForm.getTableTypeMark())) {
					String dimDName = (String) dialectMap.get(name);
					String selected;
					if (isGroupBy) {
						if (freeQueryClmForm.getGroupBy().booleanValue()) {
							groupBys.add(dimDName + DOT
									+ freeQueryClmForm.getTableClmName());
						}
						selected = freeQueryClmForm.getFunc() + "(" + dimDName
								+ DOT + freeQueryClmForm.getTableClmName()
								+ ")" + AS + freeQueryClmForm.getTableClmName();
					} else {
						selected = dimDName + DOT
								+ freeQueryClmForm.getTableClmName() + AS
								+ freeQueryClmForm.getTableClmName();
					}
					colsL.add(selected);
				}
			}

			if (freeQueryClmForm.getFilter() != null) {
				if (!"".equals(freeQueryClmForm.getFilter())) {
					filterCondtions.add(parseSql(freeQueryClmForm.getFilter()));
				}
			}

		}

		// TODO:增加权限是需要。需要从系统中获取当前登录人员的拥有全新的机构列表
		// String fitOrg=LoginManager.getLogin().getOrgListString();
		if (user != null) {
			//递归查询机构表，取出所有下属机构和其本身机构id号
			String orgId = user.getInstId();
			String getOrgSql = "select t.inst_id from u_base_inst t " +
					"start with t.parent_inst_id='"+orgId+"' " +
					"connect   by   prior t.inst_id = t.parent_inst_id " +
					"UNION (select inst_id from u_base_inst where inst_id='"+orgId+"')";
			
			if (!fitClm.equals("") && !orgId.equals("")) {
				String s = "(" + factDName + DOT + fitClm + BLANK + " in ("
						+ getOrgSql + "))";
				filterCondtions.add(s);

			}
//			List orgList = getJdbcTemplate().queryForList(getOrgSql);
//			//将取出的机构id号进行拼传
//			String fitOrg = "";
//			for(int i = 0;i < orgList.size();i++){
//				fitOrg += "'"+orgList.get(i)+"'"+",";
//			}
//			if(fitOrg.length()>0){
//				fitOrg = fitOrg.substring(0, fitOrg.length()-1);
//			}
//			if (!fitClm.equals("") && !fitOrg.equals("")) {
//				String s = "(" + factDName + DOT + fitClm + BLANK + " in ("
//						+ getOrgSql + "))";
//				filterCondtions.add(s);
//
//			}
		}

		// 使用方案时才到这里
		List conditions = form.getConditionList();
		if (!conditions.isEmpty()) {

			for (Iterator iterator = conditions.iterator(); iterator.hasNext();) {
				FreeQueryClmForm freeQueryClmForm = (FreeQueryClmForm) iterator
						.next();
				String condValue = freeQueryClmForm.getCoditionValue();
				if (!"".equals(condValue) && condValue != null) {
					filterCondtions.add(condValue);
				}
			}
		}

		if (isPreview)
			filterCondtions.add(PUR);

		return buildSQLByList(factTable, factDName, colsL, joins,
				filterCondtions, selectColumns, isGroupBy, groupBys);
	}

	public String buildSQLByList(String factTable, String factDName,
			List colsL, List joins, List filter, List selectColumns,
			boolean isGroupBy, List groupBys) throws IllegalAccessException,
			InvocationTargetException {

		Map orderMap = new HashMap();

		StringBuffer _selectCols = new StringBuffer();
		StringBuffer _fromTable = new StringBuffer();
		StringBuffer _joinTable = new StringBuffer();
		StringBuffer _whereCondition = new StringBuffer();
		StringBuffer _groupBy = new StringBuffer();
		StringBuffer _orderBy = new StringBuffer();

		_selectCols.append(SELECT).append(BLANK);
		for (Iterator iterator = colsL.iterator(); iterator.hasNext();) {
			String temp = (String) iterator.next();

			_selectCols.append(temp);
			if (iterator.hasNext()) {
				_selectCols.append(COMMA);
			}
		}

		_fromTable.append(BLANK).append(FORM).append(metabaseusername + DOT)
				.append(factTable).append(BLANK).append(factDName);
		for (Iterator iterator = joins.iterator(); iterator.hasNext();) {
			String temp = (String) iterator.next();
			_joinTable.append(temp);
			if (iterator.hasNext()) {
				_joinTable.append(BLANK);
			}
		}

		Iterator iterator1 = filter.iterator();

		if (iterator1.hasNext()) {
			_whereCondition.append(WHERE);
		}

		for (Iterator iterator = iterator1; iterator.hasNext();) {
			String filter_str = (String) iterator.next();
			_whereCondition.append(filter_str);
			if (iterator.hasNext()) {
				_whereCondition.append(AND);
			}
		}

		if (isGroupBy) {
			_groupBy.append(GROUPBY);
		}

		for (Iterator iterator = groupBys.iterator(); iterator.hasNext();) {
			String item = (String) iterator.next();
			_groupBy.append(item);
			if (iterator.hasNext())
				_groupBy.append(COMMA);
		}

		OrderByObject obo = generateGroupAndOrder(_fromTable, _joinTable,
				_whereCondition, orderMap, selectColumns);

		Iterator iterator2 = obo.orderList.iterator();
		if (iterator2.hasNext()) {
			_orderBy.append(ORDERBY);
		}
		for (Iterator iterator = iterator2; iterator.hasNext();) {
			FreeQueryClmForm freeQueryClmForm = (FreeQueryClmForm) iterator
					.next();
			String selected;
			if (isGroupBy) {
				selected = freeQueryClmForm.getFunc() + "("
						+ freeQueryClmForm.getTableDialectName() + DOT
						+ freeQueryClmForm.getTableClmName() + BLANK
						+ freeQueryClmForm.getOrderType() + ")";
			} else {
				selected = freeQueryClmForm.getTableDialectName() + DOT
						+ freeQueryClmForm.getTableClmName() + BLANK
						+ freeQueryClmForm.getOrderType();
			}
			_orderBy.append(selected);
			if (iterator.hasNext())
				_orderBy.append(COMMA);
		}

		return _selectCols.toString() + _fromTable.toString()
				+ _joinTable.toString() + _whereCondition.toString()
				+ _groupBy.toString() + _orderBy.toString();
	}

	public String parseSql(String sql) {
		List params = paramsParseFactory.getKeyList();
		for (Iterator iterator = params.iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			ProviderParams p = paramsParseFactory
					.getRuntimeDataNewInstance(key);
			sql = sql.replaceAll(key, p.getValue());
		}
		return sql;
	}

	// private String getFilterClmName(String factId) {
	// String sql = "select t.dim_orgfit_clm as fit from aly_fact_table_meta t
	// where t.id=?";
	// List a = getJdbcTemplate().queryForList(sql, new Object[] { factId });
	// String clm = "";
	// if (a.size() > 0) {
	// if (((Map) a.get(0)).get("FIT") != null)
	// clm = ((Map) a.get(0)).get("FIT").toString();
	// }
	//
	// return clm;
	// }

	private boolean getIsGroupBy(List selectColumns) {
		for (Iterator iterator = selectColumns.iterator(); iterator.hasNext();) {
			FreeQueryClmForm freeQueryClmForm = (FreeQueryClmForm) iterator
					.next();
			if (freeQueryClmForm.getGroupBy().booleanValue()) {
				return true;
			}
		}
		return false;
	}

	public int getReportBySQLCount(String sql) throws Exception {
		JdbcTemplate jdbcTemplate = getJdbcTemplate();

		try {
			return jdbcTemplate.queryForInt(sql);
		} catch (Exception e) {
			throw new Exception("请检查SQL的正确性" + "SQL:" + sql);
		}

	}

	/**
	 * 构造SQL排序顺序 重要 此处涉及到TOHTML 是否争取
	 * 
	 * 方法说明:<br>
	 * 创建时间: 2008-12-24 下午04:02:12<br>
	 * 
	 * @throws SQLExecuteException
	 */
	private OrderByObject generateGroupAndOrder(StringBuffer fromTable,
			StringBuffer joinTable, StringBuffer whereCondition, Map orderMap,
			List selectColumns) throws IllegalAccessException,
			InvocationTargetException {
		List temp1 = new ArrayList();
		for (Iterator iterator = selectColumns.iterator(); iterator.hasNext();) {
			FreeQueryClmForm freeQueryClmForm = (FreeQueryClmForm) iterator
					.next();
			if (freeQueryClmForm.getIsGroup().booleanValue()) {
				String sql = SELECT + BLANK + "count( distinct " + BLANK
						+ freeQueryClmForm.getTableClmName() + ")" + BLANK
						+ fromTable.toString() + joinTable.toString()
						+ whereCondition.toString();

				int count;
				try {
					count = getReportBySQLCount(sql);
					FreeQueryClmForm fq = new FreeQueryClmForm();
					BeanUtils.copyProperties(fq, freeQueryClmForm);
					fq.setGroupCount(count);
					temp1.add(fq);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		List temp2 = new ArrayList();
		for (Iterator iterator = selectColumns.iterator(); iterator.hasNext();) {
			FreeQueryClmForm freeQueryClmForm = (FreeQueryClmForm) iterator
					.next();
			if (freeQueryClmForm.getOrderby() != null) {
				if (!"".equals(freeQueryClmForm.getOrderby())) {
					FreeQueryClmForm fq = new FreeQueryClmForm();
					BeanUtils.copyProperties(fq, freeQueryClmForm);
					temp2.add(fq);
				}
			}
		}
		Collections.sort(temp2);
		OrderByObject obo = new OrderByObject();

		if (temp2.size() > 0) {
			obo.orderType = ((FreeQueryClmForm) temp2.get(0)).getOrderType();
		}

		List list = new ArrayList();
		list.addAll(temp1);
		list.addAll(temp2);
		obo.orderList = list;
		return obo;
	}

	public PageBox getQueryDataForPage(String sql, List clmList, int pageSize,
			int currentPageNum, List columNames, List conditionList)
			throws Exception {
		List list = new ArrayList();
		for (int i = 1; i <= clmList.size(); i++) {
			FreeQueryClmForm freeQueryClmForm = (FreeQueryClmForm) clmList
					.get(i - 1);
			freeQueryClmForm.setColPostion(i);
			ColMeta colMeta = new ColMeta(freeQueryClmForm.getTableClmName(),
					freeQueryClmForm.getTableClmDes(), i);
			colMeta.setUnitType(freeQueryClmForm.getUnitType());
			try {
				if (freeQueryClmForm.getTableClmValue() != null) {
					if (!"".equals(freeQueryClmForm.getTableClmValue().trim())) {
						String[] arrayStr = freeQueryClmForm.getTableClmValue()
								.trim().split(",");
						List templist = new ArrayList();
						for (int j = 0; j < arrayStr.length; j++) {
							templist.add(arrayStr[j]);
						}
						colMeta.setTableClmValues(templist);
					}
				}

				BeanUtils.copyProperties(colMeta, freeQueryClmForm);
				if (colMeta.getIsCondtion().booleanValue()
						&& !conditionList.isEmpty()) {
					FreeQueryClmForm conditionForm = getFQClmFormByKey(colMeta
							.getKey(), conditionList);
					if (conditionForm != null) {
						colMeta.setCoditionValue(conditionForm
								.getCoditionValue());
						colMeta.setCoditionValueCode(conditionForm
								.getCoditionValueCode());
					}
				}
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			list.add(colMeta);
		}
		return getQueryDataForPage(sql, list, pageSize, currentPageNum,
				columNames);
	}

	// public PageBox getQueryDataForPage1(String sql, List clmList, int
	// pageSize,
	// int currentPageNum, List columNames) throws Exception {
	//
	// if (clmList == null)
	// return null;
	//
	// FreeQueryData qd = new FreeQueryData();
	// Map metasMap = new HashMap();
	// for (int i = 0; i < clmList.size(); i++) {
	// ColMeta colMeta = (ColMeta) clmList.get(i);
	// metasMap.put(colMeta.getKey(), colMeta);
	// qd.addColMeta(colMeta);
	// }
	// qd.setColumnMetasMap(metasMap);
	// PageBox pb = getPageBox(sql, pageSize, currentPageNum, columNames,
	// "oracle");
	// List data = pb.getPageList();
	// buildQueryData(qd, data);
	// pb.setQueryData(qd);
	// return pb;
	// }

	public FreeQueryClmForm getFQClmFormByKey(String key, List list) {
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			FreeQueryClmForm freeQueryClmForm = (FreeQueryClmForm) iterator
					.next();
			if (key.equals(freeQueryClmForm.getTableClmName())) {
				return freeQueryClmForm;
			}
		}
		return null;
	}

	/**
	 * 构造querydata 方法 重要 方法说明:<br>
	 * 创建时间: 2009-1-7 下午02:05:51<br>
	 * 
	 * @throws Exception
	 * @throws SQLExecuteException
	 * @throws FormulaDefineException
	 */
	public PageBox getQueryDataForPage(String sql, List clmList, int pageSize,
			int currentPageNum, List columNames) throws Exception {

		if (clmList == null)
			return null;

		FreeQueryData qd = new FreeQueryData();
		Map metasMap = new HashMap();
		for (int i = 0; i < clmList.size(); i++) {
			ColMeta colMeta = (ColMeta) clmList.get(i);
			metasMap.put(colMeta.getKey(), colMeta);
			qd.addColMeta(colMeta);
		}
		qd.setColumnMetasMap(metasMap);
		PageBox pb = getPageBox(sql, pageSize, currentPageNum, columNames,
				"oracle");
		List data = pb.getPageList();
		buildQueryData(qd, data);
		pb.setQueryData(qd);
		return pb;
	}

	private CellValue getCellValue(Object objectValue, ColMeta columnMeta) {
		CellValue cv;
		if (objectValue == null) {
			if (AnalyseCst.NUMBER.equals(columnMeta.getTableClmType())) {
				cv = new NumericValue(0, columnMeta.getKey());
			} else {
				cv = new StringValue("-", columnMeta.getKey());
			}
		} else if (objectValue instanceof String) {
			cv = new StringValue((String) objectValue, columnMeta.getKey());
		} else if (objectValue instanceof Number) {
			cv = new NumericValue(((Number) objectValue).doubleValue(),
					columnMeta.getKey());
		} else {
			cv = new StringValue(String.valueOf(objectValue), columnMeta
					.getKey());
		}
		if (columnMeta.isCustom()) {
			cv.setCustomCell(true);
		}
		if (columnMeta.isEditor()) {
			cv.setEditor(true);
		}

		if (StringUtils.isNotBlank(columnMeta.getFlmMsg())) {
			cv.setFlmMsg(columnMeta.getFlmMsg());
		}
		return cv;
	}

	private boolean isEqCondition(List conditionList, Map rowMap,
			ColMeta columnMeta) {
		for (Iterator iterator = conditionList.iterator(); iterator.hasNext();) {
			CellValue cv = (CellValue) iterator.next();
			String key = cv.getColKey();
			String type;
			if (cv instanceof NumericValue) {
				type = AnalyseCst.NUMBER;
			} else {
				type = AnalyseCst.STRING;
			}
			ColMeta cm = new ColMeta(key, "", -2);
			cm.setTableClmType(type);
			cm.setCustom(columnMeta.isCustom());
			cm.setEditor(columnMeta.isEditor());
			CellValue newCv = getCellValue(rowMap.get(key), cm);

			if (!cv.equals(newCv)) {
				return false;
			}
		}
		return true;
	}

	private int getColIndex(ColMeta nextMeta, List metasList) {
		for (int i = 0; i < metasList.size(); i++) {
			ColMeta colMeta = (ColMeta) metasList.get(i);
			if (nextMeta.getKey().equals(colMeta.getKey())) {
				return i;
			}
		}
		return -1;
	}

	private int getGroupTotalCount(List data, int startRow, List conditionList,
			boolean hasChild, ColMeta nextMeta, Set childSet, List metasList,
			ColMeta columnMeta) {
		int totalCount = 0;
		for (int i = startRow; i < data.size(); i++) {
			Map rowMap = (Map) data.get(i);
			boolean isEq = isEqCondition(conditionList, rowMap, columnMeta);
			if (isEq) {
				if (hasChild) {

					int colIndex = getColIndex(nextMeta, metasList);
					if (colIndex > -1) {
						List list = bulidConditionList(colIndex, metasList,
								rowMap);
						String key = FreeQueryUtil.bulidKeyByThisList(list);
						childSet.add(key);
					}

				}
				totalCount++;
			} else {
				break;
			}
		}
		return totalCount;
	}

	private ColMeta findNextGroupMeta(ColMeta columnMeta, List metaList) {

		int index = 9999;
		int count = metaList.size();
		for (int i = 0; i < count; i++) {
			ColMeta meta = (ColMeta) metaList.get(i);
			if (columnMeta.getKey().equals(meta.getKey())) {
				index = i;
				break;
			}
		}
		while (index < count - 1) {
			index++;
			ColMeta meta = (ColMeta) metaList.get(index);
			if (meta.getIsGroup().booleanValue()) {
				return meta;
			}
		}
		return null;
	}

	private FreeQueryGroupModel buildGroupModel(List data, int thisRow,
			CellValue cv, ColMeta columnMeta, List metaList) {
		FreeQueryGroupModel fqm;

		boolean hasChild = false;

		ColMeta nextGroupMeta = findNextGroupMeta(columnMeta, metaList);
		Set childSet = new HashSet();
		if (nextGroupMeta != null) {
			hasChild = true;
		}
		int groupCount = getGroupTotalCount(data, thisRow, cv
				.getConditionList(), hasChild, nextGroupMeta, childSet,
				metaList, columnMeta);

		fqm = new FreeQueryGroupModel(thisRow, groupCount, cv);
		fqm.setThisMeta(columnMeta);
		fqm.setNextMeta(nextGroupMeta);
		fqm.setHasChild(hasChild);
		fqm.setChildSet(childSet);
		return fqm;
	}

	private List bulidConditionList(int col, List metasList, Map rowMap) {

		List conditionList = new LinkedList();
		int i = 0;
		while (col >= i) {
			ColMeta columnMeta = (ColMeta) metasList.get(i);
			String key = columnMeta.getKey();
			CellValue cv = this.getCellValue(rowMap.get(key), columnMeta);
			conditionList.add(cv);
			i++;
		}
		return conditionList;
	}

	private void buildQueryData(FreeQueryData freeQueryData, List data)
			throws DataTypeException {
		Map totalValueMap = new HashMap();
		Map totalTempMap = new HashMap();
		List columnMetaList = freeQueryData.getColsMetas();

		Map groupCountMap = freeQueryData.getGroupModelMap();

		for (int i = 0; i < data.size(); i++) {
			int thisRow = i;
			Map rowMap = (Map) data.get(i);
			List list = new ArrayList();
			// 循环meta 信息
			for (int j = 0; j < columnMetaList.size(); j++) {
				int thisCol = j;
				ColMeta columnMeta = (ColMeta) columnMetaList.get(j);
				boolean isGroup = columnMeta.getIsGroup().booleanValue();
				String columnKey = columnMeta.getKey();
				CellValue cv = getCellValue(rowMap.get(columnKey), columnMeta);
				if (isGroup) {

					List conditionList = bulidConditionList(thisCol,
							columnMetaList, rowMap);
					cv.setConditionList(conditionList);
					String conditionKey = cv.getConditionKey();
					FreeQueryGroupModel fqm;
					if (groupCountMap.get(cv.getConditionKey()) == null) {

						fqm = buildGroupModel(data, thisRow, cv, columnMeta,
								columnMetaList);
						groupCountMap.put(cv.getConditionKey(), fqm);

						Row totalRow = bulidTotalData(columnKey,
								columnMetaList, rowMap, cv.getConditionList());
						totalTempMap.put(cv.getConditionKey(), totalRow);
						list.add(cv);
					} else {

						fqm = (FreeQueryGroupModel) groupCountMap
								.get(conditionKey);
						list.add(cv);
						if (totalTempMap.get(conditionKey) != null) {
							sumThisRow(totalTempMap, conditionKey, rowMap,
									columnMetaList, cv.getConditionList());
						}
					}
					if (isGroup) {
						totalValueMap.put(conditionKey, (Row) totalTempMap
								.get(conditionKey));
					}
				} else {
					list.add(cv);
				}

			}
			Row row = new Row(list, false);
			freeQueryData.addRow(row);

		}
		freeQueryData.setTotalValueMap(totalValueMap);
		freeQueryData.effectGroupArea();
		FreeQueryDataHelper.parseQueryData(freeQueryData);
	}

	private Row bulidTotalData(String key, List clmList, Map map,
			List conditionList) {
		List dataList = new ArrayList();
		String sumKey = null;
		CellValue sumCv = null;
		for (int i = 0; i < clmList.size(); i++) {
			ColMeta cm = (ColMeta) clmList.get(i);
			String keyValue = cm.getKey();
			CellValue cv = getCellValue(map.get(keyValue), cm);
			if (key.equals(keyValue)) {
				StringValue sv = new StringValue("合计", keyValue);
				sv.setSumCell(true);
				sumCv = cv;
				cv.setConditionList(conditionList);
				sumKey = cm.getKey();
				dataList.add(sv);
			} else {
				dataList.add(cv);
			}
		}
		Row row = new Row(dataList, true);
		if (sumKey != null && sumCv != null) {
			row.setSumColKey(sumKey);
			row.setSumCellValue(sumCv);
		}
		return row;
	}

	private void sumThisRow(Map totalMap, String conditionKey, Map map,
			List cmList, List conditionList) throws DataTypeException {
		Row row = (Row) totalMap.get(conditionKey);
		List dataList = buildCurrentRowData(cmList, map, conditionList);
		row.sumRowData(dataList);
	}

	private List buildCurrentRowData(List clmList, Map rowMap,
			List conditionList) {
		List dataList = new ArrayList();
		for (int i = 0; i < clmList.size(); i++) {
			ColMeta cm = (ColMeta) clmList.get(i);
			String key = cm.getKey();
			CellValue cv = this.getCellValue(rowMap.get(key), cm);
			dataList.add(cv);
		}
		return dataList;
	}

	public String getCountAllSql4DB(String sql, String dbType) {
		String query4count;
		if (dbType.equals(AnalyseCst.DB_TYPE_IFX)) {
			query4count = "select count(*) from TABLE(MULTISET  (" + sql + "))";
		} else if (dbType.equals(AnalyseCst.DB_TYPE_DB2)) {
			query4count = "select count(*) from (" + sql + ") as t";
		} else {
			query4count = "select count(*) from (" + sql + ")";
		}
		return query4count;
	}

	public PageBox getPageBox(String sql, final int pageSize,
			final int currentPageNum, final List columNames, String dbType)
			throws Exception {

		try {
			JdbcTemplate jdbcTemplate = getJdbcTemplate();

			String countsql = getCountAllSql4DB(sql, dbType);

			int count = jdbcTemplate.queryForInt(countsql);
			// int indexCountSubStatement = sql.indexOf(" count");
			PageBox pageBox = new PageBox();
			final java.util.List pageList = new ArrayList();
			PageObject pageObject = new PageObject();
			pageObject.setPageSize(pageSize);
			pageObject.setPageIndex(currentPageNum);
			// String query4count = "";
			// query4count = getCountAllSql4DB(sql, dbType);
			// final int itemAmount = indexCountSubStatement != -1 ? 1
			// : ((Integer) jdbcTemplate.queryForObject(query4count,
			// Integer.class)).intValue();
			// pageObject.setItemAmount(itemAmount);
			String pageSql = getPageingSql4DB(sql, dbType);
			final int pageStartIndex = pageObject.getBeginIndex();
			final int pageEndIndex = pageObject.getEndIndex();

			jdbcTemplate.query(pageSql, new Object[] { new Long(pageEndIndex),
					new Long(pageStartIndex) }, new ColumnMapRowMapper() {
				public Object mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					// rowNum = pageEndIndex 是为了判断后面还有没有数
					// if (((rowNum+1) >= pageStartIndex)
					//								
					// && ((rowNum+1) <= pageEndIndex)) {
					ResultSetMetaData rsmd = null;
					int columnCount = 0;
					if (columNames.size() == 0) {
						rsmd = rs.getMetaData();
						columnCount = rsmd.getColumnCount();
					} else {
						columnCount = columNames.size();
					}
					Map mapOfColValues = createColumnMap(columnCount);
					for (int i = 1; i <= columnCount; i++) {
						String key = null;
						if (rsmd == null) {
							key = (String) columNames.get(i - 1);
						} else {
							key = getColumnKey(rsmd.getColumnName(i));
							columNames.add(key);
						}
						Object obj = getColumnValue(rs, i);
						mapOfColValues.put(key, obj);
					}
					pageList.add(mapOfColValues);
					return mapOfColValues;
					// } else {
					// return null;
					// }
				}
			});
			pageObject.setItemAmount(count);
			pageObject
					.setHasNextPage(pageObject.getPageAmount() > currentPageNum);
			// if(pageList.size() > pageObject.getPageSize()){
			// pageObject.setHasNextPage(true);
			// pageList.remove(pageList.size()-1);
			// }else{
			// pageObject.setHasNextPage(false);
			// }
			pageBox.setPageObject(pageObject);
			pageBox.setPageList(pageList);

			return pageBox;
		} catch (BadSqlGrammarException e) {
			throw new Exception("请检查SQL的正确性" + "SQL:" + sql);
		}

	}

	public String getPageingSql4DB(String sql, String dbType) {

		if (dbType.equals(AnalyseCst.DB_TYPE_ORACLE)) {
			StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);

			pagingSelect
					.append("select * from ( select row_.*, rownum rownum_ from ( ");
			pagingSelect.append(sql);
			pagingSelect.append(" ) row_ ) where rownum_ < ? and rownum_ >= ?");
			return pagingSelect.toString();

		} else if (dbType.equals(AnalyseCst.DB_TYPE_DB2)) {

		} else {

		}
		return sql;
	}

	public ParamsParseFactory getParamsParseFactory() {
		return paramsParseFactory;
	}

	public void setParamsParseFactory(ParamsParseFactory paramsParseFactory) {
		this.paramsParseFactory = paramsParseFactory;
	}

	public LoginDO getUser() {
		return user;
	}

	public void setUser(LoginDO user) {
		this.user = user;
	}
}
