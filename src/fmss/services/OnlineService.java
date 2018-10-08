package fmss.services;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import common.crms.util.StringUtil;

import fmss.dao.entity.UBaseInstDO;
import fmss.dao.entity.UBaseOnlineDO;
import fmss.dao.entity.UBaseUserDO;
import fmss.common.cache.CacheManager;
import fmss.common.util.Constants;



/**
 * <p>
 * 版权所有:(C)2003-2010 
 * </p>
 * 
 * @作者: liuyi
 * @日期: 2011-6-20 
 * @描述:[OnlineService]在线用户service类
 */
public class OnlineService extends CommonService {
	
	private static final Log log = LogFactory.getLog(OnlineService.class);
	
	private static Map onlineMap=new HashMap();
	
	
	private CacheManager cacheManager;
	
	public boolean getIsDB(){
		String s = cacheManager.getParemerCacheMapValue(Constants.PARAM_COLONY);
		if("1".equals(s)){
			return true;
		}else{
			return false;
		}
//		return false;
	}
	/**
	* <p>方法名称: findUserByAdrrAndUserId|描述:查询用户</p>
	 * @return 
	*/
	public UBaseOnlineDO findUserByAddrAndUserId(String adrr,String userId){
		if(getIsDB()){
			List list=new ArrayList();
			list.add(adrr);
			list.add(userId);
			List q = this.find("from UBaseOnlineDO u1 where u1.addr=? and u1.userId=? ", list);
			if(q==null||q.size()==0)
				return null;
			else 
				return (UBaseOnlineDO) q.get(0);
		}else{
			Iterator i = onlineMap.keySet().iterator();
			while(i.hasNext()){
				UBaseOnlineDO u = (UBaseOnlineDO)onlineMap.get(i.next());
				if(adrr.equals(u.getAddr())&&userId.equals(u.getUserId())){
					return u;
				}
			}
			return null;
		}
	}
	/**
	* <p>方法名称: findUserByLoginId|描述:获取在线用户</p>
	 * @return 
	*/
	public UBaseOnlineDO findUserByLoginId(String loginId){
		if(getIsDB()){
			return (UBaseOnlineDO) this.get(UBaseOnlineDO.class,loginId);
		}else{
			return (UBaseOnlineDO)onlineMap.get(loginId);
		}
	}
	/**
	* <p>方法名称: countUserNotList|描述:统计不是白名单里的在线人数</p>
	 * @return 
	*/
	public long countUserNotList(){
		if(getIsDB()){
			return this.find("from UBaseOnlineDO u1 left join u1.baseUser u2 where u1.status='1' and  u2.isList!='1'").size();
		}else{
			Iterator i = onlineMap.keySet().iterator();
			int count=0;
			while(i.hasNext()){
				UBaseOnlineDO u = (UBaseOnlineDO)onlineMap.get(i.next());
				if("1".equals(u.getStatus())&&!"1".equals(u.getBaseUser().getIsList())){
					count++;
				}
			}
			return count;
		}
	}
	/**
	* <p>方法名称: registerUser|描述:添加在线用户</p>
	 * @return 
	*/
	public void clearAllUser(){
		if(getIsDB()){
			this.executeUpdate("delete from UBaseOnlineDO");	
		}else{
			onlineMap=new HashMap(); 
		}
	}
	/**
	* <p>方法名称: registerUser|描述:添加在线用户</p>
	 * @return 
	*/
	public String registerUser(UBaseOnlineDO baseOnline){
		
		UBaseOnlineDO o=findUserByAddrAndUserId(baseOnline.getAddr(),baseOnline.getUserId());
		if(getIsDB()){
			if(o==null){
				try{
	 				this.delete(baseOnline);
 				}catch(Exception e){
 				}
				this.save(baseOnline);
			}
			else{
				o.setLoginTime(baseOnline.getLoginTime());
				this.update(o);
			}	
		}else{
			if(o==null){
				List list=this.find("from UBaseUserDO u left join u.ubaseInst u1 where u.userId='"+baseOnline.getUserId()+"'");
				Object objs[]=(Object[])list.get(0);
				baseOnline.setBaseUser((UBaseUserDO) objs[0]);
				baseOnline.getBaseUser().setUbaseInst((UBaseInstDO) objs[1]);
				onlineMap.put(baseOnline.getLoginId(), baseOnline);
			}
			else{
				o.setLoginTime(baseOnline.getLoginTime());
			}
		}
		if(o==null){
			return baseOnline.getLoginId();
		}else{
			return o.getLoginId();
		}
	}
	/**
	* <p>方法名称: checkLoginStatus|描述:验证用户状态</p>
	 * @return 
	*/
	public String checkLoginStatus(String loginId){
		
		if(loginId==null)
			return "ERROR_1";
			
		UBaseOnlineDO u=findUserByLoginId(loginId);
		
		if(u==null){
			return "ERROR_1";
		}else if("1".equals(u.getStatus())){
			return null;
		}else if("0".equals(u.getStatus())){
			return checkKickout(u.getUserId(),u.getAddr());
		}
		return null;
	}
	/**
	* <p>方法名称: checkKickout|描述:验证用户是否被踢出</p>
	 * @return 
	*/
	public String checkKickout(String userId,String addr){
		UBaseOnlineDO u=findUserByAddrAndUserId(addr,userId);
		if(u==null||!"0".equals(u.getStatus())) return null;
		String paramKickoutTime = cacheManager.getParemerCacheMapValue(Constants.PARAM_KICKOUT_TIME);
		long n=0;
		if(!StringUtil.isEmpty(paramKickoutTime)){
			n=Long.valueOf(paramKickoutTime).longValue()*1000-(System.currentTimeMillis()-u.getKickoutTime().getTime());
			if(n<=0){
				u.setKickoutTime(null);
				u.setStatus("1");
				if(getIsDB()){
					this.update(u);
				}
				return null;
			}else{
				return "ERROR_2_"+(n/1000);
				//return "ERROR_对不起,您已经被踢出系统.距下次登陆时间还剩###秒_"+(n/1000);
			}
		}
		//return "ERROR_对不起,您已经被踢出系统.";
		return "ERROR_3";
	}
	
