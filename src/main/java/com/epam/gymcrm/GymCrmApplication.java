package com.epam.gymcrm;

import com.epam.gymcrm.config.SpringConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

@Slf4j
public class GymCrmApplication {

	public static void main(String[] args) throws LifecycleException {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(8080);

		Context servletContext = tomcat.addContext("/", null);

		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setServletContext(servletContext.getServletContext());
		context.register(SpringConfiguration.class);
		context.refresh();


		tomcat.addServlet("/", "dispatcherServlet", new DispatcherServlet(context));
		servletContext.addServletMappingDecoded("/*", "dispatcherServlet");


		// Start Tomcat
		tomcat.start();
		tomcat.getServer().await(); // Wait for Tomcat to finish

		// Close the application context
		context.close();
	}

}
