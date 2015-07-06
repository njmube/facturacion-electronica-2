package ar.com.wuik.sistema.model;

import ar.com.wuik.sistema.entities.Usuario;
import ar.com.wuik.swing.components.table.WTableModel;

public class UsuarioModel extends WTableModel<Usuario> {

	/**
	 * Serial UID.
	 */
	private static final long serialVersionUID = -3277760177146580417L;

	public UsuarioModel() {
		super(new String[] { "DNI", "NOMBRE", "ACTIVO" });
	}

	@Override
	public double[] getColumnPercentSize() {
		return new double[] { 0.25, 0.65, 0.10};
	}

	@Override
	protected Object[] getRow(Usuario t, Object[] fila) {
		fila[0] = t.getDni();
		fila[1] = t.getNombre();
		fila[2] = (t.isActivo()) ? "SI" : "NO";
		fila[3] = t.getId();
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
		}
		return Object.class;
	}

}
