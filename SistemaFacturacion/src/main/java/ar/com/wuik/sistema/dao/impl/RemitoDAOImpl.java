package ar.com.wuik.sistema.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import ar.com.wuik.sistema.dao.RemitoDAO;
import ar.com.wuik.sistema.entities.Remito;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.RemitoFilter;
import ar.com.wuik.swing.utils.WUtils;

public class RemitoDAOImpl extends GenericCrudHBDAOImpl<Remito> implements
		RemitoDAO {

	public RemitoDAOImpl() {
		super(Remito.class);
	}

	@Override
	public Remito getById(Long id) throws DataAccessException {
		Remito remito = super.getById(id);
		Hibernate.initialize(remito.getDetalles());
		return remito;
	}

	@Override
	public boolean estaEnUso(Long id) throws DataAccessException {
		try {
			Criteria criteria = getSession().createCriteria(Remito.class);
			criteria.setProjection(Projections.rowCount());
			criteria.add(Restrictions.eq("id", id));
			criteria.createAlias("factura", "factura", JoinType.LEFT_OUTER_JOIN);
			criteria.add(Restrictions.isNotNull("factura.id"));
			Long count = (Long) criteria.uniqueResult();
			return count > 0;
		} catch (HibernateException hbexc) {
			throw new DataAccessException(hbexc);
		}
	}

	@Override
	public List<Remito> search(RemitoFilter filter) throws DataAccessException {
		try {
			Criteria criteria = buildCriteria(filter);
			List<Remito> remitos = criteria.list();
			if (WUtils.isNotEmpty(remitos)) {
				Boolean inicializarDetalles = filter.getInicializarDetalles();
				if (null != inicializarDetalles && inicializarDetalles) {
					for (Remito remito : remitos) {
						Hibernate.initialize(remito.getDetalles());
					}
				}
			}
			return remitos;
		} catch (HibernateException hbexc) {
			throw new DataAccessException(hbexc);
		}
	}

	private Criteria buildCriteria(RemitoFilter filter) {

		Criteria criteria = getSession().createCriteria(Remito.class);

		Long idCliente = filter.getIdCliente();
		Boolean asignado = filter.getAsignado();
		Boolean activo = filter.getActivo();
		List<Long> idsToExclude = filter.getIdsToExclude();
		List<Long> idsToInclude = filter.getIdsToInclude();

		if (null != idCliente) {
			criteria.add(Restrictions.eq("idCliente", idCliente));
		}

		if (null != asignado) {
			criteria.createAlias("factura", "factura", JoinType.LEFT_OUTER_JOIN);
			if (asignado) {
				criteria.add(Restrictions.isNotNull("factura.id"));
			} else {
				criteria.add(Restrictions.isNull("factura.id"));
			}
		}

		if (null != activo) {
			criteria.add(Restrictions.eq("activo", activo));
		}

		if (WUtils.isNotEmpty(idsToInclude)) {
			criteria.add(Restrictions.in("id", idsToInclude));
		}

		if (WUtils.isNotEmpty(idsToExclude)) {
			criteria.add(Restrictions.not(Restrictions.in("id", idsToExclude)));
		}

		return criteria;
	}

}
