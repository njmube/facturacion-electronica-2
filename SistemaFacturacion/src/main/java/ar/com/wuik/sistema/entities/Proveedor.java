package ar.com.wuik.sistema.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "proveedores")
public class Proveedor extends BaseEntity {

	@Column(name = "RAZON_SOCIAL")
	private String razonSocial;
	@Column(name = "DIRECCION")
	private String direccion;
	@Column(name = "TELEFONO")
	private String telefono;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_LOCALIDAD", nullable = false)
	private Localidad localidad;
	@Column(name = "CUIT")
	private String cuit;

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

}
