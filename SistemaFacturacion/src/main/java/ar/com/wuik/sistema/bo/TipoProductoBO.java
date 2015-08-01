package ar.com.wuik.sistema.bo;

import java.util.List;

import ar.com.wuik.sistema.entities.TipoProducto;
import ar.com.wuik.sistema.exceptions.BusinessException;

public interface TipoProductoBO {

	List<TipoProducto> obtenerTodos() throws BusinessException;

	TipoProducto obtener(Long id) throws BusinessException;

	void guardar(TipoProducto tipoProducto) throws BusinessException;

	void actualizar(TipoProducto tipoProducto) throws BusinessException;

	void eliminar(Long id) throws BusinessException;

	boolean estaEnUso(Long selectedItem) throws BusinessException;

}
