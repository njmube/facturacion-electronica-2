package FEV1.dif.afip.gov.ar.utils;

import java.io.IOException;
import java.util.Properties;

public class ErrorsUtil {

	private final static String PROPERTIES_PATH = "/errors.properties";
	private static Properties properties = null;

	static {
		properties = new Properties();
		try {
			properties.load(ErrorsUtil.class
					.getResourceAsStream(PROPERTIES_PATH));
		} catch (IOException e) {
			e.printStackTrace();
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

	public static String getProperty(final String key, final String defValue) {
		String value = null;
		final Properties properties = getProperties();
		if (null != properties) {
			if (null != key) {
				value = (String) properties.get(key);
			}
		}
		return (null == value || value.isEmpty()) ? defValue : value;
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println(ErrorsUtil.getProperty("10016"));
	}

}
