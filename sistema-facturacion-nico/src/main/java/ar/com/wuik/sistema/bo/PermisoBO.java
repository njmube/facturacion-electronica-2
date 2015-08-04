package ar.com.wuik.sistema.bo;

import java.util.List;

import ar.com.wuik.sistema.entities.Permiso;
import ar.com.wuik.sistema.exceptions.BusinessException;

public interface PermisoBO {

	List<Permiso> getAll() throws BusinessException;

	Permiso getById(Long id) throws BusinessException;

}
