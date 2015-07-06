package ar.com.wuik.swing.frames;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import ar.com.wuik.swing.components.WModel;
import ar.com.wuik.swing.components.WTextFieldLimit;
import ar.com.wuik.swing.listeners.WGenericSelectorListener;

public class WGenericSelectorIFrame extends WAbstractModelIFrame {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 1811337764816169998L;
	private JPanel jContentPane = null;
	private JButton btnCerrar = null;
	private JButton btnAceptar = null;
	private JLabel lblNewLabel;
	private JTextField textField;

	/**
	 * This is the xxx default constructor
	 */
	public WGenericSelectorIFrame(final String title, final String labelText,
			final WGenericSelectorListener listener) {
		this(title, labelText, listener, 30);
	}

	public WGenericSelectorIFrame(final String title, final String labelText,
			final WGenericSelectorListener listener, final int textLimitSize) {
		setFrameIcon(new ImageIcon(
				WGenericSelectorIFrame.class.getResource("/icons/add.png")));
		initialize();
		setTitle(title);
		lblNewLabel.setText(labelText);
		textField.setDocument(new WTextFieldLimit(textLimitSize));

		ActionListener thisListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listener.selectedText(textField.getText());
				hideFrame();
			}
		};
		getBtnAceptar().addActionListener(thisListener);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(367, 113);
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getBtnCerrar(), null);
			jContentPane.add(getBtnAceptar(), null);
			jContentPane.add(getLblNewLabel());
			jContentPane.add(getTextField());
		}
		return jContentPane;
	}

	/**
	 * This method initializes btnCerrar
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnCerrar() {
		if (btnCerrar == null) {
			btnCerrar = new JButton();
			btnCerrar.setIcon(new ImageIcon(WGenericSelectorIFrame.class
					.getResource("/icons/cancel.png")));
			btnCerrar.setBounds(new Rectangle(147, 44, 100, 25));
			btnCerrar.setText("Cerrar");
			btnCerrar.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					hideFrame();
				}
			});
		}
		return btnCerrar;
	}

	/**
	 * This method initializes btnAceptar
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton();
			btnAceptar.setIcon(new ImageIcon(WGenericSelectorIFrame.class
					.getResource("/icons/ok.png")));
			btnAceptar.setBounds(new Rectangle(250, 44, 100, 25));
			btnAceptar.setText("Aceptar");
		}
		return btnAceptar;
	}

	private JLabel getLblNewLabel() {
		if (lblNewLabel == null) {
			lblNewLabel = new JLabel("");
			lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			lblNewLabel.setBounds(10, 8, 74, 20);
		}
		return lblNewLabel;
	}

	private JTextField getTextField() {
		if (textField == null) {
			textField = new JTextField();
			textField.setBounds(94, 8, 256, 20);
			textField.setColumns(10);
		}
		return textField;
	}

	
	
	/**
	 * @see ar.com.wuik.swing.frames.WAbstractIFrame#enterPressed()
	 */
	@Override
	public void enterPressed() {
		getBtnAceptar().doClick();
	}

	/**
	 * @see ar.com.wuik.swing.frames.WAbstractModelIFrame#getFocusComponent()
	 */
	@Override
	protected JComponent getFocusComponent() {
		return getTextField();
	}
	
	/**
	 * @see ar.com.wuik.swing.frames.WAbstractModelIFrame#validateModel(ar.com.wuik.swing.components.WModel)
	 */
	@Override
	protected boolean validateModel(WModel model) {
		return false;
	}
}
