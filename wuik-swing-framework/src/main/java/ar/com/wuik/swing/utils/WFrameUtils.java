package ar.com.wuik.swing.utils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ar.com.wuik.swing.frames.WAbstractFrame;
import ar.com.wuik.swing.frames.WAbstractIFrame;
import ar.com.wuik.swing.frames.WApplication;


/**
 * Clase de Utilidad para WFrames.
 * 
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public final class WFrameUtils {


	private static final String FONT_NAME = "Arial";
	private static final Logger LOGGER = LoggerFactory.getLogger( WFrameUtils.class );
	private static double zoomFactor = 0;
	private static final int H_SIZE = 1360;
	private static int fontSize = 0;

	public enum FontSize {
		SMALL, NORMAL, LARGE
	};

	/**
	 * Inicializa los Datos del Layout.
	 */
	public static void initLayout() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		zoomFactor = WUtils.getRoundedValue( (double) d.width / H_SIZE + WUtils.EMPTY ).doubleValue();
		LOGGER.info( "Frame Size - [INITIALIZED] - [ZOOM-FACTOR: " + zoomFactor + "] [WIDTH: " + d.getWidth()
		    + "] [HEIGHT: " + d.getHeight() + "]" );
		fontSize = (int) ( 12 * zoomFactor );
		setUIFont( new javax.swing.plaf.FontUIResource( FONT_NAME, Font.PLAIN, fontSize ) );
	}

	/**
	 * Redimenciona los Componentes de un WAbstractFrame.
	 * 
	 * @param frame
	 *            WAbstractFrame - El frame origen en el cual se redimencionaran
	 *            sus componentes.
	 */
	public static void resizeComponents( WAbstractFrame frame ) {
		frame.setSize( (int) ( frame.getSize().getWidth() * zoomFactor ),
		    (int) ( frame.getSize().getHeight() * zoomFactor ) );
		resizeComponent( frame.getContentPane() );
	}
	
	public static void resizeComponents( Component component ) {
		resizeComponent( component);
	}

	/**
	 * Redimenciona los Componentes de un WAbstractIFrame.
	 * 
	 * @param frame
	 *            WAbstractIFrame - El frame origen en el cual se
	 *            redimencionaran sus componentes.
	 */
	public static void resizeComponents( WAbstractIFrame iframe ) {
		iframe.setSize( (int) ( iframe.getSize().getWidth() * zoomFactor ),
		    (int) ( iframe.getSize().getHeight() * zoomFactor ) );
		resizeComponent( iframe.getContentPane() );
	}

	private static void resizeComponent( Component c ) {
		if ( c instanceof JComponent ) {
			JComponent jc = (JComponent) c;
			if ( jc.getComponentCount() > 0 ) {
				for ( int i = 0; i < jc.getComponentCount(); i++ ) {
					resizeComponent( jc.getComponent( i ) );
				}
			}
			jc.setSize( (int) ( jc.getSize().getWidth() * zoomFactor ), (int) ( jc.getSize().getHeight() * zoomFactor ) );
			jc.setLocation( (int) ( jc.getLocation().getX() * zoomFactor ),
			    (int) ( jc.getLocation().getY() * zoomFactor ) );
		}

	}

	public static void setUIFont( javax.swing.plaf.FontUIResource f ) {
		java.util.Enumeration<?> keys = UIManager.getDefaults().keys();
		while( keys.hasMoreElements() ) {
			Object key = keys.nextElement();
			Object value = UIManager.get( key );
			if ( value != null && value instanceof javax.swing.plaf.FontUIResource )
				UIManager.put( key, f );
		}
	}

	public static Font getCustomFont( FontSize fSize, int style ) {

		int customFont = fontSize;
		if ( null != fSize ) {
			switch( fSize ){
				case SMALL:
					customFont = fontSize - 2;
					break;
				case LARGE:
					customFont = fontSize + 2;
					break;
				case NORMAL:
					customFont = fontSize;
					break;
			}
		}

		return new Font( FONT_NAME, style, customFont );
	}

	public static Font getCustomFont( int style, int size ) {
		int customFont = fontSize + size;
		return new Font( FONT_NAME, style, customFont );
	}
	
	public static Font getCustomFont( int style ) {
		return getCustomFont( FontSize.NORMAL, style );
	}

	public static Font getCustomFont( FontSize fSize ) {
		return getCustomFont( fSize, Font.PLAIN );
	}

	public static void showGlobalErrorMsg( String errorMsg ) {
		JOptionPane.showMessageDialog( WApplication.getInstance(), errorMsg
		    + ", vuelva a intentarlo o contacte al administrador.", "Error", JOptionPane.ERROR_MESSAGE );
	}
	
	public static void showGlobalMsg( String errorMsg ) {
		JOptionPane.showMessageDialog( WApplication.getInstance(), errorMsg, "Info", JOptionPane.INFORMATION_MESSAGE );
	}
	
	public static void installFocusListener( JSpinner spinner ) {

		JComponent spinnerEditor = spinner.getEditor();

		if ( spinnerEditor != null ) {

			// This is me spending a few days trying to make this work and
			// eventually throwing a hissy fit and just grabbing all the
			// JTextComponent components contained within the editor....
			List<JTextComponent> lstChildren = findAllChildren( spinner, JTextComponent.class );
			if ( lstChildren != null && lstChildren.size() > 0 ) {

				JTextComponent editor = lstChildren.get( 0 );
				FocusListener[] listeners = editor.getFocusListeners();
				if ( WUtils.isNotEmpty( listeners ) ) {
					for ( FocusListener focusListener: listeners ) {
						editor.removeFocusListener( focusListener );
					}
				}
				editor.addFocusListener( new SelectOnFocusGainedHandler() );
			}
		}
	}

	public static <T extends Component> List<T> findAllChildren( JComponent component, Class<T> clazz ) {

		List<T> lstChildren = new ArrayList<T>( 5 );
		for ( Component comp: component.getComponents() ) {

			if ( clazz.isInstance( comp ) ) {

				lstChildren.add( (T) comp );

			} else if ( comp instanceof JComponent ) {

				lstChildren.addAll( findAllChildren( (JComponent) comp, clazz ) );

			}

		}

		return Collections.unmodifiableList( lstChildren );

	}

	public static class SelectOnFocusGainedHandler extends FocusAdapter {


		@Override
		public void focusGained( FocusEvent e ) {

			Component comp = e.getComponent();
			if ( comp instanceof JTextComponent ) {
				final JTextComponent textComponent = (JTextComponent) comp;
				new Thread( new Runnable() {


					@Override
					public void run() {
						try {
							Thread.sleep( 25 );
						}
						catch( InterruptedException ex ) {
						}
						SwingUtilities.invokeLater( new Runnable() {


							@Override
							public void run() {
								textComponent.selectAll();
							}
						} );
					}
				} ).start();
			}
		}
	}
}
