package org.developerworld.frameworks.spring.core.convert.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;

/**
 * 日期转换器
 * 
 * @author Roy Huang
 * @version 20130612
 * 
 */
public class DateConverter implements Converter<String, Date> {
	
	private final static Log log=LogFactory.getLog(DateConverter.class);

	public Date convert(String source) {
		SimpleDateFormat dateFormat = null;
		if (source.indexOf(" ") != -1)
			dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		else
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		try {
			return dateFormat.parse(source);
		} catch (ParseException e) {
			log.warn(e);
			return null;
		}
	}

}
