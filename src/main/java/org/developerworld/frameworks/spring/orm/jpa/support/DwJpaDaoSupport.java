package org.developerworld.frameworks.spring.orm.jpa.support;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.developerworld.frameworks.spring.orm.jpa.DwJpaTemplate;
import org.springframework.dao.support.DaoSupport;

/**
 * DW Jpa 支持类
 * @author Roy Huang
 * @version 20111016
 *
 */
public class DwJpaDaoSupport extends DaoSupport {

	private DwJpaTemplate dwJpaTemplate = null;

	/**
	 * 
	 * @param dwHibernateTemplate
	 */
	public void setDwJpaTemplate(DwJpaTemplate dwJpaTemplate) {
		this.dwJpaTemplate = dwJpaTemplate;
	}
	
	public DwJpaTemplate getDwJpaTemplate() {
		return dwJpaTemplate;
	}

	public final void setEntityManagerFactory(
			EntityManagerFactory entityManagerFactory) {
		if (dwJpaTemplate == null
				|| entityManagerFactory != dwJpaTemplate
						.getEntityManagerFactory())
			this.dwJpaTemplate = createDwJpaTemplate(entityManagerFactory);
	}

	public final void setEntityManager(EntityManager entityManager) {
		this.dwJpaTemplate = createDwJpaTemplate(entityManager);
	}

	protected DwJpaTemplate createDwJpaTemplate(
			EntityManagerFactory entityManagerFactory) {
		return new DwJpaTemplate(entityManagerFactory);
	}

	protected DwJpaTemplate createDwJpaTemplate(EntityManager entityManager) {
		return new DwJpaTemplate(entityManager);
	}

	@Override
	protected final void checkDaoConfig() {
		if (this.dwJpaTemplate == null) {
			throw new IllegalArgumentException(
					"entityManagerFactory or jpaTemplate is required");
		}
	}

}
