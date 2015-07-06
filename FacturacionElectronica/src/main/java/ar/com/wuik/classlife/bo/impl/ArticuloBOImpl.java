package ar.com.wuik.classlife.bo.impl;

import java.util.List;

import ar.com.dcode.afip.utils.AbstractFactory;
import ar.com.wuik.classlife.bo.ArticuloBO;
import ar.com.wuik.classlife.dao.ArticuloDAO;
import ar.com.wuik.classlife.entities.Articulo;
import ar.com.wuik.classlife.exceptions.BusinessException;
import ar.com.wuik.classlife.exceptions.DataAccessException;

public class ArticuloBOImpl implements ArticuloBO {

	private ArticuloDAO articuloDAO;

	public ArticuloBOImpl() {
		articuloDAO = AbstractFactory.getInstance(ArticuloDAO.class);
	}

	@Override
	public Articulo getById(Long id) throws BusinessException {
		try {
			return articuloDAO.getById(id);
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
		} finally {
		}
	}

	@Override
	public void saveOrUpdate(Articulo articulo) throws BusinessException {
		try {
			articuloDAO.saveOrUpdate(articulo);
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
		} finally {
		}
	}

	@Override
	public void deleteByIDs(List<Long> idsToDelete) throws BusinessException {
		try {
			for (Long id : idsToDelete) {
				articuloDAO.delete(id);
			}
		} catch (DataAccessException daexc) {
			throw new BusinessException(daexc);
		} finally {
		}
	}

}
