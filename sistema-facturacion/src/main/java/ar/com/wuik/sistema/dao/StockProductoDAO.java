package ar.com.wuik.sistema.dao;

import java.util.List;

import ar.com.wuik.sistema.entities.StockProducto;
import ar.com.wuik.sistema.exceptions.DataAccessException;

public interface StockProductoDAO extends GenericCrudDAO<StockProducto> {

	List<StockProducto> search(Long idProducto) throws DataAccessException;
	
}
