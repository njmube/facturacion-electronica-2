package ar.com.wuik.swing.frames;

import java.awt.Cursor;

import javax.swing.JFrame;

import ar.com.wuik.swing.utils.WFrameUtils;


/**
 * Clase base para JFrames.
 * 
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public abstract class WAbstractFrame extends JFrame {


	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = - 6814676439813607224L;
	

	/**
	 * Oculta el Frame.
	 */
	public void hideFrame() {
		setVisible( Boolean.FALSE );
		this.dispose();
	}

	/**
	 * Visualiza el Frame.
	 */
	public void showFrame() {
		WFrameUtils.resizeComponents( this );
		setVisible( Boolean.TRUE );
		setLocationRelativeTo( null );
	}

	/**
	 * Establece el cursor en Espera.
	 */
	protected void showBusyCursor() {
		setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
	}

	/**
	 * Establece el cursor Default.
	 */
	protected void showNormalCursor() {
		setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );

	}

	protected void showGlobalErrorMsg( String errorMsg ) {
		WFrameUtils.showGlobalErrorMsg( errorMsg );
	}
}
