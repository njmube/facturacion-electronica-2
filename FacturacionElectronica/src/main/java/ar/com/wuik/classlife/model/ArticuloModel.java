package ar.com.wuik.classlife.model;

import ar.com.wuik.classlife.entities.Articulo;
import ar.com.wuik.swing.components.table.WTableModel;

public class ArticuloModel extends WTableModel<Articulo> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public ArticuloModel() {
		super(new String[] { "CÓD. BARRAS", "ARTÍCULO" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.50, 0.50 };
	}

	@Override
	protected Object[] getRow(Articulo t, Object[] fila) {
		fila[0] = t.getCodigoBarras();
		fila[1] = t.getNombre();
		fila[3] = t.getId();
		return fila;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return String.class;
		case 1:
			return String.class;
		}
		return Object.class;
	}
}
