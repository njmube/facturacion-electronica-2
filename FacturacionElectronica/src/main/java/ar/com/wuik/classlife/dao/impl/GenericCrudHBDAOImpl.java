package ar.com.wuik.classlife.dao.impl;

import java.util.List;

import ar.com.dcode.afip.utils.DB4OUtil;
import ar.com.wuik.classlife.dao.GenericCrudDAO;
import ar.com.wuik.classlife.entities.BaseEntity;
import ar.com.wuik.classlife.exceptions.DataAccessException;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.db4o.query.Query;

public class GenericCrudHBDAOImpl<E extends BaseEntity> implements
		GenericCrudDAO<E> {

	private Class<E> domainClass;

	protected ObjectContainer db = DB4OUtil.getContainer();

	public GenericCrudHBDAOImpl(Class<E> domainClass) {
		this.domainClass = domainClass;
	}

	@Override
	public E getById(final Long id) throws DataAccessException {
		try {
			ObjectSet<E> objectSet = db.query(new Predicate<E>() {

				/**
				 * Serial UID.
				 */
				private static final long serialVersionUID = -7590032676404892178L;

				public boolean match(E e) {
					return e.getId().equals(id);
				}
			});
			if (!objectSet.isEmpty()) {
				return (E) objectSet.get(0);
			}
			return null;
		} catch (Exception exc) {
			throw new DataAccessException(exc);
		}
	}

	@Override
	public void saveOrUpdate(E e) throws DataAccessException {
		try {

			if (null == e.getId()) {
				e.setId(DB4OUtil.getSequence());
			}
			db.store(e);
		} catch (Exception exc) {
			throw new DataAccessException(exc);
		}
	}

	@Override
	public void delete(final Long id) throws DataAccessException {
		try {
			db.delete(getById(id));
		} catch (Exception exc) {
			throw new DataAccessException(exc);
		}
	}

	@Override
	public List<E> getAll() throws DataAccessException {
		try {
			Query q = db.query();
			q.constrain(domainClass);
			return q.execute();
		} catch (Exception exc) {
			throw new DataAccessException(exc);
		}
	}

}
