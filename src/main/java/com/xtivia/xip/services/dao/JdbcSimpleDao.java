package com.xtivia.xip.services.dao;

import com.xtivia.xip.AjaxStreamResultSetExtractor;
import com.xtivia.xip.Base64StreamResultSetExtractor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@SuppressWarnings("unchecked")
@Repository("simpleDao")
public class JdbcSimpleDao implements SimpleDao{
	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	@Override
	public void streamQueryResults(String query, HttpServletResponse response) throws IOException {
		PrintWriter pw = response.getWriter();
		streamQueryResults(query, pw);
	}

	@Override
	public void streamQueryResults(String query, PrintWriter pw) throws IOException {
		pw.print("{");
		this.jdbcTemplate.query(query, new AjaxStreamResultSetExtractor(pw));
		pw.print("}");
	}

	/**
	 * Query must return only 1 row and 2 fields: "filename", "filedata"
	 * @param query
	 * @param response
	 * @throws IOException
	 */
	@Override
	public void base64StreamQueryResult(String query, HttpServletResponse response) throws IOException {
		this.jdbcTemplate.query(query, new Base64StreamResultSetExtractor(response));
	}

}
