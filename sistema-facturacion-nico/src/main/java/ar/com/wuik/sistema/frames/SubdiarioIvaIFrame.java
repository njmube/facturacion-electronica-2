package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.apache.commons.beanutils.BeanComparator;

import com.lowagie.text.Font;

import ar.com.wuik.sistema.bo.ComprobanteBO;
import ar.com.wuik.sistema.entities.Comprobante;
import ar.com.wuik.sistema.entities.enums.EstadoFacturacion;
import ar.com.wuik.sistema.entities.enums.TipoComprobante;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.ComprobanteFilter;
import ar.com.wuik.sistema.model.ComprobanteIVAModel;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.table.WTablePanel;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.frames.WCalendarIFrame;
import ar.com.wuik.swing.utils.WFrameUtils;
import ar.com.wuik.swing.utils.WFrameUtils.FontSize;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;

/**
 * 
 * @author juan.castro
 * 
 */
public class SubdiarioIvaIFrame extends WAbstractModelIFrame {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 6097359246326577323L;
	private static final String CAMPO_FECHA_DESDE = "fechaDesde";
	private static final String CAMPO_FECHA_HASTA = "fechaHasta";

	private JPanel pnlParametros;
	private JButton btnCancelar;
	private JLabel lblInicioAct;
	private JTextField txtDesde;
	private JButton btnNewButton;
	private JTextField txtHasta;
	private JButton button;
	private JLabel lblHasta;
	private WTablePanel<Comprobante> tablePanel;
	private JLabel lblTotalIVA;
	private JLabel txtTotalIVA;
	private JButton btnBuscar;
	private JLabel lblCompra;
	private JLabel txtCompra;
	private JLabel lblVenta;
	private JLabel txtVenta;

	public SubdiarioIvaIFrame() {
		setBorder(new LineBorder(null, 1, true));
		setTitle("Subdiario de IVA");
		setFrameIcon(new ImageIcon(
				SubdiarioIvaIFrame.class.getResource("/icons/subdiario.png")));
		setBounds(0, 0, 792, 539);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlParametros());
		getContentPane().add(getBtnCancelar());

