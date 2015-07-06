package ar.com.wuik.swing.events;

import java.util.Date;


/**
 * Evento lanzado cuando se ha realizado la seleccion en el WCalendarIFrame.
 * 
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public interface CalendarSelectedEvent {


	/**
	 * Ejecuta el evento de Fecha Seleccionada.
	 * 
	 * @param selectedFecha
	 *            Date - La Fecha seleccionada.
	 */
	void execute( Date selectedFecha );

}
