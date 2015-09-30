package org.developerworld.frameworks.spring.orm.jpa;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.JpaTemplate;

/**
 * Dw的JPA模板
 * 
 * @author Roy Huang
 * @version 20111221
 * 
 */
public class DwJpaTemplate extends JpaTemplate {

	public DwJpaTemplate() {
		super();
	}

	public DwJpaTemplate(EntityManagerFactory emf) {
		super(emf);
	}

	public DwJpaTemplate(EntityManager em) {
		super(em);
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
				query.setParameter(e.getKey(), e.getValue());
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
				query.setParameter(paramNames[i], values[i]);
		}
	}

	/**
	 * 根据jpql获取单一对象
	 * 
	 * @param <T>
	 * @param query
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T findSingleByQuery(Query query) throws DataAccessException {
		// 为避免发生空数据时爆出异常，这里不使用getSingleResult
		// return (T)
		// query.setFirstResult(0).setMaxResults(1).getSingleResult();
		T rst = null;
		List list = query.setFirstResult(0).setMaxResults(1).getResultList();
		if (list != null && list.size() > 0)
			rst = (T) list.get(0);
		return rst;
	}

	/**
	 * 根据jpql获取单一对象
	 * 
	 * @param <T>
	 * @param jpql
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T findSingle(final String jpql) throws DataAccessException {
		return execute(new JpaCallback<T>() {
			public T doInJpa(EntityManager em) throws PersistenceException {
				Query q = em.createQuery(jpql);
				fixQuery(q);
				return (T)findSingleByQuery(q);
			}
		});
	}

	/**
	 * 根据jpql和参数获取单一对象
	 * 
	 * @param <T>
	 * @param jpql
	 * @param params
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T findSingle(final String jpql, final Object params[])
			throws DataAccessException {
		return execute(new JpaCallback<T>() {
			public T doInJpa(EntityManager em) throws PersistenceException {
				Query q = em.createQuery(jpql);
				fixQuery(q, params);
				return (T)findSingleByQuery(q);
			}
		});
	}

	/**
	 * 根据jpql和参数获取单一对象
	 * 
	 * @param <T>
	 * @param jpql
	 * @param params
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T findSingleByNamedParams(final String jpql,
			final Map<String, Object> params) throws DataAccessException {
		return execute(new JpaCallback<T>() {
			public T doInJpa(EntityManager em) throws PersistenceException {
				Query q = em.createQuery(jpql);
				fixQuery(q, params);
				return (T)findSingleByQuery(q);
			}
		});
	}

	/**
	 * 根据jpql和参数获取单一对象
	 * 
	 * @param <T>
	 * @param jpql
	 * @param paramName
	 * @param value
	 * @return
	 * @throws DataAccessException
	 */
	public <T> T findSingleByNamedParams(final String jpql,
			final String paramName, final Object value)
			throws DataAccessException {
		return execute(new JpaCallback<T>() {
			public T doInJpa(EntityManager em) throws PersistenceException {
				Query q = em.createQuery(jpql);
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
		return execute(new JpaCallback<T>() {
			public T doInJpa(EntityManager em) throws PersistenceException {
				Query q = em.createNamedQuery(queryName);
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
	public <T> T findSingleByNamedQueryAndNamedParams(String queryName,
			String paramName, Object value) throws DataAccessException {
		return (T)findSingleByNamedQueryAndNamedParams(queryName,
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
	public <T> T findSingleByNamedQueryAndNamedParams(final String queryName,
			final Map<String, Object> params) throws DataAccessException {
		return execute(new JpaCallback<T>() {
			public T doInJpa(EntityManager em) throws PersistenceException {
				Query q = em.createNamedQuery(queryName);
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
	public <T> T findSingleByNamedQueryAndNamedParams(final String queryName,
			final String[] paramNames, final Object[] values)
			throws DataAccessException {
		return execute(new JpaCallback<T>() {
			public T doInJpa(EntityManager em) throws PersistenceException {
				Query q = em.createNamedQuery(queryName);
				fixQuery(q, paramNames, values);
				return (T)findSingleByQuery(q);
			}
		});
	}

	/**
	 * 根据jpql获取数据数量
	 * 
	 * @param jpql
	 * @return
	 * @throws DataAccessException
	 */
	public long findLong(final String jpql) throws DataAccessException {
		Long rst = findSingle(jpql);
		return rst == null ? 0 : rst;
	}

	/**
	 * 根据jpql获取数据数量
	 * 
	 * @param jpql
	 * @param params
	 * @return
	 * @throws DataAccessException
	 */
	public long findLong(final String jpql, final Map<String, Object> params)
			throws DataAccessException {
		Long rst = findSingleByNamedParams(jpql, params);
		return rst == null ? 0 : rst;
	}

	/**
	 * 根据Query获取数据数量
	 * 
	 * @param jpql
	 * @param params
	 * @return
	 * @throws DataAccessException
	 */
	public long findLong(final String jpql, final Object[] params)
			throws DataAccessException {
		Long rst = findSingle(jpql, params);
		return rst == null ? 0 : rst;
	}

	/**
	 * 根据Query获取数据数量
	 * 
	 * @param jpql
	 * @param paramName
	 * @param paramValue
	 * @return
	 */
	public long findLong(final String jpql, String paramName, Object paramValue) {
		Long rst = findSingleByNamedParams(jpql, paramName, paramValue);
		return rst == null ? 0 : rst;
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
		return executeFind(new JpaCallback<List<T>>() {
			public List<T> doInJpa(EntityManager em)
					throws PersistenceException {
				Query q = em.createQuery(queryString);
				fixQuery(q, values);
				return q.setFirstResult(firstResult).setMaxResults(maxResults)
						.getResultList();
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
	public <T> List<T> findByNamedParams(String queryString, String paramName,
			Object value, int firstResult, int maxResults)
			throws DataAccessException {
		return findByNamedParams(queryString, new String[] { paramName },
				new Object[] { value }, firstResult, maxResults);
	}

	/**
	 * 扩展原findByNamedParam方法
	 * 
	 * @param queryString
	 * @param paramName
	 * @param value
	 * @return
	 * @throws DataAccessException
	 */
	public <T> List<T> findByNamedParams(String queryString, String paramName,
			Object value) throws DataAccessException {
		return findByNamedParams(queryString, new String[] { paramName },
				new Object[] { value });
	}

	/**
	 * 扩展原findByNamedParam方法
	 * 
	 * @param queryString
	 * @param paramNames
	 * @param values
	 * @return
	 */
	public <T> List<T> findByNamedParams(final String queryString,
			final String[] paramNames, final Object[] values) {
		return executeFind(new JpaCallback<List<T>>() {
			public List<T> doInJpa(EntityManager em)
					throws PersistenceException {
				Query q = em.createQuery(queryString);
				fixQuery(q, paramNames, values);
				return q.getResultList();
			}
		});
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
	public <T> List<T> findByNamedParams(final String queryString,
			final String[] paramNames, final Object[] values,
			final int firstResult, final int maxResults)
			throws DataAccessException {
		return executeFind(new JpaCallback<List<T>>() {
			public List<T> doInJpa(EntityManager em)
					throws PersistenceException {
				Query q = em.createQuery(queryString);
				fixQuery(q, paramNames, values);
				return q.setFirstResult(firstResult).setMaxResults(maxResults)
						.getResultList();
			}
		});
	}

	/**
	 * 扩展原findByNamedParam方法，增加Map支持与分页参数
	 * 
	 * @param jpql
	 * @param params
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws DataAccessException
	 */
	public <T> List<T> findByNamedParams(final String jpql,
			final Map<String, Object> params, final int firstResult,
			final int maxResults) throws DataAccessException {
		return executeFind(new JpaCallback<List<T>>() {
			public List<T> doInJpa(EntityManager em)
					throws PersistenceException {
				Query q = em.createQuery(jpql);
				fixQuery(q, params);
				return q.setFirstResult(firstResult).setMaxResults(maxResults)
						.getResultList();
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
		return executeFind(new JpaCallback<List<T>>() {
			public List<T> doInJpa(EntityManager em)
					throws PersistenceException {
				Query q = em.createNamedQuery(queryName);
				fixQuery(q, values);
				return q.setFirstResult(firstResult).setMaxResults(maxResults)
						.getResultList();
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
	public <T> List<T> findByNamedQueryAndNamedParams(String queryName,
			String paramName, Object value, int firstResult, int maxResults)
			throws DataAccessException {
		return findByNamedQueryAndNamedParams(queryName,
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
	public <T> List<T> findByNamedQueryAndNamedParams(final String queryName,
			final String[] paramNames, final Object[] values,
			final int firstResult, final int maxResults)
			throws DataAccessException {
		return executeFind(new JpaCallback<List<T>>() {
			public List<T> doInJpa(EntityManager em)
					throws PersistenceException {
				Query q = em.createNamedQuery(queryName);
				fixQuery(q, paramNames, values);
				return q.setFirstResult(firstResult).setMaxResults(maxResults)
						.getResultList();
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
		return execute(new JpaCallback<Integer>() {
			public Integer doInJpa(EntityManager em)
					throws PersistenceException {
				Query q = em.createQuery(queryString);
				fixQuery(q, params);
				return q.executeUpdate();
			}
		});
	}

	/**
	 * 扩展原bulkUpdate方法，支持命名参数
	 * 
	 * @param queryString
	 * @param paramName
	 * @param value
	 * @return
	 * @throws DataAccessException
	 */
	public int bulkUpdate(String queryString, String paramName, Object value)
			throws DataAccessException {
		return bulkUpdate(queryString, new String[] { paramName },
				new Object[] { value });
	}

	/**
	 * 扩展原bulkUpdate方法，支持命名参数
	 * 
	 * @param queryString
	 * @param paramNames
	 * @param values
	 * @return
	 * @throws DataAccessException
	 */
	public int bulkUpdate(final String queryString, final String[] paramNames,
			final Object[] values) throws DataAccessException {
		return execute(new JpaCallback<Integer>() {
			public Integer doInJpa(EntityManager em)
					throws PersistenceException {
				Query q = em.createQuery(queryString);
				fixQuery(q, paramNames, values);
				return q.executeUpdate();
			}
		});
	}

}
