package org.developerworld.frameworks.spring.orm.hibernate3;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.developerworld.commons.dbutils.crud.GenericDao;
import org.developerworld.frameworks.spring.orm.hibernate3.support.DwHibernateDaoSupport;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;

/**
 * Spring2 的 Hibernate3 GenericDao 实现
 * 
 * @author Roy.Huang
 * @version 20110421
 * @param <T>
 * @param <PK>
 */
public abstract class AbstractHibernateGenericDaoImpl<T, PK extends Serializable>
		extends DwHibernateDaoSupport implements GenericDao<T, PK> {

	protected Class<T> entityClass;

	public AbstractHibernateGenericDaoImpl() {
		Type type = getClass().getGenericSuperclass();
		while(type!=null && !(type instanceof ParameterizedType))
			type=type.getClass().getGenericSuperclass();
		if (type!=null && type instanceof ParameterizedType) {
			Type types[] = ((ParameterizedType) type).getActualTypeArguments();
			if (types != null && types.length > 0 && types[0] instanceof Class)
				entityClass = (Class<T>) types[0];
		}
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public String getEntityClassName() {
		return getEntityClass().getName();
	}

	public long findCount() {
		return getDwHibernateTemplate().findLongByEntityClass(getEntityClass());
	}

	public long findCountByExample(T example) {
		return getDwHibernateTemplate().findLongByExample(example);
	}

	public void save(T entity) {
		getDwHibernateTemplate().save(entity);
	}

	public void update(T entity) {
		getDwHibernateTemplate().update(entity);
	}

	public void delete(final PK id) {
		delete(getDwHibernateTemplate().load(getEntityClass(), id));
	}

	public void delete(T entity) {
		getDwHibernateTemplate().delete(entity);
	}

	public void delete(Collection<T> entitys) {
		getDwHibernateTemplate().deleteAll(entitys);
	}

	public void delete(final PK[] ids) {
		for (PK id : ids)
			delete(id);
	}

	public List<T> findList() {
		return (List<T>) getDwHibernateTemplate().find(
				"from " + getEntityClassName());
	}

	public List<T> findList(final int pageNum, final int pageSize) {
		return getDwHibernateTemplate().find("from " + getEntityClassName(),
				(pageNum - 1) * pageSize, pageSize);
	}

	public List<T> findList(String order, int pageNum, int pageSize) {
		String hql = "from " + getEntityClassName()
				+ (StringUtils.isBlank(order) ? "" : " order by " + order);
		return getDwHibernateTemplate().find(hql, (pageNum - 1) * pageSize,
				pageSize);
	}

	public List<T> findList(String order) {
		String hql = "from " + getEntityClassName()
				+ (StringUtils.isBlank(order) ? "" : " order by " + order);
		return getDwHibernateTemplate().find(hql);
	}

	public T findById(PK id) {
		return findByPk(id);
	}
	
	public T findByPk(PK id) {
		return (T) getDwHibernateTemplate().get(entityClass, id);
	}

	public List<T> findByExample(T example) {
		return getDwHibernateTemplate().findByExample(example);
	}

	public List<T> findByExample(T example, int pageNum, int pageSize) {
		return getDwHibernateTemplate().findByExample(example,
				(pageNum - 1) * pageSize, pageSize);
	}

	public List<T> findByExample(T example, int pageNum, int pageSize,
			String order) {
		DetachedCriteria dc = buildDetachedCriteria().add(
				Example.create(example));
		if (StringUtils.isNotEmpty(order)) {
			String[] orders = order.split(",");
			if (orders != null) {
				for (String o : orders) {
					String[] _order = o.split(" ");
					if (_order == null || _order.length == 0)
						continue;
					if (o.toLowerCase().indexOf(" desc") != -1)
						dc.addOrder(Order.desc(_order[0]));
					else
						dc.addOrder(Order.asc(_order[0]));
				}
			}
		}
		return getDwHibernateTemplate().findByCriteria(dc,
				(pageNum - 1) * pageSize, pageSize);
	}

	/**
	 * 创建DetachedCriteria对象
	 * 
	 * @return
	 */
	protected DetachedCriteria buildDetachedCriteria() {
		return DetachedCriteria.forClass(entityClass);
	}
}
