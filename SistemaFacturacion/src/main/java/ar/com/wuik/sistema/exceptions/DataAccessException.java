package ar.com.wuik.sistema.exceptions;

public class DataAccessException extends Exception {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -274385869381347073L;

	public DataAccessException(Throwable th) {
		super(th);
	}

}
