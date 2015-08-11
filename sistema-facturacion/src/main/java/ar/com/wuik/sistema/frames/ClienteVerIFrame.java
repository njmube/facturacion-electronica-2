package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import ar.com.wuik.sistema.bo.ClienteBO;
import ar.com.wuik.sistema.bo.ParametricoBO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.Localidad;
import ar.com.wuik.sistema.entities.enums.CondicionIVA;
import ar.com.wuik.sistema.entities.enums.TipoDocumento;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.sistema.utils.AppUtils;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WOption;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.components.WTextFieldNumeric;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;

public class ClienteVerIFrame extends WAbstractModelIFrame {
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6838619883125511589L;
	private JPanel pnlBusqueda;
	private JLabel lblRazonSocial;
	private JTextField txtRazonSocial;
	private JButton btnCerrar;
	private Cliente cliente;
	private JButton btnGuardar;
	private JLabel lblDireccion;
	private JLabel lblCelular;

	private static final String CAMPO_RAZON_SOCIAL = "razonSocial";
	private static final String CAMPO_DIRECCION = "direccion";
	private static final String CAMPO_TELEFONO = "telefono";
	private static final String CAMPO_CELULAR = "celular";
	private static final String CAMPO_LOCALIDAD = "localidad";
	private static final String CAMPO_NUMERO_DOC = "nroDoc";
	private static final String CAMPO_NUMERO_DOC2 = "nroDoc2";
	private static final String CAMPO_TIPO_DOC = "tipoDoc";
	private static final String CAMPO_TIPO_IVA = "tipoIva";
	private ClienteIFrame clienteIFrame;
	private JTextField txtDireccion;
	private JTextField txtTelefono;
	private JFormattedTextField txfCuit;
	private JLabel lblDocumento;
	private JLabel lblLocalidad;
	private JTextField txtCelular;
	private JLabel lblTipoIva;
	private JComboBox cmbLocalidad;
	private JComboBox cmbTipoIva;
	private JLabel lblTelefono;
	private JLabel label;
	private JComboBox cmbTipoDoc;
	private JLabel lblTipoDoc;
	private WTextFieldNumeric txtDNIOtros;

	/**
	 * Create the frame.
	 * 
	 * @wbp.parser.constructor
	 */
	public ClienteVerIFrame(Long idCliente, ClienteIFrame clienteIFrame) {
		initialize("Editar Cliente");
		this.clienteIFrame = clienteIFrame;
		loadTiposDocumento();
		loadCliente(idCliente);
	}

	private void loadTiposDocumento() {
		TipoDocumento[] tiposDocumento = TipoDocumento.values();
		for (TipoDocumento tipoDocumento : tiposDocumento) {
			getCmbTipoDoc().addItem(
					new WOption((long) tipoDocumento.getId(), tipoDocumento
							.name()));
		}

	}

