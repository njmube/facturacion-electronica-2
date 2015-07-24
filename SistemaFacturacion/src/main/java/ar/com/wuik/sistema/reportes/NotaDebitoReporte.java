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
import ar.com.wuik.sistema.bo.NotaDebitoBO;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.ReportException;
import ar.com.wuik.sistema.reportes.entities.DetalleNotaDebitoDTO;
import ar.com.wuik.sistema.reportes.entities.NotaDebitoDTO;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.utils.WJasperUtils;

public class NotaDebitoReporte {

	public static void generarNotaDebito(Long idNotaCredito)
			throws ReportException {

		try {
			NotaDebitoBO notaDebitoBO = AbstractFactory
					.getInstance(NotaDebitoBO.class);
			NotaDebitoDTO notaDebitoDTO = notaDebitoBO
					.obtenerDTO(idNotaCredito);
			List<DetalleNotaDebitoDTO> detalles = notaDebitoDTO.getDetalles();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("COMP_LETRA", notaDebitoDTO.getLetra());
			parameters.put("COMP_TIPO", notaDebitoDTO.getTipo());
			parameters.put("RAZON_SOCIAL", notaDebitoDTO.getRazonSocial());
			parameters.put("DOMICILIO", notaDebitoDTO.getDomicilio());
			parameters.put("COND_IVA", notaDebitoDTO.getCondIVA());
			parameters.put("PTO_VTA", notaDebitoDTO.getPtoVta());
			parameters.put("COMP_NRO", notaDebitoDTO.getCompNro());
			parameters.put("FECHA_EMISION", notaDebitoDTO.getFechaEmision());
			parameters.put("CUIT", notaDebitoDTO.getCuit());
			parameters.put("ING_BRUTOS", notaDebitoDTO.getIngBrutos());
			parameters.put("INICIO_ATC", notaDebitoDTO.getInicioAct());
			parameters.put("CLIENTE_CUIT", notaDebitoDTO.getClienteCuit());
			parameters.put("CLIENTE_COND_IVA",
					notaDebitoDTO.getClienteCondIVA());
			parameters.put("CLIENTE_RAZON",
					notaDebitoDTO.getClienteRazonSocial());
			parameters.put("CLIENTE_DOM", notaDebitoDTO.getClienteDomicilio());
			parameters.put("REMITOS", notaDebitoDTO.getRemitos());
			parameters.put("SUBTOTAL", notaDebitoDTO.getSubtotal());
			parameters.put("IVA_21", notaDebitoDTO.getIva21());
			parameters.put("IVA_105", notaDebitoDTO.getIva105());
			parameters.put("TOTAL", notaDebitoDTO.getTotal());
			parameters.put("CAE", notaDebitoDTO.getCae());
			parameters.put("COD_BARRAS", notaDebitoDTO.getCodigoBarras());
			parameters.put("VTO_CAE", notaDebitoDTO.getVtoCAE());
			parameters.put("BG_IMG", NotaDebitoReporte.class
					.getResourceAsStream("/reportes/bg-comprobante.png"));

			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(NotaDebitoReporte.class
							.getResourceAsStream("/reportes/comprobante.jasper"));

			JasperPrint jasperPrintOriginal = JasperFillManager.fillReport(
					jasperReport, parameters, new JRBeanCollectionDataSource(
							detalles));

			// DUPLICADO
			parameters.put("COPIA", "DUPLICADO");
			parameters.put("BG_IMG", NotaDebitoReporte.class
					.getResourceAsStream("/reportes/bg-comprobante.png"));
			JasperPrint jasperPrintDuplicado = JasperFillManager.fillReport(
					jasperReport, parameters, new JRBeanCollectionDataSource(
							detalles));

			// TRIPLICADO
			parameters.put("COPIA", "TRIPLICADO");
			parameters.put("BG_IMG", NotaDebitoReporte.class
					.getResourceAsStream("/reportes/bg-comprobante.png"));
			JasperPrint jasperPrintTriplicado = JasperFillManager.fillReport(
					jasperReport, parameters, new JRBeanCollectionDataSource(
							detalles));

			JasperPrint jasperPrint = WJasperUtils.concatReports(
					jasperPrintOriginal, jasperPrintDuplicado,
					jasperPrintTriplicado);

			JasperViewer.viewReport(jasperPrint, Boolean.FALSE);
		} catch (BusinessException bexc) {
			throw new ReportException(bexc, "Error al obtener Nota de Débito");
		} catch (JRException jrexc) {
			throw new ReportException(jrexc, "Error al generar Nota de Débito");
		}
	}

	public static void main(String[] args) throws Exception {
		generarNotaDebito(1L);
	}
}
