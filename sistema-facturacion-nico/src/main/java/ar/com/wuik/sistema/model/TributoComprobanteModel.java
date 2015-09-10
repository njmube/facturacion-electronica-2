package ar.com.wuik.sistema.model;

import java.math.BigDecimal;

import ar.com.wuik.sistema.entities.TributoComprobante;
import ar.com.wuik.sistema.utils.AppUtils;
import ar.com.wuik.swing.components.table.WTableModel;

public class TributoComprobanteModel extends WTableModel<TributoComprobante> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public TributoComprobanteModel() {
		super(new String[] { "TRIBUTO", "DETALLE", "IMPORTE" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.40, 0.40, 0.20 };
	}

	@Override
	protected Object[] getRow(TributoComprobante t, Object[] fila) {
		fila[0] = t.getTributo().getDescripcion();
		fila[1] = t.getDetalle();
		fila[2] = AppUtils.formatPeso(t.getImporte());
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
			return BigDecimal.class;
		}
		return Object.class;
	}

}
