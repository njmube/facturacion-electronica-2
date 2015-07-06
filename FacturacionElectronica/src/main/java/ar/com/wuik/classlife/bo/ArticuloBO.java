package ar.com.wuik.classlife.bo;

import java.util.List;

import ar.com.wuik.classlife.entities.Articulo;
import ar.com.wuik.classlife.exceptions.BusinessException;

public interface ArticuloBO {

	Articulo getById(Long id) throws BusinessException;
	
	void saveOrUpdate(Articulo articulo) throws BusinessException;
	
	void deleteByIDs(List<Long> idsToDelete) throws BusinessException;
}
