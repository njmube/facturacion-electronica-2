package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import ar.com.wuik.sistema.bo.FacturaBO;
import ar.com.wuik.sistema.bo.ProductoBO;
import ar.com.wuik.sistema.entities.DetalleFactura;
import ar.com.wuik.sistema.entities.DetalleRemito;
import ar.com.wuik.sistema.entities.Factura;
import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.sistema.entities.Remito;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.ReportException;
import ar.com.wuik.sistema.filters.ProductoFilter;
import ar.com.wuik.sistema.model.DetalleFacturaModel;
import ar.com.wuik.sistema.model.DetalleFacturaRemitoModel;
import ar.com.wuik.sistema.model.ProductoDetalleModel;
import ar.com.wuik.sistema.reportes.FacturaReporte;
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

public class FacturaVerIFrame extends WAbstractModelIFrame {
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6838619883125511589L;
	private static final String CAMPO_NRO_COMP = "nroComprobante";
	private static final String CAMPO_FECHA_EMISION = "fechaEmision";
	private static final String CAMPO_OBSERVACIONES = "observaciones";
	private static final String CAMPO_ESTADO = "estado";
	private JPanel pnlBusqueda;
	private JLabel lblCAE;
	private JTextField txtNro;
	private JButton btnCerrar;
	private Factura factura;
	private JButton btnGuardar;
	private FacturaIFrame facturaIFrame;
	private JButton btnFechaEmision;
	private JTextField txtFechaEmision;
	private JLabel lblFechaEmisin;
	private JLabel lblObservaciones;
	private JTextArea txaObservaciones;
	private JScrollPane scrollPane;
	private WTablePanel<Remito> tbpRemitos;
	private JLabel lblIVA10;
	private JTextField txtSubtotalPesos;
	private Long idCliente;
	private JTextField txtIVA10;
	private JLabel lblSubtotal;
	private JLabel lblTotal;
	private JTextField txtTotalPesos;
	private JLabel lblVtoCAE;
	private JTextField txtCAE;
	private WTablePanel<DetalleFactura> tblDetalle;
	private WTablePanel<Remito> tblRemitos;
	private JLabel lblEstado;
	private JTextField txtEstado;
	private WTablePanel<Producto> tblProducto;
	private JTextField textField;
	private JButton btnAgregar;
	private JLabel lblIVA21;
	private JTextField txtIVA21;
	private JCheckBox chckbxfacturar;

	/**
	 * @wbp.parser.constructor
	 */
	public FacturaVerIFrame(FacturaIFrame facturaIFrame,
			Long idCliente, Long idFactura) {

		initialize("Nueva Venta");

		this.facturaIFrame = facturaIFrame;
		this.idCliente = idCliente;

		WModel model = populateModel();
		if (null == idFactura) {
			model.addValue(CAMPO_FECHA_EMISION,
					WUtils.getStringFromDate(new Date()));
			this.factura = new Factura();
		} else {
			initialize("Editar Venta");
			FacturaBO facturaBO = AbstractFactory.getInstance(FacturaBO.class);
			try {
				this.factura = facturaBO.obtener(idFactura);
				model.addValue(CAMPO_FECHA_EMISION,
						WUtils.getStringFromDate(factura.getFechaVenta()));
				model.addValue(CAMPO_OBSERVACIONES, factura.getObservaciones());
				refreshDetalles();
				refreshRemitos();
			} catch (BusinessException bexc) {
				showGlobalErrorMsg(bexc.getMessage());
			}

		}
		populateComponents(model);
		getTxtEstado().setText("SIN FACTURAR");
		getContentPane().add(getChckbxfacturar());
	}

