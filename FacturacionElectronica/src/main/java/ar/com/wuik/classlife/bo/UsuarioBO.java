package ar.com.wuik.classlife.bo;

import java.util.List;

import ar.com.wuik.classlife.entities.Usuario;
import ar.com.wuik.classlife.exceptions.BusinessException;


public interface UsuarioBO {


	Usuario getById( Long id ) throws BusinessException;

	void saveOrUpdate( Usuario cliente ) throws BusinessException;

	void deleteByIDs( List<Long> idsToDelete ) throws BusinessException;

}
