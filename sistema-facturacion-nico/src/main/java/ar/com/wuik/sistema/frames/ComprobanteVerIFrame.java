package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import ar.com.wuik.sistema.bo.ClienteBO;
import ar.com.wuik.sistema.bo.ComprobanteBO;
import ar.com.wuik.sistema.bo.ProductoBO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.Comprobante;
import ar.com.wuik.sistema.entities.DetalleComprobante;
import ar.com.wuik.sistema.entities.DetalleRemito;
import ar.com.wuik.sistema.entities.Producto;
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

public class ComprobanteVerIFrame extends WAbstractModelIFrame {
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6838619883125511589L;
	private static final String CAMPO_FECHA_EMISION = "fechaEmision";
	private static final String CAMPO_OBSERVACIONES = "observaciones";
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
	private JLabel txtSubtotalPesos;
	private JLabel txtIVA10;
	private JLabel lblSubtotal;
	private JLabel lblTotal;
	private JLabel txtTotalPesos;
	private WTablePanel<DetalleComprobante> tblDetalle;
	private WTablePanel<Remito> tblRemitos;
	private WTablePanel<Producto> tblProducto;
	private JTextField textField;
	private JLabel lblIVA21;
	private JLabel txtIVA21;
	private JLabel lblBuscar;
	private JPanel panel;
	private JButton btnGuardarYFacturar;
	private JPanel panel_1;
	private WTablePanel<Comprobante> tablePanel;
	private JLabel lblCliente;
	private JLabel txtCliente;
	private JLabel txtCondIVA;
	private JLabel lblCondIva;
	private JLabel lblLetra;
	private JLabel lblTipoComp;
	private JButton btnNuevoProducto;
	private JLabel lblFacturado;
	private JLabel lblEstado;
	private Cliente cliente;

	/**
	 * @wbp.parser.constructor
	 */
	public ComprobanteVerIFrame(ComprobanteIFrame comprobanteIFrame,
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
			refreshDetalles();
			refreshRemitos();

			refreshComprobantesAsoc();
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
			getTablePanel().setVisible(Boolean.FALSE);
			break;
		case NOTA_CREDITO:
			titulo = "Editar Nota de Cr�dito";
			lblTipoComp.setText("NOTA DE CR�DITO");
			icon = new ImageIcon(
					ComprobanteIFrame.class
							.getResource("/icons/notas_credito.png"));
			break;
		case NOTA_DEBITO:
			titulo = "Editar Nota de D�bito";
			lblTipoComp.setText("NOTA DE D�BITO");
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
		loadCliente(comprobante.getCliente());
	}

