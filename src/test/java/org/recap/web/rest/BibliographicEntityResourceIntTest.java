package org.recap.web.rest;

import org.recap.ScsbhipsterApp;

import org.recap.domain.BibliographicEntity;
import org.recap.repository.BibliographicEntityRepository;
import org.recap.service.BibliographicEntityService;
import org.recap.repository.search.BibliographicEntitySearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BibliographicEntityResource REST controller.
 *
 * @see BibliographicEntityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScsbhipsterApp.class)
public class BibliographicEntityResourceIntTest {

    private static final Integer DEFAULT_BIBLIOGRAPHIC_ID = 1;
    private static final Integer UPDATED_BIBLIOGRAPHIC_ID = 2;

    private static final String DEFAULT_CONTENT = "";
    private static final String UPDATED_CONTENT = "";

    private static final Integer DEFAULT_OWNING_INSTITUTION_ID = 1;
    private static final Integer UPDATED_OWNING_INSTITUTION_ID = 2;

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_CREATED_BY = "AAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBB";

    private static final LocalDate DEFAULT_LAST_UPDATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LAST_UPDATED_BY = "AAAAA";
    private static final String UPDATED_LAST_UPDATED_BY = "BBBBB";

    private static final String DEFAULT_OWNING_INSTITUTION_BIB_ID = "AAAAA";
    private static final String UPDATED_OWNING_INSTITUTION_BIB_ID = "BBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    @Inject
    private BibliographicEntityRepository bibliographicEntityRepository;

    @Inject
    private BibliographicEntityService bibliographicEntityService;

    @Inject
    private BibliographicEntitySearchRepository bibliographicEntitySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restBibliographicEntityMockMvc;

    private BibliographicEntity bibliographicEntity;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BibliographicEntityResource bibliographicEntityResource = new BibliographicEntityResource();
        ReflectionTestUtils.setField(bibliographicEntityResource, "bibliographicEntityService", bibliographicEntityService);
        this.restBibliographicEntityMockMvc = MockMvcBuilders.standaloneSetup(bibliographicEntityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BibliographicEntity createEntity(EntityManager em) {
        BibliographicEntity bibliographicEntity = new BibliographicEntity()
                .bibliographicId(DEFAULT_BIBLIOGRAPHIC_ID)
                .content(DEFAULT_CONTENT)
                .owningInstitutionId(DEFAULT_OWNING_INSTITUTION_ID)
                .createdDate(DEFAULT_CREATED_DATE)
                .createdBy(DEFAULT_CREATED_BY)
                .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE)
                .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
                .owningInstitutionBibId(DEFAULT_OWNING_INSTITUTION_BIB_ID)
                .isDeleted(DEFAULT_IS_DELETED);
        return bibliographicEntity;
    }

    @Before
    public void initTest() {
        bibliographicEntitySearchRepository.deleteAll();
        bibliographicEntity = createEntity(em);
    }

