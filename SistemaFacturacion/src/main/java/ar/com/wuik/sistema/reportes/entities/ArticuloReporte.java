/**
 * Autor : juan.vazquez@wuik.com.ar - Wuik-Working Innovation Creacion :
 * 27/02/2014 - 15:20:44
 */
package ar.com.wuik.sistema.reportes.entities;

/**
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public class ArticuloReporte {


	private String codigoArticulo;
	private String codigoBarras;
	private String articulo;
	private String talle;
	private String color;
	private String stock;

	/**
	 * @return the codigoArticulo
	 */
	public String getCodigoArticulo() {
		return codigoArticulo;
	}

	/**
	 * @param codigoArticulo
	 *            the codigoArticulo to set
	 */
	public void setCodigoArticulo( String codigoArticulo ) {
		this.codigoArticulo = codigoArticulo;
	}

	/**
	 * @return the codigoBarras
	 */
	public String getCodigoBarras() {
		return codigoBarras;
	}

	/**
	 * @param codigoBarras
	 *            the codigoBarras to set
	 */
	public void setCodigoBarras( String codigoBarras ) {
		this.codigoBarras = codigoBarras;
	}

	/**
	 * @return the articulo
	 */
	public String getArticulo() {
		return articulo;
	}

	/**
	 * @param articulo
	 *            the articulo to set
	 */
	public void setArticulo( String articulo ) {
		this.articulo = articulo;
	}

	/**
	 * @return the talle
	 */
	public String getTalle() {
		return talle;
	}

	/**
	 * @param talle
	 *            the talle to set
	 */
	public void setTalle( String talle ) {
		this.talle = talle;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	public void setColor( String color ) {
		this.color = color;
	}

	/**
	 * @return the stock
	 */
	public String getStock() {
		return stock;
	}

	/**
	 * @param stock
	 *            the stock to set
	 */
	public void setStock( String stock ) {
		this.stock = stock;
	}

}
