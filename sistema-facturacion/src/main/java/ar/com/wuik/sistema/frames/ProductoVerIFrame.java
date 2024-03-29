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
	private JTextField txtDescripcion;
	private JLabel lblDescripcion;

	private static final String CAMPO_IVA = "iva";
	private static final String CAMPO_DESCRIPCION = "descripcion";
	private static final String CAMPO_CODIGO = "codigo";
	private static final String CAMPO_UBICACION = "ubicacion";
	private ProductoIFrame productoIFrame;
	private ComprobanteVerIFrame comprobanteVerIFrame;
	private RemitoVerIFrame remitoClienteVerIFrame;
	private Producto producto;
	private JLabel lblCodigo;
	private JTextField txtCodigo;
	private JLabel lblIVA;
	private JComboBox cmbTipoIva;
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

	public ProductoVerIFrame(ComprobanteVerIFrame comprobanteVerIFrame,
			String texto) {
		initializate("Nuevo Producto");
		this.comprobanteVerIFrame = comprobanteVerIFrame;
		this.producto = new Producto();
		this.producto.setCodigo(texto);
		getTxtCodigo().setText(texto);
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
		this.producto = new Producto();
		this.producto.setCodigo(texto);
		getTxtCodigo().setText(texto);
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
			model.addValue(CAMPO_DESCRIPCION, producto.getDescripcion());
			model.addValue(CAMPO_UBICACION, producto.getUbicacion());
			populateComponents(model);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	private void initializate(String title) {
		setTitle(title);
		setBorder(new LineBorder(null, 1, true));
		setFrameIcon(new ImageIcon(
				ClienteIFrame.class.getResource("/icons/productos.png")));
		setBounds(0, 0, 452, 256);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getBtnCancelar());
		getContentPane().add(getBtnGuardar());
		getContentPane().add(getPanelDatos());
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

		List<String> messages = new ArrayList<String>();

		if (WUtils.isEmpty(codigo)) {
			messages.add("Debe ingresar un C�digo");
		} else {
			ProductoBO productoBO = AbstractFactory
					.getInstance(ProductoBO.class);
			ProductoFilter filter = new ProductoFilter();
			filter.setCodigo(codigo);
			filter.setIdToExclude(producto.getId());
			try {
				List<Producto> productos = productoBO.buscar(filter);
				if (productos.size() > 0) {
					messages.add("Ya existe un Producto con el C�digo: \""
							+ codigo + "\"");
				}
			} catch (BusinessException bexc) {
				showGlobalErrorMsg(bexc.getMessage());
			}
		}

		if (WUtils.isEmpty(descripcion)) {
			messages.add("Debe ingresar una Descripci�n");
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
			btnCancelar.setBounds(224, 187, 103, 30);
		}
		return btnCancelar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setIcon(new ImageIcon(ProductoVerIFrame.class
					.getResource("/icons/ok.png")));
			btnGuardar.setBounds(336, 187, 103, 30);
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
				String ubicacion = model.getValue(CAMPO_UBICACION);

				producto.setDescripcion(descripcion);
				producto.setTipoIVA(TipoIVAEnum.fromValue(tipoIva.getValue()
						.intValue()));
				producto.setCodigo(codigo);
				producto.setUbicacion(ubicacion);

				if (null == producto.getId()) {
					productoBO.guardar(producto);
				} else {
					productoBO.actualizar(producto);
				}

				if (null != productoIFrame) {
					productoIFrame.search();
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
			panelDatos.setBounds(11, 8, 428, 168);
			panelDatos.setLayout(null);
			panelDatos.add(getTxtDescripcion());
			panelDatos.add(getLblDescripcion());
			panelDatos.add(getLblCodigo());
			panelDatos.add(getTxtCodigo());
			panelDatos.add(getLblIVA());
			panelDatos.add(getCmbTipoIva());
			panelDatos.add(getLblUbicacion());
			panelDatos.add(getTxtUbicacion());
		}
		return panelDatos;
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
			lblIVA.setBounds(10, 95, 128, 25);
		}
		return lblIVA;
	}

	private JComboBox getCmbTipoIva() {
		if (cmbTipoIva == null) {
			cmbTipoIva = new JComboBox();
			cmbTipoIva.setName(CAMPO_IVA);
			cmbTipoIva.setBounds(148, 95, 125, 25);
		}
		return cmbTipoIva;
	}

	private JLabel getLblUbicacion() {
		if (lblUbicacion == null) {
			lblUbicacion = new JLabel("Ubicaci\u00F3n:");
			lblUbicacion.setHorizontalAlignment(SwingConstants.RIGHT);
			lblUbicacion.setBounds(10, 131, 128, 25);
		}
		return lblUbicacion;
	}

	private JTextField getTxtUbicacion() {
		if (txtUbicacion == null) {
			txtUbicacion = new JTextField();
			txtUbicacion.setName(CAMPO_UBICACION);
			txtUbicacion.setColumns(10);
			txtUbicacion.setDocument(new WTextFieldLimit(45));
			txtUbicacion.setBounds(148, 131, 230, 25);
		}
		return txtUbicacion;
	}

}
