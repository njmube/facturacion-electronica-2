package ar.com.wuik.sistema.bo.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import ar.com.wuik.sistema.bo.NotaCreditoBO;
import ar.com.wuik.sistema.dao.ClienteDAO;
import ar.com.wuik.sistema.dao.NotaCreditoDAO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.DetalleNotaCredito;
import ar.com.wuik.sistema.entities.Factura;
import ar.com.wuik.sistema.entities.NotaCredito;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.NotaCreditoFilter;
import ar.com.wuik.sistema.reportes.entities.DetalleNotaCreditoDTO;
import ar.com.wuik.sistema.reportes.entities.NotaCreditoDTO;
import ar.com.wuik.sistema.utils.HibernateUtil;
import ar.com.wuik.swing.utils.WUtils;

public class NotaCreditoBOImpl implements NotaCreditoBO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(NotaCreditoBOImpl.class);
	private FacturacionService facturacionService;
	private NotaCreditoDAO notaCreditoDAO;
	private ClienteDAO clienteDAO;

	public NotaCreditoBOImpl() {
		facturacionService = AbstractFactory
				.getInstance(FacturacionService.class);
		notaCreditoDAO = ar.com.wuik.sistema.utils.AbstractFactory
				.getInstance(NotaCreditoDAO.class);
		clienteDAO = ar.com.wuik.sistema.utils.AbstractFactory
				.getInstance(ClienteDAO.class);
	}

	@Override
	public NotaCredito obtener(Long id) throws BusinessException {
		try {
			return notaCreditoDAO.getById(id);
		} catch (DataAccessException daexc) {
			LOGGER.error("obtener() - Error al obtener Nota de Cr�dito", daexc);
			throw new BusinessException(daexc,
					"Error al obtener Nota de Cr�dito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<NotaCredito> buscar(NotaCreditoFilter filter)
			throws BusinessException {
		try {
			return notaCreditoDAO.search(filter);
		} catch (DataAccessException daexc) {
			LOGGER.error("buscar() - Error al buscar Notas de Cr�dito", daexc);
			throw new BusinessException(daexc,
					"Error al buscar Notas de Cr�dito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void guardar(NotaCredito notaCredito) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			notaCreditoDAO.saveOrUpdate(notaCredito);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("guardar() - Error al guardar Nota de Cr�dito", daexc);
			throw new BusinessException(daexc,
					"Error al guardar Nota de Cr�dito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void guardarRegistrarAFIP(NotaCredito notaCredito)
			throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			notaCreditoDAO.saveOrUpdate(notaCredito);
			Comprobante comprobante = crearComprobante(notaCredito);
			Resultado resultado = facturacionService
					.solicitarComprobante(comprobante);

			notaCredito.setCae(resultado.getCae());
			notaCredito.setFechaCAE(resultado.getFechaVtoCAE());
			notaCredito.setNroComprobante(resultado.getNroComprobante());
			notaCredito.setPtoVenta(resultado.getPtoVta());

			notaCreditoDAO.saveOrUpdate(notaCredito);
			HibernateUtil.commitTransaction();
		} catch (ServiceException sexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error(
					"guardarRegistrarAFIP() - Problemas por Servicio - Error al registrar Nota de Cr�dito",
					sexc);
			throw new BusinessException(sexc, sexc.getMessage());
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error(
					"guardarRegistrarAFIP() - Error al registrar Nota de Cr�dito",
					daexc);
			throw new BusinessException(daexc,
					"Error al registrar Nota de Cr�dito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	private Comprobante crearComprobante(NotaCredito notaCredito)
			throws DataAccessException {

		Cliente cliente = clienteDAO.getById(notaCredito.getIdCliente());

		String cuit = cliente.getCuit().replaceAll("-", "");
		Date fechaComprobante = notaCredito.getFechaVenta();
		BigDecimal subtotal = notaCredito.getSubtotal();
		BigDecimal iva = notaCredito.getIva();
		BigDecimal total = notaCredito.getTotal();

		Comprobante comprobante = new Comprobante();

		// DATOS DEL CLIENTE.
		comprobante.setDocNro(Long.valueOf(cuit));
		comprobante.setDocTipo(TipoDocumento.CUIT);

		// COTIZACION LA TOMA DEL TIPO DE MONEDA PORQUE ES EN PESOS.
		comprobante.setTipoMoneda(TipoMoneda.PESOS_ARGENTINOS);
		comprobante.setCotizacion(null);

		// COMPROBANTES ASOCIADOS.
		List<Factura> facturas = notaCredito.getFacturas();
		if (WUtils.isNotEmpty(facturas)) {
			List<ComprobanteAsociado> comprobantesAsociados = new ArrayList<ComprobanteAsociado>();
			ComprobanteAsociado comprobanteAsociado = null;
			for (Factura factura : facturas) {
				comprobanteAsociado = new ComprobanteAsociado();
				comprobanteAsociado.setNumero(factura.getNroComprobante());
				comprobanteAsociado.setPtoVta(factura.getPtoVenta().intValue());
				comprobanteAsociado.setTipoComprobante(TipoComprobante.FACTURA_A);
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
		comprobante.setTipoComprobante(TipoComprobante.NOTA_CREDITO_A);
		comprobante.setTipoConcepto(TipoConcepto.PRODUCTO);

		// DETALLES DE LA FACTURA.
		List<AlicuotaIVA> alicuotas = new ArrayList<AlicuotaIVA>();

		BigDecimal subtotal21 = BigDecimal.ZERO;
		BigDecimal totalIVA21 = BigDecimal.ZERO;
		BigDecimal subtotal105 = BigDecimal.ZERO;
		BigDecimal totalIVA105 = BigDecimal.ZERO;
		List<DetalleNotaCredito> detalles = notaCredito.getDetalles();
		for (DetalleNotaCredito detalleNotaCredito : detalles) {
			if (detalleNotaCredito.getIva().doubleValue() == 21.00) {
				subtotal21 = subtotal21.add(detalleNotaCredito.getSubtotal());
				totalIVA21 = totalIVA21.add(detalleNotaCredito.getTotalIVA());
			} else if (detalleNotaCredito.getIva().doubleValue() == 10.50) {
				subtotal105 = subtotal105.add(detalleNotaCredito.getSubtotal());
				totalIVA105 = totalIVA105.add(detalleNotaCredito.getTotalIVA());
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
	public void actualizar(NotaCredito notaCredito) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			notaCreditoDAO.saveOrUpdate(notaCredito);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("actualizar() - Error al actualizar Nota de Cr�dito",
					daexc);
			throw new BusinessException(daexc,
					"Error al actualizar Nota de Cr�dito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public NotaCreditoDTO obtenerDTO(Long id) throws BusinessException {
		try {
			NotaCredito notaCredito = notaCreditoDAO.getById(id);
			return convertToDTO(notaCredito);
		} catch (DataAccessException daexc) {
			LOGGER.error("obtenerDTO() - Error al obtener Nota de Cr�dito",
					daexc);
			throw new BusinessException(daexc,
					"Error al obtener Nota de Cr�dito");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	private NotaCreditoDTO convertToDTO(NotaCredito notaCredito) {
		NotaCreditoDTO notaCreditoDTO = new NotaCreditoDTO();

		// DATOS DEL CLIENTE.
		Cliente cliente = notaCredito.getCliente();
		notaCreditoDTO.setClienteCondIVA(cliente.getCondicionIVA()
				.getDenominacion());
		notaCreditoDTO.setClienteCuit(cliente.getCuit());
		notaCreditoDTO.setClienteDomicilio(cliente.getDireccion());
		notaCreditoDTO.setClienteRazonSocial(cliente.getRazonSocial());

		// DATOS PROPIOS. TODO: PONER EN PARAMETRICOS.
		notaCreditoDTO.setRazonSocial("VAN DER BEKEN FRANCISCO NICOLAS");
		notaCreditoDTO.setCondIVA("IVA Responsable Inscripto");
		notaCreditoDTO.setCuit("20-04974118-1");
		notaCreditoDTO.setDomicilio("Passo 50 - Rojas, Buenos Aires");
		notaCreditoDTO.setIngBrutos("200049746181");
		notaCreditoDTO.setInicioAct(WUtils.getDateFromString("01/01/1994"));

		// DATOS DE LA NOTA DE CREDITO.
		BigDecimal subtotal = BigDecimal.ZERO;
		BigDecimal subtotalIVA21 = BigDecimal.ZERO;
		BigDecimal subtotalIVA105 = BigDecimal.ZERO;
		BigDecimal total = BigDecimal.ZERO;

		List<DetalleNotaCredito> detalles = notaCredito.getDetalles();
		List<DetalleNotaCreditoDTO> detallesDTO = new ArrayList<DetalleNotaCreditoDTO>();
		DetalleNotaCreditoDTO detalleNotaCreditoDTO = null;
		for (DetalleNotaCredito detalleNotaCredito : detalles) {
			detalleNotaCreditoDTO = new DetalleNotaCreditoDTO();
			detalleNotaCreditoDTO.setAlicuota(detalleNotaCredito.getIva());
			detalleNotaCreditoDTO.setCantidad(detalleNotaCredito.getCantidad());
			detalleNotaCreditoDTO.setCodigo(detalleNotaCredito.getProducto()
					.getCodigo());
			detalleNotaCreditoDTO.setPrecioUnit(detalleNotaCredito.getPrecio());
			detalleNotaCreditoDTO.setProducto(detalleNotaCredito.getProducto()
					.getDescripcion());
			detalleNotaCreditoDTO.setSubtotal(detalleNotaCredito.getSubtotal());
			detalleNotaCreditoDTO.setSubtotalConIVA(detalleNotaCredito
					.getTotal());
			detalleNotaCreditoDTO.setComentario(detalleNotaCredito.getComentario());
			detallesDTO.add(detalleNotaCreditoDTO);

			if (detalleNotaCredito.getIva().doubleValue() == 21.00) {
				subtotalIVA21 = subtotalIVA21.add(detalleNotaCredito
						.getTotalIVA());
			} else if (detalleNotaCredito.getIva().doubleValue() == 10.50) {
				subtotalIVA105 = subtotalIVA105.add(detalleNotaCredito
						.getTotalIVA());
			}
			subtotal = subtotal.add(detalleNotaCredito.getSubtotal());
			total = total.add(detalleNotaCredito.getTotal());
		}
		notaCreditoDTO.setDetalles(detallesDTO);
		notaCreditoDTO.setCae(notaCredito.getCae());
		notaCreditoDTO.setVtoCAE(notaCredito.getFechaCAE());
		notaCreditoDTO.setCompNro(notaCredito.getNroComprobante().toString());
		notaCreditoDTO.setFechaEmision(notaCredito.getFechaVenta());
		notaCreditoDTO.setIva105(subtotalIVA105);
		notaCreditoDTO.setIva21(subtotalIVA21);
		notaCreditoDTO.setLetra("A");
		notaCreditoDTO.setPtoVta(notaCredito.getPtoVenta().toString());
		notaCreditoDTO.setSubtotal(subtotal);
		notaCreditoDTO.setTipo("NOTA DE CREDITO");
		notaCreditoDTO.setTotal(total);
		return notaCreditoDTO;
	}
}
