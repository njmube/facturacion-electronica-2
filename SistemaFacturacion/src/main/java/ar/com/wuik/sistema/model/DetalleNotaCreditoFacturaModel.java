package ar.com.wuik.sistema.model;

import ar.com.wuik.sistema.entities.Factura;
import ar.com.wuik.sistema.utils.AppUtils;
import ar.com.wuik.swing.components.table.WTableModel;
import ar.com.wuik.swing.utils.WUtils;

public class DetalleNotaCreditoFacturaModel extends WTableModel<Factura> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public DetalleNotaCreditoFacturaModel() {
		super(new String[] { "CAE", "FECHA", "TOTAL" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.40, 0.30, 0.30 };
	}

	@Override
	protected Object[] getRow(Factura t, Object[] fila) {
		fila[0] = t.getCae();
		fila[1] = WUtils.getStringFromDate(t.getFechaVenta());
		fila[2] = AppUtils.formatPeso(t.getTotal());
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
