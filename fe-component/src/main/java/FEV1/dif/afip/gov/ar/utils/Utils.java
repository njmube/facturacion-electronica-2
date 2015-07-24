package FEV1.dif.afip.gov.ar.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import FEV1.dif.afip.gov.ar.entities.TipoComprobante;

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

	public static String generarCodigoBarras(String cuit, int tipoComprobante,
			int ptoVta, String cae, String fechaVtoCae) {
		String codigoBarras = cuit + leftPadding(tipoComprobante + "", 2, "0")
				+ leftPadding(ptoVta + "", 4, "0") + cae + fechaVtoCae;

		int digitoVerificador = obtenerDigitoVerificador(codigoBarras);
		return codigoBarras + digitoVerificador;
	}

	public static int obtenerDigitoVerificador(String numero) {

		int sumaPares = 0;
		int sumaImpares = 0;

		for (int i = 1; i <= numero.length(); i++) {
			if ((i % 2) == 0) {
				sumaPares += Integer.valueOf(numero.charAt(i - 1) + "");
			} else {
				sumaImpares += Integer.valueOf(numero.charAt(i - 1) + "");
			}
		}

		int totalImpares = sumaImpares * 3;
		int totalPares = sumaPares + totalImpares;

		for (int i = 0; i < 9; i++) {
			if (((totalPares + i) % 10) == 0) {
				return i;
			}
		}
		return 0;
	}

	public static String leftPadding(final String value, final int size,
			final String pad) {
		if (null != value && (null != pad && pad.length() == 1)) {
			final int leftSize = size - value.length();
			if (leftSize > 0) {
				final StringBuilder valuePadded = new StringBuilder();
				for (int i = 0; i < leftSize; i++) {
					valuePadded.append(pad);
				}
				return valuePadded.append(value).toString();
			}
		}
		return value;
	}

	public static String generarFormatoComprobante(int ptoVta, long nroComp) {
		return  generarFormatoPtoVta(ptoVta) + "-" +  leftPadding(nroComp + "", 8, "0") ;
	}

	public static String generarFormatoPtoVta(int ptoVta) {
		return leftPadding(ptoVta + "", 4, "0");
	}
}
