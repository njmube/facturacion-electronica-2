package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ar.com.wuik.sistema.bo.ProductoBO;
import ar.com.wuik.sistema.bo.TipoProductoBO;
import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.sistema.entities.TipoProducto;
import ar.com.wuik.sistema.entities.enums.TipoIVAEnum;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.ProductoFilter;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WOption;
import ar.com.wuik.swing.components.WTextFieldDecimal;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;

public class ProductoVerIFrame extends WAbstractModelIFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8951162992767740069L;
	private JButton btnCancelar;
	private JButton btnGuardar;
	private JPanel panelDatos;
	private JLabel lblCosto;
	private JComboBox cmbTipoProd;
	private JLabel lblTipoProd;
	private JTextField txtDescripcion;
	private JLabel lblDescripcion;

	private static final String CAMPO_IVA = "iva";
	private static final String CAMPO_TIPO_PROD = "tipoProducto";
	private static final String CAMPO_COSTO = "costo";
	private static final String CAMPO_PRECIO = "precio";
	private static final String CAMPO_DESCRIPCION = "descripcion";
	private static final String CAMPO_CODIGO = "codigo";
	private static final String CAMPO_UBICACION = "ubicacion";
	private ProductoIFrame productoIFrame;
	private ComprobanteVerIFrame comprobanteVerIFrame;
	private RemitoVerIFrame remitoClienteVerIFrame;
	private Producto producto;
	private WTextFieldDecimal txfCosto;
	private JLabel lblCodigo;
	private JTextField txtCodigo;
	private JLabel lblIVA;
	private JComboBox cmbTipoIva;
	private JLabel lblPrecio;
	private WTextFieldDecimal txfPrecio;
	private JLabel lblUbicacion;
	private JTextField txtUbicacion;
	private JButton btnNewButton;

	/**
	 * @wbp.parser.constructor
	 */
	public ProductoVerIFrame(ProductoIFrame productoIFrame) {
		initializate("Nuevo Producto");
		this.productoIFrame = productoIFrame;
		this.producto = new Producto();
	}

	public ProductoVerIFrame(ComprobanteVerIFrame comprobanteVerIFrame) {
		initializate("Nuevo Producto");
		this.comprobanteVerIFrame = comprobanteVerIFrame;
		this.producto = new Producto();
	}

	public ProductoVerIFrame(ComprobanteVerIFrame comprobanteVerIFrame,
			String texto) {
		initializate("Nuevo Producto");
		this.comprobanteVerIFrame = comprobanteVerIFrame;
		this.producto = new Producto();
		this.producto.setDescripcion(texto);
		getTxtDescripcion().setText(texto);
	}

	public ProductoVerIFrame(RemitoVerIFrame remitoClienteVerIFrame) {
		initializate("Nuevo Producto");
		this.remitoClienteVerIFrame = remitoClienteVerIFrame;
		this.producto = new Producto();
	}

	public ProductoVerIFrame(RemitoVerIFrame remitoClienteVerIFrame,
			String texto) {
		initializate("Nuevo Producto");
		this.remitoClienteVerIFrame = remitoClienteVerIFrame;
		this.producto.setDescripcion(texto);
		getTxtDescripcion().setText(texto);
	}

	public ProductoVerIFrame(Long idProducto, ProductoIFrame productoIFrame) {
		initializate("Editar Producto");
		try {
			ProductoBO productoBO = AbstractFactory
					.getInstance(ProductoBO.class);
			this.producto = productoBO.obtener(idProducto);
			this.productoIFrame = productoIFrame;
			WModel model = populateModel();
			model.addValue(CAMPO_CODIGO, producto.getCodigo());
			model.addValue(CAMPO_IVA, producto.getTipoIVA().getId());
			model.addValue(CAMPO_TIPO_PROD, producto.getTipoProducto().getId());
			model.addValue(CAMPO_COSTO, producto.getCosto());
			model.addValue(CAMPO_DESCRIPCION, producto.getDescripcion());
			model.addValue(CAMPO_PRECIO, producto.getPrecio());
			model.addValue(CAMPO_UBICACION, producto.getUbicacion());
			populateComponents(model);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	public void loadTiposProducto() {
		getCmbTipoProd().removeAllItems();
		getCmbTipoProd().addItem(WOption.getWOptionSelecione());
		List<WOption> items = getTiposProducto();
		for (WOption wOption : items) {
			getCmbTipoProd().addItem(wOption);
		}
	}

	private void initializate(String title) {
		setTitle(title);
		setBorder(new LineBorder(null, 1, true));
		setFrameIcon(new ImageIcon(
				ClienteIFrame.class.getResource("/icons/productos.png")));
		setBounds(0, 0, 452, 374);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getBtnCancelar());
		getContentPane().add(getBtnGuardar());
		getContentPane().add(getPanelDatos());
		loadTiposProducto();
		loadTiposIva();
	}

	private void loadTiposIva() {

		TipoIVAEnum[] tiposIVAEnum = TipoIVAEnum.values();
		for (TipoIVAEnum tipoIVAEnum : tiposIVAEnum) {
			getCmbTipoIva().addItem(
					new WOption((long) tipoIVAEnum.getId(), tipoIVAEnum
							.getDescripcion()));
		}
		getCmbTipoIva().setSelectedItem(
				new WOption((long) TipoIVAEnum.IVA_21.getId()));
	}

	@Override
	protected boolean validateModel(WModel model) {
		String codigo = model.getValue(CAMPO_CODIGO);
		String descripcion = model.getValue(CAMPO_DESCRIPCION);
		WOption tipoProducto = model.getValue(CAMPO_TIPO_PROD);
		String costo = model.getValue(CAMPO_COSTO);
		String precio = model.getValue(CAMPO_PRECIO);

		List<String> messages = new ArrayList<String>();

		if (WUtils.isEmpty(codigo)) {
			messages.add("Debe ingresar un Código");
		} else {
			ProductoBO productoBO = AbstractFactory
					.getInstance(ProductoBO.class);
			ProductoFilter filter = new ProductoFilter();
			filter.setCodigo(codigo);
			filter.setIdToExclude(producto.getId());
			try {
				List<Producto> productos = productoBO.buscar(filter);
				if (productos.size() > 0) {
					messages.add("Ya existe un Producto con el Código: \""
							+ codigo + "\"");
				}
			} catch (BusinessException bexc) {
				showGlobalErrorMsg(bexc.getMessage());
			}
		}

		if (WUtils.isEmpty(descripcion)) {
			messages.add("Debe ingresar una Descripción");
		}

		if (WOption.getIdWOptionSeleccion().equals(tipoProducto.getValue())) {
			messages.add("Debe seleccionar un Tipo Producto");
		}

		if (WUtils.isEmpty(costo)) {
			messages.add("Debe ingresar un Costo");
		}

		if (WUtils.isEmpty(precio)) {
			messages.add("Debe ingresar un Precio");
		}

		WTooltipUtils.showMessages(messages, btnGuardar, MessageType.ERROR);

		return WUtils.isEmpty(messages);
	}

	@Override
	protected JComponent getFocusComponent() {
		return getTxtCodigo();
	}

	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					hideFrame();
				}
			});
			btnCancelar.setIcon(new ImageIcon(ProductoVerIFrame.class
					.getResource("/icons/cancel2.png")));
			btnCancelar.setBounds(224, 307, 103, 30);
		}
		return btnCancelar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setIcon(new ImageIcon(ProductoVerIFrame.class
					.getResource("/icons/ok.png")));
			btnGuardar.setBounds(336, 307, 103, 30);
			btnGuardar.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					guardar();
				}
			});
		}
		return btnGuardar;
	}

	private void guardar() {
		WModel model = populateModel();
		if (validateModel(model)) {
			try {

				ProductoBO productoBO = AbstractFactory
						.getInstance(ProductoBO.class);

				String codigo = model.getValue(CAMPO_CODIGO);
				String descripcion = model.getValue(CAMPO_DESCRIPCION);
				WOption tipoIva = model.getValue(CAMPO_IVA);
				WOption tipoProducto = model.getValue(CAMPO_TIPO_PROD);
				String costo = model.getValue(CAMPO_COSTO);
				String precio = model.getValue(CAMPO_PRECIO);
				String ubicacion = model.getValue(CAMPO_UBICACION);

				producto.setDescripcion(descripcion);
				producto.setTipoIVA(TipoIVAEnum.fromValue(tipoIva.getValue()
						.intValue()));
				producto.setIdTipo(tipoProducto.getValue());
				producto.setCosto(WUtils.getValue(costo));
				producto.setPrecio(WUtils.getValue(precio));
				producto.setCodigo(codigo);
				producto.setUbicacion(ubicacion);

				if (null == producto.getId()) {
					productoBO.guardar(producto);
				} else {
					productoBO.actualizar(producto);
				}

				if (null != productoIFrame) {
					productoIFrame.search();
				} else if (null != comprobanteVerIFrame) {
					comprobanteVerIFrame.search();
				} else if (null != remitoClienteVerIFrame) {
					remitoClienteVerIFrame.search();
				}
				hideFrame();
			} catch (BusinessException bexc) {
				showGlobalErrorMsg(bexc.getMessage());
			}
		}
	}

	private JPanel getPanelDatos() {
		if (panelDatos == null) {
			panelDatos = new JPanel();
			panelDatos.setBorder(new TitledBorder(null, "Datos",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelDatos.setBounds(11, 8, 428, 288);
			panelDatos.setLayout(null);
			panelDatos.add(getLblCosto());
			panelDatos.add(getCmbTipoProd());
			panelDatos.add(getLblTipoProd());
			panelDatos.add(getTxtDescripcion());
			panelDatos.add(getLblDescripcion());
			panelDatos.add(getTxfCosto());
			panelDatos.add(getLblCodigo());
			panelDatos.add(getTxtCodigo());
			panelDatos.add(getLblIVA());
			panelDatos.add(getCmbTipoIva());
			panelDatos.add(getLblPrecio());
			panelDatos.add(getTxfPrecio());
			panelDatos.add(getLblUbicacion());
			panelDatos.add(getTxtUbicacion());
			panelDatos.add(getBtnNewButton());
		}
		return panelDatos;
	}

	private JLabel getLblCosto() {
		if (lblCosto == null) {
			lblCosto = new JLabel("* Costo: $");
			lblCosto.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCosto.setBounds(10, 128, 128, 25);
		}
		return lblCosto;
	}

	private JComboBox getCmbTipoProd() {
		if (cmbTipoProd == null) {
			cmbTipoProd = new JComboBox();
			cmbTipoProd.setBounds(148, 95, 230, 25);
			cmbTipoProd.setName(CAMPO_TIPO_PROD);
		}
		return cmbTipoProd;
	}

	private JLabel getLblTipoProd() {
		if (lblTipoProd == null) {
			lblTipoProd = new JLabel("* Tipo:");
			lblTipoProd.setHorizontalAlignment(SwingConstants.RIGHT);
			lblTipoProd.setBounds(10, 95, 128, 25);
		}
		return lblTipoProd;
	}

	private JTextField getTxtDescripcion() {
		if (txtDescripcion == null) {
			txtDescripcion = new JTextField();
			txtDescripcion.setColumns(10);
			txtDescripcion.setBounds(148, 59, 230, 25);
			txtDescripcion.setName(CAMPO_DESCRIPCION);
			txtDescripcion.setDocument(new WTextFieldLimit(50));
		}
		return txtDescripcion;
	}

	private JLabel getLblDescripcion() {
		if (lblDescripcion == null) {
			lblDescripcion = new JLabel("* Descripci\u00F3n:");
			lblDescripcion.setHorizontalAlignment(SwingConstants.RIGHT);
			lblDescripcion.setBounds(10, 59, 128, 25);
		}
		return lblDescripcion;
	}

	private List<WOption> getTiposProducto() {
		List<WOption> tipoProductosWOption = new ArrayList<WOption>();
		TipoProductoBO tipoProductoBO = AbstractFactory
				.getInstance(TipoProductoBO.class);
		List<TipoProducto> tipoProductos;
		try {
			tipoProductos = tipoProductoBO.obtenerTodos();
			if (!WUtils.isEmpty(tipoProductos)) {
				for (TipoProducto tipoProducto : tipoProductos) {
					tipoProductosWOption.add(new WOption(Long
							.valueOf(tipoProducto.getId()), tipoProducto
							.getNombre()));
				}
			}
		} catch (BusinessException e) {
		}

		return tipoProductosWOption;
	}

	private WTextFieldDecimal getTxfCosto() {
		if (txfCosto == null) {
			txfCosto = new WTextFieldDecimal(7, 2);
			txfCosto.setName(CAMPO_COSTO);
			txfCosto.setBounds(148, 128, 125, 25);
		}
		return txfCosto;
	}

	private JLabel getLblCodigo() {
		if (lblCodigo == null) {
			lblCodigo = new JLabel("* C\u00F3digo:");
			lblCodigo.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCodigo.setBounds(10, 23, 128, 25);
		}
		return lblCodigo;
	}

	private JTextField getTxtCodigo() {
		if (txtCodigo == null) {
			txtCodigo = new JTextField();
			txtCodigo.setName(CAMPO_CODIGO);
			txtCodigo.setColumns(10);
			txtCodigo.setDocument(new WTextFieldLimit(20));
			txtCodigo.setBounds(148, 23, 125, 25);
		}
		return txtCodigo;
	}

	private JLabel getLblIVA() {
		if (lblIVA == null) {
			lblIVA = new JLabel("* IVA: %");
			lblIVA.setHorizontalAlignment(SwingConstants.RIGHT);
			lblIVA.setBounds(10, 164, 128, 25);
		}
		return lblIVA;
	}

	private JComboBox getCmbTipoIva() {
		if (cmbTipoIva == null) {
			cmbTipoIva = new JComboBox();
			cmbTipoIva.setName(CAMPO_IVA);
			cmbTipoIva.setBounds(148, 164, 125, 25);
		}
		return cmbTipoIva;
	}

	private JLabel getLblPrecio() {
		if (lblPrecio == null) {
			lblPrecio = new JLabel("* Precio: $");
			lblPrecio.setHorizontalAlignment(SwingConstants.RIGHT);
			lblPrecio.setBounds(10, 200, 128, 25);
		}
		return lblPrecio;
	}

	private WTextFieldDecimal getTxfPrecio() {
		if (txfPrecio == null) {
			txfPrecio = new WTextFieldDecimal(7, 2);
			txfPrecio.setName(CAMPO_PRECIO);
			txfPrecio.setBounds(148, 200, 125, 25);
		}
		return txfPrecio;
	}

	private JLabel getLblUbicacion() {
		if (lblUbicacion == null) {
			lblUbicacion = new JLabel("Ubicaci\u00F3n:");
			lblUbicacion.setHorizontalAlignment(SwingConstants.RIGHT);
			lblUbicacion.setBounds(10, 236, 128, 25);
		}
		return lblUbicacion;
	}

	private JTextField getTxtUbicacion() {
		if (txtUbicacion == null) {
			txtUbicacion = new JTextField();
			txtUbicacion.setName(CAMPO_UBICACION);
			txtUbicacion.setColumns(10);
			txtUbicacion.setDocument(new WTextFieldLimit(45));
			txtUbicacion.setBounds(148, 236, 230, 25);
		}
		return txtUbicacion;
	}

	private JButton getBtnNewButton() {
		if (btnNewButton == null) {
			btnNewButton = new JButton("");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addModalIFrame(new TipoProductoVerIFrame(
							ProductoVerIFrame.this));
				}
			});
			btnNewButton.setIcon(new ImageIcon(ProductoVerIFrame.class
					.getResource("/icons/add.png")));
			btnNewButton.setBounds(388, 96, 26, 25);
		}
		return btnNewButton;
	}
}
