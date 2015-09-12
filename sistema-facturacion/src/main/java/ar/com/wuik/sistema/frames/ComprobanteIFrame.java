package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;

import ar.com.wuik.sistema.bo.ClienteBO;
import ar.com.wuik.sistema.bo.ComprobanteBO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.Comprobante;
import ar.com.wuik.sistema.entities.enums.EstadoFacturacion;
import ar.com.wuik.sistema.entities.enums.TipoComprobante;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.ReportException;
import ar.com.wuik.sistema.filters.ComprobanteFilter;
import ar.com.wuik.sistema.model.ComprobanteModel;
import ar.com.wuik.sistema.reportes.ComprobanteReporte;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WOption;
import ar.com.wuik.swing.components.security.WSecure;
import ar.com.wuik.swing.components.table.WTablePanel;
import ar.com.wuik.swing.components.table.WToolbarButton;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.frames.WCalendarIFrame;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;

public class ComprobanteIFrame extends WAbstractModelIFrame implements WSecure {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 7107533032732470914L;
	private WTablePanel<Comprobante> tablePanel;
	private JButton btnCerrar;
	private Long idCliente;
	private static final String CAMPO_TIPO_COMP = "tipoComprobante";
	private JLabel lblTipoComprobante;
	private JComboBox cmbTipoComp;
	private ClienteIFrame clienteIFrame;