    @Test
    @Transactional
    public void createBibliographicEntity() throws Exception {
        int databaseSizeBeforeCreate = bibliographicEntityRepository.findAll().size();

        // Create the BibliographicEntity

        restBibliographicEntityMockMvc.perform(post("/api/bibliographic-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bibliographicEntity)))
                .andExpect(status().isCreated());

        // Validate the BibliographicEntity in the database
        List<BibliographicEntity> bibliographicEntities = bibliographicEntityRepository.findAll();
        assertThat(bibliographicEntities).hasSize(databaseSizeBeforeCreate + 1);
        BibliographicEntity testBibliographicEntity = bibliographicEntities.get(bibliographicEntities.size() - 1);
        assertThat(testBibliographicEntity.getBibliographicId()).isEqualTo(DEFAULT_BIBLIOGRAPHIC_ID);
        assertThat(testBibliographicEntity.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testBibliographicEntity.getOwningInstitutionId()).isEqualTo(DEFAULT_OWNING_INSTITUTION_ID);
        assertThat(testBibliographicEntity.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testBibliographicEntity.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testBibliographicEntity.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
        assertThat(testBibliographicEntity.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testBibliographicEntity.getOwningInstitutionBibId()).isEqualTo(DEFAULT_OWNING_INSTITUTION_BIB_ID);
        assertThat(testBibliographicEntity.isIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);

        // Validate the BibliographicEntity in ElasticSearch
        BibliographicEntity bibliographicEntityEs = bibliographicEntitySearchRepository.findOne(testBibliographicEntity.getId());
        assertThat(bibliographicEntityEs).isEqualToComparingFieldByField(testBibliographicEntity);
    }

    @Test
    @Transactional
    public void checkBibliographicIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = bibliographicEntityRepository.findAll().size();
        // set the field null
        bibliographicEntity.setBibliographicId(null);

        // Create the BibliographicEntity, which fails.

        restBibliographicEntityMockMvc.perform(post("/api/bibliographic-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bibliographicEntity)))
                .andExpect(status().isBadRequest());

        List<BibliographicEntity> bibliographicEntities = bibliographicEntityRepository.findAll();
        assertThat(bibliographicEntities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOwningInstitutionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = bibliographicEntityRepository.findAll().size();
        // set the field null
        bibliographicEntity.setOwningInstitutionId(null);

        // Create the BibliographicEntity, which fails.

        restBibliographicEntityMockMvc.perform(post("/api/bibliographic-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bibliographicEntity)))
                .andExpect(status().isBadRequest());

        List<BibliographicEntity> bibliographicEntities = bibliographicEntityRepository.findAll();
        assertThat(bibliographicEntities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOwningInstitutionBibIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = bibliographicEntityRepository.findAll().size();
        // set the field null
        bibliographicEntity.setOwningInstitutionBibId(null);

        // Create the BibliographicEntity, which fails.

        restBibliographicEntityMockMvc.perform(post("/api/bibliographic-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bibliographicEntity)))
                .andExpect(status().isBadRequest());

        List<BibliographicEntity> bibliographicEntities = bibliographicEntityRepository.findAll();
        assertThat(bibliographicEntities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBibliographicEntities() throws Exception {
        // Initialize the database
        bibliographicEntityRepository.saveAndFlush(bibliographicEntity);

        // Get all the bibliographicEntities
        restBibliographicEntityMockMvc.perform(get("/api/bibliographic-entities?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bibliographicEntity.getId().intValue())))
                .andExpect(jsonPath("$.[*].bibliographicId").value(hasItem(DEFAULT_BIBLIOGRAPHIC_ID)))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].owningInstitutionId").value(hasItem(DEFAULT_OWNING_INSTITUTION_ID)))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
                .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
                .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(DEFAULT_LAST_UPDATED_DATE.toString())))
                .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.toString())))
                .andExpect(jsonPath("$.[*].owningInstitutionBibId").value(hasItem(DEFAULT_OWNING_INSTITUTION_BIB_ID.toString())))
                .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    public void getBibliographicEntity() throws Exception {
        // Initialize the database
        bibliographicEntityRepository.saveAndFlush(bibliographicEntity);

        // Get the bibliographicEntity
        restBibliographicEntityMockMvc.perform(get("/api/bibliographic-entities/{id}", bibliographicEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bibliographicEntity.getId().intValue()))
            .andExpect(jsonPath("$.bibliographicId").value(DEFAULT_BIBLIOGRAPHIC_ID))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.owningInstitutionId").value(DEFAULT_OWNING_INSTITUTION_ID))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(DEFAULT_LAST_UPDATED_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.toString()))
            .andExpect(jsonPath("$.owningInstitutionBibId").value(DEFAULT_OWNING_INSTITUTION_BIB_ID.toString()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBibliographicEntity() throws Exception {
        // Get the bibliographicEntity
        restBibliographicEntityMockMvc.perform(get("/api/bibliographic-entities/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBibliographicEntity() throws Exception {
        // Initialize the database
        bibliographicEntityService.save(bibliographicEntity);

        int databaseSizeBeforeUpdate = bibliographicEntityRepository.findAll().size();

        // Update the bibliographicEntity
        BibliographicEntity updatedBibliographicEntity = bibliographicEntityRepository.findOne(bibliographicEntity.getId());
        updatedBibliographicEntity
                .bibliographicId(UPDATED_BIBLIOGRAPHIC_ID)
                .content(UPDATED_CONTENT)
                .owningInstitutionId(UPDATED_OWNING_INSTITUTION_ID)
                .createdDate(UPDATED_CREATED_DATE)
                .createdBy(UPDATED_CREATED_BY)
                .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE)
                .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
                .owningInstitutionBibId(UPDATED_OWNING_INSTITUTION_BIB_ID)
                .isDeleted(UPDATED_IS_DELETED);

        restBibliographicEntityMockMvc.perform(put("/api/bibliographic-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBibliographicEntity)))
                .andExpect(status().isOk());

        // Validate the BibliographicEntity in the database
        List<BibliographicEntity> bibliographicEntities = bibliographicEntityRepository.findAll();
        assertThat(bibliographicEntities).hasSize(databaseSizeBeforeUpdate);
        BibliographicEntity testBibliographicEntity = bibliographicEntities.get(bibliographicEntities.size() - 1);
        assertThat(testBibliographicEntity.getBibliographicId()).isEqualTo(UPDATED_BIBLIOGRAPHIC_ID);
        assertThat(testBibliographicEntity.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testBibliographicEntity.getOwningInstitutionId()).isEqualTo(UPDATED_OWNING_INSTITUTION_ID);
        assertThat(testBibliographicEntity.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testBibliographicEntity.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testBibliographicEntity.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
        assertThat(testBibliographicEntity.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testBibliographicEntity.getOwningInstitutionBibId()).isEqualTo(UPDATED_OWNING_INSTITUTION_BIB_ID);
        assertThat(testBibliographicEntity.isIsDeleted()).isEqualTo(UPDATED_IS_DELETED);

        // Validate the BibliographicEntity in ElasticSearch
        BibliographicEntity bibliographicEntityEs = bibliographicEntitySearchRepository.findOne(testBibliographicEntity.getId());
        assertThat(bibliographicEntityEs).isEqualToComparingFieldByField(testBibliographicEntity);
    }

    @Test
    @Transactional
    public void deleteBibliographicEntity() throws Exception {
        // Initialize the database
        bibliographicEntityService.save(bibliographicEntity);

        int databaseSizeBeforeDelete = bibliographicEntityRepository.findAll().size();

        // Get the bibliographicEntity
        restBibliographicEntityMockMvc.perform(delete("/api/bibliographic-entities/{id}", bibliographicEntity.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean bibliographicEntityExistsInEs = bibliographicEntitySearchRepository.exists(bibliographicEntity.getId());
        assertThat(bibliographicEntityExistsInEs).isFalse();

        // Validate the database is empty
        List<BibliographicEntity> bibliographicEntities = bibliographicEntityRepository.findAll();
        assertThat(bibliographicEntities).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBibliographicEntity() throws Exception {
        // Initialize the database
        bibliographicEntityService.save(bibliographicEntity);

        // Search the bibliographicEntity
        restBibliographicEntityMockMvc.perform(get("/api/_search/bibliographic-entities?query=id:" + bibliographicEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bibliographicEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].bibliographicId").value(hasItem(DEFAULT_BIBLIOGRAPHIC_ID)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].owningInstitutionId").value(hasItem(DEFAULT_OWNING_INSTITUTION_ID)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(DEFAULT_LAST_UPDATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.toString())))
            .andExpect(jsonPath("$.[*].owningInstitutionBibId").value(hasItem(DEFAULT_OWNING_INSTITUTION_BIB_ID.toString())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }
}
