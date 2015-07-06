package ar.com.wuik.sistema.dao.impl;

import ar.com.wuik.sistema.dao.TipoProductoDAO;
import ar.com.wuik.sistema.entities.TipoProducto;

public class TipoProductoDAOImpl extends GenericCrudHBDAOImpl<TipoProducto>
		implements TipoProductoDAO {

	public TipoProductoDAOImpl() {
		super(TipoProducto.class);
	}

}
