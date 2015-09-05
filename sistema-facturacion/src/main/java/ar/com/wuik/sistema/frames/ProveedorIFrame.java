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

import ar.com.wuik.sistema.bo.ProveedorBO;
import ar.com.wuik.sistema.entities.Proveedor;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.ProveedorFilter;
import ar.com.wuik.sistema.model.ProveedorModel;
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

public class ProveedorIFrame extends WAbstractModelIFrame implements WSecure {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 7107533032732470914L;
	private static final String CAMPO_RAZON_SOCIAL = "razonSocial";
	private JPanel pnlBusqueda;
	private JLabel lblRazonSocial;
	private JButton btnBuscar;
	private JButton btnLimpiar;
	private WTablePanel<Proveedor> tablePanel;
	private JButton btnCerrar;
	private JTextField txtRazonSocial;

	/**
	 * Create the frame.
	 */
	public ProveedorIFrame() {
		setBorder(new LineBorder(null, 1, true));
		setTitle("Proveedores");
		setFrameIcon(new ImageIcon(
				ProveedorIFrame.class.getResource("/icons/proveedores.png")));
		setBounds(0, 0, 907, 522);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getTablePanel());
		getContentPane().add(getBtnCerrar());
	}

	/**
	 * @see ar.com.wuik.swing.components.security.WSecure#applySecurity(java.util.List)
	 */
	@Override
	public void applySecurity(List<String> permisos) {
	}

	@Override
	protected boolean validateModel(WModel model) {
		return false;
	}

	private JPanel getPnlBusqueda() {
		if (pnlBusqueda == null) {
			pnlBusqueda = new JPanel();
			pnlBusqueda.setBorder(new TitledBorder(null, "B\u00FAsqueda",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlBusqueda.setBounds(10, 11, 884, 97);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getLblRazonSocial());
			pnlBusqueda.add(getBtnLimpiar());
			pnlBusqueda.add(getBtnBuscar());
			pnlBusqueda.add(getTxtRazonSocial());
		}
		return pnlBusqueda;
	}

	private JLabel getLblRazonSocial() {
		if (lblRazonSocial == null) {
			lblRazonSocial = new JLabel("Raz\u00F3n Social:");
			lblRazonSocial.setBounds(10, 24, 106, 25);
			lblRazonSocial.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return lblRazonSocial;
	}

	private JButton getBtnBuscar() {
		if (btnBuscar == null) {
			btnBuscar = new JButton("Buscar");
			btnBuscar.setBounds(658, 56, 103, 30);
			btnBuscar.setFocusable(false);
			btnBuscar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					search();
				}
			});
			btnBuscar.setIcon(new ImageIcon(ProveedorIFrame.class
					.getResource("/icons/search.png")));
		}
		return btnBuscar;
	}

	private JButton getBtnLimpiar() {
		if (btnLimpiar == null) {
			btnLimpiar = new JButton("Limpiar");
			btnLimpiar.setBounds(771, 56, 103, 30);
			btnLimpiar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					reset();
				}
			});
			btnLimpiar.setFocusable(false);
			btnLimpiar.setIcon(new ImageIcon(ProveedorIFrame.class
					.getResource("/icons/clear.png")));
		}
		return btnLimpiar;
	}

	private WTablePanel<Proveedor> getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new WTablePanel(ProveedorModel.class, Boolean.FALSE);
			tablePanel.setBounds(10, 119, 884, 329);
			tablePanel.addToolbarButtons(getToolbarButtons());
		}
		return tablePanel;
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		WToolbarButton buttonAdd = new WToolbarButton("Nuevo Proveedor",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/add.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						addModalIFrame(new ProveedorVerIFrame(
								ProveedorIFrame.this));
					}
				}, "Nuevo", null);
		WToolbarButton buttonEdit = new WToolbarButton("Editar Proveedor",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/edit.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long idProveedor = tablePanel.getSelectedItemID();
						if (null != idProveedor) {
							addModalIFrame(new ProveedorVerIFrame(idProveedor,
									ProveedorIFrame.this));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Proveedor",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Editar", null);

		WToolbarButton buttonDelete = new WToolbarButton("Eliminar Cliente",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/delete.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long idProveedor = tablePanel.getSelectedItemID();
						if (null != idProveedor) {
							try {
								int result = JOptionPane
										.showConfirmDialog(
												getParent(),
												"¿Desea Eliminar el Proveedor seleccionado?",
												"Alerta",
												JOptionPane.OK_CANCEL_OPTION,
												JOptionPane.WARNING_MESSAGE);
								if (result == JOptionPane.OK_OPTION) {
									ProveedorBO proveedorBO = AbstractFactory
											.getInstance(ProveedorBO.class);
									boolean enUso = proveedorBO
											.estaEnUso(idProveedor);
									if (enUso) {
										WTooltipUtils
												.showMessage(
														"El Proveedor se encuentra en uso",
														(JButton) e.getSource(),
														MessageType.ERROR);
									} else {
										proveedorBO.eliminar(idProveedor);
										search();
									}
								}
							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar al menos un Proveedor",
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

	private JButton getBtnCerrar() {
		if (btnCerrar == null) {
			btnCerrar = new JButton("Cerrar");
			btnCerrar.setFocusable(false);
			btnCerrar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					hideFrame();
				}
			});
			btnCerrar.setIcon(new ImageIcon(ProveedorIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(791, 454, 103, 30);
		}
		return btnCerrar;
	}

	private void reset() {
		txtRazonSocial.setText("");
		txtRazonSocial.requestFocus();
	}

	public void search() {
		WModel model = populateModel();
		String razonSocial = model.getValue(CAMPO_RAZON_SOCIAL);

		// Filtro
		ProveedorFilter filter = new ProveedorFilter();
		filter.setRazonSocial(razonSocial);

		try {
			ProveedorBO proveedorBO = AbstractFactory
					.getInstance(ProveedorBO.class);
			List<Proveedor> proveedores = proveedorBO.buscar(filter);
			getTablePanel().addData(proveedores);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	@Override
	protected JComponent getFocusComponent() {
		return getTxtRazonSocial();
	}

	@Override
	public void enterPressed() {
		search();
	}

	private JTextField getTxtRazonSocial() {
		if (txtRazonSocial == null) {
			txtRazonSocial = new JTextField();
			txtRazonSocial.setBounds(126, 24, 293, 25);
			txtRazonSocial.setDocument(new WTextFieldLimit(50));
			txtRazonSocial.setName(CAMPO_RAZON_SOCIAL);
		}
		return txtRazonSocial;
	}
}
