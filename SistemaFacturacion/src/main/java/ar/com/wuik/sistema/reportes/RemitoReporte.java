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
import ar.com.wuik.sistema.bo.RemitoBO;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.ReportException;
import ar.com.wuik.sistema.reportes.entities.DetalleFacturaDTO;
import ar.com.wuik.sistema.reportes.entities.RemitoDTO;
import ar.com.wuik.sistema.utils.AbstractFactory;

public class RemitoReporte {

	public static void generarRemito(Long idRemito) throws ReportException {

		try {
			RemitoBO remitoBO = AbstractFactory.getInstance(RemitoBO.class);
			RemitoDTO remitoDTO = remitoBO.obtenerDTO(idRemito);
			List<DetalleFacturaDTO> detalles = remitoDTO.getDetalles();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("RAZON_SOCIAL", remitoDTO.getRazonSocial());
			parameters.put("DOMICILIO", remitoDTO.getDomicilio());
			parameters.put("COND_IVA", remitoDTO.getCondIVA());
			parameters.put("PTO_VTA", remitoDTO.getPtoVta());
			parameters.put("COMP_NRO", remitoDTO.getCompNro());
			parameters.put("FECHA_EMISION", remitoDTO.getFechaEmision());
			parameters.put("CUIT", remitoDTO.getCuit());
			parameters.put("ING_BRUTOS", remitoDTO.getIngBrutos());
			parameters.put("INICIO_ATC", remitoDTO.getInicioAct());
			parameters.put("CLIENTE_CUIT", remitoDTO.getClienteCuit());
			parameters.put("CLIENTE_COND_IVA", remitoDTO.getClienteCondIVA());
			parameters.put("CLIENTE_RAZON", remitoDTO.getClienteRazonSocial());
			parameters.put("CLIENTE_DOM", remitoDTO.getClienteDomicilio());
			parameters.put("BG_IMG", RemitoReporte.class
					.getResourceAsStream("/reportes/bg-comprobante.png"));

			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(RemitoReporte.class
							.getResourceAsStream("/reportes/remito.jasper"));

			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, new JRBeanCollectionDataSource(
							detalles));

			// JasperPrintManager.printReport(jasperPrint, Boolean.TRUE);
			JasperViewer.viewReport(jasperPrint, Boolean.FALSE);
		} catch (BusinessException bexc) {
			throw new ReportException(bexc, "Error al obtener Factura");
		} catch (JRException jrexc) {
			throw new ReportException(jrexc, "Error al generar Factura");
		}
	}

	public static void main(String[] args) throws Exception {
		generarRemito(18L);
	}
}
