package org.developerworld.frameworks.spring.orm.hibernate3.support;

import org.developerworld.frameworks.spring.orm.hibernate3.DwHibernateTemplate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.support.DaoSupport;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

/**
 * 重新构造Spring中的HibernateDaoSupport,
 * 因为原类setSessionFactory方法是私有，导致无法在其创建hibernateTemplate时同时创建DWHibernateTemplate，
 * 所以直接重写该类，使其创建的不是hibernateTemplate,而是自行修改，追加了很多特性功能的DWHibernateTemplate
 * @author Roy.Huang
 * @version 20101129
 */
public class DwHibernateDaoSupport extends DaoSupport {

	private DwHibernateTemplate dwHibernateTemplate = null;

	/**
	 * 
	 * @return
	 */
	public DwHibernateTemplate getDwHibernateTemplate() {
		return dwHibernateTemplate;
	}

	/**
	 * 主要修改原HibernateDaoSupport地方
	 * @param sessionFactory
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		if (this.dwHibernateTemplate == null
				|| sessionFactory != this.dwHibernateTemplate
						.getSessionFactory()) {
			this.dwHibernateTemplate = createDwHibernateTemplate(sessionFactory);
		}
	}

	/**
	 * 请参考原HibernateDaoSupport的描述
	 * @param sessionFactory
	 * @return
	 */
	protected DwHibernateTemplate createDwHibernateTemplate(
			SessionFactory sessionFactory) {
		return new DwHibernateTemplate(sessionFactory);
	}

	/**
	 * 请参考原HibernateDaoSupport的描述
	 * @return
	 */
	public final SessionFactory getSessionFactory() {
		return (this.dwHibernateTemplate != null ? this.dwHibernateTemplate
				.getSessionFactory() : null);
	}

	/**
	 * 请参考原HibernateDaoSupport的描述
	 * @param dwHibernateTemplate
	 */
	public final void setDwHibernateTemplate(
			DwHibernateTemplate dwHibernateTemplate) {
		this.dwHibernateTemplate = dwHibernateTemplate;
	}

	/**
	 * 请参考原HibernateDaoSupport的描述
	 */
	@Override
	protected final void checkDaoConfig() {
		if (this.dwHibernateTemplate == null) {
			throw new IllegalArgumentException(
					"'sessionFactory' or 'dwHibernateTemplate' is required");
		}
	}

	/**
	 * 请参考原HibernateDaoSupport的描述
	 * @return
	 * @throws DataAccessResourceFailureException
	 * @throws IllegalStateException
	 */
	protected final Session getSession()
			throws DataAccessResourceFailureException, IllegalStateException {
		return getSession(this.dwHibernateTemplate.isAllowCreate());
	}

	/**
	 * 请参考原HibernateDaoSupport的描述
	 * @param allowCreate
	 * @return
	 * @throws DataAccessResourceFailureException
	 * @throws IllegalStateException
	 */
	protected final Session getSession(boolean allowCreate)
			throws DataAccessResourceFailureException, IllegalStateException {

		return (!allowCreate ? SessionFactoryUtils.getSession(
				getSessionFactory(), false) : SessionFactoryUtils.getSession(
				getSessionFactory(), this.dwHibernateTemplate
						.getEntityInterceptor(), this.dwHibernateTemplate
						.getJdbcExceptionTranslator()));
	}

	/**
	 * 请参考原HibernateDaoSupport的描述
	 * @param ex
	 * @return
	 */
	protected final DataAccessException convertHibernateAccessException(
			HibernateException ex) {
		return this.dwHibernateTemplate.convertHibernateAccessException(ex);
	}

	/**
	 * 请参考原HibernateDaoSupport的描述
	 * @param session
	 */
	protected final void releaseSession(Session session) {
		SessionFactoryUtils.releaseSession(session, getSessionFactory());
	}
}
