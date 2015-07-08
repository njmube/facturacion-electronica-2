package ar.com.wuik.sistema.bo.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.sistema.bo.ClienteBO;
import ar.com.wuik.sistema.dao.ClienteDAO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ClienteFilter;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.sistema.utils.HibernateUtil;

public class ClienteBOImpl implements ClienteBO {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClienteBOImpl.class);
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
			LOGGER.error("obtener() - Error al obtener Cliente"); 
			throw new BusinessException(daexc, "Error al obtener Cliente");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Cliente> buscar(ClienteFilter filter) throws BusinessException {
		try {
			return clienteDAO.search(filter);
		} catch (DataAccessException daexc) {
			LOGGER.error("buscar() - Error al buscar Clientes"); 
			throw new BusinessException(daexc, "Error al buscar Clientes");
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
			LOGGER.error("guardar() - Error al guardar Cliente"); 
			throw new BusinessException(daexc, "Error al guardar Cliente");
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
			LOGGER.error("actualizar() - Error al actualizar Cliente"); 
			throw new BusinessException(daexc, "Error al actualizar Cliente");
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
			LOGGER.error("eliminar() - Error al eliminar Cliente"); 
			throw new BusinessException(daexc, "Error al eliminar Cliente");
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
			LOGGER.error("activar() - Error al activar Cliente"); 
			throw new BusinessException(daexc, "Error al activar Cliente");
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
			LOGGER.error("deactivar() - Error al desactivar Cliente"); 
			throw new BusinessException(daexc, "Error al desactivar Cliente");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Cliente> obtenerTodos() throws BusinessException {
		try {
			return clienteDAO.getAll();
		} catch (DataAccessException daexc) {
			LOGGER.error("obtenerTodos() - Error al obtener todos los Cliente"); 
			throw new BusinessException(daexc, "Error al obtener todos los Cliente");
		} finally {
			HibernateUtil.closeSession();
		}
	}
	
	@Override
	public boolean estaEnUso(Long id) throws BusinessException {
		try {
			return clienteDAO.estaEnUso(id);
		} catch (DataAccessException daexc) {
			LOGGER.error("estaEnUso() - Error al validar uso del Cliente"); 
			throw new BusinessException(daexc, "Error al validar uso del Cliente");
		} finally {
			HibernateUtil.closeSession();
		}
	}
}
