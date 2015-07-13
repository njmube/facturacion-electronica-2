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
import ar.com.wuik.sistema.bo.NotaDebitoBO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.NotaDebito;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.ReportException;
import ar.com.wuik.sistema.filters.NotaDebitoFilter;
import ar.com.wuik.sistema.model.NotaDebitoModel;
import ar.com.wuik.sistema.reportes.NotaDebitoReporte;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.security.WSecure;
import ar.com.wuik.swing.components.table.WTablePanel;
import ar.com.wuik.swing.components.table.WToolbarButton;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.frames.WCalendarIFrame;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;

public class NotaDebitoIFrame extends WAbstractModelIFrame implements WSecure {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 7107533032732470914L;
	private WTablePanel<NotaDebito> tablePanel;
	private JButton btnCerrar;
	private Long idCliente;

	/**
	 * Create the frame.
	 */
	public NotaDebitoIFrame(Long idCliente) {
		this.idCliente = idCliente;
		setBorder(new LineBorder(null, 1, true));
		setTitle("Notas de D�bito");
		setFrameIcon(new ImageIcon(
				NotaDebitoIFrame.class.getResource("/icons/notas_debito.png")));
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

	private WTablePanel<NotaDebito> getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new WTablePanel(NotaDebitoModel.class, Boolean.FALSE);
			tablePanel.setBounds(10, 11, 978, 429);
			tablePanel.addToolbarButtons(getToolbarButtons());
		}
		return tablePanel;
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		WToolbarButton buttonAdd = new WToolbarButton("Nueva Nota de D�bito",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/add.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						addModalIFrame(new NotaDebitoVerIFrame(
								NotaDebitoIFrame.this, idCliente, null));
					}
				}, "Nuevo", null);
		WToolbarButton buttonEdit = new WToolbarButton("Editar Nota de D�bito",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/edit.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							NotaDebitoBO NotaDebitoBO = AbstractFactory
									.getInstance(NotaDebitoBO.class);
							try {

								NotaDebito NotaDebito = NotaDebitoBO
										.obtener(selectedItem);

								boolean facturada = NotaDebito.isFacturada();

								if (!facturada) {
									addModalIFrame(new NotaDebitoVerIFrame(
											NotaDebitoIFrame.this, idCliente,
											selectedItem));
								} else {
									WTooltipUtils
											.showMessage(
													"No es posible editar la Nota de D�bito porque se encuentra facturada en AFIP.",
													(JButton) e.getSource(),
													MessageType.ALERTA);
								}

							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}

						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar una sola Nota de D�bito",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Editar", null);

		WToolbarButton buttonVer = new WToolbarButton("Ver Nota de D�bito",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/ver.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							addModalIFrame(new NotaDebitoVistaIFrame(selectedItem));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar una sola Nota de D�bito",
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
							NotaDebitoBO notaDebitoBO = AbstractFactory
									.getInstance(NotaDebitoBO.class);
							try {
								NotaDebito notaDebito = notaDebitoBO
										.obtener(selectedItem);

								if (!notaDebito.isFacturada()) {
									notaDebitoBO.guardarRegistrarAFIP(notaDebito);
									try {
										NotaDebitoReporte
												.generarNotaDebito(selectedItem);
									} catch (ReportException rexc) {
										showGlobalErrorMsg(rexc.getMessage());
									}
									search();
								} else {
									WTooltipUtils
											.showMessage(
													"La Nota de D�bito se encuentra facturada.",
													(JButton) e.getSource(),
													MessageType.ALERTA);
								}
							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar una sola Nota de D�bito",
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

							NotaDebitoBO notaDebitoBO = AbstractFactory
									.getInstance(NotaDebitoBO.class);
							try {
								NotaDebito notaDebito = notaDebitoBO
										.obtener(selectedItem);

								if (notaDebito.isFacturada()) {
									try {
										NotaDebitoReporte
												.generarNotaDebito(selectedItem);
									} catch (ReportException rexc) {
										showGlobalErrorMsg(rexc.getMessage());
									}
								} else {
									WTooltipUtils.showMessage(
											"La Nota de D�bito debe estar facturada.",
											(JButton) e.getSource(),
											MessageType.ALERTA);
								}
							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}

						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar una sola Nota de D�bito",
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
			btnCerrar.setIcon(new ImageIcon(NotaDebitoIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(885, 451, 103, 25);
		}
		return btnCerrar;
	}

	public void search() {
		// Filtro
		NotaDebitoFilter filter = new NotaDebitoFilter();
		filter.setIdCliente(idCliente);

		try {
			NotaDebitoBO notaDebitoBO = AbstractFactory
					.getInstance(NotaDebitoBO.class);
			List<NotaDebito> notasCredito = notaDebitoBO.buscar(filter);
			getTablePanel().addData(notasCredito);
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
