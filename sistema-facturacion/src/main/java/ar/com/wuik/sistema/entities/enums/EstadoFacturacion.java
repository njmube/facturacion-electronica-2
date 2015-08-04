package ar.com.wuik.sistema.entities.enums;

public enum EstadoFacturacion {

	SIN_FACTURAR("SIN FACTURAR"), FACTURADO("FACTURADO"), FACTURADO_ERROR(
			"FACTURADO CON ERROR");

	private String denominacion;

	private EstadoFacturacion(String denominacion) {
		this.denominacion = denominacion;
	}

	public String getDenominacion() {
		return denominacion;
	}

}
