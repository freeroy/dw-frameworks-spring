package org.developerworld.frameworks.spring.jdbc.core.namedparam;

import org.developerworld.frameworks.spring.jdbc.core.support.DwJdbcDaoSupport;

/**
 * 扩展原NamedParameterJdbcDaoSupport类
 * 
 * @author Roy Huang
 * @version 20101129
 * 
 */
public class DwNamedParameterJdbcDaoSupport extends DwJdbcDaoSupport {

	private DwNamedParameterJdbcTemplate dwNamedParameterJdbcTemplate;

	/**
	 * 请参考原NamedParameterJdbcDaoSupport的描述
	 */
	@Override
	protected void initTemplateConfig() {
		this.dwNamedParameterJdbcTemplate = new DwNamedParameterJdbcTemplate(
				getDwJdbcTemplate());
	}

	/**
	 * 请参考原NamedParameterJdbcDaoSupport的描述
	 * 
	 * @return
	 */
	public DwNamedParameterJdbcTemplate getDwNamedParameterJdbcTemplate() {
		return dwNamedParameterJdbcTemplate;
	}
}
