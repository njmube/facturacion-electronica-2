package ar.com.wuik.sistema.bo.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.sistema.bo.ParametroBO;
import ar.com.wuik.sistema.dao.ParametroDAO;
import ar.com.wuik.sistema.entities.Parametro;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.sistema.utils.HibernateUtil;
import ar.com.wuik.swing.utils.WUtils;

public class ParametroBOImpl implements ParametroBO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ParametroBOImpl.class);

	private ParametroDAO parametroDAO;

	public ParametroBOImpl() {
		parametroDAO = AbstractFactory.getInstance(ParametroDAO.class);
	}

	@Override
	public Parametro obtener(Long id) throws BusinessException {
		try {
			return parametroDAO.getById(id);
		} catch (DataAccessException daexc) {
			LOGGER.error("obtener() - Error al obtener Parámetro", daexc);
			throw new BusinessException(daexc, "Error al obtener Parámetro");
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
			LOGGER.error("actualizar() - Error al actualizar Parámetro", daexc);
			throw new BusinessException(daexc, "Error al actualizar Parámetro");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public String getNroRecibo() throws BusinessException {
		try {
			Parametro parametro = parametroDAO.getById(1L);
			return WUtils.leftPadding(parametro.getNroRecibo() + "", 8, "0");
		} catch (DataAccessException daexc) {
			LOGGER.error("getNroRecibo() - Error al obtener Nro. de Recibo",
					daexc);
			throw new BusinessException(daexc,
					"Error al obtener Nro. de Recibo");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public String getNroRemito() throws BusinessException {
		try {
			Parametro parametro = parametroDAO.getById(1L);
			return WUtils.leftPadding(parametro.getNroRemito() + "", 8, "0");
		} catch (DataAccessException daexc) {
			LOGGER.error("getNroRemito() - Error al obtener Nro. de Remito",
					daexc);
			throw new BusinessException(daexc,
					"Error al obtener Nro. de Remito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public Parametro getParametro() throws BusinessException {
		try {
			return parametroDAO.getById(1L);
		} catch (DataAccessException daexc) {
			LOGGER.error("getParametro() - Error al obtener Parámetro", daexc);
			throw new BusinessException(daexc, "Error al obtener Parámetro");
		} finally {
			HibernateUtil.closeSession();
		}
	}

}
