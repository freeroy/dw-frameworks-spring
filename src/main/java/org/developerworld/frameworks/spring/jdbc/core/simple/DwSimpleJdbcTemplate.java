package org.developerworld.frameworks.spring.jdbc.core.simple;

import javax.sql.DataSource;

import org.developerworld.frameworks.spring.jdbc.core.namedparam.DwNamedParameterJdbcTemplate;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

/**
 * 
 * 扩展原SimpleJdbcTemplate类
 * 
 * @author Roy Huang
 * @version 20110602
 * 
 *          注意，这里所引用的NamedParameterJdbcOperations，
 *          为扩张后的DwNamedParameterJdbcTemplate
 * 
 */
@Deprecated
public class DwSimpleJdbcTemplate extends SimpleJdbcTemplate {

	/**
	 * 请参考原SimpleJdbcTemplate的描述
	 * 
	 * @param dataSource
	 */
	public DwSimpleJdbcTemplate(DataSource dataSource) {
		this(new DwNamedParameterJdbcTemplate(dataSource));
	}

	/**
	 * 请参考原SimpleJdbcTemplate的描述
	 * 
	 * @param classicJdbcTemplate
	 */
	public DwSimpleJdbcTemplate(JdbcOperations classicJdbcTemplate) {
		this(new DwNamedParameterJdbcTemplate(classicJdbcTemplate));
	}

	/**
	 * 请参考原SimpleJdbcTemplate的描述
	 * 
	 * @param namedParameterJdbcTemplate
	 */
	public DwSimpleJdbcTemplate(
			NamedParameterJdbcOperations namedParameterJdbcTemplate) {
		super(namedParameterJdbcTemplate);
	}
}
