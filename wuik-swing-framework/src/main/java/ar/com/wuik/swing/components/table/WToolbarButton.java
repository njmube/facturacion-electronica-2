package ar.com.wuik.swing.components.table;

import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;

import ar.com.wuik.swing.utils.WUtils;


public final class WToolbarButton extends JButton {


	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = - 4694035973070175712L;
	private String permiso;

	public WToolbarButton(String tooltip, Icon image, ActionListener actionListener, String text, String permiso) {
		super( image );
		if ( WUtils.isNotEmpty( text ) ) {
			setText( text );
		}
		setToolTipText( tooltip );
		addActionListener( actionListener );
		setFocusable( Boolean.FALSE );
		this.permiso = permiso;
	}

	/**
	 * @return the permiso
	 */
	public String getPermiso() {
		return permiso;
	}

	/**
	 * @param permiso
	 *            the permiso to set
	 */
	public void setPermiso( String permiso ) {
		this.permiso = permiso;
	}

}
