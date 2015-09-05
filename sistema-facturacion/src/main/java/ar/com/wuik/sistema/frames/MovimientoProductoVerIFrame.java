package ar.com.wuik.sistema.frames;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ar.com.wuik.sistema.bo.ProductoBO;
import ar.com.wuik.sistema.bo.ProveedorBO;
import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.sistema.entities.Proveedor;
import ar.com.wuik.sistema.entities.StockProducto;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WOption;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.components.WTextFieldNumeric;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.frames.WCalendarIFrame;
import ar.com.wuik.swing.utils.WFrameUtils;
import ar.com.wuik.swing.utils.WFrameUtils.FontSize;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;

public class MovimientoProductoVerIFrame extends WAbstractModelIFrame {
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6838619883125511589L;
	private JPanel pnlBusqueda;
	private JButton btnCerrar;
	private StockProducto stockProducto;
	private JButton btnGuardar;

	private static final String CAMPO_CODIGO = "codigo";
	private static final String CAMPO_NOMBRE = "nombre";
	private static final String CAMPO_CANTIDAD = "cantidad";
	private static final String CAMPO_FACTURA = "factura";
	private static final String CAMPO_PROVEEDOR = "proveedor";
	private static final String CAMPO_FECHA = "fecha";
	private MovimientoProductoIFrame movimientoProductoIFrame;
	private JButton btnFecha;
	private JTextField txtFecha;
	private JLabel lblFecha;
	private JLabel lblCantidad;
	private WTextFieldNumeric txtCantidad;
	private JLabel lblProveedor;
	private JComboBox cmbProveedor;
	private JLabel txtCodigo;
	private JLabel lblCodigo;
	private JLabel txtProducto;
	private JLabel lblProducto;
	private JTextField txtFactura;
	private JLabel lblFactura;
	private JButton btnProveedor;

