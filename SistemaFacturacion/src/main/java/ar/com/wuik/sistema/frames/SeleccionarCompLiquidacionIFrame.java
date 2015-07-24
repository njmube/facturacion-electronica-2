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
import ar.com.wuik.sistema.bo.NotaDebitoBO;
import ar.com.wuik.sistema.entities.Factura;
import ar.com.wuik.sistema.entities.Liquidacion;
import ar.com.wuik.sistema.entities.NotaDebito;
import ar.com.wuik.sistema.entities.enums.EstadoFacturacion;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.FacturaFilter;
import ar.com.wuik.sistema.filters.NotaDebitoFilter;
import ar.com.wuik.sistema.model.DetalleLiquidacionModel;
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

public class SeleccionarCompLiquidacionIFrame extends WAbstractModelIFrame
		implements WSecure {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 7107533032732470914L;
	private WTablePanel<Liquidacion> tablePanel;
	private JButton btnCerrar;
	private Long idCliente;
	private List<Long> idsFacturasToExclude;
	private List<Long> idsNotasDebitoToExclude;
	private ReciboVerIFrame reciboVerIFrame;
	private List<Liquidacion> liquidaciones = null;

	/**
	 * Create the frame.
	 */
	public SeleccionarCompLiquidacionIFrame(ReciboVerIFrame reciboVerIFrame,
			List<Liquidacion> liquidacionesToExclude, Long idCliente) {
		this.reciboVerIFrame = reciboVerIFrame;
		this.idCliente = idCliente;
		this.idsFacturasToExclude = new ArrayList<Long>();
		this.idsNotasDebitoToExclude = new ArrayList<Long>();
		this.liquidaciones = new ArrayList<Liquidacion>();

		if (WUtils.isNotEmpty(liquidacionesToExclude)) {
			for (Liquidacion liquidacion : liquidacionesToExclude) {
				if (null != liquidacion.getIdNotaDebito()) {
					this.idsNotasDebitoToExclude.add(liquidacion
							.getIdNotaDebito());
				}
				if (null != liquidacion.getIdFactura()) {
					this.idsFacturasToExclude.add(liquidacion.getIdFactura());
				}
			}
		}

		setBorder(new LineBorder(null, 1, true));
		setTitle("Comprobantes");
		setFrameIcon(new ImageIcon(
				SeleccionarCompLiquidacionIFrame.class
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
	}

	@Override
	protected boolean validateModel(WModel model) {
		return false;
	}

	private WTablePanel<Liquidacion> getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new WTablePanel(DetalleLiquidacionModel.class,
					Boolean.FALSE);
			tablePanel.setBounds(8, 11, 731, 340);
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
							reciboVerIFrame.addLiquidaciones(liquidaciones);
							hideFrame();
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
			btnCerrar = new JButton("Salir");
			btnCerrar.setFocusable(false);
			btnCerrar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					hideFrame();
				}
			});
			btnCerrar.setIcon(new ImageIcon(
					SeleccionarCompLiquidacionIFrame.class
							.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(636, 361, 103, 25);
		}
		return btnCerrar;
	}

	public void search() {

		try {
			FacturaFilter filterFactura = new FacturaFilter();
			filterFactura.setIdCliente(idCliente);
			filterFactura.setIdsToExclude(idsFacturasToExclude);
			filterFactura.setEstadoFacturacion(EstadoFacturacion.FACTURADO);
			filterFactura.setPaga(Boolean.FALSE);
			FacturaBO facturaBO = AbstractFactory.getInstance(FacturaBO.class);
			List<Factura> facturas = facturaBO.buscar(filterFactura);
			if (WUtils.isNotEmpty(facturas)) {
				for (Factura factura : facturas) {
					liquidaciones.add(new Liquidacion(factura));
				}
			}

		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}

		try {
			NotaDebitoFilter filterND = new NotaDebitoFilter();
			filterND.setIdCliente(idCliente);
			filterND.setIdsToExclude(idsNotasDebitoToExclude);
			filterND.setEstadoFacturacion(EstadoFacturacion.FACTURADO);
			filterND.setPaga(Boolean.FALSE);
			NotaDebitoBO notaDebitoBO = AbstractFactory
					.getInstance(NotaDebitoBO.class);
			List<NotaDebito> notasDebito = notaDebitoBO.buscar(filterND);
			if (WUtils.isNotEmpty(notasDebito)) {
				for (NotaDebito notaDebito : notasDebito) {
					liquidaciones.add(new Liquidacion(notaDebito));
				}
			}
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
		getTablePanel().addData(liquidaciones);
	}

	@Override
	protected JComponent getFocusComponent() {
		return null;
	}
}
