package ar.com.wuik.sistema.bo.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import FEV1.dif.afip.gov.ar.entities.AlicuotaIVA;
import FEV1.dif.afip.gov.ar.entities.Comprobante;
import FEV1.dif.afip.gov.ar.entities.Resultado;
import FEV1.dif.afip.gov.ar.entities.TipoComprobante;
import FEV1.dif.afip.gov.ar.entities.TipoConcepto;
import FEV1.dif.afip.gov.ar.entities.TipoDocumento;
import FEV1.dif.afip.gov.ar.entities.TipoIVA;
import FEV1.dif.afip.gov.ar.entities.TipoMoneda;
import FEV1.dif.afip.gov.ar.exceptions.ServiceException;
import FEV1.dif.afip.gov.ar.services.FacturacionService;
import FEV1.dif.afip.gov.ar.utils.AbstractFactory;
import ar.com.wuik.sistema.bo.FacturaBO;
import ar.com.wuik.sistema.bo.ParametroBO;
import ar.com.wuik.sistema.dao.ClienteDAO;
import ar.com.wuik.sistema.dao.FacturaDAO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.DetalleFactura;
import ar.com.wuik.sistema.entities.Factura;
import ar.com.wuik.sistema.entities.Parametro;
import ar.com.wuik.sistema.entities.Remito;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.DataAccessException;
import ar.com.wuik.sistema.filters.FacturaFilter;
import ar.com.wuik.sistema.reportes.entities.DetalleFacturaDTO;
import ar.com.wuik.sistema.reportes.entities.FacturaDTO;
import ar.com.wuik.sistema.utils.HibernateUtil;
import ar.com.wuik.swing.utils.WUtils;

