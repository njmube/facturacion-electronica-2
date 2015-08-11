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
import ar.com.wuik.sistema.bo.RemitoBO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.Remito;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.ReportException;
import ar.com.wuik.sistema.filters.RemitoFilter;
import ar.com.wuik.sistema.model.RemitoModel;
import ar.com.wuik.sistema.reportes.RemitoReporte;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.security.WSecure;
import ar.com.wuik.swing.components.table.WTablePanel;
import ar.com.wuik.swing.components.table.WToolbarButton;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.frames.WCalendarIFrame;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;

public class RemitoClienteIFrame extends WAbstractModelIFrame implements
		WSecure {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 7107533032732470914L;
	private WTablePanel<Remito> tablePanel;
	private JButton btnCerrar;
	private Long idCliente;

	/**
	 * Create the frame.
	 */
	public RemitoClienteIFrame(Long idCliente) {
		this.idCliente = idCliente;
		setBorder(new LineBorder(null, 1, true));
		setTitle("Remitos");
		setFrameIcon(new ImageIcon(
				RemitoClienteIFrame.class.getResource("/icons/remitos.png")));
		setBounds(0, 0, 660, 519);
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

	private WTablePanel<Remito> getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new WTablePanel(RemitoModel.class, Boolean.FALSE);
			tablePanel.setBounds(10, 11, 634, 429);
			tablePanel.addToolbarButtons(getToolbarButtons());
		}
		return tablePanel;
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		WToolbarButton buttonEdit = new WToolbarButton("Editar Remito",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/edit.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							try {
								RemitoBO remitoBO = AbstractFactory
										.getInstance(RemitoBO.class);

								boolean estaEnUso = remitoBO
										.estaEnUso(selectedItem);

								if (!estaEnUso) {

									Remito remito = (Remito) remitoBO
											.obtener(selectedItem);

									boolean activo = remito.isActivo();

									if (activo) {
										addModalIFrame(new RemitoClienteVerIFrame(
												RemitoClienteIFrame.this,
												idCliente, selectedItem));
									} else {

										WTooltipUtils
												.showMessage(
														"El Remito se encuentra Anulado.",
														(JButton) e.getSource(),
														MessageType.ALERTA);
									}
								} else {
									WTooltipUtils
											.showMessage(
													"No es posible Anular el Remito porque está asociado a una Factura.",
													(JButton) e.getSource(),
													MessageType.ALERTA);
								}

							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}

						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Remito",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Editar", null);
		WToolbarButton buttonAnular = new WToolbarButton("Anular Remito",
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
												"¿Desea anular el Remito seleccionado?",
												"Alerta",
												JOptionPane.OK_CANCEL_OPTION,
												JOptionPane.WARNING_MESSAGE);
								if (result == JOptionPane.OK_OPTION) {
									RemitoBO remitoBO = AbstractFactory
											.getInstance(RemitoBO.class);

									boolean estaEnUso = remitoBO
											.estaEnUso(selectedItem);

									if (!estaEnUso) {

										Remito remito = (Remito) remitoBO
												.obtener(selectedItem);

										boolean activo = remito.isActivo();

										if (activo) {
											remitoBO.cancelar(selectedItem);
											search();
										} else {

											WTooltipUtils
													.showMessage(
															"El Remito se encuentra Anulado.",
															(JButton) e
																	.getSource(),
															MessageType.ALERTA);
										}
									} else {
										WTooltipUtils
												.showMessage(
														"No es posible Anular el Remito porque está asociado a una Factura.",
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
											"Debe seleccionar un solo Remito",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Anular", null);

		WToolbarButton buttonVer = new WToolbarButton("Ver Remito",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/ver.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							addModalIFrame(new RemitoClienteVistaIFrame(
									selectedItem));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Remito",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Ver", null);

		WToolbarButton buttonImprimir = new WToolbarButton("Imprimir",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/imprimir.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							try {
								RemitoBO remitoBO = AbstractFactory
										.getInstance(RemitoBO.class);
								Remito remito = remitoBO.obtener(selectedItem);
								boolean activo = remito.isActivo();
								if (activo) {
									try {
										RemitoReporte
												.generarRemito(selectedItem);
									} catch (ReportException rexc) {
										showGlobalErrorMsg(rexc.getMessage());
									}
								} else {
									WTooltipUtils.showMessage(
											"El Remito se encuentra Anulado.",
											(JButton) e.getSource(),
											MessageType.ALERTA);
								}
							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}

						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Remito",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Imprimir", null);

		if (isClienteActivo()) {
//			toolbarButtons.add(buttonAdd);
			toolbarButtons.add(buttonEdit);
			toolbarButtons.add(buttonAnular);
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
			btnCerrar.setIcon(new ImageIcon(RemitoClienteIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(541, 451, 103, 25);
		}
		return btnCerrar;
	}

	public void search() {
		// Filtro
		RemitoFilter filter = new RemitoFilter();
		filter.setIdCliente(idCliente);

		try {
			RemitoBO remitoBO = AbstractFactory.getInstance(RemitoBO.class);
			List<Remito> facturas = remitoBO.buscar(filter);
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
