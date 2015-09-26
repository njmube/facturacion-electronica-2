package ar.com.wuik.sistema.dao;

import java.util.List;

import ar.com.wuik.sistema.entities.Localidad;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.LocalidadFilter;

public interface LocalidadDAO extends GenericCrudDAO<Localidad> {

	List<Localidad> search(LocalidadFilter filter) throws DataAccessException;

}
