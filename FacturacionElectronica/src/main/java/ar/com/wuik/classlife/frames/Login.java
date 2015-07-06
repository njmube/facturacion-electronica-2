package ar.com.wuik.classlife.frames;

import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dcode.afip.utils.AppUtils;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.frames.WAbstractModelFrame;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;


public class Login extends WAbstractModelFrame {


	private static final String CAMPO_PASSWORD = "password";
	private static final String CAMPO_DNI = "dni";
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JLabel lblUsuario = null;
	private JLabel lblPassword = null;
	private JTextField txtUsuario = null;
	private JPasswordField txpPassword = null;
	private JButton btnAceptar = null;
	private JButton btnCerrar = null;

	protected static final Logger LOGGER = LoggerFactory.getLogger( Login.class );
	private JLabel lblAplicacion = null;
	private JLabel lblNewLabel;

	/**
	 * This method initializes txtUsuario
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getTxtUsuario() {
		if ( txtUsuario == null ) {
			txtUsuario = new JTextField();
			txtUsuario.setBounds( new Rectangle( 78, 8, 235, 25 ) );
			txtUsuario.setDocument( new WTextFieldLimit( 50 ) );
			txtUsuario.setName( CAMPO_DNI );
			txtUsuario.addKeyListener( new java.awt.event.KeyAdapter() {


				@Override
				public void keyPressed( KeyEvent e ) {
					if ( e.getKeyChar() == KeyEvent.VK_ENTER ) {
						getBtnAceptar().doClick();
					}
				}

				@Override
				public void keyTyped( java.awt.event.KeyEvent e ) {
					if ( ! Character.isDigit( e.getKeyChar() ) ) {
						e.consume();
					}
				}
			} );
		}
		return txtUsuario;
	}

	/**
	 * This method initializes txpPassword
	 * 
	 * @return javax.swing.JPasswordField
	 */
	private JPasswordField getTxpPassword() {
		if ( txpPassword == null ) {
			txpPassword = new JPasswordField();
			txpPassword.setBounds( new Rectangle( 78, 38, 235, 25 ) );
			txpPassword.setName( CAMPO_PASSWORD );
			txpPassword.setDocument( new WTextFieldLimit( 50 ) );
			txpPassword.addKeyListener( new java.awt.event.KeyAdapter() {


				@Override
				public void keyPressed( KeyEvent e ) {
					if ( e.getKeyChar() == KeyEvent.VK_ENTER ) {
						getBtnAceptar().doClick();
					}
				}
			} );
		}
		return txpPassword;
	}

	/**
	 * This method initializes btnAceptar
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnAceptar() {
		if ( btnAceptar == null ) {
			btnAceptar = new JButton();
			btnAceptar.setBounds( new Rectangle( 73, 79, 144, 30 ) );
			btnAceptar.setText( "Iniciar" );
			btnAceptar.addActionListener( new java.awt.event.ActionListener() {


				public void actionPerformed( java.awt.event.ActionEvent e ) {
					WModel model = populateModel();
					boolean valido = validateModel( model );
					if ( valido ) {

						String dni = model.getValue( CAMPO_DNI );
						String password = model.getValue( CAMPO_PASSWORD );


//						UsuarioBO usuarioBO = AbstractFactory.getInstance( UsuarioBO.class );
//						try {
//							List<Usuario> usuarios = usuarioBO.search( filter );
//							if ( WUtils.isNotEmpty( usuarios ) ) {
//								Usuario usuario = usuarios.get( 0 );
//								if ( usuario.isActivo() ) {
									MainFrame.main( null );
									setVisible( Boolean.FALSE );
									dispose();
//								} else {
//									WTooltipUtils.showMessage( "El usuario se encuentra inactivo", btnAceptar,
//									    MessageType.ERROR );
//								}
//							} else {
//								WTooltipUtils.showMessage( "El usuario o contrase�a no es v�lida", btnAceptar,
//								    MessageType.ERROR );
//							}
//						}
//						catch( BusinessException e1 ) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
					}
				}
			} );
		}
		return btnAceptar;
	}

	/**
	 * This method initializes btnCerrar
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnCerrar() {
		if ( btnCerrar == null ) {
			btnCerrar = new JButton();
			btnCerrar.setBounds( new Rectangle( 222, 79, 90, 30 ) );
			btnCerrar.setText( "Salir" );
			btnCerrar.addActionListener( new java.awt.event.ActionListener() {


				public void actionPerformed( java.awt.event.ActionEvent e ) {
					System.exit( 0 );
				}
			} );
		}
		return btnCerrar;
	}

	/**
	 * This is the default constructor
	 */
	public Login() {
		setIconImage( Toolkit.getDefaultToolkit().getImage( Login.class.getResource( "/icons32/login.png" ) ) );
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize( 328, 274 );
		this.setContentPane( getJContentPane() );
		this.setTitle( "Inicio de Sesi�n" );
		this.setResizable( Boolean.FALSE );
		this.setLocationRelativeTo( null );
		this.setAlwaysOnTop( Boolean.TRUE );
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if ( jContentPane == null ) {
			lblAplicacion = new JLabel();
			lblAplicacion.setBounds( new Rectangle(159, 209, 154, 26) );
			lblAplicacion.setText( "v1.0.0" );
			lblAplicacion.setHorizontalAlignment( JLabel.RIGHT );
			lblPassword = new JLabel();
			lblPassword.setBounds( new Rectangle( 6, 38, 68, 25 ) );
			lblPassword.setText( "Password:" );
			lblUsuario = new JLabel();
			lblUsuario.setBounds( new Rectangle( 6, 8, 69, 25 ) );
			lblUsuario.setText( "DNI:" );
			jContentPane = new JPanel();
			jContentPane.setLayout( null );
			jContentPane.add( lblUsuario, null );
			jContentPane.add( lblPassword, null );
			jContentPane.add( getTxtUsuario(), null );
			jContentPane.add( getTxpPassword(), null );
			jContentPane.add( getBtnAceptar(), null );
			jContentPane.add( getBtnCerrar(), null );
			jContentPane.add( lblAplicacion, null );
			jContentPane.add(getLblNewLabel());
		}
		return jContentPane;
	}

	/**
	 * @see ar.com.wuik.swing.frames.WAbstractModelFrame#validateModel(ar.com.wuik.swing.components.WModel)
	 */
	@Override
	protected boolean validateModel( WModel model ) {
		String dni = model.getValue( CAMPO_DNI );
		String password = model.getValue( CAMPO_PASSWORD );

		List<String> messages = new ArrayList<String>();

		if ( WUtils.isEmpty( dni ) ) {
			messages.add( "Debe ingresar un DNI" );
		}

		if ( WUtils.isEmpty( password ) ) {
			messages.add( "Debe ingresar un Password" );
		}

		WTooltipUtils.showMessages( messages, btnAceptar, MessageType.ERROR );

		return WUtils.isEmpty( messages );
	}

	public static void main( String[] args ) {
		SwingUtilities.invokeLater( new Runnable() {


			public void run() {
				JFrame.setDefaultLookAndFeelDecorated( true );
				JDialog.setDefaultLookAndFeelDecorated( true );
				Login loginFrame = new Login();
				loginFrame.showFrame();
			}
		} );
	}

	@Override
	protected JComponent getFocusComponent() {
		return getTxtUsuario();
	}
	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("");
			lblNewLabel.setIcon(new ImageIcon(Login.class.getResource("/reportes/logo_small2color.png")));
			lblNewLabel.setBounds(10, 129, 258, 86);
		}
		return lblNewLabel;
	}
}