	/**
	 * Create the frame.
	 * 
	 * @wbp.parser.constructor
	 */
	public MovimientoProductoVerIFrame(Long idProducto,
			MovimientoProductoIFrame movimientoProductoIFrame) {
		initialize("Nuevo Movimiento de Producto");
		this.movimientoProductoIFrame = movimientoProductoIFrame;
		this.stockProducto = new StockProducto();
		this.stockProducto.setIdProducto(idProducto);

		ProductoBO productoBO = AbstractFactory.getInstance(ProductoBO.class);
		try {
			Producto producto = productoBO.obtener(idProducto);
			WModel model = populateModel();
			model.addValue(CAMPO_CODIGO, producto.getCodigo());
			model.addValue(CAMPO_NOMBRE, producto.getDescripcion());
			model.addValue(CAMPO_FECHA, WUtils.getStringFromDate(new Date()));
			populateComponents(model);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	public MovimientoProductoVerIFrame(
			MovimientoProductoIFrame movimientoProductoIFrame,
			Long idStockMovimiento) {
		initialize("Editar Movimiento de Producto");
		this.movimientoProductoIFrame = movimientoProductoIFrame;

		ProductoBO productoBO = AbstractFactory.getInstance(ProductoBO.class);
		try {
			stockProducto = productoBO.obtenerStockProducto(idStockMovimiento);
			WModel model = populateModel();
			model.addValue(CAMPO_CODIGO, stockProducto.getProducto()
					.getCodigo());
			model.addValue(CAMPO_NOMBRE, stockProducto.getProducto()
					.getDescripcion());
			model.addValue(CAMPO_CANTIDAD, stockProducto.getCantidad());
			model.addValue(CAMPO_FACTURA, stockProducto.getFactura());
			model.addValue(CAMPO_PROVEEDOR, stockProducto.getIdProveedor());
			model.addValue(CAMPO_FECHA,
					WUtils.getStringFromDate(stockProducto.getFecha()));
			populateComponents(model);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	private void initialize(String title) {
		setTitle(title);
		setBorder(new LineBorder(null, 1, true));
		setFrameIcon(new ImageIcon(
				MovimientoProductoVerIFrame.class
						.getResource("/icons/remitos.png")));
		setBounds(0, 0, 473, 335);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getBtnCerrar());
		getContentPane().add(getBtnGuardar());
		loadProveedores();
	}

	public void loadProveedores() {
		loadProveedores(null);
	}

	public void loadProveedores(Long idProveedor) {
		getCmbProveedor().removeAllItems();
		getCmbProveedor().addItem(WOption.getWOptionSelecione());
		ProveedorBO proveedorBO = AbstractFactory
				.getInstance(ProveedorBO.class);
		try {
			List<Proveedor> proveedores = proveedorBO.obtenerTodosActivos();
			if (WUtils.isNotEmpty(proveedores)) {
				for (Proveedor proveedor : proveedores) {
					getCmbProveedor().addItem(
							new WOption(proveedor.getId(), proveedor
									.getRazonSocial()));
				}
			}
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
		if (null != idProveedor) {
			getCmbProveedor().setSelectedItem(new WOption(idProveedor));
		}
	}

	@Override
	protected boolean validateModel(WModel model) {

		String fecha = model.getValue(CAMPO_FECHA);
		String cantidad = model.getValue(CAMPO_CANTIDAD);
		WOption proveedor = model.getValue(CAMPO_PROVEEDOR);

		List<String> messages = new ArrayList<String>();

		if (WUtils.isEmpty(fecha)) {
			messages.add("Debe seleccionar una Fecha");
		}

		if (WUtils.isEmpty(cantidad)) {
			messages.add("Debe ingresar una Cantidad");
		} else {
			if (new BigDecimal(cantidad).doubleValue() == 0) {
				messages.add("Debe ingresar una Cantidad mayor a 0");
			}
		}

		if (WOption.getIdWOptionSeleccion().equals(proveedor.getValue())) {
			messages.add("Debe seleccionar un Proveedor");
		}

		WTooltipUtils.showMessages(messages, btnGuardar, MessageType.ERROR);

		return WUtils.isEmpty(messages);
	}

	private JPanel getPnlBusqueda() {
		if (pnlBusqueda == null) {
			pnlBusqueda = new JPanel();
			pnlBusqueda.setBorder(new TitledBorder(UIManager
					.getBorder("TitledBorder.border"), "Datos",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlBusqueda.setBounds(10, 11, 455, 244);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getBtnFecha());
			pnlBusqueda.add(getTxtFecha());
			pnlBusqueda.add(getLblFecha());
			pnlBusqueda.add(getLblCantidad());
			pnlBusqueda.add(getTxtCantidad());
			pnlBusqueda.add(getLblProveedor());
			pnlBusqueda.add(getCmbProveedor());
			pnlBusqueda.add(getTxtCodigo());
			pnlBusqueda.add(getLblCodigo());
			pnlBusqueda.add(getTxtProducto());
			pnlBusqueda.add(getLblProducto());
			pnlBusqueda.add(getTxtFactura());
			pnlBusqueda.add(getLblFactura());
			pnlBusqueda.add(getBtnProveedor());
		}
		return pnlBusqueda;
	}

	private JButton getBtnCerrar() {
		if (btnCerrar == null) {
			btnCerrar = new JButton("Cancelar");
			btnCerrar.setFocusable(false);
			btnCerrar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					hideFrame();
				}
			});
			btnCerrar.setIcon(new ImageIcon(MovimientoProductoVerIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(249, 266, 103, 30);
		}
		return btnCerrar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setIcon(new ImageIcon(MovimientoProductoVerIFrame.class
					.getResource("/icons/ok.png")));
			btnGuardar.setBounds(362, 266, 103, 30);
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					WModel model = populateModel();
					if (validateModel(model)) {

						String fecha = model.getValue(CAMPO_FECHA);
						String cantidad = model.getValue(CAMPO_CANTIDAD);
						String factura = model.getValue(CAMPO_FACTURA);
						WOption proveedor = model.getValue(CAMPO_PROVEEDOR);

						stockProducto.setCantidad(Integer.valueOf(cantidad));
						stockProducto.setFactura(factura);
						stockProducto.setFecha(WUtils.getDateFromString(fecha));
						stockProducto.setIdProveedor(proveedor.getValue());

						try {
							ProductoBO productoBO = AbstractFactory
									.getInstance(ProductoBO.class);
							if (null == stockProducto.getId()) {
								productoBO.guardarStockProducto(stockProducto);
							} else {
								productoBO
										.actualizarStockProducto(stockProducto);
							}
							hideFrame();
							movimientoProductoIFrame.search();
						} catch (BusinessException bexc) {
							showGlobalErrorMsg(bexc.getMessage());
						}
					}

				}
			});
		}
		return btnGuardar;
	}

	@Override
	protected JComponent getFocusComponent() {
		return getTxtCantidad();
	}

	@Override
	public void enterPressed() {
	}

