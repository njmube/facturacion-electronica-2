package ar.com.wuik.sistema.bo;

import java.util.List;

import ar.com.wuik.sistema.entities.Comprobante;
import ar.com.wuik.sistema.entities.enums.TipoComprobante;
import ar.com.wuik.sistema.entities.enums.TipoLetraComprobante;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.filters.ComprobanteFilter;
import ar.com.wuik.sistema.reportes.entities.ComprobanteDTO;

public interface ComprobanteBO {

	Comprobante obtener(Long id) throws BusinessException;

	List<Comprobante> buscar(ComprobanteFilter filter) throws BusinessException;

	void guardar(Comprobante comprobante) throws BusinessException;

	void guardarRegistrarAFIP(Comprobante comprobante) throws BusinessException;

	void registrarAFIP(Comprobante comprobante) throws BusinessException;

	void actualizar(Comprobante comprobante) throws BusinessException;

	void cancelar(Long id) throws BusinessException;

	ComprobanteDTO obtenerDTO(Long id) throws BusinessException;

	Long obtenerUltimoNroComprobante(TipoComprobante tipoComprobante,
			TipoLetraComprobante tipoLetraComprobante) throws BusinessException;
}
