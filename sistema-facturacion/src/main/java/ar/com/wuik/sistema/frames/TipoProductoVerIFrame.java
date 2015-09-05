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

import ar.com.wuik.sistema.bo.TipoProductoBO;
import ar.com.wuik.sistema.entities.TipoProducto;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;

public class TipoProductoVerIFrame extends WAbstractModelIFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8951162992767740069L;
	private JButton btnCancelar;
	private JButton btnGuardar;
	private JPanel panelDatos;

	private static final String CAMPO_DESCRIPCION = "descripcion";
	private TipoProductoIFrame tipoProductoIFrame;
	private ProductoVerIFrame productoVerIFrame;
	private TipoProducto tipoProducto;
	private JLabel lblDescripcion;
	private JTextField txtDescripcion;

	/**
	 * @wbp.parser.constructor
	 */
	public TipoProductoVerIFrame(TipoProductoIFrame tipoProductoIFrame) {
		initializate("Nuevo Tipo de Producto");
		this.tipoProductoIFrame = tipoProductoIFrame;
		this.tipoProducto = new TipoProducto();
	}

	public TipoProductoVerIFrame(Long idTipoProducto,
			TipoProductoIFrame tipoProductoIFrame) {
		initializate("Editar Tipo de Producto");
		try {
			TipoProductoBO tipoProductoBO = AbstractFactory
					.getInstance(TipoProductoBO.class);
			this.tipoProducto = tipoProductoBO.obtener(idTipoProducto);
			this.tipoProductoIFrame = tipoProductoIFrame;
			WModel model = populateModel();
			model.addValue(CAMPO_DESCRIPCION, tipoProducto.getNombre());
			populateComponents(model);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	public TipoProductoVerIFrame(ProductoVerIFrame productoVerIFrame) {
		initializate("Nuevo Tipo de Producto");
		this.productoVerIFrame = productoVerIFrame;
		this.tipoProducto = new TipoProducto();
	}

	private void initializate(String title) {
		setTitle(title);
		setBorder(new LineBorder(null, 1, true));
		setFrameIcon(new ImageIcon(
				ClienteIFrame.class.getResource("/icons/tipos_productos.png")));
		setBounds(0, 0, 411, 150);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getBtnCancelar());
		getContentPane().add(getBtnGuardar());
		getContentPane().add(getPanelDatos());
	}

	@Override
	protected boolean validateModel(WModel model) {
		String descripcion = model.getValue(CAMPO_DESCRIPCION);

		List<String> messages = new ArrayList<String>();

		if (WUtils.isEmpty(descripcion)) {
			messages.add("Debe ingresar una Descripción");
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
			btnCancelar.setIcon(new ImageIcon(TipoProductoVerIFrame.class
					.getResource("/icons/cancel2.png")));
			btnCancelar.setBounds(184, 85, 103, 30);
		}
		return btnCancelar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Guardar");
			btnGuardar.setIcon(new ImageIcon(TipoProductoVerIFrame.class
					.getResource("/icons/ok.png")));
			btnGuardar.setBounds(296, 85, 103, 30);
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
			try {

				TipoProductoBO tipoProductoBO = AbstractFactory
						.getInstance(TipoProductoBO.class);

				String descripcion = model.getValue(CAMPO_DESCRIPCION);

				tipoProducto.setNombre(descripcion);

				if (null == tipoProducto.getId()) {
					tipoProductoBO.guardar(tipoProducto);
				} else {
					tipoProductoBO.actualizar(tipoProducto);
				}

				if (null != tipoProductoIFrame) {
					tipoProductoIFrame.search();
				} else if (null != productoVerIFrame) {
					productoVerIFrame.loadTiposProducto(tipoProducto.getId());
				}
				hideFrame();
			} catch (BusinessException bexc) {
				showGlobalErrorMsg(bexc.getMessage());
			}
		}
	}

	private JPanel getPanelDatos() {
		if (panelDatos == null) {
			panelDatos = new JPanel();
			panelDatos.setBorder(new TitledBorder(null, "Datos",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panelDatos.setBounds(11, 8, 388, 66);
			panelDatos.setLayout(null);
			panelDatos.add(getLblDescripcion());
			panelDatos.add(getTxtDescripcion());
		}
		return panelDatos;
	}

	private JLabel getLblDescripcion() {
		if (lblDescripcion == null) {
			lblDescripcion = new JLabel("* Descripci\u00F3n:");
			lblDescripcion.setHorizontalAlignment(SwingConstants.RIGHT);
			lblDescripcion.setBounds(10, 23, 128, 25);
		}
		return lblDescripcion;
	}

	private JTextField getTxtDescripcion() {
		if (txtDescripcion == null) {
			txtDescripcion = new JTextField();
			txtDescripcion.setName(CAMPO_DESCRIPCION);
			txtDescripcion.setColumns(10);
			txtDescripcion.setDocument(new WTextFieldLimit(50));
			txtDescripcion.setBounds(148, 23, 230, 25);
		}
		return txtDescripcion;
	}
}
