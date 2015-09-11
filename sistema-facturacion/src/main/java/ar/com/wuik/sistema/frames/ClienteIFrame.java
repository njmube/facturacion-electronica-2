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
	private JButton btnRecibo;
	private JButton btnCheque;

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
		getContentPane().add(getBtnRecibo());
		getContentPane().add(getBtnCheque());
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

		WToolbarButton buttonFacturas = new WToolbarButton("Comprobantes",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/comprobantes.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							addModalIFrame(new ComprobanteIFrame(selectedItem, ClienteIFrame.this));
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
							addModalIFrame(new RemitoIFrame(selectedItem));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Cliente",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Ver Remitos", null);

		WToolbarButton buttonRecibos = new WToolbarButton("Recibos",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/recibo.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							addModalIFrame(new ReciboIFrame(selectedItem,
									ClienteIFrame.this));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Cliente",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Ver Recibos", null);
		WToolbarButton buttonCheques = new WToolbarButton("Cheques",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/cheques.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							addModalIFrame(new ChequeIFrame(selectedItem));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Cliente",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Ver Cheques", null);

		WToolbarButton buttonResumen = new WToolbarButton("Resumen de Cuenta",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/resumen_cuenta.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							addModalIFrame(new ResumenCuentaIFrame(selectedItem));
						} else {
							WTooltipUtils
									.showMessage(
											"Debe seleccionar un solo Cliente",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Resumen de Cuenta", null);

		toolbarButtons.add(buttonAdd);
		toolbarButtons.add(buttonEdit);
		toolbarButtons.add(buttonDelete);
		toolbarButtons.add(buttonFacturas);
		toolbarButtons.add(buttonRemitos);
		toolbarButtons.add(buttonRecibos);
		toolbarButtons.add(buttonCheques);
		toolbarButtons.add(buttonResumen);
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
								TipoComprobante.NOTA_CREDITO,
								ClienteIFrame.this));
					} else {
						WTooltipUtils.showMessage(
								"Debe seleccionar un Cliente",
								(JButton) e.getSource(), MessageType.ALERTA);
					}
				}
			});
			btnNotaDeCredito.setBounds(178, 146, 147, 46);
		}
		return btnNotaDeCredito;
	}

	private JButton getBtnNotaDeDbito() {
		if (btnNotaDeDbito == null) {
			btnNotaDeDbito = new JButton("Nota de D\u00E9bito");
			btnNotaDeDbito.setIcon(new ImageIcon(ClienteIFrame.class
					.getResource("/icons32/notas_debito.png")));
			btnNotaDeDbito.setBounds(347, 146, 147, 46);
			btnNotaDeDbito.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Long selectedItem = tablePanel.getSelectedItemID();
					if (null != selectedItem) {
						addModalIFrame(new ComprobanteVerIFrame(selectedItem,
								TipoComprobante.NOTA_DEBITO, ClienteIFrame.this));
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
			btnVenta.setBounds(10, 146, 147, 46);
			btnVenta.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Long selectedItem = tablePanel.getSelectedItemID();
					if (null != selectedItem) {
						addModalIFrame(new ComprobanteVerIFrame(selectedItem,
								TipoComprobante.FACTURA, ClienteIFrame.this));
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
					if (null != selectedItem) {
						addModalIFrame(new RemitoVerIFrame(selectedItem));
					} else {
						WTooltipUtils.showMessage(
								"Debe seleccionar un Cliente",
								(JButton) e.getSource(), MessageType.ALERTA);
					}
				}
			});
			btnRemito.setBounds(516, 146, 147, 46);
		}
		return btnRemito;
	}

	private JButton getBtnRecibo() {
		if (btnRecibo == null) {
			btnRecibo = new JButton("Recibo");
			btnRecibo.setIcon(new ImageIcon(ClienteIFrame.class
					.getResource("/icons32/recibo.png")));
			btnRecibo.setBounds(684, 146, 147, 46);
			btnRecibo.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Long selectedItem = tablePanel.getSelectedItemID();
					if (null != selectedItem) {
						addModalIFrame(new ReciboVerIFrame(selectedItem,
								ClienteIFrame.this));
					} else {
						WTooltipUtils.showMessage(
								"Debe seleccionar un Cliente",
								(JButton) e.getSource(), MessageType.ALERTA);
					}
				}
			});
		}
		return btnRecibo;
	}

	private JButton getBtnCheque() {
		if (btnCheque == null) {
			btnCheque = new JButton("Cheque");
			btnCheque.setIcon(new ImageIcon(ClienteIFrame.class
					.getResource("/icons32/cheques.png")));
			btnCheque.setBounds(852, 146, 147, 46);
			btnCheque.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Long selectedItem = tablePanel.getSelectedItemID();
					if (null != selectedItem) {
						addModalIFrame(new ChequeVerIFrame(selectedItem));
					} else {
						WTooltipUtils.showMessage(
								"Debe seleccionar un Cliente",
								(JButton) e.getSource(), MessageType.ALERTA);
					}
				}
			});
		}
		return btnCheque;
	}
}