public class FacturaBOImpl implements FacturaBO {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FacturaBOImpl.class);
	private FacturacionService facturacionService;
	private FacturaDAO facturaDAO;
	private ClienteDAO clienteDAO;
	private ParametroBO parametroBO;

	public FacturaBOImpl() {
		facturacionService = AbstractFactory
				.getInstance(FacturacionService.class);
		facturaDAO = ar.com.wuik.sistema.utils.AbstractFactory
				.getInstance(FacturaDAO.class);
		clienteDAO = ar.com.wuik.sistema.utils.AbstractFactory
				.getInstance(ClienteDAO.class);
		parametroBO = ar.com.wuik.sistema.utils.AbstractFactory
				.getInstance(ParametroBO.class);
	}

	@Override
	public Factura obtener(Long id) throws BusinessException {
		try {
			return facturaDAO.getById(id);
		} catch (DataAccessException daexc) {
			LOGGER.error("obtener() - Error al obtener Factura", daexc);
			throw new BusinessException(daexc, "Error al obtener Factura");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public List<Factura> buscar(FacturaFilter filter) throws BusinessException {
		try {
			return facturaDAO.search(filter);
		} catch (DataAccessException daexc) {
			LOGGER.error("buscar() - Error al buscar Facturas", daexc);
			throw new BusinessException(daexc, "Error al buscar Facturas");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void guardar(Factura factura) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			facturaDAO.saveOrUpdate(factura);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("guardar() - Error al guardar Factura", daexc);
			throw new BusinessException(daexc, "Error al guardar Factura");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void guardarRegistrarAFIP(Factura factura) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			facturaDAO.saveOrUpdate(factura);
			Comprobante comprobante = crearComprobante(factura);
			Resultado resultado = facturacionService
					.solicitarComprobante(comprobante);

			factura.setCae(resultado.getCae());
			factura.setFechaCAE(resultado.getFechaVtoCAE());
			factura.setNroComprobante(resultado.getNroComprobante());
			factura.setPtoVenta(resultado.getPtoVta());

			facturaDAO.saveOrUpdate(factura);
			HibernateUtil.commitTransaction();
		} catch (ServiceException sexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error(
					"guardarRegistrarAFIP() - Problemas por Servicio - Error al registrar Factura",
					sexc);
			throw new BusinessException(sexc, sexc.getMessage());
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("guardarRegistrarAFIP() - Error al registrar Factura",
					daexc);
			throw new BusinessException(daexc, "Error al registrar Factura");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	private Comprobante crearComprobante(Factura factura)
			throws DataAccessException {

		Cliente cliente = clienteDAO.getById(factura.getIdCliente());

		String cuit = cliente.getCuit().replaceAll("-", "");
		Date fechaComprobante = factura.getFechaVenta();
		BigDecimal subtotal = factura.getSubtotal();
		BigDecimal iva = factura.getIva();
		BigDecimal total = factura.getTotal();

		Comprobante comprobante = new Comprobante();

		// DATOS DEL CLIENTE.
		comprobante.setDocNro(Long.valueOf(cuit));
		comprobante.setDocTipo(TipoDocumento.CUIT);

		// COTIZACION LA TOMA DEL TIPO DE MONEDA PORQUE ES EN PESOS.
		comprobante.setTipoMoneda(TipoMoneda.PESOS_ARGENTINOS);
		comprobante.setCotizacion(null);

		// TOTALES.
		comprobante.setImporteIVA(iva);
		comprobante.setImporteSubtotal(subtotal);
		comprobante.setImporteTotal(total);

		// DATOS GENERALES DEL COMPROBANTE.
		comprobante.setFechaComprobante(fechaComprobante);
		comprobante.setTipoComprobante(TipoComprobante.FACTURA_A);
		comprobante.setTipoConcepto(TipoConcepto.PRODUCTO);
		comprobante.setComprobantesAsociados(null);

		// DETALLES DE LA FACTURA.
		List<AlicuotaIVA> alicuotas = new ArrayList<AlicuotaIVA>();

		BigDecimal subtotal21 = BigDecimal.ZERO;
		BigDecimal totalIVA21 = BigDecimal.ZERO;
		BigDecimal subtotal105 = BigDecimal.ZERO;
		BigDecimal totalIVA105 = BigDecimal.ZERO;
		List<DetalleFactura> detalles = factura.getDetalles();
		for (DetalleFactura detalleFactura : detalles) {
			if (detalleFactura.getIva().doubleValue() == 21.00) {
				subtotal21 = subtotal21.add(detalleFactura.getSubtotal());
				totalIVA21 = totalIVA21.add(detalleFactura.getTotalIVA());
			} else if (detalleFactura.getIva().doubleValue() == 10.50) {
				subtotal105 = subtotal105.add(detalleFactura.getSubtotal());
				totalIVA105 = totalIVA105.add(detalleFactura.getTotalIVA());
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
	public void actualizar(Factura factura) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			facturaDAO.saveOrUpdate(factura);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("actualizar() - Error al actualizar Factura", daexc);
			throw new BusinessException(daexc, "Error al actualizar Factura");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public void cancelar(Long id) throws BusinessException {
		try {
			HibernateUtil.startTransaction();
			Factura factura = facturaDAO.getById(id);
			factura.setActivo(Boolean.FALSE);
			facturaDAO.saveOrUpdate(factura);
			HibernateUtil.commitTransaction();
		} catch (DataAccessException daexc) {
			HibernateUtil.rollbackTransaction();
			LOGGER.error("cancelar() - Error al cancelar Factura", daexc);
			throw new BusinessException(daexc, "Error al cancelar Factura");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	@Override
	public FacturaDTO obtenerDTO(Long id) throws BusinessException {
		try {
			Factura factura = facturaDAO.getById(id);
			return convertToDTO(factura);
		} catch (DataAccessException daexc) {
			LOGGER.error("obtenerDTO() - Error al obtener Factura", daexc);
			throw new BusinessException(daexc, "Error al obtener Factura");
		} finally {
			HibernateUtil.closeSession();
		}
	}

	private FacturaDTO convertToDTO(Factura factura) throws BusinessException {
		FacturaDTO facturaDTO = new FacturaDTO();

		// DATOS DEL CLIENTE.
		Cliente cliente = factura.getCliente();
		facturaDTO.setClienteCondIVA(cliente.getCondicionIVA()
				.getDenominacion());
		facturaDTO.setClienteCuit(cliente.getCuit());
		facturaDTO.setClienteDomicilio(cliente.getDireccion());
		facturaDTO.setClienteRazonSocial(cliente.getRazonSocial());

		// DATOS PROPIOS. 
		Parametro parametro = parametroBO.getParametro();
		facturaDTO.setRazonSocial(parametro.getRazonSocial());
		facturaDTO.setCondIVA(parametro.getCondIVA());
		facturaDTO.setCuit(parametro.getCuit());
		facturaDTO.setDomicilio(parametro.getDomicilio());
		facturaDTO.setIngBrutos(parametro.getIngresosBrutos());
		facturaDTO.setInicioAct(parametro.getInicioActividad());

		// DATOS REMITOS.
		List<Remito> remitos = factura.getRemitos();
		if (WUtils.isNotEmpty(remitos)) {
			String detalleRemitos = "";
			for (Remito remito : remitos) {
				detalleRemitos += remito.getNumero() + " - ";
			}
			facturaDTO.setRemitos(detalleRemitos.substring(0,
					detalleRemitos.length() - 3));
		}

		// DATOS DE LA FACTURA.
		BigDecimal subtotal = BigDecimal.ZERO;
		BigDecimal subtotalIVA21 = BigDecimal.ZERO;
		BigDecimal subtotalIVA105 = BigDecimal.ZERO;
		BigDecimal total = BigDecimal.ZERO;

		List<DetalleFactura> detalles = factura.getDetalles();
		List<DetalleFacturaDTO> detallesDTO = new ArrayList<DetalleFacturaDTO>();
		DetalleFacturaDTO detalleFacturaDTO = null;
		for (DetalleFactura detalleFactura : detalles) {
			detalleFacturaDTO = new DetalleFacturaDTO();
			detalleFacturaDTO.setAlicuota(detalleFactura.getIva());
			detalleFacturaDTO.setCantidad(detalleFactura.getCantidad());
			detalleFacturaDTO
					.setCodigo((null != detalleFactura.getProducto()) ? detalleFactura
							.getProducto().getCodigo() : "0");
			detalleFacturaDTO.setPrecioUnit(detalleFactura.getPrecio());
			detalleFacturaDTO
					.setProducto((null != detalleFactura.getProducto()) ? detalleFactura
							.getProducto().getDescripcion() : detalleFactura.getDetalle());
			detalleFacturaDTO.setSubtotal(detalleFactura.getSubtotal());
			detalleFacturaDTO.setSubtotalConIVA(detalleFactura.getTotal());
			detallesDTO.add(detalleFacturaDTO);

			if (detalleFactura.getIva().doubleValue() == 21.00) {
				subtotalIVA21 = subtotalIVA21.add(detalleFactura.getTotalIVA());
			} else if (detalleFactura.getIva().doubleValue() == 10.50) {
				subtotalIVA105 = subtotalIVA105.add(detalleFactura
						.getTotalIVA());
			}
			subtotal = subtotal.add(detalleFactura.getSubtotal());
			total = total.add(detalleFactura.getTotal());
		}
		facturaDTO.setDetalles(detallesDTO);
		facturaDTO.setCae(factura.getCae());
		facturaDTO.setVtoCAE(factura.getFechaCAE());
		facturaDTO.setCompNro(factura.getNroComprobante().toString());
		facturaDTO.setFechaEmision(factura.getFechaVenta());
		facturaDTO.setIva105(subtotalIVA105);
		facturaDTO.setIva21(subtotalIVA21);
		facturaDTO.setLetra("A");
		facturaDTO.setPtoVta(factura.getPtoVenta().toString());
		facturaDTO.setSubtotal(subtotal);
		facturaDTO.setTipo("FACTURA");
		facturaDTO.setTotal(total);
		return facturaDTO;
	}
}
