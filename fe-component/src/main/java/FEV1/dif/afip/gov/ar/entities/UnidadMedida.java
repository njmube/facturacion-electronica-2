package FEV1.dif.afip.gov.ar.entities;

public enum UnidadMedida {

	UNIDAD(7), LITROS(5), KILOS(1);

	private int id;

	private UnidadMedida(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
