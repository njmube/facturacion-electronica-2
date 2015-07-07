package ar.com.wuik.sistema.model;

import java.math.BigDecimal;

import ar.com.wuik.sistema.entities.DetalleFactura;
import ar.com.wuik.swing.components.table.WTableModel;
import ar.com.wuik.swing.utils.WUtils;

public class DetalleFacturaModel extends WTableModel<DetalleFactura> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public DetalleFacturaModel() {
		super(new String[] { "CODIGO", "PRODUCTO", "CANTIDAD", "PRECIO UNIT.",
				"SUBTOTAL", "ALICUOTA IVA", "SUBTOTAL C/IVA" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.10, 0.32, 0.08, 0.12, 0.12, 0.12, 0.14 };
	}

	@Override
	protected Object[] getRow(DetalleFactura t, Object[] fila) {
		fila[0] = t.getProducto().getCodigo();
		fila[1] = t.getProducto().getDescripcion();
		fila[2] = t.getCantidad();
		fila[3] = t.getPrecio();
		fila[4] = WUtils.getValue(t.getSubtotal());
		fila[5] = t.getIva();
		fila[6] = WUtils.getValue(t.getTotal());
		fila[7] = t.getCoalesceId();
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
			return BigDecimal.class;
		case 4:
			return BigDecimal.class;
		case 5:
			return BigDecimal.class;
		}
		return Object.class;
	}

}
