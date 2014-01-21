package com.xtivia.xip.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.org.mozilla.javascript.internal.Scriptable;

@SuppressWarnings("restriction")
public class JsonUtils {
	public static String toJson (Object obj){
		StringBuilder sb = new StringBuilder();
		addJson(sb, obj);
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	private static void addJson(StringBuilder sb, Object obj){
        boolean bFirst = true;
		if (obj instanceof sun.org.mozilla.javascript.internal.NativeObject){
			sb.append("{");
			Object[] propIds = sun.org.mozilla.javascript.internal.NativeObject.getPropertyIds((Scriptable) obj);
			for(Object propId: propIds) {
	            String key = propId.toString();
	            if (!bFirst) sb.append(",");
	            bFirst = false;
	            sb.append("\"").append(key).append("\"").append(":");
	            Object value = sun.org.mozilla.javascript.internal.NativeObject.getProperty((Scriptable) obj, key);
	            addJson(sb, value);
			}
			sb.append("}");
		} else if (obj instanceof sun.org.mozilla.javascript.internal.NativeArray){
			sb.append("[");
			Object[] propIds = sun.org.mozilla.javascript.internal.NativeObject.getPropertyIds((Scriptable) obj);
			for(Object propId: propIds) {
	            String key = propId.toString();
	            if (!bFirst) sb.append(",");
	            bFirst = false;
	            Object value = ((sun.org.mozilla.javascript.internal.NativeArray)obj).get(Integer.parseInt(key), null);
	            addJson(sb, value);
			}
			sb.append("]");
		} else if (obj instanceof sun.org.mozilla.javascript.internal.NativeJavaObject){
			Object uobj = ((sun.org.mozilla.javascript.internal.NativeJavaObject)obj).unwrap();
			addJson(sb,uobj);
		} else if (obj instanceof List){
			List<Object> listObjs = (List<Object>)obj;
			sb.append("[");
			for(Object listObj : listObjs) {
	            if (!bFirst) sb.append(",");
	            bFirst = false;
	            addJson(sb, listObj);
			}
			sb.append("]");
		} else {
			sb.append("\"").append(obj.toString()).append("\"");
		}

	}
	/*
	private static void addJson(StringBuilder sb, sun.org.mozilla.javascript.internal.NativeObject obj){
		sb.append("{");
		Object[] propIds = sun.org.mozilla.javascript.internal.NativeObject.getPropertyIds(obj);
        boolean bFirst = true;
		for(Object propId: propIds) {
            String key = propId.toString();
            if (!bFirst) sb.append(",");
            bFirst = false;

            sb.append("\"").append(key).append("\"").append(":");
            Object value = sun.org.mozilla.javascript.internal.NativeObject.getProperty(obj, key);
    		if (value instanceof sun.org.mozilla.javascript.internal.NativeObject){
    			addJson(sb, (sun.org.mozilla.javascript.internal.NativeObject) value);
    		} else if (value instanceof sun.org.mozilla.javascript.internal.NativeArray){
    			addJson(sb, (sun.org.mozilla.javascript.internal.NativeArray) value);
    		} else if (value instanceof sun.org.mozilla.javascript.internal.NativeJavaObject){
    			sb.append("\"").append(((sun.org.mozilla.javascript.internal.NativeJavaObject)value).unwrap().toString()).append("\"");
    		} else {
    			sb.append("\"").append(value.toString()).append("\"");
    		}

        }
		sb.append("}");
	}
	
	private static void addJson(StringBuilder sb, sun.org.mozilla.javascript.internal.NativeArray obj){
		sb.append("[");
		Object[] propIds = sun.org.mozilla.javascript.internal.NativeObject.getPropertyIds(obj);
        boolean bFirst = true;
		for(Object propId: propIds) {
            String key = propId.toString();
            if (!bFirst) sb.append(",");
            bFirst = false;

            Object value = obj.get(Integer.parseInt(key), null);

    		if (value instanceof sun.org.mozilla.javascript.internal.NativeObject){
    			addJson(sb, (sun.org.mozilla.javascript.internal.NativeObject) value);
    		} else if (value instanceof sun.org.mozilla.javascript.internal.NativeArray){
    			addJson(sb, (sun.org.mozilla.javascript.internal.NativeArray) value);
    		} else {
    			sb.append("\"").append(value.toString()).append("\"");
    		}

        }
		sb.append("]");
	}
	*/
	
	public static Map<String,String> getMap(sun.org.mozilla.javascript.internal.NativeObject obj) {
	    HashMap<String, String> map = new HashMap<String, String>();

	    if(obj != null) {
	        Object[] propIds = sun.org.mozilla.javascript.internal.NativeObject.getPropertyIds(obj);
	        for(Object propId: propIds) {
	            String key = propId.toString();
	            Object childObject = sun.org.mozilla.javascript.internal.NativeObject.getProperty(obj, key);
	            if (childObject instanceof sun.org.mozilla.javascript.internal.NativeJavaObject){
					childObject = ((sun.org.mozilla.javascript.internal.NativeJavaObject)childObject).unwrap();
				}
				map.put(key, childObject.toString());
	        }
	    }
	    return map;
	}

}
