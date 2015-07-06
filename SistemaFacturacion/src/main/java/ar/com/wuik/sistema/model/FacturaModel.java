package ar.com.wuik.sistema.model;

import java.math.BigDecimal;

import ar.com.wuik.sistema.entities.Factura;
import ar.com.wuik.sistema.utils.AppUtils;
import ar.com.wuik.swing.components.table.WTableModel;
import ar.com.wuik.swing.utils.WUtils;

public class FacturaModel extends WTableModel<Factura> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public FacturaModel() {
		super(new String[] { "NÚMERO", "FECHA EMISIÓN", "FECHA VTO.",
				"TOTAL ($)", "RESTO ($)", "ESTADO" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.20, 0.16, 0.16, 0.16, 0.16, 0.16 };
	}

	@Override
	protected Object[] getRow(Factura t, Object[] fila) {
		fila[0] = t.getNumero();
		fila[1] = WUtils.getStringFromDate(t.getFechaVenta());
		fila[2] = WUtils.getStringFromDate(t.getFechaVto());
		fila[3] = AppUtils.formatPeso(WUtils.getValue(t.getTotal()));
		fila[4] = AppUtils.formatPeso(WUtils.getValue(t.getTotal()));
		fila[5] = getEstado(t);
		fila[6] = t.getId();
		return fila;
	}

	private String getEstado(Factura t) {
		// if (t.isActivo()) {
		// return t.getEstado() + " - " + t.getEstadoPago();
		// } else {
		// return t.getEstado();
		// }
		return "";
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return String.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		case 4:
			return BigDecimal.class;
		case 5:
			return BigDecimal.class;
		case 6:
			return BigDecimal.class;
		case 7:
			return BigDecimal.class;
		case 8:
			return BigDecimal.class;
		case 9:
			return String.class;
		}
		return Object.class;
	}

}
