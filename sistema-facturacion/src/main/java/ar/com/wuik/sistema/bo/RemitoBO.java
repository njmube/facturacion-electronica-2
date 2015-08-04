package ar.com.wuik.sistema.bo;

import java.util.List;

import ar.com.wuik.sistema.entities.Remito;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.RemitoFilter;
import ar.com.wuik.sistema.reportes.entities.RemitoDTO;

public interface RemitoBO {

	Remito obtener(Long id) throws BusinessException;

	List<Remito> buscar(RemitoFilter filter) throws BusinessException;

	void guardar(Remito remito) throws BusinessException;

	void actualizar(Remito remito) throws BusinessException;

	void eliminar(Long id) throws BusinessException;

	boolean estaEnUso(Long id) throws BusinessException;

	List<Remito> obtenerTodos() throws BusinessException;

	void cancelar(Long id) throws BusinessException;

	RemitoDTO obtenerDTO(Long id) throws BusinessException;

}
