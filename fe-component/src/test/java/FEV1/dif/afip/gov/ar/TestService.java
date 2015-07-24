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
			 Resultado resultado =
			// facturacionService.consultarUltimoComprobante(TipoComprobante.FACTURA_A);
			// System.out.println(resultado);

//			Comprobante comprobante = crearComprobante();
//			Resultado resultado = facturacionService
//					.solicitarComprobante(comprobante);

//			System.out.println(resultado);

			// long nroComprobante = 10;
			// TipoComprobante tipoComprobante = TipoComprobante.FACTURA_B;
			//
			 resultado =
			 facturacionService.consultarComprobante(20,
			 TipoComprobante.FACTURA_A);
			 System.out.println(resultado);
		} catch (ServiceException sexc) {
			System.out.println(sexc.getMessage());
			sexc.printStackTrace();
		}
	}
}