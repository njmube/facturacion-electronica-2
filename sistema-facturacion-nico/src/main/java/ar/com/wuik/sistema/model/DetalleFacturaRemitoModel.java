package ar.com.wuik.sistema.model;

import ar.com.wuik.sistema.entities.Remito;
import ar.com.wuik.swing.components.table.WTableModel;
import ar.com.wuik.swing.utils.WUtils;

public class DetalleFacturaRemitoModel extends WTableModel<Remito> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public DetalleFacturaRemitoModel() {
		super(new String[] { "NUMERO", "FECHA", "CANTIDAD" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.34, 0.33, 0.33 };
	}

	@Override
	protected Object[] getRow(Remito t, Object[] fila) {
		fila[0] = t.getNumero();
		fila[1] = WUtils.getStringFromDate(t.getFecha());
		fila[2] = t.getCantidad();
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
		case 2:
			return Integer.class;
		}
		return Object.class;
	}

}
