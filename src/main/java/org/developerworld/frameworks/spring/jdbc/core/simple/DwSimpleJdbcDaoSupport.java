package org.developerworld.frameworks.spring.jdbc.core.simple;

import org.developerworld.frameworks.spring.jdbc.core.support.DwJdbcDaoSupport;
/**
 * 扩展原SimpleJdbcDaoSupport类
 * 
 * @author Roy Huang
 * @version 20101129
 * 
 * @deprecated
 * The DwJdbcTemplate and DwNamedParameterJdbcTemplate
 * now provide all the functionality of the DwSimpleJdbcTemplate.
 */
public class DwSimpleJdbcDaoSupport extends DwJdbcDaoSupport {

	private DwSimpleJdbcTemplate dwSimpleJdbcTemplate;

	/**
	 * 请参考原SimpleJdbcDaoSupport的描述
	 */
	@Override
	protected void initTemplateConfig() {
		this.dwSimpleJdbcTemplate = new DwSimpleJdbcTemplate(
				getDwJdbcTemplate());
	}

	/**
	 * 请参考原SimpleJdbcDaoSupport的描述
	 * 
	 * @return
	 */
	public DwSimpleJdbcTemplate getDwSimpleJdbcTemplate() {
		return this.dwSimpleJdbcTemplate;
	}
}
