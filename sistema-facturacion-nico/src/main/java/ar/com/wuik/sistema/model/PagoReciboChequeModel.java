package ar.com.wuik.sistema.model;

import java.math.BigDecimal;

import ar.com.wuik.sistema.entities.PagoReciboCheque;
import ar.com.wuik.sistema.utils.AppUtils;
import ar.com.wuik.swing.components.table.WTableModel;
import ar.com.wuik.swing.utils.WUtils;

public class PagoReciboChequeModel extends WTableModel<PagoReciboCheque> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public PagoReciboChequeModel() {
		super(new String[] { "NUMERO", "BANCO", "FECHA COBRO","IMPORTE" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.25, 0.25, 0.25, 0.25};
	}

	@Override
	protected Object[] getRow(PagoReciboCheque t, Object[] fila) {
		fila[0] = t.getCheque().getNumero();
		fila[1] = t.getCheque().getBanco().getNombre();
		fila[2] = WUtils.getStringFromDate(t.getCheque().getFechaPago());
		fila[3] = AppUtils.formatPeso(t.getCheque().getImporte());
		fila[4] = t.getCoalesceId();
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
