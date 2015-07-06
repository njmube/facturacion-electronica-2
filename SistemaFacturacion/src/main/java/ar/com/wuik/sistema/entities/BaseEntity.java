package ar.com.wuik.sistema.entities;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public int hashCode() {
		return getId().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BaseEntity) {
			if (null != getId() && null != ((BaseEntity)obj).getId()) {
				return getId().equals(((BaseEntity)obj).getId());	
			} else {
				return super.equals(obj);
			}
			
		}
		return Boolean.FALSE;
	}

}
