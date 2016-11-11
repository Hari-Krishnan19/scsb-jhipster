package org.recap.service;

import org.recap.domain.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing ItemEntity.
 */
public interface ItemEntityService {

    /**
     * Save a itemEntity.
     *
     * @param itemEntity the entity to save
     * @return the persisted entity
     */
    ItemEntity save(ItemEntity itemEntity);

    /**
     *  Get all the itemEntities.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ItemEntity> findAll(Pageable pageable);

    /**
     *  Get the "id" itemEntity.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ItemEntity findOne(Long id);

    /**
     *  Delete the "id" itemEntity.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the itemEntity corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ItemEntity> search(String query, Pageable pageable);
}
