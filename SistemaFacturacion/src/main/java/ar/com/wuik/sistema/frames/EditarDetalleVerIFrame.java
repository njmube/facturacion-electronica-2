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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.sistema.entities.DetalleFactura;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WTextFieldNumeric;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;

public class EditarDetalleVerIFrame extends WAbstractModelIFrame {
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6838619883125511589L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(EditarDetalleVerIFrame.class);
	private JPanel pnlBusqueda;
	private JButton btnCerrar;
	private DetalleFactura detalle;
	private JButton btnGuardar;

	private static final String CAMPO_CANTIDAD = "cantidad";
	private static final String CAMPO_PRODUCTO = "producto";
	private JLabel lblCantidad;
	private WTextFieldNumeric txfCantidad;
	private JTextField txtProductoSeleccionado;
	private JLabel lblProductoSeleccionado;
	private Integer cantidadRemitida;
	private VentaClienteVerIFrame ventaClienteVerIFrame;

	/**
	 * @wbp.parser.constructor
	 */
	public EditarDetalleVerIFrame(DetalleFactura detalle,
			Integer cantidadRemitida,
			VentaClienteVerIFrame ventaClienteVerIFrame) {
		this.detalle = detalle;
		this.ventaClienteVerIFrame = ventaClienteVerIFrame;
		this.cantidadRemitida = cantidadRemitida;
		initialize("Editar Detalle");
		WModel model = populateModel();
		model.addValue(CAMPO_CANTIDAD, detalle.getCantidad());
		model.addValue(CAMPO_PRODUCTO, detalle.getProducto().getCodigo() + " "
				+ detalle.getProducto().getDescripcion());
		populateComponents(model);
	}

	private void initialize(String title) {
		setTitle(title);
		setBorder(new LineBorder(null, 1, true));
		setFrameIcon(new ImageIcon(
				EditarDetalleVerIFrame.class
						.getResource("/icons/productos.png")));
		setBounds(0, 0, 508, 181);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getBtnCerrar());
		getContentPane().add(getBtnGuardar());
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
			} else {
				if (null != cantidadRemitida) {

					if (cantidadProducto.intValue() < cantidadRemitida) {
						messages.add("Debe ingresar una Cantidad igual o mayor a la Remitida");
					}
				}
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
			pnlBusqueda.setBounds(10, 11, 486, 96);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getLblCantidad());
			pnlBusqueda.add(getTxfCantidad());
			pnlBusqueda.add(getTxtProductoSeleccionado());
			pnlBusqueda.add(getLblProductoSeleccionado());
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
			btnCerrar.setIcon(new ImageIcon(EditarDetalleVerIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(280, 117, 103, 25);
		}
		return btnCerrar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setIcon(new ImageIcon(EditarDetalleVerIFrame.class
					.getResource("/icons/ok.png")));
			btnGuardar.setBounds(393, 117, 103, 25);
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					WModel model = populateModel();
					if (validateModel(model)) {
						String cantidad = model.getValue(CAMPO_CANTIDAD);
						detalle.setCantidad(WUtils.getValue(cantidad)
								.intValue());
						ventaClienteVerIFrame.refreshDetalles();
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

	@Override
	public void enterPressed() {
		getBtnGuardar().doClick();
	}

	private JLabel getLblCantidad() {
		if (lblCantidad == null) {
			lblCantidad = new JLabel("* Cantidad:");
			lblCantidad.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCantidad.setBounds(10, 57, 121, 25);
		}
		return lblCantidad;
	}

	private WTextFieldNumeric getTxfCantidad() {
		if (txfCantidad == null) {
			txfCantidad = new WTextFieldNumeric();
			txfCantidad.setBounds(141, 57, 121, 25);
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
}
