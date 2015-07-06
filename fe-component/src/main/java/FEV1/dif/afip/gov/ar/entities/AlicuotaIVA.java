package FEV1.dif.afip.gov.ar.entities;

import java.math.BigDecimal;

public class AlicuotaIVA {

	private TipoIVA tipoIVA;
	private BigDecimal baseImponible;
	private BigDecimal totalAlicuota;

	public TipoIVA getTipoIVA() {
		return tipoIVA;
	}

	public void setTipoIVA(TipoIVA tipoIVA) {
		this.tipoIVA = tipoIVA;
	}

	public BigDecimal getBaseImponible() {
		return baseImponible;
	}

	public void setBaseImponible(BigDecimal baseImponible) {
		this.baseImponible = baseImponible;
	}

	public BigDecimal getTotalAlicuota() {
		return totalAlicuota;
	}

	public void setTotalAlicuota(BigDecimal totalAlicuota) {
		this.totalAlicuota = totalAlicuota;
	}

}
