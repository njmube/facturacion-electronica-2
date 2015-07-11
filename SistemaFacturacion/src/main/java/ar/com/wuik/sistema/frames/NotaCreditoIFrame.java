package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;

import ar.com.wuik.sistema.bo.ClienteBO;
import ar.com.wuik.sistema.bo.NotaCreditoBO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.NotaCredito;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.ReportException;
import ar.com.wuik.sistema.filters.NotaCreditoFilter;
import ar.com.wuik.sistema.model.NotaCreditoModel;
import ar.com.wuik.sistema.reportes.NotaCreditoReporte;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.security.WSecure;
import ar.com.wuik.swing.components.table.WTablePanel;
import ar.com.wuik.swing.components.table.WToolbarButton;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.frames.WCalendarIFrame;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;

public class NotaCreditoIFrame extends WAbstractModelIFrame implements WSecure {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 7107533032732470914L;
	private WTablePanel<NotaCredito> tablePanel;
	private JButton btnCerrar;
	private Long idCliente;

	/**
	 * Create the frame.
	 */
	public NotaCreditoIFrame(Long idCliente) {
		this.idCliente = idCliente;
		setBorder(new LineBorder(null, 1, true));
		setTitle("Notas de Crédito");
		setFrameIcon(new ImageIcon(
				NotaCreditoIFrame.class.getResource("/icons/notas_credito.png")));
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
		getTablePanel().applySecurity(permisos);
	}

	@Override
	protected boolean validateModel(WModel model) {
		return false;
	}

	private WTablePanel<NotaCredito> getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new WTablePanel(NotaCreditoModel.class, Boolean.FALSE);
			tablePanel.setBounds(10, 11, 978, 429);
			tablePanel.addToolbarButtons(getToolbarButtons());
		}
		return tablePanel;
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		WToolbarButton buttonAdd = new WToolbarButton("Nueva Nota de Crédito",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/add.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						addModalIFrame(new NotaCreditoVerIFrame(
								NotaCreditoIFrame.this, idCliente, null));
					}
				}, "Nuevo", null);
		WToolbarButton buttonEdit = new WToolbarButton("Editar Nota de Crédito",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/edit.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							NotaCreditoBO notaCreditoBO = AbstractFactory
									.getInstance(NotaCreditoBO.class);
							try {

								NotaCredito notaCredito = notaCreditoBO
										.obtener(selectedItem);

								boolean facturada = notaCredito.isFacturada();

								if (!facturada) {
									addModalIFrame(new NotaCreditoVerIFrame(
											NotaCreditoIFrame.this, idCliente,
											selectedItem));
								} else {
									WTooltipUtils
											.showMessage(
													"No es posible editar la Nota de Crédito porque se encuentra facturada en AFIP.",
													(JButton) e.getSource(),
													MessageType.ALERTA);
								}

							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}

						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar una sola Nota de Crédito",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Editar", null);

		WToolbarButton buttonVer = new WToolbarButton("Ver Nota de Crédito",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/ver.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							addModalIFrame(new NotaCreditoVistaIFrame(selectedItem));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar una sola Nota de Crédito",
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
							NotaCreditoBO notaCreditoBO = AbstractFactory
									.getInstance(NotaCreditoBO.class);
							try {
								NotaCredito notaCredito = notaCreditoBO
										.obtener(selectedItem);

								if (!notaCredito.isFacturada()) {
									notaCreditoBO.guardarRegistrarAFIP(notaCredito);
									try {
										NotaCreditoReporte
												.generarNotaCredito(selectedItem);
									} catch (ReportException rexc) {
										showGlobalErrorMsg(rexc.getMessage());
									}
									search();
								} else {
									WTooltipUtils
											.showMessage(
													"La Nota de Crédito se encuentra facturada.",
													(JButton) e.getSource(),
													MessageType.ALERTA);
								}
							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar una sola Nota de Crédito",
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

							NotaCreditoBO notaCreditoBO = AbstractFactory
									.getInstance(NotaCreditoBO.class);
							try {
								NotaCredito notaCredito = notaCreditoBO
										.obtener(selectedItem);

								if (notaCredito.isFacturada()) {
									try {
										NotaCreditoReporte
												.generarNotaCredito(selectedItem);
									} catch (ReportException rexc) {
										showGlobalErrorMsg(rexc.getMessage());
									}
								} else {
									WTooltipUtils.showMessage(
											"La Nota de Crédito debe estar facturada.",
											(JButton) e.getSource(),
											MessageType.ALERTA);
								}
							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}

						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar una sola Nota de Crédito",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Imprimir", null);

		if (isClienteActivo()) {
			toolbarButtons.add(buttonAdd);
			toolbarButtons.add(buttonEdit);
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
			btnCerrar.setIcon(new ImageIcon(NotaCreditoIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(885, 451, 103, 25);
		}
		return btnCerrar;
	}

	public void search() {
		// Filtro
		NotaCreditoFilter filter = new NotaCreditoFilter();
		filter.setIdCliente(idCliente);

		try {
			NotaCreditoBO notaCreditoBO = AbstractFactory
					.getInstance(NotaCreditoBO.class);
			List<NotaCredito> notasCredito = notaCreditoBO.buscar(filter);
			getTablePanel().addData(notasCredito);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
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
			showGlobalErrorMsg(bexc.getMessage());
		}
		return Boolean.FALSE;
	}
}
