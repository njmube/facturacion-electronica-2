package FEV1.dif.afip.gov.ar.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
 
public class ParametrosUtil {

	private final static String PROPERTIES_PATH = "/wsaa_client.properties";
	public static final String IDLE_TIME = "idle.time.out";
	private static Properties properties;
	
	static{
		properties = new Properties();
		FileInputStream fInput = null;
		try {
			fInput = new FileInputStream(new File(System
					.getProperty("user.dir")
					+ PROPERTIES_PATH));
			properties.load(fInput);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != fInput) {
				try {
					fInput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static Properties getProperties() {
		return properties;
	}

	public static String getProperty(final String key) {
		String value = null;
		final Properties properties = getProperties();
		if (null != properties) {
			if (null != key) {
				value = (String) properties.get(key);
			}
		}
		return value;
	}

}
