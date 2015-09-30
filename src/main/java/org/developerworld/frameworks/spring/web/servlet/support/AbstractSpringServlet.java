package org.developerworld.frameworks.spring.web.servlet.support;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 基于Spring的抽象Servlet
 * 
 * @author Roy Huang
 * @version 20120618
 * 
 */
public abstract class AbstractSpringServlet extends HttpServlet {

	private WebApplicationContext applicationContext = null;

	/**
	 * 获取上下文
	 * 
	 * @return
	 */
	protected WebApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取bean
	 * 
	 * @param clazz
	 * @return
	 */
	protected <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	/**
	 * 获取bean
	 * 
	 * @param beanName
	 * @return
	 */
	protected Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	/**
	 * 获取bean
	 * 
	 * @param beanName
	 * @param clazz
	 * @return
	 */
	protected <T> Object getBean(String beanName, Class<T> clazz) {
		return applicationContext.getBean(beanName, clazz);
	}

	/**
	 * 获取bean
	 * 
	 * @param beanName
	 * @param args
	 * @return
	 */
	protected <T> Object getBean(String beanName, Object... args) {
		return applicationContext.getBean(beanName, args);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		applicationContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(config.getServletContext());
	}

	@Override
	public void destroy() {
		super.destroy();
		applicationContext = null;
	}
}
