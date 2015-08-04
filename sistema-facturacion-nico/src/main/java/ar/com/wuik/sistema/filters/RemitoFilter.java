package ar.com.wuik.sistema.filters;

import java.util.List;

public class RemitoFilter {

	private Long idCliente;
	private Boolean asignado;
	private Boolean activo;
	private List<Long> idsToExclude;
	private List<Long> idsToInclude;
	private Boolean inicializarDetalles;

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

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	public Boolean getInicializarDetalles() {
		return inicializarDetalles;
	}

	public void setInicializarDetalles(Boolean inicializarDetalles) {
		this.inicializarDetalles = inicializarDetalles;
	}

}
