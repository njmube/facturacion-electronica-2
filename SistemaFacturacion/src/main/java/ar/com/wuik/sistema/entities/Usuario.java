package ar.com.wuik.sistema.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import ar.com.wuik.swing.utils.WEncrypterUtil;


@Entity
@Table ( name = "usuarios" )
public class Usuario extends BaseEntity {


	@Column ( name = "DNI" )
	private String dni;
	@Column ( name = "PASSWORD" )
	private String password;
	@Column ( name = "NOMBRE" )
	private String nombre;
	@Column ( name = "ACTIVO" )
	private boolean activo;
	@ManyToMany ( fetch = FetchType.EAGER, cascade = CascadeType.ALL )
	@JoinTable ( name = "permisos_usuarios", joinColumns = { @JoinColumn ( name = "ID_USUARIO", nullable = false, updatable = false ) }, inverseJoinColumns = { @JoinColumn ( name = "ID_PERMISO", nullable = false, updatable = false ) } )
	private Set<Permiso> permisos;

	public String getDni() {
		return dni;
	}

	public void setDni( String dni ) {
		this.dni = dni;
	}

	public String getPassword() {
		return WEncrypterUtil.decrypt( password );
	}

	public void setPassword( String password ) {
		this.password = WEncrypterUtil.encrypt( password );
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre( String nombre ) {
		this.nombre = nombre;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo( boolean activo ) {
		this.activo = activo;
	}

	/**
	 * @return the permisos
	 */
	public Set<Permiso> getPermisos() {
		return permisos;
	}

	/**
	 * @param permisos
	 *            the permisos to set
	 */
	public void setPermisos( Set<Permiso> permisos ) {
		this.permisos = permisos;
	}

}
