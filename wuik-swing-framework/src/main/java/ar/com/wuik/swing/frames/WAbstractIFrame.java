package ar.com.wuik.swing.frames;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyVetoException;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.border.LineBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import ar.com.wuik.swing.utils.WFrameUtils;


/**
 * Clase base para JInternalFrames.
 * 
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public abstract class WAbstractIFrame extends JInternalFrame {


	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1434316636239962768L;

	/**
	 * Constructor.
	 */
	public WAbstractIFrame() {
		setBorder( new LineBorder( null ) );
		setClosable( Boolean.TRUE );
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		setGlassPane( new GlassPane() );
		addInternalFrameListener( new InternalFrameAdapter() {


			@Override
			public void internalFrameClosing( InternalFrameEvent e ) {
				WApplication.getInstance().closeIFrame( WAbstractIFrame.this );
			}
		} );
	}

	/**
	 * Oculta el Frame.
	 */
	public void hideFrame() {
		WApplication.getInstance().closeIFrame( this );
		this.dispose();
	}

	/**
	 * Visualiza el Frame.
	 */
	public void showFrame() {
		WFrameUtils.resizeComponents( this );
		setVisible( Boolean.TRUE );
//		setLocation( 0, 0 );
		Dimension desktopSize = getDesktopPane().getSize();
		Dimension jInternalFrameSize = getSize();
		setLocation((desktopSize.width - jInternalFrameSize.width)/2,
		    (desktopSize.height- jInternalFrameSize.height)/10);
		try {
			setClosed( Boolean.FALSE );
			setSelected( Boolean.TRUE );
		}
		catch( PropertyVetoException e ) {

		}
	}


	/**
	 * Filter que provee UpperCase para los Componentes de Texto.
	 * 
	 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
	 */
	class UppercaseDocumentFilter extends DocumentFilter {


		public void insertString( DocumentFilter.FilterBypass fb, int offset, String text, AttributeSet attr )
		                throws BadLocationException {

			fb.insertString( offset, text.toUpperCase(), attr );
		}

		public void replace( DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs )
		                throws BadLocationException {

			fb.replace( offset, length, text.toUpperCase(), attrs );
		}
	}

	public static class WFocusTraversalPolicy extends FocusTraversalPolicy {


		Vector<Component> order;

		public WFocusTraversalPolicy(Vector<Component> order) {
			this.order = new Vector<Component>( order.size() );
			this.order.addAll( order );
		}

		public Component getComponentAfter( Container focusCycleRoot, Component aComponent ) {
			int idx = ( order.indexOf( aComponent ) + 1 ) % order.size();
			return order.get( idx );
		}

		public Component getComponentBefore( Container focusCycleRoot, Component aComponent ) {
			int idx = order.indexOf( aComponent ) - 1;
			if ( idx < 0 ) {
				idx = order.size() - 1;
			}
			return order.get( idx );
		}

		public Component getDefaultComponent( Container focusCycleRoot ) {
			return order.get( 0 );
		}

		public Component getLastComponent( Container focusCycleRoot ) {
			return order.lastElement();
		}

		public Component getFirstComponent( Container focusCycleRoot ) {
			return order.get( 0 );
		}
	}

	/**
	 * Establece el foco en el siguiente orden en el que los Componentes son
	 * pasados por argumento.
	 * 
	 * @param components
	 *            Component - Los Componentes.
	 */
	protected void focusComponents( Component... components ) {
		if ( null != components ) {
			Vector<Component> order = new Vector<Component>();
			for ( Component component: components ) {
				order.add( component );
			}
			setFocusTraversalPolicy( new WFocusTraversalPolicy( order ) );
		}
	}

	/**
	 * Componente de bloqueo de capas para Ventanas Modales.
	 * 
	 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
	 */
	class GlassPane extends JComponent {


		/**
		 * Serial UID.
		 */
		private static final long serialVersionUID = 1384830930135266170L;
		final Color TEXT_COLOR = new Color( 0x333333 );
		final Color BORDER_COLOR = new Color( 0x333333 );

		public GlassPane() {
			setBackground( Color.GRAY );
			setFont( new Font( "Default", Font.BOLD, 16 ) );

			addFocusListener( new FocusAdapter() {


				@Override
				public void focusGained( FocusEvent e ) {
					GlassPane.this.requestFocus();
				}

				@Override
				public void focusLost( FocusEvent e ) {
				}
			} );

			addMouseListener( new MouseListener() {


				@Override
				public void mouseClicked( MouseEvent e ) {
					e.consume();
				}

				@Override
				public void mouseEntered( MouseEvent e ) {
					e.consume();
				}

				@Override
				public void mouseExited( MouseEvent e ) {
					e.consume();
				}

				@Override
				public void mousePressed( MouseEvent e ) {
					e.consume();
				}

				@Override
				public void mouseReleased( MouseEvent e ) {
					e.consume();
				}
			} );
		}

		@Override
		protected void paintComponent( Graphics g ) {
			// enables anti-aliasing
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

			// gets the current clipping area
			Rectangle clip = g.getClipBounds();

			// sets a 65% translucent composite
			AlphaComposite alpha = AlphaComposite.SrcOver.derive( 0.65f );
			Composite composite = g2.getComposite();
			g2.setComposite( alpha );

			// fills the background
			g2.setColor( getBackground() );
			g2.fillRect( clip.x, clip.y, clip.width, clip.height );

			// draws the content of the progress bar
			Paint paint = g2.getPaint();

			g2.setPaint( paint );

			g2.setComposite( composite );
		}
	}

	@Override
	public boolean isSelected() {
		return Boolean.TRUE;
	}

	/**
	 * Agrega una Ventana Modal.
	 * 
	 * @param iframe
	 *            WAbstractIFrame - La instancia de la Ventana Modal.
	 */
	protected void addModalIFrame( WAbstractIFrame iframe ) {
		WApplication.getInstance().addModalIFrame( iframe );
	}

	/**
	 * Establece el cursor en Espera.
	 */
	protected void showBusyCursor() {
		getRootPane().setCursor( Cursor.getPredefinedCursor( Cursor.WAIT_CURSOR ) );
	}

	/**
	 * Establece el cursor Default.
	 */
	protected void showNormalCursor() {
		getRootPane().setCursor( Cursor.getPredefinedCursor( Cursor.DEFAULT_CURSOR ) );
	}

	protected void showGlobalErrorMsg( String errorMsg ) {
		WFrameUtils.showGlobalErrorMsg( errorMsg );
	}
	
	protected void showGlobalMsg( String msg ) {
		WFrameUtils.showGlobalMsg( msg );
	}

	protected void addKeyListener( KeyListener keyListener, JComponent... components ) {
		for ( JComponent jComponent: components ) {
			jComponent.addKeyListener( keyListener );
		}
	}

	public void enterPressed() {
	}
}
