package test.com.xtivia.appservices;

import com.xtivia.xip.AppServicesServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class MockAppServicesServlet extends AppServicesServlet {
	private static final long serialVersionUID = 1L;

	private WebApplicationContext webApplicationContext=null;
	
	private Log log = LogFactory.getLog(MockAppServicesServlet.class);
	

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			webApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		} catch (Exception e) {
			log.error(e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		doGet(request, response);
	}

	/**
	 * Return a reference to the Spring Bean with the given name as defined in its annotation
	 * @param beanName
	 * @return
	 */
	public Object getSpringBean(String beanName){
		return webApplicationContext.getBean(beanName);
	}
}