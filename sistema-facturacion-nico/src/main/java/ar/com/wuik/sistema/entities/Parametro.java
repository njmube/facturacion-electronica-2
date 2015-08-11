package ar.com.wuik.sistema.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "parametros")
public class Parametro extends BaseEntity {

	@Column(name = "NRO_RECIBO")
	private Long nroRecibo;
	@Column(name = "NRO_FACTURA_A")
	private Long nroFacturaA;
	@Column(name = "NRO_NOTA_CREDITO_A")
	private Long nroNotaCreditoA;
	@Column(name = "NRO_NOTA_DEBITO_A")
	private Long nroNotaDebitoA;
	@Column(name = "NRO_FACTURA_B")
	private Long nroFacturaB;
	@Column(name = "NRO_NOTA_CREDITO_B")
	private Long nroNotaCreditoB;
	@Column(name = "NRO_NOTA_DEBITO_B")
	private Long nroNotaDebitoB;
	@Column(name = "NRO_REMITO")
	private Long nroRemito;
	@Column(name = "PREF_REMITO")
	private String prefRemito;
	@Column(name = "PREF_RECIBO")
	private String prefRecibo;

	public Long getNroRecibo() {
		return nroRecibo;
	}

	public void setNroRecibo(Long nroRecibo) {
		this.nroRecibo = nroRecibo;
	}

	public Long getNroFacturaA() {
		return nroFacturaA;
	}

	public void setNroFacturaA(Long nroFacturaA) {
		this.nroFacturaA = nroFacturaA;
	}

	public Long getNroNotaCreditoA() {
		return nroNotaCreditoA;
	}

	public void setNroNotaCreditoA(Long nroNotaCreditoA) {
		this.nroNotaCreditoA = nroNotaCreditoA;
	}

	public Long getNroNotaDebitoA() {
		return nroNotaDebitoA;
	}

	public void setNroNotaDebitoA(Long nroNotaDebitoA) {
		this.nroNotaDebitoA = nroNotaDebitoA;
	}

	public Long getNroFacturaB() {
		return nroFacturaB;
	}

	public void setNroFacturaB(Long nroFacturaB) {
		this.nroFacturaB = nroFacturaB;
	}

	public Long getNroNotaCreditoB() {
		return nroNotaCreditoB;
	}

	public void setNroNotaCreditoB(Long nroNotaCreditoB) {
		this.nroNotaCreditoB = nroNotaCreditoB;
	}

	public Long getNroNotaDebitoB() {
		return nroNotaDebitoB;
	}

	public void setNroNotaDebitoB(Long nroNotaDebitoB) {
		this.nroNotaDebitoB = nroNotaDebitoB;
	}

	public Long getNroRemito() {
		return nroRemito;
	}

	public void setNroRemito(Long nroRemito) {
		this.nroRemito = nroRemito;
	}

	public String getPrefRemito() {
		return prefRemito;
	}

	public void setPrefRemito(String prefRemito) {
		this.prefRemito = prefRemito;
	}

	public String getPrefRecibo() {
		return prefRecibo;
	}

	public void setPrefRecibo(String prefRecibo) {
		this.prefRecibo = prefRecibo;
	}

}
