package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ar.com.wuik.sistema.entities.TributoComprobante;
import ar.com.wuik.sistema.entities.enums.TipoTributo;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WOption;
import ar.com.wuik.swing.components.WTextFieldDecimal;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.components.WTextFieldNumeric;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;

public class TributoComprobanteVerIFrame extends WAbstractModelIFrame {
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6838619883125511589L;
	private JPanel pnlBusqueda;
	private JButton btnCerrar;
	private TributoComprobante tributo;
	private JButton btnGuardar;

	private static final String CAMPO_TRIBUTO = "tributo";
	private static final String CAMPO_DESC = "descripcion";
	private static final String CAMPO_BASE_IMP = "baseImponible";
	private static final String CAMPO_ALICUOTA = "alicuota";
	private static final String CAMPO_IMPORTE = "importe";
	private JLabel lblImporte;
	private WTextFieldNumeric txfAlicuota;
	private JTextField txtImporte;
	private JLabel lblTipoTributo;
	private ComprobanteVerIFrame comprobanteVerIFrame;
	private WTextFieldDecimal txfBaseImponible;
	private JLabel lblBaseImponible;
	private JComboBox cmbTipoTributo;
	private JLabel lblAlicuota;
	private JLabel lblDetalle;
	private JTextField txtDescripcion;

	/**
	 * @wbp.parser.constructor
	 */
	public TributoComprobanteVerIFrame(TributoComprobante tributo,
			ComprobanteVerIFrame comprobanteVerIFrame) {
		this.tributo = tributo;
		this.comprobanteVerIFrame = comprobanteVerIFrame;
		initialize("Editar Tributo");
		WModel model = populateModel();
		model.addValue(CAMPO_TRIBUTO, tributo.getTributo().getId());
		model.addValue(CAMPO_DESC, tributo.getDetalle());
		model.addValue(CAMPO_BASE_IMP, tributo.getBaseImporte());
		model.addValue(CAMPO_ALICUOTA, tributo.getAlicuota());
		model.addValue(CAMPO_IMPORTE, tributo.getImporte());
		populateComponents(model);
	}

	public TributoComprobanteVerIFrame(ComprobanteVerIFrame comprobanteVerIFrame) {
		this.tributo = new TributoComprobante();
		this.comprobanteVerIFrame = comprobanteVerIFrame;
		initialize("Nuevo Tributo");
	}

