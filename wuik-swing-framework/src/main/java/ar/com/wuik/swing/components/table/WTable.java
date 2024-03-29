package ar.com.wuik.swing.components.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.swing.events.WTableStatisticsUpdaterEvent;
import ar.com.wuik.swing.listeners.WModelListener;
import ar.com.wuik.swing.listeners.WTableListener;
import ar.com.wuik.swing.utils.WUtils;

/**
 * Representa una Custom Table que provee diferentes metodos para el manejo de
 * datos.
 * 
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 * @param <T>
 */
public final class WTable<T> extends JTable {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 5604926016621909830L;
	private static final Logger LOGGER = LoggerFactory.getLogger(WTable.class);
	private WTableStatisticsUpdaterEvent statisticsUpdater;
	private static final double FACTOR = 10000;

	/**
	 * Constructor.
	 * 
	 * @param modelClass
	 *            Class<WTableModel<T>> - La Clase que representa el Model de la
	 *            WTabla.
	 */
	public WTable(Class<WTableModel<T>> modelClass) {
		try {
			setModel(modelClass.newInstance());
		} catch (InstantiationException iexc) {
			LOGGER.error("Error al instanciar clase [ " + modelClass.getName()
					+ " ]", iexc);
		} catch (IllegalAccessException iaexc) {
			LOGGER.error("Error al instanciar clase [ " + modelClass.getName()
					+ " ]", iaexc);
		}
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		WTableModel<T> model = (WTableModel<T>) getModel();
		int columnCount = getModel().getColumnCount();
		Class<?> columnClass = null;
		for (int i = 0; i < columnCount; i++) {
			columnClass = getModel().getColumnClass(i);
			DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) getDefaultRenderer(columnClass);
			if (null != renderer) {
				if (columnClass.equals(BigDecimal.class)
						|| columnClass.equals(Long.class)
						|| columnClass.equals(Integer.class)) {
					renderer.setHorizontalAlignment(SwingConstants.RIGHT);
					getColumnModel().getColumn(i).setCellRenderer(renderer);
				}
			}
		}
		resizeColumns();
		resizeRows();
		// showColumnsSize();
	}

	/**
	 * @see javax.swing.JTable#changeSelection(int, int, boolean, boolean)
	 */
	@Override
	public void changeSelection(int row, int column, boolean toggle,
			boolean extend) {
		super.changeSelection(row, column, toggle, extend);

		if (editCellAt(row, column)) {
			Component editor = getEditorComponent();
			editor.requestFocusInWindow();
		}
	}

	/**
	 * //TODO: Describir el metodo resizeRows
	 */
	private void resizeRows() {
		setRowHeight(22);
	}

	private void showColumnsSize() {
		this.getColumnModel().addColumnModelListener(
				new TableColumnModelListener() {

					@Override
					public void columnSelectionChanged(ListSelectionEvent e) {
					}

					@Override
					public void columnRemoved(TableColumnModelEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void columnMoved(TableColumnModelEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void columnMarginChanged(ChangeEvent e) {

						final TableColumnModel tModel = getColumnModel();
						int columns = ((WTableModel) getModel())
								.getColumnPercentSize().length;
						String medidas = "";
						double sumaTotal = 0;
						for (int columnIndex = 0; columnIndex < columns; columnIndex++) {
							final TableColumn column = tModel
									.getColumn(columnIndex);
							BigDecimal size = BigDecimal.valueOf((column
									.getPreferredWidth() / FACTOR));
							size = size.setScale(2, RoundingMode.HALF_EVEN);
							medidas += size.toEngineeringString() + ", ";
							sumaTotal += size.doubleValue() * 100;
						}

						LOGGER.info(medidas + "   TOTAL: " + sumaTotal);
					}

					@Override
					public void columnAdded(TableColumnModelEvent e) {
						// TODO Auto-generated method stub

					}
				});
	}

	/**
	 * //TODO: Describir el metodo resizeColumns
	 */
	private void resizeColumns() {
		WTableModel<T> wTableModel = getWTableModel();
		final TableColumnModel tModel = getColumnModel();
		double[] columnsSize = wTableModel.getColumnPercentSize();
		for (int columnIndex = 0; columnIndex < columnsSize.length; columnIndex++) {
			final TableColumn column = tModel.getColumn(columnIndex);
			column.setPreferredWidth((int) (columnsSize[columnIndex] * FACTOR));
		}
	}

	/**
	 * Establece el Evento de Estadisticas.
	 * 
	 * @param statisticsUpdater
	 *            WTableStatisticsUpdaterEvent - El Evento.
	 * @see ar.com.wuik.swing.events.WTableStatisticsUpdaterEvent
	 */
	public void addStatisticsUpdaterEvent(
			WTableStatisticsUpdaterEvent statisticsUpdater) {
		this.statisticsUpdater = statisticsUpdater;
	}

	/**
	 * Agrega los registros a la WTable.
	 * 
	 * @param data
	 *            List<T> - La Lista de registros.
	 */
	public void addData(List<T> data, boolean keepData) {
		WTableModel<T> model = getWTableModel();
		model.addData(data, keepData);
		if (null != statisticsUpdater) {
			statisticsUpdater.updateStatistics(model.getRowCount());
		}
		resizeColumns();
	}

	/**
	 * Obtiene los Items seleccionados de la WTable.
	 * 
	 * @return Los Items seleccionados.
	 */
	public List<Object[]> getSelectedItems() {
		WTableModel<T> model = getWTableModel();
		return model.getSelectedItems(getSelectedRows());
	}

	/**
	 * Obtiene los Items seleccionados de la WTable, mediante un Point.
	 * 
	 * @param point
	 *            Point - El Point.
	 * @return Los Items seleccionados.
	 */
	public List<Object[]> getSelectedItems(Point point) {
		WTableModel<T> model = getWTableModel();
		return model.getSelectedItems(new int[] { rowAtPoint(point) });
	}

	public List<Long> getSelectedItemsID() {
		WTableModel<T> model = getWTableModel();
		return model.getSelectedItemsID(getSelectedRows());
	}

	public List<Long> getSelectedItemsID(Point point) {
		WTableModel<T> model = getWTableModel();
		return model.getSelectedItemsID(new int[] { rowAtPoint(point) });
	}

	/**
	 * Obtiene el Item seleccionado, si se han seleccionado mas de uno, entonces
	 * retornara el primero.
	 * 
	 * @returnEl Item seleccionado.
	 */
	public Object[] getSelectedItem() {
		List<Object[]> items = getSelectedItems();
		if (WUtils.isNotEmpty(items)) {
			if (items.size() > 1) {
				return null;
			} else {
				return items.get(0);
			}
		}
		return null;
	}

	/**
	 * Obtiene el Item seleccionado mediante un Point, si se han seleccionado
	 * mas de uno, entonces retornara el primero.
	 * 
	 * @returnEl Item seleccionado.
	 */
	public Object[] getSelectedItem(Point point) {
		List<Object[]> items = getSelectedItems(point);
		if (WUtils.isNotEmpty(items)) {
			if (items.size() > 1) {
				return null;
			} else {
				return items.get(0);
			}
		}
		return null;
	}

	public Long getSelectedItemID() {
		List<Long> ids = getSelectedItemsID();
		if (WUtils.isNotEmpty(ids)) {
			if (ids.size() > 1) {
				return null;
			} else {
				return ids.get(0);
			}
		}
		return null;
	}

	public Long getSelectedItemID(Point point) {
		List<Long> ids = getSelectedItemsID(point);
		if (WUtils.isNotEmpty(ids)) {
			if (ids.size() > 1) {
				return null;
			} else {
				return ids.get(0);
			}
		}
		return null;
	}

	/**
	 * Obtiene el Modelo de Tabla.
	 * 
	 * @return El Modelo de Tabla.
	 */
	@SuppressWarnings("unchecked")
	private WTableModel<T> getWTableModel() {
		return (WTableModel<T>) getModel();
	}

	/**
	 * Agrega un WTableListener.
	 * 
	 * @param tableListener
	 *            WTableListener<T> - El listener.
	 */
	public void addWListener(final WTableListener tableListener) {
		if (null != tableListener) {
			addMouseListener(new MouseAdapter() {

				@SuppressWarnings("unchecked")
				@Override
				public void mousePressed(MouseEvent event) {
					if (event.getClickCount() == 2) {
						WTable<T> target = (WTable<T>) event.getSource();
						Point point = event.getPoint();
						tableListener.doubleClickListener(target
								.getSelectedItem(point));
					} else if (event.getClickCount() == 1) {
						WTable<T> target = (WTable<T>) event.getSource();
						Point point = event.getPoint();
						tableListener.singleClickListener(target
								.getSelectedItem(point));
					}
				}
			});
		}
	}

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row,
			int col) {
		Component comp = super.prepareRenderer(renderer, row, col);

		WTableModel<T> model = (WTableModel<T>) getModel();
		if (model.isColumnColored(col)) {
			Color columnColor = model.getColumnColor(row, col);
			Object value = getModel().getValueAt(row, col);
			comp.setBackground(columnColor);
		}
		return comp;
	}

	/**
	 * Obtiene todos los Items de la WTable.
	 * 
	 * @return La lista total de Items.
	 */
	public List<Object[]> getItems() {
		WTableModel<T> model = getWTableModel();
		return model.getItems();
	}

	/**
	 * //TODO: Describir el metodo removeSelection
	 */
	public void removeSelection() {
		WTableModel<T> model = getWTableModel();
		int[] selection = getSelectedRows();
		for (int index : selection) {
			model.rows.remove(index);
		}
		model.fireTableStructureChanged();
		resizeColumns();
	}

	/**
	 * //TODO: Describir el metodo addWModelListener
	 * 
	 * @param modelListener
	 */
	public void addWModelListener(WModelListener modelListener) {
		if (null != modelListener) {
			WTableModel<T> model = getWTableModel();
			model.addModelListener(modelListener);
		}
	}
}
