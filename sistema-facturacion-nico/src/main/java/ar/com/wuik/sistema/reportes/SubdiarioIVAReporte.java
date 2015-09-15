package ar.com.wuik.sistema.reportes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import ar.com.wuik.sistema.bo.ComprobanteBO;
import ar.com.wuik.sistema.entities.Comprobante;
import ar.com.wuik.sistema.entities.DetalleComprobante;
import ar.com.wuik.sistema.entities.TributoComprobante;
import ar.com.wuik.sistema.entities.enums.CondicionIVA;
import ar.com.wuik.sistema.entities.enums.EstadoFacturacion;
import ar.com.wuik.sistema.entities.enums.TipoComprobante;
import ar.com.wuik.sistema.entities.enums.TipoTributo;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.ReportException;
import ar.com.wuik.sistema.filters.ComprobanteFilter;
import ar.com.wuik.sistema.reportes.entities.ComprobanteCompraDTO;
import ar.com.wuik.sistema.reportes.entities.ComprobanteVentaDTO;
import ar.com.wuik.sistema.reportes.entities.DetalleTipoProductoDTO;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.utils.WJasperUtils;
import ar.com.wuik.swing.utils.WUtils;

public class SubdiarioIVAReporte {

	public static void generarReporte(Date desde, Date hasta)
			throws ReportException {

		try {
			ComprobanteBO comprobanteBO = AbstractFactory
					.getInstance(ComprobanteBO.class);

			ComprobanteFilter filter = new ComprobanteFilter();
			filter.setDesde(desde);
			filter.setHasta(hasta);
			filter.setInicializarTributos(Boolean.TRUE);
			filter.setInicializarDetalles(Boolean.TRUE);
			filter.setEstadoFacturacion(EstadoFacturacion.FACTURADO);
			List<Comprobante> comprobantes = comprobanteBO.buscar(filter);

			JasperPrint jasperPrintCompra = generarReporteCompra(comprobantes,desde, hasta);

			JasperPrint jasperPrintVenta = generarReporteVenta(comprobantes, desde, hasta);

			JasperPrint jasperPrint = WJasperUtils.concatReports(
					jasperPrintCompra, jasperPrintVenta);

			// JasperPrintManager.printReport(jasperPrint, Boolean.TRUE);
			JasperViewer.viewReport(jasperPrint, Boolean.FALSE);
		} catch (BusinessException bexc) {
			throw new ReportException(bexc, "Error al obtener Factura");
		} catch (JRException jrexc) {
			throw new ReportException(jrexc, "Error al generar Factura");
		}
	}

