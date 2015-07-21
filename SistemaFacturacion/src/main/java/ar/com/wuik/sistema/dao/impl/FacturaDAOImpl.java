package ar.com.wuik.sistema.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import ar.com.wuik.sistema.dao.FacturaDAO;
import ar.com.wuik.sistema.entities.Factura;
import ar.com.wuik.sistema.entities.enums.EstadoFacturacion;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.FacturaFilter;
import ar.com.wuik.swing.utils.WUtils;

public class FacturaDAOImpl extends GenericCrudHBDAOImpl<Factura> implements
		FacturaDAO {

	public FacturaDAOImpl() {
		super(Factura.class);
	}

	@Override
	public Factura getById(Long id) throws DataAccessException {
		Factura factura = super.getById(id);
		Hibernate.initialize(factura.getDetalles());
		Hibernate.initialize(factura.getRemitos());
		return factura;
	}

	@Override
	public List<Factura> search(FacturaFilter filter)
			throws DataAccessException {
		try {
			Criteria criteria = buildCriteria(filter);
			List<Factura> facturas = criteria.list();
			if (WUtils.isNotEmpty(facturas)) {
				Boolean inicializarDetalles = filter.getInicializarDetalles();
				if (null != inicializarDetalles && inicializarDetalles) {
					for (Factura factura : facturas) {
						Hibernate.initialize(factura.getDetalles());
					}
				}
			}
			return facturas;
		} catch (HibernateException hbexc) {
			throw new DataAccessException(hbexc);
		}
	}

	private Criteria buildCriteria(FacturaFilter filter) {

		Criteria criteria = getSession().createCriteria(Factura.class);

		Long idCliente = filter.getIdCliente();
		Boolean asignado = filter.getAsignado();
		Boolean activo = filter.getActivo();
		List<Long> idsToExclude = filter.getIdsToExclude();
		List<Long> idsToInclude = filter.getIdsToInclude();
		EstadoFacturacion estadoFacturacion= filter.getEstadoFacturacion();

		if (null != idCliente) {
			criteria.add(Restrictions.eq("idCliente", idCliente));
		}

		if (null != asignado) {
			criteria.createAlias("notasCredito", "notasCredito",
					JoinType.LEFT_OUTER_JOIN);
			if (asignado) {
				criteria.add(Restrictions.sizeGt("notasCredito", 0));
			} else {
				criteria.add(Restrictions.sizeEq("notasCredito", 0));
			}
		}

		if (null != estadoFacturacion) {
				criteria.add(Restrictions.eq("estadoFacturacion", estadoFacturacion));
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

	@Override
	public boolean isUtilizada(Long id) throws DataAccessException {
		// TODO Auto-generated method stub
		return false;
	}

}
