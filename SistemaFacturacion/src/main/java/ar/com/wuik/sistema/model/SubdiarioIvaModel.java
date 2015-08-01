package ar.com.wuik.sistema.model;

import java.math.BigDecimal;

import ar.com.wuik.sistema.entities.SubdiarioIva;
import ar.com.wuik.sistema.utils.AppUtils;
import ar.com.wuik.swing.components.table.WTableModel;
import ar.com.wuik.swing.utils.WUtils;

public class SubdiarioIvaModel extends WTableModel<SubdiarioIva> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public SubdiarioIvaModel() {
		super(new String[] { "TIPO COMP.", "NRO.", "FECHA", "CLIENTE",
				"TOTAL IVA" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.17, 0.17, 0.12, 0.40, 0.14 };
	}

	@Override
	protected Object[] getRow(SubdiarioIva t, Object[] fila) {
		fila[0] = t.getTipoComprobante();
		fila[1] = t.getNumero();
		fila[2] = WUtils.getStringFromDate(t.getFecha());
		fila[3] = t.getCliente();
		fila[4] = AppUtils.formatPeso(WUtils.getValue(t.getTotalIva()));
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
		}
		return Object.class;
	}

}
