package com.xtivia.xip.service;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.ThemeDisplay;
import com.xtivia.xip.AppServicesServlet;
import com.xtivia.xip.entity.ServiceDefinition;
import com.xtivia.xip.services.dao.ServiceDefinitionDao;
import com.xtivia.xip.services.dao.SimpleDao;
import com.xtivia.xip.utils.JsonUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class XipService {
	@SuppressWarnings("restriction")
	public static String exec(String serviceName, User user, AppServicesServlet servlet, sun.org.mozilla.javascript.internal.NativeObject parms) throws IOException{
		Map<String,String> inputParameters = JsonUtils.getMap(parms);
		StringWriter sw = new StringWriter();
		executeService(serviceName, user, servlet, inputParameters, null, new PrintWriter(sw));
		String content = sw.toString();	
		return content;
	}
	
	public static void executeService(String serviceName, User user, AppServicesServlet servlet, Map<String,String> inputParameters, HttpServletRequest request, PrintWriter pw) throws IOException{
		ServiceDefinitionDao serviceDao = (ServiceDefinitionDao)servlet.getSpringBean("serviceDefinitionDao");
		ServiceDefinition sd = serviceDao.getServiceDefinition(serviceName);
		if("jdbc".equalsIgnoreCase(sd.getServiceType())){
			handleJdbcServiceLocal(servlet, user, sd, inputParameters, pw);
		} else if("wsdl".equalsIgnoreCase(sd.getServiceType())){
			handleWsdlServiceLocal(servlet, user, sd, inputParameters, pw);
		} else if("scrp".equalsIgnoreCase(sd.getServiceType())){
			handleScriptServiceLocal(servlet, user, sd, inputParameters, request, pw);
		}
	}
	

	public static void handleJdbcServiceLocal(AppServicesServlet servlet, User user, ServiceDefinition sd, Map<String,String> inputParameters, PrintWriter pw) throws IOException{
		SimpleDao dao = (SimpleDao)servlet.getSpringBean("simpleDao");
		List<String> toReplace = new ArrayList<String>();
		List<String> replaceWith = new ArrayList<String>();
		Iterator<Entry<String,String>> it = inputParameters.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String,String> pairs = it.next();
			toReplace.add("$" + pairs.getKey());
			replaceWith.add(pairs.getValue());
		}
		String query = StringUtils.replaceEach(sd.getServiceText(), (String [])toReplace.toArray(new String[0]), (String []) replaceWith.toArray(new String[0]));
		dao.streamQueryResults(query, pw);
	}

	public static void handleWsdlServiceLocal(AppServicesServlet servlet, User user, ServiceDefinition sd, Map<String,String> inputParameters, PrintWriter pw){
		
	}
	
	public static void handleScriptServiceLocal(AppServicesServlet servlet, User user, ServiceDefinition sd, Map<String, String> inputParameters, HttpServletRequest request, PrintWriter pw){
		ScriptEngineManager factory;
		ScriptEngine engine;
		ScriptContext scriptContext;
		
		engine = (ScriptEngine)request.getSession().getAttribute("scriptEngine");
		scriptContext = (ScriptContext)request.getSession().getAttribute("scriptContext");
		if(engine == null){
			factory = new ScriptEngineManager();
			engine  = factory.getEngineByName("JavaScript");
			scriptContext = new SimpleScriptContext();
			engine.setContext(scriptContext);
			request.getSession().setAttribute("scriptEngine", engine);
			request.getSession().setAttribute("scriptContext", scriptContext);
		}
		try {
			engine.put("servlet", servlet);
			engine.put("user", user);
			engine.put("serviceDefinition", sd);
			engine.put("inputParameters", inputParameters);
			engine.put("out", pw);
			engine.put("themeDisplay", (ThemeDisplay)request.getAttribute(WebKeys.THEME_DISPLAY));
			engine.put("request", request);
			engine.eval(sd.getServiceText(), scriptContext);
		} catch (ScriptException e) {
			pw.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	

}
