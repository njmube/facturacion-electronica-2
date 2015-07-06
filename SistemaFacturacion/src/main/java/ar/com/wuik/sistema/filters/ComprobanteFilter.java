package ar.com.wuik.sistema.filters;

import java.util.Date;
import java.util.List;

public class ComprobanteFilter {

	private String numero;
	private Date desdeVto;
	private Date hastaVto;
	private Date desdeEmision;
	private Date hastaEmision;
	private Long idProveedor;
	private Long idCliente;
	private List<Long> idsToExclude;
	private List<Long> idsToInclude;
	private Boolean activo;

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}


	public Date getDesdeVto() {
		return desdeVto;
	}

	public void setDesdeVto(Date desdeVto) {
		this.desdeVto = desdeVto;
	}

	public Date getHastaVto() {
		return hastaVto;
	}

	public void setHastaVto(Date hastaVto) {
		this.hastaVto = hastaVto;
	}

	public Date getDesdeEmision() {
		return desdeEmision;
	}

	public void setDesdeEmision(Date desdeEmision) {
		this.desdeEmision = desdeEmision;
	}

	public Date getHastaEmision() {
		return hastaEmision;
	}

	public void setHastaEmision(Date hastaEmision) {
		this.hastaEmision = hastaEmision;
	}

	public Long getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedor(Long idProveedor) {
		this.idProveedor = idProveedor;
	}

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

}