	private void loadCliente(Long idCliente) {
		ClienteBO clienteBO = AbstractFactory.getInstance(ClienteBO.class);
		try {
			this.cliente = clienteBO.obtener(idCliente);
			WModel model = populateModel();
			model.addValue(CAMPO_RAZON_SOCIAL, cliente.getRazonSocial());
			model.addValue(CAMPO_DIRECCION, cliente.getDireccion());
			model.addValue(CAMPO_TELEFONO, cliente.getTelefono());
			model.addValue(CAMPO_LOCALIDAD, cliente.getLocalidad().getId());
			model.addValue(CAMPO_NUMERO_DOC, cliente.getDocumento());
			model.addValue(CAMPO_NUMERO_DOC2, cliente.getDocumento());
			model.addValue(CAMPO_TIPO_DOC, cliente.getTipoDocumento().getId());
			model.addValue(CAMPO_CELULAR, cliente.getCelular());
			model.addValue(CAMPO_TIPO_IVA, cliente.getCondicionIVA().getId());
			populateComponents(model);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	public ClienteVerIFrame(ClienteIFrame clienteIFrame) {
		initialize("Nuevo Cliente");
		this.clienteIFrame = clienteIFrame;
		this.cliente = new Cliente();
		loadTiposDocumento();
	}

	private void initialize(String title) {
		setTitle(title);
		setBorder(new LineBorder(null, 1, true));
		setFrameIcon(new ImageIcon(
				ClienteVerIFrame.class.getResource("/icons/cliente.png")));
		setBounds(0, 0, 445, 401);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getBtnCerrar());
		getContentPane().add(getBtnGuardar());
		loadLocalidad();
		loadTiposIva();
	}

	private void loadTiposIva() {
		CondicionIVA[] condiciones = CondicionIVA.values();
		if (WUtils.isNotEmpty(condiciones)) {
			for (CondicionIVA condicionIVA : condiciones) {
				getCmbTipoIva().addItem(
						new WOption((long) condicionIVA.getId(), condicionIVA
								.getDenominacion()));
			}
		}
	}

	private void loadLocalidad() {

		getCmbLocalidad().removeAllItems();
		getCmbLocalidad().addItem(WOption.getWOptionSelecione());

		List<Localidad> localidades = null;
		try {
			ParametricoBO parametricoBO = AbstractFactory
					.getInstance(ParametricoBO.class);
			localidades = parametricoBO.obtenerTodosLocalidades();
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}

		if (WUtils.isNotEmpty(localidades)) {
			for (Localidad localidad : localidades) {
				getCmbLocalidad().addItem(
						new WOption(localidad.getId(), localidad.getNombre()));
			}
		}
	}

	@Override
	protected boolean validateModel(WModel model) {

		String razonSocial = model.getValue(CAMPO_RAZON_SOCIAL);
		String direccion = model.getValue(CAMPO_DIRECCION);
		String telefono = model.getValue(CAMPO_TELEFONO);
		WOption localidad = model.getValue(CAMPO_LOCALIDAD);
		String documento = model.getValue(CAMPO_NUMERO_DOC);
		String documento2 = model.getValue(CAMPO_NUMERO_DOC2);
		WOption tipoIva = model.getValue(CAMPO_TIPO_IVA);
		WOption tipoDoc = model.getValue(CAMPO_TIPO_DOC);

		List<String> messages = new ArrayList<String>();

		if (WUtils.isEmpty(razonSocial)) {
			messages.add("Debe ingresar una Razón Social");
		}

		TipoDocumento tipoDocumento = TipoDocumento.fromValue(tipoDoc
				.getValue().intValue());

		if (tipoDocumento.equals(TipoDocumento.CUIL)
				|| tipoDocumento.equals(TipoDocumento.CUIT)) {
			if (!AppUtils.esValidoCUIT(documento)) {
				messages.add("Debe ingresar un Documento válido");
			}
		} else {
			if (WUtils.isEmpty(documento2)) {
				messages.add("Debe ingresar un Documento válido");
			}
		}

		if (WUtils.isEmpty(direccion)) {
			messages.add("Debe ingresar una Dirección");
		}

		if (WUtils.isEmpty(telefono)) {
			messages.add("Debe ingresar un Teléfono");
		}

		if (WOption.getIdWOptionSeleccion().equals(localidad.getValue())) {
			messages.add("Debe seleccionar una Localidad");
		}

		if (WOption.getIdWOptionSeleccion().equals(tipoIva.getValue())) {
			messages.add("Debe seleccionar un Tipo de Iva");
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
			pnlBusqueda.setBounds(10, 11, 421, 313);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getLblRazonSocial());
			pnlBusqueda.add(getTxtRazonSocial());
			pnlBusqueda.add(getLblDireccion());
			pnlBusqueda.add(getLblCelular());
			pnlBusqueda.add(getTxtDireccion());
			pnlBusqueda.add(getTxtTelefono());
			pnlBusqueda.add(getTxfCuit());
			pnlBusqueda.add(getLblDocumento());
			pnlBusqueda.add(getLblLocalidad());
			pnlBusqueda.add(getTxtCelular());
			pnlBusqueda.add(getLblTipoIva());
			pnlBusqueda.add(getCmbLocalidad());
			pnlBusqueda.add(getCmbTipoIva());
			pnlBusqueda.add(getLblTelefono());
			pnlBusqueda.add(getLabel());
			pnlBusqueda.add(getCmbTipoDoc());
			pnlBusqueda.add(getLblTipoDoc());
			pnlBusqueda.add(getTxtDNIOtros());
		}
		return pnlBusqueda;
	}

	private JLabel getLblRazonSocial() {
		if (lblRazonSocial == null) {
			lblRazonSocial = new JLabel("* Raz\u00F3n Social:");
			lblRazonSocial.setHorizontalAlignment(SwingConstants.RIGHT);
			lblRazonSocial.setBounds(10, 23, 121, 25);
		}
		return lblRazonSocial;
	}

