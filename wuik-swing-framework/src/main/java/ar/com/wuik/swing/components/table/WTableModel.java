package ar.com.wuik.swing.components.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;


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

	public WTableModel(String[] columns) {
		this.columns = columns;
		this.columnCount = columns.length + 1;
		this.rows = new ArrayList<Object[]>();
	}

	@Override
	public final int getColumnCount() {
		return ( null != columns ) ? columns.length : 0;
	}

	@Override
	public String getColumnName( int column ) {
		return ( null != columns ) ? columns[column] : "";
	}

	@Override
	public final int getRowCount() {
		return ( null != rows ) ? rows.size() : 0;
	}

	@Override
	public final Object getValueAt( int row, int column ) {
		return rows.get( row )[column];
	}

	public final List<Object[]> getSelectedItems( int[] selectedRows ) {
		List<Object[]> items = new ArrayList<Object[]>();
		if ( null != selectedRows && selectedRows.length > 0 ) {
			for ( int i = 0; i < selectedRows.length; i++ ) {
				items.add( rows.get( selectedRows[i] ) );
			}
		}
		return items;
	}

	public final List<Object[]> getItems() {
		List<Object[]> items = new ArrayList<Object[]>();
		for ( int i = 0; i < rows.size(); i++ ) {
			items.add( rows.get( i ) );
		}
		return items;
	}

	public final void addData( List<T> data, boolean keepData ) {
		if ( null != data ) {

			if ( ! keepData ) {
				rows.clear();
			}
			for ( int i = 0; i < data.size(); i++ ) {
				Object[] row = new Object[columnCount];
				T t = data.get( i );
				rows.add( getRow( t, row ) );
			}
		}
		super.fireTableStructureChanged();
	}

	public final List<Long> getSelectedItemsID( int[] selectedRows ) {
		List<Long> ids = new ArrayList<Long>();
		Integer indexID = getIndexID();
		if ( null != selectedRows && selectedRows.length > 0 ) {
			for ( int i = 0; i < selectedRows.length; i++ ) {
				ids.add( (Long) rows.get( selectedRows[i] )[indexID] );
			}
		}
		return ids;
	}

	public abstract double[] getColumnPercentSize();

	protected abstract Object[] getRow( T t, Object[] fila );

	/**
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public abstract Class<?> getColumnClass( int columnIndex );

	protected Integer getIndexID() {
		return columnCount - 1;
	}
}
