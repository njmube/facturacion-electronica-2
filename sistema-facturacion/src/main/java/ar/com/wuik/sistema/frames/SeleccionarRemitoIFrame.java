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

import ar.com.wuik.sistema.bo.RemitoBO;
import ar.com.wuik.sistema.entities.Remito;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.RemitoFilter;
import ar.com.wuik.sistema.model.RemitoModel;
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

public class SeleccionarRemitoIFrame extends WAbstractModelIFrame implements
		WSecure {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 7107533032732470914L;
	private WTablePanel<Remito> tablePanel;
	private JButton btnCerrar;
	private Long idCliente;
	private List<Long> idsRemitosToExclude;
	private FacturaVerIFrame ventaClienteVerIFrame;

	/**
	 * Create the frame.
	 */
	public SeleccionarRemitoIFrame(FacturaVerIFrame ventaClienteVerIFrame,  List<Long> idsRemitosToExclude,
			Long idCliente) {
		this.idsRemitosToExclude = idsRemitosToExclude;
		this.ventaClienteVerIFrame = ventaClienteVerIFrame;
		this.idCliente = idCliente;
		setBorder(new LineBorder(null, 1, true));
		setTitle("Remitos");
		setFrameIcon(new ImageIcon(
				SeleccionarRemitoIFrame.class.getResource("/icons/remitos.png")));
		setBounds(0, 0, 636, 271);
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

	private WTablePanel<Remito> getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new WTablePanel(RemitoModel.class, Boolean.FALSE);
			tablePanel.setBounds(8, 11, 617, 184);
			tablePanel.addToolbarButtons(getToolbarButtons());
		}
		return tablePanel;
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		WToolbarButton buttonSeleccionar = new WToolbarButton(
				"Seleccionar Remito(s)", new ImageIcon(
						WCalendarIFrame.class.getResource("/icons/add.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						List<Long> selectedItems = tablePanel
								.getSelectedItemsID();
						if (WUtils.isNotEmpty(selectedItems)) {

							RemitoBO remitoBO = AbstractFactory
									.getInstance(RemitoBO.class);
							RemitoFilter filter = new RemitoFilter();
							filter.setIdsToInclude(selectedItems);
							filter.setInicializarDetalles(Boolean.TRUE);
							try {
								List<Remito> remitos = remitoBO.buscar(filter);
								ventaClienteVerIFrame.addRemitos(remitos);
								hideFrame();
							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}

						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar al menos un Item",
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
			btnCerrar.setIcon(new ImageIcon(SeleccionarRemitoIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(522, 206, 103, 25);
		}
		return btnCerrar;
	}

	public void search() {
		RemitoFilter filter = new RemitoFilter();
		filter.setIdCliente(idCliente);
		filter.setAsignado(Boolean.FALSE);
		filter.setActivo(Boolean.TRUE);
		filter.setIdsToExclude(idsRemitosToExclude);

		List<Remito> remitos = new ArrayList<Remito>();
		try {
			RemitoBO remitoBO = AbstractFactory.getInstance(RemitoBO.class);
			List<Remito> remitosSinAsignar = remitoBO.buscar(filter);
			remitos.addAll(remitosSinAsignar);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
		getTablePanel().addData(remitos);
	}

	@Override
	protected JComponent getFocusComponent() {
		return null;
	}
}
