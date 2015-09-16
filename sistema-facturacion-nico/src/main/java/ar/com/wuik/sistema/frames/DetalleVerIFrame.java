package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ar.com.wuik.sistema.entities.DetalleComprobante;
import ar.com.wuik.sistema.entities.enums.TipoIVAEnum;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WOption;
import ar.com.wuik.swing.components.WTextFieldDecimal;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;

public class DetalleVerIFrame extends WAbstractModelIFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8951162992767740069L;
	private JButton btnCancelar;
	private JButton btnGuardar;
	private JPanel panelDatos;
	private JTextField txtDescripcion;
	private JLabel lblDescripcion;

	private static final String CAMPO_IVA = "iva";
	private static final String CAMPO_PRECIO = "precio";
	private static final String CAMPO_DESCRIPCION = "descripcion";
	private ComprobanteVerIFrame comprobanteVerIFrame;
	private RemitoVerIFrame remitoClienteVerIFrame;
	private JLabel lblIVA;
	private JComboBox cmbTipoIva;
	private JLabel lblPrecio;
	private WTextFieldDecimal txfPrecio;

	/**
	 * @wbp.parser.constructor
	 */
	public DetalleVerIFrame(ComprobanteVerIFrame comprobanteVerIFrame) {
		initializate("Nuevo Detalle");
		this.comprobanteVerIFrame = comprobanteVerIFrame;
	}

	public DetalleVerIFrame(RemitoVerIFrame remitoClienteVerIFrame) {
		initializate("Nuevo Detalle");
		this.remitoClienteVerIFrame = remitoClienteVerIFrame;
	}

	private void initializate(String title) {
		setTitle(title);
		setBorder(new LineBorder(null, 1, true));
		setFrameIcon(new ImageIcon(
				ClienteIFrame.class.getResource("/icons/productos.png")));
		setBounds(0, 0, 411, 220);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getBtnCancelar());
		getContentPane().add(getBtnGuardar());
		getContentPane().add(getPanelDatos());
		loadTiposIva();
	}

	private void loadTiposIva() {

		TipoIVAEnum[] tiposIVAEnum = TipoIVAEnum.values();
		for (TipoIVAEnum tipoIVAEnum : tiposIVAEnum) {
			getCmbTipoIva().addItem(
					new WOption((long) tipoIVAEnum.getId(), tipoIVAEnum
							.getDescripcion()));
		}
		getCmbTipoIva().setSelectedItem(
				new WOption((long) TipoIVAEnum.IVA_21.getId()));
	}

	@Override
	protected boolean validateModel(WModel model) {
		String descripcion = model.getValue(CAMPO_DESCRIPCION);
		String precio = model.getValue(CAMPO_PRECIO);

		List<String> messages = new ArrayList<String>();

		if (WUtils.isEmpty(descripcion)) {
			messages.add("Debe ingresar una Descripción");
		}

		if (WUtils.isEmpty(precio)) {
			messages.add("Debe ingresar un Precio");
		}

		WTooltipUtils.showMessages(messages, btnGuardar, MessageType.ERROR);

		return WUtils.isEmpty(messages);
	}

	@Override
	protected JComponent getFocusComponent() {
		return getTxtDescripcion();
	}

	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					hideFrame();
				}
			});
			btnCancelar.setIcon(new ImageIcon(DetalleVerIFrame.class
					.getResource("/icons/cancel2.png")));
			btnCancelar.setBounds(184, 156, 103, 30);
		}
		return btnCancelar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setIcon(new ImageIcon(DetalleVerIFrame.class
					.getResource("/icons/ok.png")));
			btnGuardar.setBounds(296, 156, 103, 30);
			btnGuardar.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					guardar();
				}
			});
		}
		return btnGuardar;
	}

	private void guardar() {
		WModel model = populateModel();
		if (validateModel(model)) {

			String descripcion = model.getValue(CAMPO_DESCRIPCION);
			WOption tipoIva = model.getValue(CAMPO_IVA);
			String precioTxt = model.getValue(CAMPO_PRECIO);

			if (null != comprobanteVerIFrame) {
				DetalleComprobante detalle = new DetalleComprobante();
				detalle.setCantidad(BigDecimal.ONE);
				detalle.setDetalle(descripcion);
				detalle.setTipoIVA(TipoIVAEnum.fromValue(tipoIva.getValue()
						.intValue()));
				detalle.setTemporalId(System.currentTimeMillis());

				BigDecimal importeDecimal = BigDecimal.ONE.subtract(detalle
						.getTipoIVA().getImporteDecimal());

				BigDecimal totalConIVA = WUtils.getValue(precioTxt);

				BigDecimal total = totalConIVA.multiply(importeDecimal);

				detalle.setPrecio(WUtils.getValue(total));

				comprobanteVerIFrame.addDetalle(detalle);
			} else if (null != remitoClienteVerIFrame) {
				remitoClienteVerIFrame.search();
			}
			hideFrame();
		}
	}

	private JPanel getPanelDatos() {
		if (panelDatos == null) {
			panelDatos = new JPanel();
			panelDatos.setBorder(new TitledBorder(null, "Datos",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelDatos.setBounds(11, 8, 388, 137);
			panelDatos.setLayout(null);
			panelDatos.add(getTxtDescripcion());
			panelDatos.add(getLblDescripcion());
			panelDatos.add(getLblIVA());
			panelDatos.add(getCmbTipoIva());
			panelDatos.add(getLblPrecio());
			panelDatos.add(getTxfPrecio());
		}
		return panelDatos;
	}

	private JTextField getTxtDescripcion() {
		if (txtDescripcion == null) {
			txtDescripcion = new JTextField();
			txtDescripcion.setColumns(10);
			txtDescripcion.setBounds(148, 27, 230, 25);
			txtDescripcion.setName(CAMPO_DESCRIPCION);
			txtDescripcion.setDocument(new WTextFieldLimit(50));
		}
		return txtDescripcion;
	}

	private JLabel getLblDescripcion() {
		if (lblDescripcion == null) {
			lblDescripcion = new JLabel("* Descripci\u00F3n:");
			lblDescripcion.setHorizontalAlignment(SwingConstants.RIGHT);
			lblDescripcion.setBounds(10, 27, 128, 25);
		}
		return lblDescripcion;
	}

	private JLabel getLblIVA() {
		if (lblIVA == null) {
			lblIVA = new JLabel("* IVA: %");
			lblIVA.setHorizontalAlignment(SwingConstants.RIGHT);
			lblIVA.setBounds(10, 63, 128, 25);
		}
		return lblIVA;
	}

	private JComboBox getCmbTipoIva() {
		if (cmbTipoIva == null) {
			cmbTipoIva = new JComboBox();
			cmbTipoIva.setName(CAMPO_IVA);
			cmbTipoIva.setBounds(148, 63, 125, 25);
		}
		return cmbTipoIva;
	}

	private JLabel getLblPrecio() {
		if (lblPrecio == null) {
			lblPrecio = new JLabel("* Precio: $");
			lblPrecio.setHorizontalAlignment(SwingConstants.RIGHT);
			lblPrecio.setBounds(10, 99, 128, 25);
		}
		return lblPrecio;
	}

	private WTextFieldDecimal getTxfPrecio() {
		if (txfPrecio == null) {
			txfPrecio = new WTextFieldDecimal(7, 3);
			txfPrecio.setName(CAMPO_PRECIO);
			txfPrecio.setBounds(148, 99, 125, 25);
		}
		return txfPrecio;
	}

	@Override
	public void enterPressed() {
		getBtnGuardar().doClick();
	}
}
