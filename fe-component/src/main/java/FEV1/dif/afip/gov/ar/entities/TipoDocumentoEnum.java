package FEV1.dif.afip.gov.ar.entities;

public enum TipoDocumentoEnum {

	CUIT(80), CUIL(86), DNI(96), OTROS(99);

	private int id;

	private TipoDocumentoEnum(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static TipoDocumentoEnum fromValue(int docTipo) {
		switch (docTipo) {
		case 80:
			return CUIT;
		case 86:
			return CUIL;
		case 96:
			return DNI;
		case 99:
			return OTROS;
		}
		return null;
	}

}
