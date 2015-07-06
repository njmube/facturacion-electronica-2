package ar.com.wuik.sistema.dao;

import java.util.List;

import ar.com.wuik.sistema.entities.Factura;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.VentaFilter;

public interface VentaDAO extends GenericCrudDAO<Factura> {

	List<Factura> search(VentaFilter filter)
			throws DataAccessException;

	boolean isUtilizada(Long id) throws DataAccessException;
}
