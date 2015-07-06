package ar.com.wuik.sistema.filters;

import java.util.Date;
import java.util.List;

public class PedidoFilter {

	private Long nro;
	private List<Long> idsPedido;
	private Long idCliente;
	private Long idProveedor;
	private Date fechaDesde;
	private Date fechaHasta;
	private String observaciones;
	private Boolean finalizado;

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	public Long getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(Long idProveedor) {
		this.idProveedor = idProveedor;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Boolean isFinalizado() {
		return finalizado;
	}

	public void setFinalizado(Boolean finalizado) {
		this.finalizado = finalizado;
	}

	public Long getNro() {
		return nro;
	}

	public void setNro(Long nro) {
		this.nro = nro;
	}

	public List<Long> getIdsPedido() {
		return idsPedido;
	}

	public void setIdsPedido(List<Long> idsPedido) {
		this.idsPedido = idsPedido;
	}

}
