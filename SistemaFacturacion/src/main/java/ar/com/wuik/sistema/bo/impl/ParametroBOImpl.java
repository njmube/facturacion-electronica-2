package ar.com.wuik.sistema.bo.impl;

import ar.com.wuik.sistema.bo.ParametroBO;
import ar.com.wuik.sistema.dao.ParametroDAO;
import ar.com.wuik.sistema.entities.Parametro;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.sistema.utils.HibernateUtil;
import ar.com.wuik.swing.utils.WUtils;

public class ParametroBOImpl implements ParametroBO {
	private ParametroDAO parametroDAO;

	public ParametroBOImpl() {
		parametroDAO = AbstractFactory.getInstance(ParametroDAO.class);
	}

	@Override
	public Parametro obtener(Long id) throws BusinessException {
		try {
			return parametroDAO.getById(id);
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void actualizar(Parametro parametro) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			parametroDAO.saveOrUpdate(parametro);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public String getNroRecibo() throws BusinessException {
		try {
			Parametro parametro = parametroDAO.getById(1L);
			return parametro.getPrefRecibo() + "-"
					+ WUtils.leftPadding(parametro.getNroRecibo() + "", 8, "0");
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public String getNroRemito() throws BusinessException {
		try {
			Parametro parametro = parametroDAO.getById(1L);
			return parametro.getPrefRemito() + "-"
					+ WUtils.leftPadding(parametro.getNroRemito() + "", 8, "0");
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

}
