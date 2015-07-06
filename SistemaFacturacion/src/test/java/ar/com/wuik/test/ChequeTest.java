package ar.com.wuik.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.internal.builders.JUnit4Builder;

import ar.com.wuik.sistema.bo.ChequeBO;
import ar.com.wuik.sistema.entities.Cheque;
import ar.com.wuik.sistema.filters.ChequeFilter;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.utils.WUtils;

public class ChequeTest extends JUnit4Builder {

	private ChequeBO chequeBO;

	@Before
	public void setUp() throws Exception {
		chequeBO = AbstractFactory.getInstance(ChequeBO.class);
	}

	@org.junit.Test
	public void testObtener() throws Exception {
		// Guardo
		Cheque cheque = new Cheque();
		cheque.setFechaEmision(new Date());
		cheque.setFechaPago(new Date());
		cheque.setIdBanco(19L);
		cheque.setImporte(BigDecimal.TEN);
		cheque.setNumero("123");
		cheque.setRecibidoDe("JUAN MANUEL VAZQUEZ");
		chequeBO.guardar(cheque);
		Long id = cheque.getId();
		assertNotNull(id);

		// Obtengo
		cheque = chequeBO.obtener(id);
		assertNotNull(cheque);
	}

	@org.junit.Test
	public void testBuscar() throws Exception {

		// Guardo
		Cheque cheque = new Cheque();
		cheque.setFechaEmision(new Date());
		cheque.setFechaPago(new Date());
		cheque.setIdBanco(19L);
		cheque.setImporte(BigDecimal.TEN);
		cheque.setNumero("1234567890");
		cheque.setRecibidoDe("JUAN MANUEL VAZQUEZ");
		chequeBO.guardar(cheque);
		Long id = cheque.getId();
		assertNotNull(id);

		// Busco
		ChequeFilter filter = new ChequeFilter();
		filter.setNumero("NO EXISTE");
		filter.setRecibidoDe("NO EXISTE");
		List<Cheque> cheques = chequeBO.buscar(filter);
		assertFalse(WUtils.isNotEmpty(cheques));

		filter = new ChequeFilter();
		filter.setNumero("1234567890");
		filter.setRecibidoDe("JUAN MANUEL VAZQUEZ");
		cheques = chequeBO.buscar(filter);
		assertTrue(WUtils.isNotEmpty(cheques));

	}

	@org.junit.Test
	public void testGuardar() throws Exception {
		
		Date fecha = new Date();
		Long idBanco = 19L;
		BigDecimal importe = BigDecimal.TEN;
		String numero = "123";
		String recibidoDe = "JUAN MANUEL VAZQUEZ";
		
		// Guardo
		Cheque cheque = new Cheque();
		cheque.setFechaEmision(fecha);
		cheque.setFechaPago(fecha);
		cheque.setIdBanco(idBanco);
		cheque.setImporte(importe);
		cheque.setNumero(numero);
		cheque.setRecibidoDe(recibidoDe);
		chequeBO.guardar(cheque);
		Long id = cheque.getId();
		assertNotNull(id);

		// Obtengo
		cheque = chequeBO.obtener(id);
		assertEquals(WUtils.getStringFromDate(fecha), WUtils.getStringFromDate(cheque.getFechaEmision()));
		assertEquals(WUtils.getStringFromDate(fecha), WUtils.getStringFromDate(cheque.getFechaPago()));
		assertEquals(idBanco, cheque.getIdBanco());
		assertEquals(numero, cheque.getNumero());
		assertEquals(recibidoDe, cheque.getRecibidoDe());
	}

	@org.junit.Test
	public void testActualizar() throws Exception {

		// Guardo
		Cheque cheque = new Cheque();
		cheque.setFechaEmision(new Date());
		cheque.setFechaPago(new Date());
		cheque.setIdBanco(19L);
		cheque.setImporte(BigDecimal.TEN);
		cheque.setNumero("123");
		cheque.setRecibidoDe("JUAN MANUEL VAZQUEZ");
		chequeBO.guardar(cheque);
		Long id = cheque.getId();
		assertNotNull(id);

		// Obtengo
		cheque = chequeBO.obtener(id);
		assertNotNull(cheque);

		// Actualizo
		cheque.setNumero("456");
		chequeBO.actualizar(cheque);

		// Obtengo
		cheque = chequeBO.obtener(id);
		assertEquals("456", cheque.getNumero());

	}

	@org.junit.Test
	public void testEliminar() throws Exception {

		// Guardo
		Cheque cheque = new Cheque();
		cheque.setFechaEmision(new Date());
		cheque.setFechaPago(new Date());
		cheque.setIdBanco(19L);
		cheque.setImporte(BigDecimal.TEN);
		cheque.setNumero("123");
		cheque.setRecibidoDe("JUAN MANUEL VAZQUEZ");
		chequeBO.guardar(cheque);
		Long id = cheque.getId();
		assertNotNull(id);

		// Elimino
		chequeBO.eliminar(id);
		cheque = chequeBO.obtener(id);
		assertNull(cheque);
	}

	@org.junit.Test
	public void testEstaEnUso() {

	}

	@org.junit.Test
	public void testObtenerTodos() throws Exception {

		// Guardo
		Cheque cheque = new Cheque();
		cheque.setFechaEmision(new Date());
		cheque.setFechaPago(new Date());
		cheque.setIdBanco(19L);
		cheque.setImporte(BigDecimal.TEN);
		cheque.setNumero("123");
		cheque.setRecibidoDe("JUAN MANUEL VAZQUEZ");
		chequeBO.guardar(cheque);
		Long id = cheque.getId();
		assertNotNull(id);

		// Obtengo todos
		List<Cheque> cheques = chequeBO.obtenerTodos();
		assertTrue(WUtils.isNotEmpty(cheques));
	}

}
