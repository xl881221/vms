package fmss.common.ui.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;


/**
 * 类说明: 自由查询模型的 helper 类<br>
 * 创建时间: 2009-1-12 上午08:56:36<br>
 * 
 * @author 杨旭飞<br>
 * @email: yangxufei312@yahoo.com.cn<br>
 */
public class FreeQueryDataHelper {

	/**
	 * 对 指定列进行分组合计排序 方法说明:<br>
	 * 创建时间: 2009-1-9 下午04:49:42<br>
	 * 
	 * @throws DataTypeException
	 */
	public static void groupOutSort(FreeQueryData freeQueryData,
			ColMeta targetColumnMeta, String orderModel)
			throws DataTypeException {
		Map groupMap = freeQueryData.getGroupModelMap();

		for (Iterator iterator = groupMap.keySet().iterator(); iterator
				.hasNext();) {
			String key = (String) iterator.next();
			FreeQueryGroupModel fq = (FreeQueryGroupModel) groupMap.get(key);
			Row totalRow = fq.getTotalRow();
			CellValue totalValue = totalRow.getCvByTarget(targetColumnMeta
					.getKey());
			fq.setTotalCellValue(totalValue);
		}

		int targetIndex = targetColumnMeta.getPosition();

		List rowDataList = buildSortGroupOut(freeQueryData, orderModel,
				targetIndex);

		freeQueryData.setRows(rowDataList);
	}

	private static List buildSortGroupOut(FreeQueryData freeQueryData,
			String orderModel, int targetIndex) {

		Map groupModelMap = freeQueryData.getGroupModelMap();

		ColMeta firstGroupMeta = FreeQueryDataHelper.getFirstGroupMeta(freeQueryData, targetIndex);

		List firstMetaGroupModelList = getGroupModelsByMeta(groupModelMap.values(), firstGroupMeta);

		List list = sortGroupOut(firstMetaGroupModelList, orderModel,
				groupModelMap, freeQueryData, null);

		return list;
	}

	private static List sortGroupOut(List dataList, String orderModel,
			Map groupModelMap, FreeQueryData freeQueryData,
			FreeQueryGroupModel prevFqm) {
		List rowList = new ArrayList();

		Collections.sort(dataList);
		if (AnalyseCst.SORT_MODE_DESC.equals(orderModel)) {
			Collections.reverse(dataList);
		}
		for (int i = 0; i < dataList.size(); i++) {
			FreeQueryGroupModel fqm = (FreeQueryGroupModel) dataList.get(i);
			if (fqm.isHasChild()) {
				Set childSet = fqm.getChildSet();
				List tempDataList = FreeQueryDataHelper.findGroupMeta(fqm
						.getCv().getConditionList(), childSet, groupModelMap);
				List list = sortGroupOut(tempDataList, orderModel,
						groupModelMap, freeQueryData, fqm);

				rowList.addAll(list);
			} else {
				int startRow = fqm.getModStartRow();
				int endRow = fqm.getModEndRow();
				while (startRow <= endRow) {

					Row thisRow = freeQueryData.getRowObject(startRow);

					rowList.add(thisRow);

					startRow++;
				}
			}
		}
		return rowList;
	}

	/**
	 * 方法说明:根据前一项目的 prevConditionList 添加 进入包含在 childSet 中的子节点组合成 key 将所属的 分组
	 * mode 取出 存入List 以用作排序
	 * 
	 * @param prevConditionList
	 *            上级决定 上级的条件列表
	 * @param childSet
	 *            包含的 子节点
	 * @param groupModelMap
	 *            所有的 groupModelMap 创建时间: 2009-1-16 下午02:40:49<br>
	 */
	private static List findGroupMeta(List prevConditionList, Set childSet,
			Map groupModelMap) {
		List metaList = new LinkedList();
		List prevList = new LinkedList();
		prevList.addAll(prevConditionList);
		for (Iterator iterator = childSet.iterator(); iterator.hasNext();) {

			String key = (String) iterator.next();
			if (groupModelMap.get(key) != null) {
				metaList.add((FreeQueryGroupModel) groupModelMap.get(key));

			}
		}
		return metaList;
	}

	private static List getGroupModelsByMeta(Collection allGroupModelList,
			ColMeta meta) {
		List metaList = new LinkedList();
		for (Iterator iterator = allGroupModelList.iterator(); iterator
				.hasNext();) {
			FreeQueryGroupModel fqm = (FreeQueryGroupModel) iterator.next();

			if (meta.getKey().equals(fqm.getThisMeta().getKey())) {
				metaList.add(fqm);
			}
		}

		return metaList;
	}

