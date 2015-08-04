package ar.com.wuik.sistema.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import ar.com.wuik.swing.utils.WUtils;

public class BackupUtil {

	public static String doBackup(String path) throws Exception {
		String user = DBUtil.getUser();
		String password = DBUtil.getPassword();
		String pathMySQL = ParametrosUtil
				.getProperty(ParametrosUtil.KEY_PATH_MYSQL);
		String targetBackup = path + "\\Backup\\";

		File file = new File(targetBackup);
		file.mkdirs();

		String fecha = WUtils.getStringFromDate(new Date(), "dd-MM-yyyy");
		String backupFileName = "backup" + fecha + ".sql";
		targetBackup += backupFileName;

		StringBuilder command = new StringBuilder();

		command.append("mysqldump --host=localhost");
		command.append(" ");
		command.append("--user=");
		command.append(user);
		command.append(" ");
		command.append("--password=");
		command.append(password);
		command.append(" ");
		command.append("--add-drop-table");
		command.append(" ");
		command.append("sistema_facturacion>");
		command.append(" ");
		command.append("\"");
		command.append(targetBackup);
		command.append("\"");

		String[] commandsToExec = { "cmd", "/C", command.toString() };

		try {
			Runtime.getRuntime()
					.exec(commandsToExec, null, new File(pathMySQL));
			copyFileUsingStream(new File(targetBackup),
					getLocalCopyPath(backupFileName));
		} catch (IOException e) {
			throw new Exception("Error al realizar copia", e);
		}

		return targetBackup;
	}

	private static File getLocalCopyPath(String backupFileName) {
		File localCopyPath = new File(System.getProperty("user.dir")
				+ "/Backup/");
		if (!localCopyPath.exists()) {
			localCopyPath.mkdir();
		}
		return new File(localCopyPath.getAbsoluteFile() + "/" + backupFileName);
	}

	private static void copyFileUsingStream(File source, File dest)
			throws IOException {
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(source);
			os = new FileOutputStream(dest);
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			if (null != is) {
				is.close();
			}
			if (null != os) {
				os.close();
			}
		}
	}
}
