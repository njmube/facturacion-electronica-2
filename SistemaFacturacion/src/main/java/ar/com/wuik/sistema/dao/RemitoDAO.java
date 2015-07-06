package ar.com.wuik.sistema.dao;

import java.util.List;

import ar.com.wuik.sistema.entities.Remito;
import ar.com.wuik.sistema.exceptions.DataAccessException;

public interface RemitoDAO extends GenericCrudDAO<Remito> {

//	List<Remito> search(RemitoFilter filter) throws DataAccessException;

	void actualizarAsigFacturaRemito(Long idFactura,
			List<Long> idsRemitosAsignados) throws DataAccessException;
}
