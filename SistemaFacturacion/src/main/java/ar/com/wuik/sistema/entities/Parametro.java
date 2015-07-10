package ar.com.wuik.sistema.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "parametros")
public class Parametro extends BaseEntity {

	@Column(name = "NRO_RECIBO")
	private Long nroRecibo;
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
