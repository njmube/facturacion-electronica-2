package FEV1.dif.afip.gov.ar.entities;

public enum TipoDocumento {

	CUIT(80), CUIL(86), DNI(96), OTROS(99);

	private int id;

	private TipoDocumento(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
