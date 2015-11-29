package ar.com.wuik.swing.components.table;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import ar.com.wuik.swing.listeners.WModelListener;

/**
 * Represents a Model compatible for WTable.
 * 
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 * @param <T>
 */
public abstract class WTableModel<T> extends AbstractTableModel {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = 3924957079698193886L;
	protected List<Object[]> rows;
	private String[] columns;
	private int columnCount = 0;

	protected enum Aligment {
		LEFT, MIDDLE, RIGHT
	}

	protected WModelListener modelListener;

	public WTableModel(String[] columns) {
		this.columns = columns;
		this.columnCount = columns.length + 1;
		this.rows = new ArrayList<Object[]>();
	}

	@Override
	public final int getColumnCount() {
		return (null != columns) ? columns.length : 0;
	}

	@Override
	public String getColumnName(int column) {
		return (null != columns) ? columns[column] : "";
	}

	@Override
	public final int getRowCount() {
		return (null != rows) ? rows.size() : 0;
	}

	@Override
	public Object getValueAt(int row, int column) {
		return rows.get(row)[column];
	}

	public final List<Object[]> getSelectedItems(int[] selectedRows) {
		List<Object[]> items = new ArrayList<Object[]>();
		if (null != selectedRows && selectedRows.length > 0) {
			for (int i = 0; i < selectedRows.length; i++) {
				items.add(rows.get(selectedRows[i]));
			}
		}
		return items;
	}

	public final List<Object[]> getItems() {
		List<Object[]> items = new ArrayList<Object[]>();
		for (int i = 0; i < rows.size(); i++) {
			items.add(rows.get(i));
		}
		return items;
	}

	public final void addData(List<T> data, boolean keepData) {
		if (null != data) {

			if (!keepData) {
				rows.clear();
			}
			for (int i = 0; i < data.size(); i++) {
				Object[] row = new Object[columnCount];
				T t = data.get(i);
				rows.add(getRow(t, row));
			}
		}
		super.fireTableStructureChanged();
	}

	/**
	 * @see javax.swing.table.AbstractTableModel#fireTableCellUpdated(int, int)
	 */
	@Override
	public void fireTableCellUpdated(int row, int column) {
		updatedData(row, column);
		super.fireTableCellUpdated(row, column);
		if (null != modelListener) {
			modelListener.dataUpdated();
		}
	}

	public final List<Long> getSelectedItemsID(int[] selectedRows) {
		List<Long> ids = new ArrayList<Long>();
		Integer indexID = getIndexID();
		if (null != selectedRows && selectedRows.length > 0) {
			for (int i = 0; i < selectedRows.length; i++) {
				ids.add((Long) rows.get(selectedRows[i])[indexID]);
			}
		}
		return ids;
	}

	public abstract double[] getColumnPercentSize();

	protected Object[] getRow(T t, Object[] fila) {
		return null;
	}

	/**
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public abstract Class<?> getColumnClass(int columnIndex);

	protected Integer getIndexID() {
		return columnCount - 1;
	}

	public Aligment getAligment(int columnIndex) {
		return null;
	}

	public Color getBgColor(int columnIndex) {
		return null;
	}

	public boolean isColumnColored(int col) {
		return Boolean.FALSE;
	}

	public Color getColumnColor(int row, int col) {
		return Color.WHITE;
	}

	protected void updatedData(int row, int col) {

	}

	/**
	 * //TODO: Describir el metodo addModelListener
	 * 
	 * @param modelListener
	 */
	public final void addModelListener(WModelListener modelListener) {
		this.modelListener = modelListener;
	}

}
