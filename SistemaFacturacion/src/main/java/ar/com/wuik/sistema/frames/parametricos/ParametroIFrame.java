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
import ar.com.wuik.swing.utils.WFrameUtils;
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
	private static final String CAMPO_NRO_FACTURA = "nroFactura";
	private static final String CAMPO_NRO_REMITO = "nroRemito";
	private static final String CAMPO_NRO_RECIBO = "nroRecibo";
	private static final String CAMPO_NRO_NOTA_CRED = "nroNotaCred";
	private static final String CAMPO_NRO_NOTA_DEB = "nroNotaDeb";
	private static final String CAMPO_NRO_MOV_INT = "nroMovInt";

	private static final String CAMPO_P_NRO_FACTURA = "nroPFactura";
	private static final String CAMPO_P_NRO_REMITO = "nroPRemito";
	private static final String CAMPO_P_NRO_RECIBO = "nroPRecibo";
	private static final String CAMPO_P_NRO_NOTA_CRED = "nroPNotaCred";
	private static final String CAMPO_P_NRO_NOTA_DEB = "nroPNotaDeb";

	private JPanel pnlParametros;
	private JLabel lblNroFactura;
	private JLabel lblNroRecibo;
	private JLabel lblNroNotaCredito;
	private JLabel lblNroNotaDebito;
	private JLabel lblNroMovInterno;
	private JButton btnCancelar;
	private JButton btnGuardar;
	private WTextFieldNumeric txtNroFactura;
	private WTextFieldNumeric txtNroRecibo;
	private WTextFieldNumeric txtNroNotaCredito;
	private WTextFieldNumeric txtNroNotaDebito;
	private WTextFieldNumeric txtNroMovInterno;
	private WTextFieldNumeric txtNroRemito;
	private JLabel lblNroRemito;
	private JTextField txtPFactura;
	private JLabel lblPrefijo;
	private JLabel label;
	private JTextField txtPRecibo;
	private JLabel label_1;
	private JTextField txtPNotaCredito;
	private JLabel label_2;
	private JTextField txtPNotaDebito;
	private JLabel label_4;
	private JTextField txtPRemito;
	private Parametro parametro;

	public ParametroIFrame() {
		setBorder(new LineBorder(null, 1, true));
		setTitle("Parámetros");
		setFrameIcon(new ImageIcon(
				ParametroIFrame.class.getResource("/icons/parametros.png")));
		setBounds(0, 0, 488, 343);
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
			model.addValue(CAMPO_NRO_FACTURA, parametro.getNroFactura());
			model.addValue(CAMPO_NRO_MOV_INT, parametro.getNroMovInterno());
			model.addValue(CAMPO_NRO_NOTA_CRED, parametro.getNroNotaCredito());
			model.addValue(CAMPO_NRO_NOTA_DEB, parametro.getNroNotaDebito());
			model.addValue(CAMPO_NRO_RECIBO, parametro.getNroRecibo());
			model.addValue(CAMPO_NRO_REMITO, parametro.getNroRemito());
			
			model.addValue(CAMPO_P_NRO_FACTURA, parametro.getPrefFactura());
			model.addValue(CAMPO_P_NRO_RECIBO, parametro.getPrefRecibo());
			model.addValue(CAMPO_P_NRO_NOTA_CRED, parametro.getPrefNotaCredito());
			model.addValue(CAMPO_P_NRO_NOTA_DEB, parametro.getPrefNotaDebito());
			model.addValue(CAMPO_P_NRO_REMITO, parametro.getPrefRemito());
			
			populateComponents(model);
		} catch (BusinessException bexc) {
			LOGGER.error("Error al Obtener Parametro", bexc);
		}
	}

	@Override
	protected JComponent getFocusComponent() {
		return getTxtNroFactura();
	}

	@Override
	protected boolean validateModel(WModel model) {

		String nroFactura = model.getValue(CAMPO_NRO_FACTURA);
		String nroRecibo = model.getValue(CAMPO_NRO_RECIBO);
		String nroNotaCred = model.getValue(CAMPO_NRO_NOTA_CRED);
		String nroNotaDeb = model.getValue(CAMPO_NRO_NOTA_DEB);
		String nroRemito = model.getValue(CAMPO_NRO_REMITO);
		String nroMovInterno = model.getValue(CAMPO_NRO_MOV_INT);

		String nroPFactura = model.getValue(CAMPO_P_NRO_FACTURA);
		String nroPRecibo = model.getValue(CAMPO_P_NRO_RECIBO);
		String nroPNotaCred = model.getValue(CAMPO_P_NRO_NOTA_CRED);
		String nroPNotaDeb = model.getValue(CAMPO_P_NRO_NOTA_DEB);
		String nroPRemito = model.getValue(CAMPO_P_NRO_REMITO);

		List<String> messages = new ArrayList<String>();

		if (WUtils.isEmpty(nroFactura)) {
			messages.add("Debe ingresar un Nro. de Factura");
		}

		if (WUtils.isEmpty(nroPFactura)) {
			messages.add("Debe ingresar un Prefijo de Factura");
		}

		if (WUtils.isEmpty(nroRecibo)) {
			messages.add("Debe ingresar un Nro. de Recibo");
		}

		if (WUtils.isEmpty(nroPRecibo)) {
			messages.add("Debe ingresar un Prefijo de Recibo");
		}

		if (WUtils.isEmpty(nroNotaCred)) {
			messages.add("Debe ingresar un Nro. de Crédito");
		}

		if (WUtils.isEmpty(nroPNotaCred)) {
			messages.add("Debe ingresar un Prefijo de Nota de Crédito");
		}

		if (WUtils.isEmpty(nroNotaDeb)) {
			messages.add("Debe ingresar un Nro. de Débito");
		}

		if (WUtils.isEmpty(nroPNotaDeb)) {
			messages.add("Debe ingresar un Prefijo de Nota de Débito");
		}

		if (WUtils.isEmpty(nroRemito)) {
			messages.add("Debe ingresar un Nro. de Remito");
		}

		if (WUtils.isEmpty(nroPRemito)) {
			messages.add("Debe ingresar un Prefijo de Remito");
		}

		if (WUtils.isEmpty(nroMovInterno)) {
			messages.add("Debe ingresar un Nro. de Mov. Interno");
		}

		WTooltipUtils.showMessages(messages, btnGuardar, MessageType.ERROR);

		return WUtils.isEmpty(messages);
	}

	private JPanel getPnlParametros() {
		if (pnlParametros == null) {
			pnlParametros = new JPanel();
			pnlParametros.setBorder(new TitledBorder(null, "Parametros",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlParametros.setBounds(10, 11, 466, 258);
			pnlParametros.setLayout(null);
			pnlParametros.add(getLblNroFactura());
			pnlParametros.add(getLblNroRecibo());
			pnlParametros.add(getLblNroNotaCredito());
			pnlParametros.add(getLblNroNotaDebito());
			pnlParametros.add(getLblNroMovInterno());
			pnlParametros.add(getTxtNroFactura());
			pnlParametros.add(getTxtNroRecibo());
			pnlParametros.add(getTxtNroNotaCredito());
			pnlParametros.add(getTxtNroNotaDebito());
			pnlParametros.add(getTxtNroMovInterno());
			pnlParametros.add(getTxtNroRemito());
			pnlParametros.add(getLblNroRemito());
			pnlParametros.add(getTxtPFactura());
			pnlParametros.add(getLblPrefijo());
			pnlParametros.add(getLabel());
			pnlParametros.add(getTxtPRecibo());
			pnlParametros.add(getLabel_1());
			pnlParametros.add(getTxtPNotaCredito());
			pnlParametros.add(getLabel_2());
			pnlParametros.add(getTxtPNotaDebito());
			pnlParametros.add(getLabel_4());
			pnlParametros.add(getTxtPRemito());
		}
		return pnlParametros;
	}

	private JLabel getLblNroFactura() {
		if (lblNroFactura == null) {
			lblNroFactura = new JLabel("* Nro. Factura:");
			lblNroFactura.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNroFactura.setBounds(8, 26, 121, 25);
		}
		return lblNroFactura;
	}

	private JLabel getLblNroRecibo() {
		if (lblNroRecibo == null) {
			lblNroRecibo = new JLabel("* Nro. Recibo:");
			lblNroRecibo.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNroRecibo.setBounds(4, 62, 125, 25);
		}
		return lblNroRecibo;
	}

	private JLabel getLblNroNotaCredito() {
		if (lblNroNotaCredito == null) {
			lblNroNotaCredito = new JLabel("* Nro. Nota Cr\u00E9dito:");
			lblNroNotaCredito.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNroNotaCredito.setBounds(4, 96, 126, 25);
		}
		return lblNroNotaCredito;
	}

	private JLabel getLblNroNotaDebito() {
		if (lblNroNotaDebito == null) {
			lblNroNotaDebito = new JLabel("* Nro. Nota D\u00E9bito:");
			lblNroNotaDebito.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNroNotaDebito.setBounds(3, 133, 126, 25);
		}
		return lblNroNotaDebito;
	}

	private JLabel getLblNroMovInterno() {
		if (lblNroMovInterno == null) {
			lblNroMovInterno = new JLabel("* Nro. Mov. Interno:");
			lblNroMovInterno.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNroMovInterno.setBounds(5, 205, 124, 25);
		}
		return lblNroMovInterno;
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
			btnCancelar.setBounds(256, 280, 103, 25);
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

						String nroFactura = model.getValue(CAMPO_NRO_FACTURA);
						String nroRecibo = model.getValue(CAMPO_NRO_RECIBO);
						String nroNotaCred = model
								.getValue(CAMPO_NRO_NOTA_CRED);
						String nroNotaDeb = model.getValue(CAMPO_NRO_NOTA_DEB);
						String nroRemito = model.getValue(CAMPO_NRO_REMITO);
						String nroMovInterno = model
								.getValue(CAMPO_NRO_MOV_INT);

						String prefFactura = model
								.getValue(CAMPO_P_NRO_FACTURA);
						String prefRecibo = model.getValue(CAMPO_P_NRO_RECIBO);
						String prefNotaCredito = model
								.getValue(CAMPO_P_NRO_NOTA_CRED);
						String prefNotaDebito = model
								.getValue(CAMPO_P_NRO_NOTA_DEB);
						String prefRemito = model.getValue(CAMPO_P_NRO_REMITO);

						parametro.setNroFactura(Long.valueOf(nroFactura));
						parametro.setNroRecibo(Long.valueOf(nroRecibo));
						parametro.setNroNotaCredito(Long.valueOf(nroNotaCred));
						parametro.setNroNotaDebito(Long.valueOf(nroNotaDeb));
						parametro.setNroRemito(Long.valueOf(nroRemito));
						parametro.setNroMovInterno(Long.valueOf(nroMovInterno));

						parametro.setPrefFactura(prefFactura);
						parametro.setPrefNotaCredito(prefNotaCredito);
						parametro.setPrefNotaDebito(prefNotaDebito);
						parametro.setPrefRecibo(prefRecibo);
						parametro.setPrefRemito(prefRemito);

						ParametroBO parametroBO = AbstractFactory
								.getInstance(ParametroBO.class);
						try {
							parametroBO.actualizar(parametro);
							hideFrame();
						} catch (BusinessException bexc) {
							LOGGER.error("Error al guardar Parámetro", bexc);
							WFrameUtils
									.showGlobalErrorMsg("Se ha producido un error al guardar Parámetro");
						}
					}
				}
			});
			btnGuardar.setIcon(new ImageIcon(ParametroIFrame.class
					.getResource("/icons/ok.png")));
			btnGuardar.setBounds(373, 280, 103, 25);
		}
		return btnGuardar;
	}

	private WTextFieldNumeric getTxtNroFactura() {
		if (txtNroFactura == null) {
			txtNroFactura = new WTextFieldNumeric();
			txtNroFactura.setDocument(new WTextFieldLimit(20));
			txtNroFactura.setBounds(135, 26, 134, 25);
			txtNroFactura.setName(CAMPO_NRO_FACTURA);
		}
		return txtNroFactura;
	}

	private WTextFieldNumeric getTxtNroRecibo() {
		if (txtNroRecibo == null) {
			txtNroRecibo = new WTextFieldNumeric();
			txtNroRecibo.setDocument(new WTextFieldLimit(20));
			txtNroRecibo.setBounds(135, 62, 134, 25);
			txtNroRecibo.setName(CAMPO_NRO_RECIBO);
		}
		return txtNroRecibo;
	}

	private WTextFieldNumeric getTxtNroNotaCredito() {
		if (txtNroNotaCredito == null) {
			txtNroNotaCredito = new WTextFieldNumeric();
			txtNroNotaCredito.setDocument(new WTextFieldLimit(20));
			txtNroNotaCredito.setBounds(135, 98, 134, 25);
			txtNroNotaCredito.setName(CAMPO_NRO_NOTA_CRED);
		}
		return txtNroNotaCredito;
	}

	private WTextFieldNumeric getTxtNroNotaDebito() {
		if (txtNroNotaDebito == null) {
			txtNroNotaDebito = new WTextFieldNumeric();
			txtNroNotaDebito.setBounds(135, 134, 134, 25);
			txtNroNotaDebito.setDocument(new WTextFieldLimit(20));
			txtNroNotaDebito.setName(CAMPO_NRO_NOTA_DEB);
		}
		return txtNroNotaDebito;
	}

	private WTextFieldNumeric getTxtNroMovInterno() {
		if (txtNroMovInterno == null) {
			txtNroMovInterno = new WTextFieldNumeric();
			txtNroMovInterno.setBounds(135, 207, 134, 25);
			txtNroMovInterno.setDocument(new WTextFieldLimit(20));
			txtNroMovInterno.setName(CAMPO_NRO_MOV_INT);
		}
		return txtNroMovInterno;
	}

	private WTextFieldNumeric getTxtNroRemito() {
		if (txtNroRemito == null) {
			txtNroRemito = new WTextFieldNumeric();
			txtNroRemito.setBounds(135, 169, 134, 25);
			txtNroRemito.setDocument(new WTextFieldLimit(20));
			txtNroRemito.setName(CAMPO_NRO_REMITO);
		}
		return txtNroRemito;
	}

	private JLabel getLblNroRemito() {
		if (lblNroRemito == null) {
			lblNroRemito = new JLabel("* Nro. Remito:");
			lblNroRemito.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNroRemito.setBounds(5, 169, 124, 25);
		}
		return lblNroRemito;
	}

	private JTextField getTxtPFactura() {
		if (txtPFactura == null) {
			txtPFactura = new JTextField();
			txtPFactura.setBounds(353, 26, 103, 25);
			txtPFactura.setColumns(10);
			txtPFactura.setName(CAMPO_P_NRO_FACTURA);
		}
		return txtPFactura;
	}

	private JLabel getLblPrefijo() {
		if (lblPrefijo == null) {
			lblPrefijo = new JLabel("Prefijo:");
			lblPrefijo.setBounds(279, 26, 64, 25);
		}
		return lblPrefijo;
	}

	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel("Prefijo:");
			label.setBounds(279, 62, 64, 25);
		}
		return label;
	}

	private JTextField getTxtPRecibo() {
		if (txtPRecibo == null) {
			txtPRecibo = new JTextField();
			txtPRecibo.setColumns(10);
			txtPRecibo.setBounds(353, 62, 103, 25);
			txtPRecibo.setName(CAMPO_P_NRO_RECIBO);
		}
		return txtPRecibo;
	}

	private JLabel getLabel_1() {
		if (label_1 == null) {
			label_1 = new JLabel("Prefijo:");
			label_1.setBounds(279, 98, 64, 25);
		}
		return label_1;
	}

	private JTextField getTxtPNotaCredito() {
		if (txtPNotaCredito == null) {
			txtPNotaCredito = new JTextField();
			txtPNotaCredito.setColumns(10);
			txtPNotaCredito.setBounds(353, 98, 103, 25);
			txtPNotaCredito.setName(CAMPO_P_NRO_NOTA_CRED);
		}
		return txtPNotaCredito;
	}

	private JLabel getLabel_2() {
		if (label_2 == null) {
			label_2 = new JLabel("Prefijo:");
			label_2.setBounds(279, 134, 64, 25);
		}
		return label_2;
	}

	private JTextField getTxtPNotaDebito() {
		if (txtPNotaDebito == null) {
			txtPNotaDebito = new JTextField();
			txtPNotaDebito.setColumns(10);
			txtPNotaDebito.setBounds(353, 134, 103, 25);
			txtPNotaDebito.setName(CAMPO_P_NRO_NOTA_DEB);
		}
		return txtPNotaDebito;
	}

	private JLabel getLabel_4() {
		if (label_4 == null) {
			label_4 = new JLabel("Prefijo:");
			label_4.setBounds(279, 169, 64, 25);
		}
		return label_4;
	}

	private JTextField getTxtPRemito() {
		if (txtPRemito == null) {
			txtPRemito = new JTextField();
			txtPRemito.setColumns(10);
			txtPRemito.setBounds(353, 169, 103, 25);
			txtPRemito.setName(CAMPO_P_NRO_REMITO);
		}
		return txtPRemito;
	}
}
