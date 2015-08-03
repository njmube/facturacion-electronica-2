package FEV1.dif.afip.gov.ar.entities;

public enum TipoComprobanteEnum {

	FACTURA_A(1), NOTA_DEBITO_A(2), NOTA_CREDITO_A(3), FACTURA_B(6), NOTA_DEBITO_B(
			7), NOTA_CREDITO_B(8);

	private int id;

	private TipoComprobanteEnum(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static TipoComprobanteEnum fromValue(int idTipoComp) {
		switch (idTipoComp) {
		case 1:
			return FACTURA_A;
		case 2:
			return NOTA_DEBITO_A;
		case 3:
			return NOTA_CREDITO_A;
		case 6:
			return FACTURA_B;
		case 7:
			return NOTA_DEBITO_B;
		case 8:
			return NOTA_CREDITO_B;
		}
		return null;
	}

}
