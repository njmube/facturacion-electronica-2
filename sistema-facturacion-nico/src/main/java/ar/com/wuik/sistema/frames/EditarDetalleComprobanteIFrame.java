package ar.com.wuik.sistema.frames;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ar.com.wuik.sistema.entities.DetalleComprobante;
import ar.com.wuik.sistema.entities.enums.TipoIVAEnum;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WOption;
import ar.com.wuik.swing.components.WTextFieldDecimal;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.components.WTextFieldNumeric;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.utils.WFrameUtils;
import ar.com.wuik.swing.utils.WFrameUtils.FontSize;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;

public class EditarDetalleComprobanteIFrame extends WAbstractModelIFrame {
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6838619883125511589L;
	private JPanel pnlBusqueda;
	private JButton btnCerrar;
	private DetalleComprobante detalle;
	private JButton btnGuardar;

	private static final String CAMPO_CANTIDAD = "cantidad";
	private static final String CAMPO_PRODUCTO = "producto";
	private static final String CAMPO_PRECIO = "precio";
	private static final String CAMPO_IVA = "iva";
	private JLabel lblCantidad;
	private WTextFieldNumeric txfCantidad;
	private JTextField txtProductoSeleccionado;
	private JLabel lblProductoSeleccionado;
	private ComprobanteVerIFrame comprobanteVerIFrame;
	private boolean editaDetalle = false;
	private WTextFieldDecimal txfPrecio;
	private JLabel lblPrecio;
	private JComboBox cmbTipoIva;
	private JLabel lblIva;
	private JLabel lblPorcIVA;
	private JLabel lblProducto;

	/**
	 * @wbp.parser.constructor
	 */
	public EditarDetalleComprobanteIFrame(DetalleComprobante detalle,
			ComprobanteVerIFrame comprobanteVerIFrame) {
		this.detalle = detalle;
		this.comprobanteVerIFrame = comprobanteVerIFrame;
		initialize("Editar Detalle");
		WModel model = populateModel();
		model.addValue(CAMPO_CANTIDAD, detalle.getCantidad());
		model.addValue(CAMPO_PRECIO, WUtils.getValue(detalle.getPrecioConIVA()));
		model.addValue(CAMPO_IVA, detalle.getTipoIVA().getId());
		model.addValue(CAMPO_PRODUCTO,
				(null != detalle.getProducto()) ? detalle.getProducto()
						.getDescripcion() : detalle.getDetalle());
		editaDetalle = null == detalle.getProducto();
		if (editaDetalle) {
			showDetalleGenerico();
		} else {
			showDetalleProducto();
		}
		populateComponents(model);
		getLblPorcIVA().setText(detalle.getTipoIVA().getDescripcion());
		getLblProducto().setText((String) model.getValue(CAMPO_PRODUCTO));
	}

	private void showDetalleProducto() {
		getLblProductoSeleccionado().setText("Producto:");
		getTxtProductoSeleccionado().setEditable(Boolean.TRUE);
		getLblPorcIVA().setVisible(Boolean.TRUE);
		getCmbTipoIva().setVisible(Boolean.FALSE);
		getLblProducto().setVisible(Boolean.TRUE);
		getTxtProductoSeleccionado().setVisible(Boolean.FALSE);
	}

	private void showDetalleGenerico() {
		getLblProductoSeleccionado().setText("* Detalle:");
		getTxtProductoSeleccionado().setEditable(Boolean.TRUE);
		getLblIva().setText("* " + getLblIva().getText());
		getLblPorcIVA().setVisible(Boolean.FALSE);
		getLblCantidad().setVisible(Boolean.FALSE);
		getTxfCantidad().setVisible(Boolean.FALSE);
		getLblProducto().setVisible(Boolean.FALSE);
	}

	private void initialize(String title) {
		setTitle(title);
		setBorder(new LineBorder(null, 1, true));
		setFrameIcon(new ImageIcon(
				EditarDetalleComprobanteIFrame.class
						.getResource("/icons/productos.png")));
		setBounds(0, 0, 508, 253);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getBtnCerrar());
		getContentPane().add(getBtnGuardar());
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

		String cantidad = model.getValue(CAMPO_CANTIDAD);

		List<String> messages = new ArrayList<String>();

		if (WUtils.isEmpty(cantidad)) {
			messages.add("Debe ingresar una Cantidad");
		} else {
			BigDecimal cantidadProducto = WUtils.getValue(cantidad);
			if (cantidadProducto.doubleValue() == 0) {
				messages.add("Debe ingresar una Cantidad mayor a 0");
			}
		}

