package ar.com.wuik.sistema.entities.enums;

public enum TipoTributo {

	PER_RET_IMP_GANANCIAS(1, "PER./RET. IMP. GANANCIAS"), PER_RET_IVA(2,
			"PER./RET. IVA"), PER_RET_ING_BRUTOS(3, "PER./RET. ING. BRUTOS"), IMP_INTERNOS(
			4, "IMP. INTERNOS"), IMP_MUNIC(5, "IMP. MUNICIPALES"), OTROS(6,
			"OTROS");

	private int id;
	private String descripcion;

	private TipoTributo(int id, String descripcion) {
		this.id = id;
		this.descripcion = descripcion;
	}

	public int getId() {
		return id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public static TipoTributo fromValue(int id) {
		switch (id) {
		case 1:
			return PER_RET_IMP_GANANCIAS;
		case 2:
			return PER_RET_IVA;
		case 3:
			return PER_RET_ING_BRUTOS;
		case 4:
			return IMP_INTERNOS;
		case 5:
			return IMP_MUNIC;
		case 6:
			return OTROS;
		}
		return null;
	}

}
