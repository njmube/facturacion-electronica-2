package ar.com.wuik.sistema.model;

import java.math.BigDecimal;

import ar.com.wuik.sistema.reportes.entities.DetalleResumenCuentaDTO;
import ar.com.wuik.sistema.utils.AppUtils;
import ar.com.wuik.swing.components.table.WTableModel;
import ar.com.wuik.swing.utils.WUtils;

public class DetalleResumenCuentaModel extends WTableModel<DetalleResumenCuentaDTO> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public DetalleResumenCuentaModel() {
		super(new String[] { "FECHA", "NRO. COMP.", "FECHA VTO.", "DEBE",
				"HABER", "SALDO" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.16, 0.20, 0.16, 0.16, 0.16, 0.16 };
	}

	@Override
	protected Object[] getRow(DetalleResumenCuentaDTO t, Object[] fila) {
		fila[0] = WUtils.getStringFromDate(t.getFecha());
		fila[1] = t.getNroComprobante();
		fila[2] = WUtils.getStringFromDate(t.getFechaVto());
		fila[3] = (null != t.getDebe()) ? AppUtils.formatPeso(t.getDebe()) : null;
		fila[4] = (null != t.getHaber()) ? AppUtils.formatPeso(t.getHaber()) : null;
		fila[5] = AppUtils.formatPeso(t.getSaldo());
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
		case 4:
			return BigDecimal.class;
		case 5:
			return BigDecimal.class;
		}
		return Object.class;
	}

}
