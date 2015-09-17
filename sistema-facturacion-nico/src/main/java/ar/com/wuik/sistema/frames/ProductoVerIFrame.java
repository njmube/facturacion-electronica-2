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
	private JComboBox cmbTipoProd;
	private JLabel lblTipoProd;
	private JTextField txtDescripcion;
	private JLabel lblDescripcion;

	private static final String CAMPO_IVA = "iva";
	private static final String CAMPO_TIPO_PROD = "tipoProducto";
	private static final String CAMPO_DESCRIPCION = "descripcion";
	private ProductoIFrame productoIFrame;
	private ComprobanteVerIFrame comprobanteVerIFrame;
	private RemitoVerIFrame remitoClienteVerIFrame;
	private Producto producto;
	private JLabel lblIVA;
	private JComboBox cmbTipoIva;
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
		this.producto = new Producto();
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
			model.addValue(CAMPO_IVA, producto.getTipoIVA().getId());
			model.addValue(CAMPO_TIPO_PROD, producto.getTipoProducto().getId());
			model.addValue(CAMPO_DESCRIPCION, producto.getDescripcion());
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
		setBounds(0, 0, 452, 222);
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
		String descripcion = model.getValue(CAMPO_DESCRIPCION);
		WOption tipoProducto = model.getValue(CAMPO_TIPO_PROD);

		List<String> messages = new ArrayList<String>();

		if (WUtils.isEmpty(descripcion)) {
			messages.add("Debe ingresar una Descripción");
		}

		if (WOption.getIdWOptionSeleccion().equals(tipoProducto.getValue())) {
			messages.add("Debe seleccionar un Tipo Producto");
		}

		WTooltipUtils.showMessages(messages, btnGuardar, MessageType.ERROR);

		return WUtils.isEmpty(messages);
	}

	@Override
	protected JComponent getFocusComponent() {
		return getTxtDescripcion();
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
			btnCancelar.setBounds(224, 154, 103, 30);
		}
		return btnCancelar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setIcon(new ImageIcon(ProductoVerIFrame.class
					.getResource("/icons/ok.png")));
			btnGuardar.setBounds(336, 154, 103, 30);
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

				String descripcion = model.getValue(CAMPO_DESCRIPCION);
				WOption tipoIva = model.getValue(CAMPO_IVA);
				WOption tipoProducto = model.getValue(CAMPO_TIPO_PROD);

				producto.setDescripcion(descripcion);
				producto.setTipoIVA(TipoIVAEnum.fromValue(tipoIva.getValue()
						.intValue()));
				producto.setIdTipo(tipoProducto.getValue());

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
			panelDatos.setBounds(11, 8, 428, 135);
			panelDatos.setLayout(null);
			panelDatos.add(getCmbTipoProd());
			panelDatos.add(getLblTipoProd());
			panelDatos.add(getTxtDescripcion());
			panelDatos.add(getLblDescripcion());
			panelDatos.add(getLblIVA());
			panelDatos.add(getCmbTipoIva());
			panelDatos.add(getBtnNewButton());
		}
		return panelDatos;
	}

	private JComboBox getCmbTipoProd() {
		if (cmbTipoProd == null) {
			cmbTipoProd = new JComboBox();
			cmbTipoProd.setBounds(148, 62, 230, 25);
			cmbTipoProd.setName(CAMPO_TIPO_PROD);
		}
		return cmbTipoProd;
	}

	private JLabel getLblTipoProd() {
		if (lblTipoProd == null) {
			lblTipoProd = new JLabel("* Tipo:");
			lblTipoProd.setHorizontalAlignment(SwingConstants.RIGHT);
			lblTipoProd.setBounds(10, 62, 128, 25);
		}
		return lblTipoProd;
	}

	private JTextField getTxtDescripcion() {
		if (txtDescripcion == null) {
			txtDescripcion = new JTextField();
			txtDescripcion.setColumns(10);
			txtDescripcion.setBounds(148, 26, 230, 25);
			txtDescripcion.setName(CAMPO_DESCRIPCION);
			txtDescripcion.setDocument(new WTextFieldLimit(50));
		}
		return txtDescripcion;
	}

	private JLabel getLblDescripcion() {
		if (lblDescripcion == null) {
			lblDescripcion = new JLabel("* Descripci\u00F3n:");
			lblDescripcion.setHorizontalAlignment(SwingConstants.RIGHT);
			lblDescripcion.setBounds(10, 26, 128, 25);
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

	private JLabel getLblIVA() {
		if (lblIVA == null) {
			lblIVA = new JLabel("* IVA: %");
			lblIVA.setHorizontalAlignment(SwingConstants.RIGHT);
			lblIVA.setBounds(10, 98, 128, 25);
		}
		return lblIVA;
	}

	private JComboBox getCmbTipoIva() {
		if (cmbTipoIva == null) {
			cmbTipoIva = new JComboBox();
			cmbTipoIva.setName(CAMPO_IVA);
			cmbTipoIva.setBounds(148, 98, 125, 25);
		}
		return cmbTipoIva;
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
			btnNewButton.setBounds(388, 63, 26, 25);
		}
		return btnNewButton;
	}

	@Override
	public void enterPressed() {
		getBtnGuardar().doClick();
	}
}
