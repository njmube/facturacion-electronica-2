package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import ar.com.wuik.sistema.bo.ChequeBO;
import ar.com.wuik.sistema.bo.ClienteBO;
import ar.com.wuik.sistema.bo.ParametricoBO;
import ar.com.wuik.sistema.entities.Banco;
import ar.com.wuik.sistema.entities.Cheque;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WOption;
import ar.com.wuik.swing.components.WTextFieldDecimal;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.frames.WCalendarIFrame;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;

public class ChequeVerIFrame extends WAbstractModelIFrame {
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6838619883125511589L;
	private JPanel pnlBusqueda;
	private JLabel lblNro;
	private JTextField txtNro;
	private JButton btnCerrar;
	private Cheque cheque;
	private JButton btnGuardar;
	private JLabel lblFecha;
	private JLabel lblRecibido;

	private static final String CAMPO_NUMERO = "numero";
	private static final String CAMPO_FECHA_EMISION = "emision";
	private static final String CAMPO_BANCO = "banco";
	private static final String CAMPO_FECHA_COBRO = "fechaCobro";
	private static final String CAMPO_RECIBIDO = "recibo";
	private static final String CAMPO_TOTAL = "total";
	private static final String CAMPO_CLIENTE = "cliente";
	private ChequeIFrame chequeIFrame;
	private ReciboVerIFrame reciboVerIFrame;
	private JTextField txtFechaEmision;
	private JTextField txtFechaCobro;
	private JComboBox cmbBancos;
	private JLabel lblBanco;
	private JLabel lblMonto;
	private JTextField txtRecibido;
	private JLabel lblFechaCobro;
	private WTextFieldDecimal textFieldDecimal;
	private JButton btnFechaEmision;
	private JButton btnFechaCobro;
	private JLabel lblCliente;
	private JTextField txtCliente;

