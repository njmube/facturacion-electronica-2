/**
* 
*/
package ar.com.wuik.swing.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


/**
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public class WImageItem extends JPanel {


	private JLabel label;
	protected Long id;
	protected byte[] image;
	private String texto;

	public WImageItem(final Long id, byte[] image, Dimension d, Dimension dLabel) {
		this.id = id;
		this.image = image;
		setSize( d );
		setPreferredSize( d );
		setMinimumSize( d );
		setLayout( new BorderLayout( 5, 5 ) );
		label = new JLabel();
		label.setBackground( Color.BLACK );
		label.setOpaque( true );
		label.setHorizontalAlignment( SwingConstants.CENTER );
		label.setBorder( BorderFactory.createLineBorder( Color.GRAY ) );
		label.setSize(dLabel );
		label.setPreferredSize( dLabel );
		add( label, BorderLayout.CENTER );
		setCursor( new Cursor( Cursor.HAND_CURSOR ) );
	}

	public void showPreview() {
		if ( null != image ) {

			ImageIcon icon = new ImageIcon( image );
			Image image = icon.getImage();

			int h = icon.getIconHeight();
			int w = icon.getIconWidth();

			int hPanel = label.getSize().height;
			int wPanel = label.getSize().width;

			int difH = hPanel - h;
			int difW = wPanel - w;

			int wResize = w;
			int hResize = h;

			if ( difH < difW ) {
				hResize = h - Math.abs( difH );
				wResize = getAspectRatio( h, hResize, w );
			} else if ( difH > difW ) {
				wResize = w - Math.abs( difW );
				hResize = getAspectRatio( w, wResize, h );
			} else {
				wResize = w - Math.abs( difW );
				hResize = h - Math.abs( difH );
			}

			label.setIcon( new ImageIcon( image.getScaledInstance( wResize, hResize, Image.SCALE_SMOOTH ) ) );
			this.repaint();
		}
	}

	private static int getAspectRatio( int originalSize, int newSize, int originalOtherSize ) {
		double ratio = (double) newSize / originalSize;
		ratio = ratio * originalOtherSize;
		return (int) Math.round( ratio );
	}

	/**
	 * //TODO: Describir el metodo clear
	 */
	public void clear() {
		label.setIcon( null );
	}

	/**
	 * @see java.awt.Component#toString()
	 */
	@Override
	public String toString() {
		return texto;
	}

}
