/**
 * 
 */
package com.hoob.rs.sys.init;

import java.io.File;
import java.net.URI;

import javax.servlet.ServletContextEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.web.Log4jServletContextListener;


/**
 * @author Raul	
 * 2017年8月24日
 */
public class RSLog4jListener extends Log4jServletContextListener{
	
	private static final String LOG4J2_MASTER = "/opt/fonsview/NE/rs/etc/log4j2.xml";
	
	static Logger logger = LogManager.getLogger(RSLog4jListener.class);
	
	
	@Override
	public void contextInitialized(ServletContextEvent event) {		
		super.contextInitialized(event);
		File log4jFile = new File(LOG4J2_MASTER);
		if(log4jFile.exists()){
			LoggerContext context = (LoggerContext)LogManager.getContext(false);
		    context.setConfigLocation(URI.create(LOG4J2_MASTER));
		    context.reconfigure();
		    logger.info("Log4j2------>" + LOG4J2_MASTER);
		}		
	}

//	@Override
//	public void contextInitialized(ServletContextEvent arg0) {		
//		try {
//            ConfigurationSource source;            
//            File log4jFile = new File(LOG4J2_MASTER);
//            if (log4jFile.exists()) {
//                source = new ConfigurationSource(new FileInputStream(log4jFile), log4jFile);
//                Configurator.initialize(source);
//                Configurator.initialize(null, source);
//                System.out.println("Log4j2------>" + LOG4J2_MASTER);
//            }else{
//            	InputStream in = this.getClass().getResourceAsStream(LOG4J2_SLAVE);
//            	source = new ConfigurationSource(in);            	  
//            	Configurator.initialize(null, source);
//            	System.out.println("Log4j2------>classpath:" + LOG4J2_SLAVE);
//            	
//            	logger.warn("Log4j2------>classpath:" + LOG4J2_SLAVE);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//		
//	}
		

}
