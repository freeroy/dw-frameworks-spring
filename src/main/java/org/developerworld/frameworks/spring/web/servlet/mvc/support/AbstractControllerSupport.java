package org.developerworld.frameworks.spring.web.servlet.mvc.support;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Spring MVC 基础抽象类
 * 
 * @author Roy Huang
 * @version 20111230
 * 
 */
public abstract class AbstractControllerSupport implements
		ApplicationContextAware {

	private final static Log log = LogFactory
			.getLog(AbstractControllerSupport.class);

	private ObjectMapper objectMapper = new ObjectMapper();

	private ApplicationContext context;

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.context = context;
	}

	public ApplicationContext getApplicationContext() throws BeansException {
		return context;
	}

	/**
	 * info日志操作
	 * 
	 * @param message
	 */
	protected void info(Object message) {
		log.info(message);
	}

	/**
	 * info日志操作
	 * 
	 * @param message
	 * @param throwable
	 */
	protected void info(Object message, Throwable throwable) {
		log.info(message, throwable);
	}

	/**
	 * error日志操作
	 * 
	 * @param throwable
	 */
	protected void error(Object throwable) {
		log.error(throwable);
	}

	/**
	 * error日志操作
	 * 
	 * @param message
	 * @param throwable
	 */
	protected void error(Object message, Throwable throwable) {
		log.error(message, throwable);
	}

	/**
	 * debug日志操作
	 * 
	 * @param message
	 */
	protected void debug(Object message) {
		log.debug(message);
	}

	/**
	 * debug日志操作
	 * 
	 * @param message
	 * @param throwable
	 */
	protected void debug(Object message, Throwable throwable) {
		log.debug(message, throwable);
	}

	/**
	 * warn日志操作
	 * 
	 * @param message
	 */
	protected void warn(Object message) {
		log.warn(message);
	}

	/**
	 * warn日志操作
	 * 
	 * @param message
	 * @param throwable
	 */
	protected void warn(Object message, Throwable throwable) {
		log.warn(message, throwable);
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
		return context.getMessage(key, args, locale);
	}

	/**
	 * 获取国际化信息
	 * 
	 * @param key
	 * @return
	 */
	protected String getMessage(String key) {
		return getMessage(key, null, getLocale());
	}

	/**
	 * 获取国际化信息
	 * 
	 * @param key
	 * @param args
	 * @return
	 */
	protected String getMessage(String key, Object[] args) {
		return getMessage(key, args, getLocale());
	}

	/**
	 * 获取本地化信息
	 * 
	 * @return
	 */
	protected Locale getLocale() {
		Locale rst = null;
		RequestAttributes requestAttributes = RequestContextHolder
				.getRequestAttributes();
		if (requestAttributes != null
				&& requestAttributes instanceof ServletRequestAttributes) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes)
					.getRequest();
			if (request != null)
				rst = getLocale(request);
		}
		if (rst == null)
			rst = Locale.getDefault();
		return rst;
	}

	/**
	 * 获取本地化信息
	 * 
	 * @param request
	 * @return
	 */
	protected Locale getLocale(HttpServletRequest request) {
		Locale rst = RequestContextUtils.getLocale(request);
		if (rst == null)
			rst = Locale.getDefault();
		return rst;
	}

	/**
	 * 把数据转化成json字符串
	 * @param object
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	protected String objectToJsonString(Object object) throws JsonGenerationException,
			JsonMappingException, IOException {
		return objectMapper.writeValueAsString(object);
	}
}
