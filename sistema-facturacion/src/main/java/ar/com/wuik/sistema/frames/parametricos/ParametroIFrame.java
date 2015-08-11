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
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.sistema.bo.ComprobanteBO;
import ar.com.wuik.sistema.bo.ParametroBO;
import ar.com.wuik.sistema.entities.Parametro;
import ar.com.wuik.sistema.entities.enums.TipoComprobante;
import ar.com.wuik.sistema.entities.enums.TipoLetraComprobante;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
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
	
	private static final String CAMPO_NRO_FACTURA_A = "nroFacturaA";
	private static final String CAMPO_NRO_NOTA_CREDITO_A = "nroNotaCreditoA";
	private static final String CAMPO_NRO_NOTA_DEBITO_A = "nroNotaDebitoA";
	
	private static final String CAMPO_NRO_FACTURA_A_AFIP = "nroFacturaAAfip";
	private static final String CAMPO_NRO_NOTA_CREDITO_A_AFIP = "nroNotaCreditoAAfip";
	private static final String CAMPO_NRO_NOTA_DEBITO_A_AFIP = "nroNotaDebitoAAfip";
	
	
	private static final String CAMPO_NRO_FACTURA_B = "nroFacturaB";
	private static final String CAMPO_NRO_NOTA_CREDITO_B = "nroNotaCreditoB";
	private static final String CAMPO_NRO_NOTA_DEBITO_B = "nroNotaDebitoB";
	
	private static final String CAMPO_NRO_FACTURA_B_AFIP = "nroFacturaBAfip";
	private static final String CAMPO_NRO_NOTA_CREDITO_B_AFIP = "nroNotaCreditoBAfip";
	private static final String CAMPO_NRO_NOTA_DEBITO_B_AFIP = "nroNotaDebitoBAfip";


	private static final String CAMPO_P_NRO_REMITO = "nroPRemito";
	private static final String CAMPO_P_NRO_RECIBO = "nroPRecibo";

	private JPanel pnlParametros;
	private JLabel lblNroRecibo;
	private JButton btnCancelar;
	private JButton btnGuardar;
	private JSpinner txtNroRecibo;
	private JSpinner txtNroRemito;
	private JLabel lblNroRemito;
	private JLabel label;
	private JTextField txtPRecibo;
	private JLabel label_4;
	private JTextField txtPRemito;
	private Parametro parametro;
	private JSpinner spnNroFacturaA;
	private JLabel lblFactura;
	private JSpinner spnNotaCreditoA;
	private JLabel lblNotaCredito;
	private JSpinner spnNotaDebitoA;
	private JLabel lblNotaDebito;
	private JLabel lblNroFacturaAFIP;
	private JTextField txtNroFacturaAAfip;
	private JTextField txtNroNCAAfip;
	private JLabel lblNroNCAfip;
	private JTextField txtNroNDAAfip;
	private JLabel lblNroNDAfip;
	private JLabel label_1;
	private JSpinner spnNroFacturaB;
	private JLabel label_2;
	private JTextField txtNroFacturaBAfip;
	private JTextField txtNroNCBAfip;
	private JTextField txtNroNDBAfip;
	private JLabel label_3;
	private JLabel label_5;
	private JSpinner spnNotaCreditoB;
	private JLabel label_6;
	private JLabel label_7;
	private JSpinner spnNotaDebitoB;
	private JPanel panel;
	private JPanel panel_1;

	public ParametroIFrame() {
		setBorder(new LineBorder(null, 1, true));
		setTitle("Parámetros");
		setFrameIcon(new ImageIcon(
				ParametroIFrame.class.getResource("/icons/parametros.png")));
		setBounds(0, 0, 629, 488);
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
			model.addValue(CAMPO_NRO_RECIBO, parametro.getNroRecibo().intValue());
			model.addValue(CAMPO_NRO_REMITO, parametro.getNroRemito().intValue());
			model.addValue(CAMPO_P_NRO_RECIBO, parametro.getPrefRecibo());
			model.addValue(CAMPO_P_NRO_REMITO, parametro.getPrefRemito());
			
			model.addValue(CAMPO_NRO_FACTURA_A, parametro.getNroFacturaA().intValue());
			model.addValue(CAMPO_NRO_NOTA_CREDITO_A, parametro.getNroNotaCreditoA().intValue());
			model.addValue(CAMPO_NRO_NOTA_DEBITO_A, parametro.getNroNotaDebitoA().intValue());
			
			model.addValue(CAMPO_NRO_FACTURA_B, parametro.getNroFacturaB().intValue());
			model.addValue(CAMPO_NRO_NOTA_CREDITO_B, parametro.getNroNotaCreditoB().intValue());
			model.addValue(CAMPO_NRO_NOTA_DEBITO_B, parametro.getNroNotaDebitoB().intValue());

			
			
			ComprobanteBO comprobanteBO = AbstractFactory.getInstance(ComprobanteBO.class);
			Long ultNroFacturaA = comprobanteBO.obtenerUltimoNroComprobante(TipoComprobante.FACTURA, TipoLetraComprobante.A);
			Long ultNroFacturaB = comprobanteBO.obtenerUltimoNroComprobante(TipoComprobante.FACTURA, TipoLetraComprobante.B);
			
			Long ultNroNotaCreditoA = comprobanteBO.obtenerUltimoNroComprobante(TipoComprobante.NOTA_CREDITO, TipoLetraComprobante.A);
			Long ultNroNotaCreditoB = comprobanteBO.obtenerUltimoNroComprobante(TipoComprobante.NOTA_CREDITO, TipoLetraComprobante.B);
			
			Long ultNroNotaDebitoA = comprobanteBO.obtenerUltimoNroComprobante(TipoComprobante.NOTA_DEBITO, TipoLetraComprobante.A);
			Long ultNroNotaDebitoB = comprobanteBO.obtenerUltimoNroComprobante(TipoComprobante.NOTA_DEBITO, TipoLetraComprobante.B);
			
			 
			model.addValue(CAMPO_NRO_FACTURA_A_AFIP, ultNroFacturaA);
			model.addValue(CAMPO_NRO_NOTA_CREDITO_A_AFIP, ultNroNotaCreditoA);
			model.addValue(CAMPO_NRO_NOTA_DEBITO_A_AFIP, ultNroNotaDebitoA);
			model.addValue(CAMPO_NRO_FACTURA_B_AFIP, ultNroFacturaB);
			model.addValue(CAMPO_NRO_NOTA_CREDITO_B_AFIP, ultNroNotaCreditoB);
			model.addValue(CAMPO_NRO_NOTA_DEBITO_B_AFIP, ultNroNotaDebitoB);
			
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

		String nroPRecibo = model.getValue(CAMPO_P_NRO_RECIBO);
		String nroPRemito = model.getValue(CAMPO_P_NRO_REMITO);
		
		List<String> messages = new ArrayList<String>();


		if (WUtils.isEmpty(nroPRecibo)) {
			messages.add("Debe ingresar un Prefijo de Recibo");
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
			pnlParametros.setBorder(new TitledBorder(null, "",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlParametros.setBounds(10, 11, 609, 400);
			pnlParametros.setLayout(null);
			pnlParametros.add(getLblNroRecibo());
			pnlParametros.add(getTxtNroRecibo());
			pnlParametros.add(getTxtNroRemito());
			pnlParametros.add(getLblNroRemito());
			pnlParametros.add(getLabel());
			pnlParametros.add(getTxtPRecibo());
			pnlParametros.add(getLabel_4());
			pnlParametros.add(getTxtPRemito());
			pnlParametros.add(getPanel());
			pnlParametros.add(getPanel_1());
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
			btnCancelar.setBounds(399, 422, 103, 30);
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
						Integer nroRecibo = model.getValue(CAMPO_NRO_RECIBO);
						Integer nroRemito = model.getValue(CAMPO_NRO_REMITO);
						String prefRecibo = model.getValue(CAMPO_P_NRO_RECIBO);
						String prefRemito = model.getValue(CAMPO_P_NRO_REMITO);
						
						Integer nroFacturaA = model.getValue(CAMPO_NRO_FACTURA_A);
						Integer nroNotaCreditoA = model.getValue(CAMPO_NRO_NOTA_CREDITO_A);
						Integer nroNotaDebitoA = model.getValue(CAMPO_NRO_NOTA_DEBITO_A);

						Integer nroFacturaB = model.getValue(CAMPO_NRO_FACTURA_B);
						Integer nroNotaCreditoB = model.getValue(CAMPO_NRO_NOTA_CREDITO_B);
						Integer nroNotaDebitoB = model.getValue(CAMPO_NRO_NOTA_DEBITO_B);
						
						
						parametro.setNroRecibo(Long.valueOf(nroRecibo));
						parametro.setNroRemito(Long.valueOf(nroRemito));
						parametro.setPrefRecibo(prefRecibo);
						parametro.setPrefRemito(prefRemito);
						parametro.setNroFacturaA(Long.valueOf(nroFacturaA));
						parametro.setNroNotaCreditoA(Long.valueOf(nroNotaCreditoA));
						parametro.setNroNotaDebitoA(Long.valueOf(nroNotaDebitoA));
						
						parametro.setNroFacturaB(Long.valueOf(nroFacturaB));
						parametro.setNroNotaCreditoB(Long.valueOf(nroNotaCreditoB));
						parametro.setNroNotaDebitoB(Long.valueOf(nroNotaDebitoB));

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
			btnGuardar.setBounds(516, 422, 103, 30);
		}
		return btnGuardar;
	}

	private JSpinner getTxtNroRecibo() {
		if (txtNroRecibo == null) {
			txtNroRecibo = new JSpinner();
			txtNroRecibo.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
			txtNroRecibo.setBounds(178, 24, 109, 25);
			txtNroRecibo.setName(CAMPO_NRO_RECIBO);
		}
		return txtNroRecibo;
	}

	private JSpinner getTxtNroRemito() {
		if (txtNroRemito == null) {
			txtNroRemito = new JSpinner();
			txtNroRemito.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
			txtNroRemito.setBounds(178, 60, 109, 25);
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
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			label.setBounds(345, 24, 64, 25);
		}
		return label;
	}

	private JTextField getTxtPRecibo() {
		if (txtPRecibo == null) {
			txtPRecibo = new JTextField();
			txtPRecibo.setColumns(10);
			txtPRecibo.setBounds(419, 24, 103, 25);
			txtPRecibo.setName(CAMPO_P_NRO_RECIBO);
		}
		return txtPRecibo;
	}

	private JLabel getLabel_4() {
		if (label_4 == null) {
			label_4 = new JLabel("Prefijo:");
			label_4.setHorizontalAlignment(SwingConstants.RIGHT);
			label_4.setBounds(344, 60, 64, 25);
		}
		return label_4;
	}

	private JTextField getTxtPRemito() {
		if (txtPRemito == null) {
			txtPRemito = new JTextField();
			txtPRemito.setColumns(10);
			txtPRemito.setBounds(418, 60, 103, 25);
			txtPRemito.setName(CAMPO_P_NRO_REMITO);
		}
		return txtPRemito;
	}
	private JSpinner getSpnNroFacturaA() {
		if (spnNroFacturaA == null) {
			spnNroFacturaA = new JSpinner();
			spnNroFacturaA.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
			spnNroFacturaA.setBounds(167, 26, 108, 25);
			spnNroFacturaA.setName(CAMPO_NRO_FACTURA_A);
		}
		return spnNroFacturaA;
	}
	private JLabel getLblFactura() {
		if (lblFactura == null) {
			lblFactura = new JLabel("* Nro. Factura:");
			lblFactura.setBounds(11, 26, 147, 25);
			lblFactura.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblFactura;
	}
	private JSpinner getSpnNotaCreditoA() {
		if (spnNotaCreditoA == null) {
			spnNotaCreditoA = new JSpinner();
			spnNotaCreditoA.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
			spnNotaCreditoA.setBounds(167, 62, 108, 25);
			spnNotaCreditoA.setName(CAMPO_NRO_NOTA_CREDITO_A);
		}
		return spnNotaCreditoA;
	}
	private JLabel getLblNotaCredito() {
		if (lblNotaCredito == null) {
			lblNotaCredito = new JLabel("* Nro. Nota Cr\u00E9dito:");
			lblNotaCredito.setBounds(10, 62, 147, 25);
			lblNotaCredito.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblNotaCredito;
	}
	private JSpinner getSpnNotaDebitoA() {
		if (spnNotaDebitoA == null) {
			spnNotaDebitoA = new JSpinner();
			spnNotaDebitoA.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
			spnNotaDebitoA.setBounds(167, 98, 108, 25);
			spnNotaDebitoA.setName(CAMPO_NRO_NOTA_DEBITO_A);
		}
		return spnNotaDebitoA;
	}
	private JLabel getLblNotaDebito() {
		if (lblNotaDebito == null) {
			lblNotaDebito = new JLabel("* Nro. Nota D\u00E9bito:");
			lblNotaDebito.setBounds(10, 98, 147, 25);
			lblNotaDebito.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblNotaDebito;
	}
	private JLabel getLblNroFacturaAFIP() {
		if (lblNroFacturaAFIP == null) {
			lblNroFacturaAFIP = new JLabel("Nro. AFIP:");
			lblNroFacturaAFIP.setBounds(388, 26, 64, 25);
			lblNroFacturaAFIP.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblNroFacturaAFIP;
	}
	private JTextField getTxtNroFacturaAAfip() {
		if (txtNroFacturaAAfip == null) {
			txtNroFacturaAAfip = new JTextField();
			txtNroFacturaAAfip.setBounds(462, 26, 103, 25);
			txtNroFacturaAAfip.setEditable(false);
			txtNroFacturaAAfip.setName(CAMPO_NRO_FACTURA_A_AFIP);
			txtNroFacturaAAfip.setColumns(10);
		}
		return txtNroFacturaAAfip;
	}
	private JTextField getTxtNroNCAAfip() {
		if (txtNroNCAAfip == null) {
			txtNroNCAAfip = new JTextField();
			txtNroNCAAfip.setBounds(462, 62, 103, 25);
			txtNroNCAAfip.setEditable(false);
			txtNroNCAAfip.setName(CAMPO_NRO_NOTA_CREDITO_A_AFIP);
			txtNroNCAAfip.setColumns(10);
		}
		return txtNroNCAAfip;
	}
	private JLabel getLblNroNCAfip() {
		if (lblNroNCAfip == null) {
			lblNroNCAfip = new JLabel("Nro. AFIP:");
			lblNroNCAfip.setBounds(388, 62, 64, 25);
			lblNroNCAfip.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblNroNCAfip;
	}
	private JTextField getTxtNroNDAAfip() {
		if (txtNroNDAAfip == null) {
			txtNroNDAAfip = new JTextField();
			txtNroNDAAfip.setBounds(462, 98, 103, 25);
			txtNroNDAAfip.setEditable(false);
			txtNroNDAAfip.setName(CAMPO_NRO_NOTA_DEBITO_A_AFIP);
			txtNroNDAAfip.setColumns(10);
		}
		return txtNroNDAAfip;
	}
	private JLabel getLblNroNDAfip() {
		if (lblNroNDAfip == null) {
			lblNroNDAfip = new JLabel("Nro. AFIP:");
			lblNroNDAfip.setBounds(388, 98, 64, 25);
			lblNroNDAfip.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblNroNDAfip;
	}
	private JLabel getLabel_1() {
		if (label_1 == null) {
			label_1 = new JLabel("* Nro. Factura:");
			label_1.setBounds(10, 29, 147, 25);
			label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return label_1;
	}
	private JSpinner getSpnNroFacturaB() {
		if (spnNroFacturaB == null) {
			spnNroFacturaB = new JSpinner();
			spnNroFacturaB.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
			spnNroFacturaB.setBounds(167, 29, 108, 25);
			spnNroFacturaB.setName(CAMPO_NRO_FACTURA_B);
		}
		return spnNroFacturaB;
	}
	private JLabel getLabel_2() {
		if (label_2 == null) {
			label_2 = new JLabel("Nro. AFIP:");
			label_2.setBounds(389, 29, 64, 25);
			label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return label_2;
	}
	private JTextField getTxtNroFacturaBAfip() {
		if (txtNroFacturaBAfip == null) {
			txtNroFacturaBAfip = new JTextField();
			txtNroFacturaBAfip.setBounds(463, 29, 103, 25);
			txtNroFacturaBAfip.setName(CAMPO_NRO_FACTURA_B_AFIP);
			txtNroFacturaBAfip.setEditable(false);
			txtNroFacturaBAfip.setColumns(10);
		}
		return txtNroFacturaBAfip;
	}
	private JTextField getTxtNroNCBAfip() {
		if (txtNroNCBAfip == null) {
			txtNroNCBAfip = new JTextField();
			txtNroNCBAfip.setBounds(463, 65, 103, 25);
			txtNroNCBAfip.setName(CAMPO_NRO_NOTA_CREDITO_B_AFIP);
			txtNroNCBAfip.setEditable(false);
			txtNroNCBAfip.setColumns(10);
		}
		return txtNroNCBAfip;
	}
	private JTextField getTxtNroNDBAfip() {
		if (txtNroNDBAfip == null) {
			txtNroNDBAfip = new JTextField();
			txtNroNDBAfip.setBounds(463, 101, 103, 25);
			txtNroNDBAfip.setName(CAMPO_NRO_NOTA_DEBITO_B_AFIP);
			txtNroNDBAfip.setEditable(false);
			txtNroNDBAfip.setColumns(10);
		}
		return txtNroNDBAfip;
	}
	private JLabel getLabel_3() {
		if (label_3 == null) {
			label_3 = new JLabel("Nro. AFIP:");
			label_3.setBounds(389, 101, 64, 25);
			label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return label_3;
	}
	private JLabel getLabel_5() {
		if (label_5 == null) {
			label_5 = new JLabel("Nro. AFIP:");
			label_5.setBounds(389, 65, 64, 25);
			label_5.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return label_5;
	}
	private JSpinner getSpnNotaCreditoB() {
		if (spnNotaCreditoB == null) {
			spnNotaCreditoB = new JSpinner();
			spnNotaCreditoB.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
			spnNotaCreditoB.setBounds(167, 65, 108, 25);
			spnNotaCreditoB.setName(CAMPO_NRO_NOTA_CREDITO_B);
		}
		return spnNotaCreditoB;
	}
	private JLabel getLabel_6() {
		if (label_6 == null) {
			label_6 = new JLabel("* Nro. Nota Cr\u00E9dito:");
			label_6.setBounds(10, 65, 147, 25);
			label_6.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return label_6;
	}
	private JLabel getLabel_7() {
		if (label_7 == null) {
			label_7 = new JLabel("* Nro. Nota D\u00E9bito:");
			label_7.setBounds(10, 101, 147, 25);
			label_7.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return label_7;
	}
	private JSpinner getSpnNotaDebitoB() {
		if (spnNotaDebitoB == null) {
			spnNotaDebitoB = new JSpinner();
			spnNotaDebitoB.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
			spnNotaDebitoB.setBounds(167, 101, 108, 25);
			spnNotaDebitoB.setName(CAMPO_NRO_NOTA_DEBITO_B);
		}
		return spnNotaDebitoB;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Comprobantes A", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel.setBounds(10, 96, 589, 146);
			panel.setLayout(null);
			panel.add(getTxtNroNDAAfip());
			panel.add(getLblNroNDAfip());
			panel.add(getSpnNotaDebitoA());
			panel.add(getLblNotaDebito());
			panel.add(getLblNotaCredito());
			panel.add(getSpnNotaCreditoA());
			panel.add(getLblNroNCAfip());
			panel.add(getTxtNroNCAAfip());
			panel.add(getSpnNroFacturaA());
			panel.add(getLblFactura());
			panel.add(getLblNroFacturaAFIP());
			panel.add(getTxtNroFacturaAAfip());
		}
		return panel;
	}
	private JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
			panel_1.setBorder(new TitledBorder(null, "Comprobantes B", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel_1.setBounds(10, 253, 589, 137);
			panel_1.setLayout(null);
			panel_1.add(getLabel_1());
			panel_1.add(getSpnNroFacturaB());
			panel_1.add(getLabel_2());
			panel_1.add(getTxtNroFacturaBAfip());
			panel_1.add(getTxtNroNCBAfip());
			panel_1.add(getLabel_5());
			panel_1.add(getSpnNotaCreditoB());
			panel_1.add(getLabel_6());
			panel_1.add(getSpnNotaDebitoB());
			panel_1.add(getLabel_7());
			panel_1.add(getLabel_3());
			panel_1.add(getTxtNroNDBAfip());
		}
		return panel_1;
	}
}
