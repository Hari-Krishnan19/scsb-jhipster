package org.recap.service;

import org.recap.domain.HoldingsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing HoldingsEntity.
 */
public interface HoldingsEntityService {

    /**
     * Save a holdingsEntity.
     *
     * @param holdingsEntity the entity to save
     * @return the persisted entity
     */
    HoldingsEntity save(HoldingsEntity holdingsEntity);

    /**
     *  Get all the holdingsEntities.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<HoldingsEntity> findAll(Pageable pageable);

    /**
     *  Get the "id" holdingsEntity.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    HoldingsEntity findOne(Long id);

    /**
     *  Delete the "id" holdingsEntity.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the holdingsEntity corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<HoldingsEntity> search(String query, Pageable pageable);
}
