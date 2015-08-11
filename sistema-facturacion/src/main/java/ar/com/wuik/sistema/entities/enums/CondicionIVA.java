package ar.com.wuik.sistema.entities.enums;

public enum CondicionIVA {

	RESP_INSC(1, "RESPONSABLE INSCRIPTO", "RESP. INSC", TipoLetraComprobante.A), CONS_FINAL(
			2, "CONSUMIDOR FINAL", "CONS. FINAL", TipoLetraComprobante.B), EXENTO(
			3, "EXENTO", "EXENTO", TipoLetraComprobante.B), MONOTRIBUTISTA(4,
			"MONOTRIBUTISTA", "MONOTRIBUTISTA", TipoLetraComprobante.B), RESP_NO_INSC(
			5, "RESPONSABLE NO INSCRIPTO", "RESP. NO INSC.",
			TipoLetraComprobante.B);

	private String denominacion;
	private String abreviacion;
	private TipoLetraComprobante tipoLetraComprobante;
	private int id;

	private CondicionIVA(int id, String denominacion, String abreviacion,
			TipoLetraComprobante tipoLetraComprobante) {

		this.denominacion = denominacion;
		this.abreviacion = abreviacion;
		this.tipoLetraComprobante = tipoLetraComprobante;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getDenominacion() {
		return denominacion;
	}

	public String getAbreviacion() {
		return abreviacion;
	}

	public TipoLetraComprobante getTipoLetraComprobante() {
		return tipoLetraComprobante;
	}

	public static CondicionIVA fromValue(int value) {
		switch (value) {
		case 1:
			return RESP_INSC;
		case 2:
			return CONS_FINAL;
		case 3:
			return EXENTO;
		case 4:
			return MONOTRIBUTISTA;
		case 5:
			return RESP_NO_INSC;
		}
		return null;
	}

}
