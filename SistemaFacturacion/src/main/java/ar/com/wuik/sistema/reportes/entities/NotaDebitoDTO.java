package ar.com.wuik.sistema.reportes.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class NotaDebitoDTO {

	private String letra;
	private String tipo;
	private String razonSocial;
	private String domicilio;
	private String condIVA;
	private String ptoVta;
	private String compNro;
	private Date fechaEmision;
	private String cuit;
	private String ingBrutos;
	private Date inicioAct;
	private String clienteCuit;
	private String clienteDomicilio;
	private String clienteRazonSocial;
	private String clienteCondIVA;
	private String remitos;
	private BigDecimal subtotal;
	private BigDecimal iva21;
	private BigDecimal iva105;
	private BigDecimal total;
	private String cae;
	private Date vtoCAE;
	private List<DetalleNotaDebitoDTO> detalles;
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

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getCondIVA() {
		return condIVA;
	}

	public void setCondIVA(String condIVA) {
		this.condIVA = condIVA;
	}

	public String getPtoVta() {
		return ptoVta;
	}

	public void setPtoVta(String ptoVta) {
		this.ptoVta = ptoVta;
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

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public String getIngBrutos() {
		return ingBrutos;
	}

	public void setIngBrutos(String ingBrutos) {
		this.ingBrutos = ingBrutos;
	}

	public Date getInicioAct() {
		return inicioAct;
	}

	public void setInicioAct(Date inicioAct) {
		this.inicioAct = inicioAct;
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

	public List<DetalleNotaDebitoDTO> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleNotaDebitoDTO> detalles) {
		this.detalles = detalles;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

}
