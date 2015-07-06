package ar.com.wuik.sistema.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import ar.com.wuik.sistema.dao.ClienteDAO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ClienteFilter;
import ar.com.wuik.swing.utils.WUtils;

public class ClienteDAOImpl extends GenericCrudHBDAOImpl<Cliente> implements
		ClienteDAO {

	public ClienteDAOImpl() {
		super(Cliente.class);
	}
	
	@Override
	public boolean estaEnUso(Long id) throws DataAccessException {
		return false;
	}

	@Override
	public List<Cliente> search(ClienteFilter filter)
			throws DataAccessException {
		Criteria criteria = buildCriteria(filter);
		try {
			return criteria.list();
		} catch (HibernateException hbexc) {
			throw new DataAccessException(hbexc);
		}
	}

	private Criteria buildCriteria(ClienteFilter filter) {
		Criteria criteria = getSession().createCriteria(Cliente.class);

		String cuit = filter.getCuit();
		String razonSocial = filter.getRazonSocial();

		if (WUtils.isNotEmpty(cuit)) {
			criteria.add(Restrictions.eq("cuit", cuit));
		}

		if (WUtils.isNotEmpty(razonSocial)) {
			criteria.add(Restrictions.like("razonSocial", razonSocial,
					MatchMode.ANYWHERE));
		}
		return criteria;
	}

}
