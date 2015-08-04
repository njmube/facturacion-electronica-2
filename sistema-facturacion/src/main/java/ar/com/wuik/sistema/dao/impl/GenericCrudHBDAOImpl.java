package ar.com.wuik.sistema.dao.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import ar.com.wuik.sistema.dao.GenericCrudDAO;
import ar.com.wuik.sistema.entities.BaseEntity;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.utils.HibernateUtil;

public class GenericCrudHBDAOImpl<E extends BaseEntity> implements
		GenericCrudDAO<E> {

	private Class<E> domainClass;

	public GenericCrudHBDAOImpl(Class<E> domainClass) {
		this.domainClass = domainClass;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E getById(Long id) throws DataAccessException {
		try {
			return (E) getSession().get(domainClass, id);
		} catch (HibernateException hexc) {
			throw new DataAccessException(hexc);
		}
	}

	@Override
	public void saveOrUpdate(E e) throws DataAccessException {
		try {
			getSession().saveOrUpdate(e);
			getSession().flush();
		} catch (HibernateException hexc) {
			throw new DataAccessException(hexc);
		}
	}

	@Override
	public void delete(Long id) throws DataAccessException {
		try {
			getSession().delete(getById(id));
			getSession().flush();
		} catch (HibernateException hexc) {
			throw new DataAccessException(hexc);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> getAll() throws DataAccessException {
		try {
			return getSession().createCriteria(domainClass).list();
		} catch (HibernateException hexc) {
			throw new DataAccessException(hexc);
		}
	}

	public Session getSession() {
		return HibernateUtil.getSession();
	}

}
