package fmss.common.util;

import java.util.HashSet;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import fmss.dao.entity.LoginDO;
import fmss.services.OnlineService;
import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;

public class SessionLoginListener implements HttpSessionAttributeListener,
		HttpSessionListener, ServletContextListener {

	


	public void contextInitialized(ServletContextEvent event) {

	}

	public void contextDestroyed(ServletContextEvent event) {

	}

	public void sessionCreated(HttpSessionEvent event) {

	}

	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();

		
	}

	public void attributeAdded(HttpSessionBindingEvent event) {

	}

	public void attributeRemoved(HttpSessionBindingEvent event) {
		String name = event.getName();
		Object value = event.getValue();
		HttpSession session = event.getSession();


	}

	public void attributeReplaced(HttpSessionBindingEvent arg0) {

	}



}