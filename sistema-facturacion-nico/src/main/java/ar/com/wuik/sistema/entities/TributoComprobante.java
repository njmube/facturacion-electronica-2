package ar.com.wuik.sistema.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.com.wuik.sistema.entities.enums.TipoTributo;

@Entity
@Table(name = "tributos_comprobantes")
public class TributoComprobante extends BaseEntity {

	@Column(name = "DETALLE")
	private String detalle;
	@Column(name = "ALICUOTA")
	private BigDecimal alicuota;
	@Column(name = "IMPORTE")
	private BigDecimal importe;
	@Column(name = "BASE_IMPONIBLE")
	private BigDecimal baseImporte;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_COMPROBANTE", nullable = true)
	private Comprobante comprobante;
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_TRIBUTO")
	private TipoTributo tributo;
	@Transient
	private Long temporalId;

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

	public Comprobante getComprobante() {
		return comprobante;
	}

	public void setComprobante(Comprobante comprobante) {
		this.comprobante = comprobante;
	}

	public TipoTributo getTributo() {
		return tributo;
	}

	public void setTributo(TipoTributo tributo) {
		this.tributo = tributo;
	}

	public Long getCoalesceId() {
		return (null != getId()) ? getId() : temporalId;
	}

	public Long getTemporalId() {
		return temporalId;
	}

	public void setTemporalId(Long temporalId) {
		this.temporalId = temporalId;
	}

}
