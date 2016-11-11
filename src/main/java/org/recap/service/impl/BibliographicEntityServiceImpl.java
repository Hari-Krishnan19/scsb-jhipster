package org.recap.service.impl;

import org.recap.service.BibliographicEntityService;
import org.recap.domain.BibliographicEntity;
import org.recap.repository.BibliographicEntityRepository;
import org.recap.repository.search.BibliographicEntitySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing BibliographicEntity.
 */
@Service
@Transactional
public class BibliographicEntityServiceImpl implements BibliographicEntityService{

    private final Logger log = LoggerFactory.getLogger(BibliographicEntityServiceImpl.class);
    
    @Inject
    private BibliographicEntityRepository bibliographicEntityRepository;

    @Inject
    private BibliographicEntitySearchRepository bibliographicEntitySearchRepository;

    /**
     * Save a bibliographicEntity.
     *
     * @param bibliographicEntity the entity to save
     * @return the persisted entity
     */
    public BibliographicEntity save(BibliographicEntity bibliographicEntity) {
        log.debug("Request to save BibliographicEntity : {}", bibliographicEntity);
        BibliographicEntity result = bibliographicEntityRepository.save(bibliographicEntity);
        bibliographicEntitySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the bibliographicEntities.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<BibliographicEntity> findAll(Pageable pageable) {
        log.debug("Request to get all BibliographicEntities");
        Page<BibliographicEntity> result = bibliographicEntityRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one bibliographicEntity by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BibliographicEntity findOne(Long id) {
        log.debug("Request to get BibliographicEntity : {}", id);
        BibliographicEntity bibliographicEntity = bibliographicEntityRepository.findOneWithEagerRelationships(id);
        return bibliographicEntity;
    }

    /**
     *  Delete the  bibliographicEntity by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BibliographicEntity : {}", id);
        bibliographicEntityRepository.delete(id);
        bibliographicEntitySearchRepository.delete(id);
    }

    /**
     * Search for the bibliographicEntity corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BibliographicEntity> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BibliographicEntities for query {}", query);
        Page<BibliographicEntity> result = bibliographicEntitySearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
