package ar.com.wuik.sistema.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "parametros")
public class Parametro extends BaseEntity {

	@Column(name = "NRO_FACTURA")
	private Long nroFactura;
	@Column(name = "NRO_RECIBO")
	private Long nroRecibo;
	@Column(name = "NRO_NOTA_CRED")
	private Long nroNotaCredito;
	@Column(name = "NRO_NOTA_DEB")
	private Long nroNotaDebito;
	@Column(name = "NRO_MOV_INTERNO")
	private Long nroMovInterno;
	@Column(name = "NRO_REMITO")
	private Long nroRemito;
	@Column(name = "PREF_FACTURA")
	private String prefFactura;
	@Column(name = "PREF_NOTA_CRED")
	private String prefNotaCredito;
	@Column(name = "PREF_NOTA_DEB")
	private String prefNotaDebito;
	@Column(name = "PREF_RECIBO")
	private String prefRecibo;
	@Column(name = "PREF_REMITO")
	private String prefRemito;

	public Long getNroFactura() {
		return nroFactura;
	}

	public void setNroFactura(Long nroFactura) {
		this.nroFactura = nroFactura;
	}

	public Long getNroRecibo() {
		return nroRecibo;
	}

	public void setNroRecibo(Long nroRecibo) {
		this.nroRecibo = nroRecibo;
	}

	public Long getNroNotaCredito() {
		return nroNotaCredito;
	}

	public void setNroNotaCredito(Long nroNotaCredito) {
		this.nroNotaCredito = nroNotaCredito;
	}

	public Long getNroNotaDebito() {
		return nroNotaDebito;
	}

	public void setNroNotaDebito(Long nroNotaDebito) {
		this.nroNotaDebito = nroNotaDebito;
	}

	public Long getNroMovInterno() {
		return nroMovInterno;
	}

	public void setNroMovInterno(Long nroMovInterno) {
		this.nroMovInterno = nroMovInterno;
	}

	public Long getNroRemito() {
		return nroRemito;
	}

	public void setNroRemito(Long nroRemito) {
		this.nroRemito = nroRemito;
	}

	public String getPrefFactura() {
		return prefFactura;
	}

	public void setPrefFactura(String prefFactura) {
		this.prefFactura = prefFactura;
	}

	public String getPrefNotaCredito() {
		return prefNotaCredito;
	}

	public void setPrefNotaCredito(String prefNotaCredito) {
		this.prefNotaCredito = prefNotaCredito;
	}

	public String getPrefNotaDebito() {
		return prefNotaDebito;
	}

	public void setPrefNotaDebito(String prefNotaDebito) {
		this.prefNotaDebito = prefNotaDebito;
	}

	public String getPrefRecibo() {
		return prefRecibo;
	}

	public void setPrefRecibo(String prefRecibo) {
		this.prefRecibo = prefRecibo;
	}

	public String getPrefRemito() {
		return prefRemito;
	}

	public void setPrefRemito(String prefRemito) {
		this.prefRemito = prefRemito;
	}

}
