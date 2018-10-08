package fmss.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import fmss.dao.entity.LoginDO;
import fmss.dao.entity.UBaseInstDO;
import fmss.dao.entity.UBaseOnlineDO;
import fmss.common.cache.CacheManager;
import fmss.services.InstService;
import fmss.services.OnlineService;
import fmss.common.util.Constants;

/**
 * <p>
 * ��Ȩ����:(C)2003-2010 
 * </p>
 * 
 * @����: liuyi
 * @����: 2011-6-20
 * @����: [OnlineAction]�����û��������Action
 */
public class OnlineAction extends BaseAction {
	
	private UBaseOnlineDO user = new UBaseOnlineDO(); // �����û�����ʵ��

	
	private OnlineService onlineService;
	private CacheManager  cacheManager;
	private InstService instService;
	
	private boolean fixQuery=false;
	
	public boolean isFixQuery() {
		return fixQuery;
	}
	public void setFixQuery(boolean fixQuery) {
		this.fixQuery = fixQuery;
	}
	/**
	 * <p>
	 * ��������: listOnline|����: ��ʾ�û��б�
	 * </p>
	 * 
	 * @return ��ʾ�û��б�
	 */
	public String listOnline() {
		return SUCCESS;
	}
	/**
	 * <p>
	 * ��������: listOnlineHead|����: ��ʾ�û��б��ѯ֡
	 * </p>
	 * 
	 * @return ��ʾ�û��б��ѯ֡
	 */
	public String listOnlineHead() {
		return SUCCESS;
	}
	/**
	 * <p>
	 * ��������: listOnlineInstTree|����: ��ʾ�����б�
	 * </p>
	 * 
	 * @return ��ʾ�����б�
	 */
	public String listOnlineInstTree() {
		return SUCCESS;
	}
	/**
	 * <p>
	 * ��������: listOnlineMain|����: ��ѯ��ȡ�û��б�
	 * </p>
	 * 
	 * @return �û��б�
	 * @throws Exception 
	 */
	public String listOnlineMain() throws Exception {
		try {
			request.setAttribute("PARAM_KICKOUT", cacheManager.getParemerCacheMapValue(Constants.PARAM_KICKOUT));
			
			LoginDO login = (LoginDO) super.get(Constants.LOGIN_USER);
			StringBuffer sb = new StringBuffer();
			sb.append("from UBaseOnlineDO u left join u.baseUser ubu left join ubu.ubaseInst ubi where 1=1  ");
			if(onlineService.getIsDB()){
				if(!fixQuery){
					// �û���½�������ж�
					if (StringUtils.isNotEmpty(user.getAddr())) {
						sb.append(" and u.addr like '%" + user.getAddr()
								+ "%' ");
					}
					if (StringUtils.isNotEmpty(user.getUserId())) {
						sb.append(" and ubu.userId like '%" + user.getUserId()
								+ "%' ");
					}
				}else{
					sb.append(" and exists (select 1 from UBaseInstDO a where a.instId='"+user.getInstId()+"' and substring(ubi.instPath,1,length(a.instPath)) = a.instPath)");
				}
				sb.append(" and exists (select 1 from UBaseInstDO a where a.instId='"+login.getInstId()+"' and (substring(ubi.instPath,1,length(a.instPath))=a.instPath or a.isHead='true'))");
				onlineService.getBem().find(sb.toString(), new ArrayList(), this.paginationList);
			}else{
				int currentPage=1;
				if(!StringUtils.isEmpty(request.getParameter("paginationList.currentPage"))){
					currentPage = Integer.valueOf(request.getParameter("paginationList.currentPage")).intValue();
				}
				int pageSize=paginationList.getPageSize();
				paginationList.setCurrentPage(currentPage);
				int pageStart=paginationList.getPageStart();
				int dateIndex=0;
				Map map=onlineService.getOnlineMap();
				Iterator i = map.keySet().iterator();
				List recordList=new ArrayList();
				UBaseInstDO instObj1 = (UBaseInstDO) instService.getInstByInstId(login.getInstId());
				UBaseInstDO instObj2 = (UBaseInstDO) instService.getInstByInstId(user.getInstId());
				while(i.hasNext()){
					UBaseOnlineDO u = (UBaseOnlineDO)map.get(i.next());
					boolean b=true;
					if(!fixQuery){
						b=(StringUtils.isEmpty(user.getUserId())||u.getUserId().indexOf(user.getUserId())!=-1);
						b=b&&(StringUtils.isEmpty(user.getAddr())||u.getAddr().indexOf(user.getAddr())!=-1);
					}else{
						b=(u.getBaseUser().getUbaseInst().getInstPath().indexOf(instObj1.getInstPath())==0);
						b=(u.getBaseUser().getUbaseInst().getInstPath().indexOf(instObj2.getInstPath())==0);
					}
					if(b){
						dateIndex++;
						if(dateIndex>=pageStart&&dateIndex<=pageStart+pageSize-1){
							Object o[]={u,u.getBaseUser(),u.getBaseUser().getUbaseInst()};
							recordList.add(o);
						}	
					}
				}
				paginationList.setRecordCount(map.keySet().size());
				paginationList.setRecordList(recordList);
				this.setCurPage(currentPage);
			}
			
			
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			throw e;
		}
	}
	public void setOnlineService(OnlineService onlineService) {
		this.onlineService = onlineService;
	}
	public CacheManager getCacheManager() {
		return cacheManager;
	}
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	public void setInstService(InstService instService) {
		this.instService = instService;
	}
	public UBaseOnlineDO getUser() {
		return user;
	}
	public void setUser(UBaseOnlineDO user) {
		this.user = user;
	}


}