	private void initialize(String title) {
		setTitle(title);
		setBorder(new LineBorder(null, 1, true));
		setFrameIcon(new ImageIcon(
				FacturaVerIFrame.class.getResource("/icons/facturas.png")));
		setBounds(0, 0, 1006, 612);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getBtnCerrar());
		getContentPane().add(getBtnGuardar());
	}

	@Override
	protected boolean validateModel(WModel model) {

		String fechaEmision = model.getValue(CAMPO_FECHA_EMISION);

		List<String> messages = new ArrayList<String>();

		if (WUtils.isEmpty(fechaEmision)) {
			messages.add("Debe ingresar una Fecha de Emisión");
		}

		if (WUtils.isEmpty(factura.getDetalles())) {
			messages.add("Debe ingresar al menos un Detalle");
		}

		WTooltipUtils.showMessages(messages, btnGuardar, MessageType.ERROR);

		return WUtils.isEmpty(messages);
	}

	private JPanel getPnlBusqueda() {
		if (pnlBusqueda == null) {
			pnlBusqueda = new JPanel();
			pnlBusqueda.setBorder(new TitledBorder(UIManager
					.getBorder("TitledBorder.border"), "Datos",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlBusqueda.setBounds(10, 11, 984, 527);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getLblCAE());
			pnlBusqueda.add(getTxtNro());
			pnlBusqueda.add(getBtnFechaEmision());
			pnlBusqueda.add(getTxtFechaEmision());
			pnlBusqueda.add(getLblFechaEmisin());
			pnlBusqueda.add(getLblObservaciones());
			pnlBusqueda.add(getScrollPane());
			pnlBusqueda.add(getLblIVA10());
			pnlBusqueda.add(getTxtSubtotalPesos());
			pnlBusqueda.add(getTxtIVA10());
			pnlBusqueda.add(getLblSubtotal());
			pnlBusqueda.add(getLblTotal());
			pnlBusqueda.add(getTxtTotalPesos());
			pnlBusqueda.add(getLblVtoCAE());
			pnlBusqueda.add(getTxtCAE());
			pnlBusqueda.add(getTblDetalle());
			pnlBusqueda.add(getTblRemitos());
			pnlBusqueda.add(getLblEstado());
			pnlBusqueda.add(getTxtEstado());
			pnlBusqueda.add(getTblProducto());
			pnlBusqueda.add(getTextField());
			pnlBusqueda.add(getBtnAgregar());
			pnlBusqueda.add(getLblIVA21());
			pnlBusqueda.add(getTxtIVA21());
		}
		return pnlBusqueda;
	}

	private JLabel getLblCAE() {
		if (lblCAE == null) {
			lblCAE = new JLabel("CAE:");
			lblCAE.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCAE.setBounds(10, 23, 121, 25);
		}
		return lblCAE;
	}

	private JTextField getTxtNro() {
		if (txtNro == null) {
			txtNro = new JTextField();
			txtNro.setEditable(false);
			txtNro.setName(CAMPO_NRO_COMP);
			txtNro.setDocument(new WTextFieldLimit(50));
			txtNro.setBounds(141, 23, 141, 25);
		}
		return txtNro;
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
			btnCerrar.setIcon(new ImageIcon(FacturaVerIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(778, 549, 103, 25);
		}
		return btnCerrar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setIcon(new ImageIcon(FacturaVerIFrame.class
					.getResource("/icons/ok.png")));
			btnGuardar.setBounds(891, 549, 103, 25);
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					WModel model = populateModel();
					if (validateModel(model)) {
						String fechaVenta = model.getValue(CAMPO_FECHA_EMISION);
						String observaciones = model
								.getValue(CAMPO_OBSERVACIONES);

						factura.setFechaVenta(WUtils
								.getDateFromString(fechaVenta));
						factura.setObservaciones(observaciones);
						factura.setIdCliente(idCliente);

						try {
							FacturaBO facturaBO = AbstractFactory
									.getInstance(FacturaBO.class);
							if (factura.getId() == null) {
								if (getChckbxfacturar().isSelected()) {
									facturaBO.guardarRegistrarAFIP(factura);
									try {
										FacturaReporte.generarFactura(factura
												.getId());
									} catch (ReportException rexc) {
										showGlobalErrorMsg(rexc.getMessage());
									}
								} else {
									facturaBO.guardar(factura);
								}
							} else {
								facturaBO.actualizar(factura);
							}
							hideFrame();
							facturaIFrame.search();
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
		return getTxtNro();
	}

	@Override
	public void enterPressed() {
		getBtnGuardar().doClick();
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
								FacturaVerIFrame.this,
								idsRemitosToExclude, idCliente));
					}
				}, "Agregar", null);
		WToolbarButton buttonQuitar = new WToolbarButton("Quitar Remito",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/delete.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tbpRemitos.getSelectedItemID();
						if (null != selectedItem) {
							Remito remito = getRemitoById(selectedItem);
							factura.getRemitos().remove(remito);
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

	protected List<Long> getIdsRemitos() {
		List<Long> ids = new ArrayList<Long>();
		List<Remito> remitos = factura.getRemitos();
		for (Remito remito : remitos) {
			ids.add(remito.getId());
		}
		return ids;
	}

	protected void refreshRemitos() {
		getTblRemitos().addData(factura.getRemitos());
	}

	protected Remito getRemitoById(Long selectedItem) {
		List<Remito> remitos = factura.getRemitos();
		for (Remito remito : remitos) {
			if (remito.getId().equals(selectedItem)) {
				return remito;
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
			btnFechaEmision.setIcon(new ImageIcon(FacturaVerIFrame.class
					.getResource("/icons/calendar.png")));
			btnFechaEmision.setBounds(257, 59, 25, 25);
		}
		return btnFechaEmision;
	}

	private JTextField getTxtFechaEmision() {
		if (txtFechaEmision == null) {
			txtFechaEmision = new JTextField();
			txtFechaEmision.setName(CAMPO_FECHA_EMISION);
			txtFechaEmision.setEditable(false);
			txtFechaEmision.setBounds(141, 59, 106, 25);
		}
		return txtFechaEmision;
	}

	private JLabel getLblFechaEmisin() {
		if (lblFechaEmisin == null) {
			lblFechaEmisin = new JLabel("* Fecha Emisi\u00F3n:");
			lblFechaEmisin.setHorizontalAlignment(SwingConstants.RIGHT);
			lblFechaEmisin.setBounds(10, 59, 121, 25);
		}
		return lblFechaEmisin;
	}

	private JLabel getLblObservaciones() {
		if (lblObservaciones == null) {
			lblObservaciones = new JLabel("Observaciones:");
			lblObservaciones.setHorizontalAlignment(SwingConstants.RIGHT);
			lblObservaciones.setBounds(806, 33, 93, 25);
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
			scrollPane.setBounds(725, 59, 192, 60);
			scrollPane.setViewportView(getTxaObservaciones());
		}
		return scrollPane;
	}

	private List<WToolbarButton> getToolbarButtonsDetalles() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();
		WToolbarButton buttonEdit = new WToolbarButton("Editar Detalle",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/edit.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tblDetalle.getSelectedItemID();
						if (null != selectedItem) {
							DetalleFactura detalle = getDetalleById(selectedItem);
							Integer cantidadRemitida = null;
							addModalIFrame(new EditarDetalleFacturaIFrame(
									detalle, cantidadRemitida,
									FacturaVerIFrame.this));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Item",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Editar", null);
		WToolbarButton buttonEliminar = new WToolbarButton("Eliminar Detalle",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/delete.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tblDetalle.getSelectedItemID();
						if (null != selectedItem) {
							int result = JOptionPane.showConfirmDialog(
									getParent(),
									"¿Desea eliminar el Item seleccionado?",
									"Alerta", JOptionPane.OK_CANCEL_OPTION,
									JOptionPane.WARNING_MESSAGE);
							if (result == JOptionPane.OK_OPTION) {
								DetalleFactura detalle = getDetalleById(selectedItem);
								factura.getDetalles().remove(detalle);
								refreshDetalles();
							}
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Item",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Eliminar", null);

		toolbarButtons.add(buttonEdit);
		toolbarButtons.add(buttonEliminar);
		return toolbarButtons;
	}

	protected DetalleFactura getDetalleById(Long selectedItem) {
		List<DetalleFactura> detalles = factura.getDetalles();
		for (DetalleFactura detalleFactura : detalles) {
			if (detalleFactura.getCoalesceId().equals(selectedItem)) {
				return detalleFactura;
			}
		}
		return null;
	}

	private JLabel getLblIVA10() {
		if (lblIVA10 == null) {
			lblIVA10 = new JLabel("IVA 10.5%: $");
			lblIVA10.setHorizontalAlignment(SwingConstants.RIGHT);
			lblIVA10.setBounds(763, 454, 76, 25);
		}
		return lblIVA10;
	}

	private JTextField getTxtSubtotalPesos() {
		if (txtSubtotalPesos == null) {
			txtSubtotalPesos = new JTextField();
			txtSubtotalPesos.setHorizontalAlignment(SwingConstants.RIGHT);
			txtSubtotalPesos.setEditable(false);
			txtSubtotalPesos.setText("$ 0.00");
			txtSubtotalPesos.setBounds(849, 383, 125, 25);
			txtSubtotalPesos.setColumns(10);
			txtSubtotalPesos.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return txtSubtotalPesos;
	}

	private JTextField getTxtIVA10() {
		if (txtIVA10 == null) {
			txtIVA10 = new JTextField();
			txtIVA10.setText("$ 0.00");
			txtIVA10.setHorizontalAlignment(SwingConstants.RIGHT);
			txtIVA10.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			txtIVA10.setEditable(false);
			txtIVA10.setColumns(10);
			txtIVA10.setBounds(849, 454, 125, 25);
		}
		return txtIVA10;
	}

	private JLabel getLblSubtotal() {
		if (lblSubtotal == null) {
			lblSubtotal = new JLabel("Subtotal: $");
			lblSubtotal.setHorizontalAlignment(SwingConstants.RIGHT);
			lblSubtotal.setBounds(763, 383, 76, 25);
		}
		return lblSubtotal;
	}

	private JLabel getLblTotal() {
		if (lblTotal == null) {
			lblTotal = new JLabel("Total: $");
			lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
			lblTotal.setBounds(763, 490, 76, 25);
		}
		return lblTotal;
	}

	private JTextField getTxtTotalPesos() {
		if (txtTotalPesos == null) {
			txtTotalPesos = new JTextField();
			txtTotalPesos.setText("$ 0.00");
			txtTotalPesos.setHorizontalAlignment(SwingConstants.RIGHT);
			txtTotalPesos.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			txtTotalPesos.setEditable(false);
			txtTotalPesos.setColumns(10);
			txtTotalPesos.setBounds(849, 490, 125, 25);
		}
		return txtTotalPesos;
	}

	private JLabel getLblVtoCAE() {
		if (lblVtoCAE == null) {
			lblVtoCAE = new JLabel("Vto. CAE:");
			lblVtoCAE.setHorizontalAlignment(SwingConstants.RIGHT);
			lblVtoCAE.setBounds(295, 23, 76, 25);
		}
		return lblVtoCAE;
	}

	private JTextField getTxtCAE() {
		if (txtCAE == null) {
			txtCAE = new JTextField();
			txtCAE.setEditable(false);
			txtCAE.setName("fechaVto");
			txtCAE.setBounds(381, 23, 106, 25);
		}
		return txtCAE;
	}

	private WTablePanel<DetalleFactura> getTblDetalle() {
		if (tblDetalle == null) {
			tblDetalle = new WTablePanel(DetalleFacturaModel.class, "Detalles");
			tblDetalle.addToolbarButtons(getToolbarButtonsDetalles());
			tblDetalle.setBounds(10, 288, 714, 227);
		}
		return tblDetalle;
	}

	private WTablePanel<Remito> getTblRemitos() {
		if (tblRemitos == null) {
			tblRemitos = new WTablePanel(DetalleFacturaRemitoModel.class, "Remitos");
			tblRemitos.addToolbarButtons(getToolbarButtons());
			tblRemitos.setBounds(734, 146, 240, 210);
		}
		return tblRemitos;
	}

	private JLabel getLblEstado() {
		if (lblEstado == null) {
			lblEstado = new JLabel("Estado:");
			lblEstado.setHorizontalAlignment(SwingConstants.RIGHT);
			lblEstado.setBounds(550, 23, 76, 25);
		}
		return lblEstado;
	}

	private JTextField getTxtEstado() {
		if (txtEstado == null) {
			txtEstado = new JTextField();
			txtEstado.setName(CAMPO_ESTADO);
			txtEstado.setEditable(false);
			txtEstado.setBounds(637, 23, 121, 25);
		}
		return txtEstado;
	}

	private WTablePanel<Producto> getTblProducto() {
		if (tblProducto == null) {
			tblProducto = new WTablePanel(ProductoDetalleModel.class, "Búsqueda de Productos");
			tblProducto.addWTableListener(new WTableListener() {

				@Override
				public void singleClickListener(Object[] selectedItem) {
				}

				@Override
				public void doubleClickListener(Object[] selectedItem) {
					Long selectedId = (Long) selectedItem[selectedItem.length - 1];
					addDetalle(selectedId);
				}
			});
			tblProducto.setBounds(10, 146, 714, 131);
		}
		return tblProducto;
	}

	protected void addDetalle(Long selectedId) {
		ProductoBO productoBO = AbstractFactory.getInstance(ProductoBO.class);
		try {
			Producto producto = productoBO.obtener(selectedId);
			List<DetalleFactura> detalles = factura.getDetalles();
			boolean existeEnDetalle = false;
			for (DetalleFactura detalleFactura : detalles) {
				if (detalleFactura.getProducto().getId().equals(selectedId)) {
					detalleFactura
							.setCantidad(detalleFactura.getCantidad() + 1);
					existeEnDetalle = true;
				}
			}
			if (!existeEnDetalle) {
				DetalleFactura detalle = new DetalleFactura();
				detalle.setCantidad(1);
				detalle.setFactura(factura);
				detalle.setIva(producto.getIva());
				detalle.setPrecio(producto.getPrecio());
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
			textField.addKeyListener(new KeyAdapter() {

				@Override
				public void keyReleased(KeyEvent e) {
					search();
				}
			});
			textField.setDocument(new WTextFieldLimit(100));
			textField.setBounds(10, 110, 272, 25);
			textField.setColumns(10);
		}
		return textField;
	}

	public void search() {
		String toSearch = textField.getText();
		if (WUtils.isNotEmpty(toSearch)) {
			ProductoBO productoBO = AbstractFactory
					.getInstance(ProductoBO.class);
			ProductoFilter filter = new ProductoFilter();
			filter.setDescripcionCodigo(toSearch);
			try {
				List<Producto> productos = productoBO.buscar(filter);
				getTblProducto().addData(productos);
			} catch (BusinessException e1) {
			}
		} else {
			getTblProducto().addData(new ArrayList<Producto>());
		}
	}

	private JButton getBtnAgregar() {
		if (btnAgregar == null) {
			btnAgregar = new JButton("");
			btnAgregar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addModalIFrame(new ProductoVerIFrame(
							FacturaVerIFrame.this));
				}
			});
			btnAgregar.setIcon(new ImageIcon(FacturaVerIFrame.class
					.getResource("/icons/add.png")));
			btnAgregar.setBounds(289, 110, 31, 25);
		}
		return btnAgregar;
	}

	private void calcularTotales() {

		BigDecimal subtotal = BigDecimal.ZERO;
		BigDecimal subtotalIVA21 = BigDecimal.ZERO;
		BigDecimal subtotalIVA105 = BigDecimal.ZERO;
		BigDecimal total = BigDecimal.ZERO;

		List<DetalleFactura> detalles = factura.getDetalles();
		for (DetalleFactura detalleFactura : detalles) {
			if (detalleFactura.getIva().doubleValue() == 21.00) {
				subtotalIVA21 = subtotalIVA21.add(detalleFactura.getTotalIVA());
			} else if (detalleFactura.getIva().doubleValue() == 10.50) {
				subtotalIVA105 = subtotalIVA105.add(detalleFactura
						.getTotalIVA());
			}
			subtotal = subtotal.add(detalleFactura.getSubtotal());
			total = total.add(detalleFactura.getTotal());
		}

		factura.setSubtotal(subtotal);
		factura.setTotal(total);
		factura.setIva(total.subtract(subtotal));

		getTxtSubtotalPesos().setText(
				WUtils.getValue(factura.getSubtotal()).toEngineeringString());
		getTxtIVA10().setText(
				WUtils.getValue(subtotalIVA105).toEngineeringString());
		getTxtIVA21().setText(
				WUtils.getValue(subtotalIVA21).toEngineeringString());
		getTxtTotalPesos().setText(
				WUtils.getValue(factura.getTotal()).toEngineeringString());
	}

	private JLabel getLblIVA21() {
		if (lblIVA21 == null) {
			lblIVA21 = new JLabel("IVA 21%: $");
			lblIVA21.setHorizontalAlignment(SwingConstants.RIGHT);
			lblIVA21.setBounds(763, 418, 76, 25);
		}
		return lblIVA21;
	}

	private JTextField getTxtIVA21() {
		if (txtIVA21 == null) {
			txtIVA21 = new JTextField();
			txtIVA21.setText("$ 0.00");
			txtIVA21.setHorizontalAlignment(SwingConstants.RIGHT);
			txtIVA21.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			txtIVA21.setEditable(false);
			txtIVA21.setColumns(10);
			txtIVA21.setBounds(849, 418, 125, 25);
		}
		return txtIVA21;
	}

	public void refreshDetalles() {
		getTblDetalle().addData(factura.getDetalles());
		calcularTotales();
	}

	private JCheckBox getChckbxfacturar() {
		if (chckbxfacturar == null) {
			chckbxfacturar = new JCheckBox("\u00BFFacturar?");
			chckbxfacturar.setSelected(true);
			chckbxfacturar.setBounds(666, 550, 106, 23);
		}
		return chckbxfacturar;
	}

	public void addRemitos(List<Remito> remitos) {
		for (Remito remito : remitos) {
			if (!factura.getRemitos().contains(remito)) {
				List<DetalleRemito> detallesRemito = remito.getDetalles();
				for (DetalleRemito detalleRemito : detallesRemito) {
					for (int i = 0; i < detalleRemito.getCantidad(); i++) {
						addDetalle(detalleRemito.getProducto().getId());
					}
				}
				remito.setFactura(factura);
				factura.getRemitos().add(remito);
			}
		}
		refreshRemitos();
	}
}
