package ar.com.wuik.sistema.model;

import ar.com.wuik.sistema.bo.ProductoBO;
import ar.com.wuik.sistema.entities.DetalleRemito;
import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.components.table.WTableModel;

public class DetalleRemitoModel extends WTableModel<DetalleRemito> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public DetalleRemitoModel() {
		super(new String[] { "CODIGO", "PRODUCTO", "CANTIDAD" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.10, 0.75, 0.15 };
	}

	@Override
	protected Object[] getRow(DetalleRemito t, Object[] fila) {
		fila[0] = (null != t.getProducto()) ? t.getProducto().getCodigo() : "";
		fila[1] = (null != t.getProducto()) ? t.getProducto().getDescripcion()
				: "";
		fila[2] = t.getCantidad();
		fila[3] = t;
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
		}
		return Object.class;
	}

	@Override
	protected void updatedData(int row, int col) {

		Object[] fila = getItems().get(row);

		DetalleRemito t = (DetalleRemito) fila[3];

		fila[0] = t.getProducto().getCodigo();
		fila[1] = t.getProducto().getDescripcion();
		fila[2] = t.getCantidad();
		fila[3] = t;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		DetalleRemito detalle = (DetalleRemito) getItems().get(rowIndex)[3];

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
						fireTableCellUpdated(rowIndex, columnIndex);
					}
				}
			} catch (BusinessException e) {
				e.printStackTrace();
			}
			break;
		case 2:
			Integer cantidad = (Integer) aValue;
			if (null == cantidad) {
				cantidad = 1;
			}
			detalle.setCantidad(cantidad);
			fireTableCellUpdated(rowIndex, columnIndex);
			break;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		DetalleRemito detalle = (DetalleRemito) getItems().get(rowIndex)[3];

		Producto p = detalle.getProducto();

		if (null == p) {
			return columnIndex == 0;
		} else {
			return columnIndex == 0 || columnIndex == 2;
		}
	}

	private boolean existeDetalleProductoCargado(Long idProducto) {
		for (Object[] data : rows) {
			DetalleRemito detalle = (DetalleRemito) data[3];
			if (null != detalle.getProducto()
					&& idProducto.equals(detalle.getProducto().getId())) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
}
