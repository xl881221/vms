package fmss.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fmss.dao.entity.UBaseDictionaryDO;
import fmss.common.cache.CacheManager;
import fmss.common.cache.CacheabledMap;
import fmss.services.DictionaryService;
import fmss.common.util.SpringContextUtils;

import org.apache.commons.lang.StringUtils;


public class InstRegionAction extends JSONProviderAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void autocomplete() throws Exception {
		String queryPair = request.getParameter("q");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		StringBuffer sb = new StringBuffer();
		CacheManager cacheManager = (CacheManager) SpringContextUtils
				.getBean("cacheManager");
		CacheabledMap map = (CacheabledMap) cacheManager
				.getCacheObject(DictionaryService.INST_REGIONS);
		if (map != null) {
			List list = (List) map.get(DictionaryService.INST_REGIONS);
			sb.append("[");
			Iterator iterator = list.iterator();
			StringBuffer sb1 = new StringBuffer();
			while (iterator.hasNext()) {
				UBaseDictionaryDO o = (UBaseDictionaryDO) iterator.next();
				if ((o.getDicValue() + "$$" + o.getDicName())
						.indexOf(queryPair) > -1) {
					sb1.append("{");
					sb1.append("name:'" + o.getDicValue() + "'");
					sb1.append(",");
					sb1.append("to:'" + o.getDicName() + "'");
					sb1.append("}");
					sb1.append(",");
				}
			}
			sb.append(sb1.toString().endsWith(",") ? sb1.substring(0, sb1
					.length() - 1) : sb1.toString());
			sb.append("]");
		}
		log.debug(sb);
		out.print(sb.toString());
		out.close();
	}

	public void getRegion() throws Exception {
		String province = request.getParameter("selected");
		String city = request.getParameter("selected");
		String level = request.getParameter("regionLevel");
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		StringBuffer sb = new StringBuffer();
		try {
			CacheManager cacheManager = (CacheManager) SpringContextUtils
					.getBean("cacheManager");
			CacheabledMap map = (CacheabledMap) cacheManager
					.getCacheObject(DictionaryService.INST_REGIONS);
			if (map != null) {
				List list = (List) map.get(DictionaryService.INST_REGIONS);
				List datas = new ArrayList();
				sb.append("[");
				if ("1".equals(level)) {
					datas = getRegionProvince(list);
				}
				if ("2".equals(level) && StringUtils.isNotEmpty(province)
						&& !"000000".equals(city)) {
					datas = getRegionCity(list, province);
				}
				if ("3".equals(level) && !"000000".equals(city)) {
					datas = getRegionArea(list, city);
				}
				Iterator iterator = datas.iterator();
				StringBuffer sb1 = new StringBuffer();
				while (iterator.hasNext()) {
					UBaseDictionaryDO o = (UBaseDictionaryDO) iterator.next();
					sb1.append("{");
					sb1.append("value:'" + o.getDicValue() + "'");
					sb1.append(",");
					sb1.append("name:'" + o.getDicName() + "'");
					sb1.append("}");
					sb1.append(",");
				}
				sb.append(sb1.toString().endsWith(",") ? sb1.substring(0, sb1
						.length() - 1) : sb1.toString());
				sb.append("]");
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
		} finally {
			log.debug(sb);
			out.print(sb.toString());
			out.close();
		}
	}

	private List getRegionProvince(List list) {
		List _list = new ArrayList();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			UBaseDictionaryDO o = (UBaseDictionaryDO) iterator.next();
			if (o.getDicValue().substring(2).equals("0000")) {
				_list.add(o);
			}
		}
		return _list;
	}

	private List getRegionCity(List list, String code) {
		List _list = new ArrayList();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			UBaseDictionaryDO o = (UBaseDictionaryDO) iterator.next();
			if (o.getDicValue().substring(0, 2).equals(code.substring(0, 2))
					&& o.getDicValue().endsWith("00")) {
				_list.add(o);
			}
		}
		return _list;
	}

	private List getRegionArea(List list, String code) {
		List _list = new ArrayList();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			UBaseDictionaryDO o = (UBaseDictionaryDO) iterator.next();
			if (o.getDicValue().substring(0, 4).equals(code.substring(0, 4))) {
				_list.add(o);
			}
		}
		return _list;
	}

}