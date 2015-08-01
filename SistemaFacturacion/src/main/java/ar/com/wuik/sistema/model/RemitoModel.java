package ar.com.wuik.sistema.model;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import ar.com.wuik.sistema.entities.Remito;
import ar.com.wuik.swing.components.table.WTableModel;
import ar.com.wuik.swing.utils.WUtils;

public class RemitoModel extends WTableModel<Remito> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public RemitoModel() {
		super(new String[] { "NUMERO", "FECHA", "CANT. PRODUCTOS", "FACTURA/CAE",
				"ESTADO" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] {0.15, 0.15, 0.20, 0.40, 0.10 };
	}

	@Override
	protected Object[] getRow(Remito t, Object[] fila) {
		fila[0] = t.getNumero();
		fila[1] = WUtils.getStringFromDate(t.getFecha());
		fila[2] = t.getCantidad();
		fila[3] = (null != t.getFactura())? t.getFactura().getCae() : "";
		fila[4] =  t.isActivo() ? new ImageIcon(this.getClass().getResource(
				"/icons/activo.png")) : new ImageIcon(this.getClass()
				.getResource("/icons/inactivo.png"));
		fila[5] = t.getId();
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
			return Integer.class;
		case 3:
			return String.class;
		case 4:
			return Icon.class;
		}
		return Object.class;
	}

}
