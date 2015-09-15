package ar.com.wuik.sistema.reportes.entities;

import java.math.BigDecimal;
import java.util.Date;

public class ComprobanteCompraDTO {

	private Date fecha;
	private String proveedor;
	private String cuit;
	private String tipo;
	private String comprobante;
	private BigDecimal neto;
	private BigDecimal iva;
	private BigDecimal percepcion;
	private BigDecimal otros;
	private BigDecimal total;

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getNeto() {
		return neto;
	}

	public void setNeto(BigDecimal neto) {
		this.neto = neto;
	}

	public BigDecimal getIva() {
		return iva;
	}

	public void setIva(BigDecimal iva) {
		this.iva = iva;
	}

	public BigDecimal getPercepcion() {
		return percepcion;
	}

	public void setPercepcion(BigDecimal percepcion) {
		this.percepcion = percepcion;
	}

	public BigDecimal getOtros() {
		return otros;
	}

	public void setOtros(BigDecimal otros) {
		this.otros = otros;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getComprobante() {
		return comprobante;
	}

	public void setComprobante(String comprobante) {
		this.comprobante = comprobante;
	}

}
