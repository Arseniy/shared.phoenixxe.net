package net.phoenixxe.shared.persistence;

import java.util.ArrayList;

import java.util.Collection;
import java.util.List;


/**
 * Generic interface for DAOs.<br>
 * Defines generic functions for all descendants of PersistedEntity.
 * @author Arseniy Kaleshnyk, 14.04.2010, ver. 1.0
 */
public interface GenericDAO<E extends PersistedEntity> {
	/**
	 * Finds entity with specified id.<br>
	 * @param id id of entity to find
	 * @return found entity, null if no found
	 * @throws IllegalArgumentException if id is null or 0
	 */
	public E findById(Long id) throws IllegalArgumentException;
	
	/**
	 * Finds entities with specified ids.
	 * @param ids ids of entities to find
	 * @return list, containing found entities, size can be lower due to missing some entities
	 * @throws IllegalArgumentException if ids is null 
	 */
	// TODO: review PersistedEntity against E
	public ArrayList<E> find(List<Long> ids) throws IllegalArgumentException;

	public ArrayList<E> findAll();
	
	public ArrayList<Long> findRootIds();

	/**
	 * <p>
	 * Finds particular page of list of stored entities that contains only 
	 * entities from <strong>offset</strong> row to 
	 * <strong>offset + limit</strong> row. 
	 * </p>
	 *
	 * @param offset
	 * 				first row
	 * @param limit
	 * 				amount of result entities
	 * @return
	 * 				list of entities that are stored at rows from 
	 * 				<strong>offset</strong> row to 
	 * 				<strong>offset + limit</strong> row.  
	 * @throws IllegalArgumentException
	 * 				if passed offset or limit is negative
	 * @throws IndexOutOfBoundsException
	 * 				if there are no stored entities by passed offset and limit
	 * @throws DataException
	 * 				if any error occurred during access to data storage
	 */
	public List<E> findAll(int offset, int limit) 
			throws IllegalArgumentException, IndexOutOfBoundsException;
	
	public List<E> findByParentId(Long parentId) 
		throws IllegalArgumentException;

	/**
	 * <p>Retrieves amount of stored entities.</p>
	 *
	 * @return
	 *			amount of stored entities of certain type 			
	 * @throws DataException
	 * 			if any error during access to data storage occurred or 
	 * 			unexpected result received
	 */
	public long count();
	
	/**
	 * <p>Retrieves amount of stored entities in a given realm.</p>
	 *
	 * @return
	 *			amount of stored entities of certain type 			
	 * @throws DataException
	 * 			if any error during access to data storage occurred or 
	 * 			unexpected result received
	 */
	public long count(long realmId);
	
	/**
	 * <p>
	 * Persists passed entity. If entity doesn't contain realmId or realmId 
	 * isn't appropriated it's changed to appropriated realmId.
	 * </p>
	 *
	 * @param entity
	 * 			entity to be saved
	 * @return
	 * 			saved entity
	 * @throws DataException
	 * 			if any error during access to data storage occurred
	 * @throws IllegalArgumentException
	 * 			if passed entity is null 
	 */
	public E persist(E entity) throws IllegalArgumentException;

	/**
	 * <p>
	 * If passed entity wasn't stored, saves entity. If entity was stored 
	 * before, updates stored entity at the storage. Sets appropriated realmId
	 * to non-stored entity, and checks whether stored entity contains 
	 * appropriated realmId, if it doesn't - throws {@link RealmException}.  
	 * </p>
	 *
	 * @param entity
	 * 				entity to be saved/updated
	 * @return
	 * 				stored/updated entity
	 * @throws IllegalArgumentException
	 * 			if passed entity is null
	 * @throws DataException
	 * 			if any error during access to data storage occurred
	 * @throws {@link RealmException}
	 * 			if passed entity was stored before and contains inappropriate
	 * 			realmId  
	 */
	public E saveOrUpdate(E entity) throws IllegalArgumentException;

	/**
	 * <p>
	 * If passed entity wasn't stored, saves entity. If entity with the given title was stored 
	 * before, updates stored entity at the storage. Sets appropriated realmId
	 * to non-stored entity, and checks whether stored entity contains 
	 * appropriated realmId, if it doesn't - throws {@link RealmException}.  
	 * </p>
	 *
	 * @param entity
	 * 				entity to be saved/updated
	 * @return
	 * 				stored/updated entity
	 * @throws IllegalArgumentException
	 * 			if passed entity is null
	 * @throws DataException
	 * 			if any error during access to data storage occurred
	 * @throws {@link RealmException}
	 * 			if passed entity was stored before and contains inappropriate
	 * 			realmId  
	 */
	public E saveOrUpdateUnique(E entity) throws IllegalArgumentException;	
	
	/**
	 * <p>
	 * Saves/updates passed collection of entities. Sets appropriated realmId
	 * to non-stored entity, and checks whether stored entity contains 
	 * appropriated realmId, if it doesn't - throws {@link RealmException}.  
	 * </p>
	 *
	 * @param entities
	 * 				collection of entities to be saved/updated
	 * @return
	 * 				stored/updated entity
	 * @throws IllegalArgumentException
	 * 			if passed collection is null or contains null elements
	 * @throws DataException
	 * 			if any error during access to data storage occurred
	 * @throws {@link RealmException}
	 * 			if passed entity was stored before and contains inappropriate
	 * 			realmId  
	 */
	public void saveOrUpdateAll(Collection<E> entities) throws IllegalArgumentException;

	/**
	 * Deletes specified entity
	 * @param entity entity
	 * @throws IllegalArgumentException if entity is null or contains null id
	 * @throws DataException
	 * 			if any error during access to data storage occurred
	 * @throws {@link RealmException}
	 * 			if passed entity was stored before and contains inappropriate
	 * 			realmId
	 */
	public void delete(E entity) throws IllegalArgumentException;

	/**
	 * Deletes entity by id.
	 * @param id entity id
	 * @throws IllegalArgumentException if id is null or 0
	 * @throws DataException
	 * 			if any error during access to data storage occurred
	 * @throws {@link RealmException}
	 * 			if stored entity by passed id contains inappropriate
	 * 			realmId
	 */
	public void deleteById(Long id) throws IllegalArgumentException;

	/**
	 * Delete entities referred by ids in specified list.
	 * @param entities ids of entities to be deleted
	 * @throws IllegalArgumentException 
	 * 			if passed collection of entities is null or contains 
	 * 			null elements
	 */
	public void deleteAll(Collection<E> entities) throws IllegalArgumentException;
	

	public ArrayList<ArrayList<Long>> search(String searchValue);
}