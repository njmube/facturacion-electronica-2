package ar.com.wuik.sistema.exceptions;

public class ReportException extends Exception {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -8274312842032282421L;

	public ReportException(Throwable th) {
		super(th);
	}

	public ReportException(Throwable th, String msg) {
		super(msg, th);
	}

}
