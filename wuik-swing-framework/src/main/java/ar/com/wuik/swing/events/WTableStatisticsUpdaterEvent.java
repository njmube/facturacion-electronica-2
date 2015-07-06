package ar.com.wuik.swing.events;

/**
 * Evento lanzado cuando los datos de la WTable cambian.
 * 
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public interface WTableStatisticsUpdaterEvent {


	/**
	 * Actualiza las Estadisticas de la WTable.
	 * 
	 * @param rowCount
	 *            int - La Cantidad de Registros que visualiza la WTable.
	 */
	void updateStatistics( int rowCount );
}
