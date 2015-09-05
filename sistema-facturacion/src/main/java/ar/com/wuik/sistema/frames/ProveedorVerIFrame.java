package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import ar.com.wuik.sistema.bo.ParametricoBO;
import ar.com.wuik.sistema.bo.ProveedorBO;
import ar.com.wuik.sistema.entities.Localidad;
import ar.com.wuik.sistema.entities.Proveedor;
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

public class ProveedorVerIFrame extends WAbstractModelIFrame {
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6838619883125511589L;
	private JPanel pnlBusqueda;
	private JLabel lblRazonSocial;
	private JTextField txtRazonSocial;
	private JButton btnCerrar;
	private Proveedor proveedor;
	private JButton btnGuardar;
	private JLabel lblDireccion;
	private JLabel lblTelefono;

	private static final String CAMPO_RAZON_SOCIAL = "razonSocial";
	private static final String CAMPO_DIRECCION = "direccion";
	private static final String CAMPO_TELEFONO = "telefono";
	private static final String CAMPO_LOCALIDAD = "localidad";
	private static final String CAMPO_CUIT = "cuit";
	private static final String CAMPO_MAIL = "mail";
	private ProveedorIFrame proveedorIFrame;
	private MovimientoProductoVerIFrame movimientoProductoVerIFrame;
	private JTextField txtDireccion;
	private JTextField txtTelefono;
	private JFormattedTextField txfCuit;
	private JLabel lblCuit;
	private JLabel lblLocalidad;
	private JTextField txtMail;
	private JComboBox cmbLocalidad;
	private JLabel lblTelfono;
	private JLabel label;

	/**
	 * Create the frame.
	 * 
	 * @wbp.parser.constructor
	 */
	public ProveedorVerIFrame(Long idProveedor, ProveedorIFrame proveedorIFrame) {
		initialize("Editar Proveedor");
		this.proveedorIFrame = proveedorIFrame;
		loadProveedor(idProveedor);
	}

