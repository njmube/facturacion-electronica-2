package FEV1.dif.afip.gov.ar.exceptions;


public class ServiceException extends Exception {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 5999808668876703103L;

	public ServiceException(Throwable th, String msg) {
		super(msg, th);
	}

	public ServiceException(Throwable th) {
		super(th);
	}

}
