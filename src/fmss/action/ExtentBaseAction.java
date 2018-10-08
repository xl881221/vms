/**
 * 
 */
package fmss.action;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fmss.dao.entity.ExtentBase;
import fmss.dao.entity.ExtentBaseId;
import fmss.services.ExtentBaseService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


/**
 * @author liuhaibo ָ��˶�Action
 * 
 */
public class ExtentBaseAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	/** ��¼��־��Ϣ */
	private static final Logger logger = Logger
	.getLogger(ExtentBaseAction.class);
	private ExtentBaseService extentBaseService;
	private ExtentBase extentBase;

	public ExtentBase getExtentBase() {
		return extentBase;
	}

	public void setExtentBase(ExtentBase extentBase) {
		this.extentBase = extentBase;
	}

	public ExtentBaseService getExtentBaseService() {
		return extentBaseService;
	}

	public void setExtentBaseService(ExtentBaseService extentBaseService) {
		this.extentBaseService = extentBaseService;
	}

	/**
	 * ��ҳ��ѯ��ʾ
	 */
	public String list() {
		try {
			if (extentBase == null) {
				extentBase = new ExtentBase();
				extentBase.setId(new ExtentBaseId());
			}
			extentBaseService.selectByFormWithPaging(extentBase.getId().getInstName(),
					extentBase.getId().getSystemId(), extentBase.getId().getItemId(),
					extentBase.getId().getSrcsysCd(), extentBase.getId().getDdate(),
					this.paginationList);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}
	

	/**
	 * <p>
	 * ��������: batchExportList|����: ��������ָ��˶�����ΪEXCEL
	 * </p>
	 */
	public String batchExportList() {
		int max = 10000;
		try {
			if (extentBase == null) {
				extentBase = new ExtentBase();
				extentBase.setId(new ExtentBaseId());
			}
			ExtentBaseId ebId = extentBase.getId();
			List exportList = extentBaseService.selectByFormWithPaging(ebId
					.getInstName(), ebId.getSystemId(), ebId.getItemId(), ebId
					.getSrcsysCd(), ebId.getDdate());
			if (exportList == null || exportList.size() == 0) {
				response.setContentType("text/html; charset=utf-8");
				response.getWriter().print(
						"<script>alert('û�в�ѯ������!');history.go(-1);</script>");
				response.getWriter().close();
				return null;
			} else if (exportList != null && exportList.size() > max) {
				// ������������������10000��ʱ����ʾ�û�������̫����ʹ�ò�ѯ���ܹ��������ٽ��е���
				response.setContentType("text/html; charset=utf-8");
				response
						.getWriter()
						.print(
								"<script>alert('������������������" + max + "������ʹ�ò�ѯ�������ݺ���ִ�е���!');history.go(-1);</script>");
				response.getWriter().close();
				return null;
			}
			OutputStream out = response.getOutputStream();
			response.setContentType("Application/msexcel;charset=GBK");
			String filename = java.net.URLEncoder.encode("ָ��˶�.xls", "UTF-8");
			response.setHeader("Content-disposition", "attachment; filename="
					+ filename);
			this.batchExportExcel(exportList, out);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * <p>
	 * ��������: batchExportExcel|����: ����������EXCEL
	 * </p>
	 * 
	 * @param exportList
	 * @throws Exception
	 */
	private void batchExportExcel(List exportList, OutputStream out)
			throws Exception, IOException {
		// ָ��ÿ���ֶζ�Ӧ�������Լ���EXCEL�е���
		Map titleMap = new HashMap();
		titleMap.put("instName_index", "0");
		titleMap.put("ddate_index", "1");
		titleMap.put("systemId_index", "2");
		titleMap.put("itemId_index", "3");
		titleMap.put("itemName_index", "4");
		titleMap.put("rcurrCd_index", "5");
		titleMap.put("itemValue_index", "6");
		titleMap.put("reportId_index", "7");
		titleMap.put("srcsysCd_index", "8");
		titleMap.put("instName_title", "�������");
		titleMap.put("ddate_title", "��������");
		titleMap.put("systemId_title", "����ģ��");
		titleMap.put("itemId_title", "ָ�����");
		titleMap.put("itemName_title", "ָ������");
		titleMap.put("rcurrCd_title", "����");
		titleMap.put("itemValue_title", "ָ��ֵ");
		titleMap.put("reportId_title", "������");
		titleMap.put("srcsysCd_title", "��Դϵͳ");
		
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		try {
			wb = new HSSFWorkbook();
			sheet = wb.createSheet("Sheet1");

			// ����Excel��һ��
			HSSFRow row = sheet.createRow(0);
			// ����Cell��ʽ�������֣�����
			HSSFFont font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			HSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setFont(font);
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			if (exportList != null && exportList.size() > 0) {
				// ��ȡ��һ�б�����Ϣ
				Field[] tempFields = ExtentBaseId.class.getDeclaredFields();
				for (int i = 0; i < tempFields.length; i++) {
					Field field = tempFields[i];
					String fieldName = field.getName();
					String title = (String) titleMap.get(fieldName + "_title");
					if (title != null) {
						HSSFCell cell = row.createCell((short) i);
						cell.setCellStyle(cellStyle);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING); // String��������
						cell.setEncoding(HSSFCell.ENCODING_UTF_16); // �����ַ�����
						cell.setCellValue(new HSSFRichTextString(title));
						sheet.setColumnWidth((short) i, (short) 3300);
					}
				}
				
				HSSFCellStyle cellStyleTemp = wb.createCellStyle();
				// ����Cell��ʽ�����뷽ʽ
				cellStyleTemp.setAlignment(HSSFCellStyle.ALIGN_LEFT);

				// ѭ������ÿһ��
				for (int i = 0; i < exportList.size(); i++) {
					ExtentBase eb = (ExtentBase) exportList.get(i);
					if (eb != null) {
						ExtentBaseId ebId = eb.getId();
						row = sheet.createRow(i + 1);
						Class clazz = ebId.getClass();
						Field[] fields = clazz.getDeclaredFields();
						for (int j = 0; j < fields.length; j++) {
							Field field = fields[j];
							String fieldName = field.getName();
							String index = (String) titleMap.get(fieldName
									+ "_index");
							Object value = getValue(ebId, fieldName);
							HSSFCell cell = row.createCell(Short.valueOf(index)
									.shortValue());
							// �����ַ�����
							cell.setEncoding(HSSFCell.ENCODING_UTF_16);
							// ���뷽ʽ
							cell.setCellStyle(cellStyleTemp);
							// ������ֵ
							cell.setCellValue(new HSSFRichTextString(
									value == null ? "" : value.toString()));
						}
					}
				}
			}
			wb.write(out);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("д�ļ������ʱ���ִ���....");
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("close appendExcel IO error!");
			}
		}
	}

	private Object getValue(Object bean, String name) {
		Object value = null;
		if (bean != null) {
			Class calzz = bean.getClass();
			try {
				Method m = calzz.getMethod(getMethodName(name, "get"),
						new Class[] {});
				value = m.invoke(bean, new Object[] {});
			} catch (Exception e) {
				value = null;
			}
		}
		return value;
	}

	private String getMethodName(String name, String methodMode) {
		if (StringUtils.isNotEmpty(name))
			name = new StringBuffer().append(methodMode).append(
					name.substring(0, 1).toUpperCase()).append(
					name.substring(1, name.length())).toString();
		return name;
	}
	
}
