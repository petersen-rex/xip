package com.xtivia.xip;

import com.xtivia.xip.Base64.OutputStream;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

@SuppressWarnings("rawtypes")
public class Base64StreamResultSetExtractor implements ResultSetExtractor{
	private HttpServletResponse response;
	public Base64StreamResultSetExtractor(HttpServletResponse response){
		this.response = response;
	}
	
	@Override
	public Object extractData(ResultSet rs) throws SQLException,
			DataAccessException {
			try {
				rs.next();
				String fileName = rs.getString("filename");
				response.setContentType("application/force-download");
				response.setHeader("Content-Disposition", "attachment; filename='"+ fileName+ "'");
				response.setDateHeader("Expires",System.currentTimeMillis(  ) + 24*60*60*1000);

				OutputStream out = new Base64.OutputStream(response.getOutputStream(), Base64.DECODE);
				IOUtils.copy(rs.getAsciiStream("filedata"), out);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}
	

}
