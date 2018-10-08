/**
 * 
 */
package fmss.action;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.servlet.http.HttpSession;

import fmss.common.ui.controller.AnalyzingUtil;
import fmss.common.ui.controller.CellValue;
import fmss.common.ui.controller.ColMeta;
import fmss.common.ui.controller.CurrnetQueryReport;
import fmss.common.ui.controller.FreeQueryClmForm;
import fmss.common.ui.controller.FreeQueryData;
import fmss.common.ui.controller.FreeQueryForm;
import fmss.common.ui.controller.FreeQueryService;
import fmss.common.ui.controller.FreeQueryUtil;
import fmss.common.ui.controller.PageBox;
import fmss.common.ui.controller.ReportViewObject;
import fmss.common.ui.controller.Row;
import fmss.dao.entity.LoginDO;
import fmss.common.util.Constants;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;


/**
 * 自由查询Action
 * 
 * @author liuhaibo
 * 
 */
public class FreeQueryAction extends BaseAction {
	
	
	private static HSSFCellStyle titleStyle;
	private static HSSFCellStyle resultStyle;
	private static HSSFCellStyle headStyle;
	private static HSSFFont font;
	private static final int excel_max_row = 65535;
	private static final String excel_last_sheet_name = "sheetFinal";
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FreeQueryForm freeQueryForm;
	private String domElementsList;
	private String domElements;
	
	private List recordList;

	protected int curpage = 1;

	protected int pageSize = 50;

	private ReportViewObject reportViewObject;
	private String analyzingURL;

	private FreeQueryService freeQueryService;

	public FreeQueryForm getFreeQueryForm() {
		return freeQueryForm;
	}

	public void setFreeQueryForm(FreeQueryForm freeQueryForm) {
		this.freeQueryForm = freeQueryForm;
	}

	public String getDomElementsList() {
		return domElementsList;
	}

	public void setDomElementsList(String domElementsList) {
		this.domElementsList = domElementsList;
	}

	public String getDomElements() {
		return domElements;
	}

	public void setDomElements(String domElements) {
		this.domElements = domElements;
	}

	public FreeQueryService getFreeQueryService() {
		return freeQueryService;
	}

	public void setFreeQueryService(FreeQueryService freeQueryService) {
		this.freeQueryService = freeQueryService;
	}
	
	public String freeQueryList(){
		recordList = freeQueryService.getAlySchemeMetaList();
		return SUCCESS;
	}

