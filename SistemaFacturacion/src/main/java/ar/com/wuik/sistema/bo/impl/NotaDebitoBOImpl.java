package ar.com.wuik.sistema.bo.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import FEV1.dif.afip.gov.ar.entities.AlicuotaIVA;
import FEV1.dif.afip.gov.ar.entities.Comprobante;
import FEV1.dif.afip.gov.ar.entities.ComprobanteAsociado;
import FEV1.dif.afip.gov.ar.entities.Resultado;
import FEV1.dif.afip.gov.ar.entities.TipoComprobante;
import FEV1.dif.afip.gov.ar.entities.TipoConcepto;
import FEV1.dif.afip.gov.ar.entities.TipoDocumento;
import FEV1.dif.afip.gov.ar.entities.TipoIVA;
import FEV1.dif.afip.gov.ar.entities.TipoMoneda;
import FEV1.dif.afip.gov.ar.exceptions.ServiceException;
import FEV1.dif.afip.gov.ar.services.FacturacionService;
import FEV1.dif.afip.gov.ar.utils.AbstractFactory;
import ar.com.wuik.sistema.bo.NotaDebitoBO;
import ar.com.wuik.sistema.dao.ClienteDAO;
import ar.com.wuik.sistema.dao.NotaDebitoDAO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.DetalleNotaCredito;
import ar.com.wuik.sistema.entities.DetalleNotaDebito;
import ar.com.wuik.sistema.entities.Factura;
import ar.com.wuik.sistema.entities.NotaDebito;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.NotaDebitoFilter;
import ar.com.wuik.sistema.reportes.entities.DetalleNotaDebitoDTO;
import ar.com.wuik.sistema.reportes.entities.NotaDebitoDTO;
import ar.com.wuik.sistema.utils.HibernateUtil;
import ar.com.wuik.swing.utils.WUtils;

