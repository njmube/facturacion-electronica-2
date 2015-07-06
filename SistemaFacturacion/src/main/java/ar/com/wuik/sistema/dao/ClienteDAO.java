package ar.com.wuik.sistema.dao;

import java.util.List;

import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ClienteFilter;

public interface ClienteDAO extends GenericCrudDAO<Cliente> {

	List<Cliente> search(ClienteFilter filter) throws DataAccessException;

	boolean estaEnUso(Long id) throws DataAccessException;

}
