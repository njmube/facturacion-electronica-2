package ar.com.wuik.sistema.entities;

import java.util.Date;

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
	@Column(name = "RAZON_SOCIAL")
	private String razonSocial;
	@Column(name = "COND_IVA")
	private String condIVA;
	@Column(name = "CUIT")
	private String cuit;
	@Column(name = "DOMICILIO")
	private String domicilio;
	@Column(name = "ING_BRUTOS")
	private String ingresosBrutos;
	@Column(name = "INICIO_ACT")
	private Date inicioActividad;

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

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getCondIVA() {
		return condIVA;
	}

	public void setCondIVA(String condIVA) {
		this.condIVA = condIVA;
	}

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getIngresosBrutos() {
		return ingresosBrutos;
	}

	public void setIngresosBrutos(String ingresosBrutos) {
		this.ingresosBrutos = ingresosBrutos;
	}

	public Date getInicioActividad() {
		return inicioActividad;
	}

	public void setInicioActividad(Date inicioActividad) {
		this.inicioActividad = inicioActividad;
	}

}
