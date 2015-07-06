package ar.com.wuik.swing.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Monitor de Sesion de Usuarios, contiene la logica para poder interceptar
 * cuando las sesion del Usuario ha vencido.
 * 
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public final class WSessionMonitor {


	private static final Logger LOGGER = LoggerFactory.getLogger( WSessionMonitor.class );
	private long lastTimeEvent = 0;
	private static WSessionMonitor sessionMonitor;
	private static final long DEF_TIME_OUT = 10;
	private long timeoutMinutes = DEF_TIME_OUT;

	/**
	 * Constructor.
	 */
	private WSessionMonitor() {
		LOGGER.info( "Session Monitor - [STARTED]" );
		if ( LOGGER.isDebugEnabled() ) {
			LOGGER.debug( "Timeout Minutes: " + timeoutMinutes );
		}
	}

	/**
	 * Establece el Timeout de checkeo de la sesion vencida.
	 * 
	 * @param timeoutMinutes
	 *            long - El Tiempo en minutos.
	 */
	public void setTimeoutMinutes( long timeoutMinutes ) {
		if ( timeoutMinutes > 0 ) {
			this.timeoutMinutes = timeoutMinutes;
		} else {
			this.timeoutMinutes = DEF_TIME_OUT;
		}
		if ( LOGGER.isDebugEnabled() ) {
			LOGGER.debug( "Set Timeout Minutes To: " + timeoutMinutes );
		}
	}

	/**
	 * Obtiene la Instancia del WSessionMonitor.
	 * 
	 * @return La Instancia.
	 */
	public static WSessionMonitor getInstance() {
		if ( null == sessionMonitor ) {
			sessionMonitor = new WSessionMonitor();
		}
		return sessionMonitor;
	}

	private long calculateIdleTime( final long currentTime ) {
		return currentTime - lastTimeEvent;
	}

	/**
	 * Verifica si la sesion ha expirado.
	 * 
	 * @param currentTime
	 *            long - El tiempo actual el milisegundos.
	 * @return Si la sesion ha expirado.
	 */
	public boolean hasExpiredSession( final long currentTime ) {
		final long idleTime = calculateIdleTime( currentTime );
		final long minutos = ( idleTime / 60000 );
		final boolean expiredSession = minutos >= timeoutMinutes;
		if ( LOGGER.isDebugEnabled() && expiredSession ) {
			LOGGER.debug( "Session Expired: " );
		}
		return expiredSession;
	}

	/**
	 * @return the lastTimeEvent
	 */
	public long getLastTimeEvent() {
		return lastTimeEvent;
	}

	/**
	 * @param lastTimeEvent
	 *            the lastTimeEvent to set
	 */
	public void setLastTimeEvent( final long lastTimeEvent ) {
		this.lastTimeEvent = lastTimeEvent;
	}

}
