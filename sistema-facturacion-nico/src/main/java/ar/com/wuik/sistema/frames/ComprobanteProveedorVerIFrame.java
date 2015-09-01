package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ar.com.wuik.sistema.bo.ClienteBO;
import ar.com.wuik.sistema.bo.ComprobanteBO;
import ar.com.wuik.sistema.bo.ProductoBO;
import ar.com.wuik.sistema.bo.ProveedorBO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.Comprobante;
import ar.com.wuik.sistema.entities.DetalleComprobante;
import ar.com.wuik.sistema.entities.DetalleRemito;
import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.sistema.entities.Proveedor;
import ar.com.wuik.sistema.entities.Remito;
import ar.com.wuik.sistema.entities.TributoComprobante;
import ar.com.wuik.sistema.entities.enums.EstadoFacturacion;
import ar.com.wuik.sistema.entities.enums.TipoComprobante;
import ar.com.wuik.sistema.entities.enums.TipoIVAEnum;
import ar.com.wuik.sistema.entities.enums.TipoLetraComprobante;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.ReportException;
import ar.com.wuik.sistema.filters.ProductoFilter;
import ar.com.wuik.sistema.model.DetalleComprobanteModel;
import ar.com.wuik.sistema.model.DetalleComprobantesAsocModel;
import ar.com.wuik.sistema.model.DetalleFacturaRemitoModel;
import ar.com.wuik.sistema.model.ProductoDetalleModel;
import ar.com.wuik.sistema.model.TributoComprobanteModel;
import ar.com.wuik.sistema.reportes.ComprobanteReporte;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.components.table.WTablePanel;
import ar.com.wuik.swing.components.table.WToolbarButton;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.frames.WCalendarIFrame;
import ar.com.wuik.swing.listeners.WTableListener;
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
	private ComprobanteIFrame comprobanteIFrame;
	private JButton btnFechaEmision;
	private JTextField txtFechaEmision;
	private JLabel lblFechaEmisin;
	private JLabel lblObservaciones;
	private JTextArea txaObservaciones;
	private JScrollPane scrollPane;
	private JLabel lblIVA10;
	private JTextField txtNumComp;
	private JTextField txtIVA10;
	private JLabel lblNunComp;
	private JLabel lblTotal;
	private JLabel txtTotalPesos;
	private JLabel lblIVA21;
	private JTextField txtIVA21;
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
	private JLabel lblPago;
	private JLabel lblFacturado;
	private JLabel lblEstado;
	private JPanel panel;
	private JLabel labelCAE;
	private JLabel labelFechaVencCAE;
	private JTextField txtFechaCAE;
	private JButton btnFechaCAE;
	private JLabel label;
	private JTextField txtCAE;
	private JTextField txtGravados;

	/**
	 * @wbp.parser.constructor
	 */
	public ComprobanteProveedorVerIFrame(ComprobanteIFrame comprobanteIFrame,
			Long idComprobante) {

		this.comprobanteIFrame = comprobanteIFrame;

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
			refreshDetalles();
			

			if (!comprobante.getTributos().isEmpty()) {
				getChckbxAgregarTributos().setSelected(Boolean.TRUE);
			}

			refreshTributos();
			
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
		
		popularEstadoFacturacion(comprobante);
		popularEstado(comprobante);
		

		populateComponents(model);
		loadProveedor(comprobante.getProveedor());
	}

	public ComprobanteProveedorVerIFrame(ComprobanteIFrame comprobanteIFrame,
			Long idProveedor, TipoComprobante tipoComprobante) {

		initialize();

		this.comprobanteIFrame = comprobanteIFrame;

		WModel model = populateModel();
		model.addValue(CAMPO_FECHA_EMISION,
				WUtils.getStringFromDate(new Date()));
		model.addValue(CAMPO_FECHA_CAE,
				WUtils.getStringFromDate(new Date()));
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

		
		popularEstadoFacturacion(comprobante);
		popularEstado(comprobante);
		
		

		try {
			ProveedorBO proveedorBO = AbstractFactory.getInstance(ProveedorBO.class);
			Proveedor proveedor = proveedorBO.obtener(idProveedor);
//			this.comprobante.setTipoLetraComprobante(proveedor.getCondicionIVA()
//					.getTipoLetraComprobante());
			loadProveedor(proveedor);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
		populateComponents(model);
	}

	public ComprobanteProveedorVerIFrame(Long idProveedor, TipoComprobante tipoComprobante) {

		initialize();

		WModel model = populateModel();
		model.addValue(CAMPO_FECHA_EMISION,
				WUtils.getStringFromDate(new Date()));
		model.addValue(CAMPO_FECHA_CAE,
				WUtils.getStringFromDate(new Date()));
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

		popularEstadoFacturacion(comprobante);
		popularEstado(comprobante);

		try {
			ProveedorBO proveedorBO = AbstractFactory.getInstance(ProveedorBO.class);
			Proveedor proveedor = proveedorBO.obtener(idProveedor);
			this.comprobante.setTipoLetraComprobante(proveedor.getCondicionIVA()
					.getTipoLetraComprobante());
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
		getContentPane().add(getLblPago());
		getContentPane().add(getLblFacturado());
		getContentPane().add(getLblEstado());
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
			btnGuardar.setIcon(new ImageIcon(ComprobanteProveedorVerIFrame.class
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
							if (null != comprobanteIFrame) {
								comprobanteIFrame.search();
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
			btnFechaEmision.setIcon(new ImageIcon(ComprobanteProveedorVerIFrame.class
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
			lblObservaciones.setIcon(new ImageIcon(ComprobanteProveedorVerIFrame.class
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

	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setBounds(482, 42, 187, 83);
			scrollPane.setViewportView(getTxaObservaciones());
		}
		return scrollPane;
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

	private JLabel getLblIVA10() {
		if (lblIVA10 == null) {
			lblIVA10 = new JLabel("IVA 10.5%: $");
			lblIVA10.setBounds(760, 85, 76, 25);
			lblIVA10.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblIVA10;
	}

	private JTextField getTxtNumComp() {
		if (txtNumComp == null) {
			txtNumComp = new JTextField();
			txtNumComp.setBounds(145, 14, 216, 25);
			txtNumComp.setHorizontalAlignment(SwingConstants.LEFT);
			txtNumComp.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return txtNumComp;
	}

	private JTextField getTxtIVA10() {
		if (txtIVA10 == null) {
			txtIVA10 = new JTextField();
			txtIVA10.setBounds(846, 85, 125, 25);
			txtIVA10.setHorizontalAlignment(SwingConstants.RIGHT);
			txtIVA10.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return txtIVA10;
	}

	private JLabel getLblNunComp() {
		if (lblNunComp == null) {
			lblNunComp = new JLabel("N\u00FAmero Comprobante:");
			lblNunComp.setBounds(10, 11, 125, 25);
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
			txtTotalPesos.setText("$ 0.00");
			txtTotalPesos.setHorizontalAlignment(SwingConstants.RIGHT);
			txtTotalPesos.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return txtTotalPesos;
	}



	private void calcularTotales() {

		BigDecimal subtotal = BigDecimal.ZERO;
		BigDecimal subtotalIVA21 = BigDecimal.ZERO;
		BigDecimal subtotalIVA105 = BigDecimal.ZERO;
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal totalTributo = BigDecimal.ZERO;

//		List<DetalleComprobante> detalles = comprobante.getDetalles();
//		for (DetalleComprobante detalleFactura : detalles) {
//			if (detalleFactura.getTipoIVA().equals(TipoIVAEnum.IVA_21)) {
//				subtotalIVA21 = subtotalIVA21.add(detalleFactura.getTotalIVA());
//			} else if (detalleFactura.getTipoIVA().equals(TipoIVAEnum.IVA_105)) {
//				subtotalIVA105 = subtotalIVA105.add(detalleFactura
//						.getTotalIVA());
//			}
//			subtotal = subtotal.add(detalleFactura.getSubtotal());
//			total = total.add(detalleFactura.getTotal());
//		}

		List<TributoComprobante> tributos = comprobante.getTributos();
		for (TributoComprobante tributoComprobante : tributos) {
			totalTributo = totalTributo.add(tributoComprobante.getImporte());
		}

		comprobante.setSubtotal(subtotal);
		comprobante.setTotalTributos(totalTributo);
		comprobante.setTotal(total);
		comprobante.setIva(total.subtract(subtotal));
		getTxtTotalPesos().setText(
				WUtils.getValue(comprobante.getTotal().add(totalTributo))
						.toEngineeringString());
		getLblImporteOtrosTributos().setText(
				WUtils.getValue(totalTributo).toEngineeringString());
	}

	private JLabel getLblIVA21() {
		if (lblIVA21 == null) {
			lblIVA21 = new JLabel("IVA 21%: $");
			lblIVA21.setBounds(760, 49, 76, 25);
			lblIVA21.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblIVA21;
	}

	private JTextField getTxtIVA21() {
		if (txtIVA21 == null) {
			txtIVA21 = new JTextField();
			txtIVA21.setBounds(846, 49, 125, 25);
			txtIVA21.setHorizontalAlignment(SwingConstants.RIGHT);
			txtIVA21.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return txtIVA21;
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
			panel_1.add(getTxtIVA10());
			panel_1.add(getLblIVA10());
			panel_1.add(getLblIVA21());
			panel_1.add(getTxtIVA21());
			panel_1.add(getLblObservaciones());
			panel_1.add(getScrollPane());
			panel_1.add(getTblTributos());
			panel_1.add(getLblImporteOtrosTributos());
			panel_1.add(getLblOtrosTributos());
			panel_1.add(getChckbxAgregarTributos());
			panel_1.add(getLabel_1());
			panel_1.add(getLabel());
			panel_1.add(getTxtGravados());
		}
		return panel_1;
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
			lblProveedor.setIcon(new ImageIcon(ComprobanteProveedorVerIFrame.class
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
//						addModalIFrame(new TributoComprobanteVerIFrame(
//								ComprobanteProveedorVerIFrame.this));
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
//							addModalIFrame(new TributoComprobanteVerIFrame(
//									tributo, ComprobanteProveedorVerIFrame.this));
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
			lblImporteOtrosTributos.setFont(WFrameUtils.getCustomFont(
					FontSize.LARGE, Font.BOLD));
			lblImporteOtrosTributos.setBounds(846, 121, 125, 25);
		}
		return lblImporteOtrosTributos;
	}

	private JLabel getLblOtrosTributos() {
		if (lblOtrosTributos == null) {
			lblOtrosTributos = new JLabel("Importe Otros Tributos: $");
			lblOtrosTributos.setHorizontalAlignment(SwingConstants.RIGHT);
			lblOtrosTributos.setBounds(696, 121, 140, 25);
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
	private JLabel getLblPago() {
		if (lblPago == null) {
			lblPago = new JLabel("Pago:");
			lblPago.setHorizontalTextPosition(SwingConstants.LEFT);
			lblPago.setHorizontalAlignment(SwingConstants.RIGHT);
			lblPago.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			lblPago.setBounds(333, 401, 89, 19);
		}
		return lblPago;
	}
	private JLabel getLblFacturado() {
		if (lblFacturado == null) {
			lblFacturado = new JLabel("Facturado:");
			lblFacturado.setHorizontalTextPosition(SwingConstants.LEFT);
			lblFacturado.setHorizontalAlignment(SwingConstants.RIGHT);
			lblFacturado.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			lblFacturado.setBounds(171, 401, 103, 19);
		}
		return lblFacturado;
	}
	private JLabel getLblEstado() {
		if (lblEstado == null) {
			lblEstado = new JLabel("Estado:");
			lblEstado.setHorizontalTextPosition(SwingConstants.LEFT);
			lblEstado.setHorizontalAlignment(SwingConstants.RIGHT);
			lblEstado.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			lblEstado.setBounds(20, 401, 76, 19);
		}
		return lblEstado;
	}
	
	private void popularEstado(Comprobante comprobante) {
		if (comprobante.isActivo()) {
			lblEstado.setIcon(new ImageIcon(ComprobanteVistaIFrame.class
					.getResource("/icons/activo.png")));
		} else {
			lblEstado.setIcon(new ImageIcon(ComprobanteVistaIFrame.class
					.getResource("/icons/inactivo.png")));
		}

	}

	private void popularEstadoFacturacion(Comprobante comprobante) {
		EstadoFacturacion estadoFacturacion = comprobante
				.getEstadoFacturacion();
		switch (estadoFacturacion) {
		case FACTURADO:
			lblFacturado.setIcon(new ImageIcon(ComprobanteVistaIFrame.class
					.getResource("/icons/facturado.png")));
			break;
		case FACTURADO_ERROR:
			lblFacturado.setIcon(new ImageIcon(ComprobanteVistaIFrame.class
					.getResource("/icons/facturado_error.png")));
			break;
		case SIN_FACTURAR:
			lblFacturado.setIcon(new ImageIcon(ComprobanteVistaIFrame.class
					.getResource("/icons/sin_facturar.png")));
			break;
		}
	}

	@Override
	protected JComponent getFocusComponent() {
		// TODO Auto-generated method stub
		return null;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
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
			btnFechaCAE.setIcon(new ImageIcon(ComprobanteProveedorVerIFrame.class
					.getResource("/icons/calendar.png")));
			btnFechaCAE.setBounds(946, 14, 25, 25);
		}
		return btnFechaCAE;
		
	}
	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel("Importe Neto Gravado: $");
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			label.setBounds(687, 8, 149, 25);
		}
		return label;
	}
	private JTextField getTextField_1() {
		if (txtCAE == null) {
			txtCAE = new JTextField();
			txtCAE.setHorizontalAlignment(SwingConstants.LEFT);
			txtCAE.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			txtCAE.setBounds(445, 14, 223, 25);
		}
		return txtCAE;
	}
	private JTextField getTxtGravados() {
		if (txtGravados == null) {
			txtGravados = new JTextField();
			txtGravados.setHorizontalAlignment(SwingConstants.RIGHT);
			txtGravados.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			txtGravados.setBounds(846, 10, 125, 25);
			txtGravados.getDocument().addDocumentListener(new DocumentListener() {
				@Override  
				public void changedUpdate(DocumentEvent e) {
				    warn();
				  }
				@Override
				  public void removeUpdate(DocumentEvent e) {
//				    warn();
				  }
				@Override
				  public void insertUpdate(DocumentEvent e) {
//				    warn();
				  }

				  public void warn() {
				    
				       JOptionPane.showMessageDialog(null,
				          "Error: Please enter number bigger than 0", "Error Massage",
				          JOptionPane.ERROR_MESSAGE);
				    
				  }
				});
		}
		return txtGravados;
	}
}
