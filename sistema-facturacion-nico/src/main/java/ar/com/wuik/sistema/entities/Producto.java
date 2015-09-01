package ar.com.wuik.sistema.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.com.wuik.sistema.entities.enums.TipoIVAEnum;

@Entity
@Table(name = "productos")
public class Producto extends BaseEntity {

	@Column(name = "DESCRIPCION")
	private String descripcion;
	@Column(name = "ACTIVO")
	private boolean activo;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_TIPO", nullable = false, insertable = false, updatable = false)
	private TipoProducto tipoProducto;
	@Column(name = "ID_TIPO")
	private long idTipo;
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "ID_TIPO_IVA")
	private TipoIVAEnum tipoIVA;

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public TipoProducto getTipoProducto() {
		return tipoProducto;
	}

	public void setTipoProducto(TipoProducto tipoProducto) {
		this.tipoProducto = tipoProducto;
	}

	public long getIdTipo() {
		return idTipo;
	}

	public void setIdTipo(long idTipo) {
		this.idTipo = idTipo;
	}

	public TipoIVAEnum getTipoIVA() {
		return tipoIVA;
	}

	public void setTipoIVA(TipoIVAEnum tipoIVA) {
		this.tipoIVA = tipoIVA;
	}

}
