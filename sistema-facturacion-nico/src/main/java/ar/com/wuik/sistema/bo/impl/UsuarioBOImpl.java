package ar.com.wuik.sistema.bo.impl;

import java.util.List;

import ar.com.wuik.sistema.bo.UsuarioBO;
import ar.com.wuik.sistema.dao.UsuarioDAO;
import ar.com.wuik.sistema.entities.Usuario;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.UsuarioFilter;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.sistema.utils.HibernateUtil;
import ar.com.wuik.swing.utils.WEncrypterUtil;
import ar.com.wuik.swing.utils.WUtils;

public class UsuarioBOImpl implements UsuarioBO {

	private UsuarioDAO usuarioDAO;

	public UsuarioBOImpl() {
		usuarioDAO = AbstractFactory.getInstance(UsuarioDAO.class);
	}

	@Override
	public Usuario getById(Long id) throws BusinessException {
		try {
			return usuarioDAO.getById(id);
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Usuario> search(UsuarioFilter filter) throws BusinessException {
		try {
			String password = filter.getPassword();
			if (WUtils.isNotEmpty(password)) {
				final String encPassword = WEncrypterUtil.encrypt(password);
				filter.setPassword(encPassword);
			}
			return usuarioDAO.search(filter);
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void saveOrUpdate(Usuario usuario) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			usuarioDAO.saveOrUpdate(usuario);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void deleteByIDs(List<Long> idsToDelete) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			for (Long id : idsToDelete) {
				usuarioDAO.delete(id);
			}
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	/**
	 * @see ar.com.wuik.sistema.bo.UsuarioBO#existeUsuario(java.lang.String,
	 *      java.lang.Long)
	 */
	@Override
	public boolean existeUsuario(String dni, Long id) throws BusinessException {
		try {
			return usuarioDAO.existeUsuario(dni, id);
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public boolean isUtilizado(List<Long> ids) throws BusinessException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUtilizado(Long id) throws BusinessException {
		// TODO Auto-generated method stub
		return false;
	}
}
