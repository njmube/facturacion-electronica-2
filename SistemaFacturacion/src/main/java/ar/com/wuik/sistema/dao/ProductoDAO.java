package ar.com.wuik.sistema.dao;

import java.util.List;

import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ProductoFilter;

public interface ProductoDAO extends GenericCrudDAO<Producto> {

	List<Producto> search(ProductoFilter filter) throws DataAccessException;

	boolean estaEnUso(Long id) throws DataAccessException;

}
