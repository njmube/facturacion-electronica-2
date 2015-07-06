package ar.com.wuik.classlife.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.dcode.afip.utils.AbstractFactory;
import ar.com.wuik.classlife.bo.ArticuloBO;
import ar.com.wuik.classlife.entities.Articulo;
import ar.com.wuik.classlife.exceptions.BusinessException;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WOption;
import ar.com.wuik.swing.components.WTextFieldDecimal;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.frames.WGenericSelectorIFrame;
import ar.com.wuik.swing.listeners.WGenericSelectorListener;
import ar.com.wuik.swing.utils.WFrameUtils;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;


public class ArticuloVerIFrame extends WAbstractModelIFrame {


	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = - 6838619883125511589L;
	private static final Logger LOGGER = LoggerFactory.getLogger( ArticuloVerIFrame.class );
	private JPanel pnlBusqueda;
	private JLabel lblCodigo;
	private JTextField txtCodigo;
	private JLabel lblNombre;
	private JTextField txtArticulo;
	private JButton btnCerrar;
	private Articulo articulo;
	private JButton btnGuardar;
	private JLabel lblTalle;
	private JComboBox cmbTalle;
	private JLabel lblColor;
	private JSpinner spnStock;
	private JLabel lblStock;
	private WTextFieldDecimal txfPublico;
	private JCheckBox chbActivo;
	private JLabel lblPrecioPublico;

	private static final String CAMPO_ARTICULO = "articulo";
	private static final String CAMPO_CODIGO = "codigo";
	private static final String CAMPO_CODIGO_ART = "codigoArticulo";
	private static final String CAMPO_TALLE = "talle";
	private static final String CAMPO_COLOR = "color";
	private static final String CAMPO_STOCK = "stock";
	private static final String CAMPO_PUBLICO = "publico";
	private static final String CAMPO_ACTIVO = "activo";
	private JComboBox cmbColor;
	private JButton btnNuevoColor;
	private ArticuloIFrame articuloIFrame;
	private JLabel lblCdigoArt;
	private JTextField txtCodigoArt;

	/**
	 * Create the frame.
	 */
	public ArticuloVerIFrame(Long idArticulo, ArticuloIFrame articuloIFrame) {
		this.articuloIFrame = articuloIFrame;
		setBorder( new LineBorder( null, 1, true ) );
		setFrameIcon( new ImageIcon( ArticuloVerIFrame.class.getResource( "/icons/articulo.png" ) ) );
		setBounds( 0, 0, 375, 355 );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		getContentPane().setLayout( null );
		getContentPane().add( getPnlBusqueda() );
		getContentPane().add( getBtnCerrar() );
		getContentPane().add( getBtnGuardar() );

		if ( null == idArticulo ) {
			articulo = new Articulo();
			setTitle( "Nuevo Artículo" );
		} else {
			setTitle( "Editar Artículo" );
			ArticuloBO articuloBO = AbstractFactory.getInstance( ArticuloBO.class );
			try {
				articulo = articuloBO.getById( idArticulo );
				WModel model = populateModel();
				model.addValue( CAMPO_ARTICULO, articulo.getNombre() );
				model.addValue( CAMPO_CODIGO, articulo.getCodigoBarras() );
				populateComponents( model );
			}
			catch( BusinessException bexc ) {
				LOGGER.error( "Error al obtener Artículo", bexc );
			}

		}
	}


	@Override
	protected boolean validateModel( WModel model ) {

		String codigo = model.getValue( CAMPO_CODIGO );
		String articulo = model.getValue( CAMPO_ARTICULO );
		

		List<String> messages = new ArrayList<String>();

		
		if ( WUtils.isEmpty( codigo ) ) {
			messages.add( "Debe ingresar un Código de Barras" );
		}

		if ( WUtils.isEmpty( articulo ) ) {
			messages.add( "Debe ingresar un Artículo" );
		}

		WTooltipUtils.showMessages( messages, btnGuardar, MessageType.ERROR );

		return WUtils.isEmpty( messages );
	}

