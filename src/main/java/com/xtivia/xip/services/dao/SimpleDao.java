package com.xtivia.xip.services.dao;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public interface SimpleDao {
	public void streamQueryResults(String query, HttpServletResponse response) throws IOException;
	public void streamQueryResults(String query, PrintWriter pw) throws IOException;
	public void base64StreamQueryResult(String query, HttpServletResponse response) throws IOException;

}
