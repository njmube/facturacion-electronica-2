package ar.com.wuik.sistema.frames.parametricos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.sistema.bo.UsuarioBO;
import ar.com.wuik.sistema.entities.Permisos;
import ar.com.wuik.sistema.entities.Usuario;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.UsuarioFilter;
import ar.com.wuik.sistema.frames.MainFrame;
import ar.com.wuik.sistema.model.UsuarioModel;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.components.security.WSecure;
import ar.com.wuik.swing.components.table.WTablePanel;
import ar.com.wuik.swing.components.table.WToolbarButton;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.frames.WCalendarIFrame;
import ar.com.wuik.swing.utils.WFrameUtils;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;


public class UsuarioIFrame extends WAbstractModelIFrame implements WSecure{


	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 7107533032732470914L;
	private static final Logger LOGGER = LoggerFactory.getLogger( UsuarioIFrame.class );
	private static final String CAMPO_NOMBRE = "nombre";
	private static final String CAMPO_DNI = "dni";
	private JPanel pnlBusqueda;

	private JButton btnBuscar;
	private JButton btnLimpiar;
	private JButton btnCerrar;

	private WTablePanel<Usuario> tablePanel;

	private JLabel lblNombre;
	private JLabel lblDNI;

	private JTextField txtNombre;
	private JTextField txtDNI;

	/**
	 * Create the frame.
	 */
	public UsuarioIFrame() {
		setBorder( new LineBorder( null, 1, true ) );
		setTitle( "Usuarios" );
		setFrameIcon( new ImageIcon( UsuarioIFrame.class.getResource( "/icons/usuarios.png" ) ) );
		setBounds( 0, 0, 555, 413 );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		getContentPane().setLayout( null );
		getContentPane().add( getPnlBusqueda() );
		getContentPane().add( getTablePanel() );
		getContentPane().add( getBtnCerrar() );
	}
	
	/**
	 * @see ar.com.wuik.swing.components.security.WSecure#applySecurity(java.util.List)
	 */
	@Override
	public void applySecurity( List<String> permisos ) {
		getTablePanel().applySecurity( permisos );
	}

	@Override
	protected boolean validateModel( WModel model ) {
		return false;
	}

	private JPanel getPnlBusqueda() {
		if ( pnlBusqueda == null ) {
			pnlBusqueda = new JPanel();
			pnlBusqueda.setBorder( new TitledBorder( null, "B\u00FAsqueda", TitledBorder.LEADING, TitledBorder.TOP,
			    null, null ) );
			pnlBusqueda.setBounds( 10, 11, 534, 90 );
			pnlBusqueda.setLayout( null );
			pnlBusqueda.add( getLblNombre() );
			pnlBusqueda.add( getLblDNI() );
			pnlBusqueda.add( getBtnLimpiar() );
			pnlBusqueda.add( getBtnBuscar() );
			pnlBusqueda.add( getTxtNombre() );
			pnlBusqueda.add( getTxtDNI() );
		}
		return pnlBusqueda;
	}

	private JLabel getLblNombre() {
		if ( lblNombre == null ) {
			lblNombre = new JLabel( "Nombre:" );
			lblNombre.setBounds( 10, 24, 72, 20 );
			lblNombre.setHorizontalAlignment( SwingConstants.RIGHT );
		}
		return lblNombre;
	}

	private JLabel getLblDNI() {
		if ( lblDNI == null ) {
			lblDNI = new JLabel( "Dni:" );
			lblDNI.setBounds( 276, 24, 31, 20 );
			lblDNI.setHorizontalAlignment( SwingConstants.RIGHT );
		}
		return lblDNI;
	}

	private JTextField getTxtNombre() {
		if ( txtNombre == null ) {
			txtNombre = new JTextField();
			txtNombre.setBounds( 92, 24, 171, 20 );
			txtNombre.setDocument( new WTextFieldLimit( 50 ) );
			txtNombre.setName( CAMPO_NOMBRE );
		}
		return txtNombre;
	}

	private JTextField getTxtDNI() {
		if ( txtDNI == null ) {
			txtDNI = new JTextField();
			txtDNI.setBounds( 317, 24, 162, 20 );
			txtDNI.setDocument( new WTextFieldLimit( 50 ) );
			txtDNI.setName( CAMPO_DNI );
		}
		return txtDNI;
	}

	private JButton getBtnBuscar() {
		if ( btnBuscar == null ) {
			btnBuscar = new JButton( "Buscar" );
			btnBuscar.setBounds( 334, 54, 89, 25 );
			btnBuscar.setFocusable( false );
			btnBuscar.addActionListener( new ActionListener() {


				public void actionPerformed( ActionEvent e ) {
					search();
				}
			} );
			btnBuscar.setIcon( new ImageIcon( UsuarioIFrame.class.getResource( "/icons/search.png" ) ) );
		}
		return btnBuscar;
	}

