package ar.com.wuik.sistema.model;

import java.math.BigDecimal;

import ar.com.wuik.sistema.entities.Comprobante;
import ar.com.wuik.sistema.utils.AppUtils;
import ar.com.wuik.swing.components.table.WTableModel;
import ar.com.wuik.swing.utils.WUtils;

public class DetalleComprobantesAsocModel extends WTableModel<Comprobante> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public DetalleComprobantesAsocModel() {
		super(new String[] { "TIPO/NRO", "FECHA", "TOTAL" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.40, 0.30, 0.30 };
	}

	@Override
	protected Object[] getRow(Comprobante t, Object[] fila) {
		fila[0] = WUtils.isNotEmpty(t.getNroCompFormato()) ? t
				.getTipoComprobante().getValue() + t.getNroCompFormato() : t
				.getTipoComprobante().getValue() + t.getNroComprobante();
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
			return BigDecimal.class;
		}
		return Object.class;
	}

}
