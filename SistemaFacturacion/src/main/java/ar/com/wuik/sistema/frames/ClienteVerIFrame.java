package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import ar.com.wuik.sistema.entities.CondicionIVA;
import ar.com.wuik.sistema.entities.Localidad;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.sistema.utils.AppUtils;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WOption;
import ar.com.wuik.swing.components.WTextFieldLimit;
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
	private static final String CAMPO_CUIT = "cuit";
	private static final String CAMPO_TIPO_IVA = "tipoIva";
	private ClienteIFrame clienteIFrame;
	private JTextField txtDireccion;
	private JTextField txtTelefono;
	private JFormattedTextField txfCuit;
	private JLabel lblCuit;
	private JLabel lblLocalidad;
	private JTextField txtCelular;
	private JLabel lblTipoIva;
	private JComboBox cmbLocalidad;
	private JComboBox cmbTipoIva;
	private JLabel lblTelefono;

	/**
	 * Create the frame.
	 * 
	 * @wbp.parser.constructor
	 */
	public ClienteVerIFrame(Long idCliente, ClienteIFrame clienteIFrame) {
		initialize("Editar Cliente");
		this.clienteIFrame = clienteIFrame;
		loadCliente(idCliente);
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
			model.addValue(CAMPO_CUIT, cliente.getCuit());
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
	}

	private void initialize(String title) {
		setTitle(title);
		setBorder(new LineBorder(null, 1, true));
		setFrameIcon(new ImageIcon(
				ClienteVerIFrame.class.getResource("/icons/clientes.png")));
		setBounds(0, 0, 445, 369);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getBtnCerrar());
		getContentPane().add(getBtnGuardar());
		loadLocalidad();
		loadTiposIva();
	}

	private void loadTiposIva() {
		getCmbTipoIva().addItem(WOption.getWOptionSelecione());
		ParametricoBO parametricoBO = AbstractFactory
				.getInstance(ParametricoBO.class);
		try {
			List<CondicionIVA> condiciones = parametricoBO
					.obtenerTodosCondicionesIVA();
			if (WUtils.isNotEmpty(condiciones)) {
				for (CondicionIVA condicionIVA : condiciones) {
					getCmbTipoIva().addItem(
							new WOption((long) condicionIVA.getId(),
									condicionIVA.getDenominacion()));
				}
			}
		} catch (BusinessException e) {
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
		String cuit = model.getValue(CAMPO_CUIT);
		WOption tipoIva = model.getValue(CAMPO_TIPO_IVA);

		List<String> messages = new ArrayList<String>();

		if (WUtils.isEmpty(razonSocial)) {
			messages.add("Debe ingresar una Razón Social");
		}

		if (!AppUtils.esValidoCUIT(cuit)) {
			messages.add("Debe ingresar un Cuit válido");
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
			pnlBusqueda.setBounds(10, 11, 421, 281);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getLblRazonSocial());
			pnlBusqueda.add(getTxtRazonSocial());
			pnlBusqueda.add(getLblDireccion());
			pnlBusqueda.add(getLblCelular());
			pnlBusqueda.add(getTxtDireccion());
			pnlBusqueda.add(getTxtTelefono());
			pnlBusqueda.add(getTxfCuit());
			pnlBusqueda.add(getLblCuit());
			pnlBusqueda.add(getLblLocalidad());
			pnlBusqueda.add(getTxtCelular());
			pnlBusqueda.add(getLblTipoIva());
			pnlBusqueda.add(getCmbLocalidad());
			pnlBusqueda.add(getCmbTipoIva());
			pnlBusqueda.add(getLblTelefono());
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
			btnCerrar = new JButton("Cerrar");
			btnCerrar.setFocusable(false);
			btnCerrar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					hideFrame();
				}
			});
			btnCerrar.setIcon(new ImageIcon(ClienteVerIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(215, 303, 103, 25);
		}
		return btnCerrar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setIcon(new ImageIcon(ClienteVerIFrame.class
					.getResource("/icons/ok.png")));
			btnGuardar.setBounds(328, 303, 103, 25);
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					WModel model = populateModel();
					if (validateModel(model)) {

						String razonSocial = model.getValue(CAMPO_RAZON_SOCIAL);
						String direccion = model.getValue(CAMPO_DIRECCION);
						String telefono = model.getValue(CAMPO_TELEFONO);
						WOption localidad = model.getValue(CAMPO_LOCALIDAD);
						String cuit = model.getValue(CAMPO_CUIT);
						String celular = model.getValue(CAMPO_CELULAR);
						WOption tipoIva = model.getValue(CAMPO_TIPO_IVA);

						cliente.setRazonSocial(razonSocial);
						cliente.setDireccion(direccion);
						cliente.setTelefono(telefono);
						cliente.setIdLocalidad(localidad.getValue());
						cliente.setCuit(cuit);
						cliente.setCelular(celular);
						;
						cliente.setIdCondicionIVA(tipoIva.getValue());

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
			lblDireccion.setBounds(10, 95, 121, 25);
		}
		return lblDireccion;
	}

	private JLabel getLblCelular() {
		if (lblCelular == null) {
			lblCelular = new JLabel("Celular:");
			lblCelular.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCelular.setBounds(10, 167, 121, 25);
		}
		return lblCelular;
	}

	private JTextField getTxtDireccion() {
		if (txtDireccion == null) {
			txtDireccion = new JTextField();
			txtDireccion.setBounds(141, 95, 263, 25);
			txtDireccion.setDocument(new WTextFieldLimit(100));
			txtDireccion.setName(CAMPO_DIRECCION);
		}
		return txtDireccion;
	}

	private JTextField getTxtTelefono() {
		if (txtTelefono == null) {
			txtTelefono = new JTextField();
			txtTelefono.setBounds(141, 131, 145, 25);
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
			txfCuit.setName(CAMPO_CUIT);
			txfCuit.setBounds(141, 59, 145, 25);
		}
		return txfCuit;
	}

	private JLabel getLblCuit() {
		if (lblCuit == null) {
			lblCuit = new JLabel("* Cuit:");
			lblCuit.setHorizontalAlignment(SwingConstants.RIGHT);
			lblCuit.setBounds(10, 59, 121, 25);
		}
		return lblCuit;
	}

	private JLabel getLblLocalidad() {
		if (lblLocalidad == null) {
			lblLocalidad = new JLabel("* Localidad:");
			lblLocalidad.setHorizontalAlignment(SwingConstants.RIGHT);
			lblLocalidad.setBounds(10, 203, 121, 25);
		}
		return lblLocalidad;
	}

	private JTextField getTxtCelular() {
		if (txtCelular == null) {
			txtCelular = new JTextField();
			txtCelular.setName(CAMPO_CELULAR);
			txtCelular.setBounds(141, 167, 145, 25);
			txtCelular.setDocument(new WTextFieldLimit(50, Boolean.TRUE));
		}
		return txtCelular;
	}

	private JLabel getLblTipoIva() {
		if (lblTipoIva == null) {
			lblTipoIva = new JLabel("* Tipo IVA:");
			lblTipoIva.setHorizontalAlignment(SwingConstants.RIGHT);
			lblTipoIva.setBounds(10, 239, 121, 25);
		}
		return lblTipoIva;
	}

	private JComboBox getCmbLocalidad() {
		if (cmbLocalidad == null) {
			cmbLocalidad = new JComboBox();
			cmbLocalidad.setName(CAMPO_LOCALIDAD);
			cmbLocalidad.setBounds(141, 203, 164, 25);
		}
		return cmbLocalidad;
	}

	private JComboBox getCmbTipoIva() {
		if (cmbTipoIva == null) {
			cmbTipoIva = new JComboBox();
			cmbTipoIva.setName(CAMPO_TIPO_IVA);
			cmbTipoIva.setBounds(141, 239, 163, 25);
		}
		return cmbTipoIva;
	}

	private JLabel getLblTelefono() {
		if (lblTelefono == null) {
			lblTelefono = new JLabel("* Tel\u00E9fono:");
			lblTelefono.setHorizontalAlignment(SwingConstants.RIGHT);
			lblTelefono.setBounds(10, 131, 121, 25);
		}
		return lblTelefono;
	}

}