	/**
	* <p>方法名称: kickoutLogin|描述:被动注销在线用户</p>
	 * @return 
	*/
	public void kickoutLogin(String loginId){
		UBaseOnlineDO u=findUserByLoginId(loginId);
		if(getIsDB()){
			u.setStatus("0");
			u.setKickoutTime(new Timestamp(System.currentTimeMillis()));
			this.update(u);
		}else{
			u.setStatus("0");
			u.setKickoutTime(new Timestamp(System.currentTimeMillis()));
		}
	}
	/**
	* <p>方法名称: deleteLogin|描述:删除在线用户</p>
	 * @return 
	*/
	public void deleteLogin(String loginId){
		if(getIsDB()){
			this.executeUpdate("delete from UBaseOnlineDO where status!='0' and loginId='"+loginId+"'");
		}else{
			UBaseOnlineDO u=findUserByLoginId(loginId);
			if(u!=null)
				if(!u.getStatus().equals("0")){
					onlineMap.remove(loginId);
				}
		}
		
	}
	/**
	* <p>方法名称: deleteUser|描述:删除在线用户</p>
	 * @return 
	*/
	public void deleteUser(String loginId){
		if(getIsDB()){
			this.executeUpdate("delete from UBaseOnlineDO where loginId='"+loginId+"'");
		}else{
			UBaseOnlineDO u=findUserByLoginId(loginId);
			if(u!=null)
				onlineMap.remove(loginId);
		}
		
	}


	/**
	* <p>方法名称: hasLoginByUserIdAndIp|描述:判断用户是否重复登陆</p>
	 * @return 
	*/
	public boolean hasLoginByUserIdAndAddr(String userId,String ip){
		if(getIsDB()){
			List list = new ArrayList();
			list.add(userId);
			list.add(ip);
			return this.find("from UBaseOnlineDO u1 where u1.status='1' and  u1.userId=? and u1.addr!=?",list).size()>0;
		}else{
			Iterator i = onlineMap.keySet().iterator();
			while(i.hasNext()){
				UBaseOnlineDO u = (UBaseOnlineDO)onlineMap.get(i.next());
				if(userId.equals(u.getUserId())&&u.getStatus().equals("1")&&!ip.equals(u.getAddr())){
					return true;
				}
			}
			return false;
		}
	}
	/**
	* <p>方法名称: hasLoginByUserId|描述:判断用户是否在线</p>
	 * @return 
	*/
	public boolean hasLoginByUserId(String userId){
		if(getIsDB()){
			List list = new ArrayList();
			list.add(userId);
			return this.find("from UBaseOnlineDO u1 left join u1.baseUser u2  where u1.status='1' and  u2.userId=? ",list).size()>0;
		}else{
			Iterator i = onlineMap.keySet().iterator();
			while(i.hasNext()){
				UBaseOnlineDO u = (UBaseOnlineDO)onlineMap.get(i.next());
				if(userId.equals(u.getBaseUser().getUserId())&&"1".equals(u.getStatus())){
					return true;
				}
			}
			return false;
		}
	}

	/**
	* <p>方法名称: hasLoginByLoginId|描述:判断用户是否在线</p>
	 * @return 
	*/
	public boolean hasLoginByLoginId(String loginId) {
		UBaseOnlineDO u=findUserByLoginId(loginId);
		if(getIsDB()){
			if(u==null||!u.getStatus().equals("1"))
				return false;
		}else{
			if(u==null||!u.getStatus().equals("1"))
				return false;
		}
		return true;
	}
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	public Map getOnlineMap() {
		return onlineMap;
	}
}
