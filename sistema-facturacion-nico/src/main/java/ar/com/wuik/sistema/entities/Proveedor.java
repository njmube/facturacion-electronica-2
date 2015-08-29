package ar.com.wuik.sistema.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.com.wuik.sistema.entities.enums.CondicionIVA;

@Entity
@Table(name = "proveedores")
public class Proveedor extends BaseEntity {

	@Column(name = "RAZON_SOCIAL")
	private String razonSocial;
	@Column(name = "DIRECCION")
	private String direccion;
	@Column(name = "TELEFONO")
	private String telefono;
	@Column(name = "MAIL")
	private String mail;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_LOCALIDAD", nullable = false, insertable = false, updatable = false)
	private Localidad localidad;
	@Column(name = "ID_LOCALIDAD")
	private Long idLocalidad;
	@Column(name = "CUIT")
	private String cuit;
	@Column(name = "ACTIVO")
	private boolean activo = Boolean.TRUE;
	@Enumerated(EnumType.STRING)
	@Column(name = "COND_IVA")
	private CondicionIVA condicionIVA;

	public CondicionIVA getCondicionIVA() {
		return condicionIVA;
	}

	public void setCondicionIVA(CondicionIVA condicionIVA) {
		this.condicionIVA = condicionIVA;
	}

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

	public Localidad getLocalidad() {
		return localidad;
	}

	public void setLocalidad(Localidad localidad) {
		this.localidad = localidad;
	}

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public Long getIdLocalidad() {
		return idLocalidad;
	}

	public void setIdLocalidad(Long idLocalidad) {
		this.idLocalidad = idLocalidad;
	}

}
