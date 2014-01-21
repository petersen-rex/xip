package com.xtivia.xip.services.impl;

import com.liferay.portal.model.User;
import com.xtivia.xip.AppServicesServlet;
import com.xtivia.xip.services.dao.SimpleDao;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestService {
	public static void get_sampleService(User user, AppServicesServlet servlet, HttpServletRequest request, HttpServletResponse response) throws IOException{
		SimpleDao dao = (SimpleDao)servlet.getSpringBean("simpleDao");
		dao.streamQueryResults("select siteId, latitude,longitude from site limit 0, 10", response);
	}
}
