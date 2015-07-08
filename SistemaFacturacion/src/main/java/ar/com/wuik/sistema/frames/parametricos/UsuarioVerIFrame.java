package ar.com.wuik.sistema.frames.parametricos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceSkin;
import org.pushingpixels.substance.api.skin.SkinInfo;

import ar.com.wuik.sistema.bo.PermisoBO;
import ar.com.wuik.sistema.bo.UsuarioBO;
import ar.com.wuik.sistema.entities.Permiso;
import ar.com.wuik.sistema.entities.Permisos;
import ar.com.wuik.sistema.entities.Usuario;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.frames.MainFrame;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WOption;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.components.WTextFieldNumeric;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;


public class UsuarioVerIFrame extends WAbstractModelIFrame {


	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = - 6838619883125511589L;
	private JPanel pnlDatos;

	private JLabel lblDNI;
	private JLabel lblNombre;
	private JLabel lblPermisos;
	private JTextField txtNombre;
	private JButton btnGuardar;
	private JButton btnCerrar;
	private Usuario usuario;
	private JCheckBox chbActivo;
	private static final String CAMPO_NOMBRE = "nombre";
	private static final String CAMPO_DNI = "dni";
	private static final String CAMPO_PASSWORD = "password";
	private static final String CAMPO_SHOW_PASSWORD = "showPassword";
	private static final String CAMPO_ACTIVO = "activo";
	private static final String CAMPO_PERMISOS = "permisos";
	private UsuarioIFrame usuarioIFrame;
	private WTextFieldNumeric txtDNI;
	private JLabel lblPassword;
	private JPasswordField txtPassword;
	private JList lstPermisos;
	private JScrollPane scrLstPermisos;
	private JTextField txtShowPassword;
	private JCheckBox chckbxMostrarPassword;
	private JComboBox comboBox;

	/**
	 * Create the frame.
	 */
	public UsuarioVerIFrame(Long idUsuario, UsuarioIFrame usuarioIFrame) {
		this.usuarioIFrame = usuarioIFrame;
		setBorder( new LineBorder( null, 1, true ) );
		setTitle( "Usuarios" );
		setFrameIcon( new ImageIcon( UsuarioVerIFrame.class.getResource( "/icons/usuarios.png" ) ) );
		setBounds( 0, 0, 521, 301 );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		getContentPane().setLayout( null );
		getContentPane().add( getPnlDatos() );
		getContentPane().add( getBtnCerrar() );
		getContentPane().add( getBtnGuardar() );
		getContentPane().add(getComboBox());
		loadPermisos();
		if ( ! MainFrame.getInstance().getPermisos().contains( Permisos.EDI_USU.getCodPermiso() ) ) {
			getScrLstPermisos().setVisible( Boolean.FALSE );
			getLblPermisos().setVisible( Boolean.FALSE );
			pnlDatos.setBounds( 10, 11, 275, 216 );
			setBounds( 0, 0, 296, 301 );
			getBtnCerrar().setBounds( 85, 198, 94, 25 );
			getBtnGuardar().setBounds( 184, 198, 94, 25 );
		}

		if ( null == idUsuario ) {
			usuario = new Usuario();
			usuario.setPermisos( new HashSet<Permiso>() );
			setTitle( "Nuevo Usuario" );
		} else {
			setTitle( "Editar Usuario" );
			UsuarioBO usuarioBO = AbstractFactory.getInstance( UsuarioBO.class );
			try {
				usuario = usuarioBO.getById( idUsuario );
				WModel model = populateModel();
				model.addValue( CAMPO_ACTIVO, usuario.isActivo() );
				model.addValue( CAMPO_DNI, usuario.getDni() );
				model.addValue( CAMPO_NOMBRE, usuario.getNombre() );
				model.addValue( CAMPO_PASSWORD, usuario.getPassword() );
				model.addValue( CAMPO_SHOW_PASSWORD, usuario.getPassword() );
				model.addValue( CAMPO_PERMISOS, getIdsPermisos( usuario.getPermisos() ) );
				populateComponents( model );
			}
			catch( BusinessException bexc ) {
				showGlobalErrorMsg(bexc.getMessage());
			}
		}
	}

