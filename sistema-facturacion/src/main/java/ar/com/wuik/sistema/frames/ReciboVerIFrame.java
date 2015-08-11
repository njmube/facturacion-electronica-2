package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ar.com.wuik.sistema.bo.ParametroBO;
import ar.com.wuik.sistema.bo.ReciboBO;
import ar.com.wuik.sistema.entities.Cheque;
import ar.com.wuik.sistema.entities.Comprobante;
import ar.com.wuik.sistema.entities.PagoReciboCheque;
import ar.com.wuik.sistema.entities.PagoReciboEfectivo;
import ar.com.wuik.sistema.entities.Recibo;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.ReportException;
import ar.com.wuik.sistema.model.DetalleLiquidacionModel;
import ar.com.wuik.sistema.model.PagoReciboChequeModel;
import ar.com.wuik.sistema.reportes.ReciboReporte;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WTextFieldDecimal;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.components.table.WTablePanel;
import ar.com.wuik.swing.components.table.WToolbarButton;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.frames.WCalendarIFrame;
import ar.com.wuik.swing.utils.WFrameUtils;
import ar.com.wuik.swing.utils.WFrameUtils.FontSize;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;

import com.lowagie.text.Font;

public class ReciboVerIFrame extends WAbstractModelIFrame {
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
	private JButton btnGuardar;
	private ReciboIFrame reciboIFrame;
	private JButton btnFechaEmision;
	private JTextField txtFechaEmision;
	private JLabel lblFechaEmisin;
	private JLabel lblObservaciones;
	private JTextArea txaObservaciones;
	private JScrollPane scrollPane;
	private JTextField txtTotalLiquidacion;
	private Long idCliente;
	private JLabel lblTotalLiq;
	private JLabel lblTotal;
	private JTextField txtTotalPesos;
	private WTablePanel<Comprobante> tblLiquidacion;
	private WTablePanel<PagoReciboCheque> tblCheques;
	private WTextFieldDecimal txfEfectivo;
	private JLabel lblEfectivo;
	private JLabel lblNro;
	private JTextField txtNro;