	public ComprobanteVerIFrame(ComprobanteIFrame comprobanteIFrame,
			Long idCliente, TipoComprobante tipoComprobante) {

		initialize();

		this.comprobanteIFrame = comprobanteIFrame;

		WModel model = populateModel();
		model.addValue(CAMPO_FECHA_EMISION,
				WUtils.getStringFromDate(new Date()));
		this.comprobante = new Comprobante();
		this.comprobante.setTipoComprobante(tipoComprobante);
		this.comprobante.setIdCliente(idCliente);

		String titulo = "";
		Icon icon = null;

		switch (tipoComprobante) {
		case FACTURA:
			titulo = "Nueva Venta";
			icon = new ImageIcon(
					ComprobanteIFrame.class.getResource("/icons/facturas.png"));
			lblTipoComp.setText("FACTURA");
			getTablePanel().setVisible(Boolean.FALSE);
			break;
		case NOTA_CREDITO:
			titulo = "Nueva Nota de Cr�dito";
			icon = new ImageIcon(
					ComprobanteIFrame.class
							.getResource("/icons/notas_credito.png"));
			lblTipoComp.setText("NOTA DE CR�DITO");
			break;
		case NOTA_DEBITO:
			titulo = "Nueva Nota de D�bito";
			icon = new ImageIcon(
					ComprobanteIFrame.class
							.getResource("/icons/notas_debito.png"));
			lblTipoComp.setText("NOTA DE D�BITO");
			break;
		}

		setTitle(titulo);
		setFrameIcon(icon);

		popularEstadoFacturacion(comprobante);
		popularEstado(comprobante);

		getContentPane().add(getBtnGuardarYFacturar());

		try {
			ClienteBO clienteBO = AbstractFactory.getInstance(ClienteBO.class);
			Cliente cliente = clienteBO.obtener(idCliente);
			this.comprobante.setTipoLetraComprobante(cliente.getCondicionIVA()
					.getTipoLetraComprobante());
			loadCliente(cliente);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
		populateComponents(model);
	}

	public ComprobanteVerIFrame(Long idCliente, TipoComprobante tipoComprobante) {

		initialize();

		WModel model = populateModel();
		model.addValue(CAMPO_FECHA_EMISION,
				WUtils.getStringFromDate(new Date()));
		this.comprobante = new Comprobante();
		this.comprobante.setTipoComprobante(tipoComprobante);
		this.comprobante.setIdCliente(idCliente);

		String titulo = "";
		Icon icon = null;

		switch (tipoComprobante) {
		case FACTURA:
			titulo = "Nueva Venta";
			icon = new ImageIcon(
					ComprobanteIFrame.class.getResource("/icons/facturas.png"));
			lblTipoComp.setText("FACTURA");
			getTablePanel().setVisible(Boolean.FALSE);
			break;
		case NOTA_CREDITO:
			titulo = "Nueva Nota de Cr�dito";
			icon = new ImageIcon(
					ComprobanteIFrame.class
							.getResource("/icons/notas_credito.png"));
			lblTipoComp.setText("NOTA DE CR�DITO");
			break;
		case NOTA_DEBITO:
			titulo = "Nueva Nota de D�bito";
			icon = new ImageIcon(
					ComprobanteIFrame.class
							.getResource("/icons/notas_debito.png"));
			lblTipoComp.setText("NOTA DE D�BITO");
			break;
		}

		setTitle(titulo);
		setFrameIcon(icon);

		popularEstadoFacturacion(comprobante);
		popularEstado(comprobante);

		try {
			ClienteBO clienteBO = AbstractFactory.getInstance(ClienteBO.class);
			Cliente cliente = clienteBO.obtener(idCliente);
			this.comprobante.setTipoLetraComprobante(cliente.getCondicionIVA()
					.getTipoLetraComprobante());
			loadCliente(cliente);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
		populateComponents(model);
	}

	private void loadCliente(Cliente cliente) {
		this.cliente = cliente;
		getTxtCliente().setText(cliente.getRazonSocial());
		getTxtCondIVA().setText(cliente.getCondicionIVA().getDenominacion());
		if (comprobante.getTipoLetraComprobante()
				.equals(TipoLetraComprobante.A)) {
			lblLetra.setIcon(new ImageIcon(ComprobanteVerIFrame.class
					.getResource("/icons32/letra_a.png")));
		} else {
			lblLetra.setIcon(new ImageIcon(ComprobanteVerIFrame.class
					.getResource("/icons32/letra_b.png")));
		}
	}

	private void initialize() {
		setBorder(new LineBorder(null, 1, true));
		setBounds(0, 0, 1021, 722);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getBtnCerrar());
		getContentPane().add(getBtnGuardar());
		getContentPane().add(getBtnGuardarYFacturar());
		getContentPane().add(getLblFacturado());
		getContentPane().add(getLblEstado());
	}

	@Override
	protected boolean validateModel(WModel model, JComponent component) {

		String fechaEmision = model.getValue(CAMPO_FECHA_EMISION);

		List<String> messages = new ArrayList<String>();

		if (WUtils.isEmpty(fechaEmision)) {
			messages.add("Debe ingresar una Fecha de Emisi�n");
		}

		if (WUtils.isEmpty(comprobante.getDetalles())) {
			messages.add("Debe ingresar al menos un Detalle");
		} else {
			if (comprobante.getTotal().doubleValue() > 1000
					&& cliente.getDocumento().equals("0")) {
				messages.add("El monto total de la factura no debe superar los $1000. Cree un nuevo cliente con sus respectivos datos.");
			}
			if (comprobante.getDetalles().size() > 30) {
				messages.add("El total m�ximo de Detalles es de 30");
			}
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
			pnlBusqueda.setBounds(10, 11, 1001, 632);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getBtnFechaEmision());
			pnlBusqueda.add(getTxtFechaEmision());
			pnlBusqueda.add(getLblFechaEmisin());
			pnlBusqueda.add(getTblDetalle());
			pnlBusqueda.add(getTblRemitos());
			pnlBusqueda.add(getPanel());
			pnlBusqueda.add(getPanel_1());
			pnlBusqueda.add(getTablePanel());
			pnlBusqueda.add(getLblCliente());
			pnlBusqueda.add(getTxtCliente());
			pnlBusqueda.add(getTxtCondIVA());
			pnlBusqueda.add(getLblCondIva());
			pnlBusqueda.add(getLblLetra());
			pnlBusqueda.add(getLblTipoComp());
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
			btnCerrar.setIcon(new ImageIcon(ComprobanteVerIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(639, 654, 103, 30);
		}
		return btnCerrar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setIcon(new ImageIcon(ComprobanteVerIFrame.class
					.getResource("/icons/ok.png")));
			btnGuardar.setBounds(752, 654, 103, 30);
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

						try {
							ComprobanteBO comprobanteBO = AbstractFactory
									.getInstance(ComprobanteBO.class);
							if (comprobante.getId() == null) {
								comprobanteBO.guardar(comprobante);
							} else {
								comprobanteBO.actualizar(comprobante);
							}
							showGlobalMsg("El comprobante se guard� exitosamente");
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

	@Override
	protected JComponent getFocusComponent() {
		return getTextField();
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		WToolbarButton buttonAdd = new WToolbarButton("Agregar Remito",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/add.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						List<Long> idsRemitosToExclude = getIdsRemitos();
						addModalIFrame(new SeleccionarRemitoIFrame(
								ComprobanteVerIFrame.this, idsRemitosToExclude,
								comprobante.getIdCliente()));
					}
				}, "Agregar", null);
		WToolbarButton buttonQuitar = new WToolbarButton("Quitar Remito",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/delete.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tblRemitos.getSelectedItemID();
						if (null != selectedItem) {
							Remito remito = getRemitoById(selectedItem);
							comprobante.getRemitos().remove(remito);
							refreshRemitos();
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Remito",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Quitar", null);

		toolbarButtons.add(buttonAdd);
		toolbarButtons.add(buttonQuitar);
		return toolbarButtons;
	}

	private List<WToolbarButton> getToolbarButtonsComprobantes() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		WToolbarButton buttonAdd = new WToolbarButton("Agregar Comprobante",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/add.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						List<Long> idsComprobantesToExclude = getIdsComprobantes();
						addModalIFrame(new SeleccionarComprobanteIFrame(
								ComprobanteVerIFrame.this,
								idsComprobantesToExclude, comprobante
										.getIdCliente()));
					}
				}, "Agregar", null);
		WToolbarButton buttonQuitar = new WToolbarButton("Quitar Comprobante",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/delete.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							Comprobante comprobanteAsoc = getComprobanteById(selectedItem);
							comprobante.getComprobantesAsociados().remove(
									comprobanteAsoc);
							refreshComprobantesAsoc();
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Comprobante",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Quitar", null);

		toolbarButtons.add(buttonAdd);
		toolbarButtons.add(buttonQuitar);
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
		getTblRemitos().addData(comprobante.getRemitos());
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
			btnFechaEmision.setIcon(new ImageIcon(ComprobanteVerIFrame.class
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
			lblObservaciones.setIcon(new ImageIcon(ComprobanteVerIFrame.class
					.getResource("/icons/observaciones.png")));
			lblObservaciones.setBounds(10, 11, 116, 25);
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
			scrollPane.setBounds(10, 45, 187, 83);
			scrollPane.setViewportView(getTxaObservaciones());
		}
		return scrollPane;
	}

	private List<WToolbarButton> getToolbarButtonsDetalles() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();
		WToolbarButton buttonEliminar = new WToolbarButton("Eliminar Detalle",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/delete.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						List<Long> selectedItems = tblDetalle
								.getSelectedItemsID();
						if (WUtils.isNotEmpty(selectedItems)) {
							int result = JOptionPane.showConfirmDialog(
									getParent(),
									"�Desea eliminar los Items seleccionados?",
									"Alerta", JOptionPane.OK_CANCEL_OPTION,
									JOptionPane.WARNING_MESSAGE);
							if (result == JOptionPane.OK_OPTION) {
								for (Long selectedItem : selectedItems) {
									DetalleComprobante detalle = getDetalleById(selectedItem);
									comprobante.getDetalles().remove(detalle);
								}
								refreshDetalles();
							}
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar al menos un Item",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Eliminar", null);
		WToolbarButton buttonNuevoDetalle = new WToolbarButton("Nuevo Detalle",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/add.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						addModalIFrame(new DetalleVerIFrame(
								ComprobanteVerIFrame.this));
					}
				}, "Nuevo Detalle", null);

