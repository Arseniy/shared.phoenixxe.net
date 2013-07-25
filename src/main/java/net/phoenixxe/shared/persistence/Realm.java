package net.phoenixxe.shared.persistence;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.phoenixxe.shared.model.HasIdAndTitle;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Realm.
 * @author Arseniy Kaleshnyk, 04.06.2010, ver. 1.0
 */
@Entity
@Table(name = "realms")
@NamedQueries({
	@NamedQuery(name="findRealmById",
			query="from Realm where id = :pId"),
	@NamedQuery(name="findAllRealms",
			query="from Realm"),
			
	@NamedQuery(name="findRealmByDomain",
			query="from Realm where domain = :pDomain"),
	@NamedQuery(name="findRealmsByStatus",
			query="from Realm where enabled = :pStatus")
})
public class Realm implements java.io.Serializable, IsSerializable, HasIdAndTitle {
	private static final long serialVersionUID = 2415751464045494544L;

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated;
	
	private String domain;
	private boolean enabled;
	
	@Deprecated
	public Realm() {}
	
	/**
	 * Default constructor.
	 * @param domain domain
	 * @param enabled enabled
	 */
	public Realm(String domain, boolean enabled) {
		this.domain = domain;
		this.enabled = enabled;
		created = new Date();
		updated = created;
	}

	public Realm(Realm realm) {
		this.id = realm.getId();
		this.domain = realm.getDomain();
		this.created = realm.getCreated();
		this.updated = realm.getUpdated();
		this.enabled = realm.isEnabled();
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setTitle(String title) {
		domain = title;
	}

	public String getTitle() {
		return domain;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getCreated() {
		return created;
	}
	
	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getDomain() {
		return domain;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}
	
	public String toString() {
		return new StringBuilder("[Realm#").append(getId()).
				append(";domain=").append(domain).
				append(";enabled=").append(enabled).
				append("]").toString();
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (!super.equals(object) && !(object instanceof Realm)) return false;
		Realm that = (Realm) object;
		if ((getId() != null) && !getId().equals(that.getId())) return false;
		if ((getDomain() != null) && !getDomain().equals(that.getDomain())) return false;
		if (isEnabled() != that.isEnabled()) return false;
		return true;
	}
}