	/**
	 * Create the frame.
	 * 
	 * @wbp.parser.constructor
	 */
	public ChequeVerIFrame(ChequeIFrame chequeIFrame, Long idCheque,
			Long idCliente) {
		initialize("Editar Cheque");
		this.chequeIFrame = chequeIFrame;
		try {
			ChequeBO chequeBO = AbstractFactory.getInstance(ChequeBO.class);
			this.cheque = chequeBO.obtener(idCheque);
			Cliente cliente = this.cheque.getCliente();
			WModel model = populateModel();
			model.addValue(CAMPO_CLIENTE, cliente.getRazonSocial());
			model.addValue(CAMPO_NUMERO, cheque.getNumero());
			model.addValue(CAMPO_FECHA_COBRO,
					WUtils.getStringFromDate(cheque.getFechaPago()));
			model.addValue(CAMPO_FECHA_EMISION,
					WUtils.getStringFromDate(cheque.getFechaEmision()));
			model.addValue(CAMPO_BANCO, cheque.getBanco().getId());
			model.addValue(CAMPO_RECIBIDO, cheque.getaNombreDe());
			model.addValue(CAMPO_TOTAL, cheque.getImporte());
			populateComponents(model);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	public ChequeVerIFrame(Long idCliente, ChequeIFrame chequeIFrame) {
		initialize("Nuevo Cheque");
		this.chequeIFrame = chequeIFrame;
		this.cheque = new Cheque();
		this.cheque.setIdCliente(idCliente);
		try {
			ClienteBO clienteBO = AbstractFactory.getInstance(ClienteBO.class);
			Cliente cliente = clienteBO.obtener(idCliente);
			WModel model = populateModel();
			model.addValue(CAMPO_CLIENTE, cliente.getRazonSocial());
			populateComponents(model);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	public ChequeVerIFrame(Long idCliente, ReciboVerIFrame reciboVerIFrame) {
		initialize("Nuevo Cheque");
		this.reciboVerIFrame = reciboVerIFrame;
		this.cheque = new Cheque();
		this.cheque.setIdCliente(idCliente);
		try {
			ClienteBO clienteBO = AbstractFactory.getInstance(ClienteBO.class);
			Cliente cliente = clienteBO.obtener(idCliente);
			WModel model = populateModel();
			model.addValue(CAMPO_CLIENTE, cliente.getRazonSocial());
			populateComponents(model);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	private void initialize(String title) {
		setTitle(title);
		setBorder(new LineBorder(null, 1, true));
		setFrameIcon(new ImageIcon(
				ChequeVerIFrame.class.getResource("/icons/clientes.png")));
		setBounds(0, 0, 445, 369);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getBtnCerrar());
		getContentPane().add(getBtnGuardar());
		loadBancos();
	}

	private void loadBancos() {

		List<Banco> bancos = null;
		try {
			ParametricoBO parametricoBO = AbstractFactory
					.getInstance(ParametricoBO.class);
			bancos = parametricoBO.obtenerTodosBancos();
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}

		if (WUtils.isNotEmpty(bancos)) {
			for (Banco banco : bancos) {
				getCmbBancos().addItem(
						new WOption(banco.getId(), banco.getNombre()));
			}
		}
	}

	@Override
	protected boolean validateModel(WModel model) {

		String numero = model.getValue(CAMPO_NUMERO);
		String fechaCobro = model.getValue(CAMPO_FECHA_COBRO);
		String fechaEmision = model.getValue(CAMPO_FECHA_EMISION);
		WOption banco = model.getValue(CAMPO_BANCO);
		String recibido = model.getValue(CAMPO_RECIBIDO);
		String total = model.getValue(CAMPO_TOTAL);

		List<String> messages = new ArrayList<String>();

		if (WUtils.isEmpty(numero)) {
			messages.add("Debe ingresar un Número");
		}

		if (WUtils.isEmpty(fechaCobro)) {
			messages.add("Debe ingresar una Fecha de Cobro");
		}

		if (WUtils.isEmpty(fechaEmision)) {
			messages.add("Debe ingresar una Fecha de Emisión");
		}

		if (WOption.getIdWOptionSeleccion().equals(banco.getValue())) {
			messages.add("Debe seleccionar un Banco");
		}

		if (WUtils.isEmpty(recibido)) {
			messages.add("Debe ingresar el campo de Recibido");
		}

		if (WUtils.isEmpty(total)) {
			messages.add("Debe ingresar un Monto");
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
			pnlBusqueda.setBounds(10, 11, 421, 284);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getLblNro());
			pnlBusqueda.add(getTxtNro());
			pnlBusqueda.add(getLblFecha());
			pnlBusqueda.add(getLblRecibido());
			pnlBusqueda.add(getTxtFechaEmision());
			pnlBusqueda.add(getTxtFechaCobro());
			pnlBusqueda.add(getCmbBancos());
			pnlBusqueda.add(getLblBanco());
			pnlBusqueda.add(getLblMonto());
			pnlBusqueda.add(getTxtRecibido());
			pnlBusqueda.add(getLblFechaCobro());
			pnlBusqueda.add(getTextFieldDecimal());
			pnlBusqueda.add(getBtnFechaEmision());
			pnlBusqueda.add(getBtnFechaCobro());
			pnlBusqueda.add(getLblCliente());
			pnlBusqueda.add(getTxtCliente());
		}
		return pnlBusqueda;
	}

	private JLabel getLblNro() {
		if (lblNro == null) {
			lblNro = new JLabel("* N\u00FAmero:");
			lblNro.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNro.setBounds(10, 68, 121, 25);
		}
		return lblNro;
	}

	private JTextField getTxtNro() {
		if (txtNro == null) {
			txtNro = new JTextField();
			txtNro.setName(CAMPO_NUMERO);
			txtNro.setDocument(new WTextFieldLimit(30));
			txtNro.setBounds(141, 68, 145, 25);
		}
		return txtNro;
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
			btnCerrar.setIcon(new ImageIcon(ChequeVerIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(217, 306, 103, 25);
		}
		return btnCerrar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setIcon(new ImageIcon(ChequeVerIFrame.class
					.getResource("/icons/ok.png")));
			btnGuardar.setBounds(330, 306, 103, 25);
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					WModel model = populateModel();
					if (validateModel(model)) {

						String numero = model.getValue(CAMPO_NUMERO);
						String fechaCobro = model.getValue(CAMPO_FECHA_COBRO);
						String fechaEmision = model
								.getValue(CAMPO_FECHA_EMISION);
						WOption banco = model.getValue(CAMPO_BANCO);
						String recibido = model.getValue(CAMPO_RECIBIDO);
						String total = model.getValue(CAMPO_TOTAL);

						cheque.setNumero(numero);
						cheque.setFechaPago(WUtils
								.getDateFromString(fechaCobro));
						cheque.setFechaEmision(WUtils
								.getDateFromString(fechaEmision));
						cheque.setIdBanco(banco.getValue());
						cheque.setaNombreDe(recibido);
						cheque.setImporte(WUtils.getValue(total));

						ChequeBO chequeBO = AbstractFactory
								.getInstance(ChequeBO.class);
						try {
							if (cheque.getId() == null) {
								chequeBO.guardar(cheque);
							} else {
								chequeBO.actualizar(cheque);
							}
							hideFrame();
							if (null != chequeIFrame) {
								chequeIFrame.search();
							} else if (null != reciboVerIFrame) {
								Cheque chequeSeleccionado = chequeBO.obtener(cheque.getId());
								reciboVerIFrame.addCheque(chequeSeleccionado);
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
		return getTxtNro();
	}

	private JLabel getLblFecha() {
		if (lblFecha == null) {
			lblFecha = new JLabel("* Fecha Emisi\u00F3n:");
			lblFecha.setHorizontalAlignment(SwingConstants.RIGHT);
			lblFecha.setBounds(10, 140, 121, 25);
		}
		return lblFecha;
	}

	private JLabel getLblRecibido() {
		if (lblRecibido == null) {
			lblRecibido = new JLabel("* A nombre de:");
			lblRecibido.setHorizontalAlignment(SwingConstants.RIGHT);
			lblRecibido.setBounds(10, 212, 121, 25);
		}
		return lblRecibido;
	}

	private JTextField getTxtFechaEmision() {
		if (txtFechaEmision == null) {
			txtFechaEmision = new JTextField();
			txtFechaEmision.setEditable(false);
			txtFechaEmision.setBounds(141, 140, 92, 25);
			txtFechaEmision.setDocument(new WTextFieldLimit(100));
			txtFechaEmision.setName(CAMPO_FECHA_EMISION);
		}
		return txtFechaEmision;
	}

	private JTextField getTxtFechaCobro() {
		if (txtFechaCobro == null) {
			txtFechaCobro = new JTextField();
			txtFechaCobro.setEditable(false);
			txtFechaCobro.setBounds(141, 176, 92, 25);
			txtFechaCobro.setDocument(new WTextFieldLimit(20));
			txtFechaCobro.setName(CAMPO_FECHA_COBRO);
		}
		return txtFechaCobro;
	}

	private JComboBox getCmbBancos() {
		if (cmbBancos == null) {
			cmbBancos = new JComboBox();
			cmbBancos.setName(CAMPO_BANCO);
			cmbBancos.setBounds(141, 104, 201, 25);
		}
		return cmbBancos;
	}

	private JLabel getLblBanco() {
		if (lblBanco == null) {
			lblBanco = new JLabel("* Banco:");
			lblBanco.setHorizontalAlignment(SwingConstants.RIGHT);
			lblBanco.setBounds(10, 104, 121, 25);
		}
		return lblBanco;
	}

	private JLabel getLblMonto() {
		if (lblMonto == null) {
			lblMonto = new JLabel("* Monto: $");
			lblMonto.setHorizontalAlignment(SwingConstants.RIGHT);
			lblMonto.setBounds(10, 248, 121, 25);
		}
		return lblMonto;
	}

	private JTextField getTxtRecibido() {
		if (txtRecibido == null) {
			txtRecibido = new JTextField();
			txtRecibido.setName(CAMPO_RECIBIDO);
			txtRecibido.setBounds(141, 212, 242, 25);
			txtRecibido.setDocument(new WTextFieldLimit(50));
		}
		return txtRecibido;
	}

	private JLabel getLblFechaCobro() {
		if (lblFechaCobro == null) {
			lblFechaCobro = new JLabel("* Fecha Cobro:");
			lblFechaCobro.setHorizontalAlignment(SwingConstants.RIGHT);
			lblFechaCobro.setBounds(10, 176, 121, 25);
		}
		return lblFechaCobro;
	}

	private WTextFieldDecimal getTextFieldDecimal() {
		if (textFieldDecimal == null) {
			textFieldDecimal = new WTextFieldDecimal(7, 2);
			textFieldDecimal.setBounds(141, 248, 92, 25);
			textFieldDecimal.setName(CAMPO_TOTAL);
		}
		return textFieldDecimal;
	}

	private JButton getBtnFechaEmision() {
		if (btnFechaEmision == null) {
			btnFechaEmision = new JButton("");
			btnFechaEmision.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addModalIFrame(new WCalendarIFrame(txtFechaEmision));
				}
			});
			btnFechaEmision.setIcon(new ImageIcon(ChequeVerIFrame.class
					.getResource("/icons/calendar.png")));
			btnFechaEmision.setBounds(243, 141, 23, 23);
		}
		return btnFechaEmision;
	}

	private JButton getBtnFechaCobro() {
		if (btnFechaCobro == null) {
			btnFechaCobro = new JButton("");
			btnFechaCobro.setIcon(new ImageIcon(ChequeVerIFrame.class
					.getResource("/icons/calendar.png")));
			btnFechaCobro.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					addModalIFrame(new WCalendarIFrame(txtFechaCobro));
				}
			});
			btnFechaCobro.setBounds(243, 177, 23, 23);
		}
		return btnFechaCobro;
	}

	private JLabel getLblCliente() {
		if (lblCliente == null) {
			lblCliente = new JLabel("Cliente:");
			lblCliente.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCliente.setBounds(10, 32, 121, 25);
		}
		return lblCliente;
	}

	private JTextField getTxtCliente() {
		if (txtCliente == null) {
			txtCliente = new JTextField();
			txtCliente.setEditable(false);
			txtCliente.setName(CAMPO_CLIENTE);
			txtCliente.setBounds(141, 32, 270, 25);
		}
		return txtCliente;
	}
}
