package ar.com.wuik.sistema.model;

import java.math.BigDecimal;

import ar.com.wuik.sistema.entities.Recibo;
import ar.com.wuik.sistema.utils.AppUtils;
import ar.com.wuik.swing.components.table.WTableModel;
import ar.com.wuik.swing.utils.WUtils;

public class ReciboModel extends WTableModel<Recibo> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public ReciboModel() {
		super(new String[] { "NRO", "FECHA", "TOTAL" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.40, 0.25, 0.35 };
	}

	@Override
	protected Object[] getRow(Recibo t, Object[] fila) {
		fila[0] = t.getNumero();
		fila[1] = WUtils.getStringFromDate(t.getFecha());
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
			return BigDecimal.class;
		}
		return Object.class;
	}

}