	private JPanel getPnlBusqueda() {
		if ( pnlBusqueda == null ) {
			pnlBusqueda = new JPanel();
			pnlBusqueda.setBorder( new TitledBorder( UIManager.getBorder( "TitledBorder.border" ), "Datos",
			    TitledBorder.LEADING, TitledBorder.TOP, null, null ) );
			pnlBusqueda.setBounds( 10, 11, 353, 268 );
			pnlBusqueda.setLayout( null );
			pnlBusqueda.add( getLblCodigo() );
			pnlBusqueda.add( getTxtCodigo() );
			pnlBusqueda.add( getLblNombre() );
			pnlBusqueda.add( getTxtArticulo() );
			pnlBusqueda.add( getLblTalle() );
			pnlBusqueda.add( getCmbTalle() );
			pnlBusqueda.add( getLblColor() );
			pnlBusqueda.add( getSpnStock() );
			pnlBusqueda.add( getLblStock() );
			pnlBusqueda.add( getTxfPublico() );
			pnlBusqueda.add( getChbActivo() );
			pnlBusqueda.add( getLblPrecioPublico() );
			pnlBusqueda.add( getCmbColor() );
			pnlBusqueda.add( getBtnNuevoColor() );
			pnlBusqueda.add(getLblCdigoArt());
			pnlBusqueda.add(getTxtCodigoArt());
		}
		return pnlBusqueda;
	}

	private JLabel getLblCodigo() {
		if ( lblCodigo == null ) {
			lblCodigo = new JLabel( "* C\u00F3d. Barra:" );
			lblCodigo.setHorizontalAlignment( SwingConstants.RIGHT );
			lblCodigo.setBounds( 10, 55, 71, 20 );
		}
		return lblCodigo;
	}

	private JTextField getTxtCodigo() {
		if ( txtCodigo == null ) {
			txtCodigo = new JTextField();
			txtCodigo.setName( CAMPO_CODIGO );
			txtCodigo.setDocument( new WTextFieldLimit( 30 ) );
			txtCodigo.setBounds( 91, 55, 164, 20 );
			txtCodigo.setColumns( 10 );
		}
		return txtCodigo;
	}

	private JLabel getLblNombre() {
		if ( lblNombre == null ) {
			lblNombre = new JLabel( "* Art\u00EDculo:" );
			lblNombre.setHorizontalAlignment( SwingConstants.RIGHT );
			lblNombre.setBounds( 10, 86, 72, 14 );
		}
		return lblNombre;
	}