	/**
	 * Create the frame.
	 */
	public ComprobanteIFrame(Long idCliente, ClienteIFrame clienteIFrame) {
		this.idCliente = idCliente;
		this.clienteIFrame = clienteIFrame;
		setBorder(new LineBorder(null, 1, true));
		setTitle("Comprobantes");
		setFrameIcon(new ImageIcon(
				ComprobanteIFrame.class.getResource("/icons/comprobantes.png")));
		setBounds(0, 0, 1000, 519);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getBtnCerrar());
		getContentPane().add(getTablePanel());
		getContentPane().add(getLblTipoComprobante());
		getContentPane().add(getCmbTipoComp());
		loadTiposComprobantes();
		search();
	}

	private void loadTiposComprobantes() {
		TipoComprobante[] tiposComprobantes = TipoComprobante.values();
		getCmbTipoComp().addItem(WOption.getWOptionTodos());
		for (TipoComprobante tipoComprobante : tiposComprobantes) {
			getCmbTipoComp().addItem(
					new WOption((long) tipoComprobante.getId(), tipoComprobante
							.getDescripcion()));
		}
	}

	/**
	 * @see ar.com.wuik.swing.components.security.WSecure#applySecurity(java.util.List)
	 */
	@Override
	public void applySecurity(List<String> permisos) {
	}

	@Override
	protected boolean validateModel(WModel model) {
		return false;
	}

	private WTablePanel<Comprobante> getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new WTablePanel(ComprobanteModel.class, Boolean.FALSE);
			tablePanel.setBounds(10, 47, 978, 393);
			tablePanel.addToolbarButtons(getToolbarButtons());
		}
		return tablePanel;
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();
		WToolbarButton buttonEdit = new WToolbarButton("Editar Comprobante",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/edit.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							ComprobanteBO comprobanteBO = AbstractFactory
									.getInstance(ComprobanteBO.class);
							try {

								Comprobante comprobante = (Comprobante) comprobanteBO
										.obtener(selectedItem);

								if (!comprobante.getEstadoFacturacion().equals(
										EstadoFacturacion.FACTURADO)) {
									if (!comprobante
											.getEstadoFacturacion()
											.equals(EstadoFacturacion.FACTURADO_ERROR)) {
										if (comprobante.isActivo()) {
											addModalIFrame(new ComprobanteVerIFrame(
													ComprobanteIFrame.this,
													selectedItem, clienteIFrame));
										} else {
											WTooltipUtils
													.showMessage(
															"El Comprobante debe estar activo.",
															(JButton) e
																	.getSource(),
															MessageType.ALERTA);
										}

									} else {
										WTooltipUtils
												.showMessage(
														"No es posible editar el Comprobante porque se encuentra facturado con error.",
														(JButton) e.getSource(),
														MessageType.ALERTA);
									}
								} else {
									WTooltipUtils
											.showMessage(
													"No es posible editar el Comprobante porque se encuentra facturado en AFIP.",
													(JButton) e.getSource(),
													MessageType.ALERTA);
								}

							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}

						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Comprobante",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Editar", null);
		WToolbarButton buttonAnular = new WToolbarButton("Anular Comprobante",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/anular.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							try {
								int result = JOptionPane
										.showConfirmDialog(
												getParent(),
												"¿Desea anular el Comprobante seleccionado?",
												"Alerta",
												JOptionPane.OK_CANCEL_OPTION,
												JOptionPane.WARNING_MESSAGE);
								if (result == JOptionPane.OK_OPTION) {
									ComprobanteBO comprobanteBO = AbstractFactory
											.getInstance(ComprobanteBO.class);

									Comprobante comprobante = (Comprobante) comprobanteBO
											.obtener(selectedItem);

									if (comprobante.isActivo()) {
										if (!comprobante
												.getEstadoFacturacion()
												.equals(EstadoFacturacion.FACTURADO_ERROR)) {

											if (!comprobante.isPago()) {
												comprobanteBO
														.cancelar(selectedItem);
												search();
												clienteIFrame.search();
											} else {
												WTooltipUtils
														.showMessage(
																"No es posible anular el Comprobante porque se encuentra pago.",
																(JButton) e
																		.getSource(),
																MessageType.ALERTA);
											}
										} else {
											WTooltipUtils
													.showMessage(
															"No es posible anular el Comprobante porque se encuentra facturado con error.",
															(JButton) e
																	.getSource(),
															MessageType.ALERTA);
										}

									} else {

										WTooltipUtils
												.showMessage(
														"El Comprobante se encuentra Anulado.",
														(JButton) e.getSource(),
														MessageType.ALERTA);
									}
								}
							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Comprobante.",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Anular", null);

		WToolbarButton buttonVer = new WToolbarButton("Ver Comprobante",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/ver_detalle.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							addModalIFrame(new ComprobanteVistaIFrame(
									selectedItem));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Comprobante.",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Ver", null);

		WToolbarButton buttonFacturar = new WToolbarButton("Facturar",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/facturar.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							ComprobanteBO comprobanteBO = AbstractFactory
									.getInstance(ComprobanteBO.class);
							try {
								Comprobante comprobante = comprobanteBO
										.obtener(selectedItem);
								if (!comprobante.getEstadoFacturacion().equals(
										EstadoFacturacion.FACTURADO)) {
									if (comprobante.isActivo()) {
										comprobanteBO
												.registrarAFIP(comprobante);
										try {
											ComprobanteReporte
													.generarImpresion(selectedItem);
										} catch (ReportException rexc) {
											showGlobalErrorMsg(rexc
													.getMessage());
										}
									} else {
										WTooltipUtils
												.showMessage(
														"El Comprobante se encuentra anulado.",
														(JButton) e.getSource(),
														MessageType.ALERTA);
									}

								} else {
									WTooltipUtils
											.showMessage(
													"El Comprobante se encuentra facturado.",
													(JButton) e.getSource(),
													MessageType.ALERTA);
								}
							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							} finally {
								search();
							}
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Comprobante.",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Facturar", null);

		WToolbarButton buttonImprimir = new WToolbarButton("Imprimir",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/imprimir.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {

							ComprobanteBO comprobanteBO = AbstractFactory
									.getInstance(ComprobanteBO.class);
							try {
								Comprobante comprobante = comprobanteBO
										.obtener(selectedItem);
								if (comprobante.getEstadoFacturacion().equals(
										EstadoFacturacion.FACTURADO)) {
									if (comprobante.isActivo()) {
										try {
											ComprobanteReporte
													.generarImpresion(selectedItem);
										} catch (ReportException rexc) {
											showGlobalErrorMsg(rexc
													.getMessage());
										}
									} else {
										WTooltipUtils
												.showMessage(
														"El Comprobante debe estar activo.",
														(JButton) e.getSource(),
														MessageType.ALERTA);
									}
								} else {
									WTooltipUtils
											.showMessage(
													"El Comprobante debe estar facturado.",
													(JButton) e.getSource(),
													MessageType.ALERTA);
								}
							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}

						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Comprobante.",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Imprimir", null);

		WToolbarButton buttonEnviar = new WToolbarButton("Enviar por Mail",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/mail.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							ComprobanteBO comprobanteBO = AbstractFactory
									.getInstance(ComprobanteBO.class);
							try {
								Comprobante comprobante = comprobanteBO
										.obtener(selectedItem);

								if (comprobante.getEstadoFacturacion().equals(
										EstadoFacturacion.FACTURADO)) {

									Cliente cliente = comprobante.getCliente();

									if (WUtils.isNotEmpty(cliente.getMail())) {
										ComprobanteReporte
												.generarImpresionEnviarMail(
														selectedItem,
														cliente.getMail());
									} else {
										WTooltipUtils
												.showMessage(
														"El cliente no posee Mail, debe agregarlo.",
														(JButton) e.getSource(),
														MessageType.ALERTA);
									}
								} else {
									WTooltipUtils
											.showMessage(
													"El Comprobante debe estar facturado.",
													(JButton) e.getSource(),
													MessageType.ALERTA);
								}
							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							} catch (ReportException rexc) {
								showGlobalErrorMsg(rexc.getMessage());
							}

						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Comprobante.",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Enviar por Mail", null);

		if (isClienteActivo()) {
			// toolbarButtons.add(buttonAdd);
			toolbarButtons.add(buttonEdit);
			toolbarButtons.add(buttonAnular);
			toolbarButtons.add(buttonFacturar);
		}
		toolbarButtons.add(buttonImprimir);
		toolbarButtons.add(buttonVer);
		toolbarButtons.add(buttonEnviar);
		return toolbarButtons;
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
			btnCerrar.setIcon(new ImageIcon(ComprobanteIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(885, 451, 103, 30);
		}
		return btnCerrar;
	}

	public void search() {
		WModel model = populateModel();
		WOption tipoComp = model.getValue(CAMPO_TIPO_COMP);

		// Filtro
		ComprobanteFilter filter = new ComprobanteFilter();
		filter.setIdCliente(idCliente);
		filter.setTipoComprobante(TipoComprobante.fromValue(tipoComp.getValue()
				.intValue()));
		try {
			ComprobanteBO comprobanteBO = AbstractFactory
					.getInstance(ComprobanteBO.class);
			List<Comprobante> comprobantes = comprobanteBO.buscar(filter);
			getTablePanel().addData(comprobantes);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	@Override
	protected JComponent getFocusComponent() {
		return null;
	}

	private boolean isClienteActivo() {
		ClienteBO clienteBO = AbstractFactory.getInstance(ClienteBO.class);
		try {
			Cliente cliente = clienteBO.obtener(idCliente);
			return cliente.isActivo();
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
		return Boolean.FALSE;
	}

	private JLabel getLblTipoComprobante() {
		if (lblTipoComprobante == null) {
			lblTipoComprobante = new JLabel("Tipo Comprobante:");
			lblTipoComprobante.setBounds(10, 11, 116, 25);
		}
		return lblTipoComprobante;
	}

	private JComboBox getCmbTipoComp() {
		if (cmbTipoComp == null) {
			cmbTipoComp = new JComboBox();
			cmbTipoComp.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						search();
					}
				}
			});
			cmbTipoComp.setName(CAMPO_TIPO_COMP);
			cmbTipoComp.setBounds(136, 13, 160, 23);
		}
		return cmbTipoComp;
	}
}
