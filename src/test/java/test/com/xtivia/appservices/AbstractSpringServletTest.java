package test.com.xtivia.appservices;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;

@SuppressWarnings("deprecation")
public abstract class AbstractSpringServletTest extends
		AbstractDependencyInjectionSpringContextTests {

	private ServletConfig servletConfig;

	protected abstract void init() throws Exception;

	protected void onSetUp() throws Exception {
		ServletContext sctx = new MockServletContext();
		servletConfig = new MockServletConfig(sctx, "simple");
		sctx.setAttribute(
				WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
				getWebApplicationContext());
		init();
	}

	protected WebApplicationContext getWebApplicationContext() {
		ApplicationContext ctx = getApplicationContext();
		System.out.println(ctx.getClass().getName());
		GenericWebApplicationContext wac = (GenericWebApplicationContext) BeanUtils
				.instantiateClass(GenericWebApplicationContext.class);

		String[] defNames = ctx.getBeanDefinitionNames();
		for (String defName : defNames) {
			wac.getBeanFactory().registerSingleton(defName,ctx.getBean(defName));
		}
		return wac;
	}

	protected ServletConfig getServletConfig() {
		return servletConfig;
	}

	protected ServletContext getServletContext() {
		return servletConfig.getServletContext();
	}

}
