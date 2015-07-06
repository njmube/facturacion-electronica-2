package ar.com.wuik.sistema.model;

import java.math.BigDecimal;

import ar.com.wuik.sistema.entities.Cheque;
import ar.com.wuik.swing.components.table.WTableModel;
import ar.com.wuik.swing.utils.WUtils;

public class ChequeModel extends WTableModel<Cheque> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public ChequeModel() {
		super(new String[] { "NUMERO", "BANCO", "FECHA EMISIÓN", "FECHA COBRO",
				"RECIBIDO", "IMPORTE", "ESTADO" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.10, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10 };
	}

	@Override
	protected Object[] getRow(Cheque t, Object[] fila) {
		fila[0] = t.getNumero();
		fila[1] = t.getBanco().getNombre();
		fila[2] = WUtils.getStringFromDate(t.getFechaEmision());
		fila[3] = WUtils.getStringFromDate(t.getFechaPago());
		fila[4] = t.getRecibidoDe();
		fila[5] = t.getImporte();
		fila[6] = t.getId();
		return fila;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return String.class;
		case 1:
			return BigDecimal.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		case 4:
			return String.class;
		case 5:
			return String.class;
		}
		return Object.class;
	}

}
