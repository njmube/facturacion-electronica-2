package ar.com.wuik.sistema.frames;

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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ar.com.wuik.sistema.bo.ReciboBO;
import ar.com.wuik.sistema.entities.Cheque;
import ar.com.wuik.sistema.entities.Comprobante;
import ar.com.wuik.sistema.entities.PagoReciboCheque;
import ar.com.wuik.sistema.entities.PagoReciboEfectivo;
import ar.com.wuik.sistema.entities.Recibo;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.model.DetalleLiquidacionModel;
import ar.com.wuik.sistema.model.PagoReciboChequeModel;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WTextFieldDecimal;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.components.table.WTablePanel;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.frames.WCalendarIFrame;
import ar.com.wuik.swing.utils.WFrameUtils;
import ar.com.wuik.swing.utils.WFrameUtils.FontSize;
import ar.com.wuik.swing.utils.WUtils;

import com.lowagie.text.Font;

public class ReciboVistaIFrame extends WAbstractModelIFrame {
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6838619883125511589L;
	private static final String CAMPO_FECHA_EMISION = "fechaEmision";
	private static final String CAMPO_OBSERVACIONES = "observaciones";
	private static final String CAMPO_NUMERO = "numero";
	private static final String CAMPO_EFECTIVO = "efectivo";
	private JPanel pnlBusqueda;
	private JButton btnCerrar;
	private Recibo recibo;
	private JLabel txtFechaEmision;
	private JLabel lblFechaEmisin;
	private JLabel lblObservaciones;
	private JTextArea txaObservaciones;
	private JScrollPane scrollPane;
	private JLabel txtTotalLiquidacion;
	private JLabel lblTotalLiq;
	private JLabel lblTotal;
	private JLabel txtTotalPesos;
	private WTablePanel<Comprobante> tblLiquidacion;
	private WTablePanel<PagoReciboCheque> tblCheques;
	private JLabel txfEfectivo;
	private JLabel lblEfectivo;
	private JLabel lblNro;
	private JLabel txtNro;

