package ar.com.wuik.sistema.bo;

import java.util.List;

import ar.com.wuik.sistema.entities.NotaCredito;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.NotaCreditoFilter;
import ar.com.wuik.sistema.reportes.entities.NotaCreditoDTO;

public interface NotaCreditoBO {

	NotaCredito obtener(Long id) throws BusinessException;

	List<NotaCredito> buscar(NotaCreditoFilter filter) throws BusinessException;

	void guardar(NotaCredito notaCredito) throws BusinessException;

	void guardarRegistrarAFIP(NotaCredito notaCredito) throws BusinessException;
	
	void registrarAFIP(NotaCredito notaCredito) throws BusinessException;

	void actualizar(NotaCredito notaCredito) throws BusinessException;

	NotaCreditoDTO obtenerDTO(Long id) throws BusinessException;

	Long obtenerUltimoNroComprobante() throws BusinessException;
}
