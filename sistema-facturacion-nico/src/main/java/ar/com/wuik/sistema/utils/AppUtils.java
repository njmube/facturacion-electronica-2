/**
 * Autor : juan.vazquez@wuik.com.ar - Wuik-Working Innovation Creacion :
 * 12/03/2014 - 10:48:40
 */
package ar.com.wuik.sistema.utils;

import java.math.BigDecimal;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.swing.utils.WUtils;

/**
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public class AppUtils {

	protected static final Logger LOGGER = LoggerFactory
			.getLogger(AppUtils.class);

	public static String getAppVersion() {
		try {
			URLClassLoader cl = (URLClassLoader) AppUtils.class
					.getClassLoader();
			URL url = cl.findResource("META-INF/MANIFEST.MF");
			Manifest manifest = new Manifest(url.openStream());
			String version = manifest.getMainAttributes().getValue(
					"Implementation-Version");
			if (WUtils.isNotEmpty(version)) {
				return "v" + version;
			}
		} catch (Exception exc) {
			LOGGER.error("Error al obtener versión de Aplicación", exc);
		}
		return "<Version>";
	}

	public static boolean esValidoCUIT(String cuit) {

		if (WUtils.isEmpty(cuit)) {
			return Boolean.FALSE;
		}

		cuit = (cuit.contains("-") ? cuit.substring(0, 2)
				+ cuit.substring(3, 11) + cuit.substring(12) : cuit);

		int cuitLength = cuit.length();
		for (int i = 0; i < cuitLength; i++) {
			if (!Character.isDigit(cuit.charAt(0))) {
				return Boolean.FALSE;
			}
		}

		int mult[] = { 5, 4, 3, 2, 7, 6, 5, 4, 3, 2 };
		char nums[] = cuit.toCharArray();
		int total = 0;
		for (int i = 0; i < mult.length; i++) {
			total += ((int) (nums[i]) - 48) * mult[i];
		}
		int resto = total % 11;
		resto = resto == 0 ? 0 : resto == 1 ? 9 : 11 - resto;

		return (resto == ((int) (nums[nums.length - 1]) - 48));
	}

	public static BigDecimal convertirDolarAPeso(BigDecimal cotizacion,
			BigDecimal dolares) {
		if (null != dolares && dolares.doubleValue() > 0 && null != cotizacion
				&& cotizacion.doubleValue() > 0) {
			return WUtils.getValue(dolares.multiply(cotizacion)
					.toEngineeringString());
		}
		return BigDecimal.ZERO;
	}

	public static BigDecimal convertirPesoADolar(BigDecimal cotizacion,
			BigDecimal pesos) {
		if (null != pesos && pesos.doubleValue() > 0 && null != cotizacion
				&& cotizacion.doubleValue() > 0) {
			double totalPesos = pesos.doubleValue() / cotizacion.doubleValue();
			return WUtils.getValue(totalPesos + "");
		}
		return BigDecimal.ZERO;
	}

	public static BigDecimal convertirPesoADolarRounded(BigDecimal cotizacion,
			BigDecimal pesos) {
		if (null != pesos && pesos.doubleValue() > 0 && null != cotizacion
				&& cotizacion.doubleValue() > 0) {
			double totalPesos = pesos.doubleValue() / cotizacion.doubleValue();
			return WUtils.getRoundedValue(totalPesos + "");
		}
		return BigDecimal.ZERO;
	}

	public static String formatDolar(BigDecimal value) {
		return "U$S " + WUtils.getValue(value).toEngineeringString();
	}

	public static String formatPeso(BigDecimal value) {
		return "$ " + WUtils.getValue(value).toEngineeringString();
	}
}