package org.developerworld.frameworks.spring.orm.jpa.web.servlet.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;

/**
 * jpa查询标签
 * 
 * @author Roy Huang
 * 
 */
public class JpaQueryTag extends AbstractBodyTagSupport {

	public final static String REQUEST_ATTRIBUTE_STATUS_VAR = "REQUEST_ATTRIBUTE_STATUS_VAR";
	public final static String REQUEST_ATTRIBUTE_DATA_VAR = "REQUEST_ATTRIBUTE_DATA_VAR";
	public final static String REQUEST_ATTRIBUTE_DATAS_VAR = "REQUEST_ATTRIBUTE_DATAS_VAR";
	public final static String REQUEST_ATTRIBUTE_DATA = "data";
	public final static String REQUEST_ATTRIBUTE_DATAS = "datas";
	public final static String REQUEST_ATTRIBUTE_STATUS = "status";
	public final static String REQUEST_ATTRIBUTE_STATUS_INDEX = "index";
	public final static String REQUEST_ATTRIBUTE_STATUS_COUNT = "count";
	public final static String REQUEST_ATTRIBUTE_STATUS_FIRST = "first";
	public final static String REQUEST_ATTRIBUTE_STATUS_LAST = "last";

	private String datasVar;
	private String dataVar;
	private String statusVar;
	private int pageNum;
	private int pageSize;
	private String jpql;

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setStatusVar(String statusVar) {
		this.statusVar = statusVar;
	}

	public void setJpql(String jpql) {
		this.jpql = jpql;
	}

	protected List getDatas() {
		List rst = null;
		EntityManager em = buildEntityManager();
		try {
			Query query = em.createQuery(jpql);
			int pageNum = Math.max(1, this.pageNum);
			int pageSize = Math.max(1, this.pageSize);
			rst = query.setFirstResult((pageNum - 1) * pageSize)
					.setMaxResults(pageSize).getResultList();
		} finally {
			if (em != null)
				em.close();
		}
		return rst;
	}

	@Override
	public int doStartTag() throws JspException {
		int rst = SKIP_BODY;
		super.doStartTag();
		List datas = getDatas();
		if (datas.size() <= 0)
			rst = SKIP_BODY;
		else {
			String dataVar = this.dataVar;
			String datasVar = this.datasVar;
			String statusVar = this.statusVar;
			if (StringUtils.isBlank(dataVar))
				dataVar = REQUEST_ATTRIBUTE_DATA;
			if (StringUtils.isBlank(datasVar))
				datasVar = REQUEST_ATTRIBUTE_DATAS;
			if (StringUtils.isBlank(statusVar))
				statusVar = REQUEST_ATTRIBUTE_STATUS;
			// 设置状态值
			Map<String, Object> status = new HashMap<String, Object>();
			status.put(REQUEST_ATTRIBUTE_STATUS_INDEX, 0);
			status.put(REQUEST_ATTRIBUTE_STATUS_COUNT, datas.size());
			status.put(REQUEST_ATTRIBUTE_STATUS_FIRST, true);
			status.put(REQUEST_ATTRIBUTE_STATUS_LAST, datas.size() == 1);
			// 写进page,供EL访问
			setLocalRequestAttribute(statusVar, status);
			setLocalRequestAttribute(dataVar, datas.get(0));
			setLocalRequestAttribute(datasVar, datas);
			setLocalRequestAttribute(REQUEST_ATTRIBUTE_STATUS_VAR, statusVar);
			setLocalRequestAttribute(REQUEST_ATTRIBUTE_DATA_VAR, dataVar);
			setLocalRequestAttribute(REQUEST_ATTRIBUTE_DATAS_VAR, datasVar);
			rst = EVAL_BODY_BUFFERED;
		}
		return rst;
	}

	@Override
	public int doAfterBody() throws JspException {
		super.doAfterBody();
		printBodyContent();
		// 判断是否还存在数据
		String statusVar = (String) getLocalRequestAttribute(REQUEST_ATTRIBUTE_STATUS_VAR);
		String dataVar = (String) getLocalRequestAttribute(REQUEST_ATTRIBUTE_DATA_VAR);
		String datasVar = (String) getLocalRequestAttribute(REQUEST_ATTRIBUTE_DATAS_VAR);
		List datas = (List) getLocalRequestAttribute(datasVar);
		Map<String, Object> status = (Map<String, Object>) getLocalRequestAttribute(statusVar);
		Integer index = (Integer) status.get(REQUEST_ATTRIBUTE_STATUS_INDEX) + 1;
		if (index >= datas.size())
			return SKIP_BODY;
		else {
			status.put(REQUEST_ATTRIBUTE_STATUS_INDEX, index);
			status.put(REQUEST_ATTRIBUTE_STATUS_FIRST, false);
			status.put(REQUEST_ATTRIBUTE_STATUS_LAST,
					datas.size() == (Integer) status
							.get(REQUEST_ATTRIBUTE_STATUS_INDEX) + 1);
			// 写进page,供EL访问
			setLocalRequestAttribute(dataVar, datas.get(index));
			return EVAL_BODY_AGAIN;
		}
	}

}
