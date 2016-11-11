package org.recap.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.recap.domain.ItemEntity;
import org.recap.service.ItemEntityService;
import org.recap.web.rest.util.HeaderUtil;
import org.recap.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ItemEntity.
 */
@RestController
@RequestMapping("/api")
public class ItemEntityResource {

    private final Logger log = LoggerFactory.getLogger(ItemEntityResource.class);
        
    @Inject
    private ItemEntityService itemEntityService;

    /**
     * POST  /item-entities : Create a new itemEntity.
     *
     * @param itemEntity the itemEntity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new itemEntity, or with status 400 (Bad Request) if the itemEntity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/item-entities",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemEntity> createItemEntity(@Valid @RequestBody ItemEntity itemEntity) throws URISyntaxException {
        log.debug("REST request to save ItemEntity : {}", itemEntity);
        if (itemEntity.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("itemEntity", "idexists", "A new itemEntity cannot already have an ID")).body(null);
        }
        ItemEntity result = itemEntityService.save(itemEntity);
        return ResponseEntity.created(new URI("/api/item-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("itemEntity", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /item-entities : Updates an existing itemEntity.
     *
     * @param itemEntity the itemEntity to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated itemEntity,
     * or with status 400 (Bad Request) if the itemEntity is not valid,
     * or with status 500 (Internal Server Error) if the itemEntity couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/item-entities",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemEntity> updateItemEntity(@Valid @RequestBody ItemEntity itemEntity) throws URISyntaxException {
        log.debug("REST request to update ItemEntity : {}", itemEntity);
        if (itemEntity.getId() == null) {
            return createItemEntity(itemEntity);
        }
        ItemEntity result = itemEntityService.save(itemEntity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("itemEntity", itemEntity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /item-entities : get all the itemEntities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of itemEntities in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/item-entities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ItemEntity>> getAllItemEntities(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ItemEntities");
        Page<ItemEntity> page = itemEntityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/item-entities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /item-entities/:id : get the "id" itemEntity.
     *
     * @param id the id of the itemEntity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the itemEntity, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/item-entities/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ItemEntity> getItemEntity(@PathVariable Long id) {
        log.debug("REST request to get ItemEntity : {}", id);
        ItemEntity itemEntity = itemEntityService.findOne(id);
        return Optional.ofNullable(itemEntity)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /item-entities/:id : delete the "id" itemEntity.
     *
     * @param id the id of the itemEntity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/item-entities/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteItemEntity(@PathVariable Long id) {
        log.debug("REST request to delete ItemEntity : {}", id);
        itemEntityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("itemEntity", id.toString())).build();
    }

    /**
     * SEARCH  /_search/item-entities?query=:query : search for the itemEntity corresponding
     * to the query.
     *
     * @param query the query of the itemEntity search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/item-entities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ItemEntity>> searchItemEntities(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of ItemEntities for query {}", query);
        Page<ItemEntity> page = itemEntityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/item-entities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
