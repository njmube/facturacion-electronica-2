package ar.com.wuik.sistema.bo;

import java.util.List;

import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.sistema.entities.StockProducto;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.ProductoFilter;

public interface ProductoBO {

	Producto obtener(Long id) throws BusinessException;
	
	Producto obtenerPorCodigo(String codigo) throws BusinessException;

	List<Producto> buscar(ProductoFilter filter) throws BusinessException;

	List<Producto> obtenerTodos() throws BusinessException;

	void guardar(Producto producto) throws BusinessException;

	void actualizar(Producto producto) throws BusinessException;

	boolean estaEnUso(Long id) throws BusinessException;

	void eliminar(Long id) throws BusinessException;

	List<StockProducto> obtenerStockPorProducto(Long id)
			throws BusinessException;
	
	StockProducto obtenerStockProducto(Long idStockProducto)
			throws BusinessException;

	void guardarStockProducto(StockProducto stockProducto)
			throws BusinessException;
	
	void actualizarStockProducto(StockProducto stockProducto)
			throws BusinessException;

	void eliminarStockProducto(Long id) throws BusinessException;

	void eliminarStockProducto(List<Long> idsStockProducto) throws BusinessException;
}
