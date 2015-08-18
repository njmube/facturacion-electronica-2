package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.border.LineBorder;

import ar.com.wuik.sistema.bo.ProductoBO;
import ar.com.wuik.sistema.entities.StockProducto;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.model.StockProductoModel;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.security.WSecure;
import ar.com.wuik.swing.components.table.WTablePanel;
import ar.com.wuik.swing.components.table.WToolbarButton;
import ar.com.wuik.swing.frames.WAbstractIFrame;
import ar.com.wuik.swing.frames.WCalendarIFrame;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;
import ar.com.wuik.swing.utils.WUtils;

public class MovimientoProductoIFrame extends WAbstractIFrame implements
		WSecure {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3613686692179857008L;
	private WTablePanel<StockProducto> tablePanel;
	private JButton btnSalir;
	private Long idProducto;

	public MovimientoProductoIFrame(Long idProducto) {
		this.idProducto = idProducto;
		setBorder(new LineBorder(null, 1, true));
		setTitle("Movimientos Productos");
		setFrameIcon(new ImageIcon(
				ClienteIFrame.class.getResource("/icons/movimiento.png")));
		setBounds(0, 0, 578, 353);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getTablePanel());
		getContentPane().add(getBtnSalir());
		search();
	}

	public void search() {
		try {
			ProductoBO productoBO = AbstractFactory
					.getInstance(ProductoBO.class);
			List<StockProducto> stockProductos = productoBO
					.obtenerStockPorProducto(idProducto);
			getTablePanel().addData(stockProductos);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	@Override
	public void applySecurity(List<String> permisos) {
	}

	private WTablePanel<StockProducto> getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new WTablePanel(StockProductoModel.class);
			tablePanel.addToolbarButtons(getToolbarButtons());
			tablePanel.setBounds(0, 11, 567, 267);
		}
		return tablePanel;
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Cerrar");
			btnSalir.setIcon(new ImageIcon(MovimientoProductoIFrame.class
					.getResource("/icons/cancel.png")));
			btnSalir.setBounds(464, 289, 103, 30);
			btnSalir.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					hideFrame();
				}
			});
		}
		return btnSalir;
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		WToolbarButton buttonAdd = new WToolbarButton("Nuevo Movimiento",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/add.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						addModalIFrame(new MovimientoProductoVerIFrame(
								idProducto, MovimientoProductoIFrame.this));
					}
				}, "Nuevo", null);
		WToolbarButton buttonEdit = new WToolbarButton("Editar Movimiento",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/edit.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long idStockMovimiento = tablePanel.getSelectedItemID();
						if (null != idStockMovimiento) {
							addModalIFrame(new MovimientoProductoVerIFrame(
									MovimientoProductoIFrame.this,
									idStockMovimiento));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un Movimiento",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Editar", null);
		WToolbarButton buttonDelete = new WToolbarButton("Eliminar Movimiento",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/delete.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						List<Long> idsStockProducto = tablePanel
								.getSelectedItemsID();
						if (WUtils.isNotEmpty(idsStockProducto)) {
							try {
								ProductoBO productoBO = AbstractFactory
										.getInstance(ProductoBO.class);
								productoBO
										.eliminarStockProducto(idsStockProducto);
								search();
							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar al menos un Movimiento de Producto",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Eliminar", null);

		toolbarButtons.add(buttonAdd);
		toolbarButtons.add(buttonEdit);
		toolbarButtons.add(buttonDelete);
		return toolbarButtons;
	}

}
