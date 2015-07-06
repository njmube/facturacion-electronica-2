package ar.com.wuik.swing.components.security;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import ar.com.wuik.swing.frames.WCalendarIFrame;

public class WMenuItemSecurity extends JMenuItem {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -2933533552991316716L;

	private final String permiso;

	public WMenuItemSecurity(final String text, final String permiso,
			final Icon keyIcon) {
			super(text, (null == keyIcon) ? new ImageIcon(WCalendarIFrame.class
					.getResource("/icons/emptyItem.png")) : keyIcon);
		
		this.permiso = permiso;
		this.setVisible(Boolean.FALSE);
	}

	public String getPermiso() {
		return permiso;
	}

}
