package ar.com.wuik.classlife.dao;

import java.util.List;
import ar.com.wuik.classlife.entities.BaseEntity;
import ar.com.wuik.classlife.exceptions.DataAccessException;

public interface GenericCrudDAO<E extends BaseEntity> {

	E getById(Long id) throws DataAccessException;

	void saveOrUpdate(E e) throws DataAccessException;

	void delete(Long id) throws DataAccessException;

	List<E> getAll() throws DataAccessException;

}
