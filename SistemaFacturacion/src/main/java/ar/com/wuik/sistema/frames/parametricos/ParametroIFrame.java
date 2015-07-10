package ar.com.wuik.sistema.frames.parametricos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.sistema.bo.ParametroBO;
import ar.com.wuik.sistema.entities.Parametro;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.components.WTextFieldNumeric;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
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

	public ParametroIFrame() {
		setBorder(new LineBorder(null, 1, true));
		setTitle("Parámetros");
		setFrameIcon(new ImageIcon(
				ParametroIFrame.class.getResource("/icons/parametros.png")));
		setBounds(0, 0, 488, 186);
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
			populateComponents(model);
		} catch (BusinessException bexc) {
			LOGGER.error("Error al Obtener Parametro", bexc);
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

		WTooltipUtils.showMessages(messages, btnGuardar, MessageType.ERROR);

		return WUtils.isEmpty(messages);
	}

	private JPanel getPnlParametros() {
		if (pnlParametros == null) {
			pnlParametros = new JPanel();
			pnlParametros.setBorder(new TitledBorder(null, "Parametros",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlParametros.setBounds(10, 11, 466, 101);
			pnlParametros.setLayout(null);
			pnlParametros.add(getLblNroRecibo());
			pnlParametros.add(getTxtNroRecibo());
			pnlParametros.add(getTxtNroRemito());
			pnlParametros.add(getLblNroRemito());
			pnlParametros.add(getLabel());
			pnlParametros.add(getTxtPRecibo());
			pnlParametros.add(getLabel_4());
			pnlParametros.add(getTxtPRemito());
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
			btnCancelar.setBounds(256, 123, 103, 25);
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

						parametro.setNroRecibo(Long.valueOf(nroRecibo));
						parametro.setNroRemito(Long.valueOf(nroRemito));
						parametro.setPrefRecibo(prefRecibo);
						parametro.setPrefRemito(prefRemito);

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
			btnGuardar.setBounds(373, 123, 103, 25);
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
}
