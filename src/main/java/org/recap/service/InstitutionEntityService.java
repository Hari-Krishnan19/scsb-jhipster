package org.recap.service;

import org.recap.domain.InstitutionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing InstitutionEntity.
 */
public interface InstitutionEntityService {

    /**
     * Save a institutionEntity.
     *
     * @param institutionEntity the entity to save
     * @return the persisted entity
     */
    InstitutionEntity save(InstitutionEntity institutionEntity);

    /**
     *  Get all the institutionEntities.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<InstitutionEntity> findAll(Pageable pageable);

    /**
     *  Get the "id" institutionEntity.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    InstitutionEntity findOne(Long id);

    /**
     *  Delete the "id" institutionEntity.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the institutionEntity corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<InstitutionEntity> search(String query, Pageable pageable);
}
