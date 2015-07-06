package FEV1.dif.afip.gov.ar.entities;

public enum TipoConcepto {

	PRODUCTO(1), SERVICIO(2), PRODUCTO_SERVICIO(3);

	private int id;

	private TipoConcepto(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
