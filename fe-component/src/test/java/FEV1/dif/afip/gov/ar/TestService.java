package FEV1.dif.afip.gov.ar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

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

public class TestService {

	private FacturacionService facturacionService;

	@Before
	public void setUp() throws Exception {
		facturacionService = AbstractFactory
				.getInstance(FacturacionService.class);
	}

	@Test
	public void testService() {

		try {
			// Resultado resultado =
			// facturacionService.consultarUltimoComprobante(TipoComprobante.FACTURA_A);
			// System.out.println(resultado);

			Comprobante comprobante = crearComprobante();
			Resultado resultado = facturacionService
					.solicitarComprobante(comprobante);

			System.out.println(resultado);

			// long nroComprobante = 10;
			// TipoComprobante tipoComprobante = TipoComprobante.FACTURA_B;
			//
			// resultado =
			// facturacionService.consultarComprobante(nroComprobante,
			// tipoComprobante);
			//
			// System.out.println(resultado);
		} catch (ServiceException sexc) {
			sexc.printStackTrace();
			System.out.println(Arrays.toString(sexc.getErrores().toArray()));
		}
	}

	private Comprobante crearComprobante() {
		Comprobante comprobante = new Comprobante();
		List<AlicuotaIVA> alicuotas = new ArrayList<AlicuotaIVA>();
		AlicuotaIVA alicuota = new AlicuotaIVA();
		alicuota.setBaseImponible(new BigDecimal(15105.00));
		alicuota.setTipoIVA(TipoIVA.IVA_21);
		alicuota.setTotalAlicuota(new BigDecimal(3172.05));
		alicuotas.add(alicuota);
		comprobante.setAlicuotas(alicuotas);
		comprobante.setComprobantesAsociados(null);
		comprobante.setCotizacion(null);
		comprobante.setDocNro(30709933244L);
		comprobante.setDocTipo(TipoDocumento.CUIT);
		comprobante.setFechaComprobante(new Date());
		comprobante.setImporteIVA(new BigDecimal(3172.05));
		comprobante.setImporteSubtotal(new BigDecimal(15105.00));
		comprobante.setImporteTotal(new BigDecimal(18277.05));
		comprobante.setTipoComprobante(TipoComprobante.FACTURA_A);
		comprobante.setTipoConcepto(TipoConcepto.PRODUCTO);
		comprobante.setTipoMoneda(TipoMoneda.PESOS_ARGENTINOS);
		return comprobante;
	}

}
