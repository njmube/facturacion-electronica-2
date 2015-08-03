package ar.com.wuik.sistema.model;

import java.math.BigDecimal;

import FEV1.dif.afip.gov.ar.entities.Comprobante;
import ar.com.wuik.sistema.utils.AppUtils;
import ar.com.wuik.swing.components.table.WTableModel;
import ar.com.wuik.swing.utils.WUtils;

public class ComprobanteModel extends WTableModel<Comprobante> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public ComprobanteModel() {
		super(new String[] { "NRO", "CAE", "VTO. CAE", "F. VTA.", "SUBTOTAL",
				"TOTAL IVA", "TOTAL", "ESTADO AFIP", "CLIENTE" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.11, 0.12, 0.09, 0.08, 0.10, 0.10, 0.10, 0.10,
				0.20 };
	}

	@Override
	protected Object[] getRow(Comprobante t, Object[] fila) {
		fila[0] = (WUtils.isNotEmpty(t.getNroComprobanteFormato())) ? t
				.getNroComprobanteFormato() : t.getNroComprobante();
		fila[1] = t.getCae();
		fila[2] = WUtils.getStringFromDate(t.getFechaVtoCAE());
		fila[3] = WUtils.getStringFromDate(t.getFecha());
		fila[4] = AppUtils.formatPeso(WUtils.getValue(t.getImporteSubtotal()));
		fila[5] = AppUtils.formatPeso(WUtils.getValue(t.getImporteIVA()));
		fila[6] = AppUtils.formatPeso(WUtils.getValue(t.getImporteTotal()));
		fila[7] = t.getEstado();
		fila[8] = t.getDocTipo().toString() + " " + t.getDocNro();
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
		case 7:
			return String.class;
		case 8:
			return String.class;
		}
		return Object.class;
	}

}
