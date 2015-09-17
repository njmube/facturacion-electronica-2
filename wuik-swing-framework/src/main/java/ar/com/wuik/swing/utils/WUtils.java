package ar.com.wuik.swing.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase de Utilidad.
 * 
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public final class WUtils {

	public static final String EMPTY = "";
	private static final Logger LOGGER = LoggerFactory.getLogger(WUtils.class);
	public static final String DEFAULT_PATTERN = "dd/MM/yyyy";

	/**
	 * Verificia si una Collection es vacia.
	 * 
	 * @param collection
	 *            Collection - La Collection a verificar.
	 * @return Si la Collection es vacia.
	 */
	public static boolean isEmpty(final Collection<?> collection) {
		return null == collection || collection.isEmpty();
	}

	/**
	 * Verificia si un String es vacio.
	 * 
	 * @param str
	 *            String - El String a verificar.
	 * @return Si el String es vacio.
	 */
	public static boolean isEmpty(final String str) {
		return null == str || str.trim().equals("");
	}

	/**
	 * Verificia si un Array es vacio.
	 * 
	 * @param array
	 *            Object[] - El Array a verificar.
	 * @return Si el Array es vacio.
	 */
	public static boolean isEmpty(final Object[] array) {
		return null == array || array.length == 0;
	}

	/**
	 * Verificia si un String no es vacio.
	 * 
	 * @param str
	 *            String - El String a verificar.
	 * @return Si el String no es vacio.
	 */
	public static boolean isNotEmpty(final String str) {
		return !isEmpty(str);
	}

	/**
	 * Verificia si una Collection no es vacia.
	 * 
	 * @param collection
	 *            Collection - La Collection a verificar.
	 * @return Si la Collection no es vacia.
	 */
	public static boolean isNotEmpty(final Collection<?> collection) {
		return !isEmpty(collection);
	}

	/**
	 * Verificia si un Array no es vacio.
	 * 
	 * @param array
	 *            Object[] - El Array a verificar.
	 * @return Si el Array no es vacio.
	 */
	public static boolean isNotEmpty(final Object[] array) {
		return !isEmpty(array);
	}

	/**
	 * Obtiene un String apartir de un Date.
	 * 
	 * @param date
	 *            Date - La Fecha origen.
	 * @param pattern
	 *            String - El patron de la fecha.
	 * @return La fecha como String.
	 */
	public static String getStringFromDate(final Date date, final String pattern) {
		if (null != date) {
			try {
				SimpleDateFormat format = new SimpleDateFormat(
						(isNotEmpty(pattern)) ? pattern : DEFAULT_PATTERN);
				return format.format(date);
			} catch (Exception exc) {
				LOGGER.warn("Utils.getStringFromDate", exc);
			}
		}
		return null;
	}

	/**
	 * Obtiene un String apartir de un Date mediante el Pattern por Defecto.
	 * 
	 * @param date
	 *            Date - La Fecha origen.
	 * @return La fecha como String.
	 */
	public static String getStringFromDate(final Date date) {
		return getStringFromDate(date, null);
	}

	/**
	 * Obtiene un Date apartir de un String.
	 * 
	 * @param str
	 *            String - La Fecha como String.
	 * @param pattern
	 *            String - El patron de la fecha.
	 * @return La fecha como Date.
	 */
	public static Date getDateFromString(final String str, final String pattern) {
		if (isNotEmpty(str)) {
			try {
				SimpleDateFormat format = new SimpleDateFormat(
						(isNotEmpty(pattern)) ? pattern : DEFAULT_PATTERN);
				format.setLenient(Boolean.FALSE);
				return format.parse(str);
			} catch (Exception exc) {
				LOGGER.warn("Utils.getStringFromDate", exc);
			}
		}
		return null;
	}

	/**
	 * Obtiene un Date apartir de un String, mediante el Pattern por Defecto.
	 * 
	 * @param str
	 *            String - La Fecha como String.
	 * @return La fecha como Date.
	 */
	public static Date getDateFromString(final String str) {
		return getDateFromString(str, null);
	}

	/**
	 * Verifica si la Fecha String es valida.
	 * 
	 * @param date
	 *            String - La Fecha a validar.
	 * @return Si es valida o no.
	 */
	public static boolean isValidDate(final String date) {
		if (null != date) {
			final SimpleDateFormat format = new SimpleDateFormat(
					DEFAULT_PATTERN);
			format.setLenient(Boolean.FALSE);
			try {
				format.parse(date);
				return Boolean.TRUE;
			} catch (ParseException pexc) {
				LOGGER.warn("Utils.getStringFromDate", pexc);
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * Redondea un valor String (Decimal).
	 * 
	 * @param value
	 *            String - El valor a redondear.
	 * @param decimals
	 *            int - La cantidad de decimales.
	 * @return El valor redondeado como BigDecimal.
	 */
	public static BigDecimal getRoundedValue(final String value,
			final int decimals) {

		if (null != value && decimals > 0) {
			final String[] tokens = value.split("\\.");
			try {
				if (tokens.length > 1) {

					if (Long.valueOf(tokens[1]) == 0) {
						tokens[1] = "0";
					}

					tokens[1] = rightPadding(tokens[1], decimals, "0");

					BigDecimal decimalRounded = new BigDecimal(
							"0." + tokens[1]);
					decimalRounded = decimalRounded.setScale(decimals, RoundingMode.HALF_EVEN);
					BigDecimal nonDecimalPart = new BigDecimal(tokens[0]);
					if (nonDecimalPart.doubleValue() < 0) {
						return decimalRounded.add(
								new BigDecimal(tokens[0]).abs()).negate();
					} else {
						return decimalRounded.add(new BigDecimal(tokens[0]));
					}
				} else {
					return new BigDecimal(value + "."
							+ leftPadding("", decimals, "0"));
				}
			} catch (NumberFormatException nfexc) {
			}
		}
		return null;
	}

	/**
	 * Redondea un valor String (Decimal), con 2 decimales.
	 * 
	 * @param value
	 *            String - El valor a redondear.
	 * @return El valor redondeado como BigDecimal.
	 */
	public static BigDecimal getRoundedValue(final String value) {
		return getRoundedValue(value, 2);
	}

	public static BigDecimal getRoundedValue(final BigDecimal value) {
		return getRoundedValue(value.toEngineeringString(), 2);
	}

	public static BigDecimal getValue(final String value, final int decimals) {
		if (null != value && decimals > 0) {
			final String[] tokens = value.split("\\.");
			try {
				if (tokens.length > 1) {

					if (Long.valueOf(tokens[1]) == 0) {
						tokens[1] = "0";
					}

					tokens[1] = rightPadding(tokens[1], decimals, "0");

					if (tokens[1].length() > decimals) {
						tokens[1] = tokens[1].substring(0, decimals);
					}

					BigDecimal decimalRounded = new BigDecimal("0." + tokens[1]);

					BigDecimal nonDecimalPart = new BigDecimal(tokens[0]);
					if (nonDecimalPart.doubleValue() < 0) {
						return decimalRounded.add(
								new BigDecimal(tokens[0]).abs()).negate();
					} else {
						return decimalRounded.add(new BigDecimal(tokens[0])
								.abs());
					}
				} else {
					if (value.lastIndexOf('.') != -1) {
						return new BigDecimal(value
								+ leftPadding("", decimals, "0"));
					} else {
						return new BigDecimal(value + "."
								+ leftPadding("", decimals, "0"));
					}
				}
			} catch (NumberFormatException nfexc) {
			}
		}
		return null;
	}

	public static BigDecimal getValue(final String value) {
		if (isEmpty(value)) {
			return BigDecimal.ZERO;
		} else {
			return getValue(value, 2);
		}
	}

	public static BigDecimal getValueNullZero(final String value) {
		BigDecimal valueDecimal = getValue(value, 2);
		return null == valueDecimal ? BigDecimal.ZERO : valueDecimal;
	}

	public static BigDecimal getValueNullZero(final String value,
			final int decimals) {
		BigDecimal valueDecimal = getValue(value, decimals);
		return null == valueDecimal ? BigDecimal.ZERO : valueDecimal;
	}

	public static BigDecimal getValueNullZero(final BigDecimal value) {
		BigDecimal valueDecimal = getValue(value.toEngineeringString(), 2);
		return null == valueDecimal ? BigDecimal.ZERO : valueDecimal;
	}

	public static BigDecimal getValue(final BigDecimal value) {
		if (null != value) {
			return getValue(value.toEngineeringString(), 2);
		} else {
			return BigDecimal.ZERO;
		}
	}

	/**
	 * Verifica si un valor String contiene blancos.
	 * 
	 * @param value
	 *            String - El valor a verificar.
	 * @return Si contiene blancos o no.
	 */
	public static boolean containsBlank(final String value) {
		if (null != value) {
			return -1 != value.indexOf(" ");
		}
		return Boolean.FALSE;
	}

	/**
	 * Establece la hora minima (Horas, Minutos, Segundos, Milis) para una
	 * fecha.
	 * 
	 * @param fecha
	 *            Date- La Fecha a establecer.
	 * @return La fecha con su hora minima.
	 */
	public static Date getMinHour(final Date fecha) {
		if (null != fecha) {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(fecha);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			return cal.getTime();
		}
		return null;
	}

	/**
	 * Establece la hora maxima (Horas, Minutos, Segundos, Milis) para una
	 * fecha.
	 * 
	 * @param fecha
	 *            Date- La Fecha a establecer.
	 * @return La fecha con su hora maxima.
	 */
	public static Date getMaxHour(final Date fecha) {
		if (null != fecha) {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(fecha);
			cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
			cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
			cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
			return cal.getTime();
		}
		return null;
	}

	/**
	 * Capitaliza un valor.
	 * 
	 * @param value
	 *            String - El valor a capitalizar.
	 * @return El valor capitalizado.
	 */
	public static String capitalize(final String value) {
		if (isNotEmpty(value)) {
			final StringBuilder strBuilder = new StringBuilder(value);
			strBuilder.replace(0, 1, value.substring(0, 1).toUpperCase());
			return strBuilder.toString();
		}
		return value;
	}

	/**
	 * Establece la fecha minima (Dia, Hora, Minutos, Segundos, Milis) de una
	 * fecha.
	 * 
	 * @param date
	 *            Date - La Fecha a establecer.
	 * @return La fecha con su fecha minima.
	 */
	public static Date getMinDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
		return getMinHour(cal.getTime());
	}

	/**
	 * Establece la fecha maxima (Dia, Hora, Minutos, Segundos, Milis) de una
	 * fecha.
	 * 
	 * @param date
	 *            Date - La Fecha a establecer.
	 * @return La fecha con su fecha maxima.
	 */
	public static Date getMaxDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
		return getMaxHour(cal.getTime());
	}

	/**
	 * Realiza un Trim de un valor String.
	 * 
	 * @param value
	 *            String - El valor.
	 * @return El valor con su Trim.
	 */
	public static String trim(final String value) {
		return (null != value) ? value.trim() : value;
	}

	/**
	 * Crea un nuevo valor apartir de un valor origen, concatenando el valor pad
	 * hacia la izquierda teniendo en cuenta el tamanio.
	 * 
	 * @param value
	 *            String - El valor origen.
	 * @param size
	 *            int - La longitud total del nuevo valor
	 * @param pad
	 *            String - El caracter de relleno.
	 * @return El nuevo valor.
	 */
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

	public static String rightPadding(final String value, final int size,
			final String pad) {
		if (null != value && (null != pad && pad.length() == 1)) {
			final int rightSize = size - value.length();
			if (rightSize > 0) {
				final StringBuilder valuePadded = new StringBuilder(value);
				for (int i = 0; i < rightSize; i++) {
					valuePadded.append(pad);
				}
				return valuePadded.toString();
			}
		}
		return value;
	}

	/**
	 * Convierte un Arreglo de Integer a su primitivo.
	 * 
	 * @param array
	 *            Integer[] - El Arreglo de Integer.
	 * @return El Arreglo convertido a primitivo.
	 */
	public static int[] toPrimitive(Integer[] array) {
		if (array == null) {
			return null;
		} else if (array.length == 0) {
			return new int[0];
		}
		final int[] result = new int[array.length];
		for (int i = 0; i < array.length; i++) {
			Integer b = array[i];
			result[i] = (b == null ? -1 : b.intValue());
		}
		return result;
	}

	/**
	 * //TODO: Describir el metodo isNumeric
	 * 
	 * @param campoBusqueda
	 * @return
	 */
	public static boolean isNumeric(String campoBusqueda) {
		try {
			Long.parseLong(campoBusqueda);
			return Boolean.TRUE;
		} catch (Exception e) {
			return Boolean.FALSE;
		}
	}

	public static byte[] toByteArray(InputStream is) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			int reads = is.read();
			while (reads != -1) {
				baos.write(reads);
				reads = is.read();
			}
		} catch (Exception exc) {
			LOGGER.error("Error al convertir Stream a byte[]", exc);
		}
		return baos.toByteArray();
	}

	public static void main(String[] args) {
		System.out.println(getRoundedValue(new BigDecimal("4.545454")));
	}

}