	private void initialize(String title) {
		setTitle(title);
		setBorder(new LineBorder(null, 1, true));
		setFrameIcon(new ImageIcon(
				TributoComprobanteVerIFrame.class
						.getResource("/icons/productos.png")));
		setBounds(0, 0, 508, 289);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getBtnCerrar());
		getContentPane().add(getBtnGuardar());
		loadTiposTributo();
	}

	private void loadTiposTributo() {

		TipoTributo[] tiposTributoEnum = TipoTributo.values();
		for (TipoTributo tipoTributo : tiposTributoEnum) {
			getCmbTipoTributo().addItem(
					new WOption((long) tipoTributo.getId(), tipoTributo
							.getDescripcion()));
		}
	}

	@Override
	protected boolean validateModel(WModel model) {

		String detalle = model.getValue(CAMPO_DESC);
		String baseImp = model.getValue(CAMPO_BASE_IMP);
		String alicuota = model.getValue(CAMPO_ALICUOTA);
		String importe = model.getValue(CAMPO_IMPORTE);

		List<String> messages = new ArrayList<String>();

		if (WUtils.isEmpty(detalle)) {
			messages.add("Debe ingresar una Descripción");
		}

		if (WUtils.isEmpty(baseImp)) {
			messages.add("Debe ingresar una Base Imponible");
		}

		if (WUtils.isEmpty(alicuota)) {
			messages.add("Debe ingresar una Alicuota");
		}

		if (WUtils.isNotEmpty(importe)) {
			BigDecimal importeTotal = WUtils.getValue(importe);
			if (importeTotal.doubleValue() == 0) {
				messages.add("El Importe debe ser mayor a 0");
			}
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
			pnlBusqueda.setBounds(10, 11, 486, 203);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getLblImporte());
			pnlBusqueda.add(getTxfAlicuota());
			pnlBusqueda.add(getTxtImporte());
			pnlBusqueda.add(getLblTipoTributo());
			pnlBusqueda.add(getTxfBaseImponible());
			pnlBusqueda.add(getLblBaseImponible());
			pnlBusqueda.add(getCmbTipoTributo());
			pnlBusqueda.add(getLblAlicuota());
			pnlBusqueda.add(getLblDetalle());
			pnlBusqueda.add(getTxtDescripcion());
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
			btnCerrar.setIcon(new ImageIcon(TributoComprobanteVerIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(280, 225, 103, 25);
		}
		return btnCerrar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setIcon(new ImageIcon(TributoComprobanteVerIFrame.class
					.getResource("/icons/ok.png")));
			btnGuardar.setBounds(393, 225, 103, 25);
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					WModel model = populateModel();
					if (validateModel(model)) {
						WOption tributoOpt = model.getValue(CAMPO_TRIBUTO);
						String detalle = model.getValue(CAMPO_DESC);
						String baseImp = model.getValue(CAMPO_BASE_IMP);
						String alicuota = model.getValue(CAMPO_ALICUOTA);
						String importe = model.getValue(CAMPO_IMPORTE);

						tributo.setAlicuota(WUtils.getValue(alicuota));
						tributo.setBaseImporte(WUtils.getValue(baseImp));
						tributo.setDetalle(detalle);
						tributo.setImporte(WUtils.getValue(importe));
						tributo.setTributo(TipoTributo.fromValue(tributoOpt
								.getValue().intValue()));
						
						comprobanteVerIFrame.addTributo(tributo);
						hideFrame();
					}

				}
			});
		}
		return btnGuardar;
	}

	@Override
	protected JComponent getFocusComponent() {
		return getTxtDescripcion();
	}

	private JLabel getLblImporte() {
		if (lblImporte == null) {
			lblImporte = new JLabel("Importe: $");
			lblImporte.setHorizontalAlignment(SwingConstants.RIGHT);
			lblImporte.setBounds(10, 165, 121, 25);
		}
		return lblImporte;
	}

	private WTextFieldNumeric getTxfAlicuota() {
		if (txfAlicuota == null) {
			txfAlicuota = new WTextFieldNumeric();
			txfAlicuota.setBounds(141, 129, 121, 25);
			txfAlicuota.setName(CAMPO_ALICUOTA);
			txfAlicuota.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					calcularImporte();
				}
			});
		}
		return txfAlicuota;
	}

	private JTextField getTxtImporte() {
		if (txtImporte == null) {
			txtImporte = new JTextField();
			txtImporte.setHorizontalAlignment(SwingConstants.RIGHT);
			txtImporte.setEditable(Boolean.FALSE);
			txtImporte.setBounds(141, 165, 121, 25);
			txtImporte.setColumns(10);
			txtImporte.setName(CAMPO_IMPORTE);
		}
		return txtImporte;
	}

	private JLabel getLblTipoTributo() {
		if (lblTipoTributo == null) {
			lblTipoTributo = new JLabel("Tributo:");
			lblTipoTributo.setHorizontalAlignment(SwingConstants.RIGHT);
			lblTipoTributo.setBounds(10, 21, 121, 25);
		}
		return lblTipoTributo;
	}

	private WTextFieldDecimal getTxfBaseImponible() {
		if (txfBaseImponible == null) {
			txfBaseImponible = new WTextFieldDecimal(10, 2);
			txfBaseImponible.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					calcularImporte();
				}
			});
			txfBaseImponible.setName(CAMPO_BASE_IMP);
			txfBaseImponible.setBounds(141, 93, 121, 25);
		}
		return txfBaseImponible;
	}

	private JLabel getLblBaseImponible() {
		if (lblBaseImponible == null) {
			lblBaseImponible = new JLabel("* Base Imponible:");
			lblBaseImponible.setHorizontalAlignment(SwingConstants.RIGHT);
			lblBaseImponible.setBounds(10, 93, 121, 25);
		}
		return lblBaseImponible;
	}

	private JComboBox getCmbTipoTributo() {
		if (cmbTipoTributo == null) {
			cmbTipoTributo = new JComboBox();
			cmbTipoTributo.setEditable(false);
			cmbTipoTributo.setName(CAMPO_TRIBUTO);
			cmbTipoTributo.setBounds(141, 21, 236, 25);
		}
		return cmbTipoTributo;
	}

	private JLabel getLblAlicuota() {
		if (lblAlicuota == null) {
			lblAlicuota = new JLabel("* Alicuota: %");
			lblAlicuota.setHorizontalAlignment(SwingConstants.RIGHT);
			lblAlicuota.setBounds(10, 129, 121, 25);
		}
		return lblAlicuota;
	}

	private JLabel getLblDetalle() {
		if (lblDetalle == null) {
			lblDetalle = new JLabel("* Descripci\u00F3n:");
			lblDetalle.setHorizontalAlignment(SwingConstants.RIGHT);
			lblDetalle.setBounds(10, 57, 121, 25);
		}
		return lblDetalle;
	}

	private JTextField getTxtDescripcion() {
		if (txtDescripcion == null) {
			txtDescripcion = new JTextField();
			txtDescripcion.setBounds(141, 57, 318, 25);
			txtDescripcion.setName(CAMPO_DESC);
			txtDescripcion.setColumns(10);
			txtDescripcion.setDocument(new WTextFieldLimit(100));
		}
		return txtDescripcion;
	}
	
	private void calcularImporte(){
		BigDecimal baseImponible = WUtils.getValue(getTxfBaseImponible().getText());
		BigDecimal alicuota = WUtils.getValue(getTxfAlicuota().getText());
		
		BigDecimal importe = baseImponible.multiply(alicuota).divide(new BigDecimal(100));
		tributo.setImporte(importe);
		getTxtImporte().setText(WUtils.getValue(importe).toEngineeringString());
	}
}
