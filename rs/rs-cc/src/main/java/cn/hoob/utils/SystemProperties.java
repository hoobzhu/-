package cn.hoob.utils;

import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SystemProperties {

	private static final Logger LOGGER = LogManager.getLogger(SystemProperties.class);	
	public Properties getSystemProperties() throws Exception {
		Properties properties = new Properties();
		InputStream is = null;
		try {
			//is = this.getClass().getClassLoader().getResourceAsStream("config.properties");
			is = this.getClass().getClassLoader().getResourceAsStream("config.properties");
			properties.load(is);
			return properties;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new Exception();
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

}