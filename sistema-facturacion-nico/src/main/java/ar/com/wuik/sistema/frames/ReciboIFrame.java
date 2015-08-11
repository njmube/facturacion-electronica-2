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
import ar.com.wuik.sistema.bo.ReciboBO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.Recibo;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.ReportException;
import ar.com.wuik.sistema.filters.ReciboFilter;
import ar.com.wuik.sistema.model.ReciboModel;
import ar.com.wuik.sistema.reportes.ReciboReporte;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.security.WSecure;
import ar.com.wuik.swing.components.table.WTablePanel;
import ar.com.wuik.swing.components.table.WToolbarButton;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.frames.WCalendarIFrame;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;

public class ReciboIFrame extends WAbstractModelIFrame implements WSecure {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 7107533032732470914L;
	private WTablePanel<Recibo> tablePanel;
	private JButton btnCerrar;
	private Long idCliente;

	/**
	 * Create the frame.
	 */
	public ReciboIFrame(Long idCliente) {
		this.idCliente = idCliente;
		setBorder(new LineBorder(null, 1, true));
		setTitle("Recibos");
		setFrameIcon(new ImageIcon( 
				ReciboIFrame.class.getResource("/icons/recibo.png")));
		setBounds(0, 0, 545, 519);
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

	private WTablePanel<Recibo> getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new WTablePanel(ReciboModel.class, Boolean.FALSE);
			tablePanel.setBounds(10, 11, 523, 429);
			tablePanel.addToolbarButtons(getToolbarButtons());
		}
		return tablePanel;
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		WToolbarButton buttonEdit = new WToolbarButton("Editar Recibo",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/edit.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							addModalIFrame(new ReciboVerIFrame(
									ReciboIFrame.this, idCliente, selectedItem));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Recibo",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Editar", null);

		WToolbarButton buttonDelete = new WToolbarButton("Eliminar Recibo",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/delete.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							try {
								ReciboBO reciboBO = AbstractFactory
										.getInstance(ReciboBO.class);
								reciboBO.eliminar(selectedItem);
								search();
							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Recibo",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Eliminar", null);

		WToolbarButton buttonVer = new WToolbarButton("Ver Recibo",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/ver_detalle.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							addModalIFrame(new ReciboVistaIFrame(
									selectedItem));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Recibo",
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
								ReciboReporte.generarRecibo(selectedItem);
							} catch (ReportException rexc) {
								showGlobalErrorMsg(rexc.getMessage());
							}

						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Recibo",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Imprimir", null);

		if (isClienteActivo()) {
//			toolbarButtons.add(buttonAdd);
			toolbarButtons.add(buttonEdit);
			toolbarButtons.add(buttonDelete);
			toolbarButtons.add(buttonImprimir);
		}
		toolbarButtons.add(buttonVer);
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
			btnCerrar.setIcon(new ImageIcon(ReciboIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(430, 451, 103, 30);
		}
		return btnCerrar;
	}

	public void search() {
		// Filtro
		ReciboFilter filter = new ReciboFilter();
		filter.setIdCliente(idCliente);

		try {
			ReciboBO reciboBO = AbstractFactory.getInstance(ReciboBO.class);
			List<Recibo> recibos = reciboBO.buscar(filter);
			getTablePanel().addData(recibos);
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
