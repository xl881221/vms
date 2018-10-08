/**
 * 
 */
package fmss.action;

import org.apache.commons.lang.StringUtils;

import fmss.dao.entity.SapMapInfo;
import fmss.services.SapMapInfoService;

/**
 * @author liuhaibo
 * 
 */
public class SapMapInfoAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private SapMapInfo sapMapInfo;
	private SapMapInfo sapMapInfoQuery;
	private SapMapInfoService sapMapInfoService;
	private String[] sapMapInfoIds;
	

	public SapMapInfo getSapMapInfo() {
		return sapMapInfo;
	}

	public void setSapMapInfo(SapMapInfo sapMapInfo) {
		this.sapMapInfo = sapMapInfo;
	}

	public String list() {
		try {
			if (sapMapInfoQuery == null) {
				sapMapInfoQuery = new SapMapInfo();
			}
			sapMapInfoService.selectByFormWithPaging(sapMapInfoQuery.getDpvId(),
					sapMapInfoQuery.getDpvName(), sapMapInfoQuery.getAcciNo(), sapMapInfoQuery
							.getProdId(), this.paginationList);
			return SUCCESS;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	public String saveOrUpdate() {
		//还没有记录操作日志
		try {
			sapMapInfoService.saveOrUpdateSapMapInfo(sapMapInfo);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	public String delete() {
		//还没有记录操作日志
		try {
			sapMapInfoService.deleteSapMapInfos(sapMapInfoIds);
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}
	
	public String load(){
		try {
			if(sapMapInfo!=null&&StringUtils.isNotEmpty(sapMapInfo.getId())){
				sapMapInfo = sapMapInfoService.loadSapMapInfo(sapMapInfo.getId());
			}else{
				sapMapInfo = new SapMapInfo();
			}
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	public SapMapInfoService getSapMapInfoService() {
		return sapMapInfoService;
	}

	public void setSapMapInfoService(SapMapInfoService sapMapInfoService) {
		this.sapMapInfoService = sapMapInfoService;
	}


	public SapMapInfo getSapMapInfoQuery() {
		return sapMapInfoQuery;
	}

	public void setSapMapInfoQuery(SapMapInfo sapMapInfoQuery) {
		this.sapMapInfoQuery = sapMapInfoQuery;
	}

	public String[] getSapMapInfoIds() {
		return sapMapInfoIds;
	}

	public void setSapMapInfoIds(String[] sapMapInfoIds) {
		this.sapMapInfoIds = sapMapInfoIds;
	}


}
