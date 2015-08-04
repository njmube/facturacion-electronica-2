/**
 * Autor : juan.vazquez@wuik.com.ar - Wuik-Working Innovation Creacion :
 * 10/03/2014 - 17:11:24
 */
package ar.com.wuik.sistema.entities;

/**
 * @author juan.vazquez@wuik.com.ar - Wuik-Working Innovation
 */
public enum Permisos {

	ING_ART("ING-ART"), NUE_ART("NUE-ART"), EDI_ART("EDI-ART"), ELI_ART("ELI-ART"), CAR_ART("CAR-ART"), ING_CLI(
	                "ING-CLI"), NUE_CLI("NUE-CLI"), EDI_CLI("EDI-CLI"), ELI_CLI("ELI-CLI"), ING_CTE("ING-CTE"), NUE_CTE(
	                "NUE-CTE"), EDI_CTE("EDI-CTE"), ELI_CTE("ELI-CTE"), ING_MOV("ING-MOV"), NUE_MOV("NUE-MOV"), EDI_MOV(
	                "EDI-MOV"), ELI_MOV("ELI-MOV"), ING_USU("ING-USU"), NUE_USU("NUE-USU"), EDI_USU("EDI-USU"), ELI_USU(
	                "ELI-USU"), ING_VTA("ING-VTA"), NUE_VTA("NUE-VTA"), EDI_VTA("EDI-VTA"), ELI_VTA("ELI-VTA"), REP_STO(
	                "REP-STO"), REP_CTA("REP-CTA"), REP_CAJ("REP-CAJ"), REP_MOV("REP-MOV");


	private String codPermiso;

	private Permisos(String codPermiso) {
		this.codPermiso = codPermiso;
	}

	/**
	 * @return the codPermiso
	 */
	public String getCodPermiso() {
		return codPermiso;
	}

}
