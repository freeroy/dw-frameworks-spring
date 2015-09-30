package org.developerworld.frameworks.spring.orm.hibernate3;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * DW HibernateTemplate 类，增加和完善原HibernateTemplate的方法
 * 
 * @author Roy Huang
 * @version 20111017
 * 
 */
public class DwHibernateTemplate extends HibernateTemplate {
	
	public DwHibernateTemplate() {
		super();
	}

	public DwHibernateTemplate(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public DwHibernateTemplate(SessionFactory sessionFactory,
			boolean allowCreate) {
		super(sessionFactory, allowCreate);
	}

	/**
	 * 调整Query
	 * 
	 * @param query
	 */
	public void fixQuery(Query query) {
		if (query != null)
			prepareQuery(query);
	}

	/**
	 * 调整Criteria
	 * 
	 * @param criteria
	 */
	public void fixCriteria(Criteria criteria) {
		if (criteria != null)
			prepareCriteria(criteria);
	}

	/**
	 * 调整Query参数
	 * 
	 * @param query
	 * @param params
	 */
	public void fixQuery(Query query, Map<String, Object> params) {
		if (query != null && params != null) {
			fixQuery(query);
			Iterator<Entry<String, Object>> i = params.entrySet().iterator();
			while (i.hasNext()) {
				Entry<String, Object> e = i.next();
				applyNamedParameterToQuery(query, e.getKey(), e.getValue());
			}
		}
	}

	/**
	 * 调整Query参数
	 * 
	 * @param query
	 * @param params
	 */
	public void fixQuery(Query query, Object params[]) {
		if (query != null && params != null) {
			fixQuery(query);
			for (int i = 0; i < params.length; i++)
				query.setParameter(i, params[i]);
		}
	}

	/**
	 * 调整Query参数
	 * 
	 * @param query
	 * @param names
	 * @param params
	 */
	public void fixQuery(Query query, String paramNames, Object values) {
		fixQuery(query, new String[] { paramNames }, new Object[] { values });
	}

	/**
	 * 调整Query参数
	 * 
	 * @param query
	 * @param names
	 * @param params
	 */
	public void fixQuery(Query query, String paramNames[], Object values[]) {
		if (query == null || paramNames == null || values == null)
			return;
		else if (paramNames.length != values.length)
			throw new IllegalArgumentException(
					"Length of paramNames array must match length of values array");
		else {
			fixQuery(query);
			for (int i = 0; i < values.length; i++)
				applyNamedParameterToQuery(query, paramNames[i], values[i]);
		}
	}

	/**
	 * 根据hql获取单一对象
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T findSingleByQuery(Query query) throws DataAccessException {
		return (T) query.setFirstResult(0).setMaxResults(1).uniqueResult();
	}

	/**
	 * 根据Criteria获取单一对象
	 * 
	 * @param <T>
	 * @param criteria
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T findSingleByCriteria(final Criteria criteria)
			throws DataAccessException {
		return (T) criteria.setFirstResult(0).setMaxResults(1).uniqueResult();
	}

	/**
	 * 根据hql获取单一对象
	 * 
	 * @param <T>
	 * @param hql
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T findSingle(final String hql) throws DataAccessException {
		return execute(new HibernateCallback<T>() {
			public T doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query q = session.createQuery(hql);
				fixQuery(q);
				return (T)findSingleByQuery(q);
			}
		});
	}

	/**
	 * 根据hql和参数获取单一对象
	 * 
	 * @param <T>
	 * @param hql
	 * @param params
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T findSingle(final String hql, final Object params[])
			throws DataAccessException {
		return execute(new HibernateCallback<T>() {
			public T doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query q = session.createQuery(hql);
				fixQuery(q, params);
				return (T)findSingleByQuery(q);
			}
		});
	}

	/**
	 * 根据hql和参数获取单一对象
	 * 
	 * @param <T>
	 * @param hql
	 * @param params
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T findSingleByNamedParam(final String hql,
			final Map<String, Object> params) throws DataAccessException {
		return execute(new HibernateCallback<T>() {
			public T doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query q = session.createQuery(hql);
				fixQuery(q, params);
				return (T)findSingleByQuery(q);
			}
		});
	}

	/**
	 * 根据hql和参数获取单一对象
	 * 
	 * @param <T>
	 * @param hql
	 * @param paramName
	 * @param value
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T findSingleByNamedParam(final String hql,
			final String paramName, final Object value)
			throws DataAccessException {
		return execute(new HibernateCallback<T>() {
			public T doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query q = session.createQuery(hql);
				fixQuery(q, paramName, value);
				return (T)findSingleByQuery(q);
			}
		});
	}

	/**
	 * 命名检索方式获取唯一数据
	 * 
	 * @param queryName
	 * @param value
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T findSingleByNamedQuery(String queryName, Object value)
			throws DataAccessException {
		return (T)findSingleByNamedQuery(queryName, new Object[] { value });
	}

	/**
	 * 命名检索方式获取唯一数据
	 * 
	 * @param <T>
	 * @param queryName
	 * @param values
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T findSingleByNamedQuery(final String queryName,
			final Object... values) throws DataAccessException {
		return execute(new HibernateCallback<T>() {
			public T doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query q = session.getNamedQuery(queryName);
				fixQuery(q, values);
				return (T)findSingleByQuery(q);
			}
		});
	}

	/**
	 * 命名检索方式获取唯一数据
	 * 
	 * @param <T>
	 * @param queryName
	 * @param paramName
	 * @param value
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T findSingleByNamedQueryAndNamedParam(String queryName,
			String paramName, Object value) throws DataAccessException {
		return (T)findSingleByNamedQueryAndNamedParam(queryName,
				new String[] { paramName }, new Object[] { value });
	}

	/**
	 * 命名检索方式获取唯一数据
	 * 
	 * @param <T>
	 * @param queryName
	 * @param params
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T findSingleByNamedQueryAndNamedParam(final String queryName,
			final Map<String, Object> params) throws DataAccessException {
		return execute(new HibernateCallback<T>() {
			public T doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query q = session.getNamedQuery(queryName);
				fixQuery(q, params);
				return (T)findSingleByQuery(q);
			}
		});
	}

	/**
	 * 命名检索方式获取唯一数据
	 * 
	 * @param <T>
	 * @param queryName
	 * @param paramNames
	 * @param values
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T findSingleByNamedQueryAndNamedParam(final String queryName,
			final String[] paramNames, final Object[] values)
			throws DataAccessException {
		return execute(new HibernateCallback<T>() {
			public T doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query q = session.getNamedQuery(queryName);
				fixQuery(q, paramNames, values);
				return (T)findSingleByQuery(q);
			}
		});
	}

	/**
	 * 命名检索方式获取唯一数据
	 * 
	 * @param <T>
	 * @param queryName
	 * @param valueBean
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T findSingleByNamedQueryAndValueBean(final String queryName,
			final Object valueBean) throws DataAccessException {
		return execute(new HibernateCallback<T>() {
			public T doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query q = session.getNamedQuery(queryName).setProperties(
						valueBean);
				fixQuery(q);
				return (T)findSingleByQuery(q);
			}
		});
	}

	/**
	 * 根据DetachedCriteria获取单一对象
	 * 
	 * @param <T>
	 * @param detachedCriteria
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T findSingleByDetachedCriteria(
			final DetachedCriteria detachedCriteria) throws DataAccessException {
		return execute(new HibernateCallback<T>() {
			public T doInHibernate(Session session) throws HibernateException,
					SQLException {
				Criteria c = detachedCriteria.getExecutableCriteria(session);
				fixCriteria(c);
				return (T)findSingleByCriteria(c);
			}
		});
	}

	/**
	 * 根据DetachedCriteria获取单一对象（利用缓存）
	 * 
	 * @param <T>
	 * @param detachedCriteria
	 * @return
	 */
	public <T> T loadSingleByDetachedCriteria(
			final DetachedCriteria detachedCriteria) {
		return execute(new HibernateCallback<T>() {
			public T doInHibernate(Session session) throws HibernateException,
					SQLException {
				Criteria c = detachedCriteria.getExecutableCriteria(session);
				fixCriteria(c);
				c.setCacheable(true);
				return (T)findSingleByCriteria(c);
			}
		});
	}

	/**
	 * 缓存方式加载一个数据
	 * 
	 * @param <T>
	 * @param hql
	 * @param hp
	 * @return
	 */
	public <T> T loadSingleByNamedParam(final String hql,
			final Map<String, Object> params) {
		return execute(new HibernateCallback<T>() {
			public T doInHibernate(Session session) throws HibernateException,
					SQLException {
				Query q = session.createQuery(hql);
				fixQuery(q, params);
				q.setCacheable(true);
				return (T)findSingleByQuery(q);
			}
		});
	}

	/**
	 * 根据hql获取数据数量
	 * 
	 * @param hql
	 * @return
	 * @throws DataAccessException
	 */
	public long findLong(final String hql) throws DataAccessException {
		Long rst = findSingle(hql);
		return rst == null ? 0 : rst;
	}

	/**
	 * 根据hql获取数据数量
	 * 
	 * @param hql
	 * @param params
	 * @return
	 * @throws DataAccessException
	 */
	public long findLongByNamedParam(final String hql, final Map<String, Object> params)
			throws DataAccessException {
		Long rst = findSingleByNamedParam(hql, params);
		return rst == null ? 0 : rst;
	}

	/**
	 * 根据Query获取数据数量
	 * 
	 * @param hql
	 * @param params
	 * @return
	 * @throws DataAccessException
	 */
	public long findLong(final String hql, final Object[] params)
			throws DataAccessException {
		Long rst = findSingle(hql, params);
		return rst == null ? 0 : rst;
	}

	/**
	 * 获取指定实体数量
	 * 
	 * @param <T>
	 * @param entityClass
	 * @return
	 * @throws DataAccessException
	 */
	public <T> long findLongByEntityClass(final Class<T> entityClass)
			throws DataAccessException {
		return execute(new HibernateCallback<Long>() {
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria c = session.createCriteria(entityClass).setProjection(
						Projections.rowCount());
				fixCriteria(c);
				Long rst = findSingleByCriteria(c);
				return rst == null ? 0 : rst;
			}
		});
	}

