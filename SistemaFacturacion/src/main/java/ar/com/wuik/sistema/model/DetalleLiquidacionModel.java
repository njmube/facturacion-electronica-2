package ar.com.wuik.sistema.model;

import java.math.BigDecimal;

import ar.com.wuik.sistema.entities.Liquidacion;
import ar.com.wuik.sistema.utils.AppUtils;
import ar.com.wuik.swing.components.table.WTableModel;
import ar.com.wuik.swing.utils.WUtils;

public class DetalleLiquidacionModel extends WTableModel<Liquidacion> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public DetalleLiquidacionModel() {
		super(new String[] { "FECHA", "TIPO COMP.", "COMPROBANTE", "TOTAL" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.15, 0.25, 0.35, 0.25 };
	}

	@Override
	protected Object[] getRow(Liquidacion t, Object[] fila) {
		fila[0] = WUtils.getStringFromDate(t.getFecha());
		fila[1] = t.getTipo();
		fila[2] = t.getComprobante();
		fila[3] = AppUtils.formatPeso(t.getTotal());
		fila[4] = t.getId();
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
			return String.class;
		case 3:
			return BigDecimal.class;
		}
		return Object.class;
	}

}
