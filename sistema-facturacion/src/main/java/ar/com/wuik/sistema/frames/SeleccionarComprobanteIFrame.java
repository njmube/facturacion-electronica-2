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

import ar.com.wuik.sistema.bo.ComprobanteBO;
import ar.com.wuik.sistema.entities.Comprobante;
import ar.com.wuik.sistema.entities.enums.EstadoFacturacion;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.ComprobanteFilter;
import ar.com.wuik.sistema.model.ComprobanteModel;
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

public class SeleccionarComprobanteIFrame extends WAbstractModelIFrame
		implements WSecure {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 7107533032732470914L;
	private WTablePanel<Comprobante> tablePanel;
	private JButton btnCerrar;
	private ComprobanteVerIFrame comprobanteVerIFrame;
	private ReciboVerIFrame reciboVerIFrame;
	private ComprobanteFilter filter = new ComprobanteFilter();
	

	/**
	 * Create the frame.
	 * @wbp.parser.constructor
	 */
	public SeleccionarComprobanteIFrame(
			ComprobanteVerIFrame comprobanteVerIFrame,
			List<Long> idsComprobantesToExclude, Long idCliente) {
		this.comprobanteVerIFrame = comprobanteVerIFrame;
		setBorder(new LineBorder(null, 1, true));
		setTitle("Seleccionar Comprobantes");
		setFrameIcon(new ImageIcon(
				SeleccionarComprobanteIFrame.class
						.getResource("/icons/seleccionar.png")));
		setBounds(0, 0, 1015, 424);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getTablePanel());
		getContentPane().add(getBtnCerrar());
		
		filter.setIdCliente(idCliente);
		filter.setIdsToExclude(idsComprobantesToExclude);
		filter.setEstadoFacturacion(EstadoFacturacion.FACTURADO);
		
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

	private WTablePanel<Comprobante> getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new WTablePanel(ComprobanteModel.class, Boolean.FALSE);
			tablePanel.setBounds(8, 11, 995, 340);
			tablePanel.addToolbarButtons(getToolbarButtons());
		}
		return tablePanel;
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		WToolbarButton buttonSeleccionar = new WToolbarButton(
				"Seleccionar Comprobante(s)", new ImageIcon(
						WCalendarIFrame.class.getResource("/icons/add.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						List<Long> selectedItems = tablePanel
								.getSelectedItemsID();
						if (WUtils.isNotEmpty(selectedItems)) {

							ComprobanteBO comprobanteBO = AbstractFactory
									.getInstance(ComprobanteBO.class);
							ComprobanteFilter filter = new ComprobanteFilter();
							filter.setIdsToInclude(selectedItems);
							filter.setInicializarDetalles(Boolean.TRUE);
							try {
								List<Comprobante> comprobantes = comprobanteBO
										.buscar(filter);

								if (null != comprobanteVerIFrame) {
									comprobanteVerIFrame
											.addComprobantes(comprobantes);
								}

								hideFrame();
							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}

						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar al menos un Comprobante",
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
			btnCerrar = new JButton("Cancelar");
			btnCerrar.setFocusable(false);
			btnCerrar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					hideFrame();
				}
			});
			btnCerrar.setIcon(new ImageIcon(SeleccionarComprobanteIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(900, 356, 103, 30);
		}
		return btnCerrar;
	}

	public void search() {

		List<Comprobante> comprobantes = new ArrayList<Comprobante>();
		try {
			ComprobanteBO comprobanteBO = AbstractFactory
					.getInstance(ComprobanteBO.class);
			List<Comprobante> comprobantesSinAsignar = comprobanteBO
					.buscar(filter);
			comprobantes.addAll(comprobantesSinAsignar);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
		getTablePanel().addData(comprobantes);
	}

	@Override
	protected JComponent getFocusComponent() {
		return null;
	}
}
