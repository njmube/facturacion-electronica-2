package ar.com.wuik.sistema.bo.impl;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.sistema.bo.ChequeBO;
import ar.com.wuik.sistema.dao.ChequeDAO;
import ar.com.wuik.sistema.entities.Cheque;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ChequeFilter;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.sistema.utils.HibernateUtil;

public class ChequeBOImpl implements ChequeBO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ChequeBO.class);
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
			LOGGER.error("obtener() - Error al obtener Cheque",daexc);
			throw new BusinessException(daexc, "Error al obtener Cheque");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Cheque> buscar(ChequeFilter filter) throws BusinessException {
		try {
			return chequeDAO.search(filter);
		} catch (DataAccessException daexc) {
			LOGGER.error("buscar() - Error al buscar Cheques",daexc);
			throw new BusinessException(daexc, "Error al buscar Cheques");
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
			LOGGER.error("guardar() - Error al guardar Cheque",daexc);
			throw new BusinessException(daexc, "Error al guardar Cheque");
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
			LOGGER.error("actualizar() - Error al actualizar Cheque",daexc); 
			throw new BusinessException(daexc, "Error al actualizar Cheque");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Cheque> obtenerTodos() throws BusinessException {
		try {
			return chequeDAO.getAll();
		} catch (DataAccessException daexc) {
			LOGGER.error("obtenerTodos() - Error al obtener todos los Cheques",daexc);
			throw new BusinessException(daexc, "Error al obtener todos los Cheques");
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
			LOGGER.error("eliminar() - Error al eliminar Cheque",daexc); 
			throw new BusinessException(daexc, "Error al eliminar Cheque");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public boolean estaEnUso(Long id) throws BusinessException {
		throw new NotImplementedException();
	}

}
