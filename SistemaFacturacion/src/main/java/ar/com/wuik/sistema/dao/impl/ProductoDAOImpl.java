package ar.com.wuik.sistema.dao.impl;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import ar.com.wuik.sistema.dao.ProductoDAO;
import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.ProductoFilter;

public class ProductoDAOImpl extends GenericCrudHBDAOImpl<Producto> implements
		ProductoDAO {

	public ProductoDAOImpl() {
		super(Producto.class);
	}

	@Override
	public List<Producto> search(ProductoFilter filter)
			throws DataAccessException {
		throw new NotImplementedException();
	}

}
