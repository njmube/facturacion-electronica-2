package FEV1.dif.afip.gov.ar.entities;

public final class Authorization {

	private String token;
	private String sign;

	public Authorization(String token, String sign) {
		this.token = token;
		this.sign = sign;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		return "TOKEN[" + token + "] SIGN[" + sign + "]";
	}
}
