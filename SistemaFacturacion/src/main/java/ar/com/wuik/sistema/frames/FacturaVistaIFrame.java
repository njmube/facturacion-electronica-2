package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
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

import ar.com.wuik.sistema.bo.FacturaBO;
import ar.com.wuik.sistema.entities.DetalleFactura;
import ar.com.wuik.sistema.entities.Factura;
import ar.com.wuik.sistema.entities.Remito;
import ar.com.wuik.sistema.entities.enums.TipoIVAEnum;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.model.DetalleFacturaModel;
import ar.com.wuik.sistema.model.DetalleFacturaRemitoModel;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.components.table.WTablePanel;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.utils.WFrameUtils;
import ar.com.wuik.swing.utils.WFrameUtils.FontSize;
import ar.com.wuik.swing.utils.WUtils;

import com.lowagie.text.Font;

public class FacturaVistaIFrame extends WAbstractModelIFrame {
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6838619883125511589L;
	private static final String CAMPO_FECHA_EMISION = "fechaEmision";
	private static final String CAMPO_OBSERVACIONES = "observaciones";
	private static final String CAMPO_ESTADO = "estado";
	private static final String CAMPO_CAE = "cae";
	private static final String CAMPO_CAE_FECHA = "caeFecha";
	private JPanel pnlBusqueda;
	private JLabel lblCAE;
	private JTextField txtCAE;
	private JButton btnCerrar;
	private Factura factura;
	private JTextField txtFechaEmision;
	private JLabel lblFechaEmisin;
	private JLabel lblObservaciones;
	private JTextArea txaObservaciones;
	private JScrollPane scrollPane;
	private WTablePanel<Remito> tblRemitos;
	private JLabel lblIVA10;
	private JTextField txtSubtotalPesos;
	private JTextField txtIVA10;
	private JLabel lblSubtotal;
	private JLabel lblTotal;
	private JTextField txtTotalPesos;
	private JLabel lblVtoCAE;
	private JTextField txtFechaCAE;
	private WTablePanel<DetalleFactura> tblDetalle;
	private JLabel lblEstado;
	private JTextField txtEstado;
	private JLabel lblIVA21;
	private JTextField txtIVA21;

