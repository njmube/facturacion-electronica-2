package ar.com.wuik.sistema.reportes.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ComprobanteDTO {

	private String letra;
	private String tipo;
	private String compNro;
	private Date fechaEmision;
	private String clienteCuit;
	private String clienteDomicilio;
	private String clienteRazonSocial;
	private String clienteCondIVA;
	private String remitos;
	private BigDecimal subtotal;
	private BigDecimal iva21;
	private BigDecimal iva105;
	private BigDecimal total;
	private BigDecimal otrosTributos;
	private BigDecimal subtotalConIVA;
	private String cae;
	private Date vtoCAE;
	private List<DetalleComprobanteDTO> detalles;
	private List<TributoDTO> tributos;
	private String codigoBarras;

	public String getLetra() {
		return letra;
	}

	public void setLetra(String letra) {
		this.letra = letra;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCompNro() {
		return compNro;
	}

	public void setCompNro(String compNro) {
		this.compNro = compNro;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public String getClienteCuit() {
		return clienteCuit;
	}

	public void setClienteCuit(String clienteCuit) {
		this.clienteCuit = clienteCuit;
	}

	public String getClienteDomicilio() {
		return clienteDomicilio;
	}

	public void setClienteDomicilio(String clienteDomicilio) {
		this.clienteDomicilio = clienteDomicilio;
	}

	public String getClienteRazonSocial() {
		return clienteRazonSocial;
	}

	public void setClienteRazonSocial(String clienteRazonSocial) {
		this.clienteRazonSocial = clienteRazonSocial;
	}

	public String getClienteCondIVA() {
		return clienteCondIVA;
	}

	public void setClienteCondIVA(String clienteCondIVA) {
		this.clienteCondIVA = clienteCondIVA;
	}

	public String getRemitos() {
		return remitos;
	}

	public void setRemitos(String remitos) {
		this.remitos = remitos;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getIva21() {
		return iva21;
	}

	public void setIva21(BigDecimal iva21) {
		this.iva21 = iva21;
	}

	public BigDecimal getIva105() {
		return iva105;
	}

	public void setIva105(BigDecimal iva105) {
		this.iva105 = iva105;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getCae() {
		return cae;
	}

	public void setCae(String cae) {
		this.cae = cae;
	}

	public Date getVtoCAE() {
		return vtoCAE;
	}

	public void setVtoCAE(Date vtoCAE) {
		this.vtoCAE = vtoCAE;
	}

	public List<DetalleComprobanteDTO> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleComprobanteDTO> detalles) {
		this.detalles = detalles;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public BigDecimal getOtrosTributos() {
		return otrosTributos;
	}

	public void setOtrosTributos(BigDecimal otrosTributos) {
		this.otrosTributos = otrosTributos;
	}

	public List<TributoDTO> getTributos() {
		return tributos;
	}

	public void setTributos(List<TributoDTO> tributos) {
		this.tributos = tributos;
	}

	public BigDecimal getSubtotalConIVA() {
		return subtotalConIVA;
	}

	public void setSubtotalConIVA(BigDecimal subtotalConIVA) {
		this.subtotalConIVA = subtotalConIVA;
	}

}
