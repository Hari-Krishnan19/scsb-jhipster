package org.recap.web.rest;

import org.recap.ScsbhipsterApp;

import org.recap.domain.InstitutionEntity;
import org.recap.repository.InstitutionEntityRepository;
import org.recap.service.InstitutionEntityService;
import org.recap.repository.search.InstitutionEntitySearchRepository;

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

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the InstitutionEntityResource REST controller.
 *
 * @see InstitutionEntityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScsbhipsterApp.class)
public class InstitutionEntityResourceIntTest {

    private static final Integer DEFAULT_INSTITUTION_ID = 1;
    private static final Integer UPDATED_INSTITUTION_ID = 2;

    private static final String DEFAULT_INSTITUTION_CODE = "AAAAA";
    private static final String UPDATED_INSTITUTION_CODE = "BBBBB";

    private static final String DEFAULT_INSTITUTION_NAME = "AAAAA";
    private static final String UPDATED_INSTITUTION_NAME = "BBBBB";

    @Inject
    private InstitutionEntityRepository institutionEntityRepository;

    @Inject
    private InstitutionEntityService institutionEntityService;

    @Inject
    private InstitutionEntitySearchRepository institutionEntitySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restInstitutionEntityMockMvc;

    private InstitutionEntity institutionEntity;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InstitutionEntityResource institutionEntityResource = new InstitutionEntityResource();
        ReflectionTestUtils.setField(institutionEntityResource, "institutionEntityService", institutionEntityService);
        this.restInstitutionEntityMockMvc = MockMvcBuilders.standaloneSetup(institutionEntityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     * List<BibliographicEntity> bibliographicEntities = new ArrayList<>();
        for (BibRecord bibRecord : bibRecordList) {
            BibliographicEntity bibliographicEntity = new BibliographicEntity();
            InstitutionEntity institutionEntity = institutionEntityRepository.findByInstitutionCode(bibRecord.getBib().getOwningInstitutionId().getDescription());
            bibliographicEntity.setOwningInstitutionId(institutionEntity.getInstitutionId());
            Marshaller marshaller = jaxbContext.createMarshaller();
            OutputStream os = new ByteArrayOutputStream();
            marshaller.marshal(bibRecord.getBib().getContent().getCollection(), os);
            bibliographicEntity.setContent(os.toString());
            bibliographicEntities.add(bibliographicEntity);
        }
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InstitutionEntity createEntity(EntityManager em) {
        InstitutionEntity institutionEntity = new InstitutionEntity()
                .institutionId(DEFAULT_INSTITUTION_ID)
                .institutionCode(DEFAULT_INSTITUTION_CODE)
                .institutionName(DEFAULT_INSTITUTION_NAME);
        return institutionEntity;
    }

    @Before
    public void initTest() {
        institutionEntitySearchRepository.deleteAll();
        institutionEntity = createEntity(em);
    }

    @Test
    @Transactional
    public void createInstitutionEntity() throws Exception {
        int databaseSizeBeforeCreate = institutionEntityRepository.findAll().size();

        // Create the InstitutionEntity

        restInstitutionEntityMockMvc.perform(post("/api/institution-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(institutionEntity)))
                .andExpect(status().isCreated());

        // Validate the InstitutionEntity in the database
        List<InstitutionEntity> institutionEntities = institutionEntityRepository.findAll();
        assertThat(institutionEntities).hasSize(databaseSizeBeforeCreate + 1);
        InstitutionEntity testInstitutionEntity = institutionEntities.get(institutionEntities.size() - 1);
        assertThat(testInstitutionEntity.getInstitutionId()).isEqualTo(DEFAULT_INSTITUTION_ID);
        assertThat(testInstitutionEntity.getInstitutionCode()).isEqualTo(DEFAULT_INSTITUTION_CODE);
        assertThat(testInstitutionEntity.getInstitutionName()).isEqualTo(DEFAULT_INSTITUTION_NAME);

        // Validate the InstitutionEntity in ElasticSearch
        InstitutionEntity institutionEntityEs = institutionEntitySearchRepository.findOne(testInstitutionEntity.getId());
        assertThat(institutionEntityEs).isEqualToComparingFieldByField(testInstitutionEntity);
    }

    @Test
    @Transactional
    public void checkInstitutionIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = institutionEntityRepository.findAll().size();
        // set the field null
        institutionEntity.setInstitutionId(null);

        // Create the InstitutionEntity, which fails.

        restInstitutionEntityMockMvc.perform(post("/api/institution-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(institutionEntity)))
                .andExpect(status().isBadRequest());

        List<InstitutionEntity> institutionEntities = institutionEntityRepository.findAll();
        assertThat(institutionEntities).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInstitutionEntities() throws Exception {
        // Initialize the database
        institutionEntityRepository.saveAndFlush(institutionEntity);

        // Get all the institutionEntities
        restInstitutionEntityMockMvc.perform(get("/api/institution-entities?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(institutionEntity.getId().intValue())))
                .andExpect(jsonPath("$.[*].institutionId").value(hasItem(DEFAULT_INSTITUTION_ID)))
                .andExpect(jsonPath("$.[*].institutionCode").value(hasItem(DEFAULT_INSTITUTION_CODE.toString())))
                .andExpect(jsonPath("$.[*].institutionName").value(hasItem(DEFAULT_INSTITUTION_NAME.toString())));
    }

    @Test
    @Transactional
    public void getInstitutionEntity() throws Exception {
        // Initialize the database
        institutionEntityRepository.saveAndFlush(institutionEntity);

        // Get the institutionEntity
        restInstitutionEntityMockMvc.perform(get("/api/institution-entities/{id}", institutionEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(institutionEntity.getId().intValue()))
            .andExpect(jsonPath("$.institutionId").value(DEFAULT_INSTITUTION_ID))
            .andExpect(jsonPath("$.institutionCode").value(DEFAULT_INSTITUTION_CODE.toString()))
            .andExpect(jsonPath("$.institutionName").value(DEFAULT_INSTITUTION_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingInstitutionEntity() throws Exception {
        // Get the institutionEntity
        restInstitutionEntityMockMvc.perform(get("/api/institution-entities/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInstitutionEntity() throws Exception {
        // Initialize the database
        institutionEntityService.save(institutionEntity);

        int databaseSizeBeforeUpdate = institutionEntityRepository.findAll().size();

        // Update the institutionEntity
        InstitutionEntity updatedInstitutionEntity = institutionEntityRepository.findOne(institutionEntity.getId());
        updatedInstitutionEntity
                .institutionId(UPDATED_INSTITUTION_ID)
                .institutionCode(UPDATED_INSTITUTION_CODE)
                .institutionName(UPDATED_INSTITUTION_NAME);

        restInstitutionEntityMockMvc.perform(put("/api/institution-entities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInstitutionEntity)))
                .andExpect(status().isOk());

        // Validate the InstitutionEntity in the database
        List<InstitutionEntity> institutionEntities = institutionEntityRepository.findAll();
        assertThat(institutionEntities).hasSize(databaseSizeBeforeUpdate);
        InstitutionEntity testInstitutionEntity = institutionEntities.get(institutionEntities.size() - 1);
        assertThat(testInstitutionEntity.getInstitutionId()).isEqualTo(UPDATED_INSTITUTION_ID);
        assertThat(testInstitutionEntity.getInstitutionCode()).isEqualTo(UPDATED_INSTITUTION_CODE);
        assertThat(testInstitutionEntity.getInstitutionName()).isEqualTo(UPDATED_INSTITUTION_NAME);

        // Validate the InstitutionEntity in ElasticSearch
        InstitutionEntity institutionEntityEs = institutionEntitySearchRepository.findOne(testInstitutionEntity.getId());
        assertThat(institutionEntityEs).isEqualToComparingFieldByField(testInstitutionEntity);
    }

    @Test
    @Transactional
    public void deleteInstitutionEntity() throws Exception {
        // Initialize the database
        institutionEntityService.save(institutionEntity);

        int databaseSizeBeforeDelete = institutionEntityRepository.findAll().size();

        // Get the institutionEntity
        restInstitutionEntityMockMvc.perform(delete("/api/institution-entities/{id}", institutionEntity.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean institutionEntityExistsInEs = institutionEntitySearchRepository.exists(institutionEntity.getId());
        assertThat(institutionEntityExistsInEs).isFalse();

        // Validate the database is empty
        List<InstitutionEntity> institutionEntities = institutionEntityRepository.findAll();
        assertThat(institutionEntities).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchInstitutionEntity() throws Exception {
        // Initialize the database
        institutionEntityService.save(institutionEntity);

        // Search the institutionEntity
        restInstitutionEntityMockMvc.perform(get("/api/_search/institution-entities?query=id:" + institutionEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(institutionEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].institutionId").value(hasItem(DEFAULT_INSTITUTION_ID)))
            .andExpect(jsonPath("$.[*].institutionCode").value(hasItem(DEFAULT_INSTITUTION_CODE.toString())))
            .andExpect(jsonPath("$.[*].institutionName").value(hasItem(DEFAULT_INSTITUTION_NAME.toString())));
    }
}
