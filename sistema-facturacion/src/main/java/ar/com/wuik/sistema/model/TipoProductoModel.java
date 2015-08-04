package ar.com.wuik.sistema.model;

import ar.com.wuik.sistema.entities.TipoProducto;
import ar.com.wuik.swing.components.table.WTableModel;

public class TipoProductoModel extends WTableModel<TipoProducto> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public TipoProductoModel() {
		super(new String[] { "DESCRIPCIÓN" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.100 };
	}

	@Override
	protected Object[] getRow(TipoProducto t, Object[] fila) {
		fila[0] = t.getNombre();
		fila[1] = t.getId();
		return fila;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return String.class;
		}
		return Object.class;
	}

}
