//package ar.com.wuik.sistema.frames;
//
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.Date;
//import java.util.List;
//
//import javax.swing.ImageIcon;
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JComponent;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JTextField;
//import javax.swing.SwingConstants;
//import javax.swing.border.LineBorder;
//import javax.swing.border.TitledBorder;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import ar.com.wuik.sistema.bo.RemitoBO;
//import ar.com.wuik.sistema.entities.Remito;
//import ar.com.wuik.sistema.exceptions.BusinessException;
//import ar.com.wuik.sistema.utils.AbstractFactory;
//import ar.com.wuik.swing.components.WModel;
//import ar.com.wuik.swing.components.WOption;
//import ar.com.wuik.swing.components.security.WSecure;
//import ar.com.wuik.swing.components.table.WTablePanel;
//import ar.com.wuik.swing.frames.WAbstractModelIFrame;
//import ar.com.wuik.swing.frames.WCalendarIFrame;
//import ar.com.wuik.swing.utils.WUtils;
//
//public class MovimientosProductoIFrame extends WAbstractModelIFrame implements
//		WSecure {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = -3613686692179857008L;
//	private static final Logger LOGGER = LoggerFactory
//			.getLogger(MovimientosProductoIFrame.class);
//	private static final String CAMPO_FDESDE = "fDesde";
//	private static final String CAMPO_FHASTA = "fHasta";
//	private static final String CAMPO_TMOV = "tipoMov";
//	private WTablePanel<Remito> tablePanel;
//	private JPanel pnlBusqueda;
//	private JLabel lblFechaDesde;
//	private JTextField txtFechaDesde;
//	private JButton btnCalendarFechaDesde;
//	private JComboBox cmbTipoMovimiento;
//	private JLabel lblFechaHasta;
//	private JTextField txtFechaHasta;
//	private JButton btnCalendarFechaHasta;
//	private JLabel lblTipoMovimiento;
//	private JButton btnLimpiar;
//	private JButton btnBuscar;
//	private JButton btnSalir;
//	private Long idProducto;
//
//	public MovimientosProductoIFrame(Long idProducto) {
//		this.idProducto = idProducto;
//		setBorder(new LineBorder(null, 1, true));
//		setTitle("Movimientos Productos");
//		setFrameIcon(new ImageIcon(
//				ClienteIFrame.class.getResource("/icons/movimiento.png")));
//		setBounds(0, 0, 895, 376);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		getContentPane().setLayout(null);
//		getContentPane().add(getTablePanel());
//		getContentPane().add(getPnlBusqueda());
//		getContentPane().add(getBtnSalir());
//		
//		Date hoy = new Date();
//		getTxtFechaDesde().setText(
//				WUtils.getStringFromDate(WUtils.getMinDate(hoy)));
//		getTxtFechaHasta().setText(
//				WUtils.getStringFromDate(WUtils.getMaxDate(hoy)));
//		loadTiposMovimientos();
//		search();
//	}
//
//	private void loadTiposMovimientos() {
//
//		getCmbTipoMovimiento().addItem(
//				new WOption(WOption.getIdWOptionSeleccion(), "TODOS"));
//
//		TipoMovimiento[] tiposMovimientos = TipoMovimiento.values();
//		for (TipoMovimiento tipoMovimiento : tiposMovimientos) {
//			getCmbTipoMovimiento().addItem(
//					new WOption((long) tipoMovimiento.getId(), tipoMovimiento
//							.toString()));
//		}
//	}
//
//	private void search() {
//
//		WModel model = populateModel();
//		String desde = model.getValue(CAMPO_FDESDE);
//		String hasta = model.getValue(CAMPO_FHASTA);
//		WOption idTMov = model.getValue(CAMPO_TMOV);
//
//		TipoMovimiento tipoMovimiento = null;
//		if (!idTMov.getValue().equals(WOption.getIdWOptionSeleccion())) {
//			tipoMovimiento = TipoMovimiento.fromValue(idTMov.getValue()
//					.intValue());
//		} else {
//			tipoMovimiento = null;
//		}
//
//		try {
//			RemitoBO remitoBO = AbstractFactory.getInstance(RemitoBO.class);
//			RemitoFilter filter = new RemitoFilter();
//			filter.setIdProducto(idProducto);
//			filter.setTipoMovimiento(tipoMovimiento);
//			filter.setFechaDesde(WUtils.getDateFromString(desde));
//			filter.setFechaHasta(WUtils.getDateFromString(hasta));
//			List<Remito> remitos = remitoBO.search(filter);
//			getTablePanel().addData(remitos);
//		} catch (BusinessException bexc) {
//			LOGGER.error("Error al obtener Remitos de Producto", bexc);
//		}
//	}
//
//	@Override
//	public void applySecurity(List<String> permisos) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	protected boolean validateModel(WModel model) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	protected JComponent getFocusComponent() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	private WTablePanel<Remito> getTablePanel() {
//		if (tablePanel == null) {
//			tablePanel = new WTablePanel(MovimientoProductoModel.class,
//					Boolean.FALSE);
//			tablePanel.setBounds(0, 112, 883, 195);
//		}
//		return tablePanel;
//	}
//
//	private JPanel getPnlBusqueda() {
//		if (pnlBusqueda == null) {
//			pnlBusqueda = new JPanel();
//			pnlBusqueda.setBorder(new TitledBorder(null, "Busqueda",
//					TitledBorder.LEADING, TitledBorder.TOP, null, null));
//			pnlBusqueda.setBounds(0, 0, 883, 101);
//			pnlBusqueda.setLayout(null);
//			pnlBusqueda.add(getLblFechaDesde());
//			pnlBusqueda.add(getTxtFechaDesde());
//			pnlBusqueda.add(getBtnCalendarFechaDesde());
//			pnlBusqueda.add(getCmbTipoMovimiento());
//			pnlBusqueda.add(getLblFechaHasta());
//			pnlBusqueda.add(getTxtFechaHasta());
//			pnlBusqueda.add(getBtnCalendarFechaHasta());
//			pnlBusqueda.add(getLblTipoMovimiento());
//			pnlBusqueda.add(getBtnLimpiar());
//			pnlBusqueda.add(getBtnBuscar());
//		}
//		return pnlBusqueda;
//	}
//
//	private JLabel getLblFechaDesde() {
//		if (lblFechaDesde == null) {
//			lblFechaDesde = new JLabel("Fecha Desde:");
//			lblFechaDesde.setHorizontalAlignment(SwingConstants.RIGHT);
//			lblFechaDesde.setBounds(10, 32, 90, 14);
//		}
//		return lblFechaDesde;
//	}
//
//	private JTextField getTxtFechaDesde() {
//		if (txtFechaDesde == null) {
//			txtFechaDesde = new JTextField();
//			txtFechaDesde.setEditable(false);
//			txtFechaDesde.setColumns(10);
//			txtFechaDesde.setBounds(110, 29, 86, 25);
//			txtFechaDesde.setName(CAMPO_FDESDE);
//		}
//		return txtFechaDesde;
//	}
//
//	private JButton getBtnCalendarFechaDesde() {
//		if (btnCalendarFechaDesde == null) {
//			btnCalendarFechaDesde = new JButton("");
//			btnCalendarFechaDesde.setIcon(new ImageIcon(
//					MovimientosProductoIFrame.class
//							.getResource("/icons/calendar.png")));
//			btnCalendarFechaDesde.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					addModalIFrame(new WCalendarIFrame(getTxtFechaDesde()));
//				}
//			});
//			btnCalendarFechaDesde.setBounds(205, 29, 25, 25);
//		}
//		return btnCalendarFechaDesde;
//	}
//
//	private JButton getBtnCalendarFechaHasta() {
//		if (btnCalendarFechaHasta == null) {
//			btnCalendarFechaHasta = new JButton("");
//			btnCalendarFechaHasta.setIcon(new ImageIcon(
//					MovimientosProductoIFrame.class
//							.getResource("/icons/calendar.png")));
//			btnCalendarFechaHasta.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					addModalIFrame(new WCalendarIFrame(getTxtFechaHasta()));
//				}
//			});
//			btnCalendarFechaHasta.setBounds(453, 29, 25, 25);
//		}
//		return btnCalendarFechaHasta;
//	}
//
//	private JComboBox getCmbTipoMovimiento() {
//		if (cmbTipoMovimiento == null) {
//			cmbTipoMovimiento = new JComboBox();
//			cmbTipoMovimiento.setBounds(627, 29, 120, 25);
//			cmbTipoMovimiento.setName(CAMPO_TMOV);
//		}
//		return cmbTipoMovimiento;
//	}
//
//	private JLabel getLblFechaHasta() {
//		if (lblFechaHasta == null) {
//			lblFechaHasta = new JLabel("Fecha Hasta:");
//			lblFechaHasta.setBounds(279, 34, 73, 14);
//		}
//		return lblFechaHasta;
//	}
//
//	private JTextField getTxtFechaHasta() {
//		if (txtFechaHasta == null) {
//			txtFechaHasta = new JTextField();
//			txtFechaHasta.setEditable(false);
//			txtFechaHasta.setColumns(10);
//			txtFechaHasta.setBounds(357, 29, 86, 25);
//			txtFechaHasta.setName(CAMPO_FHASTA);
//		}
//		return txtFechaHasta;
//	}
//
//	private JLabel getLblTipoMovimiento() {
//		if (lblTipoMovimiento == null) {
//			lblTipoMovimiento = new JLabel("Tipo:");
//			lblTipoMovimiento.setHorizontalAlignment(SwingConstants.RIGHT);
//			lblTipoMovimiento.setBounds(573, 33, 46, 14);
//		}
//		return lblTipoMovimiento;
//	}
//
//	private JButton getBtnLimpiar() {
//		if (btnLimpiar == null) {
//			btnLimpiar = new JButton("Limpiar");
//			btnLimpiar.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					reset();
//				}
//			});
//			btnLimpiar.setIcon(new ImageIcon(MovimientosProductoIFrame.class
//					.getResource("/icons/clear.png")));
//			btnLimpiar.setBounds(346, 65, 103, 25);
//		}
//		return btnLimpiar;
//	}
//
//	protected void reset() {
//		getCmbTipoMovimiento().setSelectedIndex(0);
//		Date hoy = new Date();
//		getTxtFechaDesde().setText(
//				WUtils.getStringFromDate(WUtils.getMinDate(hoy)));
//		getTxtFechaHasta().setText(
//				WUtils.getStringFromDate(WUtils.getMaxDate(hoy)));
//	}
//
//	private JButton getBtnBuscar() {
//		if (btnBuscar == null) {
//			btnBuscar = new JButton("Buscar");
//			btnBuscar.setIcon(new ImageIcon(MovimientosProductoIFrame.class
//					.getResource("/icons/search.png")));
//			btnBuscar.setBounds(461, 65, 103, 25);
//			btnBuscar.addActionListener(new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent arg0) {
//					search();
//				}
//			});
//		}
//		return btnBuscar;
//	}
//
//	private JButton getBtnSalir() {
//		if (btnSalir == null) {
//			btnSalir = new JButton("Salir");
//			btnSalir.setIcon(new ImageIcon(MovimientosProductoIFrame.class
//					.getResource("/icons/cancel.png")));
//			btnSalir.setBounds(780, 318, 103, 25);
//			btnSalir.addActionListener(new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent arg0) {
//					hideFrame();
//				}
//			});
//		}
//		return btnSalir;
//	}
//}
