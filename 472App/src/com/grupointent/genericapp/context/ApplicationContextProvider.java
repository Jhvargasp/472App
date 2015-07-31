/**
 * 
 */
package com.grupointent.genericapp.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author jvargas
 * 
 */
public class ApplicationContextProvider implements ApplicationContextAware {

	public static ApplicationContext appContext;

	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		// Wiring the ApplicationContext into a static method
		appContext = ctx;
		System.out.println("Context hecho!!!!" + appContext);

	}
}
