package ar.com.wuik.sistema.filters;

public class DetallePedidoFilter {

	private Long idProducto;
	private Long idPedido;

	public Long getIdProducto() {
		if (null == idProducto) {

		}
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public Long getIdPedido() {
		if (null == idPedido) {

		}
		return idPedido;
	}

	public void setIdPedido(Long idPedido) {
		this.idPedido = idPedido;
	}

}
