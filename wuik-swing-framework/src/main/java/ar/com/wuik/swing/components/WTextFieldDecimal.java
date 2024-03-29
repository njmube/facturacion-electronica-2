package ar.com.wuik.swing.components;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

import javax.swing.JTextField;

public class WTextFieldDecimal extends JTextField {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 6489470619885871880L;
	private int integers;
	private int decimals;
	private boolean negativeAllowed = Boolean.FALSE;

	public WTextFieldDecimal(int integers, int decimals) {
		this(integers, decimals, false);
	}

	public WTextFieldDecimal(int integers, int decimals, boolean negativeAllowed) {
		setHorizontalAlignment(JTextField.RIGHT);
		addKeyListener(new KeyListenerDecimal());
		this.integers = integers;
		this.decimals = decimals;
		this.negativeAllowed = negativeAllowed;
	}

	public WTextFieldDecimal() {
		setHorizontalAlignment(JTextField.RIGHT);
		addKeyListener(new KeyListenerDecimal());
		this.integers = 7;
		this.decimals = 2;
	}

	class KeyListenerDecimal extends KeyAdapter {

		/**
		 * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
		 */
		@Override
		public void keyTyped(KeyEvent event) {

			if (event.getKeyChar() != KeyEvent.VK_BACK_SPACE) {

				final JTextField component = (JTextField) event.getSource();
				final String decimalNumber = component.getText()
						+ event.getKeyChar();
				String regExp = ((negativeAllowed) ? "^-?" : "^") +"\\d{0," + integers + "}+\\.?\\d{0,"
						+ decimals + "}$";
				boolean match = Pattern.matches(regExp, decimalNumber);
				if (!match) {
					event.consume();
				}
			}
		}

	}
}
