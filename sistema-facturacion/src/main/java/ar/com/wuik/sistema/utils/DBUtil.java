package ar.com.wuik.sistema.utils;

import ar.com.wuik.swing.utils.WEncrypterUtil;

public class DBUtil {

	public static String getPassword() {
		final String password = ParametrosUtil.getProperty("db.password");
		return WEncrypterUtil.decrypt(password);
	}

	public static String getUser() {
		final String user = ParametrosUtil.getProperty("db.user");
		return WEncrypterUtil.decrypt(user);
	}

	public static String getConnectionString() {
		final String connectionString = ParametrosUtil
				.getProperty("db.connection.string");
		return connectionString;
	}

}
