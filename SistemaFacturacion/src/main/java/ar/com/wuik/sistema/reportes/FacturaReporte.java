package ar.com.wuik.sistema.reportes;

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
import ar.com.wuik.sistema.bo.FacturaBO;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.ReportException;
import ar.com.wuik.sistema.reportes.entities.DetalleFacturaDTO;
import ar.com.wuik.sistema.reportes.entities.FacturaDTO;
import ar.com.wuik.sistema.utils.AbstractFactory;

public class FacturaReporte {

	public static void generarFactura(Long idFactura) throws ReportException {

		try {
			FacturaBO facturaBO = AbstractFactory.getInstance(FacturaBO.class);
			FacturaDTO facturaDTO = facturaBO.obtenerDTO(idFactura);
			List<DetalleFacturaDTO> detalles = facturaDTO.getDetalles();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("COMP_LETRA", facturaDTO.getLetra());
			parameters.put("COMP_TIPO", facturaDTO.getTipo());
			parameters.put("RAZON_SOCIAL", facturaDTO.getRazonSocial());
			parameters.put("DOMICILIO", facturaDTO.getDomicilio());
			parameters.put("COND_IVA", facturaDTO.getCondIVA());
			parameters.put("PTO_VTA", facturaDTO.getPtoVta());
			parameters.put("COMP_NRO", facturaDTO.getCompNro());
			parameters.put("FECHA_EMISION", facturaDTO.getFechaEmision());
			parameters.put("CUIT", facturaDTO.getCuit());
			parameters.put("ING_BRUTOS", facturaDTO.getIngBrutos());
			parameters.put("INICIO_ATC", facturaDTO.getInicioAct());
			parameters.put("CLIENTE_CUIT", facturaDTO.getClienteCuit());
			parameters.put("CLIENTE_COND_IVA", facturaDTO.getClienteCondIVA());
			parameters.put("CLIENTE_RAZON", facturaDTO.getClienteRazonSocial());
			parameters.put("CLIENTE_DOM", facturaDTO.getClienteDomicilio());
			parameters.put("REMITOS", facturaDTO.getRemitos());
			parameters.put("SUBTOTAL", facturaDTO.getSubtotal());
			parameters.put("IVA_21", facturaDTO.getIva21());
			parameters.put("IVA_105", facturaDTO.getIva105());
			parameters.put("TOTAL", facturaDTO.getTotal());
			parameters.put("CAE", facturaDTO.getCae());
			parameters.put("VTO_CAE", facturaDTO.getVtoCAE());
			parameters.put("BG_IMG", FacturaReporte.class
					.getResourceAsStream("/reportes/bg-comprobante.png"));

			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(FacturaReporte.class
							.getResourceAsStream("/reportes/factura_a.jasper"));

			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, new JRBeanCollectionDataSource(
							detalles));

			JasperViewer.viewReport(jasperPrint, Boolean.FALSE);
		} catch (BusinessException bexc) {
			throw new ReportException(bexc, "Error al obtener Factura");
		} catch (JRException jrexc) {
			throw new ReportException(jrexc, "Error al generar Factura");
		}
	}

	public static void main(String[] args) throws Exception {
		generarFactura(18L);
	}
}
