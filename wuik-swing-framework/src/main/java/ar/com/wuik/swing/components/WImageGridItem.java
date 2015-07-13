/**
* 
*/
package ar.com.wuik.swing.components;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;


/**
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public class WImageGridItem extends JPanel {


	private JScrollPane scrollPane;
	private JPanel panel;
	private int columnCount;
	protected List<WImageItem> items;

	/**
	 * Create the panel.
	 */
	public WImageGridItem() {
		this.columnCount = 5;
		setLayout( new BorderLayout( 0, 0 ) );
		add( getScrollPane(), BorderLayout.CENTER );
	}

	public WImageGridItem(int columnCount) {
		this.columnCount = columnCount;
		setLayout( new BorderLayout( 0, 0 ) );
		add( getScrollPane(), BorderLayout.CENTER );
	}

	public void setColumnCount( int columnCount ) {
		this.columnCount = columnCount;
	}

	private JScrollPane getScrollPane() {
		if ( scrollPane == null ) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView( getPanel() );
		}
		return scrollPane;
	}

	private JPanel getPanel() {
		if ( panel == null ) {
			panel = new JPanel();
			panel.setLayout( new WGridLayout( columnCount, 5 ) );
		}
		return panel;
	}

	public void addItems( List<WImageItem> items ) {
		this.items = items;
		for ( int i = 0; i < items.size(); i++ ) {
			WImageItem wImageItem = items.get( i );
			wImageItem.showPreview();
			panel.add( wImageItem );
		}
	}

}
