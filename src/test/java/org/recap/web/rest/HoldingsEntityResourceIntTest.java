package org.recap.web.rest;

import org.recap.ScsbhipsterApp;

import org.recap.domain.HoldingsEntity;
import org.recap.repository.HoldingsEntityRepository;
import org.recap.service.HoldingsEntityService;
import org.recap.repository.search.HoldingsEntitySearchRepository;

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
 * Test class for the HoldingsEntityResource REST controller.
 *
 * @see HoldingsEntityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScsbhipsterApp.class)
public class HoldingsEntityResourceIntTest {

    private static final Integer DEFAULT_HOLDINGS_ID = 1;
    private static final Integer UPDATED_HOLDINGS_ID = 2;

    private static final String DEFAULT_CONTENT = "";
    private static final String UPDATED_CONTENT = "";

    private static final LocalDate DEFAULT_CREATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_LAST_UPDATED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LAST_UPDATED_BY = "AAAAA";
    private static final String UPDATED_LAST_UPDATED_BY = "BBBBB";

    private static final Integer DEFAULT_OWNING_INSTITUTION_ID = 1;
    private static final Integer UPDATED_OWNING_INSTITUTION_ID = 2;

    private static final String DEFAULT_OWNING_INSTITUTION_HOLDINGS_ID = "AAAAA";
    private static final String UPDATED_OWNING_INSTITUTION_HOLDINGS_ID = "BBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String DEFAULT_CREATED_BY = "AAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBB";

    @Inject
    private HoldingsEntityRepository holdingsEntityRepository;

    @Inject
    private HoldingsEntityService holdingsEntityService;

    @Inject
    private HoldingsEntitySearchRepository holdingsEntitySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restHoldingsEntityMockMvc;

