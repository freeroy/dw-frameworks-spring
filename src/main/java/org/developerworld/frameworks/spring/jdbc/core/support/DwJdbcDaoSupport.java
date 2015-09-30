package org.developerworld.frameworks.spring.jdbc.core.support;

import javax.sql.DataSource;

import org.developerworld.frameworks.spring.jdbc.core.DwJdbcTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * 针对spring JdbcDaoSupport的扩张类
 * 
 * @author Roy Huang
 * @version 20110602
 * 
 *          注意：这里重构了父类的createJdbcTemplate，使之返回的是继承自JdbcTemplate的DwJdbcTemplate
 */
public class DwJdbcDaoSupport extends JdbcDaoSupport {

	public DwJdbcTemplate getDwJdbcTemplate() {
		return (DwJdbcTemplate) getJdbcTemplate();
	}

	/**
	 * 覆盖父类方法，返回新的子类
	 */
	@Override
	protected JdbcTemplate createJdbcTemplate(DataSource dataSource) {
		return new DwJdbcTemplate(dataSource);
	}
}