		toolbarButtons.add(buttonNuevoDetalle);
		toolbarButtons.add(buttonEliminar);
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
			lblIVA10.setBounds(751, 82, 76, 25);
			lblIVA10.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblIVA10;
	}

	private JLabel getTxtSubtotalPesos() {
		if (txtSubtotalPesos == null) {
			txtSubtotalPesos = new JLabel();
			txtSubtotalPesos.setBounds(837, 11, 125, 25);
			txtSubtotalPesos.setHorizontalAlignment(SwingConstants.RIGHT);
			txtSubtotalPesos.setText("$ 0.00");
			txtSubtotalPesos.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return txtSubtotalPesos;
	}

	private JLabel getTxtIVA10() {
		if (txtIVA10 == null) {
			txtIVA10 = new JLabel();
			txtIVA10.setBounds(837, 82, 125, 25);
			txtIVA10.setText("$ 0.00");
			txtIVA10.setHorizontalAlignment(SwingConstants.RIGHT);
			txtIVA10.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return txtIVA10;
	}

	private JLabel getLblSubtotal() {
		if (lblSubtotal == null) {
			lblSubtotal = new JLabel("Importe Neto Gravado: $");
			lblSubtotal.setBounds(678, 11, 149, 25);
			lblSubtotal.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblSubtotal;
	}

	private JLabel getLblTotal() {
		if (lblTotal == null) {
			lblTotal = new JLabel("Importe Total: $");
			lblTotal.setBounds(702, 118, 125, 25);
			lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblTotal;
	}

	private JLabel getTxtTotalPesos() {
		if (txtTotalPesos == null) {
			txtTotalPesos = new JLabel();
			txtTotalPesos.setBounds(837, 118, 125, 25);
			txtTotalPesos.setText("$ 0.00");
			txtTotalPesos.setHorizontalAlignment(SwingConstants.RIGHT);
			txtTotalPesos.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return txtTotalPesos;
	}

	private WTablePanel<DetalleComprobante> getTblDetalle() {
		if (tblDetalle == null) {
			tblDetalle = new WTablePanel(DetalleComprobanteModel.class,
					"Detalles");
			tblDetalle.addToolbarButtons(getToolbarButtonsDetalles());
			tblDetalle.setBounds(10, 289, 675, 160);
			tblDetalle.addWTableListener(new WTableListener() {

				@Override
				public void singleClickListener(Object[] selectedItem) {
				}

				@Override
				public void doubleClickListener(Object[] selectedItem) {
					Long selectedId = (Long) selectedItem[selectedItem.length - 1];

					DetalleComprobante detalle = getDetalleById(selectedId);
					addModalIFrame(new EditarDetalleComprobanteIFrame(detalle,
							ComprobanteVerIFrame.this));
				}
			});
		}
		return tblDetalle;
	}

	private WTablePanel<Remito> getTblRemitos() {
		if (tblRemitos == null) {
			tblRemitos = new WTablePanel(DetalleFacturaRemitoModel.class,
					"Remitos");
			tblRemitos.addToolbarButtons(getToolbarButtons());
			tblRemitos.setBounds(695, 289, 296, 160);
		}
		return tblRemitos;
	}

	private WTablePanel<Producto> getTblProducto() {
		if (tblProducto == null) {
			tblProducto = new WTablePanel(ProductoDetalleModel.class, "");
			tblProducto.setBounds(10, 47, 655, 125);
			tblProducto.addWTableListener(new WTableListener() {

				@Override
				public void singleClickListener(Object[] selectedItem) {
				}

				@Override
				public void doubleClickListener(Object[] selectedItem) {
					Long selectedId = (Long) selectedItem[selectedItem.length - 1];

					if (existeDetalleProducto(selectedId)) {
						addDetalle(selectedId, BigDecimal.ONE);
					} else {
						addModalIFrame(new DetalleComprobanteIFrame(selectedId,
								ComprobanteVerIFrame.this));
					}
				}
			});
		}
		return tblProducto;
	}

	protected boolean existeDetalleProducto(Long selectedId) {
		List<DetalleComprobante> detalles = comprobante.getDetalles();
		for (DetalleComprobante detalleFactura : detalles) {
			if (null != detalleFactura.getProducto()
					&& detalleFactura.getProducto().getId().equals(selectedId)) {
				return true;
			}
		}
		return false;
	}

	protected void addDetalle(Long selectedId, BigDecimal cantidad) {
		ProductoBO productoBO = AbstractFactory.getInstance(ProductoBO.class);
		try {
			Producto producto = productoBO.obtener(selectedId);
			List<DetalleComprobante> detalles = comprobante.getDetalles();
			boolean existeEnDetalle = false;
			for (DetalleComprobante detalleFactura : detalles) {
				if (null != detalleFactura.getProducto()
						&& detalleFactura.getProducto().getId()
								.equals(selectedId)) {
					detalleFactura.setCantidad(detalleFactura.getCantidad()
							.add(cantidad));
					existeEnDetalle = true;
				}
			}
			if (!existeEnDetalle) {
				DetalleComprobante detalle = new DetalleComprobante();
				detalle.setCantidad(cantidad);
				detalle.setComprobante(comprobante);
				detalle.setTipoIVA(producto.getTipoIVA());
				detalle.setPrecio(BigDecimal.ZERO);
				detalle.setProducto(producto);
				detalle.setTemporalId(System.currentTimeMillis());
				detalles.add(detalle);
			}
			getTblDetalle().addData(detalles);
			calcularTotales();
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	private JTextField getTextField() {
		if (textField == null) {
			textField = new JTextField();
			textField.setBounds(237, 11, 272, 25);
			textField.addKeyListener(new KeyAdapter() {

				@Override
				public void keyReleased(KeyEvent e) {
					search();
				}
			});
			textField.setDocument(new WTextFieldLimit(100));
			textField.setColumns(10);
		}
		return textField;
	}

	private List<Producto> productos = null;

	public void search() {
		String toSearch = textField.getText();
		if (WUtils.isNotEmpty(toSearch)) {
			ProductoBO productoBO = AbstractFactory
					.getInstance(ProductoBO.class);
			ProductoFilter filter = new ProductoFilter();
			filter.setDescripcionCodigo(toSearch);
			try {
				productos = productoBO.buscar(filter);
				getTblProducto().addData(productos);
			} catch (BusinessException e1) {
			}
		} else {
			getTblProducto().addData(productos);
		}
	}

	private void calcularTotales() {

		BigDecimal subtotal = BigDecimal.ZERO;
		BigDecimal subtotalIVA21 = BigDecimal.ZERO;
		BigDecimal subtotalIVA105 = BigDecimal.ZERO;
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal totalTributo = BigDecimal.ZERO;

		List<DetalleComprobante> detalles = comprobante.getDetalles();
		for (DetalleComprobante detalleFactura : detalles) {
			if (detalleFactura.getTipoIVA().equals(TipoIVAEnum.IVA_21)) {
				subtotalIVA21 = subtotalIVA21.add(detalleFactura.getTotalIVA());
			} else if (detalleFactura.getTipoIVA().equals(TipoIVAEnum.IVA_105)) {
				subtotalIVA105 = subtotalIVA105.add(detalleFactura
						.getTotalIVA());
			}
			subtotal = subtotal.add(detalleFactura.getSubtotal());
			total = total.add(detalleFactura.getTotal());
		}

		List<TributoComprobante> tributos = comprobante.getTributos();
		for (TributoComprobante tributoComprobante : tributos) {
			totalTributo = totalTributo.add(tributoComprobante.getImporte());
		}

		comprobante.setSubtotal(WUtils.getRoundedValue(subtotal));
		comprobante.setTotalTributos(WUtils.getRoundedValue(totalTributo));
		comprobante.setTotal(WUtils.getRoundedValue(total));
		comprobante.setIva(WUtils.getRoundedValue(total.subtract(subtotal)));

		getTxtSubtotalPesos().setText(
				WUtils.getValue(comprobante.getSubtotal())
						.toEngineeringString());
		getTxtIVA10().setText(
				WUtils.getValue(subtotalIVA105).toEngineeringString());
		getTxtIVA21().setText(
				WUtils.getValue(subtotalIVA21).toEngineeringString());
		getTxtTotalPesos().setText(
				WUtils.getValue(comprobante.getTotal().add(totalTributo))
						.toEngineeringString());
	}

	private JLabel getLblIVA21() {
		if (lblIVA21 == null) {
			lblIVA21 = new JLabel("IVA 21%: $");
			lblIVA21.setBounds(751, 46, 76, 25);
			lblIVA21.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblIVA21;
	}

	private JLabel getTxtIVA21() {
		if (txtIVA21 == null) {
			txtIVA21 = new JLabel();
			txtIVA21.setBounds(837, 46, 125, 25);
			txtIVA21.setText("$ 0.00");
			txtIVA21.setHorizontalAlignment(SwingConstants.RIGHT);
			txtIVA21.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return txtIVA21;
	}

	public void refreshDetalles() {
		getTblDetalle().addData(comprobante.getDetalles());
		calcularTotales();
	}

	public void addRemitos(List<Remito> remitos) {
		for (Remito remito : remitos) {
			if (!comprobante.getRemitos().contains(remito)) {
				List<DetalleRemito> detallesRemito = remito.getDetalles();
				for (DetalleRemito detalleRemito : detallesRemito) {
					addDetalle(detalleRemito.getProducto().getId(),
							detalleRemito.getCantidad());
				}
				remito.setComprobante(comprobante);
				comprobante.getRemitos().add(remito);
			}
		}
		refreshRemitos();
	}

	private JLabel getLblBuscar() {
		if (lblBuscar == null) {
			lblBuscar = new JLabel("C\u00F3digo/Nombre del Producto:");
			lblBuscar.setIcon(new ImageIcon(ComprobanteVerIFrame.class
					.getResource("/icons/producto.png")));
			lblBuscar.setBounds(10, 11, 217, 25);
			lblBuscar.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblBuscar;
	}

	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING,
					TitledBorder.TOP, null, null));
			panel.setBounds(10, 95, 675, 183);
			panel.setLayout(null);
			panel.add(getTblProducto());
			panel.add(getLblBuscar());
			panel.add(getTextField());
			panel.add(getBtnNuevoProducto());
		}
		return panel;
	}

	private JButton getBtnGuardarYFacturar() {
		if (btnGuardarYFacturar == null) {
			btnGuardarYFacturar = new JButton("Guardar y Facturar");
			btnGuardarYFacturar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					WModel model = populateModel();
					if (validateModel(model, btnGuardarYFacturar)) {
						String fechaVenta = model.getValue(CAMPO_FECHA_EMISION);
						String observaciones = model
								.getValue(CAMPO_OBSERVACIONES);

						comprobante.setFechaVenta(WUtils
								.getDateFromString(fechaVenta));
						comprobante.setObservaciones(observaciones);

						try {
							ComprobanteBO comprobanteBO = AbstractFactory
									.getInstance(ComprobanteBO.class);
							String observacionesAfip = comprobanteBO
									.guardarRegistrarAFIP(comprobante);
							if (WUtils.isNotEmpty(observacionesAfip)) {
								showGlobalMsg(observacionesAfip);
							}
							try {
								ComprobanteReporte.generarImpresion(comprobante
										.getId());
								showGlobalMsg("El comprobante se guard� y registr� en AFIP exitosamente");
							} catch (ReportException rexc) {
								showGlobalErrorMsg(rexc.getMessage());
							}

							if (null != comprobanteIFrame) {
								comprobanteIFrame.search();
							}
						} catch (BusinessException bexc) {
							showGlobalErrorMsg(bexc.getMessage());
						} finally {
							hideFrame();
						}
					}
				}
			});
			btnGuardarYFacturar.setIcon(new ImageIcon(
					ComprobanteVerIFrame.class.getResource("/icons/ok.png")));
			btnGuardarYFacturar.setBounds(863, 655, 148, 30);
		}
		return btnGuardarYFacturar;
	}

	public void addDetalle(DetalleComprobante detalle) {
		detalle.setTemporalId(System.currentTimeMillis());
		detalle.setComprobante(comprobante);
		List<DetalleComprobante> detalles = comprobante.getDetalles();
		detalles.add(detalle);
		getTblDetalle().addData(detalles);
		calcularTotales();
	}

	private JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			panel_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING,
					TitledBorder.TOP, null, null));
			panel_1.setBounds(10, 460, 981, 160);
			panel_1.setLayout(null);
			panel_1.add(getTxtTotalPesos());
			panel_1.add(getLblTotal());
			panel_1.add(getTxtIVA10());
			panel_1.add(getLblIVA10());
			panel_1.add(getLblIVA21());
			panel_1.add(getTxtIVA21());
			panel_1.add(getTxtSubtotalPesos());
			panel_1.add(getLblSubtotal());
			panel_1.add(getLblObservaciones());
			panel_1.add(getScrollPane());
		}
		return panel_1;
	}

	private WTablePanel<Comprobante> getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new WTablePanel(DetalleComprobantesAsocModel.class,
					"Comprobantes asociados");
			tablePanel.addToolbarButtons(getToolbarButtonsComprobantes());
			tablePanel.setBounds(695, 136, 296, 142);
		}
		return tablePanel;
	}

	public void addComprobantes(List<Comprobante> comprobantes) {
		comprobante.getComprobantesAsociados().addAll(comprobantes);
		refreshComprobantesAsoc();
	}

	private void refreshComprobantesAsoc() {
		getTablePanel().addData(comprobante.getComprobantesAsociados());
	}

	private JLabel getLblCliente() {
		if (lblCliente == null) {
			lblCliente = new JLabel("Cliente:");
			lblCliente.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCliente.setIcon(new ImageIcon(ComprobanteVerIFrame.class
					.getResource("/icons/cliente.png")));
			lblCliente.setBounds(10, 21, 69, 25);
		}
		return lblCliente;
	}

	private JLabel getTxtCliente() {
		if (txtCliente == null) {
			txtCliente = new JLabel();
			txtCliente.setBorder(null);
			txtCliente.setBounds(89, 21, 296, 25);
			txtCliente.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return txtCliente;
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

	private JButton getBtnNuevoProducto() {
		if (btnNuevoProducto == null) {
			btnNuevoProducto = new JButton("Nuevo Producto");
			btnNuevoProducto.setIcon(new ImageIcon(ComprobanteVerIFrame.class
					.getResource("/icons/add.png")));
			btnNuevoProducto.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addModalIFrame(new ProductoVerIFrame(
							ComprobanteVerIFrame.this, getTextField().getText()));
				}
			});
			btnNuevoProducto.setBounds(524, 11, 141, 25);
		}
		return btnNuevoProducto;
	}

	private JLabel getLblFacturado() {
		if (lblFacturado == null) {
			lblFacturado = new JLabel("Facturado:");
			lblFacturado.setHorizontalTextPosition(SwingConstants.LEFT);
			lblFacturado.setHorizontalAlignment(SwingConstants.RIGHT);
			lblFacturado.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			lblFacturado.setBounds(161, 654, 103, 19);
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
			lblEstado.setBounds(10, 654, 76, 19);
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
	public void enterPressed() {
		if (getTextField().hasFocus()) {
			if (WUtils.isEmpty(productos)) {
				addModalIFrame(new ProductoVerIFrame(ComprobanteVerIFrame.this,
						textField.getText()));
			} else {
				if (productos.size() == 1) {
					Long idProducto = productos.get(0).getId();
					addDetalleProducto(idProducto);
				}
			}
		}
	}

	protected void addDetalleProducto(Long selectedId) {
		if (existeDetalleProducto(selectedId)) {
			addDetalle(selectedId, BigDecimal.ONE);
		} else {
			addModalIFrame(new DetalleComprobanteIFrame(selectedId,
					ComprobanteVerIFrame.this));
		}
	}
}
