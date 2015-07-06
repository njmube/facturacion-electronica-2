package ar.com.wuik.swing.components;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

public class WTextFieldNumeric extends JTextField {
	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 6489470619885871880L;

	public WTextFieldNumeric() {
		setHorizontalAlignment(JTextField.RIGHT);
		addKeyListener(new KeyListenerNumeric());
	}

	class KeyListenerNumeric extends KeyAdapter {

		/**
		 * @see java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
		 */
		@Override
		public void keyTyped(KeyEvent event) {
			if (!Character.isDigit(event.getKeyChar())) {
				event.consume();
			}
		}

	}

}
