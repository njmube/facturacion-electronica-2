package ar.com.wuik.sistema.utils;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import ar.com.wuik.sistema.entities.Banco;
import ar.com.wuik.sistema.entities.Cheque;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.ComprobanteRecibo;
import ar.com.wuik.sistema.entities.CondicionIVA;
import ar.com.wuik.sistema.entities.DetalleFactura;
import ar.com.wuik.sistema.entities.DetalleNotaCredito;
import ar.com.wuik.sistema.entities.DetalleNotaDebito;
import ar.com.wuik.sistema.entities.DetalleRemito;
import ar.com.wuik.sistema.entities.Factura;
import ar.com.wuik.sistema.entities.Localidad;
import ar.com.wuik.sistema.entities.NotaCredito;
import ar.com.wuik.sistema.entities.NotaDebito;
import ar.com.wuik.sistema.entities.PagoReciboCheque;
import ar.com.wuik.sistema.entities.PagoReciboEfectivo;
import ar.com.wuik.sistema.entities.PagoReciboNotaCredito;
import ar.com.wuik.sistema.entities.Parametro;
import ar.com.wuik.sistema.entities.Permiso;
import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.sistema.entities.Proveedor;
import ar.com.wuik.sistema.entities.Recibo;
import ar.com.wuik.sistema.entities.Remito;
import ar.com.wuik.sistema.entities.StockProducto;
import ar.com.wuik.sistema.entities.TipoProducto;
import ar.com.wuik.sistema.entities.Usuario;

public class HibernateUtil {

	private static Session session;
	private static SessionFactory sessionFactory = null;
	private static Transaction tx;

	@SuppressWarnings("deprecation")
	private static SessionFactory buildSessionFactory() {
		try {
			final Configuration cfg = new Configuration();
			final String user = DBUtil.getUser();
			final String password = DBUtil.getPassword();
			final String connectionString = DBUtil.getConnectionString();
			cfg.setProperty("hibernate.connection.url", connectionString);
			cfg.setProperty("hibernate.connection.username", user);
			cfg.setProperty("hibernate.connection.password", password);
			getClasses(cfg);
			return cfg.buildSessionFactory();
		} catch (Throwable ex) {
			// Make sure you log the exception, as it might be swallowed
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		if (null == sessionFactory) {
			sessionFactory = buildSessionFactory();
		}
		return sessionFactory;
	}

	public static Session getSession() {
		if (null == session)
			session = getSessionFactory().openSession();
		return session;
	}

	public static void closeSession() {
		if (null != session)
			session.close();
		session = null;
		tx = null;
	}

	public static void commitTransaction() {
		if (null != session && null != tx) {
			tx.commit();
		}
	}

	public static void rollbackTransaction() {
		if (null != session && null != tx) {
			tx.rollback();
		}
	}

	public static void startTransaction() {
		tx = getSession().beginTransaction();
	}

	private static void getClasses(final Configuration cfg)
			throws ClassNotFoundException, IOException {
		cfg.addAnnotatedClass(Banco.class);
		cfg.addAnnotatedClass(Cheque.class);
		cfg.addAnnotatedClass(Cliente.class);
		cfg.addAnnotatedClass(ComprobanteRecibo.class);
		cfg.addAnnotatedClass(CondicionIVA.class);
		cfg.addAnnotatedClass(DetalleFactura.class);
		cfg.addAnnotatedClass(DetalleNotaCredito.class);
		cfg.addAnnotatedClass(DetalleNotaDebito.class);
		cfg.addAnnotatedClass(DetalleRemito.class);
		cfg.addAnnotatedClass(Factura.class);
		cfg.addAnnotatedClass(Localidad.class);
		cfg.addAnnotatedClass(NotaCredito.class);
		cfg.addAnnotatedClass(NotaDebito.class);
		cfg.addAnnotatedClass(PagoReciboCheque.class);
		cfg.addAnnotatedClass(PagoReciboEfectivo.class);
		cfg.addAnnotatedClass(PagoReciboNotaCredito.class);
		cfg.addAnnotatedClass(Parametro.class);
		cfg.addAnnotatedClass(Permiso.class);
		cfg.addAnnotatedClass(Producto.class);
		cfg.addAnnotatedClass(Proveedor.class);
		cfg.addAnnotatedClass(Recibo.class);
		cfg.addAnnotatedClass(Remito.class);
		cfg.addAnnotatedClass(StockProducto.class);
		cfg.addAnnotatedClass(TipoProducto.class);
		cfg.addAnnotatedClass(Usuario.class);
	}

}
