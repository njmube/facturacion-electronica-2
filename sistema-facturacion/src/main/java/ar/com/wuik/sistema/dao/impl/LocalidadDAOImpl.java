package ar.com.wuik.sistema.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import ar.com.wuik.sistema.dao.LocalidadDAO;
import ar.com.wuik.sistema.entities.Localidad;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.LocalidadFilter;

public class LocalidadDAOImpl extends GenericCrudHBDAOImpl<Localidad> implements
		LocalidadDAO {

	public LocalidadDAOImpl() {
		super(Localidad.class);
	}

	@Override
	public List<Localidad> search(LocalidadFilter filter)
			throws DataAccessException {

		try {
			Criteria criteria = buildCriteria(filter);
			return criteria.list();
		} catch (HibernateException hbexc) {
			throw new DataAccessException(hbexc);
		}
	}

	private Criteria buildCriteria(LocalidadFilter filter) {

		Long idProvincia = filter.getIdProvincia();

		Criteria criteria = getSession().createCriteria(Localidad.class);
		criteria.createAlias("provincia", "provincia");

		criteria.add(Restrictions.eq("provincia.id", idProvincia));
		return criteria;
	}

}
