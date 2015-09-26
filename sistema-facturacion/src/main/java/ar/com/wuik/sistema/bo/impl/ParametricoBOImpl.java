package ar.com.wuik.sistema.bo.impl;

import java.util.List;

import ar.com.wuik.sistema.bo.ParametricoBO;
import ar.com.wuik.sistema.dao.BancoDAO;
import ar.com.wuik.sistema.dao.LocalidadDAO;
import ar.com.wuik.sistema.dao.ProvinciaDAO;
import ar.com.wuik.sistema.entities.Banco;
import ar.com.wuik.sistema.entities.Localidad;
import ar.com.wuik.sistema.entities.Provincia;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.LocalidadFilter;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.sistema.utils.HibernateUtil;

public class ParametricoBOImpl implements ParametricoBO {

	private ProvinciaDAO provinciaDAO;
	private LocalidadDAO localidadDAO;
	private BancoDAO bancoDAO;

	public ParametricoBOImpl() {
		localidadDAO = AbstractFactory.getInstance(LocalidadDAO.class);
		bancoDAO = AbstractFactory.getInstance(BancoDAO.class);
		provinciaDAO = AbstractFactory.getInstance(ProvinciaDAO.class);
	}

	@Override
	public List<Banco> obtenerTodosBancos() throws BusinessException {
		try {
			return bancoDAO.getAll();
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Localidad> obtenerTodosLocalidades() throws BusinessException {
		try {
			return localidadDAO.getAll();
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Localidad> obtenerLocalidadesPorProvincia(Long idProvincia)
			throws BusinessException {
		try {
			LocalidadFilter filter = new LocalidadFilter();
			filter.setIdProvincia(idProvincia);
			return localidadDAO.search(filter);
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Provincia> obtenerTodosProvincias() throws BusinessException {
		try {
			return provinciaDAO.getAll();
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
		} finally {
			HibernateUtil.closeSession();
		}
	}

}
