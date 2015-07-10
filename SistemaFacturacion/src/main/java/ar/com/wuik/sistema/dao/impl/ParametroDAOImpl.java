package ar.com.wuik.sistema.dao.impl;

import org.hibernate.HibernateException;

import ar.com.wuik.sistema.dao.ParametroDAO;
import ar.com.wuik.sistema.entities.Parametro;
import ar.com.wuik.sistema.exceptions.DataAccessException;

public class ParametroDAOImpl extends GenericCrudHBDAOImpl<Parametro> implements
		ParametroDAO {

	public ParametroDAOImpl() {
		super(Parametro.class);
	}

	@Override
	public void incrementarNroRecibo() throws DataAccessException {
		try {
			Parametro parametro = getById(1L);
			Long nroRecibo = parametro.getNroRecibo();
			parametro.setNroRecibo(nroRecibo + 1);
			getSession().saveOrUpdate(parametro);
		} catch (HibernateException hexc) {
			throw new DataAccessException(hexc);
		}
	}

	@Override
	public void incrementarNroRemito() throws DataAccessException {
		try {
			Parametro parametro = getById(1L);
			Long nroRemito = parametro.getNroRemito();
			parametro.setNroRemito(nroRemito + 1);
			getSession().saveOrUpdate(parametro);
		} catch (HibernateException hexc) {
			throw new DataAccessException(hexc);
		}
	}

}
