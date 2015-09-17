package ar.com.wuik.sistema.frames;

import java.awt.HeadlessException;
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
import ar.com.wuik.sistema.entities.enums.TipoComprobante;
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
import ar.com.wuik.swing.utils.WUtils;

public class ClienteIFrame extends WAbstractModelIFrame implements WSecure {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 7107533032732470914L;
	private static final String CAMPO_RAZON_SOCIAL = "razonSocial";
	private static final String CAMPO_DOCUMENTO = "contacto";
	private JPanel pnlBusqueda;
	private JLabel lblRazonSocial;
	private JButton btnBuscar;
	private JButton btnLimpiar;
	private WTablePanel<Cliente> tablePanel;
	private JButton btnCerrar;
	private JTextField txtRazonSocial;
	private JTextField txtDocumento;
	private JLabel lblDocumento;
	private JButton btnNotaDeCredito;
	private JButton btnNotaDeDbito;
	private JButton btnVenta;
	private JButton btnRemito;
	private JButton btnVentaCF;
	private JButton btnNotaDeCreditoCF;
	private JButton btnNotaDeDebitoCF;

	/**
	 * Create the frame.
	 */
	public ClienteIFrame() {
		setBorder(new LineBorder(null, 1, true));
		setTitle("Clientes");
		setFrameIcon(new ImageIcon(
				ClienteIFrame.class.getResource("/icons/cliente.png")));
		setBounds(0, 0, 1176, 728);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getTablePanel());
		getContentPane().add(getBtnCerrar());
		getContentPane().add(getBtnNotaDeCredito());
		getContentPane().add(getBtnNotaDeDbito());
		getContentPane().add(getBtnVenta());
		getContentPane().add(getBtnRemito());
		getContentPane().add(getBtnVentaCF());
		getContentPane().add(getBtnNotaDeCreditoCF());
		getContentPane().add(getBtnNotaDeDebitoCF());
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
			pnlBusqueda.setBounds(10, 11, 1154, 109);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getLblRazonSocial());
			pnlBusqueda.add(getBtnLimpiar());
			pnlBusqueda.add(getBtnBuscar());
			pnlBusqueda.add(getTxtRazonSocial());
			pnlBusqueda.add(getTxtDocumento());
			pnlBusqueda.add(getLblDocumento());
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
			btnBuscar.setBounds(928, 66, 103, 30);
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
			btnLimpiar.setBounds(1041, 66, 103, 30);
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
			tablePanel.setBounds(10, 203, 1154, 451);
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
							ClienteBO clienteBO = AbstractFactory
									.getInstance(ClienteBO.class);
							try {
								if (clienteBO.obtener(idCliente)
										.getRazonSocial()
										.equalsIgnoreCase(Cliente.CONS_FINAL)) {
									JOptionPane.showMessageDialog(getParent(),
											"Este usuario no es editable.");
								} else {
									addModalIFrame(new ClienteVerIFrame(
											idCliente, ClienteIFrame.this));
								}
							} catch (HeadlessException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (BusinessException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}

						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Cliente",
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
						Long idCliente = tablePanel.getSelectedItemID();
						ClienteBO clienteBO = AbstractFactory
								.getInstance(ClienteBO.class);
						if (null != idCliente) {
							try {
								if (clienteBO.obtener(idCliente)
										.getRazonSocial()
										.equalsIgnoreCase(Cliente.CONS_FINAL)) {
									JOptionPane.showMessageDialog(getParent(),
											"Este usuario no es editable.");
								} else {
									int result = JOptionPane
											.showConfirmDialog(
													getParent(),
													"¿Desea Eliminar el Cliente seleccionado?",
													"Alerta",
													JOptionPane.OK_CANCEL_OPTION,
													JOptionPane.WARNING_MESSAGE);
									if (result == JOptionPane.OK_OPTION) {

										boolean enUso = clienteBO
												.estaEnUso(idCliente);
										if (enUso) {
											showGlobalMsg("El Cliente se encuentra en uso");
										} else {
											clienteBO.eliminar(idCliente);
											search();
										}
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

		WToolbarButton buttonFacturas = new WToolbarButton("Comprobantes",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/comprobantes.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							addModalIFrame(new ComprobanteIFrame(selectedItem));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Cliente",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Ver Comprobantes", null);

		WToolbarButton buttonRemitos = new WToolbarButton("Remitos",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/remitos.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							ClienteBO clienteBO = AbstractFactory
									.getInstance(ClienteBO.class);
							try {
								if (clienteBO.obtener(selectedItem)
										.getRazonSocial()
										.equalsIgnoreCase("CONS FINAL")) {
									JOptionPane.showMessageDialog(getParent(),
											"Este usuario no posee remitos.");
								} else {
									addModalIFrame(new RemitoIFrame(
											selectedItem));
								}
							} catch (HeadlessException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (BusinessException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Cliente",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Ver Remitos", null);

		toolbarButtons.add(buttonAdd);
		toolbarButtons.add(buttonEdit);
		toolbarButtons.add(buttonDelete);
		toolbarButtons.add(buttonFacturas);
		toolbarButtons.add(buttonRemitos);
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
			btnCerrar.setIcon(new ImageIcon(ClienteIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(1061, 660, 103, 30);
		}
		return btnCerrar;
	}

	private void reset() {
		txtRazonSocial.setText("");
		txtDocumento.setText("");
		txtRazonSocial.requestFocus();
	}

	public void search() {
		WModel model = populateModel();
		String razonSocial = model.getValue(CAMPO_RAZON_SOCIAL);
		String documento = model.getValue(CAMPO_DOCUMENTO);

		// Filtro
		ClienteFilter filter = new ClienteFilter();
		filter.setRazonSocial(razonSocial);
		filter.setDocumento(documento);

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

	private JTextField getTxtDocumento() {
		if (txtDocumento == null) {
			txtDocumento = new JTextField();
			txtDocumento.setName(CAMPO_DOCUMENTO);
			txtDocumento.setBounds(593, 24, 300, 25);
			txtDocumento.setDocument(new WTextFieldLimit(50));
		}
		return txtDocumento;
	}

	private JLabel getLblDocumento() {
		if (lblDocumento == null) {
			lblDocumento = new JLabel("CUIT/CUIL/DNI:");
			lblDocumento.setHorizontalAlignment(SwingConstants.RIGHT);
			lblDocumento.setBounds(460, 24, 123, 25);
		}
		return lblDocumento;
	}

	private JButton getBtnNotaDeCredito() {
		if (btnNotaDeCredito == null) {
			btnNotaDeCredito = new JButton("Nota de Cr\u00E9dito");
			btnNotaDeCredito.setIcon(new ImageIcon(ClienteIFrame.class
					.getResource("/icons32/notas_credito.png")));
			btnNotaDeCredito.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Long selectedItem = tablePanel.getSelectedItemID();
					if (null != selectedItem) {
						addModalIFrame(new ComprobanteVerIFrame(selectedItem,
								TipoComprobante.NOTA_CREDITO));
					} else {
						WTooltipUtils.showMessage(
								"Debe seleccionar un Cliente",
								(JButton) e.getSource(), MessageType.ALERTA);
					}
				}
			});
			btnNotaDeCredito.setBounds(679, 141, 147, 46);
		}
		return btnNotaDeCredito;
	}

	private JButton getBtnNotaDeDbito() {
		if (btnNotaDeDbito == null) {
			btnNotaDeDbito = new JButton("Nota de D\u00E9bito");
			btnNotaDeDbito.setIcon(new ImageIcon(ClienteIFrame.class
					.getResource("/icons32/notas_debito.png")));
			btnNotaDeDbito.setBounds(848, 141, 147, 46);
			btnNotaDeDbito.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Long selectedItem = tablePanel.getSelectedItemID();
					if (null != selectedItem) {
						addModalIFrame(new ComprobanteVerIFrame(selectedItem,
								TipoComprobante.NOTA_DEBITO));
					} else {
						WTooltipUtils.showMessage(
								"Debe seleccionar un Cliente",
								(JButton) e.getSource(), MessageType.ALERTA);
					}
				}
			});
		}
		return btnNotaDeDbito;
	}

	private JButton getBtnVenta() {
		if (btnVenta == null) {
			btnVenta = new JButton("Venta");
			btnVenta.setIcon(new ImageIcon(ClienteIFrame.class
					.getResource("/icons32/facturas.png")));
			btnVenta.setBounds(511, 141, 147, 46);
			btnVenta.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Long selectedItem = tablePanel.getSelectedItemID();
					if (null != selectedItem) {
						addModalIFrame(new ComprobanteVerIFrame(selectedItem,
								TipoComprobante.FACTURA));
					} else {
						WTooltipUtils.showMessage(
								"Debe seleccionar un solo Cliente",
								(JButton) e.getSource(), MessageType.ALERTA);
					}
				}
			});
		}
		return btnVenta;
	}

	private JButton getBtnRemito() {
		if (btnRemito == null) {
			btnRemito = new JButton("Remito");
			btnRemito.setIcon(new ImageIcon(ClienteIFrame.class
					.getResource("/icons32/remitos.png")));
			btnRemito.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Long selectedItem = tablePanel.getSelectedItemID();
					ClienteBO clienteBO = AbstractFactory
							.getInstance(ClienteBO.class);

					if (null != selectedItem) {
						try {
							if (clienteBO.obtener(selectedItem)
									.getRazonSocial()
									.equalsIgnoreCase("CONS FINAL")) {
								JOptionPane
										.showMessageDialog(getParent(),
												"No es posible agregar remitos para este usuario.");
							} else {
								addModalIFrame(new RemitoVerIFrame(selectedItem));
							}
						} catch (HeadlessException e1) {
							showGlobalErrorMsg(e1.getMessage());
							e1.printStackTrace();
						} catch (BusinessException bexc) {
							showGlobalErrorMsg(bexc.getMessage());
						}
					} else {
						WTooltipUtils.showMessage(
								"Debe seleccionar un Cliente",
								(JButton) e.getSource(), MessageType.ALERTA);
					}
				}
			});
			btnRemito.setBounds(1017, 141, 147, 46);
		}
		return btnRemito;
	}

	private JButton getBtnVentaCF() {
		if (btnVentaCF == null) {
			btnVentaCF = new JButton("Cons. Final Vta.");
			btnVentaCF.setIcon(new ImageIcon(ClienteIFrame.class.getResource("/icons32/facturas.png")));
			btnVentaCF.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ClienteBO clienteBO = AbstractFactory
							.getInstance(ClienteBO.class);
					ClienteFilter filter = new ClienteFilter();
					filter.setDocumento("0");
					try {
						List<Cliente> clientes = clienteBO.buscar(filter);
						if (WUtils.isNotEmpty(clientes)) {
							addModalIFrame(new ComprobanteVerIFrame(clientes
									.get(0).getId(), TipoComprobante.FACTURA));
						} else {
							WTooltipUtils.showMessage(
									"No existe usuario Consumidor Final. Contacte al Administrador.",
									(JButton) e.getSource(), MessageType.ALERTA);
						}

					} catch (BusinessException bexc) {
						showGlobalErrorMsg(bexc.getMessage());
					}
				}
			});
			btnVentaCF.setBounds(10, 141, 147, 46);
		}
		return btnVentaCF;
	}

	private JButton getBtnNotaDeCreditoCF() {
		if (btnNotaDeCreditoCF == null) {
			btnNotaDeCreditoCF = new JButton("Cons. Final NC");
			btnNotaDeCreditoCF.setIcon(new ImageIcon(ClienteIFrame.class.getResource("/icons32/notas_credito.png")));
			btnNotaDeCreditoCF.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ClienteBO clienteBO = AbstractFactory
							.getInstance(ClienteBO.class);
					ClienteFilter filter = new ClienteFilter();
					filter.setDocumento("0");
					try {
						List<Cliente> clientes = clienteBO.buscar(filter);
						if (WUtils.isNotEmpty(clientes)) {
							addModalIFrame(new ComprobanteVerIFrame(clientes
									.get(0).getId(), TipoComprobante.NOTA_CREDITO));
						} else {
							WTooltipUtils.showMessage(
									"No existe usuario Consumidor Final. Contacte al Administrador.",
									(JButton) e.getSource(), MessageType.ALERTA);
						}

					} catch (BusinessException bexc) {
						showGlobalErrorMsg(bexc.getMessage());
					}
				}
			});
			btnNotaDeCreditoCF.setBounds(178, 141, 147, 46);
		}
		return btnNotaDeCreditoCF;
	}

	private JButton getBtnNotaDeDebitoCF() {
		if (btnNotaDeDebitoCF == null) {
			btnNotaDeDebitoCF = new JButton("Cons. Final ND");
			btnNotaDeDebitoCF.setIcon(new ImageIcon(ClienteIFrame.class.getResource("/icons32/notas_debito.png")));
			btnNotaDeDebitoCF.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ClienteBO clienteBO = AbstractFactory
							.getInstance(ClienteBO.class);
					ClienteFilter filter = new ClienteFilter();
					filter.setDocumento("0");
					try {
						List<Cliente> clientes = clienteBO.buscar(filter);
						if (WUtils.isNotEmpty(clientes)) {
							addModalIFrame(new ComprobanteVerIFrame(clientes
									.get(0).getId(), TipoComprobante.NOTA_DEBITO));
						} else {
							WTooltipUtils.showMessage(
									"No existe usuario Consumidor Final. Contacte al Administrador.",
									(JButton) e.getSource(), MessageType.ALERTA);
						}

					} catch (BusinessException bexc) {
						showGlobalErrorMsg(bexc.getMessage());
					}
				}
			});
			btnNotaDeDebitoCF.setBounds(342, 141, 147, 46);
		}
		return btnNotaDeDebitoCF;
	}
}
