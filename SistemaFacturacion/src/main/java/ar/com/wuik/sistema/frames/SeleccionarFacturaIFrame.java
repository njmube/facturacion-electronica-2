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

import ar.com.wuik.sistema.bo.FacturaBO;
import ar.com.wuik.sistema.entities.Factura;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.FacturaFilter;
import ar.com.wuik.sistema.model.FacturaModel;
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

public class SeleccionarFacturaIFrame extends WAbstractModelIFrame implements
		WSecure {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 7107533032732470914L;
	private WTablePanel<Factura> tablePanel;
	private JButton btnCerrar;
	private Long idCliente;
	private List<Long> idsFacturasToExclude;
	private NotaCreditoVerIFrame notaCreditoVerIFrame;
	private NotaDebitoVerIFrame notaDebitoVerIFrame;

	/**
	 * Create the frame.
	 */
	public SeleccionarFacturaIFrame(NotaCreditoVerIFrame notaCreditoVerIFrame,
			List<Long> idsFacturasToExclude, Long idCliente) {
		this.idsFacturasToExclude = idsFacturasToExclude;
		this.notaCreditoVerIFrame = notaCreditoVerIFrame;
		this.idCliente = idCliente;
		setBorder(new LineBorder(null, 1, true));
		setTitle("Facturas");
		setFrameIcon(new ImageIcon(
				SeleccionarFacturaIFrame.class
						.getResource("/icons/facturas.png")));
		setBounds(0, 0, 751, 424);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getTablePanel());
		getContentPane().add(getBtnCerrar());
		search();
	}

	public SeleccionarFacturaIFrame(NotaDebitoVerIFrame notaDebitoVerIFrame,
			List<Long> idsFacturasToExclude, Long idCliente) {
		this.idsFacturasToExclude = idsFacturasToExclude;
		this.notaDebitoVerIFrame = notaDebitoVerIFrame;
		this.idCliente = idCliente;
		setBorder(new LineBorder(null, 1, true));
		setTitle("Facturas");
		setFrameIcon(new ImageIcon(
				SeleccionarFacturaIFrame.class
						.getResource("/icons/facturas.png")));
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
		getTablePanel().applySecurity(permisos);
	}

	@Override
	protected boolean validateModel(WModel model) {
		return false;
	}

	private WTablePanel<Factura> getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new WTablePanel(FacturaModel.class, Boolean.FALSE);
			tablePanel.setBounds(8, 11, 731, 340);
			tablePanel.addToolbarButtons(getToolbarButtons());
		}
		return tablePanel;
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		WToolbarButton buttonSeleccionar = new WToolbarButton(
				"Seleccionar Factura(s)", new ImageIcon(
						WCalendarIFrame.class.getResource("/icons/add.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						List<Long> selectedItems = tablePanel
								.getSelectedItemsID();
						if (WUtils.isNotEmpty(selectedItems)) {

							FacturaBO facturaBO = AbstractFactory
									.getInstance(FacturaBO.class);
							FacturaFilter filter = new FacturaFilter();
							filter.setIdsToInclude(selectedItems);
							filter.setInicializarDetalles(Boolean.TRUE);
							try {
								List<Factura> facturas = facturaBO
										.buscar(filter);
								if (null != notaCreditoVerIFrame) {
									notaCreditoVerIFrame.addFacturas(facturas);
								} else if (null != notaDebitoVerIFrame) {
									notaDebitoVerIFrame.addFacturas(facturas);
								}
								hideFrame();
							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}

						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar al menos una Factura",
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
			btnCerrar.setIcon(new ImageIcon(SeleccionarFacturaIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(636, 361, 103, 25);
		}
		return btnCerrar;
	}

	public void search() {
		FacturaFilter filter = new FacturaFilter();
		filter.setIdCliente(idCliente);
		filter.setIdsToExclude(idsFacturasToExclude);
		filter.setFacturado(Boolean.TRUE);

		List<Factura> facturas = new ArrayList<Factura>();
		try {
			FacturaBO facturaBO = AbstractFactory.getInstance(FacturaBO.class);
			List<Factura> facturasSinAsignar = facturaBO.buscar(filter);
			facturas.addAll(facturasSinAsignar);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
		getTablePanel().addData(facturas);
	}

	@Override
	protected JComponent getFocusComponent() {
		return null;
	}
}
