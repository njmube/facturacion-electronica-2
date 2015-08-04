package ar.com.wuik.sistema.entities.enums;

import java.math.BigDecimal;

import ar.com.wuik.swing.components.WOption;

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

}