	private JTextField getTxtRazonSocial() {
		if (txtRazonSocial == null) {
			txtRazonSocial = new JTextField();
			txtRazonSocial.setName(CAMPO_RAZON_SOCIAL);
			txtRazonSocial.setDocument(new WTextFieldLimit(50));
			txtRazonSocial.setBounds(141, 23, 263, 25);
		}
		return txtRazonSocial;
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
			btnCerrar.setIcon(new ImageIcon(ClienteVerIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(217, 335, 103, 30);
		}
		return btnCerrar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setIcon(new ImageIcon(ClienteVerIFrame.class
					.getResource("/icons/ok.png")));
			btnGuardar.setBounds(330, 335, 103, 30);
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					WModel model = populateModel();
					if (validateModel(model)) {

						String razonSocial = model.getValue(CAMPO_RAZON_SOCIAL);
						String direccion = model.getValue(CAMPO_DIRECCION);
						String telefono = model.getValue(CAMPO_TELEFONO);
						WOption localidad = model.getValue(CAMPO_LOCALIDAD);
						String documento = model.getValue(CAMPO_NUMERO_DOC);
						String documento2 = model.getValue(CAMPO_NUMERO_DOC2);
						WOption tipoDoc = model.getValue(CAMPO_TIPO_DOC);
						String celular = model.getValue(CAMPO_CELULAR);
						WOption tipoIva = model.getValue(CAMPO_TIPO_IVA);

						cliente.setRazonSocial(razonSocial);
						cliente.setDireccion(direccion);
						cliente.setTelefono(telefono);
						cliente.setIdLocalidad(localidad.getValue());
						cliente.setDocumento(documento);
						cliente.setTipoDocumento(TipoDocumento
								.fromValue(tipoDoc.getValue().intValue()));
						cliente.setCelular(celular);
						cliente.setCondicionIVA(CondicionIVA.fromValue(tipoIva
								.getValue().intValue()));

						cliente.setDocumento((WUtils.isNotEmpty(documento) && !"##-########-#"
								.equals(documento)) ? documento : documento2);

						ClienteBO clienteBO = AbstractFactory
								.getInstance(ClienteBO.class);
						try {
							if (cliente.getId() == null) {
								clienteBO.guardar(cliente);
							} else {
								clienteBO.actualizar(cliente);
							}
							hideFrame();
							clienteIFrame.search();
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
		return getTxtRazonSocial();
	}

	private JLabel getLblDireccion() {
		if (lblDireccion == null) {
			lblDireccion = new JLabel("* Direcci\u00F3n:");
			lblDireccion.setHorizontalAlignment(SwingConstants.RIGHT);
			lblDireccion.setBounds(10, 131, 121, 25);
		}
		return lblDireccion;
	}

	private JLabel getLblCelular() {
		if (lblCelular == null) {
			lblCelular = new JLabel("Celular:");
			lblCelular.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCelular.setBounds(10, 203, 121, 25);
		}
		return lblCelular;
	}

	private JTextField getTxtDireccion() {
		if (txtDireccion == null) {
			txtDireccion = new JTextField();
			txtDireccion.setBounds(141, 131, 263, 25);
			txtDireccion.setDocument(new WTextFieldLimit(100));
			txtDireccion.setName(CAMPO_DIRECCION);
		}
		return txtDireccion;
	}

	private JTextField getTxtTelefono() {
		if (txtTelefono == null) {
			txtTelefono = new JTextField();
			txtTelefono.setBounds(141, 167, 145, 25);
			txtTelefono.setDocument(new WTextFieldLimit(20));
			txtTelefono.setName(CAMPO_TELEFONO);
		}
		return txtTelefono;
	}

	private JFormattedTextField getTxfCuit() {
		if (txfCuit == null) {
			MaskFormatter formatter = null;
			try {
				formatter = new MaskFormatter("##-########-#");
				formatter.setPlaceholderCharacter('#');
			} catch (java.text.ParseException exc) {
			}

			txfCuit = new JFormattedTextField(formatter);
			txfCuit.setName(CAMPO_NUMERO_DOC);

			txfCuit.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {

					if (txfCuit.getText().length() == 13
							&& !txfCuit.getText().contains("#")) {
						if (AppUtils.esValidoCUIT(txfCuit.getText())) {
							label.setIcon(new ImageIcon(ClienteVerIFrame.class
									.getResource("/icons/ok.png")));
						} else {
							label.setIcon(new ImageIcon(ClienteVerIFrame.class
									.getResource("/icons/error.png")));
						}
					} else {
						label.setIcon(null);
					}

				}
			});
			txfCuit.setBounds(141, 95, 145, 25);
		}
		return txfCuit;
	}

	private JLabel getLblDocumento() {
		if (lblDocumento == null) {
			lblDocumento = new JLabel("* Documento:");
			lblDocumento.setHorizontalAlignment(SwingConstants.RIGHT);
			lblDocumento.setBounds(10, 95, 121, 25);
		}
		return lblDocumento;
	}

	private JLabel getLblLocalidad() {
		if (lblLocalidad == null) {
			lblLocalidad = new JLabel("* Localidad:");
			lblLocalidad.setHorizontalAlignment(SwingConstants.RIGHT);
			lblLocalidad.setBounds(10, 239, 121, 25);
		}
		return lblLocalidad;
	}

	private JTextField getTxtCelular() {
		if (txtCelular == null) {
			txtCelular = new JTextField();
			txtCelular.setName(CAMPO_CELULAR);
			txtCelular.setBounds(141, 203, 145, 25);
			txtCelular.setDocument(new WTextFieldLimit(30));
		}
		return txtCelular;
	}

	private JLabel getLblTipoIva() {
		if (lblTipoIva == null) {
			lblTipoIva = new JLabel("* Cond. IVA:");
			lblTipoIva.setHorizontalAlignment(SwingConstants.RIGHT);
			lblTipoIva.setBounds(10, 275, 121, 25);
		}
		return lblTipoIva;
	}

	private JComboBox getCmbLocalidad() {
		if (cmbLocalidad == null) {
			cmbLocalidad = new JComboBox();
			cmbLocalidad.setName(CAMPO_LOCALIDAD);
			cmbLocalidad.setBounds(141, 239, 164, 25);
		}
		return cmbLocalidad;
	}

	private JComboBox getCmbTipoIva() {
		if (cmbTipoIva == null) {
			cmbTipoIva = new JComboBox();
			cmbTipoIva.setName(CAMPO_TIPO_IVA);
			cmbTipoIva.setBounds(141, 275, 247, 25);
		}
		return cmbTipoIva;
	}

	private JLabel getLblTelefono() {
		if (lblTelefono == null) {
			lblTelefono = new JLabel("* Tel\u00E9fono:");
			lblTelefono.setHorizontalAlignment(SwingConstants.RIGHT);
			lblTelefono.setBounds(10, 167, 121, 25);
		}
		return lblTelefono;
	}

	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel("");
			label.setBounds(296, 95, 22, 25);
		}
		return label;
	}

	private JComboBox getCmbTipoDoc() {
		if (cmbTipoDoc == null) {
			cmbTipoDoc = new JComboBox();
			cmbTipoDoc.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {

						getTxfCuit().setVisible(Boolean.FALSE);
						getTxtDNIOtros().setVisible(Boolean.FALSE);
						label.setIcon(null);

						WOption selectedOption = (WOption) e.getItem();
						TipoDocumento tipoDocumento = TipoDocumento
								.fromValue(selectedOption.getValue().intValue());
						if (tipoDocumento.equals(TipoDocumento.CUIL)
								|| tipoDocumento.equals(TipoDocumento.CUIT)) {
							getTxtDNIOtros().setText("");
							getTxfCuit().setVisible(Boolean.TRUE);
						} else {
							getTxfCuit().setText("");
							getTxtDNIOtros().setVisible(Boolean.TRUE);
						}
					}
				}
			});
			cmbTipoDoc.setName(CAMPO_TIPO_DOC);
			cmbTipoDoc.setBounds(141, 59, 164, 25);
		}
		return cmbTipoDoc;
	}

	private JLabel getLblTipoDoc() {
		if (lblTipoDoc == null) {
			lblTipoDoc = new JLabel("* Tipo Doc.:");
			lblTipoDoc.setHorizontalAlignment(SwingConstants.RIGHT);
			lblTipoDoc.setBounds(10, 59, 121, 25);
		}
		return lblTipoDoc;
	}

	private WTextFieldNumeric getTxtDNIOtros() {
		if (txtDNIOtros == null) {
			txtDNIOtros = new WTextFieldNumeric();
			txtDNIOtros.setName(CAMPO_NUMERO_DOC2);
			txtDNIOtros.setDocument(new WTextFieldLimit(10));
			txtDNIOtros.setBounds(141, 95, 145, 25);
		}
		return txtDNIOtros;
	}
}
