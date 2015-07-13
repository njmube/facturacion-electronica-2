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
		super(new String[] { "CAE", "VTO. CAE", "F. VTA.", "SUBTOTAL",
				"TOTAL IVA", "TOTAL", "ESTADO" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.16, 0.10, 0.10, 0.12, 0.12, 0.12, 0.24 };
	}

	@Override
	protected Object[] getRow(Factura t, Object[] fila) {
		fila[0] = t.getCae();
		fila[1] = WUtils.getStringFromDate(t.getFechaCAE());
		fila[2] = WUtils.getStringFromDate(t.getFechaVenta());
		fila[3] = AppUtils.formatPeso(WUtils.getValue(t.getSubtotal()));
		fila[4] = AppUtils.formatPeso(WUtils.getValue(t.getIva()));
		fila[5] = AppUtils.formatPeso(WUtils.getValue(t.getTotal()));
		fila[6] = getEstado(t);
		fila[7] = t.getId();
		return fila;
	}

	private String getEstado(Factura t) {
		if (t.isActivo()) {
			return "ACTIVA"
					+ (WUtils.isEmpty(t.getCae()) ? " - SIN FACTURAR"
							: " - FACTURADA");
		} else {
			return "ANULADA"
					+ (WUtils.isEmpty(t.getCae()) ? " - SIN FACTURAR"
							: " - FACTURADA");
		}
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
		case 4:
			return BigDecimal.class;
		case 5:
			return BigDecimal.class;
		case 6:
			return String.class;
		}
		return Object.class;
	}

}
