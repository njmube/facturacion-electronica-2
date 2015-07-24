package ar.com.wuik.sistema.dao;

import java.util.List;

import ar.com.wuik.sistema.entities.Cheque;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ChequeFilter;

public interface ChequeDAO extends GenericCrudDAO<Cheque> {

	List<Cheque> search(ChequeFilter filter) throws DataAccessException;

}