	/**
	 * 获取指定样板实体的数量
	 * 
	 * @param <T>
	 * @param example
	 * @return
	 * @throws DataAccessException
	 */
	public <T> long findLongByExample(final T example)
			throws DataAccessException {
		return execute(new HibernateCallback<Long>() {
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria c = session.createCriteria(example.getClass())
						.add(Example.create(example))
						.setProjection(Projections.rowCount());
				fixCriteria(c);
				Long rst = findSingleByCriteria(c);
				return rst == null ? 0 : rst;
			}
		});
	}

	/**
	 * 根据指定DetachedCriteria获取数据条目数
	 * 
	 * @param dc
	 * @return
	 */
	public long findLongByDetachedCriteria(final DetachedCriteria dc) {
		return execute(new HibernateCallback<Long>() {
			public Long doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria c = dc.setProjection(Projections.rowCount())
						.getExecutableCriteria(session);
				fixCriteria(c);
				return (Long) c.uniqueResult();
			}
		});
	}

	/**
	 * 扩展原find方法，增加分页参数
	 * 
	 * @param queryString
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws DataAccessException
	 */
	public <T> List<T> find(String queryString, int firstResult, int maxResults)
			throws DataAccessException {
		return find(queryString, firstResult, maxResults, (Object[]) null);
	}

	/**
	 * 扩展原find方法，增加分页参数
	 * 
	 * @param queryString
	 * @param value
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws DataAccessException
	 */
	public <T> List<T> find(String queryString, Object value, int firstResult,
			int maxResults) throws DataAccessException {
		return find(queryString, firstResult, maxResults,
				new Object[] { value });
	}

	/**
	 * 扩展原find方法，增加分页参数
	 * 
	 * @param queryString
	 * @param firstResult
	 * @param maxResults
	 * @param values
	 * @return
	 * @throws DataAccessException
	 */
	public <T> List<T> find(final String queryString, final int firstResult,
			final int maxResults, final Object... values)
			throws DataAccessException {
		return executeFind(new HibernateCallback<List<T>>() {
			public List<T> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery(queryString);
				fixQuery(q, values);
				return q.setFirstResult(firstResult).setMaxResults(maxResults)
						.list();
			}
		});
	}

	/**
	 * 扩展原findByNamedParam方法，增加分页参数
	 * 
	 * @param <T>
	 * @param queryString
	 * @param paramName
	 * @param value
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws DataAccessException
	 */
	public <T> List<T> findByNamedParam(String queryString, String paramName,
			Object value, int firstResult, int maxResults)
			throws DataAccessException {
		return findByNamedParam(queryString, new String[] { paramName },
				new Object[] { value }, firstResult, maxResults);
	}

	/**
	 * 扩展原findByNamedParam方法，增加分页参数
	 * 
	 * @param <T>
	 * @param queryString
	 * @param paramNames
	 * @param values
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws DataAccessException
	 */
	public <T> List<T> findByNamedParam(final String queryString,
			final String[] paramNames, final Object[] values,
			final int firstResult, final int maxResults)
			throws DataAccessException {
		return executeFind(new HibernateCallback<List<T>>() {
			public List<T> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery(queryString);
				fixQuery(q, paramNames, values);
				return q.setFirstResult(firstResult).setMaxResults(maxResults)
						.list();
			}
		});
	}

	/**
	 * 扩展原findByNamedParam方法，增加Map支持
	 * 
	 * @param <T>
	 * @param hql
	 * @param params
	 * @return
	 * @throws DataAccessException
	 */
	public <T> List<T> findByNamedParam(final String hql,
			final Map<String, Object> params) throws DataAccessException {
		return executeFind(new HibernateCallback<List<T>>() {
			public List<T> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery(hql);
				fixQuery(q, params);
				return q.list();
			}
		});
	}

	/**
	 * 扩展原findByNamedParam方法，增加Map支持与分页参数
	 * 
	 * @param hql
	 * @param params
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws DataAccessException
	 */
	public <T> List<T> findByNamedParam(final String hql,
			final Map<String, Object> params, final int firstResult,
			final int maxResults) throws DataAccessException {
		return executeFind(new HibernateCallback<List<T>>() {
			public List<T> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery(hql);
				fixQuery(q, params);
				return (List<T>) q.setFirstResult(firstResult)
						.setMaxResults(maxResults).list();
			}
		});
	}

	/**
	 * 缓存方式加载数据列表
	 * 
	 * @param <T>
	 * @param hql
	 * @param hp
	 * @return
	 */
	public <T> List<T> loadByNamedParam(final String hql,
			final Map<String, Object> params) {
		return executeFind(new HibernateCallback<List<T>>() {
			public List<T> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery(hql);
				fixQuery(q, params);
				q.setCacheable(true);
				return (List<T>) q.list();
			}
		});
	}

	/**
	 * 扩展原findByValueBean方法，增加分页参数
	 * 
	 * @param queryString
	 * @param valueBean
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws DataAccessException
	 */

	public <T> List<T> findByValueBean(final String queryString,
			final Object valueBean, final int firstResult, final int maxResults)
			throws DataAccessException {
		return execute(new HibernateCallback<List<T>>() {
			public List<T> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery(queryString).setProperties(
						valueBean);
				fixQuery(q);
				return q.setFirstResult(firstResult).setMaxResults(maxResults)
						.list();
			}
		});
	}

	/**
	 * 扩展原findByNamedQuery方法，增加分页参数
	 * 
	 * @param queryName
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws DataAccessException
	 */
	public <T> List<T> findByNamedQuery(String queryName, int firstResult,
			int maxResults) throws DataAccessException {
		return findByNamedQuery(queryName, firstResult, maxResults,
				(Object[]) null);
	}

	/**
	 * 扩展原findByNamedQuery方法，增加分页参数
	 * 
	 * @param queryName
	 * @param value
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws DataAccessException
	 */
	public <T> List<T> findByNamedQuery(String queryName, Object value,
			int firstResult, int maxResults) throws DataAccessException {
		return findByNamedQuery(queryName, firstResult, maxResults,
				new Object[] { value });
	}

	/**
	 * 扩展原findByNamedQuery方法，增加分页参数
	 * 
	 * @param queryName
	 * @param firstResult
	 * @param maxResults
	 * @param values
	 * @return
	 * @throws DataAccessException
	 */

	public <T> List<T> findByNamedQuery(final String queryName,
			final int firstResult, final int maxResults, final Object... values)
			throws DataAccessException {
		return execute(new HibernateCallback<List<T>>() {
			public List<T> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.getNamedQuery(queryName);
				fixQuery(q, values);
				return q.setFirstResult(firstResult).setMaxResults(maxResults)
						.list();
			}
		});
	}

	/**
	 * 扩展原findByNamedQueryAndNamedParam方法，增加分页参数
	 * 
	 * @param queryName
	 * @param paramName
	 * @param value
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws DataAccessException
	 */
	public <T> List<T> findByNamedQueryAndNamedParam(String queryName,
			String paramName, Object value, int firstResult, int maxResults)
			throws DataAccessException {
		return findByNamedQueryAndNamedParam(queryName,
				new String[] { paramName }, new Object[] { value },
				firstResult, maxResults);
	}

	/**
	 * 扩展原findByNamedQueryAndNamedParam方法，增加分页参数
	 * 
	 * @param queryName
	 * @param paramNames
	 * @param values
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws DataAccessException
	 */

	public <T> List<T> findByNamedQueryAndNamedParam(final String queryName,
			final String[] paramNames, final Object[] values,
			final int firstResult, final int maxResults)
			throws DataAccessException {
		return execute(new HibernateCallback<List<T>>() {
			public List<T> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.getNamedQuery(queryName);
				fixQuery(q, paramNames, values);
				return q.setFirstResult(firstResult).setMaxResults(maxResults)
						.list();
			}
		});
	}

	/**
	 * 扩展原findByNamedQueryAndValueBean方法，增加分页参数
	 * 
	 * @param queryName
	 * @param valueBean
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws DataAccessException
	 */
	public <T> List<T> findByNamedQueryAndValueBean(final String queryName,
			final Object valueBean, final int firstResult, final int maxResults)
			throws DataAccessException {
		return execute(new HibernateCallback<List<T>>() {
			public List<T> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.getNamedQuery(queryName).setProperties(
						valueBean);
				fixQuery(q);
				return q.setFirstResult(firstResult).setMaxResults(maxResults)
						.list();
			}
		});
	}

	/**
	 * 根据DetachedCriteria通过二级缓存获取数据
	 * 
	 * @param detachedCriteria
	 * @return
	 */
	public <T> List<T> loadByDetachedCriteria(
			final DetachedCriteria detachedCriteria) {
		return execute(new HibernateCallback<List<T>>() {
			public List<T> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Criteria c = detachedCriteria.getExecutableCriteria(session);
				fixCriteria(c);
				c.setCacheable(true);
				return c.list();
			}
		});
	}

	/**
	 * 扩展原iterate方法，增加分页参数
	 * 
	 * @param queryString
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws DataAccessException
	 */
	public <T> Iterator<T> iterate(String queryString, int firstResult,
			int maxResults) throws DataAccessException {
		return iterate(queryString, firstResult, maxResults, (Object[]) null);
	}

	/**
	 * 扩展原iterate方法，增加分页参数
	 * 
	 * @param queryString
	 * @param value
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws DataAccessException
	 */
	public <T> Iterator<T> iterate(String queryString, Object value,
			int firstResult, int maxResults) throws DataAccessException {
		return iterate(queryString, firstResult, maxResults,
				new Object[] { value });
	}

	/**
	 * 扩展原iterate方法，增加分页参数
	 * 
	 * @param queryString
	 * @param firstResult
	 * @param maxResults
	 * @param values
	 * @return
	 * @throws DataAccessException
	 */

	public <T> Iterator<T> iterate(final String queryString,
			final int firstResult, final int maxResults, final Object... values)
			throws DataAccessException {
		return execute(new HibernateCallback<Iterator<T>>() {
			public Iterator<T> doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery(queryString);
				fixQuery(q, values);
				return q.setFirstResult(firstResult).setMaxResults(maxResults)
						.iterate();
			}
		});
	}

	/**
	 * 扩展原bulkUpdate方法，支持命名参数
	 * 
	 * @param queryString
	 * @param params
	 * @return
	 * @throws DataAccessException
	 */
	public int bulkUpdate(final String queryString,
			final Map<String, Object> params) throws DataAccessException {
		return execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query q = session.createQuery(queryString);
				fixQuery(q, params);
				return q.executeUpdate();
			}
		});
	}
}
