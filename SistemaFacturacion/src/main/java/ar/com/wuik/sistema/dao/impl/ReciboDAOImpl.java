package ar.com.wuik.sistema.dao.impl;

import java.util.List;

import ar.com.wuik.sistema.dao.ReciboDAO;
import ar.com.wuik.sistema.entities.Recibo;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ReciboFilter;

public class ReciboDAOImpl extends GenericCrudHBDAOImpl<Recibo> implements
		ReciboDAO {

	public ReciboDAOImpl() {
		super(Recibo.class);
	}

	@Override
	public List<Recibo> search(ReciboFilter filter) throws DataAccessException {
		// TODO Auto-generated method stub
		return null;
	}
}
