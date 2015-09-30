package org.developerworld.frameworks.spring.web.servlet.tags;

import java.util.Locale;

import org.developerworld.commons.servlet.jsp.tagext.BodyTagSupport;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * spring支持抽象tag类
 * 
 * @author Roy Huang
 * @version 20120301
 * 
 */
public class SpringBodyTagSupport extends BodyTagSupport {

	private ApplicationContext applicationContext;

	/**
	 * 获取applicationContext
	 * 
	 * @return
	 */
	protected ApplicationContext getApplicationContext() {
		if (applicationContext == null)
			applicationContext = WebApplicationContextUtils
					.getWebApplicationContext(getServletContext());
		return applicationContext;
	}

	/**
	 * 获取spring bean
	 * 
	 * @param beanName
	 * @return
	 */
	protected Object getBean(String beanName) {
		return getApplicationContext().getBean(beanName);
	}

	/**
	 * 获取spring bean
	 * 
	 * @param beanName
	 * @param args
	 * @return
	 */
	protected Object getBean(String beanName, Object... args) {
		return getApplicationContext().getBean(beanName, args);
	}

	/**
	 * 获取spring bean
	 * 
	 * @param beanName
	 * @param beanClass
	 * @return
	 */
	protected <T> T getBean(String beanName, Class<T> beanClass) {
		return getApplicationContext().getBean(beanName, beanClass);
	}

	/**
	 * 获取国际化信息
	 * 
	 * @param key
	 * @return
	 */
	protected String getMessage(String key) {
		return getMessage(key, null, getDefauleLocal());
	}

	/**
	 * 获取国际化信息
	 * 
	 * @param key
	 * @param defauleValue
	 * @return
	 */
	protected String getMessage(String key, String defauleValue) {
		return getMessage(key, null, defauleValue, getDefauleLocal());
	}

	/**
	 * 获取国际化信息
	 * 
	 * @param key
	 * @param args
	 * @return
	 */
	protected String getMessage(String key, Object[] args) {
		return getMessage(key, args, getDefauleLocal());
	}

	/**
	 * 获取国际化信息
	 * 
	 * @param key
	 * @param args
	 * @param defauleValue
	 * @return
	 */
	protected String getMessage(String key, Object[] args, String defauleValue) {
		return getMessage(key, args, defauleValue, getDefauleLocal());
	}

	/**
	 * 获取国际化信息
	 * 
	 * @param key
	 * @param args
	 * @param locale
	 * @return
	 */
	protected String getMessage(String key, Object[] args, Locale locale) {
		return getApplicationContext().getMessage(key, args, locale);
	}

	/**
	 * 获取国际化信息
	 * 
	 * @param key
	 * @param args
	 * @param defaultValue
	 * @param locale
	 * @return
	 */
	protected String getMessage(String key, Object[] args, String defaultValue,
			Locale locale) {
		return getApplicationContext().getMessage(key, args, defaultValue,
				locale);
	}

	/**
	 * 获取spring bean
	 * 
	 * @param beanClass
	 * @return
	 */
	protected <T> T getBean(Class<T> beanClass) {
		return getApplicationContext().getBean(beanClass);
	}
}
