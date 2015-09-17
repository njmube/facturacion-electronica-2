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
import ar.com.wuik.sistema.reportes.entities.DetalleRemitoDTO;
import ar.com.wuik.sistema.reportes.entities.RemitoDTO;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.utils.WJasperUtils;

public class RemitoReporte {

	public static void generarRemito(Long idRemito) throws ReportException {

		try {
			RemitoBO remitoBO = AbstractFactory.getInstance(RemitoBO.class);
			RemitoDTO remitoDTO = remitoBO.obtenerDTO(idRemito);
			List<DetalleRemitoDTO> detalles = remitoDTO.getDetalles();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("COMP_NRO", remitoDTO.getCompNro());
			parameters.put("FECHA_EMISION", remitoDTO.getFechaEmision());
			parameters.put("SHOW_ALL", true);
			parameters.put("CLIENTE_CUIT", remitoDTO.getClienteCuit());
			parameters.put("CLIENTE_COND_IVA", remitoDTO.getClienteCondIVA());
			parameters.put("CLIENTE_RAZON", remitoDTO.getClienteRazonSocial());
			parameters.put("CLIENTE_DOM", remitoDTO.getClienteDomicilio());
			parameters.put("OBSERVACIONES", remitoDTO.getObservaciones());
			parameters.put("BG_IMG", RemitoReporte.class
					.getResourceAsStream("/reportes/bg-comprobante.png"));

			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(RemitoReporte.class
							.getResourceAsStream("/reportes/remito.jasper"));

			JasperPrint jasperPrintOriginal = JasperFillManager.fillReport(
					jasperReport, parameters, new JRBeanCollectionDataSource(
							detalles));
			
			JasperPrint jasperPrintDuplicado = JasperFillManager.fillReport(
					jasperReport, parameters, new JRBeanCollectionDataSource(
							detalles));
		
			JasperPrint jasperPrint = WJasperUtils.concatReports(
					jasperPrintOriginal, jasperPrintDuplicado);

			JasperViewer.viewReport(jasperPrint, Boolean.FALSE);
			// JasperPrintManager.printReport(jasperPrint, Boolean.TRUE);
		} catch (BusinessException bexc) {
			throw new ReportException(bexc, "Error al obtener Factura");
		} catch (JRException jrexc) {
			throw new ReportException(jrexc, "Error al generar Factura");
		}
	}

	public static void main(String[] args) throws Exception {
		generarRemito(1L);
	}
}
