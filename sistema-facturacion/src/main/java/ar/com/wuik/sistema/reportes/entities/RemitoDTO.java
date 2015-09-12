package ar.com.wuik.sistema.reportes.entities;

import java.util.Date;
import java.util.List;

public class RemitoDTO {

	private String compNro;
	private Date fechaEmision;
	private String clienteCuit;
	private String clienteDomicilio;
	private String clienteRazonSocial;
	private String clienteCondIVA;
	private List<DetalleRemitoDTO> detalles;
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

	public List<DetalleRemitoDTO> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<DetalleRemitoDTO> detalles) {
		this.detalles = detalles;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

}
