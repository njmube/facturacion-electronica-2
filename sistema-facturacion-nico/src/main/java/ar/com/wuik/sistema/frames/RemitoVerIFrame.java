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

import ar.com.wuik.sistema.bo.ParametroBO;
import ar.com.wuik.sistema.bo.ProductoBO;
import ar.com.wuik.sistema.bo.RemitoBO;
import ar.com.wuik.sistema.entities.DetalleRemito;
import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.sistema.entities.Remito;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.ReportException;
import ar.com.wuik.sistema.filters.ProductoFilter;
import ar.com.wuik.sistema.model.DetalleRemitoModel;
import ar.com.wuik.sistema.model.ProductoDetalleModel;
import ar.com.wuik.sistema.reportes.RemitoReporte;
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

public class RemitoVerIFrame extends WAbstractModelIFrame {
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6838619883125511589L;
	private static final String CAMPO_NRO_COMP = "nroComprobante";
	private static final String CAMPO_FECHA_EMISION = "fechaEmision";
	private static final String CAMPO_OBSERVACIONES = "observaciones";
	private static final String CAMPO_ESTADO = "estado";
	private JPanel pnlBusqueda;
	private JLabel lblNro;
	private JTextField txtNro;
	private JButton btnCerrar;
	private Remito remito;
	private JButton btnGuardar;
	private RemitoIFrame remitoIFrame;
	private JButton btnFechaEmision;
	private JTextField txtFechaEmision;
	private JLabel lblFechaEmisin;
	private JLabel lblObservaciones;
	private JTextArea txaObservaciones;
	private JScrollPane scrollPane;
	private JTextField txtCantidad;
	private Long idCliente;
	private JLabel lblCantidad;
	private WTablePanel<DetalleRemito> tblDetalle;
	private JLabel lblEstado;
	private JTextField txtEstado;
	private WTablePanel<Producto> tblProducto;
	private JTextField textField;
	private JLabel lblBuscar;

	/**
	 * @wbp.parser.constructor
	 */
	public RemitoVerIFrame(RemitoIFrame remitoIFrame, Long idCliente,
			Long idRemito) {

		initialize("Nuevo Remito");

		this.remitoIFrame = remitoIFrame;
		this.idCliente = idCliente;

		WModel model = populateModel();
		if (null == idRemito) {
			model.addValue(CAMPO_FECHA_EMISION,
					WUtils.getStringFromDate(new Date()));
			this.remito = new Remito();
			try {
				ParametroBO parametroBO = AbstractFactory
						.getInstance(ParametroBO.class);
				String nroRemito = parametroBO.getNroRemito();
				model.addValue(CAMPO_NRO_COMP, nroRemito);
			} catch (BusinessException bexc) {
				showGlobalErrorMsg(bexc.getMessage());
			}
		} else {
			initialize("Editar Remito");
			RemitoBO remitoBO = AbstractFactory.getInstance(RemitoBO.class);
			try {
				this.remito = remitoBO.obtener(idRemito);
				model.addValue(CAMPO_FECHA_EMISION,
						WUtils.getStringFromDate(remito.getFecha()));
				model.addValue(CAMPO_OBSERVACIONES, remito.getObservaciones());
				model.addValue(CAMPO_NRO_COMP, remito.getNumero());
				refreshDetalles();
			} catch (BusinessException bexc) {
				showGlobalErrorMsg(bexc.getMessage());
			}

		}
		populateComponents(model);
		getTxtEstado().setText("SIN FACTURAR");
	}

	public RemitoVerIFrame(Long idCliente) {

		initialize("Nuevo Remito");

		this.idCliente = idCliente;

		WModel model = populateModel();
		model.addValue(CAMPO_FECHA_EMISION,
				WUtils.getStringFromDate(new Date()));
		this.remito = new Remito();
		try {
			ParametroBO parametroBO = AbstractFactory
					.getInstance(ParametroBO.class);
			String nroRemito = parametroBO.getNroRemito();
			model.addValue(CAMPO_NRO_COMP, nroRemito);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
		populateComponents(model);
		getTxtEstado().setText("SIN FACTURAR");
	}

	private void initialize(String title) {
		setTitle(title);
		setBorder(new LineBorder(null, 1, true));
		setFrameIcon(new ImageIcon(
				RemitoVerIFrame.class.getResource("/icons/remitos.png")));
		setBounds(0, 0, 758, 619);
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

		if (WUtils.isEmpty(remito.getDetalles())) {
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
			pnlBusqueda.setBounds(10, 11, 734, 531);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getLblNro());
			pnlBusqueda.add(getTxtNro());
			pnlBusqueda.add(getBtnFechaEmision());
			pnlBusqueda.add(getTxtFechaEmision());
			pnlBusqueda.add(getLblFechaEmisin());
			pnlBusqueda.add(getLblObservaciones());
			pnlBusqueda.add(getScrollPane());
			pnlBusqueda.add(getTxtCantidad());
			pnlBusqueda.add(getLblCantidad());
			pnlBusqueda.add(getTblDetalle());
			pnlBusqueda.add(getLblEstado());
			pnlBusqueda.add(getTxtEstado());
			pnlBusqueda.add(getTblProducto());
			pnlBusqueda.add(getTextField());
			pnlBusqueda.add(getLblBuscar());
		}
		return pnlBusqueda;
	}

