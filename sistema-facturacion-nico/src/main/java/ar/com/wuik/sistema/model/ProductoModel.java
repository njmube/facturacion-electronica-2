package ar.com.wuik.sistema.model;

import java.math.BigDecimal;

import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.swing.components.table.WTableModel;

public class ProductoModel extends WTableModel<Producto> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public ProductoModel() {
		super(new String[] { "DESCRIPCION", "T. PRODUCTO", "IVA" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.50, 0.30, 0.20 };
	}

	@Override
	protected Object[] getRow(Producto t, Object[] fila) {
		fila[0] = t.getDescripcion();
		fila[1] = t.getTipoProducto().getNombre();
		fila[2] = t.getTipoIVA().getDescripcion();
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
			return String.class;
		case 3:
			return String.class;
		case 4:
			return BigDecimal.class;
		case 5:
			return BigDecimal.class;
		case 6:
			return BigDecimal.class;
		}
		return Object.class;
	}

}
