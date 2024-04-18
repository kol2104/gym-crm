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
		tomcat.setBaseDir("src/main");
		tomcat.getConnector();

		Context servletContext = tomcat.addContext("", "");

		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setServletContext(servletContext.getServletContext());
		context.register(SpringConfiguration.class);
		context.refresh();

		Tomcat.addServlet(servletContext, "dispatcherServlet", new DispatcherServlet(context));
		servletContext.addServletMappingDecoded("/*", "dispatcherServlet");

		tomcat.start();
		tomcat.getServer().await();

		context.close();
	}

}
