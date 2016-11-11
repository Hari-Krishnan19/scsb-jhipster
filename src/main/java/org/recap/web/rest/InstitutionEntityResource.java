package org.recap.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.recap.domain.InstitutionEntity;
import org.recap.service.InstitutionEntityService;
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
 * REST controller for managing InstitutionEntity.
 */
@RestController
@RequestMapping("/api")
public class InstitutionEntityResource {

    private final Logger log = LoggerFactory.getLogger(InstitutionEntityResource.class);
        
    @Inject
    private InstitutionEntityService institutionEntityService;

    /**
     * POST  /institution-entities : Create a new institutionEntity.
     *
     * @param institutionEntity the institutionEntity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new institutionEntity, or with status 400 (Bad Request) if the institutionEntity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/institution-entities",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstitutionEntity> createInstitutionEntity(@Valid @RequestBody InstitutionEntity institutionEntity) throws URISyntaxException {
        log.debug("REST request to save InstitutionEntity : {}", institutionEntity);
        if (institutionEntity.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("institutionEntity", "idexists", "A new institutionEntity cannot already have an ID")).body(null);
        }
        InstitutionEntity result = institutionEntityService.save(institutionEntity);
        return ResponseEntity.created(new URI("/api/institution-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("institutionEntity", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /institution-entities : Updates an existing institutionEntity.
     *
     * @param institutionEntity the institutionEntity to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated institutionEntity,
     * or with status 400 (Bad Request) if the institutionEntity is not valid,
     * or with status 500 (Internal Server Error) if the institutionEntity couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/institution-entities",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstitutionEntity> updateInstitutionEntity(@Valid @RequestBody InstitutionEntity institutionEntity) throws URISyntaxException {
        log.debug("REST request to update InstitutionEntity : {}", institutionEntity);
        if (institutionEntity.getId() == null) {
            return createInstitutionEntity(institutionEntity);
        }
        InstitutionEntity result = institutionEntityService.save(institutionEntity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("institutionEntity", institutionEntity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /institution-entities : get all the institutionEntities.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of institutionEntities in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/institution-entities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InstitutionEntity>> getAllInstitutionEntities(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of InstitutionEntities");
        Page<InstitutionEntity> page = institutionEntityService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/institution-entities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /institution-entities/:id : get the "id" institutionEntity.
     *
     * @param id the id of the institutionEntity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the institutionEntity, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/institution-entities/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InstitutionEntity> getInstitutionEntity(@PathVariable Long id) {
        log.debug("REST request to get InstitutionEntity : {}", id);
        InstitutionEntity institutionEntity = institutionEntityService.findOne(id);
        return Optional.ofNullable(institutionEntity)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /institution-entities/:id : delete the "id" institutionEntity.
     *
     * @param id the id of the institutionEntity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/institution-entities/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInstitutionEntity(@PathVariable Long id) {
        log.debug("REST request to delete InstitutionEntity : {}", id);
        institutionEntityService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("institutionEntity", id.toString())).build();
    }

    /**
     * SEARCH  /_search/institution-entities?query=:query : search for the institutionEntity corresponding
     * to the query.
     *
     * @param query the query of the institutionEntity search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/_search/institution-entities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<InstitutionEntity>> searchInstitutionEntities(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of InstitutionEntities for query {}", query);
        Page<InstitutionEntity> page = institutionEntityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/institution-entities");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
