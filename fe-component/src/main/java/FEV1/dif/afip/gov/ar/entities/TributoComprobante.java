package FEV1.dif.afip.gov.ar.entities;

import java.math.BigDecimal;

import FEV1.dif.afip.gov.ar.TributoTipo;

public class TributoComprobante {

	private String detalle;
	private BigDecimal alicuota;
	private BigDecimal importe;
	private BigDecimal baseImporte;
	private TipoTributoEnum tipoTributoEnum;

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public BigDecimal getAlicuota() {
		return alicuota;
	}

	public void setAlicuota(BigDecimal alicuota) {
		this.alicuota = alicuota;
	}

	public BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(BigDecimal importe) {
		this.importe = importe;
	}

	public BigDecimal getBaseImporte() {
		return baseImporte;
	}

	public void setBaseImporte(BigDecimal baseImporte) {
		this.baseImporte = baseImporte;
	}

	public TipoTributoEnum getTipoTributoEnum() {
		return tipoTributoEnum;
	}

	public void setTipoTributoEnum(TipoTributoEnum tipoTributoEnum) {
		this.tipoTributoEnum = tipoTributoEnum;
	}

}