public class NotaDebitoBOImpl implements NotaDebitoBO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(NotaDebitoBOImpl.class);
	private FacturacionService facturacionService;
	private NotaDebitoDAO notaDebitoDAO;
	private ClienteDAO clienteDAO;

	public NotaDebitoBOImpl() {
		facturacionService = AbstractFactory
				.getInstance(FacturacionService.class);
		notaDebitoDAO = ar.com.wuik.sistema.utils.AbstractFactory
				.getInstance(NotaDebitoDAO.class);
		clienteDAO = ar.com.wuik.sistema.utils.AbstractFactory
				.getInstance(ClienteDAO.class);
	}

	@Override
	public NotaDebito obtener(Long id) throws BusinessException {
		try {
			return notaDebitoDAO.getById(id);
		} catch (DataAccessException daexc) {
			LOGGER.error("obtener() - Error al obtener Nota de Débito", daexc);
			throw new BusinessException(daexc,
					"Error al obtener Nota de Débito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<NotaDebito> buscar(NotaDebitoFilter filter)
			throws BusinessException {
		try {
			return notaDebitoDAO.search(filter);
		} catch (DataAccessException daexc) {
			LOGGER.error("buscar() - Error al buscar Notas de Débito", daexc);
			throw new BusinessException(daexc,
					"Error al buscar Notas de Débito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void guardar(NotaDebito notaDebito) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			notaDebitoDAO.saveOrUpdate(notaDebito);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("guardar() - Error al guardar Nota de Débito", daexc);
			throw new BusinessException(daexc,
					"Error al guardar Nota de Débito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void guardarRegistrarAFIP(NotaDebito notaDebito)
			throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			notaDebitoDAO.saveOrUpdate(notaDebito);
			Comprobante comprobante = crearComprobante(notaDebito);
			Resultado resultado = facturacionService
					.solicitarComprobante(comprobante);

			notaDebito.setCae(resultado.getCae());
			notaDebito.setFechaCAE(resultado.getFechaVtoCAE());
			notaDebito.setNroComprobante(resultado.getNroComprobante());
			notaDebito.setPtoVenta(resultado.getPtoVta());

			notaDebitoDAO.saveOrUpdate(notaDebito);
			HibernateUtil.commitTransaction();
		} catch (ServiceException sexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error(
					"guardarRegistrarAFIP() - Problemas por Servicio - Error al registrar Nota de Débito",
					sexc);
			throw new BusinessException(sexc, sexc.getMessage());
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error(
					"guardarRegistrarAFIP() - Error al registrar Nota de Débito",
					daexc);
			throw new BusinessException(daexc,
					"Error al registrar Nota de Débito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	private Comprobante crearComprobante(NotaDebito notaDebito)
			throws DataAccessException {

		Cliente cliente = clienteDAO.getById(notaDebito.getIdCliente());

		String cuit = cliente.getCuit().replaceAll("-", "");
		Date fechaComprobante = notaDebito.getFechaVenta();
		BigDecimal subtotal = notaDebito.getSubtotal();
		BigDecimal iva = notaDebito.getIva();
		BigDecimal total = notaDebito.getTotal();

		Comprobante comprobante = new Comprobante();

		// DATOS DEL CLIENTE.
		comprobante.setDocNro(Long.valueOf(cuit));
		comprobante.setDocTipo(TipoDocumento.CUIT);

		// COTIZACION LA TOMA DEL TIPO DE MONEDA PORQUE ES EN PESOS.
		comprobante.setTipoMoneda(TipoMoneda.PESOS_ARGENTINOS);
		comprobante.setCotizacion(null);

		// COMPROBANTES ASOCIADOS.
		Set<Factura> facturas = notaDebito.getFacturas();
		if (WUtils.isNotEmpty(facturas)) {
			List<ComprobanteAsociado> comprobantesAsociados = new ArrayList<ComprobanteAsociado>();
			ComprobanteAsociado comprobanteAsociado = null;
			for (Factura factura : facturas) {
				comprobanteAsociado = new ComprobanteAsociado();
				comprobanteAsociado.setNumero(factura.getNroComprobante());
				comprobanteAsociado.setPtoVta(factura.getPtoVenta().intValue());
				comprobanteAsociado
						.setTipoComprobante(TipoComprobante.FACTURA_A);
				comprobantesAsociados.add(comprobanteAsociado);
			}
			comprobante.setComprobantesAsociados(comprobantesAsociados);
		}

		// TOTALES.
		comprobante.setImporteIVA(iva);
		comprobante.setImporteSubtotal(subtotal);
		comprobante.setImporteTotal(total);

		// DATOS GENERALES DEL COMPROBANTE.
		comprobante.setFechaComprobante(fechaComprobante);
		comprobante.setTipoComprobante(TipoComprobante.NOTA_DEBITO_A);
		comprobante.setTipoConcepto(TipoConcepto.PRODUCTO);

		// DETALLES DE LA FACTURA.
		List<AlicuotaIVA> alicuotas = new ArrayList<AlicuotaIVA>();

		BigDecimal subtotal21 = BigDecimal.ZERO;
		BigDecimal totalIVA21 = BigDecimal.ZERO;
		BigDecimal subtotal105 = BigDecimal.ZERO;
		BigDecimal totalIVA105 = BigDecimal.ZERO;
		List<DetalleNotaDebito> detalles = notaDebito.getDetalles();
		for (DetalleNotaDebito detalleNotaDebito : detalles) {
			if (detalleNotaDebito.getIva().doubleValue() == 21.00) {
				subtotal21 = subtotal21.add(detalleNotaDebito.getSubtotal());
				totalIVA21 = totalIVA21.add(detalleNotaDebito.getTotalIVA());
			} else if (detalleNotaDebito.getIva().doubleValue() == 10.50) {
				subtotal105 = subtotal105.add(detalleNotaDebito.getSubtotal());
				totalIVA105 = totalIVA105.add(detalleNotaDebito.getTotalIVA());
			}
		}

		// IVA DEL 21%
		if (subtotal21.doubleValue() > 0) {
			AlicuotaIVA alicuota21 = new AlicuotaIVA();
			alicuota21.setBaseImponible(subtotal21);
			alicuota21.setTipoIVA(TipoIVA.IVA_21);
			alicuota21.setTotalAlicuota(totalIVA21);
			alicuotas.add(alicuota21);
		}

		// IVA DEL 10.5%
		if (subtotal105.doubleValue() > 0) {
			AlicuotaIVA alicuota105 = new AlicuotaIVA();
			alicuota105.setBaseImponible(subtotal105);
			alicuota105.setTipoIVA(TipoIVA.IVA_10_5);
			alicuota105.setTotalAlicuota(totalIVA105);
			alicuotas.add(alicuota105);
		}

		comprobante.setAlicuotas(alicuotas);

		return comprobante;
	}

	@Override
	public void actualizar(NotaDebito notaDebito) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			notaDebitoDAO.saveOrUpdate(notaDebito);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("actualizar() - Error al actualizar Nota de Débito",
					daexc);
			throw new BusinessException(daexc,
					"Error al actualizar Nota de Débito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public NotaDebitoDTO obtenerDTO(Long id) throws BusinessException {
		try {
			NotaDebito notaDebito = notaDebitoDAO.getById(id);
			return convertToDTO(notaDebito);
		} catch (DataAccessException daexc) {
			LOGGER.error("obtenerDTO() - Error al obtener Nota de Débito",
					daexc);
			throw new BusinessException(daexc,
					"Error al obtener Nota de Débito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	private NotaDebitoDTO convertToDTO(NotaDebito notaDebito) {
		NotaDebitoDTO notaDebitoDTO = new NotaDebitoDTO();

		// DATOS DEL CLIENTE.
		Cliente cliente = notaDebito.getCliente();
		notaDebitoDTO.setClienteCondIVA(cliente.getCondicionIVA()
				.getDenominacion());
		notaDebitoDTO.setClienteCuit(cliente.getCuit());
		notaDebitoDTO.setClienteDomicilio(cliente.getDireccion());
		notaDebitoDTO.setClienteRazonSocial(cliente.getRazonSocial());

		// DATOS PROPIOS. TODO: PONER EN PARAMETRICOS.
		notaDebitoDTO.setRazonSocial("VAN DER BEKEN FRANCISCO NICOLAS");
		notaDebitoDTO.setCondIVA("IVA Responsable Inscripto");
		notaDebitoDTO.setCuit("20-04974118-1");
		notaDebitoDTO.setDomicilio("Passo 50 - Rojas, Buenos Aires");
		notaDebitoDTO.setIngBrutos("200049746181");
		notaDebitoDTO.setInicioAct(WUtils.getDateFromString("01/01/1994"));

		// DATOS DE LA NOTA DE DEBITO.
		BigDecimal subtotal = BigDecimal.ZERO;
		BigDecimal subtotalIVA21 = BigDecimal.ZERO;
		BigDecimal subtotalIVA105 = BigDecimal.ZERO;
		BigDecimal total = BigDecimal.ZERO;

		List<DetalleNotaDebito> detalles = notaDebito.getDetalles();
		List<DetalleNotaDebitoDTO> detallesDTO = new ArrayList<DetalleNotaDebitoDTO>();
		DetalleNotaDebitoDTO detalleNotaDebitoDTO = null;
		for (DetalleNotaDebito detalleNotaDebito : detalles) {
			detalleNotaDebitoDTO = new DetalleNotaDebitoDTO();
			detalleNotaDebitoDTO.setAlicuota(detalleNotaDebito.getIva());
			detalleNotaDebitoDTO.setCantidad(detalleNotaDebito.getCantidad());
			detalleNotaDebitoDTO.setCodigo((null != detalleNotaDebito
					.getProducto()) ? detalleNotaDebito.getProducto()
					.getCodigo() : "0");
			detalleNotaDebitoDTO.setPrecioUnit(detalleNotaDebito.getPrecio());
			detalleNotaDebitoDTO.setProducto((null != detalleNotaDebito
					.getProducto()) ? detalleNotaDebito.getProducto()
					.getDescripcion() : detalleNotaDebito.getDetalle());
			detalleNotaDebitoDTO.setSubtotal(detalleNotaDebito.getSubtotal());
			detalleNotaDebitoDTO.setSubtotalConIVA(detalleNotaDebito
					.getTotal());
			detalleNotaDebitoDTO.setComentario(detalleNotaDebito
					.getComentario());
			detallesDTO.add(detalleNotaDebitoDTO);

			if (detalleNotaDebito.getIva().doubleValue() == 21.00) {
				subtotalIVA21 = subtotalIVA21.add(detalleNotaDebito
						.getTotalIVA());
			} else if (detalleNotaDebito.getIva().doubleValue() == 10.50) {
				subtotalIVA105 = subtotalIVA105.add(detalleNotaDebito
						.getTotalIVA());
			}
			subtotal = subtotal.add(detalleNotaDebito.getSubtotal());
			total = total.add(detalleNotaDebito.getTotal());
		}
		notaDebitoDTO.setDetalles(detallesDTO);
		notaDebitoDTO.setCae(notaDebito.getCae());
		notaDebitoDTO.setVtoCAE(notaDebito.getFechaCAE());
		notaDebitoDTO.setCompNro(notaDebito.getNroComprobante().toString());
		notaDebitoDTO.setFechaEmision(notaDebito.getFechaVenta());
		notaDebitoDTO.setIva105(subtotalIVA105);
		notaDebitoDTO.setIva21(subtotalIVA21);
		notaDebitoDTO.setLetra("A");
		notaDebitoDTO.setPtoVta(notaDebito.getPtoVenta().toString());
		notaDebitoDTO.setSubtotal(subtotal);
		notaDebitoDTO.setTipo("NOTA DE DEBITO");
		notaDebitoDTO.setTotal(total);
		return notaDebitoDTO;
	}
}
