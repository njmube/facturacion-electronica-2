package ar.com.wuik.sistema.bo;

import java.util.List;

import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.ClienteFilter;

public interface ClienteBO {

	Cliente obtener(Long id) throws BusinessException;

	List<Cliente> buscar(ClienteFilter filter) throws BusinessException;

	void guardar(Cliente cliente) throws BusinessException;

	void actualizar(Cliente cliente) throws BusinessException;

	void eliminar(Long idToDelete) throws BusinessException;

	boolean estaEnUso(Long id) throws BusinessException;

	List<Cliente> obtenerTodos() throws BusinessException;

	void activar(Long id) throws BusinessException;

	void desactivar(Long id) throws BusinessException;
}
