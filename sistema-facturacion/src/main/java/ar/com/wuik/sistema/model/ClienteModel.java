package ar.com.wuik.sistema.model;

import java.math.BigDecimal;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.utils.AppUtils;
import ar.com.wuik.swing.components.table.WTableModel;

public class ClienteModel extends WTableModel<Cliente> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public ClienteModel() {
		super(new String[] { "RAZÓN SOCIAL", "CUIT/CUIL/DNI", "DIRECCIÓN", "TELÉFONO", "LOCALIDAD", "COND. IVA", "SALDO", "ESTADO" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.18, 0.12, 0.22, 0.09, 0.09, 0.15, 0.09, 0.06 };
	}

	@Override
	protected Object[] getRow(Cliente t, Object[] fila) {
		fila[0] = t.getRazonSocial();
		fila[1] = t.getTipoDocumento() + " " + t.getDocumento();
		fila[2] = t.getDireccion();
		fila[3] = t.getTelefono();
		fila[4] = t.getLocalidad().getNombre();
		fila[5] = t.getCondicionIVA().getAbreviacion();
		fila[6] = AppUtils.formatPeso(t.getSaldo());
		fila[7] = t.isActivo() ? new ImageIcon(this.getClass().getResource(
				"/icons/activo.png")) : new ImageIcon(this.getClass()
				.getResource("/icons/inactivo.png"));
		fila[8] = t.getId();
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
			return String.class;
		case 5:
			return String.class;
		case 6:
			return BigDecimal.class;
		case 7:
			return Icon.class;
		}
		return Object.class;
	}

}
