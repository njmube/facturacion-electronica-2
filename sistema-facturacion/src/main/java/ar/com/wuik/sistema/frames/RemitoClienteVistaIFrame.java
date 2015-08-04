package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ar.com.wuik.sistema.bo.ProductoBO;
import ar.com.wuik.sistema.bo.RemitoBO;
import ar.com.wuik.sistema.entities.DetalleRemito;
import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.sistema.entities.Remito;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.model.DetalleRemitoModel;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.components.table.WTablePanel;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.utils.WFrameUtils;
import ar.com.wuik.swing.utils.WFrameUtils.FontSize;
import ar.com.wuik.swing.utils.WUtils;

import com.lowagie.text.Font;

public class RemitoClienteVistaIFrame extends WAbstractModelIFrame {
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6838619883125511589L;
	private static final String CAMPO_NRO_COMP = "nroComprobante";
	private static final String CAMPO_FECHA_EMISION = "fechaEmision";
	private static final String CAMPO_OBSERVACIONES = "observaciones";
	private static final String CAMPO_ESTADO = "estado";
	private static final String CAMPO_FACTURA = "factura";
	private JPanel pnlBusqueda;
	private JLabel lblNro;
	private JTextField txtNro;
	private JButton btnCerrar;
	private Remito remito;
	private JTextField txtFechaEmision;
	private JLabel lblFechaEmisin;
	private JLabel lblObservaciones;
	private JTextArea txaObservaciones;
	private JScrollPane scrollPane;
	private JTextField txtCantidad;
	private JLabel lblCantidad;
	private WTablePanel<DetalleRemito> tblDetalle;
	private JLabel lblEstado;
	private JTextField txtEstado;
	private JLabel lblFactura;
	private JTextField txtFactura;

