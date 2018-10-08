package fmss.services;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import fmss.common.cache.CacheManager;
import fmss.common.util.Constants;
import fmss.common.util.LoginUtil;
import fmss.common.util.TimerParameterBundle;

public class ScheduleUserTask implements InitializingBean, DisposableBean {
	private static final String FIRST_TIME = "timer.first.time";
	private static final String DELAY = "timer.delay";
	private static final Log log = LogFactory.getLog(ScheduleUserTask.class);

	private Timer timer = new Timer();
	private TimerTask task = new UpdateTask();

	private static final long TIMER_PERIOD = TimerParameterBundle.parsePeriodExpression("timer.period", "1000 * 10");

	private static final long TIMER_DELAY = StringUtils.isNotEmpty(TimerParameterBundle.getProperty(DELAY)) ? TimerParameterBundle
			.parsePeriodExpression(DELAY)
			: 1000l;

	private static final Date TIMER_FIRST_TIME = StringUtils.isNotEmpty(TimerParameterBundle.getProperty(FIRST_TIME)) ? TimerParameterBundle
			.parseTimeExpression(FIRST_TIME)
			: new Date();
	private CacheManager cacheManager;
	private UserService userService;
	private LoginService loginService;

	public void setLoginService(LoginService a) {
		loginService = a;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	class UpdateTask extends TimerTask {

		public void run() {
			String s = cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_TIMING_USER_UPDATE);
			if ("1".equals(s)) {
				log.info("begin schedule task, current time is " + DateFormatUtils.format(new Date(), LoginUtil.LONG_FORMAT));
				try {
					int days = loginService.getUserOverdueDays();
					Date overdue = LoginUtil.addDays(new Date(), 0 - days);
					String sql = "select userId from UBaseUserDO o where ((o.lastLoginDate is not null and o.lastLoginDate<?) or (o.createTime<? and lastLoginDate is null))";
					s = cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_USER_DELETE_LOGIC);
					if ("1".equals(s)) {
						sql += " and (o.isDelete!='1' or o.isDelete is null) ";
					}
					sql += " and (o.isUserLocked is null or o.isUserLocked='0') ";
					log.info("query sql:" + sql);
					List users = userService.find(sql, new Object[] { overdue, overdue });
					if (CollectionUtils.isNotEmpty(users)) {
						log.info("prepare lock user:" + StringUtils.join(users.iterator(), ","));
						sql = "update UBaseUserDO o set o.isUserLocked='" + LoginUtil.LOCK_LOCKED
								+ "',o.userLockedReson='" + LoginUtil.LOCK_REASON_USER_OVERDUE
								+ "' where o.userId in ($var$)";
						int counts = userService.executeUpdate(sql.replaceAll("\\$var\\$", "'"
								+ StringUtils.join(users.iterator(), "','") + "'"));
						log.info("locked user numbers:" + counts);
					} else {
						log.info("no one prepare to lock");
					}
				} catch (Exception e) {
					log.error("occur error when execute task !!!", e);
				}

				log.info("end schedule task, current time is " + DateFormatUtils.format(new Date(), LoginUtil.LONG_FORMAT));
			} else {
				log.info("cancel schedule task");
			}
		}
	}

	public void afterPropertiesSet() throws Exception {
		log.info("execute load task");
		try {
			String s = cacheManager.getParemerCacheMapValue(Constants.PARAM_SYS_TIMING_USER_UPDATE);
			if ("1".equals(s)) {
				log.info("start to schedule");
				// 优先处理delay
				if (StringUtils.isNotEmpty(TimerParameterBundle.getProperty(DELAY))) {
					timer.schedule(task, TIMER_DELAY, TIMER_PERIOD);
				} else {
					if (StringUtils.isNotEmpty(TimerParameterBundle.getProperty(FIRST_TIME))) {
						timer.schedule(task, TIMER_FIRST_TIME, TIMER_PERIOD);
					} else {
						log.info("start-time pramater not found, set to default delay 1000ms");
						timer.schedule(task, 1000, TIMER_PERIOD);
					}
				}
			} else {
				log.info(Constants.PARAM_SYS_TIMING_USER_UPDATE + " param not effect, timer cancelled");
			}
		} catch (Exception e) {
			log.info("execute load task error, timer terminate", e);
			throw e;
		}
	}

	public void destroy() throws Exception {
		task.cancel();
		timer.cancel();
	}

}
