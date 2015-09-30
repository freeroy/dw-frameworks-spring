package org.developerworld.frameworks.spring.web.servlet.support;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.developerworld.commons.servlet.AbstractUrlFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 提供过滤路径的spring Filter 统配表达式优先与正则表达式，排除路径优先包含路径
 * @author Roy Huang
 * @version 0120618
 *
 */
public abstract class AbstractSpringFilter extends AbstractUrlFilter {

	private WebApplicationContext applicationContext = null;

	/**
	 * 获取上下文
	 * @return
	 */
	protected WebApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	/**
	 * 获取bean
	 * @param clazz
	 * @return
	 */
	protected <T> T getBean(Class<T> clazz){
		return applicationContext.getBean(clazz);
	}
	
	/**
	 * 获取bean
	 * @param beanName
	 * @return
	 */
	protected Object getBean(String beanName){
		return applicationContext.getBean(beanName);
	}
	
	/**
	 * 获取bean
	 * @param beanName
	 * @param clazz
	 * @return
	 */
	protected <T> Object getBean(String beanName,Class<T> clazz){
		return applicationContext.getBean(beanName,clazz);
	}
	
	/**
	 * 获取bean
	 * @param beanName
	 * @param args
	 * @return
	 */
	protected <T> Object getBean(String beanName,Object ... args){
		return applicationContext.getBean(beanName, args);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
		applicationContext = WebApplicationContextUtils
				.getRequiredWebApplicationContext(filterConfig
						.getServletContext());
	}
	
	@Override
	public void destroy(){
		applicationContext=null;
	}

}
