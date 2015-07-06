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
	public void incrementarNroNotaCredito() throws DataAccessException {
		try {
			Parametro parametro = getById(1L);
			Long nroNotaCredito = parametro.getNroNotaCredito();
			parametro.setNroNotaCredito(nroNotaCredito + 1);
			getSession().saveOrUpdate(parametro);
		} catch (HibernateException hexc) {
			throw new DataAccessException(hexc);
		}
	}

	@Override
	public void incrementarNroNotaDebito() throws DataAccessException {
		try {
			Parametro parametro = getById(1L);
			Long nroNotaDebito = parametro.getNroNotaDebito();
			parametro.setNroNotaDebito(nroNotaDebito + 1);
			getSession().saveOrUpdate(parametro);
		} catch (HibernateException hexc) {
			throw new DataAccessException(hexc);
		}
	}

	@Override
	public void incrementarNroFactura() throws DataAccessException {
		try {
			Parametro parametro = getById(1L);
			Long nroFactura = parametro.getNroFactura();
			parametro.setNroFactura(nroFactura + 1);
			getSession().saveOrUpdate(parametro);
		} catch (HibernateException hexc) {
			throw new DataAccessException(hexc);
		}
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
	public void incrementarNroMovInterno() throws DataAccessException {
		try {
			Parametro parametro = getById(1L);
			Long nroMovInterno = parametro.getNroMovInterno();
			parametro.setNroMovInterno(nroMovInterno + 1);
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
