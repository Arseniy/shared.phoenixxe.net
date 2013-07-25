package net.phoenixxe.shared.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import net.phoenixxe.shared.model.Hierarchic;
import net.phoenixxe.shared.persistence.PersistedEntity;

/**
 * Generic domain class.
 * @author Arseniy Kaleshnyk, 25.05.2010, ver. 1.0
 */
@MappedSuperclass
public abstract class HierarchicPersistedEntity extends PersistedEntity 
		implements Hierarchic {
	private static final long serialVersionUID = -5008457742765625115L;

	@Column(name = "parent_id")
	private Long parentId;
	
	@Column(name = "children_ids")
	private ArrayList<Long> childrenIds;
	
	public HierarchicPersistedEntity() {
		super();
		childrenIds = new ArrayList<Long>();
	}
	
	/**
	 * Default constructor.
	 * @param title
	 */
	public HierarchicPersistedEntity(String title) {
		super(title);
		childrenIds = new ArrayList<Long>();
	}

	/**
	 * Full constructor
	 * @param id
	 * @param title
	 * @param created
	 */
	public HierarchicPersistedEntity(Long id, String title, Date created, Date updated,
			Long parentId, ArrayList<Long> childrenIds, Long realmId) {
		super(id, title, created, updated, realmId);
		this.parentId = (parentId != null ? new Long(parentId) : null);
		setChildrenIds(childrenIds);
	}
	
	public HierarchicPersistedEntity(HierarchicPersistedEntity entity) {
		super(entity);
		this.parentId = entity.getId();
		this.childrenIds = entity.getChildrenIds();
	}
	
	public String toString() {
		return new StringBuilder("[Entity#").append(getId()).
				append(";title=").append(getTitle()).
				append(";created=").append(getCreated()).
				append(";parentId=").append(parentId).
				append(";realm=").append(getRealmId()).
				append("]").toString();
	}
	
	@Override
	public boolean equals(Object object) {
		if (this == object) return true;
		if (!(object instanceof HierarchicPersistedEntity) || !super.equals(object)) return false;
		HierarchicPersistedEntity that = (HierarchicPersistedEntity) object;
		if (getParentId() != null) {
			if (!getParentId().equals(that.getParentId())) return false;
		} else {
			if (that.getParentId() != null) return false;
		}
		// #142

		return true;
	}

	@Override
	public Long getParentId() {
		return parentId;
	}

	@Override
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Override
	public ArrayList<Long> getChildrenIds() {
		// TODO: Arseniy, review : hotfix, remove later
		if (childrenIds == null) setChildrenIds(null); 
		return childrenIds;
	}

	@Override
	public void setChildrenIds(List<Long> childrenIds) {
		if (childrenIds != null) {
			this.childrenIds = new ArrayList<Long>(childrenIds.size());
			for (Long childId : childrenIds) {
				if (childId == null) continue;
				this.childrenIds.add(new Long(childId));
			}
		} else {
			this.childrenIds = new ArrayList<Long>();
		}
	}
}
