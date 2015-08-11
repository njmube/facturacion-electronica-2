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

import ar.com.wuik.sistema.bo.ChequeBO;
import ar.com.wuik.sistema.entities.Cheque;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.ChequeFilter;
import ar.com.wuik.sistema.model.ChequeModel;
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

public class ChequeIFrame extends WAbstractModelIFrame implements WSecure {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 6068992547434576729L;
	private WTablePanel<Cheque> tablePanel;
	private JButton btnSalir;
	private JPanel pnlBusqueda;
	private JLabel lblNumero;
	private JTextField txtNumero;
	private JButton btnLimpiar;
	private JButton btnBuscar;
	private Long idCliente;

	private static final String CAMPO_NUMERO = "numero";
	private static final String CAMPO_RECIBIDO = "recibido";
	private JTextField txtRecibido;
	private JLabel lblRecibido;

	public ChequeIFrame() {
		setBorder(new LineBorder(null, 1, true));
		setTitle("Cheques");
		setFrameIcon(new ImageIcon(
				ChequeIFrame.class.getResource("/icons/cheques.png")));
		setBounds(0, 0, 712, 594);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getTablePanel());
		getContentPane().add(getBtnSalir());
	}

	public ChequeIFrame(Long idCliente) {
		this.idCliente = idCliente;
		setBorder(new LineBorder(null, 1, true));
		setTitle("Cheques");
		setFrameIcon(new ImageIcon(
				ChequeIFrame.class.getResource("/icons/cheques.png")));
		setBounds(0, 0, 712, 594);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getTablePanel());
		getContentPane().add(getBtnSalir());
		search();
	}

	@Override
	public void applySecurity(List<String> permisos) {
	}

	public void search() {

		ChequeBO chequeBO = AbstractFactory.getInstance(ChequeBO.class);
		WModel model = populateModel();
		String numero = model.getValue(CAMPO_NUMERO);
		String recibido = model.getValue(CAMPO_RECIBIDO);

		// Filtro
		ChequeFilter filter = new ChequeFilter();
		filter.setNumero(numero);
		filter.setaNombreDe(recibido);

		try {
			List<Cheque> cheques = chequeBO.buscar(filter);
			getTablePanel().addData(cheques);
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
		return getTxtNumero();
	}

	private WTablePanel<Cheque> getTablePanel() {
		if (tablePanel == null) {
			tablePanel = new WTablePanel(ChequeModel.class, Boolean.FALSE);
			tablePanel.addToolbarButtons(getToolbarButtons());
			tablePanel.setBounds(10, 101, 690, 421);
		}
		return tablePanel;
	}

	private List<WToolbarButton> getToolbarButtons() {
		List<WToolbarButton> toolbarButtons = new ArrayList<WToolbarButton>();

		WToolbarButton buttonAdd = new WToolbarButton("Nuevo Cheque",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/add.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						addModalIFrame(new ChequeVerIFrame(idCliente,
								ChequeIFrame.this));
					}
				}, "Nuevo", null);
		WToolbarButton buttonEdit = new WToolbarButton("Editar Cheque",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/edit.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							ChequeBO chequeBO = AbstractFactory
									.getInstance(ChequeBO.class);
							try {
								Cheque cheque = chequeBO.obtener(selectedItem);
								if (!cheque.isEnUso()) {
									addModalIFrame(new ChequeVerIFrame(
											ChequeIFrame.this, selectedItem,
											idCliente));
								} else {
									WTooltipUtils
											.showMessage(
													"No es posible Editar el Cheque porque está siendo utilizado.",
													(JButton) e.getSource(),
													MessageType.ALERTA);
								}
							} catch (BusinessException bexc) {
								showGlobalErrorMsg(bexc.getMessage());
							}
						} else {
							WTooltipUtils.showMessage(
									"Debe seleccionar un Cheque",
									(JButton) e.getSource(), MessageType.ALERTA);
						}
					}
				}, "Editar", null);
		WToolbarButton buttonDelete = new WToolbarButton("Eliminar Cheque(s)",
				new ImageIcon(WCalendarIFrame.class
						.getResource("/icons/delete.png")),
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						Long selectedItem = tablePanel.getSelectedItemID();
						if (null != selectedItem) {
							int result = JOptionPane.showConfirmDialog(
									getParent(),
									"¿Desea eliminar el Cheque seleccionado?",
									"Alerta", JOptionPane.OK_CANCEL_OPTION,
									JOptionPane.WARNING_MESSAGE);
							if (result == JOptionPane.OK_OPTION) {
								ChequeBO chequeBO = AbstractFactory
										.getInstance(ChequeBO.class);
								try {
									Cheque cheque = chequeBO
											.obtener(selectedItem);
									if (!cheque.isEnUso()) {
										try {
											chequeBO.eliminar(selectedItem);
											search();
										} catch (BusinessException bexc) {
											showGlobalErrorMsg(bexc
													.getMessage());
										}
									} else {
										WTooltipUtils
												.showMessage(
														"No es posible Eliminar el Cheque porque está siendo utilizado.",
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
											"Debe seleccionar al menos un Cheque",
											(JButton) e.getSource(),
											MessageType.ALERTA);
						}
					}
				}, "Eliminar", null);

		if (null != this.idCliente) {
			toolbarButtons.add(buttonAdd);
		}
		toolbarButtons.add(buttonEdit);
		toolbarButtons.add(buttonDelete);
		return toolbarButtons;
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Salir");
			btnSalir.setIcon(new ImageIcon(ChequeIFrame.class
					.getResource("/icons/cancel.png")));
			btnSalir.setBounds(609, 531, 91, 25);
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
			pnlBusqueda.setBounds(10, 11, 690, 90);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getLblNumero());
			pnlBusqueda.add(getTxtNumero());
			pnlBusqueda.add(getBtnLimpiar());
			pnlBusqueda.add(getBtnBuscar());
			pnlBusqueda.add(getTxtRecibido());
			pnlBusqueda.add(getLblRecibido());

		}
		return pnlBusqueda;
	}

	private JLabel getLblNumero() {
		if (lblNumero == null) {
			lblNumero = new JLabel("N\u00FAmero:");
			lblNumero.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNumero.setBounds(10, 24, 104, 25);
		}
		return lblNumero;
	}

	private JTextField getTxtNumero() {
		if (txtNumero == null) {
			txtNumero = new JTextField();
			txtNumero.setBounds(124, 24, 127, 25);
			txtNumero.setColumns(10);
			txtNumero.setDocument(new WTextFieldLimit(40));
			txtNumero.setName(CAMPO_NUMERO);
		}
		return txtNumero;
	}

	private JButton getBtnLimpiar() {
		if (btnLimpiar == null) {
			btnLimpiar = new JButton("Limpiar");
			btnLimpiar.setIcon(new ImageIcon(ChequeIFrame.class
					.getResource("/icons/clear.png")));
			btnLimpiar.setBounds(251, 60, 103, 25);
			btnLimpiar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					getTxtNumero().setText("");
					getTxtRecibido().setText("");
					getTxtNumero().requestFocus();
				}
			});
		}
		return btnLimpiar;
	}

	private JButton getBtnBuscar() {
		if (btnBuscar == null) {
			btnBuscar = new JButton("Buscar");
			btnBuscar.setIcon(new ImageIcon(ChequeIFrame.class
					.getResource("/icons/search.png")));
			btnBuscar.setBounds(364, 60, 103, 25);
			btnBuscar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					search();
				}
			});
		}
		return btnBuscar;
	}

	private JTextField getTxtRecibido() {
		if (txtRecibido == null) {
			txtRecibido = new JTextField();
			txtRecibido.setName(CAMPO_RECIBIDO);
			txtRecibido.setColumns(10);
			txtRecibido.setDocument(new WTextFieldLimit(60));
			txtRecibido.setBounds(405, 24, 221, 25);
		}
		return txtRecibido;
	}

	private JLabel getLblRecibido() {
		if (lblRecibido == null) {
			lblRecibido = new JLabel("Recibido:");
			lblRecibido.setHorizontalAlignment(SwingConstants.RIGHT);
			lblRecibido.setBounds(291, 24, 104, 25);
		}
		return lblRecibido;
	}
}
