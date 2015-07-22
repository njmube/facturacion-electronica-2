package ar.com.wuik.sistema.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import ar.com.wuik.sistema.utils.BackupUtil;
import ar.com.wuik.sistema.utils.MailUtil;
import ar.com.wuik.swing.frames.WAbstractIFrame;
import ar.com.wuik.swing.utils.WFrameUtils;
import ar.com.wuik.swing.utils.WUtils;

public class BackupIFrame extends WAbstractIFrame {
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6838619883125511589L;
	private JPanel pnlBusqueda;
	private JButton btnCerrar;
	private JButton btnGuardar;
	private JCheckBox chckbxEnviarPorMail;
	private JTextField txtDestino;
	private JLabel lblDestino;
	private JButton btnExaminar;
	private JProgressBar progressBar;

	public BackupIFrame() {
		initialize("Respaldo de Sistema");
	}

	private void initialize(String title) {
		setTitle(title);
		setBorder(new LineBorder(null, 1, true));
		setFrameIcon(new ImageIcon(
				BackupIFrame.class.getResource("/icons/clientes.png")));
		setBounds(0, 0, 523, 196);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(getPnlBusqueda());
		getContentPane().add(getBtnCerrar());
		getContentPane().add(getBtnGuardar());
		getContentPane().add(getProgressBar());
	}

	private JPanel getPnlBusqueda() {
		if (pnlBusqueda == null) {
			pnlBusqueda = new JPanel();
			pnlBusqueda.setBorder(new TitledBorder(UIManager
					.getBorder("TitledBorder.border"), "Datos",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			pnlBusqueda.setBounds(10, 11, 500, 109);
			pnlBusqueda.setLayout(null);
			pnlBusqueda.add(getChckbxEnviarPorMail());
			pnlBusqueda.add(getTxtDestino());
			pnlBusqueda.add(getLblDestino());
			pnlBusqueda.add(getBtnExaminar());
		}
		return pnlBusqueda;
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
			btnCerrar.setIcon(new ImageIcon(BackupIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(294, 131, 103, 25);
		}
		return btnCerrar;
	}

	private JButton getBtnGuardar() {
		if (btnGuardar == null) {
			btnGuardar = new JButton("Realizar");
			btnGuardar.setIcon(new ImageIcon(BackupIFrame.class
					.getResource("/icons/ok.png")));
			btnGuardar.setBounds(407, 131, 103, 25);
			btnGuardar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					if (WUtils.isEmpty(getTxtDestino().getText())) {
						showGlobalMsg("Debe seleccionar un Destino para la copia");
					} else {
						try {

							final String targetBackup = BackupUtil
									.doBackup(getTxtDestino().getText());

							if (getChckbxEnviarPorMail().isSelected()) {

								WFrameUtils
										.showGlobalMsg("Enviando Respaldo por Mail, esto puede tardar unos minutos");

								getProgressBar().setVisible(Boolean.TRUE);
								btnGuardar.setEnabled(Boolean.FALSE);

								new Thread(new Runnable() {

									@Override
									public void run() {
										boolean error = Boolean.FALSE;
										try {
											MailUtil.sendMailBackup(targetBackup);
										} catch (final Exception exc) {
											error = Boolean.TRUE;
											java.awt.EventQueue
													.invokeLater(new Runnable() {

														@Override
														public void run() {
															btnGuardar
																	.setEnabled(Boolean.TRUE);
															getProgressBar()
																	.setVisible(
																			Boolean.FALSE);
															showGlobalErrorMsg(exc
																	.getMessage());
														}
													});
										} finally {

											if (!error) {
												java.awt.EventQueue
														.invokeLater(new Runnable() {

															@Override
															public void run() {
																JOptionPane
																		.showConfirmDialog(
																				getContentPane(),
																				"El Respaldo de Sistema se envió con éxito.",
																				"Información",
																				JOptionPane.CLOSED_OPTION,
																				JOptionPane.INFORMATION_MESSAGE,
																				null);
																btnGuardar
																		.setEnabled(Boolean.TRUE);
																getProgressBar()
																		.setVisible(
																				Boolean.FALSE);
															}
														});
											}

										}
									}
								}).start();
							}
						} catch (Exception exc) {
							showGlobalErrorMsg(exc.getMessage());
						}
					}
				}
			});
		}
		return btnGuardar;
	}

	private JCheckBox getChckbxEnviarPorMail() {
		if (chckbxEnviarPorMail == null) {
			chckbxEnviarPorMail = new JCheckBox("Enviar por Mail");
			chckbxEnviarPorMail.setSelected(true);
			chckbxEnviarPorMail.setBounds(10, 73, 118, 23);
		}
		return chckbxEnviarPorMail;
	}

	private JTextField getTxtDestino() {
		if (txtDestino == null) {
			txtDestino = new JTextField();
			txtDestino.setEditable(false);
			txtDestino.setBounds(98, 24, 291, 23);
			txtDestino.setColumns(10);
		}
		return txtDestino;
	}

	private JLabel getLblDestino() {
		if (lblDestino == null) {
			lblDestino = new JLabel("Destino:");
			lblDestino.setBounds(10, 24, 78, 23);
		}
		return lblDestino;
	}

	private JButton getBtnExaminar() {
		if (btnExaminar == null) {
			btnExaminar = new JButton("Examinar");
			btnExaminar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JFileChooser fc = new JFileChooser();
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					fc.setMultiSelectionEnabled(false);
					int returnVal = fc.showDialog(BackupIFrame.this,
							"Respaldar");
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						File file = fc.getSelectedFile();
						String path = file.getAbsolutePath();
						getTxtDestino().setText(path);
					}
				}
			});
			btnExaminar.setBounds(399, 24, 89, 23);
		}
		return btnExaminar;
	}

	private JProgressBar getProgressBar() {
		if (progressBar == null) {
			progressBar = new JProgressBar();
			progressBar.setVisible(false);
			progressBar.setIndeterminate(true);
			progressBar.setStringPainted(true);
			progressBar.setString("Enviando Mail");
			progressBar.setBounds(10, 131, 168, 23);
		}
		return progressBar;
	}
}
