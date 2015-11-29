package ar.com.wuik.sistema.entities.enums;

import java.math.BigDecimal;

public enum TipoIVAEnum {

	IVA_105(1, "10.5 %", BigDecimal.valueOf(10.5)), IVA_21(2, "21 %",
			BigDecimal.valueOf(21));

	private String descripcion;
	private BigDecimal importe;
	private long id;

	private TipoIVAEnum(long id, String descripcion, BigDecimal importe) {
		this.descripcion = descripcion;
		this.importe = importe;
		this.id = id;
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

	@Override
	public String toString() {
		return this.getDescripcion();
	}

	public static TipoIVAEnum fromValue(String aValue) {
		if (aValue.equals(IVA_21.descripcion)) {
			return TipoIVAEnum.IVA_21;
		} else if (aValue.equals(IVA_105.descripcion)) {
			return TipoIVAEnum.IVA_105;
		}
		return null;
	}

	public static TipoIVAEnum fromMonto(BigDecimal aValue) {
		if (IVA_105.importe.equals(aValue)) {
			return IVA_105;
		} else if (IVA_21.importe.equals(aValue)) {
			return IVA_21;
		}
		return null;
	}

}