	private void loadProveedor(Long idProveedor) {
		ProveedorBO proveedorBO = AbstractFactory
				.getInstance(ProveedorBO.class);
		try {
			this.proveedor = proveedorBO.obtener(idProveedor);
			WModel model = populateModel();
			model.addValue(CAMPO_RAZON_SOCIAL, proveedor.getRazonSocial());
			model.addValue(CAMPO_CUIT, proveedor.getCuit());
			model.addValue(CAMPO_DIRECCION, proveedor.getDireccion());
			model.addValue(CAMPO_TELEFONO, proveedor.getTelefono());
			model.addValue(CAMPO_MAIL, proveedor.getMail());
			model.addValue(CAMPO_LOCALIDAD, proveedor.getLocalidad().getId());
			populateComponents(model);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	public ProveedorVerIFrame(ProveedorIFrame proveedorIFrame) {
		initialize("Nuevo Proveedor");
		this.proveedorIFrame = proveedorIFrame;
		this.proveedor = new Proveedor();
	}

	public ProveedorVerIFrame(
			MovimientoProductoVerIFrame movimientoProductoVerIFrame) {
		initialize("Nuevo Proveedor");
		this.movimientoProductoVerIFrame = movimientoProductoVerIFrame;
		this.proveedor = new Proveedor();
	}

	private void initialize(String title) {
		setTitle(title);
		setBorder(new LineBorder(null, 1, true));
		setFrameIcon(new ImageIcon(
				ProveedorVerIFrame.class.getResource("/icons/proveedores.png")));
		setBounds(0, 0, 470, 329);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getBtnCerrar());
		getContentPane().add(getBtnGuardar());
		loadLocalidad();
	}

	private void loadLocalidad() {

		getCmbLocalidad().addItem(WOption.getWOptionSelecione());

		try {
			ParametricoBO parametricoBO = AbstractFactory
					.getInstance(ParametricoBO.class);
			List<Localidad> localidades = parametricoBO
					.obtenerTodosLocalidades();
			if (WUtils.isNotEmpty(localidades)) {
				for (Localidad localidad : localidades) {
					getCmbLocalidad().addItem(
							new WOption(localidad.getId(), localidad
									.getNombre()));
				}
			}
			getCmbLocalidad().setSelectedItem(new WOption(105L));
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}

	}

	@Override
	protected boolean validateModel(WModel model) {

		String razonSocial = model.getValue(CAMPO_RAZON_SOCIAL);
		WOption localidad = model.getValue(CAMPO_LOCALIDAD);
		String cuit = model.getValue(CAMPO_CUIT);

		List<String> messages = new ArrayList<String>();

		if (WUtils.isEmpty(razonSocial)) {
			messages.add("Debe ingresar una Razón Social");
		}

		if (!AppUtils.esValidoCUIT(cuit)) {
			messages.add("Debe ingresar un Cuit válido");
		}

		if (WOption.getIdWOptionSeleccion().equals(localidad.getValue())) {
			messages.add("Debe seleccionar una Localidad");
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
			pnlBusqueda.setBounds(10, 11, 448, 242);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getLblRazonSocial());
			pnlBusqueda.add(getTxtRazonSocial());
			pnlBusqueda.add(getLblDireccion());
			pnlBusqueda.add(getLblTelefono());
			pnlBusqueda.add(getTxtDireccion());
			pnlBusqueda.add(getTxtTelefono());
			pnlBusqueda.add(getTxfCuit());
			pnlBusqueda.add(getLblCuit());
			pnlBusqueda.add(getLblLocalidad());
			pnlBusqueda.add(getTxtMail());
			pnlBusqueda.add(getCmbLocalidad());
			pnlBusqueda.add(getLblTelfono());
			pnlBusqueda.add(getLabel());
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
			btnCerrar.setIcon(new ImageIcon(ProveedorVerIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(242, 264, 103, 30);
		}
		return btnCerrar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setIcon(new ImageIcon(ClienteVerIFrame.class
					.getResource("/icons/ok.png")));
			btnGuardar.setBounds(355, 264, 103, 30);
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					WModel model = populateModel();
					if (validateModel(model)) {

						String razonSocial = model.getValue(CAMPO_RAZON_SOCIAL);
						String direccion = model.getValue(CAMPO_DIRECCION);
						String telefono = model.getValue(CAMPO_TELEFONO);
						WOption localidad = model.getValue(CAMPO_LOCALIDAD);
						String cuit = model.getValue(CAMPO_CUIT);
						String mail = model.getValue(CAMPO_MAIL);

						proveedor.setRazonSocial(razonSocial);
						proveedor.setDireccion(direccion);
						proveedor.setTelefono(telefono);
						proveedor.setIdLocalidad(localidad.getValue());
						proveedor.setCuit(cuit);
						proveedor.setMail(mail);

						ProveedorBO proveedorBO = AbstractFactory
								.getInstance(ProveedorBO.class);
						try {
							if (proveedor.getId() == null) {
								proveedorBO.guardar(proveedor);
							} else {
								proveedorBO.actualizar(proveedor);
							}
							if (null != proveedorIFrame) {
								proveedorIFrame.search();
							} else if (null != movimientoProductoVerIFrame) {
								movimientoProductoVerIFrame.loadProveedores(proveedor.getId());
							}
							hideFrame();
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
			lblDireccion = new JLabel("Direcci\u00F3n:");
			lblDireccion.setHorizontalAlignment(SwingConstants.RIGHT);
			lblDireccion.setBounds(10, 95, 121, 25);
		}
		return lblDireccion;
	}

	private JLabel getLblTelefono() {
		if (lblTelefono == null) {
			lblTelefono = new JLabel("Mail:");
			lblTelefono.setHorizontalAlignment(SwingConstants.RIGHT);
			lblTelefono.setBounds(10, 167, 121, 25);
		}
		return lblTelefono;
	}

	private JTextField getTxtDireccion() {
		if (txtDireccion == null) {
			txtDireccion = new JTextField();
			txtDireccion.setBounds(141, 95, 297, 25);
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
				System.err.println("formatter is bad: " + exc.getMessage());
				System.exit(-1);
			}
			txfCuit = new JFormattedTextField(formatter);
			txfCuit.setName(CAMPO_CUIT);
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

	private JTextField getTxtMail() {
		if (txtMail == null) {
			txtMail = new JTextField();
			txtMail.setName(CAMPO_MAIL);
			txtMail.setBounds(141, 167, 219, 25);
			txtMail.setDocument(new WTextFieldLimit(50, Boolean.TRUE));
		}
		return txtMail;
	}

	private JComboBox getCmbLocalidad() {
		if (cmbLocalidad == null) {
			cmbLocalidad = new JComboBox();
			cmbLocalidad.setName(CAMPO_LOCALIDAD);
			cmbLocalidad.setBounds(141, 203, 219, 25);
		}
		return cmbLocalidad;
	}

	private JLabel getLblTelfono() {
		if (lblTelfono == null) {
			lblTelfono = new JLabel("Tel\u00E9fono:");
			lblTelfono.setHorizontalAlignment(SwingConstants.RIGHT);
			lblTelfono.setBounds(10, 131, 121, 25);
		}
		return lblTelfono;
	}
	private JLabel getLabel() {
		if (label == null) {
			label = new JLabel("");
			label.setBounds(296, 59, 26, 25);
		}
		return label;
	}
}
