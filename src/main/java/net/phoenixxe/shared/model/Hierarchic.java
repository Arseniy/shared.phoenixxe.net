package net.phoenixxe.shared.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface for entities with parent and/or children. 
 * @author Arseniy Kaleshnyk, 08.04.2010, ver. 1.0
 */
public interface Hierarchic extends HasIdAndTitle {
	Long getParentId();
	void setParentId(Long parentId);
	
	ArrayList<Long> getChildrenIds();
	void setChildrenIds(List<Long> childrenIds);
}