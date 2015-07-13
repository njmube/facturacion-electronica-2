package ar.com.wuik.swing.components;

import java.util.Locale;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class WTextFieldLimit extends PlainDocument {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -172417759959320086L;
	private int limit;
	private boolean retainCase;

	public WTextFieldLimit(final int limit) {
		this.limit = limit;
	}

	public WTextFieldLimit(final int limit, final boolean retainCase) {
		this.limit = limit;
		this.retainCase = retainCase;
	}

	public void insertString(final int offset, final String str,
			final AttributeSet attr) throws BadLocationException {
		if (str == null) {
			return;
		}

		if ((getLength() + str.length()) <= limit) {
			if (retainCase) {
				super.insertString(offset, str, attr);
			} else {
				super.insertString(offset,
						str.toUpperCase(Locale.getDefault()), attr);
			}
		}
	}

	@Override
	public void replace(final int offset, final int length, final String str,
			final AttributeSet attrs) throws BadLocationException {
		if (str == null) {
			return;
		}

		if ((getLength() + str.length()) <= limit) {
			if (retainCase) {
				super.replace(offset, length, str, attrs);
			} else {
				super.replace(offset, length,
						str.toUpperCase(Locale.getDefault()), attrs);
			}
		}
	}
}
