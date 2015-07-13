package ar.com.wuik.sistema.model;

import java.math.BigDecimal;

import ar.com.wuik.sistema.entities.NotaCredito;
import ar.com.wuik.sistema.entities.NotaDebito;
import ar.com.wuik.sistema.utils.AppUtils;
import ar.com.wuik.swing.components.table.WTableModel;
import ar.com.wuik.swing.utils.WUtils;

public class NotaDebitoModel extends WTableModel<NotaDebito> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public NotaDebitoModel() {
		super(new String[] { "CAE", "VTO. CAE", "FECHA", "SUBTOTAL",
				"IVA", "TOTAL", "ESTADO" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.16, 0.14, 0.14, 0.14, 0.14, 0.14, 0.14 };
	}

	@Override
	protected Object[] getRow(NotaDebito t, Object[] fila) {
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

	private String getEstado(NotaDebito t) {
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