	/**
	 * @wbp.parser.constructor
	 */
	public ReciboVistaIFrame(Long idRecibo) {

		initialize("Ver Recibo");

		WModel model = populateModel();
		ReciboBO reciboBO = AbstractFactory.getInstance(ReciboBO.class);
		try {
			this.recibo = reciboBO.obtener(idRecibo);
			model.addValue(CAMPO_FECHA_EMISION,
					WUtils.getStringFromDate(recibo.getFecha()));
			model.addValue(CAMPO_NUMERO, recibo.getNumero());
			model.addValue(CAMPO_EFECTIVO, obtenerPagoEfectivo());
			model.addValue(CAMPO_OBSERVACIONES, recibo.getObservaciones());
			populateComponents(model);
			refreshLiquidaciones();
			refreshPagosCheques();
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	private BigDecimal obtenerPagoEfectivo() {
		List<PagoReciboEfectivo> pagosEfectivo = recibo.getPagosEfectivo();
		if (WUtils.isNotEmpty(pagosEfectivo)) {
			PagoReciboEfectivo pagoReciboEfectivo = pagosEfectivo.get(0);
			return pagoReciboEfectivo.getTotal();
		}
		return BigDecimal.ZERO;
	}

	private void initialize(String title) {
		setTitle(title);
		setBorder(new LineBorder(null, 1, true));
		setFrameIcon(new ImageIcon(
				ReciboVistaIFrame.class.getResource("/icons/recibo.png")));
		setBounds(0, 0, 926, 505);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getBtnCerrar());
	}

	@Override
	protected boolean validateModel(WModel model) {
		return Boolean.FALSE;
	}

	private JPanel getPnlBusqueda() {
		if (pnlBusqueda == null) {
			pnlBusqueda = new JPanel();
			pnlBusqueda.setBorder(new TitledBorder(UIManager
					.getBorder("TitledBorder.border"), "Datos",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlBusqueda.setBounds(10, 11, 904, 421);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getTxtFechaEmision());
			pnlBusqueda.add(getLblFechaEmisin());
			pnlBusqueda.add(getLblObservaciones());
			pnlBusqueda.add(getScrollPane());
			pnlBusqueda.add(getTxtTotalLiquidacion());
			pnlBusqueda.add(getLblTotalLiq());
			pnlBusqueda.add(getLblTotal());
			pnlBusqueda.add(getTxtTotalPesos());
			pnlBusqueda.add(getTblLiquidacion());
			pnlBusqueda.add(getTblCheques());
			pnlBusqueda.add(getTxfEfectivo());
			pnlBusqueda.add(getLblEfectivo());
			pnlBusqueda.add(getLblNro());
			pnlBusqueda.add(getTxtNro());
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
			btnCerrar.setIcon(new ImageIcon(ReciboVistaIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(811, 437, 103, 30);
		}
		return btnCerrar;
	}

	@Override
	protected JComponent getFocusComponent() {
		return getTxfEfectivo();
	}

	protected List<Long> getIdsCheques() {
		List<Long> ids = new ArrayList<Long>();
		List<PagoReciboCheque> pagoCheques = recibo.getPagosCheque();
		for (PagoReciboCheque pagoReciboCheque : pagoCheques) {
			ids.add(pagoReciboCheque.getCheque().getId());
		}
		return ids;
	}

	protected void refreshPagosCheques() {
		getTblCheques().addData(recibo.getPagosCheque());
		calcularTotales();
	}

	protected PagoReciboCheque getPagoChequeById(Long selectedItem) {
		List<PagoReciboCheque> pagoCheques = recibo.getPagosCheque();
		for (PagoReciboCheque pagoReciboCheque : pagoCheques) {
			if (selectedItem.equals(pagoReciboCheque.getCoalesceId())) {
				return pagoReciboCheque;
			}
		}
		return null;
	}

	private JLabel getTxtFechaEmision() {
		if (txtFechaEmision == null) {
			txtFechaEmision = new JLabel();
			txtFechaEmision.setHorizontalAlignment(SwingConstants.LEFT);
			txtFechaEmision.setName(CAMPO_FECHA_EMISION);
			txtFechaEmision.setBounds(141, 61, 106, 25);
			txtFechaEmision.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return txtFechaEmision;
	}

	private JLabel getLblFechaEmisin() {
		if (lblFechaEmisin == null) {
			lblFechaEmisin = new JLabel("Fecha Emisi\u00F3n:");
			lblFechaEmisin.setIcon(new ImageIcon(ReciboVistaIFrame.class.getResource("/icons/calendar.png")));
			lblFechaEmisin.setHorizontalAlignment(SwingConstants.RIGHT);
			lblFechaEmisin.setBounds(10, 61, 121, 25);
		}
		return lblFechaEmisin;
	}

	private JLabel getLblObservaciones() {
		if (lblObservaciones == null) {
			lblObservaciones = new JLabel("Observaciones:");
			lblObservaciones.setIcon(new ImageIcon(ReciboVistaIFrame.class.getResource("/icons/observaciones.png")));
			lblObservaciones.setHorizontalAlignment(SwingConstants.RIGHT);
			lblObservaciones.setBounds(445, 26, 116, 25);
		}
		return lblObservaciones;
	}

	private JTextArea getTxaObservaciones() {
		if (txaObservaciones == null) {
			txaObservaciones = new JTextArea();
			txaObservaciones.setEnabled(false);
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
			scrollPane.setBounds(571, 25, 187, 43);
			scrollPane.setViewportView(getTxaObservaciones());
		}
		return scrollPane;
	}

	protected Comprobante getComprobanteById(Long selectedItem) {
		for (Comprobante comprobante : recibo.getComprobantes()) {
			if (comprobante.getId().equals(selectedItem)) {
				return comprobante;
			}
		}
		return null;
	}

	private JLabel getTxtTotalLiquidacion() {
		if (txtTotalLiquidacion == null) {
			txtTotalLiquidacion = new JLabel();
			txtTotalLiquidacion.setHorizontalAlignment(SwingConstants.LEFT);
			txtTotalLiquidacion.setText("$ 0.00");
			txtTotalLiquidacion.setBounds(344, 384, 125, 25);
			txtTotalLiquidacion.setFont(WFrameUtils.getCustomFont(
					FontSize.LARGE, Font.BOLD));
		}
		return txtTotalLiquidacion;
	}

	private JLabel getLblTotalLiq() {
		if (lblTotalLiq == null) {
			lblTotalLiq = new JLabel("Total a Liquidar: $");
			lblTotalLiq.setHorizontalAlignment(SwingConstants.RIGHT);
			lblTotalLiq.setBounds(209, 384, 125, 25);
		}
		return lblTotalLiq;
	}

	private JLabel getLblTotal() {
		if (lblTotal == null) {
			lblTotal = new JLabel("Total Entrega: $");
			lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
			lblTotal.setBounds(638, 384, 121, 25);
		}
		return lblTotal;
	}

	private JLabel getTxtTotalPesos() {
		if (txtTotalPesos == null) {
			txtTotalPesos = new JLabel();
			txtTotalPesos.setText("0.00");
			txtTotalPesos.setHorizontalAlignment(SwingConstants.LEFT);
			txtTotalPesos.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			txtTotalPesos.setBounds(769, 384, 125, 25);
		}
		return txtTotalPesos;
	}

	private WTablePanel<Comprobante> getTblLiquidacion() {
		if (tblLiquidacion == null) {
			tblLiquidacion = new WTablePanel(DetalleLiquidacionModel.class,
					"Liquidación");
			tblLiquidacion.setBounds(10, 122, 459, 250);
		}
		return tblLiquidacion;
	}

	private WTablePanel<PagoReciboCheque> getTblCheques() {
		if (tblCheques == null) {
			tblCheques = new WTablePanel(PagoReciboChequeModel.class, "Cheques");
			tblCheques.setBounds(479, 171, 415, 202);
		}
		return tblCheques;
	}

	private void calcularTotales() {

		BigDecimal total = BigDecimal.ZERO;

		BigDecimal efectivo = WUtils.getValue(getTxfEfectivo().getText());
		
		BigDecimal cheques = BigDecimal.ZERO;
		List<PagoReciboCheque> pagoCheques = recibo.getPagosCheque();
		for (PagoReciboCheque pagoReciboCheque : pagoCheques) {
			cheques = cheques.add(pagoReciboCheque.getCheque().getImporte());
		}

		// TOTALES ENTREGA.
		total = total.add(efectivo);
		total = total.add(cheques);
		recibo.setTotal(total);
		getTxtTotalPesos().setText(recibo.getTotal().toEngineeringString());

		// TOTAL LIQUIDACION
		BigDecimal totalLiquidacion = BigDecimal.ZERO;
		for (Comprobante liquidacion : recibo.getComprobantes()) {
			totalLiquidacion = totalLiquidacion.add(liquidacion.getTotal());
		}
		getTxtTotalLiquidacion()
				.setText(totalLiquidacion.toEngineeringString());
	}

	private JLabel getTxfEfectivo() {
		if (txfEfectivo == null) {
			txfEfectivo = new JLabel("New label");
			txfEfectivo.setHorizontalAlignment(SwingConstants.LEFT);
			txfEfectivo.setName(CAMPO_EFECTIVO);
			txfEfectivo.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			txfEfectivo.setBounds(565, 133, 125, 25);
		}
		return txfEfectivo;
	}

	private JLabel getLblEfectivo() {
		if (lblEfectivo == null) {
			lblEfectivo = new JLabel("Efectivo: $");
			lblEfectivo.setHorizontalAlignment(SwingConstants.LEFT);
			lblEfectivo.setBounds(479, 133, 76, 25);
		}
		return lblEfectivo;
	}

	private JLabel getLblNro() {
		if (lblNro == null) {
			lblNro = new JLabel("N\u00FAmero:");
			lblNro.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNro.setBounds(10, 25, 121, 25);
		}
		return lblNro;
	}

	private JLabel getTxtNro() {
		if (txtNro == null) {
			txtNro = new JLabel();
			txtNro.setHorizontalAlignment(SwingConstants.LEFT);
			txtNro.setBounds(141, 24, 106, 26);
			txtNro.setName(CAMPO_NUMERO);
			txtNro.setFont(WFrameUtils.getCustomFont(FontSize.LARGE, Font.BOLD));
		}
		return txtNro;
	}

	public void addCheques(List<Cheque> cheques) {
		List<PagoReciboCheque> pagoCheques = recibo.getPagosCheque();
		PagoReciboCheque pagoReciboCheque = null;
		Long temporalId = System.currentTimeMillis();
		for (Cheque cheque : cheques) {
			pagoReciboCheque = new PagoReciboCheque();
			pagoReciboCheque.setTemporalId(temporalId);
			pagoReciboCheque.setCheque(cheque);
			pagoReciboCheque.setRecibo(recibo);
			pagoCheques.add(pagoReciboCheque);
			temporalId++;
		}
		getTblCheques().addData(pagoCheques);
		calcularTotales();
	}

	public void addCheque(Cheque cheque) {
		List<PagoReciboCheque> pagoCheques = recibo.getPagosCheque();
		PagoReciboCheque pagoReciboCheque = new PagoReciboCheque();
		pagoReciboCheque.setTemporalId(System.currentTimeMillis());
		pagoReciboCheque.setCheque(cheque);
		pagoReciboCheque.setRecibo(recibo);
		pagoCheques.add(pagoReciboCheque);
		getTblCheques().addData(pagoCheques);
		calcularTotales();
	}

	public void addComprobantes(List<Comprobante> comprobantes) {
		this.recibo.getComprobantes().addAll(comprobantes);
		refreshLiquidaciones();
	}

	protected void refreshLiquidaciones() {
		getTblLiquidacion().addData(this.recibo.getComprobantes());
		calcularTotales();
	}

	private List<Long> getIdsComprobantes() {
		List<Long> ids = new ArrayList<Long>();
		for (Comprobante comprobante : recibo.getComprobantes()) {
			ids.add(comprobante.getId());
		}
		return ids;
	}
}
