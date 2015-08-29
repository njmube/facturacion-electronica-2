package ar.com.wuik.sistema.dao;

import java.util.List;

import ar.com.wuik.sistema.entities.Comprobante;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ComprobanteProveedorFilter;

public interface ComprobanteProveedorDAO extends GenericCrudDAO<Comprobante> {

	List<Comprobante> search(ComprobanteProveedorFilter filter)
			throws DataAccessException;

	boolean isUtilizada(Long id) throws DataAccessException;

}
