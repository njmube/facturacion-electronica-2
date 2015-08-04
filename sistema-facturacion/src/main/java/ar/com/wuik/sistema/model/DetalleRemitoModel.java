package ar.com.wuik.sistema.model;

import ar.com.wuik.sistema.entities.DetalleRemito;
import ar.com.wuik.swing.components.table.WTableModel;

public class DetalleRemitoModel extends WTableModel<DetalleRemito> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public DetalleRemitoModel() {
		super(new String[] { "CODIGO", "PRODUCTO", "CANTIDAD"});
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.10, 0.75, 0.15 };
	}

	@Override
	protected Object[] getRow(DetalleRemito t, Object[] fila) {
		fila[0] = t.getProducto().getCodigo();
		fila[1] = t.getProducto().getDescripcion();
		fila[2] = t.getCantidad();
		fila[3] = t.getCoalesceId();
		return fila;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return String.class;
		case 1:
			return String.class;
		case 2:
			return Integer.class;
		}
		return Object.class;
	}

}
