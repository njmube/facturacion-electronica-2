package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ar.com.wuik.sistema.bo.ComprobanteBO;
import ar.com.wuik.sistema.entities.Comprobante;
import ar.com.wuik.sistema.entities.DetalleComprobante;
import ar.com.wuik.sistema.entities.Proveedor;
import ar.com.wuik.sistema.entities.Remito;
import ar.com.wuik.sistema.entities.TributoComprobante;
import ar.com.wuik.sistema.entities.enums.TipoLetraComprobante;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.model.TributoComprobanteModel;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.components.table.WTablePanel;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.utils.WFrameUtils;
import ar.com.wuik.swing.utils.WFrameUtils.FontSize;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;

import com.lowagie.text.Font;

public class ComprobanteProveedorVistaIFrame extends WAbstractModelIFrame {
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6838619883125511589L;
	private static final String CAMPO_FECHA_EMISION = "fechaEmision";
	private static final String CAMPO_OBSERVACIONES = "observaciones";
	private static final String CAMPO_VTO_CAE = "vtoCae";
	private static final String CAMPO_CAE = "cae";
	private static final String CAMPO_NRO_COMP = "nroComp";
	private JPanel pnlBusqueda;
	private JButton btnCerrar;
	private Comprobante comprobante;
	private JLabel txtFechaEmision;
	private JLabel lblFechaEmisin;
	private JLabel lblObservaciones;
	private JTextArea txaObservaciones;
	private JScrollPane scrollPane;
	private JLabel txtSubtotalPesos;
	private JLabel lblSubtotal;
	private JLabel lblTotal;
	private JLabel txtTotalPesos;
	private JLabel lblIVA21;
	private JLabel txtIVA21;
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
	private JLabel lblCAE;
	private JLabel lblCae;
	private JLabel lblFechaVtoCae;
	private JLabel lblFechaCAE;
	private JLabel lblTributos;
	private JLabel lblNroComp;
	private JLabel txtNroComp;

	/**
	 * @wbp.parser.constructor
	 */
	public ComprobanteProveedorVistaIFrame(Long idComprobante) {

		WModel model = populateModel();
		initialize();
		ComprobanteBO comprobanteBO = AbstractFactory
				.getInstance(ComprobanteBO.class);
		try {
			this.comprobante = comprobanteBO.obtener(idComprobante);
			model.addValue(CAMPO_FECHA_EMISION,
					WUtils.getStringFromDate(comprobante.getFechaVenta()));
			model.addValue(CAMPO_OBSERVACIONES, comprobante.getObservaciones());
			model.addValue(CAMPO_CAE, comprobante.getCae());
			model.addValue(CAMPO_VTO_CAE,
					WUtils.getStringFromDate(comprobante.getFechaCAE()));
			model.addValue(CAMPO_NRO_COMP, comprobante.getNroCompFormato());
			
			txaObservaciones.setText(comprobante.getObservaciones());
			lblFechaCAE.setText(comprobante.getFechaCAE().toString());
			
			calcularTotales();
			refreshTributos();
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}

		String titulo = "";
		Icon icon = null;

		switch (comprobante.getTipoComprobante()) {
		case FACTURA:
			titulo = "Ver Factura Proveedor";
			icon = new ImageIcon(
					ComprobanteIFrame.class.getResource("/icons/facturas.png"));
			lblTipoComp.setText("FACTURA");
			break;
		case NOTA_CREDITO:
			titulo = "Ver Nota de Cr�dito";
			icon = new ImageIcon(
					ComprobanteIFrame.class
							.getResource("/icons/notas_credito.png"));
			lblTipoComp.setText("NOTA DE CR�DITO");
			break;
		case NOTA_DEBITO:
			titulo = "Ver Nota de D�bito";
			icon = new ImageIcon(
					ComprobanteIFrame.class
							.getResource("/icons/notas_debito.png"));
			lblTipoComp.setText("NOTA DE D�BITO");
			break;
		}

		setTitle(titulo);
		setFrameIcon(icon);

		populateComponents(model);
		loadProveedor(comprobante.getProveedor());
	}

	private void loadProveedor(Proveedor proveedor) {
		getTxtProveedor().setText(proveedor.getRazonSocial());
		getTxtCondIVA().setText(proveedor.getCondicionIVA().getDenominacion());
		if (comprobante.getTipoLetraComprobante()
				.equals(TipoLetraComprobante.A)) {
			lblLetra.setIcon(new ImageIcon(
					ComprobanteProveedorVistaIFrame.class
							.getResource("/icons32/letra_a.png")));
		} else {
			lblLetra.setIcon(new ImageIcon(
					ComprobanteProveedorVistaIFrame.class
							.getResource("/icons32/letra_b.png")));
		}
	}