		if (editaDetalle) {
			String detalle = model.getValue(CAMPO_PRODUCTO);
			String iva = model.getValue(CAMPO_IVA);
			String precio = model.getValue(CAMPO_PRECIO);
			if (WUtils.isEmpty(detalle)) {
				messages.add("Debe ingresar un Detalle");
			}
			if (WUtils.isEmpty(iva)) {
				messages.add("Debe ingresar un % de IVA");
			}
			if (WUtils.isEmpty(precio)) {
				messages.add("Debe ingresar un Precio");
			}
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
			pnlBusqueda.setBounds(10, 11, 486, 168);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getLblCantidad());
			pnlBusqueda.add(getTxfCantidad());
			pnlBusqueda.add(getTxtProductoSeleccionado());
			pnlBusqueda.add(getLblProductoSeleccionado());
			pnlBusqueda.add(getTxfPrecio());
			pnlBusqueda.add(getLblPrecio());
			pnlBusqueda.add(getCmbTipoIva());
			pnlBusqueda.add(getLblIva());
			pnlBusqueda.add(getLblPorcIVA());
			pnlBusqueda.add(getLblProducto());
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
			btnCerrar.setIcon(new ImageIcon(
					EditarDetalleComprobanteIFrame.class
							.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(280, 190, 103, 30);
		}
		return btnCerrar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setIcon(new ImageIcon(
					EditarDetalleComprobanteIFrame.class
							.getResource("/icons/ok.png")));
			btnGuardar.setBounds(393, 190, 103, 30);
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					WModel model = populateModel();
					if (validateModel(model)) {
						String cantidad = model.getValue(CAMPO_CANTIDAD);
						String precioTxt = model.getValue(CAMPO_PRECIO);

						if (editaDetalle) {
							String detalleDescripcion = model
									.getValue(CAMPO_PRODUCTO);
							WOption tipoIva = model.getValue(CAMPO_IVA);
							detalle.setDetalle(detalleDescripcion);
							detalle.setTipoIVA(TipoIVAEnum.fromValue(tipoIva
									.getValue().intValue()));
						}

						BigDecimal importeDecimal = BigDecimal.valueOf(100)
								.add(detalle.getTipoIVA().getImporte());

						BigDecimal totalConIVA = WUtils.getValue(precioTxt);

						BigDecimal total = totalConIVA.multiply(
								BigDecimal.valueOf(100)).divide(importeDecimal,
								2, RoundingMode.HALF_UP);

						detalle.setPrecio(WUtils.getValue(
								total.toEngineeringString(), 4));
						detalle.setCantidad(WUtils.getValue(cantidad));

						comprobanteVerIFrame.refreshDetalles();
						hideFrame();
					}

				}
			});
		}
		return btnGuardar;
	}

	@Override
	protected JComponent getFocusComponent() {
		return getTxfPrecio();
	}

	private JLabel getLblCantidad() {
		if (lblCantidad == null) {
			lblCantidad = new JLabel("* Cantidad:");
			lblCantidad.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCantidad.setBounds(10, 129, 121, 25);
		}
		return lblCantidad;
	}

	private WTextFieldNumeric getTxfCantidad() {
		if (txfCantidad == null) {
			txfCantidad = new WTextFieldNumeric();
			txfCantidad.setBounds(141, 129, 121, 25);
			txfCantidad.setName(CAMPO_CANTIDAD);
		}
		return txfCantidad;
	}

	private JTextField getTxtProductoSeleccionado() {
		if (txtProductoSeleccionado == null) {
			txtProductoSeleccionado = new JTextField();
			txtProductoSeleccionado.setBounds(141, 21, 331, 25);
			txtProductoSeleccionado.setColumns(10);
			txtProductoSeleccionado.setName(CAMPO_PRODUCTO);
			txtProductoSeleccionado.setDocument(new WTextFieldLimit(100));
		}
		return txtProductoSeleccionado;
	}

	private JLabel getLblProductoSeleccionado() {
		if (lblProductoSeleccionado == null) {
			lblProductoSeleccionado = new JLabel("Producto:");
			lblProductoSeleccionado
					.setHorizontalAlignment(SwingConstants.RIGHT);
			lblProductoSeleccionado.setBounds(10, 21, 121, 25);
		}
		return lblProductoSeleccionado;
	}

	private WTextFieldDecimal getTxfPrecio() {
		if (txfPrecio == null) {
			txfPrecio = new WTextFieldDecimal(10, 2);
			txfPrecio.setName(CAMPO_PRECIO);
			txfPrecio.setBounds(141, 57, 121, 25);
		}
		return txfPrecio;
	}

	private JLabel getLblPrecio() {
		if (lblPrecio == null) {
			lblPrecio = new JLabel("Precio:");
			lblPrecio.setHorizontalAlignment(SwingConstants.RIGHT);
			lblPrecio.setBounds(10, 57, 121, 25);
		}
		return lblPrecio;
	}

	private JComboBox getCmbTipoIva() {
		if (cmbTipoIva == null) {
			cmbTipoIva = new JComboBox();
			cmbTipoIva.setName(CAMPO_IVA);
			cmbTipoIva.setBounds(141, 93, 121, 25);
		}
		return cmbTipoIva;
	}

	private JLabel getLblIva() {
		if (lblIva == null) {
			lblIva = new JLabel("IVA:");
			lblIva.setHorizontalAlignment(SwingConstants.RIGHT);
			lblIva.setBounds(10, 93, 121, 25);
		}
		return lblIva;
	}

	private JLabel getLblPorcIVA() {
		if (lblPorcIVA == null) {
			lblPorcIVA = new JLabel("");
			lblPorcIVA.setBounds(141, 93, 121, 25);
			lblPorcIVA.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return lblPorcIVA;
	}

	private JLabel getLblProducto() {
		if (lblProducto == null) {
			lblProducto = new JLabel("");
			lblProducto.setBounds(141, 21, 331, 25);
			lblProducto.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return lblProducto;
	}

	@Override
	public void enterPressed() {
		getBtnGuardar().doClick();
	}
}
