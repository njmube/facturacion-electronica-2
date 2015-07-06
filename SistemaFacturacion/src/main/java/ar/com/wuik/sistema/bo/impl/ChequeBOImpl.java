package ar.com.wuik.sistema.bo.impl;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import ar.com.wuik.sistema.bo.ChequeBO;
import ar.com.wuik.sistema.dao.ChequeDAO;
import ar.com.wuik.sistema.entities.Cheque;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ChequeFilter;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.sistema.utils.HibernateUtil;

public class ChequeBOImpl implements ChequeBO {

	private ChequeDAO chequeDAO;

	public ChequeBOImpl() {
		chequeDAO = AbstractFactory.getInstance(ChequeDAO.class);
	}

	@Override
	public Cheque obtener(Long id) throws BusinessException {
		try {
			Cheque cheque = chequeDAO.getById(id);
			return cheque;
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Cheque> buscar(ChequeFilter filter) throws BusinessException {
		try {
			return chequeDAO.search(filter);
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void guardar(Cheque cheque) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			chequeDAO.saveOrUpdate(cheque);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void actualizar(Cheque cheque) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			chequeDAO.saveOrUpdate(cheque);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Cheque> obtenerTodos() throws BusinessException {
		try {
			return chequeDAO.getAll();
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void eliminar(Long id) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			chequeDAO.delete(id);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public boolean estaEnUso(Long id) throws BusinessException {
		throw new NotImplementedException();
	}

}
