package ar.com.wuik.sistema.filters;

public class ProductoFilter {

	private String descripcion;
	private String descripcionCodigo;
	private Long idToExclude;

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcionCodigo() {
		return descripcionCodigo;
	}

	public void setDescripcionCodigo(String descripcionCodigo) {
		this.descripcionCodigo = descripcionCodigo;
	}

	public Long getIdToExclude() {
		return idToExclude;
	}

	public void setIdToExclude(Long idToExclude) {
		this.idToExclude = idToExclude;
	}

}
