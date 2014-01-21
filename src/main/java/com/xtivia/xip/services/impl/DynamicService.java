package com.xtivia.xip.services.impl;

import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.model.User;
import com.xtivia.xip.AppServicesServlet;
import com.xtivia.xip.JsonUtils;
import com.xtivia.xip.entity.ServiceDefinition;
import com.xtivia.xip.service.XipService;
import com.xtivia.xip.services.dao.ServiceDefinitionDao;
import com.xtivia.xip.services.dao.SimpleDao;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DynamicService {
	@SuppressWarnings("unchecked")
	public static void get_executeService(User user, AppServicesServlet servlet, HttpServletRequest request, HttpServletResponse response) throws IOException{
		Map<String,String> map = new HashMap<String, String>();
		Enumeration<String> en = request.getParameterNames();
		while(en.hasMoreElements()){
			String key = en.nextElement();
			map.put(key, request.getParameter(key));
		}
		XipService.executeService(request.getParameter("service"), user, servlet, map, request, response.getWriter());
	}

	public static void get_getServiceDefinitions(User user, AppServicesServlet servlet, HttpServletRequest request, HttpServletResponse response) throws IOException{
		SimpleDao dao = (SimpleDao)servlet.getSpringBean("simpleDao");
		dao.streamQueryResults("select serviceName, serviceType, serviceProvider, serviceAction, serviceText, serviceInputMappings, serviceOutputMappings from service_definition", response);
	}

	public static void post_setServiceDefinition(User user, AppServicesServlet servlet, HttpServletRequest request, HttpServletResponse response) throws JSONException, IOException{
		ServiceDefinitionDao serviceDao = (ServiceDefinitionDao)servlet.getSpringBean("serviceDefinitionDao");
		ServiceDefinition sd = new ServiceDefinition();
		sd.setServiceAction(request.getParameter("serviceAction"));
		sd.setServiceInputMappings(request.getParameter("serviceInputMappings"));
		sd.setServiceName(request.getParameter("serviceName"));
		sd.setServiceOutputMappings(request.getParameter("serviceOutputMappings"));
		sd.setServiceProvider(request.getParameter("serviceProvider"));
		sd.setServiceRoles(request.getParameter("serviceRoles"));
		sd.setServiceText(request.getParameter("serviceText"));
		sd.setServiceType(request.getParameter("serviceType"));
		serviceDao.upsertServiceDefinition(sd);
		response.getWriter().println(JsonUtils.getJsonSuccessMessage(sd.getServiceName()+ " Service Definition Updated"));
	}
	
	public static void post_deleteServiceDefinition(User user, AppServicesServlet servlet, HttpServletRequest request, HttpServletResponse response) throws IOException{
		String serviceName = request.getParameter("serviceName");
		ServiceDefinitionDao serviceDao = (ServiceDefinitionDao)servlet.getSpringBean("serviceDefinitionDao");
		serviceDao.deleteServiceDefinition(serviceName);
		response.getWriter().println(JsonUtils.getJsonSuccessMessage(serviceName+ " Deleted"));
	}
}
