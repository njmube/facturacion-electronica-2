package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ar.com.wuik.sistema.bo.ComprobanteBO;
import ar.com.wuik.sistema.bo.ProveedorBO;
import ar.com.wuik.sistema.entities.Comprobante;
import ar.com.wuik.sistema.entities.DetalleComprobante;
import ar.com.wuik.sistema.entities.Proveedor;
import ar.com.wuik.sistema.entities.Remito;
import ar.com.wuik.sistema.entities.TributoComprobante;
import ar.com.wuik.sistema.entities.enums.TipoComprobante;
import ar.com.wuik.sistema.entities.enums.TipoLetraComprobante;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.model.TributoComprobanteModel;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WTextFieldDecimal;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.components.table.WTablePanel;
import ar.com.wuik.swing.components.table.WToolbarButton;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.frames.WCalendarIFrame;
import ar.com.wuik.swing.utils.WFrameUtils;
import ar.com.wuik.swing.utils.WFrameUtils.FontSize;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;

import com.lowagie.text.Font;

public class ComprobanteProveedorVerIFrame extends WAbstractModelIFrame {
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6838619883125511589L;
	private static final String CAMPO_FECHA_EMISION = "fechaEmision";
	private static final String CAMPO_OBSERVACIONES = "observaciones";
	private static final String CAMPO_FECHA_CAE = "fechaCAE";
	private JPanel pnlBusqueda;
	private JButton btnCerrar;
	private Comprobante comprobante;
	private JButton btnGuardar;
	private ComprobanteProveedorIFrame comprobanteProveedorIFrame;
	private JButton btnFechaEmision;
	private JTextField txtFechaEmision;
	private JLabel lblFechaEmisin;
	private JLabel lblObservaciones;
	private JTextArea txaObservaciones;
	private JTextField txtNumComp;
	private JLabel lblNunComp;
	private JLabel lblTotal;
	private JLabel txtTotalPesos;
	private JLabel lblTipoIVA;
	private JPanel panel_1;
	private JLabel lblProveedor;
	private JLabel txtProveedor;
	private JLabel txtCondIVA;
	private JLabel lblCondIva;
	private JLabel lblLetra;
	private JLabel lblTipoComp;
	private WTablePanel<TributoComprobante> tblTributos;
	private JLabel lblImporteOtrosTributos;
	private JLabel lblOtrosTributos;
	private JCheckBox chckbxAgregarTributos;
	private JLabel label_1;
	private JPanel panel;
	private JLabel labelCAE;
	private JLabel labelFechaVencCAE;
	private JTextField txtFechaCAE;
	private JButton btnFechaCAE;
	private JLabel label;
	private JTextField txtCAE;
	private WTextFieldDecimal txtGravados;
	private WTextFieldDecimal txtIVA; 
	private JTextArea textArea;
	private JScrollPane scrollPane;

