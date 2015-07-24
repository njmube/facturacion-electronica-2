package ar.com.wuik.sistema.model;

import java.math.BigDecimal;

import ar.com.wuik.sistema.entities.NotaCredito;
import ar.com.wuik.sistema.utils.AppUtils;
import ar.com.wuik.swing.components.table.WTableModel;
import ar.com.wuik.swing.utils.WUtils;

public class NotaCreditoModel extends WTableModel<NotaCredito> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public NotaCreditoModel() {
		super(new String[] { "NRO","CAE", "VTO. CAE", "FECHA", "SUBTOTAL",
				"IVA", "TOTAL", "ESTADO" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.08, 0.13, 0.10, 0.10, 0.12, 0.12, 0.12, 0.19 };
	}

	@Override
	protected Object[] getRow(NotaCredito t, Object[] fila) {
		fila[0] = t.getNroComprobante();
		fila[1] = t.getCae();
		fila[2] = WUtils.getStringFromDate(t.getFechaCAE());
		fila[3] = WUtils.getStringFromDate(t.getFechaVenta());
		fila[4] = AppUtils.formatPeso(WUtils.getValue(t.getSubtotal()));
		fila[5] = AppUtils.formatPeso(WUtils.getValue(t.getIva()));
		fila[6] = AppUtils.formatPeso(WUtils.getValue(t.getTotal()));
		fila[7] = t.getEstado();
		fila[8] = t.getId();
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
