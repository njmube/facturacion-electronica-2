package ar.com.wuik.sistema.bo;

import java.util.List;

import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.ProductoFilter;

public interface ProductoBO {

	Producto obtener(Long id) throws BusinessException;

	List<Producto> buscar(ProductoFilter filter) throws BusinessException;

	List<Producto> obtenerTodos() throws BusinessException;

	void guardar(Producto producto) throws BusinessException;

	void actualizar(Producto producto) throws BusinessException;

	boolean estaEnUso(Long id) throws BusinessException;

	void eliminar(Long id) throws BusinessException;

}