	/**
	 * @wbp.parser.constructor
	 */
	public ReciboVerIFrame(ReciboIFrame reciboIFrame, Long idCliente,
			Long idRecibo) {

		initialize("Nuevo Recibo");

		this.reciboIFrame = reciboIFrame;
		this.idCliente = idCliente;

		WModel model = populateModel();
		if (null == idRecibo) {
			this.recibo = new Recibo();
			try {
				ParametroBO parametroBO = AbstractFactory
						.getInstance(ParametroBO.class);
				String numero = parametroBO.getNroRecibo();
				model.addValue(CAMPO_FECHA_EMISION,
						WUtils.getStringFromDate(new Date()));
				model.addValue(CAMPO_NUMERO, numero);
				populateComponents(model);
			} catch (BusinessException bexc) {
				showGlobalErrorMsg(bexc.getMessage());
			}
		} else {
			initialize("Editar Recibo");
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

	}

	public ReciboVerIFrame(Long idCliente) {

		initialize("Nuevo Recibo");

		this.idCliente = idCliente;

		WModel model = populateModel();
		this.recibo = new Recibo();
		try {
			ParametroBO parametroBO = AbstractFactory
					.getInstance(ParametroBO.class);
			String numero = parametroBO.getNroRecibo();
			model.addValue(CAMPO_FECHA_EMISION,
					WUtils.getStringFromDate(new Date()));
			model.addValue(CAMPO_NUMERO, numero);
			populateComponents(model);
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
				ReciboVerIFrame.class.getResource("/icons/recibo.png")));
		setBounds(0, 0, 926, 506);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getBtnCerrar());
		getContentPane().add(getBtnGuardar());
	}

	@Override
	protected boolean validateModel(WModel model) {

		String fechaEmision = model.getValue(CAMPO_FECHA_EMISION);
		BigDecimal total = recibo.getTotal();
		BigDecimal totalLiquidacion = BigDecimal.ZERO;
		Set<Comprobante> comprobantes = recibo.getComprobantes();
		for (Comprobante comprobante : comprobantes) {
			totalLiquidacion = totalLiquidacion.add(comprobante.getTotal());
		}

		List<String> messages = new ArrayList<String>();

		if (WUtils.isEmpty(fechaEmision)) {
			messages.add("Debe ingresar una Fecha de Emisión");
		}

		if (WUtils.isEmpty(comprobantes)) {
			messages.add("Debe seleccionar al menos un Comprobante a liquidar");
		}

		if (null == total || total.doubleValue() == 0) {
			messages.add("Debe ingresar al menos monto en Efectivo o Cheque");
		}

		if (null != total
				&& total.doubleValue() != totalLiquidacion.doubleValue()) {
			messages.add("Total de la entrega debe coincidir con el total de las liquidaciones");
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
			pnlBusqueda.setBounds(10, 11, 904, 421);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getBtnFechaEmision());
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
			btnCerrar = new JButton("Cancelar");
			btnCerrar.setFocusable(false);
			btnCerrar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					hideFrame();
				}
			});
			btnCerrar.setIcon(new ImageIcon(ReciboVerIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(698, 442, 103, 30);
		}
		return btnCerrar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setIcon(new ImageIcon(ReciboVerIFrame.class
					.getResource("/icons/ok.png")));
			btnGuardar.setBounds(811, 442, 103, 30);
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					WModel model = populateModel();
					if (validateModel(model)) {

						String fechaVenta = model.getValue(CAMPO_FECHA_EMISION);
						String observaciones = model
								.getValue(CAMPO_OBSERVACIONES);

						String efectivo = model.getValue(CAMPO_EFECTIVO);

						recibo.setFecha(WUtils.getDateFromString(fechaVenta));
						recibo.setObservaciones(observaciones);
						recibo.setIdCliente(idCliente);

						if (WUtils.isNotEmpty(efectivo)) {

							PagoReciboEfectivo pagoEfectivo = null;

							if (recibo.getPagosEfectivo().isEmpty()) {
								recibo.getPagosEfectivo().add(
										new PagoReciboEfectivo());
							}
							pagoEfectivo = recibo.getPagosEfectivo().get(0);
							pagoEfectivo.setRecibo(recibo);
							pagoEfectivo.setTotal(WUtils.getValue(efectivo));
						}

						try {
							ReciboBO reciboBO = AbstractFactory
									.getInstance(ReciboBO.class);
							if (recibo.getId() == null) {
								reciboBO.guardar(recibo);
							} else {
								reciboBO.actualizar(recibo);
							}
							try {
								ReciboReporte.generarRecibo(recibo.getId());
							} catch (ReportException rexc) {
								showGlobalErrorMsg(rexc.getMessage());
							}
							hideFrame();
							if (null != reciboIFrame) {
								reciboIFrame.search();
							}
						} catch (BusinessException bexc) {
							showGlobalErrorMsg(bexc.getMessage());
						}
					}
				}
			});
		}
		return btnGuardar;
	}

	@Override
	protected JComponent getFocusComponent() {
		return getTxfEfectivo();
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		WToolbarButton buttonAdd = new WToolbarButton("Agregar Cheque",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/add.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						List<Long> idsChequeToExclude = getIdsCheques();
						addModalIFrame(new SeleccionarChequeIFrame(
								ReciboVerIFrame.this, idsChequeToExclude,
								idCliente));
					}
				}, "Agregar Cheque", null);
		WToolbarButton buttonQuitar = new WToolbarButton("Quitar Cheque",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/delete.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						List<Long> selectedItems = tblCheques
								.getSelectedItemsID();
						if (WUtils.isNotEmpty(selectedItems)) {
							for (Long id : selectedItems) {
								PagoReciboCheque pagoReciboCheque = getPagoChequeById(id);
								recibo.getPagosCheque()
										.remove(pagoReciboCheque);
							}
							refreshPagosCheques();
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar al menos un Cheque",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Quitar Cheque", null);
		WToolbarButton buttonNew = new WToolbarButton("Nuevo Cheque",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/add.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						addModalIFrame(new ChequeVerIFrame(idCliente,
								ReciboVerIFrame.this));
					}
				}, "Nuevo Cheque", null);

		toolbarButtons.add(buttonNew);
		toolbarButtons.add(buttonAdd);
		toolbarButtons.add(buttonQuitar);
		return toolbarButtons;
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

	private JButton getBtnFechaEmision() {
		if (btnFechaEmision == null) {
			btnFechaEmision = new JButton("");
			btnFechaEmision.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addModalIFrame(new WCalendarIFrame(txtFechaEmision));
				}
			});
			btnFechaEmision.setIcon(new ImageIcon(ReciboVerIFrame.class
					.getResource("/icons/calendar.png")));
			btnFechaEmision.setBounds(257, 61, 25, 25);
		}
		return btnFechaEmision;
	}

	private JTextField getTxtFechaEmision() {
		if (txtFechaEmision == null) {
			txtFechaEmision = new JTextField();
			txtFechaEmision.setName(CAMPO_FECHA_EMISION);
			txtFechaEmision.setEditable(false);
			txtFechaEmision.setBounds(141, 61, 106, 25);
		}
		return txtFechaEmision;
	}

	private JLabel getLblFechaEmisin() {
		if (lblFechaEmisin == null) {
			lblFechaEmisin = new JLabel("* Fecha Emisi\u00F3n:");
			lblFechaEmisin.setHorizontalAlignment(SwingConstants.RIGHT);
			lblFechaEmisin.setBounds(10, 61, 121, 25);
		}
		return lblFechaEmisin;
	}

	private JLabel getLblObservaciones() {
		if (lblObservaciones == null) {
			lblObservaciones = new JLabel("Observaciones:");
			lblObservaciones.setHorizontalAlignment(SwingConstants.RIGHT);
			lblObservaciones.setBounds(445, 26, 116, 25);
		}
		return lblObservaciones;
	}

	private JTextArea getTxaObservaciones() {
		if (txaObservaciones == null) {
			txaObservaciones = new JTextArea();
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

	private List<WToolbarButton> getToolbarButtonsDetalles() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();
		WToolbarButton buttonEdit = new WToolbarButton("Agregar Comprobante",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/add.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						addModalIFrame(new SeleccionarComprobanteIFrame(
								ReciboVerIFrame.this, getIdsComprobantes(),
								idCliente));
					}
				}, "Agregar", null);
		WToolbarButton buttonEliminar = new WToolbarButton(
				"Quitar Comprobante",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/delete.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						List<Long> selectedItems = tblLiquidacion
								.getSelectedItemsID();
						if (WUtils.isNotEmpty(selectedItems)) {
							int result = JOptionPane
									.showConfirmDialog(
											getParent(),
											"¿Desea eliminar los Comprobantes seleccionados?",
											"Alerta",
											JOptionPane.OK_CANCEL_OPTION,
											JOptionPane.WARNING_MESSAGE);
							if (result == JOptionPane.OK_OPTION) {
								for (Long selectedItem : selectedItems) {
									Comprobante comprobante = getComprobanteById(selectedItem);
									recibo.getComprobantes()
											.remove(comprobante);
								}
								refreshLiquidaciones();
							}
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar al menos un Comprobante",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Quitar", null);

		toolbarButtons.add(buttonEdit);
		toolbarButtons.add(buttonEliminar);
		return toolbarButtons;
	}

	protected Comprobante getComprobanteById(Long selectedItem) {
		for (Comprobante comprobante : recibo.getComprobantes()) {
			if (comprobante.getId().equals(selectedItem)) {
				return comprobante;
			}
		}
		return null;
	}

	private JTextField getTxtTotalLiquidacion() {
		if (txtTotalLiquidacion == null) {
			txtTotalLiquidacion = new JTextField();
			txtTotalLiquidacion.setHorizontalAlignment(SwingConstants.RIGHT);
			txtTotalLiquidacion.setEditable(false);
			txtTotalLiquidacion.setText("0.00");
			txtTotalLiquidacion.setBounds(344, 384, 125, 25);
			txtTotalLiquidacion.setColumns(10);
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

	private JTextField getTxtTotalPesos() {
		if (txtTotalPesos == null) {
			txtTotalPesos = new JTextField();
			txtTotalPesos.setText("0.00");
			txtTotalPesos.setHorizontalAlignment(SwingConstants.RIGHT);
			txtTotalPesos.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			txtTotalPesos.setEditable(false);
			txtTotalPesos.setColumns(10);
			txtTotalPesos.setBounds(769, 384, 125, 25);
		}
		return txtTotalPesos;
	}

	private WTablePanel<Comprobante> getTblLiquidacion() {
		if (tblLiquidacion == null) {
			tblLiquidacion = new WTablePanel(DetalleLiquidacionModel.class,
					"Liquidación");
			tblLiquidacion.addToolbarButtons(getToolbarButtonsDetalles());
			tblLiquidacion.setBounds(10, 122, 459, 250);
		}
		return tblLiquidacion;
	}

	private WTablePanel<PagoReciboCheque> getTblCheques() {
		if (tblCheques == null) {
			tblCheques = new WTablePanel(PagoReciboChequeModel.class, "Cheques");
			tblCheques.addToolbarButtons(getToolbarButtons());
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

	private WTextFieldDecimal getTxfEfectivo() {
		if (txfEfectivo == null) {
			txfEfectivo = new WTextFieldDecimal(7, 2);
			txfEfectivo.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					calcularTotales();
				}
			});
			txfEfectivo.setHorizontalAlignment(SwingConstants.RIGHT);
			txfEfectivo.setName(CAMPO_EFECTIVO);
			txfEfectivo.setColumns(10);
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

	private JTextField getTxtNro() {
		if (txtNro == null) {
			txtNro = new JTextField();
			txtNro.setEditable(false);
			txtNro.setBounds(141, 24, 106, 26);
			txtNro.setName(CAMPO_NUMERO);
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
