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

import ar.com.wuik.sistema.bo.ClienteBO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.ClienteFilter;
import ar.com.wuik.sistema.model.ClienteModel;
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

public class ClienteIFrame extends WAbstractModelIFrame implements WSecure {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 7107533032732470914L;
	private static final String CAMPO_RAZON_SOCIAL = "razonSocial";
	private static final String CAMPO_CONTACTO = "contacto";
	private JPanel pnlBusqueda;
	private JLabel lblRazonSocial;
	private JButton btnBuscar;
	private JButton btnLimpiar;
	private WTablePanel<Cliente> tablePanel;
	private JButton btnCerrar;
	private JTextField txtRazonSocial;
	private JTextField txtContacto;
	private JLabel lblContacto;

	/**
	 * Create the frame.
	 */
	public ClienteIFrame() {
		setBorder(new LineBorder(null, 1, true));
		setTitle("Clientes");
		setFrameIcon(new ImageIcon(
				ClienteIFrame.class.getResource("/icons/clientes.png")));
		setBounds(0, 0, 1176, 522);
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
		getTablePanel().applySecurity(permisos);
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
			pnlBusqueda.setBounds(10, 11, 1154, 97);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getLblRazonSocial());
			pnlBusqueda.add(getBtnLimpiar());
			pnlBusqueda.add(getBtnBuscar());
			pnlBusqueda.add(getTxtRazonSocial());
			pnlBusqueda.add(getTxtContacto());
			pnlBusqueda.add(getLblContacto());
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
			btnBuscar.setBounds(608, 61, 103, 25);
			btnBuscar.setFocusable(false);
			btnBuscar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					search();
				}
			});
			btnBuscar.setIcon(new ImageIcon(ClienteIFrame.class
					.getResource("/icons/search.png")));
		}
		return btnBuscar;
	}

	private JButton getBtnLimpiar() {
		if (btnLimpiar == null) {
			btnLimpiar = new JButton("Limpiar");
			btnLimpiar.setBounds(495, 61, 103, 25);
			btnLimpiar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					reset();
				}
			});
			btnLimpiar.setFocusable(false);
			btnLimpiar.setIcon(new ImageIcon(ClienteIFrame.class
					.getResource("/icons/clear.png")));
		}
		return btnLimpiar;
	}

	private WTablePanel<Cliente> getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new WTablePanel(ClienteModel.class, Boolean.FALSE);
			tablePanel.setBounds(10, 119, 1154, 329);
			tablePanel.addToolbarButtons(getToolbarButtons());
		}
		return tablePanel;
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		WToolbarButton buttonAdd = new WToolbarButton("Nuevo Cliente",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/add.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						addModalIFrame(new ClienteVerIFrame(ClienteIFrame.this));
					}
				}, "Nuevo", null);
		WToolbarButton buttonEdit = new WToolbarButton("Editar Cliente",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/edit.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long idCliente = tablePanel.getSelectedItemID();
						if (null != idCliente) {
							addModalIFrame(new ClienteVerIFrame(idCliente,
									ClienteIFrame.this));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Cliente",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Editar", null);
		WToolbarButton buttonActivar = new WToolbarButton("Activar Cliente",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/activar-desactivar.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long idCliente = tablePanel.getSelectedItemID();
						if (null != idCliente) {
							try {
								int result = JOptionPane
										.showConfirmDialog(
												getParent(),
												"¿Desea Activar el Cliente seleccionado?",
												"Alerta",
												JOptionPane.OK_CANCEL_OPTION,
												JOptionPane.WARNING_MESSAGE);
								if (result == JOptionPane.OK_OPTION) {
									ClienteBO clienteBO = AbstractFactory
											.getInstance(ClienteBO.class);
									clienteBO.activar(idCliente);
									search();
								}
							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar al menos un Cliente",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Activar", null);

		WToolbarButton buttonDelete = new WToolbarButton("Eliminar Cliente",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/delete.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long idCliente = tablePanel.getSelectedItemID();
						if (null != idCliente) {
							try {
								int result = JOptionPane
										.showConfirmDialog(
												getParent(),
												"¿Desea Eliminar el Cliente seleccionado?",
												"Alerta",
												JOptionPane.OK_CANCEL_OPTION,
												JOptionPane.WARNING_MESSAGE);
								if (result == JOptionPane.OK_OPTION) {
									ClienteBO clienteBO = AbstractFactory
											.getInstance(ClienteBO.class);

									boolean enUso = clienteBO
											.estaEnUso(idCliente);
									if (enUso) {
										showGlobalMsg("El Cliente se encuentra en uso");
									} else {
										clienteBO.eliminar(idCliente);
										search();
									}
								}
							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar al menos un Cliente",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Eliminar", null);

		WToolbarButton buttonDesactivar = new WToolbarButton(
				"Desactivar Cliente", new ImageIcon(
						WCalendarIFrame.class
								.getResource("/icons/activar-desactivar.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long idCliente = tablePanel.getSelectedItemID();
						if (null != idCliente) {
							try {
								int result = JOptionPane
										.showConfirmDialog(
												getParent(),
												"¿Desea Desactivar el Cliente seleccionado?",
												"Alerta",
												JOptionPane.OK_CANCEL_OPTION,
												JOptionPane.WARNING_MESSAGE);
								if (result == JOptionPane.OK_OPTION) {
									ClienteBO clienteBO = AbstractFactory
											.getInstance(ClienteBO.class);
									clienteBO.desactivar(idCliente);
									search();
								}
							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar al menos un Cliente",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Desactivar", null);

		WToolbarButton buttonNotasCreditos = new WToolbarButton(
				"Notas de Crédito", new ImageIcon(
						WCalendarIFrame.class
								.getResource("/icons/notas_credito.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							addModalIFrame(new NotaCreditoIFrame(
									selectedItem));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Cliente",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Notas de Crédito", null);

		WToolbarButton buttonNotasDebitos = new WToolbarButton(
				"Notas de Dédito", new ImageIcon(
						WCalendarIFrame.class
								.getResource("/icons/notas_debito.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							// addModalIFrame(new NotaDebitoClienteIFrame(
							// selectedItem));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Cliente",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Notas de Dédito", null);

		WToolbarButton buttonFacturas = new WToolbarButton("Ventas",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/facturas.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							addModalIFrame(new FacturaIFrame(selectedItem));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Cliente",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Ventas", null);

		WToolbarButton buttonRemitos = new WToolbarButton("Remitos",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/remitos.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							addModalIFrame(new RemitoClienteIFrame(selectedItem));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Cliente",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Remitos", null);

		WToolbarButton buttonRecibos = new WToolbarButton("Recibos",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/recibos.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							// addModalIFrame(new
							// ReciboClienteIFrame(selectedItem));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Cliente",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Recibos", null);

		toolbarButtons.add(buttonAdd);
		toolbarButtons.add(buttonEdit);
		toolbarButtons.add(buttonDelete);
		toolbarButtons.add(buttonActivar);
		toolbarButtons.add(buttonDesactivar);
		toolbarButtons.add(buttonNotasCreditos);
		toolbarButtons.add(buttonNotasDebitos);
		toolbarButtons.add(buttonFacturas);
		toolbarButtons.add(buttonRemitos);
		toolbarButtons.add(buttonRecibos);
		return toolbarButtons;
	}

	private JButton getBtnCerrar() {
		if (btnCerrar == null) {
			btnCerrar = new JButton("Salir");
			btnCerrar.setFocusable(false);
			btnCerrar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					hideFrame();
				}
			});
			btnCerrar.setIcon(new ImageIcon(ClienteIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(1061, 459, 103, 25);
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
		String contacto = model.getValue(CAMPO_CONTACTO);

		// Filtro
		ClienteFilter filter = new ClienteFilter();
		filter.setRazonSocial(razonSocial);

		try {
			ClienteBO clienteBO = AbstractFactory.getInstance(ClienteBO.class);
			List<Cliente> clientes = clienteBO.buscar(filter);
			getTablePanel().addData(clientes);
		} catch (BusinessException bexc) {
			showGlobalErrorMsg(bexc.getMessage());
		}
	}

	@Override
	protected JComponent getFocusComponent() {
		return getTxtRazonSocial();
	}

	private JTextField getTxtRazonSocial() {
		if (txtRazonSocial == null) {
			txtRazonSocial = new JTextField();
			txtRazonSocial.setBounds(126, 24, 300, 25);
			txtRazonSocial.setDocument(new WTextFieldLimit(50));
			txtRazonSocial.setName(CAMPO_RAZON_SOCIAL);
		}
		return txtRazonSocial;
	}

	private JTextField getTxtContacto() {
		if (txtContacto == null) {
			txtContacto = new JTextField();
			txtContacto.setName(CAMPO_CONTACTO);
			txtContacto.setBounds(542, 24, 300, 25);
			txtContacto.setDocument(new WTextFieldLimit(50));
		}
		return txtContacto;
	}

	private JLabel getLblContacto() {
		if (lblContacto == null) {
			lblContacto = new JLabel("Contacto:");
			lblContacto.setHorizontalAlignment(SwingConstants.RIGHT);
			lblContacto.setBounds(460, 24, 72, 25);
		}
		return lblContacto;
	}
}
