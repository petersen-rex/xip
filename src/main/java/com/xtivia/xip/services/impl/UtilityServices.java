package com.xtivia.xip.services.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.model.User;
import com.xtivia.xip.AppServicesServlet;

public class UtilityServices {

	public static void get_getSessionAttribute(User user, AppServicesServlet servlet, HttpServletRequest request, HttpServletResponse response) throws IOException{
		String attr = (String)request.getParameter("attr");
		response.getWriter().printf("Attribute %s, is : %s\n", attr, request.getSession().getAttribute(attr));
	}

}
