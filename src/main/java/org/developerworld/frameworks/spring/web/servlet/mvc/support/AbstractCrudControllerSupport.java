package org.developerworld.frameworks.spring.web.servlet.mvc.support;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 抽象 增删改查 Controler类，存放公共方法或成员变量(注意，所有Controller默认是单例的)
 * 
 * @author Roy Huang
 * @version 20111230
 * 
 */
public abstract class AbstractCrudControllerSupport extends
		AbstractControllerSupport {

	protected final String INDEX = "index";
	protected final String EDIT = "edit";
	protected final String DELETE = "delete";
	protected final String DESTORY = "destory";
	protected final String UPDATE = "update";
	protected final String CREATE = "create";
	protected final String NEW = "new";
	protected final String SHOW = "show";
	private String resourceName;

	public AbstractCrudControllerSupport() {
		// 若子类已经重载了该方法并非控制，则不执行
		if (StringUtils.isBlank(getResourceName())) {
			if (StringUtils.isBlank(resourceName)) {
				Class<? extends AbstractCrudControllerSupport> _class = getClass();
				// 根据@RequestMapping的value值设置resourceName
				if (_class.isAnnotationPresent(RequestMapping.class)) {
					RequestMapping rm = _class
							.getAnnotation(RequestMapping.class);
					if (rm.value() != null && rm.value().length > 0) {
						for (String v : rm.value()) {
							if (StringUtils.isNotBlank(v)) {
								resourceName = v;
								break;
							}
						}
					}
					resourceName = fixResourceName(resourceName);
				}
				// 根据@Controller的value值设置resourceName
				if (StringUtils.isEmpty(resourceName)
						&& _class.isAnnotationPresent(Controller.class)) {
					Controller c = _class.getAnnotation(Controller.class);
					if (StringUtils.isNotBlank(c.value()))
						resourceName = c.value();
				}
				if (StringUtils.isEmpty(getResourceName()))
					warn(getClass()
							+ ":AbstractCrudControllerSupport subclass must have "
							+ "@RequestMapping.value or @Controller.value in class define,"
							+ "if not must overwrite getResourceName method and return a not empty value!");
			}
		}
	}

	/**
	 * 需要对mapping的路径进行字符处理，把"{...}"转换成"-"
	 */
	private String fixResourceName(String resourceName) {
		if (StringUtils.isNotBlank(resourceName)) {
			int begin = -1;
			int end = -1;
			while (true) {
				begin = resourceName.indexOf("{");
				if (begin < 0)
					break;
				else if (begin > 0
						&& resourceName.substring(begin - 1, begin).equals("/"))
					begin--;
				end = resourceName.indexOf("}", begin + 1);
				if (end < 0)
					break;
				else if (end + 1 < resourceName.length()
						&& resourceName.substring(end + 1, end + 2).equals("/"))
					end++;
				if (end + 1 < resourceName.length())
					resourceName = resourceName.substring(0, begin) + "-"
							+ resourceName.substring(end + 1);
				else
					resourceName = resourceName.substring(0, begin);
			}
		}
		return resourceName;
	}

	protected String getResourceName() {
		return resourceName;
	}

	protected String indexView() {
		return getResourceName() + "-" + INDEX;
	}

	protected ModelAndView indexModelAndView() {
		return new ModelAndView(indexView());
	}

	protected String newView() {
		return getResourceName() + "-" + NEW;
	}

	protected ModelAndView newModelAndView() {
		return new ModelAndView(newView());
	}

	protected String editView() {
		return getResourceName() + "-" + EDIT;
	}

	protected ModelAndView editModelAndView() {
		return new ModelAndView(editView());
	}

	protected String deleteView() {
		return getResourceName() + "-" + DELETE;
	}

	protected ModelAndView deleteModelAndView() {
		return new ModelAndView(deleteView());
	}

	protected String updateView() {
		return getResourceName() + "-" + UPDATE;
	}

	protected ModelAndView updateModelAndView() {
		return new ModelAndView(updateView());
	}

	protected String createView() {
		return getResourceName() + "-" + CREATE;
	}

	protected ModelAndView createModelAndView() {
		return new ModelAndView(createView());
	}

	protected String showView() {
		return getResourceName() + "-" + SHOW;
	}

	protected ModelAndView showModelAndView() {
		return new ModelAndView(showView());
	}
}
