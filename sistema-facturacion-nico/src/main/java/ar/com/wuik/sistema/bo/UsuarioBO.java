package ar.com.wuik.sistema.bo;

import java.util.List;

import ar.com.wuik.sistema.entities.Usuario;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.UsuarioFilter;

public interface UsuarioBO {

	Usuario getById(Long id) throws BusinessException;

	List<Usuario> search(UsuarioFilter filter) throws BusinessException;

	void saveOrUpdate(Usuario cliente) throws BusinessException;

	void deleteByIDs(List<Long> idsToDelete) throws BusinessException;

	boolean existeUsuario(String dni, Long id) throws BusinessException;

	boolean isUtilizado(List<Long> ids) throws BusinessException;

	boolean isUtilizado(Long id) throws BusinessException;

}
