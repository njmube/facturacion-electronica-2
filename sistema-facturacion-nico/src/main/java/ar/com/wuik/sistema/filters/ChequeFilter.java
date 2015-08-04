package ar.com.wuik.sistema.filters;

import java.util.List;

public class ChequeFilter {

	private String numero;
	private String aNombreDe;
	private List<Long> idsToExclude;
	private List<Long> idsToInclude;
	private Boolean enUso;
	private Long idCliente;

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getaNombreDe() {
		return aNombreDe;
	}

	public void setaNombreDe(String aNombreDe) {
		this.aNombreDe = aNombreDe;
	}

	public List<Long> getIdsToInclude() {
		return idsToInclude;
	}

	public void setIdsToInclude(List<Long> idsToInclude) {
		this.idsToInclude = idsToInclude;
	}

	public Boolean getEnUso() {
		return enUso;
	}

	public void setEnUso(Boolean enUso) {
		this.enUso = enUso;
	}

	public List<Long> getIdsToExclude() {
		return idsToExclude;
	}

	public void setIdsToExclude(List<Long> idsToExclude) {
		this.idsToExclude = idsToExclude;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

}
