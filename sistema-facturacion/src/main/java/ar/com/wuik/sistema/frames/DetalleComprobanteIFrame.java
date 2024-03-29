package ar.com.wuik.sistema.frames;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ar.com.wuik.sistema.bo.ProductoBO;
import ar.com.wuik.sistema.entities.DetalleComprobante;
import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.sistema.utils.AppUtils;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WTextFieldDecimal;
import ar.com.wuik.swing.components.WTextFieldNumeric;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.utils.WFrameUtils;
import ar.com.wuik.swing.utils.WFrameUtils.FontSize;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;

public class DetalleComprobanteIFrame extends WAbstractModelIFrame {
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
	private JLabel txtProductoSeleccionado;
	private JLabel lblProductoSeleccionado;
	private ComprobanteVerIFrame comprobanteVerIFrame;
	private boolean editaDetalle = false;
	private WTextFieldDecimal txfPrecio;
	private JLabel lblPrecio;
	private JLabel lblTotal;
	private JLabel txtTotal;
	private JLabel lblIVA;
	private JLabel txtIVA;

	/**
	 * @wbp.parser.constructor
	 */
	public DetalleComprobanteIFrame(Long idProducto,
			ComprobanteVerIFrame comprobanteVerIFrame) {
		this.detalle = new DetalleComprobante();

		ProductoBO productoBO = AbstractFactory.getInstance(ProductoBO.class);
		try {
			Producto producto = productoBO.obtener(idProducto);
			detalle.setTemporalId(System.currentTimeMillis());
			detalle.setProducto(producto);
			detalle.setTipoIVA(producto.getTipoIVA());
			detalle.setCantidad(1);
			this.comprobanteVerIFrame = comprobanteVerIFrame;
			initialize("Nuevo Detalle Producto");
			WModel model = populateModel();
			model.addValue(CAMPO_CANTIDAD, detalle.getCantidad());
			model.addValue(CAMPO_IVA, detalle.getTipoIVA().getDescripcion());
			model.addValue(
					CAMPO_PRODUCTO,
					(null != detalle.getProducto()) ? detalle.getProducto()
							.getCodigo()
							+ " "
							+ detalle.getProducto().getDescripcion() : detalle
							.getDetalle());
			populateComponents(model);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	private void initialize(String title) {
		setTitle(title);
		setBorder(new LineBorder(null, 1, true));
		setFrameIcon(new ImageIcon(
				DetalleComprobanteIFrame.class
						.getResource("/icons/productos.png")));
		setBounds(0, 0, 508, 297);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getBtnCerrar());
		getContentPane().add(getBtnGuardar());
	}

	@Override
	protected boolean validateModel(WModel model) {

		String cantidad = model.getValue(CAMPO_CANTIDAD);
		String precio = model.getValue(CAMPO_PRECIO);

		List<String> messages = new ArrayList<String>();

		if (WUtils.isEmpty(cantidad)) {
			messages.add("Debe ingresar una Cantidad");
		} else {
			Integer cantidadProducto = Integer.valueOf(cantidad);
			if (cantidadProducto == 0) {
				messages.add("Debe ingresar una Cantidad mayor a 0");
			}
		}

		if (WUtils.isEmpty(precio)) {
			messages.add("Debe ingresar un Precio");
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
			pnlBusqueda.setBounds(10, 11, 486, 205);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getLblCantidad());
			pnlBusqueda.add(getTxfCantidad());
			pnlBusqueda.add(getTxtProductoSeleccionado());
			pnlBusqueda.add(getLblProductoSeleccionado());
			pnlBusqueda.add(getTxfPrecio());
			pnlBusqueda.add(getLblPrecio());
			pnlBusqueda.add(getLblTotal());
			pnlBusqueda.add(getTxtTotal());
			pnlBusqueda.add(getLblIVA());
			pnlBusqueda.add(getTxtIVA());
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
			btnCerrar.setIcon(new ImageIcon(DetalleComprobanteIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(280, 227, 103, 30);
		}
		return btnCerrar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Aceptar");
			btnGuardar.setIcon(new ImageIcon(DetalleComprobanteIFrame.class
					.getResource("/icons/ok.png")));
			btnGuardar.setBounds(393, 227, 103, 30);
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					WModel model = populateModel();
					if (validateModel(model)) {
						String cantidad = model.getValue(CAMPO_CANTIDAD);
						String precioTxt = model.getValue(CAMPO_PRECIO);

						BigDecimal iva = detalle.getTipoIVA().getImporte()
								.add(new BigDecimal(100));

						BigDecimal precio = WUtils.getValue(precioTxt);

						detalle.setPrecio(WUtils.getValue(precio));
						detalle.setCantidad(WUtils.getValue(cantidad)
								.intValue());
						comprobanteVerIFrame.addDetalle(detalle);
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
			txfCantidad.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					calcularTotales();
				}
			});
		}
		return txfCantidad;
	}

	private JLabel getTxtProductoSeleccionado() {
		if (txtProductoSeleccionado == null) {
			txtProductoSeleccionado = new JLabel();
			txtProductoSeleccionado.setBounds(141, 21, 331, 25);
			txtProductoSeleccionado.setName(CAMPO_PRODUCTO);
			txtProductoSeleccionado.setFont(WFrameUtils.getCustomFont(
					FontSize.LARGE, Font.BOLD));
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
			txfPrecio.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					calcularTotales();
				}
			});
			txfPrecio.setName(CAMPO_PRECIO);
			txfPrecio.setBounds(141, 93, 121, 25);
		}
		return txfPrecio;
	}

	private JLabel getLblPrecio() {
		if (lblPrecio == null) {
			lblPrecio = new JLabel("* Precio:");
			lblPrecio.setHorizontalAlignment(SwingConstants.RIGHT);
			lblPrecio.setBounds(10, 93, 121, 25);
		}
		return lblPrecio;
	}

	private JLabel getLblTotal() {
		if (lblTotal == null) {
			lblTotal = new JLabel("Total:");
			lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
			lblTotal.setBounds(10, 167, 121, 25);
		}
		return lblTotal;
	}

	private JLabel getTxtTotal() {
		if (txtTotal == null) {
			txtTotal = new JLabel();
			txtTotal.setHorizontalAlignment(SwingConstants.RIGHT);
			txtTotal.setBounds(141, 167, 121, 25);
			txtTotal.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return txtTotal;
	}

	private void calcularTotales() {
		WModel model = populateModel();
		String cantidadTxt = model.getValue(CAMPO_CANTIDAD);
		String precioTxt = model.getValue(CAMPO_PRECIO);

		BigDecimal cantidad = WUtils.getValueNullZero(cantidadTxt);
		BigDecimal precio = WUtils.getValueNullZero(precioTxt);

		BigDecimal total = precio.multiply(cantidad);

		getTxtTotal().setText(AppUtils.formatPeso(WUtils.getValue(total)));
	}

	private JLabel getLblIVA() {
		if (lblIVA == null) {
			lblIVA = new JLabel("IVA:");
			lblIVA.setHorizontalAlignment(SwingConstants.RIGHT);
			lblIVA.setBounds(10, 57, 121, 25);
		}
		return lblIVA;
	}

	private JLabel getTxtIVA() {
		if (txtIVA == null) {
			txtIVA = new JLabel();
			txtIVA.setName(CAMPO_IVA);
			txtIVA.setFont(WFrameUtils.getCustomFont(FontSize.LARGE, Font.BOLD));
			txtIVA.setBounds(141, 57, 219, 25);
		}
		return txtIVA;
	}

	@Override
	public void enterPressed() {
		getBtnGuardar().doClick();
	}
}
