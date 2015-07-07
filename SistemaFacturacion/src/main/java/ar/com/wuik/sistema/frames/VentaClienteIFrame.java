package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.sistema.bo.ClienteBO;
import ar.com.wuik.sistema.bo.FacturaBO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.Factura;
import ar.com.wuik.sistema.exceptions.BusinessException;
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
import ar.com.wuik.swing.utils.WFrameUtils;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;

public class VentaClienteIFrame extends WAbstractModelIFrame implements WSecure {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 7107533032732470914L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(VentaClienteIFrame.class);
	private static final String CAMPO_DESDE_VTO = "desdeVto";
	private static final String CAMPO_HASTA_VTO = "hastaVto";
	private static final String CAMPO_DESDE_EMISION = "desdeEmision";
	private static final String CAMPO_HASTA_EMISION = "hastaEmision";
	private static final String CAMPO_NRO = "numero";
	private WTablePanel<Factura> tablePanel;
	private JButton btnCerrar;
	private Long idCliente;

	/**
	 * Create the frame.
	 */
	public VentaClienteIFrame(Long idCliente) {
		this.idCliente = idCliente;
		setBorder(new LineBorder(null, 1, true));
		setTitle("Ventas");
		setFrameIcon(new ImageIcon(
				VentaClienteIFrame.class.getResource("/icons/facturas.png")));
		setBounds(0, 0, 1000, 519);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getBtnCerrar());
		Date hoy = new Date();
		getContentPane().add(getTablePanel());
		search();
	}

	/**
	 * @see ar.com.wuik.swing.components.security.WSecure#applySecurity(java.util.List)
	 */
	@Override
	public void applySecurity(List<String> permisos) {
		getTablePanel().applySecurity(permisos);
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
						addModalIFrame(new VentaClienteVerIFrame(
								VentaClienteIFrame.this, idCliente));
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
								boolean esUtilizado = facturaBO
										.estaEnUso(selectedItem);

								if (!esUtilizado) {

									Factura factura = (Factura) facturaBO
											.obtener(selectedItem);

									boolean activa = factura.isActivo();

									if (activa) {
										// addModalIFrame(new
										// FacturaClienteVerIFrame(
										// selectedItem,
										// FacturaClienteIFrame.this));
									} else {
										WTooltipUtils
												.showMessage(
														"No es posible Editar el Item porque está Anulado.",
														(JButton) e.getSource(),
														MessageType.ALERTA);
									}

								} else {
									WTooltipUtils
											.showMessage(
													"No es posible Editar el Item porque está siendo utilizado.",
													(JButton) e.getSource(),
													MessageType.ALERTA);
								}

							} catch (BusinessException bexc) {
								LOGGER.error(
										"Error al obtener detalle de Factura",
										bexc);
								WFrameUtils
										.showGlobalErrorMsg("Se ha producido un error al obtener detalle de Factura");
							}

						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Item",
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
								int result = JOptionPane.showConfirmDialog(
										getParent(),
										"¿Desea anular el Item seleccionado?",
										"Alerta", JOptionPane.OK_CANCEL_OPTION,
										JOptionPane.WARNING_MESSAGE);
								if (result == JOptionPane.OK_OPTION) {
									FacturaBO facturaBO = AbstractFactory
											.getInstance(FacturaBO.class);

									boolean esUtilizado = facturaBO
											.estaEnUso(selectedItem);

									if (!esUtilizado) {

										Factura factura = (Factura) facturaBO
												.obtener(selectedItem);

										boolean activa = factura.isActivo();

										if (activa) {
											facturaBO.cancelar(selectedItem);
											search();
										} else {

											WTooltipUtils
													.showMessage(
															"El Item se encuentra Anulado.",
															(JButton) e
																	.getSource(),
															MessageType.ALERTA);
										}
									} else {
										WTooltipUtils
												.showMessage(
														"No es posible Anular el Item porque está asociado a Comprobantes.",
														(JButton) e.getSource(),
														MessageType.ALERTA);
									}
								}
							} catch (BusinessException bexc) {
								LOGGER.error("Error al Anular Factura", bexc);
								WFrameUtils
										.showGlobalErrorMsg("Se ha producido un error al Anular Factura");
							}
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Item",
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
							// FacturaClienteVerIFrame facturaVerIFrame = new
							// FacturaClienteVerIFrame(
							// selectedItem, FacturaClienteIFrame.this);
							// facturaVerIFrame.setReadOnly();
							// addModalIFrame(facturaVerIFrame);
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Item",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Ver", null);

		WToolbarButton buttonRemitir = new WToolbarButton("Remitir Factura",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/remitos.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();

						if (null != selectedItem) {
							try {
								FacturaBO facturaBO = AbstractFactory
										.getInstance(FacturaBO.class);

								Factura factura = (Factura) facturaBO
										.obtener(selectedItem);

								boolean activa = factura.isActivo();

								if (activa) {
									// addModalIFrame(new
									// RemitirFacturaClienteIFrame(
									// selectedItem, idCliente));
								} else {
									WTooltipUtils.showMessage(
											"El Item se encuentra Anulado.",
											(JButton) e.getSource(),
											MessageType.ALERTA);
								}
							} catch (BusinessException bexc) {
								LOGGER.error("Error al Remitir Factura", bexc);
								WFrameUtils
										.showGlobalErrorMsg("Se ha producido un error al Remitir Factura");
							}

						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Item",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Remitir", null);

		WToolbarButton buttonImprimir = new WToolbarButton("Imprimir",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/imprimir.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							try {
								FacturaReporte.generarFactura(selectedItem);
							} catch (Exception e1) {
								WFrameUtils
										.showGlobalErrorMsg("Se ha producido un error al imprimir la Factura");
							}
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Item",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Imprimir", null);

		if (isClienteActivo()) {
			toolbarButtons.add(buttonAdd);
			toolbarButtons.add(buttonEdit);
			toolbarButtons.add(buttonAnular);
			toolbarButtons.add(buttonRemitir);
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
			btnCerrar.setIcon(new ImageIcon(VentaClienteIFrame.class
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
			LOGGER.error("Error al buscar Facturas", bexc);
		}
	}

	@Override
	protected JComponent getFocusComponent() {
		return null;
	}

	@Override
	public void enterPressed() {
		search();
	}

	private boolean isClienteActivo() {
		ClienteBO clienteBO = AbstractFactory.getInstance(ClienteBO.class);
		try {
			Cliente cliente = clienteBO.obtener(idCliente);
			return cliente.isActivo();
		} catch (BusinessException bexc) {
			LOGGER.error("Error al obtener Cliente", bexc);
		}
		return Boolean.FALSE;
	}
}
