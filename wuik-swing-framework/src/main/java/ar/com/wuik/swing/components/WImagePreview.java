package ar.com.wuik.swing.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class WImagePreview extends JPanel {


	private static final long serialVersionUID = 7727716891718362939L;
	private JLabel label;

	public WImagePreview() {
		setLayout( new BorderLayout( 5, 5 ) );
		label = new JLabel();
		label.setBackground( Color.BLACK );
		label.setOpaque( true );
		label.setHorizontalAlignment( SwingConstants.CENTER );
		label.setBorder( BorderFactory.createLineBorder( Color.GRAY, 1 ) );
		add( label, BorderLayout.CENTER );
	}

	public void setFile( File file ) {
		ImageIcon icon = new ImageIcon( file.getAbsolutePath() );
		showPreview( icon );
	}

	public void setFileBytes( byte[] fileBytes ) {
		if ( null != fileBytes ) {
			ImageIcon icon = new ImageIcon( fileBytes );
			showPreview( icon );
		}
	}

	private void showPreview( ImageIcon icon ) {
		Image image = icon.getImage();

		int h = icon.getIconHeight();
		int w = icon.getIconWidth();

		int hPanel = getSize().height;
		int wPanel = getSize().width;

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

	private static int getAspectRatio( int originalSize, int newSize, int originalOtherSize ) {
		double ratio = (double) newSize / originalSize;
		ratio = ratio * originalOtherSize;
		return (int) Math.round( ratio );
	}

	public void clear() {
		label.setIcon( null );
	}

}
