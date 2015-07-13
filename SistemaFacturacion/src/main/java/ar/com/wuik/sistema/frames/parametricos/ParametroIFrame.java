package ar.com.wuik.sistema.frames.parametricos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.sistema.bo.ParametroBO;
import ar.com.wuik.sistema.entities.Parametro;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.sistema.utils.AppUtils;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.components.WTextFieldNumeric;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.frames.WCalendarIFrame;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;

/**
 * 
 * @author juan.castro
 * 
 */
public class ParametroIFrame extends WAbstractModelIFrame {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 6097359246326577323L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ParametroIFrame.class);
	private static final String CAMPO_NRO_REMITO = "nroRemito";
	private static final String CAMPO_NRO_RECIBO = "nroRecibo";

	private static final String CAMPO_P_NRO_REMITO = "nroPRemito";
	private static final String CAMPO_P_NRO_RECIBO = "nroPRecibo";

	private static final String CAMPO_RAZON_SOCIAL = "razonSocial";
	private static final String CAMPO_CUIT = "cuit";
	private static final String CAMPO_ING_BRUTOS = "ingBrutos";
	private static final String CAMPO_DOMICILIO = "domicilio";
	private static final String CAMPO_COND_IVA = "condIVA";
	private static final String CAMPO_INICIO_ACT = "inicioActividad";

	private JPanel pnlParametros;
	private JLabel lblNroRecibo;
	private JButton btnCancelar;
	private JButton btnGuardar;
	private WTextFieldNumeric txtNroRecibo;
	private WTextFieldNumeric txtNroRemito;
	private JLabel lblNroRemito;
	private JLabel label;
	private JTextField txtPRecibo;
	private JLabel label_4;
	private JTextField txtPRemito;
	private Parametro parametro;
	private JLabel lblRazonSocial;
	private JTextField txtRazonSocial;
	private JLabel lblCuit;
	private JFormattedTextField txtCuit;
	private JLabel lblDomicilio;
	private JTextField txtDomicilio;
	private JLabel lblCondIVA;
	private JTextField txtCondIVA;
	private JLabel lblIngBrutos;
	private WTextFieldNumeric txtIngBrutos;
	private JLabel lblInicioAct;
	private JTextField txtInicioAct;
	private JButton btnNewButton;

	public ParametroIFrame() {
		setBorder(new LineBorder(null, 1, true));
		setTitle("Parámetros");
		setFrameIcon(new ImageIcon(
				ParametroIFrame.class.getResource("/icons/parametros.png")));
		setBounds(0, 0, 488, 415);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlParametros());
		getContentPane().add(getBtnCancelar());
		getContentPane().add(getBtnGuardar());
		loadParametros();
	}

	private void loadParametros() {
		ParametroBO parametroBO = AbstractFactory
				.getInstance(ParametroBO.class);
		try {
			parametro = parametroBO.obtener(1L);
			WModel model = populateModel();
			model.addValue(CAMPO_NRO_RECIBO, parametro.getNroRecibo());
			model.addValue(CAMPO_NRO_REMITO, parametro.getNroRemito());
			model.addValue(CAMPO_P_NRO_RECIBO, parametro.getPrefRecibo());
			model.addValue(CAMPO_P_NRO_REMITO, parametro.getPrefRemito());

			model.addValue(CAMPO_RAZON_SOCIAL, parametro.getRazonSocial());
			model.addValue(CAMPO_COND_IVA, parametro.getCondIVA());
			model.addValue(CAMPO_CUIT, parametro.getCuit());
			model.addValue(CAMPO_DOMICILIO, parametro.getDomicilio());
			model.addValue(CAMPO_ING_BRUTOS, parametro.getIngresosBrutos());
			model.addValue(CAMPO_INICIO_ACT,
					WUtils.getStringFromDate(parametro.getInicioActividad()));
			populateComponents(model);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	@Override
	protected JComponent getFocusComponent() {
		return getTxtNroRecibo();
	}

	@Override
	protected boolean validateModel(WModel model) {

		String nroRecibo = model.getValue(CAMPO_NRO_RECIBO);
		String nroRemito = model.getValue(CAMPO_NRO_REMITO);

		String nroPRecibo = model.getValue(CAMPO_P_NRO_RECIBO);
		String nroPRemito = model.getValue(CAMPO_P_NRO_REMITO);

		String razonSocial = model.getValue(CAMPO_RAZON_SOCIAL);
		String cuit = model.getValue(CAMPO_CUIT);
		String domicilio = model.getValue(CAMPO_DOMICILIO);
		String ingBrutos = model.getValue(CAMPO_ING_BRUTOS);
		String inicioAct = model.getValue(CAMPO_INICIO_ACT);
		String condIVA = model.getValue(CAMPO_COND_IVA);

		List<String> messages = new ArrayList<String>();

		if (WUtils.isEmpty(nroRecibo)) {
			messages.add("Debe ingresar un Nro. de Recibo");
		}

		if (WUtils.isEmpty(nroPRecibo)) {
			messages.add("Debe ingresar un Prefijo de Recibo");
		}

		if (WUtils.isEmpty(nroRemito)) {
			messages.add("Debe ingresar un Nro. de Remito");
		}

		if (WUtils.isEmpty(nroPRemito)) {
			messages.add("Debe ingresar un Prefijo de Remito");
		}

		if (WUtils.isEmpty(razonSocial)) {
			messages.add("Debe ingresar una Razón Social");
		}

		if (!AppUtils.esValidoCUIT(cuit)) {
			messages.add("Debe ingresar un CUIT");
		}

		if (WUtils.isEmpty(domicilio)) {
			messages.add("Debe ingresar un Domicilio");
		}

		if (WUtils.isEmpty(condIVA)) {
			messages.add("Debe ingresar una Cond. de IVA");
		}

		if (WUtils.isEmpty(ingBrutos)) {
			messages.add("Debe ingresar el Ingreso Bruto");
		}

		if (WUtils.isEmpty(inicioAct)) {
			messages.add("Debe seleccionar un Inicio de Actividad");
		}

		WTooltipUtils.showMessages(messages, btnGuardar, MessageType.ERROR);

		return WUtils.isEmpty(messages);
	}

	private JPanel getPnlParametros() {
		if (pnlParametros == null) {
			pnlParametros = new JPanel();
			pnlParametros.setBorder(new TitledBorder(null, "Parametros",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlParametros.setBounds(10, 11, 466, 317);
			pnlParametros.setLayout(null);
			pnlParametros.add(getLblNroRecibo());
			pnlParametros.add(getTxtNroRecibo());
			pnlParametros.add(getTxtNroRemito());
			pnlParametros.add(getLblNroRemito());
			pnlParametros.add(getLabel());
			pnlParametros.add(getTxtPRecibo());
			pnlParametros.add(getLabel_4());
			pnlParametros.add(getTxtPRemito());
			pnlParametros.add(getLblRazonSocial());
			pnlParametros.add(getTxtRazonSocial());
			pnlParametros.add(getLblCuit());
			pnlParametros.add(getTxtCuit());
			pnlParametros.add(getLblDomicilio());
			pnlParametros.add(getTxtDomicilio());
			pnlParametros.add(getLblCondIVA());
			pnlParametros.add(getTxtCondIVA());
			pnlParametros.add(getLblIngBrutos());
			pnlParametros.add(getTxtIngBrutos());
			pnlParametros.add(getLblInicioAct());
			pnlParametros.add(getTxtInicioAct());
			pnlParametros.add(getBtnNewButton());
		}
		return pnlParametros;
	}

	private JLabel getLblNroRecibo() {
		if (lblNroRecibo == null) {
			lblNroRecibo = new JLabel("* Nro. Recibo:");
			lblNroRecibo.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNroRecibo.setBounds(4, 24, 125, 25);
		}
		return lblNroRecibo;
	}

	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					hideFrame();
				}
			});
			btnCancelar.setIcon(new ImageIcon(ParametroIFrame.class
					.getResource("/icons/cancel2.png")));
			btnCancelar.setBounds(256, 352, 103, 25);
		}
		return btnCancelar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					WModel model = populateModel();
					if (validateModel(model)) {
						String nroRecibo = model.getValue(CAMPO_NRO_RECIBO);
						String nroRemito = model.getValue(CAMPO_NRO_REMITO);
						String prefRecibo = model.getValue(CAMPO_P_NRO_RECIBO);
						String prefRemito = model.getValue(CAMPO_P_NRO_REMITO);

						String razonSocial = model.getValue(CAMPO_RAZON_SOCIAL);
						String cuit = model.getValue(CAMPO_CUIT);
						String domicilio = model.getValue(CAMPO_DOMICILIO);
						String ingBrutos = model.getValue(CAMPO_ING_BRUTOS);
						String inicioAct = model.getValue(CAMPO_INICIO_ACT);
						String condIVA = model.getValue(CAMPO_COND_IVA);

						parametro.setNroRecibo(Long.valueOf(nroRecibo));
						parametro.setNroRemito(Long.valueOf(nroRemito));
						parametro.setPrefRecibo(prefRecibo);
						parametro.setPrefRemito(prefRemito);
						parametro.setCondIVA(condIVA);
						parametro.setCuit(cuit);
						parametro.setDomicilio(domicilio);
						parametro.setIngresosBrutos(ingBrutos);
						parametro.setInicioActividad(WUtils
								.getDateFromString(inicioAct));
						parametro.setRazonSocial(razonSocial);

						ParametroBO parametroBO = AbstractFactory
								.getInstance(ParametroBO.class);
						try {
							parametroBO.actualizar(parametro);
							hideFrame();
						} catch (BusinessException bexc) {
							LOGGER.error("Error al guardar Parámetro", bexc);
							showGlobalErrorMsg(bexc.getMessage());
						}
					}
				}
			});
			btnGuardar.setIcon(new ImageIcon(ParametroIFrame.class
					.getResource("/icons/ok.png")));
			btnGuardar.setBounds(373, 352, 103, 25);
		}
		return btnGuardar;
	}

	private WTextFieldNumeric getTxtNroRecibo() {
		if (txtNroRecibo == null) {
			txtNroRecibo = new WTextFieldNumeric();
			txtNroRecibo.setDocument(new WTextFieldLimit(20));
			txtNroRecibo.setBounds(135, 24, 134, 25);
			txtNroRecibo.setName(CAMPO_NRO_RECIBO);
		}
		return txtNroRecibo;
	}

	private WTextFieldNumeric getTxtNroRemito() {
		if (txtNroRemito == null) {
			txtNroRemito = new WTextFieldNumeric();
			txtNroRemito.setBounds(134, 60, 134, 25);
			txtNroRemito.setDocument(new WTextFieldLimit(20));
			txtNroRemito.setName(CAMPO_NRO_REMITO);
		}
		return txtNroRemito;
	}

	private JLabel getLblNroRemito() {
		if (lblNroRemito == null) {
			lblNroRemito = new JLabel("* Nro. Remito:");
			lblNroRemito.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNroRemito.setBounds(4, 60, 124, 25);
		}
		return lblNroRemito;
	}

	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel("Prefijo:");
			label.setBounds(279, 24, 64, 25);
		}
		return label;
	}

	private JTextField getTxtPRecibo() {
		if (txtPRecibo == null) {
			txtPRecibo = new JTextField();
			txtPRecibo.setColumns(10);
			txtPRecibo.setBounds(353, 24, 103, 25);
			txtPRecibo.setName(CAMPO_P_NRO_RECIBO);
		}
		return txtPRecibo;
	}

	private JLabel getLabel_4() {
		if (label_4 == null) {
			label_4 = new JLabel("Prefijo:");
			label_4.setBounds(278, 60, 64, 25);
		}
		return label_4;
	}

	private JTextField getTxtPRemito() {
		if (txtPRemito == null) {
			txtPRemito = new JTextField();
			txtPRemito.setColumns(10);
			txtPRemito.setBounds(352, 60, 103, 25);
			txtPRemito.setName(CAMPO_P_NRO_REMITO);
		}
		return txtPRemito;
	}

	private JLabel getLblRazonSocial() {
		if (lblRazonSocial == null) {
			lblRazonSocial = new JLabel("* Raz\u00F3n Social:");
			lblRazonSocial.setHorizontalAlignment(SwingConstants.RIGHT);
			lblRazonSocial.setBounds(4, 96, 124, 25);
		}
		return lblRazonSocial;
	}

	private JTextField getTxtRazonSocial() {
		if (txtRazonSocial == null) {
			txtRazonSocial = new JTextField();
			txtRazonSocial.setName(CAMPO_RAZON_SOCIAL);
			txtRazonSocial.setBounds(134, 96, 243, 25);
			txtRazonSocial.setDocument(new WTextFieldLimit(100));
		}
		return txtRazonSocial;
	}

	private JLabel getLblCuit() {
		if (lblCuit == null) {
			lblCuit = new JLabel("* Cuit:");
			lblCuit.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCuit.setBounds(5, 132, 124, 25);
		}
		return lblCuit;
	}

	private JFormattedTextField getTxtCuit() {
		if (txtCuit == null) {

			MaskFormatter formatter = null;
			try {
				formatter = new MaskFormatter("##-########-#");
				formatter.setPlaceholderCharacter('#');
			} catch (java.text.ParseException exc) {
			}
			txtCuit = new JFormattedTextField(formatter);
			txtCuit.setName(CAMPO_CUIT);
			txtCuit.setBounds(135, 132, 134, 25);
		}
		return txtCuit;
	}

	private JLabel getLblDomicilio() {
		if (lblDomicilio == null) {
			lblDomicilio = new JLabel("* Domicilio:");
			lblDomicilio.setHorizontalAlignment(SwingConstants.RIGHT);
			lblDomicilio.setBounds(4, 168, 124, 25);
		}
		return lblDomicilio;
	}

	private JTextField getTxtDomicilio() {
		if (txtDomicilio == null) {
			txtDomicilio = new JTextField();
			txtDomicilio.setName(CAMPO_DOMICILIO);
			txtDomicilio.setDocument(new WTextFieldLimit(100));
			txtDomicilio.setBounds(134, 168, 243, 25);
		}
		return txtDomicilio;
	}

	private JLabel getLblCondIVA() {
		if (lblCondIVA == null) {
			lblCondIVA = new JLabel("* Cond. IVA:");
			lblCondIVA.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCondIVA.setBounds(5, 204, 124, 25);
		}
		return lblCondIVA;
	}

	private JTextField getTxtCondIVA() {
		if (txtCondIVA == null) {
			txtCondIVA = new JTextField();
			txtCondIVA.setName(CAMPO_COND_IVA);
			txtCondIVA.setDocument(new WTextFieldLimit(100));
			txtCondIVA.setBounds(135, 204, 187, 25);
		}
		return txtCondIVA;
	}

	private JLabel getLblIngBrutos() {
		if (lblIngBrutos == null) {
			lblIngBrutos = new JLabel("* Ing. Brutos");
			lblIngBrutos.setHorizontalAlignment(SwingConstants.RIGHT);
			lblIngBrutos.setBounds(4, 240, 124, 25);
		}
		return lblIngBrutos;
	}

	private WTextFieldNumeric getTxtIngBrutos() {
		if (txtIngBrutos == null) {
			txtIngBrutos = new WTextFieldNumeric();
			txtIngBrutos.setHorizontalAlignment(SwingConstants.LEFT);
			txtIngBrutos.setName(CAMPO_ING_BRUTOS);
			txtIngBrutos.setBounds(134, 240, 134, 25);
		}
		return txtIngBrutos;
	}

	private JLabel getLblInicioAct() {
		if (lblInicioAct == null) {
			lblInicioAct = new JLabel("* Inicio Act.:");
			lblInicioAct.setHorizontalAlignment(SwingConstants.RIGHT);
			lblInicioAct.setBounds(4, 276, 124, 25);
		}
		return lblInicioAct;
	}

	private JTextField getTxtInicioAct() {
		if (txtInicioAct == null) {
			txtInicioAct = new JTextField();
			txtInicioAct.setEditable(false);
			txtInicioAct.setName(CAMPO_INICIO_ACT);
			txtInicioAct.setBounds(134, 276, 103, 25);
		}
		return txtInicioAct;
	}

	private JButton getBtnNewButton() {
		if (btnNewButton == null) {
			btnNewButton = new JButton("");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addModalIFrame(new WCalendarIFrame(txtInicioAct));
				}
			});
			btnNewButton.setIcon(new ImageIcon(ParametroIFrame.class
					.getResource("/icons/calendar.png")));
			btnNewButton.setBounds(247, 276, 27, 23);
		}
		return btnNewButton;
	}
}
