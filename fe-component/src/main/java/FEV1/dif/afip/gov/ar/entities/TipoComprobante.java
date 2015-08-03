package FEV1.dif.afip.gov.ar.entities;

public class TipoComprobante {

	private int id;
	private String desc;
	
	public TipoComprobante(int id,String desc) {
		this.id = id;
		this.desc = desc;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
