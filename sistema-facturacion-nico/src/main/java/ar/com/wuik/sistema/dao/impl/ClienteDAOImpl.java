package ar.com.wuik.sistema.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import ar.com.wuik.sistema.dao.ClienteDAO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.Factura;
import ar.com.wuik.sistema.entities.NotaCredito;
import ar.com.wuik.sistema.entities.NotaDebito;
import ar.com.wuik.sistema.entities.Remito;
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
		// Valida si el Cliente tiene asociadas Facturas.
		Criteria criteria = getSession().createCriteria(Factura.class);
		criteria.setProjection(Projections.rowCount());
		criteria.add(Restrictions.eq("idCliente", id));
		Long cantidad = (Long) criteria.uniqueResult();
		if (cantidad > 0) {
			return Boolean.TRUE;
		} else {
			// Valida si el Cliente tiene asociadas Notas de Credito.
			criteria = getSession().createCriteria(NotaCredito.class);
			criteria.setProjection(Projections.rowCount());
			criteria.add(Restrictions.eq("idCliente", id));
			cantidad = (Long) criteria.uniqueResult();
			if (cantidad > 0) {
				return Boolean.TRUE;
			} else {
				// Valida si el Cliente tiene asociadas Notas de Debito.
				criteria = getSession().createCriteria(NotaDebito.class);
				criteria.setProjection(Projections.rowCount());
				criteria.add(Restrictions.eq("idCliente", id));
				cantidad = (Long) criteria.uniqueResult();
				if (cantidad > 0) {
					return Boolean.TRUE;
				} else {
					// Valida si el Cliente tiene asociados Remitos.
					criteria = getSession().createCriteria(Remito.class);
					criteria.setProjection(Projections.rowCount());
					criteria.add(Restrictions.eq("idCliente", id));
					cantidad = (Long) criteria.uniqueResult();
					if (cantidad > 0) {
						return Boolean.TRUE;
					}
					//TODO SI TIENE ASOCIADOS RECIBOS.
				}
			}
		}
		return Boolean.FALSE;
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
