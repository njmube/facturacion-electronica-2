package ar.com.wuik.classlife.frames;

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

import ar.com.dcode.afip.utils.AbstractFactory;
import ar.com.wuik.classlife.bo.ArticuloBO;
import ar.com.wuik.classlife.entities.Articulo;
import ar.com.wuik.classlife.exceptions.BusinessException;
import ar.com.wuik.classlife.model.ArticuloModel;
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


public class ArticuloIFrame extends WAbstractModelIFrame implements WSecure {


	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 7107533032732470914L;
	private static final Logger LOGGER = LoggerFactory.getLogger( ArticuloIFrame.class );
	private static final String CAMPO_CODIGO = "codigo";
	private static final String CAMPO_ARTICULO = "articulo";
	private JPanel pnlBusqueda;
	private JLabel lblCodigo;
	private JTextField txtCodigo;
	private JLabel lblArticulo;
	private JTextField txtArticulo;
	private JButton btnBuscar;
	private JButton btnLimpiar;
	private WTablePanel<Articulo> tablePanel;
	private JButton btnCerrar;

	/**
	 * Create the frame.
	 */
	public ArticuloIFrame() {
		setBorder( new LineBorder( null, 1, true ) );
		setTitle( "Art\u00EDculos" );
		setFrameIcon( new ImageIcon( ArticuloIFrame.class.getResource( "/icons/articulo.png" ) ) );
		setBounds( 0, 0, 788, 413 );
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
			pnlBusqueda.setBounds( 10, 11, 766, 90 );
			pnlBusqueda.setLayout( null );
			pnlBusqueda.add( getLblCodigo() );
			pnlBusqueda.add( getTxtCodigo() );
			pnlBusqueda.add( getLblArticulo() );
			pnlBusqueda.add( getTxtArticulo() );
			pnlBusqueda.add( getBtnLimpiar() );
			pnlBusqueda.add( getBtnBuscar() );
		}
		return pnlBusqueda;
	}

	private JLabel getLblCodigo() {
		if ( lblCodigo == null ) {
			lblCodigo = new JLabel( "C\u00F3digo:" );
			lblCodigo.setHorizontalAlignment( SwingConstants.RIGHT );
			lblCodigo.setBounds( 10, 24, 60, 20 );
		}
		return lblCodigo;
	}

	private JTextField getTxtCodigo() {
		if ( txtCodigo == null ) {
			txtCodigo = new JTextField();
			txtCodigo.setName( CAMPO_CODIGO );
			txtCodigo.setDocument( new WTextFieldLimit( 50 ) );
			txtCodigo.setBounds( 80, 24, 140, 20 );
			txtCodigo.setColumns( 10 );
		}
		return txtCodigo;
	}

	private JLabel getLblArticulo() {
		if ( lblArticulo == null ) {
			lblArticulo = new JLabel( "Art\u00EDculo:" );
			lblArticulo.setBounds( 234, 24, 72, 20 );
			lblArticulo.setHorizontalAlignment( SwingConstants.RIGHT );
		}
		return lblArticulo;
	}

	private JTextField getTxtArticulo() {
		if ( txtArticulo == null ) {
			txtArticulo = new JTextField();
			txtArticulo.setBounds( 316, 24, 230, 20 );
			txtArticulo.setName( CAMPO_ARTICULO );
			txtArticulo.setDocument( new WTextFieldLimit( 50 ) );
			txtArticulo.setColumns( 10 );
		}
		return txtArticulo;
	}

	private JButton getBtnBuscar() {
		if ( btnBuscar == null ) {
			btnBuscar = new JButton( "Buscar" );
			btnBuscar.setBounds( 568, 54, 89, 25 );
			btnBuscar.setFocusable( false );
			btnBuscar.addActionListener( new ActionListener() {


				public void actionPerformed( ActionEvent e ) {
					search();
				}
			} );
			btnBuscar.setIcon( new ImageIcon( ArticuloIFrame.class.getResource( "/icons/search.png" ) ) );
		}
		return btnBuscar;
	}

	private JButton getBtnLimpiar() {
		if ( btnLimpiar == null ) {
			btnLimpiar = new JButton( "Limpiar" );
			btnLimpiar.setBounds( 667, 54, 89, 25 );
			btnLimpiar.addActionListener( new ActionListener() {


				public void actionPerformed( ActionEvent e ) {
					reset();
				}
			} );
			btnLimpiar.setFocusable( false );
			btnLimpiar.setIcon( new ImageIcon( ArticuloIFrame.class.getResource( "/icons/clear.png" ) ) );
		}
		return btnLimpiar;
	}

	private WTablePanel<Articulo> getTablePanel() {
		if ( tablePanel == null ) {
			tablePanel = new WTablePanel( ArticuloModel.class, Boolean.FALSE );
			tablePanel.setBounds( 10, 103, 766, 247 );
			tablePanel.addToolbarButtons( getToolbarButtons() );
		}
		return tablePanel;
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		WToolbarButton buttonAdd =
		    new WToolbarButton( "Nuevo Artículo",
		        new ImageIcon( WCalendarIFrame.class.getResource( "/icons/add.png" ) ), new ActionListener() {


			        @Override
			        public void actionPerformed( ActionEvent e ) {
				        addModalIFrame( new ArticuloVerIFrame( null, ArticuloIFrame.this ) );
			        }
		        }, "Nuevo", null );
		WToolbarButton buttonEdit =
		    new WToolbarButton( "Editar Artículo",
		        new ImageIcon( WCalendarIFrame.class.getResource( "/icons/edit.png" ) ), new ActionListener() {


			        @Override
			        public void actionPerformed( ActionEvent e ) {
				        Long selectedItem = tablePanel.getSelectedItemID();
				        if ( null != tablePanel.getSelectedItemID() ) {
					        addModalIFrame( new ArticuloVerIFrame( selectedItem, ArticuloIFrame.this ) );
				        } else {
					        WTooltipUtils.showMessage( "Debe seleccionar un solo Item", (JButton) e.getSource(),
					            MessageType.ALERTA );
				        }
			        }
		        }, "Editar", null );
		WToolbarButton buttonDelete =
		    new WToolbarButton( "Eliminar Artículo(s)", new ImageIcon(
		        WCalendarIFrame.class.getResource( "/icons/delete.png" ) ), new ActionListener() {


			    @Override
			    public void actionPerformed( ActionEvent e ) {
				    List<Long> idsToDelete = tablePanel.getSelectedItemsID();
				    if ( WUtils.isNotEmpty( idsToDelete ) ) {
//					    try {
//						    int result =
//						        JOptionPane.showConfirmDialog( getParent(), "¿Desea eliminar el/los Item(s)?",
//						            "Alerta", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE );
//						    if ( result == JOptionPane.OK_OPTION ) {
//							    ArticuloBO articuloBO = AbstractFactory.getInstance( ArticuloBO.class );
//
//							    boolean usado = Boolean.FALSE;
//							    for ( Long idToDelete: idsToDelete ) {
//								    if ( articuloBO.isUtilizado( idToDelete ) ) {
//									    usado = Boolean.TRUE;
//								    }
//							    }
//
//							    if ( usado ) {
//								    WTooltipUtils.showMessage(
//								        "Alguno de los Artículos no puede ser eliminado porque está siendo utilizado",
//								        (WToolbarButton) e.getSource(), MessageType.ALERTA );
//							    } else {
//								    articuloBO.deleteByIDs( idsToDelete );
//								    search();
//							    }
//						    }
//					    }
//					    catch( BusinessException bexc ) {
//						    LOGGER.error( "Error al eliminar Articulos", bexc );
//						    WFrameUtils.showGlobalErrorMsg( "Se ha producido un error al eliminar Artículos" );
//					    }
				    } else {
					    WTooltipUtils.showMessage( "Debe seleccionar al menos un Item", (JButton) e.getSource(),
					        MessageType.ALERTA );
				    }
			    }
		    }, "Eliminar", null );

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
			btnCerrar.setIcon( new ImageIcon( ArticuloIFrame.class.getResource( "/icons/cancel.png" ) ) );
			btnCerrar.setBounds( 682, 350, 94, 25 );
		}
		return btnCerrar;
	}

	private void reset() {
		txtCodigo.setText( "" );
		txtArticulo.setText( "" );
		txtCodigo.requestFocus();
	}

	public void search() {
		ArticuloBO articuloBO = AbstractFactory.getInstance( ArticuloBO.class );

		List<Articulo> articulos = null;

		WModel model = populateModel();

//		ArticuloFilter filter = new ArticuloFilter();
//		String codigo = model.getValue( CAMPO_CODIGO );
//		filter.setCodigoBarra( codigo );
//		String articulo = model.getValue( CAMPO_ARTICULO );
//		filter.setNombre( articulo );
//		try {
//			articulos = articuloBO.search( filter );
//		}
//		catch( BusinessException bexc ) {
//			LOGGER.error( "Error al buscar Artículos", bexc );
//		}

		getTablePanel().addData( articulos );
	}

	@Override
	protected JComponent getFocusComponent() {
		return getTxtCodigo();
	}

	@Override
	public void enterPressed() {
		search();
	}
}
