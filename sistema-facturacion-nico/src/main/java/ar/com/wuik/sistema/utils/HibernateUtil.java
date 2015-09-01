package ar.com.wuik.sistema.utils;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import ar.com.wuik.sistema.entities.Banco;
import ar.com.wuik.sistema.entities.Cliente;
import ar.com.wuik.sistema.entities.Comprobante;
import ar.com.wuik.sistema.entities.DetalleComprobante;
import ar.com.wuik.sistema.entities.DetalleRemito;
import ar.com.wuik.sistema.entities.Localidad;
import ar.com.wuik.sistema.entities.Parametro;
import ar.com.wuik.sistema.entities.Permiso;
import ar.com.wuik.sistema.entities.Producto;
import ar.com.wuik.sistema.entities.Proveedor;
import ar.com.wuik.sistema.entities.Remito;
import ar.com.wuik.sistema.entities.TipoProducto;
import ar.com.wuik.sistema.entities.TributoComprobante;
import ar.com.wuik.sistema.entities.Usuario;
import ar.com.wuik.sistema.entities.enums.CondicionIVA;

public class HibernateUtil {

	private static Session session;
	private static SessionFactory sessionFactory = null;
	private static Transaction tx;

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
			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
					.applySettings(cfg.getProperties()).buildServiceRegistry();
			return cfg.buildSessionFactory(serviceRegistry);
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
			tx = null;
		}
	}

	public static void rollbackTransaction() {
		if (null != session && null != tx) {
			tx.rollback();
			tx = null;
		}
	}

	public static void startTransaction() {
		tx = getSession().beginTransaction();
	}

	private static void getClasses(final Configuration cfg)
			throws ClassNotFoundException, IOException {
		cfg.addAnnotatedClass(Banco.class);
		cfg.addAnnotatedClass(Cliente.class);
		cfg.addAnnotatedClass(CondicionIVA.class);
		cfg.addAnnotatedClass(DetalleComprobante.class);
		cfg.addAnnotatedClass(DetalleRemito.class);
		cfg.addAnnotatedClass(Comprobante.class);
		cfg.addAnnotatedClass(Localidad.class);
		cfg.addAnnotatedClass(Parametro.class);
		cfg.addAnnotatedClass(Permiso.class);
		cfg.addAnnotatedClass(Producto.class);
		cfg.addAnnotatedClass(Proveedor.class);
		cfg.addAnnotatedClass(Remito.class);
		cfg.addAnnotatedClass(TipoProducto.class);
		cfg.addAnnotatedClass(TributoComprobante.class);
		cfg.addAnnotatedClass(Usuario.class);
	}

}
