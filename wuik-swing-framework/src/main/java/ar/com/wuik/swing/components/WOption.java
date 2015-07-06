package ar.com.wuik.swing.components;

/**
 * Clase que representa una Option, contiene clave-valor.
 * 
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public final class WOption {


	private Long value;
	private String key;

	/**
	 * Constructor.
	 * 
	 * @param value
	 *            Long - El Valor.
	 * @param key
	 *            String - La Clave.
	 */
	public WOption(Long value, String key) {
		if ( null == value ) {
			throw new IllegalArgumentException( "El valor para el campo Value no puede ser nulo" );
		}
		if ( null == key ) {
			throw new IllegalArgumentException( "El valor para el campo Key no puede ser nulo" );
		}
		this.value = value;
		this.key = key;
	}

	/**
	 * Constructor.
	 * 
	 * @param value
	 *            Long - El Valor.
	 */
	public WOption(Long value) {
		if ( null == value ) {
			throw new IllegalArgumentException( "El valor para el campo Value no puede ser nulo" );
		}
		this.value = value;
	}

	/**
	 * Obtiene el valor de Value.
	 * 
	 * @return El valor de Value.
	 */
	public Long getValue() {
		return value;
	}

	/**
	 * Obtiene el valor de la Key.
	 * 
	 * @return El valor de la Key.
	 */
	public String getKey() {
		return key;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( obj instanceof WOption ) {
			return ( (WOption) obj ).getValue().equals( getValue() );
		}
		return Boolean.FALSE;
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public String toString() {
		if ( null != key )
			return key.toString();
		return "";
	}

	public static WOption getWOptionSelecione() {
		return new WOption( - 1L, "- Seleccione -" );
	}
	
	public static WOption getWOptionTodos() {
		return new WOption( - 1L, "TODOS" );
	}

	public static Long getIdWOptionSeleccion() {
		return - 1L;
	}
	
	public static Long getIdWOptionTodos() {
		return - 1L;
	}
}
