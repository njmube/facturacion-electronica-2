package ar.com.wuik.sistema.exceptions;

public class BusinessException extends Exception {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -8274312842032282421L;

	public BusinessException(Throwable th) {
		super(th);
	}
	
	public BusinessException(Throwable th, String msg) {
		super(msg, th);
	}
	
	public BusinessException(String msg) {
		super(msg);
	}

}
