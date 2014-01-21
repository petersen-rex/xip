package com.xtivia.xip;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;


@SuppressWarnings("rawtypes")
public class AjaxStreamResultSetExtractor implements ResultSetExtractor{
	private PrintWriter pw;
	public AjaxStreamResultSetExtractor(PrintWriter pw){
		this.pw = pw;
	}
	@Override
	public Object extractData(ResultSet rs) throws SQLException,
			DataAccessException {
			try {
				pw.println("\"data\":[");
				int i=0;
				int colCount = rs.getMetaData().getColumnCount();
				boolean bFirst=true;
				while(rs.next()){
					if(bFirst){
						bFirst=false;
					} else {
						pw.println(",");
					}
					pw.print("{");
					boolean bFirst2=true;
					for(int c=1;c<=colCount;c++){
						if(bFirst2){
							bFirst2=false;
						} else {
							pw.print(",");
						}
						pw.print("\"");
						pw.print(rs.getMetaData().getColumnLabel(c));
						pw.print("\":\"");
						
						JsonUtils.encodeJsonStream(pw,rs.getAsciiStream(c));
						pw.print("\"");
					}
					pw.print("}");
					i++;
				}
				pw.println("], \"rowCount\":\"" + i + "\"");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}
	
}
