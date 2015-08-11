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
		super(new String[] { "CODIGO", "PRODUCTO/DETALLE", "CANT.",
				"PRECIO X U", "SUBTOTAL", "% IVA", "TOTAL" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.10, 0.47, 0.05, 0.10, 0.10, 0.08, 0.10 };
	}

	@Override
	protected Object[] getRow(DetalleComprobante t, Object[] fila) {
		fila[0] = (null != t.getProducto()) ? t.getProducto().getCodigo() : "0";
		fila[1] = (null != t.getProducto()) ? t.getProducto().getDescripcion()
				: t.getDetalle();
		fila[2] = t.getCantidad();
		fila[3] = AppUtils.formatPeso(t.getPrecio());
		fila[4] = AppUtils.formatPeso(WUtils.getValue(t.getSubtotal()));
		fila[5] = t.getTipoIVA().getDescripcion();
		fila[6] = AppUtils.formatPeso(WUtils.getValue(t.getTotal()));
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
		}
		return Object.class;
	}

}
