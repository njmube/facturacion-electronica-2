package ar.com.wuik.sistema.reportes.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ReciboDTO {

	private String compNro;
	private Date fechaEmision;
	private String clienteCuit;
	private String clienteDomicilio;
	private String clienteRazonSocial;
	private String clienteCondIVA;
	private BigDecimal totalEfectivo;
	private BigDecimal totalCheque;
	private BigDecimal total;
	private String totalLetras;
	private List<DetalleReciboDTO> detalles;
	private List<LiquidacionDTO> liquidaciones;
	private String observaciones;

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

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public String getTotalLetras() {
		return totalLetras;
	}

	public void setTotalLetras(String totalLetras) {
		this.totalLetras = totalLetras;
	}

	public List<DetalleReciboDTO> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleReciboDTO> detalles) {
		this.detalles = detalles;
	}

	public BigDecimal getTotalEfectivo() {
		return totalEfectivo;
	}

	public void setTotalEfectivo(BigDecimal totalEfectivo) {
		this.totalEfectivo = totalEfectivo;
	}

	public BigDecimal getTotalCheque() {
		return totalCheque;
	}

	public void setTotalCheque(BigDecimal totalCheque) {
		this.totalCheque = totalCheque;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public List<LiquidacionDTO> getLiquidaciones() {
		return liquidaciones;
	}

	public void setLiquidaciones(List<LiquidacionDTO> liquidaciones) {
		this.liquidaciones = liquidaciones;
	}

}
