package ar.com.wuik.sistema.filters;

import java.util.List;

import ar.com.wuik.sistema.entities.enums.EstadoFacturacion;

public class FacturaFilter {

	private Long idCliente;
	private Boolean asignado;
	private Boolean activo;
	private List<Long> idsToExclude;
	private List<Long> idsToInclude;
	private Boolean inicializarDetalles;
	private EstadoFacturacion estadoFacturacion;

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Boolean getAsignado() {
		return asignado;
	}

	public void setAsignado(Boolean asignado) {
		this.asignado = asignado;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public List<Long> getIdsToExclude() {
		return idsToExclude;
	}

	public void setIdsToExclude(List<Long> idsToExclude) {
		this.idsToExclude = idsToExclude;
	}

	public List<Long> getIdsToInclude() {
		return idsToInclude;
	}

	public void setIdsToInclude(List<Long> idsToInclude) {
		this.idsToInclude = idsToInclude;
	}

	public Boolean getInicializarDetalles() {
		return inicializarDetalles;
	}

	public void setInicializarDetalles(Boolean inicializarDetalles) {
		this.inicializarDetalles = inicializarDetalles;
	}

	public EstadoFacturacion getEstadoFacturacion() {
		return estadoFacturacion;
	}

	public void setEstadoFacturacion(EstadoFacturacion estadoFacturacion) {
		this.estadoFacturacion = estadoFacturacion;
	}

}
