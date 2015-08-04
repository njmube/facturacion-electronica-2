package ar.com.wuik.sistema.model;

import java.math.BigDecimal;

import ar.com.wuik.sistema.entities.DetalleNotaCredito;
import ar.com.wuik.sistema.utils.AppUtils;
import ar.com.wuik.swing.components.table.WTableModel;
import ar.com.wuik.swing.utils.WUtils;

public class DetalleNotaCreditoModel extends WTableModel<DetalleNotaCredito> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public DetalleNotaCreditoModel() {
		super(new String[] { "CODIGO", "PRODUCTO/DETALLE", "CANTIDAD", "PRECIO UNIT.",
				"SUBTOTAL", "ALICUOTA IVA", "SUBTOTAL C/IVA", "COMENTARIO" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.10, 0.18, 0.08, 0.12, 0.12, 0.12, 0.14,0.14 };
	}

	@Override
	protected Object[] getRow(DetalleNotaCredito t, Object[] fila) {
		fila[0] = (null != t.getProducto()) ? t.getProducto().getCodigo() : "0";
		fila[1] = (null != t.getProducto()) ? t.getProducto().getDescripcion()
				: t.getDetalle();
		fila[2] = t.getCantidad();
		fila[3] = AppUtils.formatPeso(t.getPrecio());
		fila[4] = AppUtils.formatPeso(WUtils.getValue(t.getSubtotal()));
		fila[5] = t.getTipoIVA().getDescripcion();
		fila[6] = AppUtils.formatPeso(WUtils.getValue(t.getTotal()));
		fila[7] = t.getComentario();
		fila[8] = t.getCoalesceId();
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
