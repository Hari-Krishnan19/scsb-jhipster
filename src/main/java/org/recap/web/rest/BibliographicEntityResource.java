package org.recap.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.recap.domain.BibliographicEntity;
import org.recap.service.BibliographicEntityService;
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
 * REST controller for managing BibliographicEntity.
 */
@RestController
@RequestMapping("/api")
public class BibliographicEntityResource {

    private final Logger log = LoggerFactory.getLogger(BibliographicEntityResource.class);
        
    @Inject
    private BibliographicEntityService bibliographicEntityService;

    /**
     * POST  /bibliographic-entities : Create a new bibliographicEntity.
     *
     * @param bibliographicEntity the bibliographicEntity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new bibliographicEntity, or with status 400 (Bad Request) if the bibliographicEntity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bibliographic-entities",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BibliographicEntity> createBibliographicEntity(@Valid @RequestBody BibliographicEntity bibliographicEntity) throws URISyntaxException {
        log.debug("REST request to save BibliographicEntity : {}", bibliographicEntity);
        if (bibliographicEntity.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("bibliographicEntity", "idexists", "A new bibliographicEntity cannot already have an ID")).body(null);
        }
        BibliographicEntity result = bibliographicEntityService.save(bibliographicEntity);
        return ResponseEntity.created(new URI("/api/bibliographic-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("bibliographicEntity", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bibliographic-entities : Updates an existing bibliographicEntity.
     *
     * @param bibliographicEntity the bibliographicEntity to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated bibliographicEntity,
     * or with status 400 (Bad Request) if the bibliographicEntity is not valid,
     * or with status 500 (Internal Server Error) if the bibliographicEntity couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bibliographic-entities",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BibliographicEntity> updateBibliographicEntity(@Valid @RequestBody BibliographicEntity bibliographicEntity) throws URISyntaxException {
        log.debug("REST request to update BibliographicEntity : {}", bibliographicEntity);
        if (bibliographicEntity.getId() == null) {
            return createBibliographicEntity(bibliographicEntity);
        }
        BibliographicEntity result = bibliographicEntityService.save(bibliographicEntity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("bibliographicEntity", bibliographicEntity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bibliographic-entities : get all the bibliographicEntities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bibliographicEntities in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/bibliographic-entities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BibliographicEntity>> getAllBibliographicEntities(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of BibliographicEntities");
        Page<BibliographicEntity> page = bibliographicEntityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bibliographic-entities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bibliographic-entities/:id : get the "id" bibliographicEntity.
     *
     * @param id the id of the bibliographicEntity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the bibliographicEntity, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/bibliographic-entities/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BibliographicEntity> getBibliographicEntity(@PathVariable Long id) {
        log.debug("REST request to get BibliographicEntity : {}", id);
        BibliographicEntity bibliographicEntity = bibliographicEntityService.findOne(id);
        return Optional.ofNullable(bibliographicEntity)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bibliographic-entities/:id : delete the "id" bibliographicEntity.
     *
     * @param id the id of the bibliographicEntity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/bibliographic-entities/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBibliographicEntity(@PathVariable Long id) {
        log.debug("REST request to delete BibliographicEntity : {}", id);
        bibliographicEntityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("bibliographicEntity", id.toString())).build();
    }

    /**
     * SEARCH  /_search/bibliographic-entities?query=:query : search for the bibliographicEntity corresponding
     * to the query.
     *
     * @param query the query of the bibliographicEntity search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/bibliographic-entities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BibliographicEntity>> searchBibliographicEntities(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of BibliographicEntities for query {}", query);
        Page<BibliographicEntity> page = bibliographicEntityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/bibliographic-entities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
