package com.xtivia.xip;

import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

@SuppressWarnings("rawtypes")
public class AppServicesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Map<String, Method>stubMethods=new HashMap<String,Method>();
	private Map<String, Method>implMethods=new HashMap<String,Method>();
	private WebApplicationContext webApplicationContext=null;
	
	protected Log log = LogFactory.getLog(AppServicesServlet.class);
	

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			List<Class>classList = getClasses(config.getInitParameter("stub-services"));
			stubMethods.clear();
			addMethods(stubMethods, classList);
			
			classList = getClasses(config.getInitParameter("impl-services"));
			implMethods.clear();
			addMethods(implMethods, classList);
			

		} catch (Exception e) {
			log.error(e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		handleRequest("get", request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		handleRequest("post", request, response);
	}

	private void handleRequest(String requestType, HttpServletRequest request, HttpServletResponse response){
		String cmd = request.getParameter("cmd");
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		if(null==cmd){
			try {
				response.getWriter().print(JsonUtils.getJsonFailureMessage("NO_CMD"));
			} catch (IOException e) {
				log.error(e);
			}
			return;
		}

		try{
			User user = PortalUtil.getUser(request);
			if(allowAccess(user,cmd,request,response)) {
				dispatchCommand(requestType, user, cmd, request, response);
			} else {
				response.getWriter().println(JsonUtils.getJsonFailureMessage("ACCESS_DENIED"));
			}
		} catch(Exception e){
			try {
				response.getWriter().print(JsonUtils.getJsonFailureMessage(e.getCause().toString()));
			} catch (IOException e1) {
				log.error(e1);
			}
		}
		
	}
	/**
	 * returns true if the requested command should be executed or false if access is denied
	 * @param user
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	protected boolean allowAccess(User user, String cmd, HttpServletRequest request, HttpServletResponse response) throws IOException{
		if(null==user){
			return false;
		}
		return true;
	}
	
	private void dispatchCommand(String requestType, User user, String command, HttpServletRequest request, HttpServletResponse response) throws IOException{
		String methodName = requestType+"_"+command;
		try {
			Method m = implMethods.get(methodName);
			
			if(null==m){
				m = stubMethods.get(methodName);
			}
			
			if(null==m){
				response.getWriter().print(JsonUtils.getJsonFailureMessage("METHOD_DOES_NOT_EXIST"));
			} else {
				m.invoke(this, user, this, request, response);
			}
		} catch(InvocationTargetException ite){
			log.error(ite.getTargetException());
			showException(ite.getTargetException(), user, request, command);
			response.getWriter().print("Error executing command " + command + ".  This is being routed to a technician for resolution.");
		} catch (Exception e){
			log.error(e);
			response.getWriter().print(JsonUtils.getJsonFailureMessage("Error executing command: " + command + ", " + e.getCause().toString()));
		} 
	}
	
	private void addMethods(Map<String, Method> methods, List<Class>classes){
		for(Class clazz : classes){
			try {
				for(Method method : clazz.getDeclaredMethods()){
					if(method.getName().startsWith("get_") || method.getName().startsWith("post_")){
						methods.put(method.getName(), method);
					}
				}
			} catch(Exception e){
				log.error(e.getMessage());
			}
		}
	}

	private static List<Class> getClasses(String packageName) throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes;
	}
	
	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}

		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}
	
	protected void showException(Throwable e, User user, HttpServletRequest request, String command){
		
	}
	/**
	 * Return a reference to the Spring Bean with the given name as defined in its annotation
	 * @param beanName
	 * @return
	 */
	public Object getSpringBean(String beanName){
		return getWebApplicationContext().getBean(beanName);
	}
	public WebApplicationContext getWebApplicationContext(){
		if(webApplicationContext==null){
			ServletContext context = this.getServletContext();
			webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
		}
		return webApplicationContext;
	}
}