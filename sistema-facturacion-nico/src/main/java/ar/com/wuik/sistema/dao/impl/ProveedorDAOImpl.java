package ar.com.wuik.sistema.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import ar.com.wuik.sistema.dao.ProveedorDAO;
import ar.com.wuik.sistema.entities.Proveedor;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ProveedorFilter;
import ar.com.wuik.swing.utils.WUtils;

public class ProveedorDAOImpl extends GenericCrudHBDAOImpl<Proveedor> implements
		ProveedorDAO {

	public ProveedorDAOImpl() {
		super(Proveedor.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Proveedor> search(ProveedorFilter filter)
			throws DataAccessException {
		try {
			Criteria criteria = buildCriteria(filter);
			return criteria.list();
		} catch (HibernateException hexc) {
			throw new DataAccessException(hexc);
		}
	}

	@Override
	public boolean estaEnUso(Long id) throws DataAccessException {
		return Boolean.FALSE;
	}

	private Criteria buildCriteria(ProveedorFilter filter) {

		Criteria criteria = getSession().createCriteria(Proveedor.class);

		String razonSocial = filter.getRazonSocial();
		String cuit = filter.getCuit();
		Boolean activo = filter.getActivo();

		if (WUtils.isNotEmpty(razonSocial)) {
			Criterion criterion = Restrictions.like("razonSocial", razonSocial,
					MatchMode.ANYWHERE);
			criteria.add(criterion);
		}

		if (WUtils.isNotEmpty(cuit)) {
			Criterion criterion = Restrictions.eq("cuit", cuit);
			criteria.add(criterion);
		}

		if (null != activo) {
			Criterion criterion = Restrictions.eq("activo", activo);
			criteria.add(criterion);
		}

		return criteria;
	}
}