	private static JasperPrint generarReporteCompra(
			List<Comprobante> comprobantes, Date desde, Date hasta) throws JRException {
		List<ComprobanteCompraDTO> comprobantesCompra = convertirComprobantesCompra(comprobantes);

		BigDecimal neto = BigDecimal.ZERO;
		BigDecimal iva = BigDecimal.ZERO;
		BigDecimal perc = BigDecimal.ZERO;
		BigDecimal otros = BigDecimal.ZERO;

		for (ComprobanteCompraDTO comprobanteCompraDTO : comprobantesCompra) {
			neto = neto.add(comprobanteCompraDTO.getNeto());
			iva = iva.add(comprobanteCompraDTO.getIva());
			perc = perc.add(comprobanteCompraDTO.getPercepcion());
			otros = otros.add(comprobanteCompraDTO.getOtros());
		}

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("DESDE", desde);
		parameters.put("HASTA", hasta);
		parameters.put("NETO_GRAVADO", neto);
		parameters.put("IVA", iva);
		parameters.put("PERCEPCION", perc);
		parameters.put("OTROS", otros);
		parameters.put("TOTAL", neto.add(iva).add(perc).add(otros));

		JasperReport jasperReport = (JasperReport) JRLoader
				.loadObject(SubdiarioIVAReporte.class
						.getResourceAsStream("/reportes/subdiario_iva_compra.jasper"));

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
				parameters, new JRBeanCollectionDataSource(comprobantesCompra));
		return jasperPrint;
	}

	private static JasperPrint generarReporteVenta(
			List<Comprobante> comprobantes, Date desde, Date hasta) throws JRException {

		Map<String, Object> parameters = new HashMap<String, Object>();

		List<ComprobanteVentaDTO> comprobantesVenta = convertirComprobantesVenta(
				comprobantes, parameters);

		BigDecimal neto = BigDecimal.ZERO;
		BigDecimal iva = BigDecimal.ZERO;

		for (ComprobanteVentaDTO comprobanteVentaDTO : comprobantesVenta) {
			neto = neto.add(comprobanteVentaDTO.getNeto());
			iva = iva.add(comprobanteVentaDTO.getIva());
		}

		parameters.put("DESDE", desde);
		parameters.put("HASTA", hasta);
		parameters.put("NETO_GRAVADO", neto);
		parameters.put("IVA", iva);
		parameters.put("TOTAL", neto.add(iva));
		
		JasperReport subReport = (JasperReport) JRLoader
				.loadObject(SubdiarioIVAReporte.class
						.getResourceAsStream("/reportes/subdiario_iva_venta_tproductos.jasper"));
		
		parameters.put("SUBREPORT", subReport);

		JasperReport jasperReport = (JasperReport) JRLoader
				.loadObject(SubdiarioIVAReporte.class
						.getResourceAsStream("/reportes/subdiario_iva_venta.jasper"));

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
				parameters, new JRBeanCollectionDataSource(comprobantesVenta));
		return jasperPrint;
	}

	private static List<ComprobanteVentaDTO> convertirComprobantesVenta(
			List<Comprobante> comprobantes, Map<String, Object> parameters) {
		List<ComprobanteVentaDTO> comprobantesDTO = new ArrayList<ComprobanteVentaDTO>();

		BigDecimal netoRI = BigDecimal.ZERO;
		BigDecimal netoCF = BigDecimal.ZERO;
		BigDecimal netoMON = BigDecimal.ZERO;
		BigDecimal netoOT = BigDecimal.ZERO;

		BigDecimal ivaRI = BigDecimal.ZERO;
		BigDecimal ivaCF = BigDecimal.ZERO;
		BigDecimal ivaMON = BigDecimal.ZERO;
		BigDecimal ivaOT = BigDecimal.ZERO;

		Map<Long, DetalleTipoProductoDTO> mapaTiposProductos = new HashMap<Long, DetalleTipoProductoDTO>();

		if (WUtils.isNotEmpty(comprobantes)) {
			ComprobanteVentaDTO comprobanteDTO = null;
			for (Comprobante comprobante : comprobantes) {
				if (null != comprobante.getIdCliente()) {
					comprobanteDTO = new ComprobanteVentaDTO();
					comprobanteDTO.setCuit(comprobante.getCliente()
							.getDocumento());
					comprobanteDTO.setFecha(comprobante.getFechaVenta());
					comprobanteDTO.setIva(comprobante.getIva());
					comprobanteDTO.setNeto(comprobante.getSubtotal());
					comprobanteDTO.setRazonSocial(comprobante.getCliente()
							.getRazonSocial());
					comprobanteDTO.setTipo(comprobante.getCliente()
							.getCondicionIVA().getAbreviacion());
					comprobanteDTO.setComprobante(comprobante
							.getTipoComprobante().getValue()
							+ " "
							+ (WUtils.isNotEmpty(comprobante
									.getNroCompFormato()) ? comprobante
									.getNroCompFormato() : "0000-00000000"));
					comprobanteDTO.setTotal(comprobante.getTotal());
					populateDetalles(mapaTiposProductos,
							comprobante.getDetalles());
					
					
					if (comprobante.getTipoComprobante().equals(TipoComprobante.NOTA_CREDITO)) {
						comprobanteDTO.setTotal(comprobanteDTO.getTotal().negate());
						comprobanteDTO.setIva(comprobanteDTO.getIva().negate());
						comprobanteDTO.setNeto(comprobanteDTO.getNeto().negate());
					}
					
					comprobantesDTO.add(comprobanteDTO);

					CondicionIVA condicionIVA = comprobante.getCliente()
							.getCondicionIVA();
					switch (condicionIVA) {
					case CONS_FINAL:
						netoCF = netoCF.add(comprobanteDTO.getNeto());
						ivaCF = ivaCF.add(comprobanteDTO.getIva());
						break;
					case EXENTO:
						netoOT = netoOT.add(comprobanteDTO.getNeto());
						ivaOT = ivaOT.add(comprobanteDTO.getIva());
						break;
					case MONOTRIBUTISTA:
						netoMON = netoMON.add(comprobanteDTO.getNeto());
						ivaMON = ivaMON.add(comprobanteDTO.getIva());
						break;
					case RESP_INSC:
						netoRI = netoRI.add(comprobanteDTO.getNeto());
						ivaRI = ivaRI.add(comprobanteDTO.getIva());
						break;
					case RESP_NO_INSC:
						netoOT = netoOT.add(comprobanteDTO.getNeto());
						ivaOT = ivaOT.add(comprobanteDTO.getIva());
						break;
					}

				}
			}
			parameters.put(
					"TOTAL_TIPO_PRODUCTO",
					new ArrayList<DetalleTipoProductoDTO>(mapaTiposProductos
							.values()));
		}

		parameters.put("RI_NETO", netoRI);
		parameters.put("CF_NETO", netoCF);
		parameters.put("MON_NETO", netoMON);
		parameters.put("OT_NETO", netoOT);
		parameters.put("TOTAL_NETO", netoRI.add(netoCF).add(netoMON)
				.add(netoOT));

		parameters.put("RI_IVA", ivaRI);
		parameters.put("CF_IVA", ivaCF);
		parameters.put("MON_IVA", ivaMON);
		parameters.put("OT_IVA", ivaOT);
		parameters.put("TOTAL_IVA", ivaRI.add(ivaCF).add(ivaMON).add(ivaOT));

		return comprobantesDTO;
	}

	private static void populateDetalles(
			Map<Long, DetalleTipoProductoDTO> mapaTiposProductos,
			List<DetalleComprobante> detalles) {

		if (WUtils.isNotEmpty(detalles)) {
			DetalleTipoProductoDTO detalleTipoProductoDTO = null;
			for (DetalleComprobante detalleComprobante : detalles) {
				if (null != detalleComprobante.getProducto()) {
					Long idTipoProducto = detalleComprobante.getProducto()
							.getTipoProducto().getId();
					detalleTipoProductoDTO = mapaTiposProductos
							.get(idTipoProducto);
					if (null == detalleTipoProductoDTO) {
						detalleTipoProductoDTO = new DetalleTipoProductoDTO();
						detalleTipoProductoDTO.setIva(BigDecimal.ZERO);
						detalleTipoProductoDTO.setNeto(BigDecimal.ZERO);
						detalleTipoProductoDTO
								.setTipoProducto(detalleComprobante
										.getProducto().getTipoProducto()
										.getNombre());
					}
					detalleTipoProductoDTO.setIva(detalleTipoProductoDTO
							.getIva().add(WUtils.getValue(detalleComprobante.getTotalIVA())));
					detalleTipoProductoDTO.setNeto(detalleTipoProductoDTO
							.getNeto().add(detalleComprobante.getSubtotal()));
					mapaTiposProductos.put(idTipoProducto,
							detalleTipoProductoDTO);
				}
			}
		}

	}

	private static List<ComprobanteCompraDTO> convertirComprobantesCompra(
			List<Comprobante> comprobantes) {
		List<ComprobanteCompraDTO> comprobantesDTO = new ArrayList<ComprobanteCompraDTO>();
		if (WUtils.isNotEmpty(comprobantes)) {
			ComprobanteCompraDTO comprobanteDTO = null;
			for (Comprobante comprobante : comprobantes) {
				if (null != comprobante.getIdProveedor()) {
					comprobanteDTO = new ComprobanteCompraDTO();
					comprobanteDTO
							.setCuit(comprobante.getProveedor().getCuit());
					comprobanteDTO.setFecha(comprobante.getFechaVenta());
					comprobanteDTO.setIva(comprobante.getIva());
					comprobanteDTO.setNeto(comprobante.getSubtotal());
					comprobanteDTO.setOtros(getTotalTipoTributo(
							comprobante.getTributos(), TipoTributo.OTROS));
					comprobanteDTO.setPercepcion(getTotalTipoTributo(
							comprobante.getTributos(),
							TipoTributo.PER_RET_ING_BRUTOS));
					comprobanteDTO.setProveedor(comprobante.getProveedor()
							.getRazonSocial());
					comprobanteDTO.setTipo(comprobante.getProveedor()
							.getCondicionIVA().getAbreviacion());
					comprobanteDTO.setComprobante(comprobante
							.getTipoComprobante().getValue()
							+ " "
							+ (WUtils.isNotEmpty(comprobante
									.getNroCompFormato()) ? comprobante
									.getNroCompFormato() : "0000-00000000"));
					comprobanteDTO.setTotal(comprobante.getTotal());
					
					if (comprobante.getTipoComprobante().equals(TipoComprobante.NOTA_CREDITO)) {
						comprobanteDTO.setTotal(comprobanteDTO.getTotal().negate());
						comprobanteDTO.setIva(comprobanteDTO.getIva().negate());
						comprobanteDTO.setNeto(comprobanteDTO.getNeto().negate());
						comprobanteDTO.setOtros(comprobanteDTO.getOtros().negate());
						comprobanteDTO.setPercepcion(comprobanteDTO.getPercepcion().negate());
					}
					comprobantesDTO.add(comprobanteDTO);
				}
			}
		}

		return comprobantesDTO;
	}

	private static BigDecimal getTotalTipoTributo(
			List<TributoComprobante> tributos, TipoTributo tipoTributo) {
		if (WUtils.isNotEmpty(tributos)) {
			BigDecimal totalTributo = BigDecimal.ZERO;
			for (TributoComprobante tributoComprobante : tributos) {
				if (tributoComprobante.getTributo().equals(tipoTributo)) {
					totalTributo = totalTributo.add(tributoComprobante
							.getImporte());
				}
			}
			return totalTributo;
		}
		return BigDecimal.ZERO;
	}

	public static void main(String[] args) throws Exception {
		generarReporte(null, null);
	}
}
