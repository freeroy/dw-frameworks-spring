package org.developerworld.frameworks.spring.jdbc.core;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * 针对spring JdbcTemplate的扩展类
 * 
 * @author Roy Huang
 * @version 20101129
 * 
 */
public class DwJdbcTemplate extends JdbcTemplate {

	/**
	 * 请参考原JdbcTemplate的描述
	 */
	public DwJdbcTemplate() {
		super();
	}

	/**
	 * 请参考原JdbcTemplate的描述
	 */
	public DwJdbcTemplate(DataSource dataSource) {
		super(dataSource);
	}

	/**
	 * 请参考原JdbcTemplate的描述
	 */
	public DwJdbcTemplate(DataSource dataSource, boolean lazyInit) {
		super(dataSource, lazyInit);
	}
}
