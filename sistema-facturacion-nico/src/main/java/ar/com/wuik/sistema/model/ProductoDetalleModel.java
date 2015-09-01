package ar.com.wuik.sistema.model;

import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.swing.components.table.WTableModel;

public class ProductoDetalleModel extends WTableModel<Producto> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public ProductoDetalleModel() {
		super(new String[] {"DESCRIPCION", "T. PRODUCTO"});
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.60, 0.40 };
	}

	@Override
	protected Object[] getRow(Producto t, Object[] fila) {
		fila[0] = t.getDescripcion();
		fila[1] = t.getTipoProducto().getNombre();
		fila[2] = t.getId();
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
