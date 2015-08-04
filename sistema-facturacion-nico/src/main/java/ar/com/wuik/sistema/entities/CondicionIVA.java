package ar.com.wuik.sistema.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "condiciones_iva")
public class CondicionIVA extends BaseEntity {

	@Column(name = "DENOMINACION")
	private String denominacion;
	@Column(name = "ABREV")
	private String abreviacion;

	public String getDenominacion() {
		return denominacion;
	}

	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}

	public String getAbreviacion() {
		return abreviacion;
	}

	public void setAbreviacion(String abreviacion) {
		this.abreviacion = abreviacion;
	}

}
