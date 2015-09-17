package ar.com.wuik.sistema.model;

import java.math.BigDecimal;

import ar.com.wuik.sistema.entities.DetalleComprobante;
import ar.com.wuik.sistema.utils.AppUtils;
import ar.com.wuik.swing.components.table.WTableModel;
import ar.com.wuik.swing.utils.WUtils;

public class DetalleComprobanteModel extends WTableModel<DetalleComprobante> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public DetalleComprobanteModel() {
		super(new String[] { "PRODUCTO/DETALLE", "CANT.",
				"PRECIO X U", "PRECIO C/IVA", "SUBTOTAL", "% IVA", "TOTAL" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.47, 0.05, 0.10, 0.10, 0.10, 0.08, 0.10 };
	}

	@Override
	protected Object[] getRow(DetalleComprobante t, Object[] fila) {
		fila[0] = (null != t.getProducto()) ? t.getProducto().getDescripcion()
				: t.getDetalle();
		fila[1] = t.getCantidad();
		fila[2] = AppUtils.formatPeso(WUtils.getRoundedValue(t.getPrecio()));
		fila[3] = AppUtils.formatPeso(WUtils.getRoundedValue(t.getPrecioConIVA()));
		fila[4] = AppUtils.formatPeso(WUtils.getRoundedValue(t.getSubtotal()));
		fila[5] = t.getTipoIVA().getDescripcion();
		fila[6] = AppUtils.formatPeso(WUtils.getRoundedValue(t.getTotal()));
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
		case 6:
			return BigDecimal.class;
		case 7:
			return BigDecimal.class;
		}
		return Object.class;
	}

}