	/**
	 * @wbp.parser.constructor
	 */
	public RemitoClienteVistaIFrame(Long idRemito) {

		WModel model = populateModel();
		initialize("Ver Remito");
		RemitoBO remitoBO = AbstractFactory.getInstance(RemitoBO.class);
		try {
			this.remito = remitoBO.obtener(idRemito);
			model.addValue(CAMPO_FECHA_EMISION,
					WUtils.getStringFromDate(remito.getFecha()));
			model.addValue(CAMPO_OBSERVACIONES, remito.getObservaciones());
			model.addValue(CAMPO_NRO_COMP, remito.getNumero());
			model.addValue(CAMPO_ESTADO, remito.isActivo() ? "ACTIVO"
					: "ANULADO");
			model.addValue(CAMPO_FACTURA, (null !=  remito.getFactura()) ? remito.getFactura().getCae() : "");
			refreshDetalles();
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());

		}
		populateComponents(model);
	}

	private void initialize(String title) {
		setTitle(title);
		setBorder(new LineBorder(null, 1, true));
		setFrameIcon(new ImageIcon(
				RemitoClienteVistaIFrame.class
						.getResource("/icons/remitos.png")));
		setBounds(0, 0, 758, 493);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getBtnCerrar());
	}

	@Override
	protected boolean validateModel(WModel model) {
		return true;
	}

	private JPanel getPnlBusqueda() {
		if (pnlBusqueda == null) {
			pnlBusqueda = new JPanel();
			pnlBusqueda.setBorder(new TitledBorder(UIManager
					.getBorder("TitledBorder.border"), "Datos",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlBusqueda.setBounds(10, 11, 734, 407);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getLblNro());
			pnlBusqueda.add(getTxtNro());
			pnlBusqueda.add(getTxtFechaEmision());
			pnlBusqueda.add(getLblFechaEmisin());
			pnlBusqueda.add(getLblObservaciones());
			pnlBusqueda.add(getScrollPane());
			pnlBusqueda.add(getTxtCantidad());
			pnlBusqueda.add(getLblCantidad());
			pnlBusqueda.add(getTblDetalle());
			pnlBusqueda.add(getLblEstado());
			pnlBusqueda.add(getTxtEstado());
			pnlBusqueda.add(getLblFactura());
			pnlBusqueda.add(getTxtFactura());
		}
		return pnlBusqueda;
	}

	private JLabel getLblNro() {
		if (lblNro == null) {
			lblNro = new JLabel("N\u00FAmero:");
			lblNro.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNro.setBounds(10, 23, 121, 25);
		}
		return lblNro;
	}

	private JTextField getTxtNro() {
		if (txtNro == null) {
			txtNro = new JTextField();
			txtNro.setEditable(false);
			txtNro.setName(CAMPO_NRO_COMP);
			txtNro.setDocument(new WTextFieldLimit(50));
			txtNro.setBounds(141, 23, 141, 25);
		}
		return txtNro;
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
			btnCerrar.setIcon(new ImageIcon(RemitoClienteVistaIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(641, 430, 103, 25);
		}
		return btnCerrar;
	}

	@Override
	protected JComponent getFocusComponent() {
		return getTxtNro();
	}

	private JTextField getTxtFechaEmision() {
		if (txtFechaEmision == null) {
			txtFechaEmision = new JTextField();
			txtFechaEmision.setName(CAMPO_FECHA_EMISION);
			txtFechaEmision.setEditable(false);
			txtFechaEmision.setBounds(141, 59, 106, 25);
		}
		return txtFechaEmision;
	}

	private JLabel getLblFechaEmisin() {
		if (lblFechaEmisin == null) {
			lblFechaEmisin = new JLabel("Fecha Emisi\u00F3n:");
			lblFechaEmisin.setHorizontalAlignment(SwingConstants.RIGHT);
			lblFechaEmisin.setBounds(10, 59, 121, 25);
		}
		return lblFechaEmisin;
	}

	private JLabel getLblObservaciones() {
		if (lblObservaciones == null) {
			lblObservaciones = new JLabel("Observaciones:");
			lblObservaciones.setHorizontalAlignment(SwingConstants.RIGHT);
			lblObservaciones.setBounds(10, 334, 93, 25);
		}
		return lblObservaciones;
	}

	private JTextArea getTxaObservaciones() {
		if (txaObservaciones == null) {
			txaObservaciones = new JTextArea();
			txaObservaciones.setEditable(false);
			txaObservaciones.setLineWrap(true);
			txaObservaciones.setName(CAMPO_OBSERVACIONES);
			txaObservaciones.setDocument(new WTextFieldLimit(100));
		}
		return txaObservaciones;
	}

	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setBounds(131, 333, 192, 60);
			scrollPane.setViewportView(getTxaObservaciones());
		}
		return scrollPane;
	}

	protected DetalleRemito getDetalleById(Long selectedItem) {
		List<DetalleRemito> detalles = remito.getDetalles();
		for (DetalleRemito detalleFactura : detalles) {
			if (detalleFactura.getCoalesceId().equals(selectedItem)) {
				return detalleFactura;
			}
		}
		return null;
	}

	private JTextField getTxtCantidad() {
		if (txtCantidad == null) {
			txtCantidad = new JTextField();
			txtCantidad.setText("0");
			txtCantidad.setHorizontalAlignment(SwingConstants.RIGHT);
			txtCantidad.setEditable(false);
			txtCantidad.setBounds(599, 368, 125, 25);
			txtCantidad.setColumns(10);
			txtCantidad.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return txtCantidad;
	}

	private JLabel getLblCantidad() {
		if (lblCantidad == null) {
			lblCantidad = new JLabel("Cantidad:");
			lblCantidad.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCantidad.setBounds(513, 368, 76, 25);
		}
		return lblCantidad;
	}

	private WTablePanel<DetalleRemito> getTblDetalle() {
		if (tblDetalle == null) {
			tblDetalle = new WTablePanel(DetalleRemitoModel.class, "Detalles");
			tblDetalle.setBounds(10, 95, 714, 227);
		}
		return tblDetalle;
	}

	private JLabel getLblEstado() {
		if (lblEstado == null) {
			lblEstado = new JLabel("Estado:");
			lblEstado.setHorizontalAlignment(SwingConstants.RIGHT);
			lblEstado.setBounds(335, 23, 76, 25);
		}
		return lblEstado;
	}

	private JTextField getTxtEstado() {
		if (txtEstado == null) {
			txtEstado = new JTextField();
			txtEstado.setName(CAMPO_ESTADO);
			txtEstado.setEditable(false);
			txtEstado.setBounds(421, 23, 178, 25);
		}
		return txtEstado;
	}

	protected void addDetalle(Long selectedId) {
		ProductoBO productoBO = AbstractFactory.getInstance(ProductoBO.class);
		try {
			Producto producto = productoBO.obtener(selectedId);
			List<DetalleRemito> detalles = remito.getDetalles();
			boolean existeEnDetalle = false;
			for (DetalleRemito detalleRemito : detalles) {
				if (detalleRemito.getProducto().getId().equals(selectedId)) {
					detalleRemito.setCantidad(detalleRemito.getCantidad() + 1);
					existeEnDetalle = true;
				}
			}
			if (!existeEnDetalle) {
				DetalleRemito detalle = new DetalleRemito();
				detalle.setCantidad(1);
				detalle.setRemito(remito);
				detalle.setProducto(producto);
				detalle.setTemporalId(System.currentTimeMillis());
				detalles.add(detalle);
			}
			getTblDetalle().addData(detalles);
			calcularTotales();
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	private void calcularTotales() {
		int cantidad = 0;
		List<DetalleRemito> detalles = remito.getDetalles();
		for (DetalleRemito detalleRemito : detalles) {
			cantidad += detalleRemito.getCantidad();
		}
		getTxtCantidad().setText(cantidad + "");
	}

	public void refreshDetalles() {
		getTblDetalle().addData(remito.getDetalles());
		calcularTotales();
	}

	private JLabel getLblFactura() {
		if (lblFactura == null) {
			lblFactura = new JLabel("Factura:");
			lblFactura.setHorizontalAlignment(SwingConstants.RIGHT);
			lblFactura.setBounds(290, 59, 121, 25);
		}
		return lblFactura;
	}

	private JTextField getTxtFactura() {
		if (txtFactura == null) {
			txtFactura = new JTextField();
			txtFactura.setName(CAMPO_FACTURA);
			txtFactura.setEditable(false);
			txtFactura.setBounds(421, 59, 178, 25);
		}
		return txtFactura;
	}
}
