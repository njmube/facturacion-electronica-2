package ar.com.dcode.afip.utils;


public class AbstractFactory {

	@SuppressWarnings("unchecked")
	public static <E> E getInstance(final Class<E> interfaceName) {

		E entidad = null;

		final String packageName = interfaceName.getPackage().getName() + ".impl";
		final String className = interfaceName.getSimpleName() + "Impl";

		try {
			final Class<E> classImpl = (Class<E>) Class.forName(packageName + "."
					+ className);
			entidad = classImpl.newInstance();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IllegalAccessException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}

		return entidad;
	}

	@SuppressWarnings("unchecked")
	public static <E> E getInstance(final Class<E> interfaceName, final Class<?> clazz) {

		E entidad = null;

		final String packageName = interfaceName.getPackage().getName() + ".impl";
		final String className = interfaceName.getSimpleName() + "Impl";

		try {
			final Class<E> classImpl = (Class<E>) Class.forName(packageName + "."
					+ className);
			if (null != clazz) {
				final Class<?>[] paramTypes = { Class.class };
				entidad = classImpl.getConstructor(paramTypes).newInstance(clazz);
			} else {
				entidad = classImpl.newInstance();
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		return entidad;
	}

}
