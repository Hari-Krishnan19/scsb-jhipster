package org.recap.service.impl;

import org.recap.service.InstitutionEntityService;
import org.recap.domain.InstitutionEntity;
import org.recap.repository.InstitutionEntityRepository;
import org.recap.repository.search.InstitutionEntitySearchRepository;
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
 * Service Implementation for managing InstitutionEntity.
 */
@Service
@Transactional
public class InstitutionEntityServiceImpl implements InstitutionEntityService{

    private final Logger log = LoggerFactory.getLogger(InstitutionEntityServiceImpl.class);
    
    @Inject
    private InstitutionEntityRepository institutionEntityRepository;

    @Inject
    private InstitutionEntitySearchRepository institutionEntitySearchRepository;

    /**
     * Save a institutionEntity.
     *
     * @param institutionEntity the entity to save
     * @return the persisted entity
     */
    public InstitutionEntity save(InstitutionEntity institutionEntity) {
        log.debug("Request to save InstitutionEntity : {}", institutionEntity);
        InstitutionEntity result = institutionEntityRepository.save(institutionEntity);
        institutionEntitySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the institutionEntities.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<InstitutionEntity> findAll(Pageable pageable) {
        log.debug("Request to get all InstitutionEntities");
        Page<InstitutionEntity> result = institutionEntityRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one institutionEntity by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public InstitutionEntity findOne(Long id) {
        log.debug("Request to get InstitutionEntity : {}", id);
        InstitutionEntity institutionEntity = institutionEntityRepository.findOne(id);
        return institutionEntity;
    }

    /**
     *  Delete the  institutionEntity by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete InstitutionEntity : {}", id);
        institutionEntityRepository.delete(id);
        institutionEntitySearchRepository.delete(id);
    }

    /**
     * Search for the institutionEntity corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<InstitutionEntity> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of InstitutionEntities for query {}", query);
        Page<InstitutionEntity> result = institutionEntitySearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
