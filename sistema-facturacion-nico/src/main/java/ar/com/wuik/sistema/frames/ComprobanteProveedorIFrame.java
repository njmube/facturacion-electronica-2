package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import ar.com.wuik.sistema.bo.ComprobanteBO;
import ar.com.wuik.sistema.bo.ProveedorBO;
import ar.com.wuik.sistema.entities.Comprobante;
import ar.com.wuik.sistema.entities.Proveedor;
import ar.com.wuik.sistema.entities.enums.EstadoFacturacion;
import ar.com.wuik.sistema.entities.enums.TipoComprobante;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.ReportException;
import ar.com.wuik.sistema.filters.ComprobanteFilter;
import ar.com.wuik.sistema.model.ComprobanteProveedorModel;
import ar.com.wuik.sistema.reportes.ComprobanteReporte;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WOption;
import ar.com.wuik.swing.components.security.WSecure;
import ar.com.wuik.swing.components.table.WTablePanel;
import ar.com.wuik.swing.components.table.WToolbarButton;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.frames.WCalendarIFrame;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;

public class ComprobanteProveedorIFrame extends WAbstractModelIFrame implements
		WSecure {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 7107533032732470914L;
	private WTablePanel<Comprobante> tablePanel;
	private JButton btnCerrar;
	private Long idProveedor;
	private static final String CAMPO_TIPO_COMP = "tipoComprobante";
	private JLabel lblTipoComprobante;
	private JComboBox cmbTipoComp;

	/**
	 * Create the frame.
	 */
	public ComprobanteProveedorIFrame(Long idProveedor) {
		this.idProveedor = idProveedor;
		setBorder(new LineBorder(null, 1, true));
		setTitle("Comprobantes");
		setFrameIcon(new ImageIcon(
				ComprobanteProveedorIFrame.class
						.getResource("/icons/comprobantes.png")));
		setBounds(0, 0, 1000, 519);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getBtnCerrar());
		getContentPane().add(getTablePanel());
		getContentPane().add(getLblTipoComprobante());
		getContentPane().add(getCmbTipoComp());
		loadTiposComprobantes();
		search();
	}

	private void loadTiposComprobantes() {
		TipoComprobante[] tiposComprobantes = TipoComprobante.values();
		getCmbTipoComp().addItem(WOption.getWOptionTodos());
		for (TipoComprobante tipoComprobante : tiposComprobantes) {
			getCmbTipoComp().addItem(
					new WOption((long) tipoComprobante.getId(), tipoComprobante
							.getDescripcion()));
		}
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
			tablePanel = new WTablePanel(ComprobanteProveedorModel.class,
					Boolean.FALSE);
			tablePanel.setBounds(10, 47, 978, 393);
			tablePanel.addToolbarButtons(getToolbarButtons());
		}
		return tablePanel;
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();
		WToolbarButton buttonEdit = new WToolbarButton("Editar Comprobante",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/edit.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							ComprobanteBO comprobanteProveedorBO = AbstractFactory
									.getInstance(ComprobanteBO.class);
							try {

								Comprobante comprobante = (Comprobante) comprobanteProveedorBO
										.obtener(selectedItem);
								if (comprobante.isActivo()) {
									addModalIFrame(new ComprobanteProveedorVerIFrame(
											ComprobanteProveedorIFrame.this,
											comprobante.getId()));
								} else {
									WTooltipUtils
											.showMessage(
													"El Comprobante debe estar activo.",
													(JButton) e.getSource(),
													MessageType.ALERTA);
								}

							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}

						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Comprobante",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Editar", null);

		WToolbarButton buttonVer = new WToolbarButton("Ver Comprobante",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/ver_detalle.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							addModalIFrame(new ComprobanteProveedorVistaIFrame(
									selectedItem));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Comprobante.",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Ver", null);

				if (isProveedorActivo()) {
			// toolbarButtons.add(buttonAdd);
			toolbarButtons.add(buttonEdit);
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
			btnCerrar.setIcon(new ImageIcon(ComprobanteProveedorIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(885, 451, 103, 30);
		}
		return btnCerrar;
	}

	public void search() {
		WModel model = populateModel();
		WOption tipoComp = model.getValue(CAMPO_TIPO_COMP);

		// Filtro
		ComprobanteFilter filter = new ComprobanteFilter();
		filter.setIdProveedor(idProveedor);
		filter.setTipoComprobante(TipoComprobante.fromValue(tipoComp.getValue()
				.intValue()));
		try {
			ComprobanteBO comprobanteBO = AbstractFactory
					.getInstance(ComprobanteBO.class);
			List<Comprobante> comprobantes = comprobanteBO.buscar(filter);
			getTablePanel().addData(comprobantes);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	@Override
	protected JComponent getFocusComponent() {
		return null;
	}

	private boolean isProveedorActivo() {
		ProveedorBO proveedorBO = AbstractFactory
				.getInstance(ProveedorBO.class);
		try {
			Proveedor proveedor = proveedorBO.obtener(idProveedor);
			return proveedor.isActivo();
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
		return Boolean.FALSE;
	}

	private JLabel getLblTipoComprobante() {
		if (lblTipoComprobante == null) {
			lblTipoComprobante = new JLabel("Tipo Comprobante:");
			lblTipoComprobante.setBounds(10, 11, 116, 25);
		}
		return lblTipoComprobante;
	}

	private JComboBox getCmbTipoComp() {
		if (cmbTipoComp == null) {
			cmbTipoComp = new JComboBox();
			cmbTipoComp.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						search();
					}
				}
			});
			cmbTipoComp.setName(CAMPO_TIPO_COMP);
			cmbTipoComp.setBounds(136, 13, 160, 23);
		}
		return cmbTipoComp;
	}
}
