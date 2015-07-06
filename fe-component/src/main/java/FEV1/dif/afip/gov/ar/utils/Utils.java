package FEV1.dif.afip.gov.ar.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

	public static String getStringFromDate(Date fecha, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(fecha);
	}

	public static String getStringFromDate(Date fecha) {
		return getStringFromDate(fecha, "dd/MM/yyyy");
	}

	public static Date getDateFromString(String fecha, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(fecha);
		} catch (ParseException e) {
			return null;
		}
	}
}
