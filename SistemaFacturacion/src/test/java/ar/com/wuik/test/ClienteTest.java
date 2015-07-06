package ar.com.wuik.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.internal.builders.JUnit4Builder;

import ar.com.wuik.sistema.bo.ClienteBO;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.filters.ClienteFilter;
import ar.com.wuik.sistema.utils.AbstractFactory;
import ar.com.wuik.swing.utils.WUtils;

public class ClienteTest extends JUnit4Builder {

	private ClienteBO clienteBO;

	@Before
	public void setUp() throws Exception {
		clienteBO = AbstractFactory.getInstance(ClienteBO.class);
	}

	@org.junit.Test
	public void testObtener() throws Exception {
		// Guardo
		Cliente cliente = new Cliente();
		cliente.setCelular("02474-15-123456");
		cliente.setCuit("30-71214423-4");
		cliente.setDireccion("SAN MARTIN 245");
		cliente.setIdCondicionIVA(1L);
		cliente.setIdLocalidad(105L);
		cliente.setRazonSocial("VAZQUEZ HERMANOS");
		cliente.setTelefono("02474-435566");
		clienteBO.guardar(cliente);
		Long id = cliente.getId();
		assertNotNull(id);

		// Obtengo
		cliente = clienteBO.obtener(id);
		assertNotNull(cliente);
	}

	@org.junit.Test
	public void testBuscar() throws Exception {

		// Guardo
		Cliente cliente = new Cliente();
		cliente.setCelular("02474-15-123456");
		cliente.setCuit("30-71214423-4");
		cliente.setDireccion("SAN MARTIN 245");
		cliente.setIdCondicionIVA(1L);
		cliente.setIdLocalidad(105L);
		cliente.setRazonSocial("VAZQUEZ HERMANOS");
		cliente.setTelefono("02474-435566");
		clienteBO.guardar(cliente);
		Long id = cliente.getId();
		assertNotNull(id);

		// Busco
		ClienteFilter filter = new ClienteFilter();
		filter.setCuit("NO EXISTE");
		filter.setRazonSocial("NO EXISTE");
		List<Cliente> clientes = clienteBO.buscar(filter);
		assertFalse(WUtils.isNotEmpty(clientes));

		filter = new ClienteFilter();
		filter.setCuit("30-71214423-4");
		filter.setRazonSocial("VAZQUEZ HERMANOS");
		clientes = clienteBO.buscar(filter);
		assertTrue(WUtils.isNotEmpty(clientes));

	}

	@org.junit.Test
	public void testGuardar() throws Exception {

		String celular = "02474-15-123456";
		String cuit = "30-71214423-4";
		String direccion = "SAN MARTIN 245";
		Long idCondIVA = 1L;
		Long idLocalidad = 105L;
		String razonSocial = "VAZQUEZ HERMANOS";
		String telefono = "02474-435566";

		// Guardo
		Cliente cliente = new Cliente();
		cliente.setCelular(celular);
		cliente.setCuit(cuit);
		cliente.setDireccion(direccion);
		cliente.setIdCondicionIVA(idCondIVA);
		cliente.setIdLocalidad(idLocalidad);
		cliente.setRazonSocial(razonSocial);
		cliente.setTelefono(telefono);
		clienteBO.guardar(cliente);
		Long id = cliente.getId();
		assertNotNull(id);

		// Obtengo
		cliente = clienteBO.obtener(id);
		assertEquals(celular, cliente.getCelular());
		assertEquals(cuit, cliente.getCuit());
		assertEquals(direccion, cliente.getDireccion());
		assertEquals(idCondIVA, cliente.getIdCondicionIVA());
		assertEquals(idLocalidad, cliente.getIdLocalidad());
		assertEquals(razonSocial, cliente.getRazonSocial());
		assertEquals(telefono, cliente.getTelefono());
	}

	@org.junit.Test
	public void testActualizar() throws Exception {

		// Guardo
		Cliente cliente = new Cliente();
		cliente.setCelular("02474-15-123456");
		cliente.setCuit("30-71214423-4");
		cliente.setDireccion("SAN MARTIN 245");
		cliente.setIdCondicionIVA(1L);
		cliente.setIdLocalidad(105L);
		cliente.setRazonSocial("VAZQUEZ HERMANOS");
		cliente.setTelefono("02474-435566");
		clienteBO.guardar(cliente);
		Long id = cliente.getId();
		assertNotNull(id);

		// Obtengo
		cliente = clienteBO.obtener(id);
		assertNotNull(cliente);

		// Actualizo
		cliente.setRazonSocial("VAZQUEZ Y ASOCIADOS");
		clienteBO.actualizar(cliente);

		// Obtengo
		cliente = clienteBO.obtener(id);
		assertEquals("VAZQUEZ Y ASOCIADOS", cliente.getRazonSocial());

	}

	@org.junit.Test
	public void testEliminar() throws Exception {

		// Guardo
		Cliente cliente = new Cliente();
		cliente.setCelular("02474-15-123456");
		cliente.setCuit("30-71214423-4");
		cliente.setDireccion("SAN MARTIN 245");
		cliente.setIdCondicionIVA(1L);
		cliente.setIdLocalidad(105L);
		cliente.setRazonSocial("VAZQUEZ HERMANOS");
		cliente.setTelefono("02474-435566");
		clienteBO.guardar(cliente);
		Long id = cliente.getId();
		assertNotNull(id);

		// Elimino
		clienteBO.eliminar(id);
		cliente = clienteBO.obtener(id);
		assertNull(cliente);
	}

	@org.junit.Test
	public void testEstaEnUso() {

	}

	@org.junit.Test
	public void testObtenerTodos() throws Exception {

		// Guardo
		Cliente cliente = new Cliente();
		cliente.setCelular("02474-15-123456");
		cliente.setCuit("30-71214423-4");
		cliente.setDireccion("SAN MARTIN 245");
		cliente.setIdCondicionIVA(1L);
		cliente.setIdLocalidad(105L);
		cliente.setRazonSocial("VAZQUEZ HERMANOS");
		cliente.setTelefono("02474-435566");
		clienteBO.guardar(cliente);
		Long id = cliente.getId();
		assertNotNull(id);

		// Obtengo todos
		List<Cliente> clientes = clienteBO.obtenerTodos();
		assertTrue(WUtils.isNotEmpty(clientes));
	}

	@org.junit.Test
	public void testActivarDesactivar() throws Exception {

		// Guardo
		Cliente cliente = new Cliente();
		cliente.setCelular("02474-15-123456");
		cliente.setCuit("30-71214423-4");
		cliente.setDireccion("SAN MARTIN 245");
		cliente.setIdCondicionIVA(1L);
		cliente.setIdLocalidad(105L);
		cliente.setRazonSocial("VAZQUEZ HERMANOS");
		cliente.setTelefono("02474-435566");
		clienteBO.guardar(cliente);
		Long id = cliente.getId();
		assertNotNull(id);

		// Desactivo
		clienteBO.desactivar(id);
		cliente = clienteBO.obtener(id);
		assertTrue(!cliente.isActivo());
		

		// Activo
		clienteBO.activar(id);
		cliente = clienteBO.obtener(id);
		assertTrue(cliente.isActivo());
	}

}
