package org.developerworld.frameworks.spring.orm.jpa.web.servlet.tags;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;

/**
 * jpa分页标签
 * 
 * @author Roy Huang
 * 
 */
public class JpaPageTag extends AbstractBodyTagSupport {

	public final static String REQUEST_ATTRIBUTE_PAGE_VAR = "page";

	private int pageNum;
	private int pageSize;
	private int jumpPageSize;
	private String pageVar;
	private String jpql;

	public void setJpql(String jpql) {
		this.jpql = jpql;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setPageVar(String pageVar) {
		this.pageVar = pageVar;
	}

	public void setJumpPageSize(Integer jumpPageSize) {
		this.jumpPageSize = jumpPageSize;
	}

	@Override
	public int doStartTag() throws JspException {
		super.doStartTag();
		// 获取总数目
		long total = getTotal();
		// 修正分页参数
		Integer pageNum = Math.max(1, this.pageNum);
		Integer pageSize = Math.max(1, this.pageSize);
		Integer jumpPageSize = Math.max(1, this.jumpPageSize);
		String pageVar = this.pageVar;
		if (StringUtils.isBlank(pageVar))
			pageVar = REQUEST_ATTRIBUTE_PAGE_VAR;
		int totalPage = (int) Math.ceil((double) total / pageSize);
		// 保存局部变脸
		Map<String, Object> page = new HashMap<String, Object>();
		page.put("pageNum", pageNum);
		page.put("nextPageNum", Math.min(pageNum + 1, totalPage));
		page.put("prevPageNum", Math.max(pageNum - 1, 1));
		page.put("pageSize", pageSize);
		page.put("total", total);
		page.put("totalPage", totalPage);
		page.put("jumpPageSize", jumpPageSize);
		page.put("nextJumpPageNum", Math.min(totalPage, pageNum + jumpPageSize));
		page.put("prevJumpPageNum", Math.max(1, pageNum - jumpPageSize));
		setLocalRequestAttribute(pageVar, page);
		return EVAL_BODY_BUFFERED;
	}

	/**
	 * 获取总条目数
	 * 
	 * @return
	 * @throws JspException
	 */
	private long getTotal() {
		long rst = 0l;
		EntityManager em = buildEntityManager();
		try {
			Query query = em.createQuery(jpql);
			List list = query.setFirstResult(0).setMaxResults(1)
					.getResultList();
			if (list != null && list.size() > 0)
				rst = (Long) list.get(0);
		} finally {
			if (em != null)
				em.close();
		}
		return rst;
	}

	@Override
	public int doAfterBody() throws JspException {
		super.doAfterBody();
		// 输出内容
		printBodyContent();
		return SKIP_BODY;
	}
}
