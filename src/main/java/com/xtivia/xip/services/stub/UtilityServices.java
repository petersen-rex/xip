package com.xtivia.xip.services.stub;

import com.liferay.portal.model.User;
import com.xtivia.xip.AppServicesServlet;
import com.xtivia.xip.JsonUtils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UtilityServices {
	public static void get_ping(User user, AppServicesServlet servlet, HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.getWriter().println(JsonUtils.getJsonSuccessMessage("Ping Success! " + user.getFullName()));
	}
}
