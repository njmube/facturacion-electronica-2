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
import javax.persistence.Transient;

import ar.com.wuik.sistema.entities.enums.TipoIVAEnum;

@Entity
@Table(name = "detalles_comprobantes")
public class DetalleComprobante extends BaseEntity {

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_PRODUCTO", nullable = true)
	private Producto producto;
	@Column(name = "CANTIDAD")
	private Integer cantidad;
	@Column(name = "PRECIO")
	private BigDecimal precio;
	@Enumerated(EnumType.ORDINAL)
	@Column(name = "ID_TIPO_IVA")
	private TipoIVAEnum tipoIVA;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ID_COMPROBANTE", nullable = true)
	private Comprobante comprobante;
	@Column(name = "COMENTARIO")
	private String comentario;
	@Column(name = "DETALLE")
	private String detalle;
	@Transient
	private Long temporalId;

	public DetalleComprobante() {
		this.cantidad = 1;
		this.precio = BigDecimal.ZERO;
		this.tipoIVA = TipoIVAEnum.IVA_21;
		this.temporalId = System.currentTimeMillis();
	}
	
	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public BigDecimal getPrecioConIVA() {
		if (null != getTipoIVA() && null != getPrecio()) {
			BigDecimal iva = getTipoIVA().getImporte().add(new BigDecimal(100));
			return getPrecio().multiply(iva).divide(new BigDecimal(100));
		}
		return null;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public Comprobante getComprobante() {
		return comprobante;
	}

	public void setComprobante(Comprobante comprobante) {
		this.comprobante = comprobante;
	}

	public BigDecimal getSubtotal() {
		if (null != getPrecio() && null != getCantidad()) {
			return getPrecio().multiply(new BigDecimal(getCantidad()));
		}
		return null;
	}

	public BigDecimal getTotal() {
		if (null != getTipoIVA() && null != getSubtotal()) {
			BigDecimal iva = getTipoIVA().getImporte().add(new BigDecimal(100));
			return getSubtotal().multiply(iva).divide(new BigDecimal(100));
		}
		return null;
	}

	public BigDecimal getTotalIVA() {
		if (null != getTotal() && null != getSubtotal()) {
			return getTotal().subtract(getSubtotal());
		}

		return null;
	}

	public Long getCoalesceId() {
		return (null != getId()) ? getId() : temporalId;
	}

	public Long getTemporalId() {
		return temporalId;
	}

	public void setTemporalId(Long temporalId) {
		this.temporalId = temporalId;
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public TipoIVAEnum getTipoIVA() {
		return tipoIVA;
	}

	public void setTipoIVA(TipoIVAEnum tipoIVA) {
		this.tipoIVA = tipoIVA;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public BigDecimal getSubtotalConIVA() {
		if (null != getPrecioConIVA() && null != getCantidad()) {
			return getPrecioConIVA().multiply(new BigDecimal(getCantidad()));
		}
		return null;
	}

}
