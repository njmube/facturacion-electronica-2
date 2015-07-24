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
import ar.com.wuik.sistema.bo.NotaCreditoBO;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.ReportException;
import ar.com.wuik.sistema.reportes.entities.DetalleNotaCreditoDTO;
import ar.com.wuik.sistema.reportes.entities.NotaCreditoDTO;
import ar.com.wuik.sistema.utils.AbstractFactory;

public class NotaCreditoReporte {

	public static void generarNotaCredito(Long idNotaCredito) throws ReportException {

		try {
			NotaCreditoBO notaCreditoBO = AbstractFactory.getInstance(NotaCreditoBO.class);
			NotaCreditoDTO notaCreditoDTO = notaCreditoBO.obtenerDTO(idNotaCredito);
			List<DetalleNotaCreditoDTO> detalles = notaCreditoDTO.getDetalles();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("COMP_LETRA", notaCreditoDTO.getLetra());
			parameters.put("COMP_TIPO", notaCreditoDTO.getTipo());
			parameters.put("RAZON_SOCIAL", notaCreditoDTO.getRazonSocial());
			parameters.put("DOMICILIO", notaCreditoDTO.getDomicilio());
			parameters.put("COND_IVA", notaCreditoDTO.getCondIVA());
			parameters.put("PTO_VTA", notaCreditoDTO.getPtoVta());
			parameters.put("COMP_NRO", notaCreditoDTO.getCompNro());
			parameters.put("FECHA_EMISION", notaCreditoDTO.getFechaEmision());
			parameters.put("CUIT", notaCreditoDTO.getCuit());
			parameters.put("ING_BRUTOS", notaCreditoDTO.getIngBrutos());
			parameters.put("INICIO_ATC", notaCreditoDTO.getInicioAct());
			parameters.put("CLIENTE_CUIT", notaCreditoDTO.getClienteCuit());
			parameters.put("CLIENTE_COND_IVA", notaCreditoDTO.getClienteCondIVA());
			parameters.put("CLIENTE_RAZON", notaCreditoDTO.getClienteRazonSocial());
			parameters.put("CLIENTE_DOM", notaCreditoDTO.getClienteDomicilio());
			parameters.put("REMITOS", notaCreditoDTO.getRemitos());
			parameters.put("SUBTOTAL", notaCreditoDTO.getSubtotal());
			parameters.put("IVA_21", notaCreditoDTO.getIva21());
			parameters.put("IVA_105", notaCreditoDTO.getIva105());
			parameters.put("TOTAL", notaCreditoDTO.getTotal());
			parameters.put("CAE", notaCreditoDTO.getCae());
			parameters.put("VTO_CAE", notaCreditoDTO.getVtoCAE());
			parameters.put("COPIA", "ORIGINAL");
			parameters.put("BG_IMG", FacturaReporte.class
					.getResourceAsStream("/reportes/bg-comprobante.png"));

			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(NotaCreditoReporte.class
							.getResourceAsStream("/reportes/comprobante.jasper"));

			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, new JRBeanCollectionDataSource(
							detalles));
			
			parameters.put("COPIA", "DUPLICADO");
			parameters.put("BG_IMG", FacturaReporte.class
					.getResourceAsStream("/reportes/bg-comprobante.png"));
			
			
			
			parameters.put("COPIA", "TRIPLICADO");
			parameters.put("BG_IMG", FacturaReporte.class
					.getResourceAsStream("/reportes/bg-comprobante.png"));

			JasperViewer.viewReport(jasperPrint, Boolean.FALSE);
		} catch (BusinessException bexc) {
			throw new ReportException(bexc, "Error al obtener Nota de Crédito");
		} catch (JRException jrexc) {
			throw new ReportException(jrexc, "Error al generar Nota de Crédito");
		}
	}

	public static void main(String[] args) throws Exception {
		generarNotaCredito(1L);
	}
}
