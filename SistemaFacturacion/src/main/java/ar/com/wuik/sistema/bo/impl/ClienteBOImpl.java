package ar.com.wuik.sistema.bo.impl;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import ar.com.wuik.sistema.bo.ClienteBO;
import ar.com.wuik.sistema.dao.ClienteDAO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ClienteFilter;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.sistema.utils.HibernateUtil;

public class ClienteBOImpl implements ClienteBO {

	private ClienteDAO clienteDAO;

	public ClienteBOImpl() {
		clienteDAO = AbstractFactory.getInstance(ClienteDAO.class);
	}

	@Override
	public Cliente obtener(Long id) throws BusinessException {
		try {
			Cliente cliente = clienteDAO.getById(id);
			return cliente;
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Cliente> buscar(ClienteFilter filter) throws BusinessException {
		try {
			return clienteDAO.search(filter);
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void guardar(Cliente cliente) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			clienteDAO.saveOrUpdate(cliente);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void actualizar(Cliente cliente) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			clienteDAO.saveOrUpdate(cliente);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void eliminar(Long idToDelete) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			clienteDAO.delete(idToDelete);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void activar(Long id) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			Cliente cliente = clienteDAO.getById(id);
			cliente.setActivo(Boolean.TRUE);
			clienteDAO.saveOrUpdate(cliente);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void desactivar(Long id) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			Cliente cliente = clienteDAO.getById(id);
			cliente.setActivo(Boolean.FALSE);
			clienteDAO.saveOrUpdate(cliente);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Cliente> obtenerTodos() throws BusinessException {
		try {
			return clienteDAO.getAll();
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}
	
	@Override
	public boolean estaEnUso(Long id) throws BusinessException {
		try {
			return clienteDAO.estaEnUso(id);
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}
}