	private Object getIdsPermisos( Set<Permiso> permisos ) {
		List<Long> idsPermisos = new ArrayList<Long>();
		if ( WUtils.isNotEmpty( permisos ) ) {
			for ( Permiso permiso: permisos ) {
				idsPermisos.add( permiso.getId() );
			}
		}
		return idsPermisos;
	}

	/**
	 * //TODO: Describir el metodo loadPermisos
	 * 
	 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
	 */
	private void loadPermisos() {
		try {

			PermisoBO permisoBO = AbstractFactory.getInstance( PermisoBO.class );

			final DefaultListModel model = new DefaultListModel();
			List<Permiso> permisos = permisoBO.getAll();
			for ( Permiso permiso: permisos ) {
				WOption option = new WOption( permiso.getId(), permiso.getDescripcion() );
				model.addElement( option );
			}
			lstPermisos.setModel( model );
		}
		catch( BusinessException bexc ) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	@Override
	protected boolean validateModel( WModel model ) {

		String nombre = model.getValue( CAMPO_NOMBRE );
		String dni = model.getValue( CAMPO_DNI );
		String password = model.getValue( CAMPO_PASSWORD );

		List<String> messages = new ArrayList<String>();

		if ( WUtils.isEmpty( nombre ) ) {
			messages.add( "Debe ingresar un Nombre" );
		}
		
		if ( WUtils.isEmpty( dni ) ) {
			messages.add( "Debe ingresar un DNI" );
		}
		
		if ( WUtils.isEmpty( password ) ) {
			messages.add( "Debe ingresar un Password" );
		}

		WTooltipUtils.showMessages( messages, btnGuardar, MessageType.ERROR );

		return WUtils.isEmpty( messages );
	}

	private JPanel getPnlDatos() {
		if ( pnlDatos == null ) {
			pnlDatos = new JPanel();
			pnlDatos.setBorder( new TitledBorder( UIManager.getBorder( "TitledBorder.border" ), "Datos",
			    TitledBorder.LEADING, TitledBorder.TOP, null, null ) );
			pnlDatos.setBounds( 10, 11, 496, 216 );
			pnlDatos.setLayout( null );
			pnlDatos.add( getLblDNI() );
			pnlDatos.add( getLblNombre() );
			pnlDatos.add( getTxtNombre() );
			pnlDatos.add( getChbActivo() );
			pnlDatos.add( getTxtDNI() );
			pnlDatos.add( getLblPassword() );
			pnlDatos.add( getTxtPassword() );
			pnlDatos.add( getLblPermisos() );
			pnlDatos.add( getScrLstPermisos() );
			pnlDatos.add(getTxtShowPassword());
			pnlDatos.add(getChckbxMostrarPassword());

		}
		return pnlDatos;
	}

	private JLabel getLblDNI() {
		if ( lblDNI == null ) {
			lblDNI = new JLabel( "* DNI:" );
			lblDNI.setHorizontalAlignment( SwingConstants.RIGHT );
			lblDNI.setBounds( 9, 59, 72, 25 );
		}
		return lblDNI;
	}

	private JLabel getLblNombre() {
		if ( lblNombre == null ) {
			lblNombre = new JLabel( "* Nombre:" );
			lblNombre.setHorizontalAlignment( SwingConstants.RIGHT );
			lblNombre.setBounds( 10, 23, 71, 25 );
		}
		return lblNombre;
	}

	private JLabel getLblPermisos() {
		if ( lblPermisos == null ) {
			lblPermisos = new JLabel( "* Permisos:" );
			lblPermisos.setHorizontalAlignment( SwingConstants.RIGHT );
			lblPermisos.setBounds( 180, 183, 75, 22 );
		}
		return lblPermisos;
	}

	private JTextField getTxtNombre() {
		if ( txtNombre == null ) {
			txtNombre = new JTextField();
			txtNombre.setName( CAMPO_NOMBRE );
			txtNombre.setDocument( new WTextFieldLimit( 50 ) );
			txtNombre.setBounds( 91, 23, 164, 25 );
		}
		return txtNombre;
	}

	private JButton getBtnCerrar() {
		if ( btnCerrar == null ) {
			btnCerrar = new JButton( "Cerrar" );
			btnCerrar.setFocusable( false );
			btnCerrar.addActionListener( new ActionListener() {


				public void actionPerformed( ActionEvent e ) {
					hideFrame();
				}
			} );
			btnCerrar.setIcon( new ImageIcon( UsuarioVerIFrame.class.getResource( "/icons/cancel.png" ) ) );
			btnCerrar.setBounds( 313, 238, 94, 25 );
		}
		return btnCerrar;
	}

	private JButton getBtnGuardar() {
		if ( btnGuardar == null ) {
			btnGuardar = new JButton( "Guardar" );
			btnGuardar.setIcon( new ImageIcon( UsuarioVerIFrame.class.getResource( "/icons/ok.png" ) ) );
			btnGuardar.setBounds( 412, 238, 94, 25 );
			btnGuardar.addActionListener( new ActionListener() {


				public void actionPerformed( ActionEvent e ) {
					WModel model = populateModel();
					if ( validateModel( model ) ) {

						UsuarioBO usuarioBO = AbstractFactory.getInstance( UsuarioBO.class );
						PermisoBO permisoBO = AbstractFactory.getInstance( PermisoBO.class );

						try {
							Boolean activo = model.getValue( CAMPO_ACTIVO );
							String nombre = model.getValue( CAMPO_NOMBRE );
							String dni = model.getValue( CAMPO_DNI );
							String password = model.getValue( CAMPO_PASSWORD );
							Object[] options = model.getValue( CAMPO_PERMISOS );
							
							boolean existeUsuario = usuarioBO.existeUsuario( dni, usuario.getId() );
							if ( existeUsuario ) {
								WTooltipUtils.showMessage( "El DNI ingresado existe para otro Usuario", btnGuardar, MessageType.ERROR );
								return;
							}
								
							usuario.setNombre( nombre );
							usuario.setDni( dni );
							usuario.setActivo( activo );
							usuario.setPassword( password );

							if ( getScrLstPermisos().isVisible() ) {

								usuario.getPermisos().clear();

								WOption option = null;
								for ( int i = 0; i < options.length; i++ ) {
									option = (WOption) options[i];
									Permiso permiso = permisoBO.getById( option.getValue() );
									usuario.getPermisos().add( permiso );
								}
							}

							usuarioBO.saveOrUpdate( usuario );
							hideFrame();
							if ( null != usuarioIFrame ) {
								usuarioIFrame.search();
							} else {
								// LOGOUT
								( (MainFrame) MainFrame.getInstance() ).logout( Boolean.FALSE );
							}
						}
						catch( BusinessException bexc ) {
							showGlobalErrorMsg(bexc.getMessage());
						}
					}
				}
			} );
		}
		return btnGuardar;
	}

	@Override
	protected JComponent getFocusComponent() {
		return getTxtNombre();
	}

	private JCheckBox getChbActivo() {
		if ( chbActivo == null ) {
			chbActivo = new JCheckBox( "Activo" );
			chbActivo.setBounds( 91, 161, 97, 23 );
			chbActivo.setName( CAMPO_ACTIVO );
			chbActivo.setSelected( Boolean.TRUE );
		}
		return chbActivo;
	}

	@Override
	public void enterPressed() {
		getBtnGuardar().doClick();
	}

	private WTextFieldNumeric getTxtDNI() {
		if ( txtDNI == null ) {
			txtDNI = new WTextFieldNumeric();
			txtDNI.setBounds( 91, 59, 164, 25 );
			txtDNI.setName( CAMPO_DNI );
			txtDNI.setDocument( new WTextFieldLimit( 45 ) );
		}
		return txtDNI;
	}

	private JLabel getLblPassword() {
		if ( lblPassword == null ) {
			lblPassword = new JLabel( "* Password:" );
			lblPassword.setHorizontalAlignment( SwingConstants.RIGHT );
			lblPassword.setBounds( 9, 91, 72, 25 );
		}
		return lblPassword;
	}

	private JPasswordField getTxtPassword() {
		if ( txtPassword == null ) {
			txtPassword = new JPasswordField();
			txtPassword.setName( CAMPO_PASSWORD );
			txtPassword.setBounds( 91, 91, 164, 25 );
			txtPassword.setDocument( new WTextFieldLimit( 45 ) );
			txtPassword.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					txtShowPassword.setText(new String(txtPassword.getPassword()));
				}
			});
		}
		return txtPassword;
	}

	private JList getLstPermisos() {
		if ( lstPermisos == null ) {
			lstPermisos = new JList();
		}
		return lstPermisos;
	}

	private JScrollPane getScrLstPermisos() {
		if ( scrLstPermisos == null ) {
			scrLstPermisos = new JScrollPane();
			scrLstPermisos.setBounds( 265, 21, 221, 184 );
			scrLstPermisos.setName( CAMPO_PERMISOS );
			scrLstPermisos.setViewportView( getLstPermisos() );
		}
		return scrLstPermisos;
	}
	private JTextField getTxtShowPassword() {
		if (txtShowPassword == null) {
			txtShowPassword = new JTextField();
			txtShowPassword.setBounds(91, 91, 164, 25);
			txtShowPassword.setColumns(10);
			txtShowPassword.setName( CAMPO_SHOW_PASSWORD );
			txtShowPassword.setVisible(Boolean.FALSE);
			txtShowPassword.setEditable( Boolean.FALSE );
		}
		return txtShowPassword;
	}
	private JCheckBox getChckbxMostrarPassword() {
		if (chckbxMostrarPassword == null) {
			chckbxMostrarPassword = new JCheckBox("Mostrar Password");
			chckbxMostrarPassword.addItemListener(new java.awt.event.ItemListener() {
				public void itemStateChanged(java.awt.event.ItemEvent e) {
					txtShowPassword.setVisible(chckbxMostrarPassword
							.isSelected());
					txtPassword.setVisible(!chckbxMostrarPassword
							.isSelected());
				}
			});
			chckbxMostrarPassword.setBounds(91, 123, 143, 23);
		}
		return chckbxMostrarPassword;
	}
	
	private JComboBox getComboBox() {
		if (comboBox == null) {
			comboBox = new JComboBox(getSkins().toArray());
			comboBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					final Skin s = (Skin)e.getItem();
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {

							try {
								SubstanceLookAndFeel.setSkin( (SubstanceSkin) Class.forName(s.skinInfo.getClassName()).newInstance());
							}
							catch( Exception e ) {
								System.out.println( "Substance Skin failed to initialize" );
							}

							
						}
					});
				}
			});
			comboBox.setBounds(10, 238, 159, 20);
		}
		return comboBox;
	}

	private List<Skin> getSkins() {
		List<Skin> skins = new ArrayList<UsuarioVerIFrame.Skin>();
		Map<String, SkinInfo> mapa = SubstanceLookAndFeel.getAllSkins();
		Set<String> keys = mapa.keySet();
		for (String key : keys) {
			Skin s = new Skin();
			s.name = key;
			s.skinInfo = mapa.get(key);
			skins.add(s);
		}
		return skins;
	}

	class Skin {
		public SkinInfo skinInfo;
		public String name;

		@Override
		public String toString() {
			return name;
		}
	}
}
