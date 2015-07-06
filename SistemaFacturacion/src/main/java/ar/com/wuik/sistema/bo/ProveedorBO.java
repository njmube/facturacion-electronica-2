package ar.com.wuik.sistema.bo;

import java.util.List;

import ar.com.wuik.sistema.entities.Proveedor;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.ProveedorFilter;

public interface ProveedorBO {

	Proveedor obtener(Long id) throws BusinessException;

	List<Proveedor> buscar(ProveedorFilter filter) throws BusinessException;

	List<Proveedor> obtenerTodos() throws BusinessException;

	void guardar(Proveedor proveedor) throws BusinessException;

	void actualizar(Proveedor proveedor) throws BusinessException;

	boolean estaEnUso(Long id) throws BusinessException;

	void eliminar(Long id) throws BusinessException;
}
