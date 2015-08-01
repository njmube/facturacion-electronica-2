package ar.com.wuik.sistema.dao;

import ar.com.wuik.sistema.entities.TipoProducto;
import ar.com.wuik.sistema.exceptions.DataAccessException;

public interface TipoProductoDAO extends GenericCrudDAO<TipoProducto>{

	boolean estaEnUso(Long id) throws DataAccessException;
}