	private JButton getBtnFecha() {
		if (btnFecha == null) {
			btnFecha = new JButton("");
			btnFecha.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addModalIFrame(new WCalendarIFrame(txtFecha));
				}
			});
			btnFecha.setIcon(new ImageIcon(MovimientoProductoVerIFrame.class
					.getResource("/icons/calendar.png")));
			btnFecha.setBounds(229, 95, 25, 25);
		}
		return btnFecha;
	}

	private JTextField getTxtFecha() {
		if (txtFecha == null) {
			txtFecha = new JTextField();
			txtFecha.setName(CAMPO_FECHA);
			txtFecha.setEditable(false);
			txtFecha.setBounds(113, 95, 106, 25);
		}
		return txtFecha;
	}

	private JLabel getLblFecha() {
		if (lblFecha == null) {
			lblFecha = new JLabel("* Fecha:");
			lblFecha.setHorizontalAlignment(SwingConstants.RIGHT);
			lblFecha.setBounds(10, 95, 93, 25);
		}
		return lblFecha;
	}

	private JLabel getLblCantidad() {
		if (lblCantidad == null) {
			lblCantidad = new JLabel("* Cantidad:");
			lblCantidad.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCantidad.setBounds(10, 131, 93, 25);
		}
		return lblCantidad;
	}

	private WTextFieldNumeric getTxtCantidad() {
		if (txtCantidad == null) {
			txtCantidad = new WTextFieldNumeric();
			txtCantidad.setHorizontalAlignment(SwingConstants.RIGHT);
			txtCantidad.setBounds(113, 131, 125, 25);
			txtCantidad.setColumns(10);
			txtCantidad.setName(CAMPO_CANTIDAD);
		}
		return txtCantidad;
	}

	private JLabel getLblProveedor() {
		if (lblProveedor == null) {
			lblProveedor = new JLabel("* Proveedor:");
			lblProveedor.setHorizontalAlignment(SwingConstants.RIGHT);
			lblProveedor.setBounds(10, 167, 93, 25);
		}
		return lblProveedor;
	}

	private JComboBox getCmbProveedor() {
		if (cmbProveedor == null) {
			cmbProveedor = new JComboBox();
			cmbProveedor.setEditable(false);
			cmbProveedor.setBounds(113, 167, 280, 25);
			cmbProveedor.setName(CAMPO_PROVEEDOR);
		}
		return cmbProveedor;
	}

	private JLabel getTxtCodigo() {
		if (txtCodigo == null) {
			txtCodigo = new JLabel();
			txtCodigo.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			txtCodigo.setBounds(113, 23, 130, 25);
			txtCodigo.setName(CAMPO_CODIGO);
		}
		return txtCodigo;
	}

	private JLabel getLblCodigo() {
		if (lblCodigo == null) {
			lblCodigo = new JLabel("C\u00F3digo:");
			lblCodigo.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCodigo.setBounds(10, 23, 93, 25);
		}
		return lblCodigo;
	}

	private JLabel getTxtProducto() {
		if (txtProducto == null) {
			txtProducto = new JLabel();
			txtProducto.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			txtProducto.setBounds(113, 59, 280, 25);
			txtProducto.setName(CAMPO_NOMBRE);
		}
		return txtProducto;
	}

	private JLabel getLblProducto() {
		if (lblProducto == null) {
			lblProducto = new JLabel("Producto:");
			lblProducto.setHorizontalAlignment(SwingConstants.RIGHT);
			lblProducto.setBounds(10, 59, 93, 25);
		}
		return lblProducto;
	}

	private JTextField getTxtFactura() {
		if (txtFactura == null) {
			txtFactura = new JTextField();
			txtFactura.setBounds(113, 203, 142, 25);
			txtFactura.setName(CAMPO_FACTURA);
			txtFactura.setDocument(new WTextFieldLimit(50));
		}
		return txtFactura;
	}

	private JLabel getLblFactura() {
		if (lblFactura == null) {
			lblFactura = new JLabel("Factura:");
			lblFactura.setHorizontalAlignment(SwingConstants.RIGHT);
			lblFactura.setBounds(10, 203, 93, 25);
		}
		return lblFactura;
	}

	private JButton getBtnProveedor() {
		if (btnProveedor == null) {
			btnProveedor = new JButton("");
			btnProveedor.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addModalIFrame(new ProveedorVerIFrame(
							MovimientoProductoVerIFrame.this));
				}
			});
			btnProveedor.setIcon(new ImageIcon(
					MovimientoProductoVerIFrame.class
							.getResource("/icons/add.png")));
			btnProveedor.setBounds(403, 168, 25, 23);
		}
		return btnProveedor;
	}

}
