package ar.com.wuik.swing.utils;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.java.balloontip.BalloonTip;
import net.java.balloontip.styles.BalloonTipStyle;
import net.java.balloontip.styles.RoundedBalloonStyle;
import net.java.balloontip.utils.TimingUtils;
import org.apache.commons.lang.WordUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;


/**
 * Clase de Utilidad para los Tooltips de mensajes.
 * 
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public final class WTooltipUtils {


	/**
	 * Enumeracion Tipos de Mensajes.
	 * 
	 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
	 */
	public enum MessageType {
		INFO, ERROR, ALERTA
	}

	private static BalloonTip bTip = null;

	private static synchronized void showMessages( final List<JLabelToolTip> jLabelToolTips,
	                final JComponent targetComponent ) {

		if ( WUtils.isNotEmpty( jLabelToolTips ) ) {
			final JPanel jPanel = new JPanel();
			jPanel.setLayout( new BoxLayout( jPanel, BoxLayout.PAGE_AXIS ) );
			for ( JLabelToolTip labelToolTip: jLabelToolTips ) {
				jPanel.add( labelToolTip );
			}

			if ( null != bTip ) {
				bTip.closeBalloon();
			}

			final BalloonTipStyle edgedLook = new RoundedBalloonStyle( 5, 5, jPanel.getBackground(), Color.GRAY );
			bTip = new BalloonTip( targetComponent, jPanel, edgedLook, Boolean.FALSE );
			bTip.addMouseListener( new MouseAdapter() {


				@Override
				public void mouseClicked( final MouseEvent arg0 ) {
					bTip.closeBalloon();
				}
			} );
			TimingUtils.showTimedBalloon( bTip, 3000 );
		}
	}

	/**
	 * Muestra el Tooltip para los mensajes recibidos como argumento.
	 * 
	 * @param messages
	 *            List<String> - La Lista de Mensajes.
	 * @param targetComponent
	 *            JComponent - El componente al cual se le atachara el tooltip.
	 * @param messageType
	 *            MessageType - El tipo de Mensaje.
	 */
	public static synchronized void showMessages( final List<String> messages, final JComponent targetComponent,
	                final MessageType messageType ) {

		if ( WUtils.isNotEmpty( messages ) ) {
			final List<JLabelToolTip> jLabelToolTips = new ArrayList<JLabelToolTip>();
			for ( String mensaje: messages ) {
				jLabelToolTips.add( new JLabelToolTip( messageType, mensaje ) );
			}
			showMessages( jLabelToolTips, targetComponent );
		}
	}

	/**
	 * Muestra el Tooltip para el mensaje recibido como argumento.
	 * 
	 * @param message
	 *            String - El mensaje.
	 * @param targetComponent
	 *            JComponent - El componente al cual se le atachara el tooltip.
	 * @param messageType
	 *            MessageType - El tipo de Mensaje.
	 */
	public static synchronized void showMessage( final String message, final JComponent targetComponent,
	                final MessageType messageType ) {
		if ( WUtils.isNotEmpty( message ) ) {
			List<String> messages = new ArrayList<String>();
			messages.add( message );
			showMessages( messages, targetComponent, messageType );
		}
	}

}

/**
 * Clase que representa al contenedor de mensajes del Tooltip.
 * 
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
class JLabelToolTip extends JLabel {


	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = - 2938513196074612523L;

	/**
	 * Constructor.
	 * 
	 * @param messageType
	 *            MessageType - El tipo de Mensaje.
	 * @param mensaje
	 *            String - El Mensaje.
	 */
	public JLabelToolTip(final MessageType messageType, final String mensaje) {
		ImageIcon icon = null;

		switch( messageType ){
			case INFO:
				icon = new ImageIcon( WTooltipUtils.class.getResource( "/icons/info.png" ) );
				break;
			case ALERTA:
				icon = new ImageIcon( WTooltipUtils.class.getResource( "/icons/alerta.png" ) );
				break;
			case ERROR:
				icon = new ImageIcon( WTooltipUtils.class.getResource( "/icons/error.png" ) );
				break;
			default:
				break;
		}
		setIcon( icon );
		setText( "<html>" + WordUtils.wrap( mensaje, 40 ).replace( "\r\n", "<br/>" ) + "</html>" );
	}

}
