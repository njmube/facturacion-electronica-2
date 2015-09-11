package ar.com.wuik.sistema.reportes;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.com.wuik.sistema.bo.ClienteBO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.exceptions.BusinessException;
import ar.com.wuik.sistema.exceptions.ReportException;
import ar.com.wuik.sistema.reportes.entities.DetalleResumenCuentaDTO;
import ar.com.wuik.sistema.reportes.entities.ResumenCuentaDTO;
import ar.com.wuik.sistema.utils.AbstractFactory;

public class ResumenReporte {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ResumenReporte.class);

	public static void generarImpresion(Long idCliente)
			throws ReportException {

		try {
			ClienteBO clienteBO = AbstractFactory
					.getInstance(ClienteBO.class);
			Cliente cliente = clienteBO.obtener(idCliente);
			ResumenCuentaDTO resumenCuenta = clienteBO
					.obtenerResumenCuenta(idCliente);

			List<DetalleResumenCuentaDTO> detalles = resumenCuenta.getDetalles();
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("FECHA_EMISION", new Date());
			parameters.put("CLIENTE_CUIT", resumenCuenta.getClienteCuit());
			parameters.put("CLIENTE_COND_IVA",
					resumenCuenta.getClienteCondIVA());
			parameters.put("CLIENTE_RAZON",
					resumenCuenta.getClienteRazonSocial());
			parameters.put("CLIENTE_DOM", resumenCuenta.getClienteDomicilio());
			parameters.put("SALDO", resumenCuenta.getSaldo());
			parameters.put("BG_IMG", ResumenReporte.class
					.getResourceAsStream("/reportes/bg-comprobante.png"));

			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(ResumenReporte.class
							.getResourceAsStream("/reportes/resumen_cuenta.jasper"));

			// ORIGINAL
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parameters, new JRBeanCollectionDataSource(
							detalles));

			JasperViewer.viewReport(jasperPrint, Boolean.FALSE);
		} catch (BusinessException bexc) {
			throw new ReportException(bexc, "Error al obtener Comprobante");
		} catch (JRException jrexc) {
			LOGGER.error("Error al generar Comprobante", jrexc);
			throw new ReportException(jrexc, "Error al generar Comprobante");
		}
	}

	public static void main(String[] args) throws Exception {
		generarImpresion(1L);
	}
}