    private HoldingsEntity holdingsEntity;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HoldingsEntityResource holdingsEntityResource = new HoldingsEntityResource();
        ReflectionTestUtils.setField(holdingsEntityResource, "holdingsEntityService", holdingsEntityService);
        this.restHoldingsEntityMockMvc = MockMvcBuilders.standaloneSetup(holdingsEntityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HoldingsEntity createEntity(EntityManager em) {
        HoldingsEntity holdingsEntity = new HoldingsEntity()
                .holdingsId(DEFAULT_HOLDINGS_ID)
                .content(DEFAULT_CONTENT)
                .createdDate(DEFAULT_CREATED_DATE)
                .lastUpdatedDate(DEFAULT_LAST_UPDATED_DATE)
                .lastUpdatedBy(DEFAULT_LAST_UPDATED_BY)
                .owningInstitutionId(DEFAULT_OWNING_INSTITUTION_ID)
                .owningInstitutionHoldingsId(DEFAULT_OWNING_INSTITUTION_HOLDINGS_ID)
                .isDeleted(DEFAULT_IS_DELETED)
                .createdBy(DEFAULT_CREATED_BY);
        return holdingsEntity;
    }

    @Before
    public void initTest() {
        holdingsEntitySearchRepository.deleteAll();
        holdingsEntity = createEntity(em);
    }

    @Test
    @Transactional
    public void createHoldingsEntity() throws Exception {
        int databaseSizeBeforeCreate = holdingsEntityRepository.findAll().size();

        // Create the HoldingsEntity

        restHoldingsEntityMockMvc.perform(post("/api/holdings-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(holdingsEntity)))
                .andExpect(status().isCreated());

        // Validate the HoldingsEntity in the database
        List<HoldingsEntity> holdingsEntities = holdingsEntityRepository.findAll();
        assertThat(holdingsEntities).hasSize(databaseSizeBeforeCreate + 1);
        HoldingsEntity testHoldingsEntity = holdingsEntities.get(holdingsEntities.size() - 1);
        assertThat(testHoldingsEntity.getHoldingsId()).isEqualTo(DEFAULT_HOLDINGS_ID);
        assertThat(testHoldingsEntity.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testHoldingsEntity.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testHoldingsEntity.getLastUpdatedDate()).isEqualTo(DEFAULT_LAST_UPDATED_DATE);
        assertThat(testHoldingsEntity.getLastUpdatedBy()).isEqualTo(DEFAULT_LAST_UPDATED_BY);
        assertThat(testHoldingsEntity.getOwningInstitutionId()).isEqualTo(DEFAULT_OWNING_INSTITUTION_ID);
        assertThat(testHoldingsEntity.getOwningInstitutionHoldingsId()).isEqualTo(DEFAULT_OWNING_INSTITUTION_HOLDINGS_ID);
        assertThat(testHoldingsEntity.isIsDeleted()).isEqualTo(DEFAULT_IS_DELETED);
        assertThat(testHoldingsEntity.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);

        // Validate the HoldingsEntity in ElasticSearch
        HoldingsEntity holdingsEntityEs = holdingsEntitySearchRepository.findOne(testHoldingsEntity.getId());
        assertThat(holdingsEntityEs).isEqualToComparingFieldByField(testHoldingsEntity);
    }

    @Test
    @Transactional
    public void checkHoldingsIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = holdingsEntityRepository.findAll().size();
        // set the field null
        holdingsEntity.setHoldingsId(null);

        // Create the HoldingsEntity, which fails.

        restHoldingsEntityMockMvc.perform(post("/api/holdings-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(holdingsEntity)))
                .andExpect(status().isBadRequest());

        List<HoldingsEntity> holdingsEntities = holdingsEntityRepository.findAll();
        assertThat(holdingsEntities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOwningInstitutionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = holdingsEntityRepository.findAll().size();
        // set the field null
        holdingsEntity.setOwningInstitutionId(null);

        // Create the HoldingsEntity, which fails.

        restHoldingsEntityMockMvc.perform(post("/api/holdings-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(holdingsEntity)))
                .andExpect(status().isBadRequest());

        List<HoldingsEntity> holdingsEntities = holdingsEntityRepository.findAll();
        assertThat(holdingsEntities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOwningInstitutionHoldingsIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = holdingsEntityRepository.findAll().size();
        // set the field null
        holdingsEntity.setOwningInstitutionHoldingsId(null);

        // Create the HoldingsEntity, which fails.

        restHoldingsEntityMockMvc.perform(post("/api/holdings-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(holdingsEntity)))
                .andExpect(status().isBadRequest());

        List<HoldingsEntity> holdingsEntities = holdingsEntityRepository.findAll();
        assertThat(holdingsEntities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHoldingsEntities() throws Exception {
        // Initialize the database
        holdingsEntityRepository.saveAndFlush(holdingsEntity);

        // Get all the holdingsEntities
        restHoldingsEntityMockMvc.perform(get("/api/holdings-entities?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(holdingsEntity.getId().intValue())))
                .andExpect(jsonPath("$.[*].holdingsId").value(hasItem(DEFAULT_HOLDINGS_ID)))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
                .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(DEFAULT_LAST_UPDATED_DATE.toString())))
                .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.toString())))
                .andExpect(jsonPath("$.[*].owningInstitutionId").value(hasItem(DEFAULT_OWNING_INSTITUTION_ID)))
                .andExpect(jsonPath("$.[*].owningInstitutionHoldingsId").value(hasItem(DEFAULT_OWNING_INSTITUTION_HOLDINGS_ID.toString())))
                .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
                .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())));
    }

    @Test
    @Transactional
    public void getHoldingsEntity() throws Exception {
        // Initialize the database
        holdingsEntityRepository.saveAndFlush(holdingsEntity);

        // Get the holdingsEntity
        restHoldingsEntityMockMvc.perform(get("/api/holdings-entities/{id}", holdingsEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(holdingsEntity.getId().intValue()))
            .andExpect(jsonPath("$.holdingsId").value(DEFAULT_HOLDINGS_ID))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdatedDate").value(DEFAULT_LAST_UPDATED_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdatedBy").value(DEFAULT_LAST_UPDATED_BY.toString()))
            .andExpect(jsonPath("$.owningInstitutionId").value(DEFAULT_OWNING_INSTITUTION_ID))
            .andExpect(jsonPath("$.owningInstitutionHoldingsId").value(DEFAULT_OWNING_INSTITUTION_HOLDINGS_ID.toString()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHoldingsEntity() throws Exception {
        // Get the holdingsEntity
        restHoldingsEntityMockMvc.perform(get("/api/holdings-entities/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHoldingsEntity() throws Exception {
        // Initialize the database
        holdingsEntityService.save(holdingsEntity);

        int databaseSizeBeforeUpdate = holdingsEntityRepository.findAll().size();

        // Update the holdingsEntity
        HoldingsEntity updatedHoldingsEntity = holdingsEntityRepository.findOne(holdingsEntity.getId());
        updatedHoldingsEntity
                .holdingsId(UPDATED_HOLDINGS_ID)
                .content(UPDATED_CONTENT)
                .createdDate(UPDATED_CREATED_DATE)
                .lastUpdatedDate(UPDATED_LAST_UPDATED_DATE)
                .lastUpdatedBy(UPDATED_LAST_UPDATED_BY)
                .owningInstitutionId(UPDATED_OWNING_INSTITUTION_ID)
                .owningInstitutionHoldingsId(UPDATED_OWNING_INSTITUTION_HOLDINGS_ID)
                .isDeleted(UPDATED_IS_DELETED)
                .createdBy(UPDATED_CREATED_BY);

        restHoldingsEntityMockMvc.perform(put("/api/holdings-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedHoldingsEntity)))
                .andExpect(status().isOk());

        // Validate the HoldingsEntity in the database
        List<HoldingsEntity> holdingsEntities = holdingsEntityRepository.findAll();
        assertThat(holdingsEntities).hasSize(databaseSizeBeforeUpdate);
        HoldingsEntity testHoldingsEntity = holdingsEntities.get(holdingsEntities.size() - 1);
        assertThat(testHoldingsEntity.getHoldingsId()).isEqualTo(UPDATED_HOLDINGS_ID);
        assertThat(testHoldingsEntity.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testHoldingsEntity.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testHoldingsEntity.getLastUpdatedDate()).isEqualTo(UPDATED_LAST_UPDATED_DATE);
        assertThat(testHoldingsEntity.getLastUpdatedBy()).isEqualTo(UPDATED_LAST_UPDATED_BY);
        assertThat(testHoldingsEntity.getOwningInstitutionId()).isEqualTo(UPDATED_OWNING_INSTITUTION_ID);
        assertThat(testHoldingsEntity.getOwningInstitutionHoldingsId()).isEqualTo(UPDATED_OWNING_INSTITUTION_HOLDINGS_ID);
        assertThat(testHoldingsEntity.isIsDeleted()).isEqualTo(UPDATED_IS_DELETED);
        assertThat(testHoldingsEntity.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);

        // Validate the HoldingsEntity in ElasticSearch
        HoldingsEntity holdingsEntityEs = holdingsEntitySearchRepository.findOne(testHoldingsEntity.getId());
        assertThat(holdingsEntityEs).isEqualToComparingFieldByField(testHoldingsEntity);
    }

    @Test
    @Transactional
    public void deleteHoldingsEntity() throws Exception {
        // Initialize the database
        holdingsEntityService.save(holdingsEntity);

        int databaseSizeBeforeDelete = holdingsEntityRepository.findAll().size();

        // Get the holdingsEntity
        restHoldingsEntityMockMvc.perform(delete("/api/holdings-entities/{id}", holdingsEntity.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean holdingsEntityExistsInEs = holdingsEntitySearchRepository.exists(holdingsEntity.getId());
        assertThat(holdingsEntityExistsInEs).isFalse();

        // Validate the database is empty
        List<HoldingsEntity> holdingsEntities = holdingsEntityRepository.findAll();
        assertThat(holdingsEntities).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchHoldingsEntity() throws Exception {
        // Initialize the database
        holdingsEntityService.save(holdingsEntity);

        // Search the holdingsEntity
        restHoldingsEntityMockMvc.perform(get("/api/_search/holdings-entities?query=id:" + holdingsEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(holdingsEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].holdingsId").value(hasItem(DEFAULT_HOLDINGS_ID)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdatedDate").value(hasItem(DEFAULT_LAST_UPDATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdatedBy").value(hasItem(DEFAULT_LAST_UPDATED_BY.toString())))
            .andExpect(jsonPath("$.[*].owningInstitutionId").value(hasItem(DEFAULT_OWNING_INSTITUTION_ID)))
            .andExpect(jsonPath("$.[*].owningInstitutionHoldingsId").value(hasItem(DEFAULT_OWNING_INSTITUTION_HOLDINGS_ID.toString())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())));
    }
}