	/**
	 * @wbp.parser.constructor
	 */
	public ComprobanteProveedorVerIFrame(ComprobanteProveedorIFrame comprobanteProveedorIFrame,
			Long idComprobante) {
		
		this.comprobanteProveedorIFrame = comprobanteProveedorIFrame;

		WModel model = populateModel();
		initialize();
		ComprobanteBO comprobanteBO = AbstractFactory
				.getInstance(ComprobanteBO.class);
		try {
			this.comprobante = comprobanteBO.obtener(idComprobante);
			model.addValue(CAMPO_FECHA_EMISION,
					WUtils.getStringFromDate(comprobante.getFechaVenta()));
			model.addValue(CAMPO_OBSERVACIONES, comprobante.getObservaciones());
			model.addValue(CAMPO_FECHA_CAE,
					WUtils.getStringFromDate(comprobante.getFechaVenta()));			
			
			BigDecimal subtotal = BigDecimal.ZERO;
			BigDecimal subtotalIVA = BigDecimal.ZERO;
			BigDecimal total = BigDecimal.ZERO;
			BigDecimal totalTributo = BigDecimal.ZERO;

			subtotal = comprobante.getSubtotal();
			subtotalIVA = comprobante.getIva();
			total = comprobante.getTotal();
			totalTributo = comprobante.getTotalTributos();
			
			txtNumComp.setText(comprobante.getNroComprobante());
			txtCAE.setText(comprobante.getCae());
			txaObservaciones.setText(comprobante.getObservaciones());
			txtGravados.setText(subtotal.toString());
			txtIVA.setText(subtotalIVA.toString());
			txtTotalPesos.setText(total.toString());

			if (!comprobante.getTributos().isEmpty()) {
				getChckbxAgregarTributos().setSelected(Boolean.TRUE);
			}

			getTblTributos().addData(comprobante.getTributos());

			getLblImporteOtrosTributos().setText(totalTributo.toString());
			
			
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}

		String titulo = "";
		Icon icon = null;

		switch (comprobante.getTipoComprobante()) {
		case FACTURA:
			titulo = "Editar Venta";
			lblTipoComp.setText("FACTURA");
			icon = new ImageIcon(
					ComprobanteIFrame.class.getResource("/icons/facturas.png"));
			break;
		case NOTA_CREDITO:
			titulo = "Editar Nota de Crédito";
			lblTipoComp.setText("NOTA DE CRÉDITO");
			icon = new ImageIcon(
					ComprobanteIFrame.class
							.getResource("/icons/notas_credito.png"));
			break;
		case NOTA_DEBITO:
			titulo = "Editar Nota de Débito";
			lblTipoComp.setText("NOTA DE DÉBITO");
			icon = new ImageIcon(
					ComprobanteIFrame.class
							.getResource("/icons/notas_debito.png"));
			break;
		}

		setTitle(titulo);
		setFrameIcon(icon);

		populateComponents(model);
		loadProveedor(comprobante.getProveedor());
	}

	public ComprobanteProveedorVerIFrame(Long idProveedor,
			TipoComprobante tipoComprobante) {

		initialize();

		WModel model = populateModel();
		model.addValue(CAMPO_FECHA_EMISION,
				WUtils.getStringFromDate(new Date()));
		model.addValue(CAMPO_FECHA_CAE, WUtils.getStringFromDate(new Date()));
		this.comprobante = new Comprobante();
		this.comprobante.setTipoComprobante(tipoComprobante);
		this.comprobante.setIdProveedor(idProveedor);

		String titulo = "";
		Icon icon = null;

		switch (tipoComprobante) {
		case FACTURA:
			titulo = "Nueva Factura de Proveedor";
			icon = new ImageIcon(
					ComprobanteIFrame.class.getResource("/icons/facturas.png"));
			lblTipoComp.setText("FACTURA");
			break;
		case NOTA_CREDITO:
			titulo = "Nueva Nota de Crédito";
			icon = new ImageIcon(
					ComprobanteIFrame.class
							.getResource("/icons/notas_credito.png"));
			lblTipoComp.setText("NOTA DE CRÉDITO");
			break;
		case NOTA_DEBITO:
			titulo = "Nueva Nota de Débito";
			icon = new ImageIcon(
					ComprobanteIFrame.class
							.getResource("/icons/notas_debito.png"));
			lblTipoComp.setText("NOTA DE DÉBITO");
			break;
		}

		setTitle(titulo);
		setFrameIcon(icon);

		try {
			ProveedorBO proveedorBO = AbstractFactory
					.getInstance(ProveedorBO.class);
			Proveedor proveedor = proveedorBO.obtener(idProveedor);
			this.comprobante.setTipoLetraComprobante(proveedor
					.getCondicionIVA().getTipoLetraComprobante());
			loadProveedor(proveedor);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
		populateComponents(model);
	}

	private void loadProveedor(Proveedor proveedor) {
		getTxtProveedor().setText(proveedor.getRazonSocial());
		getTxtCondIVA().setText(proveedor.getCondicionIVA().getDenominacion());
		if (comprobante.getTipoLetraComprobante()
				.equals(TipoLetraComprobante.A)) {
			lblLetra.setIcon(new ImageIcon(ComprobanteProveedorVerIFrame.class
					.getResource("/icons32/letra_a.png")));
		} else {
			lblLetra.setIcon(new ImageIcon(ComprobanteProveedorVerIFrame.class
					.getResource("/icons32/letra_b.png")));
		}
	}

	private void initialize() {
		setBorder(new LineBorder(null, 1, true));
		setBounds(0, 0, 1020, 469);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getBtnCerrar());
		getContentPane().add(getBtnGuardar());
	}

