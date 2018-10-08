package fmss.action;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fmss.dao.entity.UBaseHolidayTypeDO;
import fmss.services.HolidayService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;


import com.jes.core.api.holiday.HolidayManager;
import com.jes.core.api.holiday.HolidayTypeDO;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class HolidayAPIAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private HolidayService holidayService;

	public void setHolidayService(HolidayService holidayService) {
		this.holidayService = holidayService;
	}

	public void getHolidayTypes() {
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			String enabled = request.getParameter("enabled");
			List list = holidayService.getHolidayTypes(enabled);
			if (CollectionUtils.isNotEmpty(list)) {
				log.info("从子系统取资源数据");
				response.setContentType("text/xml;charset=gbk");
				// 新修改 返回流对象由 OutputStream 为 Writer,解决setContentType在部分特殊环境
				// 下不能生效的问题. fix websphere linux 节假日列表 乱码问题。
				OutputStreamWriter writer = new OutputStreamWriter(response
						.getOutputStream(), "gbk");
				XStream x = new XStream(new DomDriver());
				x.toXML(transform(list), writer);
			} else {
				log.warn("日期类别为空");
			}
		} catch (Throwable e) {
			log.error("从子系统 取得资源 数据 发生异常 :", e);
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				log.error(e);
			}
		}
	}

	private List transform(List list) {
		List l = new ArrayList();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			UBaseHolidayTypeDO o = (UBaseHolidayTypeDO) iterator.next();
			l.add(new HolidayTypeDO(o.getHolidayType(), o.getHolidayName(), o
					.getEnable(), o.getRemark()));
		}
		return l;
	}

	public void checkHoliday() {
		String date = request.getParameter("date");
		String holidayType = request.getParameter("holidayType");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			if (StringUtils.isEmpty(date) || StringUtils.isEmpty(holidayType)) {
				log.warn("日期参数或类别参数为空,date:" + date + ",holidayType:"
						+ holidayType);
				pw.print(HolidayManager.CHECK_CODE_PARAM_INVALID);
				return;
			}
			int code = holidayService.isHolidayInstance(date, holidayType);
			response.setContentType("text/html; charset=GBK");
			pw.print(code);
		} catch (Exception e) {
			log.error("判断是否节假日出错:", e);
			pw.print(HolidayManager.CHECK_CODE_SYSTEM_EXCEPTION);
		} finally {
			pw.close();
		}
	}

	public void addBatchHolidays() {
		String dates = request.getParameter("dates");
		String holidayType = request.getParameter("holidayType");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			if (StringUtils.isEmpty(dates)) {
				log.warn("批量添加-日期参数为空,date:" + dates);
			}
			if (StringUtils.isEmpty(holidayType)) {
				log.warn("类别参数为空holidayType:" + holidayType);
				pw.print(HolidayManager.CHECK_CODE_PARAM_INVALID);
				return;
			}
			String[] holidays = new String[0];
			if (StringUtils.isNotEmpty(dates))
				holidays = dates.split(HolidayManager.SEPARATOR);
			if (ArrayUtils.isEmpty(holidays)) {
				log.warn("批量添加-日期数组为空,date:" + dates);
			}
			int code = holidayService.saveBatchHolidays(holidays, holidayType);
			response.setContentType("text/html; charset=GBK");
			pw.print(code);
		} catch (Exception e) {
			log.error("判断是否节假日出错:", e);
			pw.print(HolidayManager.CHECK_CODE_SYSTEM_EXCEPTION);
		} finally {
			pw.close();
		}
	}
}
