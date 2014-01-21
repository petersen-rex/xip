package com.xtivia.xip;

import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

public class JsonUtils {

	public static String getJsonSuccessMessage(String message){
		JSONObject json = JSONFactoryUtil.createJSONObject();
		json.put("success", true);
		json.put("message", message);
		return json.toString();
	}
	
	public static String getJsonFailureMessage(String message){
		JSONObject json = JSONFactoryUtil.createJSONObject();
		json.put("success", false);
		json.put("message", message);
		return json.toString();
	}
	
	public static void addSuccessMessage(JSONObject json){
		json.put("success", true);
		json.put("message", "success");
	}
	
	public static void encodeJsonStream(PrintWriter pw, InputStream json){
		byte[] b=new byte[1];
		try {
			if(null==json){
				return;
			}
			while(json.read(b,0,1)!=-1){
				if((b[0]=='\\') || (b[0]=='"')){
					pw.print("\\");
					pw.write(b[0]);
				} else if(b[0]==10){
					pw.print("\\n");
				} else if(b[0]=='\r'){
					
				} else if(b[0]=='\t'){
					
				} else {
					pw.write(b[0]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String sanitizeInput(String input){
		return null==input ? null : input.replaceAll("[\\\"\\']", "");
	}
	
	public static JSONObject getPostData(HttpServletRequest request) throws JSONException, IOException{
		String scrudRequest = IOUtils.toString(request.getInputStream());
		JSONObject json = JSONFactoryUtil.createJSONObject(scrudRequest);
		return json;
	}
}
