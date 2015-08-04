package ar.com.wuik.sistema.model;

import ar.com.wuik.sistema.entities.StockProducto;
import ar.com.wuik.swing.components.table.WTableModel;
import ar.com.wuik.swing.utils.WUtils;

public class StockProductoModel extends WTableModel<StockProducto> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public StockProductoModel() {
		super(new String[] { "FECHA", "CANTIDAD", "PROVEEDOR", "FACTURA" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.25, 0.25, 0.25, 0.25 };
	}

	@Override
	protected Object[] getRow(StockProducto t, Object[] fila) {
		fila[0] = WUtils.getStringFromDate(t.getFecha());
		fila[1] = t.getCantidad();
		fila[2] = t.getProveedor().getRazonSocial();
		fila[3] = t.getFactura();
		fila[4] = t.getId();
		return fila;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return String.class;
		case 1:
			return Integer.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		}
		return Object.class;
	}

}
