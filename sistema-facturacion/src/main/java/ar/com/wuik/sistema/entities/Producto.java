package ar.com.wuik.sistema.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import ar.com.wuik.sistema.entities.enums.TipoIVAEnum;

@Entity
@Table(name = "productos")
public class Producto extends BaseEntity {

	@Column(name = "DESCRIPCION")
	private String descripcion;
	@Column(name = "CODIGO")
	private String codigo;
	@Column(name = "ACTIVO")
	private boolean activo;
	@Column(name = "UBICACION")
	private String ubicacion;
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "ID_TIPO_IVA")
	private TipoIVAEnum tipoIVA;

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public TipoIVAEnum getTipoIVA() {
		return tipoIVA;
	}

	public void setTipoIVA(TipoIVAEnum tipoIVA) {
		this.tipoIVA = tipoIVA;
	}

}
