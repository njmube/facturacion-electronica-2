package ar.com.wuik.sistema.dao;

import java.util.List;

import ar.com.wuik.sistema.entities.NotaCredito;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.NotaCreditoFilter;

public interface NotaCreditoDAO extends GenericCrudDAO<NotaCredito> {

	List<NotaCredito> search(NotaCreditoFilter filter)
			throws DataAccessException;

	boolean isUtilizada(Long id) throws DataAccessException;
}
