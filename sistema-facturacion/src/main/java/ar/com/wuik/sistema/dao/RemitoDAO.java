package ar.com.wuik.sistema.dao;

import java.util.List;

import ar.com.wuik.sistema.entities.Remito;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.RemitoFilter;

public interface RemitoDAO extends GenericCrudDAO<Remito> {

	List<Remito> search(RemitoFilter filter) throws DataAccessException;

	boolean estaEnUso(Long id) throws DataAccessException;
}
