package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;

import ar.com.wuik.sistema.bo.ClienteBO;
import ar.com.wuik.sistema.bo.FacturaBO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.Factura;
import ar.com.wuik.sistema.entities.enums.EstadoFacturacion;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.ReportException;
import ar.com.wuik.sistema.filters.FacturaFilter;
import ar.com.wuik.sistema.model.FacturaModel;
import ar.com.wuik.sistema.reportes.FacturaReporte;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.security.WSecure;
import ar.com.wuik.swing.components.table.WTablePanel;
import ar.com.wuik.swing.components.table.WToolbarButton;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.frames.WCalendarIFrame;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;

public class FacturaIFrame extends WAbstractModelIFrame implements WSecure {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 7107533032732470914L;
	private WTablePanel<Factura> tablePanel;
	private JButton btnCerrar;
	private Long idCliente;

	/**
	 * Create the frame.
	 */
	public FacturaIFrame(Long idCliente) {
		this.idCliente = idCliente;
		setBorder(new LineBorder(null, 1, true));
		setTitle("Ventas");
		setFrameIcon(new ImageIcon(
				FacturaIFrame.class.getResource("/icons/facturas.png")));
		setBounds(0, 0, 1000, 519);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getBtnCerrar());
		getContentPane().add(getTablePanel());
		search();
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

	private WTablePanel<Factura> getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new WTablePanel(FacturaModel.class, Boolean.FALSE);
			tablePanel.setBounds(10, 11, 978, 429);
			tablePanel.addToolbarButtons(getToolbarButtons());
		}
		return tablePanel;
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		WToolbarButton buttonAdd = new WToolbarButton("Nueva Factura",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/add.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						addModalIFrame(new FacturaVerIFrame(FacturaIFrame.this,
								idCliente, null));
					}
				}, "Nuevo", null);
		WToolbarButton buttonEdit = new WToolbarButton("Editar Factura",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/edit.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							FacturaBO facturaBO = AbstractFactory
									.getInstance(FacturaBO.class);
							try {

								Factura factura = (Factura) facturaBO
										.obtener(selectedItem);

								if (!factura.getEstadoFacturacion().equals(
										EstadoFacturacion.FACTURADO)) {
									if (!factura.getEstadoFacturacion().equals(
											EstadoFacturacion.FACTURADO_ERROR)) {
										addModalIFrame(new FacturaVerIFrame(
												FacturaIFrame.this, idCliente,
												selectedItem));
									} else {
										WTooltipUtils
												.showMessage(
														"No es posible editar la Factura porque se encuentra facturada con error.",
														(JButton) e.getSource(),
														MessageType.ALERTA);
									}
								} else {
									WTooltipUtils
											.showMessage(
													"No es posible editar la Factura porque se encuentra facturada en AFIP.",
													(JButton) e.getSource(),
													MessageType.ALERTA);
								}

							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}

						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar una sola Factura",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Editar", null);
		WToolbarButton buttonAnular = new WToolbarButton("Anular Factura",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/cancel2.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							try {
								int result = JOptionPane
										.showConfirmDialog(
												getParent(),
												"¿Desea anular la Factura seleccionada?",
												"Alerta",
												JOptionPane.OK_CANCEL_OPTION,
												JOptionPane.WARNING_MESSAGE);
								if (result == JOptionPane.OK_OPTION) {
									FacturaBO facturaBO = AbstractFactory
											.getInstance(FacturaBO.class);

									Factura factura = (Factura) facturaBO
											.obtener(selectedItem);

									if (factura.isActivo()) {
										if (!factura
												.getEstadoFacturacion()
												.equals(EstadoFacturacion.FACTURADO_ERROR)) {
											facturaBO.cancelar(selectedItem);
											search();
										} else {
											WTooltipUtils
													.showMessage(
															"No es posible editar la Factura porque se encuentra facturada con error.",
															(JButton) e
																	.getSource(),
															MessageType.ALERTA);
										}

									} else {

										WTooltipUtils
												.showMessage(
														"La Factura se encuentra Anulada.",
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
											"Debe seleccionar una sola Factura",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Anular", null);

		WToolbarButton buttonVer = new WToolbarButton("Ver Factura",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/ver.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							addModalIFrame(new FacturaVistaIFrame(selectedItem));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar una sola Factura",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Ver", null);

		WToolbarButton buttonFacturar = new WToolbarButton("Facturar",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/ver.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							FacturaBO facturaBO = AbstractFactory
									.getInstance(FacturaBO.class);
							try {
								Factura factura = facturaBO
										.obtener(selectedItem);
								if (!factura.getEstadoFacturacion().equals(
										EstadoFacturacion.FACTURADO)) {
									facturaBO.registrarAFIP(factura);
									try {
										FacturaReporte
												.generarFactura(selectedItem);
									} catch (ReportException rexc) {
										showGlobalErrorMsg(rexc.getMessage());
									}
								} else {
									WTooltipUtils
											.showMessage(
													"La Factura se encuentra facturada.",
													(JButton) e.getSource(),
													MessageType.ALERTA);
								}
							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							} finally{
								search();
							}
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar una sola Factura",
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

							FacturaBO facturaBO = AbstractFactory
									.getInstance(FacturaBO.class);
							try {
								Factura factura = facturaBO
										.obtener(selectedItem);
								if (factura.getEstadoFacturacion().equals(
										EstadoFacturacion.FACTURADO)) {
									if (factura.isActivo()) {
										try {
											FacturaReporte
													.generarFactura(selectedItem);
										} catch (ReportException rexc) {
											showGlobalErrorMsg(rexc
													.getMessage());
										}
									} else {
										WTooltipUtils
												.showMessage(
														"La Factura debe estar activa.",
														(JButton) e.getSource(),
														MessageType.ALERTA);
									}
								} else {
									WTooltipUtils.showMessage(
											"La Factura debe estar facturada.",
											(JButton) e.getSource(),
											MessageType.ALERTA);
								}
							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}

						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar una sola Factura",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Imprimir", null);

		if (isClienteActivo()) {
			toolbarButtons.add(buttonAdd);
			toolbarButtons.add(buttonEdit);
			toolbarButtons.add(buttonAnular);
			toolbarButtons.add(buttonFacturar);
			toolbarButtons.add(buttonImprimir);
		}
		toolbarButtons.add(buttonVer);
		return toolbarButtons;
	}

	private JButton getBtnCerrar() {
		if (btnCerrar == null) {
			btnCerrar = new JButton("Salir");
			btnCerrar.setFocusable(false);
			btnCerrar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					hideFrame();
				}
			});
			btnCerrar.setIcon(new ImageIcon(FacturaIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(885, 451, 103, 25);
		}
		return btnCerrar;
	}

	public void search() {
		// Filtro
		FacturaFilter filter = new FacturaFilter();
		filter.setIdCliente(idCliente);

		try {
			FacturaBO facturaBO = AbstractFactory.getInstance(FacturaBO.class);
			List<Factura> facturas = facturaBO.buscar(filter);
			getTablePanel().addData(facturas);
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
}
