package ar.com.wuik.sistema.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.sistema.bo.ChequeBO;
import ar.com.wuik.sistema.bo.SubdiarioIvaBO;
import ar.com.wuik.sistema.dao.FacturaDAO;
import ar.com.wuik.sistema.dao.NotaCreditoDAO;
import ar.com.wuik.sistema.dao.NotaDebitoDAO;
import ar.com.wuik.sistema.entities.Factura;
import ar.com.wuik.sistema.entities.NotaCredito;
import ar.com.wuik.sistema.entities.NotaDebito;
import ar.com.wuik.sistema.entities.SubdiarioIva;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.FacturaFilter;
import ar.com.wuik.sistema.filters.NotaCreditoFilter;
import ar.com.wuik.sistema.filters.NotaDebitoFilter;
import ar.com.wuik.sistema.filters.SubdiarioIvaFilter;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.sistema.utils.HibernateUtil;
import ar.com.wuik.swing.utils.WUtils;

public class SubdiarioIvaBOImpl implements SubdiarioIvaBO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ChequeBO.class);
	private FacturaDAO facturaDAO;
	private NotaCreditoDAO notaCreditoDAO;
	private NotaDebitoDAO notaDebitoDAO;

	public SubdiarioIvaBOImpl() {
		facturaDAO = AbstractFactory.getInstance(FacturaDAO.class);
		notaCreditoDAO = AbstractFactory.getInstance(NotaCreditoDAO.class);
		notaDebitoDAO = AbstractFactory.getInstance(NotaDebitoDAO.class);
	}

	@Override
	public List<SubdiarioIva> buscar(SubdiarioIvaFilter filter)
			throws BusinessException {
		try {
			
			List<SubdiarioIva> subdiariosIva = new ArrayList<SubdiarioIva>(); 
			
			Date desde = filter.getDesde();
			Date hasta = filter.getHasta();
			
			FacturaFilter filterFactura = new FacturaFilter();
			filterFactura.setDesde(desde);
			filterFactura.setHasta(hasta);
			List<Factura> facturas = facturaDAO.search(filterFactura);
			if (WUtils.isNotEmpty(facturas)) {
				for (Factura factura : facturas) {
					subdiariosIva.add(new SubdiarioIva(factura));
				}
			}
			
			NotaDebitoFilter filterNotaDebito = new NotaDebitoFilter();
			filterNotaDebito.setDesde(desde);
			filterNotaDebito.setHasta(hasta);
			List<NotaDebito> notasDebito = notaDebitoDAO.search(filterNotaDebito);
			if (WUtils.isNotEmpty(notasDebito)) {
				for (NotaDebito notaDebito : notasDebito) {
					subdiariosIva.add(new SubdiarioIva(notaDebito));
				}
			}
			
			NotaCreditoFilter filterNotaCredito = new NotaCreditoFilter();
			filterNotaCredito.setDesde(desde);
			filterNotaCredito.setHasta(hasta);
			List<NotaCredito> notasCredito = notaCreditoDAO.search(filterNotaCredito);
			if (WUtils.isNotEmpty(notasCredito)) {
				for (NotaCredito notaCredito : notasCredito) {
					subdiariosIva.add(new SubdiarioIva(notaCredito));
				}
			}
			return subdiariosIva;
		} catch (DataAccessException daexc) {
			LOGGER.error("buscar() - Error al buscar Subdiario de IVA", daexc);
			throw new BusinessException(daexc, "Error al buscar Subdiario de IVA");
		} finally {
			HibernateUtil.closeSession();
		}
	}

}
