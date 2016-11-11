package org.recap.service.impl;

import org.recap.service.ItemEntityService;
import org.recap.domain.ItemEntity;
import org.recap.repository.ItemEntityRepository;
import org.recap.repository.search.ItemEntitySearchRepository;
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
 * Service Implementation for managing ItemEntity.
 */
@Service
@Transactional
public class ItemEntityServiceImpl implements ItemEntityService{

    private final Logger log = LoggerFactory.getLogger(ItemEntityServiceImpl.class);
    
    @Inject
    private ItemEntityRepository itemEntityRepository;

    @Inject
    private ItemEntitySearchRepository itemEntitySearchRepository;

    /**
     * Save a itemEntity.
     *
     * @param itemEntity the entity to save
     * @return the persisted entity
     */
    public ItemEntity save(ItemEntity itemEntity) {
        log.debug("Request to save ItemEntity : {}", itemEntity);
        ItemEntity result = itemEntityRepository.save(itemEntity);
        itemEntitySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the itemEntities.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ItemEntity> findAll(Pageable pageable) {
        log.debug("Request to get all ItemEntities");
        Page<ItemEntity> result = itemEntityRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one itemEntity by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ItemEntity findOne(Long id) {
        log.debug("Request to get ItemEntity : {}", id);
        ItemEntity itemEntity = itemEntityRepository.findOne(id);
        return itemEntity;
    }

    /**
     *  Delete the  itemEntity by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ItemEntity : {}", id);
        itemEntityRepository.delete(id);
        itemEntitySearchRepository.delete(id);
    }

    /**
     * Search for the itemEntity corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ItemEntity> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ItemEntities for query {}", query);
        Page<ItemEntity> result = itemEntitySearchRepository.search(queryStringQuery(query), pageable);
        return result;
    }
}
