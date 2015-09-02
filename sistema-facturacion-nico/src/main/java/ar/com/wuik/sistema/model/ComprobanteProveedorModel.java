package ar.com.wuik.sistema.model;

import java.math.BigDecimal;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import ar.com.wuik.sistema.entities.Comprobante;
import ar.com.wuik.sistema.entities.enums.EstadoFacturacion;
import ar.com.wuik.sistema.utils.AppUtils;
import ar.com.wuik.swing.components.table.WTableModel;
import ar.com.wuik.swing.utils.WUtils;

public class ComprobanteProveedorModel extends WTableModel<Comprobante> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public ComprobanteProveedorModel() {
		super(new String[] { "TIPO/NRO COMP", "CAE", "VTO. CAE", "F. VTA.",
				"SUBTOTAL", "TOTAL IVA", "OTROS IMP.", "TOTAL" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.11, 0.12, 0.09, 0.08, 0.10, 0.10, 0.10, 0.10};
	}

	@Override
	protected Object[] getRow(Comprobante t, Object[] fila) {
		fila[0] = ((WUtils.isNotEmpty(t.getNroCompFormato())) ? t
				.getTipoComprobante().getValue() + t.getNroCompFormato()
				: (WUtils.isNotEmpty(t.getNroComprobante()) ? t
						.getTipoComprobante().getValue()
						+ t.getNroComprobante() : ""));
		fila[1] = t.getCae();
		fila[2] = WUtils.getStringFromDate(t.getFechaCAE());
		fila[3] = WUtils.getStringFromDate(t.getFechaVenta());
		fila[4] = AppUtils.formatPeso(WUtils.getValue(t.getSubtotal()));
		fila[5] = AppUtils.formatPeso(WUtils.getValue(t.getIva()));
		fila[6] = AppUtils.formatPeso(WUtils.getValue(t.getTotalTributos()));

		BigDecimal totalTributos = (null != t.getTotalTributos()) ? t
				.getTotalTributos() : BigDecimal.ZERO;

		fila[7] = AppUtils.formatPeso(WUtils.getValue(t.getTotal()));
		fila[8] = t.getId();
		return fila;
	}



	@Override
	public ar.com.wuik.swing.components.table.WTableModel.Aligment getAligment(
			int columnIndex) {

		switch (columnIndex) {
		case 2:
			return Aligment.MIDDLE;
		case 3:
			return Aligment.MIDDLE;
		case 7:
			return Aligment.MIDDLE;
		}
		return super.getAligment(columnIndex);
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
		case 7:
			return BigDecimal.class;

		}
		return Object.class;
	}

}
