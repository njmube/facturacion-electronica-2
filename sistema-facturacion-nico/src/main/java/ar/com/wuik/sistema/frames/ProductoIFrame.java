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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ar.com.wuik.sistema.bo.ProductoBO;
import ar.com.wuik.sistema.entities.Permisos;
import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.ProductoFilter;
import ar.com.wuik.sistema.model.ProductoModel;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.components.security.WSecure;
import ar.com.wuik.swing.components.table.WTablePanel;
import ar.com.wuik.swing.components.table.WToolbarButton;
import ar.com.wuik.swing.frames.WAbstractModelIFrame;
import ar.com.wuik.swing.frames.WCalendarIFrame;
import ar.com.wuik.swing.utils.WTooltipUtils;
import ar.com.wuik.swing.utils.WTooltipUtils.MessageType;

/**
 * 
 * @author juan.castro
 * 
 */
public class ProductoIFrame extends WAbstractModelIFrame implements WSecure {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5205039853574138013L;
	private WTablePanel<Producto> tablePanel;
	private JButton btnSalir;
	private JPanel pnlBusqueda;
	private JLabel lbDenominacion;
	private JTextField txtDenominacion;
	private JButton btnLimpiar;
	private JButton btnBuscar;

	private static final String CAMPO_DESCRIPCION = "descripcion";
	private ProductoBO productoBO = AbstractFactory
			.getInstance(ProductoBO.class);

	public ProductoIFrame() {
		setBorder(new LineBorder(null, 1, true));
		setTitle("Productos");
		setFrameIcon(new ImageIcon(
				ProductoIFrame.class.getResource("/icons/productos.png")));
		setBounds(0, 0, 712, 594);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getTablePanel());
		getContentPane().add(getBtnSalir());
	}

	@Override
	public void applySecurity(List<String> permisos) {
		// TODO Auto-generated method stub

	}

	public void search() {
		WModel model = populateModel();
		String descripcion = model.getValue(CAMPO_DESCRIPCION);

		// Filtro
		ProductoFilter filter = new ProductoFilter();
		filter.setDescripcion(descripcion);

		try {
			List<Producto> productos = productoBO.buscar(filter);
			getTablePanel().addData(productos);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	@Override
	protected boolean validateModel(WModel model) {
		return false;
	}

	@Override
	protected JComponent getFocusComponent() {
		return getTxtDenominacion();
	}

	private WTablePanel<Producto> getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new WTablePanel(ProductoModel.class, Boolean.FALSE);
			tablePanel.addToolbarButtons(getToolbarButtons());
			tablePanel.setBounds(10, 128, 690, 394);
		}
		return tablePanel;
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		WToolbarButton buttonAdd = new WToolbarButton("Nuevo Producto",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/add.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						addModalIFrame(new ProductoVerIFrame(
								ProductoIFrame.this));
					}
				}, "Nuevo", Permisos.NUE_CLI.getCodPermiso());
		WToolbarButton buttonEdit = new WToolbarButton("Editar Producto",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/edit.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							addModalIFrame(new ProductoVerIFrame(selectedItem,
									ProductoIFrame.this));
						} else {
							WTooltipUtils.showMessage(
									"Debe seleccionar un Producto",
									(JButton) e.getSource(), MessageType.ALERTA);
						}
					}
				}, "Editar", Permisos.EDI_CLI.getCodPermiso());
		WToolbarButton buttonDelete = new WToolbarButton(
				"Eliminar Producto(s)",
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
								try {
									boolean estaEnUso = productoBO
											.estaEnUso(selectedItem);
									if (!estaEnUso) {
										try {
											productoBO.eliminar(selectedItem);
											search();
										} catch (BusinessException bexc) {
											showGlobalErrorMsg(bexc
													.getMessage());
										}
									} else {
										WTooltipUtils
												.showMessage(
														"No es posible Eliminar el Producto porque está siendo utilizado.",
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
											"Debe seleccionar al menos un Producto",
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
			btnSalir = new JButton("Cerrar");
			btnSalir.setIcon(new ImageIcon(ProductoIFrame.class
					.getResource("/icons/cancel.png")));
			btnSalir.setBounds(597, 526, 103, 30);
			btnSalir.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					hideFrame();
				}
			});
		}
		return btnSalir;
	}

	private JPanel getPnlBusqueda() {
		if (pnlBusqueda == null) {
			pnlBusqueda = new JPanel();
			pnlBusqueda.setBorder(new TitledBorder(null, "B\u00FAsqueda",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlBusqueda.setBounds(10, 11, 690, 106);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getLbDenominacion());
			pnlBusqueda.add(getTxtDenominacion());
			pnlBusqueda.add(getBtnLimpiar());
			pnlBusqueda.add(getBtnBuscar());

		}
		return pnlBusqueda;
	}

	private JLabel getLbDenominacion() {
		if (lbDenominacion == null) {
			lbDenominacion = new JLabel("Denominaci\u00F3n:");
			lbDenominacion.setHorizontalAlignment(SwingConstants.RIGHT);
			lbDenominacion.setBounds(10, 24, 104, 25);
		}
		return lbDenominacion;
	}

	private JTextField getTxtDenominacion() {
		if (txtDenominacion == null) {
			txtDenominacion = new JTextField();
			txtDenominacion.setBounds(124, 24, 243, 25);
			txtDenominacion.setColumns(10);
			txtDenominacion.setDocument(new WTextFieldLimit(45));
			txtDenominacion.setName(CAMPO_DESCRIPCION);
		}
		return txtDenominacion;
	}

	private JButton getBtnLimpiar() {
		if (btnLimpiar == null) {
			btnLimpiar = new JButton("Limpiar");
			btnLimpiar.setIcon(new ImageIcon(ProductoIFrame.class
					.getResource("/icons/clear.png")));
			btnLimpiar.setBounds(577, 65, 103, 30);
			btnLimpiar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					getTxtDenominacion().setText("");
					getTxtDenominacion().requestFocus();
				}
			});
		}
		return btnLimpiar;
	}

	private JButton getBtnBuscar() {
		if (btnBuscar == null) {
			btnBuscar = new JButton("Buscar");
			btnBuscar.setIcon(new ImageIcon(ProductoIFrame.class
					.getResource("/icons/search.png")));
			btnBuscar.setBounds(464, 65, 103, 30);
			btnBuscar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					search();
				}
			});
		}
		return btnBuscar;
	}

}
