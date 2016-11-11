package org.recap.service.impl;

import org.recap.service.HoldingsEntityService;
import org.recap.domain.HoldingsEntity;
import org.recap.repository.HoldingsEntityRepository;
import org.recap.repository.search.HoldingsEntitySearchRepository;
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
 * Service Implementation for managing HoldingsEntity.
 */
@Service
@Transactional
public class HoldingsEntityServiceImpl implements HoldingsEntityService{

    private final Logger log = LoggerFactory.getLogger(HoldingsEntityServiceImpl.class);
    
    @Inject
    private HoldingsEntityRepository holdingsEntityRepository;

    @Inject
    private HoldingsEntitySearchRepository holdingsEntitySearchRepository;

    /**
     * Save a holdingsEntity.
     *
     * @param holdingsEntity the entity to save
     * @return the persisted entity
     */
    public HoldingsEntity save(HoldingsEntity holdingsEntity) {
        log.debug("Request to save HoldingsEntity : {}", holdingsEntity);
        HoldingsEntity result = holdingsEntityRepository.save(holdingsEntity);
        holdingsEntitySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the holdingsEntities.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<HoldingsEntity> findAll(Pageable pageable) {
        log.debug("Request to get all HoldingsEntities");
        Page<HoldingsEntity> result = holdingsEntityRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one holdingsEntity by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public HoldingsEntity findOne(Long id) {
        log.debug("Request to get HoldingsEntity : {}", id);
        HoldingsEntity holdingsEntity = holdingsEntityRepository.findOneWithEagerRelationships(id);
        return holdingsEntity;
    }

    /**
     *  Delete the  holdingsEntity by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete HoldingsEntity : {}", id);
        holdingsEntityRepository.delete(id);
        holdingsEntitySearchRepository.delete(id);
    }

    /**
     * Search for the holdingsEntity corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HoldingsEntity> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of HoldingsEntities for query {}", query);
        Page<HoldingsEntity> result = holdingsEntitySearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
