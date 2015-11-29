package ar.com.wuik.sistema.entities;

import java.math.BigDecimal;

import ar.com.wuik.sistema.entities.enums.TipoIVAEnum;
import ar.com.wuik.swing.utils.WUtils;

public class DetalleComprobanteDTO {

	private String codigo;
	private String descripcion;
	private int cantidad;
	private BigDecimal precio;
	private TipoIVAEnum tipoIVA;
	private Long temporalId;
	private Long id;
	private DetalleComprobante detalleComprobante;

	public DetalleComprobanteDTO(DetalleComprobante detalleComprobante) {
		this.detalleComprobante = detalleComprobante;
		this.codigo = (null != detalleComprobante.getProducto()) ? detalleComprobante
				.getProducto().getCodigo() : "0";
		this.descripcion = (null != detalleComprobante.getProducto()) ? detalleComprobante
				.getProducto().getDescripcion() : detalleComprobante
				.getDetalle();
		this.cantidad = detalleComprobante.getCantidad();
		this.precio = detalleComprobante.getPrecio();
		this.tipoIVA = detalleComprobante.getTipoIVA();
		this.id = detalleComprobante.getId();
	}

	public DetalleComprobanteDTO() {
		this.detalleComprobante = new DetalleComprobante();
		this.temporalId = System.currentTimeMillis();
		setTipoIVA(TipoIVAEnum.IVA_21);
		setCantidad(1);
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.detalleComprobante.setDetalle(descripcion);
		this.descripcion = descripcion;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.detalleComprobante.setCantidad(cantidad);
		this.cantidad = cantidad;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.detalleComprobante.setPrecio(precio);
		this.precio = precio;
	}

	public TipoIVAEnum getTipoIVA() {
		return tipoIVA;
	}

	public void setTipoIVA(TipoIVAEnum tipoIVA) {
		this.detalleComprobante.setTipoIVA(tipoIVA);
		this.tipoIVA = tipoIVA;
	}

	public BigDecimal getTotalIVA() {
		if (null != getTotal() && null != getSubtotal()) {
			return getTotal().subtract(getSubtotal());
		}

		return null;
	}

	public BigDecimal getSubtotalConIVA() {
		if (null != getPrecioConIVA()) {
			return getPrecioConIVA().multiply(new BigDecimal(getCantidad()));
		}
		return null;
	}

	public BigDecimal getSubtotal() {
		if (null != getPrecio()) {
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

	public BigDecimal getPrecioConIVA() {
		if (null != getTipoIVA() && null != getPrecio()) {
			BigDecimal iva = getTipoIVA().getImporte().add(new BigDecimal(100));
			return getPrecio().multiply(iva).divide(new BigDecimal(100));
		}
		return null;
	}

	public DetalleComprobante getDetalleComprobante() {
		return detalleComprobante;
	}

	public Long getTemporalId() {
		return temporalId;
	}

	public void setTemporalId(Long temporalId) {
		this.temporalId = temporalId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCoalesceId() {
		return (null != getId()) ? getId() : temporalId;
	}

	public boolean isCompleto() {
		return WUtils.isNotEmpty(codigo) && null != precio;
	}

	public void setProducto(Producto producto) {
		detalleComprobante.setProducto(producto);
		setCodigo(producto.getCodigo());
		setDescripcion(producto.getDescripcion());
	}

}
