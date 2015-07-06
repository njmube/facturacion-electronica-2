package FEV1.dif.afip.gov.ar.entities;

public enum TipoComprobante {

	FACTURA_A(1), NOTA_DEBITO_A(2), NOTA_CREDITO_A(3), FACTURA_B(6), NOTA_DEBITO_B(
			7), NOTA_CREDITO_B(8);

	private int id;

	private TipoComprobante(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
