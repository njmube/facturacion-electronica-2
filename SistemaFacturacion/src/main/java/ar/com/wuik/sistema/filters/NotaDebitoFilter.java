package ar.com.wuik.sistema.filters;

import java.util.Date;
import java.util.List;

import ar.com.wuik.sistema.entities.enums.EstadoFacturacion;

public class NotaDebitoFilter {

	private Long idCliente;
	private Boolean activo;
	private List<Long> idsToExclude;
	private List<Long> idsToInclude;
	private Boolean inicializarDetalles;
	private EstadoFacturacion estadoFacturacion;
	private Boolean paga;
	private Date desde;
	private Date hasta;

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
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

	public Boolean getPaga() {
		return paga;
	}

	public void setPaga(Boolean paga) {
		this.paga = paga;
	}

	public Date getDesde() {
		return desde;
	}

	public void setDesde(Date desde) {
		this.desde = desde;
	}

	public Date getHasta() {
		return hasta;
	}

	public void setHasta(Date hasta) {
		this.hasta = hasta;
	}

}
