package FEV1.dif.afip.gov.ar.entities;

import java.math.BigDecimal;

public enum TipoMonedaEnum {

	PESOS_ARGENTINOS("PES", BigDecimal.ONE);

	private String id;
	private BigDecimal cotizacion;

	private TipoMonedaEnum(String id, BigDecimal cotizacion) {
		this.id = id;
		this.cotizacion = cotizacion;
	}

	public String getId() {
		return id;
	}

	public BigDecimal getCotizacion() {
		return cotizacion;
	}

}
