package ar.com.wuik.swing.components;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Clase que representa a un Model, guarda los valores en un mapa Clave-Valor.
 * 
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public final class WModel {

	/** Contenedor */
	private final Map<String, Object> data;

	/**
	 * Constructor
	 */
	public WModel() {
		data = new HashMap<String, Object>();
	}

	/**
	 * Agrega un nuevo valor para un campo.
	 * 
	 * @param field
	 *            String - El nombre de Campo.
	 * @param value
	 *            Object - El valor del Campo.
	 */
	public void addValue(final String field, final Object value) {
		data.put(field, value);
	}

	/**
	 * Obtiene el valor de un campo, mediante su nombre.
	 * 
	 * @param field
	 *            String - El nombre del Campo.
	 * @return El valor del Campo.
	 */
	@SuppressWarnings ( "unchecked" )
    public <T> T getValue(final String field) {
		return (T) data.get(field);
	}

	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		if (null != data) {
			Set<String> keys = data.keySet();
			for (String key : keys) {
				strBuilder.append("KEY[" + key + "] - VALUE[" + data.get(key)
						+ "]\n");
			}
		}

		return strBuilder.toString();
	}

	/**
	 * Verifica si el Campo ya existe en el Model.
	 * 
	 * @param field
	 *            String - El Campo a verificar.
	 * @return Si existe o no en el Model.
	 */
	public boolean containsField(final String field) {
		return data.containsKey(field);
	}

	public void resetModel() {
		Set<String> keys = data.keySet();
		for (String key : keys) {
			data.put(key, null);
		}
	}

}
