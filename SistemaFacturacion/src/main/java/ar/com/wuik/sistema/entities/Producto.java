package ar.com.wuik.sistema.entities;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "productos")
public class Producto extends BaseEntity {

	@Column(name = "DESCRIPCION")
	private String descripcion;
	@Column(name = "CODIGO")
	private String codigo;
	@Column(name = "IVA")
	private BigDecimal iva;
	@Column(name = "COSTO")
	private BigDecimal costo;
	@Column(name = "PRECIO")
	private BigDecimal precio;
	@Column(name = "ACTIVO")
	private boolean activo;
	@Column(name = "UBICACION")
	private String ubicacion;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_TIPO", nullable = false)
	private TipoProducto tipoProducto;

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

	public BigDecimal getIva() {
		return iva;
	}

	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}

	public BigDecimal getCosto() {
		return costo;
	}

	public void setCosto(BigDecimal costo) {
		this.costo = costo;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
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

	public TipoProducto getTipoProducto() {
		return tipoProducto;
	}

	public void setTipoProducto(TipoProducto tipoProducto) {
		this.tipoProducto = tipoProducto;
	}

}