	/**
	 * @return 第一个查询条件的页面
	 */
	public String freeQueryCondition() {

		try {
			String schemeCode = request.getParameter("schemeCode");
			freeQueryForm = freeQueryService.getFreeQueryForm(schemeCode);
			domElementsList = freeQueryService
					.getDomElementsList(freeQueryForm);
			domElements = freeQueryService.getDomElements(freeQueryForm);

			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ERROR;
	}

	/**
	 * @return查询结果的页面
	 */
	public String freeQueryData() {
		//机构权限处理
		LoginDO user = (LoginDO) super.get(Constants.LOGIN_USER);
		freeQueryService.setUser(user);
		
		
		System.out.println("------------------");
		System.out.println(curpage);
		System.out.println("------------------");
		
		
		
		String result = request.getParameter("result");
		if(StringUtils.isNotEmpty(result)&&request.getParameter("curpage")==null){
			return SUCCESS;
		}
		String schemeCode = request.getParameter("schemeCode");
		FreeQueryForm form = new FreeQueryForm();
		HttpSession session = request.getSession();
		try {
			if (AnalyzingUtil.isRefreshForm(session, schemeCode,
					"freeQueryForm")) {
				form = (FreeQueryForm) freeQueryService
						.getFreeQueryForm(schemeCode);
				session.setAttribute("freeQueryForm", form);
			} else {
				form = (FreeQueryForm) session.getAttribute("freeQueryForm");
			}
			CurrnetQueryReport currnetQueryReport = new CurrnetQueryReport(
					form, pageSize, curpage);
//			Map conditionMap = request.getParameterMap();
			List tempL = form.getConditionList();
			Map paramsMap = new HashMap();
			for(int i = 0;i < tempL.size();i++){
				FreeQueryClmForm freeQueryClmForm = (FreeQueryClmForm)tempL.get(i);
				String value = request.getParameter(freeQueryClmForm.getTableClmName());
				if(StringUtils.isNotEmpty(value)){
					paramsMap.put(freeQueryClmForm.getTableClmName(), value);
				}
			}
			doCondition(currnetQueryReport.getFreeQueryForm(), paramsMap);

			this.reportViewObject = generateReportView(currnetQueryReport);
			session.setAttribute("reportViewObject", reportViewObject);
			session.setAttribute("queryp", paramsMap);
			if(request.getParameter("curpage")!=null){
				return "success";
				
			}
			else{
				response.getWriter().print("{success:true,msg:'成功'}");
				
				
			}
			
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	private void doCondition(FreeQueryForm form, Map paramsMap) {
		List condtionList = form.getConditionList();
		for (Iterator iterator = condtionList.iterator(); iterator.hasNext();) {
			FreeQueryClmForm freeQueryClmForm = (FreeQueryClmForm) iterator
					.next();
			String value = (String) paramsMap.get(freeQueryClmForm
					.getTableClmName());
			String result = "";
			if (value != null && !"".equals(value.trim())) {
				result = FreeQueryUtil.processStr(value, freeQueryClmForm);
			}
			freeQueryClmForm.setCoditionValueCode(value);
			freeQueryClmForm.setCoditionValue(result);
		}
	}
	
	public void exportExcel(){
		FreeQueryForm form = new FreeQueryForm();
		HttpSession session = request.getSession();
		form = (FreeQueryForm) session.getAttribute("freeQueryForm");
		String ps=request.getParameter("pageSize");
		String cp=request.getParameter("curpage");CurrnetQueryReport currnetReport=null;
		if(ps!=null&&cp!=null&&!ps.equals("")&&!cp.equals("")){
			 currnetReport = new CurrnetQueryReport(form,Integer.parseInt(ps), Integer.parseInt(cp));
		}else{
			 currnetReport = new CurrnetQueryReport(form,1000000, 1);
			
		}
		

		if (currnetReport != null) {
			FreeQueryForm baseSchemeForm = currnetReport.getFreeQueryForm();
//			BaseDataService baseDataService = serviceProviderFactory.getRuntimeDataNewInstance(baseSchemeForm.getSchemeType());
			PageBox pb = null;
			try {
				pb = freeQueryService.getQueryDataForPage(baseSchemeForm, currnetReport.getPageSize(), currnetReport.getCurpage());
			}  catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			List columList = ((FreeQueryData)pb.getQueryData()).getColsMetas();
			List resultList = ((FreeQueryData)pb.getQueryData()).getRows();
			String title = baseSchemeForm.getSchemeCode();
//			if (resultList.isEmpty()) {
//				throw new RuntimeException("导出数据源为空：请检查数据源: ");
//			} else
			{
				HSSFWorkbook wb = new HSSFWorkbook();
				titleStyle(wb);
				resultStyle(wb);
				headStyle(wb);
				int maxRowSize = excel_max_row;
				writeWorkbook(wb, columList, resultList, maxRowSize,title);
				reNameSheetName(wb);

				response.addHeader("Content-Disposition",
						"attachment;filename=" + getCurrentDate()
								+ "Export.xls");
				response.setContentType("application/excel");
				OutputStream ops;
				try {
					ops = response.getOutputStream();
					wb.write(ops);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}else{
			throw new NullPointerException("CurrnetQueryReport is null! please check it!");
		}
	}
	private String getCurrentDate() {
		Date now = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(now);
	}
	private void reNameSheetName(HSSFWorkbook wb) {
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			wb.setSheetName(i, "sheet" + i);
		}
	}
	
	private int createCurrRowTitle(HSSFSheet sheet, int rowNum, List columList,
			HSSFCellStyle style) {
		sheet.createRow(rowNum++);
		int n = 0;
		HSSFRow rh = sheet.getRow(rowNum - 1);
		for (int i = 0; i < columList.size(); i++) {
			ColMeta col = (ColMeta) columList.get(i);
			if (col.getIsDisplay().booleanValue()) {
				HSSFCell ch = rh.createCell((short) n++);
				ch.setCellValue(new HSSFRichTextString(col.getName()));
				ch.setCellStyle(style);
			}
		}
		return rowNum;
	}
	
	private int writeResult(HSSFSheet sheet, int rowNum, List columList,
			List resultList, HSSFCellStyle style) {
		Stack stack = new Stack();
		for (int i = 0; i < resultList.size(); i++) {
			Row row = (Row) resultList.get(i);

			sheet.createRow(rowNum++);
			int n = 0;
			HSSFRow r = sheet.getRow(rowNum - 1);
			for (int j = 0; j < columList.size(); j++) {
				ColMeta col = (ColMeta) columList.get(j);
				CellValue cellValue = null;
				if (col.getIsDisplay().booleanValue()) {
					HSSFCell c = r.createCell((short) n++);
					cellValue = (CellValue) row.getRowData().get(
							col.getPosition() - 1);
					
					c.setCellValue(new HSSFRichTextString(cellValue
							.getValueByString()));
					c.setCellStyle(style);
				}
				// 表格分组
				if (i > 0) {
					if (col.getIsGroup().booleanValue()) {
						Row oldRow = (Row) stack.peek();
						CellValue oldCellValue = (CellValue) oldRow
								.getRowData().get(col.getPosition() - 1);
						if (cellValue.getValueByString().equals(
								oldCellValue.getValueByString())) {
							sheet.addMergedRegion(new Region(rowNum - 2,
									(short) (n - 1), rowNum - 1,
									(short) (n - 1)));
						}

					}
				}
			}

			stack.push(row);
		}
		stack.clear();
		return rowNum;
	}
	
	private void writeWorkbook(HSSFWorkbook wb, List columList,
			List resultList, int maxRowSize,String title) {
		if (resultList.size() <= maxRowSize) {

			int rowNum = 0;
			HSSFSheet sheet = wb.createSheet();
			sheet.setDefaultColumnWidth((short) 15);
			wb.setSheetName(0, excel_last_sheet_name);
			rowNum = writeHead(sheet, rowNum, columList, headStyle,title);
			rowNum = createCurrRowTitle(sheet, rowNum, columList, titleStyle);
			rowNum = writeResult(sheet, rowNum, columList, resultList,
					resultStyle);
		} else {
			int batchTimes = resultList.size() / maxRowSize;
			boolean haveElementsNotAfullSheet = resultList.size() % maxRowSize != 0;
			for (int i = 0; i < batchTimes; i++) {
				int dynaStartPos = i * maxRowSize;
				HSSFSheet sheet = wb.createSheet();
				if ((i == batchTimes - 1) && (!haveElementsNotAfullSheet)) {
					wb.setSheetName(i, excel_last_sheet_name);
				} else {
					wb.setSheetName(i, "sheet" + i);
				}
				int rowNum = 0;
				rowNum = writeHead(sheet, rowNum, columList, titleStyle,title);
				List subParameterList = resultList.subList(dynaStartPos,
						dynaStartPos + maxRowSize);
				rowNum = writeResult(sheet, rowNum, columList,
						subParameterList, resultStyle);
			}
			if (haveElementsNotAfullSheet) {
				int finalPos = batchTimes * maxRowSize;
				int rowNum = 0;
				HSSFSheet sheet = wb.createSheet(excel_last_sheet_name);
				rowNum = writeHead(sheet, rowNum, columList, titleStyle,title);
				List subParameterList = resultList.subList(finalPos, resultList
						.size());
				writeResult(sheet, rowNum, columList, subParameterList,
						resultStyle);
			}
		}
	}
	
	private int writeHead(HSSFSheet sheet, int rowNum, List columList,
			HSSFCellStyle style,String title) {
		sheet.createRow(rowNum++);
		int n = 0;
		HSSFRow r = sheet.getRow(rowNum - 1);
		for (int i = 0; i < columList.size(); i++) {
			ColMeta col = (ColMeta) columList.get(i);
			if (col.getIsDisplay().booleanValue()) {
				HSSFCell c = r.createCell((short) n++);
				c.setCellValue(new HSSFRichTextString(title));
				c.setCellStyle(style);
			}
		}

		sheet.addMergedRegion(new Region(rowNum - 1, (short) 0, rowNum - 1,
				(short) --n));
		return rowNum;
	}
	
	private static HSSFCellStyle resultStyle(HSSFWorkbook work) {
		resultStyle = work.createCellStyle();
		resultStyle.setBorderLeft((short) 1);
		resultStyle.setBorderRight((short) 1);
		resultStyle.setBorderBottom((short) 1);
		resultStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		resultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		return resultStyle;
	}

	private static HSSFCellStyle headStyle(HSSFWorkbook work) {
		headStyle = work.createCellStyle();
		headStyle.setBorderLeft((short) 1);
		headStyle.setBorderRight((short) 1);
		headStyle.setBorderBottom((short) 1);
		headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		return headStyle;
	}
	
	private static HSSFCellStyle titleStyle(HSSFWorkbook work) {
		titleStyle = work.createCellStyle();
		font = work.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);// 加粗
		font.setFontHeightInPoints((short) 12);// 设置字体大小
		font.setColor(HSSFColor.BLUE_GREY.index);// 设置字体颜色
		titleStyle.setBorderLeft((short) 1);// 设置左边框
		titleStyle.setBorderRight((short) 1);// 设置右边框
		titleStyle.setBorderBottom((short) 1);// 设置底边框
		titleStyle.setBorderTop((short) 1);// 设置顶部边框
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中显示
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		titleStyle.setFont(font);
		return titleStyle;
	}

	public ReportViewObject getReportViewObject() {
		return reportViewObject;
	}

	public void setReportViewObject(ReportViewObject reportViewObject) {
		this.reportViewObject = reportViewObject;
	}

	public ReportViewObject generateReportView(
			CurrnetQueryReport currnetQueryReport) throws Exception {
		FreeQueryForm form = (FreeQueryForm) currnetQueryReport
				.getFreeQueryForm();

		PageBox pb = new PageBox();
		;
		try {
			pb = freeQueryService.getQueryDataForPage(form, pageSize, curpage);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		FreeQueryData data = (FreeQueryData) pb.getQueryData();
		String html = freeQueryService.toHtml(data, form.getSchemeName(), form
				.getUnit(), form.getDecimalDigits());
		String graphJson = null;
		if (form.getGraphChartList() == null) {
			form.setGraphChartList(new LinkedList());
		}
		List allGraphList = new LinkedList();// 存储所有图形
		List formGraphList = form.getGraphChartList();// 方案中己经存在的图形

		// 数据库中该方案的图形

		if (formGraphList.size() > 0) {
			allGraphList.addAll(formGraphList);
		}

		// System.out.println(graphJson);
		return new ReportViewObject(form.getSchemeCode(), html, graphJson, pb
				.getPageObject(), form.getSql(), data.getColsMetas());
	}

	public String getAnalyzingURL() {
		return analyzingURL;
	}

	public void setAnalyzingURL(String analyzingURL) {
		this.analyzingURL = analyzingURL;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurpage() {
		return curpage;
	}

	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}

	public static HSSFCellStyle getTitleStyle() {
		return titleStyle;
	}

	public static void setTitleStyle(HSSFCellStyle titleStyle) {
		FreeQueryAction.titleStyle = titleStyle;
	}

	public static HSSFCellStyle getResultStyle() {
		return resultStyle;
	}

	public static void setResultStyle(HSSFCellStyle resultStyle) {
		FreeQueryAction.resultStyle = resultStyle;
	}

	public static HSSFCellStyle getHeadStyle() {
		return headStyle;
	}

	public static void setHeadStyle(HSSFCellStyle headStyle) {
		FreeQueryAction.headStyle = headStyle;
	}

	public static HSSFFont getFont() {
		return font;
	}

	public static void setFont(HSSFFont font) {
		FreeQueryAction.font = font;
	}

	public static int getExcel_max_row() {
		return excel_max_row;
	}

	public static String getExcel_last_sheet_name() {
		return excel_last_sheet_name;
	}

	public List getRecordList() {
		return recordList;
	}

	public void setRecordList(List recordList) {
		this.recordList = recordList;
	}

}
