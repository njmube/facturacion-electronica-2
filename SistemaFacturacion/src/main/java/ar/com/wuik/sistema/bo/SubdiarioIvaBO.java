package ar.com.wuik.sistema.bo;

import java.util.List;

import FacturaBO.SubdiarioIvaFilter;
import ar.com.wuik.sistema.entities.SubdiarioIva;
import ar.com.wuik.sistema.exceptions.BusinessException;

public interface SubdiarioIvaBO {

	List<SubdiarioIva> buscar(SubdiarioIvaFilter filter) throws BusinessException;

}