	@Override
	protected boolean validateModel(WModel model, JComponent component) {

		String fechaEmision = model.getValue(CAMPO_FECHA_EMISION);
		
		List<String> messages = new ArrayList<String>();

		if (WUtils.isEmpty(fechaEmision)) {
			messages.add("Debe ingresar una Fecha de Emisión");
		}

		if (WUtils.isEmpty(txtNumComp.getText())) {
			messages.add("Debe ingresar un número de comprobante");
		}

		if (WUtils.isEmpty(txtGravados.getText())) {
			messages.add("Debe ingresar el importe neto gravado");
		}
		
		if (WUtils.isEmpty(txtIVA.getText())) {
			messages.add("Debe ingresar un valor de IVA");
		}

		WTooltipUtils.showMessages(messages, component, MessageType.ERROR);

		return WUtils.isEmpty(messages);
	}

	private JPanel getPnlBusqueda() {
		if (pnlBusqueda == null) {
			pnlBusqueda = new JPanel();
			pnlBusqueda.setBorder(new TitledBorder(UIManager
					.getBorder("TitledBorder.border"), "",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlBusqueda.setBounds(10, 11, 1001, 379);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getBtnFechaEmision());
			pnlBusqueda.add(getTxtFechaEmision());
			pnlBusqueda.add(getLblFechaEmisin());
			pnlBusqueda.add(getPanel_1());
			pnlBusqueda.add(getLblProveedor());
			pnlBusqueda.add(getTxtProveedor());
			pnlBusqueda.add(getTxtCondIVA());
			pnlBusqueda.add(getLblCondIva());
			pnlBusqueda.add(getLblLetra());
			pnlBusqueda.add(getLblTipoComp());
			pnlBusqueda.add(getPanel());

		}
		return pnlBusqueda;
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
			btnCerrar.setIcon(new ImageIcon(ComprobanteProveedorVerIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(795, 401, 103, 30);
		}
		return btnCerrar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setIcon(new ImageIcon(
					ComprobanteProveedorVerIFrame.class
							.getResource("/icons/ok.png")));
			btnGuardar.setBounds(908, 401, 103, 30);
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					WModel model = populateModel();
					if (validateModel(model, btnGuardar)) {
						String fechaVenta = model.getValue(CAMPO_FECHA_EMISION);
						String observaciones = model
								.getValue(CAMPO_OBSERVACIONES);

						comprobante.setFechaVenta(WUtils
								.getDateFromString(fechaVenta));
						comprobante.setObservaciones(observaciones);

						if (!getChckbxAgregarTributos().isSelected()) {
							comprobante.getTributos().clear();
						}

						try {
							ComprobanteBO comprobanteBO = AbstractFactory
									.getInstance(ComprobanteBO.class);
							if (comprobante.getId() == null) {
								comprobanteBO.guardar(comprobante);								
							} else {
								comprobanteBO.actualizar(comprobante);
							}
							hideFrame();
							showGlobalMsg("Comprobante de proveedor agregado exitosamente");
							if (null != comprobanteProveedorIFrame) {
								comprobanteProveedorIFrame.search();
							}
						} catch (BusinessException bexc) {
							showGlobalErrorMsg(bexc.getMessage());
						}
					}
				}
			});
		}
		return btnGuardar;
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		return toolbarButtons;
	}

	private List<WToolbarButton> getToolbarButtonsComprobantes() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		return toolbarButtons;
	}

	protected List<Long> getIdsRemitos() {
		List<Long> ids = new ArrayList<Long>();
		List<Remito> remitos = comprobante.getRemitos();
		for (Remito remito : remitos) {
			ids.add(remito.getId());
		}
		return ids;
	}

	protected List<Long> getIdsComprobantes() {
		List<Long> ids = new ArrayList<Long>();
		Set<Comprobante> comprobantes = comprobante.getComprobantesAsociados();
		for (Comprobante comprobante : comprobantes) {
			ids.add(comprobante.getId());
		}
		return ids;
	}

	protected void refreshRemitos() {
	}

	protected Remito getRemitoById(Long selectedItem) {
		List<Remito> remitos = comprobante.getRemitos();
		for (Remito remito : remitos) {
			if (remito.getId().equals(selectedItem)) {
				return remito;
			}
		}
		return null;
	}

	protected Comprobante getComprobanteById(Long selectedItem) {
		Set<Comprobante> comprobantes = comprobante.getComprobantesAsociados();
		for (Comprobante comprobante : comprobantes) {
			if (comprobante.getId().equals(selectedItem)) {
				return comprobante;
			}
		}
		return null;
	}

	private JButton getBtnFechaEmision() {
		if (btnFechaEmision == null) {
			btnFechaEmision = new JButton("");
			btnFechaEmision.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addModalIFrame(new WCalendarIFrame(txtFechaEmision));
				}
			});
			btnFechaEmision.setIcon(new ImageIcon(
					ComprobanteProveedorVerIFrame.class
							.getResource("/icons/calendar.png")));
			btnFechaEmision.setBounds(602, 21, 25, 25);
		}
		return btnFechaEmision;
	}

	private JTextField getTxtFechaEmision() {
		if (txtFechaEmision == null) {
			txtFechaEmision = new JTextField();
			txtFechaEmision.setName(CAMPO_FECHA_EMISION);
			txtFechaEmision.setEditable(false);
			txtFechaEmision.setBounds(486, 21, 106, 25);
		}
		return txtFechaEmision;
	}

	private JLabel getLblFechaEmisin() {
		if (lblFechaEmisin == null) {
			lblFechaEmisin = new JLabel("* Fecha Emisi\u00F3n:");
			lblFechaEmisin.setHorizontalAlignment(SwingConstants.RIGHT);
			lblFechaEmisin.setBounds(384, 21, 92, 25);
		}
		return lblFechaEmisin;
	}

	private JLabel getLblObservaciones() {
		if (lblObservaciones == null) {
			lblObservaciones = new JLabel("Observaciones:");
			lblObservaciones.setIcon(new ImageIcon(
					ComprobanteProveedorVerIFrame.class
							.getResource("/icons/observaciones.png")));
			lblObservaciones.setBounds(482, 8, 116, 25);
			lblObservaciones.setHorizontalAlignment(SwingConstants.LEFT);
		}
		return lblObservaciones;
	}
	
	private JTextArea getTxaObservaciones() {
		if (txaObservaciones == null) {
			txaObservaciones = new JTextArea();
			txaObservaciones.setLineWrap(true);
			txaObservaciones.setName(CAMPO_OBSERVACIONES);
			txaObservaciones.setDocument(new WTextFieldLimit(100));
		}
		return txaObservaciones;
	}

	private List<WToolbarButton> getToolbarButtonsDetalles() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		return toolbarButtons;
	}

	protected DetalleComprobante getDetalleById(Long selectedItem) {
		List<DetalleComprobante> detalles = comprobante.getDetalles();
		for (DetalleComprobante detalleFactura : detalles) {
			if (detalleFactura.getCoalesceId().equals(selectedItem)) {
				return detalleFactura;
			}
		}
		return null;
	}

	private JTextField getTxtNumComp() {
		if (txtNumComp == null) {
			txtNumComp = new JTextField();
			txtNumComp.setBounds(149, 14, 216, 25);
			txtNumComp.setHorizontalAlignment(SwingConstants.LEFT);
			txtNumComp.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return txtNumComp;
	}

	private JLabel getLblNunComp() {
		if (lblNunComp == null) {
			lblNunComp = new JLabel("* N\u00FAmero Comprobante:");
			lblNunComp.setBounds(10, 11, 146, 25);
			lblNunComp.setHorizontalAlignment(SwingConstants.LEFT);
		}
		return lblNunComp;
	}

	private JLabel getLblTotal() {
		if (lblTotal == null) {
			lblTotal = new JLabel("Importe Total: $");
			lblTotal.setBounds(711, 152, 125, 25);
			lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblTotal;
	}

	private JLabel getTxtTotalPesos() {
		if (txtTotalPesos == null) {
			txtTotalPesos = new JLabel();
			txtTotalPesos.setBounds(846, 152, 125, 25);
			txtTotalPesos.setHorizontalAlignment(SwingConstants.RIGHT);
			txtTotalPesos.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return txtTotalPesos;
	}

	private void calcularTotales() {

		BigDecimal subtotal = BigDecimal.ZERO;
		BigDecimal subtotalIVA = BigDecimal.ZERO;
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal totalTributo = BigDecimal.ZERO;
		String numComp = null;
		if (null != txtNumComp.getText() && txtNumComp.getText().trim().length() > 0){
			numComp = txtNumComp.getText();
		}
		comprobante.setNroComprobante(numComp);
		
		String cae = null;
		if (null != txtCAE.getText() && txtCAE.getText().trim().length() > 0){
			cae = txtCAE.getText();
		}
		comprobante.setCae(cae);
		
		WModel model = populateModel();		
		String fechaCAE = model.getValue(CAMPO_FECHA_CAE);		
		comprobante.setFechaCAE(WUtils.getDateFromString(fechaCAE));		

		List<TributoComprobante> tributos = comprobante.getTributos();
		for (TributoComprobante tributoComprobante : tributos) {
			totalTributo = totalTributo.add(tributoComprobante.getImporte());
		}
		
		if (null != txtGravados.getText() && txtGravados.getText().trim().length() > 0){
			subtotal = new BigDecimal(txtGravados.getText());
		}
		if (null != txtIVA.getText() && txtIVA.getText().trim().length() > 0){
			subtotalIVA = new BigDecimal(txtIVA.getText());
		}		
		total = subtotal.add(subtotalIVA).add(totalTributo);

		comprobante.setSubtotal(subtotal);
		comprobante.setTotalTributos(totalTributo);		
		comprobante.setIva(subtotalIVA);
		comprobante.setTotal(total);
		getTxtTotalPesos().setText(String.valueOf(total));
		getLblImporteOtrosTributos().setText(
				WUtils.getValue(totalTributo).toEngineeringString());
	}

	private JLabel getLblTipoIVA() {
		if (lblTipoIVA == null) {
			lblTipoIVA = new JLabel("* IVA $:");
			lblTipoIVA.setBounds(760, 49, 76, 25);
			lblTipoIVA.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblTipoIVA;
	}

	public void refreshDetalles() {
		calcularTotales();
	}

	private JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			panel_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING,
					TitledBorder.TOP, null, null));
			panel_1.setBounds(10, 174, 981, 189);
			panel_1.setLayout(null);
			panel_1.add(getTxtTotalPesos());
			panel_1.add(getLblTotal());
			panel_1.add(getLblTipoIVA());
			panel_1.add(getLblObservaciones());
			panel_1.add(getTblTributos());
			panel_1.add(getLblImporteOtrosTributos());
			panel_1.add(getLblOtrosTributos());
			panel_1.add(getChckbxAgregarTributos());
			panel_1.add(getLabel_1());
			panel_1.add(getLabel());
			panel_1.add(getTxtGravados());
			panel_1.add(getTxtIVA());
			panel_1.add(getScrollPane());
		}
		return panel_1;
	}
	
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setBounds(482, 42, 187, 83);
			scrollPane.setViewportView(getTxaObservaciones());
		}
		return scrollPane;
	}

	public void addComprobantes(List<Comprobante> comprobantes) {
		comprobante.getComprobantesAsociados().addAll(comprobantes);
		refreshComprobantesAsoc();
	}

	private void refreshComprobantesAsoc() {
	}

	private JLabel getLblProveedor() {
		if (lblProveedor == null) {
			lblProveedor = new JLabel("Proveedor:");
			lblProveedor.setHorizontalAlignment(SwingConstants.RIGHT);
			lblProveedor.setIcon(new ImageIcon(
					ComprobanteProveedorVerIFrame.class
							.getResource("/icons/cliente.png")));
			lblProveedor.setBounds(10, 21, 85, 25);
		}
		return lblProveedor;
	}

	private JLabel getTxtProveedor() {
		if (txtProveedor == null) {
			txtProveedor = new JLabel();
			txtProveedor.setBorder(null);
			txtProveedor.setBounds(121, 21, 269, 25);
			txtProveedor.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return txtProveedor;
	}

	private JLabel getTxtCondIVA() {
		if (txtCondIVA == null) {
			txtCondIVA = new JLabel();
			txtCondIVA.setBorder(null);
			txtCondIVA.setBounds(89, 59, 284, 25);
			txtCondIVA.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return txtCondIVA;
	}

	private JLabel getLblCondIva() {
		if (lblCondIva == null) {
			lblCondIva = new JLabel("Cond. IVA:");
			lblCondIva.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCondIva.setBounds(10, 57, 69, 25);
		}
		return lblCondIva;
	}

	private JLabel getLblLetra() {
		if (lblLetra == null) {
			lblLetra = new JLabel("");
			lblLetra.setBorder(new TitledBorder(null, "", TitledBorder.LEADING,
					TitledBorder.TOP, null, null));
			lblLetra.setHorizontalAlignment(SwingConstants.CENTER);
			lblLetra.setBounds(695, 21, 50, 50);
		}
		return lblLetra;
	}

	private JLabel getLblTipoComp() {
		if (lblTipoComp == null) {
			lblTipoComp = new JLabel("");
			lblTipoComp.setBorder(new TitledBorder(null, "",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			lblTipoComp.setHorizontalAlignment(SwingConstants.CENTER);
			lblTipoComp.setBounds(755, 21, 236, 50);
			lblTipoComp.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return lblTipoComp;
	}

	private WTablePanel<TributoComprobante> getTblTributos() {
		if (tblTributos == null) {
			tblTributos = new WTablePanel(TributoComprobanteModel.class, "");
			tblTributos.setVisible(false);
			tblTributos.addToolbarButtons(getToolbarButtonsTributos());
			tblTributos.setBounds(10, 42, 462, 132);
		}
		return tblTributos;
	}

	public void addTributo(TributoComprobante tributo) {
		if (null == tributo.getCoalesceId()) {
			tributo.setComprobante(comprobante);
			tributo.setTemporalId(System.currentTimeMillis());
			comprobante.getTributos().add(tributo);
		}
		refreshTributos();
	}

	private void refreshTributos() {
		getTblTributos().addData(comprobante.getTributos());
		calcularTotales();
	}

	private List<WToolbarButton> getToolbarButtonsTributos() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();
		WToolbarButton buttonAdd = new WToolbarButton("Nuevo Tributo",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/add.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						 addModalIFrame(new TributoComprobanteVerIFrame(
						 ComprobanteProveedorVerIFrame.this));
					}
				}, "Nuevo", null);

		WToolbarButton buttonEdit = new WToolbarButton("Editar Tributo",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/edit.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tblTributos.getSelectedItemID();
						if (null != selectedItem) {
							TributoComprobante tributo = getTributoById(selectedItem);
							 addModalIFrame(new TributoComprobanteVerIFrame(
							 tributo, ComprobanteProveedorVerIFrame.this));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Tributo",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Editar", null);
		WToolbarButton buttonEliminar = new WToolbarButton("Eliminar Tributo",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/delete.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						List<Long> selectedItems = tblTributos
								.getSelectedItemsID();
						if (WUtils.isNotEmpty(selectedItems)) {
							for (Long selectedItem : selectedItems) {
								TributoComprobante tributo = getTributoById(selectedItem);
								comprobante.getTributos().remove(tributo);
							}
							refreshTributos();
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar al menos un Tributo",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Eliminar", null);

		toolbarButtons.add(buttonAdd);
		toolbarButtons.add(buttonEdit);
		toolbarButtons.add(buttonEliminar);
		return toolbarButtons;
	}

	protected TributoComprobante getTributoById(Long selectedItem) {
		List<TributoComprobante> tributos = comprobante.getTributos();
		for (TributoComprobante tributoComprobante : tributos) {
			if (tributoComprobante.getCoalesceId().equals(selectedItem)) {
				return tributoComprobante;
			}
		}
		return null;
	}

	private JLabel getLblImporteOtrosTributos() {
		if (lblImporteOtrosTributos == null) {
			lblImporteOtrosTributos = new JLabel();
			lblImporteOtrosTributos.setText("$ 0.00");
			lblImporteOtrosTributos
					.setHorizontalAlignment(SwingConstants.RIGHT);
			lblImporteOtrosTributos.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			lblImporteOtrosTributos.setBounds(846, 94, 125, 25);
		}
		return lblImporteOtrosTributos;
	}

	private JLabel getLblOtrosTributos() {
		if (lblOtrosTributos == null) {
			lblOtrosTributos = new JLabel("Importe Otros Tributos: $");
			lblOtrosTributos.setHorizontalAlignment(SwingConstants.RIGHT);
			lblOtrosTributos.setBounds(696, 94, 140, 25);
		}
		return lblOtrosTributos;
	}

	private JCheckBox getChckbxAgregarTributos() {
		if (chckbxAgregarTributos == null) {
			chckbxAgregarTributos = new JCheckBox("Agregar Tributos");
			chckbxAgregarTributos.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						getTblTributos().setVisible(Boolean.TRUE);
					} else {
						getTblTributos().setVisible(Boolean.FALSE);
					}
				}
			});
			chckbxAgregarTributos.setBounds(44, 9, 157, 23);
		}
		return chckbxAgregarTributos;
	}

	private JLabel getLabel_1() {
		if (label_1 == null) {
			label_1 = new JLabel("");
			label_1.setIcon(new ImageIcon(ComprobanteProveedorVerIFrame.class
					.getResource("/icons/compAsociados.png")));
			label_1.setHorizontalAlignment(SwingConstants.CENTER);
			label_1.setBounds(13, 11, 25, 23);
		}
		return label_1;
	}


	@Override
	protected JComponent getFocusComponent() {
		// TODO Auto-generated method stub
		return null;
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING,
					TitledBorder.TOP, null, null));
			panel.setBounds(10, 113, 981, 50);
			panel.setLayout(null);
			panel.add(getLblNunComp());
			panel.add(getTxtNumComp());
			panel.add(getLabelCAE());
			panel.add(getLabelFechaVencCAE());
			panel.add(getTxtFechaCAE());
			panel.add(getBtnFechaCAE());
			panel.add(getTextField_1());
		}
		return panel;
	}

	private JLabel getLabelCAE() {
		if (labelCAE == null) {
			labelCAE = new JLabel("CAE:");
			labelCAE.setHorizontalAlignment(SwingConstants.LEFT);
			labelCAE.setBounds(409, 11, 36, 25);
		}
		return labelCAE;
	}

	private JLabel getLabelFechaVencCAE() {
		if (labelFechaVencCAE == null) {
			labelFechaVencCAE = new JLabel("Fecha Vencimiento CAE:");
			labelFechaVencCAE.setHorizontalAlignment(SwingConstants.LEFT);
			labelFechaVencCAE.setBounds(700, 11, 146, 25);
		}
		return labelFechaVencCAE;
	}

	private JTextField getTxtFechaCAE() {
		if (txtFechaCAE == null) {
			txtFechaCAE = new JTextField();
			txtFechaCAE.setName(CAMPO_FECHA_CAE);
			txtFechaCAE.setEditable(false);
			txtFechaCAE.setBounds(848, 14, 88, 25);
		}
		return txtFechaCAE;
	}

	private JButton getBtnFechaCAE() {
		if (btnFechaCAE == null) {
			btnFechaCAE = new JButton("");
			btnFechaCAE.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addModalIFrame(new WCalendarIFrame(txtFechaCAE));
				}
			});
			btnFechaCAE.setIcon(new ImageIcon(
					ComprobanteProveedorVerIFrame.class
							.getResource("/icons/calendar.png")));
			btnFechaCAE.setBounds(946, 14, 25, 25);
		}
		return btnFechaCAE;

	}

	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel("* Importe Neto Gravado: $");
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			label.setBounds(687, 8, 149, 25);
		}
		return label;
	}

	private JTextField getTextField_1() {
		if (txtCAE == null) {
			txtCAE = new JTextField();
			txtCAE.setHorizontalAlignment(SwingConstants.LEFT);
			txtCAE.setFont(WFrameUtils.getCustomFont(FontSize.LARGE, Font.BOLD));
			txtCAE.setBounds(445, 14, 223, 25);
		}
		return txtCAE;
	}

	private WTextFieldDecimal getTxtGravados() {
		if (txtGravados == null) {
			txtGravados = new WTextFieldDecimal(7, 2);
			txtGravados.setHorizontalAlignment(SwingConstants.RIGHT);
			txtGravados.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			txtGravados.setBounds(846, 10, 125, 25);
			txtGravados.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					// BigDecimal gravados = new BigDecimal(0);
					// BigDecimal total = new BigDecimal(0);
					// if (null != txtGravados.getText()
					// && txtGravados.getText().trim().length() > 0) {
					// gravados = new BigDecimal(txtGravados.getText());
					// }
					// if (null != txtTotalPesos.getText()
					// && txtTotalPesos.getText().trim().length() > 0) {
					// total = new BigDecimal(txtTotalPesos.getText());

					// BigDecimal importe = new
					// BigDecimal(txtGravados.getText());
					// BigDecimal iva = new BigDecimal(txtIVA.getText());
					//
					// txtTotalPesos.setText(String.valueOf()));
					calcularTotales();

				}
			});
		}

		return txtGravados;
	}

	private WTextFieldDecimal getTxtIVA() {
		txtIVA = new WTextFieldDecimal(7, 2);
		txtIVA.setHorizontalAlignment(SwingConstants.RIGHT);
		txtIVA.setFont(WFrameUtils.getCustomFont(FontSize.LARGE, Font.BOLD));
		txtIVA.setBounds(846, 51, 125, 25);
		txtIVA.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// BigDecimal gravados = new BigDecimal(0);
				// BigDecimal total = new BigDecimal(0);
				// if (null != txtGravados.getText()
				// && txtGravados.getText().trim().length() > 0) {
				// gravados = new BigDecimal(txtGravados.getText());
				// }
				// if (null != txtTotalPesos.getText()
				// && txtTotalPesos.getText().trim().length() > 0) {
				// total = new BigDecimal(txtTotalPesos.getText());

				// BigDecimal importe = new
				// BigDecimal(txtGravados.getText());
				// BigDecimal iva = new BigDecimal(txtIVA.getText());
				//
				// txtTotalPesos.setText(String.valueOf()));
				calcularTotales();

			}
		});
		return txtIVA;
	}

	private FocusListener getFocusListener() {
		return new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				super.focusLost(arg0);

			}
		};
	}
	private JTextArea getTextArea() {
		if (textArea == null) {
			textArea = new JTextArea();
			textArea.setBounds(482, 49, 184, 125);
		}
		return textArea;
	}
}
