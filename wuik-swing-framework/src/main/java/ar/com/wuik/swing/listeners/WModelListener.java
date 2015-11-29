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
public interface WModelListener {


	void dataUpdated();
	
	void genericEvent(Object o);
	
}
