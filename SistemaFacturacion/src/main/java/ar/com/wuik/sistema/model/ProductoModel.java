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
		super(new String[] { "CODIGO", "DESCRIPCION", "T. PRODUCTO",
				"UBICACION", "COSTO", "IVA", "PRECIO" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.10, 0.30, 0.15, 0.15, 0.10, 0.10, 0.10 };
	}

	@Override
	protected Object[] getRow(Producto t, Object[] fila) {
		fila[0] = t.getCodigo();
		fila[1] = t.getDescripcion();
		fila[2] = t.getTipoProducto().getNombre();
		fila[3] = t.getUbicacion();
		fila[4] = t.getCosto();
		fila[5] = t.getIva();
		fila[6] = t.getPrecio();
		fila[7] = t.getId();
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
