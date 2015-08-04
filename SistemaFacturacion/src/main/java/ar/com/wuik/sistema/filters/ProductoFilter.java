package ar.com.wuik.sistema.filters;

public class ProductoFilter {

	private String descripcion;
	private String descripcionCodigo;
	private String codigo;
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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Long getIdToExclude() {
		return idToExclude;
	}

	public void setIdToExclude(Long idToExclude) {
		this.idToExclude = idToExclude;
	}

}
