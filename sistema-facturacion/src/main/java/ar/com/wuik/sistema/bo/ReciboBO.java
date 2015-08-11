package ar.com.wuik.sistema.bo;

import java.util.List;

import ar.com.wuik.sistema.entities.Recibo;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.ReciboFilter;
import ar.com.wuik.sistema.reportes.entities.ReciboDTO;


public interface ReciboBO {

	Recibo obtener(Long id) throws BusinessException;

	List<Recibo> buscar(ReciboFilter filter) throws BusinessException;

	void guardar(Recibo recibo) throws BusinessException;

	void actualizar(Recibo recibo) throws BusinessException;

	void eliminar(Long id) throws BusinessException;

	ReciboDTO obtenerDTO(Long id) throws BusinessException;

}
