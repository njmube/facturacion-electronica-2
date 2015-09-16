package ar.com.wuik.sistema.entities.enums;

import java.math.BigDecimal;

public enum TipoIVAEnum {

	IVA_105(1, "10.5 %", BigDecimal.valueOf(10.5), BigDecimal.valueOf(0.105)), IVA_21(
			2, "21 %", BigDecimal.valueOf(21), BigDecimal.valueOf(0.21));

	private String descripcion;
	private BigDecimal importe;
	private BigDecimal importeDecimal;
	private long id;

	private TipoIVAEnum(long id, String descripcion, BigDecimal importe,
			BigDecimal importeDecimal) {
		this.descripcion = descripcion;
		this.importe = importe;
		this.id = id;
		this.importeDecimal = importeDecimal;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public long getId() {
		return id;
	}

	public static TipoIVAEnum fromValue(int id) {
		switch (id) {
		case 1:
			return IVA_105;
		case 2:
			return IVA_21;
		}
		return null;
	}

	public BigDecimal getImporteDecimal() {
		return importeDecimal;
	}

}
