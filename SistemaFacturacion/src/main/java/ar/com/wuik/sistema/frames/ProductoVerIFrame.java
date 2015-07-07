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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.sistema.bo.ParametricoBO;
import ar.com.wuik.sistema.bo.ProductoBO;
import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.sistema.entities.TipoProducto;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WOption;
import ar.com.wuik.swing.components.WTextFieldDecimal;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.utils.WFrameUtils;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;

public class ProductoVerIFrame extends WAbstractModelIFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8951162992767740069L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProductoVerIFrame.class);
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
	private VentaClienteVerIFrame ventaClienteVerIFrame;
	private Producto producto;
	private WTextFieldDecimal txfCosto;
	private JLabel lblCodigo;
	private JTextField txtCodigo;
	private JLabel lblIVA;
	private WTextFieldDecimal txfIVA;
	private JLabel lblPrecio;
	private WTextFieldDecimal txfPrecio;
	private JLabel lblUbicacion;
	private JTextField txtUbicacion;

	/**
	 * @wbp.parser.constructor
	 */
	public ProductoVerIFrame(ProductoIFrame productoIFrame) {
		initializate("Nuevo Producto");
		this.productoIFrame = productoIFrame;
		this.producto = new Producto();
	}
	
	public ProductoVerIFrame(VentaClienteVerIFrame ventaClienteVerIFrame) {
		initializate("Nuevo Producto");
		this.ventaClienteVerIFrame = ventaClienteVerIFrame;
		this.producto = new Producto();
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
			model.addValue(CAMPO_IVA, producto.getIva());
			model.addValue(CAMPO_TIPO_PROD, producto.getTipoProducto().getId());
			model.addValue(CAMPO_COSTO, producto.getCosto());
			model.addValue(CAMPO_DESCRIPCION, producto.getDescripcion());
			model.addValue(CAMPO_PRECIO, producto.getPrecio());
			model.addValue(CAMPO_UBICACION, producto.getUbicacion());
			populateComponents(model);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
	}

	private void initializate(String title) {
		setTitle(title);
		setBorder(new LineBorder(null, 1, true));
		setFrameIcon(new ImageIcon(
				ClienteIFrame.class.getResource("/icons/productos.png")));
		setBounds(0, 0, 411, 370);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getBtnCancelar());
		getContentPane().add(getBtnGuardar());
		getContentPane().add(getPanelDatos());
	}

	@Override
	protected boolean validateModel(WModel model) {
		String codigo = model.getValue(CAMPO_CODIGO);
		String descripcion = model.getValue(CAMPO_DESCRIPCION);
		String iva = model.getValue(CAMPO_IVA);
		WOption tipoProducto = model.getValue(CAMPO_TIPO_PROD);
		String costo = model.getValue(CAMPO_COSTO);
		String precio = model.getValue(CAMPO_PRECIO);

		List<String> messages = new ArrayList<String>();

		if (WUtils.isEmpty(codigo)) {
			messages.add("Debe ingresar un C�digo");
		}

		if (WUtils.isEmpty(descripcion)) {
			messages.add("Debe ingresar una Descripci�n");
		}

		if (WOption.getIdWOptionSeleccion().equals(tipoProducto.getValue())) {
			messages.add("Debe seleccionar un Tipo Producto");
		}

		if (WUtils.isEmpty(costo)) {
			messages.add("Debe ingresar un Costo");
		}

		if (WUtils.isEmpty(iva)) {
			messages.add("Debe ingresar % de IVA");
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
			btnCancelar.setBounds(184, 307, 103, 25);
		}
		return btnCancelar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setIcon(new ImageIcon(ProductoVerIFrame.class
					.getResource("/icons/ok.png")));
			btnGuardar.setBounds(296, 307, 103, 25);
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
				String iva = model.getValue(CAMPO_IVA);
				WOption tipoProducto = model.getValue(CAMPO_TIPO_PROD);
				String costo = model.getValue(CAMPO_COSTO);
				String precio = model.getValue(CAMPO_PRECIO);
				String ubicacion = model.getValue(CAMPO_UBICACION);

				producto.setDescripcion(descripcion);
				producto.setIva(WUtils.getValue(iva));
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
				} else if (null != ventaClienteVerIFrame) {
					ventaClienteVerIFrame.search();
				}
				hideFrame();
			} catch (BusinessException bexc) {
				LOGGER.error("Error al guardar Producto", bexc);
				WFrameUtils
						.showGlobalErrorMsg("Se ha producido un error al guardar Producto");
			}
		}
	}

	private JPanel getPanelDatos() {
		if (panelDatos == null) {
			panelDatos = new JPanel();
			panelDatos.setBorder(new TitledBorder(null, "Datos",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelDatos.setBounds(11, 8, 388, 288);
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
			panelDatos.add(getTxfIVA());
			panelDatos.add(getLblPrecio());
			panelDatos.add(getTxfPrecio());
			panelDatos.add(getLblUbicacion());
			panelDatos.add(getTxtUbicacion());
		}
		return panelDatos;
	}

	private JLabel getLblCosto() {
		if (lblCosto == null) {
			lblCosto = new JLabel("* Costo:");
			lblCosto.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCosto.setBounds(10, 128, 128, 25);
		}
		return lblCosto;
	}

	private JComboBox getCmbTipoProd() {
		if (cmbTipoProd == null) {
			cmbTipoProd = new JComboBox();
			cmbTipoProd.setBounds(148, 95, 230, 25);
			cmbTipoProd.addItem(WOption.getWOptionSelecione());
			List<WOption> items = getTiposProducto();

			for (WOption wOption : items) {
				cmbTipoProd.addItem(wOption);
			}
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
		ParametricoBO parametricoBO = AbstractFactory
				.getInstance(ParametricoBO.class);
		List<TipoProducto> tipoProductos;
		try {
			tipoProductos = parametricoBO.obtenerTodosTiposProductos();
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
			txfCosto = new WTextFieldDecimal(7, 3);
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
			lblIVA = new JLabel("* IVA:");
			lblIVA.setHorizontalAlignment(SwingConstants.RIGHT);
			lblIVA.setBounds(10, 164, 128, 25);
		}
		return lblIVA;
	}

	private WTextFieldDecimal getTxfIVA() {
		if (txfIVA == null) {
			txfIVA = new WTextFieldDecimal(7, 3);
			txfIVA.setName(CAMPO_IVA);
			txfIVA.setBounds(148, 164, 125, 25);
		}
		return txfIVA;
	}

	private JLabel getLblPrecio() {
		if (lblPrecio == null) {
			lblPrecio = new JLabel("* Precio:");
			lblPrecio.setHorizontalAlignment(SwingConstants.RIGHT);
			lblPrecio.setBounds(10, 200, 128, 25);
		}
		return lblPrecio;
	}

	private WTextFieldDecimal getTxfPrecio() {
		if (txfPrecio == null) {
			txfPrecio = new WTextFieldDecimal(7, 3);
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
}
