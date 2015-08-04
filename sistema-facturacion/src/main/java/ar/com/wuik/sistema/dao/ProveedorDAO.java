package ar.com.wuik.sistema.dao;

import java.util.List;

import ar.com.wuik.sistema.entities.Proveedor;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ProveedorFilter;

public interface ProveedorDAO extends GenericCrudDAO<Proveedor> {

	List<Proveedor> search(ProveedorFilter filter) throws DataAccessException;

	boolean estaEnUso(Long id) throws DataAccessException;
}
