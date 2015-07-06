package FEV1.dif.afip.gov.ar.exceptions;

import java.util.List;

public class ServiceException extends Exception {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 5999808668876703103L;

	private List<String> errores;

	public ServiceException(Throwable th, String msg) {
		super(msg, th);
	}

	public ServiceException(String msg, List<String> errores) {
		super(msg);
		this.errores = errores;
	}

	public List<String> getErrores() {
		return errores;
	}

}
