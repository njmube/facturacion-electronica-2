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

import org.hibernate.annotations.Formula;

import ar.com.wuik.sistema.entities.enums.CondicionIVA;
import ar.com.wuik.sistema.entities.enums.TipoDocumento;

@Entity
@Table(name = "clientes")
public class Cliente extends BaseEntity {

	@Column(name = "RAZON_SOCIAL")
	private String razonSocial;
	@Column(name = "DIRECCION")
	private String direccion;
	@Column(name = "TELEFONO")
	private String telefono;
	@Column(name = "CELULAR")
	private String celular;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_LOCALIDAD", nullable = true, insertable = false, updatable = false)
	private Localidad localidad;
	@Column(name = "ID_LOCALIDAD")
	private Long idLocalidad;
	@Column(name = "DOCUMENTO")
	private String documento;
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_DOC")
	private TipoDocumento tipoDocumento;
	@Column(name = "ACTIVO")
	private boolean activo = Boolean.TRUE;
	@Enumerated(EnumType.STRING)
	@Column(name = "COND_IVA")
	private CondicionIVA condicionIVA;
	@Formula(value = "(SELECT (v.HABER - v.DEBE) + v.SALDO_INICIAL from vw_saldos v WHERE v.ID_CLIENTE = ID)")
	private BigDecimal saldo;
	@Column(name = "SALDO_INICIAL")
	private BigDecimal saldoInicial;
	@Column(name = "MAIL")
	private String mail;

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public Localidad getLocalidad() {
		return localidad;
	}

	public void setLocalidad(Localidad localidad) {
		this.localidad = localidad;
	}

	public Long getIdLocalidad() {
		return idLocalidad;
	}

	public void setIdLocalidad(Long idLocalidad) {
		this.idLocalidad = idLocalidad;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public CondicionIVA getCondicionIVA() {
		return condicionIVA;
	}

	public void setCondicionIVA(CondicionIVA condicionIVA) {
		this.condicionIVA = condicionIVA;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public BigDecimal getSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoInicial(BigDecimal saldoInicial) {
		this.saldoInicial = saldoInicial;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

}
