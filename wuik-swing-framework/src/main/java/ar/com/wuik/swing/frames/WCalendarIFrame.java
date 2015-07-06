package ar.com.wuik.swing.frames;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import ar.com.wuik.swing.events.CalendarSelectedEvent;
import ar.com.wuik.swing.utils.WUtils;


/**
 * Componente Calendario.
 * 
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public class WCalendarIFrame extends WAbstractIFrame {


	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1811337764816169998L;
	private JPanel jContentPane = null;
	private JLabel lblLunes = null;
	private JLabel lblMartes = null;
	private JLabel lblMiercoles = null;
	private JLabel lblJueves = null;
	private JLabel lblViernes = null;
	private JLabel lblSabado = null;
	private JLabel lblDomingo = null;
	private JButton btnAnioAtras = null;
	private JButton btnMesAtras = null;
	private Calendar calendar = null;
	private Map<Integer, JButton> mapaBtnDia = null; // @jve:decl-index=0:
	private JButton btnMesAdelante = null;
	private JButton btnAnioAdelante = null;
	private CalendarSelectedEvent event;
	private JButton btnHoy = null;
	private JSeparator separadorDias = null;
	private Calendar calendarHoy = null;
	private JButton btnBorrar = null;
	private JButton btnCerrar = null;

	/**
	 * This is the xxx default constructor
	 * 
	 * @wbp.parser.constructor
	 */
	public WCalendarIFrame(Date fecha, CalendarSelectedEvent event) {
		initialize();
		this.event = event;
		if ( null == fecha ) {
			fecha = new Date();
		}
		setTitle( title );
		initializeCalendar();
		calendar = Calendar.getInstance();
		calendarHoy = Calendar.getInstance();
		calendarHoy.setTime( fecha );
		calendar.setTime( fecha );
		setDate();
	}

	public WCalendarIFrame(final JTextField txtFecha) {
		initialize();
		this.event = new CalendarSelectedEvent() {


			@Override
			public void execute( Date selectedFecha ) {
				if ( null != selectedFecha ) {
					txtFecha.setText( WUtils.getStringFromDate( selectedFecha ) );
				} else {
					txtFecha.setText( WUtils.EMPTY );
				}
			}
		};

		Date fecha = WUtils.getDateFromString( txtFecha.getText() );

		if ( null == fecha ) {
			fecha = new Date();
		}
		setTitle( title );
		initializeCalendar();
		calendar = Calendar.getInstance();
		calendarHoy = Calendar.getInstance();
		calendarHoy.setTime( fecha );
		calendar.setTime( fecha );
		setDate();
	}

	private void initializeCalendar() {
		mapaBtnDia = new HashMap<Integer, JButton>();
		int y = 65;
		int yInc = 30;
		int xInc = 40;
		int index = 0;
		for ( int i = 1; i <= 6; i++ ) {
			for ( int j = 0; j < 7; j++ ) {
				final JButton btn = new JButton();
				btn.setSize( 34, 25 );
				btn.setName( "btnDia" );
				btn.setLocation( ( xInc * j ) + 5, y );
				btn.addActionListener( new ActionListener() {


					@Override
					public void actionPerformed( ActionEvent e ) {
						String texto = btn.getText();
						texto =
						    texto.replace( "<html>", "" ).replace( "</html>", "" ).replace( "<u>", "" )
						        .replace( "</u>", "" ).replace( "<b>", "" ).replace( "</b>", "" );
						int dia = Integer.valueOf( texto );
						calendar.set( Calendar.DATE, dia );
						hideFrame();
						event.execute( calendar.getTime() );
					}
				} );
				mapaBtnDia.put( index, btn );
				getContentPane().add( btn );
				index++;
			}
			y += yInc;
		}
	}

	private void setDate() {
		hideButtons();

		int diaActual = calendarHoy.get( Calendar.DATE );
		int mesActual = calendarHoy.get( Calendar.MONTH );
		int anioActual = calendarHoy.get( Calendar.YEAR );

		int maxDate = calendar.getActualMaximum( Calendar.DATE );
		int minDate = calendar.getActualMinimum( Calendar.DATE );

		// Establezo el primer dia del Mes.
		calendar.set( Calendar.DATE, minDate );
		DiaEnum diaEnum = DiaEnum.fromId( calendar.get( Calendar.DAY_OF_WEEK ) );

		int indexBtn = diaEnum.ordinal();

		JButton button = null;
		for ( int i = minDate; i <= maxDate; i++ ) {
			button = mapaBtnDia.get( indexBtn );
			button.setVisible( Boolean.TRUE );
			calendar.set( Calendar.DATE, i );

			diaEnum = DiaEnum.fromId( calendar.get( Calendar.DAY_OF_WEEK ) );
			if ( diaActual == i && calendar.get( Calendar.MONTH ) == mesActual
			    && calendar.get( Calendar.YEAR ) == anioActual ) {
				button.setBorder( BorderFactory.createBevelBorder( BevelBorder.RAISED, Color.BLUE, Color.LIGHT_GRAY ) );
				button.setText( "<html><u><b>" + i + "</b></u></html>" );
			} else {
				button.setText( "<html><u>" + i + "</u></html>" );
			}
			button.setCursor( new Cursor( Cursor.HAND_CURSOR ) );

			indexBtn++;
		}
		showFecha();
	}

	private void hideButtons() {
		int compCount = getContentPane().getComponentCount();
		Component comp = null;
		JButton btn = null;
		String name = null;
		for ( int i = 0; i < compCount; i++ ) {
			comp = getContentPane().getComponent( i );
			if ( comp instanceof JButton ) {
				name = ( (JButton) comp ).getName();
				btn = (JButton) comp;
				if ( WUtils.isNotEmpty( name ) && name.startsWith( "btnDia" ) ) {
					btn.setVisible( Boolean.FALSE );
					btn.setText( "" );
					btn.setBorder( null );
				}
			}
		}
	}

	private enum DiaEnum {
		LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO;


		public static DiaEnum fromId( int id ) {
			switch( id ){
				case Calendar.MONDAY:
					return LUNES;
				case Calendar.TUESDAY:
					return MARTES;
				case Calendar.WEDNESDAY:
					return MIERCOLES;
				case Calendar.THURSDAY:
					return JUEVES;
				case Calendar.FRIDAY:
					return VIERNES;
				case Calendar.SATURDAY:
					return SABADO;
				case Calendar.SUNDAY:
					return DOMINGO;
			}
			return null;
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize( 296, 311 );
		this.setContentPane( getJContentPane() );
		this.setFrameIcon( new ImageIcon( WCalendarIFrame.class.getResource( "/icons/calendar.png" ) ) );

	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if ( jContentPane == null ) {
			lblDomingo = new JLabel();
			lblDomingo.setBounds( new Rectangle( 244, 32, 34, 25 ) );
			lblDomingo.setText( "Dom" );
			lblDomingo.setHorizontalAlignment( SwingConstants.CENTER );
			lblDomingo.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED ) );
			lblSabado = new JLabel();
			lblSabado.setBounds( new Rectangle( 204, 32, 34, 25 ) );
			lblSabado.setText( "Sáb" );
			lblSabado.setHorizontalAlignment( SwingConstants.CENTER );
			lblSabado.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED ) );
			lblViernes = new JLabel();
			lblViernes.setBounds( new Rectangle( 164, 32, 34, 25 ) );
			lblViernes.setText( "Vie" );
			lblViernes.setHorizontalAlignment( SwingConstants.CENTER );
			lblViernes.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED ) );
			lblJueves = new JLabel();
			lblJueves.setBounds( new Rectangle( 124, 32, 34, 25 ) );
			lblJueves.setText( "Jue" );
			lblJueves.setHorizontalAlignment( SwingConstants.CENTER );
			lblJueves.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED ) );
			lblMiercoles = new JLabel();
			lblMiercoles.setBounds( new Rectangle( 84, 32, 34, 25 ) );
			lblMiercoles.setText( "Mie" );
			lblMiercoles.setHorizontalAlignment( SwingConstants.CENTER );
			lblMiercoles.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED ) );
			lblMartes = new JLabel();
			lblMartes.setBounds( new Rectangle( 44, 32, 34, 25 ) );
			lblMartes.setText( "Mar" );
			lblMartes.setHorizontalAlignment( SwingConstants.CENTER );
			lblMartes.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED ) );
			lblLunes = new JLabel();
			lblLunes.setBounds( new Rectangle( 4, 32, 34, 25 ) );
			lblLunes.setText( "Lun" );
			lblLunes.setHorizontalAlignment( SwingConstants.CENTER );
			lblLunes.setBorder( BorderFactory.createBevelBorder( BevelBorder.LOWERED ) );
			separadorDias = new JSeparator();
			separadorDias.setBounds( 0, 60, 285, 1 );
			jContentPane = new JPanel();
			jContentPane.setLayout( null );
			jContentPane.add( lblLunes, null );
			jContentPane.add( lblMartes, null );
			jContentPane.add( lblMiercoles, null );
			jContentPane.add( lblJueves, null );
			jContentPane.add( lblViernes, null );
			jContentPane.add( lblSabado, null );
			jContentPane.add( lblDomingo, null );
			jContentPane.add( separadorDias, null );
			jContentPane.add( getBtnAnioAtras(), null );
			jContentPane.add( getBtnMesAtras(), null );
			jContentPane.add( getBtnMesAdelante(), null );
			jContentPane.add( getBtnAnioAdelante(), null );
			jContentPane.add( getBtnHoy(), null );
			jContentPane.add( getBtnBorrar(), null );
			jContentPane.add( getBtnCerrar(), null );
		}
		return jContentPane;
	}

	/**
	 * This method initializes btnAnioAtras
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnAnioAtras() {
		if ( btnAnioAtras == null ) {
			btnAnioAtras = new JButton();
			btnAnioAtras.setBounds( new Rectangle( 5, 4, 25, 25 ) );
			btnAnioAtras.setIcon( new ImageIcon( WCalendarIFrame.class.getResource( "/icons/primero.png" ) ) );
			btnAnioAtras.setToolTipText( "Año Anterior" );
			btnAnioAtras.addActionListener( new java.awt.event.ActionListener() {


				public void actionPerformed( java.awt.event.ActionEvent e ) {
					calendar.roll( Calendar.YEAR, Boolean.FALSE );
					setDate();
				}
			} );
		}
		return btnAnioAtras;
	}

	private void showFecha() {
		setTitle( WUtils.capitalize( WUtils.getStringFromDate( calendar.getTime(), "MMMMM 'del' yyyy" ) ) );
	}

	/**
	 * This method initializes btnMesAtras
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnMesAtras() {
		if ( btnMesAtras == null ) {
			btnMesAtras = new JButton();
			btnMesAtras.setIcon( new ImageIcon( WCalendarIFrame.class.getResource( "/icons/anterior.png" ) ) );
			btnMesAtras.setBounds( new Rectangle( 33, 4, 25, 25 ) );
			btnMesAtras.setToolTipText( "Mes Anterior" );
			btnMesAtras.addActionListener( new java.awt.event.ActionListener() {


				public void actionPerformed( java.awt.event.ActionEvent e ) {
					calendar.add( Calendar.MONTH, - 1 );
					setDate();
				}
			} );
		}
		return btnMesAtras;
	}

	/**
	 * This method initializes btnMesAdelante
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnMesAdelante() {
		if ( btnMesAdelante == null ) {
			btnMesAdelante = new JButton();
			btnMesAdelante.setBounds( new Rectangle( 228, 4, 25, 25 ) );
			btnMesAdelante.setIcon( new ImageIcon( WCalendarIFrame.class.getResource( "/icons/proximo.png" ) ) );
			btnMesAdelante.setToolTipText( "Próximo Mes" );
			btnMesAdelante.addActionListener( new java.awt.event.ActionListener() {


				public void actionPerformed( java.awt.event.ActionEvent e ) {
					calendar.add( Calendar.MONTH, 1 );
					setDate();
				}
			} );
		}
		return btnMesAdelante;
	}

	/**
	 * This method initializes btnAnioAdelante
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnAnioAdelante() {
		if ( btnAnioAdelante == null ) {
			btnAnioAdelante = new JButton();
			btnAnioAdelante.setBounds( new Rectangle( 255, 4, 25, 25 ) );
			btnAnioAdelante.setIcon( new ImageIcon( WCalendarIFrame.class.getResource( "/icons/ultimo.png" ) ) );
			btnAnioAdelante.setToolTipText( "Próximo Año" );
			btnAnioAdelante.addActionListener( new java.awt.event.ActionListener() {


				public void actionPerformed( java.awt.event.ActionEvent e ) {
					calendar.roll( Calendar.YEAR, Boolean.TRUE );
					setDate();
				}
			} );
		}
		return btnAnioAdelante;
	}

	/**
	 * This method initializes btnHoy
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnHoy() {
		if ( btnHoy == null ) {
			final Date hoy = new Date();
			String tokenFecha1 = WUtils.capitalize( WUtils.getStringFromDate( hoy, "EEE, dd 'de' " ) );
			String tokenFecha2 = WUtils.capitalize( WUtils.getStringFromDate( hoy, "MMM 'de' yyyy" ) );

			btnHoy = new JButton( tokenFecha1 + tokenFecha2 );
			btnHoy.setBounds( new Rectangle( 59, 4, 167, 25 ) );
			btnHoy.addActionListener( new java.awt.event.ActionListener() {


				public void actionPerformed( java.awt.event.ActionEvent e ) {
					hideFrame();
					event.execute( hoy );
				}
			} );
		}
		return btnHoy;
	}

	/**
	 * This method initializes btnBorrar
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnBorrar() {
		if ( btnBorrar == null ) {
			btnBorrar = new JButton( "Borrar" );
			btnBorrar.setBounds( new Rectangle( 6, 249, 125, 24 ) );
			btnBorrar.setIcon( new ImageIcon( WCalendarIFrame.class.getResource( "/icons/clear.png" ) ) );
			btnBorrar.addActionListener( new java.awt.event.ActionListener() {


				public void actionPerformed( java.awt.event.ActionEvent e ) {
					hideFrame();
					event.execute( null );
				}
			} );
		}
		return btnBorrar;
	}

	/**
	 * This method initializes btnCerrar
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnCerrar() {
		if ( btnCerrar == null ) {
			btnCerrar = new JButton();
			btnCerrar.setBounds( new Rectangle( 153, 249, 126, 25 ) );
			btnCerrar.setIcon( new ImageIcon( WCalendarIFrame.class.getResource( "/icons/cancel.png" ) ) );
			btnCerrar.setText( "Cerrar" );
			btnCerrar.addActionListener( new java.awt.event.ActionListener() {


				public void actionPerformed( java.awt.event.ActionEvent e ) {
					hideFrame();
				}
			} );
		}
		return btnCerrar;
	}

} // @jve:decl-index=0:visual-constraint="10,10"
