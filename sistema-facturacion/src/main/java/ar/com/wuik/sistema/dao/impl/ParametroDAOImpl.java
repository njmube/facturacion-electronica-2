package ar.com.wuik.sistema.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;

import ar.com.wuik.sistema.dao.ParametroDAO;
import ar.com.wuik.sistema.entities.Parametro;
import ar.com.wuik.sistema.entities.enums.TipoComprobante;
import ar.com.wuik.sistema.entities.enums.TipoLetraComprobante;
import ar.com.wuik.sistema.exceptions.DataAccessException;

public class ParametroDAOImpl extends GenericCrudHBDAOImpl<Parametro> implements
		ParametroDAO {

	public ParametroDAOImpl() {
		super(Parametro.class);
	}

	@Override
	public void incrementarNroRecibo() throws DataAccessException {
		try {
			Parametro parametro = getById(1L);
			Long nroRecibo = parametro.getNroRecibo();
			parametro.setNroRecibo(nroRecibo + 1);
			getSession().saveOrUpdate(parametro);
		} catch (HibernateException hexc) {
			throw new DataAccessException(hexc);
		}
	}

	@Override
	public void incrementarNroRemito() throws DataAccessException {
		try {
			Parametro parametro = getById(1L);
			Long nroRemito = parametro.getNroRemito();
			parametro.setNroRemito(nroRemito + 1);
			getSession().saveOrUpdate(parametro);
		} catch (HibernateException hexc) {
			throw new DataAccessException(hexc);
		}
	}

	@Override
	public Parametro getParametro() throws DataAccessException {
		try {
			return getById(1L);
		} catch (HibernateException hexc) {
			throw new DataAccessException(hexc);
		}
	}

	@Override
	public void incrementarNroComprobante(TipoComprobante tipoComprobante,
			TipoLetraComprobante tipoLetraComprobante)
			throws DataAccessException {
		try {
			Parametro parametro = getById(1L);

			switch (tipoComprobante) {
			case FACTURA:
				if (tipoLetraComprobante.equals(TipoLetraComprobante.A)) {
					parametro.setNroFacturaA(parametro.getNroFacturaA() + 1);
				} else {
					parametro.setNroFacturaB(parametro.getNroFacturaB() + 1);
				}
				break;
			case NOTA_CREDITO:
				if (tipoLetraComprobante.equals(TipoLetraComprobante.A)) {
					parametro
							.setNroNotaCreditoA(parametro.getNroNotaCreditoA() + 1);
				} else {
					parametro
							.setNroNotaCreditoB(parametro.getNroNotaCreditoB() + 1);
				}
				break;
			case NOTA_DEBITO:
				if (tipoLetraComprobante.equals(TipoLetraComprobante.A)) {
					parametro
							.setNroNotaDebitoA(parametro.getNroNotaDebitoA() + 1);
				} else {
					parametro
							.setNroNotaDebitoB(parametro.getNroNotaDebitoB() + 1);
				}
				break;
			}
			getSession().saveOrUpdate(parametro);
		} catch (HibernateException hexc) {
			throw new DataAccessException(hexc);
		}
	}

	@Override
	public String obtenerNroComprobante(TipoComprobante tipoComprobante,
			TipoLetraComprobante tipoLetraComprobante)
			throws DataAccessException {
		try {
			Parametro parametro = getById(1L);

			switch (tipoComprobante) {
			case FACTURA:
				if (tipoLetraComprobante.equals(TipoLetraComprobante.A)) {
					return StringUtils.leftPad(parametro.getNroFacturaA() + "",
							8, "0");
				} else {
					return StringUtils.leftPad(parametro.getNroFacturaB() + "",
							8, "0");
				}
			case NOTA_CREDITO:
				if (tipoLetraComprobante.equals(TipoLetraComprobante.A)) {
					return StringUtils.leftPad(parametro.getNroNotaCreditoA()
							+ "", 8, "0");
				} else {
					return StringUtils.leftPad(parametro.getNroNotaCreditoB()
							+ "", 8, "0");
				}
			case NOTA_DEBITO:
				if (tipoLetraComprobante.equals(TipoLetraComprobante.A)) {
					return StringUtils.leftPad(parametro.getNroNotaDebitoA()
							+ "", 8, "0");
				} else {
					return StringUtils.leftPad(parametro.getNroNotaDebitoB()
							+ "", 8, "0");
				}
			}
			return null;
		} catch (HibernateException hexc) {
			throw new DataAccessException(hexc);
		}
	}

}
