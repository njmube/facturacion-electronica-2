package ar.com.wuik.sistema.filters;

public class UsuarioFilter {


	private String dni;
	private String nombre;
	private String password;
	private Long idToExclude;

	public String getDni() {
		return dni;
	}

	public void setDni( String dni ) {
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre( String nombre ) {
		this.nombre = nombre;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword( String password ) {
		this.password = password;
	}

	/**
	 * @return the idToExclude
	 */
	public Long getIdToExclude() {
		return idToExclude;
	}

	/**
	 * @param idToExclude
	 *            the idToExclude to set
	 */
	public void setIdToExclude( Long idToExclude ) {
		this.idToExclude = idToExclude;
	}

}