	private JButton getBtnLimpiar() {
		if ( btnLimpiar == null ) {
			btnLimpiar = new JButton( "Limpiar" );
			btnLimpiar.setBounds( 433, 54, 89, 25 );
			btnLimpiar.addActionListener( new ActionListener() {


				public void actionPerformed( ActionEvent e ) {
					reset();
				}
			} );
			btnLimpiar.setFocusable( false );
			btnLimpiar.setIcon( new ImageIcon( UsuarioIFrame.class.getResource( "/icons/clear.png" ) ) );
		}
		return btnLimpiar;
	}

	private WTablePanel<Usuario> getTablePanel() {
		if ( tablePanel == null ) {
			tablePanel = new WTablePanel( UsuarioModel.class, Boolean.FALSE );
			tablePanel.setBounds( 10, 103, 534, 247 );
			tablePanel.addToolbarButtons( getToolbarButtons() );
		}
		return tablePanel;
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		WToolbarButton buttonAdd =
		    new WToolbarButton( "Nuevo Usuario",
		        new ImageIcon( WCalendarIFrame.class.getResource( "/icons/add.png" ) ), new ActionListener() {


			        @Override
			        public void actionPerformed( ActionEvent e ) {
				        addModalIFrame( new UsuarioVerIFrame( null, UsuarioIFrame.this ) );
			        }
		        }, "Nuevo", Permisos.NUE_USU.getCodPermiso() );
		WToolbarButton buttonEdit =
		    new WToolbarButton( "Editar Usuario",
		        new ImageIcon( WCalendarIFrame.class.getResource( "/icons/edit.png" ) ), new ActionListener() {


			        @Override
			        public void actionPerformed( ActionEvent e ) {
				        Long selectedItem = tablePanel.getSelectedItemID();
				        if ( null != selectedItem ) {
					        addModalIFrame( new UsuarioVerIFrame( selectedItem, UsuarioIFrame.this ) );
				        } else {
					        WTooltipUtils.showMessage( "Debe seleccionar un solo Item", (JButton) e.getSource(),
					            MessageType.ALERTA );
				        }
			        }
		        }, "Editar", Permisos.EDI_USU.getCodPermiso() );
		WToolbarButton buttonDelete =
		    new WToolbarButton( "Eliminar Usuario(s)", new ImageIcon(
		        WCalendarIFrame.class.getResource( "/icons/delete.png" ) ), new ActionListener() {


			    @Override
			    public void actionPerformed( ActionEvent e ) {
				    List<Long> idsToDelete = tablePanel.getSelectedItemsID();
				    if ( WUtils.isNotEmpty( idsToDelete ) ) {
					    try {
						    int result =
						        JOptionPane.showConfirmDialog( getParent(), "¿Desea eliminar el/los Item(s)?",
						            "Alerta", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE );
						    if ( result == JOptionPane.OK_OPTION ) {
							    UsuarioBO usuarioBO = AbstractFactory.getInstance( UsuarioBO.class );
							    usuarioBO.deleteByIDs( idsToDelete );
							    search();
						    }
					    }
					    catch( BusinessException bexc ) {
						    LOGGER.error( "Error al eliminar Usuarios" );
						    WFrameUtils.showGlobalErrorMsg( "Se ha producido un error al eliminar Usuarios" );
					    }
				    } else {
					    WTooltipUtils.showMessage( "Debe seleccionar al menos un Item", (JButton) e.getSource(),
					        MessageType.ALERTA );
				    }
			    }
		    }, "Eliminar", Permisos.ELI_USU.getCodPermiso() );

		toolbarButtons.add( buttonAdd );
		toolbarButtons.add( buttonEdit );
		toolbarButtons.add( buttonDelete );
		return toolbarButtons;
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
			btnCerrar.setIcon( new ImageIcon( UsuarioIFrame.class.getResource( "/icons/cancel.png" ) ) );
			btnCerrar.setBounds( 450, 350, 94, 25 );
		}
		return btnCerrar;
	}

	private void reset() {
		txtNombre.setText( "" );
		txtNombre.requestFocus();
	}

	public void search() {
		UsuarioBO usuarioBO = AbstractFactory.getInstance( UsuarioBO.class );

		WModel model = populateModel();
		String nombreCompleto = model.getValue( CAMPO_NOMBRE );
		String dni = model.getValue( CAMPO_DNI );
		Long idToExclude = MainFrame.usuarioLogin.getId();

		List<Usuario> usuarios = null;

		UsuarioFilter filter = new UsuarioFilter();
		filter.setNombre( nombreCompleto );
		filter.setDni( dni );
		filter.setIdToExclude( idToExclude );
		try {
			usuarios = usuarioBO.search( filter );
		}
		catch( BusinessException bexc ) {
			LOGGER.error( "Error al buscar Usuarios", bexc );
		}

		getTablePanel().addData( usuarios );
	}

	@Override
	protected JComponent getFocusComponent() {
		return getTxtNombre();
	}

	@Override
	public void enterPressed() {
		search();
	}

}
