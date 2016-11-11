package org.recap.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.recap.domain.HoldingsEntity;
import org.recap.service.HoldingsEntityService;
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
 * REST controller for managing HoldingsEntity.
 */
@RestController
@RequestMapping("/api")
public class HoldingsEntityResource {

    private final Logger log = LoggerFactory.getLogger(HoldingsEntityResource.class);
        
    @Inject
    private HoldingsEntityService holdingsEntityService;

    /**
     * POST  /holdings-entities : Create a new holdingsEntity.
     *
     * @param holdingsEntity the holdingsEntity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new holdingsEntity, or with status 400 (Bad Request) if the holdingsEntity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/holdings-entities",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HoldingsEntity> createHoldingsEntity(@Valid @RequestBody HoldingsEntity holdingsEntity) throws URISyntaxException {
        log.debug("REST request to save HoldingsEntity : {}", holdingsEntity);
        if (holdingsEntity.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("holdingsEntity", "idexists", "A new holdingsEntity cannot already have an ID")).body(null);
        }
        HoldingsEntity result = holdingsEntityService.save(holdingsEntity);
        return ResponseEntity.created(new URI("/api/holdings-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("holdingsEntity", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /holdings-entities : Updates an existing holdingsEntity.
     *
     * @param holdingsEntity the holdingsEntity to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated holdingsEntity,
     * or with status 400 (Bad Request) if the holdingsEntity is not valid,
     * or with status 500 (Internal Server Error) if the holdingsEntity couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/holdings-entities",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HoldingsEntity> updateHoldingsEntity(@Valid @RequestBody HoldingsEntity holdingsEntity) throws URISyntaxException {
        log.debug("REST request to update HoldingsEntity : {}", holdingsEntity);
        if (holdingsEntity.getId() == null) {
            return createHoldingsEntity(holdingsEntity);
        }
        HoldingsEntity result = holdingsEntityService.save(holdingsEntity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("holdingsEntity", holdingsEntity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /holdings-entities : get all the holdingsEntities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of holdingsEntities in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/holdings-entities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<HoldingsEntity>> getAllHoldingsEntities(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of HoldingsEntities");
        Page<HoldingsEntity> page = holdingsEntityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/holdings-entities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /holdings-entities/:id : get the "id" holdingsEntity.
     *
     * @param id the id of the holdingsEntity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the holdingsEntity, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/holdings-entities/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<HoldingsEntity> getHoldingsEntity(@PathVariable Long id) {
        log.debug("REST request to get HoldingsEntity : {}", id);
        HoldingsEntity holdingsEntity = holdingsEntityService.findOne(id);
        return Optional.ofNullable(holdingsEntity)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /holdings-entities/:id : delete the "id" holdingsEntity.
     *
     * @param id the id of the holdingsEntity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/holdings-entities/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHoldingsEntity(@PathVariable Long id) {
        log.debug("REST request to delete HoldingsEntity : {}", id);
        holdingsEntityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("holdingsEntity", id.toString())).build();
    }

    /**
     * SEARCH  /_search/holdings-entities?query=:query : search for the holdingsEntity corresponding
     * to the query.
     *
     * @param query the query of the holdingsEntity search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/holdings-entities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<HoldingsEntity>> searchHoldingsEntities(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of HoldingsEntities for query {}", query);
        Page<HoldingsEntity> page = holdingsEntityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/holdings-entities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
