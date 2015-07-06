package FEV1.dif.afip.gov.ar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

	public static void main(String[] args) throws Exception {

		// ServiceSoapProxy service = new ServiceSoapProxy();
		// FEAuthRequest auth = new FEAuthRequest();
		// auth.setCuit(20276229185L);
		// auth.setSign("FIT+U//yT53HJrff7+0lQE3YxKnDluSOFSo/gXS58Xmqp9RYoyOPMFnXTYGDvsftbV+7xi5rWkeYROkSt/YxeP9YjjwExkCbVPg9PG8/o/SZkN2BgcdvJeRPNhrYhNDnALscTgEj/4SoCV7p8BBhGtySG2C+IubVcoCAVFMKEZ8=");
		// auth.setToken("PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/Pgo8c3NvIHZlcnNpb249IjIuMCI+CiAgICA8aWQgdW5pcXVlX2lkPSIxNTA1NDE5NzMiIHNyYz0iQ049d3NhYWhvbW8sIE89QUZJUCwgQz1BUiwgU0VSSUFMTlVNQkVSPUNVSVQgMzM2OTM0NTAyMzkiIGdlbl90aW1lPSIxNDM0NzI3NzgxIiBleHBfdGltZT0iMTQzNDc3MTA0MSIgZHN0PSJDTj13c2ZlLCBPPUFGSVAsIEM9QVIiLz4KICAgIDxvcGVyYXRpb24gdmFsdWU9ImdyYW50ZWQiIHR5cGU9ImxvZ2luIj4KICAgICAgICA8bG9naW4gdWlkPSJDPWFyLCBPPW5lb3JpcywgU0VSSUFMTlVNQkVSPUNVSVQgMjAyNzYyMjkxODUsIENOPWRhbWlhbnBlcm9uIiBzZXJ2aWNlPSJ3c2ZlIiByZWdtZXRob2Q9IjIyIiBlbnRpdHk9IjMzNjkzNDUwMjM5IiBhdXRobWV0aG9kPSJjbXMiPgogICAgICAgICAgICA8cmVsYXRpb25zPgogICAgICAgICAgICAgICAgPHJlbGF0aW9uIHJlbHR5cGU9IjQiIGtleT0iMjAyNzYyMjkxODUiLz4KICAgICAgICAgICAgPC9yZWxhdGlvbnM+CiAgICAgICAgPC9sb2dpbj4KICAgIDwvb3BlcmF0aW9uPgo8L3Nzbz4KCg==");
		// MonedaResponse resopnse = service.FEParamGetTiposMonedas(auth);
		// Moneda[] monedas = resopnse.getResultGet();
		// for (Moneda moneda : monedas) {
		// System.out.println(moneda.getId());
		// }

		ServiceSoapProxy service = new ServiceSoapProxy();
		FECAERequest feCAEReq = getCAERequest();
		FEAuthRequest auth = new FEAuthRequest();
		auth.setCuit(20276229185L);
		auth.setToken("PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiIHN0YW5kYWxvbmU9InllcyI/Pgo8c3NvIHZlcnNpb249IjIuMCI+CiAgICA8aWQgdW5pcXVlX2lkPSIyMzEyMjU0NTc2IiBzcmM9IkNOPXdzYWFob21vLCBPPUFGSVAsIEM9QVIsIFNFUklBTE5VTUJFUj1DVUlUIDMzNjkzNDUwMjM5IiBnZW5fdGltZT0iMTQzNTkyNDYxOSIgZXhwX3RpbWU9IjE0MzU5Njc4NzkiIGRzdD0iQ049d3NmZSwgTz1BRklQLCBDPUFSIi8+CiAgICA8b3BlcmF0aW9uIHZhbHVlPSJncmFudGVkIiB0eXBlPSJsb2dpbiI+CiAgICAgICAgPGxvZ2luIHVpZD0iQz1hciwgTz1uZW9yaXMsIFNFUklBTE5VTUJFUj1DVUlUIDIwMjc2MjI5MTg1LCBDTj1kYW1pYW5wZXJvbiIgc2VydmljZT0id3NmZSIgcmVnbWV0aG9kPSIyMiIgZW50aXR5PSIzMzY5MzQ1MDIzOSIgYXV0aG1ldGhvZD0iY21zIj4KICAgICAgICAgICAgPHJlbGF0aW9ucz4KICAgICAgICAgICAgICAgIDxyZWxhdGlvbiByZWx0eXBlPSI0IiBrZXk9IjIwMjc2MjI5MTg1Ii8+CiAgICAgICAgICAgIDwvcmVsYXRpb25zPgogICAgICAgIDwvbG9naW4+CiAgICA8L29wZXJhdGlvbj4KPC9zc28+Cgo=");
		auth.setSign("pk7LXoQw9mcbOUEzMuvFixX9xuQ1DDBUILVplOBgUnvHZDSYtax5s2adQuq2wcAAjQ1Fr9/XQ4MnYkJgrtjPYwov+Oa5vkJBU1tjewq/aEIXJJiUK+2qSk63jtT0xiY96n7umxziYSWZLfd9dph0/fFvgoCIZNX2RCA3vJSg3gA=");

		FECAEResponse resopnse = service.FECAESolicitar(auth, feCAEReq);

		Err[] errors = resopnse.getErrors();
		if (null != errors) {
			for (Err err : errors) {
				System.out.println(err.getMsg());
			}
		}
		Obs[] obs = resopnse.getFeDetResp()[0].getObservaciones();
		if (null != obs) {
			for (Obs obs2 : obs) {
				System.out.println(obs2.getMsg());
			}
		}
		// Moneda[] monedas = resopnse.get
		// for (Moneda moneda : monedas) {
		// System.out.println(moneda.getId());
		// }
	}

	private static FECAERequest getCAERequest() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		FECAERequest req = new FECAERequest();
		FECAECabRequest feCabReq = new FECAECabRequest();
		feCabReq.setCantReg(1);
		feCabReq.setCbteTipo(4);
		// feCabReq.setCbteTipo(8);
		feCabReq.setPtoVta(13);
		req.setFeCabReq(feCabReq);
		FECAEDetRequest[] feDetReq = new FECAEDetRequest[1];
		feDetReq[0] = new FECAEDetRequest();
		FECAEDetRequest feDetReq1 = feDetReq[0];
		feDetReq1.setCbteDesde(1);
		feDetReq1.setCbteHasta(1);
		feDetReq1.setCbteFch(format.format(new Date()));
		feDetReq1.setConcepto(1);
		feDetReq1.setDocNro(30712144234L);
		feDetReq1.setDocTipo(80);
		// feDetReq1.setFchServDesde("20150615");
		// feDetReq1.setFchServHasta("20150615");
		// feDetReq1.setFchVtoPago("20150620");
		feDetReq1.setImpIVA(209.79);
		feDetReq1.setImpNeto(999);
		feDetReq1.setImpTotal(1208.79);
		// feDetReq1.setImpTotConc(100);
		AlicIva[] ivas = new AlicIva[1];
		ivas[0] = new AlicIva();
		ivas[0].setBaseImp(999);
		ivas[0].setImporte(209.79);
		ivas[0].setId(5);
		feDetReq1.setIva(ivas);
		feDetReq1.setMonId("PES");
		feDetReq1.setMonCotiz(1);
		req.setFeDetReq(feDetReq);
		return req;
	}

}