		WModel model = populateModel();

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		model.addValue(CAMPO_FECHA_DESDE,
				WUtils.getStringFromDate(calendar.getTime()));
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		model.addValue(CAMPO_FECHA_HASTA,
				WUtils.getStringFromDate(calendar.getTime()));
		populateComponents(model);
		search();
	}

	private void search() {

		WModel model = populateModel();

		if (validateModel(model)) {

			String fechaDesde = model.getValue(CAMPO_FECHA_DESDE);
			String fechaHasta = model.getValue(CAMPO_FECHA_HASTA);

			ComprobanteBO comprobanteBO = AbstractFactory
					.getInstance(ComprobanteBO.class);
			ComprobanteFilter filter = new ComprobanteFilter();
			filter.setDesde(WUtils.getDateFromString(fechaDesde));
			filter.setHasta(WUtils.getDateFromString(fechaHasta));
			filter.setEstadoFacturacion(EstadoFacturacion.FACTURADO);
			try {
				List<Comprobante> comprobantes = comprobanteBO.buscar(filter);
				if (WUtils.isNotEmpty(comprobantes)) {
					Collections.sort(comprobantes, new BeanComparator(
							"fechaVenta"));
				}
				calcularTotal(comprobantes);
				getTablePanel().addData(comprobantes);
			} catch (BusinessException bexc) {
				showGlobalErrorMsg(bexc.getMessage());
			}
		}

	}

	private void calcularTotal(List<Comprobante> comprobantes) {

		BigDecimal totalIvaCompra = BigDecimal.ZERO;
		BigDecimal totalIvaVenta = BigDecimal.ZERO;

		if (WUtils.isNotEmpty(comprobantes)) {
			for (Comprobante comprobante : comprobantes) {
				if (comprobante.getTipoComprobante().equals(
						TipoComprobante.NOTA_CREDITO)) {
					if (null != comprobante.getIdProveedor()) {
						totalIvaCompra = totalIvaCompra.subtract(comprobante
								.getIva());
					} else {
						totalIvaVenta = totalIvaVenta.subtract(comprobante
								.getIva());
					}
				} else {
					if (null != comprobante.getIdProveedor()) {
						totalIvaCompra = totalIvaCompra.add(comprobante
								.getIva());
					} else {
						totalIvaVenta = totalIvaVenta.add(comprobante.getIva());
					}
				}
			}
		}

		getTxtCompra().setText(totalIvaCompra.toEngineeringString());
		getTxtVenta().setText(totalIvaVenta.toEngineeringString());
		getTxtTotalIVA().setText(
				totalIvaCompra.subtract(totalIvaVenta).toEngineeringString());
	}

	@Override
	protected boolean validateModel(WModel model) {

		String fechaDesde = model.getValue(CAMPO_FECHA_DESDE);
		String fechaHasta = model.getValue(CAMPO_FECHA_HASTA);

		List<String> messages = new ArrayList<String>();

		if (WUtils.isEmpty(fechaDesde)) {
			messages.add("Debe seleccionar una Fecha Desde");
		}

		if (WUtils.isEmpty(fechaHasta)) {
			messages.add("Debe seleccionar una Fecha Hasta");
		}

		WTooltipUtils.showMessages(messages, btnBuscar, MessageType.ERROR);

		return WUtils.isEmpty(messages);
	}

	private JPanel getPnlParametros() {
		if (pnlParametros == null) {
			pnlParametros = new JPanel();
			pnlParametros.setBorder(new TitledBorder(null, "",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlParametros.setBounds(10, 11, 770, 454);
			pnlParametros.setLayout(null);
			pnlParametros.add(getLblInicioAct());
			pnlParametros.add(getTxtDesde());
			pnlParametros.add(getBtnNewButton());
			pnlParametros.add(getTxtHasta());
			pnlParametros.add(getButton());
			pnlParametros.add(getLabel_1());
			pnlParametros.add(getTablePanel());
			pnlParametros.add(getLabel_2());
			pnlParametros.add(getTxtTotalIVA());
			pnlParametros.add(getBtnBuscar());
			pnlParametros.add(getLblCompra());
			pnlParametros.add(getTxtCompra());
			pnlParametros.add(getLabel_1_1());
			pnlParametros.add(getTxtVenta());
		}
		return pnlParametros;
	}

	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cerrar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					hideFrame();
				}
			});
			btnCancelar.setIcon(new ImageIcon(SubdiarioIvaIFrame.class
					.getResource("/icons/cancel2.png")));
			btnCancelar.setBounds(677, 476, 103, 30);
		}
		return btnCancelar;
	}

	private JLabel getLblInicioAct() {
		if (lblInicioAct == null) {
			lblInicioAct = new JLabel("Desde:");
			lblInicioAct.setHorizontalAlignment(SwingConstants.RIGHT);
			lblInicioAct.setBounds(10, 29, 95, 25);
		}
		return lblInicioAct;
	}

	private JTextField getTxtDesde() {
		if (txtDesde == null) {
			txtDesde = new JTextField();
			txtDesde.setEditable(false);
			txtDesde.setName(CAMPO_FECHA_DESDE);
			txtDesde.setBounds(115, 29, 103, 25);
		}
		return txtDesde;
	}

	private JButton getBtnNewButton() {
		if (btnNewButton == null) {
			btnNewButton = new JButton("");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addModalIFrame(new WCalendarIFrame(txtDesde));
				}
			});
			btnNewButton.setIcon(new ImageIcon(SubdiarioIvaIFrame.class
					.getResource("/icons/calendar.png")));
			btnNewButton.setBounds(228, 29, 27, 25);
		}
		return btnNewButton;
	}

	private JTextField getTxtHasta() {
		if (txtHasta == null) {
			txtHasta = new JTextField();
			txtHasta.setName(CAMPO_FECHA_HASTA);
			txtHasta.setEditable(false);
			txtHasta.setBounds(115, 65, 103, 25);
		}
		return txtHasta;
	}

	private JButton getButton() {
		if (button == null) {
			button = new JButton("");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addModalIFrame(new WCalendarIFrame(txtHasta));
				}
			});
			button.setIcon(new ImageIcon(SubdiarioIvaIFrame.class
					.getResource("/icons/calendar.png")));
			button.setBounds(228, 65, 27, 25);
		}
		return button;
	}

	private JLabel getLabel_1() {
		if (lblHasta == null) {
			lblHasta = new JLabel("Hasta:");
			lblHasta.setHorizontalAlignment(SwingConstants.RIGHT);
			lblHasta.setBounds(10, 65, 95, 25);
		}
		return lblHasta;
	}

	private WTablePanel<Comprobante> getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new WTablePanel(ComprobanteIVAModel.class);
			tablePanel.setBounds(10, 101, 750, 297);
		}
		return tablePanel;
	}

	private JLabel getLabel_2() {
		if (lblTotalIVA == null) {
			lblTotalIVA = new JLabel("Total IVA Venta: $");
			lblTotalIVA.setHorizontalAlignment(SwingConstants.RIGHT);
			lblTotalIVA.setFont(WFrameUtils.getCustomFont(FontSize.LARGE));
			lblTotalIVA.setBounds(521, 408, 126, 34);
		}
		return lblTotalIVA;
	}

	private JLabel getTxtTotalIVA() {
		if (txtTotalIVA == null) {
			txtTotalIVA = new JLabel("0.00");
			txtTotalIVA.setHorizontalAlignment(JTextField.RIGHT);
			txtTotalIVA.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			txtTotalIVA.setBounds(657, 409, 103, 34);
		}
		return txtTotalIVA;
	}

	private JButton getBtnBuscar() {
		if (btnBuscar == null) {
			btnBuscar = new JButton("Buscar");
			btnBuscar.setIcon(new ImageIcon(SubdiarioIvaIFrame.class
					.getResource("/icons/search.png")));
			btnBuscar.setBounds(671, 62, 89, 30);
			btnBuscar.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					search();
				}
			});
		}
		return btnBuscar;
	}

	@Override
	protected JComponent getFocusComponent() {
		return null;
	}

	private JLabel getLblCompra() {
		if (lblCompra == null) {
			lblCompra = new JLabel("Total IVA Compra: $");
			lblCompra.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCompra.setFont(WFrameUtils.getCustomFont(FontSize.LARGE));
			lblCompra.setBounds(10, 409, 126, 34);
		}
		return lblCompra;
	}

	private JLabel getTxtCompra() {
		if (txtCompra == null) {
			txtCompra = new JLabel("0.00");
			txtCompra.setHorizontalAlignment(SwingConstants.RIGHT);
			txtCompra.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			txtCompra.setBounds(146, 409, 109, 34);
		}
		return txtCompra;
	}

	private JLabel getLabel_1_1() {
		if (lblVenta == null) {
			lblVenta = new JLabel("Total IVA Venta: $");
			lblVenta.setHorizontalAlignment(SwingConstants.RIGHT);
			lblVenta.setFont(WFrameUtils.getCustomFont(FontSize.LARGE));
			lblVenta.setBounds(265, 408, 126, 34);
		}
		return lblVenta;
	}

	private JLabel getTxtVenta() {
		if (txtVenta == null) {
			txtVenta = new JLabel("0.00");
			txtVenta.setHorizontalAlignment(SwingConstants.RIGHT);
			txtVenta.setFont(WFrameUtils.getCustomFont(FontSize.LARGE,
					Font.BOLD));
			txtVenta.setBounds(401, 409, 103, 34);
		}
		return txtVenta;
	}
}
