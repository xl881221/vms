package fmss.services;

import java.util.ArrayList;
import java.util.List;

import fmss.common.util.PaginationList;

import org.apache.commons.lang.StringUtils;


/**
 * @author liuhaibo 用于extent_base表。
 * 
 */
public class ExtentBaseService extends CommonService {

	/**
	 * @param instName
	 * @param systemId
	 * @param itemId
	 * @param pageInfo
	 *            分页对象
	 * @return
	 */
	public List selectByFormWithPaging(String instName, String systemId,
			String itemId, String srcsysCd, String ddate,PaginationList pageInfo) {
		String hql = "from ExtentBase a where 1=1";
		ArrayList list = new ArrayList();
		if (StringUtils.isNotEmpty(instName)) {
			list.add("%" + instName + "%");
			hql += " and a.id.instName like ?";
		}
		if (StringUtils.isNotEmpty(systemId)) {
			list.add("%" + systemId + "%");
			hql += " and a.id.systemId like ?";
		}
		if (StringUtils.isNotEmpty(itemId)) {
			list.add("%" + itemId + "%");
			hql += " and a.id.itemId like ?";
		}
		if (StringUtils.isNotEmpty(srcsysCd)) {
			list.add("%" + srcsysCd + "%");
			hql += " and a.id.srcsysCd like ?";
		}
		if (StringUtils.isNotEmpty(ddate)) {
			list.add("%" + ddate + "%");
			hql += " and a.id.ddate like ?";
		}
		return super.find(hql, list, pageInfo);
	}
	
	public List selectByFormWithPaging(String instName, String systemId,
			String itemId, String srcsysCd, String ddate) {
		String hql = "from ExtentBase a where 1=1";
		ArrayList list = new ArrayList();
		if (StringUtils.isNotEmpty(instName)) {
			list.add("%" + instName + "%");
			hql += " and a.id.instName like ?";
		}
		if (StringUtils.isNotEmpty(systemId)) {
			list.add("%" + systemId + "%");
			hql += " and a.id.systemId like ?";
		}
		if (StringUtils.isNotEmpty(itemId)) {
			list.add("%" + itemId + "%");
			hql += " and a.id.itemId like ?";
		}
		if (StringUtils.isNotEmpty(srcsysCd)) {
			list.add("%" + srcsysCd + "%");
			hql += " and a.id.srcsysCd like ?";
		}
		if (StringUtils.isNotEmpty(ddate)) {
			list.add("%" + ddate + "%");
			hql += " and a.id.ddate like ?";
		}
		return super.find(hql, list);
	}
	
}
