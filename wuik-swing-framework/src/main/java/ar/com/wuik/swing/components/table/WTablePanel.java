package ar.com.wuik.swing.components.table;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.swing.components.security.WSecure;
import ar.com.wuik.swing.events.WTableStatisticsUpdaterEvent;
import ar.com.wuik.swing.listeners.WModelListener;
import ar.com.wuik.swing.listeners.WTableListener;
import ar.com.wuik.swing.utils.WFrameUtils;
import ar.com.wuik.swing.utils.WFrameUtils.FontSize;
import ar.com.wuik.swing.utils.WUtils;

/**
 * Panel that contains the WTable, Toolbar Buttons and Statistics.
 * 
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 * @param <T>
 */
public final class WTablePanel<T> extends JPanel implements WSecure {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3451191125189600094L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WTablePanel.class);
	private static final String ITEMS_ENCONTRADOS = " Items Encontrados";
	private JLabel lblStatistics;
	private JScrollPane scrollPane;
	private WTable<T> table;
	private JPanel toolbar;

	public WTablePanel(Class<WTableModel<T>> modelClass, String title,
			boolean showStatistics) {
		getTable(modelClass);
		setBorder(new TitledBorder(null, title, TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		setLayout(new BorderLayout(0, 0));
		add(getScrollPane(), BorderLayout.CENTER);
		add(getToolbar(), BorderLayout.NORTH);
		if (showStatistics) {
			add(getLblStatistics(), BorderLayout.SOUTH);
		}
		initialize(showStatistics);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("WTablePanel - [INITIALIZED] - [MODEL-CLASS: "
					+ modelClass + "]");
		}
	}

	public WTablePanel(Class<WTableModel<T>> modelClass, String title) {
		this(modelClass, title, Boolean.FALSE);
	}

	public WTablePanel(Class<WTableModel<T>> modelClass) {
		this(modelClass, "Resultados");
	}

	public WTablePanel(Class<WTableModel<T>> modelClass, boolean showStatistics) {
		this(modelClass, "Resultados", showStatistics);
	}

	public WTablePanel() {
	}

	private void initialize(boolean showStatistics) {
		if (showStatistics) {

			table.addStatisticsUpdaterEvent(new WTableStatisticsUpdaterEvent() {

				@Override
				public void updateStatistics(int rowCount) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("WTablePanel - [ROW-COUNT: " + rowCount
								+ "]");
					}
					lblStatistics.setText(rowCount + ITEMS_ENCONTRADOS);
					lblStatistics.setFont(WFrameUtils
							.getCustomFont(FontSize.SMALL));
				}
			});
		}
	}

	private JLabel getLblStatistics() {
		if (lblStatistics == null) {
			lblStatistics = new JLabel();
			lblStatistics.setHorizontalAlignment(JLabel.CENTER);
		}
		return lblStatistics;
	}

	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(table);
		}
		return scrollPane;
	}

	private WTable<T> getTable(Class<WTableModel<T>> modelClass) {
		if (table == null) {
			table = new WTable<T>(modelClass);
		}
		return table;
	}

	public WTable<T> getTable() {
		return table;
	}

	/**
	 * Agrega un WTableListener.
	 * 
	 * @param tableListener
	 *            WTableListener<T> - El listener.
	 */
	public void addWTableListener(WTableListener tableListener) {
		table.addWListener(tableListener);
	}
	
	public void addWModelListener(WModelListener modelListener){
		table.addWModelListener(modelListener);
	}

	/**
	 * Agrega los registros a la WTable.
	 * 
	 * @param data
	 *            List<T> - La Lista de registros.
	 */
	public void addData(List<T> data) {
		table.addData(data, Boolean.FALSE);
	}

	public void addData(Set<T> data) {
		if (null != data) {
			List<T> dataParse = new ArrayList<T>(data);
			table.addData(dataParse, Boolean.FALSE);
		} else {
			table.addData(new ArrayList<T>(), Boolean.FALSE);
		}
	}
	
	public void removeSelection(){
		table.removeSelection();
	}

	public void addDataNoRemove(List<T> data) {
		table.addData(data, Boolean.TRUE);
	}

	public List<Object[]> getSelectedItems() {
		return table.getSelectedItems();
	}

	public List<Long> getSelectedItemsID() {
		return table.getSelectedItemsID();
	}

	public Long getSelectedItemID() {
		return table.getSelectedItemID();
	}

	public Object[] getSelectedItem() {
		return table.getSelectedItem();
	}

	public List<Object[]> getItems() {
		return table.getItems();
	}

	private JPanel getToolbar() {
		if (toolbar == null) {
			toolbar = new JPanel();
			toolbar.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 3));
		}
		return toolbar;
	}

	public void addToolbarButtons(List<WToolbarButton> toolbarButtons) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("WTablePanel - Adding Toolbar Buttons");
		}
		if (WUtils.isNotEmpty(toolbarButtons)) {
			for (WToolbarButton toolbarButton : toolbarButtons) {
				toolbar.add(toolbarButton);
			}
		}
	}

	public void setReadOnly() {
		int compCount = toolbar.getComponentCount();
		for (int i = 0; i < compCount; i++) {
			Component c = toolbar.getComponent(i);
			if (c instanceof WToolbarButton) {
				((WToolbarButton) c).setVisible(Boolean.FALSE);
			}
		}
	}

	/**
	 * @see ar.com.wuik.swing.components.security.WSecure#applySecurity(java.util.List)
	 */
	@Override
	public void applySecurity(List<String> permisos) {
		Component[] components = getToolbar().getComponents();
		for (Component component : components) {
			if (component instanceof WToolbarButton) {
				WToolbarButton toolbarButton = (WToolbarButton) component;
				if (WUtils.isNotEmpty(toolbarButton.getPermiso())
						&& !permisos.contains(toolbarButton.getPermiso())) {
					toolbarButton.setVisible(Boolean.FALSE);
				}
			}
		}
	}
	
}
