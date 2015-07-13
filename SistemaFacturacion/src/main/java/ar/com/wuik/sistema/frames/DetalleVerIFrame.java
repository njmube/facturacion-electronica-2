package ar.com.wuik.sistema.frames;

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

import ar.com.wuik.sistema.entities.DetalleFactura;
import ar.com.wuik.sistema.entities.DetalleNotaCredito;
import ar.com.wuik.sistema.entities.DetalleNotaDebito;
import ar.com.wuik.swing.components.WModel;
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
	private FacturaVerIFrame ventaClienteVerIFrame;
	private RemitoClienteVerIFrame remitoClienteVerIFrame;
	private NotaCreditoVerIFrame notaCreditoVerIFrame;
	private NotaDebitoVerIFrame notaDebitoVerIFrame;
	private JLabel lblIVA;
	private WTextFieldDecimal txfIVA;
	private JLabel lblPrecio;
	private WTextFieldDecimal txfPrecio;

	public DetalleVerIFrame(FacturaVerIFrame ventaClienteVerIFrame) {
		initializate("Nuevo Detalle");
		this.ventaClienteVerIFrame = ventaClienteVerIFrame;
	}

	public DetalleVerIFrame(RemitoClienteVerIFrame remitoClienteVerIFrame) {
		initializate("Nuevo Detalle");
		this.remitoClienteVerIFrame = remitoClienteVerIFrame;
	}

	public DetalleVerIFrame(NotaCreditoVerIFrame notaCreditoVerIFrame) {
		initializate("Nuevo Detalle");
		this.notaCreditoVerIFrame = notaCreditoVerIFrame;
	}
	
	public DetalleVerIFrame(NotaDebitoVerIFrame notaDebitoVerIFrame) {
		initializate("Nuevo Detalle");
		this.notaDebitoVerIFrame = notaDebitoVerIFrame;
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
	}

	@Override
	protected boolean validateModel(WModel model) {
		String descripcion = model.getValue(CAMPO_DESCRIPCION);
		String iva = model.getValue(CAMPO_IVA);
		String precio = model.getValue(CAMPO_PRECIO);

		List<String> messages = new ArrayList<String>();

		if (WUtils.isEmpty(descripcion)) {
			messages.add("Debe ingresar una Descripción");
		}

		if (WUtils.isEmpty(iva)) {
			messages.add("Debe ingresar % de IVA");
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
			btnCancelar.setBounds(184, 156, 103, 25);
		}
		return btnCancelar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setIcon(new ImageIcon(DetalleVerIFrame.class
					.getResource("/icons/ok.png")));
			btnGuardar.setBounds(296, 156, 103, 25);
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
			String iva = model.getValue(CAMPO_IVA);
			String precio = model.getValue(CAMPO_PRECIO);

			if (null != ventaClienteVerIFrame) {
				DetalleFactura detalle = new DetalleFactura();
				detalle.setCantidad(1);
				detalle.setDetalle(descripcion);
				detalle.setIva(WUtils.getValue(iva));
				detalle.setPrecio(WUtils.getValue(precio));
				detalle.setTemporalId(System.currentTimeMillis());
				ventaClienteVerIFrame.addDetalle(detalle);
			} else if (null != remitoClienteVerIFrame) {
				remitoClienteVerIFrame.search();
			} else if (null != notaCreditoVerIFrame) {
				DetalleNotaCredito detalle = new DetalleNotaCredito();
				detalle.setCantidad(1);
				detalle.setDetalle(descripcion);
				detalle.setIva(WUtils.getValue(iva));
				detalle.setPrecio(WUtils.getValue(precio));
				detalle.setTemporalId(System.currentTimeMillis());
				notaCreditoVerIFrame.addDetalle(detalle);
			} else if (null != notaDebitoVerIFrame){
				DetalleNotaDebito detalle = new DetalleNotaDebito();
				detalle.setCantidad(1);
				detalle.setDetalle(descripcion);
				detalle.setIva(WUtils.getValue(iva));
				detalle.setPrecio(WUtils.getValue(precio));
				detalle.setTemporalId(System.currentTimeMillis());
				notaDebitoVerIFrame.addDetalle(detalle);
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
			panelDatos.add(getTxfIVA());
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

	private WTextFieldDecimal getTxfIVA() {
		if (txfIVA == null) {
			txfIVA = new WTextFieldDecimal(7, 3);
			txfIVA.setName(CAMPO_IVA);
			txfIVA.setBounds(148, 63, 125, 25);
		}
		return txfIVA;
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
}
