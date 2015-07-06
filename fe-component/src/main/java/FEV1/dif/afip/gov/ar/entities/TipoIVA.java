package FEV1.dif.afip.gov.ar.entities;

public enum TipoIVA {

	IVA_0(3), IVA_10_5(4), IVA_21(5), IVA_27(6), IVA_5(8), IVA_2_5(9);

	private int id;

	private TipoIVA(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
