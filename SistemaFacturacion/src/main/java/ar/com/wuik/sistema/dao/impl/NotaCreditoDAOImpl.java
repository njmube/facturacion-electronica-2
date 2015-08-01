package ar.com.wuik.sistema.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import ar.com.wuik.sistema.dao.NotaCreditoDAO;
import ar.com.wuik.sistema.entities.NotaCredito;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.NotaCreditoFilter;

public class NotaCreditoDAOImpl extends GenericCrudHBDAOImpl<NotaCredito>
		implements NotaCreditoDAO {

	public NotaCreditoDAOImpl() {
		super(NotaCredito.class);
	}

	@Override
	public NotaCredito getById(Long id) throws DataAccessException {
		NotaCredito notaCredito = super.getById(id);
		Hibernate.initialize(notaCredito.getDetalles());
		Hibernate.initialize(notaCredito.getFacturas());
		return notaCredito;
	}

	@Override
	public List<NotaCredito> search(NotaCreditoFilter filter)
			throws DataAccessException {
		try {
			Criteria criteria = buildCriteria(filter);
			return criteria.list();
		} catch (HibernateException hbexc) {
			throw new DataAccessException(hbexc);
		}
	}

	private Criteria buildCriteria(NotaCreditoFilter filter) {

		Criteria criteria = getSession().createCriteria(NotaCredito.class);

		Long idCliente = filter.getIdCliente();
		Date desde = filter.getDesde();
		Date hasta = filter.getHasta();

		if (null != idCliente) {
			criteria.add(Restrictions.eq("idCliente", idCliente));
		}

		if (null != desde && null != hasta) {
			criteria.add(Restrictions.between("fechaVenta", desde, hasta));
		}

		return criteria;
	}

	@Override
	public boolean isUtilizada(Long id) throws DataAccessException {
		// TODO Auto-generated method stub
		return false;
	}

}
