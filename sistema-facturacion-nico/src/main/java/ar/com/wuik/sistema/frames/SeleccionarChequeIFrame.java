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

import ar.com.wuik.sistema.bo.ChequeBO;
import ar.com.wuik.sistema.entities.Cheque;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.ChequeFilter;
import ar.com.wuik.sistema.model.ChequeModel;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.security.WSecure;
import ar.com.wuik.swing.components.table.WTablePanel;
import ar.com.wuik.swing.components.table.WToolbarButton;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.frames.WCalendarIFrame;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;

public class SeleccionarChequeIFrame extends WAbstractModelIFrame implements
		WSecure {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 7107533032732470914L;
	private WTablePanel<Cheque> tablePanel;
	private JButton btnCerrar;
	private Long idCliente;
	private List<Long> idsChequesToExclude;
	private ReciboVerIFrame reciboVerIFrame;

	/**
	 * Create the frame.
	 */
	public SeleccionarChequeIFrame(ReciboVerIFrame reciboVerIFrame,
			List<Long> idsChequesToExclude, Long idCliente) {
		this.idsChequesToExclude = idsChequesToExclude;
		this.reciboVerIFrame = reciboVerIFrame;
		this.idCliente = idCliente;
		setBorder(new LineBorder(null, 1, true));
		setTitle("Seleccionar Cheques");
		setFrameIcon(new ImageIcon(
				SeleccionarComprobanteIFrame.class
						.getResource("/icons/seleccionar.png")));
		setBounds(0, 0, 751, 424);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getTablePanel());
		getContentPane().add(getBtnCerrar());
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

	private WTablePanel<Cheque> getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new WTablePanel(ChequeModel.class, Boolean.FALSE);
			tablePanel.setBounds(8, 11, 731, 340);
			tablePanel.addToolbarButtons(getToolbarButtons());
		}
		return tablePanel;
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		WToolbarButton buttonSeleccionar = new WToolbarButton(
				"Seleccionar Cheque(s)", new ImageIcon(
						WCalendarIFrame.class.getResource("/icons/add.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						List<Long> selectedItems = tablePanel
								.getSelectedItemsID();
						if (WUtils.isNotEmpty(selectedItems)) {

							ChequeBO chequeBO = AbstractFactory
									.getInstance(ChequeBO.class);
							ChequeFilter filter = new ChequeFilter();
							filter.setIdsToInclude(selectedItems);
							try {
								List<Cheque> cheques = chequeBO.buscar(filter);
								if (null != reciboVerIFrame) {
									reciboVerIFrame.addCheques(cheques);
								}
								hideFrame();
							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}

						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar al menos un Cheque",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Seleccionar", null);

		toolbarButtons.add(buttonSeleccionar);
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
			btnCerrar.setIcon(new ImageIcon(SeleccionarChequeIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(636, 361, 103, 25);
		}
		return btnCerrar;
	}

	public void search() {
		ChequeBO chequeBO = AbstractFactory.getInstance(ChequeBO.class);
		ChequeFilter filter = new ChequeFilter();
		filter.setEnUso(Boolean.FALSE);
		filter.setIdCliente(idCliente);
		filter.setIdsToExclude(idsChequesToExclude);
		List<Cheque> chequesSinAsignar = null;
		try {
			chequesSinAsignar = chequeBO.buscar(filter);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
		getTablePanel().addData(chequesSinAsignar);
	}

	@Override
	protected JComponent getFocusComponent() {
		return null;
	}
}
