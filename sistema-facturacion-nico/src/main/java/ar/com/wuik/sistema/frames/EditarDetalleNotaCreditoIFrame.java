package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ar.com.wuik.sistema.entities.DetalleNotaCredito;
import ar.com.wuik.sistema.entities.enums.TipoIVAEnum;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WOption;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.components.WTextFieldNumeric;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;
import javax.swing.JComboBox;

public class EditarDetalleNotaCreditoIFrame extends WAbstractModelIFrame {
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6838619883125511589L;
	private JPanel pnlBusqueda;
	private JButton btnCerrar;
	private DetalleNotaCredito detalle;
	private JButton btnGuardar;

	private static final String CAMPO_CANTIDAD = "cantidad";
	private static final String CAMPO_PRODUCTO = "producto";
	private static final String CAMPO_PRECIO = "precio";
	private static final String CAMPO_IVA = "iva";
	private static final String CAMPO_COMENTARIO = "comentario";
	private JLabel lblCantidad;
	private WTextFieldNumeric txfCantidad;
	private JTextField txtProductoSeleccionado;
	private JLabel lblProductoSeleccionado;
	private NotaCreditoVerIFrame notaCreditoVerIFrame;
	private JTextField txtComentario;
	private JLabel lblComentario;
	private boolean editaDetalle;
	private JLabel lblPrecio;
	private WTextFieldNumeric txfPrecio;
	private JLabel lblIva;
	private JComboBox cmbTipoIva;

	/**
	 * @wbp.parser.constructor
	 */
	public EditarDetalleNotaCreditoIFrame(DetalleNotaCredito detalle,
			NotaCreditoVerIFrame notaCreditoVerIFrame) {
		this.detalle = detalle;
		this.notaCreditoVerIFrame = notaCreditoVerIFrame;
		initialize("Editar Detalle");
		WModel model = populateModel();
		model.addValue(CAMPO_CANTIDAD, detalle.getCantidad());
		model.addValue(CAMPO_PRECIO, detalle.getPrecio());
		model.addValue(CAMPO_IVA, detalle.getTipoIVA().getId());
		model.addValue(
				CAMPO_PRODUCTO,
				(null != detalle.getProducto()) ? detalle.getProducto()
						.getCodigo()
						+ " "
						+ detalle.getProducto().getDescripcion() : detalle
						.getDetalle());
		model.addValue(CAMPO_COMENTARIO, detalle.getComentario());
		editaDetalle = null == detalle.getProducto();
		if (editaDetalle) {
			getLblProductoSeleccionado().setText("* Detalle:");
			getTxtProductoSeleccionado().setEditable(Boolean.TRUE);
			getLblIva().setText("* " + getLblIva().getText());
			getCmbTipoIva().setEditable(Boolean.TRUE);
			getLblPrecio().setText("* " + getLblPrecio().getText());
			getTxfPrecio().setEditable(Boolean.TRUE);
		}

		populateComponents(model);
	}

	private void initialize(String title) {
		setTitle(title);
		setBorder(new LineBorder(null, 1, true));
		setFrameIcon(new ImageIcon(
				EditarDetalleNotaCreditoIFrame.class
						.getResource("/icons/productos.png")));
		setBounds(0, 0, 508, 286);
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
			Integer cantidadProducto = Integer.valueOf(cantidad);
			if (cantidadProducto == 0) {
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
			pnlBusqueda.setBounds(10, 11, 486, 202);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getLblCantidad());
			pnlBusqueda.add(getTxfCantidad());
			pnlBusqueda.add(getTxtProductoSeleccionado());
			pnlBusqueda.add(getLblProductoSeleccionado());
			pnlBusqueda.add(getTxtComentario());
			pnlBusqueda.add(getLblComentario());
			pnlBusqueda.add(getLblPrecio());
			pnlBusqueda.add(getTxfPrecio());
			pnlBusqueda.add(getLblIva());
			pnlBusqueda.add(getCmbTipoIva());
		}
		return pnlBusqueda;
	}

	private JButton getBtnCerrar() {
		if (btnCerrar == null) {
			btnCerrar = new JButton("Cerrar");
			btnCerrar.setFocusable(false);
			btnCerrar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					hideFrame();
				}
			});
			btnCerrar.setIcon(new ImageIcon(
					EditarDetalleNotaCreditoIFrame.class
							.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(280, 224, 103, 25);
		}
		return btnCerrar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setIcon(new ImageIcon(
					EditarDetalleNotaCreditoIFrame.class
							.getResource("/icons/ok.png")));
			btnGuardar.setBounds(393, 224, 103, 25);
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					WModel model = populateModel();
					if (validateModel(model)) {
						String cantidad = model.getValue(CAMPO_CANTIDAD);
						String comentario = model.getValue(CAMPO_COMENTARIO);

						detalle.setCantidad(WUtils.getValue(cantidad)
								.intValue());

						if (editaDetalle) {
							String detalleDescripcion = model
									.getValue(CAMPO_PRODUCTO);
							String precio = model.getValue(CAMPO_PRECIO);
							WOption tipoIva = model.getValue(CAMPO_IVA);
							detalle.setDetalle(detalleDescripcion);
							detalle.setPrecio(WUtils.getValue(precio));
							detalle.setTipoIVA(TipoIVAEnum.fromValue(tipoIva.getValue().intValue()));
						}
						detalle.setComentario(comentario);
						notaCreditoVerIFrame.refreshDetalles();
						hideFrame();
					}

				}
			});
		}
		return btnGuardar;
	}

	@Override
	protected JComponent getFocusComponent() {
		return getTxfCantidad();
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
			txtProductoSeleccionado.setEditable(Boolean.FALSE);
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

	private JTextField getTxtComentario() {
		if (txtComentario == null) {
			txtComentario = new JTextField();
			txtComentario.setName(CAMPO_COMENTARIO);
			txtComentario.setBounds(141, 165, 331, 25);
			txtComentario.setDocument(new WTextFieldLimit(100));
		}
		return txtComentario;
	}

	private JLabel getLblComentario() {
		if (lblComentario == null) {
			lblComentario = new JLabel("Comentario:");
			lblComentario.setHorizontalAlignment(SwingConstants.RIGHT);
			lblComentario.setBounds(10, 165, 121, 25);
		}
		return lblComentario;
	}
	private JLabel getLblPrecio() {
		if (lblPrecio == null) {
			lblPrecio = new JLabel("Precio:");
			lblPrecio.setHorizontalAlignment(SwingConstants.RIGHT);
			lblPrecio.setBounds(10, 57, 121, 25);
		}
		return lblPrecio;
	}
	private WTextFieldNumeric getTxfPrecio() {
		if (txfPrecio == null) {
			txfPrecio = new WTextFieldNumeric();
			txfPrecio.setName(CAMPO_PRECIO);
			txfPrecio.setBounds(141, 57, 121, 25);
		}
		return txfPrecio;
	}
	private JLabel getLblIva() {
		if (lblIva == null) {
			lblIva = new JLabel("IVA:");
			lblIva.setHorizontalAlignment(SwingConstants.RIGHT);
			lblIva.setBounds(10, 93, 121, 25);
		}
		return lblIva;
	}
	private JComboBox getCmbTipoIva() {
		if (cmbTipoIva == null) {
			cmbTipoIva = new JComboBox();
			cmbTipoIva.setName(CAMPO_IVA);
			cmbTipoIva.setBounds(141, 93, 121, 25);
		}
		return cmbTipoIva;
	}
}
