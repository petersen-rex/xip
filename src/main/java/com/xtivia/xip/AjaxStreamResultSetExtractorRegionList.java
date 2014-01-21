package com.xtivia.xip;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;


@SuppressWarnings("rawtypes")
public class AjaxStreamResultSetExtractorRegionList implements ResultSetExtractor{
	private PrintWriter pw;
	public AjaxStreamResultSetExtractorRegionList(PrintWriter pw){
		this.pw = pw;
	}
	
	@Override
	public Object extractData(ResultSet rs) throws SQLException,
			DataAccessException {
			try {
				Set<String> regionSet = new HashSet<String>();
				while(rs.next()) {
					regionSet.add(rs.getString("region"));
				}				
				List<String> regionList = new ArrayList<String>(regionSet);
								
				boolean bFirst3=true;
				for(String region : regionList) {
					rs.first();
					if(null != region && !region.isEmpty()) {
						if(bFirst3){
							bFirst3=false;
						} else {
							pw.println(",");
						}
						pw.println("\"" + region + "\":[");
						boolean bFirst=true;
						while(rs.next()) {	
							if(region.equals(rs.getString("region"))) {
								int colCount = rs.getMetaData().getColumnCount();
									if(bFirst){
										bFirst=false;
									} else {
										pw.print(",");
									}
									pw.println("{");
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
							}
						}
						pw.print("]");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}
		
}
