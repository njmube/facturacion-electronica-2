package ar.com.wuik.sistema.model;

import java.math.BigDecimal;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import ar.com.wuik.sistema.entities.NotaCredito;
import ar.com.wuik.sistema.utils.AppUtils;
import ar.com.wuik.swing.components.table.WTableModel;
import ar.com.wuik.swing.utils.WUtils;

public class NotaCreditoModel extends WTableModel<NotaCredito> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public NotaCreditoModel() {
		super(new String[] { "NRO","CAE", "VTO. CAE", "FECHA", "SUBTOTAL",
				"IVA", "TOTAL", "ESTADO" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.10, 0.12, 0.08, 0.09, 0.18, 0.17, 0.20, 0.06 };
	}

	@Override
	protected Object[] getRow(NotaCredito t, Object[] fila) {
		fila[0] = (WUtils.isNotEmpty(t.getNroCompFormato())) ? t.getNroCompFormato() : t.getNroComprobante();
		fila[1] = t.getCae();
		fila[2] = WUtils.getStringFromDate(t.getFechaCAE());
		fila[3] = WUtils.getStringFromDate(t.getFechaVenta());
		fila[4] = AppUtils.formatPeso(WUtils.getValue(t.getSubtotal()));
		fila[5] = AppUtils.formatPeso(WUtils.getValue(t.getIva()));
		fila[6] = AppUtils.formatPeso(WUtils.getValue(t.getTotal()));
		fila[7] =  t.isActivo() ? new ImageIcon(this.getClass().getResource(
				"/icons/activo.png")) : new ImageIcon(this.getClass()
				.getResource("/icons/inactivo.png"));
		fila[8] = t.getId();
		return fila;
	}
	
	@Override
	public ar.com.wuik.swing.components.table.WTableModel.Aligment getAligment(
			int columnIndex) {
		
		switch (columnIndex) {
		case 0:
			return Aligment.MIDDLE;
		case 1:
			return Aligment.MIDDLE;
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
			return Icon.class;
		}
		return Object.class;
	}

}
