package FEV1.dif.afip.gov.ar.entities;

import java.math.BigDecimal;

public class Item {

	private Integer unidadesMtx;
	private String codigoMtx;
	private String codigo;
	private String descripcion;
	private BigDecimal cantidad;
	private UnidadMedidaEnum unidadMedida;
	private BigDecimal precioUnitario;
	private BigDecimal importeBonificacion;
	private TipoIVAEnum tipoIVA;
	private BigDecimal totalIVA;
	private BigDecimal total;

	public Integer getUnidadesMtx() {
		return unidadesMtx;
	}

	public void setUnidadesMtx(Integer unidadesMtx) {
		this.unidadesMtx = unidadesMtx;
	}

	public String getCodigoMtx() {
		return codigoMtx;
	}

	public void setCodigoMtx(String codigoMtx) {
		this.codigoMtx = codigoMtx;
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
		this.descripcion = descripcion;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public UnidadMedidaEnum getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(UnidadMedidaEnum unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public BigDecimal getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(BigDecimal precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public BigDecimal getImporteBonificacion() {
		return importeBonificacion;
	}

	public void setImporteBonificacion(BigDecimal importeBonificacion) {
		this.importeBonificacion = importeBonificacion;
	}

	public TipoIVAEnum getTipoIVA() {
		return tipoIVA;
	}

	public void setTipoIVA(TipoIVAEnum tipoIVA) {
		this.tipoIVA = tipoIVA;
	}

	public BigDecimal getTotalIVA() {
		return totalIVA;
	}

	public void setTotalIVA(BigDecimal totalIVA) {
		this.totalIVA = totalIVA;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

}
