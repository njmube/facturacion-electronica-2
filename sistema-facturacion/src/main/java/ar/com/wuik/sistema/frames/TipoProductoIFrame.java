package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;

import ar.com.wuik.sistema.bo.TipoProductoBO;
import ar.com.wuik.sistema.entities.Permisos;
import ar.com.wuik.sistema.entities.TipoProducto;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.model.TipoProductoModel;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.security.WSecure;
import ar.com.wuik.swing.components.table.WTablePanel;
import ar.com.wuik.swing.components.table.WToolbarButton;
import ar.com.wuik.swing.frames.WAbstractIFrame;
import ar.com.wuik.swing.frames.WCalendarIFrame;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;

/**
 * 
 * @author juan.castro
 * 
 */
public class TipoProductoIFrame extends WAbstractIFrame implements WSecure {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5205039853574138013L;
	private WTablePanel<TipoProducto> tablePanel;
	private JButton btnSalir;

	public TipoProductoIFrame() {
		setBorder(new LineBorder(null, 1, true));
		setTitle("Tipos de Producto");
		setFrameIcon(new ImageIcon(
				TipoProductoIFrame.class.getResource("/icons/tipos_productos.png")));
		setBounds(0, 0, 344, 308);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getTablePanel());
		getContentPane().add(getBtnSalir());
		search();
	}

	@Override
	public void applySecurity(List<String> permisos) {
	}

	public void search() {
		TipoProductoBO tipoProductoBO = AbstractFactory
				.getInstance(TipoProductoBO.class);
		try {
			List<TipoProducto> tiposProducto= tipoProductoBO.obtenerTodos();
			getTablePanel().addData(tiposProducto);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	private WTablePanel<TipoProducto> getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new WTablePanel(TipoProductoModel.class, Boolean.FALSE);
			tablePanel.addToolbarButtons(getToolbarButtons());
			tablePanel.setBounds(10, 11, 322, 221);
		}
		return tablePanel;
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		WToolbarButton buttonAdd = new WToolbarButton("Nuevo Tipo de Producto",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/add.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						addModalIFrame(new TipoProductoVerIFrame(
								TipoProductoIFrame.this));
					}
				}, "Nuevo", Permisos.NUE_CLI.getCodPermiso());
		WToolbarButton buttonEdit = new WToolbarButton("Editar Tipo de Producto",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/edit.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							addModalIFrame(new TipoProductoVerIFrame(selectedItem,
									TipoProductoIFrame.this));
						} else {
							WTooltipUtils.showMessage(
									"Debe seleccionar un Producto",
									(JButton) e.getSource(), MessageType.ALERTA);
						}
					}
				}, "Editar", Permisos.EDI_CLI.getCodPermiso());
		WToolbarButton buttonDelete = new WToolbarButton(
				"Eliminar Tipo de Producto(s)",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/delete.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							int result = JOptionPane
									.showConfirmDialog(
											getParent(),
											"¿Desea eliminar el Producto seleccionado?",
											"Alerta",
											JOptionPane.OK_CANCEL_OPTION,
											JOptionPane.WARNING_MESSAGE);
							if (result == JOptionPane.OK_OPTION) {
								
								TipoProductoBO tipoProductoBO = AbstractFactory
										.getInstance(TipoProductoBO.class);
								try {
									boolean estaEnUso = tipoProductoBO
											.estaEnUso(selectedItem);
									if (!estaEnUso) {
										try {
											tipoProductoBO.eliminar(selectedItem);
											search();
										} catch (BusinessException bexc) {
											showGlobalErrorMsg(bexc
													.getMessage());
										}
									} else {
										WTooltipUtils
												.showMessage(
														"No es posible Eliminar el Tipo de Producto porque está siendo utilizado.",
														(JButton) e.getSource(),
														MessageType.ALERTA);
									}
								} catch (BusinessException bexc) {
									showGlobalErrorMsg(bexc.getMessage());
								}
							}
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar al menos un Tipo de Producto",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Eliminar", Permisos.ELI_CLI.getCodPermiso());

		toolbarButtons.add(buttonAdd);
		toolbarButtons.add(buttonEdit);
		toolbarButtons.add(buttonDelete);
		return toolbarButtons;
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Salir");
			btnSalir.setIcon(new ImageIcon(TipoProductoIFrame.class
					.getResource("/icons/cancel.png")));
			btnSalir.setBounds(241, 243, 91, 25);
			btnSalir.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					hideFrame();
				}
			});
		}
		return btnSalir;
	}

}
