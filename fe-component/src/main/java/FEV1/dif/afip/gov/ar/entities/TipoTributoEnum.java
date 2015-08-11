package FEV1.dif.afip.gov.ar.entities;

public enum TipoTributoEnum {

	OTRO(99), IMP_NACIONAL(1), IMP_PROV(2), IMP_MUNICIPAL(3), IMP_INTERNO(4);

	private int id;

	private TipoTributoEnum(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
