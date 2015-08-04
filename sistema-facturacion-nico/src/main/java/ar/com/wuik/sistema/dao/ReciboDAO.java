package ar.com.wuik.sistema.dao;

import java.util.List;

import ar.com.wuik.sistema.entities.Recibo;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ReciboFilter;

public interface ReciboDAO extends GenericCrudDAO<Recibo> {

	List<Recibo> search(ReciboFilter filter) throws DataAccessException;

}
