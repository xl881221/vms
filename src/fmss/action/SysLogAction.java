package fmss.action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.logging.LogFactory;

import common.logger.LogDO;
import common.logger.LogManagerDAO;

import fmss.dao.entity.LoginDO;
import fmss.services.LogManagerService;
import fmss.common.util.Constants;
import fmss.common.util.JXLTool;

public class SysLogAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LogManagerService logManagerService;
	private List dataList;
	private String userCname;
	private String menuName;
	private String status;
	private String startTime;
	private String endTime;
	
	static final Map STATUS_MAP = new LinkedHashMap();
	
	static{
		STATUS_MAP.put("", "全部");
		STATUS_MAP.put("0", "失败");
		STATUS_MAP.put("1", "成功");
	}
	
	public Map getSTATUS_MAP(){
		return STATUS_MAP;
	}

	public String list() {
		try {
			Map parms = new HashMap();
			parms.put("userCname", this.getUserCname());
			parms.put("menuName", this.getMenuName());
			parms.put("status", this.getStatus());
			parms.put("startTime", this.getStartTime());
			parms.put("endTime", this.getEndTime());	
			parms.put("loginDO", (LoginDO) super.get(Constants.LOGIN_USER));
			dataList = logManagerService.selectByFormWithPaging(parms,
					paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	public String delete() {
		try {
			String[] delIds = new String[getIds().size()];
			for (int i = 0; i < getIds().size(); i++) {
				delIds[i] = (String) getIds().get(i);
			}
			logManagerService.deleteByPrimarys(delIds);
			list();
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	public void logToExcel() throws Exception {
		try {
			Map parms = new HashMap();
			parms.put("userCname", this.getUserCname());
			parms.put("menuName", this.getMenuName());
			parms.put("status", this.getStatus());
			parms.put("startTime", this.getStartTime());
			parms.put("endTime", this.getEndTime());		
			Map result = logManagerService.selectAllByParams(parms);
			String code = (String) result.get(LogManagerDAO.RESULT_KEY);
			if (String.valueOf(LogManagerDAO.RESULT_EXCEED_CODE).equals(code)) {
 				String message = "<script>alert('记录数不能超过"+ LogManagerDAO.RESULT_EXCEED_NUM+ "条');window.history.back();</script>";
				request.setAttribute("mes", message);
				RequestDispatcher rd = request.getRequestDispatcher("/messagedisplay.jsp");
				rd.forward(request,response); 
				return;
			}
			List content = (List) result.get(LogManagerDAO.RESULT_VALUE);
			StringBuffer fileName = new StringBuffer("日志");
			fileName.append(".xls");
			String name = "attachment;filename="
					+ URLEncoder.encode(fileName.toString(), "UTF-8")
							.toString();
			response.setHeader("Content-type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition", name);
			OutputStream os = response.getOutputStream();
			writeToExcel(os, content);
			os.flush();
			os.close();
		} catch (Exception e) {
			LogFactory.getLog(this.getClass()).error(e);
			throw e;
		}
	}

	public void writeToExcel(OutputStream os, List content) throws IOException,
			RowsExceededException, WriteException {
		WritableWorkbook wb = Workbook.createWorkbook(os);
		WritableSheet ws = null;
		ws = wb.createSheet("日志报告", 0);
		Label header0 = new Label(0, 0, "日志编号", JXLTool.getHeader());
 		Label header1 = new Label(1, 0, "用户中文名", JXLTool.getHeader());
 		Label header2 = new Label(2, 0, "机构名", JXLTool.getHeader());
 		Label header3 = new Label(3, 0, "菜单名", JXLTool.getHeader());
 		Label header4 = new Label(4, 0, "操作时间", JXLTool.getHeader());
		Label header5 = new Label(5, 0, "说明", JXLTool.getHeader());
 		Label header6 = new Label(6, 0, "操作状态", JXLTool.getHeader());
		ws.addCell(header0); 
		ws.addCell(header1); 
		ws.addCell(header2); 
		ws.addCell(header3);
 		ws.addCell(header4);
		ws.addCell(header5); 
		ws.addCell(header6);
		
 		
		for (int i = 0; i < 14; i++) {
			ws.setColumnView(i, 18);
		}
		int count = 1;
		for (int i = 0; i < content.size(); i++) {
			LogDO o = (LogDO) content.get(i);
			int column = count++;
			setWritableSheet(ws, o, column);
		}
		
 		wb.write();
		wb.close();
		// ws.setColumnView(i + 1, width);
	}

	private static final String PATTREN = "yyyy-MM-dd HH:mm:ss";
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat(PATTREN);

	public void setWritableSheet(WritableSheet ws, LogDO o, int column)
			throws WriteException {
		Label cell0 = new Label(0, column, String.valueOf(column),JXLTool.getContentFormat()); 
		Label cell1 = new Label(1, column, o.getUserCname(), JXLTool.getContentFormat()); 
		Label cell2 = new Label(2, column, o.getInstCname(), JXLTool.getContentFormat()); 
		Label cell3 = new Label(3, column, o.getMenuName(), JXLTool.getContentFormat()); 
		Label cell4 = new Label(4, column,o.getExecTime() != null ? DATE_FORMAT.format(o.getExecTime())
						: "", JXLTool.getContentFormat());
		Label cell5 = new Label(5, column, o.getDescription(), JXLTool.getContentFormat()); 
		Label cell6 = new Label(6, column, "0".equals(o.getStatus()) ? "失败"
				: "成功", JXLTool.getContentFormat());
		ws.addCell(cell0); 
		ws.addCell(cell1);
 		ws.addCell(cell2);
 		ws.addCell(cell3);
 		ws.addCell(cell4);
		ws.addCell(cell5);
 		ws.addCell(cell6);
	}

	public String getUserCname() {
		return userCname;
	}

	public void setUserCname(String userCname) {
		this.userCname = userCname;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List getDataList() {
		return dataList;
	}

	public void setDataList(List dataList) {
		this.dataList = dataList;
	}

	public void setLogManagerService(LogManagerService logManagerService) {
		this.logManagerService = logManagerService;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
