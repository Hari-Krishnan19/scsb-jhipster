package org.recap.service;

import org.recap.domain.BibliographicEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing BibliographicEntity.
 */
public interface BibliographicEntityService {

    /**
     * Save a bibliographicEntity.
     *
     * @param bibliographicEntity the entity to save
     * @return the persisted entity
     */
    BibliographicEntity save(BibliographicEntity bibliographicEntity);

    /**
     *  Get all the bibliographicEntities.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BibliographicEntity> findAll(Pageable pageable);

    /**
     *  Get the "id" bibliographicEntity.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    BibliographicEntity findOne(Long id);

    /**
     *  Delete the "id" bibliographicEntity.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the bibliographicEntity corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<BibliographicEntity> search(String query, Pageable pageable);
}
