package net.phoenixxe.shared.persistence;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.phoenixxe.shared.model.HasIdAndTitle;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Generic domain class, provides<br> 
 * * id (Long)<br>
 * * title (String)<br>
 * * creation date (Date)<br> 
 * * update date (Date)<br>
 * * realmId (Long)<br>
 * According to GenericDAOImpl realization each extending class<br>
 * has to provide following named queries:<br>
 * * find<b><i>Entity</i></b>ById<br>
 * * find<b><i>Entities</i></b>ByIds<br>
 * * findAll<b><i>Entities</i></b><br>
 * @author Arseniy Kaleshnyk, 08.04.2010, ver. 1.0
 */
@MappedSuperclass
public class PersistedEntity implements java.io.Serializable, 
		IsSerializable, HasIdAndTitle {
	private static final long serialVersionUID = 277653830211980388L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String title;

	@Temporal(TemporalType.TIMESTAMP)
	private Date created;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date updated;
	
	@Column(name = "realm", nullable = false)
	private Long realmId;

	public PersistedEntity() {}
	
	/**
	 * Default constructor.
	 * @param title
	 */
	public PersistedEntity(String title) {
		setTitle(title);
		created = new Date();
		updated = created;
	}

	/**
	 * Full clone constructor
	 * @param id
	 * @param title
	 * @param created
	 */
	public PersistedEntity(Long id, String title, Date created, Date updated, Long realmId) {
		this.id = (id != null ? new Long(id) : null);
		this.title = (title != null ? new String(title) : new String(""));
		
		this.created = (created != null ? (Date) created.clone() : new Date());
		this.updated = (updated != null ? (Date) updated.clone() : new Date());
		this.realmId = (realmId != null ? new Long(realmId) : null);
	}
	
	public PersistedEntity(PersistedEntity persistedEntity) {
		this.id = (persistedEntity.getId() != null ? new Long(persistedEntity.getId()) : null);
		this.title = (persistedEntity.getTitle() != null ? new String(persistedEntity.getTitle()) : new String(""));
		
		this.created = (persistedEntity.getCreated() != null ? (Date) persistedEntity.getCreated().clone() : new Date());
		this.updated = (persistedEntity.getUpdated() != null ? (Date) persistedEntity.getUpdated().clone() : new Date());
		this.realmId = (persistedEntity.getRealmId() != null ? new Long(persistedEntity.getRealmId()) : null);
		
	}
	
	public String toString() {
		return new StringBuilder("[Entity#").append(getId()).
				append(";title=").append(getTitle()).
				append(";created=").append(created).
				append(";updated=").append(updated).
				append(";realm=").append(realmId).
				append("]").toString();
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (!(object instanceof PersistedEntity)) return false;
		PersistedEntity that = (PersistedEntity) object;
		if (getId() != null) {
			if (!getId().equals(that.getId())) return false;
		} else {
			if (that.getId() != null) return false;
		}
		
		if (getTitle() != null) {
			if (!getTitle().equals(that.getTitle())) return false;
		} else {
			if (that.getTitle() != null) return false;
		}
		
		if (getRealmId() != null) {
			if (!getRealmId().equals(that.getRealmId())) return false;
		} else {
			if (that.getRealmId() != null) return false;
		}
		return true;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
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
	
	public void setRealmId(Long realmId) {
		this.realmId = realmId;
	}

	public Long getRealmId() {
		return realmId;
	}
}