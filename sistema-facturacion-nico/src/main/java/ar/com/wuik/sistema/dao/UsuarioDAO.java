package ar.com.wuik.sistema.dao;

import java.util.List;

import ar.com.wuik.sistema.entities.Usuario;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.UsuarioFilter;


public interface UsuarioDAO extends GenericCrudDAO<Usuario> {


	List<Usuario> search( UsuarioFilter filter ) throws DataAccessException;

	boolean existeUsuario( String dni, Long id ) throws DataAccessException;
}