	private JTextField getTxtArticulo() {
		if ( txtArticulo == null ) {
			txtArticulo = new JTextField();
			txtArticulo.setName( CAMPO_ARTICULO );
			txtArticulo.setDocument( new WTextFieldLimit( 50 ) );
			txtArticulo.setBounds( 91, 83, 230, 20 );
			txtArticulo.setColumns( 10 );
		}
		return txtArticulo;
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
			btnCerrar.setIcon( new ImageIcon( ArticuloVerIFrame.class.getResource( "/icons/cancel.png" ) ) );
			btnCerrar.setBounds( 170, 290, 94, 25 );
		}
		return btnCerrar;
	}

	private JButton getBtnGuardar() {
		if ( btnGuardar == null ) {
			btnGuardar = new JButton( "Guardar" );
			btnGuardar.setIcon( new ImageIcon( ArticuloVerIFrame.class.getResource( "/icons/ok.png" ) ) );
			btnGuardar.setBounds( 269, 290, 94, 25 );
			btnGuardar.addActionListener( new ActionListener() {


				public void actionPerformed( ActionEvent e ) {
					WModel model = populateModel();
					if ( validateModel( model ) ) {

						String codigoArt = model.getValue( CAMPO_CODIGO_ART );
						String codigoBarras = model.getValue( CAMPO_CODIGO );
						String nombre = model.getValue( CAMPO_ARTICULO );
						WOption optColor = model.getValue( CAMPO_COLOR );
						String color = optColor.getKey();
						WOption optTalle = model.getValue( CAMPO_TALLE );

						Integer stock = model.getValue( CAMPO_STOCK );
						String publico = model.getValue( CAMPO_PUBLICO );
						Boolean activo = model.getValue( CAMPO_ACTIVO );

						articulo.setNombre( nombre );
						articulo.setCodigoBarras( codigoBarras );

						ArticuloBO articuloBO = AbstractFactory.getInstance( ArticuloBO.class );
						try {
							articuloBO.saveOrUpdate( articulo );
							hideFrame();
							articuloIFrame.search();
						}
						catch( BusinessException bexc ) {
							LOGGER.error( "Error al guardar Artículo" );
							WFrameUtils.showGlobalErrorMsg( "Se ha producido un error al guardar el Artículo" );
						}

					}

				}
			} );
		}
		return btnGuardar;
	}

	@Override
	protected JComponent getFocusComponent() {
		return getTxtCodigo();
	}

	private JLabel getLblTalle() {
		if ( lblTalle == null ) {
			lblTalle = new JLabel( "* Talle:" );
			lblTalle.setHorizontalAlignment( SwingConstants.RIGHT );
			lblTalle.setBounds( 35, 111, 46, 20 );
		}
		return lblTalle;
	}

	private JComboBox getCmbTalle() {
		if ( cmbTalle == null ) {
			cmbTalle = new JComboBox();
			cmbTalle.setName( CAMPO_TALLE );
			cmbTalle.setBounds( 91, 111, 56, 20 );
		}
		return cmbTalle;
	}

	private JLabel getLblColor() {
		if ( lblColor == null ) {
			lblColor = new JLabel( "* Color:" );
			lblColor.setHorizontalAlignment( SwingConstants.RIGHT );
			lblColor.setBounds( 35, 142, 46, 19 );
		}
		return lblColor;
	}

	private JSpinner getSpnStock() {
		if ( spnStock == null ) {
			spnStock = new JSpinner();
			spnStock.setName( CAMPO_STOCK );
			spnStock.setBounds( 91, 172, 56, 20 );
		}
		return spnStock;
	}

	private JLabel getLblStock() {
		if ( lblStock == null ) {
			lblStock = new JLabel( "* Stock:" );
			lblStock.setHorizontalAlignment( SwingConstants.RIGHT );
			lblStock.setBounds( 35, 172, 46, 20 );
		}
		return lblStock;
	}

	private WTextFieldDecimal getTxfPublico() {
		if ( txfPublico == null ) {
			txfPublico = new WTextFieldDecimal();
			txfPublico.setName( CAMPO_PUBLICO );
			txfPublico.setBounds( 91, 203, 77, 20 );
		}
		return txfPublico;
	}

	private JCheckBox getChbActivo() {
		if ( chbActivo == null ) {
			chbActivo = new JCheckBox( "Activo" );
			chbActivo.setBounds( 91, 230, 97, 23 );
			chbActivo.setName( CAMPO_ACTIVO );
			chbActivo.setSelected( Boolean.TRUE );
		}
		return chbActivo;
	}

	private JLabel getLblPrecioPublico() {
		if ( lblPrecioPublico == null ) {
			lblPrecioPublico = new JLabel( "* $ Publico:" );
			lblPrecioPublico.setHorizontalAlignment( SwingConstants.RIGHT );
			lblPrecioPublico.setBounds( 10, 203, 72, 20 );
		}
		return lblPrecioPublico;
	}

	private JComboBox getCmbColor() {
		if ( cmbColor == null ) {
			cmbColor = new JComboBox();
			cmbColor.setBounds( 91, 142, 164, 20 );
			cmbColor.setName( CAMPO_COLOR );
		}
		return cmbColor;
	}

	private JButton getBtnNuevoColor() {
		if ( btnNuevoColor == null ) {
			btnNuevoColor = new JButton( "" );
			btnNuevoColor.addActionListener( new ActionListener() {


				public void actionPerformed( ActionEvent e ) {
					addModalIFrame( new WGenericSelectorIFrame( "Nuevo Color", "Color:",
					    new WGenericSelectorListener() {


						    @Override
						    public void selectedText( String text ) {
							    if ( WUtils.isNotEmpty( text ) ) {
								    WOption option = new WOption( (long) text.hashCode(), text );
								    int itemCount = getCmbColor().getItemCount();
								    boolean existe = Boolean.FALSE;
								    if ( itemCount > 0 ) {
									    for ( int i = 0; i < itemCount; i++ ) {
										    if ( getCmbColor().getItemAt( i ).equals( option ) ) {
											    existe = Boolean.TRUE;
										    }
									    }
								    }
								    if ( ! existe ) {
									    getCmbColor().addItem( option );
								    }
							    }
						    }
					    }, 20 ) );
				}
			} );
			btnNuevoColor.setIcon( new ImageIcon( ArticuloVerIFrame.class.getResource( "/icons/add.png" ) ) );
			btnNuevoColor.setBounds( 259, 142, 25, 20 );
		}
		return btnNuevoColor;
	}

	@Override
	public void enterPressed() {
	}
	private JLabel getLblCdigoArt() {
		if (lblCdigoArt == null) {
			lblCdigoArt = new JLabel("* C\u00F3d. Art.:");
			lblCdigoArt.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCdigoArt.setBounds(10, 27, 71, 20);
		}
		return lblCdigoArt;
	}
	private JTextField getTxtCodigoArt() {
		if (txtCodigoArt == null) {
			txtCodigoArt = new JTextField();
			txtCodigoArt.setBounds(91, 27, 140, 20);
			txtCodigoArt.setColumns(10);
			txtCodigoArt.setName( CAMPO_CODIGO_ART );
			txtCodigoArt.setDocument( new WTextFieldLimit( 15 ) );
		}
		return txtCodigoArt;
	}
}
