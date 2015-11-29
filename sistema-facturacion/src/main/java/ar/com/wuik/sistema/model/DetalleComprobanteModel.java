package ar.com.wuik.sistema.model;

import java.math.BigDecimal;

import ar.com.wuik.sistema.bo.ProductoBO;
import ar.com.wuik.sistema.entities.DetalleComprobante;
import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.sistema.entities.enums.TipoIVAEnum;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.table.WTableModel;
import ar.com.wuik.swing.utils.WUtils;

public class DetalleComprobanteModel extends WTableModel<DetalleComprobante> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public DetalleComprobanteModel() {
		super(new String[] { "CÓDIGO", "PRODUCTO", "CANT.", "PREC. UNIT.",
				"ALICUOTA", "IMPORTE IVA", "SUBTOTAL  C/IVA" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.10, 0.40, 0.08, 0.10, 0.10, 0.08, 0.15 };
	}

	@Override
	protected Object[] getRow(DetalleComprobante t, Object[] fila) {
		fila[0] = (null != t.getProducto()) ? t.getProducto().getCodigo() : "0";
		fila[1] = (null != t.getProducto()) ? t.getProducto().getDescripcion()
				: t.getDetalle();
		fila[2] = t.getCantidad();
		fila[3] = t.getPrecio();
		fila[4] = t.getTipoIVA().getImporte();
		fila[5] = WUtils.getValue(t.getTotalIVA());
		fila[6] = WUtils.getValue(t.getSubtotalConIVA());
		fila[7] = t;
		return fila;
	}

	@Override
	protected void updatedData(int row, int col) {

		Object[] fila = getItems().get(row);

		DetalleComprobante t = (DetalleComprobante) fila[7];

		fila[0] = (null != t.getProducto()) ? t.getProducto().getCodigo() : "0";
		fila[1] = (null != t.getProducto()) ? t.getProducto().getDescripcion()
				: t.getDetalle();
		fila[2] = t.getCantidad();
		fila[3] = t.getPrecio();
		fila[4] = t.getTipoIVA().getImporte();
		fila[5] = WUtils.getValue(t.getTotalIVA());
		fila[6] = WUtils.getValue(t.getSubtotalConIVA());
		fila[7] = t;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		DetalleComprobante detalle = (DetalleComprobante) getItems().get(
				rowIndex)[7];

		switch (columnIndex) {
		case 0:
			String codigo = aValue.toString().toUpperCase();
			ProductoBO productoBO = AbstractFactory
					.getInstance(ProductoBO.class);
			try {
				Producto producto = productoBO.obtenerPorCodigo(codigo);
				if (null != producto) {
					if (!existeDetalleProductoCargado(producto.getId())) {
						detalle.setProducto(producto);
						detalle.setTipoIVA(producto.getTipoIVA());
						fireTableCellUpdated(rowIndex, columnIndex);
					}
				} else {
					if (!codigo.equals("0")) {
						modelListener.genericEvent(codigo);
					} else {
						detalle.setTipoIVA(TipoIVAEnum.IVA_21);
						// detalle.setCodigo(codigo);
						fireTableCellUpdated(rowIndex, columnIndex);
					}

				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			break;
		case 1:
			String desc = (String) aValue;
			detalle.setDetalle(desc.toUpperCase());
			fireTableCellUpdated(rowIndex, columnIndex);
			// event.dataChanged(this.detalles);
			break;
		case 2:
			Integer cantidad = (Integer) aValue;
			if (null == cantidad) {
				cantidad = 1;
			}
			detalle.setCantidad(cantidad);
			fireTableCellUpdated(rowIndex, columnIndex);
			// event.dataChanged(this.detalles);
			break;
		case 3:
			detalle.setPrecio((BigDecimal) aValue);
			fireTableCellUpdated(rowIndex, columnIndex);
			// event.dataChanged(this.detalles);
			break;
		case 4:
			TipoIVAEnum tipoIva = TipoIVAEnum.fromMonto((BigDecimal) aValue);
			if (null != tipoIva) {
				detalle.setTipoIVA(tipoIva);
				fireTableCellUpdated(rowIndex, columnIndex);
				// event.dataChanged(this.detalles);
			}
			break;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		DetalleComprobante detalle = (DetalleComprobante) getItems().get(
				rowIndex)[7];

		Producto p = detalle.getProducto();

		if (null == p) {
			return columnIndex == 0 || columnIndex == 1 || columnIndex == 2
					|| columnIndex == 3 || columnIndex == 4;
		} else {
			return columnIndex == 0 || columnIndex == 2 || columnIndex == 3;
		}
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

	private boolean existeDetalleProductoCargado(Long idProducto) {
		for (Object[] data : rows) {
			DetalleComprobante detalle = (DetalleComprobante) data[7];
			if (null != detalle.getProducto()
					&& idProducto.equals(detalle.getProducto().getId())) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
}
