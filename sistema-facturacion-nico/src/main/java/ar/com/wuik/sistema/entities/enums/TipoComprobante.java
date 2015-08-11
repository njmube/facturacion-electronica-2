package ar.com.wuik.sistema.entities.enums;

public enum TipoComprobante {
	FACTURA("F", "FACTURA", 1), NOTA_CREDITO("NC", "NOTA CRÉDITO", 2), NOTA_DEBITO(
			"ND", "NOTA DÉBITO", 3);

	private String value;
	private String descripcion;
	private int id;

	private TipoComprobante(String value, String descripcion, int id) {
		this.value = value;
		this.descripcion = descripcion;
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getValue() {
		return value;
	}

	public int getId() {
		return id;
	}

	public static TipoComprobante fromValue(int id) {
		switch (id) {
		case 1:
			return FACTURA;
		case 2:
			return NOTA_CREDITO;
		case 3:
			return NOTA_DEBITO;
		}
		return null;
	}

}
