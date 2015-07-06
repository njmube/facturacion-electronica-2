/**
 * com.foo is a group of bar utils for operating on foo things.
 */
package ar.com.wuik.swing.listeners;

/**
 * Listener para WTable.
 * 
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 * @param <T>
 */
public interface WTableListener {


	/**
	 * Listener para Double Click sobre Fila.
	 * 
	 * @param selectedItem
	 *            Object[] - El item seleccionado.
	 */
	void doubleClickListener( Object[] selectedItem );

	/**
	 * Listener para Simple Click sobre Fila.
	 * 
	 * @param selectedItem
	 *            Object[] - El item seleccionado.
	 */
	void singleClickListener( Object[] selectedItem );

}
