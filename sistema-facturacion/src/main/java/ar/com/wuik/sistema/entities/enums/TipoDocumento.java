package ar.com.wuik.sistema.entities.enums;

public enum TipoDocumento {

	CUIT(1);
//	CUIL(2), DNI(3), OTROS(4);

	private int id;

	private TipoDocumento(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static TipoDocumento fromValue(int docTipo) {
		switch (docTipo) {
		case 1:
			return CUIT;
//		case 2:
//			return CUIL;
//		case 3:
//			return DNI;
//		case 4:
//			return OTROS;
		}
		return null;
	}

}
