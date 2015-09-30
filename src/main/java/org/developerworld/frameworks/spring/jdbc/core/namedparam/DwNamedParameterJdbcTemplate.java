package org.developerworld.frameworks.spring.jdbc.core.namedparam;

import javax.sql.DataSource;

import org.developerworld.frameworks.spring.jdbc.core.DwJdbcTemplate;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * 扩展原NamedParameterJdbcTemplate类
 * 
 * @author Roy Huang
 * @version 20110602
 * 
 *          注意：这里引用的JdbcOperations，为经过扩展后的DwJdbcTemplate
 * 
 */
public class DwNamedParameterJdbcTemplate extends NamedParameterJdbcTemplate {

	/**
	 * 请参考原NamedParameterJdbcTemplate的描述
	 * 
	 * @param dataSource
	 */
	public DwNamedParameterJdbcTemplate(DataSource dataSource) {
		this(new DwJdbcTemplate(dataSource));
	}

	/**
	 * 请参考原NamedParameterJdbcTemplate的描述
	 * 
	 * @param classicJdbcTemplate
	 */
	public DwNamedParameterJdbcTemplate(JdbcOperations classicJdbcTemplate) {
		super(classicJdbcTemplate);
	}

}