	public static List getPrevGroupMetaList(FreeQueryData freeQueryData,
			int targetIndex) {
		int i = 1;
		List metaList = new ArrayList();
		while (i < targetIndex) {
			ColMeta cm = freeQueryData.getMetaByPostion(i);
			if (cm != null) {
				if (cm.getIsGroup().booleanValue()) {
					metaList.add(cm);
				}
			}
			i++;
		}
		return metaList;
	}

	public static ColMeta getFirstGroupMeta(FreeQueryData freeQueryData,
			int targetIndex) {
		int i = 1;
		while (i <= targetIndex) {
			ColMeta cm = freeQueryData.getMetaByPostion(i);
			if (cm != null) {
				if (cm.getIsGroup().booleanValue()) {
					return cm;
				}
			}
			i++;
		}
		return null;
	}

	/**
	 * 方法说明: 为该数据模型 增加一个空白列<br>
	 * 创建时间: 2009-2-3 上午11:02:30<br>
	 */
	public static void addCustomBlankColumn(FreeQueryData fq, ColumnPostion cp,
			String customColumnMetaKey, String customColumnMetaName,
			String customColumnMetaType) {

		if (cp.isEnd()) { // 结尾添加行
			int postion = fq.getMaxColumnPostion() + 1;
			ColMeta custiomColumnMeta = new ColMeta(customColumnMetaKey,
					customColumnMetaName, postion);
			custiomColumnMeta.setTableClmType(customColumnMetaType);
			custiomColumnMeta.setCustom(true);
			custiomColumnMeta.setEditor(true);
			custiomColumnMeta.setIsDisplay(new Boolean(true));
			fq.setCustomMeta(custiomColumnMeta);
			fq.addCustomColumn(customColumnMetaKey, customColumnMetaType,
					AnalyseCst.CUSTOM_MODEL_BLANK, postion, null);
		}
	}
	/**
	 * 方法说明: 对 QURRY DATA 进行公式解析 并且解析合计行<br>
	 * 创建时间: 2009-2-10 下午04:34:44<br>
	 */
	public static void parseQueryData(FreeQueryData freeQueryData) {
		List metaList = freeQueryData.getColsMetas();
		AnalyseContext ctx = new AnalyseContext();
		ctx.setContext(AnalyseContext.CURR_QUERY_DATA, freeQueryData);

		for (int i = 0; i < freeQueryData.getRows().size(); i++) {
			Row row = (Row) freeQueryData.getRows().get(i);
			ctx.setContext(AnalyseContext.CURR_EFFECTIVE_DATA_TYPE,AnalyseCst.ROW);
			ctx.setContext(AnalyseContext.CURR_EFFECTIVE_DATA, row);
			try {
				parseRow(metaList, ctx, row);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		parseTotalRow(ctx, metaList, freeQueryData.getGroupModelMap(),
				freeQueryData.getTotalValueMap());
	}

	/**
	 * 解析合计行 方法说明:<br>
	 * 创建时间: 2009-2-10 下午04:33:54<br>
	 */
	private static void parseTotalRow(AnalyseContext ctx, List metaList,
			Map groupModelMap, Map totalValueMap) {

		for (Iterator iterator = groupModelMap.keySet().iterator(); iterator
				.hasNext();) {
			String key = (String) iterator.next();
			Row totalRow = (Row) totalValueMap.get(key);
			ctx.setContext(AnalyseContext.CURR_EFFECTIVE_DATA_TYPE,AnalyseCst.ROW);
			ctx.setContext(AnalyseContext.CURR_EFFECTIVE_DATA, totalRow);
			try {
				parseRow(metaList, ctx, totalRow);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 解析 ROW
	 * 方法说明:<br>
	 * 创建时间: 2009-2-10 下午04:34:17<br>
	 * @throws Exception 
	 */
	private static void parseRow(List metaList, AnalyseContext ctx, Row row) throws Exception {
		for (int j = 0; j < metaList.size(); j++) {

			CellValue cv = (CellValue) row.getRowData().get(j);

			if (cv.isCustomCell() && StringUtils.isNotBlank(cv.getFlmMsg())) {

				Expression exp = new FreeQueryExpression(cv.getFlmMsg());
				try {
					exp.calc(ctx);

					Object object = ctx.getContext(AnalyseContext.REALTIME_CALC_RESULT);
					
					Double d = (Double) object;
					if(d.isNaN()){
						d = new Double(0);
					}
					NumericValue nv = (NumericValue) cv;
					nv.setNumericValue(d.doubleValue());
					cv.setEditor(false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					throw new Exception(e.getMessage());
				}
			}

		}
	}
}
