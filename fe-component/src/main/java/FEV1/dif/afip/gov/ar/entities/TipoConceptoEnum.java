package FEV1.dif.afip.gov.ar.entities;

public enum TipoConceptoEnum {

	PRODUCTO(1), SERVICIO(2), PRODUCTO_SERVICIO(3);

	private int id;

	private TipoConceptoEnum(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static TipoConceptoEnum fromValue(int concepto) {
		switch (concepto) {
		case 1:
			return PRODUCTO;
		case 2:
			return SERVICIO;
		case 3:
			return PRODUCTO_SERVICIO;
		}
		return null;
	}

}