	private void initialize() {
		setBorder(new LineBorder(null, 1, true));
		setBounds(0, 0, 1021, 413);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getBtnCerrar());
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
		}

		WTooltipUtils.showMessages(messages, component, MessageType.ERROR);

		return WUtils.isEmpty(messages);
	}

	private JPanel getPnlBusqueda() {
		if (pnlBusqueda == null) {
			pnlBusqueda = new JPanel();
			pnlBusqueda.setBorder(new TitledBorder(UIManager
					.getBorder("TitledBorder.border"), "Datos",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlBusqueda.setBounds(10, 11, 1001, 320);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getTxtFechaEmision());
			pnlBusqueda.add(getLblFechaEmisin());
			pnlBusqueda.add(getPanel_1());
			pnlBusqueda.add(getLblProveedor());
			pnlBusqueda.add(getTxtProveedor());
			pnlBusqueda.add(getTxtCondIVA());
			pnlBusqueda.add(getLblCondIva());
			pnlBusqueda.add(getLblLetra());
			pnlBusqueda.add(getLblTipoComp());
			pnlBusqueda.add(getLblCAE());
			pnlBusqueda.add(getLblCae());
			pnlBusqueda.add(getLblFechaVtoCae());
			pnlBusqueda.add(getLblFechaCAE());
			pnlBusqueda.add(getLblNroComp());
			pnlBusqueda.add(getTxtNroComp());
		}
		return pnlBusqueda;
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
			btnCerrar.setIcon(new ImageIcon(
					ComprobanteProveedorVistaIFrame.class
							.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(906, 342, 103, 30);
		}
		return btnCerrar;
	}

	@Override
	protected JComponent getFocusComponent() {
		return null;
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

	private JLabel getTxtFechaEmision() {
		if (txtFechaEmision == null) {
			txtFechaEmision = new JLabel();
			txtFechaEmision.setName(CAMPO_FECHA_EMISION);
			txtFechaEmision.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			txtFechaEmision.setBounds(486, 21, 141, 25);
		}
		return txtFechaEmision;
	}

	private JLabel getLblFechaEmisin() {
		if (lblFechaEmisin == null) {
			lblFechaEmisin = new JLabel("Fecha Emisi\u00F3n:");
			lblFechaEmisin.setIcon(new ImageIcon(
					ComprobanteProveedorVistaIFrame.class
							.getResource("/icons/calendar.png")));
			lblFechaEmisin.setHorizontalAlignment(SwingConstants.RIGHT);
			lblFechaEmisin.setBounds(366, 21, 110, 25);
		}
		return lblFechaEmisin;
	}

	private JLabel getLblObservaciones() {
		if (lblObservaciones == null) {
			lblObservaciones = new JLabel("Observaciones:");
			lblObservaciones.setBounds(482, 8, 116, 25);
			lblObservaciones.setHorizontalAlignment(SwingConstants.LEFT);
		}
		return lblObservaciones;
	}

	private JTextArea getTxaObservaciones() {
		if (txaObservaciones == null) {
			txaObservaciones = new JTextArea();
			txaObservaciones.setEditable(false);
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

	protected DetalleComprobante getDetalleById(Long selectedItem) {
		List<DetalleComprobante> detalles = comprobante.getDetalles();
		for (DetalleComprobante detalleFactura : detalles) {
			if (detalleFactura.getCoalesceId().equals(selectedItem)) {
				return detalleFactura;
			}
		}
		return null;
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
			lblTotal.setBounds(702, 113, 125, 25);
			lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblTotal;
	}

	private JLabel getTxtTotalPesos() {
		if (txtTotalPesos == null) {
			txtTotalPesos = new JLabel();
			txtTotalPesos.setBounds(837, 113, 125, 25);
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

		subtotalIVA21 = comprobante.getIva();
		subtotal = comprobante.getSubtotal();
		total = comprobante.getTotal();

		List<TributoComprobante> tributos = comprobante.getTributos();
		for (TributoComprobante tributoComprobante : tributos) {
			totalTributo = totalTributo.add(tributoComprobante.getImporte());
		}

		comprobante.setSubtotal(subtotal);
		comprobante.setTotalTributos(totalTributo);
		comprobante.setTotal(total);
		comprobante.setIva(subtotalIVA21);

		getTxtSubtotalPesos().setText(
				WUtils.getValue(comprobante.getSubtotal())
						.toEngineeringString());
		getTxtIVA21().setText(subtotalIVA21.toString());
		getTxtTotalPesos().setText(total.toString());
		getLblImporteOtrosTributos().setText(
				WUtils.getValue(totalTributo).toEngineeringString());
	}

	private JLabel getLblIVA21() {
		if (lblIVA21 == null) {
			lblIVA21 = new JLabel("IVA: $");
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

	public void addDetalle(DetalleComprobante detalle) {
		detalle.setComprobante(comprobante);
		List<DetalleComprobante> detalles = comprobante.getDetalles();
		detalles.add(detalle);
		calcularTotales();
	}

	private JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			panel_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING,
					TitledBorder.TOP, null, null));
			panel_1.setBounds(10, 131, 981, 185);
			panel_1.setLayout(null);
			panel_1.add(getTxtTotalPesos());
			panel_1.add(getLblTotal());
			panel_1.add(getLblIVA21());
			panel_1.add(getTxtIVA21());
			panel_1.add(getTxtSubtotalPesos());
			panel_1.add(getLblSubtotal());
			panel_1.add(getLblObservaciones());
			panel_1.add(getScrollPane());
			panel_1.add(getTblTributos());
			panel_1.add(getLblImporteOtrosTributos());
			panel_1.add(getLblOtrosTributos());
			panel_1.add(getLblTributos());
		}
		return panel_1;
	}

	public void addComprobantes(List<Comprobante> comprobantes) {
		comprobante.getComprobantesAsociados().addAll(comprobantes);
		refreshComprobantesAsoc();
	}

	private void refreshComprobantesAsoc() {
		List<Comprobante> comprobantes = new ArrayList<Comprobante>(
				comprobante.getComprobantesAsociados());
	}

	private JLabel getLblProveedor() {
		if (lblProveedor == null) {
			lblProveedor = new JLabel("Proveedor:");
			lblProveedor.setIcon(new ImageIcon(
					ComprobanteProveedorVistaIFrame.class
							.getResource("/icons/proveedores.png")));
			lblProveedor.setBounds(10, 21, 81, 25);
		}
		return lblProveedor;
	}

	private JLabel getTxtProveedor() {
		if (txtProveedor == null) {
			txtProveedor = new JLabel();
			txtProveedor.setBorder(null);
			txtProveedor.setBounds(101, 21, 255, 25);
			txtProveedor.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return txtProveedor;
	}

	private JLabel getTxtCondIVA() {
		if (txtCondIVA == null) {
			txtCondIVA = new JLabel();
			txtCondIVA.setBorder(null);
			txtCondIVA.setBounds(101, 57, 285, 25);
			txtCondIVA.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
		}
		return txtCondIVA;
	}

	private JLabel getLblCondIva() {
		if (lblCondIva == null) {
			lblCondIva = new JLabel("Cond. IVA:");
			lblCondIva.setBounds(10, 57, 81, 25);
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
			tblTributos.setBounds(10, 36, 462, 138);
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
			lblImporteOtrosTributos.setBounds(837, 82, 125, 25);
		}
		return lblImporteOtrosTributos;
	}

	private JLabel getLblOtrosTributos() {
		if (lblOtrosTributos == null) {
			lblOtrosTributos = new JLabel("Importe Otros Tributos: $");
			lblOtrosTributos.setHorizontalAlignment(SwingConstants.RIGHT);
			lblOtrosTributos.setBounds(687, 82, 140, 25);
		}
		return lblOtrosTributos;
	}

	private JLabel getLblCAE() {
		if (lblCAE == null) {
			lblCAE = new JLabel();
			lblCAE.setName(CAMPO_CAE);
			lblCAE.setFont(WFrameUtils.getCustomFont(FontSize.LARGE, Font.BOLD));
			lblCAE.setBounds(486, 57, 141, 25);
		}
		return lblCAE;
	}

	private JLabel getLblCae() {
		if (lblCae == null) {
			lblCae = new JLabel("CAE:");
			lblCae.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCae.setBounds(366, 57, 110, 25);
		}
		return lblCae;
	}

	private JLabel getLblFechaVtoCae() {
		if (lblFechaVtoCae == null) {
			lblFechaVtoCae = new JLabel("Fecha Vto.:");
			lblFechaVtoCae.setHorizontalAlignment(SwingConstants.RIGHT);
			lblFechaVtoCae.setBounds(366, 95, 110, 25);
		}
		return lblFechaVtoCae;
	}

	private JLabel getLblFechaCAE() {
		if (lblFechaCAE == null) {
			lblFechaCAE = new JLabel();
			lblFechaCAE.setName(CAMPO_VTO_CAE);
			lblFechaCAE.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			lblFechaCAE.setBounds(486, 95, 141, 25);
		}
		return lblFechaCAE;
	}

	private JLabel getLblTributos() {
		if (lblTributos == null) {
			lblTributos = new JLabel("Tributos:");
			lblTributos.setIcon(new ImageIcon(
					ComprobanteProveedorVistaIFrame.class
							.getResource("/icons/compAsociados.png")));
			lblTributos.setBounds(10, 8, 184, 25);

		}
		return lblTributos;
	}
	private JLabel getLblNroComp() {
		if (lblNroComp == null) {
			lblNroComp = new JLabel("Nro. Comp.:");
			lblNroComp.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNroComp.setBounds(10, 93, 81, 25);
		}
		return lblNroComp;
	}
	private JLabel getTxtNroComp() {
		if (txtNroComp == null) {
			txtNroComp = new JLabel();
			txtNroComp.setText((String) null);
			txtNroComp.setName(CAMPO_NRO_COMP);
			txtNroComp.setFont(WFrameUtils.getCustomFont(FontSize.LARGE, Font.BOLD));
			txtNroComp.setBounds(101, 93, 141, 25);
		}
		return txtNroComp;
	}
}
