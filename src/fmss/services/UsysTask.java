package fmss.services;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import fmss.common.cache.CacheManager;
import fmss.common.cache.SystemCache;
import fmss.common.util.Constants;
import fmss.common.util.LoginUtil;
 

public class UsysTask implements InitializingBean, DisposableBean {
	private static final Log log = LogFactory.getLog(UsysTask.class);

	private Timer timer1 = new Timer();
	private Timer timer2 = new Timer();
	private Timer timer3 = new Timer();
	private Timer timer4 = new Timer();
	private TimerTask task1 = new SystemCacheTask();
	private OnlineTask task2 = new OnlineTask();
	private InstTask task3 = new InstTask();
	private InstTask task4 = new InstTask();
	
	

	private SystemCache systemCache;
	private CacheManager cacheManager;
	private OnlineService onlineService;
	private InstService instService;
	
	//刷新缓存任务
	class SystemCacheTask extends TimerTask {
		public void run() {
			log.info("begin SystemCache task, current time is " + DateFormatUtils.format(new Date(), LoginUtil.LONG_FORMAT));
			try{
				systemCache.runCacheRegister();
			}catch(Exception e){
				log.error("occur error when execute task !!!", e);
			}
			log.info("end SystemCache task, current time is " + DateFormatUtils.format(new Date(), LoginUtil.LONG_FORMAT));
		}
	}
	//刷新表INST INST_PATH INST_LEVEL
	class InstTask extends TimerTask {
		public void run() {
			log.info("begin InstTask task, current time is " + DateFormatUtils.format(new Date(), LoginUtil.LONG_FORMAT));
			try{
				instService.initInst();
			}catch(Exception e){
				log.error("occur error when execute task !!!", e);
			}
			log.info("end InstTask task, current time is " + DateFormatUtils.format(new Date(), LoginUtil.LONG_FORMAT));
			
		}
		
	}
	//清空再线用户
	class OnlineTask extends TimerTask {
		public void run() {
			log.info("begin OnlineTask task, current time is " + DateFormatUtils.format(new Date(), LoginUtil.LONG_FORMAT));
			try{
				onlineService.clearAllUser();
			}catch(Exception e){
				log.error("occur error when execute task !!!", e);
			}  
			log.info("end OnlineTask task, current time is " + DateFormatUtils.format(new Date(), LoginUtil.LONG_FORMAT));
		
		}
	}

	public void afterPropertiesSet() throws Exception {
		String s = cacheManager.getParemerCacheMapValue(Constants.PARAM_COLONY);
		if("1".equals(s)){
			log.info("UsysTask  PARAM_COLONY='1'");
			s = cacheManager.getParemerCacheMapValue(Constants.PARAM_REFRESH_CACHE);
			try{
				timer1.schedule(task1,1000l*30, Long.valueOf(s).longValue()*1000);
			}catch(Exception e){
				log.error("timer1", e);
			}
 		}
		s = cacheManager.getParemerCacheMapValue(Constants.PARAM_CLEAR_USER);
		if(StringUtils.isNotEmpty(s)){
			log.info(" UsysTask PARAM_CLEAR_USER="+s);
			SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat d2=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
 			try{
 				String day1 = dateformat.format(new Date());
				String date= day1+" "+s+":00"; 
				Date d = d2.parse(date);
				System.out.println(d);
 				timer2.scheduleAtFixedRate(task2,d,24*60*60*1000);
			}catch(Exception e){
				log.error("timer2 定时器异常：", e);
			}
 		}
		if(true){
			try{
				timer3.schedule(task3,3000);
			}catch(Exception e){
				log.error("timer3", e);
			}
			
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY,Integer.valueOf("06").intValue());
			c.set(Calendar.MINUTE, Integer.valueOf("00").intValue());
			if(c.getTimeInMillis()<System.currentTimeMillis())
				c.add(Calendar.DATE, 1);
			try{
				timer4.schedule(task4,c.getTimeInMillis()-System.currentTimeMillis(),24*60*60*1000);
			}catch(Exception e){
				log.error("timer4", e);
			}
		}	
	}
	/**
	 * 刷新定时器
	 * @throws Exception
	 */
	public void afterPropertiesSetRefresh() throws Exception {
	    this.destroy();
		timer1 = new Timer();
		timer2 = new Timer();
		timer3 = new Timer();
		timer4 = new Timer();
		task1 = new SystemCacheTask();
		task2 = new OnlineTask();
		task3 = new InstTask();
		task4 = new InstTask();
		String s = cacheManager.getParemerCacheMapValue(Constants.PARAM_COLONY);
		if("1".equals(s)){
			log.info("UsysTask  PARAM_COLONY='1'");
			s = cacheManager.getParemerCacheMapValue(Constants.PARAM_REFRESH_CACHE);
			try{
				timer1.schedule(task1,1000l*30, Long.valueOf(s).longValue()*1000);
			}catch(Exception e){
				log.error("timer1", e);
			}
 		}
		s = cacheManager.getParemerCacheMapValue(Constants.PARAM_CLEAR_USER);
		if(StringUtils.isNotEmpty(s)){
			log.info(" UsysTask PARAM_CLEAR_USER="+s);
			SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat d2=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
 			try{
 				String day1 = dateformat.format(new Date());
				String date= day1+" "+s+":00"; 
				Date d = d2.parse(date);
				System.out.println(d);
 				timer2.scheduleAtFixedRate(task2,d,24*60*60*1000);
			}catch(Exception e){
				log.error("timer2 定时器异常：", e);
			}
 		}
		if(true){
			try{
				timer3.schedule(task3,3000);
			}catch(Exception e){
				log.error("timer3", e);
			}
			
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY,Integer.valueOf("06").intValue());
			c.set(Calendar.MINUTE, Integer.valueOf("00").intValue());
			if(c.getTimeInMillis()<System.currentTimeMillis())
				c.add(Calendar.DATE, 1);
			try{
				timer4.schedule(task4,c.getTimeInMillis()-System.currentTimeMillis(),24*60*60*1000);
			}catch(Exception e){
				log.error("timer4", e);
			}
 		}
	}

	public void destroy() throws Exception {
		task1.cancel();
		task2.cancel();
		task3.cancel();
		task4.cancel();
		timer1.cancel();
		timer2.cancel();
		timer3.cancel();
		timer4.cancel();
	}

	public void setSystemCache(SystemCache systemCache) {
		this.systemCache = systemCache;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public void setOnlineService(OnlineService onlineService) {
		this.onlineService = onlineService;
	}

	public void setInstService(InstService instService) {
		this.instService = instService;
	}

}
