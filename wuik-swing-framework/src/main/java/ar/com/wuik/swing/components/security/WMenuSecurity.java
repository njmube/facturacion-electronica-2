package ar.com.wuik.swing.components.security;

import java.util.List;

import javax.swing.Icon;
import javax.swing.JMenu;

import ar.com.wuik.swing.utils.WUtils;

public class WMenuSecurity extends JMenu {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -6234948281866245468L;

	public WMenuSecurity(final String text, final Icon icon,
			final WMenuItemSecurity... itemSecurities) {
		super(text);
		if (null != icon) {
			this.setIcon(icon);
			this.setRolloverIcon(icon);
			this.setSelectedIcon(icon);
			this.setRolloverSelectedIcon(icon);
		}
		if (WUtils.isNotEmpty(itemSecurities)) {
			for (WMenuItemSecurity menuItemSecurity : itemSecurities) {
				this.add(menuItemSecurity);
			}
		}
		evaluateVisibility();
	}
	
	public void addItem(final WMenuItemSecurity menuItemSecurity){
		this.add(menuItemSecurity);
		evaluateVisibility();
	}

	public void evaluateMenuItems(final List<String> permisos) {
		if (WUtils.isNotEmpty(permisos)) {
			final int itemsCount = this.getItemCount();
			for (int i = 0; i < itemsCount; i++) {
				final WMenuItemSecurity menuItem = (WMenuItemSecurity) this
						.getItem(i);
				if (null == menuItem.getPermiso()) {
					menuItem.setVisible(Boolean.TRUE);
				} else {
					if (permisos.contains(menuItem.getPermiso())) {
						menuItem.setVisible(Boolean.TRUE);
					} else {
						menuItem.setVisible(Boolean.FALSE);
					}
				}
			}
			evaluateVisibility();
		}
	}

	public void restore() {
		final int itemsCount = this.getItemCount();
		for (int i = 0; i < itemsCount; i++) {
			final WMenuItemSecurity menuItem = (WMenuItemSecurity) this
					.getItem(i);
			if (null != menuItem.getPermiso()) {
				menuItem.setVisible(Boolean.FALSE);
			}
		}
		evaluateVisibility();
	}

	private void evaluateVisibility() {
		final int itemsCount = this.getItemCount();
		int countItInvisble = 0;
		for (int i = 0; i < itemsCount; i++) {
			final WMenuItemSecurity menuItem = (WMenuItemSecurity) this
					.getItem(i);
			if (!menuItem.isVisible()) {
				countItInvisble++;
			}
		}
		this.setVisible(countItInvisble != itemsCount);
	}

}
