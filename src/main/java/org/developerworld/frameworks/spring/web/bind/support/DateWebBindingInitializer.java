package org.developerworld.frameworks.spring.web.bind.support;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

/**
 * 日期类型自定义数据绑定器
 * @author Roy Huang
 * @version 20130612
 *
 */
public class DateWebBindingInitializer implements WebBindingInitializer {

	private final static Log log = LogFactory.getLog(DateWebBindingInitializer.class);

	public void initBinder(WebDataBinder binder, WebRequest request) {

		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {

			private SimpleDateFormat dateFormat;

			public void setAsText(String value) {
				if (StringUtils.isBlank(value))
					setValue(null);
				else
					try {
						if (value.indexOf(" ") != -1)
							dateFormat = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
						else
							dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						setValue(dateFormat.parse(value));
					} catch (ParseException e) {
						setValue(null);
						log.error(e);
					}
			}

			public String getAsText() {
				if (getValue() != null && dateFormat != null)
					return dateFormat.format((Date) getValue());
				else
					return null;
			}
		});
	}
}