	/**
	 * @wbp.parser.constructor
	 */
	public FacturaVistaIFrame(Long idFactura) {
		initialize("Ver Venta");
		WModel model = populateModel();
		FacturaBO facturaBO = AbstractFactory.getInstance(FacturaBO.class);
		try {
			this.factura = facturaBO.obtener(idFactura);
			model.addValue(CAMPO_FECHA_EMISION,
					WUtils.getStringFromDate(factura.getFechaVenta()));
			model.addValue(CAMPO_OBSERVACIONES, factura.getObservaciones());
			model.addValue(CAMPO_CAE, factura.getCae());
			model.addValue(CAMPO_CAE_FECHA,
					WUtils.getStringFromDate(factura.getFechaCAE()));
			model.addValue(CAMPO_ESTADO, factura.getEstado());
			refreshDetalles();
			refreshRemitos();
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
		populateComponents(model);
	}

	protected void refreshRemitos() {
		getTblRemitos().addData(factura.getRemitos());
	}

	private void initialize(String title) {
		setTitle(title);
		setBorder(new LineBorder(null, 1, true));
		setFrameIcon(new ImageIcon(
				FacturaVistaIFrame.class.getResource("/icons/facturas.png")));
		setBounds(0, 0, 807, 468);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getBtnCerrar());
		getContentPane().add(getLblEstado());
		getContentPane().add(getTxtEstado());
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
			pnlBusqueda.setBounds(10, 11, 786, 382);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getLblCAE());
			pnlBusqueda.add(getTxtCAE());
			pnlBusqueda.add(getTxtFechaEmision());
			pnlBusqueda.add(getLblFechaEmisin());
			pnlBusqueda.add(getLblObservaciones());
			pnlBusqueda.add(getScrollPane());
			pnlBusqueda.add(getTblRemitos());
			pnlBusqueda.add(getLblIVA10());
			pnlBusqueda.add(getTxtSubtotalPesos());
			pnlBusqueda.add(getTxtIVA10());
			pnlBusqueda.add(getLblSubtotal());
			pnlBusqueda.add(getLblTotal());
			pnlBusqueda.add(getTxtTotalPesos());
			pnlBusqueda.add(getLblVtoCAE());
			pnlBusqueda.add(getTxtFechaCAE());
			pnlBusqueda.add(getTblDetalle());
			pnlBusqueda.add(getLblIVA21());
			pnlBusqueda.add(getTxtIVA21());
		}
		return pnlBusqueda;
	}

	private JLabel getLblCAE() {
		if (lblCAE == null) {
			lblCAE = new JLabel("CAE:");
			lblCAE.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCAE.setBounds(10, 23, 121, 25);
		}
		return lblCAE;
	}

	private JTextField getTxtCAE() {
		if (txtCAE == null) {
			txtCAE = new JTextField();
			txtCAE.setEditable(false);
			txtCAE.setName(CAMPO_CAE);
			txtCAE.setDocument(new WTextFieldLimit(50));
			txtCAE.setBounds(141, 23, 141, 25);
		}
		return txtCAE;
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
			btnCerrar.setIcon(new ImageIcon(FacturaVistaIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(692, 404, 103, 25);
		}
		return btnCerrar;
	}

	@Override
	protected JComponent getFocusComponent() {
		return getTxtCAE();
	}

	private JTextField getTxtFechaEmision() {
		if (txtFechaEmision == null) {
			txtFechaEmision = new JTextField();
			txtFechaEmision.setName(CAMPO_FECHA_EMISION);
			txtFechaEmision.setEditable(false);
			txtFechaEmision.setBounds(670, 23, 106, 25);
		}
		return txtFechaEmision;
	}

	private JLabel getLblFechaEmisin() {
		if (lblFechaEmisin == null) {
			lblFechaEmisin = new JLabel("Fecha Emisi\u00F3n:");
			lblFechaEmisin.setHorizontalAlignment(SwingConstants.RIGHT);
			lblFechaEmisin.setBounds(539, 23, 121, 25);
		}
		return lblFechaEmisin;
	}

	private JLabel getLblObservaciones() {
		if (lblObservaciones == null) {
			lblObservaciones = new JLabel("Observaciones:");
			lblObservaciones.setHorizontalAlignment(SwingConstants.CENTER);
			lblObservaciones.setBounds(381, 227, 164, 25);
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
			scrollPane.setBounds(381, 263, 164, 52);
			scrollPane.setViewportView(getTxaObservaciones());
		}
		return scrollPane;
	}

	private JLabel getLblIVA10() {
		if (lblIVA10 == null) {
			lblIVA10 = new JLabel("IVA 10.5%: $");
			lblIVA10.setHorizontalAlignment(SwingConstants.RIGHT);
			lblIVA10.setBounds(555, 309, 76, 25);
		}
		return lblIVA10;
	}

	private JTextField getTxtSubtotalPesos() {
		if (txtSubtotalPesos == null) {
			txtSubtotalPesos = new JTextField();
			txtSubtotalPesos.setHorizontalAlignment(SwingConstants.RIGHT);
			txtSubtotalPesos.setEditable(false);
			txtSubtotalPesos.setText("$ 0.00");
			txtSubtotalPesos.setBounds(641, 238, 125, 25);
			txtSubtotalPesos.setColumns(10);
			txtSubtotalPesos.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return txtSubtotalPesos;
	}

	private JTextField getTxtIVA10() {
		if (txtIVA10 == null) {
			txtIVA10 = new JTextField();
			txtIVA10.setText("$ 0.00");
			txtIVA10.setHorizontalAlignment(SwingConstants.RIGHT);
			txtIVA10.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			txtIVA10.setEditable(false);
			txtIVA10.setColumns(10);
			txtIVA10.setBounds(641, 309, 125, 25);
		}
		return txtIVA10;
	}

	private JLabel getLblSubtotal() {
		if (lblSubtotal == null) {
			lblSubtotal = new JLabel("Subtotal: $");
			lblSubtotal.setHorizontalAlignment(SwingConstants.RIGHT);
			lblSubtotal.setBounds(555, 238, 76, 25);
		}
		return lblSubtotal;
	}

	private JLabel getLblTotal() {
		if (lblTotal == null) {
			lblTotal = new JLabel("Total: $");
			lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
			lblTotal.setBounds(555, 345, 76, 25);
		}
		return lblTotal;
	}

	private JTextField getTxtTotalPesos() {
		if (txtTotalPesos == null) {
			txtTotalPesos = new JTextField();
			txtTotalPesos.setText("$ 0.00");
			txtTotalPesos.setHorizontalAlignment(SwingConstants.RIGHT);
			txtTotalPesos.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			txtTotalPesos.setEditable(false);
			txtTotalPesos.setColumns(10);
			txtTotalPesos.setBounds(641, 345, 125, 25);
		}
		return txtTotalPesos;
	}

	private JLabel getLblVtoCAE() {
		if (lblVtoCAE == null) {
			lblVtoCAE = new JLabel("Vto. CAE:");
			lblVtoCAE.setHorizontalAlignment(SwingConstants.RIGHT);
			lblVtoCAE.setBounds(325, 23, 76, 25);
		}
		return lblVtoCAE;
	}

	private JTextField getTxtFechaCAE() {
		if (txtFechaCAE == null) {
			txtFechaCAE = new JTextField();
			txtFechaCAE.setEditable(false);
			txtFechaCAE.setName(CAMPO_CAE_FECHA);
			txtFechaCAE.setBounds(411, 23, 106, 25);
		}
		return txtFechaCAE;
	}

	private WTablePanel<DetalleFactura> getTblDetalle() {
		if (tblDetalle == null) {
			tblDetalle = new WTablePanel(DetalleFacturaModel.class, "Detalles");
			tblDetalle.setBounds(10, 59, 766, 157);
		}
		return tblDetalle;
	}

	private JLabel getLblEstado() {
		if (lblEstado == null) {
			lblEstado = new JLabel("Estado:");
			lblEstado.setBounds(10, 404, 76, 25);
			lblEstado.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblEstado;
	}

	private JTextField getTxtEstado() {
		if (txtEstado == null) {
			txtEstado = new JTextField();
			txtEstado.setBounds(97, 404, 185, 25);
			txtEstado.setHorizontalAlignment(SwingConstants.CENTER);
			txtEstado.setName(CAMPO_ESTADO);
			txtEstado.setEditable(false);
		}
		return txtEstado;
	}

	private void calcularTotales() {

		BigDecimal subtotal = BigDecimal.ZERO;
		BigDecimal subtotalIVA21 = BigDecimal.ZERO;
		BigDecimal subtotalIVA105 = BigDecimal.ZERO;
		BigDecimal total = BigDecimal.ZERO;

		List<DetalleFactura> detalles = factura.getDetalles();
		for (DetalleFactura detalleFactura : detalles) {
			if (detalleFactura.getTipoIVA().equals(TipoIVAEnum.IVA_21)) {
				subtotalIVA21 = subtotalIVA21.add(detalleFactura.getTotalIVA());
			} else if (detalleFactura.getTipoIVA().equals(TipoIVAEnum.IVA_105)) {
				subtotalIVA105 = subtotalIVA105.add(detalleFactura
						.getTotalIVA());
			}
			subtotal = subtotal.add(detalleFactura.getSubtotal());
			total = total.add(detalleFactura.getTotal());
		}

		factura.setSubtotal(subtotal);
		factura.setTotal(total);
		factura.setIva(total.subtract(subtotal));

		getTxtSubtotalPesos().setText(
				WUtils.getValue(factura.getSubtotal()).toEngineeringString());
		getTxtIVA10().setText(
				WUtils.getValue(subtotalIVA105).toEngineeringString());
		getTxtIVA21().setText(
				WUtils.getValue(subtotalIVA21).toEngineeringString());
		getTxtTotalPesos().setText(
				WUtils.getValue(factura.getTotal()).toEngineeringString());
	}

	private JLabel getLblIVA21() {
		if (lblIVA21 == null) {
			lblIVA21 = new JLabel("IVA 21%: $");
			lblIVA21.setHorizontalAlignment(SwingConstants.RIGHT);
			lblIVA21.setBounds(555, 273, 76, 25);
		}
		return lblIVA21;
	}

	private JTextField getTxtIVA21() {
		if (txtIVA21 == null) {
			txtIVA21 = new JTextField();
			txtIVA21.setText("$ 0.00");
			txtIVA21.setHorizontalAlignment(SwingConstants.RIGHT);
			txtIVA21.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			txtIVA21.setEditable(false);
			txtIVA21.setColumns(10);
			txtIVA21.setBounds(641, 273, 125, 25);
		}
		return txtIVA21;
	}

	private WTablePanel<Remito> getTblRemitos() {
		if (tblRemitos == null) {
			tblRemitos = new WTablePanel(DetalleFacturaRemitoModel.class,
					"Remitos");
			tblRemitos.setBounds(10, 227, 361, 143);
		}
		return tblRemitos;
	}

	public void refreshDetalles() {
		getTblDetalle().addData(factura.getDetalles());
		calcularTotales();
	}
}
