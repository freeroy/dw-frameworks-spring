package org.developerworld.frameworks.spring.orm.jpa.web.servlet.tags;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.lang.StringUtils;
import org.developerworld.frameworks.spring.web.servlet.tags.SpringBodyTagSupport;

public class AbstractBodyTagSupport extends SpringBodyTagSupport {

	private String entityManagerFactoryBeanName;

	public void setEntityManagerFactoryBeanName(
			String entityManagerFactoryBeanName) {
		this.entityManagerFactoryBeanName = entityManagerFactoryBeanName;
	}

	private EntityManagerFactory entityManagerFactory;

	/**
	 * 获取管理器
	 * 
	 * @return
	 */
	protected EntityManager buildEntityManager() {
		EntityManager rst = null;
		if (entityManagerFactory == null)
			if (StringUtils.isNotBlank(entityManagerFactoryBeanName))
				entityManagerFactory = getBean(entityManagerFactoryBeanName,
						EntityManagerFactory.class);
			else
				entityManagerFactory = getBean(EntityManagerFactory.class);
		if (entityManagerFactory != null)
			rst = entityManagerFactory.createEntityManager();
		return rst;
	}
}
