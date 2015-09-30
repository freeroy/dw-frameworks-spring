package org.developerworld.frameworks.spring.web.bind.support;

import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

/**
 * 自定义web数据绑定器
 * @author Roy Huang
 * @version 20130612
 *
 */
public class CustomWebBindingInitializer implements WebBindingInitializer {
	
	private Set<WebBindingInitializer> webBindingInitiallizers=new LinkedHashSet<WebBindingInitializer>();
	
	public Set<WebBindingInitializer> getWebBindingInitiallizers() {
		return webBindingInitiallizers;
	}

	public void setWebBindingInitiallizers(
			Set<WebBindingInitializer> webBindingInitiallizers) {
		this.webBindingInitiallizers = webBindingInitiallizers;
	}

	public void initBinder(WebDataBinder binder, WebRequest request) {
		for(WebBindingInitializer webBindingInitiallizer:webBindingInitiallizers)
			webBindingInitiallizer.initBinder(binder, request);
	}
}