	private JLabel getLblNro() {
		if (lblNro == null) {
			lblNro = new JLabel("Nro.:");
			lblNro.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNro.setBounds(10, 23, 121, 25);
		}
		return lblNro;
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
			btnCerrar.setIcon(new ImageIcon(RemitoVerIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(528, 553, 103, 30);
		}
		return btnCerrar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setIcon(new ImageIcon(RemitoVerIFrame.class
					.getResource("/icons/ok.png")));
			btnGuardar.setBounds(641, 553, 103, 30);
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					WModel model = populateModel();
					if (validateModel(model)) {
						String fechaVenta = model.getValue(CAMPO_FECHA_EMISION);
						String observaciones = model
								.getValue(CAMPO_OBSERVACIONES);

						remito.setFecha(WUtils.getDateFromString(fechaVenta));
						remito.setObservaciones(observaciones);
						remito.setIdCliente(idCliente);

						try {
							RemitoBO remitoBO = AbstractFactory
									.getInstance(RemitoBO.class);
							if (remito.getId() == null) {
								remitoBO.guardar(remito);
								try {
									RemitoReporte.generarRemito(remito.getId());
								} catch (ReportException rexc) {
									showGlobalErrorMsg(rexc.getMessage());
								}
							} else {
								remitoBO.actualizar(remito);
							}
							hideFrame();
							if (null != remitoIFrame) {
								remitoIFrame.search();
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

	private JButton getBtnFechaEmision() {
		if (btnFechaEmision == null) {
			btnFechaEmision = new JButton("");
			btnFechaEmision.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addModalIFrame(new WCalendarIFrame(txtFechaEmision));
				}
			});
			btnFechaEmision.setIcon(new ImageIcon(RemitoVerIFrame.class
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
			lblObservaciones.setIcon(new ImageIcon(RemitoVerIFrame.class
					.getResource("/icons/observaciones.png")));
			lblObservaciones.setHorizontalAlignment(SwingConstants.LEFT);
			lblObservaciones.setBounds(10, 462, 121, 25);
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
			scrollPane.setBounds(115, 461, 192, 60);
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
						Long selectedItem = tblDetalle.getSelectedItemID();
						if (null != selectedItem) {
							int result = JOptionPane
									.showConfirmDialog(
											getParent(),
											"¿Desea eliminar los Detalles seleccionados?",
											"Alerta",
											JOptionPane.OK_CANCEL_OPTION,
											JOptionPane.WARNING_MESSAGE);
							if (result == JOptionPane.OK_OPTION) {
								DetalleRemito detalle = getDetalleById(selectedItem);
								remito.getDetalles().remove(detalle);
								refreshDetalles();
							}
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar al menos un Detalle",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Eliminar", null);

		toolbarButtons.add(buttonEliminar);
		return toolbarButtons;
	}

	protected DetalleRemito getDetalleById(Long selectedItem) {
		List<DetalleRemito> detalles = remito.getDetalles();
		for (DetalleRemito detalleFactura : detalles) {
			if (detalleFactura.getCoalesceId().equals(selectedItem)) {
				return detalleFactura;
			}
		}
		return null;
	}

	private JTextField getTxtCantidad() {
		if (txtCantidad == null) {
			txtCantidad = new JTextField();
			txtCantidad.setText("0");
			txtCantidad.setHorizontalAlignment(SwingConstants.RIGHT);
			txtCantidad.setEditable(false);
			txtCantidad.setBounds(594, 496, 125, 25);
			txtCantidad.setColumns(10);
			txtCantidad.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return txtCantidad;
	}

	private JLabel getLblCantidad() {
		if (lblCantidad == null) {
			lblCantidad = new JLabel("Cantidad:");
			lblCantidad.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCantidad.setBounds(508, 496, 76, 25);
		}
		return lblCantidad;
	}

	private WTablePanel<DetalleRemito> getTblDetalle() {
		if (tblDetalle == null) {
			tblDetalle = new WTablePanel(DetalleRemitoModel.class, "Detalles");
			tblDetalle.addToolbarButtons(getToolbarButtonsDetalles());
			tblDetalle.setBounds(10, 266, 714, 185);
			tblDetalle.addWTableListener(new WTableListener() {

				@Override
				public void singleClickListener(Object[] selectedItem) {
				}

				@Override
				public void doubleClickListener(Object[] selectedItem) {
					Long selectedId = (Long) selectedItem[selectedItem.length - 1];
					DetalleRemito detalle = getDetalleById(selectedId);
					addModalIFrame(new EditarDetalleRemitoIFrame(detalle,
							RemitoVerIFrame.this));
				}
			});
		}
		return tblDetalle;
	}

	private JLabel getLblEstado() {
		if (lblEstado == null) {
			lblEstado = new JLabel("Estado:");
			lblEstado.setHorizontalAlignment(SwingConstants.RIGHT);
			lblEstado.setBounds(508, 23, 76, 25);
		}
		return lblEstado;
	}

	private JTextField getTxtEstado() {
		if (txtEstado == null) {
			txtEstado = new JTextField();
			txtEstado.setName(CAMPO_ESTADO);
			txtEstado.setEditable(false);
			txtEstado.setBounds(595, 23, 121, 25);
		}
		return txtEstado;
	}

	private WTablePanel<Producto> getTblProducto() {
		if (tblProducto == null) {
			tblProducto = new WTablePanel(ProductoDetalleModel.class);
			tblProducto.addWTableListener(new WTableListener() {

				@Override
				public void singleClickListener(Object[] selectedItem) {
				}

				@Override
				public void doubleClickListener(Object[] selectedItem) {
					Long selectedId = (Long) selectedItem[selectedItem.length - 1];
					addDetalleProducto(selectedId);
				}
			});
			tblProducto.setBounds(10, 131, 714, 131);
		}
		return tblProducto;
	}

	protected void addDetalle(Long selectedId, BigDecimal cantidad) {
		ProductoBO productoBO = AbstractFactory.getInstance(ProductoBO.class);
		try {
			Producto producto = productoBO.obtener(selectedId);
			List<DetalleRemito> detalles = remito.getDetalles();
			boolean existeEnDetalle = false;
			for (DetalleRemito detalleRemito : detalles) {
				if (detalleRemito.getProducto().getId().equals(selectedId)) {
					detalleRemito.setCantidad(detalleRemito.getCantidad().add(cantidad));
					existeEnDetalle = true;
				}
			}
			if (!existeEnDetalle) {
				DetalleRemito detalle = new DetalleRemito();
				detalle.setCantidad(cantidad);
				detalle.setRemito(remito);
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

	protected void addDetalle(DetalleRemito detalle) {
		detalle.setRemito(remito);
		List<DetalleRemito> detalles = remito.getDetalles();
		detalles.add(detalle);
		getTblDetalle().addData(detalles);
		refreshDetalles();
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
			textField.setBounds(141, 95, 272, 25);
			textField.setColumns(10);
		}
		return textField;
	}

	protected void addDetalleProducto(Long selectedId) {
		if (existeDetalleProducto(selectedId)) {
			addDetalle(selectedId, BigDecimal.ONE);
		} else {
			addModalIFrame(new DetalleRemitoIFrame(selectedId,
					RemitoVerIFrame.this));
		}
	}

	protected boolean existeDetalleProducto(Long selectedId) {
		List<DetalleRemito> detalles = remito.getDetalles();
		for (DetalleRemito detalleRemito : detalles) {
			if (detalleRemito.getProducto().getId().equals(selectedId)) {
				return true;
			}
		}
		return false;
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
		double cantidad = 0;
		List<DetalleRemito> detalles = remito.getDetalles();
		for (DetalleRemito detalleRemito : detalles) {
			cantidad += detalleRemito.getCantidad().doubleValue();
		}
		getTxtCantidad().setText(cantidad + "");
	}

	public void refreshDetalles() {
		getTblDetalle().addData(remito.getDetalles());
		calcularTotales();
	}

	private JLabel getLblBuscar() {
		if (lblBuscar == null) {
			lblBuscar = new JLabel("B\u00FAsqueda:");
			lblBuscar.setHorizontalAlignment(SwingConstants.RIGHT);
			lblBuscar.setBounds(10, 95, 121, 25);
		}
		return lblBuscar;
	}

	@Override
	public void enterPressed() {

		if (getTextField().hasFocus()) {
			if (WUtils.isEmpty(productos)) {
				addModalIFrame(new ProductoVerIFrame(RemitoVerIFrame.this,
						textField.getText()));
			} else {
				if (productos.size() == 1) {
					Long idProducto = productos.get(0).getId();
					addDetalleProducto(idProducto);
				}
			}
		}
	}
}
